package org.apdoer.condition.core.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderConditionReqVo {

	//合約ID
    private Integer contractId;

    //张数
    private BigDecimal quantity;

    //买卖方向
    private Integer side;

    //委托类型
    private Integer orderType;

    //委托价格
    private BigDecimal orderPrice;

    //开平标志，1开仓，2平仓
    private Integer positionEffect;

    //保证金类型，1全仓，2逐仓
    private Integer marginType;

    //保证金率
    private BigDecimal marginRate;

    //最小成交量
    private BigDecimal minimalQuantity;
    
    //触发类型
    private Integer triggerType;

    //触发价格
    private BigDecimal triggerPrice;

    //条件单类型
	private Integer conditionOrderType;
	
	//uuid
	private String uuid;
}
