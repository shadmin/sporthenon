<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-extlinks" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.extlinks", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<table class="toolbar" style="position:relative;top:0;right:0;float:right;">
				<tr>
					<td id="msg"></td>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveExtLinks();" value="<%=StringUtils.text("save", session)%>"/></td>
				</tr>
			</table>
			<table id="options"><tr>
				<td><%=StringUtils.text("sport", session)%>&nbsp;:</td>
				<td><select id="elsport" style="width:200px;">
				<%
					Contributor cb = (Contributor) session.getAttribute("user");
					String lang = String.valueOf(session.getAttribute("locale"));
					for (Sport sp : (List<Sport>) DatabaseHelper.execute("from Sport" + (cb != null && !cb.isAdmin() ? " where id in (" + cb.getSports() + ")" : "") + " order by label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "")))
						out.print("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
					if (cb != null && cb.isAdmin())
						out.print("<option value='0'>[" + StringUtils.text("all", session) + "]</option>");
				%>
				</select></td>
				<td><%=StringUtils.text("count", session)%>&nbsp;:</td>
				<td><input id="elcount" type="text" value="100" style="width:80px;"/>
				<td>ID max.&nbsp;:</td>
				<td><input id="elidmax" type="text" value="100000" style="width:80px;"/>
				<td><input id="elincludechecked" type="checkbox"/></td>
				<td><label for="elincludechecked"><%=StringUtils.text("include.checked.links", session)%></label>&nbsp;</td>
				<td><input type="button" value="OK" onclick="loadExtLinks();"/></td>
				<td><input type="button" value="<%=StringUtils.text("automatic.update", session)%>" onclick="updateLinksAuto();"/></td>
			</tr><tr>
				<td><%=StringUtils.text("show", session)%>&nbsp;:</td>
				<td><select id="elentity" style="width:200px;">
				<option value="PR"><%=StringUtils.text("entity.PR", session)%></option>
				<option value="CP"><%=StringUtils.text("entity.CP", session)%></option>
				<option value="CT"><%=StringUtils.text("entity.CT", session)%></option>
				<option value="CX"><%=StringUtils.text("entity.CX", session)%></option>
				<option value="CN"><%=StringUtils.text("entity.CN", session)%></option>
				<option value="EV"><%=StringUtils.text("entity.EV", session)%></option>
				<option value="OL"><%=StringUtils.text("entity.OL", session)%></option>
				<option value="RS"><%=StringUtils.text("entity.RS", session)%></option>
				<option value="SP"><%=StringUtils.text("entity.SP", session)%></option>
				<option value="ST"><%=StringUtils.text("entity.ST", session)%></option>
				<option value="TM"><%=StringUtils.text("entity.TM", session)%></option>
				</select></td>
				<td><%=StringUtils.text("find", session)%>&nbsp;:</td>
				<td><input id="elpattern" type="text" style="width:100px;"/>			
			</tr></table>
			<div id="elcontent"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	loadExtLinks();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>