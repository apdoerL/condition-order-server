package org.apdoer.condition.quot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.config.EventBusThreadPoolProperties;
import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.event.eventbus.impl.GuavaEventBusManager;
import org.apdoer.condition.quot.job.manager.SourceJobManager;
import org.apdoer.condition.quot.service.QuotInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class QuotInitServiceImpl implements QuotInitService {
	
	@Autowired
	private SourceJobManager sourceJobManager;
	
	// 推送使用配置
	@Autowired
	private QuotConfigCenterService quotConfigCenterService;
	
	@Autowired
	private EventBusThreadPoolProperties eventBusThreadPoolProperties;
	
	@Override
	public void init() {
		this.flush();
		this.jobInit();
	}
	
	@Override
	public void flush() {
		List<ContractChannelMappingPo> mappingList = this.quotConfigCenterService.queryAllMapping();
		if (null == mappingList || mappingList.size() == 0) {
			throw new RuntimeException("channel not config");
		}
		this.quotChannelInit(mappingList);
		this.tickChannelInit(mappingList);
	}

	private void jobInit() {
		this.sourceJobManager.init();
	}
	
    public void quotChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====quot channel init start ");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getQuotEventBusConfig();
        for (ContractChannelMappingPo po : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(po.getQuotChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
    }

    public void tickChannelInit(List<ContractChannelMappingPo> mappingList) {
        log.info("====tick channel init start ");
        EventBusThreadPoolProperties.ThreadPoolConfig config = eventBusThreadPoolProperties.getTickEventBusConfig();
        for (ContractChannelMappingPo mappingPo : mappingList) {
            GuavaEventBusManager.getInstance().buildGuavaEventBus(mappingPo.getTickChannel(),
                    config.getCorePoolSize(),
                    config.getMaxPoolSize(),
                    config.getBackPressureSize(),
                    config.getInitCapacity(),
                    config.getKeepAlive());
        }
        log.info("========tick channel init success=");
    }
}
