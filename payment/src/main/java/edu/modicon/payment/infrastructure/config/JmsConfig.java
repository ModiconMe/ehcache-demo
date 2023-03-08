package edu.modicon.payment.infrastructure.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Session;
import java.util.List;

@EnableJms
@Configuration
public class JmsConfig {

    @Value("${spring.activemq.broker-url}")
    public String brokerUrl;
    @Value("${spring.activemq.user}")
    public String username;
    @Value("${spring.activemq.password}")
    public String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        var factory = new ActiveMQConnectionFactory(username, password, brokerUrl);
        factory.setTrustedPackages(List.of("edu.modicon.payment", "java.lang"));
        return factory;
    }

    @Bean
    @Qualifier("jmsQueueTemplate")
    public JmsTemplate jmsQueueTemplate() {
        var jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setSessionTransacted(false);
        return jmsTemplate;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerQueueContainerFactory() {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }
}
