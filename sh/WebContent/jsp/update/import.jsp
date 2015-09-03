<%@page import="java.util.List"%>
<%@page import="com.sporthenon.db.DatabaseHelper"%>
<%@page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<form id="f" action="/update/execute-import" method="post" enctype="multipart/form-data">
<div id="update-import" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.import", session).toUpperCase()%></div>
		<div class="fscontent">
			<table id="options"><tr>
				<td><%=StringUtils.text("file", session)%>&nbsp;:</td>
				<td><input id="f" name="f" type="file"/>
				<td><input type="button" value="OK" onclick="executeImport();"/></td>
			</tr></table>
		</div>
	</div>
</div>
</form>
<jsp:include page="/jsp/common/footer.jsp" />