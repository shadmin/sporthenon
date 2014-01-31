<%@ page language="java" %>
<jsp:include page="/jsp/common/header.jsp" />
<div id="search" class="fieldset">
	<div class="fstitle criteria">SEARCH CRITERIA</div>
	<form id="search-form" action="/search" onsubmit="return false;">
	<div class="pattern">
		<input type="text" class="text" name="pattern" id="pattern" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');" onkeydown="if(event.keyCode == 13){runSearch();}"></input><br/>
		&nbsp;* = Many characters<br/>&nbsp;_ = Single character<br/><br/>
		<table cellspacing="0">
			<tr><td><input type="checkbox" name="case" id="case"></input></td><td><label for="case">Case Sensitive</label></td></tr>
			<tr><td><input type="checkbox" name="match" id="match"></input></td><td><label for="match">Exact Match</label></td></tr>
			<tr><td><input type="checkbox" name="ref" id="ref" checked="checked"></input></td><td><label for="ref">Count References</label></td></tr>
		</table>
	</div>
	<fieldset class="scope">
		<legend>Scope</legend>
		<table cellspacing="2">
		<tr>
			<td><input type="checkbox" value="PR" name="scope" id="PR" checked="checked"></input></td><td><label for="PR">Athletes</label></td>
			<td><input type="checkbox" value="SP" name="scope" id="SP" checked="checked"></input></td><td><label for="SP">Sports</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CN" name="scope" id="CN" checked="checked"></input></td><td><label for="CN">Countries</label>&nbsp;</td>
			<td><input type="checkbox" value="TM" name="scope" id="TM" checked="checked"></input></td><td><label for="TM">Teams</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CP,EV" name="scope" id="EV" checked="checked"></input></td><td><label for="EV">Events</label></td>
			<td><input type="checkbox" value="YR" name="scope" id="YR" checked="checked"></input></td><td><label for="YR">Years</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CT,CX,ST" name="scope" id="PL" checked="checked"></input></td><td><label for="PL">Places</label></td>
			<td colspan="2"></td>
			<td style="padding-left:50px;"><label for="AL">All:</label></td><td><input type="checkbox" value="AL" name="scope" id="AL" checked="checked" onclick="toggleCheck(this);"></input></td>
		</tr>
		</table>
	</fieldset>
	</form>
</div>
<%@include file="../../html/buttons.html" %>
<%@include file="../../html/tabcontrol.html" %>
<script type="text/javascript">
	$('pattern').activate();
	function toggleCheck(cb) {
		$$('#search .scope input').each(function(el){
			$(el).checked = cb.checked;
		});
	}
	initTabControl();
</script>
<jsp:include page="/jsp/common/footer.jsp" />