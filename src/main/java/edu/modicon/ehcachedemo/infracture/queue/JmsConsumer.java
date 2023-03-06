package edu.modicon.ehcachedemo.infracture.queue;

import edu.modicon.ehcachedemo.domain.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
@Slf4j
public class JmsConsumer implements MessageListener {


    @Override
    @JmsListener(destination = "${active-mq.topic}", containerFactory = "jmsListenerContainerFactory")
    public void onMessage(Message message) {
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
