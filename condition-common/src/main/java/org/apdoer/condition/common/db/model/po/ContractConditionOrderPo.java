package org.apdoer.condition.common.db.model.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tk.mybatis.mapper.entity.IDynamicTableName;

/**
 * IDynamicTableName 通过此接口实现分表路由
 * @author apdoer
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContractConditionOrderPo implements Serializable , IDynamicTableName{
	
	@Id
    private Long conditionOrderId;

	@Column(name = "user_id")
    private Integer userId;

	@Column(name = "contract_id")
    private Integer contractId;

	@Column(name = "quantity")
    private BigDecimal quantity;

	@Column(name = "side")
    private Integer side;

	@Column(name = "order_type")
    private Integer orderType;

	@Column(name = "order_price")
    private BigDecimal orderPrice;

	@Column(name = "position_effect")
    private Integer positionEffect;

	@Column(name = "margin_type")
    private Integer marginType;

	@Column(name = "margin_rate")
    private BigDecimal marginRate;

	@Column(name = "minimal_quantity")
    private BigDecimal minimalQuantity;
    
	@Column(name = "trigger_type")
    private Integer triggerType;
	
	@Column(name = "curt_price")
	private BigDecimal curtPrice;

	@Column(name = "trigger_price")
    private BigDecimal triggerPrice;

	@Column(name = "real_trigger_price")
    private BigDecimal realTriggerPrice;
	
	@Column(name = "condition_order_type")
	private Integer conditionOrderType;

	@Column(name = "stop_profit_price")
    private BigDecimal stopProfitPrice;

	@Column(name = "stop_loss_price")
    private BigDecimal stopLossPrice;

	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "code")
    private Integer code;
	

	@Column(name = "order_id")
    private String orderId;

	@Column(name = "status")
    private Integer status;

	@Column(name = "update_time")
    private Date updateTime;

	@Column(name = "create_time")
    private Date createTime;
    
    @Transient
    @JsonIgnore
    private String tableName;

    @JsonIgnore
	@Override
	public String getDynamicTableName() {
		return tableName;
	}

}
