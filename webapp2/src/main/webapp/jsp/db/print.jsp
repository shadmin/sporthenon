<%@ page language="java"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<link rel="stylesheet" type="text/css" href="/css/sh.css?${version}"/>
	<link rel="stylesheet" type="text/css" href="/css/render.css?${version}"/>
	<script src="/js/prototype.js?${version}"></script>
	<script src="/js/includes.js?${version}"></script>
	<script src="/js/sh.js?${version}"></script>
</head>
<body class="print">
<div id="content">
<div class="render">${html}</div>
<jsp:include page="/jsp/common/footer.jsp"/>
<script><!--
var title = $$('.title')[0].innerHTML + ' | Sporthenon';
if (title) {
	document.title = replaceAll(title, '&nbsp;', ' ');
}
if ('<%=request.getParameter("print")%>' != 'null') {
	['#content #topbar', '#summary', '#summary2'].each(function(el) {
		const el_ = $$(el);
		if (el_ != null && el_.length > 0) {
			el_[0].hide();
		}
	});
	window.print();
}
--></script>