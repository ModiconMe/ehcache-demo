package edu.modicon.customer.infracture.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.modicon.customer.domain.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

@Component
@Slf4j
public class JmsProducer {

    private final JmsTemplate jmsQueueTemplate;
    private final JmsTemplate jmsTopicTemplate;
    private final ObjectMapper objectMapper;

    public JmsProducer(@Qualifier("jmsQueueTemplate") JmsTemplate jmsQueueTemplate,
                       @Qualifier("jmsTopicTemplate") JmsTemplate jmsTopicTemplate, ObjectMapper objectMapper) {
        this.jmsQueueTemplate = jmsQueueTemplate;
        this.jmsTopicTemplate = jmsTopicTemplate;
        this.objectMapper = objectMapper;
    }

    @Value("${spring.activemq.queue}")
    private String queue;

    @Value("${spring.activemq.topic}")
    private String topic;

    public void sendMessageToQueue(Customer customer){
        try{
            log.info("Attempting Send message to Topic: "+ queue);
            jmsQueueTemplate.convertAndSend(queue, customer);
        } catch(Exception e){
            log.error("Received Exception during send Message: ", e);
        }
    }

    public void sendMessageToTopic(Customer customer){
        try{
            log.info("Attempting Send message to Topic: "+ topic);
            jmsTopicTemplate.convertAndSend(topic, customer);
        } catch(Exception e){
            log.error("Received Exception during send Message: ", e);
        }
    }

//    public void sendAndReceivedMessage(Customer customer) {
//        jmsQueueTemplate.sendAndReceive(queue, (session) -> {
//            TextMessage textMessage = null;
//            try {
//                textMessage = session.createTextMessage(objectMapper.writeValueAsString(customer));
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//            textMessage.setStringProperty("_type", "mu property");
//            return textMessage;
//        });
//    }
}
