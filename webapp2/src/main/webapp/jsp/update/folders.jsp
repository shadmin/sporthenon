<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.entity.*"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.PicklistItem"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%! @SuppressWarnings("unchecked") %>
<jsp:include page="/jsp/common/header.jsp"/>
<%
	Contributor cb = (Contributor) session.getAttribute("user");
	String lang = String.valueOf(session.getAttribute("locale"));
	String label = "label" + ResourceUtils.getLocaleParam(lang);
	StringBuffer sbSport = new StringBuffer();
	StringBuffer sbChampionship = new StringBuffer();
	StringBuffer sbEvent = new StringBuffer();
	String sql = "SELECT * FROM sport" + (cb != null && !cb.isAdmin() ? " WHERE id IN (" + cb.getSports() + ")" : "") + " ORDER BY " + label;
	for (Sport sp : (List<Sport>) DatabaseManager.executeSelect(sql, Sport.class)) {
		out.print("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
	}
	sql = "SELECT id, " + label + " FROM championship";
	for (PicklistItem plb : DatabaseManager.getPicklist(sql, null)) {
		sbChampionship.append("<option value='" + plb.getValue() + "'>" + plb.getText() + "</option>");
	}
	sql = "SELECT EV.id, EV." + label + ", TP." + label + " FROM event EV JOIN type TP ON TP.id = EV.id_type ORDER BY EV." + label;
	for (PicklistItem plb : DatabaseManager.getPicklist(sql, null)) {
		sbEvent.append("<option value='" + plb.getValue() + "'>" + plb.getText() + " (" + plb.getParam() + ")" + "</option>");
	}
%>
<div id="update-folders" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.folders", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<table class="toolbar" style="position:relative;top:0;right:0;float:right;">
				<tr>
					<td id="msg"></td>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveFolders();" value="<%=StringUtils.text("save", session)%>"/></td>
				</tr>
			</table>
			<table id="options"><tr>
				<td><select id="list1" multiple="multiple" size="20"></select></td>
				<td><input type="button" value="&gt;" onclick="moveFolder('list1', 'list2');"/><br/><input type="button" value="&lt;" onclick="moveFolder('list2', 'list1');"/></td>
				<td><select id="list2" multiple="multiple" size="20"></select></td>
			</tr>
			<tr><td><%=StringUtils.text("entity.SP.1", session)%> :</td></tr>
			<tr><td><select id="sp"><option value=""></option><%=sbSport.toString()%></select></td><td></td><td><input type="checkbox" id="cb1"/><label for="cb1"><%=StringUtils.text("automatic.subevent", session)%></label></td></tr>
			<tr><td><%=StringUtils.text("entity.CP.1", session)%> :</td><td></td><td><input type="checkbox" id="cb2"/><label for="cb2"><%=StringUtils.text("clear.event", session)%> #2</label></td></tr>
			<tr><td><select id="cp"><option value=""></option><%=sbChampionship.toString()%></select></td><td></td><td><input type="checkbox" id="cb3"/><label for="cb3"><%=StringUtils.text("clear.event", session)%> #3</label></td></tr>
			<tr><td><%=StringUtils.text("entity.EV.1", session)%> #1 :</td></tr>
			<tr><td><select id="ev1"><option value=""></option><%=sbEvent.toString()%></select></td></tr>
			<tr><td><%=StringUtils.text("entity.EV.1", session)%> #2 :</td></tr>
			<tr><td><select id="ev2"><option value=""></option><%=sbEvent.toString()%></select></td></tr>
			<tr><td><%=StringUtils.text("entity.EV.1", session)%> #3 :</td></tr>
			<tr><td><select id="ev3"><option value=""></option><%=sbEvent.toString()%></select></td></tr>
			</table>
		</div>
	</div>
</div>
<script><!--
window.onload = function() {
	loadFolders();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>