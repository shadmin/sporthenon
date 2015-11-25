<%@page import="java.util.List"%>
<%@page import="com.sporthenon.db.DatabaseHelper"%>
<%@page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-import" class="update">
	<script type="text/javascript" src="/js/dropzone.js"></script>
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.import", session).toUpperCase()%></div>
		<div class="fscontent">
			<table id="options"><tr>
				<td><%=StringUtils.text("type", session)%>&nbsp;:</td>
				<td><select id="type"><option value="RS"><%=StringUtils.text("entity.RS", session)%></option><option value="DR"><%=StringUtils.text("entity.DR", session)%></option><option value="RC"><%=StringUtils.text("entity.RC", session)%></option></select></td>
				<td><%=StringUtils.text("csv.file", session)%>&nbsp;:</td>
				<td><div id="dz-file"><p id="fname"></p></div></td>
				<td><input id="processbtn" type="button" value="<%=StringUtils.text("btn.process", session)%>" disabled="disabled" onclick="executeImport(0);"/></td>
				<td><input id="updatebtn" type="button" value="<%=StringUtils.text("btn.update", session)%>" disabled="disabled" onclick="executeImport(1);"/></td>
				<td><input type="button" value="<%=StringUtils.text("btn.template", session)%>" onclick="loadTemplate();"/></td>
				<td><div id="progressbar" style="display:none;"><div id="progress"></div></div></td>
				<td id="pgpercent"></td>
			</tr></table>
			<div id="report"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	initImport();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />