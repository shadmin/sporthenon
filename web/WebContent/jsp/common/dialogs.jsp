<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<!-- Report Error -->
<div id="d-error" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("report.error", session).toUpperCase()%></div>
	<div class="fscontent"><%=StringUtils.text("page.address", session)%>:<br/><input id="errlinkurl" disabled="disabled" style="width:650px;margin-top:3px;"/><br/><br/>
	<%=StringUtils.text("error.description", session)%>:<br/><textarea id="errlinktext" rows="10" cols="80" style="width:650px;margin-top:3px;"></textarea></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="saveError();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dError);"/></div>
</div>
</div>
<!-- Link -->
<div id="d-link" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.link", session).toUpperCase()%></div>
	<div class="fscontent"><%=StringUtils.text("direct.address", session)%>:<br/><input id="linktxt" type="text" readonly="readonly" onclick="this.select();"/><br/>(<%=StringUtils.text("use.ctrl.C", session)%>)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- Info -->
<div id="d-info" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.info", session).toUpperCase()%></div>
	<div class="fscontent"><%=StringUtils.text("info.statistics", session)%>:<table style="width:600px;margin-top:8px;">
		<tr><th><%=StringUtils.text("address", session)%></th><td></td></tr><tr><th><%=StringUtils.text("size", session)%></th><td></td></tr>
		<tr><th><%=StringUtils.text("display.time", session)%></th><td></td></tr><tr><th><%=StringUtils.text("pictures", session)%></th><td></td></tr>
	</table></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dInfo);"/></div>
</div>
</div>
<!-- Person List -->
<div id="d-plist" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Person List&nbsp;<span id="plist-title"></span></div>
	<div class="fscontent" style="width:400px;height:300px;overflow:auto;"><div id="ajaxsearch2" class="ajaxsearch"></div><div id="plist"></div><a href="javascript:addPersonList();">+10</a></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="savePersonList();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dPersonList);"/></div>
</div>
</div>
<!-- Find -->
<div id="d-find" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("find", session).toUpperCase()%></div>
	<div class="fscontent"><input type="text" id="fpattern" onkeydown="if(event.keyCode==13){searchEntity();}"/><div id="fresults"></div></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dFind);"/></div>
</div>
</div>
<!-- Confirm -->
<div id="d-question" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("confirm", session).toUpperCase()%></div>
	<div class="fscontent"><div id="confirmtxt"></div></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("no", session)%>" onclick="closeDialog(dQuestion);"/><input id="confirmbtn" type="button" class="button ok" value="<%=StringUtils.text("yes", session)%>"/></div>
</div>
</div>
<!-- Comment -->
<div id="d-comment" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("comment", session).toUpperCase()%></div>
	<div class="fscontent"><table>
		<tr><td><img alt="EN" src="/img/header/lang-en.png"/>&nbsp;<%=StringUtils.text("comment", session)%> (EN)</td></tr>
		<tr><td><textarea id="cmt-en" cols="100" rows="6" style="width:750px;"></textarea></td></tr>
		<tr><td><img alt="FR" src="/img/header/lang-fr.png"/>&nbsp;<%=StringUtils.text("comment", session)%> (FR)</td></tr>
		<tr><td><textarea id="cmt-fr" cols="100" rows="6" style="width:750px;"></textarea></td></tr>
		<tr><td><table style="width:auto;"><tr><td style="padding:1px;"><input type="radio" name="cmtmode" id="cmtmode1" checked="checked"/></td><td style="padding:1px;"><label for="cmtmode1"><%=StringUtils.text("show.tooltip", session)%></label></td><td style="padding:1px;"><img alt="" src="/img/render/note.png"/></td><td style="padding:1px;"><input type="radio" name="cmtmode" id="cmtmode2"/></td><td style="padding:1px;"><label for="cmtmode2"><%=StringUtils.text("show.normally", session)%></label>&nbsp;/&nbsp;<%=StringUtils.text("force.tooltip", session)%></td></tr></table></td></tr>
	</table></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dComment);"/><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="saveComment();"/></div>
</div>
</div>
<!-- Help -->
<div id="d-help" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Help</div>
	<div class="fscontent" style="width:500px;height:300px;overflow:auto;">
	<h3>Codes pays/équipes</h3>
	<a href="javascript:loadDataTip('country');"><%=StringUtils.text("country.codes", session)%></a><br/><br/>
	<a href="javascript:loadDataTip('state');"><%=StringUtils.text("country.states", session)%></a><br/><br/>
	<a href="javascript:loadDataTip('team');"><%=StringUtils.text("entity.TM", session)%></a>
	<div id="datatip" style="display:none;"></div>
	<h3>Wiki</h3>
	<a target="_blank" href="https://github.com/shadmin/sporthenon/wiki">https://github.com/shadmin/sporthenon/wiki</a>
	</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dHelp);"/></div>
</div>
</div>
<script type="text/javascript"><!--
dError = new Control.Modal($('d-error'),{ closeOnClick: false, fade: false });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
dPersonList = new Control.Modal($('d-plist'),{ closeOnClick: false, fade: false });
dFind = new Control.Modal($('d-find'),{ closeOnClick: false, fade: false });
dQuestion = new Control.Modal($('d-question'),{ closeOnClick: false, fade: false });
dComment = new Control.Modal($('d-comment'),{ closeOnClick: false, fade: false });
dHelp = new Control.Modal($('d-help'),{ closeOnClick: false, fade: false });
--></script>