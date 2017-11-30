package com.yiche.publish.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import com.yiche.publish.dao.ArticleRepository;
import com.yiche.publish.entity.Article;
import com.yiche.publish.utile.FinalUtile;
import com.yiche.publish.utile.GetFullMethodName;
import com.yiche.publish.utile.TimeFormattingUtile;
import com.yiche.publish.utile.UeflexUtile;

/**
 * 
* Copyright: Copyright (c) 2017 LanRu-Caifu
* 
* @ClassName: ArticleRepositoryImpl
* @Description: dao层用于与数据库或其他存放数据的DB(ElasticSearch)进行交互
*
* @version: v1.0.0
* @author: wangyingtong
* @date: 2017年11月17日 下午3:00:50 
* 
* Modification History:
* Date         Author          Version        Description
*---------------------------------------------------------*
* 2017年11月17日   wangyingtong        v1.0.0          修改原因
 */
@Repository
public class ArticleRepositoryImpl implements ArticleRepository{

	Logger logger = LoggerFactory.getLogger(ArticleRepositoryImpl.class);

	public static final Boolean change_status_true = true ;
	public static final Boolean change_status_false = false ;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	/**
	 * 单字段对某字符串模糊查询，并根据发布时间按倒叙进行排序
	 * pageable：分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 */
	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#getArticleByProperty(com.yiche.publish.entity.Article, org.springframework.data.domain.Pageable)  
	 *  
	 * @Title: getArticleByProperty
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 *
	 * @param article
	 * @param pageable
	 * @return
	 * @throws：@param article
	 * @throws：@param pageable
	 * @throws：@return 异常描述
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:36:29 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> getArticleByProperty(Article article,
			@PageableDefault(sort = FinalUtile.POST_TIME, direction = Sort.Direction.DESC) Pageable pageable) {
		QueryBuilder queryBuilder = null;
		// 如果queryBuilder为空，则全部查出来
		SearchQuery nativeSearchQuery = null ;
		
		List<Article> queryForList = null ;
		try {
			if (article.getAuthor() != null) {
				queryBuilder = new MatchQueryBuilder(FinalUtile.AUTHOR, article.getAuthor());
			} else if (article.getClickCount() != null) {
				queryBuilder = new MatchQueryBuilder(FinalUtile.CLICK_COUNT, article.getClickCount());
			} else if (article.getContent() != null) {
				queryBuilder = new MatchQueryBuilder(FinalUtile.CONTEN, article.getContent());
			} else if (article.getPostTime() != null) {
				queryBuilder = new MatchQueryBuilder(FinalUtile.POST_TIME, article.getPostTime());
			} else if (article.getTitle() != null) {
				queryBuilder = new MatchQueryBuilder(FinalUtile.TITLE, article.getTitle());
			}

			nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
											.withSort(new FieldSortBuilder(FinalUtile.POST_TIME).order(SortOrder.DESC)).build();
			queryForList = elasticsearchTemplate.queryForList(nativeSearchQuery, Article.class);
			logger.info("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(), "getArticleByProperty",Article.class,Pageable.class) + " <=== 方法中，单字段对某字符串模糊查询成功！！！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(), "getArticleByProperty",Article.class,Pageable.class) + " <=== 方法中，单字段对某字符串模糊查询失败！！！ERROR：",e);
		}

