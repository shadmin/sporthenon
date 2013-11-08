<%@ page language="java" %>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<jsp:include page="/jsp/common/header.jsp" />
<div id="home">
	<div style="float:right;">
		<div class="box">
			<div class="boxtitle">Database Statistics</div>
			<img id="img-stat" src="img/db/loading.gif" alt="Loading..."/>
			<table id="table-stat" style="width:95%;display:none;">
				<tr><td align="left" style="width:100%;">Sports:</td><td id="count-sport" class="stat"></td></tr>
				<tr><td align="left">Events:</td><td id="count-event" class="stat"></td></tr>
				<tr><td align="left">Results:</td><td id="count-result" class="stat"></td></tr>
				<tr><td align="left">Athletes:</td><td id="count-person" class="stat"></td></tr>
			</table>
		</div>
		<div class="box">
			<div class="boxtitle">Last Updates</div>
			<img id="img-updates" src="img/db/loading.gif" alt="Loading..."/>
			<div id="div-updates" style="margin:6px;"></div>
			<div class="more">&nbsp;<a href="#">[+]&nbsp;More</a></div>
		</div>
	</div>
	<div class="homecontent">
		<h2 style="font-family:Georgia;">Welcome to SPORTHENON.COM, the temple of sport results !</h2>
		<div class="fieldset">
			<div class="fstitle info">WHAT IS SPORTHENON.COM ?</div>
			<div class="fscontent">
				Sporthenon aims to be a large database of sports results (opened to contributors).<br/>
				<br/>Below is a description of the site map:<br/><br/>
				<table>
					<tr><th width="150">Menu</th><th>Description</th></tr>
					<tr><td style="background:url(img/menu/dbresults.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="results" style="color:#000;">RESULTS</a></b></td><td>History results organized by sport, competition and event. For example:<ul><li>Athletics&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Olympic Games&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Men&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Long Jump&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1924 to 1992</li><li>Tennis&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Grand Slam&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;French Open (Roland Garros)&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Women&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;All Years</li></ul></td></tr>
					<tr><td style="background:url(img/menu/dbolympics.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="olympics" style="color:#000;">OLYMPICS</a></b></td><td>Event results and medal tables from winter/summer Olympic Games. For example:<ul><li>Summer Olympic Games&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1956, Melbourne&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Men&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Marathon</li><li>Winter Olympic Games&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1988 to 2010&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Medal Tables&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;All Countries</li></ul></td></tr>
					<tr><td style="background:url(img/menu/dbusleagues.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="usleagues" style="color:#000;">US LEAGUES</a></b></td><td>Championships history, individual &amp; team records and statistics from the 4 major US leagues (NFL/NBA/NHL/MLB). For example:<ul><li>NBA&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Records&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Rebounds&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Regular Season</li><li>NHL&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;Hall of Fame&nbsp;<img src="img/arrow.gif" alt="-"/>&nbsp;1985</li></ul></td></tr>
					<tr><td style="background:url(img/menu/dbsearch.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="search" style="color:#000;">SEARCH</a></b></td><td>Search engine to look quickly for a specific term in the entire database.</td></tr>
					<tr><td style="background:url(img/menu/project.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="project" style="color:#000;">PROJECT</a></b></td><td>Some extra information about the Sporthenon project (downloads, statistics...).</td></tr>
					<tr><td style="background:url(img/menu/login.png) 5px center no-repeat;padding:5px;padding-left:25px;"><b><a href="login" style="color:#000;">LOGIN</a></b></td><td>Login as a member. Members have access to specific features and can contribute to update the database.</td></tr>
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
				<img src="img/bullet.gif" alt="-"/>&nbsp;<b>Nov. 8, 2013</b>&nbsp;-&nbsp;Version 0.4.0 Released&nbsp;(<a target="_blank" href="changelog.txt">Change Log</a>)<br/><br/>
				<img src="img/bullet.gif" alt="-"/>&nbsp;<b>Oct. 29, 2013</b>&nbsp;-&nbsp;Version 0.3.0 Released<br/><br/>
				<img src="img/bullet.gif" alt="-"/>&nbsp;<b>Sep. 13, 2013</b>&nbsp;-&nbsp;Version 0.2.0 Released<br/><br/>
				<img src="img/bullet.gif" alt="-"/>&nbsp;<b>Aug. 2, 2013</b>&nbsp;-&nbsp;Version 0.1.0 Released
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	loadHomeData();
</script>
<jsp:include page="/jsp/common/footer.jsp" />