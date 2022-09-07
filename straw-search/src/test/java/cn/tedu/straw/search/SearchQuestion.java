package cn.tedu.straw.search;

import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class SearchQuestion {

    @Resource
    IQuestionService questionService;

    @Test
    public void test(){
        PageInfo<QuestionVo> pageInfo=
                questionService.search("java",
                        "st2",1,6);
        for(QuestionVo q:pageInfo.getList()) {
            System.out.println(q);
        }
    }


}
