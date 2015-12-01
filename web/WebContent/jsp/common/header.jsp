<%@page import="org.hibernate.exception.Configurable"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.ConfigUtils" %>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<%@ page import="com.sporthenon.utils.res.ResourceUtils" %>
<%@ page import="com.sporthenon.db.entity.meta.Contributor" %>
<%
	Object o = session.getAttribute("user");
	Contributor m = null;
	if (o != null && o instanceof Contributor)
		m = (Contributor) o;
	ResourceUtils.setLocale(request);
	Object title = request.getAttribute("title");
	Object desc = request.getAttribute("desc");
	String version = ConfigUtils.getProperty("version");
	String lang = String.valueOf(session.getAttribute("locale"));
	String url = ConfigUtils.getProperty("url");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML+RDFa 1.1//EN" "http://www.w3.org/MarkUp/DTD/xhtml-rdfa-2.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="<%=lang%>" xml:lang="<%=lang%>">
<head>
	<title><%=title%></title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta name="Description" content="<%=desc%>"/>
	<meta name="keywords" content="<%=StringUtils.text("keywords", session)%>"/>
	<link rel="alternate" hreflang="x-default" href="http://www.sporthenon.com/"/>
	<link rel="alternate" hreflang="fr" href="http://fr.sporthenon.com/"/>
	<meta property="og:title" content="<%=title%>"/>
	<meta property="og:type" content="website"/>
	<meta property="og:image" content="<%=url%>img/icon-notext-shadow.png?1"/>
	<% if (request.isSecure() || !ConfigUtils.getProperty("env").equals("prod") || !request.getRequestURL().toString().contains("sporthenon.com")) { %>
	<meta name="robots" content="noindex, nofollow"/>
	<% } %>
	<link rel="stylesheet" type="text/css" href="/css/sh.css?v=<%=version%>"/>	
	<link rel="stylesheet" type="text/css" href="/css/render.css?v=<%=version%>"/>
	<link rel="shortcut icon" type="image/x-icon" href="/img/iconfav.ico?v=6"/>
	<script type="text/javascript" src="/js/prototype.js?v=<%=version%>"></script>
	<script type="text/javascript" src="/js/includes.js?v=<%=version%>"></script>
	<script type="text/javascript" src="/js/sh.js?v=<%=version%>"></script>
</head>

<body>
<div id="header">
	<div id="logo"><a href="/" title="<%=StringUtils.text("menu.home", session)%>"><img src="/img/icon.png?v=8" alt="sporthenon.com"/></a></div>
	<div id="shmenu">
		<ul>
			<li><a id="shmenu-results" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("results") ? "class='selected'" : "")%> href="/results"><%=StringUtils.text("menu.results", session)%></a></li>
			<li><a id="shmenu-calendar" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("calendar") ? "class='selected'" : "")%> href="/calendar"><%=StringUtils.text("menu.calendar", session)%></a></li>
			<li><a id="shmenu-olympics" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("olympics") ? "class='selected'" : "")%> href="/olympics"><%=StringUtils.text("menu.olympics", session)%></a></li>
			<li><a id="shmenu-usleagues" <%=(request.getAttribute("menu") != null && request.getAttribute("menu").equals("usleagues") ? "class='selected'" : "")%> href="/usleagues"><%=StringUtils.text("menu.usleagues", session)%></a></li>
		</ul>
	</div>
	<div id="sharesite">
		<table>
			<tr><td style="padding-bottom:3px;"><%=StringUtils.text("share", session)%>:</td>
			<td><a href="https://www.facebook.com/sharer/sharer.php?u=<%=url.replaceAll("\\:", "%3A").replaceAll("\\/", "%2F") + lang%>" target="_blank"><img alt="facebook" title="<%=StringUtils.text("share.on", session)%> Facebook" src="/img/header/facebook.png"/></a></td>
			<td><a href="https://twitter.com/share?text=<%=StringUtils.text("title", session).replaceAll("\\s", "%20")%>&amp;url=<%=url.replaceAll("\\:", "%3A").replaceAll("\\/", "%2F") + lang%>" target="_blank"><img alt="twitter" title="<%=StringUtils.text("share.on", session)%> Twitter" src="/img/header/twitter.png"/></a></td>
			<td><a href="https://plus.google.com/share?url<%=url.replaceAll("\\:", "%3A").replaceAll("\\/", "%2F") + lang%>" target="_blank"><img alt="gplus" title="<%=StringUtils.text("share.on", session)%> Google+" src="/img/header/gplus.png"/></a></td></tr>
		</table>
	</div>
	<div id="android">
		<a href="https://play.google.com/store/apps/details?id=com.sporthenon.android" target="_blank"><img alt="Android app on Google Play" src="https://developer.android.com/images/brand/<%=lang%>_app_rgb_wo_45.png" /></a>
	</div>
