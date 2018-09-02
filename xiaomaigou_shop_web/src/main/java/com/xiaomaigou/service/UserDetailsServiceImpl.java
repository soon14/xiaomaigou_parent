package com.xiaomaigou.service;

import com.xiaomaigou.pojo.TbSeller;
import com.xiaomaigou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * spring security认证类
 *
 * @author root
 */
public class UserDetailsServiceImpl implements UserDetailsService {


    //此处不使用注解的方式，而是使用配置的方式，如果采用配置的方式，就必须有一个set的方法
    private SellerService sellerService;
    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * @param username //用户在前端输入的用户名，会传入这个方法
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("经过了UserDetailsServiceImpl，username：" + username);

        //构建一个角色列表
        List<GrantedAuthority> grantAuths = new ArrayList<>();
        // 添加了一个名称为ROLE_SELLER角色（ROLE_SELLER需要与配置文件中的角色名一致）
        grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //通过商家用户名(id，主键，唯一)获取商家对象
        TbSeller seller = sellerService.findOne(username);
        if (seller != null) {
            //只有状态为"1"（审核通过）的用户才能登录
            if (seller.getStatus().equals("1")) {
                /*
                 * Spring Security认证的流程是：
                 * 此处返回一个用户的正确信息，包括用户名、密码、角色等，如果用户在前端输入的密码与此处返回的密码相同（注意：不会去判断用户名，只判断输入的密码是否与返回的密码相同），则Spring Security框架自动将此用户认证为通过，并授权它访问该角色下的所有资源
                 */
                System.out.println("认证返回信息：username:"+username+"Password:"+seller.getPassword()+"grantAuths:"+grantAuths);
                return new User(username, seller.getPassword(), grantAuths);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

}
