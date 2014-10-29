/**
 * 
 */
package com.nationsky.backstage.util.performance;


/**
 * 功能：查看对象使用内存情况
 * 使用方法：使用方法为继承本类实现newInstance方法返回新实例通过memorySize()获得byte值
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public abstract class MemoryUsage{
	
	private final Runtime s_runtime = Runtime.getRuntime();
	
	protected abstract Object newInstance();
	
	/**
	 * 等到使用内存大小单位:byte
	 * @return
	 * @throws Exception
	 */
	public int memorySize() throws Exception {
		runGC();
		
		// 提供尽可能多(10000)的实例以使计算结果更精确 
		final int count = 10000;
		Object[] objects = new Object[count];
		long heap1 = usedMemory();
		for (int i = -1; i < count; ++i) {
			Object object = null;
			object = newInstance();
			if (i >= 0) {
				objects[i] = object;
			}else{
				object = null;
				runGC();
				heap1 = usedMemory();
			}
		}
		
		runGC();
		long heap2 = usedMemory();
		final int size = Math.round(((float) (heap2 - heap1)) / count);
		for (int i = 0; i < count; ++i) { 
			objects[i] = null;
		}
		objects = null;
		return size;
	}
	
	private void runGC() throws Exception {
		// 执行多次以使内存收集更有效 
		for (int r = 0; r < 4; ++r) {
			_runGC();
		}
	}
	
	private void _runGC() throws Exception {
		long usedMem1 = usedMemory();
		long usedMem2 = Long.MAX_VALUE; 
		for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++i) { 
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.yield();
			usedMem2 = usedMem1;
			usedMem1 = usedMemory(); 
		}
	}
	
	private long usedMemory() { 
		return s_runtime.totalMemory() - s_runtime.freeMemory();
	}
	
}
