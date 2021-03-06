<%@page import="com.sporthenon.utils.HtmlUtils"%>
<%@page import="java.util.Collection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<%@ page import="com.sporthenon.db.DatabaseHelper"%>
<%@ page import="com.sporthenon.db.entity.Sport"%>
<%@ page import="com.sporthenon.db.PicklistBean"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<form id="flogin" name="flogin" action="/LoginServlet?auth" method="post">
<div id="logindiv">
<%
	String msg = (String) request.getAttribute("msg");
	if (msg != null)
		out.println("<br/><span class='loginmsg'>" + msg + "</span>");
%>
<div class="fieldset" style="text-align:center;width:300px;">
	<div class="fstitle"><%=StringUtils.text("title.authentication", session)%></div>
	<div>
		<table class="tlogin">
			<tr>
				<th><%=StringUtils.text("loginid", session)%> :</td><td><input type="text" id="login" name="login" size="15" onkeydown="if(event.keyCode==13){$('flogin').submit();}"/></td>
			</tr>
			<tr>
				<th><%=StringUtils.text("password", session)%> :</td><td><input type="password" id="password" name="password" size="15" onkeydown="if(event.keyCode==13){$('flogin').submit();}"/></td>
			</tr>
			<tr>
				<td colspan="2" style="padding-top:10px;text-align:center;"><input type="button" class="button ok" onclick="auth();" value="OK" style="padding-right:5px;"/></td>
			</tr>
		</table>
	</div>
</div>
<div class="fieldset" style="text-align:center;width:600px;margin-top:20px;margin-bottom:10px;">
	<div class="fstitle"><%=StringUtils.text("title.createaccount", session)%></div>
	<div class="register">
	<div id="rmsg" style="display:none;"></div>
	<table style="width:auto;">
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("loginid", session)%> :</td><td><input type="text" id="rlogin"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("password", session)%> :</td><td><input type="password" id="rpassword"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("confirm.password", session)%> :</td><td><input type="password" id="rpassword2"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("email.addr", session)%> :</td><td><input type="text" id="remail" size="30"/></td>
		</tr>
		<tr>
			<th><%=StringUtils.text("public.name", session)%> :</td><td><input type="text" id="rpublicname"/></td>
		</tr>
	</table>
	<table>
		<tr><td colspan="2"><span class="mandatory">*</span><%=StringUtils.text("select.sports.contributor", session)%> :</td></tr>
		<tr>
			<td><ul id="sp1" class="loginsports">
			<%
				String lang = String.valueOf(session.getAttribute("locale"));
				Collection<PicklistBean> cPicklist = DatabaseHelper.getEntityPicklist(Sport.class, "label", null, lang);
				for (PicklistBean plb : cPicklist)
					out.print("<li id='sp-" + plb.getValue() + "' onclick='moveSport(this, \"sp1\", \"sp2\");'><div class='left'>" + HtmlUtils.writeImage((short)0, plb.getValue(), 'S', null, null) + "</div><div class='right'>" + plb.getText() + "</div></li>");
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
</div>
</div>
</form>
<script type="text/javascript"><!--
window.onload = function() {
	$('login').focus();
	$$('#logindiv input').each(function(el){
		$(el).writeAttribute('autocomplete', el.id != 'login' ? 'off' : 'on');	
	});
}
	
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>