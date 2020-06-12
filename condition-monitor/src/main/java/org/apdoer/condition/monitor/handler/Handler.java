package org.apdoer.condition.monitor.handler;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/8 19:02
 */
public interface Handler<T> {
    /**
     * 监听处理
     * @param t
     */
    void handle(T t);
}
