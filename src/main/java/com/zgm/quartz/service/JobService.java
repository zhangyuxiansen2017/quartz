package com.zgm.quartz.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zgm.quartz.entity.Job;
import com.zgm.quartz.entity.JobExample;
import com.zgm.quartz.entity.JobExample.Criteria;
import com.zgm.quartz.mapper.JobMapper;
import com.zgm.quartz.util.CronUtils;
import com.zgm.quartz.util.ScheduleConstants;
import com.zgm.quartz.util.ScheduleUtils;

/**
 * @author zhang
 * @date 2018年12月11日 下午2:04:43
 * 
 */
@Service
public class JobService {
	@Autowired
	private Scheduler scheduler;

	@Autowired
	private JobMapper jobMapper;

	/**
	 * 项目启动时，初始化定时器
	 */
	@PostConstruct
	public void init() {
		List<Job> jobList = jobMapper.selectByExample(null);
		for (Job job : jobList) {
			CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, job.getJobId());
			// 如果不存在，则创建
			if (cronTrigger == null) {
				ScheduleUtils.createScheduleJob(scheduler, job);
			} else {
				ScheduleUtils.updateScheduleJob(scheduler, job);
			}
		}
	}


	/**
	 * 暂停任务
	 * 
	 * @param job 调度信息
	 */
	public int pauseJob(Job job) {
		job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
		int rows = jobMapper.updateByExampleSelective(job, null);
		if (rows > 0) {
			ScheduleUtils.pauseJob(scheduler, job.getJobId());
		}
		return rows;
	}

	/**
	 * 恢复任务
	 * 
	 * @param job 调度信息
	 */
	public int resumeJob(Job job) {
		job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
		int rows = jobMapper.updateByExampleSelective(job, null);
		if (rows > 0) {
			ScheduleUtils.resumeJob(scheduler, job.getJobId());
		}
		return rows;
	}

	/**
	 * 删除任务后，所对应的trigger也将被删除
	 * 
	 * @param job 调度信息
	 */
	public int deleteJob(Job job) {
		JobExample example = new JobExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andJobIdEqualTo(job.getJobId());
		int rows = jobMapper.deleteByExample(example);
		if (rows > 0) {
			ScheduleUtils.deleteScheduleJob(scheduler, job.getJobId());
		}
		return rows;
	}


	/**
	 * 任务调度状态修改
	 * 
	 * @param job 调度信息
	 */
	public int changeStatus(Job job) {
		int rows = 0;
		String status = job.getStatus();
		if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
			rows = resumeJob(job);
		} else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
			rows = pauseJob(job);
		}
		return rows;
	}

	/**
	 * 立即运行任务
	 * 
	 * @param job 调度信息
	 */
	public int run(Job job) {
		return ScheduleUtils.run(scheduler, selectJobById(job.getJobId()));
	}
	
	public Job selectJobById(Long jobId) {
		JobExample example = new JobExample();
		Criteria createCriteria = example.createCriteria();
		createCriteria.andJobIdEqualTo(jobId);
		List<Job> selectByExample = jobMapper.selectByExample(example);
		if (selectByExample.size()==0) {
			return selectByExample.get(0);
		}else {
			return null;
		}
		
	}

	/**
	 * 校验cron表达式是否有效
	 * 
	 * @param cronExpression 表达式
	 * @return 结果
	 */
	public boolean checkCronExpressionIsValid(String cronExpression) {
		return CronUtils.isValid(cronExpression);
	}
	
}
