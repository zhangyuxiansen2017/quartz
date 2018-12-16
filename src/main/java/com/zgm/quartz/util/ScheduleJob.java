package com.zgm.quartz.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.zgm.quartz.entity.Job;

/**
 * 定时任务
 * 
 * @author ruoyi
 *QuartzJobBean
 */
@DisallowConcurrentExecution
public class ScheduleJob extends QuartzJobBean {

	private ExecutorService service = Executors.newSingleThreadExecutor();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Job job = (Job) context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES);
		try {
			// 执行任务
			ScheduleRunnable task = new ScheduleRunnable(job.getJobName(), job.getMethodName(), job.getMethodParams());
			Future<?> future = service.submit(task);
			future.get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
