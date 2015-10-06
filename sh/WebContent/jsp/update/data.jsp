<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-data" class="update">
	<script type="text/javascript" src="/js/dropzone.js"></script>
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle"><%=StringUtils.text("update.data", session).toUpperCase()%></div>
		<div class="fscontent" style="height:auto;">
			<a id="link-PR" href="javascript:showPanel('PR');"><%=StringUtils.text("entity.PR", session)%></a>&nbsp;|&nbsp;
			<a id="link-CP" href="javascript:showPanel('CP');"><%=StringUtils.text("entity.CP", session)%></a>&nbsp;|&nbsp;
			<a id="link-CT" href="javascript:showPanel('CT');"><%=StringUtils.text("entity.CT", session)%></a>&nbsp;|&nbsp;
			<a id="link-CX" href="javascript:showPanel('CX');"><%=StringUtils.text("entity.CX", session)%></a>&nbsp;|&nbsp;
			<a id="link-CN" href="javascript:showPanel('CN');"><%=StringUtils.text("entity.CN", session)%></a>&nbsp;|&nbsp;
			<a id="link-EV" href="javascript:showPanel('EV');"><%=StringUtils.text("entity.EV", session)%></a>&nbsp;|&nbsp;
			<a id="link-OL" href="javascript:showPanel('OL');"><%=StringUtils.text("entity.OL", session)%></a>&nbsp;|&nbsp;
			<a id="link-SP" href="javascript:showPanel('SP');"><%=StringUtils.text("entity.SP", session)%></a>&nbsp;|&nbsp;
			<a id="link-ST" href="javascript:showPanel('ST');"><%=StringUtils.text("entity.ST", session)%></a>&nbsp;|&nbsp;
			<a id="link-TM" href="javascript:showPanel('TM');"><%=StringUtils.text("entity.TM", session)%></a>&nbsp;|&nbsp;
			<a id="link-YR" href="javascript:showPanel('YR');"><%=StringUtils.text("entity.YR", session)%></a><br/><br/>
			<!-- PHOTO -->
			<div id="imgzone" style="position:absolute;width:130px;">
				<fieldset><legend><%=StringUtils.text("photo", session)%></legend>
					<div id="dz-file"><p><%=StringUtils.text("click.drag.drop", session)%></p></div>	
				</fieldset>
			</div>
			<div id="currentimg"></div>
			<!-- ATHLETE -->
			<table id="table-PR" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="pr-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("last.name", session)%></th><td><input type="text" id="pr-lastname"/></td></tr>
				<tr><th><%=StringUtils.text("first.name", session)%></th><td><input type="text" id="pr-firstname"/></td></tr>
				<tr><th><%=StringUtils.text("sport", session)%></th><td><input type="hidden" id="pr-sport"/><input type="text" id="pr-sport-l"/></td></tr>
				<tr><th><%=StringUtils.text("entity.TM.1", session)%></th><td><input type="hidden" id="pr-team"/><input type="text" id="pr-team-l"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="pr-country"/><input type="text" id="pr-country-l"/></td></tr>
				<tr><th><%=StringUtils.text("photo.source", session)%></th><td><input type="text" id="pr-source"/></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="pr-link"/><input type="text" id="pr-link-l"/></td></tr>
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
				<tr><th><%=StringUtils.text("entity.ST.1", session)%></th><td><input type="hidden" id="ct-state"/><input type="text" id="ct-state-l"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="ct-country"/><input type="text" id="ct-country-l"/></td></tr>
				<tr><th><%=StringUtils.text("photo.source", session)%></th><td><input type="text" id="ct-source"/></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="ct-link"/><input type="text" id="ct-link-l"/></td></tr>
			</table>
			<!-- COMPLEX -->
			<table id="table-CX" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="cx-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (EN)</th><td><input type="text" id="cx-label"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%> (FR)</th><td><input type="text" id="cx-labelfr"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CT.1", session)%></th><td><input type="hidden" id="cx-city"/><input type="text" id="cx-city-l"/></td></tr>
				<tr><th><%=StringUtils.text("photo.source", session)%></th><td><input type="text" id="cx-source"/></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="cx-link"/><input type="text" id="cx-link-l"/></td></tr>
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
				<tr><th><%=StringUtils.text("type", session)%></th><td><input type="hidden" id="ev-type"/><input type="text" id="ev-type-l"/></td></tr>
				<tr><th><%=StringUtils.text("index", session)%></th><td><input type="text" id="ev-index"/></td></tr>
			</table>
			<!-- OLYMPICS -->
			<table id="table-OL" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="ol-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("entity.YR.1", session)%></th><td><input type="hidden" id="ol-year"/><input type="text" id="ol-year-l"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CT.1", session)%></th><td><input type="hidden" id="ol-city"/><input type="text" id="ol-city-l"/></td></tr>
				<tr><th><%=StringUtils.text("type", session)%></th><td><input type="text" id="ol-type"/></td></tr>
				<tr><th><%=StringUtils.text("start.date", session)%></th><td><input type="text" id="ol-start"/></td></tr>
				<tr><th><%=StringUtils.text("end.date", session)%></th><td><input type="text" id="ol-end"/></td></tr>
				<tr><th><%=StringUtils.text("entity.SP", session)%></th><td><input type="text" id="ol-sports"/></td></tr>
				<tr><th><%=StringUtils.text("entity.EV", session)%></th><td><input type="text" id="ol-events"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CN", session)%></th><td><input type="text" id="ol-countries"/></td></tr>
				<tr><th><%=StringUtils.text("entity.PR", session)%></th><td><input type="text" id="ol-persons"/></td></tr>
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
			<table id="table-TM" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="tm-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%></th><td><input type="text" id="tm-label"/></td></tr>
				<tr><th><%=StringUtils.text("entity.SP.1", session)%></th><td><input type="hidden" id="tm-sport"/><input type="text" id="tm-sport-l"/></td></tr>
				<tr><th><%=StringUtils.text("entity.CN.1", session)%></th><td><input type="hidden" id="tm-country"/><input type="text" id="tm-country-l"/></td></tr>
				<tr><th><%=StringUtils.text("league", session)%></th><td><input type="hidden" id="tm-league"/><input type="text" id="tm-league-l"/></td></tr>
				<tr><th><%=StringUtils.text("conference", session)%></th><td><input type="text" id="tm-conference"/></td></tr>
				<tr><th><%=StringUtils.text("division", session)%></th><td><input type="text" id="tm-division"/></td></tr>
				<tr><th><%=StringUtils.text("comment", session)%></th><td><input type="text" id="tm-comment"/></td></tr>
				<tr><th><%=StringUtils.text("start.date", session)%></th><td><input type="text" id="tm-year1"/></td></tr>
				<tr><th><%=StringUtils.text("end.date", session)%></th><td><input type="text" id="tm-year2"/></td></tr>
				<tr><th><%=StringUtils.text("linked.to", session)%></th><td><input type="hidden" id="tm-link"/><input type="text" id="tm-link-l"/></td></tr>
			</table>
			<!-- YEAR -->
			<table id="table-YR" style="display:none;">
				<tr><th>ID</th><td><input type="text" disabled="disabled" id="yr-id" class="id"/></td></tr>
				<tr><th><%=StringUtils.text("label", session)%></th><td><input type="text" id="yr-label"/></td></tr>
			</table>
			<!-- BUTTON PANEL -->
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
<jsp:include page="/jsp/common/footer.jsp" />