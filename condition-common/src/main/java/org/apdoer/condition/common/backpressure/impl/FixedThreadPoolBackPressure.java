package org.apdoer.condition.common.backpressure.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.backpressure.BackPressure;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Li
 * @version 1.0
 * @date 2020/6/5 11:14
 */
@AllArgsConstructor
@Slf4j
@NoArgsConstructor
public class FixedThreadPoolBackPressure implements BackPressure {
    private String name;

    private int backPressureSize;

    private ThreadPoolExecutor threadPoolExecutor;


    /**
     * 当前线程池队列数据 + 当前放入数据 > 反压阈值 就开始反压
     * @param batch 当前数据
     * @param timeout 反压时间
     * @param timeUnit 时间单位
     * @return
     * @throws InterruptedException
     */
    @Override
    public boolean tryDo(int batch, long timeout, TimeUnit timeUnit) throws InterruptedException {
        if (threadPoolExecutor.getQueue().size() + batch > this.backPressureSize) {
            Thread.sleep(timeUnit.toMillis(timeout));
            log.warn("name={} backpressure,thread sleep:{}", name, timeUnit.toMillis(timeout));
            return true;
        }
        return false;
    }
}
