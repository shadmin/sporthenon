<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%
	Object o = session.getAttribute("user");
	Contributor m = null;
	if (o != null && o instanceof Contributor)
		m = (Contributor) o;
%>
<table class="toolbar" style="float:right;margin-top:10px;">
	<tr>
		<td><input id="upd-overview" type="button" class="button upd-overview" onclick="location.href='/update/overview';" value="<%=StringUtils.text("update.overview", session)%>"/></td>
		<td><input id="upd-results" type="button" class="button upd-results" onclick="location.href='/update/results';" value="<%=StringUtils.text("entity.RS", session)%>"/></td>
		<td><input id="upd-data" type="button" class="button upd-data" onclick="location.href='/update/data';" value="<%=StringUtils.text("data", session)%>"/></td>
		<td><input id="upd-pictures" type="button" class="button upd-pictures" onclick="location.href='/update/pictures';" value="<%=StringUtils.text("update.pictures", session)%>"/></td>
		<td><input id="upd-import" type="button" class="button upd-import" onclick="location.href='/update/import';" value="<%=StringUtils.text("update.import", session)%>"/></td>
		<td><input id="upd-tools" type="button" class="button upd-tools" onclick="location.href='/update/tools';" value="<%=StringUtils.text("update.tools", session)%>"/></td>
		<% if (m != null && m.isAdmin()) { %>
		<td><input id="upd-admin" type="button" class="button upd-admin" onclick="location.href='/update/admin';" value="Admin"/></td>
		<% } %>
		<td><input id="upd-help" type="button" class="button upd-help" onclick="$('header').setStyle({ opacity: 0.4 });$('content').setStyle({ opacity: 0.4 });dHelp.open();" value="<%=StringUtils.text("update.help", session)%>"/></td>
	</tr>
</table>