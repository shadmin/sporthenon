<%@ page language="java"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<!-- REPORT ERROR -->
<div id="d-error" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("report.error", session).toUpperCase()%></div>
	<div class="fscontent"><input id="errlinkurl" disabled="disabled" style="width:650px;"/><br/><textarea id="errlinktext" rows="10" cols="80" style="width:650px;margin-top:5px;"></textarea></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="saveError();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dError);"/></div>
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
<!-- DATA -->
<div id="d-data" class="dialog" style="display:none;">
<div class="fieldset">
	<div id="datatip" class="fscontent"></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dDataTip);"/></div>
</div>
</div>
<!-- PERSON LIST -->
<div id="d-plist" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Person List</div>
	<div class="fscontent" style="width:400px;height:300px;overflow:auto;"><div id="ajaxsearch2" class="ajaxsearch"></div><div id="plist"></div></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="savePersonList();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dPersonList);"/></div>
</div>
</div>
<script type="text/javascript">
dError = new Control.Modal($('d-error'),{ closeOnClick: false, fade: false });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
dDataTip = new Control.Modal($('d-data'),{ closeOnClick: false, fade: false });
dPersonList = new Control.Modal($('d-plist'),{ closeOnClick: false, fade: false });
</script>