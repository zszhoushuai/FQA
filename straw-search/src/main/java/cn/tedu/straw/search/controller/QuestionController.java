package cn.tedu.straw.search.controller;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Slf4j
@RequestMapping("/v1/questions")
public class QuestionController {

    @Resource
    IQuestionService questionService;

    @PostMapping
    public R<PageInfo<QuestionVo>> search(String key
            , Integer pageNum
            , @AuthenticationPrincipal UserDetails userDetails){
        if(key==null)
            key="";
        Integer pageSize=8;
        PageInfo<QuestionVo> pageInfo=questionService.search(
                key,userDetails.getUsername(),pageNum,pageSize);
        return R.ok(pageInfo);

    }




}
