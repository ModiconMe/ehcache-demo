package edu.modicon.customer.infracture.queue;

import edu.modicon.customer.domain.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.ObjectMessage;

@Component
@Slf4j
public class JmsConsumer{

    @Autowired
    ApplicationContext applicationContext;

//    @JmsListener(destination = "${spring.activemq.queue}", containerFactory = "jmsListenerQueueContainerFactory")
//    public void onMessageQueue(
//            @Payload Customer customer,
//            @Headers MessageHeaders headers,
//            Message message
//    ) {
//        try{
//            log.info("Received Message: "+ customer.toString());
//        } catch(Exception e) {
//            log.error("Received Exception : "+ e);
//        }
//    }

    @JmsListener(destination = "${spring.activemq.topic}", containerFactory = "jmsListenerTopicContainerFactory")
    public void onMessageTopic(Message message) {
        try{
            ObjectMessage objectMessage = (ObjectMessage)message;
            Customer customer = (Customer) objectMessage.getObject();
            //do additional processing
            log.info("Received Message: "+ customer.toString());
        } catch(Exception e) {
            log.error("Received Exception : "+ e);
        }
    }

}
