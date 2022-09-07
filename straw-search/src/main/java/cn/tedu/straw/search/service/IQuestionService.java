package cn.tedu.straw.search.service;


import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;

public interface IQuestionService {

    //同步数据库和ES的方法
    //synchronized 的缩写
    void  sync();

    //从Es中按搜索条件查询的方法
    PageInfo<QuestionVo> search(String key,String username,
                                Integer pageNum,Integer pageSize);

    //新增QuestionVo对象到ES的方法
    void saveQuestion(QuestionVo questionVo);

}
