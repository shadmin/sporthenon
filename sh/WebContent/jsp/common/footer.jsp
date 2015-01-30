<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
</div><!-- id="content" -->
<jsp:include page="/jsp/common/dialogs.jsp"/>
<div id="footer">
	<div style="float:right;text-align:right;">
		<img alt="top" onclick="backTop();" style="cursor:pointer;" title="<%=StringUtils.text("back.top", session)%>" src="img/top.png"/>&nbsp;
		<a target="_blank" title="Valid XHTML 1.1" href="http://validator.w3.org/check?uri=referer"><img src="img/header/validxhtml11.png" alt="Valid XHTML 1.1"/></a>
		<div id="version">Version&nbsp;<%=ConfigUtils.getProperty("version")%></div>
	</div>
	&copy;2011-15&nbsp;(sporthenon.com)<br/><br/><a href="mailto:contact@sporthenon.com">contact@sporthenon.com</a>
</div>
</body>
</html>