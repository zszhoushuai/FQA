package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.Comment;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.faq.mapper.CommentMapper;
import cn.tedu.straw.faq.service.ICommentService;
import cn.tedu.straw.faq.vo.CommentVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    RibbonClient ribbonClient;

    @Override
    public Comment saveComment(CommentVo commentVo, String username) {
        //获得当前登录用户信息
        //User user=userMapper.findUserByUsername(username);
        User user=ribbonClient.getUser(username);
        //构建要新增的评论对象
        Comment comment=new Comment()
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setAnswerId(commentVo.getAnswerId())
                .setContent(commentVo.getContent())
                .setCreatetime(LocalDateTime.now());
        int num=commentMapper.insert(comment);
        if(num!=1){
            throw ServiceException.busy();
        }
        return comment;
    }

    @Override
    public boolean removeComment(Integer commentId, String username) {
        //User user=userMapper.findUserByUsername(username);
        User user=ribbonClient.getUser(username);
        System.out.println("user:"+user);
        System.out.println("type:"+user.getType());
        //判断身份
        if(user.getType()!=null&&user.getType()==1){
            //如果是老师,可以删除
            int num=commentMapper.deleteById(commentId);
            return num == 1;
        }
        //不是老师要删除评论,要判断这个评论是不是当前登录用户发布的
        //那么就获得这个评论的对象
        Comment comment=commentMapper.selectById(commentId);
        //判断要删除的评论的发布者的id是不是当前登录用户的id
        if(comment.getUserId()==user.getId()){
            //是同一用户,可以删除
            int num=commentMapper.deleteById(commentId);
            return num == 1;
        }
        throw ServiceException.invalidRequest("权限不足");
    }

    @Override
    @Transactional
    public Comment updateComment(Integer commentId,
                                 CommentVo commentVo, String username) {
        //获得登录用户信息
        //User user=userMapper.findUserByUsername(username);
        User user=ribbonClient.getUser(username);
        //获得要修改的评论信息
        Comment comment=commentMapper.selectById(commentId);
        //判断修改权限
        if((user.getType()!=null&&user.getType()==1)
                || comment.getUserId()==user.getId()){
            //权限允许,开始修改,修改只能改内容
            comment.setContent(commentVo.getContent());
            int num=commentMapper.updateById(comment);
            if(num != 1){
                throw ServiceException.busy();
            }
            return comment;
        }
        throw ServiceException.invalidRequest("权限不足");
    }
}
