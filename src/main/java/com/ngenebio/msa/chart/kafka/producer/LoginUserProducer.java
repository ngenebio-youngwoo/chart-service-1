package com.ngenebio.msa.chart.kafka.producer;

import com.ngenebio.msa.chart.model.kafka.LoginUserMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

//@Component
@Slf4j
@RequiredArgsConstructor
public class LoginUserProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, LoginUserMessage payload) {
        kafkaTemplate.send(topic, payload);
    }
}
