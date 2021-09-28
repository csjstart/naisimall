package com.chen.naisimall.auth.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.chen.common.utils.HttpUtils;
import com.chen.common.utils.R;
import com.chen.naisimall.auth.config.OAuthConfigComponent;
import com.chen.naisimall.auth.feign.MemberFeignService;
import com.chen.common.vo.MemberRespVo;
import com.chen.naisimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 处理社交登录请求
 * @author woita
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    OAuthConfigComponent authConfigComponent;

    @Autowired
    MemberFeignService memberFeignService;

//    @GetMapping("/oauth2.0/gitee/success")
    @GetMapping("/oauth2.0/weibo/success")
    public String weibo(@RequestParam("code") String code, HttpSession session, HttpServletResponse servletResponse) throws Exception {

        System.out.println(code);
        HashMap<String, String> header = new HashMap<>();
        HashMap<String, String> query = new HashMap<>();

        HashMap<String, String> map = new HashMap<>();
        map.put("client_id","994058823");
        map.put("client_secret","3fff69adaa37cb1adb2303af7e4d1680");
        map.put("grant_type","authorization_code");
        map.put("redirect_uri","http://auth.naisimall.com/oauth2.0/weibo/success");
        map.put("code",code);
        //1,根据code换取Access_Token
        HttpResponse response = HttpUtils.doPost("https://api.weibo.com", "/oauth2/access_token", "post", header, query, map);

        //2,处理
        if (response.getStatusLine().getStatusCode() == 200){
            //获取到了access_token
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);

            //知道当前是那个社交用户
            //1),当前用户如果是第一次进网站,自动注册进来(为当前社交用户生成一个会员信息帐号,以后这个社交账号就对应指定的会员)
            R oauthlogin = memberFeignService.oauthlogin(socialUser);
            if (oauthlogin.getCode() == 0){
                //2,登陆成功
                MemberRespVo data = oauthlogin.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登陆成功:用户信息:{}",data.toString());
                //1,第一次使用session:命令浏览器保存卡号,JSESSION这个cookie
                //以后浏览器访问那个网站就会带上这个网站的cookie
                //子域之间:naisimall.com    order.naisimall.com    auth.naisimall.com
                //发卡的时候(指定域名为父域名),即使是子域系统发的卡,也能让父域直接使用
                //TODO 1,默认发的令牌,session=dadea   作用域:当前域:    解决子域session共享问题
                //TODO 2,使用JSON的序列化方式来序列化对象数据到redis中
                session.setAttribute("loginUser",data);
                //new Cookie("JSESSIONID","chensfae").setDomain();
                //servletResponse.addCookie();
                return "redirect:http://naisimall.com";
            }else {
                return "redirect:http://auth.naisimall.com/login.html";
            }
        }else {
            return "redirect:http://auth.naisimall.com/login.html";
        }
    }
}
