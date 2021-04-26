<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.db.entity.meta.Contributor"%>
<%@ page import="com.sporthenon.utils.*"%>
<%@ page import="com.sporthenon.utils.res.ResourceUtils"%>
<%@ page import="com.sporthenon.web.ServletHelper"%>
<%
	Object o = session.getAttribute("user");
	Contributor m = null;
	if (o != null && o instanceof Contributor) {
		m = (Contributor) o;
	}
	ResourceUtils.setLocale(request);
	Object title = request.getAttribute("title");
	Object desc = request.getAttribute("desc");
	String version = ConfigUtils.getProperty("version");
	String lang = String.valueOf(session.getAttribute("locale"));
	String url = "http://" + request.getServerName();
%>
<!DOCTYPE html>
<html lang="<%=lang%>">
<head>
	<title><%=title%></title>
	<meta charset="utf-8">
	<meta name="Description" content="<%=desc%>"/>
	<meta name="keywords" content="<%=StringUtils.text("keywords", session)%>"/>
	<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<link rel="alternate" hreflang="x-default" href="<%=request.getAttribute("urlEN")%>"/>
	<link rel="alternate" hreflang="en" href="<%=request.getAttribute("urlEN")%>"/>
	<link rel="alternate" hreflang="fr" href="<%=request.getAttribute("urlFR")%>"/>
	<link rel="stylesheet" type="text/css" href="/css/sh.css?v=<%=version%>"/>	
	<link rel="stylesheet" type="text/css" href="/css/render.css?v=<%=version%>"/>
	<link rel="stylesheet" type="text/css" href="/css/mobile.css?v=<%=version%>"/>
	<link rel="shortcut icon" type="image/x-icon" href="/img/logo/iconfav.png"/>
	<script src="/js/prototype.js?v=<%=version%>"></script>
	<script src="/js/includes.js?v=<%=version%>"></script>
	<%if (ConfigUtils.isProd()) {%>
	<script src="/js/sh.min.js?v=<%=version%>"></script>
	<!-- Global site tag (gtag.js) - Google Analytics -->
	<script async src="https://www.googletagmanager.com/gtag/js?id=G-WN79FCNBJD"></script>
	<script>
	  window.dataLayer = window.dataLayer || [];
	  function gtag(){dataLayer.push(arguments);}
	  gtag('js', new Date());
	  gtag('config', 'G-WN79FCNBJD');
	</script>
	<%} else {%>
	<script src="/js/sh.js?v=<%=version%>"></script>
	<%}%>
</head>

<body>
<!-- LEFT BAR -->
<div id="header">
	<div id="logo"><a href="/"><img src="/img/logo/icon.png?v=<%=version%>" alt="sporthenon.com"/></a></div>
	<div id="shmenu">
		<ul>
			<li><a id="shmenu-results" href="/results"><%=StringUtils.text("menu.results.2", session)%></a></li>
			<li><a id="shmenu-browse" href="/browse"><%=StringUtils.text("menu.browse.2", session)%></a></li>
			<li><a id="shmenu-calendar" href="/calendar"><%=StringUtils.text("menu.calendar.2", session)%></a></li>
			<li><a id="shmenu-olympics" href="/olympics"><%=StringUtils.text("menu.olympics.2", session)%></a></li>
			<li><a id="shmenu-usleagues" href="/usleagues"><%=StringUtils.text("menu.usleagues.2", session)%></a></li>
			<li><a id="shmenu-search" href="/search"><%=StringUtils.text("menu.search.2", session)%></a></li>
		</ul>
	</div>
	<div id="flags">
		<a title="English" href="<%=request.getAttribute("urlEN")%>"><img alt="EN" src="/img/header/lang-en.png"/></a>&nbsp;
		<a title="Français" href="<%=request.getAttribute("urlFR")%>"><img alt="FR" src="/img/header/lang-fr.png"/></a>
	</div>
</div>

