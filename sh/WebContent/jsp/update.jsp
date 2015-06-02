<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%
String lang = String.valueOf(session.getAttribute("locale"));
Calendar cal = Calendar.getInstance();
String today = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
cal.add(Calendar.DAY_OF_YEAR, -1);
String yesterday = StringUtils.toTextDate(new Timestamp(cal.getTimeInMillis()), lang, "dd/MM/yyyy");
%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update">
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("create.modify.result", session)%></div>
		<div class="fscontent" style="height:auto;">
			<!-- RESULT INFO -->
			<div style="float:left;padding-right:10px;">
			<table style="margin-top:0px;">
				<tr><td colspan="5"><input type="text" id="sp" tabindex="1" name="<%=StringUtils.text("entity.SP.1", session)%>"/><a href="javascript:clearValue('sp');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="4"><input type="text" id="cp" tabindex="2" name="<%=StringUtils.text("entity.CP.1", session)%>"/><a href="javascript:clearValue('cp');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="3"><input type="text" id="ev" tabindex="3" name="<%=StringUtils.text("entity.EV.1", session)%> #1"/><a href="javascript:clearValue('ev');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="2"><input type="text" id="se" tabindex="4" name="<%=StringUtils.text("entity.EV.1", session)%> #2"/><a href="javascript:clearValue('se');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td><input type="text" tabindex="5" id="se2" name="<%=StringUtils.text("entity.EV.1", session)%> #3"/><a href="javascript:clearValue('se2');">[X]</a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="yr" tabindex="6" name="<%=StringUtils.text("entity.YR.1", session)%>"/><a href="javascript:clearValue('yr');">[X]</a></td>
				<td><input id='prevbtn' type='button' class='button' onclick='loadResult("prev");' value=''/></td>
				<td><input id='nextbtn' type='button' class='button' onclick='loadResult("next");' value=''/></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="dt1" tabindex="7" name="<%=StringUtils.text("date.from", session)%>"/><a href="javascript:clearValue('dt1');">[X]</a><br/><a href="#" onclick="$('dt1').value='<%=today%>';$('dt1').addClassName('completed2');"><%=StringUtils.text("today", session)%></a>&nbsp;<a href="#" onclick="$('dt1').value='<%=yesterday%>';$('dt1').addClassName('completed2');"><%=StringUtils.text("yesterday", session)%></a></td>
				<td>&nbsp;<input type="text" id="dt2" tabindex="8" name="<%=StringUtils.text("date.to", session)%>"/><a href="javascript:clearValue('dt2');">[X]</a><br/><a href="#" onclick="$('dt2').value='<%=today%>';$('dt2').addClassName('completed2');"><%=StringUtils.text("today", session)%></a>&nbsp;<a href="#" onclick="$('dt2').value='<%=yesterday%>';$('dt2').addClassName('completed2');"><%=StringUtils.text("yesterday", session)%></a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="pl1" tabindex="9" name="<%=StringUtils.text("venue.city", session)%> #1"/><a href="javascript:clearValue('pl1');">[X]</a></td></tr>
				<tr><td><input type="text" id="pl2" tabindex="10" name="<%=StringUtils.text("venue.city", session)%> #2"/><a href="javascript:clearValue('pl2');">[X]</a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="exa" tabindex="11" name="<%=StringUtils.text("tie", session)%>"/></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="cmt" tabindex="12" name="<%=StringUtils.text("comment", session)%>"/></td></tr>
			</table>
			</div>
			<!-- RANKINGS -->
			<div>
			<table style="margin-top:0px;">
				<tr><td><input type="text" id="rk1" tabindex="100" name="<%=StringUtils.text("rank.1", session)%>"/><a href="javascript:clearValue('rk1');">[X]</a></td><td>&nbsp;<input type="text" id="rs1" tabindex="101" name="<%=StringUtils.text("result.score", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk2" tabindex="102" name="<%=StringUtils.text("rank.2", session)%>"/><a href="javascript:clearValue('rk2');">[X]</a></td><td>&nbsp;<input type="text" id="rs2" tabindex="103" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk3" tabindex="104" name="<%=StringUtils.text("rank.3", session)%>"/><a href="javascript:clearValue('rk3');">[X]</a></td><td>&nbsp;<input type="text" id="rs3" tabindex="105" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk4" tabindex="106" name="<%=StringUtils.text("rank.4", session)%>"/><a href="javascript:clearValue('rk4');">[X]</a></td><td>&nbsp;<input type="text" id="rs4" tabindex="107" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk5" tabindex="108" name="<%=StringUtils.text("rank.5", session)%>"/><a href="javascript:clearValue('rk5');">[X]</a></td><td>&nbsp;<input type="text" id="rs5" tabindex="109" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk6" tabindex="110" name="<%=StringUtils.text("rank.6", session)%>"/><a href="javascript:clearValue('rk6');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk7" tabindex="111" name="<%=StringUtils.text("rank.7", session)%>"/><a href="javascript:clearValue('rk7');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk8" tabindex="112" name="<%=StringUtils.text("rank.8", session)%>"/><a href="javascript:clearValue('rk8');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk9" tabindex="113" name="<%=StringUtils.text("rank.9", session)%>"/><a href="javascript:clearValue('rk9');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk10" tabindex="114" name="<%=StringUtils.text("rank.10", session)%>"/><a href="javascript:clearValue('rk10');">[X]</a></td></tr>
			</table>
			<table>
				<tr><td><textarea id="exl" tabindex="13" name="<%=StringUtils.text("extlinks", session)%>" cols="100" rows="5" style="width:400px;"><%=StringUtils.text("extlinks", session)%></textarea></td></tr>
			</table>
			</div>
			<!-- DRAWS -->
			<div style="margin-top:20px;">
			<fieldset style="border:1px solid #AAA;">
			<legend><a href="javascript:toggleDraw();"><img id="drawimg" alt="" src="/img/render/expand.gif"/>&nbsp;<%=StringUtils.text("entity.DR.1", session).toUpperCase()%></a></legend>
			<table id="draw" style="display:none;margin-top:10px;">
				<tr><td><input type="text" id="qf1w" tabindex="1000" name="<%=StringUtils.text("quarterfinal", session)%> #1 - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('qf1w');">[X]</a></td></tr>
				<tr><td><input type="text" id="qf1l" tabindex="1001" name="<%=StringUtils.text("quarterfinal", session)%> #1 - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('qf1l');">[X]</a></td></tr>
				<tr><td style="text-align:center;">&nbsp;<input type="text" id="qf1rs" tabindex="1002" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td></tr>
				<tr><td><input type="text" id="qf2w" tabindex="1003" name="<%=StringUtils.text("quarterfinal", session)%> #2 - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('qf2w');">[X]</a></td><td style="padding-left:50px;"><input type="text" id="sf1w" tabindex="1012" name="<%=StringUtils.text("semifinal", session)%> #1 - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('sf1w');">[X]</a></td></tr>
				<tr><td><input type="text" id="qf2l" tabindex="1004" name="<%=StringUtils.text("quarterfinal", session)%> #2 - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('qf2l');">[X]</a></td><td style="padding-left:50px;"><input type="text" id="sf1l" tabindex="1013" name="<%=StringUtils.text("semifinal", session)%> #1 - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('sf1l');">[X]</a></td></tr>
				<tr><td style="text-align:center;">&nbsp;<input type="text" id="qf2rs" tabindex="1005" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td><td style="padding-left:50px;text-align:center;">&nbsp;<input type="text" id="sf1rs" tabindex="1014" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td></tr>
				<tr><td><input type="text" id="qf3w" tabindex="1006" name="<%=StringUtils.text("quarterfinal", session)%> #3 - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('qf3w');">[X]</a></td><td style="padding-left:50px;"><input type="text" id="sf2w" tabindex="1015" name="<%=StringUtils.text("semifinal", session)%> #2 - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('sf2w');">[X]</a></td></tr>
				<tr><td><input type="text" id="qf3l" tabindex="1007" name="<%=StringUtils.text("quarterfinal", session)%> #3 - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('qf3l');">[X]</a></td><td style="padding-left:50px;"><input type="text" id="sf2l" tabindex="1016" name="<%=StringUtils.text("semifinal", session)%> #2 - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('sf2l');">[X]</a></td></tr>
				<tr><td style="text-align:center;">&nbsp;<input type="text" id="qf3rs" tabindex="1008" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td><td style="padding-left:50px;text-align:center;">&nbsp;<input type="text" id="sf2rs" tabindex="1017" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td></tr>
				<tr><td><input type="text" id="qf4w" tabindex="1009" name="<%=StringUtils.text("quarterfinal", session)%> #4 - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('qf4w');">[X]</a></td><td style="padding-left:50px;"><input type="text" id="thdw" tabindex="1018" name="<%=StringUtils.text("third.place", session)%> - <%=StringUtils.text("winner", session)%>"/><a href="javascript:clearValue('thdw');">[X]</a></td></tr>
				<tr><td><input type="text" id="qf4l" tabindex="1010" name="<%=StringUtils.text("quarterfinal", session)%> #4 - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('qf4l');">[X]</a></td><td style="padding-left:50px;"><input type="text" id="thdl" tabindex="1019" name="<%=StringUtils.text("third.place", session)%> - <%=StringUtils.text("loser", session)%>"/><a href="javascript:clearValue('thdl');">[X]</a></td></tr>
				<tr><td style="text-align:center;">&nbsp;<input type="text" id="qf4rs" tabindex="1011" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td><td style="padding-left:50px;text-align:center;">&nbsp;<input type="text" id="thdrs" tabindex="1020" name="<%=StringUtils.text("score", session)%>" style="width:150px;"/></td></tr>
			</table>
			</fieldset>
			</div>
			<table><tr>
				<td><input id='addbtn' type='button' class='button' onclick='saveResult();' value='<%=StringUtils.text("button.add", session)%>'/></td>
				<td><input id='modifybtn' type='button' class='button' onclick='saveResult();' value='<%=StringUtils.text("button.modify", session)%>' style="display:none;"/></td>
				<td id="msg" style="font-weight:bold;padding:5px;height:45px;"></td>
			</tr></table>
		</div>
	</div>
</div>
<script type="text/javascript">
window.onload = function() {
	initUpdate("<%=request.getAttribute("value")%>");
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />