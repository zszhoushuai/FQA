package cn.tedu.straw.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@SpringBootTest
class StrawGatewayApplicationTests {

    @Resource
    private RestTemplate restTemplate;

    //测试跨微服务调用的功能
    @Test
    void contextLoads() {
        String url="http://sys-service/v1/auth/demo";
        String str=restTemplate.getForObject(url,String.class);
        System.out.println(str);
    }

}
