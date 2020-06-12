package org.apdoer.condition.core.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderConditionHisQueryRespVo {

    private String conditionOrderId;

    private Integer userId;

    private Integer contractId;

    private BigDecimal quantity;

    private Integer side;

    private Integer orderType;

    private BigDecimal orderPrice;

    private Integer positionEffect;

    private Integer marginType;

    private BigDecimal marginRate;

    private BigDecimal minimalQuantity;
    
    private BigDecimal curtPrice;
    
    private Integer triggerType;

    private BigDecimal triggerPrice;

    private BigDecimal realTriggerPrice;
	
	private Integer conditionOrderType;

    private BigDecimal stopProfitPrice;

    private BigDecimal stopLossPrice;

    private Integer code;

    private String orderId;

    private Integer status;

    private Date updateTime;

    private Date createTime;
    
    private String uuid;
}
