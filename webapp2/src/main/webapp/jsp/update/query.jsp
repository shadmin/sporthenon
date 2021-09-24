<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-query" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.query", session).toUpperCase()%></div>
		<div class="fscontent">
			<ul>
			<li>
				<fieldset><legend><%=StringUtils.text("predefined.queries", session)%></legend>
				<a href="javascript:executeQuery(0);">Duplicate athletes by sport</a><br/>
				<a href="javascript:executeQuery(1);">Events/Championships not used</a><br/>
				<a href="javascript:executeQuery(2);">Events not completed for current year</a><br/>
				<a href="javascript:executeQuery(4);">Untranslated items</a><br/>
				<a href="javascript:executeQuery(5);">Incomplete event results</a><br/>
				<a href="javascript:executeQuery(6);">Athletes/teams without country</a><br/>
				<a href="javascript:executeQuery(7);">Entities without external link</a><br/>
				<a href="javascript:executeQuery(8);">Duplicate cities</a><br/>
				</fieldset>
			</li>
			<li>
				<fieldset><legend><%=StringUtils.text("custom.queries", session)%></legend>
				<textarea id="query" rows="6" cols="200" style="width:500px;"></textarea>
				<br/><button onclick="executeQuery(-1);"><%=StringUtils.text("execute", session)%></button>
				</fieldset>
			</li>
			<li>
				<fieldset><legend><%=StringUtils.text("output", session)%></legend>
				<input type="radio" id="rscreen" name="output" checked="checked"/><label for="rscreen"><%=StringUtils.text("screen", session)%></label><br/>
				<input type="radio" id="rcsv" name="output"/><label for="rcsv"><%=StringUtils.text("csv.file2", session)%></label>
				</fieldset>
			</li>
			</ul>
			<div id="qresults"></div>
		</div>
	</div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>