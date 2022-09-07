package cn.tedu.straw.faq.service.impl;


import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.faq.kafka.KafkaProducer;
import cn.tedu.straw.faq.mapper.QuestionMapper;
import cn.tedu.straw.faq.mapper.QuestionTagMapper;
import cn.tedu.straw.faq.mapper.UserQuestionMapper;
import cn.tedu.straw.faq.service.IQuestionService;
import cn.tedu.straw.faq.service.ITagService;
import cn.tedu.straw.faq.vo.QuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tedu.cn
 * @since 2020-12-09
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionTagMapper questionTagMapper;
    @Autowired
    UserQuestionMapper userQuestionMapper;
    @Autowired
    ITagService tagService;
    @Autowired
    RestTemplate restTemplate;

    //使用RestTemplate获得用户的信息
    private User getUser(String username){
        String url="http://sys-service/v1/auth/user?username={1}";
        User user=restTemplate.getForObject(
                url,User.class,username);
        return user;
    }


    //按登录用户查询当前用户问题的方法
    @Override
    public PageInfo<Question> getMyQuestions(
            //传入翻页查询的参数
            String username ,Integer pageNum,Integer pageSize
    ) {
        //分页查询,决定查询的页数
        if(pageNum==null || pageSize==null){
            //分页查询信息不全,直接抛异常
            throw ServiceException.invalidRequest("参数不能为空");
        }

        //获得当前登录用户的用户名
        //String username=userService.currentUsername();
        log.debug("当前登录用户为:{}",username);
        //如果已经登录,使用之前编写好的findUserByUsername方法
        //查询出当前用户的详细信息(实际上主要需要用户的id)
        //User user=userMapper.findUserByUsername(username);
        User user=getUser(username);
        if(user == null){
            throw ServiceException.gone("登录用户不存在");
        }
        log.debug("开始查询{}用户的问题",user.getId());
        QueryWrapper<Question> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("user_id",user.getId());
        queryWrapper.eq("delete_status",0);
        queryWrapper.orderByDesc("createtime");
        //执行查询之前,要设置分页查询信息
        PageHelper.startPage(pageNum,pageSize);
        //紧接着的查询就是按照上面分页配置的分页查询
        List<Question> list=questionMapper.selectList(queryWrapper);
        log.debug("当前用户的问题数量为:{}",list.size());
        //遍历当前查询出的所有问题对象
        for(Question q: list){
            //将问题每个对象的对应的Tag都查询出来,并赋值为实体类中的List<Tag>
            List<Tag> tags=tagNamesToTags(q.getTagNames());
            q.setTags(tags);
        }
        return new PageInfo<Question>(list);
    }

    @Resource
    private KafkaProducer kafkaProducer;
    @Override
    @Transactional
    public void saveQuestion(String username,QuestionVo questionVo) {
        log.debug("收到问题数据{}",questionVo);
        // 获取当前登录用户信息(可以验证登录情况)
//        String username=userService.currentUsername();
//        User user=userMapper.findUserByUsername(username);
        User user=getUser(username);
        // 将该问题包含的标签拼接成字符串以","分割 以便添加tag_names列
        StringBuilder bud=new StringBuilder();
        for(String tag : questionVo.getTagNames()){
            bud.append(tag).append(",");
        }
        //删除最后一个","
        bud.deleteCharAt(bud.length()-1);
        String tagNames=bud.toString();

        // 构造Question对象
        Question question=new Question()
                .setTitle(questionVo.getTitle())
                .setContent(questionVo.getContent())
                .setUserId(user.getId())
                .setUserNickName(user.getNickname())
                .setTagNames(tagNames)
                .setCreatetime(LocalDateTime.now())
                .setStatus(0)
                .setPageViews(0)
                .setPublicStatus(0)
                .setDeleteStatus(0);
        // 新增Question对象
        int num=questionMapper.insert(question);
        if(num!=1){
            throw  new ServiceException("服务器忙!");
        }
        log.debug("保存了对象:{}",question);
        // 处理新增的Question和对应Tag的关系
        Map<String, Tag> name2TagMap=tagService.getName2TagMap();
        for(String tagName : questionVo.getTagNames()){
            //根据本次循环的标签名称获得对应的标签对象
            Tag tag=name2TagMap.get(tagName);
            //构建QuestionTag实体类对象
            QuestionTag questionTag=new QuestionTag()
                    .setQuestionId(question.getId())
                    .setTagId(tag.getId());
            //执行新增
            num=questionTagMapper.insert(questionTag);
            if(num!=1){
                throw new ServiceException("数据库忙!");
            }
            log.debug("新增了问题和标签的关系:{}",questionTag);
        }


        // 处理新增的Question和对应User(老师)的关系
        //Map<String, User> masterMap=userService.getMasterMap();
        String url="http://sys-service/v1/users/master";
        User[] users=restTemplate.getForObject(
                url,User[].class);
        Map<String,User> masterMap=new HashMap<>();
        for(User u:users){
            masterMap.put(u.getNickname(),u);
        }
        for(String masterName : questionVo.getTeacherNickNames()){
            //根据本次循环的讲师名称获得对应的讲师对象
            User uu=masterMap.get(masterName);
            //构建QuestionTag实体类对象
            UserQuestion userQuestion=new UserQuestion()
                    .setQuestionId(question.getId())
                    .setUserId(uu.getId())
                    .setCreatetime(LocalDateTime.now());
            //执行新增
            num=userQuestionMapper.insert(userQuestion);
            if(num!=1){
                throw new ServiceException("数据库忙!");
            }
            log.debug("新增了问题和讲师的关系:{}",userQuestion);
        }
        //将问题发送到kafka
        kafkaProducer.sendQuestion(question);
    }



    //根据Question的tag_names列的值,返回List<Tag>
    private  List<Tag> tagNamesToTags(String tagNames){
        //得到的tag_name拆分字符串
        //tagNames="java基础,javaSE,面试题"
        String[] names=tagNames.split(",");
        //names={"java基础","javaSE","面试题"}
        //声明List以便返回
        List<Tag> list=new ArrayList<>();
        Map<String,Tag> map=tagService.getName2TagMap();
        //遍历String数组
        for(String name:names) {
            //根据String数组中当前的元素获得Map对应的value
            Tag tag=map.get(name);
            //将这个value保存在list对象中
            list.add(tag);
        }
        return list;
    }

    @Override
    public Integer countQuestionsByUserId(Integer userId) {
        //使用QueryWrapper查询数量的方法
        QueryWrapper<Question> query=new QueryWrapper<>();
        query.eq("user_id",userId);
        query.eq("delete_status",0);
        Integer count=questionMapper.selectCount(query);
        //别忘了返回
        return count;
    }

    @Override
    public PageInfo<Question> getQuestionsByTeacherName(
            String username, Integer pageNum, Integer pageSize) {
        if(pageNum == null)
            pageNum=1;
        if(pageSize == null)
            pageSize=8;

        //根据用户名查询用户对象
        //User user=userMapper.findUserByUsername(username);
        User user=getUser(username);
        //设置分页查询
        PageHelper.startPage(pageNum,pageSize);
        List<Question> questions=
                questionMapper.findTeachersQuestions(user.getId());
        //别忘了,要将问题列中的标签字符串转成标签的List
        for(Question q: questions){
            List<Tag> tags=tagNamesToTags(q.getTagNames());
            q.setTags(tags);
        }
        return new PageInfo<Question>(questions);
    }

    @Override
    public Question getQuestionById(Integer id) {
        //先按id查询出Question
        Question question=questionMapper.selectById(id);
        //再按Question的tag_names列的标签转换为List<Tag>
        List<Tag> tags=tagNamesToTags(question.getTagNames());
        //将转换完成的List<Tag>保存到这个Question的tags属性中
        question.setTags(tags);
        return question;
    }

    @Override
    public PageInfo<Question> getQuestion(
            Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Question> list=
                questionMapper.selectList(null);
        return new PageInfo<Question>(list);
    }
}


