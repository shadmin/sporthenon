<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="java.util.Calendar"%>
<jsp:include page="/jsp/common/header.jsp"/>
<%
	int y = Calendar.getInstance().get(Calendar.YEAR);
	int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
	int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	StringBuffer sbY = new StringBuffer();
	StringBuffer sbM = new StringBuffer("<option value=''>--- " + StringUtils.text("all", session) + " ---</option>");
	StringBuffer sbD = new StringBuffer("<option value=''>--- " + StringUtils.text("all", session) + " ---</option>");
	for (int i = 1851 ; i <= y + 5 ; i++) {
		sbY.append("<option value='" + i + "'" + (i == y ? " selected='selected'" : "") + ">" + i + "</option>");
	}
	for (int i = 1 ; i <= 12 ; i++) {
		String s = (i < 10 ? "0" : "") + i;
		sbM.append("<option value='" + s + "'" + (i == m ? " selected='selected'" : "") + ">" + s + " " + StringUtils.SEP1 + " " + StringUtils.text("month." + i, session) + "</option>");
	}
	for (int i = 1 ; i <= 31 ; i++) {
		sbD.append("<option value='" + i + "'" + (i == d ? " selected='selected'" : "") + ">" + i + "</option>");
	}
%>
<div id="title-calendar" class="title">
	<div><%=StringUtils.text("menu.calendar", session)%></div>
</div>
<div id="calendar">
	<ul>
	<!-- DATE 1 -->
	<li class="dateimg"><div id="label1" class="label" style="display:none;"><%=StringUtils.text("date.from", session).toUpperCase()%></div><div id="day1" class="day"></div><div id="month1" class="month"></div><div id="year1" class="year"></div></li>
	<li>
		<table class="noborder" style="padding-right:0px;">
			<tr><td class="ccaption"><%=StringUtils.text("year", session)%> :</td><td><select id="yr1" onchange="refreshDate(1);"><%=sbY.toString()%></select></td></tr>
			<tr><td class="ccaption"><%=StringUtils.text("month", session)%> :</td><td><select id="mo1" onchange="refreshDate(1);"><%=sbM.toString()%></select></td></tr>
			<tr><td class="ccaption"><%=StringUtils.text("day", session)%> :</td><td><select id="dt1" onchange="refreshDate(1);"><%=sbD.toString()%></select></td></tr>
		</table>
	</li>
	<!-- DATE 2 -->
	<li id="date2-1" class="dateimg" style="display:none;"><div class="label"><%=StringUtils.text("date.to", session).toUpperCase()%></div><div id="day2" class="day"></div><div id="month2" class="month"></div><div id="year2" class="year"></div></li>
	<li id="date2-2" style="display:none;">
		<table class="noborder">
			<tr><td class="ccaption"><%=StringUtils.text("year", session)%> :</td><td><select id="yr2" onchange="refreshDate(2);"><%=sbY.toString()%></select></td></tr>
			<tr><td class="ccaption"><%=StringUtils.text("month", session)%> :</td><td><select id="mo2" onchange="refreshDate(2);"><%=sbM.toString()%></select></td></tr>
			<tr><td class="ccaption"><%=StringUtils.text("day", session)%> :</td><td><select id="dt2" onchange="refreshDate(2);"><%=sbD.toString()%></select></td></tr>
		</table>
	</li>
	<li id="spanbutton">
		<table class="noborder toolbar">
			<tr><td><input id="calendarspan" type="button" class="button calendarspan" onclick="showDate2();" value="<%=StringUtils.text("define.span", session)%>"/></td></tr>
		</table>
	</li>
	</ul>
</div>
<%@include file="../../html/buttons.html"%>
<div id="errperiod">
	<%=StringUtils.text("err.period.too.large", session)%>
</div>
<script><!--
var todayY = '<%=y%>';
var todayM = '<%=(m < 10 ? "0" : "") + m%>';
var todayD = '<%=d%>';
window.onload = function() {
	refreshDate(1);
	refreshDate(2);
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>