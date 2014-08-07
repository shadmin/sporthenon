<%@ page language="java" %>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.meta.News"%>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%@ page import="java.util.List" %>
<jsp:include page="/jsp/common/header.jsp" />
<div id="home">
	<div style="float:right;">
		<div class="box">
			<div class="boxtitle">Database Statistics</div>
			<img id="img-stat" src="img/db/loading.gif?6" alt="Loading..."/>
			<table id="table-stat" style="width:97%;display:none;">
				<tr><td align="left" style="width:100%;"><img src="img/bullet.gif" alt="-"/>&nbsp;Sports</td><td id="count-sport" class="stat"></td></tr>
				<tr><td align="left"><img src="img/bullet.gif" alt="-"/>&nbsp;Events</td><td id="count-event" class="stat"></td></tr>
				<tr><td align="left"><img src="img/bullet.gif" alt="-"/>&nbsp;Results</td><td id="count-result" class="stat"></td></tr>
				<tr><td align="left" style="padding-bottom:0px;"><img src="img/bullet.gif" alt="-"/>&nbsp;Athletes</td><td id="count-person" class="stat" style="padding-bottom:0px;"></td></tr>
			</table>
		</div>
		<div class="box">
			<div class="boxtitle"><div class="more"><a href="#" title="More" onclick="displayLastUpdates();">[+]</a></div>Last Updates</div>
			<img id="img-updates" src="img/db/loading.gif?6" alt="Loading..."/>
			<div id="div-updates" class="fscontent" style="margin:6px;"></div>
		</div>
	</div>
	<div class="homecontent">
		<!--<h2 style="font-family:Verdana;">Welcome to SPORTHENON.COM, the temple of sport results!</h2>-->
		<div class="fieldset">
			<div class="fstitle info">WHAT IS SPORTHENON.COM ?</div>
			<div class="fscontent">
				Sporthenon aims to be a large database of sports results (opened to contributors).<br/>
				<br/>Below is a description of the site map:<br/><br/>
				<table>
					<tr><th style="width:150px;">Menu</th><th>Description</th></tr>
					<tr><td style="background:url(img/menu/dbresults.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="results" style="color:#000;">RESULTS</a></b></td><td>History results organized by sport, competition and event. For example:<ul><li>Athletics&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Olympic Games&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Men&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Long Jump&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1924 to 1992</li><li>Tennis&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Grand Slam&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;French Open (Roland Garros)&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Women&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;All Years</li></ul></td></tr>
					<tr><td style="background:url(img/menu/dbolympics.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="olympics" style="color:#000;">OLYMPICS</a></b></td><td>Event results and medal tables from winter/summer Olympic Games. For example:<ul><li>Summer Olympic Games&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1956, Melbourne&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Men&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Marathon</li><li>Winter Olympic Games&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1988 to 2010&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Medal Tables&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;All Countries</li></ul></td></tr>
					<tr><td style="background:url(img/menu/dbusleagues.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="usleagues" style="color:#000;">US LEAGUES</a></b></td><td>Championships history, individual &amp; team records and statistics from the 4 major US leagues (NFL/NBA/NHL/MLB). For example:<ul><li>NBA&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Records&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Rebounds&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Regular Season</li><li>NHL&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Hall of Fame&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1985</li></ul></td></tr>
					<tr><td style="background:url(img/menu/dbsearch.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="search" style="color:#000;">SEARCH</a></b></td><td>Search engine to look quickly for a specific term in the entire database.</td></tr>
					<tr><td style="background:url(img/menu/project.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="project" style="color:#000;">PROJECT</a></b></td><td>Some extra information about the Sporthenon project (downloads, statistics...).</td></tr>
				</table>
			</div>
		</div>
		<div class="fieldset">
			<div class="fstitle contact">CONTACT</div>
			<div class="fscontent">For any question/comment, send an e-mail to: <a href="mailto:inachos@sporthenon.com">inachos@sporthenon.com</a></div>
		</div>
		<div class="fieldset">
			<div class="fstitle news">NEWS</div>
			<div class="fscontent">
			<%
				boolean isMore = false;
				int i = 0;
				for (News n : (List<News>) DatabaseHelper.execute("from News order by date desc")) {
					if (++i % 5 == 0) {
						isMore = true;
						out.print("<a id='amnews' href=\"javascript:$('amnews').remove();$('mnews').show();\"><br/><br/>More</a><span id='mnews' style='display:none;'>");
					}
					out.print((i > 1 ? "<br/><br/>" : "") + "<img src='img/bullet.gif' alt='-'/>&nbsp;<b>");
					out.print(n.getDateText() + "</b>&nbsp;-&nbsp;" + n.getTextHtml());
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
		if ($(el).id == 'shmenu-home') {
			$(el).addClassName('selected');
		}
		else {
			$(el).removeClassName('selected');
		}
	});
	loadHomeData();
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />