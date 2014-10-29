/**
 * 
 */
package com.nationsky.backstage.common;

import java.util.List;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.nationsky.backstage.ServiceLocator;
import com.nationsky.backstage.common.bsc.ICommonService;
import com.nationsky.backstage.common.bsc.dao.po.Schedule;

/**
 * 功能：任务调度管理器
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class ScheduleManager {

	private static ScheduleManager instance = new ScheduleManager();
	private Scheduler scheduler;
	private ScheduleManager(){}
	
	public static ScheduleManager get(){
		return instance;
	}
	
	public int startupAllSchedules() {
		int hasNoStart=0;
		try{
			SchedulerFactory sf = new StdSchedulerFactory();
	        scheduler = sf.getScheduler();
	        List<Schedule> schedules = ServiceLocator.getService(ICommonService.class).findList(Schedule.class, 0, 1000, "id:ASC");
			for(int i=0,j=schedules.size();i<j;i++){
				Schedule s = (Schedule)schedules.get(i);
				String triggerName = "Trigger"+s.getId();
				String jobName = "Job"+s.getId();
				try{
					Class<? extends Object> jobClass = Class.forName(s.getTask());
					JobDetail job = new JobDetail(jobName, Scheduler.DEFAULT_GROUP, jobClass);

					StringBuffer cronExpression = new StringBuffer();
					cronExpression.append(s.getSecond()>=0?String.valueOf(s.getSecond()):"*").append(" ");
					cronExpression.append(s.getMinute()>=0?String.valueOf(s.getMinute()):"*").append(" ");
					cronExpression.append(s.getHour()>=0?String.valueOf(s.getHour()):"*").append(" ");
					cronExpression.append(s.getDayOfMonth()>=0?String.valueOf(s.getDayOfMonth()):s.getWeekDay()>=0?"?":"*").append(" ");
					cronExpression.append(s.getMonth()>=0?String.valueOf(s.getMonth()):"*").append(" ");
					cronExpression.append(s.getWeekDay()>=0?String.valueOf(s.getWeekDay()):"?");
					
					job.getJobDataMap().put("schedule", s);

					CronTrigger trigger = new CronTrigger(triggerName, Scheduler.DEFAULT_GROUP, jobName,
							Scheduler.DEFAULT_GROUP, cronExpression.toString());

					scheduler.addJob(job, true);
			        scheduler.scheduleJob(trigger);
					hasNoStart++;
				}catch(ClassNotFoundException cnfe){
					cnfe.printStackTrace();
				}
			}
			scheduler.start();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return hasNoStart;
	}

	public void shutdownAllSchedules() {
		if(this.scheduler!=null)
			try{
				scheduler.shutdown();
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public void restartAllSchedules(){
		this.shutdownAllSchedules();
		this.startupAllSchedules();
	}
	
}
