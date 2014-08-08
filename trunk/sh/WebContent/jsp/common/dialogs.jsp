<%@ page language="java" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<!-- LAST UPDATES -->
<div id="d-lastupdates" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.last.updates", session)%></div>
	<div class="fscontent"><table id="ctupdates"><tr><td><%=StringUtils.text("count", session)%>:</td><td><input id="countupdt" type="text" maxlength="3" size="3" value="20" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"/></td><td><a href="javascript:refreshLastUpdates();"><%=StringUtils.text("show", session)%></a></td></tr></table><div id="dupdates"></div></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dLastUpdates);"/></div>
</div>
</div>
<!-- EXPORT -->
<div id="d-export" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.export", session)%></div>
	<div class="fscontent"><%=StringUtils.text("select.format", session)%>:<table style="margin-top:8px;"><tr>
		<td><label for="ehtml" style="display: block;"><img alt="HTML" src="img/db/html.png"/><br/><b><%=StringUtils.text("web.page", session)%> (.html)</b><br/></label><input id="ehtml" type="radio" name="eformat" checked="checked"/></td>
		<td><label for="eexcel" style="display: block;"><img alt="XLS" src="img/db/excel.png"/><br/><b><%=StringUtils.text("excel.sheet", session)%> (.xls)</b><br/></label><input id="eexcel" type="radio" name="eformat"/></td>
		<td><label for="epdf" style="display: block;"><img alt="PDF" src="img/db/pdf.png"/><br/><b><%=StringUtils.text("pdf.file", session)%> (.pdf)</b><br/></label><input id="epdf" type="radio" name="eformat"/></td>
		<td><label for="etext" style="display: block;"><img alt="TXT" src="img/db/text.png"/><br/><b><%=StringUtils.text("plain.text", session)%> (.txt)</b><br/></label><input id="etext" type="radio" name="eformat"/></td>
	</tr></table></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="Cancel" onclick="closeDialog(dExport);"/><input type="button" class="button ok" value="OK" onclick="closeDialog(dExport);exportTab();"/></div>
</div>
</div>
<!-- LINK -->
<div id="d-link" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.link", session)%></div>
	<div class="fscontent"><%=StringUtils.text("direct.address", session)%>:<br/><input id="linktxt" type="text" readonly="readonly" onclick="this.select();"/><br/>(<%=StringUtils.text("use.ctrl.C", session)%>)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="OK" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- INFO -->
<div id="d-info" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.info", session)%></div>
	<div class="fscontent"><%=StringUtils.text("info.statistics", session)%>:<table style="width:500px;margin-top:8px;">
		<tr><th><%=StringUtils.text("address", session)%></th><td></td></tr><tr><th><%=StringUtils.text("size", session)%></th><td></td></tr>
		<tr><th><%=StringUtils.text("display.time", session)%></th><td></td></tr><tr><th><%=StringUtils.text("pictures", session)%></th><td></td></tr>
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