package com.nationsky.backstage.business.common;

/**
 * 通知类型 1收到任务邀请类型, 2任务被拒绝类型, 3任务被同意类型, 4任务被删除类型, 5任务已完成类型, 6任务延期类型, 7好友添加通知, 8任务修改类型, 9退出任务类型,
 * @author liuchang
 *
 */
public enum NotifyTypeEnum {
	nt1(1), nt2(2), nt3(3), nt4(4), nt5(5), nt6(6), nt7(7), nt8(8), nt9(9);
	// 成员变量
	private int index;
	// 构造方法
	private NotifyTypeEnum(int index) {
		this.index = index;
	}
	
	// 普通方法
	public static int getIndex(int index) {
		for (NotifyTypeEnum c : NotifyTypeEnum.values()) {
			if (c.getIndex() == index) {
				return c.index;
			}
		}
		return 0;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}