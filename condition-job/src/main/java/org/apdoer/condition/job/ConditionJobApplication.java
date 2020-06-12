package org.apdoer.condition.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author apdoer
 */
@SpringBootApplication
@EnableEurekaClient
public class ConditionJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConditionJobApplication.class, args);
    }

}
