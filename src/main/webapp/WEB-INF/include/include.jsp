<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.yiche.publish.entity.Article"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">

<style> 
body {
	text-align: center
}

.divcss5 {
	margin: 0 auto;
	width: 400px;
	height: 100px;
	border: 1px solid #000
}
input{  
    text-align:center;  
}  
table,table tr th, table tr td { border:1px solid #0094ff; }
table { width: 200px; min-height: 25px; line-height: 25px; text-align: center; border-collapse: collapse; padding:2px;}   

h1 {font-size: 12px;color: #FFF; display : inline}
h5 {font-size: 12px;color: #FFF; display : inline}
</style> 

<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery/jquery-3.2.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/moment/moment.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/js/zh/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/sample in bootstrap v3/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/sample in bootstrap v3/bootstrap/css/bootstrap.css"></script>
<link
	href="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.css" 
	type="text/css"
	rel="stylesheet"></link>
<link
	href="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/sample in bootstrap v2/bootstrap/css/bootstrap.css" 
	type="text/css"
	rel="stylesheet"></link>

<script type="text/javascript">
	$(function() {
		/* 通过id数组删除文档 */
		$("#delect_articles").click(function() {
			var ids =[];
			$('input[name="articleId"]:checked').each(function(){
				ids.push($(this).val());
			}); 
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/conterController/delectByIds/blog/article",
				data : {"ids":ids},
				traditional:true,
				dataType : "json",
				success : function(msg) {
					if (msg) {
						//alert("删除文档组成功!");
					}
					window.location.href = "${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?page=${pageNumber}"
				},
				error: function(){
					console.log("##encountered error when calling ajax###3")
				}
			});
		})
		
		
		/* 按照时间区间进行查找 */
		$("#postTime_search").click(function() {
			var gte = $("#postTime_begin").val();
			var lte = $("#postTime_end").val();
			alert("初试时间:" + gte) ;
			alert("结束时间:" + lte) ;
			window.location.href = "${pageContext.request.contextPath}/conterController/findByPropertyLteGle/blog/article?gte="+encodeURI(encodeURI(gte)) + "&lte=" +encodeURI(encodeURI(lte))
		})
		
		/* 字段模糊查询，全文搜索 */
		$("#full_text_search").click(function() {
			var search_word = $("#search_word").val();
			alert("全文搜索字符串: " + search_word) ;
			window.location.href = "${pageContext.request.contextPath}/conterController/findByWordAllOrderByProperty/blog/article?word="+encodeURI(encodeURI(search_word))
		})
		
		
		/* 字段完全匹配查询 */
		$("#contain_matching_search").click(function() {
			var author_title = $("#author_title").val();
			alert("按标题和作者完全匹配搜索字符串:" + author_title) ;
			window.location.href = "${pageContext.request.contextPath}/conterController/findContain/blog/article?filed="+encodeURI(encodeURI(author_title))
		})
	})
</script>
</head>
<body>
	<div class=container-fluid>
	<center>
		<form class="form-horizontal" style="margin-top: 77px;">
			<c:if test="${empty page.articles}">
				<h1>当前索引中还没有文档请添加文档</h1>
				<a href="${pageContext.request.contextPath}/conterController/findById/blog/article">添加文档数据</a>
			</c:if>
			<c:if test="${!empty page.articles}">
				<h5 style="color:blue;text-align:center;font: 21px/1.5 Tahoma,Helvetica,Arial,'宋体',sans-serif">查询</h5>
				<table  border="1" width="100%" style="width:100%;border:1px white solid" cellspacing="5">
					<tr>
						<td>
							<div class="input-append date form_datetime" data-date="2013-02-21T15:25:00Z">
							    <input id="postTime_begin" size="16" type="text" name="postTime_begin" value="${gte}"  placeholder="请输入要查询的开始时间" readonly/>
							    <span class="add-on"><i class="icon-remove"></i></span>
							    <span class="add-on"><i class="icon-calendar"></i></span>
							</div>
							<script type="text/javascript">
							   $(".form_datetime").datetimepicker({
							       format: "yyyy-mm-dd hh:ii:ss",
							       language:'zh-CN',
							       autoclose: true,
							       todayBtn: true,
							       startDate: "2016-01-12 16:15:12",
							       minuteStep: 10
							   });
							</script>
						</td>
						<td>
							<div class="input-append date form_datetime" data-date="2013-02-21T15:25:00Z">
							    <input id="postTime_end" size="16" type="text" name="postTime_end" value="${lte}"  placeholder="请输入要查询的结束时间" readonly/>
							    <span class="add-on"><i class="icon-remove"></i></span>
							    <span class="add-on"><i class="icon-calendar"></i></span>
							</div>
							<script type="text/javascript">
							   $(".form_datetime").datetimepicker({
							       format: "yyyy-mm-dd hh:ii:ss",
							       language:'zh-CN',
							       autoclose: true,
							       todayBtn: true,
							       startDate: new Date() ,
							       minuteStep: 10
							   });
							</script>
						</td>
						<td><input id="postTime_search" type="button" value="搜索"/></td>
						<td>[按时间区间进行搜索]</td>
					</tr>
					<tr>
						<td colspan="2">
							<input id="search_word" placeholder="请输入要搜索的文字" type="text" name="search_word" value="${word}"/>
						</td>
						<td><input id="full_text_search" type="button" value="全文搜索"/></td>
						<td>
							<div class="form-group form-group-lg">
								<label class="col-sm-2 control-label" for="formGroupInputLarge">[全文搜索]</label>
							</div>
						</td>
					</tr>
					<tr>
						<td>输入作者或标题：</td>
						<td><input id="author_title" type="text" name="filed" value="${filed}" placeholder="请输入要搜索的文字"/></td>
						<td><input id="contain_matching_search" type="button" value="完全匹配搜索"/></td>
						<td>[按作者和标题进行完全匹配搜索]</td>
					</tr>
				</table><br><br><br>
				<%-- <caption style="color:blue;text-align:center">查询结果</caption> --%>
				<h5 style="color:blue;text-align:center;font: 21px/1.5 Tahoma,Helvetica,Arial,'宋体',sans-serif">查询结果</h5>
				<table class="table-striped table-bordered table-condensed" border="1" width="100%" style="width:100%;border:1px #000 solid; border-top:0" cellspacing="0">
					<thead>
						<tr>
							<th>ID</th>
							<!-- <th>选中</th> -->
							<th class="seq">标题</th>
							<th>作者</th>
							<th>发布时间</th>
							<th>编辑</th>
							<th>删除</th>
							<th>添加</th>
							<th>查看</th>
							<!-- <th>删除选中项</th> -->
						</tr>
					</thead>
					<c:forEach items="${page.articles}" var="article">
						<% Article article = (Article)pageContext.getAttribute("article") ;
							SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
							String strDateFormat = dateFormat.format(article.getPostTime()) ;
						%>
						<tbody>
						</tbody>
							<tr>
								<td>
									<div class="column">
										  <div class="col-xs-2">
												<input id="article_id" type="text" value="${article.id}" class="form-control" placeholder=".col-xs-2"/>
										  </div>
									</div>
								</td>
								<%-- <td>
									<input class="article_class" type="checkbox" value="${article.id}"
									name="articleId" />
								</td> --%>
								<td>
									<input id="title_id" type="text" name="title"
									value="${article.title}" class="form-control" placeholder="标题不能为空"/>
								</td>
								<td>
									<input  id="author_id" type="text" name="author"
									value="${article.author}"  placeholder="作者不能为空"/>
								</td>
								<td>
									<input class="postTime_class" id="postTime_id" type="text" value="<%=strDateFormat %>"/>
								</td>
								<%-- <td><textarea rows="5" class="ckeditor" cols="20" name="content">${article.content}</textarea></td> --%>
								<td>
									<a id="post_article"
									href="${pageContext.request.contextPath}/conterController/findById/blog/article?id=${article.id}" class="btn btn-default" role="button">编辑</a>
								</td>
								<td>
									<input id="delect_article" class="delect_article_class" type="button" value="删除"/>
								</td>
								<td>
									<a id="put_article"
									href="${pageContext.request.contextPath}/conterController/findById/blog/article"  class="btn btn-default" role="button">添加</a>
								</td>
								<td>
									<a id="geet_article"
									href="${pageContext.request.contextPath}/conterController/queryById/blog/article?id=${article.id}" class="btn btn-default" role="button">查看详细信息</a>
								</td>
								<!-- <td rowspan="4">
									<input id="delect_articles" type="button" value="删除指定的文档"/>
								</td> -->
							</tr>
					</c:forEach>
							<tr>
								<td colspan="8"><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article" class="btn btn-success" role="button">回首页</a></td>
							</tr>
				</table>
			</c:if>
		</form>
	</center>