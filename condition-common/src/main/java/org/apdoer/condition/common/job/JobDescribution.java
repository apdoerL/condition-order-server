package org.apdoer.condition.common.job;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author apdoer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDescribution {

    private JobType jobType;

    private String jobGroup;

    private String jobName;

    private JobStatus jobStatus = JobStatus.NEW;

    private long exceptionContineTimes;

    private long allowExceptionContineTimes = Long.MAX_VALUE;

    private long exceptionSleepTime = 5 * 1000;

    private long delayTime = 10 * 1000;

    private long workSleepTime = 0;

    private long sourceBlankSleepTime = 10L;

    private long createTime = System.currentTimeMillis();

    public boolean isRunnable() {
        return (System.currentTimeMillis() - this.createTime) > this.delayTime;
    }


    public void exceptionContineTimesAdd() {
        this.exceptionContineTimes++;
    }

    /**
     * 是否达到异常允许次数上线
     */
    public boolean isAllowException() {
        return this.allowExceptionContineTimes >= this.allowExceptionContineTimes;
    }

    public static enum JobType {
        /**
         * 行情任务
         */
        QUOT_JOB,
        DEAMEN_JOB;
    }

    public static enum JobStatus {
        /**
         * NEW：               创建
         * READY：          准备
         * RUNNING：     运行
         * SLEEP：          休眠
         * STOPED：        停止
         * EXCEPTION：异常
         */
        NEW,
        READY,
        RUNNING,
        SLEEP,
        STOPED,
        EXCEPTION;
    }

}
