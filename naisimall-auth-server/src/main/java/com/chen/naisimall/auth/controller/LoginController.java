package com.chen.naisimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.chen.common.constant.AuthServerConstant;
import com.chen.common.exception.BizCodeEnume;
import com.chen.common.utils.R;
import com.chen.common.vo.MemberRespVo;
import com.chen.naisimall.auth.feign.MemberFeignService;
import com.chen.naisimall.auth.feign.ThirdPartFeignService;
import com.chen.naisimall.auth.vo.UserLoginVo;
import com.chen.naisimall.auth.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    ThirdPartFeignService thirdPartFeignService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    MemberFeignService memberFeignService;

//    @GetMapping("/login.html")
//    public String loginPage(){
//        return "login";
//    }
//
//    @GetMapping("/reg.html")
//    public String regPage(){
//        return "reg";
//    }
    /**
     * 发送一个请求直接跳转到一个页面
     * SpringMVC viewcontroller:将请求和页面映射过来
     */
    @GetMapping("/sms/sendcode")
    @ResponseBody
    public R sendCode(@RequestParam("phone") String phone) throws Exception {
        //1,接口防刷 TODO
        String redisCode = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PERFIX + phone);
        if (!StringUtils.isEmpty(redisCode)){
            long l = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() -l < 60000){
                //60秒内不能再发
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(),BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        //2,验证码的再次校验 redis,存key-phone,value-code    sms:code:13292122993 -> 88888
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        String substring = code +"_"+System.currentTimeMillis();

        //redis缓存验证码,防止同一个手机号,在60秒内再次发送验证码
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PERFIX + phone,substring,10, TimeUnit.MINUTES);

        thirdPartFeignService.sendCode(phone,code);
        return R.ok();
    }

    /**
     * TODO 重定向携带数据,利用session原理,将数据放在session中,
     * 只要跳到下一个页面取出这个数据以后,session里面的数据就会删掉
     * TODO 1,分布式下的session问题
     * @param vo
     * @param result
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/regist")
    public String regist(@Valid UserRegistVo vo, BindingResult result,
                         RedirectAttributes redirectAttributes){
        if (result.hasErrors()){
            /**
             * .map(fieldError -> {
             *                 String field = fieldError.getField();
             *                 String defaultMessage = fieldError.getDefaultMessage();
             *                 errors.put(field,defaultMessage);
             *             })
             */
//            Map<String,String> errors = new HashMap<>();
//            for (FieldError fieldError : result.getFieldErrors()) {
//                String field = fieldError.getField();
//                String defaultMessage = fieldError.getDefaultMessage();
//                errors.put(field,defaultMessage);
//            }
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
            redirectAttributes.addFlashAttribute("errors",errors);
            //model.addAttribute("errors",errors);

            //Request method 'POST' not supported
            //用户注册->regist[post]---->转发/reg.html(路径映射默认都是get方式访问的)

            //校验出错,转发到注册页
            //return "redirect:/reg.html";
            return "redirect:http://auth.naisimall.com/reg.html";
        }
        //真正注册,调用用远程服务进行注册
        //1,校验验证码
        String code = vo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PERFIX + vo.getPhone());
        if (!StringUtils.isEmpty(s)){
            if (code.equals(s.split("_")[0])){
                //删除验证码 令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PERFIX + vo.getPhone());
                //验证码通过,    //真正注册,调用远程服务进行注册
                R r = memberFeignService.regist(vo);
                if (r.getCode() == 0){
                    //成功
                    return "redirect:http://auth.naisimall.com/login.html";
                }else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg",r.getData("msg",new TypeReference<String>(){}));
                    redirectAttributes.addFlashAttribute("errors",errors);
                    return "redirect:http://auth.naisimall.com/reg.html";
                }

            }else {
                Map<String, String> errors = new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                return "redirect:http://auth.naisimall.com/reg.html";
            }
        }else {
            Map<String, String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错,转发到注册页
            return "redirect:http://auth.naisimall.com/reg.html";
        }


        //注册成功回到首页,回到登录页
        //return "redirect:/login.html";
    }

    @GetMapping("/login.html")
    public String loginPage(HttpSession session){
        Object attribute = session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute == null){
            return "login";
        }else {
            return "redirect:http://naisimall.com";
        }
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo,RedirectAttributes redirectAttributes,
                        HttpSession session){
        //远程登录
        R login = memberFeignService.login(vo);
        if (login.getCode() == 0){

            MemberRespVo data = login.getData("data", new TypeReference<MemberRespVo>() {
            });
            //成功放到Session中
            session.setAttribute(AuthServerConstant.LOGIN_USER,data);
            return "redirect:http://naisimall.com";
        }else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.naisimall.com/login.html";
        }
    }
}
