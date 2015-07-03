<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div class="tc" style="border:none;text-align:left;">${html}<br/><a id="errorlink" href="javascript:displayErrorReport();"><%=StringUtils.text("report.error", session) %></a></div>
<script type="text/javascript"><!--
window.onload = function() {
	if ('<%=request.getParameter("print")%>' != 'null') {
		window.print();
	}
	t1 = parseInt('<%=request.getAttribute("t1")%>');
	t2 = parseInt('<%=request.getAttribute("t2")%>');
	handleRender();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />