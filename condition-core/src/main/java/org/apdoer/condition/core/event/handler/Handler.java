package org.apdoer.condition.core.event.handler;

/**
 * @author Li
 * @version 1.0
 * @date 2020/5/13 18:51
 */
public interface Handler<T> {


    /**
     * 监听处理
     * @param t
     */
    void handle(T t);
}
