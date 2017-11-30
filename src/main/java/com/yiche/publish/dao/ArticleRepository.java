package com.yiche.publish.dao;

import java.util.Date;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;

import com.yiche.publish.entity.Article;
import com.yiche.publish.utile.TimeFormattingUtile;

public interface ArticleRepository {

	/**
	 * 单字段对某字符串模糊查询
	 * pageable：分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 */
	public List<Article> getArticleByProperty(Article article, @PageableDefault Pageable pageable);

	/**
	 * 单字符串模糊查询，单字段排序。默认使用时间进行排序：postTime
	 */
	public List<Article> findByWordAllOrderByProperty(String word,
			@PageableDefault(sort = "postTime", direction = Sort.Direction.DESC) Pageable pageable);

	/**
	 * 字符串全文搜索查处的总条数
	 */
	public long findAllWordCount(String word) ;
	
	/**
	 * 单字段对某短语进行匹配查询，短语分词的顺序会影响结果
	 */
	public List<Article> singlePhraseMatch(Article article, @PageableDefault Pageable pageable);

	
    /** 
     * term匹配，即不分词匹配，你传来什么值就会拿你传的值去做完全匹配 
     */  
    public List<Article> singleTerm(Long id);  
    
    
    /** 
     * 按作者和标题进行完全匹配搜索
     */  
    public List<Article> findContain(String filed, @PageableDefault(sort = "postTime", direction = Sort.Direction.DESC) Pageable pageable);  
	
    /**
	 * 按作者和标题进行完全匹配搜索查询的条数///////////可以将下面查询放在搜索结果集方法中
	 */
	public long findByTitleAndAuthorCount(String filed) ;
    
	/**
	 * 范围查询(按发表时间进行查询)
	 */
	public List<Article> findByPropertyLteGle(Long lte,Long gte,@PageableDefault(sort = "postTime", direction = Sort.Direction.DESC) Pageable pageable);
	
	/**
	 * 按指定时间查询出总文档数
	 */
	public Long findByTimeCount(Long gte, Long lte);
	
	/**
	 * 单字段对某字符串模糊查询
	 * pageable：分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * @throws Exception 
	 */
	public List<Article> getAllArticle(@PageableDefault Pageable pageable) throws Exception;
	
	/**
	 * 查询总数
	 * @return
	 */
	public Long getCount();

	/*
	 * 按照某个字段进行排序(具体字段根据业务而定)
	 */
	public List<Article> findByTitleOrderByIdDesc();

	/********************************************** 查询到此结束 ********************************************/
	/**
	 * 添加数据和更新
	 * 
	 * @param article
	 * @return
	 */
	public String save(Article article);

	/**
	 * 根据单个id删除数据
	 */
	public String delectById(Long id);

	/**
	 * 根据ids(ids为文档id集合)删除Article类型的数据
	 * 
	 * @param ids
	 * @return
	 */
	public boolean delectByIds(List<String> ids);

	
	/**
	 * 查询指定索引是否存在
	 */
	public Boolean indexExists();
	
	/**
	 * 根据指定类型创建索引
	 */
	public Boolean createIndex();

}
