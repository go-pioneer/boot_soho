package com.soho.rabbitmq.handler;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import java.io.IOException;

public abstract class DefaultHandler {

    private final static Logger log = LoggerFactory.getLogger(DefaultHandler.class);

    protected abstract void doProcess(Object object);

    protected abstract void doException(Object object, Exception e);

    public void process(byte[] body, long tag, Channel channel) {
        Object object = null;
        try {
            if (body != null && body.length > 0) {
                object = SerializationUtils.deserialize(body);
                doProcess(object);
            }
        } catch (Exception e) {
            if (object != null) {
                doException(object, e);
            }
            log.error(e.getMessage(), e);
        } finally {
            try {
                channel.basicAck(tag, false);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                try {
                    channel.basicAck(tag, false);
                } catch (IOException e1) {
                    log.error(e1.getMessage(), e1);
                }
            }
        }
    }

}