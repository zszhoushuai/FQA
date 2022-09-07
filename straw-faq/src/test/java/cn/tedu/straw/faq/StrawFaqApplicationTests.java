package cn.tedu.straw.faq;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.faq.mapper.QuestionMapper;
import cn.tedu.straw.faq.mapper.QuestionTagMapper;
import cn.tedu.straw.faq.mapper.TagMapper;
import cn.tedu.straw.faq.mapper.UserQuestionMapper;
import cn.tedu.straw.faq.service.IQuestionService;
import cn.tedu.straw.faq.service.impl.QuestionServiceImpl;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
class StrawFaqApplicationTests {

    @Resource
    QuestionMapper questionMapper;
    @Resource
    QuestionTagMapper questionTagMapper;
    @Resource
    TagMapper tagMapper;
    @Resource
    UserQuestionMapper userQuestionMapper;
    @Test
    void contextLoads() {
        System.out.println(questionMapper);
        System.out.println(questionTagMapper);
        System.out.println(tagMapper);
        System.out.println(userQuestionMapper);
    }

    @Resource
    private IQuestionService questionService;
    @Test
    public void ques(){
        PageInfo<Question> list=
                questionService.getQuestion(1,10);
        for(Question q:list.getList()){
            System.out.println(q);
        }
    }









}
