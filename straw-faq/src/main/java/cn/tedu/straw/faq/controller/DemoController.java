package cn.tedu.straw.faq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/v1/faq")
public class DemoController {
    @GetMapping("/demo")
    public String demo(
            @AuthenticationPrincipal UserDetails userDetails){
        return userDetails.getUsername();
    }
}
