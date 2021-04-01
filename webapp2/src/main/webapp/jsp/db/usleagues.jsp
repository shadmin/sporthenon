<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.entity.*"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.PicklistItem"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.web.servlet.USLeaguesServlet"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="usleagues" class="fieldset">
	<div class="fstitle criteria"><%=StringUtils.text("search.criteria", session)%></div>
	<form id="usleagues-form" action="/usleagues">
	<ul>
	<li class="league">
		<div id="slider-league-img" class="slider"><%@include file="../../html/slider.html"%></div>
	</li>
	<li id="usltype">
		<div id="nfl" class="selected" onclick="changeLeague(this.id);"><img alt="-" src="/img/bullet.gif"/> NFL &#150; National Football League</div>
		<div id="nba" onclick="changeLeague(this.id);"><img alt="-" src="/img/bullet.gif"/> NBA &#150; National Basketball Association</div>
		<div id="nhl" onclick="changeLeague(this.id);"><img alt="-" src="/img/bullet.gif"/> NHL &#150; National Hockey League</div>
		<div id="mlb" onclick="changeLeague(this.id);" style="margin-bottom:0px;"><img alt="-" src="/img/bullet.gif"/> MLB &#150; Major League Baseball</div>
	</li>
	<li style="display:inline-block;width:400px;height:170px;" class="fieldset">
		<div style="float:left;width:auto;">
			<table id="usstype">
				<tr><td id="championships" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/> <%=ResourceUtils.getText("championships", "en")%></td></tr>
				<tr><td id="records" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/> <%=ResourceUtils.getText("records", "en")%></td></tr>
				<tr><td id="stats" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/> <%=ResourceUtils.getText("yearly.stats", "en")%></td></tr>
				<tr><td id="hof" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/> <%=ResourceUtils.getText("hall.fame", "en")%></td></tr>
				<tr><td id="retnum" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/> <%=ResourceUtils.getText("retired.numbers", "en")%></td></tr>
				<tr><td id="teamstadiums" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/> <%=ResourceUtils.getText("team.stadiums", "en")%></td></tr>
			</table>
		</div>
		<div id="ustopics">
			<!-- CHAMPIONSHIPS -->
			<div id="f-championships" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("year", "en")%>:<br/>
				<div id="sm-pl-championships-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
			</div>
			<!-- RECORDS -->
			<div id="f-records" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("type", "en")%>:<br/>
				<table style="margin-bottom:5px;">
					<tr><td colspan="2"><select id="pl-records-tp1" name="pl-records-tp1" style="width:130px;" onchange="changeRcType();">
						<option value="i"><%=ResourceUtils.getText("individual", "en")%></option>
						<option value="t"><%=ResourceUtils.getText("team", "en")%></option>
						<option value="it">[<%=ResourceUtils.getText("all", "en")%>]</option>
					</select></td></tr>
					<tr><td><img src="/img/component/treeview/join.gif" alt="L"/></td>
					<td><select id="pl-records-tp2" name="pl-records-tp2" style="width:130px;"><option/></select></td></tr>
				</table>
				&nbsp;<%=ResourceUtils.getText("category", "en")%>:<br/>
				<div id="sm-pl-records-ct" class="selmultiple" style="margin-bottom:8px;"><%@include file="../../html/selectmult.html"%></div>
				<table><tr><td><input type="checkbox" name="records-pf" id="records-pf"/></td><td><label id="lrecords-pf" for="records-pf">Include postseason/Super Bowl</label></td></tr></table>
			</div>
			<!-- HALL OF FAME -->
			<div id="f-hof" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("year", "en")%>:<br/>
				<div id="sm-pl-hof-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
				<br/><table><tr>
					<td>&nbsp;<%=ResourceUtils.getText("position", "en")%>:</td>
					<td class="text"><input type="text" name="hof-position" id="hof-position" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"></input></td>
					<td><span id="hof-postip" style="color:#666;cursor:help;font-weight:bold;">[?]</span></td>
				</tr></table>
			</div>
			<!-- RETIRED NUMBERS -->
			<div id="f-retnum" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("team", "en")%>:<br/>
				<div id="sm-pl-retnum-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
				<br/>&nbsp;<%=ResourceUtils.getText("number", "en")%>:<br/>
				<input type="text" name="retnum-number" id="retnum-number" onfocus="$(this).addClassName('selected');" onblur="$(this).removeClassName('selected');"></input>
			</div>
			<!-- TEAM STADIUMS -->
			<div id="f-teamstadiums" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("team", "en")%>:<br/>
				<div id="sm-pl-teamstadiums-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
			</div>
			<!-- YEARLY STATS -->
			<div id="f-stats" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("year", "en")%>:<br/>
				<div id="sm-pl-stats-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
				<br/>&nbsp;<%=ResourceUtils.getText("category", "en")%>:<br/>
				<table style="margin-bottom:5px;">
					<tr><td colspan="2"><select id="pl-stats-type" name="pl-stats-type" style="width:130px;" onchange="changeStType();">
						<option value="i"><%=ResourceUtils.getText("individual", "en")%></option>
						<option value="t"><%=ResourceUtils.getText("team", "en")%></option>
					</select></td></tr>
					<tr><td><img src="/img/component/treeview/join.gif" alt="L"/></td>
					<td><div id="sm-pl-stats-ct" class="selmultiple" style="margin-bottom:8px;"><%@include file="../../html/selectmult.html"%></div></td></tr>
				</table>
			</div>
		</div>
	</li>
	</ul>
	</form>
</div>
<%@include file="../../html/buttons.html"%>
<%@include file="../../html/tabcontrol.html"%>
<script type="text/javascript"><!--
var tStatsYr = [];
var tStatsCtI = [];
var tStatsCtT = [];
var tHofYr = [];
var tChampYr = [];
var tTm = [];
var tRcCtI = [];
var tRcCtT = [];
var tRcCtA = [];
<%
	String uslStatEvLabel = ConfigUtils.getValue("USL_STATS_EVENT_LABEL");
	for (short i : new short[]{1, 2, 3, 4}) {
		// Hof (year)
		String sql = "SELECT YR.id, YR.label"
				+ " FROM year YR "
				+ " WHERE YR.id IN (SELECT id_year FROM hall_of_fame WHERE id_league = ?) "
				+ " ORDER by YR.id DESC";
		Collection<PicklistItem> c = DatabaseManager.getPicklist(sql, Arrays.asList(i));
		StringBuffer sb = new StringBuffer();
		for (PicklistItem plb : c) {
			sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
		}
		out.print("tHofYr[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.years", "en") + " ––</option>" + sb.toString() + "';\r\n");
		// Championships (year)
		sql = "SELECT YR.id, YR.label"
				+ " FROM year YR "
				+ " WHERE YR.id IN (SELECT id_year FROM result WHERE id_championship = ?) "
				+ " ORDER by YR.id DESC";
		c = DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(i)));
		sb = new StringBuffer();
		for (PicklistItem plb : c) {
			sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
		}
		out.print("tChampYr[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.years", "en") + " ––</option>" + sb.toString() + "';\r\n");
		// Retnum + Stadiums
		sql = "SELECT TM.id, TM.label, TM.inactive"
				+ " FROM team TM "
				+ " WHERE TM.id IN (SELECT id_team FROM retired_number WHERE id_league = ?) "
				+ " ORDER by TM.inactive, TM.label";
		c = DatabaseManager.getPicklist(sql, Arrays.asList(i));
		sb = new StringBuffer();
		for (PicklistItem plb : c) {
			sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText().replaceAll("^true\\-", "&dagger;").replaceAll("^false\\-", "") + "</option>");
		}
		out.print("tTm[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.teams", "en") + " ––</option>" + sb.toString() + "';\r\n");
		// Record (Subevent)
		c = DatabaseManager.getPicklist("SELECT DISTINCT SE.id, SE.label, RC.type1 FROM record RC JOIN event SE ON SE.id = RC.id_subevent WHERE id_championship = ? ORDER BY SE.label", Arrays.asList(USLeaguesServlet.HLEAGUES.get(i)));
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		StringBuffer sb3 = new StringBuffer();
		for (PicklistItem plb : c) {
			if (!sb1.toString().contains(">" + plb.getText() + "<"))
				sb1.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
			if (plb.getParam() != null && String.valueOf(plb.getParam()).equalsIgnoreCase("individual"))
				sb2.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
			else if (plb.getParam() != null && String.valueOf(plb.getParam()).equalsIgnoreCase("team"))
				sb3.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
		}
		out.print("tRcCtA[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.categories", "en") + " ––</option>" + sb1.toString() + "';\r\n");
		out.print("tRcCtI[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.categories", "en") + " ––</option>" + sb2.toString() + "';\r\n");
		out.print("tRcCtT[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.categories", "en") + " ––</option>" + sb3.toString() + "';\r\n");
		// Yearly stats (year)
		sql = "SELECT YR.id, YR.label"
				+ " FROM year YR "
				+ " WHERE YR.id IN (SELECT id_year FROM result RS JOIN event EV ON EV.id = RS.id_event WHERE id_championship = ? AND EV.label LIKE ?) "
				+ " ORDER by YR.id DESC";
		c = DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(i), "%" + uslStatEvLabel + "%"));
		sb = new StringBuffer();
		for (PicklistItem plb : c)
			sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
		out.print("tStatsYr[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.years", "en") + " ––</option>" + sb.toString() + "';\r\n");
		// Yearly stats (category)
		sql = "SELECT SE2.id, SE2.label, TP.number"
				+ " FROM event SE2 "
				+ " JOIN type TP ON TP.id = SE2.id_type "
				+ " WHERE SE2.id IN (SELECT id_subevent2 FROM result RS JOIN event EV ON EV.id = RS.id_event WHERE id_championship = ? AND EV.label LIKE ? ORDER BY SE2.label)"
				+ " ORDER by SE2.label DESC";
		c = DatabaseManager.getPicklist(sql, Arrays.asList(USLeaguesServlet.HLEAGUES.get(i), "%" + uslStatEvLabel + "%"));
		sb1 = new StringBuffer();
		sb2 = new StringBuffer();
		for (PicklistItem plb : c) {
			if (plb.getParam() != null && String.valueOf(plb.getParam()).equalsIgnoreCase("50"))
				sb1.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
			else
				sb2.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
		}
		out.print("tStatsCtT[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.categories", "en") + " ––</option>" + sb1.toString() + "';\r\n");
		out.print("tStatsCtI[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.categories", "en") + " ––</option>" + sb2.toString() + "';\r\n");
	}
