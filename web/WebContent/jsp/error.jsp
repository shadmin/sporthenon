<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div class="tc" style="border:none;text-align:left;">
<div class="noresult" style="margin:10px 0px;"><%=StringUtils.text("page.error", session) %></div>
</div>
<jsp:include page="/jsp/common/footer.jsp" />