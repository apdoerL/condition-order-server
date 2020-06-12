package org.apdoer.condition.quot.job.factory;

import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.job.Job;
import org.apdoer.condition.quot.properties.ZmqSourceProperties;


public interface JobFactory {


    /**
     * 构造job
     *
     * @param config
     * @param service
     * @return
     */
    Job getJob(ZmqSourceProperties.ZmqSource config, QuotConfigCenterService service);


}