%>
var tPos = new Array();
window.onload = function() {
	initSelectMult('sm-pl-championships-yr', TX_YEARS, 200);
	initSelectMult('sm-pl-hof-yr', TX_YEARS, 200);
	initSelectMult('sm-pl-retnum-tm', TX_TEAMS, 200, 60);
	initSelectMult('sm-pl-teamstadiums-tm', TX_TEAMS, 200, 60);
	initSelectMult('sm-pl-records-ct', TX_CATEGORIES, 200, 45);
	initSelectMult('sm-pl-stats-yr', TX_YEARS, 200);
	initSelectMult('sm-pl-stats-ct', TX_CATEGORIES, 175);
	initSliderUS();
	changeModeUS();
	changeLeague('nfl');
	initTabControl();
<%
	List<Object[]> l = (List<Object[]>) DatabaseManager.executeSelect("SELECT DISTINCT id_league, position FROM hall_of_fame WHERE position IS NOT NULL AND position <> '' ORDER BY id_league, position");
	for (Object[] tObj : l) {
		String league = String.valueOf(tObj[0]);
		String position = String.valueOf(tObj[1]);
		String labelpos = StringUtils.getUSPosition(StringUtils.toInt(league), position);
		if (!position.matches(".*\\-.*")) {
			//out.print("tPos[" + league + "] = (tPos[" + league + "] ? tPos[" + league + "] + '\\r\\n' : '') + '" + position + "' - '" + labelpos + "'");
		}
	}
%>
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>