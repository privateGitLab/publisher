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
<title>详细信息页面</title>
<style type="text/css">
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
</style>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/ckeditor/ckeditor.js"></script>
</head>  
<body>
	<center>
		<form>
				【ID不能手动添加或修改，在页面是不可见的】
				<input id="id" type="text" name="id" value="${article.id}"/><br/> 
			标题：<input id="title_id" type="text" name="title" value="${article.title}"/><br/> 
			文章字数：<input type="text" value="${article.wordNumber}"><br/> 
			文本内容：<textarea id="content_id" rows="5" cols="20" class="ckeditor" name="content">${article.content}</textarea><br/> 
			作者：<input id="author_id" type="text" name="author" value="${article.author}"/><br/> 
			<!-- 用户不能更改 -->
			<!-- 点击量也需要统计 -->
			发布时间：<input id="postTime_id" type="text" name="postTime" value="<%=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((Article)request.getAttribute("article")).getPostTime()) %>"/>
			点击量：<input id="clickCount_id" name="clickCount" type="text" value="${article.clickCount}">
			<br/> 
		</form>
	</center>
</body>
</html>