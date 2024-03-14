package com.passwordkeeper.notification.emailagentadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class MailingAgentKafkaProducer implements EmailAgentRepository {

    private static final Logger logger = LoggerFactory.getLogger(MailingAgentKafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MailingAgentKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public boolean sendEmail(EmailDto emailDto) {

        try {
            final ProducerRecord<String, String> record = createRecord(emailDto);
            kafkaTemplate.send(record).get(10, TimeUnit.SECONDS);
        }
        catch (ExecutionException | TimeoutException | InterruptedException | JsonProcessingException e) {
            //handleFailure(data, record, e.getCause());
            logger.error("Exception while sending messages to kafka. EmailDto : %s, cause : %s", emailDto.toString(), e.getCause().toString());
            return false;
        }

        return true;
    }

    private ProducerRecord<String, String> createRecord(EmailDto emailDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(emailDto);
        return new ProducerRecord<String, String>("password-keeper-emails", UUID.randomUUID().toString(), s);
    }
}
