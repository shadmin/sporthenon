<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.function.LastUpdateBean"%>
<%@ page import="com.sporthenon.db.function.StatisticsBean"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%@ page import="com.sporthenon.web.servlet.IndexServlet"%>
<%! @SuppressWarnings("unchecked") %>
<jsp:include page="/jsp/common/header.jsp"/>
<script type="text/javascript"><!--
<%	
	String lang = String.valueOf(session.getAttribute("locale"));
	out.print("t1 = " + System.currentTimeMillis() + ";");
	StringBuffer sbSports1 = new StringBuffer();
	StringBuffer sbSports2 = new StringBuffer();
	String sql = "SELECT * FROM sport ORDER BY label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? "_" + lang : "");
	for (Sport sp : (List<Sport>) DatabaseManager.executeSelect(sql, Sport.class)) {
		sbSports1.append("<option value=\"" + StringUtils.urlEscape("/sport/" + sp.getLabel() + "/" + StringUtils.encode("SP-" + sp.getId())) + "\">" + sp.getLabel(lang) + "</option>");
		sbSports2.append("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
	}
%>
--></script>
<div id="home">
<div class="homecontent">
	<!-- PRESENTATION -->
	<div class="fieldset">
		<div class="fstitle info"><%=StringUtils.text("title.presentation", session)%></div>
		<div class="fscontent">
			<h3><%=StringUtils.text("sporthenon.welcome", session)%></h3><br/>
			<img src='/img/bullet.gif' alt='-'/><%=StringUtils.text("sporthenon.desc", session)%><br/>
			<hr/><img src='/img/bullet.gif' alt='-'/> <b><%=StringUtils.text("site.topics", session)%></b><br/>
			<div id="topics"><table><tr>
				<td class="results" onclick="location.href='/results';" onmouseover='overTopic(TX_DESC_RESULTS);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.results", session)%></td>
				<td class="calendar" onclick="location.href='/calendar';" onmouseover='overTopic(TX_DESC_CALENDAR);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.calendar", session)%></td>
				<td class="olympics" onclick="location.href='/olympics';" onmouseover='overTopic(TX_DESC_OLYMPICS);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.olympics", session)%></td>
				<td class="usleagues" onclick="location.href='/usleagues';" onmouseover='overTopic(TX_DESC_USLEAGUES);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.usleagues", session)%></td>
				<td class="search" onclick="location.href='/search';" onmouseover='overTopic(TX_DESC_SEARCH);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.search", session)%></td>
				<td id="details" style="display:none;"></td></tr></table></div>
			<hr/><img src='/img/bullet.gif' alt='-'/> <b><%=StringUtils.text("random.event", session)%></b><br/><table id="randomevent"><tr><td id="randomeventvalue"><%=IndexServlet.getRandomEvent(lang)%></td><td><a href="javascript:getRandomEvent();"><img alt="Change" title="<%=StringUtils.text("change", session)%>" src="/img/db/refresh.png"/></a></td></tr></table>
			<hr/><img src='/img/bullet.gif' alt='-'/> <b><%=StringUtils.text("access.sport", session)%></b><select style="margin-left:10px;" onchange="location.href=this.value;"><option value="">–– <%=StringUtils.text("select.sport", session)%> ––</option><%=sbSports1.toString()%></select><br/>
			<div id="sports" class="slider"><%@include file="../html/slider.html"%></div>
		</div>
	</div>
	<!-- LAST UPDATES -->
	<div class="fieldset">
		<div class="fstitle lastupdates"><%=StringUtils.text("title.last.results", session)%></div>
		<div class="fscontent">
		<div id="dupdates">
		<table id="dupdates-opts">
			<tr><td><%=StringUtils.text("filter.sport", session)%> :</td>
			<td><select onchange="getLastUpdates(null, null, this.value);"><option value="0">–– <%=StringUtils.text("all.sports", session)%> ––</option><%=sbSports2.toString()%></select></td></tr>
			<tr><td id="dupdates-loading" style="display:none;"><img alt="Loading..." style="float:left;" src="/img/db/loading.gif?6"/></td></tr>
		</table>
		<table id="tlast" class="tsort"><thead><tr class='rsort'>
			<th onclick="sort('tlast', this, 0);"><%=StringUtils.text("year", session)%></th>
			<th onclick="sort('tlast', this, 1);"><%=StringUtils.text("sport", session)%></th>
			<th onclick="sort('tlast', this, 2);"><%=StringUtils.text("event", session)%></th>
			<th onclick="sort('tlast', this, 3);"><%=StringUtils.text("entity.RS.1", session)%></th>
			<th id="tlast-dtcol" class="sorted desc" style="width:100px;" onclick="sort('tlast', this, 4);"><%=StringUtils.text("date", session)%></th>
		</tr></thead><tbody class="tby" id="tb-tlast">
		<%
			final int ITEM_LIMIT = Integer.parseInt(ConfigUtils.getValue("default_lastupdates_limit"));
        	List<Object> params = new ArrayList<Object>();
        	params.add(0);
        	params.add(ITEM_LIMIT);
        	params.add(Integer.valueOf(0));
        	params.add("_" + lang);
        	Collection<LastUpdateBean> coll = (Collection<LastUpdateBean>) DatabaseManager.callFunction("_last_updates", params, LastUpdateBean.class);
        	out.print(HtmlConverter.convertLastUpdates(coll, 0, ITEM_LIMIT, 0, lang));
        	Timestamp ts = null;
        	for (LastUpdateBean bean : coll) {
        		if (ts == null || bean.getLastUpdate().compareTo(ts) > 0)
        			ts = bean.getLastUpdate();
        	}
        	request.setAttribute("lastupdate", StringUtils.toTextDate(ts, lang, "d MMM yyyy, HH:mm"));
		%></tbody></table></div></div>
	</div>
	<!-- STATISTICS -->
	<%
		List<StatisticsBean> stats = (List<StatisticsBean>) DatabaseManager.callFunctionSelect("_statistics", null, StatisticsBean.class);
		StatisticsBean stb = stats.get(0);
	%>
	<div class="fieldset">
		<div class="fstitle statistics"><%=StringUtils.text("title.statistics", session)%></div>
		<div class="fscontent" style="height:440px;overflow:auto;">
			<ul>
			<li style="width:200px;margin-right:5px;"><table>
				<tr><th><%=StringUtils.text("entity.SP", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountSport(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.EV", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountEvent(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.RS", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountResult(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.RD", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountRound(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("photos", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountPicture(), lang)%></td></tr>
			</table><table style="margin-top:10px;">
				<tr><th><%=StringUtils.text("entity.PR", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountPerson(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.TM", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountTeam(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.CN", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountCountry(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.CT", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountCity(), lang)%></td></tr>
				<tr><th><%=StringUtils.text("entity.CX", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountComplex(), lang)%></td></tr>
			</table></li>
			<li><table>
				<tr><th style="text-align:center;"><div style="float:left;margin-left:2px;font-weight:normal;"><a href="javascript:changeReport(-1);">&lt; <%=StringUtils.text("previous", session)%></a></div><div style="float:right;margin-right:2px;font-weight:normal;"><a href="javascript:changeReport(1);"><%=StringUtils.text("next", session)%> &gt;</a></div><span id="ctitle">.</span></th></tr>
				<tr><td id="chart"></td></tr>
			</table></li>
			</ul>
		</div>
	</div>
	<div style="clear:both;"></div>
</div>
</div>
<script type="text/javascript" src="js/RGraph.common.core.js"></script>
<script type="text/javascript" src="js/RGraph.hbar.js"></script>
<script type="text/javascript" src="js/RGraph.line.js"></script>
<script type="text/javascript" src="js/RGraph.pie.js"></script>
<script type="text/javascript"><!--
var ctitle = ['<%=StringUtils.text("report.1", session)%>', '<%=StringUtils.text("report.2", session)%>'];
window.onload = function() {
	tCurrentSortedCol['tlast'] = $('tlast-dtcol');
	initSliderHome("<%=IndexServlet.getSportDivs(lang)%>");
	t2 = <%=System.currentTimeMillis()%>;
	handleRender();
	changeReport(0);
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>