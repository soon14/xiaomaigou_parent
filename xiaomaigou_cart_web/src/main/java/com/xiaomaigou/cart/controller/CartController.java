package com.xiaomaigou.cart.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xiaomaigou.cart.service.CartService;
import com.xiaomaigou.pojogroup.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;

import entity.Result;
import util.CookieUtil;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout=6000)
    private CartService cartService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/findCartList")
    public List<Cart> findCartList(){
        //当前登录用户账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户名："+username);

        String cartListString = CookieUtil.getCookieValue(request, "cartList", "UTF-8");
        if(cartListString==null || cartListString.equals("")){
            cartListString="[]";
        }
        List<Cart> cartList_cookie = JSON.parseArray(cartListString, Cart.class);

        if(username.equals("anonymousUser")){//如果未登录
            //从cookie中提取购物车
            System.out.println("从cookie中获取购物车");

            return cartList_cookie;

        }else{//如果已登录
            //获取redis购物车
            List<Cart> cartList_redis = cartService.findCartListFromRedis(username);
            if(cartList_cookie.size()>0){//判断当本地购物车中存在数据
                //得到合并后的购物车
                List<Cart> cartList = cartService.mergeCartList(cartList_cookie, cartList_redis);
                //将合并后的购物车存入redis
                cartService.saveCartListToRedis(username, cartList);
                //本地购物车清除
                CookieUtil.deleteCookie(request, response, "cartList");
                System.out.println("执行了合并购物车的逻辑");
                return cartList;
            }
            return cartList_redis;
        }

    }

    @RequestMapping("/addGoodsToCartList")
    @CrossOrigin(origins="http://192.168.199.190:9097",allowCredentials="true")
    public Result addGoodsToCartList(Long itemId,Integer num){

        //response.setHeader("Access-Control-Allow-Origin", "http://192.168.199.190:9097");//可以访问的域(当此方法不需要操作cookie)
        //response.setHeader("Access-Control-Allow-Origin", "*");//使用"*"将允许所有的域均可访问，但是如果为"*"，则不能再操作cookie，因为cookie本身是和域相关的一个东西
        //response.setHeader("Access-Control-Allow-Credentials", "true");//如果操作cookie，必须加上这句话，并且，需要在客户端请求时添加{'withCredentials':true}

        //当前登录人账号
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("当前登录用户名："+username);

        try {
            //提取购物车
            List<Cart> cartList = findCartList();
            //调用服务方法操作购物车
            cartList = cartService.addGoodsToCartList(cartList, itemId, num);

            if(username.equals("anonymousUser")){//如果未登录
                //将新的购物车存入cookie
                String cartListString = JSON.toJSONString(cartList);
                // 3600*24 为一天
                util.CookieUtil.setCookie(request, response, "cartList", cartListString, 3600*24, "UTF-8");
                System.out.println("向cookie存入购物车");

            }else{//如果登录
                cartService.saveCartListToRedis(username, cartList);
            }

            return new Result(true, "添加购物车成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加购物车失败");
        }

    }

}