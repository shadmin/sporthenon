<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-errors" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.errors", session).toUpperCase()%></div>
		<div class="fscontent">
			<div id="ercontent"></div>
		</div>
	</div>
</div>
<script><!--
window.onload = function() {
	loadErrors();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>