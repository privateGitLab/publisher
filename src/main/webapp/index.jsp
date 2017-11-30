<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>首页</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/static/jquery/jquery-3.2.1.js"></script>
<script type="text/javascript">
	$(function() {
		$("#submit").click(function() {
			$.ajax({
				type : "GET",
				url : "conterController/findAllArticle/blog/article",
				data : {"page":0,"size":5} ,
				dataType :"json" ,
				success : function(msg) {
					alert() ;
					alert("Data Saved: " + msg);
					//window.location.href="conterController/findAllArticle/blog/article" 
				}
			});
			/* return true; */
		}
		)
	})
</script>
</head>
<body>
	<center>
		<a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article">跳转到首页</a>
		<!-- <form >
			<input id="submit" type="button" value="提交">
		</form> -->
	<center>
</body>
</html>