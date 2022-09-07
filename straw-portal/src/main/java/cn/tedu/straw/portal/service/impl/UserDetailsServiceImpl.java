package cn.tedu.straw.portal.service.impl;

import cn.tedu.straw.portal.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 这个类实现UserDetailsService接口
 * 为Spring-Security提供认证的数据
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //Spring-Security认证信息时
    //会将用户名传递到这个方法中
    //根据这个用户名获得数据库中加密的密码,
    //如果匹配则登录成功
    @Autowired
    IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userService.getUserDetails(username);
    }
}
