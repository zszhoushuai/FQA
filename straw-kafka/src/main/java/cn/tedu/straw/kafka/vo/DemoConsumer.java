package cn.tedu.straw.kafka.vo;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoConsumer {

    //消息的接收者需要将消息转成java格式
    private Gson gson=new Gson();

    //下面是kafka接收消息的方法
    //需要指定接收消息的话题
    @KafkaListener(topics = "MyTopic")
    //上面的kafka监听器监听的话题只要有新的信息出现,就会自动运行这个方法
    //下面这个方法的参数是监听器传过来的,类型时固定的
    //他代表从kafka中获得的一条消息记录
    public void receive(ConsumerRecord<String,String> record){
        //将接收到的记录的value取出
        String json=record.value();
        log.debug("接收信息:{}",json);
        //将获得到的json个数字符串转回为java对象
        DemoMessage message=gson.fromJson(json,DemoMessage.class);
        log.debug("java对象:{}",message);

    }



}
