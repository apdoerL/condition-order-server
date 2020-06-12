package org.apdoer.condition.core.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class OrderConditionCancelsReqVo {
	
	//条件单，委托ID
	List<Long> conditionOrderIdList;
	
}
