/**
 * 
 */
package com.nationsky.backstage.core;

import java.util.Collections;
import java.util.List;

/**
 * 功能：分页类
 * @author yubaojian0616@163.com
 *
 * mobile enterprise application platform
 * Version 0.1
 */
public class Paging {
	
	//定义空页面
	public static final Paging EMPTY_PAGE = new Paging(Collections.emptyList(),0,10,0);
	
	private java.util.List<?> list;
    private int start;
    private int count;
    private int pageSize;
    private int pageCount;
    private int currentPage;
    private int startOfPreviousPage;
    private int startOfNextPage;
    private boolean hasPrevious;
    private boolean hasNext;
    private int previousPage;
    private int nextPage;
    public Paging(List<?> iList,int iStart,int iPageSize,int iCount){
    	this.list = iList;
        this.start = iStart;
        this.count = iCount;
        this.pageSize= iPageSize>1?iPageSize:1;
        this.pageCount = (count%pageSize>0)?(count/pageSize+1):(count/pageSize);
        this.currentPage = (start/pageSize + 1)>pageCount?pageCount:(start/pageSize + 1);
        this.startOfPreviousPage = (start - pageSize)>0?(start - pageSize):0;  
        this.startOfNextPage = (start + pageSize)>count?start:(start + pageSize);
        this.hasPrevious = currentPage>1;
        this.hasNext = pageCount>currentPage;
        this.previousPage = (currentPage-1)>0?(currentPage-1):currentPage;
        this.nextPage = (currentPage+1)>pageCount?pageCount:currentPage+1;
    }
    /**
     * 获得非null的页面对象
     * @param paging
     * @return
     */
	public static Paging getPage(Paging paging){
    	if(paging == null)return (Paging) Paging.EMPTY_PAGE;
 		return paging;
    }
	/**
	 * 获得最后一页的索引号
	 * @param count
	 * @param size
	 * @return
	 */
	public static int getLastPageIndex(int count,int size){
		if(size<1) return count;
		return count/size+1;
	}
	/**
	 * 获得最后一页的起始码
	 * @param count
	 * @param size
	 * @return
	 */
	public static int getLastPageStartIndex(int count,int size){
		int index = (getLastPageIndex(count,size) - 1) * size ;
		return index>0?index:0;
	}

	/**
	 * @return the list
	 */
	public java.util.List<?> getList() {
		return list;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * @return the pageCount
	 */
	public int getPageCount() {
		return pageCount;
	}

	/**
	 * @return the currentPage
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * @return the startOfPreviousPage
	 */
	public int getStartOfPreviousPage() {
		return startOfPreviousPage;
	}

	/**
	 * @return the startOfNextPage
	 */
	public int getStartOfNextPage() {
		return startOfNextPage;
	}

	/**
	 * @return the hasPrevious
	 */
	public boolean isHasPrevious() {
		return hasPrevious;
	}

	/**
	 * @return the hasNext
	 */
	public boolean isHasNext() {
		return hasNext;
	}

	/**
	 * @return the previousPage
	 */
	public int getPreviousPage() {
		return previousPage;
	}

	/**
	 * @return the nextPage
	 */
	public int getNextPage() {
		return nextPage;
	}
}
