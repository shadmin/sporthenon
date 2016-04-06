<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%
	String lang = String.valueOf(session.getAttribute("locale"));
	Calendar cal = Calendar.getInstance();
	String today = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
	cal.add(Calendar.DAY_OF_YEAR, -1);
	String yesterday = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
%>
<jsp:include page="/jsp/common/header.jsp" />
<script type="text/javascript"><!--
var treeItems = null;
<%
	Contributor cb = (Contributor) session.getAttribute("user");
	ArrayList<Object> params = new ArrayList<Object>();
	params.add(cb != null && !cb.isAdmin() ? " where SP.id in (" + cb.getSports() + ")" : "");
	params.add("_" + lang.toLowerCase());
	HtmlConverter.convertTreeArray(DatabaseHelper.call("TreeResults", params), out, false, lang);
%>
--></script>
<script type="text/javascript" src="/js/dropzone.js"></script>
<div id="update-results" class="update">
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.results", session).toUpperCase()%><div id="msg2"></div></div>
		<div class="fscontent" style="height:auto;">
			<!-- ID -->
			<div style="float:right;">
				<table style="margin-top:0px;"><tr>
				<td style="padding-right:10px;"><a id="pagelink" target="_blank" href="#" style="display:none;font-size:12px;"><%=StringUtils.text("page", session) %></a></td>
				<td>ID:</td><td><input id="id" type="text" disabled="disabled" style="width:50px;"/></td>
				</tr></table>
			</div>
			<div id="shortcutdiv"><a href="javascript:displayShortcuts();">[+]&nbsp;<%=StringUtils.text("actions.bar", session) %></a>
				<table id="shortcuts1" class="toolbar" style="display:none;position:relative;top:0;right:0;float:right;margin-top:0px;">
					<tr>
						<td style="padding:0px;"><table style="border:1px solid #AAA;margin:0px;"><tr><td>&nbsp;<%=StringUtils.text("mode", session) %>:</td><td><input id="addmode1" type="radio" name="mode1" checked="checked" onclick="$('addmode2').checked = true;"/></td><td><label for="addmode1" style="color:green;"><b><%=StringUtils.text("button.add", session)%></b></label></td><td><input id="modifmode1" type="radio" name="mode1" onclick="$('modifmode2').checked = true;"/></td><td><label for="modifmode1" style="color:orange;"><b><%=StringUtils.text("button.modify", session)%></b></label>&nbsp;</td></tr></table></td>
						<td><input id="upd-save" type="button" class="button upd-save" onclick="saveResult();"/></td>
						<td><input id="upd-delete" type="button" class="button upd-delete" onclick="deleteResult();"/></td>
					</tr>
				</table>
				<table id="shortcuts2" class="toolbar" style="display:none;position:relative;top:0;right:0;clear:right;float:right;margin-top:0px;">
					<tr>
						<td><input id="upd-first" type="button" class="button upd-first" onclick="loadResult('first');"/></td>
						<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadResult('prev');"/></td>
						<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity(0);"/></td>
						<td><input id="upd-next" type="button" class="button upd-next" onclick="loadResult('next');"/></td>
						<td><input id="upd-last" type="button" class="button upd-last" onclick="loadResult('last');"/></td>
					</tr>
				</table>
			</div>
			<div id="treediv" class="treediv"><div id="treeview">
				<table cellpadding="0" cellspacing="0">
				<thead><tr><th style="text-align:right;"><img alt="" src="/img/render/expand.gif" class="toggleimg" onclick="toggleContent(this);"/><span class="toggletext" onclick="toggleContent(this);"><%=StringUtils.text("tree", session)%></span></th></tr></thead>
				<tbody class="tby"><tr style="display:none;"><td id="tree">
				<script type="text/javascript">new Tree(treeItems, treeTemplate);</script>
				</td></tr></tbody></table>
			</div></div>
			<div id="currentimg" style="margin-top:90px;"></div>
			<ul>
			<!-- EVENT -->
			<li>
			<fieldset style="height:140px;"><legend><%=StringUtils.text("entity.EV.1", session)%></legend>
				<table>
					<tr><td colspan="5"><input type="text" id="sp" tabindex="1" name="<%=StringUtils.text("entity.SP.1", session)%>"/><a href="javascript:clearValue('sp');">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="4"><input type="text" id="cp" tabindex="2" name="<%=StringUtils.text("entity.CP.1", session)%>"/><a href="javascript:clearValue('cp');">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="3"><input type="text" id="ev" tabindex="3" name="<%=StringUtils.text("entity.EV.1", session)%> #1"/><a href="javascript:clearValue('ev');">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="2"><input type="text" id="se" tabindex="4" name="<%=StringUtils.text("entity.EV.1", session)%> #2"/><a href="javascript:clearValue('se');">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td><input type="text" tabindex="5" id="se2" name="<%=StringUtils.text("entity.EV.1", session)%> #3"/><a href="javascript:clearValue('se2');">[X]</a></td></tr>
				</table>
			</fieldset>
			</li>
			<!-- DATES -->
			<li>
			<fieldset style="height:140px;width:329px;"><legend><%=StringUtils.text("dates", session)%></legend>
				<table>
					<tr><td><input type="text" id="yr" tabindex="6" name="<%=StringUtils.text("entity.YR.1", session)%>"/><a href="javascript:clearValue('yr');">[X]</a></td>
					<td><input id="prevbtn" type="button" class="button" onclick="loadResult('prev');" value=""/></td>
					<td><input id="yrfind" type="text" style="font-size:11px;width:35px;" onblur="tValues['yrfind']=this.value;loadResult('year');" onkeydown="if (event.keyCode == 13){this.blur();}"/></td>
					<td><input id="nextbtn" type="button" class="button" onclick="loadResult('next');" value=""/></td></tr>
				</table>
				<table>
					<tr><td><input type="text" id="dt1" tabindex="7" name="<%=StringUtils.text("date", session)%> #1"/><a href="javascript:clearValue('dt1');">[X]</a><br/><a href="javascript:$('dt1').value='<%=today%>';$('dt1').addClassName('completed2');"><%=StringUtils.text("today", session)%></a>&nbsp;<a href="javascript:$('dt1').value='<%=yesterday%>';$('dt1').addClassName('completed2');"><%=StringUtils.text("yesterday", session)%></a></td>
					<td>&nbsp;<input type="text" id="dt2" tabindex="8" name="<%=StringUtils.text("date", session)%> #2"/><a href="javascript:clearValue('dt2');">[X]</a><br/><a href="javascript:$('dt2').value='<%=today%>';$('dt2').addClassName('completed2');"><%=StringUtils.text("today", session)%></a>&nbsp;<a href="javascript:$('dt2').value='<%=yesterday%>';$('dt2').addClassName('completed2');"><%=StringUtils.text("yesterday", session)%></a></td></tr>
				</table>
			</fieldset>
			</li>
			<!-- PHOTO -->
			<li id="imgzone">
				<fieldset style="height:140px;"><legend><%=StringUtils.text("photo", session)%></legend>
					<div id="dz-file"><p><%=StringUtils.text("click.drag.drop", session)%></p></div>
				</fieldset>
			</li>
			</ul>
			<ul>
			<!-- PLACES/VENUES -->
			<li>
			<fieldset style="height:145px;"><legend><%=StringUtils.text("places", session)%></legend>
				<table>
					<tr><td><input type="text" id="pl1" tabindex="9" name="<%=StringUtils.text("venue.city", session)%> #1"/><a href="javascript:clearValue('pl1');">[X]</a></td></tr>
					<tr><td style="padding-top:10px;"><input type="text" id="pl2" tabindex="10" name="<%=StringUtils.text("venue.city", session)%> #2"/><a href="javascript:clearValue('pl2');">[X]</a></td></tr>
				</table>
			</fieldset>
			</li>
			<!-- OTHER -->
			<li>
			<fieldset style="height:145px;width:567px;"><legend><%=StringUtils.text("other.info", session)%></legend>
				<table>
					<tr><td style="width:200px;"><input type="text" id="exa" tabindex="11" name="<%=StringUtils.text("tie", session)%>" style="width:150px;"/></td>
						<td style="width:20px;"><input id="inact" type="checkbox" onclick="showWarning();"/></td>
						<td><label for="inact"><%=StringUtils.text("event.notheld", session)%></label></td></tr>
					<tr><td colspan="3"><input type="text" id="cmt" tabindex="12" name="<%=StringUtils.text("comment", session)%>" style="width:500px;"/></td><td><input type="button" class="button" title="<%=StringUtils.text("manage.comment", session)%>" onclick="openCommentDialog();" value="..." style="padding-left:5px;"/></td></tr>
					<tr><td colspan="3"><input type="text" id="source" tabindex="13" name="<%=StringUtils.text("photo.source", session)%>" style="width:500px;"/></td></tr>
					<tr><td colspan="3"><textarea id="exl" tabindex="14" name="<%=StringUtils.text("extlinks", session)%>" cols="100" rows="3" style="width:500px;"><%=StringUtils.text("extlinks", session)%></textarea></td></tr>
				</table>
			</fieldset>
			</li>
			</ul>
			<ul>
			<!-- RANKINGS -->
			<li>
			<fieldset>
				<legend><a href="javascript:toggle('rankings');"><img id="img-rankings" alt="" src="/img/render/collapse.gif" class="toggleimg" onclick="toggleContent(this);"/><%=StringUtils.text("rankings", session)%></a></legend>
				<table id="rankings" style="margin-top:0px;width:877px;">
					<tr><td><input type="text" id="rk1" tabindex="100" name="<%=StringUtils.text("rank.1", session)%>"/><a href="javascript:clearValue('rk1');">[X]</a></td><td><a href="javascript:initPersonList(1);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs1" tabindex="101" name="<%=StringUtils.text("result.score", session)%>" style="width:120px;"/></td><td><input type="text" id="rk11" tabindex="120" name="<%=StringUtils.text("rank.11", session)%>"/><a href="javascript:clearValue('rk11');">[X]</a></td><td><a href="javascript:initPersonList(11);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs11" tabindex="121" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk2" tabindex="102" name="<%=StringUtils.text("rank.2", session)%>"/><a href="javascript:clearValue('rk2');">[X]</a></td><td><a href="javascript:initPersonList(2);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs2" tabindex="103" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk12" tabindex="122" name="<%=StringUtils.text("rank.12", session)%>"/><a href="javascript:clearValue('rk12');">[X]</a></td><td><a href="javascript:initPersonList(12);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs12" tabindex="123" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk3" tabindex="104" name="<%=StringUtils.text("rank.3", session)%>"/><a href="javascript:clearValue('rk3');">[X]</a></td><td><a href="javascript:initPersonList(3);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs3" tabindex="105" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk13" tabindex="124" name="<%=StringUtils.text("rank.13", session)%>"/><a href="javascript:clearValue('rk13');">[X]</a></td><td><a href="javascript:initPersonList(13);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs13" tabindex="125" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk4" tabindex="106" name="<%=StringUtils.text("rank.4", session)%>"/><a href="javascript:clearValue('rk4');">[X]</a></td><td><a href="javascript:initPersonList(4);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs4" tabindex="107" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk14" tabindex="126" name="<%=StringUtils.text("rank.14", session)%>"/><a href="javascript:clearValue('rk14');">[X]</a></td><td><a href="javascript:initPersonList(14);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs14" tabindex="127" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk5" tabindex="108" name="<%=StringUtils.text("rank.5", session)%>"/><a href="javascript:clearValue('rk5');">[X]</a></td><td><a href="javascript:initPersonList(5);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs5" tabindex="109" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk15" tabindex="128" name="<%=StringUtils.text("rank.15", session)%>"/><a href="javascript:clearValue('rk15');">[X]</a></td><td><a href="javascript:initPersonList(15);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs15" tabindex="129" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk6" tabindex="110" name="<%=StringUtils.text("rank.6", session)%>"/><a href="javascript:clearValue('rk6');">[X]</a></td><td><a href="javascript:initPersonList(6);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs6" tabindex="111" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk16" tabindex="130" name="<%=StringUtils.text("rank.16", session)%>"/><a href="javascript:clearValue('rk16');">[X]</a></td><td><a href="javascript:initPersonList(16);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs16" tabindex="131" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk7" tabindex="112" name="<%=StringUtils.text("rank.7", session)%>"/><a href="javascript:clearValue('rk7');">[X]</a></td><td><a href="javascript:initPersonList(7);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs7" tabindex="113" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk17" tabindex="132" name="<%=StringUtils.text("rank.17", session)%>"/><a href="javascript:clearValue('rk17');">[X]</a></td><td><a href="javascript:initPersonList(17);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs17" tabindex="133" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk8" tabindex="114" name="<%=StringUtils.text("rank.8", session)%>"/><a href="javascript:clearValue('rk8');">[X]</a></td><td><a href="javascript:initPersonList(8);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs8" tabindex="115" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk18" tabindex="134" name="<%=StringUtils.text("rank.18", session)%>"/><a href="javascript:clearValue('rk18');">[X]</a></td><td><a href="javascript:initPersonList(18);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs18" tabindex="135" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk9" tabindex="116" name="<%=StringUtils.text("rank.9", session)%>"/><a href="javascript:clearValue('rk9');">[X]</a></td><td><a href="javascript:initPersonList(9);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs9" tabindex="117" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk19" tabindex="136" name="<%=StringUtils.text("rank.19", session)%>"/><a href="javascript:clearValue('rk19');">[X]</a></td><td><a href="javascript:initPersonList(19);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs19" tabindex="137" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk10" tabindex="118" name="<%=StringUtils.text("rank.10", session)%>"/><a href="javascript:clearValue('rk10');">[X]</a></td><td><a href="javascript:initPersonList(10);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs10" tabindex="119" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk20" tabindex="138" name="<%=StringUtils.text("rank.20", session)%>"/><a href="javascript:clearValue('rk20');">[X]</a></td><td><a href="javascript:initPersonList(20);"><img src="/img/update/personlist.png"/></a></td><td>&nbsp;<input type="text" id="rs20" tabindex="139" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				</table>	
			</fieldset>
			</ul>
			<ul>
			<!-- ROUNDS -->
			<li>
			<fieldset>
				<legend><a href="javascript:toggle('rounds');"><img id="img-rounds" alt="" src="/img/render/collapse.gif" class="toggleimg" onclick="toggleContent(this);"/><%=StringUtils.text("matches.rounds", session)%></a></legend>
				<div id="rounds"><table style="margin-top:0px;"></table><a href="javascript:addRounds();">[+10]</a></div>
			</fieldset>
			</li>
			</ul>
			<!-- BUTTON PANEL -->
			<table class="toolbar" style="position:relative;top:0;right:0;float:right;margin-top:15px;">
				<tr>
					<td style="padding:0px;"><table style="border:1px solid #AAA;margin:0px;"><tr><td>&nbsp;<%=StringUtils.text("update.mode", session) %>:</td><td><input id="addmode2" type="radio" name="mode2" checked="checked" onclick="$('addmode1').checked = true;"/></td><td><label for="addmode2" style="color:green;"><b><%=StringUtils.text("button.add", session)%></b></label></td><td><input id="modifmode2" type="radio" name="mode2" onclick="$('modifmode1').checked = true;"/></td><td><label for="modifmode2" style="color:orange;"><b><%=StringUtils.text("button.modify", session)%></b></label>&nbsp;</td></tr></table></td>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveResult();" value="<%=StringUtils.text("save", session)%>"/></td>
					<td><input id="upd-delete" type="button" class="button upd-delete" onclick="deleteResult();" value="<%=StringUtils.text("button.delete", session)%>"/></td>
				</tr>
			</table><br/>
			<table class="toolbar" style="position:relative;top:0;right:0;clear:right;float:right;margin-top:5px;">
				<tr>
					<td><input id="upd-first" type="button" class="button upd-first" onclick="loadResult('first');" value="<%=StringUtils.text("first", session)%>"/></td>
					<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadResult('prev');" value="<%=StringUtils.text("previous", session)%>"/></td>
					<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity(0);" value="<%=StringUtils.text("find", session)%>"/></td>
					<td><input id="upd-next" type="button" class="button upd-next" onclick="loadResult('next');" value="<%=StringUtils.text("next", session)%>"/></td>
					<td><input id="upd-last" type="button" class="button upd-last" onclick="loadResult('last');" value="<%=StringUtils.text("last", session)%>"/></td>
				</tr>
			</table>
			<div id="msg" style="float:left;"></div>
		</div>
	</div>
	<br/>
	<span class="small"><%=StringUtils.text("for.any.request", session)%>&nbsp;:&nbsp;<a href="mailto:admin@sporthenon.com">admin@sporthenon.com</a></span>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	initUpdateResults("<%=request.getAttribute("value")%>");
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />