package org.apdoer.condition.job.service;

/**
 * 总体初始化
 * @author apdoer
 * @version 1.0
 * @date 2020/5/13 17:11
 */
public interface WorkJobService {

    /**
     * job 启动系统准备
     */
    void init();

    /**
     * 添加合约时，内部组件刷新（通道、数据监听器刷新）
     */
    void flush();
}
