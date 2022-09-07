package cn.tedu.straw.gateway.service.impl;

import cn.tedu.straw.commons.model.Permission;
import cn.tedu.straw.commons.model.Role;
import cn.tedu.straw.commons.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class UserDetailServiceImpl
        implements UserDetailsService {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(
                url,User.class,username);
        if(user == null){
            throw new UsernameNotFoundException("用户名密码不正确");
        }
        //跨服务查询用户所有权限
        url="http://sys-service/v1/auth/permissions?userId={1}";
        Permission[] permissions=restTemplate.getForObject(
                url,Permission[].class,user.getId());
        //跨服务查询用户所有角色
        url="http://sys-service/v1/auth/roles?userId={1}";
        Role[] roles=restTemplate.getForObject(
                url,Role[].class,user.getId());
        if(permissions==null || roles==null){
            throw new UsernameNotFoundException("角色或权限缺失!");
        }
        //构建权限和角色的数组,最终赋值到Spring-Security中,用于认证
        String[] auths=new String[permissions.length+roles.length];
        int index=0;
        for(Permission p : permissions){
            auths[index]=p.getName();
            index++;
        }
        for(Role r:roles){
            auths[index]=r.getName();
            index++;
        }
        //构建一个UserDetail对象,返回给Spring-Security
        UserDetails u=
                org.springframework.security
                        .core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(user.getEnabled()==0)
                .accountLocked(user.getLocked()==1)
                .authorities(auths)
                .build();
        return u;
    }
}
