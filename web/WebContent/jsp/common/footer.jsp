<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
</div><!-- id="content" -->
<%if (request.getParameter("print") == null) {%>
<jsp:include page="/jsp/common/dialogs.jsp"/>
<div id="footer">
	<div style="float:right;text-align:right;">
		<img alt="top" onclick="backTop();" style="cursor:pointer;" title="<%=StringUtils.text("back.top", session)%>" src="/img/top.png"/>
		<a target="_blank" title="Valid XHTML 1.1" href="http://validator.w3.org/check?uri=referer"><img src="/img/header/validxhtml11.png" alt="Valid XHTML 1.1"/></a>
		<div id="version">Version <%=ConfigUtils.getProperty("version")%></div>
	</div>
	&copy;2011-16 (sporthenon.com)<br/><br/>
	<span style="color:#888;">
		<%=StringUtils.text("last.update", session)%>: <%=(StringUtils.notEmpty(request.getAttribute("lastupdate")) ? request.getAttribute("lastupdate") : StringUtils.toTextDate(ConfigUtils.getProperty("date"), String.valueOf(session.getAttribute("locale")), "dd MMM yyyy"))%>
		<span id="loadtime" style="display:none;"> &ndash; <span></span> <%=StringUtils.text("seconds", session)%></span>
	</span>
</div>
<%}%>
</body>
</html>