package com.zgm.quartz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zgm.quartz.entity.Job;
import com.zgm.quartz.service.JobService;

/**
 * @author zhang
 * @date 2018年12月11日 下午4:24:41
 * 
*/
@RestController
public class JobController {
	@Autowired
	private JobService jobService;
	
	@RequestMapping(value="doJob")
	public String doJob(Long jobId) {
		Job job = jobService.selectJobById(jobId);
		jobService.changeStatus(job);
		return null;
	}

}
