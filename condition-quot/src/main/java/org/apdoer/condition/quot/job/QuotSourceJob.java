package org.apdoer.condition.quot.job;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.eventbus.EventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBus;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.condition.common.job.Job;
import org.apdoer.condition.common.job.JobDescribution;
import org.apdoer.condition.quot.payload.QuotPriceMessageProcessPayload;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.Processor;
import org.apdoer.condition.quot.source.Source;

import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/7 11:35
 */
@Slf4j
@AllArgsConstructor
public class QuotSourceJob implements Job {

    private Source<QuotPriceMessageSourcePayload> source;
    private Processor<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> processor;
    private JobDescribution jobDescribution;


    @Override
    public void init() {
        log.info("job init start,jobName:{}", this.jobDescribution.getJobName());
        this.source.init();
        log.info("job init success,jobName:{}", this.jobDescribution.getJobName());
    }

    @Override
    public void start() throws Exception {
        log.info("job starting,jobName:{}", this.jobDescribution.getJobName());
        this.source.open();
        log.info("job starting success,jobName:{}", this.jobDescribution.getJobName());
    }

    @Override
    public void doWork() throws Exception {
        QuotPriceMessageSourcePayload sourcePayload = this.source.read();
        if (null != sourcePayload) {
            QuotPriceMessageProcessPayload processPayload = this.processor.process(sourcePayload);
            if (null != processPayload && StringUtils.isNotBlank(processPayload.getSystemChannel())) {
                GuavaEventBus eventBus = ((GuavaEventBus) GuavaEventBusManager.getInstance().getEventBus(processPayload.getSystemChannel()));
                if (null != eventBus) {
                    while (eventBus.tryDo(1, 1L, TimeUnit.SECONDS)) {
                        log.info("backPressureing");
                    }
                    eventBus.publish(new SourceEvent(processPayload.getData()));
                }
            }
        }
    }

    @Override
    public void shutdown() throws Exception {
        this.source.close();
    }

    @Override
    public void cleanup() {
        this.source.cleanup();
    }

    @Override
    public void exceptionHandle(Exception e) {
        log.error("job execute error!,jobName={}", this.jobDescribution.getJobName(), e);
    }

    @Override
    public JobDescribution getJobDesc() {
        return this.jobDescribution;
    }
}
