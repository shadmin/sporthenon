<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.entity.meta.Config"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="update-pictures" class="update">
	<script type="text/javascript" src="/js/dropzone.js"></script>
	<jsp:include page="/jsp/update/toolbar.jsp"/>
	<div class="fieldset">
		<div class="fstitle users"><%=StringUtils.text("update.pictures", session).toUpperCase()%></div>
		<div class="fscontent">
			<div style="width:100%;margin-bottom:10px;">
			<table class="toolbar" style="margin-top:0px;position:relative;float:left;top:0;">
				<tr>
					<td><%=StringUtils.text("type", session)%> :</td>
					<td><select id="type" onchange="changePictureType();"><option value="CP"><%=StringUtils.text("entity.CP", session)%></option>
					<option value="CN"><%=StringUtils.text("entity.CN", session)%></option>
					<option value="EV"><%=StringUtils.text("entity.EV", session)%></option>
					<option value="OL"><%=StringUtils.text("entity.OL", session)%></option>
					<option value="SP"><%=StringUtils.text("entity.SP", session)%></option>
					<option value="ST"><%=StringUtils.text("entity.ST", session)%></option>
					<option value="TM"><%=StringUtils.text("entity.TM", session)%></option></select></td>
					<td id="spcell1"><%=StringUtils.text("entity.SP.1", session)%> :</td>
					<td id="spcell2"><select id="sport" onchange="loadPictures('direct');"><option value=""></option>
					<%
						Contributor cb = (Contributor) session.getAttribute("user");
						String lang = String.valueOf(session.getAttribute("locale"));
						for (Sport sp : (List<Sport>) DatabaseHelper.execute("from Sport" + (cb != null && !cb.isAdmin() ? " where id in (" + cb.getSports() + ")" : "") + " order by label" + (lang != null && !lang.equalsIgnoreCase(ResourceUtils.LGDEFAULT) ? lang.toUpperCase() : "")))
							out.print("<option value=\"" + sp.getId() + "\">" + sp.getLabel(lang) + "</option>");
					%>
					</select></td>
					<td><%=StringUtils.text("entity.YR", session)%> :</td>
					<td><input id="year1" type="text" style="width:35px;"/></td>
					<td>-</td>
					<td><input id="year2" type="text" style="width:35px;"/></td>
					<td><input id="upd-first" type="button" class="button upd-first" onclick="loadPictures('first');" value="<%=StringUtils.text("first", session)%>"/></td>
					<td><input id="upd-previous" type="button" class="button upd-previous" onclick="loadPictures('previous');" value="<%=StringUtils.text("previous", session)%>"/></td>
					<td><input id="upd-find" type="button" class="button upd-find" onclick="findEntity(0);" value="<%=StringUtils.text("find", session)%>"/></td>
					<td><input id="upd-copy" type="button" class="button upd-copy" onclick="findEntity(2);" value="<%=StringUtils.text("copy.to", session)%>"/></td>
					<td><input id="upd-next" type="button" class="button upd-next" onclick="loadPictures('next');" value="<%=StringUtils.text("next", session)%>"/></td>
					<td><input id="upd-last" type="button" class="button upd-last" onclick="loadPictures('last');" value="<%=StringUtils.text("last", session)%>"/></td>
				</tr>
			</table>
			</div>
			<div>
			<fieldset><legend><%=StringUtils.text("local", session)%></legend>
			<table><tr><th style="width:80px;"><%=StringUtils.text("picture", session)%></th><td><div id="dz-file"></div><div id="remove-local" style="display:none;"><a href="javascript:removeLocalPicture();"><%=StringUtils.text("button.delete", session)%></a></div></td></tr>
			<tr><th><%=StringUtils.text("name", session)%></th><td id="name-local"></td></tr>
			</table>
			</fieldset><br/>
			<a href="/ImageServlet?missing"><%=StringUtils.text("missing.pictures", session)%></a>
			</div>
			<div style="width:110px;">
			<table class="toolbar" style="float:none;margin-top:5px;">
				<tr><td><input id="upd-upload" type="button" class="button upd-upload" onclick="uploadPicture();" value="<%=StringUtils.text("upload", session)%>" style="width:100px;"/></td></tr>
				<tr><td><input id="upd-download" type="button" class="button upd-download" onclick="downloadPicture();" value="<%=StringUtils.text("download", session)%>" style="width:100px;"/></td></tr>
				<tr><td><input id="upd-delete" type="button" class="button upd-delete" onclick="deletePicture();" value="<%=StringUtils.text("button.delete", session)%>" style="width:100px;"/></td></tr>
			</table>
			</div>
			<div>
			<fieldset><legend><%=StringUtils.text("remote", session)%></legend>
			<table><tr><th style="width:80px;"><%=StringUtils.text("picture", session)%></th><td id='imgremote-cell'><div id="img-remote"><img src=""/></div></td></tr>
			<tr><th><%=StringUtils.text("label", session)%></th><td id="label-remote" style="font-weight:bold;"></td></tr>
			<tr><th><%=StringUtils.text("size", session)%></th><td><table id="tsize"><tr><td style="border:none;"><input id="size1" type="radio" name="size" checked="checked" onclick="loadPictures('direct');"/></td><td><label for="size1"><%=StringUtils.text("img.large", session)%></label></td><td><input id="size2" type="radio" name="size" onclick="loadPictures('direct');"/></td><td><label for="size2"><%=StringUtils.text("img.small", session)%></label></td></tr></table></td></tr>
			<tr><th><%=StringUtils.text("name", session)%></th><td><select id="list-remote" size="5" style="width:250px;height:70px;" onchange="loadPicture();"></select></td></tr>
			<tr><td colspan="2"><table id="ttrtest"><tr><td style="border:none;"><input id="nopic" type="checkbox" name="nopic" onclick="setNopic();"/></td><td style="padding-right:10px;"><label for="nopic"><%=StringUtils.text("no.picture", session)%></label></td><td style="border:none;"><input id="trtest" type="checkbox" name="trtest" onclick="$('imgremote-cell').style.backgroundColor=(this.checked ? 'gray' : '#FFF');"/></td><td><label for="trtest"><%=StringUtils.text("test.transparency", session)%></label></td></tr></table></td></tr>
			</table>
			</fieldset>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	initPictures();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>