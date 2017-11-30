package com.yiche.publish.entity;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Page<T> implements Pageable{

	public Page(Integer page, Integer size) {
		this.pageCurrent = page ;
		this.size = size ;
		
		if (size == 0) {
			this.size = 4 ;
		}
	}
	
	public Page(){
		this(0,4) ;
	}
	
	private static final long serialVersionUID = 1L;

	//当前页
	private int pageCurrent ;

	//每页显示的页数
	private int size ;
	
	//总页数
	private int pageCount ;
	
	//一共有多少条数据
	private int rows  ;
	
	//当前页文档集合
	private Set<T> articles = new HashSet<>() ;

	//*******************************************用is也可以但尽量不要用，因为部分框架会引起序列化错误。***********************************************//	
	//*************************************例如：定义为基本数据类型Boolean isSuccess；的属性，他的方法也是isSuccess*********************************//
	//**********************************RPC框架在反向解析的时候，"以为"对应的属性名称是success，导致属性获取不到，进而抛出异常*****************************//
	/*
	 * 判断是否有下一页
	 */
	public boolean isJudgePageNext(){
		if (this.pageCurrent >= this.pageCount - 1) {
			return false ;
		}
		return true ;
	}
	
	/*
	 * 判断是否有上一页
	 */
	public boolean isJudgePageLost(){
		if (this.pageCurrent <= 0) {
			return false ;
		}
		return true ;
	}
	
	/*
	 * 判断是否有下一页
	 */
	/*public boolean getJudgePageNext(){
		if (this.pageCurrent >= this.pageCount - 1) {
			return false ;
		}
		return true ;
	}
	
	
	 * 判断是否有上一页
	 
	public boolean getJudgePageLost(){
		if (this.pageCurrent <= 0) {
			return false ;
		}
		return true ;
	}*/
	//******************************************************************************************//	
	
	/////////////////////////////////////////getter,setter方法///////////////////////////////////////////////////////////
	public int getPageCurrent() {
		return pageCurrent;
	}

	public void setPageCurrent(int pageCurrent) {
		this.pageCurrent = pageCurrent;
		if (pageCurrent < 0) {
			this.pageCurrent = 0 ;
		}
	}
	
	
	/*
	 * 获取下一页,PageRequest中是将其抛出异常
	 */
	public int getPageNext() {
		return this.pageCurrent +1;
	}

	/*
	 *  获取上一页 
	 */
	public int getPageLost() {
		return this.pageCurrent - 1;
	}
	
	

	/*
	 * 获取总页数
	 */
	public int getPageCount() {
		this.pageCount = (rows % size == 0) ? rows/size : rows /size + 1;
		return pageCount;
	}

	public void setRows(int rows){
		this.rows = rows ;
	}

	public int getRows(){
		return this.rows ;
	}
	
	public Set<T> getArticles() {
		return articles;
	}

	public void setArticles(Set<T> articles) {
		this.articles = articles;
	}

	public int getSize() {
		return size;
	}


	@Override
	public String toString() {
		return "Page [pageCurrent=" + pageCurrent +  ", size="
				+ size + ", pageCount=" + pageCount + ", rows=" + rows + ", articles=" + articles + "]";
	}

	////////////////////////////////////////////////////Pageable接口的方法不对其实现///////////////////////////////////////////////////
	@Override
	public Sort getSort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable next() {
		// TODO Auto-generated method stub
		return null;
	}

	public Pageable previous() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pageable first() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPageNumber() {
		return this.pageCurrent ;
	}

	@Override
	public int getPageSize() {
		return this.size;
	}

	@Override
	public long getOffset() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Pageable previousOrFirst() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		return false;
	}
}
