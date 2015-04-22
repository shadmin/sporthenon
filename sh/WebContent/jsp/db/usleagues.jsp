<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="usleagues" class="fieldset">
	<div class="fstitle criteria"><%=StringUtils.text("search.criteria", session)%></div>
	<form id="usleagues-form" action="/usleagues">
	<div class="league">
		<div id="slider-league-img" class="slider"><%@include file="../../html/slider.html"%></div>
	</div>
	<div id="usltype">
		<div id="nfl" class="selected" onclick="changeLeague(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;NFL &#150; National Football League</div>
		<div id="nba" onclick="changeLeague(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;NBA &#150; National Basketball Association</div>
		<div id="nhl" onclick="changeLeague(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;NHL &#150; National Hockey League</div>
		<div id="mlb" onclick="changeLeague(this.id);" style="margin-bottom:0px;"><img alt="-" src="/img/bullet.gif"/>&nbsp;MLB &#150; Major League of Baseball</div>
	</div>
	<div style="float:left;width:400px;height:170px;" class="fieldset">
		<div style="float:left;width:auto;">
			<table id="usstype">
				<tr><td id="championships" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;<%=ResourceUtils.getText("championships", "en")%></td></tr>
				<tr><td id="records" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;<%=ResourceUtils.getText("records", "en")%></td></tr>
				<tr><td id="hof" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;<%=ResourceUtils.getText("hall.fame", "en")%></td></tr>
				<tr><td id="retnum" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;<%=ResourceUtils.getText("retired.numbers", "en")%></td></tr>
				<tr><td id="teamstadiums" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;<%=ResourceUtils.getText("team.stadiums", "en")%></td></tr>
				<tr><td id="winloss" onclick="changeModeUS(this.id);"><img alt="-" src="/img/bullet.gif"/>&nbsp;<%=ResourceUtils.getText("wins.losses", "en")%></td></tr>
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
				&nbsp;<%=ResourceUtils.getText("category", "en")%>:<br/>
				<div id="sm-pl-records-se" class="selmultiple" style="margin-bottom:8px;"><%@include file="../../html/selectmult.html"%></div>
				&nbsp;<%=ResourceUtils.getText("type", "en")%>:<br/>
				<table><tr>
					<td colspan="2"><select id="pl-records-tp1" name="pl-records-tp1" style="width:130px;">
						<option value="i"><%=ResourceUtils.getText("individual", "en")%></option>
						<option value="t"><%=ResourceUtils.getText("team", "en")%></option>
						<option value="it">[<%=ResourceUtils.getText("all", "en")%>]</option>
					</select></td></tr>
					<tr><td><img src="/img/component/treeview/join.gif" alt="L"/></td>
					<td><select id="pl-records-tp2" name="pl-records-tp2" style="width:130px;"><option/></select></td>
				</tr></table>
				<table><tr><td><input type="checkbox" name="records-pf" id="records-pf"/></td><td><label for="records-pf">Include Postseason/Super Bowl</label></td></tr></table>
			</div>
			<!-- HALL OF FAME -->
			<div id="f-hof" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("year", "en")%>:<br/>
				<div id="sm-pl-hof-yr" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
				<br/><table><tr>
					<td><%=ResourceUtils.getText("position", "en")%>:</td>
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
			<!-- WINS/LOSSES -->
			<div id="f-winloss" style="display:none;">
				&nbsp;<%=ResourceUtils.getText("team", "en")%>:<br/>
				<div id="sm-pl-winloss-tm" class="selmultiple"><%@include file="../../html/selectmult.html"%></div>
			</div>
		</div>
	</div>
	</form>
</div>
<%@include file="../../html/buttons.html"%>
<%@include file="../../html/tabcontrol.html"%>
<script type="text/javascript">
var tPos = new Array();
window.onload = function() {
	initSelectMult('sm-pl-championships-yr', TX_YEARS, 200);
	initSelectMult('sm-pl-hof-yr', TX_YEARS, 200);
	initSelectMult('sm-pl-retnum-tm', TX_TEAMS, 200, 60);
	initSelectMult('sm-pl-teamstadiums-tm', TX_TEAMS, 200, 60);
	initSelectMult('sm-pl-records-se', TX_CATEGORIES, 200, 45);
	initSelectMult('sm-pl-winloss-tm', TX_TEAMS, 200, 60);
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