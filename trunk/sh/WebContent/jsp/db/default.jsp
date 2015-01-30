<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.sporthenon.utils.ConfigUtils"%>
<%@page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div class="tc" style="border:none;text-align:left;">${html}</div>
<script type="text/javascript"><!--
if ('<%=request.getParameter("print")%>' != 'null') {
	window.print();
}
handleRender();
--></script>
<jsp:include page="/jsp/common/footer.jsp" />