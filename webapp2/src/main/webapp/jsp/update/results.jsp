<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="com.sporthenon.db.entity.Year"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.db.entity.meta.TreeItem"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%
	String lang = String.valueOf(session.getAttribute("locale"));
	Calendar cal = Calendar.getInstance();
	String d = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
	cal.add(Calendar.DAY_OF_YEAR, -2); String d1 = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
	cal.add(Calendar.DAY_OF_YEAR, 1); String d2 = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
	cal.add(Calendar.DAY_OF_YEAR, 2); String d3 = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
	cal.add(Calendar.DAY_OF_YEAR, 1); String d4 = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
	cal.add(Calendar.DAY_OF_YEAR, -2); Integer y = cal.get(Calendar.YEAR);
	String sql = "SELECT * FROM year WHERE label = ?";
	Year y1_ = (Year) DatabaseManager.loadEntity(sql, Arrays.asList(String.valueOf(y - 2)), Year.class);
	Year y2_ = (Year) DatabaseManager.loadEntity(sql, Arrays.asList(String.valueOf(y - 1)), Year.class);
	Year y_ = (Year) DatabaseManager.loadEntity(sql, Arrays.asList(String.valueOf(y)), Year.class);
	Year y3_ = (Year) DatabaseManager.loadEntity(sql, Arrays.asList(String.valueOf(y + 1)), Year.class);
	Year y4_ = (Year) DatabaseManager.loadEntity(sql, Arrays.asList(String.valueOf(y + 2)), Year.class);
%>
<jsp:include page="/jsp/common/header.jsp"/>
<script><!--
var tDateValues = ['<%=d1%>', '<%=d2%>', '<%=d%>', '<%=d3%>', '<%=d4%>'];
var treeItems = null;
<%
	Contributor cb = (Contributor) session.getAttribute("user");
	ArrayList<Object> params = new ArrayList<Object>();
	params.add(cb != null && !cb.isAdmin() ? " where SP.id in (" + cb.getSports() + ")" : "");
	params.add(ResourceUtils.getLocaleParam(lang));
	HtmlConverter.convertTreeArray(DatabaseManager.callFunctionSelect("tree_results", params, TreeItem.class), out, false, lang);
