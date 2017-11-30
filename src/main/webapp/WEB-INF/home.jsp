<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<!-- 此处不能使用动态包含，只能使用静态包含 -->
<%-- <jsp:include page="include/include.jsp"></jsp:include> --%>
<%@ include file="include/include.jsp" %>
<title>首页</title>
<script type="text/javascript">
	$(function() {
		
		//var currentDateValue = $(".postTime_class").val();
		//var formattedValue = moment(currentDateValue).format('YYYY-MM-dd HH:mm:SS') ;
		//console.log(currentDateValue)
		//console.log(formattedValue);
		//$(".postTime_class").val(formattedValue);
		var pageCurrent = ${page.pageCurrent}
		/* 通过id删除文档 */
		$(".delect_article_class").click(function() {
			var id = $("#article_id").val();
			
			alert("要删除的文档ID：" + id) ;
			
			$.ajax({
				type:"post",
				url:"${pageContext.request.contextPath}/conterController/delectById/blog/article",
				data :{"id": id},
				dataType : "json",
				//async:false ,
				success : function(msg) {
					//alert(msg) ;
					if (msg == id) {
						alert("删除文档成功!"+"删除文档ID为：" + msg);
						window.location.href = "${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=" + pageCurrent
					}
				},
				error: function(){
					console.log("##encountered error when calling ajax###3")
				}
			});
		})
	})
</script>

		<div class="container">
			<ul class="pager">
				<%-- <h5 style="color:blue;text-align:center;font: 10px/1.5 Tahoma,Helvetica,Arial,'宋体',sans-serif">当前第【${page.pageCurrent+1}】页</h5> --%>
				<!-- <ul class="pagination"> -->
				<!-- <li><a href="#">«</a></li> -->
			    <c:if test="${page.judgePageLost}">
					<li><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=0">首页</a></li>
					<li><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageLost}">上一页</a></li>
				<c:if test="${page.pageCurrent-1 != 0}">
				    <li><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageCurrent-2}">${page.pageCurrent-1}</a></li>
			    </c:if>
				    <li><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageCurrent-1}">${page.pageCurrent}</a></li>
			    </c:if>
			   
			    <li class="disabled"><a href="#">${page.pageCurrent+1}</a></li>
			    
			   	<c:if test="${page.judgePageNext}">
				   
				    <li><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageCurrent+1}">${page.pageCurrent+2}</a></li>
				    <c:if test="${page.pageCurrent+2 != page.pageCount}">
				    	<li class="active"><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageCurrent+2}">${page.pageCurrent+3}</a></li>
			    	</c:if>
				    <li><a href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageNext}">下一页</a></li>
				    <li><a role="button" href="${pageContext.request.contextPath}/conterController/findAllArticle/blog/article?pageCurrent=${page.pageCount - 1}">末页</a></li>
			 	</c:if>
			 	
			 	<li><h5 style="color:blue;text-align:center;font: 16px/1.0 Tahoma,Helvetica,Arial,'宋体',sans-serif">共【${page.pageCount}】页</h5></li>
			</ul>
			<!-- </ul> -->
		</div>
	</div>
</body>
</html>