<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="calendar" class="fieldset">
	<div class="fstitle criteria"><%=StringUtils.text("search.criteria", session)%></div>
	<ul>
	<li id="dateimg"><div id="day"></div><div id="month"></div><div id="year"></div></li>
	<li style="display:inline-block;">
		<table>
			<tr><td><%=StringUtils.text("year", session)%>&nbsp;:</td><td><select id="yr" onchange="refreshDate();">
			<%
				int y = Calendar.getInstance().get(Calendar.YEAR);
				for (int i = 1851 ; i <= y + 5 ; i++) {
					out.print("<option value='" + i + "'" + (i == y ? " selected='selected'" : "") + ">" + i + "</option>");
				}
			%></select></td></tr>
			<tr><td><%=StringUtils.text("month", session)%>&nbsp;:</td><td><select id="mo" onchange="refreshDate();"><option value=""></option>
			<%
				int m = Calendar.getInstance().get(Calendar.MONTH) + 1;
				for (int i = 1 ; i <= 12 ; i++) {
					String s = (i < 10 ? "0" : "") + i;
					out.print("<option value='" + s + "'" + (i == m ? " selected='selected'" : "") + ">" + s + " – " + StringUtils.text("month." + i, session) + "</option>");
				}
			%></select></td></tr>
			<tr><td><%=StringUtils.text("day", session)%>&nbsp;:</td><td><select id="dt" onchange="refreshDate();"><option value=""></option>
			<%
				int d = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				for (int i = 1 ; i <= 31 ; i++) {
					out.print("<option value='" + i + "'" + (i == d ? " selected='selected'" : "") + ">" + i + "</option>");
				}
			%></select></td></tr>
		</table>
	</li>
	</ul>
</div>
<%@include file="../../html/buttons.html" %>
<%@include file="../../html/tabcontrol.html" %>
<script type="text/javascript"><!--
window.onload = function() {
	initTabControl();
	refreshDate();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />