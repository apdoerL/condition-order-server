package org.apdoer.condition.quot.service;


/**
 * 行情模块初始化
 * @author apdoer
 */
public interface QuotInitService {

	/**
	 * 整体初始化
	 */
	void init();
	
	/**
	 * 刷新渠道
	 */
	void flush();
	
}
