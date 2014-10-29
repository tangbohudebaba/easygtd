/**
 * 
 */
package com.nationsky.backstage.core;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.nationsky.backstage.util.ConvertUtil;
import com.nationsky.backstage.util.DateUtil;
import com.nationsky.backstage.util.StringUtil;

/**
 * 功能：封装各种生成唯一性ID算法的工具类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class Identities {
	
	private static SecureRandom random = new SecureRandom();
	private static AtomicLong atomicLongID = new AtomicLong(10000L);

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间有-分割.
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 * @return
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 使用SecureRandom随机生成Long. 
	 * @return
	 */
	public static long randomLong() {
		return Math.abs(random.nextLong());
	}

	/**
	 * 基于Base62编码的SecureRandom随机生成bytes.
	 * @param length
	 * @return
	 */
	public static String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return ConvertUtil.convertEncodeBase62(randomBytes);
	}
	
	/**
	 * 主要用于数据库的主键，方便基于时间点的跨数据库的异步数据同步。
	 * 前半部分是currentTimeMillis，后半部分是nanoTime（正数）补齐20位的字符串
	 * 如果通过System.nanoTime()获取的是负数，则通过nanoTime = nanoTime+Long.MAX_VALUE+1
	 * @return 形如 yyyyMMddHHmmssSSSZ000001955819583229710000 的(42位)保证唯一的递增的序列号字符串
	 */
	public static String getTimeMillisSequence(){
		long nanoTime = System.nanoTime();
		String preFix = null;
		if (nanoTime<0){
			//负数补位A保证负数排在正数Z前面,解决正负临界值(如A9223372036854775807至Z0000000000000000000)问题。
			preFix="A";
			nanoTime = nanoTime+Long.MAX_VALUE+1;
		}else{
			 preFix="Z";
		}
		String nanoTimeStr = String.valueOf(nanoTime);
		int difBit=String.valueOf(Long.MAX_VALUE).length()-nanoTimeStr.length();
		for (int i=0;i<difBit;i++){
			preFix = StringUtil.concat(preFix,"0");
		}
		long last = atomicLongID.getAndIncrement()%10000 + 10000;		
		return StringUtil.concat(DateUtil.format(new Date(), "yyyyMMddHHmmssSSS"),preFix,nanoTimeStr,last);
	}
}
