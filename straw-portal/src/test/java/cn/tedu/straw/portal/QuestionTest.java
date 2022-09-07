package cn.tedu.straw.portal;

import cn.tedu.straw.portal.mapper.AnswerMapper;
import cn.tedu.straw.portal.mapper.QuestionMapper;
import cn.tedu.straw.portal.mapper.UserMapper;
import cn.tedu.straw.portal.model.Answer;
import cn.tedu.straw.portal.model.Question;
import cn.tedu.straw.portal.model.Role;
import cn.tedu.straw.portal.service.IQuestionService;
import cn.tedu.straw.portal.vo.UserVo;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

@SpringBootTest
public class QuestionTest {
    @Autowired
    IQuestionService questionService;

    @Test
    //@WithMockUser是Spring-Security提供的注解
    //在测试中如果需要从Spring-Security中获得用户信息,那么就可以用这个注解标记
    //指定用户信息,也要注意,这只是个测试,Spring-Security不会对信息验证
    @WithMockUser(username = "st2",password = "123456")
    public void getQuest(){
        PageInfo<Question> pi=
                questionService.getMyQuestions(1,8);
        for(Question q:pi.getList()){
            System.out.println(q);
        }
    }

    @Autowired
    UserMapper userMapper;
    @Test
    public void roles(){
        List<Role> list=userMapper.findUserRolesById(1);
        for(Role role:list){
            System.out.println(role);
        }
    }


    @Test
    public void testUser(){
        UserVo user=userMapper.findUserVoByUsername("st2");
        System.out.println(user);
    }

    @Autowired
    QuestionMapper questionMapper;
    @Test
    public void teacherQuestions(){
        List<Question> list=
                questionMapper.findTeachersQuestions(3);
        for(Question question:list){
            System.out.println(question);
        }
    }

    @Autowired
    AnswerMapper answerMapper;
    @Test
    public void testAnswer(){
        List<Answer> answers= answerMapper.findAnswersByQuestionId(114);
        for(Answer answer : answers){
            System.out.println(answer);
        }
    }






}
