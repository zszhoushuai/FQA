package cn.tedu.straw.faq.service.impl;

import cn.tedu.straw.commons.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Component
@Slf4j
public class RibbonClient {
    @Resource
    RestTemplate restTemplate;
    public User getUser(String username){
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(
                url,User.class,username);
        return user;
    }
    public User[] masters(){
        String url="http://sys-service/v1/users/master";
        User[] users=restTemplate.getForObject(
                url,User[].class);
        return users;
    }





}
