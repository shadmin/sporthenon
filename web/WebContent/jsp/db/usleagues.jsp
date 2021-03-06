<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Collection"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.entity.*"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.PicklistBean"%>
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
	Collection<PicklistBean> c = DatabaseHelper.getPicklist(HallOfFame.class, "year", "league.id=" + i, null, (short)1, "en");
	StringBuffer sb = new StringBuffer();
	for (PicklistBean plb : c)
		sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
	out.print("tHofYr[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.years", "en") + " ––</option>" + sb.toString() + "';\r\n");
	// Championships (year)
	c = DatabaseHelper.getPicklist(Result.class, "year", "championship.id=" + USLeaguesServlet.HLEAGUES.get(i), null, (short)1, "en");
	sb = new StringBuffer();
	for (PicklistBean plb : c)
		sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
	out.print("tChampYr[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.years", "en") + " ––</option>" + sb.toString() + "';\r\n");
	// Retnum + Stadiums
	c = DatabaseHelper.getPicklist(RetiredNumber.class, "team", "league.id=" + i, "x.team.inactive || '-'", "x.team.inactive, x.team.label", "en");
	sb = new StringBuffer();
	for (PicklistBean plb : c)
		sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText().replaceAll("^true\\-", "&dagger;").replaceAll("^false\\-", "") + "</option>");
	out.print("tTm[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.teams", "en") + " ––</option>" + sb.toString() + "';\r\n");
	// Record (Subevent)
	c = DatabaseHelper.getPicklistFromQuery("select distinct x.subevent.id, x.subevent.label, x.type1 from Record x where championship.id=" + USLeaguesServlet.HLEAGUES.get(i) + " order by x.subevent.label", false);
	StringBuffer sb1 = new StringBuffer();
	StringBuffer sb2 = new StringBuffer();
	StringBuffer sb3 = new StringBuffer();
	for (PicklistBean plb : c) {
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
	c = DatabaseHelper.getPicklist(Result.class, "year", "championship.id=" + USLeaguesServlet.HLEAGUES.get(i) + " and event.label like '%" + uslStatEvLabel + "%'", null, (short)1, "en");
	sb = new StringBuffer();
	for (PicklistBean plb : c)
		sb.append("<option value=\"" + plb.getValue() + "\">" + plb.getText() + "</option>");
	out.print("tStatsYr[" + i + "] = '<option value=\"0\">–– " + ResourceUtils.getText("all.years", "en") + " ––</option>" + sb.toString() + "';\r\n");
	// Yearly stats (category)
	c = DatabaseHelper.getPicklistFromQuery("select distinct x.subevent2.id, x.subevent2.label, x.subevent2.type.number from Result x where championship.id=" + USLeaguesServlet.HLEAGUES.get(i) + " and event.label like '%" + uslStatEvLabel + "%' order by x.subevent2.label", false);
	sb1 = new StringBuffer();
	sb2 = new StringBuffer();
	for (PicklistBean plb : c) {
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
	List<Object[]> l = (List<Object[]>) DatabaseHelper.executeNative("select distinct id_league, position from \"HallOfFame\" where position is not null and position<>'' order by id_league, position");
	for (Object[] tObj : l) {
		String league = String.valueOf(tObj[0]);
		String position = String.valueOf(tObj[1]);
		String labelpos = StringUtils.getUSPosition(new Integer(league), position);
		if (!position.matches(".*\\-.*")) {
%>
	tPos[<%=league%>] = (tPos[<%=league%>] ? tPos[<%=league%>] + '\r\n' : '') + <%="'" + position + "'"%> + ' - ' + <%="'" + labelpos + "'"%>;
<%}}%>
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>