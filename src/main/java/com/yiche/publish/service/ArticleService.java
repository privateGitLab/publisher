package com.yiche.publish.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.yiche.publish.entity.Article;
import com.yiche.publish.entity.Page;

public interface ArticleService {

	/**
	 * 插入或更新数据
	 */
	public String addArticle(Article article) ;
	
	
	
	/**
	 * 将文档全部查出来
	 */
	public Page<Article> findAllArticle(Pageable pageable);
	
	/**
	 * 根据文档id查出来
	 */
	public List<Article> singleTerm(Long id);
	
	
	/**
	 * 根据指定实体类型查询索引是否存在
	 */
	public Boolean indexExists();
	
	/**
	 * 根据指定类型创建索引
	 */
	public Boolean createIndex();
	

	/**
	 * 按照时间进行查询
	 */
	public Page<Article> findByPropertyLteGle(String lte,String gte,Pageable pageable);
	
	/**
	 * 按照给定字符串进行模糊查询
	 */
	public Page<Article> findByWordAllOrderByProperty(String word,Pageable pageable);
	
	
	/** 
     * 按作者和标题进行完全匹配搜索
     */  
    public Page<Article> findContain(String filed,Pageable pageable); 
	
	/**
	 * 根据id删除文档
	 * @param id
	 */
	public String delectById(Long id);
	
	/**
	 * 根据ids删除文档
	 */
	public Boolean delectByIds(List<String> ids);
}
