package org.apdoer.condition.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Li
 * @version 1.0
 * @date 2020/5/20 11:21
 */
@Data
@ConfigurationProperties(prefix = "eventbus")
@Component
public class EventBusThreadPoolProperties {

    private ThreadPoolConfig quotEventBusConfig;
    private ThreadPoolConfig tickEventBusConfig;
    private ThreadPoolConfig conditionOrderEventBusConfig;
    private ThreadPoolConfig orderChannelEventBusConfig;


    @Data
    public static class ThreadPoolConfig {
        private int corePoolSize;
        private int maxPoolSize;
        private int initCapacity;
        private int backPressureSize;
        private long keepAlive;
    }
}
