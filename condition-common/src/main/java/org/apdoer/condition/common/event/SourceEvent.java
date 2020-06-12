package org.apdoer.condition.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * eventbus 数据交换类
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SourceEvent {
    /**
     * 封装数据结构，可以自己定义成任意类型：MessageProcessPayload
     */
    private Object data;
}
