<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.entity.*"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.PicklistBean"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<%
	Contributor cb = (Contributor) session.getAttribute("user");
	String lang = String.valueOf(session.getAttribute("locale"));
	StringBuffer sbSport = new StringBuffer();
	StringBuffer sbChampionship = new StringBuffer();
	StringBuffer sbEvent = new StringBuffer();
	for (Sport sp : (List<Sport>) DatabaseHelper.execute("from Sport" + (cb != null && !cb.isAdmin() ? " where id in (" + cb.getSports() + ")" : "") + " order by label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "")))
		sbSport.append("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
	for (PicklistBean plb : DatabaseHelper.getEntityPicklist(Championship.class, "label", null, lang))
		sbChampionship.append("<option value='" + plb.getValue() + "'>" + plb.getText() + "</option>");
	for (PicklistBean plb : DatabaseHelper.getEntityPicklist(Event.class, "label", "type.label", lang))
		sbEvent.append("<option value='" + plb.getValue() + "'>" + plb.getText() + " (" + plb.getParam() + ")" + "</option>");
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
			<tr><td><%=StringUtils.text("entity.SP.1", session)%>&nbsp;:</td></tr>
			<tr><td><select id="sp"><option value=""></option><%=sbSport.toString()%></select></td><td/><td><input type="checkbox" id="cb1"/><label for="cb1"><%=StringUtils.text("automatic.subevent", session)%></label></td></tr>
			<tr><td><%=StringUtils.text("entity.CP.1", session)%>&nbsp;:</td><td/><td><input type="checkbox" id="cb2"/><label for="cb2"><%=StringUtils.text("clear.event", session)%> #2</label></td></tr>
			<tr><td><select id="cp"><option value=""></option><%=sbChampionship.toString()%></select></td><td/><td><input type="checkbox" id="cb3"/><label for="cb3"><%=StringUtils.text("clear.event", session)%> #3</label></td></tr>
			<tr><td><%=StringUtils.text("entity.EV.1", session)%>&nbsp;#1&nbsp;:</td></tr>
			<tr><td><select id="ev1"><option value=""></option><%=sbEvent.toString()%></select></td></tr>
			<tr><td><%=StringUtils.text("entity.EV.1", session)%>&nbsp;#2&nbsp;:</td></tr>
			<tr><td><select id="ev2"><option value=""></option><%=sbEvent.toString()%></select></td></tr>
			<tr><td><%=StringUtils.text("entity.EV.1", session)%>&nbsp;#3&nbsp;:</td></tr>
			<tr><td><select id="ev3"><option value=""></option><%=sbEvent.toString()%></select></td></tr>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	loadFolders();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>