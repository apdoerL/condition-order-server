//package org.apdoer.condition.common.utils;
//
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.stereotype.Component;
//
///**
// * @author Li
// * @version 1.0
// * @date 2020/5/15 10:42
// */
//@Component
//public class SpringBeanUtils implements ApplicationContextAware {
//    private ApplicationContext applicationContext;
//
//
//    public <T> T getBean(String beanName, Class<T> clazz) {
//        return applicationContext.getBean(beanName, clazz);
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }
//}
