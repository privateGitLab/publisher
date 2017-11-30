<%@page import="com.yiche.publish.entity.Article"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page isELIgnored="false"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>文本发布器</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery/jquery-3.2.1.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/js/zh/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/bootstrap-datetimepicker-master/sample in bootstrap v3/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript">
	$(function() {
		$("#add_update_button").click(function() {
			var id = $("#id").val() ;
			var title = $("#title_id").val() ;
			//var content = $("#content_id").val() ;
			var content = CKEDITOR.instances.content_id.getData() ;
			var author = $("#author_id").val() ;
			var clickCount = $("#clickCount_id").val() ;
			alert("点击量：" + clickCount) ;
			
			var postTime = $("#postTime_id").val() ;
			$.ajax({
				type : "POST",
				url : "${pageContext.request.contextPath}/conterController/addOrEditArticle/blog/article",
				data : { "id" : id ,"title": title, "content": content,"author" :author ,"clickCount" : clickCount,"postTime" : postTime},
				dataType :"json" ,
				success : function(msg) {
					if(msg == id){
						alert("更改ID为【" + id + "】的文档成功！！！") ;
					}else{
						alert("添加成功！！！新添加文档ID为【" + msg + "】") ;
					}
					window.location.href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article" 
				}
			});
		}
		)
	})
</script>
</head>  
<body>
	<div class="container-fluid">
		<center>
			<form class="form-horizontal">
				<!-- <input type="hidden" name="_method" value="PUT"> -->
					<!-- 【ID不能手动添加或修改，在页面是不可见的】 -->
					<input id="id" type="hidden" name="id" value="${article.id}"/><br>
					<input id="clickCount_id" name="clickCount" type="hidden" value="${article.clickCount}"><br>
				标题：<input id="title_id" type="text" name="title" value="${article.title}"/><br/><br>
				<h3>文本内容</h3>
				<textarea id="content_id" rows="5" cols="20" class="ckeditor" name="content">${article.content}</textarea>
				<br>
				作者：<input id="author_id" type="text" name="author" value="${article.author}"/>
				<!-- 用户不能更改点击量-->
				点击率：<input id="clickCount_id" type="text" value="${article.clickCount}" name="clickCount"/>
				<!-- 文章字数 -->
				<!-- <input type="text" value=""/> -->
				发布时间：<input type="text" name="postTime" value="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %>"/>
						<input id="postTime_id" type="hidden" name="postTime" value="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) %>"/>
				<br/><br>
				<c:if test="${!empty article}">
					<input id="add_update_button" type="button" value="更改"/>
				</c:if>
				<c:if test="${empty article}">
					<input id="add_update_button" type="button" value="添加"/>
				</c:if>
			</form>
		</center>
	</div>
</body>
</html>