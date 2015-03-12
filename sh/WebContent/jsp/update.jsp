<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.sporthenon.utils.ConfigUtils"%>
<%@page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update">
	<div class="fieldset">
		<div class="fstitle info">CREER/MODIFIER UN RESULTAT</div>
		<div class="fscontent" style="height:450px;">
			<div id="ajaxdiv"></div>
			<div style="float:left;">
			<table style="width:auto;">
				<tr><td colspan="5"><input type="text" id="sp" name="Sport" value="Sport"/></td></tr>
				<tr><td><img src="/img/component/treeview/join.gif"/></td><td colspan="4"><input type="text" id="cp" name="Championship" value="Championship"/></td></tr>
				<tr><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/join.gif"/></td><td colspan="3"><input type="text" id="ev" name="Event 1" value="Event 1"/></td></tr>
				<tr><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/join.gif"/></td><td colspan="2"><input type="text" id="se" name="Event 2" value="Event 2"/></td></tr>
				<tr><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/join.gif"/></td><td><input type="text" id="se2" name="Event 3" value="Event 3"/></td></tr>
			</table>
			<table style="width:auto;margin-top:10px;">
				<tr><td><input type="text" id="yr" name="Year" value="Year"/></td>
			</table>
			<table style="width:auto;margin-top:10px;">
				<tr><td><input type="text" id="dt1" name="From" value="From"/><br/><a href="#">Today</a>&nbsp;<a href="#">Yesterday</a></td>
				<td><input type="text" id="dt2" name="To" value="To"/><br/><a href="#">Today</a>&nbsp;<a href="#">Yesterday</a></td></tr>
			</table>
			<table style="width:auto;margin-top:10px;">
				<tr><td><input type="text" id="pl1" name="Venue/City 1" value="Venue/City 1"/></td></tr>
				<tr><td><input type="text" id="pl2" name="Venue/City 2" value="Venue/City 2"/></td></tr>
			</table>
			</div>
			<table style="width:auto;">
				<tr><td><input type="text" id="rk1" name="Rank #1" value="Rank #1"/></td><td><input type="text" id="rs1" name="Result/Score" value="Result" style="width:50px;"/></td></tr>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
var tValues = [];
function setValue(text, li) {
	var t = li.id.split('-');
	tValues[t[0]] = t[1];
	$(t[0]).addClassName('completed');
}
window.onload = function() {
	['sp', 'cp', 'ev', 'se', 'se2', 'yr', 'pl1', 'pl2'].each(function(s){
		new Ajax.Autocompleter(
			s,
			'ajaxdiv',
			'/update/ajax/' + s,
			{ paramName: 'value', minChars: 2, afterUpdateElement: setValue}
		);
	});
	$$('#update input').each(function(el){
		$(el).addClassName('default');
		Event.observe($(el), 'focus', function(){
			if ($(this).value == $(this).name) {
				$(this).value = '';
			}
		});
		Event.observe($(el), 'blur', function(){
			if ($(this).value == '') {
				$(this).value = $(this).name;
				$(this).removeClassName('completed');
				tValues[$(this).id] = '';
			}
		});
	});
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />