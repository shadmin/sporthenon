<%@ page language="java" %>
<jsp:include page="/jsp/common/header.jsp" />
<form id="flogin" name="flogin" action="LoginServlet?auth" method="post">
<div align="center">
<%
	String msg = (String) request.getAttribute("msg");
	if (msg != null)
		out.println("<br/><span class='loginmsg'>" + msg + "</span>");
%>
<div class="fieldset" style="text-align:center;width:300px;">
	<div class="fstitle login">AUTHENTICATION</div>
	<div>
		<table class="tlogin">
			<tr>
				<td>Login:</td>
				<td><input type="text" id="login" name="login"/></td>
			</tr>
			<tr>
				<td>Password:</td>
				<td><input type="password" id="password" name="password" onkeydown="if(event.keyCode==13){auth()}"/></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align:center;">
					<div class="button" style="width:70px;"><a href="javascript:" onclick="auth()">Login</a></div>
				</td>
			</tr>
		</table>
	</div>
</div>
</div>
</form>
<div align="center">
<div class="fieldset" style="text-align:center;width:500px;">
	<div class="fstitle login">NOT REGISTERED? CREATE AN ACCOUNT</div>
	<div class="register">
	<div id="r-msg" style="display:none;"></div>
	<table>
		<tr>
			<td style="text-align:right;"><span class="mandatory">*</span>Login:</td>
			<td><input type="text" id="r-login"/></td>
		</tr>
		<tr>
			<td style="text-align:right;"><span class="mandatory">*</span>Password:</td>
			<td><input type="password" id="r-password"/></td>
		</tr>
		<tr>
			<td style="text-align:right;"><span class="mandatory">*</span>Confirm Password:</td>
			<td><input type="password" id="r-password2"/></td>
		</tr>
		<tr>
			<td style="text-align:right;"><span class="mandatory">*</span>E-Mail Address:</td>
			<td><input type="text" id="r-email" size="40"/></td>
		</tr>
		<tr>
			<td style="text-align:right;">Last Name:</td>
			<td><input type="text" id="r-lastname"/></td>
		</tr>
		<tr>
			<td style="text-align:right;">First Name:</td>
			<td><input type="text" id="r-firstname"/></td>
		</tr>
		<tr>
		<td colspan="2" style="text-align:center;">
			<div class="button" style="width:130px;"><a href="javascript:void(0);" onclick="createAccount()">Create Account</a></div>
		</td>
	</tr>
	</table>
	</div>
</div>
</div>
<script type="text/javascript">
	$('login').focus();
</script>
<jsp:include page="/jsp/common/footer.jsp" />