<script><!--
<%
	out.print("var VERSION=\"" + version + "\";");
	out.print("var IMG_URL=\"" + ImageUtils.getUrl() + "\";");
	out.print("var TX_OK=\"" + StringUtils.text("ok", session) + "\";");
	out.print("var TX_CANCEL=\"" + StringUtils.text("cancel", session) + "\";");
	out.print("var TX_ACTIONS_TAB=\"" + StringUtils.text("actions.currenttab", session) + "\";");
	out.print("var TX_RUN=\"" + StringUtils.text("button.run", session) + "\";");
	out.print("var TX_RESET=\"" + StringUtils.text("button.reset", session) + "\";");
	out.print("var TX_EXPORT=\"" + StringUtils.text("button.export", session) + "\";");
	out.print("var TX_LINK=\"" + StringUtils.text("button.link", session) + "\";");
	out.print("var TX_PRINT=\"" + StringUtils.text("button.print", session) + "\";");
	out.print("var TX_INFO=\"" + StringUtils.text("button.info", session) + "\";");
	out.print("var TX_CLOSE=\"" + StringUtils.text("button.close", session) + "\";");
	out.print("var TX_BLANK=\"" + StringUtils.text("blank", session) + "\";");
	out.print("var TX_CURRENTLY_SELECTED=\"" + StringUtils.text("currently.selected", session) + "\";");
	out.print("var TX_ALL=\"" + StringUtils.text("all", session) + "\";");
	out.print("var TX_CLICK_CHANGE=\"" + StringUtils.text("click.change", session) + "\";");
	out.print("var TX_EXPAND=\"" + StringUtils.text("expand", session) + "\";");
	out.print("var TX_COLLAPSE=\"" + StringUtils.text("collapse", session) + "\";");
	out.print("var TX_KB=\"" + StringUtils.text("kb", session) + "\";");
	out.print("var TX_SECONDS=\"" + StringUtils.text("seconds", session) + "\";");
	out.print("var TX_YEARS=\"" + StringUtils.text("years", session) + "\";");
	out.print("var TX_OLYMPIC_GAMES=\"" + StringUtils.text("olympic.games", session) + "\";");
	out.print("var TX_EVENTS=\"" + StringUtils.text("events", session) + "\";");
	out.print("var TX_COUNTRIES=\"" + StringUtils.text("countries", session) + "\";");
	out.print("var TX_TEAMS=\"" + StringUtils.text("teams", session) + "\";");
	out.print("var TX_CATEGORIES=\"" + StringUtils.text("categories", session) + "\";");
	out.print("var TX_SELECT=\"" + StringUtils.text("select", session) + "\";");
	out.print("var TX_SELECTION=\"" + StringUtils.text("selection", session) + "\";");
	out.print("var TX_LOADING=\"" + StringUtils.text("loading", session) + "\";");
	out.print("var TX_SEARCH=\"" + StringUtils.text("search.for", session) + "\";");
	out.print("var TX_SEARCH2=\"" + StringUtils.text("search.in", session) + " Sporthenon\";");
	out.print("var TX_MLOGIN=\"" + StringUtils.text("mandatory.login", session) + "\";");
	out.print("var TX_MPASSWORD=\"" + StringUtils.text("mandatory.password", session) + "\";");
	out.print("var TX_MCONFIRMPWD=\"" + StringUtils.text("mandatory.confirmpwd", session) + "\";");
	out.print("var TX_MEMAIL=\"" + StringUtils.text("mandatory.email", session) + "\";");
	out.print("var TX_MSPORTS=\"" + StringUtils.text("mandatory.sports", session) + "\";");
	out.print("var TX_PWDNOTMATCH=\"" + StringUtils.text("pwd.nomatch", session) + "\";");
	out.print("var TX_PREVIOUS=\"" + StringUtils.text("previous", session) + "\";");
	out.print("var TX_NEXT=\"" + StringUtils.text("next", session) + "\";");
	out.print("var TX_CLICK_DRAGDROP=\"" + StringUtils.text("click.drag.drop", session) + "\";");
	out.print("var TX_REMOVE=\"" + StringUtils.text("remove", session) + "\";");
	out.print("var TX_MODIF_WARNING=\"" + StringUtils.text("modif.warning", session) + "\";");
	out.print("var TX_CONFIRM=\"" + StringUtils.text("confirm", session) + "\";");
	out.print("var TX_ADDFAV=\"" + StringUtils.text("add.favorites", session) + "\";");
	out.print("var TX_DELFAV=\"" + StringUtils.text("delete.favorites", session) + "\";");
	out.print("var TX_NOFAV=\"" + StringUtils.text("no.favorite", session) + "\";");
	out.print("var TX_ERROR=\"" + StringUtils.text("error", session) + "\";");
	out.print("var TX_IMPORT_COMPLETE=\"" + StringUtils.text("import.complete", session) + "\";");
	out.print("var TX_TYPE=\"" + StringUtils.text("type", session) + "\";");
	out.print("var TX_RANK1=\"" + StringUtils.text("rank.1", session) + "\";");
	out.print("var TX_RESULT_SCORE=\"" + StringUtils.text("result.score", session) + "\";");
	out.print("var TX_RANK2=\"" + StringUtils.text("rank.2", session) + "\";");
	out.print("var TX_RESULT=\"" + StringUtils.text("entity.RS.1", session) + "\";");
	out.print("var TX_RANK3=\"" + StringUtils.text("rank.3", session) + "\";");
	out.print("var TX_RANK4=\"" + StringUtils.text("rank.4", session) + "\";");
	out.print("var TX_RANK5=\"" + StringUtils.text("rank.5", session) + "\";");
	out.print("var TX_DATE=\"" + StringUtils.text("date", session) + "\";");
	out.print("var TX_PLACE=\"" + StringUtils.text("place", session) + "\";");
	out.print("var TX_TIE=\"" + StringUtils.text("tie", session) + "\";");
	out.print("var TX_COMMENT=\"" + StringUtils.text("comment", session) + "\";");
	out.print("var TX_OPEN_DIALOG=\"" + StringUtils.text("open.dialog", session) + "\";");
	out.print("var TX_ENLARGE=\"" + StringUtils.text("enlarge", session) + "\";");
	out.print("var TX_ACCOUNT_CREATED=\"" + StringUtils.text("msg.registered", session) + "\";");
