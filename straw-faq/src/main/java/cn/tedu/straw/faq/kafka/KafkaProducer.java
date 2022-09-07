package cn.tedu.straw.faq.kafka;

import cn.tedu.straw.commons.model.Question;
import cn.tedu.straw.commons.vo.Topic;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;

    private Gson gson=new Gson();

    public void sendQuestion(Question question){
        log.debug("准备发送问题:{}"+question);
        //将问题转换为json格式
        String json=gson.toJson(question);
        log.debug("开始发送{}"+json);
        kafkaTemplate.send(Topic.QUESTIONS,json);
        log.debug("发送完成!");
    }
}
