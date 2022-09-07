package cn.tedu.straw.faq.service;


import cn.tedu.straw.commons.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
public interface ITagService extends IService<Tag> {

    //获得所有标签的方法
    List<Tag> getTags();

    //获得所有标签返回Map的方法
    Map<String,Tag> getName2TagMap();

}
