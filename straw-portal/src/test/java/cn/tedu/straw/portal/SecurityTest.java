package cn.tedu.straw.portal;

import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.model.Permission;
import cn.tedu.straw.portal.model.User;
import cn.tedu.straw.portal.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
public class SecurityTest {

    //@Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void encodeTest(){
        /*
            每次运行加密结果不同
            是因为加密对象采用了"随机加盐"技术,提高安全性
         */
        String pwd=passwordEncoder.encode("123456");
        System.out.println(pwd);
//$2a$10$IHMiKBqpiPFYgRg4P0E0HeU.xdkr1nw0/y1AWKIvHh5TMNwxVuBRW
    }
    @Test
    public void matchTest(){
        /*
        验证我们输入的密码是不是能匹配生成的密文
         */
        boolean b=passwordEncoder.matches("123456",
                "$2a$10$IHMiKBqpiPFYgRg4P0E0" +
                        "HeU.xdkr1nw0/y1AWKIvHh5TMNwxVuBRW");
        System.out.println(b);
    }

    @Autowired
    UserMapper userMapper;
    @Test
    public void findUser(){
        User user=userMapper.findUserByUsername("tc2");
        System.out.println(user);
    }
    @Test
    public void findPermissions(){
        List<Permission> list=userMapper.findUserPermissionsById(3);
        for(Permission p: list){
            System.out.println(p);
        }
    }


    @Autowired
    IUserService userService;
    @Test
    public void abcLogin(){
        UserDetails ud=userService.getUserDetails("tc2");
        System.out.println(ud);
    }










}
