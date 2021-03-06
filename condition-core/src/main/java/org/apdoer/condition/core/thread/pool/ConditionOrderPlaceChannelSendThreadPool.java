package org.apdoer.condition.core.thread.pool;

import org.apdoer.common.service.thread.NameableThreadFactory;
import org.apdoer.condition.common.backpressure.BackPressure;
import org.apdoer.condition.common.backpressure.impl.FixedThreadPoolBackPressure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author apdoer
 */
@Component
public class ConditionOrderPlaceChannelSendThreadPool implements BackPressure {

    @Value("${thread.order-send.core-pool-size:20}")
    private Integer corePoolSize;

    @Value("${thread.order-send.max-pool-size:20}")
    private Integer maxPoolSize;

    @Value("${thread.order-send.keep-alive_time:30}")
    private Long keepAliveTime;

    @Value("${thread.order-send.queue-capacity:10000}")
    private Integer queueCapacity;

    @Value("${thread.order-send.queue-backpressure:8000}")
    private Integer queueBackPressure;

    private ThreadPoolExecutor threadPoolExecutor;

    private BackPressure backPressure;
    public void init() {
        this.threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize,
                this.maxPoolSize,
                this.keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(this.queueCapacity),
                new NameableThreadFactory("conditionOrder-msgSend-pool"));
        this.backPressure = new FixedThreadPoolBackPressure("threadPoolBackPressure", this.queueBackPressure, this.threadPoolExecutor);
    }

    @Override
    public boolean tryDo(int batch, long timeout, TimeUnit timeUnit) throws InterruptedException {
        return this.backPressure.tryDo(batch, timeout, timeUnit);
    }

    public int size() {
        return this.threadPoolExecutor.getQueue().size();
    }

    public void execute(Runnable runnable) {
        this.threadPoolExecutor.execute(runnable);
    }

    public void shutdown() {
        this.threadPoolExecutor.shutdown();
    }
}
