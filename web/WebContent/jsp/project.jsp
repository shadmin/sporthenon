<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils"%>
<%@ page import="com.sporthenon.utils.StringUtils"%>
<jsp:include page="/jsp/common/header.jsp"/>
<div id="project">
	<div class="right">
		<!-- GITHUB -->
		<div class="fieldset" style="height:300px;overflow-y:auto;margin-top:0px;">
			<div class="fstitle github">GITHUB</div>
			<div class="fscontent">
				<%=StringUtils.text("github.desc", session)%> :<br/><a target="_blank" href="https://github.com/shadmin/sporthenon">https://github.com/shadmin/sporthenon</a>
				<table style="margin-top:10px;">
					<tr><th style="padding:0px;"><table><tr><td><img alt="" src="/img/db/export/txt.png"/></td><td style="padding-bottom:1px;"><%=StringUtils.text("change.log", session)%></td></tr></table></th></tr>
					<tr><td style="text-align:center;"><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/CHANGELOG.txt">https://raw.githubusercontent.com/shadmin/sporthenon/master/CHANGELOG.txt</a></td></tr>
					<tr><th style="padding:0px;"><table><tr><td><img alt="" src="/img/project/reportbug.png"/></td><td style="padding-bottom:1px;"><%=StringUtils.text("report.bug", session)%></td></tr></table></th></tr>
					<tr><td style="text-align:center;"><a target="_blank" href="http://github.com/shadmin/sporthenon/issues">http://github.com/shadmin/sporthenon/issues</a></td></tr>
					<tr><th style="padding:0px;"><table><tr><td><img alt="" src="/img/project/wiki.png"/></td><td style="padding-bottom:1px;">Wiki</td></tr></table></th></tr>
					<tr><td style="text-align:center;"><a target="_blank" href="http://github.com/shadmin/sporthenon/wiki">http://github.com/shadmin/sporthenon/wiki</a></td></tr>
				</table>
			</div>
		</div>
	</div>
	<div class="left">
		<!-- CONTRIBUTORS -->
		<div class="fieldset" style="height:300px;">
		<div class="fstitle contributors"><%=StringUtils.text("contributors", session)%></div>
		<div class="fscontent">
			<div style="height:260px;overflow:auto;">
			<table><tr><th>ID</th><th><%=StringUtils.text("name", session)%></th><th><%=StringUtils.text("entity.SP", session)%></th><th><%=StringUtils.text("contributions", session)%></th></tr><%=request.getAttribute("contributors")%></table>
			</div>
		</div>
		</div>
	</div>
	<div>
		<!-- ANDROID -->
		<div class="fieldset">
		<div class="fstitle android">ANDROID</div>
		<div class="fscontent" style="overflow:auto;">
			<%=StringUtils.text("link.google.play", session)%> : <a target="_blank" href="http://play.google.com/store/apps/details?id=com.sporthenon.android">http://play.google.com/store/apps/details?id=com.sporthenon.android</a>
			<br/><br/><%=StringUtils.text("screenshots", session)%> :
			<table id="screenshots"><tr>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-17.png"><img alt="sc1" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-17.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-33.png"><img alt="sc2" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-33.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-40.png"><img alt="sc3" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-40.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-55.png"><img alt="sc4" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-48-55.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-02.png"><img alt="sc5" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-02.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-33.png"><img alt="sc6" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-33.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-51.png"><img alt="sc7" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-51.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-58.png"><img alt="sc8" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-49-58.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-50-06.png"><img alt="sc9" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-50-06.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-50-06.png"><img alt="sc10" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-50-06.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-50-35.png"><img alt="sc11" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-50-35.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-51-44.png"><img alt="sc12" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-51-44.png"/></a></td>
				<td><a target="_blank" href="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-51-57.png"><img alt="sc13" src="https://raw.githubusercontent.com/shadmin/sporthenon/master/res/Screenshots/Android%20V2.1/Screenshot_2016-11-08-16-51-57.png"/></a></td>				
			</tr></table>
		</div>
		</div>
		<!-- TECHNICAL INFO -->
		<div class="fieldset">
		<div class="fstitle technicalinfo"><%=StringUtils.text("technical.info", session)%></div>
		<div class="fscontent">
			<table>
				<tr><th colspan="4" style="text-align:center;"><%=StringUtils.text("programming", session)%></th></tr>
				<tr><td>Java Development Kit</td><td>1.7.0_79</td><td>Required JDK + JRE</td><td><a href="http://www.oracle.com/technetwork/java/index.html" target="_blank">http://www.oracle.com/technetwork/java/index.html</a></td></tr>
				<tr><td>Eclipse</td><td>4.2.0 (Juno)</td><td>J2EE development platform</td><td><a href="http://www.eclipse.org/" target="_blank">http://www.eclipse.org</a></td></tr>
				<tr><td>NSIS</td><td>2.46</td><td>Setup-wizard maker</td><td><a href="http://nsis.sourceforge.net/" target="_blank">http://nsis.sourceforge.net</a></td></tr>
				<tr><td>Jsoup</td><td>1.7.2</td><td>Java HTML Parser</td><td><a href="http://jsoup.org/" target="_blank">http://jsoup.org</a></td></tr>
				<tr><td>Janel</td><td>4.0.4</td><td>Executable maker (from JAR)</td><td><a href="http://sourceforge.net/projects/janel/" target="_blank">http://sourceforge.net/projects/janel</a></td></tr>
				<tr><td>Prototype</td><td>1.6.1</td><td>Javascript framework</td><td><a href="http://prototypejs.org/" target="_blank">http://prototypejs.org</a></td></tr>
				<tr><td>Script.aculo.us</td><td>1.8.3</td><td>Javascript framework (UI)</td><td><a href="http://script.aculo.us/" target="_blank">http://script.aculo.us</a></td></tr>
				<tr><td>Subclipse</td><td>1.8.20</td><td>SVN plug-in for Eclipse</td><td><a href="http://subclipse.tigris.org/" target="_blank">http://subclipse.tigris.org</a></td></tr>
				<tr><th colspan="4" style="text-align:center;"><%=StringUtils.text("db.hosting", session)%></th></tr>
				<tr><td>CentOS</td><td>5.6</td><td>Linux server</td><td><a href="http://www.centos.org/" target="_blank">http://www.centos.org</a></td></tr>
				<tr><td>Glassfish</td><td>3.1.2</td><td>Java application server</td><td><a href="http://glassfish.java.net/" target="_blank">http://glassfish.java.net</a></td></tr>
				<tr><td>PostgreSQL</td><td>9.4.5</td><td>Database server</td><td><a href="http://www.postgresql.org/" target="_blank">http://www.postgresql.org</a></td></tr>
			</table>
		</div>
		</div>
	</div>
</div>
<script type="text/javascript"><!--
window.onload = function() {
	//loadChart();
	t1 = parseInt('<%=request.getAttribute("t1")%>');
	t2 = parseInt('<%=request.getAttribute("t2")%>');
	handleRender();
}
--></script>
<jsp:include page="/jsp/common/footer.jsp"/>