		return queryForList ;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#singlePhraseMatch(com.yiche.publish.entity.Article, org.springframework.data.domain.Pageable)  
	 *  
	 * @Title: singlePhraseMatch
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 单字段对某短语进行匹配查询，短语分词的顺序会影响结果，并按发布时间倒序排序
	 *
	 * @param article：文档类
	 * @param pageable：查询限制条件，分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * @return
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:33:43 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> singlePhraseMatch(Article article,
			@PageableDefault(sort =FinalUtile.POST_TIME , direction = Sort.Direction.DESC) Pageable pageable) {

		QueryBuilder queryBuilder = null;

		SearchQuery searchQuery = null ;
		
		List<Article> articles = null ;
		try {
			if (article.getAuthor() != null) {
				queryBuilder = new MatchPhraseQueryBuilder(FinalUtile.AUTHOR, article.getAuthor()).slop(2);
			} else if (article.getClickCount() != null) {
				queryBuilder = new MatchPhraseQueryBuilder(FinalUtile.CLICK_COUNT, article.getClickCount()).slop(2);
			} else if (article.getContent() != null) {
				queryBuilder = new MatchPhraseQueryBuilder(FinalUtile.CONTEN, article.getContent()).slop(2);
			} else if (article.getPostTime() != null) {
				queryBuilder = new MatchPhraseQueryBuilder(FinalUtile.POST_TIME, article.getPostTime()).slop(2);
			} else if (article.getTitle() != null) {
				queryBuilder = new MatchPhraseQueryBuilder(FinalUtile.TITLE, article.getTitle()).slop(2);
			}
			searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
									  .withSort(new FieldSortBuilder(FinalUtile.POST_TIME).order(SortOrder.DESC)).build();
			articles = elasticsearchTemplate.queryForList(searchQuery, getEntityClass());
			logger.info("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(), "singlePhraseMatch",Article.class,Pageable.class) + " <=== 方法中，单字段对某短语进行匹配查询成功！！！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(), "singlePhraseMatch",Article.class,Pageable.class) + " <=== 方法中，单字段对某短语进行匹配查询失败！！！ERROR：",e);
		}
		return articles ;
	}

	/*****************************************************************************************************************/
	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findByWordAllOrderByProperty(java.lang.String, org.springframework.data.domain.Pageable)  
	 *  
	 * @Title: findByWordAllOrderByProperty
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 单字符串模糊查询，全文搜索，，，，，单字段排序。默认使用发布时间倒叙排序：postTime
	 *
	 * @param word ：按此字符串进行全文搜索
	 * @param pageable：查询限制条件，分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * @return
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:32:49 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> findByWordAllOrderByProperty(String word,
			@PageableDefault(sort = FinalUtile.POST_TIME, direction = Sort.Direction.DESC) Pageable pageable) {

		List<Article> queryForList = null ;
		try {
			// 使用queryStringQuery完成单字符串查询
			QueryBuilder queryBuilder = new QueryStringQueryBuilder(word);
			SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
									  .withSort(new FieldSortBuilder(FinalUtile.POST_TIME).order(SortOrder.DESC)).withHighlightFields(new HighlightBuilder.Field(FinalUtile.CONTEN).preTags("<em>").postTags("</em>").fragmentSize(250)).build() ;
			queryForList = elasticsearchTemplate.queryForList(searchQuery, Article.class);
			logger.info("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(),"findByWordAllOrderByProperty",String.class,Pageable.class) + " <=== 方法中，按字符串" + word + "全文检索成功!!!");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(),"findByWordAllOrderByProperty",String.class,Pageable.class) + " <=== 方法中，按字符串" + word + "全文检索出现错误，ERROR：", e);
		}
		return queryForList ;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findAllWordCount(java.lang.String)  
	 *  
	 * @Title: findAllWordCount
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 字符串全文搜索查处的总条数
	 *
	 * @param word：按此字符串进行全文搜索
	 * @return 返回符合条件的结果集
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:31:51 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public long findAllWordCount(String word) {
		SearchQuery searchQuery = null;
		QueryBuilder queryBuilder = null;

		long count = 0;
		try {
			queryBuilder = new QueryStringQueryBuilder(word);
			searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withSort(new FieldSortBuilder("postTime").order(SortOrder.DESC)).build();
			count = elasticsearchTemplate.count(searchQuery, getEntityClass()) ;
			logger.info("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(), "findAllWordCount",String.class) + " <=== 方法中，按字符串 word = " + word + "全文本检索查询总记录数成功！！！一共" + count + "条数据");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在 ===> " + GetFullMethodName.getFullMethodName(getClazz(), "findAllWordCount",String.class) + " <=== 方法中，按字符串 word = " + word + "全文本检索查询总记录数失败！！！ERROR：", e);
		}
		return count;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#singleTerm(java.lang.Long)  
	 *  
	 * @Title: singleTerm
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO term匹配，即不分词匹配，你传来什么值就会拿你传的值去做完全匹配
	 *
	 * @param id：按此ArticleId进行查询文档
	 * @return 返回符合条件的文档
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:30:41 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> singleTerm(Long id) {
		QueryBuilder queryBuilder = null;
		SearchQuery searchQuery = null;
		List<Article> queryForList = null ;
		try {
			queryBuilder = new TermQueryBuilder(FinalUtile.ID, id);
			// 不对传来的值分词，去找完全匹配的
			searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
			queryForList = elasticsearchTemplate.queryForList(searchQuery, getEntityClass());
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "singleTerm",Long.class) + "<===方法中，根据ArticleID: " + id + " 查询出现成功！！！");
		} catch (Exception e) {
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "singleTerm",Long.class) + "<===方法中，根据ArticleID: " + id + "  查询出现错误，ERROR：", e);
		}
		return queryForList ;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findContain(java.lang.String, org.springframework.data.domain.Pageable)  
	 *  
	 * @Title: findContain
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO按作者和标题进行完全匹配搜索，并按发布时间进行倒序排序
	 *
	 * @param filed：按此字段进行查询
	 * @param pageable：查询限制条件，分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * @return 返回符合条件的文档结果集
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:27:07 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> findContain(String filed,
			@PageableDefault(sort = FinalUtile.POST_TIME, direction = Sort.Direction.DESC) Pageable pageable) {

		QueryBuilder queryBuilder = null;
		SearchQuery searchQuery = null;
		
		List<Article> queryForList = null ;
		try {
			queryBuilder = new MultiMatchQueryBuilder(filed, FinalUtile.TITLE,FinalUtile.AUTHOR).operator(Operator.AND);
			searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable).withSort(new FieldSortBuilder("postTime").order(SortOrder.DESC)).withHighlightFields(new HighlightBuilder.Field(FinalUtile.AUTHOR).preTags("<span style=\"color:red\">").postTags("</span>").fragmentSize(250)).build();
			queryForList = elasticsearchTemplate.queryForList(searchQuery, getEntityClass());
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findContain",String.class,Pageable.class) + "<===方法中，按字符串  filed=" + filed + ",对作者和标题字段进行完全匹配搜索成功！！！");
		} catch (Exception e) {
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findContain",String.class,Pageable.class) + "<===方法中，按字符串  filed=" + filed + ",对作者和标题字段进行完全匹配搜索出现错误，ERROR：", e);
		}
		return queryForList ;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findByTitleAndAuthorCount(java.lang.String)  
	 *  
	 * @Title: findByTitleAndAuthorCount
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO按作者和标题进行完全匹配搜索查询的条数///////////可以将下面查询放在搜索结果集方法中
	 *
	 * @param filed：按此字段进行查询
	 * @return：返回查询出来的文档个数
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:25:47 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public long findByTitleAndAuthorCount(String filed) {
		SearchQuery searchQuery = null;
		QueryBuilder queryBuilder = null;
		long count = 0;
		try {
			queryBuilder = new MultiMatchQueryBuilder(filed, FinalUtile.TITLE,FinalUtile.AUTHOR).operator(Operator.AND);
			searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
			count = elasticsearchTemplate.count(searchQuery, getEntityClass()) ;
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findByTitleAndAuthorCount", String.class) +"<===方法中，按字符串  filed = " + filed + ",对作者和标题字段进行完全匹配查询记录数成功！！！一共" + count + "条数据");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findByTitleAndAuthorCount", String.class) +"<===方法中，按字符串  filed = " + filed + ",对作者和标题字段进行完全匹配查询记录数失败！！！ERROR：", e);
		}
		return count;
	}

	/**
	 * 范围查询(按发表时间进行查询)
	 */
	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findByPropertyLteGle(java.lang.Long, java.lang.Long, org.springframework.data.domain.Pageable)  
	 *  
	 * @Title: findByPropertyLteGle
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO查询出该时间段内所有文档
	 *
	 * @param gte：查询的初试时间
	 * @param lte：查询的结束时间
	 * @param pageable 查询限制条件，分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * @return 返回该时间段内的文档集合
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:23:13 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> findByPropertyLteGle(Long gte, Long lte,
			@PageableDefault(sort = FinalUtile.POST_TIME, direction = Sort.Direction.DESC) Pageable pageable) {

		QueryBuilder queryBuilder = null;
		List<Article> queryForList = null ;
		try {
			queryBuilder = new RangeQueryBuilder("postTime").gte(gte).lte(lte);

			SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(pageable)
									.withSort(new FieldSortBuilder(FinalUtile.POST_TIME).order(SortOrder.DESC)).build();
			queryForList = elasticsearchTemplate.queryForList(searchQuery, getEntityClass());
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findByPropertyLteGle",Long.class,Long.class,Pageable.class) +"<===方法中，按时间进行查找成功！！！");
		} catch (Exception e) {
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findByPropertyLteGle",Long.class,Long.class,Pageable.class) +"<===方法中，按时间进行查找失败！！！", e);
		}
		return queryForList ;
	}

	/*
	 * public long findByTimeCount(){ SearchQuery searchQuery = null ;
	 * QueryBuilder queryBuilder = null ;
	 * 
	 * queryBuilder = new RangeQueryBuilder("").gte(from) }
	 */
	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findByTimeCount(java.lang.Long, java.lang.Long)  
	 *  
	 * @Title: findByTimeCount
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO在该时间段内文档的总个数
	 *
	 * @param gte：初试时间
	 * @param lte：结束时间
	 * @return 返回该时间段内的文档数
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月17日 下午3:00:09 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月17日     wangyingtong      v1.0.0              修改原因
	 */
	public Long findByTimeCount(Long gte, Long lte) {
		
		String toTimeFormat = (gte == null) ? "1970:00:00" : TimeFormattingUtile.getLongToTimeFormat(gte) ;
		String timeFormat = (lte == null) ? "最晚时间" : TimeFormattingUtile.getLongToTimeFormat(lte) ;
		
		SearchQuery searchQuery = null;
		QueryBuilder queryBuilder = null;
		long count = 0;
		try {
			queryBuilder = new RangeQueryBuilder(FinalUtile.POST_TIME).gte(gte).lte(lte) ;
			searchQuery = new NativeSearchQuery(queryBuilder);
			count = elasticsearchTemplate.count(searchQuery, getEntityClass()) ;
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findByTimeCount", Long.class,Long.class) + "<===方法中，在 初试时间：" + toTimeFormat+"----结束时间：" + timeFormat + "时间内查到 " + count + " 条数据");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "findByTimeCount", Long.class,Long.class) + "方法中<===，在 初试时间：" + toTimeFormat +"----结束时间：" + timeFormat + "时间内总文档数失败！！！ERROR：", e);
		}
		return count;
	}
	
	

	/**
	 * 将索引中的文档按指定条数全部查出来,并按发布时间倒序排序
	 * pageable：分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * 
	 * @throws Exception
	 */
	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#getAllArticle(org.springframework.data.domain.Pageable)  
	 *  
	 * @Title: getAllArticle
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 将索引中的文档按指定条数全部查出来,并按发布时间倒序排序
	 *
	 * @param pageable 查询限制条件，分页对象()可以通过PageRequest.of(args...)进行实例化，参数传当前页和每页多少条数据等
	 * @return 返回当前索引中所有的文档
	 * @throws Exception 抛出异常
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:38:13 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> getAllArticle(@PageableDefault(sort = {
			FinalUtile.POST_TIME }, direction = Sort.Direction.DESC) Pageable pageable) throws Exception {
		List<Article> articleList = null;
		//pageable = PageRequest.of(pageable.getPageNumber(), 10, Direction.DESC, "id");
		try {
			QueryBuilder queryBuilder = new MatchAllQueryBuilder();
			Long count = getCount() ;
			// 如果queryBuilder为空，则全部查出来
			SearchQuery nativeSearchQuery =  null ;
			//如果count为0，说明当前索引中在此查询条件下没有相关文档，不能进行排序，否则会报错
			nativeSearchQuery = (count == 0) ? new NativeSearchQueryBuilder().withQuery(queryBuilder)
					.withPageable(pageable).build() : new NativeSearchQueryBuilder().withQuery(queryBuilder)
					.withPageable(pageable).withSort(new FieldSortBuilder(FinalUtile.POST_TIME).order(SortOrder.DESC)).build() ;
			//withSort(new FieldSortBuilder("postTime").order(SortOrder.DESC))
			articleList = elasticsearchTemplate.queryForList(nativeSearchQuery,getEntityClass()) ;
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "getAllArticle", Pageable.class) + "<=== 方法中查询全部文档时成功！！！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(), "getAllArticle", Pageable.class) + "<=== 方法中查询全部文档时出现错误，ERROR:", e);
		}
		return articleList;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#getCount()  
	 *  
	 * @Title: getCount
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 查询当前索引中一共有多少个文档(可以根据条件聚合查询)
	 *
	 * @return 返回当前索引中文档的总数
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:41:29 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public Long getCount() {

		SearchQuery searchQuery = null;
		QueryBuilder queryBuilder = null;
		Long count = null ;
		try {
			queryBuilder = new MatchAllQueryBuilder();
			searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
			count = elasticsearchTemplate.count(searchQuery, getEntityClass());
			logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(),"getCount",null) + " <=== 方法中聚合查询成功！！！一共" + count + "条数据");
		} catch (Exception e) {
			logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(),"getCount",null) + " <=== 方法中聚合查询出现错误，ERROR：", e);
		}
		return count ;
	}

	/**
	 * 另一种查询总数的方式（需要单独指定索引名称）
	 */
	/*
	 * public Long getCount() {
	 * 
	 * NativeSearchQueryBuilder searchQuery = null ; BoolQueryBuilder
	 * queryBuilderb = null ; try{ searchQuery = new NativeSearchQueryBuilder();
	 * queryBuilderb = QueryBuilders.boolQuery();
	 * //bqb.must(QueryBuilders.termQuery("id","74247991186710"));
	 * //searchQuery.withIndices(objectIndex).withQuery(queryBuilderb);
	 * searchQuery.withIndices("*").withQuery(queryBuilderb); return
	 * elasticsearchTemplate.count(searchQuery.build()); }catch(Exception e){
	 * logger.error("聚合查询出现错误，ERROR：",e); return null ; } }
	 */

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#findByTitleOrderByIdDesc()  
	 *  
	 * @Title: findByTitleOrderByIdDesc
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 按照某个字段进行排序(具体字段根据业务进行排序)
	 *
	 * @return 返回按某个字段进行排序后的文档结果集
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:43:20 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public List<Article> findByTitleOrderByIdDesc() {
		return null;
	}

	/********************************************** 查询到此结束 ********************************************/
	/**
	 * 
	 * 
	 * @param article
	 * @return
	 */
	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#save(com.yiche.publish.entity.Article)  
	 *  
	 * @Title: save
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 添加数据和更新
	 *
	 * @param article：封装的文档类
	 * @return 返回插入或更新文档的ID
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:46:27 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public String save(Article article) {
		elasticsearchTemplate.refresh(getEntityClass());
		Long id = article.getId();
		if (id == null) {
			long articleId = getArticleId();
			// 如果没有设置id值则设置为当前时间戳
			// long time = new Date().getTime() ;
			article.setId(articleId);
		}
		try {
			IndexQuery indexQuery = new IndexQueryBuilder().withId(article.getId() + "").withObject(article).build();
			String index = elasticsearchTemplate.index(indexQuery);
			if (id == null) {
				logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "save",Article.class) + " <=== 方法中，插入数据成功，插入的文档ID为：" + index);
			} else {
				logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "save",Article.class) + " <=== 方法中，更改数据成功，更改的文档ID为：" + index);
			}
			//当文档更新时，ElasticSearch不会马上更新数据，有默认的刷新时间(默认是1秒)。
			//如果对文档进行编辑后马上查询数据，查询的换是编辑之前的数据，直观的解决方法是在更新后让当前线程睡眠一秒以等待ElasticSearch刷新数据
			//也可以通过更改配置文件或通过api代码进行设置让ES再编辑后马上更新
			Thread.sleep(1000l);
			
			return index;
		} catch (Exception e) {
			if (id == null) {
				logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "save",Article.class) + " <=== 方法中，插入数据出现错误", e);
			}else{
				logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "save",Article.class) + " <=== 方法中，修改数据出现错误",e);
			}
			// 如果插入或修改出现错误则返回null
			return null;
		}
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#delectById(java.lang.Long)  
	 *  
	 * @Title: delectById
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 根据单个id删除数据
	 *
	 * @param id：要删除文档的ID
	 * @return 返回已成功删除文档的ID
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:48:47 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public String delectById(Long id) {
		//elasticsearchTemplate.refresh(getEntityClass());
		//BulkRequestBuilder bulkRequestBuilder = elasticsearchTemplate.getClient().prepareBulk().setRefreshPolicy(RefreshPolicy.IMMEDIATE) ;
		
		String delete = null ;
		try {
			// 删除数据id
			delete = elasticsearchTemplate.delete(getEntityClass(), id + "");
			//bulkRequestBuilder.
			logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "delectById",Long.class) +  " <=== 方法中，删除 " + getEntityClass() + " id是： " + id + "的文档成功！！！");
			
			//当文档更新时，ElasticSearch不会马上更新数据，有默认的刷新时间(默认是1秒)。
			//如果对文档进行编辑后马上查询数据，查询的换是编辑之前的数据，直观的解决方法是在更新后让当前线程睡眠一秒以等待ElasticSearch刷新数据
			//也可以通过更改配置文件或通过api代码进行设置让ES再编辑后马上更新
			Thread.sleep(1000l);
		} catch (Exception e) {
			// 如果删除失败则打印错误信息并返回null
			logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "delectById",Long.class) +  " <=== 方法中，删除 " + getEntityClass() + " id是： " + id + " error.", e);
		}
		return delete ;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#delectByIds(java.util.List)  
	 *  
	 * @Title: delectByIds
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 根据ids(ids为文档id集合)删除Article类型的数据
	 *
	 * @param ids：要删除的文档的id集合
	 * @return 返回是否删除成功(true：成功，false：失败)
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:50:16 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public boolean delectByIds(List<String> ids) {
		elasticsearchTemplate.refresh(getEntityClass()); 
		try {
			Criteria criteria = new Criteria(FinalUtile.ID) ;
			
			criteria = criteria.in(ids.iterator()) ;
			
			CriteriaQuery criteriaQuery = new CriteriaQuery(criteria) ;
			//SearchQuery searchQuery = new NativeSearchQueryBuilder().build() ;
			
			elasticsearchTemplate.delete(criteriaQuery, getEntityClass());
			logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "delectByIds",List.class) + " <=== 方法中，删除" + getEntityClass() + " id集合 " + ids.toString() + "成功");
			// 删除成功返回true
			return change_status_true;
		} catch (Exception e) {
			// 删除失败打印错误信息并返回false
			logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "delectByIds",List.class) + " <=== 方法中，删除" + getEntityClass() + " id集合 " + ids.toString() + "出现错误， error.", e);
			return change_status_false;
		}
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#indexExists()  
	 *  
	 * @Title: indexExists
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 查询指定索引是否存在
	 *
	 * @return 返回索引是否存在(true：存在，false：不存在)
	 *
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:51:26 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public Boolean indexExists() {
		Boolean indexExists = null;
		/*try {
			new UeflexUtile<Article>().setField(getEntityClass().newInstance(), Document.class, "indexName", TimeFormattingUtile.getTimeFormat(new Date()));
		} catch (Exception e1) {
			e1.printStackTrace();
		}*/
		try {
			System.out.println(new UeflexUtile<>().getObjectAnnotationName(getEntityClass().newInstance()));
			indexExists = elasticsearchTemplate.indexExists(getEntityClass());
			if (!indexExists) {
				logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "indexExists",null) + " <=== 方法中，查询当前没有可用索引！");
			}else{
				logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "indexExists",null) + " <=== 方法中，查当前索引为："+ new UeflexUtile<>().getObjectAnnotationName(new Article())) ;
			}
		} catch (Exception e) {
			logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "indexExists",null) + " <=== 方法中，查询" + Article.class + "索引是否存在出现异常  error：", e);
		}

		return indexExists;
	}

	/**
	 * 
	 * @see com.yiche.publish.dao.ArticleRepository#createIndex()  
	 *  
	 * @Title: createIndex
	 * @Function: ArticleRepositoryImpl.java
	 * @Description: TODO 根据指定类型创建索引
	 *
	 * @retur 返回创建索引是否成功(true：成功，false：不成功)	
	 * @version: v1.0.0
	 * @author: wangyingtong
	 * @date: 2017年11月19日 下午9:53:00 
	 *
	 *
	 * Modification History:
	 * Date         Author          Version            Description
	 *---------------------------------------------------------*
	 * 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public Boolean createIndex() {
		//elasticsearchTemplate.refresh(getEntityClass());
		Boolean createIndex = null;
		try {
			System.out.println(new UeflexUtile<>().getObjectAnnotationName(getEntityClass().newInstance()));
			createIndex = elasticsearchTemplate.createIndex(getEntityClass());
			if (createIndex) {
				logger.info("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "createIndex",null)+ " <=== 方法中，创建索引成功！！！");
			}
			
			//当文档更新时，ElasticSearch不会马上更新数据，有默认的刷新时间(默认是1秒)。
			//如果对文档进行编辑后马上查询数据，查询的换是编辑之前的数据，直观的解决方法是在更新后让当前线程睡眠一秒以等待ElasticSearch刷新数据
			//也可以通过更改配置文件或通过api代码进行设置让ES再编辑后马上更新
			Thread.sleep(1000l);
		} catch (Exception e) {
			logger.error("在===> " + GetFullMethodName.getFullMethodName(getClazz(), "createIndex",null)+ " <=== 方法中，创建" + Article.class + "索引出现异常 error：", e);
		}
		return createIndex;
	}

	/**
	 * 
	* @Title: getEntityClass
	* @Function: ArticleRepositoryImpl.java
	* @Description: TODO 获取Articel的Class
	*
	* @return Class<Article> 返回指定类型的Class实例
	*
	* @version: v1.0.0
	* @author: wangyingtong
	* @date: 2017年11月19日 下午9:54:01 
	*
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public Class<Article> getEntityClass() {
		return Article.class;
	}

	/**
	 * 
	* @Title: getArticleId
	* @Function: ArticleRepositoryImpl.java
	* @Description: TODO 当没有指定文档ID时，用于获取ArticeID
	*
	* @return Long ArticleId
	*
	* @version: v1.0.0
	* @author: wangyingtong
	* @date: 2017年11月19日 下午9:54:51 
	*
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2017年11月19日     wangyingtong      v1.0.0              修改原因
	 */
	public Long getArticleId() {
		UUID randomUUID = UUID.randomUUID();
		String UUID_MATH = randomUUID.toString().replaceAll("[-a-zA-Z]", "").substring(0, 8)
				+ String.valueOf((Math.random() * 9 + 1) * 1000000).substring(0, 6);
		return Long.valueOf(UUID_MATH);
	}
	
	/**
	 * 
	* @Title: specifiedIndex
	* @Function: ArticleRepositoryImpl.java
	* @Description: 更改要查询的索引
	*
	* @param 
	* @return 是否更改成功
	*
	* @version: v1.0.0
	* @author: wangyingtong
	* @date: 2017年11月28日 上午10:06:54 
	*
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2017年11月28日     wangyingtong      v1.0.0              修改原因
	 */
	public Boolean specifiedIndex(String indexNameValue) {
		try {
			new UeflexUtile<>().setField(getEntityClass(), Document.class,"indexName",indexNameValue);
			logger.info("在===>" + GetFullMethodName.getFullMethodName(getClazz(),"specifiedIndex", String.class) + "<===方法中更改查询索引成功，索引值为:" + indexNameValue);
			return change_status_true ;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("在===>" + GetFullMethodName.getFullMethodName(getClazz(),"specifiedIndex",String.class) + "<===方法中更改查询索引失败，错误：",e);
		}
		return change_status_false ;
	} 
	
	/**
	 * 获取本类中Class
	 */
	public Class<ArticleRepositoryImpl> getClazz(){
		Class<ArticleRepositoryImpl> clazz = ArticleRepositoryImpl.class ;
		return clazz ;
	}
}