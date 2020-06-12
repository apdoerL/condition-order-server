package org.apdoer.condition.quot.process.assemble.impl;


import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.model.dto.LatestPriceDto;
import org.apdoer.condition.quot.payload.QuotPriceMessageProcessPayload;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.assemble.Assembler;


/**
 * 最新成交-数据组装
 * @author apdoer
 */
public class TickMessageAssembler implements Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> {
	
	private QuotConfigCenterService quotConfigCenterService;

	public TickMessageAssembler(QuotConfigCenterService _quotConfigCenterService) {
		this.quotConfigCenterService = _quotConfigCenterService;
	}

	@Override
	public QuotPriceMessageProcessPayload assemble(QuotPriceMessageSourcePayload resource) {
		return this.buildProcessPayload(resource);
	}
	
    private QuotPriceMessageProcessPayload buildProcessPayload(QuotPriceMessageSourcePayload resource) {
    	if (null != resource) {
    		//成交价格
    		return this.buildTickMessageProcessPayload(resource);
    	} else {
    		return null;
    	}
    }
    
    private QuotPriceMessageProcessPayload buildTickMessageProcessPayload(QuotPriceMessageSourcePayload data) {
    	ContractChannelMappingPo po = this.quotConfigCenterService.queryContractChannelMaping(((LatestPriceDto)data.getData()).getContractId());
		if (null != po) {
			QuotPriceMessageProcessPayload payload = new QuotPriceMessageProcessPayload();
			payload.setSystemChannel(po.getTickChannel());
			payload.setData(data.getData());
			return payload;
		} else {
			return null;
		}
    }

}
