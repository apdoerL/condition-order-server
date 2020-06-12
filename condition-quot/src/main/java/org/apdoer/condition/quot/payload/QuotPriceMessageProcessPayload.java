package org.apdoer.condition.quot.payload;

import lombok.Data;


/**
 * 内部通道数据负载
 *
 * @author Li
 * @version 1.0
 * @date 2020/5/12 15:26
 */
@Data
public class QuotPriceMessageProcessPayload {
	//内部通道
    private String systemChannel;

    //传输数据
    private Object data;
}
