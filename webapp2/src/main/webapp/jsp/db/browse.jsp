<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.entity.meta.TreeItem"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<script><!--
	var treeItems = null;
<%
	String lang = String.valueOf(session.getAttribute("locale"));
	List<Object> params = new ArrayList<Object>();
	params.add("");
	params.add(ResourceUtils.getLocaleParam(lang));
	HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class), out, false, lang);
%>
--></script>
<div id="browse">
	<div id="title-browse" class="title">
		<div><%=StringUtils.text("menu.browse", session)%></div>
	</div>
	<div style="display:flex;">
		<div class="treediv">
			<div id="treeview">
				<table><tr><td>
				<script><!--
					new Tree(treeItems, treeTemplate);
				--></script>
				</td></tr></table>
			</div>
		</div>
		<div id="treeresults" class="render"></div>
	</div>
</div>
<script><!--
window.onload = function() {
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>