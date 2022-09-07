package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.faq.service.IAnswerService;
import cn.tedu.straw.faq.vo.AnswerVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@RequestMapping("/v1/answers")
@Slf4j
public class AnswerController {

    @Resource
    private IAnswerService answerService;

    //新增回复的控制方法
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    public R postAnswer(
            @Validated AnswerVo answerVo,
            BindingResult result,
            @AuthenticationPrincipal User user){
        log.debug("收到回复信息{}",answerVo);
        if(result.hasErrors()){
            String message=result.getFieldError().getDefaultMessage();
            log.warn(message);
            return  R.unproecsableEntity(message);
        }
        //这里调用业务逻辑层方法
        Answer answer=
            answerService.saveAnswer(answerVo,user.getUsername());
        return R.created(answer);
    }

    //根据问题id获得这个问题的所有回答的方法
    // 例如:/v1/answers/question/12
    @GetMapping("/question/{id}")
    public R<List<Answer>> questionAnswers(
            @PathVariable Integer id){
        if(id==null){
            return R.invalidRequest("问题ID不能为空!");
        }
        List<Answer> answers=answerService
                .getAnswersByQuestionId(id);
        return R.ok(answers);
    }

    @GetMapping("/{id}/solved")
    public R solved(
            @PathVariable Integer id){
        log.debug("收到参数:{}",id);
        boolean accepted=answerService.accept(id);
        if(accepted) {
            return R.accepted("采纳成功!");
        }else{
            return R.notFound("不能重复采纳答案");
        }
    }
}
