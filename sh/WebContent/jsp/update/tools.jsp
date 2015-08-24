<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-tools" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.tools", session).toUpperCase()%></div>
		<div class="fscontent">
			<div style="position:absolute;left:550px;"><input type="radio" id="rscreen" name="output" checked="checked"/><label for="rscreen">Screen</label>&nbsp;<input type="radio" id="rcsv" name="output"/><label for="rcsv">CSV</label></div>
			<a href="javascript:executeQuery(0);">Duplicate athletes by sport</a><br/>
			<a href="javascript:executeQuery(1);">Events/Championships not used</a><br/>
			<a href="javascript:executeQuery(2);">Events not completed for current year</a><br/>
			<a href="javascript:executeQuery(4);">Untranslated items</a><br/>
			<a href="javascript:executeQuery(5);">Incomplete event results</a><br/>
			<a href="javascript:executeQuery(6);">Athletes/teams without country</a><br/>
			<a href="javascript:executeQuery(7);">Entities without external link</a>
			<div id="qresults"></div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/common/footer.jsp" />