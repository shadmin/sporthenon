<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-overview" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.overview", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<table id="options"><tr>
				<td><%=StringUtils.text("sport", session)%>&nbsp;:</td>
				<td><select id='ovsport'>
				<%
					String lang = String.valueOf(session.getAttribute("locale"));
					for (Sport sp : (List<Sport>) DatabaseHelper.execute("from Sport order by label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "")))
						out.print("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
				%>
				<option value="0">[<%=StringUtils.text("all", session)%>]</option>
				</select></td>
				<td><%=StringUtils.text("count", session)%>&nbsp;:</td>
				<td><input id="ovcount" type="text" value="50" style="width:50px;"/>
				<td><%=StringUtils.text("find", session)%>&nbsp;:</td>
				<td><input id="ovpattern" type="text" style="width:100px;"/>
				<td><input type="button" value="OK" onclick="loadOverview();"/></td>
			</tr></table>
			<div id="ovcontent"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	loadOverview();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />