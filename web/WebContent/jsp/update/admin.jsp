<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-admin" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.users", session).toUpperCase()%></div>
		<div class="fscontent" style="padding-bottom:60px;">
			<table id="table-CB">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="cb-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("loginid", session)%></th><td><input type="text" id="cb-login"/></td></tr>
				<tr><th><%=StringUtils.text("public.name", session)%></th><td><input type="text" id="cb-name"/></td></tr>
				<tr><th><%=StringUtils.text("email.addr", session)%></th><td><input type="text" id="cb-email"/></td></tr>
				<tr><th><%=StringUtils.text("active", session)%></th><td><input type="checkbox" id="cb-active"/></td></tr>
				<tr><th><%=StringUtils.text("admin", session)%></th><td><input type="checkbox" id="cb-admin"/></td></tr>
				<tr><th><%=StringUtils.text("sports", session)%></th><td><input type="text" id="cb-sports"/></td></tr>
			</table>
			<table class="toolbar" style="position:relative;float:right;top:0;right:0;">
				<tr>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveEntity();" value="<%=StringUtils.text("save", session)%>"/></td>
				</tr>
			</table><br/>
			<table class="toolbar" style="position:relative;clear:right;float:right;top:0;right:0;">
				<tr>
					<td><input id="upd-first" type="button" class="button upd-first" onclick="loadEntity('first');" value="<%=StringUtils.text("first", session)%>"/></td>
					<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadEntity('previous');" value="<%=StringUtils.text("previous", session)%>"/></td>
					<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity();" value="<%=StringUtils.text("find", session)%>"/></td>
					<td><input id="upd-next" type="button" class="button upd-next" onclick="loadEntity('next');" value="<%=StringUtils.text("next", session)%>"/></td>
					<td><input id="upd-last" type="button" class="button upd-last" onclick="loadEntity('last');" value="<%=StringUtils.text("last", session)%>"/></td>
				</tr>
			</table>
			<div id="msg" style="float:left;"></div>
		</div>
	</div>
	<div class="fieldset" style="margin-top:10px;">
		<div class="fstitle config"><%=StringUtils.text("update.config", session).toUpperCase()%></div>
		<div class="fscontent">
		<table id="tconfig" style="width:500px;"><tr><th>Key</th><th>Value</th></tr>
		<%
			for (Config c : (List<Config>) DatabaseHelper.execute("from Config order by key")) {
				out.print("<tr><td><b>" + c.getKey() + "</b></td>");
				if (c.getKey().startsWith("html"))
					out.print("<td><textarea id='" + c.getKey() + "' rows='8' cols='150'>" + c.getValueHtml() + "</textarea></td></tr>");
				else
					out.print("<td><input id='" + c.getKey() + "' type='text' value='" + c.getValue() + "'/></td></tr>");
			}
		%>
		</table>
		<table class="toolbar" style="position:relative;clear:right;float:right;top:0;right:0;">
			<tr>
				<td><input id="upd-save" type="button" class="button upd-save" onclick="saveConfig();" value="<%=StringUtils.text("save", session)%>"/></td>
			</tr>
		</table>
		<div id="msg2" style="float:left;"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	currentAlias = 'CB';
	loadEntity('last');
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />