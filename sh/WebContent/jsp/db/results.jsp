<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.PicklistBean"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.Result"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.converter.HtmlConverter"%>
<%@ page import="com.sporthenon.utils.HtmlUtils"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.HashMap"%>
<jsp:include page="/jsp/common/header.jsp"/>
<script type="text/javascript">
<%HtmlConverter.convertTreeJSON(DatabaseHelper.call("TreeResults", null), out);%>
</script>
<div id="results" class="fieldset">
	<div class="fstitle criteria">SEARCH CRITERIA</div>
	<form action="/results">
	<div class="spcpdiv">
	<table>
		<tr><td style="text-align:left;">Sport:</td></tr>
		<tr>
			<td>
			<select id="pl-sp" name="pl-sp" onchange="changeSport()">
			<%
			HashMap<String, String> hSportImg = new HashMap<String, String>();
			Collection<PicklistBean> cPicklist = DatabaseHelper.getPicklist(Result.class, "sport", null, null, (short) 2);
			int i = 0;
			for (PicklistBean plb : cPicklist) {
				out.println("<option class='" + (i++ % 2 == 0 ? "alternative" : "") + "' value='" + plb.getValue() + "'>" + plb.getText() + "</option>");
				hSportImg.put(String.valueOf(plb.getValue()), HtmlUtils.writeImage((short)0, plb.getValue(), 'L', false));
			}
			%>
			</select>
			</td>
		</tr>
		<tr><td><div id="slider-sp" class="slider"><%@include file="../../html/slider.html" %></div></td></tr>
	</table>
	</div>
	<div class="evdiv" style="padding-left:5px;">
	<table>
		<tr><td colspan="3" style="text-align:left;">Event:</td></tr>
		<tr>
			<td colspan="3"><select disabled="disabled" class="disabled" id="pl-cp" name="pl-cp" onchange="getPicklist('pl-ev');"><option/></select></td>
		</tr>
		<tr>
			<td style="width:1px;"><img src="img/component/treeview/join.gif" alt="L"/></td>
			<td colspan="2"><select disabled="disabled" class="disabled" id="pl-ev" name="pl-ev" onchange="getPicklist('pl-se');getPicklist('pl-yr');"><option/></select></td>
		</tr>
		<tr>
			<td style="width:1px;"><img src="img/component/treeview/empty.gif" alt=""/></td>
			<td style="width:1px;"><img src="img/component/treeview/join.gif" alt="L"/></td>
			<td><select disabled="disabled" class="disabled" id="pl-se" name="pl-se" onchange="getPicklist('pl-yr')"><option/></select></td>
		</tr>
		<tr><td colspan="3" style="text-align:left;">Years:</td></tr>
		<tr><td colspan="3" style="padding:0;"><div id="sm-pl-yr" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
		<tr><td colspan="3" style="padding-top:32px;text-align:right;">(&dagger;&nbsp;Event not held anymore)</td></tr>
	</table>
	</div>
	<div class="treediv" style="padding-left:5px;">
	<table>
		<tr><td style="text-align:left;">Select in tree:</td></tr>
		<tr><td>
			<div id="treeview" class="collapsed">
				<table cellpadding="0" cellspacing="0"><tr><td>
				<script type="text/javascript">
					new Tree(treeItems, treeTemplate);
				</script>
				</td></tr></table>
			</div>
		</td></tr>
	</table>
	<div id="treeicon">
		<img id="treeiconimg" src="img/db/tree_expand.png" alt="Expand Tree" title="Expand Tree" class="treeicon" onclick="toggleTreeExpand();"/>
	</div>
	</div>
	</form>
</div>
<%@include file="../../html/buttons.html" %>
<%@include file="../../html/tabcontrol.html" %>
<script type="text/javascript">
	var hSportImg = new Array();
	<%for (String s : hSportImg.keySet()) {%>
	hSportImg[<%=s%>] = "<%=hSportImg.get(s)%>";	
	<%}%>
	initSelectMult('sm-pl-yr', 'Years', 265);
	initSliderRes('sp');
	changeSport();
</script>
<jsp:include page="/jsp/common/footer.jsp" />