package com.sx.mail.config;

import com.rabbitmq.client.Channel;
import com.sx.pojo.Employee;
import com.sx.pojo.MailConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

@Component
public class MailReceliver {
    private  static final Logger LOGGER = LoggerFactory.getLogger(MailReceliver.class);
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailProperties mailProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private TemplateEngine templateEngine;
    @RabbitListener(queues =MailConstants.MAIL_QUEUE_NAME)
    public void handler(Message message, Channel channel) {
        Employee employee = (Employee) message.getPayload();
        MessageHeaders headers = message.getHeaders();
        //消息id
        long tag = (long) headers.get(AmqpHeaders.DELIVERY_TAG);
        String msgId = (String) headers.get("msgid");
        HashOperations hashOperations = redisTemplate.opsForHash();

        try {
            if (hashOperations.entries("mail_log").containsKey(msgId)) {
                LOGGER.info("消息已经被消费");
                channel.basicAck(tag, false);
                return;
            }
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(mailProperties.getUsername());
            helper.setTo(employee.getEmail());
            helper.setSubject("入职欢迎邮件");
            helper.setSentDate(new Date());
            Context context = new Context();
            context.setVariable("name", employee.getName());
            context.setVariable("workID", employee.getWorkID());
            context.setVariable("jobLevelName", employee.getJoblevel().getName());
            context.setVariable("departmentName", employee.getDepartment().getName());
            String mail = templateEngine.process("mail", context);
            helper.setText(mail, true);
            javaMailSender.send(mimeMessage);
            LOGGER.info("邮件发送成功");
            //将消息id存入redis
            hashOperations.put("mail_log", msgId, "OK");
            //手动确认
            channel.basicAck(tag, false);
        } catch (Exception e) {
            /**
             * 手动确认消息
             * tag：消息序号
             * multiple：是否确认多条
             * requeue：是否退回队列
             */
            try {
                channel.basicNack(tag, false, true);
            } catch (IOException ex) {
                LOGGER.error("邮件发送失败=========>{}", e.getMessage());
            }
            LOGGER.error("邮件发送失败=========>{}", e.getMessage());
        }
    }
}
