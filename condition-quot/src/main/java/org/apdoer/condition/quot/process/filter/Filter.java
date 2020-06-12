package org.apdoer.condition.quot.process.filter;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 14:49
 */
public interface Filter<R, T> {


    /**
     * 数据过滤
     *
     * @param resource
     * @return
     */
    T filter(R resource);
}
