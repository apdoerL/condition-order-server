package org.apdoer.condition.core.config;

import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/12 17:04
 */
@Configuration
@ConditionalOnProperty(name = "rocket-mq.enabled", havingValue = "true")
public class RocketMQConfiguration {

    @Value("${rocket-mq.order-property.group-id}")
    public String groupId;

    @Value("${rocket-mq.order-property.access-key}")
    public String accessKey;

    @Value("${rocket-mq.order-property.secret-key}")
    public String secretKey;

    @Value("${rocket-mq.order-property.namesrv-addr}")
    public String namesrvAddr;

    @Bean(name = "orderProducer", initMethod = "start", destroyMethod = "shutdown")
    public Producer transactionProducer() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, namesrvAddr);
        ProducerBean producerBean = new ProducerBean();
        producerBean.setProperties(properties);
        return producerBean;
    }
}
