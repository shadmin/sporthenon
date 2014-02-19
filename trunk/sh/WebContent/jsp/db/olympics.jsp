<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.PicklistBean"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.Olympics"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.converter.HtmlConverter"%>
<%@ page import="com.sporthenon.utils.HtmlUtils"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.HashMap"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="olympics" class="fieldset">
	<div class="fstitle criteria">SEARCH CRITERIA</div>
	<form id="olympics-form" action="/olympics">
	<!-- TYPE SELECTION -->
	<div id="oltype">
		<div onclick="$('olt1').checked = true;$('olt1').onclick();">
			<img alt="Summer" src="img/db/summer2.png"/><br/>
			<input type="radio" name="ol-type" id="olt1" checked="checked" onclick="changeModeOL()"/><br/>
			<b>Summer Olympic Games</b>
		</div>
		<div style="clear:left;margin-top:0px;" onclick="$('olt2').checked = true;$('olt2').onclick();">
			<img alt="Winter" src="img/db/winter2.png"/><br/>
			<input type="radio" name="ol-type" id="olt2" onclick="changeModeOL()"/><br/>
			<b>Winter Olympic Games</b>
		</div>
	</div>
	<!-- SUMMER -->
	<div id="summerfs" class="fieldset">
		<div id="slider-summer-ol" class="slider"><%@include file="../../html/slider.html" %></div>
		<table id="summer-tb" style="margin-top:5px;">
			<tr><td style="padding-bottom:3px;"><div id="sm-summer-pl-ol" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			<tr><td colspan="2" style="padding-bottom:3px;border-top:1px solid #BBB;border-bottom:1px solid #BBB;"><table cellpadding="0" cellspacing="0" style="margin-left:2px;margin-top:5px;margin-bottom:0px;width:0%;"><tr>
				<td><input type="radio" name="summer-q" id="sq1" checked="checked" onclick="$('summer-q1').show();$('summer-q2').hide();" style="margin:0px;margin-top:3px;"/></td>
				<td style="padding-left:2px;"><label for="sq1">Event&nbsp;Results</label>&nbsp;&nbsp;&nbsp;</td>
				<td><input type="radio" name="summer-q" id="sq2" onclick="$('summer-q2').show();$('summer-q1').hide();" style="margin:0px;margin-top:3px;"/></td>
				<td style="padding-left:2px;"><label for="sq2">Medals&nbsp;Tables</label></td>
			</tr></table></td></tr>
			<!-- EVENTS RESULTS -->
			<tr><td colspan="2"><table id="summer-q1" cellpadding="0" cellspacing="0">
				<tr><td rowspan="5"><div id="slider-summer-sp" class="slider" style="width:110px;"><%@include file="../../html/slider.html" %></div></td>
				<td colspan="2" style="padding:2px;padding-top:0px;">Sport:</td></tr>
				<tr><td colspan="2" class="select"><select id="summer-pl-sp" name="summer-pl-sp" onchange="changeSportOL(this, 'summer', false);"><option/></select></td></tr>
				<tr><td colspan="2" style="padding:2px;">Event:</td></tr>
				<tr><td colspan="2"><div id="sm-summer-pl-ev" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
				<tr><td style="width:0px;"><img src="img/component/treeview/join.gif" alt="L"/></td><td style="padding-left:5px;"><div id="sm-summer-pl-se" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			</table></td></tr>
			<!-- MEDALS TABLES -->
			<tr><td  colspan="2"><table id="summer-q2" style="display:none;" cellpadding="0" cellspacing="0">
				<tr><td style="padding: 2px;">Country:</td></tr>
				<tr><td><div id="sm-summer-pl-cn" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			</table></td></tr>
		</table>
	</div>
	<!-- WINTER -->
	<div id="winterfs" class="fieldset">
		<div id="slider-winter-ol" class="slider"><%@include file="../../html/slider.html" %></div>
		<table id="winter-tb" style="margin-top:5px;">
			<tr><td style="padding-bottom:3px;"><div id="sm-winter-pl-ol" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			<tr><td colspan="2" style="padding-bottom:3px;border-top:1px solid #BBB;border-bottom:1px solid #BBB;"><table cellpadding="0" cellspacing="0" style="margin-left:2px;margin-top:5px;margin-bottom:0px;width:0%;"><tr>
				<td><input type="radio" name="winter-q" id="wq1" checked="checked" onclick="$('winter-q1').show();$('winter-q2').hide();" style="margin:0px;margin-top:3px;"/></td>
				<td style="padding-left:2px;"><label for="wq1">Event&nbsp;Results</label>&nbsp;&nbsp;&nbsp;</td>
				<td><input type="radio" name="winter-q" id="wq2" onclick="$('winter-q2').show();$('winter-q1').hide();" style="margin:0px;margin-top:3px;"/></td>
				<td style="padding-left:2px;"><label for="wq2">Medals&nbsp;Tables</label></td>
			</tr></table></td></tr>
			<!-- EVENTS RESULTS -->
			<tr><td colspan="2"><table id="winter-q1" cellpadding="0" cellspacing="0">
				<tr><td rowspan="5"><div id="slider-winter-sp" class="slider" style="width:110px;"><%@include file="../../html/slider.html" %></div></td>
				<td colspan="2" style="padding:2px;padding-top:0px;">Sport:</td></tr>
				<tr><td colspan="2" class="select"><select id="winter-pl-sp" name="winter-pl-sp" onchange="changeSportOL(this, 'winter', false);"><option/></select></td></tr>
				<tr><td colspan="2" style="padding:2px;">Event:</td></tr>
				<tr><td colspan="2"><div id="sm-winter-pl-ev" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
				<tr><td style="width:0px;"><img src="img/component/treeview/join.gif" alt="L"/></td><td style="padding-left:5px;"><div id="sm-winter-pl-se" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			</table></td></tr>
			<!-- MEDALS TABLES -->
			<tr><td  colspan="2"><table id="winter-q2" style="display:none;" cellpadding="0" cellspacing="0">
				<tr><td style="padding: 2px;">Country:</td></tr>
				<tr><td><div id="sm-winter-pl-cn" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			</table></td></tr>
		</table>
	</div>
	</form>
