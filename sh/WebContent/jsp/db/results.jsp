<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.PicklistBean"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.Result"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%@ page import="com.sporthenon.utils.HtmlUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.HashMap"%>
<jsp:include page="/jsp/common/header.jsp"/>
<script type="text/javascript"><!--
var treeItems = null;
<%
	String lang = String.valueOf(session.getAttribute("locale"));
	ArrayList<Object> params = new ArrayList<Object>();
	params.add("");
	params.add("_" + lang.toLowerCase());
	HtmlConverter.convertTreeArray(DatabaseHelper.call("TreeResults", params), out, false);
%>
--></script>
<div id="tree" class="fieldset">
<div class="fstitle treetitle">
<%=StringUtils.text("tree", session)%>&nbsp;(<img style="display:none;" id="treeiconimg" src="/img/db/tree_expand.png" alt="<%=StringUtils.text("expand", session)%>" class="treeicon"/><a id="treeicontxt" href='javascript:toggleTreeExpand();'><%=StringUtils.text("expand", session)%></a>)
</div>
	<div class="treediv"><div id="treeview" class="collapsed">
		<table cellpadding="0" cellspacing="0"><tr><td>
		<script type="text/javascript">
			new Tree(treeItems, treeTemplate);
		</script>
		</td></tr></table>
	</div></div>
</div>
<div id="results" class="fieldset">
	<div class="fstitle criteria"><%=StringUtils.text("search.criteria", session)%></div>
	<form action="/results">
	<div class="spcpdiv">
	<table>
		<tr><td style="text-align:left;"><%=StringUtils.text("sport", session)%>:</td></tr>
		<tr><td><select id="pl-sp" name="pl-sp"><option/></select></td></tr>
		<tr><td><div id="slider-sp" class="slider"><%@include file="../../html/slider.html" %></div></td></tr>
	</table>
	</div>
	<div class="evdiv" style="padding-left:5px;">
	<table>
		<tr><td colspan="4" style="text-align:left;"><%=StringUtils.text("event", session)%>:</td></tr>
		<tr>
			<td colspan="4"><select disabled="disabled" class="disabled" id="pl-cp" name="pl-cp" onchange="getPicklist('pl-ev');"><option/></select></td>
		</tr>
		<tr>
			<td style="width:1px;"><img src="/img/component/treeview/join.gif" alt="L"/></td>
			<td colspan="3"><select disabled="disabled" class="disabled" id="pl-ev" name="pl-ev" onchange="getPicklist('pl-se');getPicklist('pl-yr');"><option/></select></td>
		</tr>
		<tr>
			<td style="width:1px;"><img src="/img/component/treeview/empty.gif" alt=""/></td>
			<td style="width:1px;"><img src="/img/component/treeview/join.gif" alt="L"/></td>
			<td colspan="2"><select disabled="disabled" class="disabled" id="pl-se" name="pl-se" onchange="getPicklist('pl-se2');getPicklist('pl-yr');"><option/></select></td>
		</tr>
		<tr>
			<td style="width:1px;"><img src="/img/component/treeview/empty.gif" alt=""/></td>
			<td style="width:1px;"><img src="/img/component/treeview/empty.gif" alt=""/></td>
			<td style="width:1px;"><img src="/img/component/treeview/join.gif" alt="L"/></td>
			<td><select disabled="disabled" class="disabled" id="pl-se2" name="pl-se2" onchange="getPicklist('pl-yr')"><option/></select></td>
		</tr>
		<tr><td colspan="4" style="text-align:left;padding-top:5px;"><%=StringUtils.text("years", session)%>:</td></tr>
		<tr><td colspan="4" style="padding:0;"><div id="sm-pl-yr" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
		<tr><td colspan="4" style="padding-top:15px;text-align:right;">(&dagger;&nbsp;<%=StringUtils.text("event.notheld", session)%>)</td></tr>
	</table>
	</div>
	</form>
</div>
<%@include file="../../html/buttons.html" %>
<%@include file="../../html/tabcontrol.html" %>
<%
HashMap<String, String> hSportImg = new HashMap<String, String>();
Collection<PicklistBean> cPicklist = DatabaseHelper.getPicklist(Result.class, "sport", null, null, (short) 2, lang);
for (PicklistBean plb : cPicklist)
	hSportImg.put(String.valueOf(plb.getValue()), HtmlUtils.writeImage((short)0, plb.getValue(), 'L', null, null));
%>
<script type="text/javascript">
var hSportImg = new Array();
window.onload = function() {
	<%for (String s : hSportImg.keySet()) {%>
	hSportImg[<%=s%>] = '<%=hSportImg.get(s).replaceAll("\\<img alt\\=\\'\\' src\\='|\\'\\/\\>", "")%>';	
	<%}%>
	getPicklist('pl-sp');
	initSliderRes('sp');
	initSelectMult('sm-pl-yr', TX_YEARS, 265);
	$('pl-sp').onchange = function(){
		changeSport(false);
	};
	changeSport();
	initTabControl();
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />