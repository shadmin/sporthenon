<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.meta.News"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%@ page import="com.sporthenon.web.servlet.IndexServlet"%>
<%@ page import="java.util.List" %>
<jsp:include page="/jsp/common/header.jsp" />
<div id="home">
<div class="homecontent">
	<!-- PRESENTATION -->
	<div class="fieldset">
		<div class="fstitle info">PRÉSENTATION</div>
		<div class="fscontent">
			<h3>Bienvenue sur <a href="project">SPORTHENON</a>, le temple des résultats sportifs !</h3><br/>
			<img src='img/bullet.gif' alt='-'/>&nbsp;Sporthenon est une base de données sportive gratuite et <a href="contribute">ouverte aux contributeurs</a><br/>
			<hr/><img src='img/bullet.gif' alt='-'/>&nbsp;<b>Rubriques du site</b><br/>
			<div id="topics"><table><tr>
				<td class="results" onmouseover='overTopic(TEXT_DESC_RESULTS);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.results", session)%></td>
				<td class="olympics" onmouseover='overTopic(TEXT_DESC_OLYMPICS);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.olympics", session)%></td>
				<td class="usleagues" onmouseover='overTopic(TEXT_DESC_USLEAGUES);' onmouseout="$('details').hide();"><%=StringUtils.text("menu.usleagues", session)%></td>
				<td id="details" style="display:none;"></td></tr></table></div>
			<hr/><img src='img/bullet.gif' alt='-'/>&nbsp;<b>Accès direct par sport</b><br/>
			<div id="sports"><%=IndexServlet.getSportDivs(String.valueOf(session.getAttribute("locale")))%></div>
		</div>
	</div>
	<!-- LAST UPDATES -->
	<div class="fieldset">
		<div class="fstitle lastupdates">DERNIERS RÉSULTATS</div>
		<div class="fscontent"><table id="ctupdates"><tr><td><%=StringUtils.text("display.the", session)%>&nbsp;</td><td><input id="countupdt" type="text" maxlength="3" size="2" value="20" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"/></td><td>&nbsp;<%=StringUtils.text("last.updates.2", session)%>&nbsp;</td><td><a href="javascript:refreshLastUpdates();"><%=StringUtils.text("show", session)%></a></td></tr></table>
		<div id="dupdates"></div></div>
	</div>
</div>
<div class="homecontent right" style="display:none;">
	<!-- STATISTICS -->
	<div class="fieldset" style="margin-top:0px;">
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
	<!-- CONTACT -->
	<div class="fieldset">
		<div class="fstitle contact"><%=StringUtils.text("title.contact", session)%></div>
		<div class="fscontent"><a href="mailto:inachos@sporthenon.com">inachos@sporthenon.com</a></div>
	</div>
	<!-- NEWS -->
	<div class="fieldset">
		<div class="fstitle news"><%=StringUtils.text("title.news", session)%></div>
		<div class="fscontent">
		<%
			String lang = String.valueOf(session.getAttribute("locale"));
			boolean isMore = false;
			int i = 0;
			for (News n : (List<News>) DatabaseHelper.execute("from News order by id desc")) {
				if (++i % 5 == 0) {
					isMore = true;
					out.print("<a id='amnews' href=\"javascript:$('amnews').remove();$('mnews').show();\"><br/><br/>More</a><span id='mnews' style='display:none;'>");
				}
				String title = n.getTitle();
				String text = n.getTextHtml();
				if (lang.equalsIgnoreCase("fr")) {
					title = n.getTitleFR();
					text = n.getTextHtmlFR();
				}
				out.print((i > 1 ? "<br/><br/>" : "") + "<img src='img/bullet.gif' alt='-'/>&nbsp;<b>");
				out.print(title + "</b>&nbsp;(" + StringUtils.toTextDate(n.getDate(), lang, "d MMM yyyy") + ")");
				out.print(StringUtils.notEmpty(text) ? "&nbsp;" + text : "");
			}
			if (isMore)
				out.print("</span>");
		%>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
window.onload = function() {
	document.title = '<%=StringUtils.text("title", session)%>';
	$$('#shmenu a').each(function(el){
		$(el).removeClassName('selected');
	});
	loadHomeData();
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />