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
		<div class="fscontent" style="height:430px;">
			<div id="ajaxdiv"></div>
			<div style="float:left;padding-right:10px;height:380px;">
			<table style="margin-top:0px;">
				<tr><td colspan="5"><input type="text" id="sp" name="<%=StringUtils.text("entity.SP.1", session)%>"/><a href="javascript:clearValue('sp');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="4"><input type="text" id="cp" name="<%=StringUtils.text("entity.CP.1", session)%>"/><a href="javascript:clearValue('cp');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="3"><input type="text" id="ev" name="<%=StringUtils.text("entity.EV.1", session)%> #1"/><a href="javascript:clearValue('ev');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td colspan="2"><input type="text" id="se" name="<%=StringUtils.text("entity.EV.1", session)%> #2"/><a href="javascript:clearValue('se');">[X]</a></td></tr>
				<tr><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/empty.gif"/></td><td><img alt="" src="/img/component/treeview/join.gif"/></td><td><input type="text" id="se2" name="<%=StringUtils.text("entity.EV.1", session)%> #3"/><a href="javascript:clearValue('se2');">[X]</a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="yr" name="<%=StringUtils.text("entity.YR.1", session)%>"/><a href="javascript:clearValue('yr');">[X]</a></td>
				<td><input id='prevbtn' type='button' class='button' onclick='loadResult("prev");' value=''/></td>
				<td><input id='nextbtn' type='button' class='button' onclick='loadResult("next");' value=''/></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="dt1" name="<%=StringUtils.text("date.from", session)%>"/><a href="javascript:clearValue('dt1');">[X]</a><br/><a href="#" onclick="$('dt1').value='<%=today%>';$('dt1').addClassName('completed2');"><%=StringUtils.text("today", session)%></a>&nbsp;<a href="#" onclick="$('dt1').value='<%=yesterday%>';$('dt1').addClassName('completed2');"><%=StringUtils.text("yesterday", session)%></a></td>
				<td>&nbsp;<input type="text" id="dt2" name="<%=StringUtils.text("date.to", session)%>"/><a href="javascript:clearValue('dt2');">[X]</a><br/><a href="#" onclick="$('dt2').value='<%=today%>';$('dt2').addClassName('completed2');"><%=StringUtils.text("today", session)%></a>&nbsp;<a href="#" onclick="$('dt2').value='<%=yesterday%>';$('dt2').addClassName('completed2');"><%=StringUtils.text("yesterday", session)%></a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="pl1" name="<%=StringUtils.text("venue.city", session)%> #1"/><a href="javascript:clearValue('pl1');">[X]</a></td></tr>
				<tr><td><input type="text" id="pl2" name="<%=StringUtils.text("venue.city", session)%> #2"/><a href="javascript:clearValue('pl2');">[X]</a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="exa" name="<%=StringUtils.text("tie", session)%>"/></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="cmt" name="<%=StringUtils.text("comment", session)%>"/></td></tr>
			</table>
			</div>
			<div style="height:380px;">
			<table style="margin-top:0px;">
				<tr><td><input type="text" id="rk1" name="<%=StringUtils.text("rank.1", session)%>"/><a href="javascript:clearValue('rk1');">[X]</a></td><td>&nbsp;<input type="text" id="rs1" name="<%=StringUtils.text("result.score", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk2" name="<%=StringUtils.text("rank.2", session)%>"/><a href="javascript:clearValue('rk2');">[X]</a></td><td>&nbsp;<input type="text" id="rs2" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk3" name="<%=StringUtils.text("rank.3", session)%>"/><a href="javascript:clearValue('rk3');">[X]</a></td><td>&nbsp;<input type="text" id="rs3" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk4" name="<%=StringUtils.text("rank.4", session)%>"/><a href="javascript:clearValue('rk4');">[X]</a></td><td>&nbsp;<input type="text" id="rs4" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk5" name="<%=StringUtils.text("rank.5", session)%>"/><a href="javascript:clearValue('rk5');">[X]</a></td><td>&nbsp;<input type="text" id="rs5" name="<%=StringUtils.text("entity.RS.1", session)%>" style="width:120px;"/></td></tr>
				<tr><td><input type="text" id="rk6" name="<%=StringUtils.text("rank.6", session)%>"/><a href="javascript:clearValue('rk6');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk7" name="<%=StringUtils.text("rank.7", session)%>"/><a href="javascript:clearValue('rk7');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk8" name="<%=StringUtils.text("rank.8", session)%>"/><a href="javascript:clearValue('rk8');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk9" name="<%=StringUtils.text("rank.9", session)%>"/><a href="javascript:clearValue('rk9');">[X]</a></td></tr>
				<tr><td><input type="text" id="rk10" name="<%=StringUtils.text("rank.10", session)%>"/><a href="javascript:clearValue('rk10');">[X]</a></td></tr>
			</table>
			</div>
			<table><tr>
				<% if (request.getAttribute("id") != null) { %>
				<td><input id='modifybtn' type='button' class='button' onclick='saveResult();' value='<%=StringUtils.text("button.modify", session)%>'/></td>
				<% } else { %>
				<td><input id='addbtn' type='button' class='button' onclick='saveResult();' value='<%=StringUtils.text("button.add", session)%>'/></td>
				<% } %>
				<td id="msg" style="font-weight:bold;padding:5px;"></td>
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