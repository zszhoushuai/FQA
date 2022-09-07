package cn.tedu.straw.faq.controller;


import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.commons.vo.R;
import cn.tedu.straw.faq.service.ICommentService;
import cn.tedu.straw.faq.vo.CommentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/v1/comments")
@Slf4j
public class CommentController {
    @Resource
    ICommentService commentService;
    @PostMapping
    public R<Comment> postComment(
            @Validated CommentVo commentVo,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails){
        if(result.hasErrors()){
            String message=result.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(message);
        }
        log.debug("收到评论信息{}：",commentVo);
        //这里调用业务逻辑层方法执行评论的新增即可
        Comment comment=commentService.saveComment(
                commentVo,userDetails.getUsername());
        return R.created(comment);

    }

    @GetMapping("/{id}/delete")
    public R removeComment(
            @PathVariable Integer id,
            @AuthenticationPrincipal User user){
        log.debug("开始执行删除评论,id为:{}",id);
        //这里调用业务逻辑层删除的调用
        boolean isdelete=commentService.removeComment(id,user.getUsername());
        if(isdelete) {
            return R.gone("删除成功");
        }else{
            return R.notFound("没有找到对应记录");
        }
    }

    @PostMapping("/{id}/update")
    public R<Comment> update(
            @PathVariable Integer id,
            @Validated CommentVo commentVo,BindingResult result,
            @AuthenticationPrincipal User user){
        if(result.hasErrors()){
            String message=result.getFieldError().getDefaultMessage();
            return R.unproecsableEntity(message);
        }
        Comment comment=
                commentService.updateComment(id,commentVo,user.getUsername());
        return R.ok(comment);

    }








}
