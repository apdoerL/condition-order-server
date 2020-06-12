package org.apdoer.condition.quot.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/7 10:37
 */
@Data
@Component
@ConfigurationProperties(prefix = "source.zmq")
@ConditionalOnProperty(name = "source.zmq.enabled",havingValue = "true")
public class ZmqSourceProperties {

    private boolean enabled;

    private List<ZmqSource> sourceList;

    @Data
    public static class ZmqSource{
        private String sourceType;
        private String url;
        private Integer queueSize;
        private Integer releaseSize;
        private String sourceClass;
        private String deserializerClass;
        private String cleanerClass;
        private String filterClass;
        private String assembleClass;
        private String jobName;
    }
}
