package org.apdoer.condition.common.enums;

/**
 * @author apdoer
 */

public enum SlaveDataSortTypeEnum {

    /**
     * 从数据区数据排序规则
     */
    ASC(1, "正序"),
    DESC(2, "反序");

    SlaveDataSortTypeEnum(int code, String msg) {
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
        for (SlaveDataSortTypeEnum typeEnum : SlaveDataSortTypeEnum.values()) {
            if (typeEnum.getCode() == type) {
                return true;
            }
        }
        return false;
    }
}
