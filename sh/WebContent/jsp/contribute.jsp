<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<jsp:include page="/jsp/common/header.jsp" />
<div id="contribute">
<div class="fieldset">
	<div class="fstitle info"><%=StringUtils.text("step", session)%>&nbsp;1</div>
	<div class="fscontent"><%=StringUtils.text("step1.desc", session)%></div>
</div>
<div class="fieldset">
	<div class="fstitle info"><%=StringUtils.text("step", session)%>&nbsp;2</div>
	<div class="fscontent"><%=StringUtils.text("step2.desc", session)%></div>
</div>
<div class="fieldset">
	<div class="fstitle info"><%=StringUtils.text("step", session)%>&nbsp;3</div>
	<div class="fscontent"><%=StringUtils.text("step3.desc", session)%></div>
</div>
</div>
<jsp:include page="/jsp/common/footer.jsp" />