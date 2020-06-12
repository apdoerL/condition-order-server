
package org.apdoer.condition.job.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;

@Configuration
@ComponentScan({
		"org.apdoer.common.service",
        "org.apdoer.condition.common",
        "org.apdoer.condition.job",
        "org.apdoer.condition.monitor",
        "org.apdoer.condition.core",
        "org.apdoer.condition.quot"
})
@MapperScan(basePackages = {
		"org.apdoer.common.service.mapper",
        "org.apdoer.condition.common.db.mapper"
})
@EnableFeignClients(basePackages = {
		"org.apdoer.channel.client"
})
public class ServiceScanConfig {
}
