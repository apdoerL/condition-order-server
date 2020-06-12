package org.apdoer.condition.core.service;


/**
 * core 模块初始化
 * @author apdoer
 */
public interface TradeCoreInitService {

	/**
	 * 初始化数据通道及数据监听器
	 */
	void init();
	
	/**
	 * 刷新数据通道及数据监听器
	 */
	void flush();
}
