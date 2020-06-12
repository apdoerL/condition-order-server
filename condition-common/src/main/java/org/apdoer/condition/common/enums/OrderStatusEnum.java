package org.apdoer.condition.common.enums;


public enum OrderStatusEnum {

    /**
     * 条件单状态
     */
    WAIT(1, "待触发"),
    SUCC(2, "下单成功"),
    FAIL(3, "下单失败"),
    CANCEL(4, "撤销")
    ;

	OrderStatusEnum(int code, String msg) {
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
        for (OrderStatusEnum typeEnum : OrderStatusEnum.values()) {
            if (typeEnum.getCode() == closeType) {
                return true;
            }
        }
        return false;
    }
}
