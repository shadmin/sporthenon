<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div class="render" style="border:none;text-align:left;">
	<div id="msgerr" style="margin:10px 0px;">
		<%=StringUtils.text("page.error", session)%>
	</div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>