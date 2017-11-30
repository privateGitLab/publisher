package com.yiche.publisher.test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import com.yiche.publish.dao.impl.ArticleRepositoryImpl;
import com.yiche.publish.entity.Article;
import com.yiche.publish.utile.GetFullMethodName;
import com.yiche.publish.utile.Material;
import com.yiche.publish.utile.MyException;
import com.yiche.publish.utile.TimeFormattingUtile;
import com.yiche.publish.utile.UeflexUtile;

import junit.framework.Assert;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:spring.xml")
public class ArticleTest {

	Logger logger = LoggerFactory.getLogger(ArticleTest.class) ;
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;

	@Autowired
	ArticleRepositoryImpl articleRepository;
	
	@Test
	public void test114() {
		try {
			String hostAddress = getLocalHostAddress().getHostAddress() ;
			System.out.println(hostAddress);
			
			String hostAddress2 = getLocalHostLANAddress().getHostAddress();
			
			System.out.println(hostAddress2);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static InetAddress getLocalHostAddress() throws UnknownHostException {
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();

                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                        if (ip != null && ip instanceof Inet4Address) {
                            return ip;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
        if (jdkSuppliedAddress == null) {
            throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
        }
        return jdkSuppliedAddress;
    }
	
	
	// 正确的IP拿法，即优先拿site-local地址
    private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }

	
	/*
	 * 测试异常类
	 */
	@Test
	public void test113() {
//		try {
			throw new MyException("gdfngdrg") ;
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(e.getMessage());
//		}
	}
	
	/*
	 * 测试uuid的重复性
	 */
	@Test
	public void test112() {
		int count = 0 ;
		Set<String> sets = new HashSet<>() ;
		for (int i = 0; i < 100000; i++) {
			String uuid = UUID.randomUUID().toString() ;
			//System.out.println(uuid);
			sets.add(uuid) ;
			count ++ ;
		}
		System.out.println(count);
		System.out.println(sets.size());
		
	}
	
	/*
	 *测试JUnit中的断言 
	 */
	@Test
	public void test111(){
//		Assume.assumeFalse(92 > 93);
//		Assume.assumeFalse(92 < 93);
		Assert.assertTrue(92 > 93);
//		org.junit.Assert.assertTrue(92 > 93);
	}
	
	/**
	 * 测试elasticsearch对事务的支持
	 */
	@Test
	public void test23(){
		
		
		//BulkRequestBuilder setRefreshPolicy = elasticsearchTemplate.getClient().prepareBulk().setRefreshPolicy(RefreshPolicy.IMMEDIATE) ;

		Map setting = elasticsearchTemplate.getSetting(Article.class) ;
		Set keySet = setting.keySet() ;
		for (Object object : keySet) {
			
			System.out.println(object + ":" + setting.get(object));
		}
		
	}
	
	@Test
	public void test21(){
		
		Class clazz = ArticleTest.class ;
		
		String fullMethodName = GetFullMethodName.getFullMethodName(clazz,"test21",null) ;
		System.out.println(fullMethodName);
	}
	
	@Test
	public void test22(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		try {
			Date parse = dateFormat.parse(null) ;
			String format = dateFormat.format(parse);
			System.out.println(format);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void test() {

		boolean createIndex = elasticsearchTemplate.createIndex(Article.class);

		String delete = elasticsearchTemplate.delete(Article.class, "1");
		System.out.println(delete);

		// System.out.println(articleEsRepository.createArticleIndex());
	}
	
	@Test
	public void test17() {

		Long count = articleRepository.getCount() ;
		System.out.println(count);
	}

	/**
	 * 测试
	 */
	@Test
	public void test13() {
		List<String> ids = null;
		try {
			ids = new ArrayList<String>();
			CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria());
			Criteria criteria = criteriaQuery.getCriteria();

			criteriaQuery.setIds(ids);
			elasticsearchTemplate.delete(criteriaQuery, Article.class);
			// 删除成功返回true
		} catch (Exception e) {
			// 删除失败打印错误信息并返回false
			logger.error("删除" + Article.class + " id集合 " + ids.toString() + " error.", e);
		}
	}

	/**
	 * 删除ids
	 */
	@Test
	public void test12() {
		List<String> ids = new ArrayList<>();
		ids.add("19416647516575");
		ids.add("75823587856649");
		boolean delectByIds = articleRepository.delectByIds(ids);
		System.out.println(delectByIds);
	}

	/**
	 * 插入测试数据
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void sava1() throws InterruptedException {
		
		for (int i = 0; i < Material.getContent().size(); i++) {
			List<String> author = Material.getAuthor() ;
			List<String> content = Material.getContent() ;
			List<String> title = Material.getTitle() ;
			
			Article article = new Article();
			article.setAuthor(author.get(i));
			article.setClickCount(1l);
			article.setContent(content.get(i));
			article.setTitle(title.get(i));
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"
			 +"HH:mm:ss");
			 String format = dateFormat.format(new Date());
			article.setPostTime(new Date());
			article.setWordNumber(11111l);

			try {
				new UeflexUtile<>().setField(article,Document.class,"indexName", TimeFormattingUtile.getTimeFormat(new Date()));
				String save = articleRepository.save(article);

				System.out.println(save);
				System.out.println("执行成功！");
			} catch (Exception e) {
				logger.error("插入或更新文档失败！！！ ERROR：", e);
			}

		}
	}
	
	/**
	 * 测试id范围查询
	 */
	@Test
	public void test20(){
		String gteStr = "47160444911407" ;
		String lteStr = "93170884925213" ;
		
		long gte = Long.valueOf(gteStr) ;
		long lte = Long.valueOf(lteStr) ;
		
		
		//List<Article> findByPostTime = articleRepository.findByPostTime(gte, lte, PageRequest.of(0, 4)) ;
		
		List<Article> findByPropertyLteGle = articleRepository.findByPropertyLteGle(gte, lte, PageRequest.of(0, 4)) ;
		
		for (Article article : findByPropertyLteGle) {
			System.out.println(article);
		}
	}
	
	
	

	/**
	 * 测试时间区间查询
	 * @throws Exception 
	 */
	@Test
	public void test19() throws Exception{
		Pageable pageable = PageRequest.of(0, 100) ;
		
		String gteStr = "2017-11-15 11:07:50" ;
		String lteStr = "2017-11-15 17:39:23" ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		long gte = dateFormat.parse(gteStr).getTime() ;
		
		long lte = dateFormat.parse(lteStr).getTime() ;
		
		//long lte = new Date().getTime() ;
		
		List<Article> findByPropertyLteGle = articleRepository.findByPropertyLteGle(gte,lte, pageable) ;
		
		for (Article article : findByPropertyLteGle) {
			System.out.print(article.getId());
			System.out.println(dateFormat.format(article.getPostTime()));
		}
	}
	
	/**
	 * 多字段包含查询
	 */
	@Test
	public void tet11() {
		Pageable pageable = PageRequest.of(0, 4);
		List<Article> contain = articleRepository.findContain("郭等发", pageable);
		for (Article article : contain) {
			System.out.println(article);
		}
	}

	/**
	 * 测试查询
	 * 
	 * @throws Exception
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	@Test
	public void test1() throws NoSuchFieldException, SecurityException, IllegalArgumentException, Exception {
		Article article = new Article();

		// new UeflexUtile<Article>().setField(Article.class.newInstance(),
		// Document.class, "indexName", "*");
		// String classObject = new
		// UeflexUtile<Article>().getClassObject(article);
		// System.out.println(classObject);

		// article.setId(id);
		// Pageable是接口，PageRequest是接口实现
		// PageRequest的对象构造函数有多个，page是页数，初始值是0，size是查询结果的条数，后两个参数参考Sort对象的构造方法
		Pageable pageable = PageRequest.of(0, 1000);

		// List<Article> list =
		// articleRepository.getArticleByProperty(article,pageable);
		List<Article> allArticle = articleRepository.getAllArticle(pageable);
		for (Article article2 : allArticle) {
			System.out.println(article2.getPostTime());
			System.out.println(TimeFormattingUtile.getTimeFormat(article2.getPostTime()));
		}

	}

	/**
	 * 根据id删除数据
	 */
	@Test
	public void delect() {
		Article article = new Article();
		// article.setId(1000l);
		String delectById = articleRepository.delectById(100l);
		System.out.println(delectById);

	}

	/**
	 * 查询文档数量
	 */
	@Test
	public void test10() {
		NativeSearchQueryBuilder searchQuery = new NativeSearchQueryBuilder();
		BoolQueryBuilder bqb = QueryBuilders.boolQuery();

		// bqb.must(QueryBuilders.termQuery("id","74247991186710"));
		searchQuery.withIndices("blog").withQuery(bqb);
		long count = elasticsearchTemplate.count(searchQuery.build());
		System.out.println(count);
	}

	/**
	 * 获取索引
	 */
	@Test
	public void test9() {
		// elasticsearchTemplate.createIndex(indexName) ;
		boolean indexExists = elasticsearchTemplate.indexExists(Article.class);
		System.out.println(indexExists);

		boolean indexExists2 = elasticsearchTemplate.indexExists("blog");
		System.out.println(indexExists2);
	}

	/**
	 * 索引映射类型
	 */
	@Test
	public void test8() {
		// Client client = elasticsearchTemplate.getClient() ;

		Map mapping = elasticsearchTemplate.getMapping(Article.class);
		for (Object object : mapping.keySet()) {
			System.out.println(object + ":" + mapping.get(object));
		}
		
		 Set entrySet = mapping.entrySet() ; 
		 
		 for (Object object : entrySet) {
			 System.out.println(object); 
		 }
		 
	}

	/**
	 * 创建索引
	 */
	@Test
	public void test7() {
		boolean createIndex = elasticsearchTemplate.createIndex(Article.class);
		System.out.println(createIndex);
	}

	/**
	 * uuid
	 */
	@Test
	public void uuid() {
		Set<String> set = new HashSet<>();
		for (int i = 0; i < 100000; i++) {
			UUID randomUUID = UUID.randomUUID();
			// System.out.println(randomUUID);
			String replaceAll = randomUUID.toString().replaceAll("[-a-zA-Z]", "").substring(0, 8);
			// set.add(replaceAll) ;
			System.out.println(replaceAll);

			String trandNo = String.valueOf((Math.random() * 9 + 1) * 1000000).substring(0, 6);
			// System.out.println(trandNo + replaceAll);
			String one = trandNo + replaceAll;
			set.add(one);
		}
		int size = set.size();
		System.out.println(size);
	}

	/**
	 * 
	 */
	@Test
	public void test2() {
		try {
			int i = 10 / 0;

		} catch (Exception e) {
			// e.printStackTrace();
			logger.error("nnreorggog", e);
		}
	}

	@Test
	public void test4() throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		String format = formatter.format(date);
		System.out.println(format);

		Date date2 = formatter.parse(format);
		System.out.println(date2);

		
		String format1 = formatter.format(date2) ; System.out.println(format1);
		 
	}

	@Test
	public void test15() {
//		try {
//			new UeflexUtile<>().setField(new Article(),Document.class,"indexName","*");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		try {
			List<Article> allArticle = articleRepository.getAllArticle(PageRequest.of(0, 4)) ;
			
			String classObject = new UeflexUtile<>().getObjectAnnotationName(new Article()) ;
			String classObject1 = new UeflexUtile<>().getObjectAnnotationName(new Article()) ;
			System.out.println(classObject);
			System.out.println(classObject1);
			
			for (Article article : allArticle) {
				//System.out.println(article);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Article> articleList = null ;
			// 如果queryBuilder为空，则全部查出来
			SearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(null).withPageable(PageRequest.of(0,4))
					.build();
			
			//new UeflexUtile<Article>().setField(getEntityClass().newInstance(),Document.class, "indexName","test_the_text_is_released_index_2017.11.14"); 
			articleList = elasticsearchTemplate.queryForList(nativeSearchQuery, Article.class);
		
		String classObject = new UeflexUtile<>().getObjectAnnotationName(new Article()) ;
		System.out.println(classObject);
	}

	@Test
	public void test16() {
		String classObject = new UeflexUtile<>().getObjectAnnotationName(new Article());
		System.out.println(classObject);
	}

	// 错误、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、、
	@Test
	public void test3() {
		QueryBuilder queryBuilder = new TermsQueryBuilder("title", "title");

		SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();

		long count = elasticsearchTemplate.count(searchQuery);
		System.out.println(count);
	}
	
	
}