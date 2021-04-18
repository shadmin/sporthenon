<%@page import="java.util.Calendar"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
</div><!-- id="content" -->
<%if (request.getParameter("print") == null) {%>
<jsp:include page="/jsp/common/dialogs.jsp"/>
<div id="footer">
	<div style="float:right;text-align:right;">
		<img alt="top" onclick="backTop();" style="cursor:pointer;" title="<%=StringUtils.text("back.top", session)%>" src="/img/top.png"/>
		<a target="_blank" title="Valid HTML5" href="http://validator.w3.org/check?uri=referer"><img src="/img/header/validhtml5.png" alt="Valid HTML5"/></a>
		<div id="version">v<%=ConfigUtils.getProperty("version")%></div>
	</div>
	&copy;2011-<%=Calendar.getInstance().get(Calendar.YEAR)%> (sporthenon.com)<br/><br/>
	<span style="color:#888;">
		<%=StringUtils.text("last.update", session)%>: <%=(StringUtils.notEmpty(request.getAttribute("lastupdate")) ? request.getAttribute("lastupdate") : StringUtils.toTextDate(ConfigUtils.getProperty("date"), String.valueOf(session.getAttribute("locale")), "d MMM yyyy"))%>
		<span id="loadtime" style="display:none;"> &ndash; <span></span> <%=StringUtils.text("seconds", session)%></span>
	</span>
</div>
<%}%>
</body>
</html>