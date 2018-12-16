package com.zgm.quartz;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.CentralProcessor.TickType;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuartzApplicationTests {

	@Test
	public void contextLoads() {
		SystemInfo si = new SystemInfo();
		Properties props = System.getProperties();
		HardwareAbstractionLayer hal = si.getHardware();
		System.out.println(hal.getProcessor());
		System.out.println(si.getOperatingSystem());
		System.out.println(hal.getMemory());
		
		System.out.println(props.getProperty("os.name"));
		System.out.println(props.getProperty("os.arch"));
		System.out.println(props.getProperty("user.dir"));
		
		System.out.println(props.getProperty("java.version"));
		System.out.println(props.getProperty("java.home"));
		FileSystem fileSystem = si.getOperatingSystem().getFileSystem();
		OSFileStore[] fsArray = fileSystem.getFileStores();
		
		System.out.println(Runtime.getRuntime().totalMemory());
		System.out.println(Runtime.getRuntime().maxMemory());
		System.out.println(Runtime.getRuntime().freeMemory());
		
		
		long[] prevTicks = hal.getProcessor().getSystemCpuLoadTicks();
		Util.sleep(1000);
		long[] ticks = hal.getProcessor().getSystemCpuLoadTicks();
		long nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
		long irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
		long softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
		long steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
		long cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
		long user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
		long iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
		long idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		
	}

}
