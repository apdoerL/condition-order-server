package org.apdoer.condition.core.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConditionOrderMqDto {

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

	private Integer triggerType;

	private BigDecimal curtPrice;

	private BigDecimal triggerPrice;

	private BigDecimal realTriggerPrice;

	private Integer conditionOrderType;

	private String uuid;

	private Integer code;

	private String orderId;

	private Integer status;

	private long updateTime;

	private long createTime;

}
