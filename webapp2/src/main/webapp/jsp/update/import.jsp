<%@page import="java.util.List"%>
<%@page import="com.sporthenon.db.DatabaseManager"%>
<%@page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-import" class="update">
	<script src="/js/dropzone.js"></script>
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.import", session).toUpperCase()%></div>
		<div class="fscontent">
			<table id="options"><tr>
				<td><%=StringUtils.text("type", session)%> :</td>
				<td><select id="type"><option value="RS"><%=StringUtils.text("entity.RS", session)%></option></select></td>
				<td><%=StringUtils.text("csv.file2", session)%> :</td>
				<td><div id="dz-file"><p id="fname"></p></div></td>
				<td><input id="processbtn" type="button" value="<%=StringUtils.text("btn.process", session)%>" disabled="disabled" onclick="executeImport(0);"/></td>
				<td><input id="updatebtn" type="button" value="<%=StringUtils.text("btn.update", session)%>" disabled="disabled" onclick="executeImport(1);"/></td>
				<td><input type="button" value="<%=StringUtils.text("btn.template", session)%> CSV" onclick="loadTemplate('csv');"/></td>
				<td><input type="button" value="<%=StringUtils.text("btn.template", session)%> XLS" onclick="loadTemplate('xls');"/></td>
				<td><div id="progressbar" style="display:none;"><div id="progress"></div></div></td>
				<td id="pgpercent"></td>
			</tr></table>
			<div id="report"></div>
		</div>
	</div>
</div>
<script><!--
window.onload = function() {
	initImport();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>