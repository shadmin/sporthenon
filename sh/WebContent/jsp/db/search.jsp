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
		<table cellspacing="0">
		<tr>
			<td><input type="checkbox" value="PR" name="scope" id="PR" checked="checked"></input></td><td><label for="PR">Athletes</label></td>
			<td><input type="checkbox" value="EV" name="scope" id="EV" checked="checked"></input></td><td><label for="EV">Events</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CP" name="scope" id="CP" checked="checked"></input></td><td><label for="CP">Championships</label></td>
			<td><input type="checkbox" value="SP" name="scope" id="SP" checked="checked"></input></td><td><label for="SP">Sports</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CT" name="scope" id="CT" checked="checked"></input></td><td><label for="CT">Cities</label></td>
			<td><input type="checkbox" value="ST" name="scope" id="ST" checked="checked"></input></td><td><label for="ST">States</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CX" name="scope" id="CX" checked="checked"></input></td><td><label for="CX">Complexes</label></td>
			<td><input type="checkbox" value="TM" name="scope" id="TM" checked="checked"></input></td><td><label for="TM">Teams</label></td>
		</tr>
		<tr>
			<td><input type="checkbox" value="CN" name="scope" id="CN" checked="checked"></input></td><td><label for="CN">Countries</label></td>
			<td><input type="checkbox" value="YR" name="scope" id="YR" checked="checked"></input></td><td><label for="YR">Years</label></td>
			<td style="padding-left:70px;"><label for="AL">All:</label></td><td><input type="checkbox" value="AL" name="scope" id="AL" checked="checked" onclick="toggleCheck(this);"></input></td>
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
</script>
<jsp:include page="/jsp/common/footer.jsp" />