package cn.tedu.straw.sys.controller;

import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/register")
    public R registerStudent(
            @Validated RegisterVo registerVo,
            BindingResult validaResult) {
        if (validaResult.hasErrors()) {
            String error = validaResult.getFieldError()
                    .getDefaultMessage();
            return R.unproecsableEntity(error);
        }
        System.out.println(registerVo);
        log.debug("得到信息为:{}", registerVo);
        userService.registerStudent(registerVo);
        return R.created("注册成功!");

    }

    @GetMapping("/me")
    public R<UserVo> me(
            @AuthenticationPrincipal User user){
        UserVo userVo=userService.currentUserVo(user.getUsername());
        return R.ok(userVo);
    }

    @GetMapping("/master")
    public List<cn.tedu.straw.commons.model.User> master(){
        return userService.getMasters();
    }
    @GetMapping("/masters")
    public R<List<cn.tedu.straw.commons.model.User>> masters(){
        List<cn.tedu.straw.commons.model.User>
                list=userService.getMasters();
        return R.ok(list);
    }






}
