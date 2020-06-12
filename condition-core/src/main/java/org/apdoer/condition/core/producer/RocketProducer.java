package org.apdoer.condition.core.producer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RocketProducer {

    @Autowired
    @Qualifier("orderProducer")
    private Producer orderProducer;
    
    public void send(String topic, String tags, String body) {
    	Message msg = new Message(topic, tags, body.getBytes());
    	this.doSend(msg);
    }
    
    public void send(String topic, String tags, String body, String businessKey) {
    	Message msg = new Message(topic, tags, businessKey, body.getBytes());
    	this.doSend(msg);
    }

    private void doSend(Message msg) {
    	try {
    		SendResult result = this.orderProducer.send(msg);
    		if (null != result && null != result.getMessageId()) {
    			log.info("Send mq success, topic:{}, msgId={}", result.getTopic(), result.getMessageId());
    		}
		} catch (Exception e) {
			log.error("Rocket Send topic={} error", msg.getTopic(),e);
		}
    }
}
