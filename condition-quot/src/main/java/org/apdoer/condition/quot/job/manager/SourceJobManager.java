package org.apdoer.condition.quot.job.manager;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.job.AbstractJobManager;
import org.apdoer.condition.common.job.Job;
import org.apdoer.condition.quot.job.factory.impl.SourceJobFactory;
import org.apdoer.condition.quot.properties.ZmqSourceProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/8 18:50
 */
@Slf4j
@Component
public class SourceJobManager extends AbstractJobManager {

    @Autowired
    private ZmqSourceProperties zmqSourceProperties;

    @Autowired
    private QuotConfigCenterService quotConfigCenterService;


    @Override
    protected List<Job> getJobList() {
        log.info("build job start");
        List<Job> jobList = new ArrayList<>();
        List<ZmqSourceProperties.ZmqSource> sourceList = zmqSourceProperties.getSourceList();
        if (!CollectionUtils.isEmpty(sourceList)) {
            for (ZmqSourceProperties.ZmqSource zmqSource : sourceList) {
                Job job = SourceJobFactory.getInstance().getJob(zmqSource, quotConfigCenterService);
                if (null != job) {
                    jobList.add(job);
                    log.info("build job success:{}", job.getJobDesc().getJobName());
                }
            }
        }
        log.info("build jobList success size:{}", jobList.size());
        return jobList;
    }
}
