package org.apdoer.condition.quot.process;

/**
 * 数据处理
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 14:47
 */
public interface Processor<R, T> {


    /**
     * 处理数据
     * @param resource
     * @return
     */
    T process(R resource);


}
