package com.xiaomaigou.user.service;

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
 * 这个类的主要作用是在登录后得到用户名，可以根据用户名查询角色或执行一些逻辑。
 *
 * @author root
 */
public class UserDetailServiceImpl implements UserDetailsService {

    /**
     * @param username //用户名，cas认证后会传入这个方法
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("经过了UserDetailsServiceImpl，username：" + username);

        // 构建一个角色列表
        List<GrantedAuthority> grantAuths = new ArrayList<>();
        // 添加了一个名称为ROLE_USER角色（ROLE_USER需要与配置文件中的角色名一致）
        grantAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

        /*
         * Spring Security原始的认证的流程是：
         * 此处返回一个用户的正确信息，包括用户名、密码、角色等，如果用户在前端输入的密码与此处返回的密码相同（注意：不会去判断用户名，只判断输入的密码是否与返回的密码相同），则Spring Security框架自动将此用户认证为通过，并授权它访问该角色下的所有资源
         *
         * 但是，在本项目中该类并不会去真正认证，而是由单点登录认证，该类的主要作用就是根据用户名获取其角色的作用，并授权它访问该角色下的所有资源
         */
        return new User(username,"",grantAuths);
    }
}
