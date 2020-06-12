package org.apdoer.condition.common.enums;

/**
 * @author apdoer
 */

public enum ConditionOrderTypeEnum {

    /**
     * 条件单状态
     */
    COMMON(0, "待触发"),
    PROFIT(1, "止盈"),
    LOSS(2, "止损")
    ;

	ConditionOrderTypeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean isValid(Integer closeType) {
    	if (null == closeType) {
    		return false;
    	}
        for (ConditionOrderTypeEnum typeEnum : ConditionOrderTypeEnum.values()) {
            if (typeEnum.getCode() == closeType) {
                return true;
            }
        }
        return false;
    }
}
