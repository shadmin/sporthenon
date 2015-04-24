<%@ page language="java"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<!-- EXPORT -->
<div id="d-export" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.export", session)%></div>
	<div class="fscontent"><%=StringUtils.text("select.format", session)%>:<table style="margin-top:8px;"><tr>
		<td onclick="exportPage('html')"><img alt="HTML" src="/img/db/html.png"/><br/><b><%=StringUtils.text("web.page", session)%><br/>(.html)</b></td>
		<td onclick="exportPage('excel')"><img alt="XLS" src="/img/db/excel.png"/><br/><b><%=StringUtils.text("excel.sheet", session)%><br/>(.xls)</b></td>
		<td onclick="exportPage('text')"><img alt="TXT" src="/img/db/text.png"/><br/><b><%=StringUtils.text("plain.text", session)%><br/>(.txt)</b></td>
	</tr></table></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("button.close", session)%>" onclick="closeDialog(dExport);"/></div>
</div>
</div>
<!-- LINK -->
<div id="d-link" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.link", session)%></div>
	<div class="fscontent"><%=StringUtils.text("direct.address", session)%>:<br/><input id="linktxt" type="text" readonly="readonly" onclick="this.select();"/><br/>(<%=StringUtils.text("use.ctrl.C", session)%>)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- INFO -->
<div id="d-info" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.info", session)%></div>
	<div class="fscontent"><%=StringUtils.text("info.statistics", session)%>:<table style="width:600px;margin-top:8px;">
		<tr><th><%=StringUtils.text("address", session)%></th><td></td></tr><tr><th><%=StringUtils.text("size", session)%></th><td></td></tr>
		<tr><th><%=StringUtils.text("display.time", session)%></th><td></td></tr><tr><th><%=StringUtils.text("pictures", session)%></th><td></td></tr>
	</table></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dInfo);"/></div>
</div>
</div>
<script type="text/javascript">
dLastUpdates = new Control.Modal($('d-lastupdates'),{ closeOnClick: false, fade: false });
dExport = new Control.Modal($('d-export'),{ closeOnClick: false, fade: false });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
</script>