package cn.tedu.straw.gateway;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
public class RedisTest {
    @Resource
    RedisTemplate<String,String> redisTemplate;
    @Test
    public void redis(){
        //添加数据
        //redisTemplate.opsForValue().set("msg","helloWorld!!!");
        //获得数据
        String str=redisTemplate.opsForValue().get("msg");
        System.out.println(str);
        //删除数据
        //redisTemplate.delete("msg");


    }






}
