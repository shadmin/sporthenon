<!-- EXPORT -->
<div id="d-export" class="dialog">
<div class="fieldset">
	<div class="fstitle">EXPORT</div>
	<div class="fscontent">Select an export format:<table style="margin-top:8px;"><tr>
		<td style="text-align:center;"><label for="ehtml"><img src="img/db/html.png"/><br/><b>Web Page (.html)</b><br/></label><input id="ehtml" type="radio" name="eformat" checked="checked"/></td>
		<td style="text-align:center;"><label for="eexcel"><img src="img/db/excel.png"/><br/><b>Excel Sheet (.xls)</b><br/></label><input id="eexcel" type="radio" name="eformat"/></td>
		<td style="text-align:center;"><label for="epdf"><img src="img/db/pdf.png"/><br/><b>PDF File (.pdf)</b><br/></label><input id="epdf" type="radio" name="eformat"/></td>
		<td style="text-align:center;"><label for="etext"><img src="img/db/text.png"/><br/><b>Plain Text (.txt)</b><br/></label><input id="etext" type="radio" name="eformat"/></td>
	</tr></table></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="Cancel" onclick="closeDialog(dExport);"/><input type="button" class="button ok" value="OK" onclick="closeDialog(dExport);"/></div>
</div>
</div>
<!-- LINK -->
<div id="d-link" class="dialog">
<div class="fieldset">
	<div class="fstitle">LINK</div>
	<div class="fscontent">Direct address to this result:<br/><input id="linktxt" type="text" style="width:400px;"/><br/>(use Ctrl+C or Right-click/Copy)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- INFO -->
<div id="d-info" class="dialog">
<div class="fieldset">
	<div class="fstitle">INFO</div>
	<div class="fscontent"></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<script type="text/javascript">
dExport = new Control.Modal($('d-export'),{ closeOnClick: false, fade: false });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
</script>