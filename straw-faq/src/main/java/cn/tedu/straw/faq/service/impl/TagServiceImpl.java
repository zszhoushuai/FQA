package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.faq.mapper.TagMapper;
import cn.tedu.straw.faq.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    //CopyOnWriteArrayList<>是线程安全的集合,适合在高并发的环境下使用
    private final List<Tag> tags=new CopyOnWriteArrayList<>();
    //ConcurrentHashMap是线程安全的Map,适合在高并发的环境下使用
    private final Map<String,Tag> map=new ConcurrentHashMap<>();

    @Override
    public List<Tag> getTags() {
        //这个if主要是为了保证tags被顺利赋值之后的高效运行
        if(tags.isEmpty()) {
            synchronized (tags) {
                //这个if主要是为了保证不会有两条以上线程为tags重复添加内容
                if (tags.isEmpty()) {
                    //super.list()是父类提供的查询当前指定实体类全部行的代码
                    tags.addAll(super.list());
                    //为所有标签赋值List类型之后,可以同步给map赋值
                    for(Tag t: tags){
                        //将tags中所有标签赋值给map
                        //而map的key是tag的name,value就是tag
                        map.put(t.getName(),t);
                    }

                }
            }
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getName2TagMap() {
        //判断如果map是空,证明上面getTags方法没有运行
        if(map.isEmpty()){
            //那么就调用上面的getTags方法
            getTags();
        }
        return map;
    }
}
