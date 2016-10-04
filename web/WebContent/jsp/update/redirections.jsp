<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-redirections" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.redirections", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<table class="toolbar" style="position:relative;top:0;right:0;float:left;">
				<tr>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveRedirections();" value="<%=StringUtils.text("save", session)%>"/></td>
					<td id="msg"></td>
				</tr>
			</table>
			<div id="recontent"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	loadRedirections();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>