package cn.tedu.straw.faq.mapper;


import cn.tedu.straw.commons.model.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@Repository
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("SELECT q.* " +
            " FROM question q" +
            " LEFT JOIN user_question uq " +
            "      ON q.id=uq.question_id" +
            " WHERE uq.user_id=#{userId} OR q.user_id=#{userId}" +
            " ORDER BY q.createtime DESC")
    List<Question> findTeachersQuestions(Integer userId);

    @Update("update question set status=#{status} " +
            " where id=#{questionId}")
    int updateStatus(@Param("questionId") Integer questionId,
                     @Param("status") Integer status);






}
