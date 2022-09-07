package cn.tedu.straw.search.kafka;

import cn.tedu.straw.commons.vo.Topic;
import cn.tedu.straw.search.service.IQuestionService;
import cn.tedu.straw.search.vo.QuestionVo;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class KafkaConsumer {

    private Gson gson=new Gson();

    @Resource
    private IQuestionService questionService;

    @KafkaListener(topics = Topic.QUESTIONS)
    public void receiveQuestion(ConsumerRecord<String,String> record) {
        String json = record.value();
        QuestionVo questionVo = gson.fromJson(json, QuestionVo.class);
        log.debug("收到问题{},开始新增", questionVo);
        questionService.saveQuestion(questionVo);
    }
}
