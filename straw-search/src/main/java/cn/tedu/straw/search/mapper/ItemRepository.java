package cn.tedu.straw.search.mapper;

import cn.tedu.straw.search.bean.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepository extends
        ElasticsearchRepository<Item,Long> {

    //SpringData自动生成实现的查询,我们只需要编写方法即可
    //SpringData会根据约定好的方法名,推测要执行的内容
    Iterable<Item> queryItemsByTitleMatches(String a);

    //按两个条件查询并按指定字段排序
    Iterable<Item>
    queryItemsByTitleMatchesAndBrandMatchesOrderByPriceDesc(
            String title,String brand);

    //实行分页查询
    //返回值不再是Iterable而变为了Page
    Page<Item>
    queryItemsByTitleMatches(String title,Pageable pageable);


}
