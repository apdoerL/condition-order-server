package org.apdoer.condition.common.enums;

/**
 * 触发价
 * @author apdoer
 */

public enum TriggerType {

    /**
     * 触发价类型
     */
	TICK(2, "成交价"),
    INDEX(3, "指数价"),
    MARK(4, "标记价")
    ;

	TriggerType(int code, String msg) {
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

    public static boolean isValid(Integer type) {
    	if (null == type) {
    		return false;
    	}
        for (TriggerType typeEnum : TriggerType.values()) {
            if (typeEnum.getCode() == type) {
                return true;
            }
        }
        return false;
    }
}
