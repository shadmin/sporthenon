<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-translations" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.translations", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<table class="toolbar" style="position:relative;top:0;right:0;float:right;">
				<tr>
					<td id="msg"></td>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveTranslations();" value="<%=StringUtils.text("save", session)%>"/></td>
				</tr>
			</table>
			<table id="options"><tr>
				<td>IDs :</td>
				<td><input id="trrange" type="text" value="1-500" style="width:90px;"/></td>
				<td><%=StringUtils.text("find", session)%> :</td>
				<td><input id="trpattern" type="text" style="width:100px;"/></td>
				<td><%=StringUtils.text("show", session)%> :</td>
				<td><select id="trentity">
				<option value="CP"><%=StringUtils.text("entity.CP", session)%></option>
				<option value="CT"><%=StringUtils.text("entity.CT", session)%></option>
				<option value="CN"><%=StringUtils.text("entity.CN", session)%></option>
				<option value="EV"><%=StringUtils.text("entity.EV", session)%></option>
				<option value="SP"><%=StringUtils.text("entity.SP", session)%></option>
				<option value="ST"><%=StringUtils.text("entity.ST", session)%></option>
				</select></td>
				<td><input id="trincludechecked" type="checkbox"/></td>
				<td><label for="trincludechecked"><%=StringUtils.text("include.checked.translations", session)%></label></td>
				<td><input type="button" value="OK" onclick="loadTranslations();"/></td>
			</tr></table>
			<div id="trcontent"></div>
		</div>
	</div>
</div>
<script><!--
window.onload = function() {
	loadTranslations();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>