package cn.tedu.straw.sys.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/sys")
public class DemoController {
    @GetMapping("/demo")
    public String demo(){
        return "Hello sys!!!";
    }
    //   localhost:9000/sys/v1/sys/testSession
    @GetMapping("/testSession")
    public String session(
            @AuthenticationPrincipal User user){
        System.out.println(user.getUsername());
        return user.getUsername();
    }



}
