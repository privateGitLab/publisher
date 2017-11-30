package com.yiche.publish.controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yiche.publish.entity.Article;
import com.yiche.publish.entity.Page;
import com.yiche.publish.service.ArticleService;
import com.yiche.publish.utile.GetFullMethodName;

/**
 * 
* Copyright: Copyright (c) 2017 LanRu-Caifu
* 
* @ClassName: ArticleControllerImpl
* @Description: 控制层，用于与前后台交互 
*
* @version: v1.0.0
* @author: wangyingtong
* @date: 2017年11月21日 下午2:22:25 
* 
* Modification History:
* Date         Author          Version        Description
*---------------------------------------------------------*
* 2017年11月21日   wangyingtong        v1.0.0          修改原因
 */
@Controller
@RequestMapping("/conterController")
public class ArticleControllerImpl {

	Logger logger = LoggerFactory.getLogger(ArticleControllerImpl.class) ;
	
	@Autowired
	ArticleService articleService;

	/**
	 * 插入或编辑文档
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addOrEditArticle/blog/article")
	@ResponseBody
	public ResponseEntity<String> addOrEditArticle(Article article) {
		return new ResponseEntity<String>(articleService.addArticle(article), HttpStatus.OK);
	}

	/**
	 * 准备数据模型
	 */
	/*@ManagedAttribute
	public void getArticle(Map<String, Article> map, @RequestParam(value = "id", required = false) Integer id) {
		if (id != null) {
			map.put("article", articleService.singleTerm((long) id).get(0));
			System.out.println(articleService.singleTerm((long) id));
		}
	}*/

	/**************************************** 删除数据 ********************************************************/

	/**
	 * 根据文档id删除一条数据
	 */
	@RequestMapping(value = "/delectById/blog/article")
	@ResponseBody
	public ResponseEntity delectById(Long id) {
		String articleId = articleService.delectById(id);
		
		return new ResponseEntity<>(articleId, HttpStatus.OK) ;
	}

	/**
	 * 根据文档ids删除多条数据
	 */
	@RequestMapping(value = "/delectByIds/blog/article")
	@ResponseBody
	public Boolean delectByIds(String[] ids) {
		List<String> asList = Arrays.asList(ids);
		Boolean delectByIds = articleService.delectByIds(asList);
		return delectByIds;
	}

	/*************************************** 查询数据 **********************************************/
	/**
	 * 将文档全部查出来
	 * 
	 * @return
	 */
	@RequestMapping(value = "/findAllArticle/blog/article",method=RequestMethod.GET)
	public String findAllArticle(Map<String, Object> map,Page<Article> pageable) {
		Boolean indexExists = articleService.indexExists();
		System.out.println(indexExists);
		if (!indexExists) {
			articleService.createIndex();
		}
		map.put("page",articleService.findAllArticle(pageable));
		//ResponseEntity<Page<Article>> responseEntity = new ResponseEntity<>(articleService.findAllArticle(pageable), HttpStatus.OK) ;
		return "home";
	}

