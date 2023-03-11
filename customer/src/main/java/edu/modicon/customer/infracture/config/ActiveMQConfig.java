package edu.modicon.customer.infracture.config;

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
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;
    @Value("${spring.activemq.user}")
    private String username;
    @Value("${spring.activemq.password}")
    private String password;

    @Bean
    public ConnectionFactory connectionFactory() {
        var factory = new ActiveMQConnectionFactory(username, password, brokerUrl);
        factory.setTrustedPackages(List.of("edu.modicon.common", "java.lang"));
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
    @Qualifier("jmsTopicTemplate")
    public JmsTemplate jmsTopicTemplate() {
        var jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setPubSubDomain(true);
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

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerTopicContainerFactory() {
        var factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);
        factory.setPubSubDomain(true);
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsDurListenerTopicContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setPubSubDomain(true);
        factory.setSubscriptionDurable(true);
        factory.setClientId("dur-1");
        return factory;
    }
}
