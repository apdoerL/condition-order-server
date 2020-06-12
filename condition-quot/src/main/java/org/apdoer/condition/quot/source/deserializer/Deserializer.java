package org.apdoer.condition.quot.source.deserializer;

/**
 * 反序列化
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 11:17
 */
public interface Deserializer<S, T> {

    /**
     * 反序列化
     * @param source 源数据
     * @return 目标数据
     */
    T deserialize(S source);


}
