package cn.tedu.straw.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class HomeController {

    //声明两个常亮以便判断用户的角色
    static final GrantedAuthority STUDENT =
            new SimpleGrantedAuthority("ROLE_STUDENT");
    static final GrantedAuthority TEACHER =
            new SimpleGrantedAuthority("ROLE_TEACHER");

    @GetMapping("/register.html")
    public ModelAndView register(){
        return new ModelAndView("register");
    }

    //显示首页
    @GetMapping("/index.html")
    public ModelAndView index(
            @AuthenticationPrincipal User user){
        if(user.getAuthorities().contains(STUDENT)){
            return  new ModelAndView("index");
        }else if(user.getAuthorities().contains(TEACHER)){
            return new ModelAndView("index_teacher");
        }
        return null;
    }
    //访问学生提问页面的方法
    @GetMapping("/question/create.html")
    public ModelAndView create(){
        return new ModelAndView("question/create");
    }

    //显示首页
    @GetMapping("/question/detail.html")
    public ModelAndView detail(
            @AuthenticationPrincipal User user){
        if(user.getAuthorities().contains(STUDENT)){
            return  new ModelAndView(
                    "question/detail");
        }else if(user.getAuthorities().contains(TEACHER)){
            return new ModelAndView(
                    "question/detail_teacher");
        }
        return null;
    }

    @GetMapping("/search.html")
    public ModelAndView search(){
        return new ModelAndView("search");
    }



}





