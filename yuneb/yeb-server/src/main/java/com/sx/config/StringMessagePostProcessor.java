package com.sx.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class StringMessagePostProcessor implements MessagePostProcessor {

    @Override
    public Message postProcessMessage(Message message) {
        String content = new String(message.getBody(), StandardCharsets.US_ASCII);
        return new Message(content.getBytes(), message.getMessageProperties());
    }
}
