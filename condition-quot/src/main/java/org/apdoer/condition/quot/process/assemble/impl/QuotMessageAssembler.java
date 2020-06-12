package org.apdoer.condition.quot.process.assemble.impl;


import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.model.dto.IndexPriceDto;
import org.apdoer.condition.quot.payload.QuotPriceMessageProcessPayload;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.assemble.Assembler;


/**
 * 行情消息数据组装
 *
 * @author apdoer
 */
public class QuotMessageAssembler implements Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> {

    private QuotConfigCenterService quotConfigCenterService;

    public QuotMessageAssembler(QuotConfigCenterService quotConfigCenterService) {
        this.quotConfigCenterService = quotConfigCenterService;
    }

    @Override
    public QuotPriceMessageProcessPayload assemble(QuotPriceMessageSourcePayload resource) {
        return this.buildProcessPayload(resource);
    }

    private QuotPriceMessageProcessPayload buildProcessPayload(QuotPriceMessageSourcePayload resource) {
        if (null != resource) {
            return this.buildIndexMessageProcessPayload(resource);
        } else {
            return null;
        }
    }

    private QuotPriceMessageProcessPayload buildIndexMessageProcessPayload(QuotPriceMessageSourcePayload data) {
        ContractChannelMappingPo po = this.quotConfigCenterService
                .queryContractChannelMaping(((IndexPriceDto) data.getData()).getContractId());
        if (null != po) {
            QuotPriceMessageProcessPayload payload = new QuotPriceMessageProcessPayload();
            payload.setSystemChannel(po.getQuotChannel());
            payload.setData(data.getData());
            return payload;
        } else {
            return null;
        }
    }
}
