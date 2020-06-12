package org.apdoer.condition.quot.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源类型
 * @author li
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuotPriceMessageSourcePayload {

    /**
     * 数据类型
     */
    private Integer messageType;

    /**
     * //数据
     */
    private Object data;


}
