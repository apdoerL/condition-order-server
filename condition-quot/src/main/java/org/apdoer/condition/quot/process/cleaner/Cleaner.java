package org.apdoer.condition.quot.process.cleaner;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 14:48
 */
public interface Cleaner<R, T> {

    /**
     * 数据清洗
     * @param resource
     * @return
     */
    T clean(R resource);
}
