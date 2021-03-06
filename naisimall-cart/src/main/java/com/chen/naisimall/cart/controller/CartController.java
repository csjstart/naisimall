package com.chen.naisimall.cart.controller;

import com.chen.common.constant.AuthServerConstant;
import com.chen.naisimall.cart.interceptor.CartInterceptor;
import com.chen.naisimall.cart.service.CartService;
import com.chen.naisimall.cart.vo.Cart;
import com.chen.naisimall.cart.vo.CartItem;
import com.chen.naisimall.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return "redirect:http://cart.naisimall.com/cart.html";
    }

    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num){
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.naisimall.com/cart.html";
    }



    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,
                            @RequestParam("check") Integer check){
        cartService.checkItem(skuId,check);
        return "redirect:http://cart.naisimall.com/cart.html";
    }
    /**
     * 浏览器有一个cookies:user-key:表示用户身份,一个月后过期
     * 如果第一次使用JD的购物车功能,都会给用户一个临时的用户身份
     * 浏览器以后保存,每次访问都会带上这个cookie
     *
     * 登录:session有
     * 没登录:按照cookis里面带来user-key来做
     * 第一次:如果没有临时用户,帮忙来创建一个临时用户
     * @return
     */
    @GetMapping("/cart.html")
    public String cartListPage(Model model) throws ExecutionException, InterruptedException {
        //1,快速得到用户信息,id,user-key
        //UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        //CartInterceptor.threadLocal.remove();
        Cart cart = cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     * RedirectAttributes ra
     *      ra.addFlashAttribute():将数据放在session里面可以在页面取出,但是只能取一次
     *      ra.addAttribute("skuId",skuId);将数据放在url后面
     * 添加商品到购物车
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes ra) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId,num);
        ra.addAttribute("skuId",skuId);

        //model.addAttribute("item",cartItem);
        return "redirect:http://cart.naisimall.com/addToCartSuccess.html";
    }


    /**
     * 跳转到成功页
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model){
        //重定向到成功页面,再次查询购物车数据即可
        CartItem cartItem = cartService.getCartItem(skuId);
        model.addAttribute("item",cartItem);
        return "success";
    }
}
