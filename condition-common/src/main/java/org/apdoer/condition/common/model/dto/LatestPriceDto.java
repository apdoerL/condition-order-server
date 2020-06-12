package org.apdoer.condition.common.model.dto;

import org.apdoer.condition.common.utils.NumberUtils;

import java.math.BigDecimal;


public class LatestPriceDto {

    private Integer messageType;

    private Integer contractId;

    private Long timestamp;

    /**
     * 最新成交价
     */
    private BigDecimal tradePrice;

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public void setContractId(Integer contractId) {
        this.contractId = contractId;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = new BigDecimal(NumberUtils.toSmall(tradePrice));
    }

	public BigDecimal getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public Integer getContractId() {
		return contractId;
	}

	public Long getTimestamp() {
		return timestamp;
	}
}
