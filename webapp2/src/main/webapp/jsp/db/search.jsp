<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="title-search" class="title">
	<div><%=StringUtils.text("menu.search", session)%></div>
</div>
<div id="search">
	<form id="search-form" action="/search" onsubmit="return false;">
	<div class="pattern">
		<%=StringUtils.text("search.for", session)%>:<br/><input type="text" class="text" name="pattern" id="pattern" onkeydown="if(event.keyCode == 13){runSearch();}"/>
		<a href="#helplink" style="cursor:help;color:#000;">[?]</a>
		<br/><br/><table>
			<tr><td><input type="checkbox" name="match" id="match"/></td><td><label for="match"><%=StringUtils.text("exact.match", session)%></label></td></tr>
		</table>
		<table style="margin-top:10px;">
			<tr><td>Max: </td><td><input type="text" name="max" id="max" value="100" style="width:50px;"/></td></tr>
		</table>
	</div>
	<div id="helplink" class="rendertip" style="display:none;"><%=StringUtils.text("pattern.tip", session)%></div>
	<fieldset class="scope">
		<legend><%=StringUtils.text("scope", session)%></legend>
		<table>
		<tr>
			<td><input type="checkbox" value="PR" name="scope" id="PR" checked="checked"/></td><td><label for="PR"><%=StringUtils.text("athletes", session)%></label></td>
			<td><input type="checkbox" value="SP" name="scope" id="SP" checked="checked"/></td><td><label for="SP"><%=StringUtils.text("sports", session)%></label></td>
			<td></td><td></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CN" name="scope" id="CN" checked="checked"/></td><td><label for="CN"><%=StringUtils.text("countries", session)%></label></td>
			<td><input type="checkbox" value="TM" name="scope" id="TM" checked="checked"/></td><td><label for="TM"><%=StringUtils.text("teams", session)%></label></td>
			<td></td><td></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CP,EV" name="scope" id="EV" checked="checked"/></td><td><label for="EV"><%=StringUtils.text("events", session)%></label></td>
			<td><input type="checkbox" value="YR" name="scope" id="YR" checked="checked"/></td><td><label for="YR"><%=StringUtils.text("years", session)%></label></td>
			<td></td><td></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CT,CX,ST" name="scope" id="PL" checked="checked"/></td><td><label for="PL"><%=StringUtils.text("places", session)%></label></td>
			<td colspan="2"></td>
			<td style="padding-left:50px;"><label for="AL"><%=StringUtils.text("all", session)%>:</label></td><td><input type="checkbox" value="AL" name="scope" id="AL" checked="checked" onclick="toggleCheck(this);"/></td>
		</tr>
		</table>
	</fieldset>
	</form>
</div>
<%@include file="../../html/buttons.html"%>
<script><!--
function toggleCheck(cb) {
	$$('#search .scope input').each(function(el){
		$(el).checked = cb.checked;
	});
}
window.onload = function() {
	$('pattern').writeAttribute('autocomplete', 'off');
	$('pattern').activate();
	new Control.Window($(document.body).down('[href=#helplink]'),{  
		position: 'relative', hover: true, offsetLeft: 20, offsetTop: 0, className: 'tip'
	});
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>