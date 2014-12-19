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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.bsc.CRUD;


@Entity
@org.hibernate.annotations.Entity(selectBeforeUpdate = true,dynamicUpdate=true)
@Table(name = "task_info")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@CRUD
public class TaskInfo extends PO {

	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;//任务ID
	private String title;//任务标题
	private Long beginTime = 0L;//开始时间戳GTM毫秒数
	private Long endTime = 0L;//结束时间戳GTM毫秒数
	private String location;//地理位置经纬度
	private Integer isHasMembers = 0;//是否有成员 0无成员 1有成员
	private transient Integer isDone = 0;//是否已完成 0未完成 1已经完成
	private transient Integer isFlag = 0;//是否已星标 0不是星标任务 1是星标任务
	private Integer isDelete = 0;//是否已经被删除  0没删除 1已经删除
	private transient Integer userId;//当前用户ID
	private transient String memberUserIds;//成员用户IDs,英文半角逗号分割
	private String remark;//备注
	private Integer createrUserId;//创建任务用户ID
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());//任务创建时间
	private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());//任务更新时间
	
	
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getIsHasMembers() {
		return isHasMembers;
	}

	public void setIsHasMembers(Integer isHasMembers) {
		this.isHasMembers = isHasMembers;
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

	public String getMemberUserIds() {
		return memberUserIds;
	}
	public void setMemberUserIds(String memberUserIds) {
		this.memberUserIds = memberUserIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getCreaterUserId() {
		return createrUserId;
	}
	
	public void setCreaterUserId(Integer createrUserId) {
		this.createrUserId = createrUserId;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	
}
