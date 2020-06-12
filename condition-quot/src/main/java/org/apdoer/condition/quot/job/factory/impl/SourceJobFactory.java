package org.apdoer.condition.quot.job.factory.impl;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.condition.common.db.service.QuotConfigCenterService;
import org.apdoer.condition.common.job.Job;
import org.apdoer.condition.common.job.JobDescribution;
import org.apdoer.condition.quot.job.QuotSourceJob;
import org.apdoer.condition.quot.job.factory.JobFactory;
import org.apdoer.condition.quot.payload.QuotPriceMessageProcessPayload;
import org.apdoer.condition.quot.payload.QuotPriceMessageSourcePayload;
import org.apdoer.condition.quot.process.Processor;
import org.apdoer.condition.quot.process.assemble.Assembler;
import org.apdoer.condition.quot.process.cleaner.Cleaner;
import org.apdoer.condition.quot.process.filter.Filter;
import org.apdoer.condition.quot.process.impl.QuotMessageProcessor;
import org.apdoer.condition.quot.properties.ZmqSourceProperties;
import org.apdoer.condition.quot.source.Source;
import org.apdoer.condition.quot.source.deserializer.Deserializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Li
 * @version 1.0
 * @date 2020/6/8 12:02
 */
@Slf4j
public class SourceJobFactory implements JobFactory {
    private SourceJobFactory() {
    }

    private static class InnerSourceJobFactory {
        private static final SourceJobFactory INSTANCE = new SourceJobFactory();
    }

    public static SourceJobFactory getInstance() {
        return InnerSourceJobFactory.INSTANCE;
    }


    @Override
    public Job getJob(ZmqSourceProperties.ZmqSource config, QuotConfigCenterService service) {
        try {
            return this.buildJob(config, service);
        } catch (Exception e) {
            log.error("build job error", e);
            return null;
        }
    }

    private Job buildJob(ZmqSourceProperties.ZmqSource config, QuotConfigCenterService service) throws Exception {
        //构造反序列化器
        Deserializer deserializer = this.buildDeserializer(config.getDeserializerClass());
        //构造source
        Source<QuotPriceMessageSourcePayload> source = this.buildSource(config.getSourceClass(), config, deserializer);
        //构造处理器
        Processor processor = this.buildProcessor(config, service);
        String jobGroup = config.getSourceType();
        String jobName = jobGroup + "_" + config.getJobName();
        JobDescribution jobDescribution = this.buildJobDescribution(jobName, jobGroup);
        return new QuotSourceJob(source, processor, jobDescribution);
    }

    private JobDescribution buildJobDescribution(String jobName, String jobGroup) {
        JobDescribution jobDescribution = new JobDescribution();
        jobDescribution.setJobType(JobDescribution.JobType.QUOT_JOB);
        jobDescribution.setJobGroup(jobGroup);
        jobDescribution.setJobName(jobName);
        return jobDescribution;
    }

    private Processor buildProcessor(ZmqSourceProperties.ZmqSource config, QuotConfigCenterService service) throws Exception {
        //构造cleaner
        List<Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> cleaners = this.buildCleaners(config.getCleanerClass());
        //构造filter
        List<Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> filters = this.buildFilterList(config.getFilterClass());
        //构造 assembler
        Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> assembler = this.buildAssembler(config.getAssembleClass(), service);
        return new QuotMessageProcessor(cleaners, filters, assembler);
    }

    private Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload> buildAssembler(String assembleClass, QuotConfigCenterService service) throws Exception {
        Class<?> clazz = Class.forName(assembleClass);
        Class<?>[] parameterTypes = {QuotConfigCenterService.class};
        Object[] initArgs = {service};
        Constructor<?> constructor = clazz.getConstructor(parameterTypes);
        return (Assembler<QuotPriceMessageSourcePayload, QuotPriceMessageProcessPayload>) constructor.newInstance(initArgs);
    }

    private List<Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> buildFilterList(String filterClass) throws Exception {
        String[] clazzArray = filterClass.split("[,]");
        List<Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> filters = new ArrayList<>();
        for (String clazz : clazzArray) {
            filters.add(((Filter<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>) Class.forName(clazz).newInstance()));
        }
        return filters;
    }

    private List<Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> buildCleaners(String cleanerClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String[] clazzArray = cleanerClass.split("[,]");
        List<Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>> cleaners = new ArrayList<>();
        for (String clazz : clazzArray) {
            cleaners.add(((Cleaner<QuotPriceMessageSourcePayload, QuotPriceMessageSourcePayload>) Class.forName(clazz).newInstance()));
        }
        return cleaners;
    }

    private Source<QuotPriceMessageSourcePayload> buildSource(String sourceClass, ZmqSourceProperties.ZmqSource config, Deserializer deserializer) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(sourceClass);
        Class<?>[] parameterTypes = {ZmqSourceProperties.ZmqSource.class, Deserializer.class};
        Object[] initArgs = {config, deserializer};
        Constructor<?> constructor = clazz.getConstructor(parameterTypes);
        return (Source<QuotPriceMessageSourcePayload>) constructor.newInstance(initArgs);
    }

    private Deserializer buildDeserializer(String deserializerClass) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return ((Deserializer) Class.forName(deserializerClass).newInstance());
    }
}
