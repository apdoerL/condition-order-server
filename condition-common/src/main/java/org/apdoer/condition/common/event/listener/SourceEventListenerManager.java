package org.apdoer.condition.common.event.listener;

/**
 * 监听器管理
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 17:53
 */
public interface SourceEventListenerManager {


    /**
     * 获取监听器
     * @param listenerName
     * @return
     */
    SourceEventListener getListener(String listenerName);

    /**
     * 构造添加监听器
     * @param listenerName
     * @return
     */
    SourceEventListener buildListener(String listenerName, String handlerName);

    /**
     * 监听器是否存在
     * @param listenerName
     * @return
     */
    boolean exists(String listenerName);
}
