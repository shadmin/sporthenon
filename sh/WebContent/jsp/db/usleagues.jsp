<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="usleagues" class="fieldset">
	<div class="fstitle criteria"><%=StringUtils.text("search.criteria", session)%></div>
	<form id="usleagues-form" action="/usleagues">
	<div class="league">
		<!-- LEAGUE -->
		<table style="margin-left:3px; margin-bottom:2px;">
			<tr>
				<td><input type="radio" name="league" id="nfl" onclick="changeLeague(this.id)" style="margin:0px;" checked="checked"></input></td>
				<td><label for="nfl">NFL</label></td>
				<td><input type="radio" name="league" id="nba" onclick="changeLeague(this.id)" style="margin:0px; margin-left:10px;"></input></td>
				<td><label for="nba">NBA</label></td>
				<td><input type="radio" name="league" id="nhl" onclick="changeLeague(this.id)" style="margin:0px; margin-left:10px;"></input></td>
				<td><label for="nhl">NHL</label></td>
				<td><input type="radio" name="league" id="mlb" onclick="changeLeague(this.id)" style="margin:0px; margin-left:10px;"></input></td>
				<td><label for="mlb">MLB</label></td>
			</tr>
		</table>
		<div id="slider-league-img" class="slider"><%@include file="../../html/slider.html"%></div>
	</div>
	<table id="ustopics">
		<tr>
			<!-- CHAMPIONSHIPS -->
			<td><div id="f-championship" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="championship" checked="checked" onclick="changeModeUS()"></input></td>
					<td><label for="championship">&nbsp;<%=StringUtils.text("championships", session)%></label></td></tr></table>
				</div>
				<div id="championship-inactive" class="inactive-msg" style="width:250px; height:50px;"></div>
				<table style="margin-top:15px;">
					<tr><td><%=StringUtils.text("year", session)%>:</td>
					<td><div id="sm-pl-championship-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div></td>
			<!-- RECORDS -->
			<td><div id="f-record" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="record" onclick="changeModeUS()"></input></td>
					<td><label for="record">&nbsp;<%=StringUtils.text("records", session)%></label></td></tr></table>
				</div>
				<div id="record-inactive" class="inactive-msg" style="width:250px; height:50px;"></div>
				<table>
					<tr><td><%=StringUtils.text("category", session)%>:</td>
					<td><div id="sm-pl-record-se" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
				<table>
					<tr><td style="width:80px;"><select id="pl-record-tp2" name="pl-record-tp2" style="width:115px;"><option/></select></td>
					<td style="width:80px;"><select id="pl-record-tp1" name="pl-record-tp1" style="width:115px;">
						<option value="'Individual'"><%=StringUtils.text("individual", session)%></option>
						<option value="'Team'"><%=StringUtils.text("team", session)%></option>
						<option value="'Individual', 'Team'">[<%=StringUtils.text("all", session)%>]</option>
					</select></td></tr>
				</table>
			</div></td>
			<!-- WINS/LOSSES -->
			<td><div id="f-winloss" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="winloss" onclick="changeModeUS()"></input></td>
					<td><label for="winloss">&nbsp;<%=StringUtils.text("wins.losses", session)%></label></td></tr></table>
				</div>
				<div id="winloss-inactive" class="inactive-msg" style="width:250px; height:50px;"></div>
				<table style="margin-top:15px;">
					<tr><td><%=StringUtils.text("team", session)%>:</td>
					<td><div id="sm-pl-winloss-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div></td>
		</tr>
		<tr>
			<!-- HALL OF FAME -->
			<td><div id="f-hof" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="hof" onclick="changeModeUS()"></input></td>
					<td><label for="hof">&nbsp;<%=StringUtils.text("hall.fame", session)%></label></td></tr></table>
				</div>
				<div id="hof-inactive" class="inactive-msg" style="width:250px; height:50px;"></div>
				<table>
					<tr><td><%=StringUtils.text("year", session)%>:</td>
					<td><div id="sm-pl-hof-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
				<table>
					<tr><td><%=StringUtils.text("position", session)%>:</td>
					<td class="text"><input type="text" name="hof-position" id="hof-position" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"></input></td>
					<td><span id="hof-postip" style="color:#666;cursor:help;font-weight:bold;">[?]</span></td></tr>
				</table>
			</div></td>
			<!-- RETIRED NUMBERS -->
			<td><div id="f-retnum" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="retnum" onclick="changeModeUS()"></input></td>
					<td><label for="retnum">&nbsp;<%=StringUtils.text("retired.numbers", session)%></label></td></tr></table>
				</div>
				<div id="retnum-inactive" class="inactive-msg" style="width:250px; height:50px;"></div>
				<table>
					<tr><td><%=StringUtils.text("team", session)%>:</td>
					<td><div id="sm-pl-retnum-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
				<table>
					<tr><td><%=StringUtils.text("number", session)%>:</td>
					<td class="text"><input type="text" name="retnum-number" id="retnum-number" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"></input></td></tr>
				</table>
			</div></td>
			<!-- TEAM STADIUMS -->
			<td><div id="f-teamstadium" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="teamstadium" onclick="changeModeUS()"></input></td>
					<td><label for="teamstadium">&nbsp;<%=StringUtils.text("team.stadiums", session)%></label></td></tr></table>
				</div>
				<div id="teamstadium-inactive" class="inactive-msg" style="width:250px; height:50px;"></div>
				<table style="margin-top:15px;">
					<tr><td><%=StringUtils.text("team", session)%>:</td>
					<td><div id="sm-pl-teamstadium-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div></td>
		</tr>
	</table>
	</form>
</div>
<%@include file="../../html/buttons.html"%>
<%@include file="../../html/tabcontrol.html"%>
<script type="text/javascript">
var tPos = new Array();
window.onload = function() {
	initSelectMult('sm-pl-championship-yr', TEXT_YEARS, 175);
	initSelectMult('sm-pl-hof-yr', TEXT_YEARS, 175);
	initSelectMult('sm-pl-retnum-tm', TEXT_TEAMS, 170, 60);
	initSelectMult('sm-pl-teamstadium-tm', TEXT_TEAMS, 170, 60);
	initSelectMult('sm-pl-record-se', TEXT_CATEGORIES, 145, 45);
	initSelectMult('sm-pl-winloss-tm', TEXT_TEAMS, 170, 60);
	initSliderUS();
	changeModeUS();
	changeLeague('nfl');
	initTabControl();
<%
	List<Object[]> l = (List<Object[]>) DatabaseHelper.executeNative("select distinct id_league, position from \"HALL_OF_FAME\" where position is not null and position<>'' order by id_league, position");
	for (Object[] tObj : l) {
		String league = String.valueOf(tObj[0]);
		String position = String.valueOf(tObj[1]);
		String labelpos = StringUtils.getUSPosition(new Integer(league), position);
		if (!position.matches(".*\\-.*")) {
%>
	tPos[<%=league%>] = (tPos[<%=league%>] ? tPos[<%=league%>] + '\r\n' : '') + <%="'" + position + "'"%> + ' - ' + <%="'" + labelpos + "'"%>;
<%}}%>
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />