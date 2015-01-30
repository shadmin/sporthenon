<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%@ page import="com.sporthenon.db.entity.meta.Member" %>
<%
	Object o = session.getAttribute("user");
	Member m = null;
	if (o != null && o instanceof Member)
		m = (Member) o;
	String version = ConfigUtils.getProperty("version");
	if (session.getAttribute("locale") == null)
		session.setAttribute("locale", request.getLocale().toString().substring(0, 2));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML+RDFa 1.1//EN" "http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<title><%=request.getAttribute("title")%></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="Description" content="Search for results in various sports and events."/>
	<meta name="keywords" content="sport, results, database, records, olympics"/>
	<meta property="og:title" content="SPORTHENON - Temple of Sports Results"/>
	<meta property="og:type" content="website"/>
	<meta property="og:image" content="http://92.243.3.85/img/icon-notext.png?v=6"/>
	<meta property="og:description" content="Search for results in various sports and events."/>
	<link rel="stylesheet" type="text/css" href="css/sh.css?v=<%=version%>"/>
	<!--[if IE 6]>
	<link rel="stylesheet" type="text/css" href="css/ie6fix.css?v=<%=version%>"/>
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="css/render.css?v=<%=version%>"/>
	<link rel="shortcut icon" type="image/x-icon" href="img/iconfav.ico?v=6"/>
	<script type="text/javascript" src="js/prototype.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/includes.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/sh.js?v=<%=version%>"></script>
	<script type="text/javascript">
		var imgFolder = '<%=ConfigUtils.getProperty("img.url")%>';
	</script>
</head>

<body>
<div id="header">
	<div id="logo"><a href="/" title="Home Page"><img src="img/icon.png?v=8" alt="Sporthenon.com"/></a></div>
	<div id="shmenu">
		<ul>
			<li><a id="shmenu-results" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("results") ? "class='selected'" : "")%> href="results"><%=StringUtils.text("menu.results", session)%></a></li>
			<li><a id="shmenu-olympics" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("olympics") ? "class='selected'" : "")%> href="olympics"><%=StringUtils.text("menu.olympics", session)%></a></li>
			<li><a id="shmenu-usleagues" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("usleagues") ? "class='selected'" : "")%> href="usleagues"><%=StringUtils.text("menu.usleagues", session)%></a></li>
		</ul>
	</div>
	<div id="share">
		<table>
			<tr><td style="padding-bottom:3px;"><%=StringUtils.text("share", session)%>:</td>
			<td><a href="https://www.facebook.com/sharer/sharer.php?u=http%3A%2F%2Fwww.sporthenon.com%2F" target="_blank"><img alt="facebook" title="<%=StringUtils.text("share.on", session)%> Facebook" src="img/header/facebook.png"/></a></td>
			<td><a href="https://twitter.com/share?text=Visit%20http%3A%2F%2Fwww.sporthenon.com%20the%20temple%20of%20sports%20results%21" target="_blank"><img alt="twitter" title="<%=StringUtils.text("share.on", session)%> Twitter" src="img/header/twitter.png"/></a></td>
			<td><a href="https://plus.google.com/share?url=www.sporthenon.com" target="_blank"><img alt="gplus" title="<%=StringUtils.text("share.on", session)%> Google+" src="img/header/gplus.png"/></a></td></tr>
		</table>
	</div>
</div>

<script type="text/javascript">
	var TEXT_OK = "<%=StringUtils.text("ok", session)%>";
	var TEXT_CANCEL = "<%=StringUtils.text("cancel", session)%>";
	var TEXT_ACTIONS_TAB = "<%=StringUtils.text("actions.currenttab", session)%>";
	var TEXT_RUN = "<%=StringUtils.text("button.run", session)%>";
	var TEXT_RESET = "<%=StringUtils.text("button.reset", session)%>";
	var TEXT_EXPORT = "<%=StringUtils.text("button.export", session)%>";
	var TEXT_LINK = "<%=StringUtils.text("button.link", session)%>";
	var TEXT_PRINT = "<%=StringUtils.text("button.print", session)%>";
	var TEXT_INFO = "<%=StringUtils.text("button.info", session)%>";
	var TEXT_CLOSE = "<%=StringUtils.text("button.close", session)%>";
	var TEXT_BLANK = "<%=StringUtils.text("blank", session)%>";
	var TEXT_CURRENTLY_SELECTED = "<%=StringUtils.text("currently.selected", session)%>";
	var TEXT_ALL = "<%=StringUtils.text("all", session)%>";
	var TEXT_CLICK_CHANGE = "<%=StringUtils.text("click.change", session)%>";
	var TEXT_EXPAND = "<%=StringUtils.text("expand", session)%>";
	var TEXT_COLLAPSE = "<%=StringUtils.text("collapse", session)%>";
	var TEXT_KB = "<%=StringUtils.text("kb", session)%>";
	var TEXT_SECONDS = "<%=StringUtils.text("seconds", session)%>";
	var TEXT_YEARS = "<%=StringUtils.text("years", session)%>";
	var TEXT_OLYMPIC_GAMES = "<%=StringUtils.text("olympic.games", session)%>";
	var TEXT_EVENTS = "<%=StringUtils.text("events", session)%>";
	var TEXT_COUNTRIES = "<%=StringUtils.text("countries", session)%>";
	var TEXT_TEAMS = "<%=StringUtils.text("teams", session)%>";
	var TEXT_CATEGORIES = "<%=StringUtils.text("categories", session)%>";
	var TEXT_SELECT = "<%=StringUtils.text("select", session)%>";
	var TEXT_SELECTION = "<%=StringUtils.text("selection", session)%>";
	var TEXT_LOADING = "<%=StringUtils.text("loading", session)%>";
	var TEXT_SEARCH = "<%=StringUtils.text("search.for", session)%>";
	var TEXT_DESC_RESULTS = "<%=StringUtils.text("desc.results", session)%>";
	var TEXT_DESC_OLYMPICS = "<%=StringUtils.text("desc.olympics", session)%>";
	var TEXT_DESC_USLEAGUES = "<%=StringUtils.text("desc.usleagues", session)%>";
</script>

<div id="headertop">
	<div id="menutop">
		<div id="mthome"><a href="/"><%=StringUtils.text("menu.home", session)%></a></div>
		<div id="mtproject"><a href="project"><%=StringUtils.text("menu.project", session)%></a></div>
		<div id="mtcontribute"><a href="contribute"><%=StringUtils.text("menu.contribute", session)%></a></div>
	</div>
	<div id="flags"><a title="English" href="javascript:setLang('en');"><img alt="EN" src="img/header/lang-en.png"/></a>&nbsp;<a title="Français" href="javascript:setLang('fr');"><img alt="FR" src="img/header/lang-fr.png"/></a>&nbsp;</div>
	<div id="searchpanel">
		<table style="border-spacing:0px;"><tr><td><a title="Click for advanced search" href="search"><img alt="Search" src="img/menu/dbsearch.png"/></a></td>
		<td class="pattern" style="padding-bottom:3px;"><input type="text" class="text" name="dpattern" id="dpattern" value="<%=StringUtils.text("search.for", session)%>" title="Search in Sporthenon" onkeydown="directSearch();" onfocus="dpatternFocus();" onblur="dpatternBlur();" style="color:#AAA;"></input></td>
		</tr></table>
	</div>
</div>

<div id="content">