%>
--></script>

<!-- TOP BAR (MOBILE) -->
<div id="mobilebar" style="display:none;">
	<div>
		<a href="javascript:toggleMenuM();"><img alt="M" src="/img/header/menu_m.png?v=<%=version%>"/></a>
	</div>	
	<div id="title_m">
		<div><a href="/"><img alt="logo" src="/img/logo/icon_m.png?v=<%=version%>"/></a></div>
		<div><span>Sporthenon</span></div>
	</div>
	<div>
		<a href="javascript:toggleSearchM();"><img alt="S" src="/img/header/search_m.png?v=<%=version%>"/></a>
	</div>
</div>

<!-- TOP BAR -->
<div id="headertop">
	<div id="menutop">
		<div id="mthome"><a href="/"><%=StringUtils.text("menu.home", session)%></a></div>
		<div id="mtcontribute"><a href="/contribute"><%=StringUtils.text("menu.contribute", session)%></a></div>
		<div id="mtfavorites"><a href="javascript:showFavorites();"><%=StringUtils.text("menu.favorites", session)%></a><div id="favorites" style="display:none;"></div></div>
		<%if (m != null) {%>
		<div id="mtcbarea"><a href="/update/overview"><%=StringUtils.text("menu.cbarea", session)%></a></div>
		<div id="mtlogout"><a href="/LoginServlet?logout"><%=StringUtils.text("menu.logout", session)%></a> (<%=m.getLogin()%>)</div>
		<%} else {%>
		<div id="mtlogin"><a href="/login"><%=StringUtils.text("menu.login", session)%></a></div>
		<%}%>
	</div>
	<div id="searchpanel">
		<table class="noborder" style="border-spacing:0px;">
		<tr><td class="pattern" style="padding-bottom:3px;">
			<input type="text" class="text" name="dpattern" id="dpattern" value="<%=StringUtils.text("search.in", session)%> Sporthenon" onfocus="dpatternFocus();" onblur="dpatternBlur();"/>
		</td>
		</tr></table>
	</div>
</div>
<div id="ajaxsearch" class="ajaxsearch"></div>

<script><!--
new Ajax.Autocompleter(
	'dpattern',
	'ajaxsearch',
	'/search/ajax/1',
	{ paramName: 'value', minChars: 2, frequency: 0.05, updateElement: directSearch}
);
Event.observe($('dpattern'), 'keyup', directSearch);

// Load favorites
var tfav = getCookie('shfav').split('|');
var tfavHTML = [];
var nf = 0;
for (var i = 0 ; i < tfav.length ; i++) {
	var t = tfav[i].split(':');
	if (t && t.length > 1) {
		tfavHTML.push('<li id="fav-' + i + '"><a href="' + t[0] + '">' + t[1] + '</a> <img alt="del" title="' + TX_REMOVE + '" src="/img/delete.gif" style="cursor:pointer;" onclick="deleteFavClick(' + i + ');"/></li>');
		nf++;
	}
}
if (tfavHTML.length == 0) {
	tfavHTML.push('<li style="list-style:none;"><b>' + TX_NOFAV + '</b></li>');
	nf = 1;
}
tfavHTML.push('<a href="javascript:hideFavorites();" style="padding-top:5px;float:right;">' + TX_CANCEL + '</a>');
$('favorites').style.height = 'auto';
$('favorites').update(tfavHTML.join(''));

var lang = '<%=lang%>';
--></script>

<div id="content">