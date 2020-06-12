package org.apdoer.condition.common.db.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.mapper.ContractChannelMappingMapper;
import org.apdoer.condition.common.db.mapper.ContractConditionUserConfigMapper;
import org.apdoer.condition.common.db.model.po.ContractChannelMappingPo;
import org.apdoer.condition.common.db.model.po.ContractConditionUserConfigPo;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Component
@Slf4j
public class QuotConfigCenterServiceImpl implements QuotConfigCenterService {

    private Map<Integer, ContractChannelMappingPo> config = new ConcurrentHashMap<>();

    @Autowired
    private ContractChannelMappingMapper contractChannelMappingMapper;
    @Autowired
    private ContractConditionUserConfigMapper contractConditionUserConfigMapper;

    @Override
    public ContractChannelMappingPo queryContractChannelMaping(Integer contractId) {
        if (this.config.containsKey(contractId)) {
            return this.config.get(contractId);
        } else {
            ContractChannelMappingPo mappingPo = this.contractChannelMappingMapper
                    .queryMappingByContractId(contractId);
            if (null != mappingPo) {
                this.config.put(contractId, mappingPo);
                return mappingPo;
            } else {
                return null;
            }
        }
    }

    @Override
    public List<ContractChannelMappingPo> queryAllMapping() {
        return this.contractChannelMappingMapper.queryAllMapping();
    }

    @Cacheable(value = "cdo", key = "'cdo_userConfig_'+#userId.toString()+'_'+#contractId.toString()")
	@Override
	public ContractConditionUserConfigPo queryUserConfig(Integer userId, Integer contractId) {
		ContractConditionUserConfigPo config = this.contractConditionUserConfigMapper.queryUserConfig(userId, contractId);
		if (null != config) {
			return config;
		}
		config = this.contractConditionUserConfigMapper.queryUserConfig(userId, 0);
		if (null != config) {
			return config;
		}
		return this.contractConditionUserConfigMapper.queryUserConfig(0, 0);
	}

}
