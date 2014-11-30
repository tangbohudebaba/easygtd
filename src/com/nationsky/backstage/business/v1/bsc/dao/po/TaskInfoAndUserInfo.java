/**
 * 
 */
package com.nationsky.backstage.business.v1.bsc.dao.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.bsc.CRUD;


@Entity
@org.hibernate.annotations.Entity(selectBeforeUpdate = true,dynamicUpdate=true)
@Table(name = "task_info_and_user_info")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@CRUD
public class TaskInfoAndUserInfo extends PO {

	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	private Integer isDone = 0;//是否已完成 
	private Integer isFlag = 0;//是否已星标
	private Integer userId;//用户ID
	private Integer taskId;//任务ID
	private Integer isAgree = 0;//是否已经同意了此任务 0没同意 1同意
	private Integer delayNotify = 0;//延期通知 0还没通知过  1已经通知过了,不用在通知了
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getIsDone() {
		return isDone;
	}

	public void setIsDone(Integer isDone) {
		this.isDone = isDone;
	}

	public Integer getIsFlag() {
		return isFlag;
	}

	public void setIsFlag(Integer isFlag) {
		this.isFlag = isFlag;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public Integer getIsAgree() {
		return isAgree;
	}
	
	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public Integer getDelayNotify() {
		return delayNotify;
	}

	public void setDelayNotify(Integer delayNotify) {
		this.delayNotify = delayNotify;
	}
	
	
}
