<%@ page language="java" %>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%@ page import="com.sporthenon.db.entity.meta.Member" %>
<%
	Object o = session.getAttribute("user");
	Member m = null;
	if (o != null && o instanceof Member)
		m = (Member) o;
	String mn = String.valueOf(session.getAttribute("menu"));
	String userTxt = (m != null ? (StringUtils.notEmpty(m.getLastName()) ? m.getFirstName() + " " + m.getLastName() + " [" + m.getLogin() + "]" : "[" + m.getLogin() + "]") : "[Anonymous]");
	String version = ConfigUtils.getProperty("version");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title>Sporthenon - The temple of sports results</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/sh.css?v=<%=version%>" />
	<!--[if IE 6]>
	<link rel="stylesheet" type="text/css" href="css/ie6fix.css?v=<%=version%>" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="css/render.css?v=<%=version%>" />
	<link rel="icon" type="image/x-icon" href="img/icon16.ico?v=10" />
	<script type="text/javascript" src="js/prototype.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/includes.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/sh.js?v=<%=version%>"></script>
	<script type="text/javascript">
		var imgFolder = '<%=ConfigUtils.getProperty("img.url")%>';
	</script>
</head>

<body>
<div id="header">
	<div id="logo"><a href="http://www.sporthenon.com" title="Sporthenon.com"><img src="img/icon.png" alt="Sporthenon.com"/></a></div>
	<div id="shmenu">
		<ul>
			<li><a id="shmenu-home" <%=(mn.equals("home") ? "class='selected'" : "")%> href="home">HOME</a></li>
			<li><a id="shmenu-results" <%=(mn.equals("results") ? "class='selected'" : "")%> href="results">RESULTS</a></li>
			<li><a id="shmenu-olympics" <%=(mn.equals("olympics") ? "class='selected'" : "")%> href="olympics">OLYMPICS</a></li>
			<li><a id="shmenu-usleagues" <%=(mn.equals("usleagues") ? "class='selected'" : "")%> href="usleagues">US LEAGUES</a></li>
			<li><a id="shmenu-search" <%=(mn.equals("search") ? "class='selected'" : "")%> href="search">SEARCH</a></li>
			<li><a id="shmenu-project" <%=(mn.equals("project") ? "class='selected'" : "")%> href="project">PROJECT</a></li>
			<% if (session.getAttribute("user") != null) { %>
			<li><a id="shmenu-update" <%=(mn.matches(".*update.*") ? "class='selected'" : "")%> href="update?data">UPDATE</a></li>
			<li><a id="shmenu-logout" <%=(mn.equals("logout") ? "class='selected'" : "")%> href="LoginServlet?logout">LOGOUT</a></li>
			<% } else { %>
			<li><a id="shmenu-login" <%=(mn.equals("login") ? "class='selected'" : "")%> href="login">LOGIN</a></li>
			<% } %>
		</ul>
	</div>
	<div id="links">
		<a target="_blank" title="SourceForge.net" href="http://sourceforge.net/projects/sporthenon"><img style="border:1px solid #888;" src="img/header/sf.png" alt="Sourceforge.net" /></a>&nbsp;
		<a target="_blank" title="Valid XHTML 1.0" href="http://validator.w3.org/check?uri=referer"><img src="img/header/validxhtml10.png" alt="Valid XHTML 1.0"/></a><br/>
		&copy;2011-13 (sporthenon.com)<br/>
		<div id="version">v<%=ConfigUtils.getProperty("version")%></div>
	</div>
</div>

<div id="content">