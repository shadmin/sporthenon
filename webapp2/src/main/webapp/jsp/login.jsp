<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.*"%>
<%@ page import="java.util.Collection"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.PicklistItem"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<form id="flogin" name="flogin" action="/LoginServlet?auth" method="post">
<div id="login">
<%
	String msg = (String) request.getAttribute("msg");
	if (msg != null) {
		out.println("<br/><span class='loginmsg'>" + msg + "</span>");
	}
%>
<div class="loginpanel" style="width:350px;">
	<div class="logintitle"><%=StringUtils.text("title.authentication", session)%></div>
	<table class="tlogin noborder">
		<tr>
			<th><%=StringUtils.text("loginid", session)%> :</th><td><input type="text" id="username" name="username" size="15" onkeydown="if(event.keyCode==13){$('flogin').submit();}"/></td>
		</tr>
		<tr>
			<th><%=StringUtils.text("password", session)%> :</th><td><input type="password" id="password" name="password" size="15" onkeydown="if(event.keyCode==13){$('flogin').submit();}"/></td>
		</tr>
		<tr>
			<td colspan="2" style="padding-top:10px;text-align:center;"><input type="button" class="button ok" onclick="auth();" value="OK" style="padding-right:5px;"/></td>
		</tr>
	</table>
</div>
<div class="loginpanel" style="width:550px;">
	<div class="logintitle"><%=StringUtils.text("title.createaccount", session)%></div>
	<div id="rmsg" style="display:none;"></div>
	<table class="noborder">
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("loginid", session)%> :</th><td><input type="text" id="rlogin"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("password", session)%> :</th><td><input type="password" id="rpassword"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("confirm.password", session)%> :</th><td><input type="password" id="rpassword2"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("email.addr", session)%> :</th><td><input type="text" id="remail" size="30"/></td>
		</tr>
		<tr>
			<th><%=StringUtils.text("public.name", session)%> :</th><td><input type="text" id="rpublicname"/></td>
		</tr>
	</table>
	<table class="noborder" style="margin-top:8px;">
		<tr><td colspan="2"><span class="mandatory">*</span><%=StringUtils.text("select.sports.contributor", session)%> :</td></tr>
		<tr>
			<td><ul id="sp1" class="loginsports">
			<%
				String lang = String.valueOf(session.getAttribute("locale"));
				Collection<PicklistItem> cPicklist = DatabaseManager.getPicklist("SELECT id, label" + ResourceUtils.getLocaleParam(lang) + " FROM sport ORDER BY 2", null);
				for (PicklistItem plb : cPicklist) {
					out.print("<li id='sp-" + plb.getValue() + "' onclick='moveSport(this, \"sp1\", \"sp2\");'><div class='left'>" + HtmlUtils.writeImage((short)0, plb.getValue(), 'S', null, null) + "</div><div class='right'>" + plb.getText() + "</div></li>");
				}
			%>
			</ul></td>
			<td><ul id="sp2" class="loginsports"></ul></td>
			<td>
		</tr>
		<tr>
		<td colspan="2" style="padding-top:10px;text-align:center;"><input type="button" class="button ok" onclick="createAccount();" value="<%=StringUtils.text("create.account", session)%>" style="padding-right:5px;"/></td>
	</tr>
	</table>
</div>
</div><!-- <div id="login"> -->
</form>
<script><!--
window.onload = function() {
	$('username').focus();
	$$('#login input').each(function(el){
		$(el).writeAttribute('autocomplete', el.id != 'username' ? 'off' : 'on');	
	});
}
	
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>