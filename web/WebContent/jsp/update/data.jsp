<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-data" class="update">
	<script type="text/javascript" src="/js/dropzone.js"></script>
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.data", session).toUpperCase()%></div>
		<div class="fscontent" style="min-height:150px;height:auto;">
			<!-- Global data -->
			<a id="link-PR" href="javascript:showPanel('PR');"><%=StringUtils.text("entity.PR", session)%></a>|<a id="link-CL" href="javascript:showPanel('CL');"><%=StringUtils.text("entity.CL", session)%></a>|<a id="link-CP" href="javascript:showPanel('CP');"><%=StringUtils.text("entity.CP", session)%></a>|<a id="link-CT" href="javascript:showPanel('CT');"><%=StringUtils.text("entity.CT", session)%></a>|<a id="link-CX" href="javascript:showPanel('CX');"><%=StringUtils.text("entity.CX", session)%></a>|<a id="link-CN" href="javascript:showPanel('CN');"><%=StringUtils.text("entity.CN", session)%></a>|<a id="link-EV" href="javascript:showPanel('EV');"><%=StringUtils.text("entity.EV", session)%></a>|<a id="link-OL" href="javascript:showPanel('OL');"><%=StringUtils.text("entity.OL", session)%></a>|<a id="link-OR" href="javascript:showPanel('OR');"><%=StringUtils.text("entity.OR", session)%></a>|<a id="link-RT" href="javascript:showPanel('RT');"><%=StringUtils.text("entity.RT", session)%></a>|<a id="link-SP" href="javascript:showPanel('SP');"><%=StringUtils.text("entity.SP", session)%></a>|<a id="link-ST" href="javascript:showPanel('ST');"><%=StringUtils.text("entity.ST", session)%></a>|<a id="link-TM" href="javascript:showPanel('TM');"><%=StringUtils.text("entity.TM", session)%></a>|<a id="link-YR" href="javascript:showPanel('YR');"><%=StringUtils.text("entity.YR", session)%></a><br/>
			<!-- US leagues data -->
			<a id="link-HF" href="javascript:showPanel('HF');"><%=StringUtils.text("entity.HF", session)%></a>|<a id="link-RC" href="javascript:showPanel('RC');"><%=StringUtils.text("entity.RC", session)%></a>|<a id="link-RN" href="javascript:showPanel('RN');"><%=StringUtils.text("entity.RN", session)%></a>|<a id="link-TS" href="javascript:showPanel('TS');"><%=StringUtils.text("entity.TS", session)%></a>|<a id="link-WL" href="javascript:showPanel('WL');"><%=StringUtils.text("entity.WL", session)%></a>
			<br/><br/>
			<!-- ATHLETE -->
			<table id="table-PR" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="pr-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("last.name", session)%></th><td><input type="text" id="pr-lastname"/></td></tr>
				<tr><th><%=StringUtils.text("first.name", session)%></th><td><input type="text" id="pr-firstname"/></td></tr>
				<tr><th><%=StringUtils.text("sport", session)%></th><td><input type="hidden" id="pr-sport"/><input type="text" id="pr-sport-l"/><a href="javascript:clearValue('pr-sport');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.TM.1", session)%></th><td><input type="hidden" id="pr-team"/><input type="text" id="pr-team-l"/><a href="javascript:clearValue('pr-team');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="pr-country"/><input type="text" id="pr-country-l"/><a href="javascript:clearValue('pr-country');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="pr-link"/><input type="text" id="pr-link-l"/><a href="javascript:clearValue('pr-link');">[X]</a></td></tr>
			</table>
			<!-- CALENDAR -->
			<table id="table-CL" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="cl-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("entity.SP.1", session)%></th><td><input type="hidden" id="cl-sport"/><input type="text" id="cl-sport-l"/><a href="javascript:clearValue('cl-sport');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CP.1", session)%></th><td><input type="hidden" id="cl-championship"/><input type="text" id="cl-championship-l"/><a href="javascript:clearValue('cl-championship');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.EV.1", session)%> 1</th><td><input type="hidden" id="cl-event"/><input type="text" id="cl-event-l"/><a href="javascript:clearValue('cl-event');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.EV.1", session)%> 2</th><td><input type="hidden" id="cl-subevent"/><input type="text" id="cl-subevent-l"/><a href="javascript:clearValue('cl-subevent');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.EV.1", session)%> 3</th><td><input type="hidden" id="cl-subevent2"/><input type="text" id="cl-subevent2-l"/><a href="javascript:clearValue('cl-subevent2');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CX.1", session)%></th><td><input type="hidden" id="cl-complex"/><input type="text" id="cl-complex-l"/><a href="javascript:clearValue('cl-complex');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CT.1", session)%></th><td><input type="hidden" id="cl-city"/><input type="text" id="cl-city-l"/><a href="javascript:clearValue('cl-city');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="cl-country"/><input type="text" id="cl-country-l"/><a href="javascript:clearValue('cl-country');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 1</th><td><input type="text" id="cl-date1"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 2</th><td><input type="text" id="cl-date2"/></td></tr>
			</table>
			<!-- CHAMPIONSHIP -->
			<table id="table-CP" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="cp-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="cp-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="cp-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("index", session)%></th><td><input type="text" id="cp-index"/></td></tr>
			</table>
			<!-- CITY -->
			<table id="table-CT" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="ct-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="ct-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="ct-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("entity.ST.1", session)%></th><td><input type="hidden" id="ct-state"/><input type="text" id="ct-state-l"/><a href="javascript:clearValue('ct-state');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="ct-country"/><input type="text" id="ct-country-l"/><a href="javascript:clearValue('ct-country');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="ct-link"/><input type="text" id="ct-link-l"/><a href="javascript:clearValue('ct-link');">[X]</a></td></tr>
			</table>
			<!-- COMPLEX -->
			<table id="table-CX" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="cx-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%></th><td><input type="text" id="cx-label"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CT.1", session)%></th><td><input type="hidden" id="cx-city"/><input type="text" id="cx-city-l"/><a href="javascript:clearValue('cx-city');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="cx-link"/><input type="text" id="cx-link-l"/><a href="javascript:clearValue('cx-link');">[X]</a></td></tr>
			</table>
			<!-- COUNTRY -->
			<table id="table-CN" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="cn-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="cn-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="cn-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("code", session)%></th><td><input type="text" id="cn-code"/></td></tr>
			</table>
			<!-- EVENT -->
			<table id="table-EV" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="ev-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="ev-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="ev-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("type", session)%></th><td><input type="hidden" id="ev-type"/><input type="text" id="ev-type-l"/><a href="javascript:clearValue('ev-type');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("index", session)%></th><td><input type="text" id="ev-index"/></td></tr>
			</table>
			<!-- OLYMPICS -->
			<table id="table-OL" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="ol-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("entity.YR.1", session)%></th><td><input type="hidden" id="ol-year"/><input type="text" id="ol-year-l"/><a href="javascript:clearValue('ol-year');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CT.1", session)%></th><td><input type="hidden" id="ol-city"/><input type="text" id="ol-city-l"/><a href="javascript:clearValue('ol-city');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("type", session)%></th><td><input type="text" id="ol-type"/></td></tr>
				<tr><th><%=StringUtils.text("start.date", session)%></th><td><input type="text" id="ol-start"/></td></tr>
				<tr><th><%=StringUtils.text("end.date", session)%></th><td><input type="text" id="ol-end"/></td></tr>
				<tr><th><%=StringUtils.text("entity.SP", session)%></th><td><input type="text" id="ol-sports"/></td></tr>
				<tr><th><%=StringUtils.text("entity.EV", session)%></th><td><input type="text" id="ol-events"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CN", session)%></th><td><input type="text" id="ol-countries"/></td></tr>
				<tr><th><%=StringUtils.text("entity.PR", session)%></th><td><input type="text" id="ol-persons"/></td></tr>
			</table>
			<!-- OLYMPIC MEDALS -->
			<table id="table-OR" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="or-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("entity.OL.1", session)%></th><td><input type="hidden" id="or-olympics"/><input type="text" id="or-olympics-l"/><a href="javascript:clearValue('or-olympics');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="or-country"/><input type="text" id="or-country-l"/><a href="javascript:clearValue('or-country');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("gold", session)%></th><td><input type="text" id="or-gold"/></td></tr>
				<tr><th><%=StringUtils.text("silver", session)%></th><td><input type="text" id="or-silver"/></td></tr>
				<tr><th><%=StringUtils.text("bronze", session)%></th><td><input type="text" id="or-bronze"/></td></tr>
			</table>
			<!-- ROUND TYPES -->
			<table id="table-RT" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="rt-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="rt-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="rt-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("index", session)%></th><td><input type="text" id="rt-index"/></td></tr>
			</table>
			<!-- SPORT -->
			<table id="table-SP" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="sp-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="sp-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="sp-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("type", session)%></th><td><input type="text" id="sp-type"/></td></tr>
				<tr><th><%=StringUtils.text("index", session)%></th><td><input type="text" id="sp-index"/></td></tr>
			</table>
			<!-- STATE -->
			<table id="table-ST" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="st-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="st-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="st-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("code", session)%></th><td><input type="text" id="st-code"/></td></tr>
				<tr><th><%=StringUtils.text("capital", session)%></th><td><input type="text" id="st-capital"/></td></tr>
			</table>
			<!-- TEAM -->
			<div id="table-TM" style="display:none;">
			<table>
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="tm-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%></th><td><input type="text" id="tm-label"/></td></tr>
				<tr><th><%=StringUtils.text("entity.SP.1", session)%></th><td><input type="hidden" id="tm-sport"/><input type="text" id="tm-sport-l"/><a href="javascript:clearValue('tm-sport');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="tm-country"/><input type="text" id="tm-country-l"/><a href="javascript:clearValue('tm-country');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="tm-link"/><input type="text" id="tm-link-l"/><a href="javascript:clearValue('tm-link');">[X]</a></td></tr>
			</table>
			<fieldset style="width:600px;">
				<legend><a href="javascript:toggle('tmusleagues');"><img id="img-tmusleagues" alt="" src="/img/render/expand.gif?v=2" class="toggleimg" onclick="toggleContent(this);"/><%=StringUtils.text("menu.usleagues.2", session)%></a></legend>
				<table id="tmusleagues" style="margin-top:0px;padding:5px;width:auto;display:none;">
				<tr><th><%=StringUtils.text("league", session)%></th><td><input type="hidden" id="tm-league"/><input type="text" id="tm-league-l"/><a href="javascript:clearValue('tm-league');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("conference", session)%></th><td><input type="text" id="tm-conference"/></td></tr>
				<tr><th><%=StringUtils.text("division", session)%></th><td><input type="text" id="tm-division"/></td></tr>
				<tr><th><%=StringUtils.text("comment", session)%></th><td><input type="text" id="tm-comment"/></td></tr>
				<tr><th><%=StringUtils.text("start.date", session)%></th><td><input type="text" id="tm-year1"/></td></tr>
				<tr><th><%=StringUtils.text("end.date", session)%></th><td><input type="text" id="tm-year2"/></td></tr>
				</table>	
			</fieldset>
			</div>
			<!-- YEAR -->
			<table id="table-YR" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="yr-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%></th><td><input type="text" id="yr-label"/></td></tr>
			</table>
			<!-- HALL OF FAME -->
			<table id="table-HF" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="hf-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("league", session)%></th><td><input type="hidden" id="hf-league"/><input type="text" id="hf-league-l"/><a href="javascript:clearValue('hf-league');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.YR.1", session)%></th><td><input type="hidden" id="hf-year"/><input type="text" id="hf-year-l"/><a href="javascript:clearValue('hf-year');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.PR.1", session)%></th><td><input type="hidden" id="hf-person"/><input type="text" id="hf-person-l"/><a href="javascript:clearValue('hf-person');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("position", session)%></th><td><input type="text" id="hf-position"/></td></tr>
			</table>
			<!-- RECORD -->
			<table id="table-RC" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="rc-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("entity.SP.1", session)%></th><td><input type="hidden" id="rc-sport"/><input type="text" id="rc-sport-l"/><a href="javascript:clearValue('rc-sport');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CP.1", session)%></th><td><input type="hidden" id="rc-championship"/><input type="text" id="rc-championship-l"/><a href="javascript:clearValue('rc-championship');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.EV.1", session)%> 1</th><td><input type="hidden" id="rc-event"/><input type="text" id="rc-event-l"/><a href="javascript:clearValue('rc-event');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.EV.1", session)%> 2</th><td><input type="hidden" id="rc-subevent"/><input type="text" id="rc-subevent-l"/><a href="javascript:clearValue('rc-subevent');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("type", session)%> 1</th><td><input type="text" id="rc-type1"/></td></tr>
				<tr><th><%=StringUtils.text("type", session)%> 2</th><td><input type="text" id="rc-type2"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CT.1", session)%></th><td><input type="hidden" id="rc-city"/><input type="text" id="rc-city-l"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%></th><td><input type="text" id="rc-label"/></td></tr>
				<tr><th><%=StringUtils.text("rank.1", session)%></th><td><input type="hidden" id="rc-rank1"/><input type="text" id="rc-rank1-l"/><a href="javascript:clearValue('rc-rank1');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("record2", session)%> 1</th><td><input type="text" id="rc-record1"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 1</th><td><input type="text" id="rc-date1"/></td></tr>
				<tr><th><%=StringUtils.text("rank.2", session)%></th><td><input type="hidden" id="rc-rank2"/><input type="text" id="rc-rank2-l"/><a href="javascript:clearValue('rc-rank2');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("record2", session)%> 2</th><td><input type="text" id="rc-record2"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 2</th><td><input type="text" id="rc-date2"/></td></tr>
				<tr><th><%=StringUtils.text("rank.3", session)%></th><td><input type="hidden" id="rc-rank3"/><input type="text" id="rc-rank3-l"/><a href="javascript:clearValue('rc-rank3');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("record2", session)%> 3</th><td><input type="text" id="rc-record3"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 3</th><td><input type="text" id="rc-date3"/></td></tr>
				<tr><th><%=StringUtils.text("rank.4", session)%></th><td><input type="hidden" id="rc-rank4"/><input type="text" id="rc-rank4-l"/><a href="javascript:clearValue('rc-rank4');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("record2", session)%> 4</th><td><input type="text" id="rc-record4"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 4</th><td><input type="text" id="rc-date4"/></td></tr>
				<tr><th><%=StringUtils.text("rank.5", session)%></th><td><input type="hidden" id="rc-rank5"/><input type="text" id="rc-rank5-l"/><a href="javascript:clearValue('rc-rank5');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("record2", session)%> 5</th><td><input type="text" id="rc-record5"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 5</th><td><input type="text" id="rc-date5"/></td></tr>
				<tr><th><%=StringUtils.text("counting", session)%></th><td><input type="text" id="rc-counting"/></td></tr>
				<tr><th><%=StringUtils.text("index", session)%></th><td><input type="text" id="rc-index"/></td></tr>
				<tr><th><%=StringUtils.text("tie", session)%></th><td><input type="text" id="rc-tie"/></td></tr>
				<tr><th><%=StringUtils.text("comment", session)%></th><td><input type="text" id="rc-comment"/></td></tr>
			</table>
			<!-- RETIRED NUMBER -->
			<table id="table-RN" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="rn-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("league", session)%></th><td><input type="hidden" id="rn-league"/><input type="text" id="rn-league-l"/><a href="javascript:clearValue('rn-league');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.TM.1", session)%></th><td><input type="hidden" id="rn-team"/><input type="text" id="rn-team-l"/><a href="javascript:clearValue('rn-team');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.PR.1", session)%></th><td><input type="hidden" id="rn-person"/><input type="text" id="rn-person-l"/><a href="javascript:clearValue('rn-person');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.YR.1", session)%></th><td><input type="hidden" id="rn-year"/><input type="text" id="rn-year-l"/><a href="javascript:clearValue('rn-year');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("number", session)%></th><td><input type="text" id="rn-number"/></td></tr>
			</table>
			<!-- TEAM STADIUMS -->
			<table id="table-TS" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="ts-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("league", session)%></th><td><input type="hidden" id="ts-league"/><input type="text" id="ts-league-l"/><a href="javascript:clearValue('ts-league');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.TM.1", session)%></th><td><input type="hidden" id="ts-team"/><input type="text" id="ts-team-l"/><a href="javascript:clearValue('ts-team');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.CX.1", session)%></th><td><input type="hidden" id="ts-complex"/><input type="text" id="ts-complex-l"/><a href="javascript:clearValue('ts-complex');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 1</th><td><input type="text" id="ts-date1"/></td></tr>
				<tr><th><%=StringUtils.text("date", session)%> 2</th><td><input type="text" id="ts-date2"/></td></tr>
				<tr><th><%=StringUtils.text("renamed", session)%></th><td><input type="text" id="ts-renamed"/></td></tr>
			</table>
			<!-- WIN/LOSS -->
			<table id="table-WL" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="wl-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("league", session)%></th><td><input type="hidden" id="wl-league"/><input type="text" id="wl-league-l"/><a href="javascript:clearValue('wl-league');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("entity.TM.1", session)%></th><td><input type="hidden" id="wl-team"/><input type="text" id="wl-team-l"/><a href="javascript:clearValue('wl-team');">[X]</a></td></tr>
				<tr><th><%=StringUtils.text("type", session)%></th><td><input type="text" id="wl-type"/></td></tr>
				<tr><th><%=StringUtils.text("win", session)%></th><td><input type="text" id="wl-win"/></td></tr>
				<tr><th><%=StringUtils.text("loss", session)%></th><td><input type="text" id="wl-loss"/></td></tr>
				<tr><th><%=StringUtils.text("tie", session)%></th><td><input type="text" id="wl-tie"/></td></tr>
				<tr><th><%=StringUtils.text("ot.loss", session)%></th><td><input type="text" id="wl-otloss"/></td></tr>
			</table>
			<!-- Ext.links -->
			<fieldset id="table-exl" style="display:none;">
				<legend><%=StringUtils.text("ext.links", session)%></legend>
				<table><tr><td style="border:none;"><textarea id="exl" tabindex="14" cols="100" rows="5" style="width:780px;"></textarea></td></tr></table>
			</fieldset>
			<!-- Photos -->
			<fieldset id="imgzone">
				<legend><%=StringUtils.text("photos", session)%></legend>
				<div id="dz-file" style="float:left;"><p><%=StringUtils.text("click.drag.drop", session)%></p></div>
				<div id="embimg">
					Embedded:<br/><textarea id="emb" tabindex="15" cols="50" rows="7" style="width:660px;height:102px;white-space:normal;"></textarea>
				</div>
				<div style="clear:left;margin-bottom:5px;">
					<input type="text" id="src" title="<%=StringUtils.text("source", session)%>" style="width:117px;margin-right:3px;"/>
					<input type="button" class="button" onclick="addPhoto();" value="<%=StringUtils.text("button.add", session)%>" style="padding-left:5px;"/>
				</div>
				<ul id="currentphotos"><li/></ul>
			</fieldset>
			<!-- Button panel -->
			<table class="toolbar" style="position:relative;top:0;right:0;float:right;margin-top:15px;">
				<tr>
					<td><input id="upd-new" type="button" class="button upd-new" onclick="newEntity();" value="<%=StringUtils.text("new", session)%>"/></td>
					<td><input id="upd-save" type="button" class="button upd-save" onclick="saveEntity();" value="<%=StringUtils.text("save", session)%>"/></td>
					<td><input id="upd-delete" type="button" class="button upd-delete" onclick="deleteEntity();" value="<%=StringUtils.text("button.delete", session)%>"/></td>
					<td><input id="upd-copy" type="button" class="button upd-copy" onclick="copyEntity();" value="<%=StringUtils.text("copy", session)%>"/></td>
					<td><input id="upd-merge" type="button" class="button upd-merge" onclick="findEntity(1);" value="<%=StringUtils.text("merge", session)%>"/></td>
				</tr>
			</table><br/>
			<table class="toolbar" style="position:relative;top:0;right:0;clear:right;float:right;margin-top:5px;">
				<tr>
					<td><input id="upd-first" type="button" class="button upd-first" onclick="loadEntity('first');" value="<%=StringUtils.text("first", session)%>"/></td>
					<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadEntity('previous');" value="<%=StringUtils.text("previous", session)%>"/></td>
					<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity(0);" value="<%=StringUtils.text("find", session)%>"/></td>
					<td><input id="upd-next" type="button" class="button upd-next" onclick="loadEntity('next');" value="<%=StringUtils.text("next", session)%>"/></td>
					<td><input id="upd-last" type="button" class="button upd-last" onclick="loadEntity('last');" value="<%=StringUtils.text("last", session)%>"/></td>
				</tr>
			</table>
			<div id="msg" style="float:left;"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	initUpdateData();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>