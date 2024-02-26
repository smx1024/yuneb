package com.sx.config;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sx.pojo.MailConstants;
import com.sx.pojo.MailLog;
import com.sx.service.IMailLogService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class RabbitMQConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Autowired
    private CachingConnectionFactory cachingConnectionFactory;
    @Autowired
    private IMailLogService mailLogService;

    @Primary
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        /**
         * 消息确认回调，确认消息是否到达broker
         * data：消息唯一标识
         * ack：确认结果
         * cause：失败原因
         */
        rabbitTemplate.setConfirmCallback((data, ack, cause) -> {
            //消息msgid
               String msgId = data.getId();

            if (ack) {
                System.out.println("消息发送成功：" + msgId);
                LOGGER.info("{}============>消息发送成功", msgId);
                mailLogService.update(new UpdateWrapper<MailLog>().set("status", 1).eq("msgId", msgId));
            } else {
                System.out.println("消息发送失败：" + msgId);
                LOGGER.error("{}============>消息发送失败", msgId);
            }
        });

        /**
         * 消息失败回调，比如router不到queue时回调
         * msg:消息主题
         * repCode :响应码
         * repText:相应描述
         * exchange :交换机
         * routingkey:路由键
         */
        rabbitTemplate.setReturnCallback((msg, repCode, repText, exchange, routingkey) -> {
            System.out.println("消息发送失败：" + msg.getBody());
            LOGGER.error("{}============>消息发送到queue时失败", msg.getBody());
        });

        return rabbitTemplate;
    }


    @Bean
    public Queue queue(){
        return new Queue(MailConstants.MAIL_QUEUE_NAME);
    }
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(MailConstants.MAIL_EXCHANGE_NAME);
    }
    @Bean
    public Binding binding(){
        return new Binding(MailConstants.MAIL_QUEUE_NAME, Binding.DestinationType.QUEUE,MailConstants.MAIL_EXCHANGE_NAME,MailConstants.MAIL_ROUTING_KEY_NAME,null);
    }

}
