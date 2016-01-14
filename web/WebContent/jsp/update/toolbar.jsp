<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%
	Object o = session.getAttribute("user");
	Contributor m = null;
	if (o != null && o instanceof Contributor)
		m = (Contributor) o;
%>
<table class="toolbar">
	<tr>
		<td><input id="upd-overview" type="button" class="button upd-overview" onclick="location.href='/update/overview';" value="<%=StringUtils.text("update.overview", session)%>"/></td>
		<td><input id="upd-results" type="button" class="button upd-results" onclick="location.href='/update/results';" value="<%=StringUtils.text("entity.RS", session)%>"/></td>
		<td><input id="upd-data" type="button" class="button upd-data" onclick="location.href='/update/data';" value="<%=StringUtils.text("data", session)%>"/></td>
		<td><input id="upd-pictures" type="button" class="button upd-pictures" onclick="location.href='/update/pictures';" value="<%=StringUtils.text("update.pictures", session)%>"/></td>
		<td><input id="upd-folders" type="button" class="button upd-folders" onclick="location.href='/update/folders';" value="<%=StringUtils.text("update.folders", session)%>"/></td>
		<td><input id="upd-import" type="button" class="button upd-import" onclick="location.href='/update/import';" value="<%=StringUtils.text("update.import", session)%>"/></td>
		<td onmouseover="$('toolsopt').show();" onmouseout="$('toolsopt').hide();"><input id="upd-tools" type="button" class="button upd-tools" value="<%=StringUtils.text("update.tools", session)%>"/>
			<div id="toolsopt" class="baroptions" style="display:none;"><table>
				<tr><td onclick="location.href='/update/extlinks';" class="extlinks"><%=StringUtils.text("update.extlinks", session)%></td></tr>
				<tr><td onclick="location.href='/update/translations';" class="translations"><%=StringUtils.text("update.translations", session)%></td></tr>
				<tr><td onclick="location.href='/update/query';" class="query"><%=StringUtils.text("update.query", session)%></td></tr>
				<tr><td onclick="location.href='/update/errors';" class="errors"><%=StringUtils.text("update.errors", session)%></td></tr>
				<tr><td onclick="location.href='/update/redirections';" class="redirections"><%=StringUtils.text("update.redirections", session)%></td></tr>
				<%if (m != null && m.isAdmin()) { %><tr><td onclick="location.href='/update/admin';" class="admin">Admin</td></tr><%}%>
				<tr><td onclick="$('header').setStyle({ opacity: 0.4 });$('content').setStyle({ opacity: 0.4 });dHelp.open();" class="help"><%=StringUtils.text("update.help", session)%></td></tr>
			</table></div>
		</td>
	</tr>
</table>