<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.PicklistItem"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.entity.Result"%>
<%@ page import="com.sporthenon.db.entity.meta.TreeItem"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%@ page import="com.sporthenon.utils.HtmlUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="java.util.*"%>
<jsp:include page="/jsp/common/header.jsp"/>
<script type="text/javascript"><!--
	var treeItems = null;
<%
	String lang = String.valueOf(session.getAttribute("locale"));
	List<Object> params = new ArrayList<Object>();
	params.add("");
	params.add(ResourceUtils.getLocaleParam(lang));
	HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class), out, false, lang);
%>
--></script>
<div id="title-results" class="title">
	<div><%=StringUtils.text("menu.results", session)%></div>
</div>
<div id="results">
	<form action="/results">
		<ul>
		<li class="spcpdiv">
			<table class="noborder">
				<tr><td style="text-align:left;"><img src='/img/bullet.gif' alt='-'/>&nbsp;<%=StringUtils.text("sport", session)%></td></tr>
				<tr><td><select id="pl-sp" name="pl-sp"><option/></select></td></tr>
				<tr><td><div id="slider-sp" class="slider"><%@include file="../../html/slider.html"%></div></td></tr>
			</table>
		</li>
		<li class="evdiv">
			<table class="noborder">
				<tr><td colspan="4" style="text-align:left;"><img src='/img/bullet.gif' alt='-'/>&nbsp;<%=StringUtils.text("event", session)%></td></tr>
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
				<tr><td colspan="4" style="padding-top:15px;text-align:right;">(&dagger; <%=StringUtils.text("event.notheld", session)%>)</td></tr>
			</table>
		</li>
		<li class="yrdiv">
			<table class="noborder">
				<tr><td colspan="4" style="text-align:left;padding-top:5px;"><img src='/img/bullet.gif' alt='-'/>&nbsp;<%=StringUtils.text("years", session)%></td></tr>
				<tr><td colspan="4" style="padding:0;"><div id="sm-pl-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>			
			</table>
		</li>
		</ul>
	</form>
</div>
<%@include file="../../html/buttons.html"%>
<%
	HashMap<String, String> hSportImg = new HashMap<String, String>();
	String sql = "SELECT SP.id, SP.label" + ResourceUtils.getLocaleParam(lang)
			+ " FROM sport SP "
			+ " WHERE SP.id IN (SELECT id_sport FROM result)"
			+ " ORDER by 2";
	Collection<PicklistItem> cPicklist = DatabaseManager.getPicklist(sql, null);
	for (PicklistItem plb : cPicklist) {
		hSportImg.put(String.valueOf(plb.getValue()), HtmlUtils.writeImage((short)0, plb.getValue(), 'L', null, null));
	}
%>
<script type="text/javascript"><!--
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
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>