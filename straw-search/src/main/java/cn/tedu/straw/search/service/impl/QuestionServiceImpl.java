package cn.tedu.straw.search.service.impl;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.model.Tag;
import cn.tedu.straw.commons.model.User;
import cn.tedu.straw.search.repository.QuestionRepository;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.utils.Pages;
import cn.tedu.straw.search.vo.QuestionVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class QuestionServiceImpl implements IQuestionService {

    @Resource
    QuestionRepository questionRepository;

    @Resource
    RestTemplate restTemplate;

    @Override
    public void sync() {
        //先确定循环次数(确定数据一共有多少页)
        String url=
          "http://faq-service/v1/questions/page/count?pageSize={1}";
        Integer page=restTemplate.getForObject(
                url,Integer.class,10);
        for(int i=1;i<=page;i++){
            //循环调用分页查询Question的方法
            url= "http://faq-service/v1/questions/page?" +
                       "pageNum={1}&pageSize={2}";
            QuestionVo[] questions=restTemplate.getForObject(
                    url,QuestionVo[].class,i,10);
            //增加到ES中
            questionRepository.saveAll(Arrays.asList(questions));
            log.debug("第{}页新增完毕",i);
        }
    }

    public User getUser(String username){
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(
                url,User.class,username);
        return user;
    }
    @Override
    public PageInfo<QuestionVo> search(String key,
        String username, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null)
            pageSize=8;
        User user=getUser(username);
        //Pageable可以内置排序规则
        Pageable pageable= PageRequest.of(pageNum-1,
                pageSize,Sort.Direction.DESC,"createtime");
        Page<QuestionVo> page=questionRepository.queryAllByParams(
                key,key,user.getId(),pageable);
        Map<String,Tag> name2TagMap=getName2TagMap();
        for(QuestionVo q:page.getContent()){
            List<Tag> tags=tagNamesToTags(q.getTagNames());
            q.setTags(tags);
        }
        return Pages.pageInfo(page);
    }

    //编写一个方法,从faq中获得所有标签,然后将这些标签转换为map
    private final Map<String, Tag> name2TagMap=
            new ConcurrentHashMap<>();
    private Map<String,Tag> getName2TagMap(){
        if(name2TagMap.isEmpty()){
            synchronized (name2TagMap){
                if (name2TagMap.isEmpty()){
                    String url="http://faq-service/v1/tags/list";
                    Tag[] tags=restTemplate.getForObject(
                            url,Tag[].class);
                    for(Tag t: tags){
                        name2TagMap.put(t.getName(),t);
                    }
                }
            }
        }
        return name2TagMap;
    }

    private List<Tag> tagNamesToTags(String tagNames){
        //拆分字符串
        String[] names=tagNames.split(",");
        //调用上面的方法获得所有的标签
        Map<String,Tag> name2TagMap=getName2TagMap();
        List<Tag> tags=new ArrayList<>();
        for(String name:names){
            Tag t=name2TagMap.get(name);
            tags.add(t);
        }
        return tags;
    }

    @Override
    public void saveQuestion(QuestionVo questionVo) {
        questionRepository.save(questionVo);
    }
}




