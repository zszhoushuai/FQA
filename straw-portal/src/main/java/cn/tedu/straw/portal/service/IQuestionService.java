package cn.tedu.straw.portal.service;

import cn.tedu.straw.portal.model.Question;
import cn.tedu.straw.portal.vo.QuestionVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
public interface IQuestionService extends IService<Question> {

    //按登录用户查询当前用户问题的方法
    PageInfo<Question> getMyQuestions(
            Integer pageNum,Integer pageSize
    );
    //保存用户发布信息的方法
    void saveQuestion(QuestionVo questionVo);

    //按用户id查询问题数的
    Integer countQuestionsByUserId(Integer userId);

    //分页查询当前登录的老师问题的方法
    PageInfo<Question> getQuestionsByTeacherName(
            String username,Integer pageNum,Integer pageSize
    );

    //按id查询问题详情的方法
    Question getQuestionById(Integer id);

}
