<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.sporthenon.utils.ConfigUtils"%>
<%@page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div class="tc" style="border:none;text-align:left;">${html}</div>
<jsp:include page="/jsp/common/footer.jsp" />
<script type="text/javascript">
$$('.rendertip').each(function(el) {
	new Control.Window($(document.body).down('[href=#' + el.id + ']'),{  
		position: 'relative', hover: true, offsetLeft: 20, offsetTop: 0, className: 'tip'
	});
});
if ('<%=request.getParameter("print")%>' != 'null') {
	window.print();
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />