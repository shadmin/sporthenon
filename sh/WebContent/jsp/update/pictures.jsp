<%@page import="java.util.List"%>
<%@page import="com.sporthenon.db.DatabaseHelper"%>
<%@page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-pictures" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.pictures", session).toUpperCase()%></div>
		<div class="fscontent" style="padding-bottom:60px;">
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />