package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.faq.service.IQuestionService;
import cn.tedu.straw.faq.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {
    @Autowired
    IQuestionService questionService;

    //查询返回当前登录用户发布的问题
    @GetMapping("/my")
    public R<PageInfo<Question>> my(Integer pageNum,
                                    @AuthenticationPrincipal UserDetails user) {
        if (pageNum == null) {
            pageNum = 1;
        }
        int pageSize = 8;
        log.debug("开始查询当前用户的问题");
        //这里要处理个异常,因为用户可能没有登录
        try {
            PageInfo<Question> questions =
                    questionService.getMyQuestions(
                            user.getUsername(), pageNum, pageSize);
            return R.ok(questions);
        } catch (ServiceException e) {
            log.error("用户查询问题失败!", e);
            return R.failed(e);
        }
    }

    //学生发布问题的控制器方法
    @PostMapping
    public R createQuestion(
            @Validated QuestionVo questionVo,
            BindingResult result,
            @AuthenticationPrincipal UserDetails user) {
        if (result.hasErrors()) {
            String message = result.getFieldError()
                    .getDefaultMessage();
            log.warn(message);
            return R.unproecsableEntity(message);
        }
        if (questionVo.getTagNames().length == 0) {
            log.warn("必须选择至少一个标签");
            return R.unproecsableEntity("必须选择至少一个标签");
        }
        if (questionVo.getTeacherNickNames().length == 0) {
            log.warn("必须选择至少一个老师");
            return R.unproecsableEntity("必须选择至少一个老师");
        }
        //这里应该将vo对象交由service层去新增
        log.debug("接收到表单数据{}", questionVo);

        questionService.saveQuestion(
                user.getUsername(), questionVo);
        return R.ok("发布成功!");

    }

    //处理讲师获得分页问题列表的方法
    //这个方法需要特殊权限才能访问
    @GetMapping("/teacher")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public R<PageInfo<Question>> teachers(
            //声明权限是为了获得用户名的
            @AuthenticationPrincipal UserDetails user,
            Integer pageNum) {
        if (pageNum == null)
            pageNum = 1;
        Integer pageSize = 8;
        //调用业务逻辑层的方法
        PageInfo<Question> pageInfo = questionService
                .getQuestionsByTeacherName(
                        user.getUsername(), pageNum, pageSize
                );
        return R.ok(pageInfo);

    }

    //显示问题详细的Controller方法
    //为了遵守RESTful的风格这个位置的路径比较特殊
    //例如:/v1/questions/12
    //上面的路径SpringMvc会自动将12赋值给{id}
    //@PathVariable标记的同名属性的值也会是12
    @GetMapping("/{id}")
    public R<Question> question(
            @PathVariable Integer id) {
        //判断必须要有id
        if (id == null) {
            return R.invalidRequest("ID不能为空");
        }
        Question question = questionService.getQuestionById(id);
        return R.ok(question);

    }

    //按用户id返回该用户问题数
    @GetMapping("/count")
    public Integer count(Integer userId){
        return questionService.countQuestionsByUserId(userId);
    }

    //search模块调用这个方法获得所有问题
    @GetMapping("/page")
    public List<Question> questions(
            Integer pageNum,Integer pageSize){
        PageInfo<Question> pageInfo=
                questionService.getQuestion(pageNum,pageSize);
        return pageInfo.getList();
    }
    //这个方法也是用于search调用的
    //目的是返回按照指定每页的条数能查出多少页
    @GetMapping("/page/count")
    public Integer pageCount(Integer pageSize){
        //count()方式是MybatisPlus自带的方法
        Integer totalCount=questionService.count();
        return totalCount%pageSize==0?totalCount/pageSize
                                     :totalCount/pageSize+1;
        //或
        //return (totalCount+pageSize-1)/pageSize;
    }

    /*
        68 /10   6     7
        70 /10   7     7
        68 %10 =8!=0  +1
        70 %10 =0==0  +0
        总行数%每页条数,如果==0就直接使用除法的结果
                      如果!=0就需要对当前的结果+1

        (68+9)/10
        77/10   =7
        79/10   =7
        80/10   =8


     */







}
