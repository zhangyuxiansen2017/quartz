package com.zgm.quartz.service;

import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @date 2018年12月11日 下午2:53:24
 * 
*/
@Component
public class TaskService {
	
	public void task(String name) {
		System.out.println("=================开始任务====================="+name);
	}

}
