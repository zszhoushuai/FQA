package cn.tedu.straw.portal.controller;

import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.MARSHAL;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//负责除了登录和注册页面的所有其他页面的显示
@RestController
@Slf4j
public class HomeController {

    //声明两个常亮以便判断用户的角色
    static final GrantedAuthority STUDENT =
            new SimpleGrantedAuthority("ROLE_STUDENT");
    static final GrantedAuthority TEACHER =
            new SimpleGrantedAuthority("ROLE_TEACHER");

    //显示首页
    @GetMapping("/index.html")
    //@AuthenticationPrincipal 注解后面跟Spring-Security的User类型参数
    //表示需要Spring-Security将当前登录用户的权限信息赋值给User对象
    //以便我们在方法中验证他的权限或身份
    public ModelAndView index(
            @AuthenticationPrincipal User user){
        //  根据Spring-Security提供的用户判断权限,绝对返回哪个页面
        if(user.getAuthorities().contains(STUDENT)){
            return  new ModelAndView("index");
        }else if(user.getAuthorities().contains(TEACHER)){
            return new ModelAndView("index_teacher");
        }
        return null;

    }



    //显示学生问题发布页面
    @GetMapping("/question/create.html")
    public ModelAndView createQuestion(){
        //templates/question/create.html
        return new ModelAndView("question/create");
    }

    //显示问题详情页面
    @GetMapping("/question/detail.html")
    public ModelAndView detail(
            @AuthenticationPrincipal User user){
        if(user.getAuthorities().contains(STUDENT)){
            //如果是学生,跳detail.html
            return new ModelAndView("question/detail");
        }else if(user.getAuthorities().contains(TEACHER)){
            //如果是老师,跳detail_teacher.html
            return new ModelAndView(
                    "question/detail_teacher");
        }
        return null;
    }




    //临时显示讲师主页的控制器方法
//    @GetMapping("/index_teacher.html")
//    public ModelAndView indexTeacher(){
//        return new ModelAndView("index_teacher");
//    }







}
