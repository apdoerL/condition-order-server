package org.apdoer.condition.common.job;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/7 11:14
 */
public interface Job {
    /**
     * 任务初始化
     */
    void init();

    /**
     * 开始任务,准备工作
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 具体执行任务
     *
     * @throws Exception
     */
    void doWork() throws Exception;


    /**
     * 停止任务
     *
     * @throws Exception
     */
    void shutdown() throws Exception;

    /**
     * 清除任务
     */
    void cleanup();

    /**
     * 任务异常处理
     *
     * @param e
     */
    void exceptionHandle(Exception e);

    /**
     * 获取任务描述
     *
     * @return
     */
    JobDescribution getJobDesc();
}
