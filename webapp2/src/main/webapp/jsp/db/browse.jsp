<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.entity.meta.TreeItem"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<jsp:include page="/jsp/common/header.jsp"/>
<script type="text/javascript"><!--
	var treeItems = null;
<%
	String lang = String.valueOf(session.getAttribute("locale"));
	List<Object> params = new ArrayList<Object>();
	params.add("");
	params.add("_" + lang.toLowerCase());
	HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class), out, false, lang);
%>
--></script>
<div id="treeresults" class="render"></div>
<div class="treediv">
	<div id="treeview">
		<table><tr><td>
		<script type="text/javascript"><!--
			new Tree(treeItems, treeTemplate);
		--></script>
		</td></tr></table>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>