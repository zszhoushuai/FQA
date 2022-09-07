package cn.tedu.straw.search;

import cn.tedu.straw.search.bean.Item;
import cn.tedu.straw.search.mapper.ItemRepository;
import cn.tedu.straw.search.repository.QuestionRepository;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class StrawSearchApplicationTests {

    @Resource
    ElasticsearchOperations elasticsearchOperations;
    @Test
    void contextLoads() {
        System.out.println(elasticsearchOperations);
    }

    @Resource
    ItemRepository itemRepository;
    @Test
    void addItem(){
//        Item item=new Item(1L,"华为Mate40","手机"
//                ,"华为",4890.0,"/image/11.jpg");
//        itemRepository.save(item);
        List<Item> list=new ArrayList<>();
        list.add(new Item(2L,"小米手机12","手机"
                ,"小米",4688.0,"/image/11.jpg"));
        list.add(new Item(3L,"Vivo手机X40","手机"
                ,"Vivo",5266.0,"/image/11.jpg"));
        list.add(new Item(4L,"小米手机ix3","手机"
                ,"小米",5655.0,"/image/11.jpg"));
        list.add(new Item(5L,"华为P40","手机"
                ,"华为",4988.0,"/image/11.jpg"));
        list.add(new Item(6L,"荣耀V10","手机"
                ,"荣耀",3280.0,"/image/11.jpg"));
        itemRepository.saveAll(list);
    }

    @Test
    void getById(){
        //Object item=itemRepository.findById(1L);
        Iterable<Item> items= itemRepository.findAll();
        for (Item i: items) {
            System.out.println(i);
        }
        //System.out.println(item);
    }

    @Test
    public void questTitle(){
        Iterable<Item> items=
             itemRepository.
             queryItemsByTitleMatchesAndBrandMatchesOrderByPriceDesc(
                "手机","小米");
        for(Item i: items){
            System.out.println(i);
        }
    }

    @Test
    public void page(){
        //设置分页对象
        //PageRequest.of([从0开始为第一页的页码],[每页条数])
        Pageable pageable=PageRequest.of(0,2);
        Page<Item> page=itemRepository
                .queryItemsByTitleMatches("手机",pageable);
        //Page类和我们之前学习的PageInfo类似
        //除了查询出的信息,还包含分页信息
        //想从Page类中获得查询出的信息,需要调用getContent方法返回List
        //所以遍历如下
        List<Item> items=page.getContent();
        for(Item i:items){
            System.out.println(i);
        }
        //除此之外还可以输出Page类中包含的分页信息
        System.out.println("总页数:"+page.getTotalPages());
        System.out.println("每页条数:"+page.getSize());
        System.out.println("是不是第一页:"+ page.isFirst());
        System.out.println("是不是最后页:"+ page.isLast());
        System.out.println("当前页:"+page.getNumber());



    }

    @Resource
    IQuestionService questionService;
    @Test
    public void go(){
        questionService.sync();
    }
    @Resource
    QuestionRepository questionRepository;
    @Test
    public void get(){
        Object o=questionRepository.findById(1);
        System.out.println(o);
    }

    @Test
    public void getQuery(){
        Page<QuestionVo> page=questionRepository
                .queryAllByParams("java","java",
                        11,PageRequest.of(0,5));
        for(QuestionVo q: page.getContent()){
            System.out.println(q);
        }
    }



}
