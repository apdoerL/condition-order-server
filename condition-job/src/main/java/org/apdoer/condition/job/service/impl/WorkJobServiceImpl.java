package org.apdoer.condition.job.service.impl;

import org.apdoer.condition.monitor.service.MonitorInitService;
import org.apdoer.condition.common.service.CommonInitService;
import org.apdoer.condition.core.service.TradeCoreInitService;
import org.apdoer.condition.job.service.WorkJobService;
import org.apdoer.condition.quot.service.QuotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class WorkJobServiceImpl implements WorkJobService {


    @Autowired
    private QuotInitService quotInitService;

    @Autowired
    private MonitorInitService monitorInitService;
    
    @Autowired
    private CommonInitService commonInitService;
    
    @Autowired
    private TradeCoreInitService tradeCoreInitService;


    @PostConstruct
    @Override
    public void init() {
    	try {
    		//数据区域初始化
			this.commonInitService.init();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        // 行情模块初始化
        this.quotInitService.init();
        // 监控模块初始化
        this.monitorInitService.init();
        //交易模块初始化
        this.tradeCoreInitService.init();
    }

    @Override
    public void flush() {
    	this.commonInitService.flush();
        // 行情模块初始化
        this.quotInitService.flush();
        // 监控模块初始化
        this.monitorInitService.flush();
        //交易模块初始化
        this.tradeCoreInitService.flush();
    }
}