	/**
	 * 添加和编辑：根据id查出文档
	 */
	@RequestMapping(value="/findById/blog/article",method=RequestMethod.GET)
	public String findById(Map<String, Article> map, @RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			List<Article> singleTerm = articleService.singleTerm(id);
			map.put("article", singleTerm.get(0));
		}
		return "edit";
	}

	/**
	 * 查看根据id查看文档
	 */
	@RequestMapping(value="/queryById/blog/article",method=RequestMethod.GET)
	public String queryById(Map<String, Article> map, @RequestParam(value = "id", required = false) Long id) {
		if (id != null) {
			List<Article> singleTerm = articleService.singleTerm(id);
			map.put("article", singleTerm.get(0));
		}
		return "query";
	}

	/**
	 * 按照时间区间进行查询
	 */
	@RequestMapping(value="/findByPropertyLteGle/blog/article",method=RequestMethod.GET)
	public String findByPropertyLteGle(Map<String, Object> map, String gte ,String lte,Page<Article> page,HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		String filedNameGte = null;
		String filedNameLte = null;
		// 需要抛异常
		try {
			filedNameGte = "".equals(gte) || gte == null ? null :java.net.URLDecoder.decode(gte, "UTF-8");
			filedNameLte = "".equals(lte) || lte == null ? null : java.net.URLDecoder.decode(lte, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("在" + GetFullMethodName.getFullMethodName(getClazz(), "findByPropertyLteGle",Map.class,String.class,String.class,Page.class) + "方法中URLDecode出现异常！！！");
			e.printStackTrace();
		}
		Page<Article> pagePteGle = articleService.findByPropertyLteGle(filedNameGte, filedNameLte, page);
		map.put("page", pagePteGle) ;
		//保存到session域中，使之成为会员级别的变量
		session.setAttribute("gte", filedNameGte);
		session.setAttribute("lte", filedNameLte);
//		map.put("gte",filedNameGte) ;
//		map.put("lte",filedNameLte) ;
		return "postTime_search";
	}

	/**
	 * 按照输入的字符串进行全文搜索
	 */
	@RequestMapping(value="/findByWordAllOrderByProperty/blog/article",method=RequestMethod.GET)
	public String findByWordAllOrderByProperty(Map<String, Object> map, String word, Page<Article> page,HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		String filedName = null;
		// 需要抛异常
		try {
			filedName = "".equals(word) || word == null ? "" :java.net.URLDecoder.decode(word, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("在" + GetFullMethodName.getFullMethodName(getClazz(), "findByWordAllOrderByProperty",Map.class,String.class,Integer.class) + "方法中URLDecode出现异常！！！");
			e.printStackTrace();
		}
		Page<Article> pageWord = articleService.findByWordAllOrderByProperty(filedName, page);
		map.put("page",pageWord);
		//保存到session域中，使之成为会员级别的变量
		session.setAttribute("word", filedName); 
		//map.put("word", filedName);
		return "full_text_search";
	}

	/**
	 * 按作者和标题进行完全匹配搜索
	 */
	@RequestMapping(value="/findContain/blog/article",method=RequestMethod.GET)
	public String findContain(Map<String, Object> map, String filed, Page<Article> page,HttpServletRequest request) {
		HttpSession session = request.getSession() ;
		String filedName = null;
		// 需要抛异常
		try {
			filedName = java.net.URLDecoder.decode(filed, "UTF-8");
			filedName = "".equals(filed) || filed == null ? "" :java.net.URLDecoder.decode(filed, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Page<Article> findContain = articleService.findContain(filedName, page);
		
		map.put("page", findContain) ;
		//保存到session域中，使之成为会员级别的变量
		session.setAttribute("filed", filedName);
//		map.put("filed", filedName);
		return "contain_matching_search";
	}

	//////////////////////////////////////////////////////////////////////////////////
	/**
	 * 根据条件查询数据集合
	 */
//	@RequestMapping(value = "/getArticleByProperty/blog/article", method = RequestMethod.POST)
//	@ResponseBody
//	// public ResponseEntity<List<Article>> getArticleByProperty(Article
	// article) {
	// return new
	// ResponseEntity<List<Article>>(articleService.getArticleByProperty(article),
	// HttpStatus.OK);
	// }

	/*
	 * 按照某个字段进行排序查询(具体字段根据业务而定)
	 */
	// @RequestMapping(value="/findByAvailableTrueOrderByTitleDesc()/blog/article",method=RequestMethod.GET)
	// @ResponseBody
	// public List<Article> findByAvailableTrueOrderByTitleDesc() {
	// return articleService.findByAvailableTrueOrderByTitleDesc();
	// }

	/*
	 * 某个字段的模糊查询(具体字段根据业务而定)
	 */
	// @RequestMapping(value="/findByTitleLike/blog/article",method=RequestMethod.POST)
	// @ResponseBody
	// public ResponseEntity<List<Article>> findByTitleLike(Article title) {
	// return new
	// ResponseEntity<List<Article>>(articleService.findByTitleLike(title),
	// HttpStatus.OK);
	// }
	
	public Class<ArticleControllerImpl> getClazz(){
		
		Class<ArticleControllerImpl> clazz = ArticleControllerImpl.class ;
		
		return clazz ;
	}
}
