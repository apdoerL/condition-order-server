package org.apdoer.condition.quot.process.assemble;

/**
 * 数据组装
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 14:50
 */
public interface Assembler<R, T> {


    /**
     * 数据组装
     * @param resource
     * @return
     */
    T assemble(R resource);
}
