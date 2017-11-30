package com.yiche.publish.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * 
* Copyright: Copyright (c) 2017 LanRu-Caifu
* 
* @ClassName: Article
* @Description: 将文章的信息进行封装
*
* @version: v1.0.0
* @author: wangyingtong
* @date: 2017年11月17日 下午2:22:24 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2017年11月17日    wangyingtong          v1.0.0               修改原因
 */
@Document(indexName="test_article_index", type = "article")
public class Article implements Serializable{

	private static final long serialVersionUID = 1L;

	/*@Id
	@Field(index=false,store=true)
	private Long id;// id
*/
	@Field(type=FieldType.text,index=true,analyzer="ik",store=true,searchAnalyzer="ik")
	private String title;// 文章标题

	@Field(type=FieldType.text,index=true,analyzer="ik",store=true,searchAnalyzer="ik")
	private String author;// 作者

	@Field(type=FieldType.Long,index=false,store=true)
	private Long wordNumber;// 文章字数

	@Field(type=FieldType.text,index=true,analyzer="ik",store=true,searchAnalyzer="ik")
	private String content;// 文章内容

	//发表时间
	/*@Field(format=DateFormat.date_time,index=true,store=true,type=FieldType.Date)
	private Date postTime;*/
	
	//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")  //取日期时使用  
	@DateTimeFormat(iso=ISO.DATE_TIME,pattern = "yyyy-MM-dd HH:mm:ss")//存日期时使用
	@Field(format=DateFormat.date_time,index=false,store=true,type=FieldType.Date,pattern="yyyy-MM-dd HH:mm:ss")
	private Date postTime;
	
	@Id
	@Field(index=false,store=true)
	private Long id;// id

	/*@Field(type=FieldType.String,index=FieldIndex.analyzed,analyzer="ik",store=true,searchAnalyzer="ik")
	private String title;// 文章标题

	@Field(type=FieldType.String,index=FieldIndex.analyzed,analyzer="ik",store=true,searchAnalyzer="ik")
	private String author;// 作者

	@Field(type=FieldType.Long,index=FieldIndex.not_analyzed,store=true)
	private Long wordNumber;// 文章字数

	@Field(type=FieldType.String,index=FieldIndex.analyzed,analyzer="ik",store=true,searchAnalyzer="ik")
	private String content;// 文章内容

	//发表时间
	@Field(format=DateFormat.date_time,index=FieldIndex.no,store=true,type=FieldType.Object)
	private Date postTime;*/
	
	//点击量
	@Field(type=FieldType.Long,index=false)
	private Long clickCount;
	
	//无参构造器
	public Article(){}
	
	//有参构造器
	public Article(Long id,String title,String author,Long wordNumber,String content,Date postTime,Long clickCount){
		this.id = id ;
		this.title = title ;
		this.author = author ;
		this.wordNumber = wordNumber ;
		this.content = content ;
		
		this.postTime = postTime ;
		this.clickCount = clickCount ;
	}
	/**
	 */
	public Date getPostTime() {
		return postTime;
	}

	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getWordNumber() {
		return wordNumber;
	}

	public void setWordNumber(Long wordNumber) {
		this.wordNumber = wordNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", author=" + author + ", wordNumber=" + wordNumber
				+ ", content=" + content + ", postTime=" + postTime + ", clickCount=" + clickCount + "]";
	}
}
