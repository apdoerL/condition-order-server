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
 * 条件委托-止盈止损撤单-执行线程池
 *
 * @author apdoer
 */
@Component
public class StopProfitLossCancelThreadPool implements BackPressure {

    @Value("${thread.profitloss-cancel.core-pool-size:10}")
    private Integer corePoolSize;

    @Value("${thread.profitloss-cancel.max-pool-size:10}")
    private Integer maxPoolSize;

    @Value("${thread.profitloss-cancel.keep-alive_time:60}")
    private Long keepAliveTime;

    @Value("${thread.profitloss-cancel.queue-capacity:100000}")
    private Integer queueCapacity;

    @Value("${thread.profitloss-cancel.queue-backpressure:80000}")
    private Integer queueBackPressure;

    private ThreadPoolExecutor threadPoolExecutor;

    private BackPressure backPressure;

    public void init() {
        this.threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize,
                this.maxPoolSize,
                this.keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(this.queueCapacity),
                new NameableThreadFactory("stopLAP-cancel-pool"));
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
