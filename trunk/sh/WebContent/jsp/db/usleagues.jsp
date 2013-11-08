<%@ page language="java"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="java.util.Collection"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="usleagues" class="fieldset">
	<div class="fstitle criteria">SEARCH CRITERIA</div>
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
					<td><label for="championship">&nbsp;Championships</label></td></tr></table>
				</div>
				<div id="championship-inactive" class="inactive-msg" style="width:230px; height:50px;"></div>
				<table style="margin-top:15px;">
					<tr><td>Year:</td>
					<td><div id="sm-pl-championship-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div></td>
			<!-- RECORDS -->
			<td><div id="f-record" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="record" onclick="changeModeUS()"></input></td>
					<td><label for="record">&nbsp;Records</label></td></tr></table>
				</div>
				<div id="record-inactive" class="inactive-msg" style="width:230px; height:50px;"></div>
				<table>
					<tr><td>Stat:</td>
					<td><div id="sm-pl-record-se" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
				<table>
					<tr><td><div id="sm-pl-record-ev" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td>
					<td style="width:60px;"><select id="pl-record-tp" name="pl-record-tp" style="width:75px;">
						<option value="'Individual'">Individual</option>
						<option value="'Team'">Team</option>
						<option value="'Individual', 'Team'">Both</option>
					</select></td></tr>
				</table>
			</div></td>
			<!-- WINS/LOSSES -->
			<td><div id="f-winloss" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="winloss" onclick="changeModeUS()"></input></td>
					<td><label for="winloss">&nbsp;Wins/Losses</label></td></tr></table>
				</div>
				<div id="winloss-inactive" class="inactive-msg" style="width:230px; height:50px;"></div>
				<table style="margin-top:15px;">
					<tr><td>Team:</td>
					<td><div id="sm-pl-winloss-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div></td>
		</tr>
		<tr>
			<!-- HALL OF FAME -->
			<td><div id="f-hof" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="hof" onclick="changeModeUS()"></input></td>
					<td><label for="hof">&nbsp;Hall of Fame</label></td></tr></table>
				</div>
				<div id="hof-inactive" class="inactive-msg" style="width:230px; height:50px;"></div>
				<table style="margin-top:15px;">
					<tr><td>Year:</td>
					<td><div id="sm-pl-hof-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div></td>
			<!-- RETIRED NUMBERS -->
			<td><div id="f-retnum" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="retnum" onclick="changeModeUS()"></input></td>
					<td><label for="retnum">&nbsp;Retired Numbers</label></td></tr></table>
				</div>
				<div id="retnum-inactive" class="inactive-msg" style="width:230px; height:50px;"></div>
				<table>
					<tr><td>Team:</td>
					<td><div id="sm-pl-retnum-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
				<table>
					<tr><td>Number:</td>
					<td class="text"><input type="text" name="retnum-number" id="retnum-number"></input></td></tr>
				</table>
			</div></td>
			<!-- TEAM STADIUMS -->
			<td><div id="f-teamstadium" class="fieldset">
				<div class="legend">
					<table><tr><td><input type="radio" name="topic" id="teamstadium" onclick="changeModeUS()"></input></td>
					<td><label for="teamstadium">&nbsp;Team Stadiums</label></td></tr></table>
				</div>
				<div id="teamstadium-inactive" class="inactive-msg" style="width:230px; height:50px;"></div>
				<table>
					<tr><td>Team:</td>
					<td><div id="sm-pl-teamstadium-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
				<table>
					<tr><td>From:</td>
					<td class="text"><input type="text" name="teamstadium-from" id="teamstadium-from"></input></td>
					<td>&nbsp;To:</td>
					<td class="text"><input type="text" name="teamstadium-to" id="teamstadium-to"></input></td></tr>
				</table>
			</div></td>
		</tr>
	</table>
	</form>
</div>
<%@include file="../../html/buttons.html"%>
<%@include file="../../html/tabcontrol.html"%>
<script type="text/javascript">
	initSelectMult('sm-pl-championship-yr', 'Years', 165);
	initSelectMult('sm-pl-hof-yr', 'Years', 165);
	initSelectMult('sm-pl-retnum-tm', 'Teams', 160, 60);
	initSelectMult('sm-pl-teamstadium-tm', 'Teams', 160, 60);
	initSelectMult('sm-pl-record-ev', 'Stats', 120, 80);
	initSelectMult('sm-pl-record-se', 'Types', 165, 45);
	initSelectMult('sm-pl-winloss-tm', 'Teams', 160, 60);
	initSliderUS();
	changeModeUS();
	changeLeague('nfl');
</script>
<jsp:include page="/jsp/common/footer.jsp" />