<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<table class="toolbar" style="float:right;">
	<tr>
		<td><input id="upd-new" type="button" class="button upd-new" onclick="newEntity();" value="<%=StringUtils.text("new", session)%>"/></td>
		<td><input id="upd-save" type="button" class="button upd-save" onclick="saveEntity();" value="<%=StringUtils.text("save", session)%>"/></td>
		<td><input id="upd-copy" type="button" class="button upd-copy" onclick="copyEntity();" value="<%=StringUtils.text("copy", session)%>"/></td>
	</tr>
</table><br/>
<table class="toolbar" style="clear:right;float:right;margin-top:5px;">
	<tr>
		<td><input id="upd-first" type="button" class="button upd-first" onclick="loadEntity('first');" value="<%=StringUtils.text("first", session)%>"/></td>
		<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadEntity('previous');" value="<%=StringUtils.text("previous", session)%>"/></td>
		<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity();" value="<%=StringUtils.text("find", session)%>"/></td>
		<td><input id="upd-next" type="button" class="button upd-next" onclick="loadEntity('next');" value="<%=StringUtils.text("next", session)%>"/></td>
		<td><input id="upd-last" type="button" class="button upd-last" onclick="loadEntity('last');" value="<%=StringUtils.text("last", session)%>"/></td>
	</tr>
</table>
<div id="msg" style="float:left;"></div>