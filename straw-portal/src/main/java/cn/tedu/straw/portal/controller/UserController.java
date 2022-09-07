package cn.tedu.straw.portal.controller;


import cn.tedu.straw.portal.model.User;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.R;
import cn.tedu.straw.portal.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    IUserService userService;

    @GetMapping("/master")
    public R<List<User>> master(){
        List<User> masters=userService.getMasters();
        return R.ok(masters);
    }

    //显示用户信息面板的控制层方法
    @GetMapping("/me")
    public R<UserVo> me(){
        UserVo user=userService.currentUserVo();
        return R.ok(user);
    }



}
