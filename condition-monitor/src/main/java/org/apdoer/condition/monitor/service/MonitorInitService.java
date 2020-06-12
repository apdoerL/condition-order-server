package org.apdoer.condition.monitor.service;

/**
 * 行情模块初始化
 *
 * @author apdoer
 */
public interface MonitorInitService {

    /**
     * 监听器初始化
     */
    void init();

    /**
     * 刷新通道及监听器
     */
    void flush();
}
