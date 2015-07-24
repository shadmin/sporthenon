<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
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
				<th><%=StringUtils.text("loginid", session)%>&nbsp;:</td><td><input type="text" id="login" name="login" size="15"/></td>
			</tr>
			<tr>
				<th><%=StringUtils.text("password", session)%>&nbsp;:</td><td><input type="password" id="password" name="password" size="15" onkeydown="if(event.keyCode==13){$('flogin').submit();}"/></td>
			</tr>
			<tr>
				<td colspan="2" style="padding-top:10px;text-align:center;"><input type="button" class="button ok" onclick="auth();" value="OK"/></td>
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
			<th><span class="mandatory">*</span><%=StringUtils.text("loginid", session)%>&nbsp;:</td><td><input type="text" id="rlogin"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("password", session)%>&nbsp;:</td><td><input type="password" id="rpassword"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("confirm.password", session)%>&nbsp;:</td><td><input type="password" id="rpassword2"/></td>
		</tr>
		<tr>
			<th><span class="mandatory">*</span><%=StringUtils.text("email.addr", session)%>&nbsp;:</td><td><input type="text" id="remail" size="30"/></td>
		</tr>
		<tr>
			<th><%=StringUtils.text("public.name", session)%>&nbsp;:</td><td><input type="text" id="rpublicname"/></td>
		</tr>
		<tr>
			<th><%=StringUtils.text("entity.SP", session)%>&nbsp;:</td><td><table><tr>
				<td><select></select></td>
			</tr></table></td>
		</tr>
		<tr>
		<td colspan="2" style="padding-top:10px;text-align:center;"><input type="button" class="button ok" onclick="createAccount();" value="<%=StringUtils.text("create.account", session)%>"/></td>
	</tr>
	</table>
	</div>
</div>
</div>
</form>
<script type="text/javascript">
	$('login').focus();
</script>
<jsp:include page="/jsp/common/footer.jsp" />