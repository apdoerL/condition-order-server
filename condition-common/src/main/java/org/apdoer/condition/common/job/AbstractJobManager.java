package org.apdoer.condition.common.job;

import lombok.extern.slf4j.Slf4j;
import org.apdoer.common.service.thread.NameableThreadFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author apdoer
 * @version 1.0
 * @date 2020/6/7 11:17
 */
@Slf4j
public abstract class AbstractJobManager {
    protected Map<String, Job> jobMap = new ConcurrentHashMap<>();
    protected Map<String, Runnable> runnableMap = new ConcurrentHashMap<>();
    JobThreadPool jobThreadPool = new JobThreadPool();

    public void init() {
        for (Job job : this.getJobList()) {
            this.jobMap.put(job.getJobDesc().getJobName(), job);
            JobRunnable jobRunnable = new JobRunnable(job);
            this.runnableMap.put(job.getJobDesc().getJobName(), jobRunnable);
            jobThreadPool.execute(jobRunnable);
        }
    }

    protected abstract List<Job> getJobList();

    private class JobRunnable implements Runnable {

        private Job job;
        private CountDownLatch countDownLatch;

        public JobRunnable(Job job) {
            this.job = job;
        }

        public JobRunnable(Job job, CountDownLatch countDownLatch) {
            this.job = job;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                while (!this.job.getJobDesc().isRunnable()) {
                    Thread.sleep(job.getJobDesc().getDelayTime());
                }
                this.job.init();
                this.job.start();

                while (this.job.getJobDesc().getJobStatus() != JobDescribution.JobStatus.STOPED) {
                    try {
                        this.job.doWork();
                        Thread.sleep(this.job.getJobDesc().getWorkSleepTime());
                    } catch (Exception e) {
                        log.error("Job work exception", e);
                        this.job.getJobDesc().exceptionContineTimesAdd();
                        if (this.job.getJobDesc().isAllowException()) {
                            Thread.sleep(this.job.getJobDesc().getExceptionSleepTime());
                        } else {
                            this.job.exceptionHandle(e);
                            this.job.getJobDesc().setJobStatus(JobDescribution.JobStatus.STOPED);
                        }
                    }
                }
            } catch (Exception e) {
                this.job.exceptionHandle(e);
            } finally {
                try {
                    this.job.shutdown();
                    this.job.cleanup();
                } catch (Exception e) {
                    log.error("job work exception", e);
                }
                if (null != countDownLatch) {
                    countDownLatch.countDown();
                }
            }
        }
    }

    private static class JobThreadPool {
        ThreadPoolExecutor threadPoolExecutor;

        public JobThreadPool() {
            this.threadPoolExecutor = new ThreadPoolExecutor(0, 5, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100), new NameableThreadFactory("job-threadpool"));
        }

        public void execute(Runnable runnable) {
            threadPoolExecutor.execute(runnable);
        }

    }
}