</div>

<script type="text/javascript"><!--
	var IMG_URL = "<%=ConfigUtils.getProperty("img.url")%>";
	var TX_OK = "<%=StringUtils.text("ok", session)%>";
	var TX_CANCEL = "<%=StringUtils.text("cancel", session)%>";
	var TX_ACTIONS_TAB = "<%=StringUtils.text("actions.currenttab", session)%>";
	var TX_RUN = "<%=StringUtils.text("button.run", session)%>";
	var TX_RESET = "<%=StringUtils.text("button.reset", session)%>";
	var TX_EXPORT = "<%=StringUtils.text("button.export", session)%>";
	var TX_LINK = "<%=StringUtils.text("button.link", session)%>";
	var TX_PRINT = "<%=StringUtils.text("button.print", session)%>";
	var TX_INFO = "<%=StringUtils.text("button.info", session)%>";
	var TX_CLOSE = "<%=StringUtils.text("button.close", session)%>";
	var TX_BLANK = "<%=StringUtils.text("blank", session)%>";
	var TX_CURRENTLY_SELECTED = "<%=StringUtils.text("currently.selected", session)%>";
	var TX_ALL = "<%=StringUtils.text("all", session)%>";
	var TX_CLICK_CHANGE = "<%=StringUtils.text("click.change", session)%>";
	var TX_EXPAND = "<%=StringUtils.text("expand", session)%>";
	var TX_COLLAPSE = "<%=StringUtils.text("collapse", session)%>";
	var TX_KB = "<%=StringUtils.text("kb", session)%>";
	var TX_SECONDS = "<%=StringUtils.text("seconds", session)%>";
	var TX_YEARS = "<%=StringUtils.text("years", session)%>";
	var TX_OLYMPIC_GAMES = "<%=StringUtils.text("olympic.games", session)%>";
	var TX_EVENTS = "<%=StringUtils.text("events", session)%>";
	var TX_COUNTRIES = "<%=StringUtils.text("countries", session)%>";
	var TX_TEAMS = "<%=StringUtils.text("teams", session)%>";
	var TX_CATEGORIES = "<%=StringUtils.text("categories", session)%>";
	var TX_SELECT = "<%=StringUtils.text("select", session)%>";
	var TX_SELECTION = "<%=StringUtils.text("selection", session)%>";
	var TX_LOADING = "<%=StringUtils.text("loading", session)%>";
	var TX_SEARCH = "<%=StringUtils.text("search.for", session)%>";
	var TX_SEARCH2 = "<%=StringUtils.text("search.in", session)%> Sporthenon";
	var TX_DESC_RESULTS = "<%=StringUtils.text("desc.results", session)%>";
	var TX_DESC_CALENDAR = "<%=StringUtils.text("desc.calendar", session)%>";
	var TX_DESC_OLYMPICS = "<%=StringUtils.text("desc.olympics", session)%>";
	var TX_DESC_USLEAGUES = "<%=StringUtils.text("desc.usleagues", session)%>";
	var TX_MLOGIN = "<%=StringUtils.text("mandatory.login", session)%>";
	var TX_MPASSWORD = "<%=StringUtils.text("mandatory.password", session)%>";
	var TX_MCONFIRMPWD = "<%=StringUtils.text("mandatory.confirmpwd", session)%>";
	var TX_MEMAIL = "<%=StringUtils.text("mandatory.email", session)%>";
	var TX_MSPORTS = "<%=StringUtils.text("mandatory.sports", session)%>";
	var TX_PWDNOTMATCH = "<%=StringUtils.text("pwd.nomatch", session)%>";
	var TX_PREVIOUS = "<%=StringUtils.text("previous", session)%>";
	var TX_NEXT = "<%=StringUtils.text("next", session)%>";
	var TX_CLICK_DRAGDROP = "<%=StringUtils.text("click.drag.drop", session)%>";
	var TX_REMOVE = "<%=StringUtils.text("remove", session)%>";
	var TX_MODIF_WARNING = "<%=StringUtils.text("modif.warning", session)%>";
	var TX_CONFIRM = "<%=StringUtils.text("confirm", session)%>";
	var TX_ADDFAV = "<%=StringUtils.text("add.favorites", session)%>";
	var TX_DELFAV = "<%=StringUtils.text("delete.favorites", session)%>";
	var TX_NOFAV = "<%=StringUtils.text("no.favorite", session)%>";
	var TX_ERROR = "<%=StringUtils.text("error", session)%>";
