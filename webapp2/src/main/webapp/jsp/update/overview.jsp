<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%! @SuppressWarnings("unchecked") %>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-overview" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.overview", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<table id="options"><tr>
				<td><%=StringUtils.text("sport", session)%> :</td>
				<td><select id="ovsport">
				<%
					Contributor cb = (Contributor) session.getAttribute("user");
					String lang = String.valueOf(session.getAttribute("locale"));
					String sql = "SELECT * FROM sport" + (cb != null && !cb.isAdmin() ? " WHERE id IN (" + cb.getSports() + ")" : "") + " ORDER BY label" + ResourceUtils.getLocaleParam(lang);
					for (Sport sp : (List<Sport>) DatabaseManager.executeSelect(sql, Sport.class)) {
						out.print("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
					}
					if (cb != null && cb.isAdmin()) {
						out.print("<option value='0'>[" + StringUtils.text("all", session) + "]</option>");
					}
				%>
				</select></td>
				<td>&nbsp;<%=StringUtils.text("find", session)%> :</td>
				<td><input id="ovpattern" type="text" style="width:100px;"/></td>
				<td>&nbsp;<%=StringUtils.text("in", session)%> :</td>
				<td><select id="oventity">
					<option value="">-- <%=StringUtils.text("all", session)%> --</option>
					<option value="RS"><%=StringUtils.text("entity.RS", session)%></option>
					<option value="PR"><%=StringUtils.text("entity.PR", session)%></option>
					<option value="TM"><%=StringUtils.text("entity.TM", session)%></option>
					<option value="SP"><%=StringUtils.text("entity.SP", session)%></option>
					<option value="CP"><%=StringUtils.text("entity.CP", session)%></option>
					<option value="EV"><%=StringUtils.text("entity.EV", session)%></option>
					<option value="CT"><%=StringUtils.text("entity.CT", session)%></option>
					<option value="CX"><%=StringUtils.text("entity.CX", session)%></option>
				</select></td>				
				<td>&nbsp;<%=StringUtils.text("limit", session)%> :</td>
				<td><input id="ovcount" type="text" value="20" style="width:50px;"/></td>
				<td><input id="showimg" type="checkbox"/></td>
				<td><label for="showimg"><%=StringUtils.text("show.img", session)%></label>&nbsp;</td>
				<td><input type="button" value="<%=StringUtils.text("search.for", session)%>" onclick="loadOverview();"/></td>
			</tr></table>
			<div style="position:absolute;top:120px;right:25px;">
				<fieldset>
					<legend><%=StringUtils.text("year.report", session)%></legend>
					<input id="year" type="text" value="<%=Calendar.getInstance().get(Calendar.YEAR)%>" style="width:50px;"/>&nbsp;<input type="button" value="OK" onclick="executeQuery(2);"/>
				</fieldset>
			</div>
			<div id="ovcontent"></div>
		</div>
	</div>
</div>
<script><!--
window.onload = function() {
	loadOverview();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>