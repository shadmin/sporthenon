<%@ page language="java" %>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="olympics" class="fieldset">
	<div class="fstitle criteria">SEARCH CRITERIA</div>
	<form id="olympics-form" action="/olympics">
	<!-- SUMMER -->
	<div class="fieldset">
		<div class="legend">
			<table><tr><td><input type="radio" name="ol-type" id="olt1" checked="checked" onclick="changeModeOL()"></input></td>
			<td><img src="img/db/summer.png" alt="Summer"/></td><td><label for="olt1">Summer Olympic Games</label></td></tr></table>
		</div>
		<div id="summer-inactive" class="inactive-msg" style="width:275px;height:165px;"></div>
		<div id="slider-summer-ol" class="slider"><%@include file="../../html/slider.html" %></div>
		<table id="summer-tb" style="margin-top:5px;">
			<tr><td><div id="sm-summer-pl-ol" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			<tr><td colspan="2"><hr class="separator"/></td></tr>
			<tr><td colspan="2"><table cellpadding="0" cellspacing="0" style="margin-left:2px;margin-top:2px;width:0%;"><tr>
				<td><input type="radio" name="summer-q" id="sq1" checked="checked" onclick="$('summer-q1').show();$('summer-q2').hide();" style="margin:0px;"/></td>
				<td style="padding-left:2px;"><label for="sq1">Event&nbsp;results</label>&nbsp;</td>
				<td><input type="radio" name="summer-q" id="sq2" onclick="$('summer-q2').show();$('summer-q1').hide();" style="margin:0px;"/></td>
				<td style="padding-left:2px;"><label for="sq2">Medals&nbsp;tables</label></td>
			</tr></table></td></tr>
			<tr><td  colspan="2"><hr class="separator"/></td></tr>
			<!-- EVENTS RESULTS -->
			<tr><td colspan="2"><table id="summer-q1" cellpadding="0" cellspacing="0">
				<tr><td colspan="2" style="padding:2px;padding-top:0px;">Sport:</td></tr>
				<tr><td colspan="2" class="select"><select id="summer-pl-sp" name="summer-pl-sp" onchange="getPicklistOL('summer-pl-ev', true);updateTip(this.id);"><option/></select></td></tr>
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
	<div class="fieldset">
		<div class="legend">
			<table><tr><td><input type="radio" name="ol-type" id="olt2" onclick="changeModeOL()"></input></td>
			<td><img src="img/db/winter.png" alt="Winter"/></td><td><label for="olt2">Winter Olympic Games</label></td></tr></table>
		</div>
		<div id="winter-inactive" class="inactive-msg" style="width:275px;height:165px;"></div>
		<div id="slider-winter-ol" class="slider"><%@include file="../../html/slider.html" %></div>
		<table id="winter-tb" style="margin-top:5px;">
			<tr><td><div id="sm-winter-pl-ol" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			<tr><td colspan="2"><hr class="separator"/></td></tr>
			<tr><td colspan="2"><table cellpadding="0" cellspacing="0" style="margin-left:2px;margin-top:2px;width:0%;"><tr>
				<td><input type="radio" name="winter-q" id="wq1" checked="checked" onclick="$('winter-q1').show();$('winter-q2').hide();" style="margin:0px;"/></td>
				<td style="padding-left:2px;"><label for="wq1">Event&nbsp;results</label>&nbsp;</td>
				<td><input type="radio" name="winter-q" id="wq2" onclick="$('winter-q2').show();$('winter-q1').hide();" style="margin:0px;"/></td>
				<td style="padding-left:2px;"><label for="wq2">Medals&nbsp;tables</label></td>
			</tr></table></td></tr>
			<tr><td colspan="2"><hr class="separator"/></td></tr>
			<!-- EVENTS RESULTS -->
			<tr><td colspan="2"><table id="winter-q1" cellpadding="0" cellspacing="0">
				<tr><td colspan="2" style="padding:2px;padding-top:0px;">Sport:</td></tr>
				<tr><td colspan="2" class="select"><select id="winter-pl-sp" name="winter-pl-sp" onchange="getPicklistOL('winter-pl-ev', true);updateTip(this.id);"><option/></select></td></tr>
				<tr><td colspan="2" style="padding: 2px;">Event:</td></tr>
				<tr><td colspan="2"><div id="sm-winter-pl-ev" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
				<tr><td style="width:0px;"><img src="img/component/treeview/join.gif" alt="L"/></td><td style="padding-left:5px;"><div id="sm-winter-pl-se" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			</table></td></tr>
			<!-- MEDALS TABLES -->
			<tr><td colspan="2"><table id="winter-q2" style="display:none;" cellpadding="0" cellspacing="0">
				<tr><td style="padding: 2px;">Country:</td></tr>
				<tr><td><div id="sm-winter-pl-cn" class="selmultiple"><%@include file="../../html/selectmult.html" %></div></td></tr>
			</table></td></tr>
		</table>
	</div>
	
	</form>
</div>
<%@include file="../../html/buttons.html" %>
<%@include file="../../html/tabcontrol.html" %>
<script type="text/javascript">
	initSelectMult('sm-summer-pl-ol', 'Olympic Games', 255);
	initSelectMult('sm-winter-pl-ol', 'Olympic Games', 255);
	initSelectMult('sm-summer-pl-ev', 'Events', 255);
	initSelectMult('sm-summer-pl-se', 'Events', 230);
	initSelectMult('sm-winter-pl-ev', 'Events', 255);
	initSelectMult('sm-winter-pl-se', 'Events', 230);
	initSelectMult('sm-summer-pl-cn', 'Countries', 255);
	initSelectMult('sm-winter-pl-cn', 'Countries', 255);
	$('summer-pl-ol').observe('change', function(el){
		changeOlympics(this.id);
		updateTip(this.id);
	}.bind($('summer-pl-ol')));
	$('winter-pl-ol').observe('change', function(el){
		changeOlympics(this.id);
		updateTip(this.id);
	}.bind($('winter-pl-ol')));
	createSlider('slider-summer-ol', 250, 50);
	createSlider('slider-winter-ol', 250, 50);
	initOlympics('summer-pl-ol');
	initOlympics('winter-pl-ol');
	changeModeOL();
</script>
<jsp:include page="/jsp/common/footer.jsp" />