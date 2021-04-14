<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<!-- Account Confirm -->
<div id="d-accountconf" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.accountconf", session)%></div>
	<div class="fscontent"><%=StringUtils.text("msg.registered", session)%><br/><br/><a href='javascript:' onclick='rauth()'><%=StringUtils.text("menu.login", session)%></a></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dAccountConf);"/></div>
</div>
</div>
<!-- Report Error -->
<div id="d-error" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("report.error", session)%></div>
	<div class="fscontent"><%=StringUtils.text("page.address", session)%>:<br/><input id="errlinkurl" disabled="disabled" style="width:650px;margin-top:3px;"/><br/><br/>
	<%=StringUtils.text("error.description", session)%>:<br/><textarea id="errlinktext" rows="10" cols="80" style="width:650px;margin-top:3px;"></textarea></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="saveError();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dError);"/></div>
</div>
</div>
<!-- Picture -->
<div id="d-picture" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fscontent" id="dpicture-div"></div>
</div>
</div>
<!-- Link -->
<div id="d-link" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.link", session)%></div>
	<div class="fscontent"><%=StringUtils.text("direct.address", session)%>:<br/><input id="linktxt" type="text" readonly="readonly" onclick="this.select();"/><br/>(<%=StringUtils.text("use.ctrl.C", session)%>)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- Info -->
<div id="d-info" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.info", session)%></div>
	<div class="fscontent">
	<table style="width:450px;margin-top:8px;">
		<tr><th><%=StringUtils.text("size", session)%></th><td></td></tr>
		<tr><th><%=StringUtils.text("display.time", session)%></th><td></td></tr><tr><th><%=StringUtils.text("pictures", session)%></th><td></td></tr>
		<tr><th><%=StringUtils.text("language", session)%></th><td></td></tr><tr><th><%=StringUtils.text("last.update", session)%></th><td></td></tr>
	</table></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dInfo);"/></div>
</div>
</div>
<!-- Person List -->
<div id="d-plist" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Person List <span id="plist-title"></span></div>
	<div class="fscontent" style="width:400px;height:300px;overflow:auto;"><div id="ajaxsearch2" class="ajaxsearch"></div><div id="plist"></div><a href="javascript:addPersonList();">+10</a></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="savePersonList();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dPersonList);"/></div>
</div>
</div>
<!-- Round -->
<div id="d-round" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Edit Round <span id="round-title"></span></div>
	<div class="fscontent" style="width:680px;height:300px;overflow:auto;">
		<div id="ajaxsearch3" class="ajaxsearch"></div>
		<div style="float:right;">
		<table class="toolbar" style="top:0;right:0;clear:right;float:right;margin-top:0px;">
			<tr>
				<td style="padding-right:20px;"><a href="javascript:copyRStoRD();"><%=StringUtils.text("copy.from.result", session)%></a></td>
				<td><input id="upd-previous_" type="button" class="button upd-previous" onclick="moveRound(-1);" value="<%=StringUtils.text("previous", session)%>"/></td>
				<td><input id="upd-next_" type="button" class="button upd-next" onclick="moveRound(1);" value="<%=StringUtils.text("next", session)%>"/></td>
			</tr>
		</table>
		</div>
		<table style="width:100px;">
			<tr><td>ID:</td><td><input id="rddlg-id" type="text" disabled="disabled" style="width:50px;"/></td></tr>
		</table>
		<table id="dlg-round">
			<tr><td><input type="text" id="rddlg-rt" name="<%=StringUtils.text("type", session)%>" style="width:250px;"/></td><td></td></tr>
			<tr><td><input type="text" id="rddlg-rk1" name="<%=StringUtils.text("rank.1", session)%>" style="width:300px;"/><a href="javascript:clearValue('rddlg-rk1');">[X]</a></td>
			<td><input type="text" id="rddlg-rs1" name="<%=StringUtils.text("result.score", session)%>" style="width:120px;"/></td></tr>
			<tr><td><input type="text" id="rddlg-rk2" name="<%=StringUtils.text("rank.2", session)%>" style="width:300px;"/><a href="javascript:clearValue('rddlg-rk2');">[X]</a></td>
			<td><input type="text" id="rddlg-rs2" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
			<tr><td><input type="text" id="rddlg-rk3" name="<%=StringUtils.text("rank.3", session)%>" style="width:300px;"/><a href="javascript:clearValue('rddlg-rk3');">[X]</a></td>
			<td><input type="text" id="rddlg-rs3" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
			<tr><td><input type="text" id="rddlg-rk4" name="<%=StringUtils.text("rank.4", session)%>" style="width:300px;"/><a href="javascript:clearValue('rddlg-rk4');">[X]</a></td>
			<td><input type="text" id="rddlg-rs4" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
			<tr><td><input type="text" id="rddlg-rk5" name="<%=StringUtils.text("rank.5", session)%>" style="width:300px;"/><a href="javascript:clearValue('rddlg-rk5');">[X]</a></td>
			<td><input type="text" id="rddlg-rs5" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
			<tr><td><input type="text" id="rddlg-dt1" name="<%=StringUtils.text("date", session)%> #1" style="width:120px;"/><a href="javascript:$('rddlg-dt1').value=tDateValues[0];$('rddlg-dt1').addClassName('completed2');">D-2</a>/<a href="javascript:$('rddlg-dt1').value=tDateValues[1];$('rddlg-dt1').addClassName('completed2');">D-1</a>/<a href="javascript:$('rddlg-dt1').value=tDateValues[2];$('rddlg-dt1').addClassName('completed2');"> D </a>/<a href="javascript:$('rddlg-dt1').value=tDateValues[3];$('rddlg-dt1').addClassName('completed2');">D+1</a>/<a href="javascript:$('rddlg-dt1').value=tDateValues[4];$('rddlg-dt1').addClassName('completed2');">D+2</a></td>
			<td><input type="text" id="rddlg-dt2" name="<%=StringUtils.text("date", session)%> #2" style="width:120px;"/><a href="javascript:$('rddlg-dt2').value=tDateValues[0];$('rddlg-dt2').addClassName('completed2');">D-2</a>/<a href="javascript:$('rddlg-dt2').value=tDateValues[1];$('rddlg-dt2').addClassName('completed2');">D-1</a>/<a href="javascript:$('rddlg-dt2').value=tDateValues[2];$('rddlg-dt2').addClassName('completed2');"> D </a>/<a href="javascript:$('rddlg-dt2').value=tDateValues[3];$('rddlg-dt2').addClassName('completed2');">D+1</a>/<a href="javascript:$('rddlg-dt2').value=tDateValues[4];$('rddlg-dt2').addClassName('completed2');">D+2</a></td></tr>
			<tr><td><input type="text" id="rddlg-pl1" name="<%=StringUtils.text("place", session)%> #1" style="width:300px;"/></td>
			<td><input type="text" id="rddlg-pl2" name="<%=StringUtils.text("place", session)%> #2" style="width:300px;"/></td></tr>
			<tr><td colspan="2"><input type="text" id="rddlg-exa" name="<%=StringUtils.text("tie", session)%>" style="width:50px;"/></td></tr>
			<tr><td colspan="2"><textarea id="rddlg-cmt" tabindex="14" name="<%=StringUtils.text("comment", session)%>" cols="100" rows="3" style="width:500px;"></textarea></td></tr>
		</table>
	</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="setRoundValues();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dRound);"/></div>
