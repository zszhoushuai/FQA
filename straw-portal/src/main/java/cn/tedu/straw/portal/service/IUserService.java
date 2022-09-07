package cn.tedu.straw.portal.service;

import cn.tedu.straw.portal.model.User;
import cn.tedu.straw.portal.vo.RegisterVo;
import cn.tedu.straw.portal.vo.UserVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
public interface IUserService extends IService<User> {

    //这个方法用法查询获得用户详情对象的业务
    //UserDetails是SpringSecurity验证用户必要的信息
    //String username是SpringSecurity接收的用户输入的用户名
    UserDetails getUserDetails(String username);

    //用户注册的方法(现在是针对学生注册)
    void registerStudent(RegisterVo registerVo);


    //从Spring-Security中获得当前登录用户的用户名的方法
    String currentUsername();

    //查询所有老师用户的方法
    List<User> getMasters();
    //查询所有老师用户的Map方法
    Map<String,User> getMasterMap();

    //查询当前登录用户信息面板的方法
    UserVo currentUserVo();




}
