package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Answer;
import cn.tedu.straw.faq.vo.AnswerVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
public interface IAnswerService extends IService<Answer> {

    //提交讲师回复问题的答案信息
    Answer saveAnswer(AnswerVo answerVo, String username);

    //根据问题id查询这个问题的所有回答的方法
    List<Answer> getAnswersByQuestionId(Integer questionId);

    //采纳答案的方法
    boolean accept(Integer answerId);


}