</div>
</div>
<!-- Find -->
<div id="d-find" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("find", session)%></div>
	<div class="fscontent"><input type="text" id="fpattern" onkeydown="if(event.keyCode==13){searchEntity();}"/><div id="fresults"></div></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dFind);"/></div>
</div>
</div>
<!-- Confirm -->
<div id="d-question" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("confirm", session)%></div>
	<div class="fscontent"><div id="confirmtxt"></div></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("no", session)%>" onclick="closeDialog(dQuestion);"/><input id="confirmbtn" type="button" class="button ok" value="<%=StringUtils.text("yes", session)%>"/></div>
</div>
</div>
<!-- Comment -->
<div id="d-comment" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("comment", session)%></div>
	<div class="fscontent"><table>
		<tr><td><img alt="EN" src="/img/header/lang-en.png"/> <%=StringUtils.text("comment", session)%> (EN)</td></tr>
		<tr><td><textarea id="cmt-en" cols="100" rows="6" style="width:750px;"></textarea></td></tr>
		<tr><td><img alt="FR" src="/img/header/lang-fr.png"/> <%=StringUtils.text("comment", session)%> (FR)</td></tr>
		<tr><td><textarea id="cmt-fr" cols="100" rows="6" style="width:750px;"></textarea></td></tr>
		<tr><td><table style="width:auto;"><tr><td style="padding:1px;"><input type="radio" name="cmtmode" id="cmtmode1" checked="checked"/></td><td style="padding:1px;"><label for="cmtmode1"><%=StringUtils.text("show.tooltip", session)%></label></td><td style="padding:1px;"><img alt="" src="/img/render/note.png"/></td><td style="padding:1px;"><input type="radio" name="cmtmode" id="cmtmode2"/></td><td style="padding:1px;"><label for="cmtmode2"><%=StringUtils.text("show.normally", session)%></label> / <%=StringUtils.text("force.tooltip", session)%></td></tr></table></td></tr>
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
<script><!--
dAccountConf = new Control.Modal($('d-accountconf'),{ closeOnClick: false, fade: false });
dError = new Control.Modal($('d-error'),{ closeOnClick: false, fade: false });
dPicture = new Control.Modal($('d-picture'),{ closeOnClick: true, fade: true, afterClose:function(){setOpacity(1.0);} });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
dPersonList = new Control.Modal($('d-plist'),{ closeOnClick: false, fade: false });
dRound = new Control.Modal($('d-round'),{ closeOnClick: false, fade: false });
dFind = new Control.Modal($('d-find'),{ closeOnClick: false, fade: false });
dQuestion = new Control.Modal($('d-question'),{ closeOnClick: false, fade: false });
dComment = new Control.Modal($('d-comment'),{ closeOnClick: false, fade: false });
dHelp = new Control.Modal($('d-help'),{ closeOnClick: false, fade: false });
--></script>