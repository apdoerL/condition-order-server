package org.apdoer.condition.common.event.listener;

import org.apdoer.condition.common.event.SourceEvent;

import java.util.Set;

/**
 * 消息监听器
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 16:41
 */
public interface SourceEventListener {


    /**
     * 监听事件
     *
     * @param event
     */
    void listen(SourceEvent event);


    /**
     * 获取当前已经绑定的通道
     *
     * @return
     */
    Set<String> getSubscribedChannels();

    /**
     * 是否绑定通道
     *
     * @param channelName
     * @return
     */
    boolean isSubscribed(String channelName);

    /**
     * 订阅指定通道
     *
     * @param channelName
     */
    void subscribeChannel(String channelName);
}
