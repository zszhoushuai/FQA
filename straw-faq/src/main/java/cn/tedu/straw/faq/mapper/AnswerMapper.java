package cn.tedu.straw.faq.mapper;

import cn.tedu.straw.commons.model.Answer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* <p>
    *  Mapper 接口
    * </p>
*
* @author tedu.cn
* @since 2020-12-09
*/
    @Repository
    public interface AnswerMapper extends BaseMapper<Answer> {
        //复杂映射查询按问题id获得所有回答以及每个回答包含的评论
        List<Answer> findAnswersByQuestionId(Integer questionId);

        @Update("update answer set accept_status=#{status}" +
                " where id=#{answerId}")
        int updateStatus(@Param("answerId") Integer answerId,
                         @Param("status") Integer acceptStatus);

    }
