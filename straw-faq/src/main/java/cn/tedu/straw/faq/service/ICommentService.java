package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.faq.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
public interface ICommentService extends IService<Comment> {

    // 新增评论
    Comment saveComment(CommentVo commentVo, String username);

    // 删除评论
    boolean removeComment(Integer commentId,String username);


    // 修改评论
    Comment updateComment(Integer commentId,CommentVo commentVo,String username);

}