</div>
<%@include file="../../html/buttons.html" %>
<%@include file="../../html/tabcontrol.html" %>
<%
HashMap<String, String> hOlympicsImg = new HashMap<String, String>();
HashMap<String, String> hSportImg = new HashMap<String, String>();
Collection<PicklistBean> cPicklist = DatabaseHelper.getEntityPicklist(Olympics.class, "id", null);
for (PicklistBean plb : cPicklist)
	hOlympicsImg.put(String.valueOf(plb.getValue()), HtmlUtils.writeImage((short)3, plb.getValue(), 'S', null, false));
cPicklist = DatabaseHelper.getEntityPicklist(Sport.class, "id", null);
for (PicklistBean plb : cPicklist)
	hSportImg.put(String.valueOf(plb.getValue()), HtmlUtils.writeImage((short)0, plb.getValue(), 'L', null, false));
%>
<script type="text/javascript">
var hOlympicsImg = new Array();
var hSportImg = new Array();
window.onload = function() {
	<%for (String s : hOlympicsImg.keySet()) {%>
	hOlympicsImg[<%=s%>] = '<%=hOlympicsImg.get(s).replaceAll("\\<img alt\\=\\'\\' src\\='|\\'\\/\\>", "")%>';	
	<%}%>
	<%for (String s : hSportImg.keySet()) {%>
	hSportImg[<%=s%>] = '<%=hSportImg.get(s).replaceAll("\\<img alt\\=\\'\\' src\\='|\\'\\/\\>", "")%>';	
	<%}%>
	initSelectMult('sm-summer-pl-ol', 'Olympic Games', 403);
	initSelectMult('sm-winter-pl-ol', 'Olympic Games', 403);
	initSelectMult('sm-summer-pl-ev', 'Events', 295);
	initSelectMult('sm-summer-pl-se', 'Events', 271);
	initSelectMult('sm-winter-pl-ev', 'Events', 295);
	initSelectMult('sm-winter-pl-se', 'Events', 271);
	initSelectMult('sm-summer-pl-cn', 'Countries', 405);
	initSelectMult('sm-winter-pl-cn', 'Countries', 405);
	$('summer-pl-ol').onchange = function() {
		changeOlympics(this.id);
		updateTip(this.id);
	};
	$('winter-pl-ol').onchange = function() {
		changeOlympics(this.id);
		updateTip(this.id);
	};
	createSlider('slider-summer-ol', 400, 50);
	createSlider('slider-summer-sp', 80, 80);
	createSlider('slider-winter-ol', 400, 50);
	createSlider('slider-winter-sp', 80, 80);
	initOlympics('summer-pl-ol');
	initOlympics('winter-pl-ol');
	changeModeOL();
	initTabControl();
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />