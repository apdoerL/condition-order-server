package org.apdoer.condition.quot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author apdoer
 */

@AllArgsConstructor
@Getter
public enum QuotMessageTypeEnum {

    /**
     * 指数价格
     */
    QUOT_SNAPSHOT(4, "最新指数价格/标记价格"),
    TRADE_PRICE(6, "最新指数价格");


    private int code;
    private String msg;



    public static boolean valid(int code){
        QuotMessageTypeEnum[] values = QuotMessageTypeEnum.values();
        for (QuotMessageTypeEnum value : values) {
            if (code == value.code){
                return true;
            }
        }
        return false;
    }
}