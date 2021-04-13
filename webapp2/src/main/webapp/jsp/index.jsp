<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.Timestamp"%>
<%@ page import="java.util.*"%>
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
	String sql = "SELECT * FROM sport ORDER BY label" + ResourceUtils.getLocaleParam(lang);
	for (Sport sp : (List<Sport>) DatabaseManager.executeSelect(sql, Sport.class)) {
		sbSports1.append("<option value=\"" + StringUtils.urlEscape("/sport/" + sp.getLabel() + "/" + StringUtils.encode("SP-" + sp.getId())) + "\">" + sp.getLabel(lang) + "</option>");
		sbSports2.append("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
	}
	String url = "http://" + request.getServerName();
%>
--></script>
<div id="home">
<div style="display:flex;width:100%;">
	<!-- Presentation -->
	<div class="homepanel" style="width:100%;">
		<div class="hometitle info"><%=StringUtils.text("title.presentation", session)%></div>
		<%=StringUtils.text("sporthenon.welcome", session)%>
		<br/><br/><%=StringUtils.text("site.topics", session)%> :<br/><br/>
		<img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("menu.results.2", session)%></b> : <%=StringUtils.text("desc.results", session)%><br/><br/>
		<img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("menu.browse.2", session)%></b> : <%=StringUtils.text("desc.browse", session)%><br/><br/>
		<img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("menu.calendar.2", session)%></b> : <%=StringUtils.text("desc.calendar", session)%><br/><br/>
		<img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("menu.olympics.2", session)%></b> : <%=StringUtils.text("desc.olympics", session)%><br/><br/>
		<img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("menu.usleagues.2", session)%></b> : <%=StringUtils.text("desc.usleagues", session)%><br/><br/>
		<img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("menu.search.2", session)%></b> : <%=StringUtils.text("desc.search", session)%><br/>
	</div>
	<div style="display:flex;flex-direction:column;">
		<!-- Random event -->
		<div class="homepanel">
			<div class="hometitle randomevent"><%=StringUtils.text("title.random.event", session)%></div>
			<table id="randomevent" class="noborder">
				<tr><td id="randomeventvalue"><%=IndexServlet.getRandomEvent(lang)%></td>
				<td><a href="javascript:getRandomEvent();"><img alt="Change" title="<%=StringUtils.text("change", session)%>" src="/img/db/refresh.png"/></a></td>
				</tr>
			</table>
		</div>
		<!-- Share -->
		<div class="homepanel">
			<div class="hometitle share"><%=StringUtils.text("title.share", session)%></div>
			<table class="noborder"><tr>
				<td><a href="https://www.facebook.com/sharer/sharer.php?u=<%=url.replaceAll("\\:", "%3A").replaceAll("\\/", "%2F")%>" target="_blank"><img alt="facebook" title="<%=StringUtils.text("share.on", session)%> Facebook" src="/img/header/facebook.png"/></a></td>
				<td><a href="https://twitter.com/share?text=<%=StringUtils.text("title", session).replaceAll("\\s", "%20")%>&amp;url=<%=url.replaceAll("\\:", "%3A").replaceAll("\\/", "%2F")%>" target="_blank"><img alt="twitter" title="<%=StringUtils.text("share.on", session)%> Twitter" src="/img/header/twitter.png"/></a></td>
				<td><a href="https://plus.google.com/share?url<%=url.replaceAll("\\:", "%3A").replaceAll("\\/", "%2F")%>" target="_blank"><img alt="gplus" title="<%=StringUtils.text("share.on", session)%> Google+" src="/img/header/gplus.png"/></a></td>
			</tr></table>
		</div>
		<!-- Mobile application -->
		<div class="homepanel">
			<div class="hometitle mobile"><%=StringUtils.text("title.mobile.app", session)%></div>
			<a href="https://play.google.com/store/apps/details?id=com.sporthenon.android" target="_blank"><img alt="Android app on Google Play" src="/img/header/android<%=lang%>.png"/></a>
		</div>
	</div>
</div>
<!-- Access sport -->
<div class="homepanel">
	<div class="hometitle opensport"><%=StringUtils.text("title.access.sport", session)%></div>
	<select style="margin-left:10px;" onchange="location.href=this.value;">
		<option value="">–– <%=StringUtils.text("select.sport", session)%> ––</option>
		<%=sbSports1.toString()%>
	</select><br/>
	<div id="sports" class="slider"><%@include file="../html/slider.html"%></div>
</div>
<!-- Last updates -->
<div class="homepanel" style="margin-top:10px;">
	<div class="hometitle lastupdates"><%=StringUtils.text("title.last.results", session)%></div>
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
        	params.add(0);
        	params.add(ResourceUtils.getLocaleParam(lang));
        	Collection<LastUpdateBean> coll = (Collection<LastUpdateBean>) DatabaseManager.callFunction("_last_updates", params, LastUpdateBean.class);
        	out.print(HtmlConverter.convertLastUpdates(coll, 0, ITEM_LIMIT, 0, lang));
        	Timestamp ts = null;
        	for (LastUpdateBean bean : coll) {
        		if (ts == null || bean.getLastUpdate().compareTo(ts) > 0) {
        			ts = bean.getLastUpdate();
        		}
        	}
        	request.setAttribute("lastupdate", StringUtils.toTextDate(ts, lang, "d MMM yyyy, HH:mm"));
		%></tbody></table>
	</div>
</div>
<!-- Statistics -->
<div class="homepanel" style="margin-top:10px;">
	<div class="hometitle statistics"><%=StringUtils.text("title.statistics", session)%></div>
	<%
		List<StatisticsBean> stats = (List<StatisticsBean>) DatabaseManager.callFunctionSelect("_statistics", null, StatisticsBean.class);
		StatisticsBean stb = stats.get(0);
	%>
	<div style="float:left;margin-right:10px;">
		<table>
			<tr><th><%=StringUtils.text("entity.SP", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountSport(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.EV", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountEvent(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.RS", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountResult(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.RD", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountRound(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("photos", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountPicture(), lang)%></td></tr>
			<tr><td colspan="2" style="height:10px;border:none;"></td></tr>
			<tr><th><%=StringUtils.text("entity.PR", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountPerson(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.TM", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountTeam(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.CN", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountCountry(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.CT", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountCity(), lang)%></td></tr>
			<tr><th><%=StringUtils.text("entity.CX", session)%></th><td class="stat"><%=StringUtils.formatNumber(stb.getCountComplex(), lang)%></td></tr>
		</table>
	</div>
	<div>
		<table>
			<tr><th style="text-align:center;"><div style="float:left;margin-left:2px;font-weight:normal;"><a href="javascript:changeReport(-1);">&lt; <%=StringUtils.text("previous", session)%></a></div><div style="float:right;margin-right:2px;font-weight:normal;"><a href="javascript:changeReport(1);"><%=StringUtils.text("next", session)%> &gt;</a></div><span id="ctitle">.</span></th></tr>
			<tr><td id="chart"></td></tr>
		</table>
	</div>
</div>
<!-- Statistics -->
<div class="homepanel" style="margin-top:10px;">
	<div class="hometitle extlinks"><%=StringUtils.text("extlinks", session)%></div>
	<table class="noborder">
		<tr>
			<td><img alt="" src="/img/home/github.png"/></td>
			<td><a target="_blank" href="https://github.com/shadmin/sporthenon">Page du projet sur GitHub</a></td>
		</tr>
		<tr>
			<td><img alt="" src="/img/db/export/txt.png"/></td>
			<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/CHANGELOG.txt"><%=StringUtils.text("change.log", session)%></a></td>
		</tr>
		<tr>
			<td><img alt="" src="/img/home/reportbug.png"/></td>
			<td><a target="_blank" href="http://github.com/shadmin/sporthenon/issues"><%=StringUtils.text("report.bug", session)%></a></td>
		</tr>
		<tr>
			<td><img alt="" src="/img/home/wiki.png"/></td>
			<td><a target="_blank" href="http://github.com/shadmin/sporthenon/wiki">Wiki</a></td>
		</tr>
	</table>
</div>
</div><!-- <div id="home"> -->
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