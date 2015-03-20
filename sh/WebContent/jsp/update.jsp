<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="com.sporthenon.utils.ConfigUtils"%>
<%@page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp" />
<div id="update">
	<div class="fieldset">
		<div class="fstitle">CREER/MODIFIER UN RESULTAT</div>
		<div class="fscontent" style="height:430px;">
			<div id="ajaxdiv"></div>
			<div style="float:left;padding-right:10px;height:350px;">
			<table style="margin-top:0px;">
				<tr><td colspan="5"><input type="text" id="sp" name="Sport" value="Sport"/></td></tr>
				<tr><td><img src="/img/component/treeview/join.gif"/></td><td colspan="4"><input type="text" id="cp" name="Championship" value="Championship"/></td></tr>
				<tr><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/join.gif"/></td><td colspan="3"><input type="text" id="ev" name="Event #1" value="Event #1"/></td></tr>
				<tr><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/join.gif"/></td><td colspan="2"><input type="text" id="se" name="Event #2" value="Event #2"/></td></tr>
				<tr><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/empty.gif"/></td><td><img src="/img/component/treeview/join.gif"/></td><td><input type="text" id="se2" name="Event #3" value="Event #3"/></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="yr" name="Year" value="Year"/></td>
			</table>
			<table>
				<tr><td><input type="text" id="dt1" name="From" value="From"/><br/><a href="#">Today</a>&nbsp;<a href="#">Yesterday</a></td>
				<td>&nbsp;<input type="text" id="dt2" name="To" value="To"/><br/><a href="#">Today</a>&nbsp;<a href="#">Yesterday</a></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="pl1" name="Venue/City 1" value="Venue/City 1"/></td></tr>
				<tr><td><input type="text" id="pl2" name="Venue/City 2" value="Venue/City 2"/></td></tr>
			</table>
			<table>
				<tr><td><input type="text" id="exa" name="Ties" value="Ties"/></td>
			</table>
			<table>
				<tr><td><input type="text" id="cmt" name="Comment" value="Comment"/></td>
			</table>
			</div>
			<div style="height:350px;">
			<table style="margin-top:0px;">
				<tr><td><input type="text" id="rk1" name="Rank #1" value="Rank #1"/></td><td>&nbsp;<input type="text" id="rs1" name="Result/Score" value="Result/Score" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk2" name="Rank #2" value="Rank #2"/></td><td>&nbsp;<input type="text" id="rs2" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk3" name="Rank #3" value="Rank #3"/></td><td>&nbsp;<input type="text" id="rs3" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk4" name="Rank #4" value="Rank #4"/></td><td>&nbsp;<input type="text" id="rs4" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk5" name="Rank #5" value="Rank #5"/></td><td>&nbsp;<input type="text" id="rs5" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk6" name="Rank #6" value="Rank #6"/></td><td>&nbsp;<input type="text" id="rs6" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk7" name="Rank #7" value="Rank #7"/></td><td>&nbsp;<input type="text" id="rs7" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk8" name="Rank #8" value="Rank #8"/></td><td>&nbsp;<input type="text" id="rs8" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk9" name="Rank #9" value="Rank #9"/></td><td>&nbsp;<input type="text" id="rs9" name="Result" value="Result" style="width:100px;"/></td></tr>
				<tr><td><input type="text" id="rk10" name="Rank #10" value="Rank #10"/></td><td>&nbsp;<input type="text" id="rs10" name="Result" value="Result" style="width:100px;"/></td></tr>
			</table>
			</div>
			<table>
				<tr><td><input id='savebtn' type='button' class='button' onclick='saveResult();' value='Enregistrer'/></td></tr>
				<tr><td id="msg" style="font-weight:bold;padding-top:10px;"></td></tr>
			</table>
		</div>
	</div>
</div>
<script type="text/javascript">
window.onload = function() {
	initUpdate('<%=request.getAttribute("value")%>');
}
</script>
<jsp:include page="/jsp/common/footer.jsp" />