<%@page import="java.sql.Timestamp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.meta.News"%>
<%@ page import="com.sporthenon.db.function.LastUpdateBean"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%@ page import="com.sporthenon.web.HtmlConverter"%>
<%@ page import="com.sporthenon.web.servlet.IndexServlet"%>
<jsp:include page="/jsp/common/header.jsp" />
<script type="text/javascript"><!--
	t1 = <%=System.currentTimeMillis()%>;
--></script>
<div id="home">
<div class="homecontent">
	<!-- PRESENTATION -->
	<div class="fieldset">
		<div class="fstitle info"><%=StringUtils.text("title.presentation", session)%></div>
		<div class="fscontent">
			<h3><%=StringUtils.text("sporthenon.welcome", session)%></h3><br/>
			<img src='/img/bullet.gif' alt='-'/>&nbsp;<%=StringUtils.text("sporthenon.desc", session)%></a><br/>
			<hr/><img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("site.topics", session)%></b><br/>
			<div id="topics"><table><tr>
				<td class="results" onclick="location.href='results';" onmouseover='overTopic(TX_DESC_RESULTS);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.results", session)%></td>
				<td class="olympics" onclick="location.href='olympics';" onmouseover='overTopic(TX_DESC_OLYMPICS);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.olympics", session)%></td>
				<td class="usleagues" onclick="location.href='usleagues';" onmouseover='overTopic(TX_DESC_USLEAGUES);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.usleagues", session)%></td>
				<td id="details" style="display:none;"></td></tr></table></div>
			<hr/><img src='/img/bullet.gif' alt='-'/>&nbsp;<b><%=StringUtils.text("access.sport", session)%></b><br/>
			<div id="sports" class="slider"><%@include file="../html/slider.html" %></div>
		</div>
	</div>
	<!-- LAST UPDATES -->
	<div class="fieldset">
		<div class="fstitle lastupdates"><%=StringUtils.text("title.last.results", session)%></div>
		<div class="fscontent">
		<div id="dupdates">
		<table id="tlast" class="tsort"><thead><tr class='rsort'>
			<th onclick="sort('tlast', this, 0);"><%=StringUtils.text("year", session)%></th>
			<th onclick="sort('tlast', this, 1);"><%=StringUtils.text("sport", session)%></th>
			<th onclick="sort('tlast', this, 2);"><%=StringUtils.text("event", session)%></th>
			<th onclick="sort('tlast', this, 3);"><%=StringUtils.text("entity.RS.1", session)%></th>
			<th id="tlast-dtcol" class="sorted desc" onclick="sort('tlast', this, 4);"><%=StringUtils.text("updated.on", session)%></th>
		</tr></thead><tbody class="tby" id="tb-tlast">
		<%
			String lang = String.valueOf(session.getAttribute("locale"));
        	ArrayList<Object> lParams = new ArrayList<Object>();
        	lParams.add(new Integer(20));
        	lParams.add(new Integer(0));
        	lParams.add("_" + lang);
        	Collection coll = DatabaseHelper.call("LastUpdates", lParams);
        	out.print(HtmlConverter.convertLastUpdates(coll, 20, 0, lang));
        	Timestamp ts = null;
        	for (Object o : coll) {
        		LastUpdateBean bean = (LastUpdateBean) o;
        		if (ts == null || bean.getRsUpdate().compareTo(ts) > 0)
        			ts = bean.getRsUpdate();
        	}
        	request.setAttribute("lastupdate", StringUtils.toTextDate(ts, lang, "dd/MM/yyyy"));
		%></tbody></table></div></div>
	</div>
	<!-- STATISTICS -->
	<div class="fieldset" style="width:300px;float:right;">
		<div class="fstitle statistics"><%=StringUtils.text("title.statistics", session)%></div>
		<div class="fscontent" style="padding:15px;">
			<table>
				<tr><th style="width:130px;"><%=StringUtils.text("entity.SP", session)%></th><td id="count-sport" class="stat"></td></tr>
				<tr><th><%=StringUtils.text("entity.EV", session)%></th><td id="count-event" class="stat"></td></tr>
				<tr><th><%=StringUtils.text("entity.RS", session)%></th><td id="count-result" class="stat"></td></tr>
				<tr><th><%=StringUtils.text("entity.PR", session)%></th><td id="count-person" class="stat"></td></tr>
			</table>
		</div>
	</div>
	<!-- NEWS -->
	<div class="fieldset" style="margin-right:310px;">
		<div class="fstitle news"><%=StringUtils.text("title.news", session)%></div>
		<div class="fscontent">
		<%
			boolean isMore = false;
			int i = 0;
			for (News n : (List<News>) DatabaseHelper.execute("from News order by id desc")) {
				if (++i == 5) {
					isMore = true;
					out.print("<a id='amnews' href=\"javascript:$('amnews').remove();$('mnews').show();\"><br/><br/>" + StringUtils.text("more", session) + "</a><span id='mnews' style='display:none;'>");
				}
				String title = n.getTitle();
				String text = n.getTextHtml();
				if (lang.equalsIgnoreCase("fr")) {
					title = n.getTitleFR();
					text = n.getTextHtmlFR();
				}
				out.print((i > 1 ? "<br/><br/>" : "") + "<img src='/img/bullet.gif' alt='-'/>&nbsp;<b>");
				out.print(title + "</b>&nbsp;(" + StringUtils.toTextDate(n.getDate(), lang, "d MMM yyyy") + ")");
				out.print(StringUtils.notEmpty(text) ? "&nbsp;" + text : "");
			}
			if (isMore)
				out.print("</span>");
		%>
		</div>
	</div>
	<div style="clear:both;"></div>
</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	loadHomeData();
	tCurrentSortedCol['tlast'] = $('tlast-dtcol');
	initSliderHome("<%=IndexServlet.getSportDivs(lang)%>");
	t2 = <%=System.currentTimeMillis()%>;
	handleRender();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />