package org.apdoer.condition.common.service.impl;

import org.apdoer.condition.common.constants.CommonConstants;
import org.apdoer.condition.common.data.ConditionOrderData;
import org.apdoer.condition.common.db.service.ContractConditionOrderService;
import org.apdoer.condition.common.service.CommonInitService;
import org.apdoer.condition.common.utils.OrderNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InitServiceImpl implements CommonInitService {
	
	
	@Autowired
	private ContractConditionOrderService contractConditionOrderService;
	@Autowired
	private OrderNumberUtil orderNumberUtil;

	@Override
	public void init() throws Exception {
		this.orderNumberUtil.init();
		List<Integer> contractList = this.contractConditionOrderService.queryContractIds();
		ConditionOrderData.init(CommonConstants.USER_MODE, contractList, this.contractConditionOrderService);
	}

	@Override
	public void flush() {
		List<Integer> contractList = this.contractConditionOrderService.queryContractIds();
		ConditionOrderData.flushMemorySpace(contractList);
	}

}
