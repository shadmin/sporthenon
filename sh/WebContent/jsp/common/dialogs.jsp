<!-- LAST UPDATES -->
<div id="d-lastupdates" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">LAST UPDATES</div>
	<div class="fscontent"><table id="ctupdates"><tr><td>Count:</td><td><input id="countupdt" type="text" maxlength="3" size="3" value="20" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"/></td><td><a href="javascript:refreshLastUpdates();">Show</a></td></tr></table><div id="dupdates"></div></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dLastUpdates);"/></div>
</div>
</div>
<!-- EXPORT -->
<div id="d-export" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">EXPORT</div>
	<div class="fscontent">Select an export format:<table style="margin-top:8px;"><tr>
		<td><label for="ehtml" style="display: block;"><img alt="HTML" src="img/db/html.png"/><br/><b>Web Page (.html)</b><br/></label><input id="ehtml" type="radio" name="eformat" checked="checked"/></td>
		<td><label for="eexcel" style="display: block;"><img alt="XLS" src="img/db/excel.png"/><br/><b>Excel Sheet (.xls)</b><br/></label><input id="eexcel" type="radio" name="eformat"/></td>
		<td><label for="epdf" style="display: block;"><img alt="PDF" src="img/db/pdf.png"/><br/><b>PDF File (.pdf)</b><br/></label><input id="epdf" type="radio" name="eformat"/></td>
		<td><label for="etext" style="display: block;"><img alt="TXT" src="img/db/text.png"/><br/><b>Plain Text (.txt)</b><br/></label><input id="etext" type="radio" name="eformat"/></td>
	</tr></table></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="Cancel" onclick="closeDialog(dExport);"/><input type="button" class="button ok" value="OK" onclick="closeDialog(dExport);exportTab();"/></div>
</div>
</div>
<!-- LINK -->
<div id="d-link" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">LINK</div>
	<div class="fscontent">Direct address to this result:<br/><input id="linktxt" type="text" readonly="readonly" onclick="this.select();"/><br/>(use Ctrl+C or Right-click/Copy)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- INFO -->
<div id="d-info" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">INFO</div>
	<div class="fscontent">Informations/Statistics on this page:<table style="width:500px;margin-top:8px;">
		<tr><th>Address</th><td></td></tr><tr><th>Size</th><td></td></tr>
		<tr><th>Display&nbsp;Time</th><td></td></tr><tr><th>Pictures</th><td></td></tr>
	</table></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dInfo);"/></div>
</div>
</div>
<script type="text/javascript">
dLastUpdates = new Control.Modal($('d-lastupdates'),{ closeOnClick: false, fade: false });
dExport = new Control.Modal($('d-export'),{ closeOnClick: false, fade: false });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
</script>