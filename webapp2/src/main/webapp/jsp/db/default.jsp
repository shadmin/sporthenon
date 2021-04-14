<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div class="render" style="border:none;text-align:left;">${html}</div>
<script><!--
window.onload = function() {
	if ('<%=request.getParameter("print")%>' != 'null') {
		window.print();
	}
	t1 = parseInt('<%=request.getAttribute("t1")%>');
	t2 = parseInt('<%=request.getAttribute("t2")%>');
	handleRender();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>