--></script>

<div id="headertop">
	<div id="menutop">
		<div id="mthome"><a href="/"><%=StringUtils.text("menu.home", session)%></a></div>
		<div id="mtproject"><a href="/project"><%=StringUtils.text("menu.project", session)%></a></div>
		<div id="mtcontribute"><a href="/contribute"><%=StringUtils.text("menu.contribute", session)%></a></div>
		<div id="mtfavorites"><a href="javascript:$('favorites').show();"><%=StringUtils.text("menu.favorites", session)%></a><div id="favorites" style="display:none;"></div></div>
		<% if (m != null) { %>
		<div id="mtcbarea"><a href="/update/overview"><%=StringUtils.text("menu.cbarea", session)%></a></div>
		<div id="mtlogout"><a href="/LoginServlet?logout"><%=StringUtils.text("menu.logout", session)%></a>&nbsp;(<%=m.getLogin()%>)</div>
		<% } else { %>
		<div id="mtlogin"><a href="<%=request.getAttribute("urlLogin")%>"><%=StringUtils.text("menu.login", session)%></a></div>
		<% } %>
	</div>
	<div id="flags"><a title="English" href="<%=request.getAttribute("urlEN")%>"><img alt="EN" src="/img/header/lang-en.png"/></a>&nbsp;<a title="Français" href="<%=request.getAttribute("urlFR")%>"><img alt="FR" src="/img/header/lang-fr.png"/></a>&nbsp;</div>
	<div id="searchpanel">
		<table style="border-spacing:0px;"><tr><td style="padding-top:3px;"><a title="<%=StringUtils.text("advanced.search", session)%>" href="/search"><img alt="Search" src="/img/menu/dbsearch.png"/></a></td>
		<td class="pattern" style="padding-bottom:3px;"><input type="text" class="text" name="dpattern" id="dpattern" value="<%=StringUtils.text("search.in", session)%> Sporthenon" onfocus="dpatternFocus();" onblur="dpatternBlur();"></input></td>
		</tr></table>
	</div>
</div>
<div id="ajaxsearch" class="ajaxsearch"></div>

<script type="text/javascript"><!--
new Ajax.Autocompleter(
	'dpattern',
	'ajaxsearch',
	'/search/ajax/1',
	{ paramName: 'value', minChars: 1, frequency: 0.05, updateElement: directSearch}
);
Event.observe($('dpattern'), 'keyup', directSearch);

// Load favorites
var tfav = getCookie('shfav').split('|');
var tfavHTML = [];
var nf = 0;
for (var i = 0 ; i < tfav.length ; i++) {
	var t = tfav[i].split(':');
	if (t && t.length > 1) {
		tfavHTML.push('<li id="fav-' + i + '"><a href="' + t[0] + '">' + t[1] + '</a>&nbsp;<img alt="del" title="' + TX_REMOVE + '" src="/img/delete.gif" style="cursor:pointer;" onclick="deleteFavClick(' + i + ');"/></li>');
		nf++;
	}
}
if (tfavHTML.length == 0) {
	tfavHTML.push('<li style="list-style:none;"><b>' + TX_NOFAV + '</b></li>');
	nf = 1;
}
tfavHTML.push('<a href="javascript:$(\'favorites\').hide();" style="padding-top:5px;float:right;">' + TX_CANCEL + '</a>');
$('favorites').style.height = ((nf * 15) + 25) + 'px';
$('favorites').update(tfavHTML.join(''));

var lang = '<%=lang%>';
--></script>

<div id="content">