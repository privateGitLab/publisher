<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%@ include file="include/include.jsp" %>
<title>按时间作者进行完全匹配搜索</title>
<script type="text/javascript">
	$(function() {
		
		//var currentDateValue = $(".postTime_class").val();
		//var formattedValue = moment(currentDateValue).format('YYYY-MM-dd HH:mm:SS') ;
		//console.log(currentDateValue)
		//console.log(formattedValue);
		//$(".postTime_class").val(formattedValue);
		
		var begin = $("#postTime_begin").val() ;
		/* 通过id删除文档 */
		$("#delect_article").click(function() {
			var id = $("#article_id").val();
			var pageCurrent = ${page.pageCurrent}
			alert("要删除的文档ID：" + id) ;
			
			var author_title = $("#author_title").val();
			
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/conterController/delectById/blog/article",
				data :{"id": id},
				dataType : "json",
				success : function(msg) {
					if (msg == id) {
						alert("删除文档成功!"+"删除文档ID为："+id);
					}
					alert(author_title) ;
					window.location.href = "${pageContext.request.contextPath}/conterController/findContain/blog/article?filed="+encodeURI(encodeURI(author_title)) + "&pageCurrent=" + pageCurrent
				},
				error: function(){
					console.log("##encountered error when calling ajax###3")
				}
			});
		})
	})
</script>	

		<%-- <c:if test="${page.judgePageLost}">
			<a class="btn btn-small btn-info" role="button" href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=0">首页</a>
			<a class="btn btn-small btn-info" role="button" href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageLost}">上一页</a>
		</c:if>
		<c:if test="${!empty page.articles}">
			当前页:【${page.pageCurrent + 1}】
			总页数：【${page.pageCount}】
		</c:if>
		<c:if test="${page.judgePageNext}">
			<a class="btn btn-small btn-info" role="button" href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageNext}">下一页</a>
			<a class="btn btn-small btn-info" role="button" href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageCount}">末页</a>
		</c:if> --%>
		<input type="hidden" value="${page.pageCount}"></input> 
		<div class="container">
			<ul class="pager">
				<%-- <h5 style="color:blue;text-align:center;font: 10px/1.5 Tahoma,Helvetica,Arial,'宋体',sans-serif">当前第【${page.pageCurrent+1}】页</h5> --%>
				<!-- <ul class="pagination"> -->
				<!-- <li><a href="#">«</a></li> -->
			    <c:if test="${page.judgePageLost}">
					<li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=0">首页</a></li>
					<li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageLost}">上一页</a></li>
				<c:if test="${page.pageCurrent-1 != 0}">
				    <li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageCurrent-2}">${page.pageCurrent-1}</a></li>
			    </c:if>
				    <li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageCurrent-1}">${page.pageCurrent}</a></li>
			    </c:if>
			   
			    <li class="disabled"><a href="#">${page.pageCurrent+1}</a></li>
			    
			   	<c:if test="${page.judgePageNext}">
				   
				    <li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageCurrent+1}">${page.pageCurrent+2}</a></li>
				   	
				   	<c:if test="${page.pageCurrent+2 != page.pageCount}">
				    	<li class="active"><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageCurrent+2}">${page.pageCurrent+3}</a></li>
			    	</c:if>
			    	
				    <li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageNext}">下一页</a></li>
				    <li><a href="${pageContext.request.contextPath}/conterController/findContain/blog/article?filed=<%=java.net.URLEncoder.encode(request.getParameter("filed")) %>&pageCurrent=${page.pageCount}">末页</a></li>
			 	</c:if>
			 	
			 	<li><h5 style="color:blue;text-align:center;font: 16px/1.0 Tahoma,Helvetica,Arial,'宋体',sans-serif">共【${page.pageCount}】页</h5></li>
			</ul>
			<!-- </ul> -->
		</div>
	</div>
</body>
</html>