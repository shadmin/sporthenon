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
	<title>SPORTHENON&nbsp;-&nbsp;Temple of Sports Results</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/sh.css?v=<%=version%>" />
	<!--[if IE 6]>
	<link rel="stylesheet" type="text/css" href="css/ie6fix.css?v=<%=version%>" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="css/render.css?v=<%=version%>" />
	<link rel="icon" type="image/x-icon" href="img/icon16.ico?v=1" />
	<script type="text/javascript" src="js/prototype.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/includes.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/sh.js?v=<%=version%>"></script>
	<script type="text/javascript">
		var imgFolder = '<%=ConfigUtils.getProperty("img.url")%>';
	</script>
</head>

<body>
<div id="header">
	<div id="logo"><a href="http://www.sporthenon.com" title="Sporthenon.com (Home)"><img src="img/icon.png" alt="Sporthenon.com"/></a></div>
	<div id="shmenu">
		<ul>
			<li><a id="shmenu-home" <%=(mn.equals("home") ? "class='selected'" : "")%> href="home">HOME</a></li>
			<li><a id="shmenu-results" <%=(mn.equals("results") ? "class='selected'" : "")%> href="results">RESULTS</a></li>
			<li><a id="shmenu-olympics" <%=(mn.equals("olympics") ? "class='selected'" : "")%> href="olympics">OLYMPICS</a></li>
			<li><a id="shmenu-usleagues" <%=(mn.equals("usleagues") ? "class='selected'" : "")%> href="usleagues">US LEAGUES</a></li>
			<li><a id="shmenu-search" <%=(mn.equals("search") ? "class='selected'" : "")%> href="search">SEARCH</a></li>
			<li><a id="shmenu-project" <%=(mn.equals("project") ? "class='selected'" : "")%> href="project">PROJECT</a></li>
		</ul>
	</div>
	<div id="links">
		<table>
			<tr><td style="padding-bottom:3px;">Share:</td>
			<td><a href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fwww.sporthenon.com" target="_blank"><img alt="facebook" title="Share on Facebook" src="img/header/facebook.png"/></a></td>
			<td><a href="https://twitter.com/share?text=Visit%20http%3A%2F%2Fwww.sporthenon.com%20the%20temple%20of%20sports%20results%21" target="_blank"><img alt="twitter" title="Share on Twitter" src="img/header/twitter.png"/></a></td>
			<td><a href="https://plus.google.com/share?url=www.sporthenon.com" target="_blank"><img alt="gplus" title="Share on Google+" src="img/header/gplus.png"/></a></td></tr>
		</table><br/>
		<a target="_blank" title="Sporthenon on Google Code" href="https://code.google.com/p/sporthenon/"><img src="img/header/gcode.png" alt="Google Code" /></a>&nbsp;
		<a target="_blank" title="Valid XHTML 1.0" href="http://validator.w3.org/check?uri=referer"><img src="img/header/validxhtml10.png" alt="Valid XHTML 1.0"/></a><br/>
		&copy;2011-14 (sporthenon.com)<br/>
		<div id="version">Version&nbsp;<%=ConfigUtils.getProperty("version")%></div>
	</div>
</div>

<div id="content">