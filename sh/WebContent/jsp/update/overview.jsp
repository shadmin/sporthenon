<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%
String lang = String.valueOf(session.getAttribute("locale"));
%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-overview" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.overview", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">...</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	//initUpdate("<%=request.getAttribute("value")%>");
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />