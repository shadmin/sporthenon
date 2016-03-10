<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%
String lang = String.valueOf(session.getAttribute("locale"));
%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="contribute">
<div class="fieldset">
	<div class="fstitle info"><%=StringUtils.text("about.contribution", session)%></div>
	<div class="fscontent"><%=ConfigUtils.getValue("html_contribution1_" + lang)%></div>
</div>
<div class="fieldset">
	<div class="fstitle help"><%=StringUtils.text("howto.contribute", session)%></div>
	<div class="fscontent"><%=ConfigUtils.getValue("html_contribution2_" + lang)%></div>
</div>
</div>
<jsp:include page="/jsp/common/footer.jsp" />