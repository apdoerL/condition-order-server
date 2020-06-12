package org.apdoer.condition.common.model.dto;

import java.math.BigDecimal;


import lombok.ToString;
import org.apdoer.condition.common.utils.NumberUtils;

/**
 * 外部行情价
 *
 * @author apdoer
 * @version 1.0
 * @date 2020/5/12 11:23
 */
@ToString
public class IndexPriceDto {

    private Integer messageType;

    private Integer contractId;

    /**
     * 指数价格
     */
    private BigDecimal indexPrice;

    /**
     * 标记价格
     */
    private BigDecimal clearPrice;

    /**
     * 微妙
     */
    private Long time;

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public BigDecimal getIndexPrice() {
		return indexPrice;
	}

	public void setIndexPrice(String _indexPrice) {
		this.indexPrice = new BigDecimal(NumberUtils.toSmall(_indexPrice));;
	}

	public BigDecimal getClearPrice() {
		return clearPrice;
	}

	public void setClearPrice(String _clearPrice) {
		this.clearPrice = new BigDecimal(NumberUtils.toSmall(_clearPrice));
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
}
