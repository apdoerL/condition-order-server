package org.apdoer.condition.common.event.eventbus;

import org.apdoer.condition.common.event.SourceEvent;
import org.apdoer.condition.common.event.listener.SourceEventListener;

/**
 * eventbus 通道接口
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 16:44
 */
public interface EventBus {

    /**
     * 为当前eventbus通道注册监听器
     *
     * @param listener
     */
    void subscribe(SourceEventListener listener);

    /**
     * 为当前eventbus通道移除指定监听器
     *
     * @param listener
     */
    void unSubscribe(SourceEventListener listener);

    /**
     * 在当前eventbus通道发布事件
     *
     * @param event
     */
    void publish(SourceEvent event);

    /**
     * 关闭当前通道
     */
    void shutdown();

    /**
     * 获取当前通道的名称
     *
     * @return
     */
    String getName();
}
