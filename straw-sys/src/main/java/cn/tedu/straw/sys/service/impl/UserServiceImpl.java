package cn.tedu.straw.sys.service.impl;


import cn.tedu.straw.commons.model.*;
import cn.tedu.straw.commons.service.ServiceException;
import cn.tedu.straw.sys.mapper.ClassroomMapper;
import cn.tedu.straw.sys.mapper.UserMapper;
import cn.tedu.straw.sys.mapper.UserRoleMapper;
import cn.tedu.straw.sys.service.IUserService;
import cn.tedu.straw.sys.vo.RegisterVo;
import cn.tedu.straw.sys.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
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
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired(required = false)
    UserMapper userMapper;

    @Autowired
    ClassroomMapper classroomMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    BCryptPasswordEncoder passwordEncoder=
            new BCryptPasswordEncoder();

    @Override
    @Transactional
    public void registerStudent(RegisterVo registerVo) {
        //判断registerVo非空
        if(registerVo==null){
            //如果信息是空则发生异常
            //这里的异常逻辑是我们编写的项目发生的,不是系统异常
            //所以这里以及以后的方法中都需要抛出自定义的异常
            throw ServiceException.unprocesabelEntity("表单数据为空");
        }
        //根据输入的邀请码查询班级,验证邀请码有效性
        QueryWrapper<Classroom> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("invite_code",registerVo.getInviteCode());
        Classroom classroom=classroomMapper.selectOne(queryWrapper);
        log.debug("邀请码对应的班级为:{}",classroom);
        if(classroom==null){
            throw ServiceException.unprocesabelEntity("邀请码错误!");
        }
        //验证数据库中是否已经注册过输入的用户名(手机号)
        //用户名查询用户对象
        User u=userMapper.findUserByUsername(registerVo.getPhone());
        if(u!=null){
            //用户已存在
            throw ServiceException.unprocesabelEntity("手机号已经注册!");
        }
        //User对象的赋值(将表单中的值和一些默认值确定后)
        User user=new User();
        user.setUsername(registerVo.getPhone());
        user.setPhone(registerVo.getPhone());
        user.setNickname(registerVo.getNickname());
        //用户输入的是明文密码,数据库保存的是带算法ID的加密结果!
        user.setPassword("{bcrypt}"+
                passwordEncoder.encode(registerVo.getPassword()));
        user.setClassroomId(classroom.getId());
        user.setCreatetime(LocalDateTime.now());
        user.setEnabled(1);
        user.setLocked(0);
        //执行User新增
        int num=userMapper.insert(user);
        //验证新增结果
        if(num!=1) {
            throw new ServiceException("服务器忙,稍后再试");
        }
        //将新增的用户赋予学生的角色(新增user_role的关系表)
        UserRole userRole=new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(2);
        num=userRoleMapper.insert(userRole);
        //验证关系表新增结果
        if(num!=1) {
            throw new ServiceException("服务器忙,稍后再试");
        }
    }

    private final List<User> masters=
            new CopyOnWriteArrayList<>();
    private final Map<String,User> masterMap=
            new ConcurrentHashMap<>();
    private final Timer timer=new Timer();
    //初始化块:在构造方法运行前开始运行
    {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (masters){
                    masters.clear();
                    masterMap.clear();
                }
            }
        },1000*60*30,1000*60*30);
    }



    @Override
    public List<User> getMasters() {
        if(masters.isEmpty()){
            synchronized (masters){
                if(masters.isEmpty()){
                    QueryWrapper<User> query=new QueryWrapper<>();
                    query.eq("type",1);
                    //将所有老师缓存masters集合中
                    masters.addAll(userMapper.selectList(query));
                    for(User u: masters){
                        masterMap.put(u.getNickname(),u);
                    }
                    //脱敏:将敏感信息从数组(集合\map)中移除
                    for(User u: masters){
                        u.setPassword("");
                    }
                }
            }
        }
        return masters;
    }

    @Override
    public Map<String, User> getMasterMap() {
        if(masterMap.isEmpty()){
            getMasters();
        }
        return masterMap;
    }

//    @Autowired
//    IQuestionService questionService;
    @Autowired
    RestTemplate restTemplate;
    @Override
    public UserVo currentUserVo(String username) {
        UserVo user=userMapper.findUserVoByUsername(username);
        String url="http://faq-service/v1/questions/count?userId={1}";
        Integer count=restTemplate.getForObject(
                url,Integer.class,user.getId());
        user.setQuestions(count);
        //问题收藏数暂时不做
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.findUserByUsername(username);
    }
    @Override
    public List<Permission> getUserPermissions(Integer userId) {
        return userMapper.findUserPermissionsById(userId);
    }
    @Override
    public List<Role> getUserRoles(Integer userId) {
        return userMapper.findUserRolesById(userId);
    }
}
