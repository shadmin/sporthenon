<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%
String lang = String.valueOf(session.getAttribute("locale"));
%>
<jsp:include page="/jsp/common/header.jsp"/>
		<div class="fieldset" style="height:300px;">
		<div class="fstitle contributors"><%=StringUtils.text("contributors", session)%></div>
		<div class="fscontent">
			<div style="height:260px;overflow:auto;">
			<table><tr><th>ID</th><th><%=StringUtils.text("name", session)%></th><th><%=StringUtils.text("entity.SP", session)%></th><th><%=StringUtils.text("contributions", session)%></th></tr><%=request.getAttribute("contributors")%></table>
			</div>
		</div>
		</div>
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
<jsp:include page="/jsp/common/footer.jsp"/>