package cn.tedu.straw.kafka;

import cn.tedu.straw.kafka.vo.DemoConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class StrawKafkaApplicationTests {

    @KafkaListener(topics = "MyTopic")
    public void abc(ConsumerRecord<String,String> record){
        String json=record.value();
        log.debug("接收信息:{}",json);
    }
    @Test
    void contextLoads() {

    }

}
