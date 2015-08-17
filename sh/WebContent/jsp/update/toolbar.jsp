<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<table class="toolbar" style="float:right;margin-top:10px;">
	<tr>
		<td><input id="upd-overview" type="button" class="button upd-overview" onclick="location.href='/update/overview';" value="<%=StringUtils.text("update.overview", session)%>"/></td>
		<td><input id="upd-results" type="button" class="button upd-results" onclick="location.href='/update/results';" value="<%=StringUtils.text("update.results", session)%>"/></td>
		<td><input id="upd-data" type="button" class="button upd-data" onclick="location.href='/update/data';" value="<%=StringUtils.text("update.data", session)%>"/></td>
		<td><input id="upd-help" type="button" class="button upd-help" onclick="$('header').setStyle({ opacity: 0.4 });$('content').setStyle({ opacity: 0.4 });dHelp.open();" value="<%=StringUtils.text("update.help", session)%>"/></td>
	</tr>
</table>