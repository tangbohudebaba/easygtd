/**
 * 
 */
package com.nationsky.backstage.business.v1.bsc.dao.po;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.bsc.CRUD;


@Entity
@org.hibernate.annotations.Entity(selectBeforeUpdate = true,dynamicUpdate=true)
@Table(name = "notify")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@CRUD
public class Notify extends PO {

	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;//通知ID
	private Integer type;//通知类型 1收到任务邀请类型, 2任务被拒绝类型, 3任务被同意类型, 4任务被删除类型, 5任务已完成类型, 6任务延期类型, 7好友添加通知, 8任务修改类型, 9退出任务类型, 
	private Integer taskId;//任务ID
	private Integer userId;//被通知用户ID
	private Integer fromUserId;//来源人员Id
	private String fromUserName;//来源人员姓名
	private String title;//任务标题
	private Long beginTime;//开始时间戳
	private	Long endTime;//结束时间戳
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());//通知创建时间
	private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());//通知更新时间
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Long beginTime) {
		this.beginTime = beginTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	
}
