package com.yiche.publish.service.impl;

import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yiche.publish.dao.ArticleRepository;
import com.yiche.publish.dao.impl.ArticleRepositoryImpl;
import com.yiche.publish.entity.Article;
import com.yiche.publish.entity.Page;
import com.yiche.publish.service.ArticleService;
import com.yiche.publish.utile.GetFullMethodName;
import com.yiche.publish.utile.TimeFormattingUtile;
import com.yiche.publish.utile.UeflexUtile;

/**
 * 
* Copyright: Copyright (c) 2017 LanRu-Caifu
* 
* @ClassName: ArticleServiceImpl
* @Description: 业务逻辑层，用于处理业务逻辑 
*
* @version: v1.0.0
* @author: wangyingtong
* @date: 2017年11月21日 下午2:23:31 
* 
* Modification History:
* Date         Author          Version        Description
*---------------------------------------------------------*
* 2017年11月21日   wangyingtong        v1.0.0          修改原因
 */
@Service
public class ArticleServiceImpl implements ArticleService{

	Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class) ;
	
	@Autowired
	ArticleRepository articleRepository;

	/**
	 * 将文档全部查出来
	 */
	@Override
	public Page<Article> findAllArticle(Pageable pageable) {
		Page<Article> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize()) ;
		page.setRows(Integer.valueOf(articleRepository.getCount().toString()));
		if (pageable.getPageNumber() > page.getPageCount()) {
			page.setPageCurrent(page.getPageCount());
		}
		List<Article> allArticle = null ;
		try {
			allArticle = articleRepository.getAllArticle(page);
			if (page.getPageCurrent() != 0 && allArticle.isEmpty()) {
				page.setPageCurrent(page.getPageCurrent() - 1);
				allArticle = articleRepository.getAllArticle(page) ;
			}
			logger.info("在" + GetFullMethodName.getFullMethodName(getClazz(), "findAllArticle", Pageable.class) + "方法中查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在" + GetFullMethodName.getFullMethodName(getClazz(), "findAllArticle", Pageable.class) + "方法中查询出现错误！！！，ERROR：",e);
		}
		if (allArticle != null) {
			page.setArticles(new HashSet<>(allArticle));
		}
		return page ;
	}
	
	/**
	 * 根据文档id查出来
	 */
	public List<Article> singleTerm(Long id){
		return articleRepository.singleTerm(id) ;
	}
	
	
	/**
	 * 根据指定实体类型查询索引是否存在
	 */
	public Boolean indexExists(){
		return articleRepository.indexExists() ;
	}
	
	/**
	 * 根据指定类型创建索引
	 */
	public Boolean createIndex(){
		return articleRepository.createIndex() ;
	}
	
	/**
	 * 插入或更新数据
	 * @param article
	 * @return
	 */
	@Override
	public String addArticle(Article article) {
		return articleRepository.save(article) ;
	}

	/**
	 * 按照时间进行查询
	 */
	public Page<Article> findByPropertyLteGle(String gte,String lte,Pageable pageable){
		Long gteLong = null ;
		Long lteLong= null ;
		
		if (gte == null & lte != null) {
			try {
				lteLong = TimeFormattingUtile.getFormatStr(lte) ;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("在" + GetFullMethodName.getFullMethodName(getClazz(), "findByPropertyLteGle", String.class,String.class,Pageable.class) + "方法中" + lte + "类型转换异常！！！",e); 
			}
		}else if(lte == null & gte != null){
			try {
				gteLong = TimeFormattingUtile.getFormatStr(gte) ;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("在" + GetFullMethodName.getFullMethodName(getClazz(), "findByPropertyLteGle", String.class,String.class,Pageable.class) + "方法中" + gte + "类型转换异常！！！",e); 
			}
		}else if(lte != null && gte != null){
			try {
				gteLong = TimeFormattingUtile.getFormatStr(gte) ;
				lteLong = TimeFormattingUtile.getFormatStr(lte) ;
			} catch (NumberFormatException e) {
				e.printStackTrace();
				logger.error("在" + GetFullMethodName.getFullMethodName(getClazz(), "findByPropertyLteGle", String.class,String.class,Pageable.class) + "方法中类型转换异常！！！",e); 
			}
		}
		
		logger.info("在" + GetFullMethodName.getFullMethodName(getClass(), "findByPropertyLteGle", String.class,String.class,Pageable.class) + "方法中，类型转换成功：gte="+ gteLong + ",lte=" + lteLong);
		
		Page<Article> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize()) ;
		List<Article> findByPropertyLteGle = articleRepository.findByPropertyLteGle(gteLong, lteLong, pageable) ;
		Long findByTimeCount = articleRepository.findByTimeCount(gteLong, lteLong) ;
		if (findByTimeCount != null) {
			page.setRows(Integer.valueOf(findByTimeCount.toString()));
		}
		if (findByPropertyLteGle.isEmpty() && page.getPageCurrent() != 0) {
			page.setPageCurrent(page.getPageCurrent() - 1);
			findByPropertyLteGle = articleRepository.findByPropertyLteGle(gteLong, lteLong, page);
		}
		page.setArticles(new HashSet<>(findByPropertyLteGle));
		return page ;
	}
	
	/**
	 * 按照给定字符串进行模糊查询
	 */
	public Page<Article> findByWordAllOrderByProperty(String word,Pageable pageable){
		
		Page<Article> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize()) ;
		List<Article> findByWordAllOrderByProperty = articleRepository.findByWordAllOrderByProperty(word, pageable) ;
		Long findAllWordCount = articleRepository.findAllWordCount(word) ;
		if (findAllWordCount != null) {
			page.setRows(Integer.valueOf(findAllWordCount.toString()));
		}
		if (findAllWordCount != null && findByWordAllOrderByProperty.isEmpty()) {
			page.setPageCurrent(pageable.getPageNumber() - 1);
			findByWordAllOrderByProperty = articleRepository.findByWordAllOrderByProperty(word,page) ;
		}
		page.setArticles(new HashSet<>(findByWordAllOrderByProperty));
		
		return page ;
	}
	
	
	/** 
     * 按作者和标题进行完全匹配搜索
     */  
    public Page<Article> findContain(String filed,Pageable pageable) {  
    	
    	Page<Article> page = new Page<>(pageable.getPageNumber(),pageable.getPageSize()) ;
    	
    	List<Article> findContain = articleRepository.findContain(filed, pageable) ;
    	
    	Long titleAndAuthorCount = articleRepository.findByTitleAndAuthorCount(filed) ;
    	if (titleAndAuthorCount != null) {
    		page.setRows(Integer.valueOf(titleAndAuthorCount.toString()));
		}
    	if (titleAndAuthorCount != null && findContain.isEmpty()) {
			page.setPageCurrent(pageable.getPageNumber() - 1);
			findContain = articleRepository.findContain(filed, page) ;
		}
    	
    	page.setArticles(new HashSet<>(findContain));
    	
        return page ;  
    } 
	
	/**
	 * 根据id删除文档
	 * @param id
	 */
	public String delectById(Long id) {
		String articleId = articleRepository.delectById(id) ;
		return articleId ;
	}
	
	/**
	 * 根据ids删除文档
	 */
	public Boolean delectByIds(List<String> ids){
		boolean delectByIds = articleRepository.delectByIds(ids) ;
		
		return delectByIds ;
	}
	
	/**
	 * 获取本类Class实例
	 */
	public Class<ArticleServiceImpl> getClazz(){
		Class<ArticleServiceImpl> clazz = ArticleServiceImpl.class ;
		return clazz ;
	}
	
}
