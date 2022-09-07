package cn.tedu.straw.portal;

import cn.tedu.straw.portal.mapper.ClassroomMapper;
import cn.tedu.straw.portal.model.Classroom;
import cn.tedu.straw.portal.service.IUserService;
import cn.tedu.straw.portal.vo.RegisterVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.AfterTestClass;

@SpringBootTest
public class QueryWrapperTest {

    @Autowired
    ClassroomMapper classroomMapper;
    @Test
    public void testWrapper(){
        //按邀请码查询Classroom对象
        QueryWrapper<Classroom> queryWrapper=new QueryWrapper<>();
        //设置条件
        queryWrapper.eq("invite_code","JSD1912-876840");
        //执行查询
        Classroom cr=classroomMapper.selectOne(queryWrapper);

        System.out.println(cr);
    }

    @Autowired
    IUserService userService;

    @Test
    public void testUserService(){
        RegisterVo registerVo=new RegisterVo();
        registerVo.setPhone("13333113131");
        registerVo.setNickname("大树");
        registerVo.setInviteCode("JSD1912-876840");
        registerVo.setPassword("123456");
        registerVo.setConfirm("123456");
        userService.registerStudent(registerVo);
        System.out.println("complate!");
    }












}
