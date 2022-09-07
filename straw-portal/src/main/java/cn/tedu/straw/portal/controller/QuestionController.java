package cn.tedu.straw.portal.controller;


import cn.tedu.straw.portal.model.Question;
import cn.tedu.straw.portal.service.IQuestionService;
import cn.tedu.straw.portal.service.ServiceException;
import cn.tedu.straw.portal.vo.QuestionVo;
import cn.tedu.straw.portal.vo.R;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/v1/questions")
@Slf4j
public class QuestionController {
    @Autowired
    IQuestionService questionService;

    //查询返回当前登录用户发布的问题
    @GetMapping("/my")
    public R<PageInfo<Question>> my(Integer pageNum){
        if(pageNum==null){
            pageNum=1;
        }
        int pageSize=8;
        log.debug("开始查询当前用户的问题");
        //这里要处理个异常,因为用户可能没有登录
        try{
            PageInfo<Question> questions=
               questionService.getMyQuestions(pageNum,pageSize);
            return R.ok(questions);
        }catch (ServiceException e){
            log.error("用户查询问题失败!",e);
            return R.failed(e);
        }
    }
    //学生发布问题的控制器方法
    @PostMapping
    public R createQuestion(
            @Validated QuestionVo questionVo,
            BindingResult result){
        if(result.hasErrors()){
            String message=result.getFieldError()
                    .getDefaultMessage();
            log.warn(message);
            return R.unproecsableEntity(message);
        }
        if(questionVo.getTagNames().length==0){
            log.warn("必须选择至少一个标签");
            return R.unproecsableEntity("必须选择至少一个标签");
        }
        if(questionVo.getTeacherNickNames().length==0){
            log.warn("必须选择至少一个老师");
            return R.unproecsableEntity("必须选择至少一个老师");
        }
        //这里应该将vo对象交由service层去新增
        log.debug("接收到表单数据{}",questionVo);

            questionService.saveQuestion(questionVo);
            return R.ok("发布成功!");

    }

    //处理讲师获得分页问题列表的方法
    //这个方法需要特殊权限才能访问
    @GetMapping("/teacher")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public R<PageInfo<Question>> teachers(
            //声明权限是为了获得用户名的
            @AuthenticationPrincipal User user,
            Integer pageNum){
        if(pageNum == null)
            pageNum = 1;
        Integer pageSize=8;
        //调用业务逻辑层的方法
        PageInfo<Question> pageInfo=questionService
                .getQuestionsByTeacherName(
                     user.getUsername(),pageNum,pageSize
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
            @PathVariable Integer id){
        //判断必须要有id
        if(id==null){
            return R.invalidRequest("ID不能为空");
        }
        Question question=questionService.getQuestionById(id);
        return R.ok(question);

    }








}
