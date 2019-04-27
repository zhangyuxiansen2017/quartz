package com.zgm.quartz.service;

import com.zgm.quartz.entity.Job;
import com.zgm.quartz.mapper.JobMapper;
import com.zgm.quartz.util.CronUtils;
import com.zgm.quartz.util.ScheduleConstants;
import com.zgm.quartz.util.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author zhang
 * @date 2018年12月11日 下午2:04:43
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
        List<Job> jobList = jobMapper.selectAll();
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
    public int pauseJob(Long id) {
        int rows = jobMapper.updateStatusById(id, Integer.valueOf(ScheduleConstants.Status.PAUSE.getValue()));
        if (rows > 0) {
            ScheduleUtils.pauseJob(scheduler, id);
        }
        return rows;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    public int resumeJob(Long id) {
        int rows = jobMapper.updateStatusById(id, Integer.valueOf(ScheduleConstants.Status.NORMAL.getValue()));
        if (rows > 0) {
            ScheduleUtils.resumeJob(scheduler, id);
        }
        return rows;
    }

    /**
     * 删除任务后，所对应的trigger也将被删除
     *
     * @param job 调度信息
     */
    public int deleteJob(Long id) {
        int rows = jobMapper.updateStatusById(id, Integer.valueOf(ScheduleConstants.Status.PAUSE.getValue()));
        if (rows > 0) {
            ScheduleUtils.deleteScheduleJob(scheduler, id);
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
            rows = pauseJob(job.getJobId());
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = resumeJob(job.getJobId());
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

        Job job = jobMapper.selectById(jobId);
        if (job != null) {
            return job;
        } else {
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
