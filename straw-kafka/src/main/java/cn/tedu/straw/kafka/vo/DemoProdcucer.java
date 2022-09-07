package cn.tedu.straw.kafka.vo;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class DemoProdcucer {

    //我们依赖了Spring-kafka所以可以直接获得发送信息的对象
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    //我们需要将java对象转换为json格式
    private Gson gson=new Gson();
    //kafka的发送者设置每10秒发送一次信息
    @Scheduled(fixedRate = 1000*10)
    public void sendMessage(){
        DemoMessage message=new DemoMessage()
                .setContent("你好外星人!")
                .setId(1000)
                .setTime(System.currentTimeMillis());
        //将message对象转换成json格式
        String json=gson.toJson(message);
        log.debug("发送信息:{}",json);

        //向kafka发送消息,需要制定话题(Topic)
        kafkaTemplate.send("MyTopic",json);

    }
}
