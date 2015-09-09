<%@page import="java.util.List"%>
<%@page import="com.sporthenon.db.DatabaseHelper"%>
<%@page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update-pictures" class="update">
	<script type="text/javascript" src="/js/dropzone.js"></script>
	<jsp:include page="/jsp/update/toolbar.jsp" />
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.pictures", session).toUpperCase()%></div>
		<div class="fscontent">
			<div>
			<fieldset><legend>Local</legend>
			<table><tr><th>Picture</th><td><div id="dz-file"></div></td></tr>
			<tr><th>Name</th><td id="name-local"></td></tr>
			</table>
			</fieldset>
			</div>
			<div style="width:110px;">
			<table class="toolbar" style="position:relative;top:5px;margin-left:12px;">
				<tr><td><input id="upd-upload" type="button" class="button upd-upload" onclick="uploadPicture();" value="<%=StringUtils.text("upload", session)%>" style="width:100px;"/></td></tr>
				<tr><td><input id="upd-download" type="button" class="button upd-download" onclick="downloadPicture();" value="<%=StringUtils.text("download", session)%>" style="width:100px;"/></td></tr>
				<tr><td><input id="upd-delete" type="button" class="button upd-delete" onclick="deletePicture();" value="<%=StringUtils.text("button.delete", session)%>" style="width:100px;"/></td></tr>
			</table>
			</div>
			<div>
			<fieldset><legend>Remote</legend>
			<table><tr><th>Picture</th><td id="img-remote"><img src=""/></td></tr>
			<tr><th>Label</th><td id="label-remote" style="font-weight:bold;"></td></tr>
			<tr><th>Name</th><td><select id="list-remote" size="5" style="width:250px;height:70px;" onchange="loadPicture();"></select></td></tr>
			</table>
			</fieldset>
			</div>
			<table class="toolbar" style="position:relative;top:0;right:0;clear:right;float:right;margin-top:15px;">
				<tr>
					<td><%=StringUtils.text("type", session)%>&nbsp;:</td>
					<td><select id="type" onchange="currentAlias=this.value;loadPictures('last');"><option value="CP"><%=StringUtils.text("entity.CP", session)%></option>
					<option value="CN"><%=StringUtils.text("entity.CN", session)%></option>
					<option value="EV"><%=StringUtils.text("entity.EV", session)%></option>
					<option value="OL"><%=StringUtils.text("entity.OL", session)%></option>
					<option value="SP"><%=StringUtils.text("entity.SP", session)%></option>
					<option value="ST"><%=StringUtils.text("entity.ST", session)%></option>
					<option value="TM"><%=StringUtils.text("entity.TM", session)%></option></select></td>
					<td><%=StringUtils.text("entity.YR", session)%>&nbsp;:</td>
					<td><input id="year1" type="text" style="width:35px;"/></td>
					<td>-</td>
					<td><input id="year2" type="text" style="width:35px;"/></td>
					<td><input id="size1" type="radio" name="size" checked="checked" onclick="loadPictures('direct');"/></td><td><label for="size1">Large</label></td>
					<td><input id="size2" type="radio" name="size" onclick="loadPictures('direct');"/></td><td><label for="size2">Small</label></td>
					<td><input id="upd-first" type="button" class="button upd-first" onclick="loadPictures('first');" value="<%=StringUtils.text("first", session)%>"/></td>
					<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadPictures('previous');" value="<%=StringUtils.text("previous", session)%>"/></td>
					<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity(0);" value="<%=StringUtils.text("find", session)%>"/></td>
					<td><input id="upd-next" type="button" class="button upd-next" onclick="loadPictures('next');" value="<%=StringUtils.text("next", session)%>"/></td>
					<td><input id="upd-last" type="button" class="button upd-last" onclick="loadPictures('last');" value="<%=StringUtils.text("last", session)%>"/></td>
				</tr>
			</table>
			<div id="msg" style="float:left;"></div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	initPictures();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp" />