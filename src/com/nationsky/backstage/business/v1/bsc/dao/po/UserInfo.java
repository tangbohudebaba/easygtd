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

import com.nationsky.backstage.core.PO;
import com.nationsky.backstage.core.bsc.CRUD;

/**
 * 
 * @title : 用户信息实体类
 * @description : 
 * @projectname : easygtd
 * @classname : UserInfo
 * @version 1.0
 * @company : nationsky
 * @email : liuchang@nationsky.com
 * @author : liuchang
 * @createtime : 2014年10月30日 上午10:39:07
 */
@Entity
@org.hibernate.annotations.Entity(selectBeforeUpdate = true,dynamicUpdate=true)
@Table(name = "user_info")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@CRUD
public class UserInfo extends PO {

	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;//用户ID
	private String name;//用户名
	private String password;//用户密码：MD5
	private String headURL;//用户头像图片url
	private String phone;//用户手机号
	private String buddyUserIds;//好友userID,空格分开
	private Integer privateType = 2;//1：指定人可见、2：只有任务相关人员可见[默认]、3：所有人可见；4：所有人不可见
	private String privateUserIds;//指定任务可见人员userID,空格分开
	private String pushToken;
	private Timestamp createdAt = new Timestamp(System.currentTimeMillis());//用户创建时间
	private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());//用户信息更新时间
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHeadURL() {
		return headURL;
	}

	public void setHeadURL(String headURL) {
		this.headURL = headURL;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getBuddyUserIds() {
		return buddyUserIds;
	}

	public void setBuddyUserIds(String buddyUserIds) {
		this.buddyUserIds = buddyUserIds;
	}

	public Integer getPrivateType() {
		return privateType;
	}

	public void setPrivateType(Integer privateType) {
		this.privateType = privateType;
	}

	public String getPrivateUserIds() {
		return privateUserIds;
	}

	public void setPrivateUserIds(String privateUserIds) {
		this.privateUserIds = privateUserIds;
	}

	public String getPushToken() {
		return pushToken;
	}

	public void setPushToken(String pushToken) {
		this.pushToken = pushToken;
	}
	
}
