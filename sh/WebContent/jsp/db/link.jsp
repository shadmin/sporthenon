<%@ page language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/sh.css?${version}" />
	<link rel="stylesheet" type="text/css" href="css/render.css?${version}" />
	<link rel="shortcut icon" type="image/x-icon" href="img/iconfav.ico?v=6"/>
	<script type="text/javascript" src="js/prototype.js?${version}"></script>
	<script type="text/javascript" src="js/includes.js?${version}"></script>
	<script type="text/javascript" src="js/sh.js?${version}"></script>
</head>
<body class="link">
<div id="content">
<div class="tc">${html}</div>
<jsp:include page="/jsp/common/footer.jsp" />
<script type="text/javascript">
var title = $$('.shorttitle')[0].innerHTML + ' | SPORTHENON';
if (title) {
	document.title = replaceAll(title, '&nbsp;', ' ');
}
$$('.rendertip').each(function(el) {
	new Control.Window($(document.body).down('[href=#' + el.id + ']'),{  
		position: 'relative', hover: true, offsetLeft: 20, offsetTop: 0, className: 'tip'
	});
});
if ('<%=request.getParameter("print")%>' != 'null') {
	window.print();
}
</script>