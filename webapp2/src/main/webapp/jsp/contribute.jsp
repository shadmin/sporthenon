<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.*"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sporthenon.db.function.ContributorBean"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.db.DatabaseManager"%>
<%
	String lang = String.valueOf(session.getAttribute("locale"));
%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="contribute">
	<div style="display:flex;width:100%;">
		<div class="cbpanel" style="width:100%;">
			<div class="cbtitle about"><%=StringUtils.text("about.contribution", session)%></div>
			<%=ConfigUtils.getValue("html_contribution1_" + lang)%>
		</div>
		<div class="cbpanel" style="white-space:nowrap;">
			<div class="cbtitle download"><%=StringUtils.text("download2", session)%></div>
			<%=StringUtils.text("download.desc", session)%>
		</div>
	</div>
	<div style="display:flex;width:100%;">
		<div class="cbpanel" style="width:100%;">
			<div class="cbtitle howto"><%=StringUtils.text("howto.contribute", session)%></div>
			<%=ConfigUtils.getValue("html_contribution2_" + lang)%>
		</div>
		<div class="cbpanel">
			<div class="cbtitle users"><%=StringUtils.text("contributors", session)%></div>
			<table>
				<tr>
					<th>ID</th><th><%=StringUtils.text("name", session)%></th>
					<th><%=StringUtils.text("entity.SP", session)%></th>
					<th><%=StringUtils.text("contributions", session)%></th>
				</tr>
				<%
					StringBuffer html = new StringBuffer();
					Collection<?> coll = DatabaseManager.callFunctionSelect("_contributors", null, ContributorBean.class);
					for (Object obj : coll) {
						ContributorBean bean = (ContributorBean) obj;
						String sports = null;
						if (StringUtils.notEmpty(bean.getSports())) {
							List<String> l = Arrays.asList((lang.equalsIgnoreCase("fr") ? bean.getSportsFR() : bean.getSports()).split("\\|"));
							Collections.sort(l);
							sports = StringUtils.join(l, "<br/>");
						}
						out.print("<tr><td><a href='" + HtmlUtils.writeLink(Contributor.alias, bean.getId(), null, bean.getLogin()) + "'>" + bean.getLogin() + "</a></td>");
						out.print("<td>" + (StringUtils.notEmpty(bean.getName()) ? bean.getName() : "-") + "</td>");
						out.print("<td>" + (StringUtils.notEmpty(sports) ? sports : "-") + "</td>");
						out.print("<td style='white-space:nowrap;'><img style='vertical-align:middle;padding-bottom:2px;padding-right:5px;' alt='adds' title='" + ResourceUtils.getText("co.adds", lang) + "' src='/img/home/adds.png'/>" + bean.getCountA());
						out.print("&nbsp;<img style='vertical-align:middle;padding-bottom:2px;padding-right:5px;' alt='updates' title='" + ResourceUtils.getText("co.updates", lang) + "' src='/img/home/updates.png'/>" + bean.getCountU() + "</td></tr>");
					}
				%>
			</table>
		</div>
	</div>
</div>
<jsp:include page="/jsp/common/footer.jsp"/>