%>
--></script>
<script src="/js/dropzone.js"></script>
<div id="update-results" class="update">
	<!-- Help tips -->
	<div id="help-event" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helpevent_" + lang)%></div>
	<div id="help-dates" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helpdates_" + lang)%></div>
	<div id="help-photo" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helpphoto_" + lang)%></div>
	<div id="help-places" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helpplaces_" + lang)%></div>
	<div id="help-other" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helpother_" + lang)%></div>
	<div id="help-rankings" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helprankings_" + lang)%></div>
	<div id="help-rounds" class="rendertip" style="display:none;"><%=ConfigUtils.getValue("html_helprounds_" + lang)%></div>
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.results", session).toUpperCase()%><div id="msg2"></div></div>
		<div class="fscontent" style="height:auto;">
			<!-- ID -->
			<div style="float:right;">
				<table style="margin-top:0px;"><tr>
				<td>ID:</td><td><input id="id" type="text" disabled="disabled" style="width:50px;"/></td>
				</tr></table>
			</div>
			<div id="shortcutdiv"><a href="javascript:displayShortcuts();">[+] <%=StringUtils.text("actions.bar", session)%></a>
				<table id="shortcuts1" class="toolbar" style="display:none;position:relative;top:0;right:0;float:right;margin-top:0px;">
					<tr>
						<td style="padding:0px;"><table style="border:1px solid #AAA;margin:0px;"><tr><td> <%=StringUtils.text("mode", session)%>:</td><td><input id="addmode1" type="radio" name="mode1" checked="checked" onclick="$('addmode2').checked = true;"/></td><td><label for="addmode1" style="color:green;"><b><%=StringUtils.text("button.add", session)%></b></label></td><td><input id="modifmode1" type="radio" name="mode1" onclick="$('modifmode2').checked = true;"/></td><td><label for="modifmode1" style="color:orange;"><b><%=StringUtils.text("button.modify", session)%></b></label> </td></tr></table></td>
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
				<table>
				<thead><tr><th style="text-align:right;"><img alt="" src="/img/render/expand.gif?v=2" class="toggleimg" onclick="toggleContent(this);"/><span class="toggletext" onclick="toggleContent(this);"><%=StringUtils.text("tree", session)%></span></th></tr></thead>
				<tbody class="tby"><tr style="display:none;"><td id="tree">
				<script>new Tree(treeItems, treeTemplate);</script>
				</td></tr></tbody></table>
			</div></div>
			<ul>
			<!-- EVENT -->
			<li>
			<fieldset style="height:140px;"><legend><%=StringUtils.text("entity.EV.1", session)%>&nbsp;<a href="#help-event" style="color:#00C;">[?]</a><a href="javascript:clearValue('sp,cp,ev,se,se2');" style="color:#00C;">[X]</a></legend>
				<table style="margin-top:3px;">
					<tr><td colspan="5"><input type="text" id="sp" tabindex="1" name="<%=StringUtils.text("entity.SP.1", session)%>"/><a href="javascript:clearValue('sp', true);">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="4"><input type="text" id="cp" tabindex="2" name="<%=StringUtils.text("entity.CP.1", session)%>"/><a href="javascript:clearValue('cp', true);">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="3"><input type="text" id="ev" tabindex="3" name="<%=StringUtils.text("entity.EV.1", session)%> #1"/><a href="javascript:clearValue('ev', true);">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="2"><input type="text" id="se" tabindex="4" name="<%=StringUtils.text("entity.EV.1", session)%> #2"/><a href="javascript:clearValue('se', true);">[X]</a></td></tr>
					<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td><input type="text" tabindex="5" id="se2" name="<%=StringUtils.text("entity.EV.1", session)%> #3"/><a href="javascript:clearValue('se2', true);">[X]</a></td></tr>
				</table>
			</fieldset>
			</li>
			<!-- DATES -->
			<li>
			<fieldset style="height:140px;width:483px;"><legend><%=StringUtils.text("dates", session)%>&nbsp;<a href="#help-dates" style="color:#00C;">[?]</a><a href="javascript:clearValue('yr,dt1,dt2');" style="color:#00C;">[X]</a></legend>
				<table style="margin-top:3px;">
					<tr><td><input type="text" id="yr" tabindex="6" name="<%=StringUtils.text("entity.YR.1", session)%>"/><a href="javascript:clearValue('yr', true);">[X]</a>&nbsp;<a href="javascript:$('yr').value=parseInt($F('yr')) - 1;tValues['yr']=parseInt(tValues['yr']) - 1;showWarning();">[-]</a>&nbsp;<a href="javascript:$('yr').value=parseInt($F('yr')) + 1;tValues['yr']=parseInt(tValues['yr']) + 1;showWarning();">[+]</a><br/>
					<a href="javascript:$('yr').value='<%=y-2%>';$('yr').addClassName('completed');tValues['yr']=<%=y1_.getId()%>;showWarning();">Y-2</a>/<a href="javascript:$('yr').value='<%=y-1%>';$('yr').addClassName('completed');tValues['yr']=<%=y2_.getId()%>;showWarning();">Y-1</a>/<a href="javascript:$('yr').value='<%=y%>';$('yr').addClassName('completed');tValues['yr']=<%=y_.getId()%>;showWarning();"> Y </a>/<a href="javascript:$('yr').value='<%=y+1%>';$('yr').addClassName('completed');tValues['yr']=<%=y3_.getId()%>;showWarning();">Y+1</a>/<a href="javascript:$('yr').value='<%=y+2%>';$('yr').addClassName('completed');tValues['yr']=<%=y4_.getId()%>;showWarning();">Y+2</a></td>
					<td style="padding-left:50px;"><input id="prevbtn" type="button" class="button" onclick="loadResult('prev');" value=""/></td>
					<td><input id="yrfind" type="text" style="font-size:11px;width:35px;" onblur="tValues['yrfind']=this.value;loadResult('year');" onkeydown="if (event.keyCode == 13){this.blur();}"/></td>
					<td><input id="nextbtn" type="button" class="button" onclick="loadResult('next');" value=""/></td>
					<td id="otherids"></td></tr>
				</table>
				<table>
					<tr><td><input type="text" id="dt1" tabindex="7" name="<%=StringUtils.text("date", session)%> #1"/><a href="javascript:clearValue('dt1', true);">[X]</a><br/><a href="javascript:$('dt1').value=tDateValues[0];$('dt1').addClassName('completed2');showWarning()">D-2</a>/<a href="javascript:$('dt1').value=tDateValues[1];$('dt1').addClassName('completed2');showWarning()">D-1</a>/<a href="javascript:$('dt1').value=tDateValues[2];$('dt1').addClassName('completed2');showWarning()"> D </a>/<a href="javascript:$('dt1').value=tDateValues[3];$('dt1').addClassName('completed2');showWarning()">D+1</a>/<a href="javascript:$('dt1').value=tDateValues[4];$('dt1').addClassName('completed2');showWarning()">D+2</a></td>
					<td> <input type="text" id="dt2" tabindex="8" name="<%=StringUtils.text("date", session)%> #2"/><a href="javascript:clearValue('dt2', true);">[X]</a><br/><a href="javascript:$('dt2').value=tDateValues[0];$('dt2').addClassName('completed2');showWarning()">D-2</a>/<a href="javascript:$('dt2').value=tDateValues[1];$('dt2').addClassName('completed2');showWarning()">D-1</a>/<a href="javascript:$('dt2').value=tDateValues[2];$('dt2').addClassName('completed2');showWarning()"> D </a>/<a href="javascript:$('dt2').value=tDateValues[3];$('dt2').addClassName('completed2');showWarning()">D+1</a>/<a href="javascript:$('dt2').value=tDateValues[4];$('dt2').addClassName('completed2');showWarning()">D+2</a></td></tr>
				</table>
				<table>
					<tr><td style="width:20px;"><input id="nodate" type="checkbox" onclick="showWarning();"/></td>
					<td><label for="nodate"><%=StringUtils.text("no.date", session)%></label></td></tr>
				</table>
			</fieldset>
			</li>
			</ul>
			<ul>
			<!-- PLACES/VENUES -->
			<li>
			<fieldset style="height:145px;"><legend><%=StringUtils.text("places", session)%>&nbsp;<a href="#help-places" style="color:#00C;">[?]</a><a href="javascript:clearValue('pl1,pl2');" style="color:#00C;">[X]</a></legend>
				<table style="margin-top:3px;">
					<tr><td><input type="text" id="pl1" tabindex="9" name="<%=StringUtils.text("venue.city", session)%> #1"/><a href="javascript:clearValue('pl1', true);">[X]</a></td></tr>
					<tr><td style="padding-top:10px;"><input type="text" id="pl2" tabindex="10" name="<%=StringUtils.text("venue.city", session)%> #2"/><a href="javascript:clearValue('pl2', true);">[X]</a></td></tr>
				</table>
				<table>
					<tr><td style="width:20px;"><input id="noplace" type="checkbox" onclick="showWarning();"/></td>
					<td><label for="noplace"><%=StringUtils.text("no.place", session)%></label></td></tr>
				</table>
			</fieldset>
			</li>
			<!-- OTHER -->
			<li>
			<fieldset style="height:145px;width:567px;"><legend><%=StringUtils.text("other.info", session)%>&nbsp;<a href="#help-other" style="color:#00C;">[?]</a><a href="javascript:clearValue('exa,cmt,exl');" style="color:#00C;">[X]</a></legend>
				<table style="margin-top:3px;">
					<tr><td style="width:200px;"><input type="text" id="exa" tabindex="11" name="<%=StringUtils.text("tie", session)%>" style="width:150px;"/></td>
						<td style="width:20px;"><input id="inact" type="checkbox" onclick="showWarning();"/></td>
						<td><label for="inact"><%=StringUtils.text("event.notheld", session)%></label></td>
						<td style="width:20px;padding-left:30px;"><input id="draft" type="checkbox" onclick="showWarning();"/></td>
						<td><label for="draft"><%=StringUtils.text("draft", session)%></label></td></tr>
					<tr><td colspan="5"><input type="text" id="cmt" tabindex="12" name="<%=StringUtils.text("comment", session)%>" style="width:500px;"/></td><td><input type="button" class="button" title="<%=StringUtils.text("manage.comment", session)%>" onclick="openCommentDialog();" value="..." style="padding-left:5px;"/></td></tr>
					<tr><td colspan="5"><textarea id="exl" tabindex="14" name="<%=StringUtils.text("extlinks", session)%>" cols="100" rows="4" style="width:500px;"><%=StringUtils.text("extlinks", session)%></textarea></td></tr>
				</table>
			</fieldset>
			</li>
			</ul>
			<!-- RANKINGS -->
			<ul><li>
			<fieldset>
				<legend><a href="javascript:toggle('rankings');"><img id="img-rankings" alt="" src="/img/render/collapse.gif" class="toggleimg" onclick="toggleContent(this);"/><%=StringUtils.text("rankings", session)%></a>&nbsp;<a href="#help-rankings" style="color:#00C;">[?]</a><a href="javascript:clearFieldset('rankings');" style="color:#00C;">[X]</a></legend>
				<table id="rankings" style="margin-top:0px;width:876px;">
					<tr><td><input type="text" id="rk1" tabindex="100" name="<%=StringUtils.text("rank.1", session)%>"/><a href="javascript:clearValue('rk1', true);">[X]</a></td><td><a href="javascript:initPersonList(1);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs1" tabindex="101" name="<%=StringUtils.text("result.score", session)%>" style="width:120px;"/></td><td><input type="text" id="rk11" tabindex="120" name="<%=StringUtils.text("rank.11", session)%>"/><a href="javascript:clearValue('rk11', true);">[X]</a></td><td><a href="javascript:initPersonList(11);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs11" tabindex="121" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk2" tabindex="102" name="<%=StringUtils.text("rank.2", session)%>"/><a href="javascript:clearValue('rk2', true);">[X]</a></td><td><a href="javascript:initPersonList(2);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs2" tabindex="103" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk12" tabindex="122" name="<%=StringUtils.text("rank.12", session)%>"/><a href="javascript:clearValue('rk12', true);">[X]</a></td><td><a href="javascript:initPersonList(12);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs12" tabindex="123" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk3" tabindex="104" name="<%=StringUtils.text("rank.3", session)%>"/><a href="javascript:clearValue('rk3', true);">[X]</a></td><td><a href="javascript:initPersonList(3);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs3" tabindex="105" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk13" tabindex="124" name="<%=StringUtils.text("rank.13", session)%>"/><a href="javascript:clearValue('rk13', true);">[X]</a></td><td><a href="javascript:initPersonList(13);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs13" tabindex="125" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk4" tabindex="106" name="<%=StringUtils.text("rank.4", session)%>"/><a href="javascript:clearValue('rk4', true);">[X]</a></td><td><a href="javascript:initPersonList(4);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs4" tabindex="107" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk14" tabindex="126" name="<%=StringUtils.text("rank.14", session)%>"/><a href="javascript:clearValue('rk14', true);">[X]</a></td><td><a href="javascript:initPersonList(14);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs14" tabindex="127" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk5" tabindex="108" name="<%=StringUtils.text("rank.5", session)%>"/><a href="javascript:clearValue('rk5', true);">[X]</a></td><td><a href="javascript:initPersonList(5);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs5" tabindex="109" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk15" tabindex="128" name="<%=StringUtils.text("rank.15", session)%>"/><a href="javascript:clearValue('rk15', true);">[X]</a></td><td><a href="javascript:initPersonList(15);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs15" tabindex="129" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk6" tabindex="110" name="<%=StringUtils.text("rank.6", session)%>"/><a href="javascript:clearValue('rk6', true);">[X]</a></td><td><a href="javascript:initPersonList(6);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs6" tabindex="111" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk16" tabindex="130" name="<%=StringUtils.text("rank.16", session)%>"/><a href="javascript:clearValue('rk16', true);">[X]</a></td><td><a href="javascript:initPersonList(16);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs16" tabindex="131" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk7" tabindex="112" name="<%=StringUtils.text("rank.7", session)%>"/><a href="javascript:clearValue('rk7', true);">[X]</a></td><td><a href="javascript:initPersonList(7);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs7" tabindex="113" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk17" tabindex="132" name="<%=StringUtils.text("rank.17", session)%>"/><a href="javascript:clearValue('rk17', true);">[X]</a></td><td><a href="javascript:initPersonList(17);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs17" tabindex="133" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk8" tabindex="114" name="<%=StringUtils.text("rank.8", session)%>"/><a href="javascript:clearValue('rk8', true);">[X]</a></td><td><a href="javascript:initPersonList(8);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs8" tabindex="115" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk18" tabindex="134" name="<%=StringUtils.text("rank.18", session)%>"/><a href="javascript:clearValue('rk18', true);">[X]</a></td><td><a href="javascript:initPersonList(18);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs18" tabindex="135" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk9" tabindex="116" name="<%=StringUtils.text("rank.9", session)%>"/><a href="javascript:clearValue('rk9', true);">[X]</a></td><td><a href="javascript:initPersonList(9);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs9" tabindex="117" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk19" tabindex="136" name="<%=StringUtils.text("rank.19", session)%>"/><a href="javascript:clearValue('rk19', true);">[X]</a></td><td><a href="javascript:initPersonList(19);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs19" tabindex="137" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
					<tr><td><input type="text" id="rk10" tabindex="118" name="<%=StringUtils.text("rank.10", session)%>"/><a href="javascript:clearValue('rk10', true);">[X]</a></td><td><a href="javascript:initPersonList(10);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs10" tabindex="119" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td><td><input type="text" id="rk20" tabindex="138" name="<%=StringUtils.text("rank.20", session)%>"/><a href="javascript:clearValue('rk20', true);">[X]</a></td><td><a href="javascript:initPersonList(20);"><img alt="" src="/img/update/personlist.png"/></a></td><td> <input type="text" id="rs20" tabindex="139" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				</table>	
			</fieldset>
			</li></ul>
			<!-- ROUNDS -->
			<ul><li>
			<fieldset>
				<legend><a href="javascript:toggle('rounds');"><img id="img-rounds" alt="" src="/img/render/collapse.gif" class="toggleimg" onclick="toggleContent(this);"/><%=StringUtils.text("matches.rounds", session)%></a>&nbsp;<a href="#help-rounds" style="color:#00C;">[?]</a><a href="javascript:clearFieldset('rounds');" style="color:#00C;">[X]</a></legend>
				<div id="rounds"><table style="margin-top:0px;"><tr><td></td></tr></table><a href="javascript:addRounds();">[+10]</a></div>
			</fieldset>
			</li></ul>
			<!-- PHOTOS -->
			<ul><li>
			<fieldset id="imgzone">
				<legend><a href="javascript:toggle('photos');"><img id="img-photos" alt="" src="/img/render/collapse.gif" class="toggleimg" onclick="toggleContent(this);"/><%=StringUtils.text("photos", session)%></a></legend>
				<div id="photos">
				<legend><%=StringUtils.text("photos", session)%></legend>
				<div id="dz-file" style="float:left;"><p><%=StringUtils.text("click.drag.drop", session)%></p></div>
				<div id="embimg">
					Embedded:<br/><textarea id="emb" tabindex="15" cols="50" rows="7" style="width:730px;height:102px;white-space:normal;"></textarea>
				</div>
				<div style="clear:left;margin-bottom:5px;">
					<input type="text" id="src" title="<%=StringUtils.text("source", session)%>" style="width:117px;margin-right:3px;"/>
					<input type="button" class="button" onclick="addPhoto();" value="<%=StringUtils.text("button.add", session)%>" style="padding-left:5px;"/>
				</div>
				<ul id="currentphotos" style="width:876px;"><li/></ul>
				</div>
			</fieldset>
			<br/><span id="metadata"></span><br/><br/>
			<a id="pagelink1" target="_blank" href="#" style="display:none;font-size:12px;"><%=StringUtils.text("test.page", session)%> (1)</a>
			<a id="pagelink2" target="_blank" href="#" style="display:none;font-size:12px;"><%=StringUtils.text("test.page", session)%> (2)</a>
			</li></ul>
			<!-- BUTTON PANEL -->
			<table class="toolbar" style="position:relative;top:0;right:0;float:right;margin-top:15px;">
				<tr>
					<td style="padding:0px;"><table style="border:1px solid #AAA;margin:0px;"><tr><td> <%=StringUtils.text("update.mode", session)%>:</td><td><input id="addmode2" type="radio" name="mode2" checked="checked" onclick="$('addmode1').checked = true;"/></td><td><label for="addmode2" style="color:green;"><b><%=StringUtils.text("button.add", session)%></b></label></td><td><input id="modifmode2" type="radio" name="mode2" onclick="$('modifmode1').checked = true;"/></td><td><label for="modifmode2" style="color:orange;"><b><%=StringUtils.text("button.modify", session)%></b></label> </td></tr></table></td>
					<td><input id="upd-save2" type="button" class="button upd-save" onclick="saveResult();" value="<%=StringUtils.text("save", session)%>"/></td>
					<td><input id="upd-delete2" type="button" class="button upd-delete" onclick="deleteResult();" value="<%=StringUtils.text("button.delete", session)%>"/></td>
				</tr>
			</table><br/>
			<table class="toolbar" style="position:relative;top:0;right:0;clear:right;float:right;margin-top:5px;">
				<tr>
					<td><input id="upd-first2" type="button" class="button upd-first" onclick="loadResult('first');" value="<%=StringUtils.text("first", session)%>"/></td>
					<td><input id="upd-previous2" type="button" class="button upd-previous" onclick="loadResult('prev');" value="<%=StringUtils.text("previous", session)%>"/></td>
					<td><input id="upd-find2" type="button" class="button upd-find" onclick="findEntity(0);" value="<%=StringUtils.text("find", session)%>"/></td>
					<td><input id="upd-next2" type="button" class="button upd-next" onclick="loadResult('next');" value="<%=StringUtils.text("next", session)%>"/></td>
					<td><input id="upd-last2" type="button" class="button upd-last" onclick="loadResult('last');" value="<%=StringUtils.text("last", session)%>"/></td>
				</tr>
			</table>
			<div id="msg" style="float:left;"></div>
		</div>
	</div>
	<br/>
	<span class="small"><%=StringUtils.text("for.any.request", session)%> : <a href="mailto:admin@sporthenon.com">admin@sporthenon.com</a></span>
</div>
<script><!--
window.onload = function() {
	initUpdateResults("<%=request.getAttribute("value")%>");
	
	
	$$('#update-results .rendertip').each(function(el) {
		new Control.Window($(document.body).down('[href=#' + el.id + ']'),{
			position: 'relative', hover: true, offsetLeft: 20, offsetTop: 28, className: 'tip'
		});
	});
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>