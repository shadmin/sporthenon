/*============================
  ========== COMMON ========== 
  ============================*/
function fillPicklistXML(response) {
	var xml = (response != null ? response.responseXML : pl.outerHTML);
	if (!xml) {return;}
	var root = xml.documentElement;
	var picklistId = root.getAttribute('id');
	var picklist = $(picklistId);
	if (root.childNodes.length > 0) {
		var tHtml = new Array();
		var node = null;
		for (var i = 0 ; i < root.childNodes.length ; i++) {
			node = root.childNodes[i];
			tHtml.push('<option value="' + node.getAttribute('value') + '">' + node.getAttribute('text') + '</option>');
		}
		picklistId.split(',').each(function(pl) {
			if ($(pl)) {
				$(pl).update(tHtml.join(''));
				$(pl).removeClassName('disabled');
				$(pl).writeAttribute('disabled', null);
			}
		});
		updateTip(picklistId);
		updateSelectMult(picklistId);
	}
	else {
		picklist.update('');
		picklist.addClassName('disabled');
		picklist.writeAttribute('disabled', 'disabled');
		updateTip(picklistId, true);
	}
	if (picklist.onchange) {
		picklist.onchange();
	}
}
function fillPicklistArray(id, array) {
	var picklist = $(id);
	if (array.length > 0) {
		var tHtml = new Array();
		for (var i = 0 ; i < array.length ; i++) {
			tHtml.push('<option value="' + array[i].value + '">' + array[i].text + '</option>');
		}
		id.split(',').each(function(pl) {
			if ($(pl)) {
				$(pl).update(tHtml.join(''));
				$(pl).removeClassName('disabled');
				$(pl).writeAttribute('disabled', null);
			}
		});
		updateTip(id);
		updateSelectMult(id);
	}
	else {
		picklist.update('');
		picklist.addClassName('disabled');
		picklist.writeAttribute('disabled', 'disabled');
		updateTip(id, true);
	}
	if (picklist.onchange) {
		picklist.onchange();
	}
}
var hTips = new Hash();
function createTip(name, pl) {
	$(document.body).insert(new Element('div', {id: name}));
	new Control.Window($(document.body).down('[href=#' + name + ']'),{  
		position: 'relative',
		hover: true,
		offsetLeft: -5,
		offsetTop: 25,
		className: 'tip'
	});
	hTips.set(pl, name);
	updateTip(pl);
}
function updateTip(pl, empty) {
	if (hTips.size() == 0 || !hTips.get(pl)) {return;}
	var text = TX_CURRENTLY_SELECTED + ':';
	if (empty) {
		text = '';
	}
	else {
		var t = $(pl).value.split(',');
		if (t[0] == 0) {
			text += '<br/>[' + TX_ALL + ']';
		}
		else {
			var h = new Hash();
			$$('#' + pl + ' option').each(function(el) {
				h.set(el.value, el.text);
			});
			for (var i = 0 ; i < t.length && i < 10 ; i++) {
				text += '<br/>-&nbsp;' + h.get(t[i]);
			}
			text += (t.length > 10 ? '<br/>(+' + (t.length - 10) + ' more)' : '');
		}
		text += '<br/><br/><span class="bold">(' + TX_CLICK_CHANGE + ')</span>';
	}
	$(hTips.get(pl)).update(text);
}
function handleRender() {
	var tabId = null;
	if (tabs != null) {
		tabId = 't-' + tabcurrent;
		if ($(tabId)) {
			var title = $('title-' + tabcurrent);
			var stitle = $$('#' + tabId + ' .title')[0].innerHTML;
			title.update(stitle);
			t2 = currentTime();	
		}
	}
	else {
		tabId = 'content';
	}
	var info = $$('#' + tabId + ' .infostats')[0];
	$$('#' + tabId + ' .rendertip').each(function(el) {
		new Control.Window($(document.body).down('[href=#' + el.id + ']'),{
			position: 'relative', hover: true, offsetLeft: 20, offsetTop: 28, className: 'tip'
		});
	});
	var t = elapsedTime(t1, t2);
	if (info) {
		info.update(info.innerHTML.replace('#DTIME#', t));	
	}
	if ($('loadtime')) {
		$('loadtime').down('span').update(t);
		$('loadtime').show();
	}
}
function toggleContent(el) {
	if (el.tagName != 'IMG') {
		el = $(el).previous('img');
	}
	var isDisplayed = (el.src.indexOf('collapse.gif') != -1);
	var table = $(el).up('table');
	var row = table.down('.tby').down();
	while (row != null) {
		if (!isDisplayed) {
			row.show();
		}
		else {
			row.hide();
		}
		row = row.next();
	}
	if (table.down('thead')) {
		row = table.down('thead').down('tr', 1);
		if (row) {
			row[!isDisplayed ? 'show' : 'hide']();
		}
	}
	else {
		table.down('tr').show();
	}
	el.src = '/img/render/' + (isDisplayed ? 'expand.gif' : 'collapse.gif');
}
function info(s) {
	t1 = currentTime();
	if ($('tabcontrol')) {
		window.scrollTo(0, 0);
		addTab(TX_BLANK);
		var tab = initTab();
		new Ajax.Updater(tab, '/InfoRefServlet?p=' + s, {
			onComplete: handleRender,
			parameters: addOptions($H())
		});
	}
	else {
		location.href = '/results/' + s;
	}
}
function currentTime() {
	return new Date().getTime();
}
function initOptionsPanel() {
	var selOpt = new Control.SelectMultiple('optionsselect', 'optionspanel', {
		checkboxSelector: 'tr td input[type=checkbox]',
		nameSelector: 'tr td.name'
	});
	$('options').observe('click',function(event){
		$(this.select).style.visibility = 'hidden';
		new Effect.BlindDown(this.container,{ duration: 0.4 });
		Event.stop(event);
		return false;
	}.bindAsEventListener(selOpt));
	$$('#optionspanel .button a')[0].observe('click',function(event){
		$(this.select).style.visibility = 'visible';
		new Effect.BlindUp(this.container,{ duration: 0.4 });
		Event.stop(event);
		return false;
	}.bindAsEventListener(selOpt));
}
function addOptions(h) {
	$$('#optionspanel input, #optionspanel select').each(function(el) {
		h.set(el.id, (el.tagName == 'SELECT' ? el.value : (el.checked ? '1' : '0')));
	});
	return h;
}
function replaceAll(s1, s2, s3) {
	while (s1.indexOf(s2) != -1) {
		s1 = s1.replace(s2, s3);
	}
	return s1;
}
function moreItems(row, p) {
	var cell = $(row).up();
	cell.update('<img src="/img/db/loading.gif?6"/>');
	cell.style.backgroundColor = '#FFF';
	new Ajax.Request('/entity/more/' + p, {
		onSuccess: function(response){
			$(cell).up('tbody').insert(response.responseText);
			$(cell).up('tr').remove();
		},
		parameters: addOptions($H())
	});
}
function togglePlist(img, id) {
	if ($(id).style.display == 'none') {
		$(id).show();
		img.src = '/img/render/collapse.gif';
	}
	else {
		$(id).hide();
		img.src = '/img/render/expand.gif';
	}
}
function winrecMore(row) {
	var table = $(row).up('.winrec');
	var row_ = table.down('.hidden');
	while (row_ != null) {
		row_.removeClassName('hidden');
		row_ = row_.next();
	}
	$(row).remove();
}
var tCurrentSortedCol = [];
function sort(id, col, sortIndex) {
	var isAsc = $(col).hasClassName('asc');
	var t = [];
	var tIndex = [];
	var index = null;
	var cell = null;
	var i = 0;
	var s = null;
	var table = $("tb-" + id);
	var tr = table.down('tr');
	var trsf = table.down('.moreitems');
	while (tr != null) {
		cell = $(tr).down('.srt', sortIndex);
		if (cell) {
			index = (cell.id && cell.id != '' ? cell.id : cell.innerHTML);
			index = index.replace('-', '').replace(/(<\s*\/?\s*)\s*(\s*([^>]*)?\s*>)/gi, '');
			if (!isNaN(index)) {
				s = index + '';
				while (s.length < 10) {
					s = '0' + s;
				}
				index = s;
			}
			index += '-' + i++;
			tIndex.push(index);
			t[index] = tr.outerHTML;
		}
		tr = tr.next();
	}
	var tRow = [];
	tIndex.sort().each(function(el){
		tRow.push(t[el]);
	});
	table.update(!isAsc ? tRow.join('') : tRow.reverse().join(''));
	if (trsf != null) {
		table.insert(trsf);
	}
	if (tCurrentSortedCol[id] != null) {
		$(tCurrentSortedCol[id]).removeClassName('sorted').removeClassName('asc').removeClassName('desc');
	}
	if (isAsc) {
		$(col).removeClassName('asc').addClassName('desc');
	}
	else {
		$(col).removeClassName('desc').addClassName('asc');
	}
	$(col).addClassName('sorted');
	tCurrentSortedCol[id] = col;
}
var treeExpanded = false;
function toggleTreeExpand() {
	var img = $('treeiconimg');
	if (!treeExpanded) {
		$('treeview').removeClassName('collapsed').addClassName('expanded');
		img.src = img.src.replace('expand', 'collapse');
		img.alt = '[-]&nbsp;' + TX_COLLAPSE;
		$('treeview').style.border = '1px solid #000';
	}
	else {
		$('treeview').removeClassName('expanded').addClassName('collapsed');
		img.src = img.src.replace('collapse', 'expand');
		img.alt = '[+]&nbsp;' + TX_EXPAND;
		$('treeview').style.border = '1px solid #DDD';
	}
	$('treeicontxt').update(img.alt);
	//img.title = img.alt;
	treeExpanded = !treeExpanded;
}
function moreImg(c) {
	$$('.' + c).each(function(el) {
		el.show();
	});
	$(c + '-link').hide();
}
/*============================
  ========== UTILS ========== 
  ============================*/
var t1 = null;
var t2 = null;
function elapsedTime(t1_, t2_) {
	return ((t2_ - t1_) / 1000);
}
function backTop() {
	window.scrollTo(0, 0);
}
/*=================================
  ========== TAB CONTROL ========== 
  =================================*/
var tabs = null;
var tabcurrent = 0;
var tabcount = 0;
function initTabControl() {
	addTab(TX_BLANK);
	
	tabbar.insert('<li title="Open New Tab" id="link-add"><a href="javascript:void(0);"></a></li>');
	var link = $$('#link-add a')[0];
	link.observe('click', function(){addTab(TX_BLANK);});
}
function getCloseImg(idx) {
	var img = new Element('img', {id: 'close-' + idx, src: '/img/component/tabcontrol/close.gif'});
	img.observe('mouseover', overCloseImg)
	   .observe('mouseout', outCloseImg)
	   .observe('mousedown', clickCloseImg);
	return img;
}
function overCloseImg() {
	this.src = this.src.replace('close.gif', 'close-over.gif');
}
function outCloseImg() {
	this.src = this.src.replace('close-over.gif', 'close.gif');
}
function clickCloseImg(id_) {
	var idx = (this.id == 'close' ? tabs.activeContainer.id.replace('t-', '') : this.id.replace('close-', ''));
	$('link-' + idx).remove();
	tabs.removeTab('t-' + idx);
	if (tabs.activeContainer.id == 't-' + idx) {
		var i = tabcurrent + 1;
		while (!$('t-' + i--) && i > 0);
		tabs.setActiveTab(i > 0 ? 't-' + (i + 1) : 0);
	}
	if ($$('#tabbar li').length <= 2) {
		addTab(TX_BLANK);
	}
}
function addTab(title) {
	tabcurrent = ++tabcount;
	title = '<span id="title-' + tabcurrent + '">' + title + '</span>';
	tabcontrol.insert('<div class="tc" id="t-' + tabcurrent + '"><br/><br/><br/></div>');
	tabbar.insert('<li id="link-' + tabcurrent + '"><a href="#t-' + tabcurrent + '">' + title + '</a></li>');
	var link = $$('#link-' + tabcurrent + ' a')[0];
	link.insert(getCloseImg(tabcurrent));
	$('link-' + tabcurrent).observe('mouseup', function(){tabcurrent = this.id.replace('link-', '');});
	tabs.addTab(link);
	tabs.last();
	tabbar.insert($('link-add'));
	return $('t-' + tabcurrent);
}
function initTab() {
	var tab = $(tabs.activeContainer.id);
	$('title-' + tab.id.replace('t-', '')).update(TX_LOADING);
	return tab.update('<div class="loading"></div>');
}
function closeTabs() {
	$('tabbar').update('<li/>');
	$$('#tabcontrol .tc').each(function(el) {
		el.remove();
	});
	tabcurrent = 0;
	tabcount = 0;
	initTabControl();
}
function closeDialog(dlg) {
	dlg.close();
	$('header').setStyle({ opacity: 1.0 });
	$('content').setStyle({ opacity: 1.0 });
}
var dError = null;
var dLink = null;
var dInfo = null;
var dPersonList = null;
var dFind = null;
var dQuestion = null;
var dHelp = null;
function share(type) {
	var url = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .url')[0].innerHTML;
	var langParam = '?lang=' + lang;
	if (type == 'fb') {
		url = 'https://www.facebook.com/sharer/sharer.php?u=' + encodeURIComponent(url + langParam);
	}
	else if (type == 'tw') {
		url = 'https://twitter.com/share?url=' + encodeURIComponent(url + langParam);
	}
	else if (type == 'gp') {
		url = 'https://plus.google.com/share?url=' + encodeURIComponent(url + langParam);
	}
	else if (type == 'bg') {
		url = 'https://www.blogger.com/blog-this.g?u=' + encodeURIComponent(url + langParam) + '&n=' + escape(document.title);
	}
	else if (type == 'tm') {
		url = 'http://tumblr.com/share?&u=' + encodeURIComponent(url + langParam);
	}
	$('shareopt').hide();
	window.open(url, '_blank');
}
function displayShare() {
	$('shareopt').show();
}
function exportPage(type) {
	var url = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .url')[0].innerHTML;
	if (url) {
		location.href = url + '?export=' + type;
		$('exportopt').hide();
	}
}
function displayExport() {
	$('exportopt').show();
}
function displayErrorReport() {
	var url = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .url')[0].innerHTML;
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	$('errlinkurl').value = url;
	$('errlinktext').value = '';
	dError.open();
	$('errlinktext').focus();
}
function displayLink() {
	var url = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .url')[0].innerHTML;
	if (dLink && url) {
		$('header').setStyle({ opacity: 0.4 });
		$('content').setStyle({ opacity: 0.4 });
		dLink.open();
		$('linktxt').value = url;
		$('linktxt').select();
		$('linktxt').focus();
	}
}
function displayInfo() {
	var url = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .url')[0].innerHTML;
	var info = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .infostats')[0].innerHTML;
	var tInfo = info.split('|');
	var t = $$('#d-info td');
	t[0].update('<a href="' + url + '" target="_blank">' + url + '</a>');
	t[1].update(tInfo[0] + '&nbsp;' + TX_KB);
	t[2].update(tInfo[1] + '&nbsp;' + TX_SECONDS);
	t[3].update(tInfo[2]);
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	dInfo.open();
}
function printCurrentTab() {
	var url = $$('#' + (tabs != null ? tabs.activeContainer.id : 'content') + ' .url')[0].innerHTML;
	if (url) {
		window.open(url + '?print', '_blank');
	}
}
function saveError() {
	var h = $H({url: $F('errlinkurl'), text: $F('errlinktext')});
	new Ajax.Request('/IndexServlet?error=1', {
		onSuccess: function(response){
		},
		parameters: h
	});
	closeDialog(dError);
}
/*============================
  ========== SLIDER ========== 
  ============================*/
var hSliders = new Hash();
function sliderLinkOver() {
}
function sliderLinkOut() {
}
function handleSliderArr(id) {
}
function createSlider(id, w, h, c) {
	var container = $$('#' + id + ' .container')[0];
	var slides = $$('#' + id + ' .slide');
	var links = $$('#' + id + ' .slider-control');
	container.down().style.width = '5000px';
	container.setStyle({width: w + 'px', height: h + 'px'});
	slides.each(function(slide) {
		slide.setStyle({width: w + 'px', height: h + 'px'});
	});
	links.each(function(link) {
		Event.observe(link, 'mouseover', sliderLinkOver);
		Event.observe(link, 'mouseout', sliderLinkOut);
	});
	hSliders.set(id, new Carousel(container, slides, links, {duration: 0.4, circular: (c ? true : false), afterMove: (!c ? function(){handleSliderArr(id);} : null)}));
	if (!c) {
		handleSliderArr(id);
	}
	return hSliders.get(id);
}
/*=====================================
  ========== SELECT-MULTIPLE ========== 
  =====================================*/
var hSelMult = $H();
function initSelectMult(id, s, w, o) {
	$(id).down(4).writeAttribute('id', id.replace('sm-', ''));
	var container = $$('#' + id + ' .optcontainer')[0];
	var openLink = $$('#' + id + ' a')[0];
	var btns = $$('#' + id + ' input.button');
	var btnOk = btns[1];
	var btnCancel = btns[0];
	var label = $$('#' + id + ' .applyall label')[0];
	var input = $$('#' + id + ' .applyall input')[0];
	id = id.replace('sm-', '');
	container.writeAttribute('id', id + '-options');
	label.writeAttribute('for', id + '-applyall');
	input.writeAttribute('id', id + '-applyall');
	container.down().innerHTML = TX_SELECT + ': ' + s;
	$(id).setStyle({width: w + 'px'});
	container.setStyle({width: (w + (o ? o : 20)) + 'px'});
	openLink.writeAttribute('href', '#tip-' + id);
	createTip('tip-' + id, id);

	var selMult = new Control.SelectMultiple(id, id + '-options', {
		checkboxSelector: '.scroll tr td input[type=checkbox]',
		nameSelector: '.scroll tr td.name',
		afterChange: function(){ if (this.setSelectedRows) {this.setSelectedRows();} }
	});
	openLink.observe('click', function(event){
		if ($(this.select).disabled) {return;}
		$(this.select).style.visibility = 'hidden';
		this.set();
		new Effect.BlindDown(this.container,{ duration: 0.4 });
		Event.stop(event);
		var isAll = ($(this.select).value == 0);
		$(id + '-applyall').checked = isAll;
		if (isAll) {
			var n = 0;
			this.checkboxes.each(function(el){
				el.checked = true;
				if (++n == this.checkboxes.length) {
					this.checkboxOnClick(el);
				}
			}.bind(selMult));
		}
		return false;
	}.bindAsEventListener(selMult));
	btnOk.observe('click', function(event){
		$(this.select).style.visibility = 'visible';
		if ($(id).onchange) {
			$(id).onchange();
		}
		new Effect.BlindUp(this.container,{ duration: 0.4 });
		return false;
	}.bindAsEventListener(selMult));
	btnCancel.observe('click', function(event){
		$(this.select).style.visibility = 'visible';
		this.reset();
		new Effect.BlindUp(this.container,{ duration: 0.4 });
		Event.stop(event);
		return false;
	}.bindAsEventListener(selMult));
	$(input).observe('click',function(event){
		var toCheck = $(id + '-applyall').checked;
		var n = 0;
		this.checkboxes.each(function(el){
			el.checked = toCheck;
			if (++n == this.checkboxes.length) {
				this.checkboxOnClick(el);
			}
		}.bind(selMult));
		return false;
	}.bindAsEventListener(selMult));
	hSelMult.set(id, selMult);
}
function updateSelectMult(id) {
	if (hSelMult.size() == 0 || !hSelMult.get(id)) {return;}
	var selMult = hSelMult.get(id);
	if (selMult) {
		var tRows = [''];
		var n = 0;
		$$('#' + id + ' option').each(function(el) {
			if (el.value > 0) {
				tRows.push('<tr><td id="' + id + '-' + n++ + '" class="name">' + el.text + '</td><td><input type="checkbox" value="' + el.value + '"/></td></tr>');
			}
		});
		$$('#' + id + '-options .scroll table')[0].update(tRows.join(''));
		$$('#' + id + '-options .scroll tr').each(function(el){
			el.down().observe('click', selMultClick);
			el.observe('mouseover', function(){this.addClassName('over');});
			el.observe('mouseout', function(){this.removeClassName('over');});
		});
		selMult.initialize(selMult.select, selMult.container, selMult.options);
		selMult.setSelectedRows = function(){
			this.checkboxes.each(function(checkbox){
				var tr = $(checkbox.parentNode.parentNode);
				tr.removeClassName('selected');
				if(checkbox.checked)
					tr.addClassName('selected');
			});
		}.bind(selMult);
		selMult.checkboxes.each(function(checkbox){
			$(checkbox).observe('click', selMult.setSelectedRows);
		});
		selMult.setSelectedRows();
	}
}
function selMultClick() {
	var id = this.id;
	var sel = hSelMult.get(id.replace(/-\d+/, ''));
	var c = sel.checkboxes[id.replace(/[^\d]+/, '').replace('2-', '')];
	c.checked = (c.checked ? false : true);
	sel.checkboxOnClick(c);
}
/*==========================
  ========== HOME ========== 
  ==========================*/
function overTopic(txt) {
	$('details').update(txt);
	$('details').show();
}
function moreSports(index1, index2) {
	for (var i = index1 ; i <= index2 ; i++) {
		$('sport-' + i).show();
	}
	$$('#sports .otherimglink').each(function(el){
		$(el).hide();
	});
	$('more-' + index1 + '-' + index2).show();
}
function initSliderHome(html) {
	$$('#sports .content')[0].update(html);
	createSlider('sports', 872, 120, true);
}
function moreLastUpdates(row, p) {
	var cell = $(row).up();
	cell.update('<img src="/img/db/loading.gif?6"/>');
	cell.style.backgroundColor = '#FFF';
	new Ajax.Request('/IndexServlet?p=' + p + '&lastupdates&t=' + currentTime(), {
		onSuccess: function(response){
			$(cell).up('tbody').insert(response.responseText);
			$(cell).up('tr').remove();
		},
		parameters: addOptions($H())
	});
}
var cindex = 0;
var cmax = 2;
var cdata = [];
var clabel = [];
var ccolor = [['Gradient(#94cff5:#179ef5)'], ['Gradient(#f8cb98:#f88f18)'], ['Gradient(#94cff5:#179ef5)', 'Gradient(#f8cb98:#f88f18)', 'red', 'blue']];
function loadReport(cdata_, clabel_, ccolor_) {
	$('chart').update('<canvas id="cvs" width="720" height="400"></canvas>');
	if (cindex == 0 || cindex == 1) {
		var bar = new RGraph.HBar('cvs', cdata_)
		.Set('gutter.top', 0)
		.Set('gutter.left', 120)
		.Set('labels', clabel_)
		.Set('colors', ccolor_)
		.Set('text.font', 'Verdana')
		.Set('text.size', 8)
		.Set('vmargin', 5)
		.Draw();
	}
	else if (cindex == 2) {
		var pie = new RGraph.Pie('cvs', cdata_)
		.Set('labels', clabel_)
		.Set('text.font', 'Verdana')
		.Set('text.size', 8)
		.Set('gutter.top', 40)
		.Set('gutter.bottom', 40)
		.Set('vmargin', 5)
		.Set('exploded', 8)
		.Set('strokestyle', '#666')
		.Draw();
	}
}
function changeReport(idx) {
	cindex += idx;
	if (cindex > cmax) {
		cindex = 0;
	}
	if (cindex < 0) {
		cindex = cmax;
	}
	$('chart').update('<img src="/img/db/loading.gif?6"/>');
	$('ctitle').update(ctitle[cindex]);
	new Ajax.Request('/IndexServlet?report=' + cindex + '&t=' + currentTime(), {
		onSuccess: function(response){
			var t = response.responseText.split('~');
			clabel = t[0].split('|');
			cdata = t[1].split('|');
			for (var i = 0 ; i < cdata.length ; i++) {
				cdata[i] = parseInt(cdata[i]);
			}
			loadReport(cdata, clabel, ccolor[cindex]);
		}
	});
}
/*============================
  ========== RESULTS ========= 
  ============================*/
function initSliderRes(s) {
	var sliderContent = [];
	var t = $$('#pl-' + s + ' option');
	t.each(function(el) {
		sliderContent.push('<div id="' + s + '-' + el.value + '" class="slide"><img alt="" title="' + el.text + '" src=\'' + hSportImg[el.value] + '\'/></div>');
	});
	sliderContent.push('<div id="' + s + '-' + t[0].value + '" class="slide">' + hSportImg[t[0].value] + '</div>');
	$$('#slider-' + s + ' .content')[0].update(sliderContent.join(''));
	var sl = hSliders.get('slider-' + s);
	if (!sl) {
		sl = createSlider('slider-' + s, 102, 102, true);
		sl.options.afterMove = function() {
			var currentId = sl.current.id;
			$('pl-' + s).setValue(currentId.replace(s + '-', ''));
			if (s == 'sp') {
				changeSport(true);
			}
		};
	}
	else {
		var slides = $$('#slider-' + s + ' .slide');
		sl.initialize(sl.scroller, slides, sl.controls, sl.options);
	}
	sl.moveTo($(s + '-' + t[0].value));
}
function changeSport(srcsl) {
	if (!srcsl) {
		hSliders.get('slider-sp').moveTo($('sp-' + $F('pl-sp')));
	}
	else {
		getPicklist('pl-cp');
	}
}
function getPicklist(picklistId) {
	if (picklistId != 'pl-yr') {
		var spIndex = $('pl-sp').options.selectedIndex;
		var cpIndex = $('pl-cp').options.selectedIndex;
		var evIndex = $('pl-ev').options.selectedIndex;
		var seIndex = $('pl-se').options.selectedIndex;
		var t = null;
		if (picklistId == 'pl-sp') {
			t = treeItems[0];
		}
		else if (picklistId == 'pl-cp') {
			t = treeItems[0][spIndex + 2];
		}
		else if (picklistId == 'pl-ev') {
			t = treeItems[0][spIndex + 2][cpIndex + 2];
		}
		else if (picklistId == 'pl-se') {
			t = treeItems[0][spIndex + 2][cpIndex + 2][evIndex + 2];
		}
		else if (picklistId == 'pl-se2') {
			t = treeItems[0][spIndex + 2][cpIndex + 2][evIndex + 2][seIndex + 2];
		}
		var array = new Array();
		var val = null;
		var txt = null;
		for (var i = 2 ; i < t.length ; i++) {
			val = t[i][1];
			if (val) {
				if (val.indexOf('_') != -1) {
					val = val.substring(val.lastIndexOf('_') + 1);
				}
				txt = t[i][0];
				array.push({value: val, text: txt});	
			}
		}
		fillPicklistArray(picklistId, array);
	}
	else {
		var h = $H({sp: $F('pl-sp'), cp: $F('pl-cp'), ev: $F('pl-ev'), se: $F('pl-se'), se2: $F('pl-se2')});
		new Ajax.Request('/ResultServlet?' + picklistId, {
			onSuccess: function(response) {
				fillPicklistXML(response);
			},
			parameters: h
		});
	}
}
function runResults(tleaf) {
	t1 = currentTime();
	var tab = initTab();
	var h = null;
	if (tleaf && tleaf instanceof Array) { // Treeview
		var sp_ = tleaf[0];
		var cp_ = tleaf[1];
		var ev_ = (tleaf.length > 2 ? tleaf[2] : null);
		var se_ = (tleaf.length > 3 ? tleaf[3] : null);
		var se2_ = (tleaf.length > 4 ? tleaf[4] : null);
		h = $H({sp: sp_, cp: cp_, ev: ev_, se: se_, se2: se2_, yr: 0});
	}
	else { // Picklist
		h = $H({sp: $F('pl-sp'), cp: $F('pl-cp'), ev: $F('pl-ev'), se: $F('pl-se'), se2: $F('pl-se2'), yr: $F('pl-yr')});
	}
	addOptions(h);
	new Ajax.Updater(tab, '/ResultServlet?run', {
		parameters: h,
		onComplete: handleRender
	});
}
function resetResults() {
	closeTabs();
	$('pl-sp').selectedIndex = 0;
	changeSport();
}
function treeLeafClick(anchor, value) {
	if (treeExpanded) {
		toggleTreeExpand();
	}
	if (value.indexOf('link-') == 0) {
		info(value.substring(5));
		return;
	}
	var t = value.split('_');
	if (location.href.indexOf('/update/results') != -1) {
		tValues['sp'] = t[0];
		tValues['cp'] = t[1];
		tValues['ev'] = t[2];
		tValues['se'] = (t.length > 3 ? t[3] : '');
		tValues['se2'] = (t.length > 4 ? t[4] : '');
		tValues['yr'] = '10000';
		loadResult();
		return;
	}
	runResults(t);
	$('pl-sp').value = t[0];
	$('pl-sp').onchange();
	setTimeout(function(){
		$('pl-cp').value = t[1];
		$('pl-cp').onchange();
		setTimeout(function(){
			$('pl-ev').value = t[2];
			$('pl-ev').onchange();
			if (t.length > 3 && t[3] != '') {
				setTimeout(function(){
					$('pl-se').value = t[3];
					$('pl-se').onchange();
					if (t.length > 4 && t[4] != '') {
						setTimeout(function(){
							$('pl-se2').value = t[4];
						}, 600);
					}
				}, 600);
			}
		}, 600);
	}, 600);
}
/*==============================
  ========== OLYMPICS ========== 
  ==============================*/
function initOlympics(picklistId) {
	var type = (picklistId.indexOf('summer') == 0 ? 'summer' : 'winter');
	var url = '/OlympicsServlet?type=' + type;
	new Ajax.Request(url, {
		onSuccess: fillPicklistXML,
		onFailure: function(response) {}
	});
}
function changeModeOL() {
	var isSummer = $F('olt1');
	if (isSummer) {
		$('summerfs').style.display = 'inline-block';
		$('winterfs').hide();
		changeOlympics('summer-pl-ol');
	}
	else {
		$('summerfs').hide();
		$('winterfs').style.display = 'inline-block';
		changeOlympics('winter-pl-ol');
	}
}
function changeOlympics(id) {
	var isSummer = (id.indexOf('summer') == 0);
	var code = (isSummer ? 'summer' : 'winter');
	var h = $H({ol: $F(code + '-pl-ol')});
	h.set('type', code);
	new Ajax.Request('/OlympicsServlet?tree=1', {
		onSuccess: function(response){
			eval(response.responseText);
			getPicklistOL(code + '-pl-sp');
			updateSliderSp(code);
		},
		parameters: h
	});
	new Ajax.Request('/OlympicsServlet?pl-cn', {
		onSuccess: function(response){
			fillPicklistXML(response);
		},
		parameters: h
	});
	updateSliderOl(code);
}
function changeSportOL(obj, code, srcsl) {
	if (!srcsl) {
		var sl = hSliders.get('slider-' + code + '-sp');
		if (sl) {
			var slide = $(code + '-sp-' + $F(code + '-pl-sp'));
			if (slide) {
				sl.moveTo(slide);
			}
		}
	}
	else {
		getPicklistOL(code + '-pl-ev', true);
	}
}
function getPicklistOL(picklistId) {
	var isSummer = (picklistId.indexOf('summer') == 0);
	var code = (isSummer ? 'summer' : 'winter');
	var spIndex = $(code + '-pl-sp').options.selectedIndex;
	var evIndex = $(code + '-pl-ev').options.selectedIndex;
	var seIndex = $(code + '-pl-se').options.selectedIndex;
	var t = null;
	if (picklistId == code + '-pl-sp') {
		t = treeItems[0];
	}
	else if (picklistId == code + '-pl-ev') {
		t = treeItems[0][spIndex + 2][2];
	}
	else if (picklistId == code + '-pl-se') {
		t = treeItems[0][spIndex + 2][2][evIndex + 2];
	}
	else if (picklistId == code + '-pl-se2') {
		t = treeItems[0][spIndex + 2][2][evIndex + 2][seIndex + 2];
	}
	var array = [];
	var val = null;
	var txt = null;
	if (t != null) {
		for (var i = 2 ; i < t.length ; i++) {
			val = t[i][1];
			if (val) {
				if (val.indexOf('_') != -1) {
					val = val.substring(val.lastIndexOf('_') + 1);
				}
				txt = t[i][0];
				array.push({value: val, text: txt});	
			}
		}	
	}
	if (picklistId != code + '-pl-sp' && array.length > 1) {
		array.push({value: 0, text: '[' + TX_ALL + ']'});
	}
	fillPicklistArray(picklistId, array);
}
function updateSliderOl(code) {
	var slider = $$('#slider-' + code + '-ol .content')[0];
	var sliderContent = ['<div id="' + code + '-sl1" class="slide">'];
	var n = -1;
	var currentOl = $F(code + '-pl-ol');
	$$('#' + code + '-pl-ol option').each(function(el) {
		if (el.text.indexOf('---') != 0 && el.text.indexOf('[') != 0) {
			var pattern = new RegExp('(^|.*,)' + el.value + '(,.*|$)');
			var text = null;
			if (currentOl == '0' || pattern.match(currentOl)) {
				text = el.text;
				text = text.substring(text.indexOf(' - ') + 3) + ' ' + text.substring(0, 4);
				sliderContent.push(++n > 0 && n % 8 == 0 ? '</div><div class="slide">' : '');
				sliderContent.push('<img alt="' + text + '" title="' + text + '" src="' + hOlympicsImg[el.value] + '" style="cursor:pointer;" onclick="slideOlClick(\'' + code + '\', \'' + el.value + '\');"/>');
			}
		}
	});
	sliderContent.push('</div>');
	slider.update(sliderContent.join(''));
	var sl = hSliders.get('slider-' + code + '-ol');
	var slides = $$('#slider-' + code + '-ol .slide');
	sl.initialize(sl.scroller, slides, sl.controls, sl.options);
	sl.moveTo($(code + '-sl1'));
	handleSliderArr('slider-' + code + '-ol');
}
function updateSliderSp(code) {
	var slider = $$('#slider-' + code + '-sp .content')[0];
	var sliderContent = [];
	$$('#' + code + '-pl-sp option').each(function(el) {
		sliderContent.push('<div id="' + code + '-sp-' + el.value + '" class="slide">');
		sliderContent.push('<img alt="' + el.text + '" title="' + el.text + '" src="' + hSportImg[el.value] + '"/></div>');
	});
	sliderContent.push('</div>');
	slider.update(sliderContent.join(''));
	var sl = hSliders.get('slider-' + code + '-sp');
	var slides = $$('#slider-' + code + '-sp .slide');
	sl.options.afterMove = function() {
		$(code + '-pl-sp').setValue(sl.current.id.replace(code + '-sp-', ''));
		changeSportOL(null, code, true);
		handleSliderArr('slider-' + code + '-sp');
	};
	sl.initialize(sl.scroller, slides, sl.controls, sl.options);
	sl.first();
	handleSliderArr('slider-' + code + '-sp');
}
function slideOlClick(code, value) {
	var list = $(code + '-pl-ol');
	list.value = value;
	changeOlympics(list.id);
}
function runOlympics() {
	t1 = currentTime();
	var tab = initTab();
	var ind = ($('olt1').checked ? $('sq1').checked : $('wq1').checked);
	var code = ($('olt1').checked ? 'summer' : 'winter');
	var h = $H({ol: $F(code + '-pl-ol')});
	if (ind) {
		h.set('sp', $F(code + '-pl-sp'));
		h.set('ev', $F(code + '-pl-ev'));
		h.set('se', $F(code + '-pl-se'));
		h.set('se2', $F(code + '-pl-se2'));
	}
	else {
		h.set('cn', $F(code + '-pl-cn'));
	}
	addOptions(h);
	var url = '/OlympicsServlet?run&type=' + (ind ? 'ind' : 'cnt');
	new Ajax.Updater(tab, url, {
		parameters: h,
		onComplete: handleRender
	});
}
function resetOlympics() {
	closeTabs();
	var ind = ($('olympics-individual') ? true : false);
	$('olt1').checked = true;
	changeModeOL();
	$('summer-pl-ol').selectedIndex = 0;
	$('winter-pl-ol').selectedIndex = 0;
	changeOlympics('summer-pl-ol', ind);
	changeOlympics('winter-pl-ol', ind);
}
/*============================
  ========== US LEAGUES ====== 
  ============================*/
var currentLeague = null;
var currentUtype = null;
function initSliderUS() {
	var sliderContent = [];
	sliderContent.push('<div id="sl-nfl" class="slide" title="NFL" style="background-image:url(/img/db/nfl.gif)"></div>');
	sliderContent.push('<div id="sl-nba" class="slide" style="background-image:url(/img/db/nba.gif)"></div>');
	sliderContent.push('<div id="sl-nhl" class="slide" style="background-image:url(/img/db/nhl.gif)"></div>');
	sliderContent.push('<div id="sl-mlb" class="slide" style="background-image:url(/img/db/mlb.gif)"></div>');
	sliderContent.push('<div id="sl-nfl" class="slide" title="NFL" style="background-image:url(/img/db/nfl.gif)"></div>');
	$$('#slider-league-img .content')[0].update(sliderContent.join(''));
	createSlider('slider-league-img', 100, 100, true);
	hSliders.get('slider-league-img').options.afterMove = function() {
		var currentId = hSliders.get('slider-league-img').current.id;
		changeLeague(currentId.replace('sl-', ''), true);
	};
}
function changeLeague(id, srcsl) {
	currentLeague = id;
	if (!srcsl) {
		$('nfl').removeClassName('selected');$('nba').removeClassName('selected');$('nhl').removeClassName('selected');$('mlb').removeClassName('selected');$(id).addClassName('selected');
		hSliders.get('slider-league-img').moveTo($('sl-' + id));
	}
	else {
		$('nfl').removeClassName('selected');$('nba').removeClassName('selected');$('nhl').removeClassName('selected');$('mlb').removeClassName('selected');$(id).addClassName('selected');
		var league = (id == 'nfl' ? 1 : (id == 'nba' ? 2 : (id == 'nhl' ? 3 : 4)));
		var url = '/USLeaguesServlet?league=' + league;
		$('pl-hof-yr').update(tHofYr[league]); updateTip('pl-hof-yr'); updateSelectMult('pl-hof-yr');
		$('pl-championships-yr').update(tChampYr[league]); updateTip('pl-championships-yr'); updateSelectMult('pl-championships-yr');
		$('pl-retnum-tm').update(tTm[league]); updateTip('pl-retnum-tm'); updateSelectMult('pl-retnum-tm');
		$('pl-teamstadiums-tm').update(tTm[league]); updateTip('pl-teamstadiums-tm'); updateSelectMult('pl-teamstadiums-tm');
		$('pl-winloss-tm').update(tTm[league]); updateTip('pl-winloss-tm'); updateSelectMult('pl-winloss-tm');
		$('pl-records-se').update(tRcSe[league]); updateTip('pl-records-se'); updateSelectMult('pl-records-se');
		$('hof-position').value = ''; updateTip('hof-position'); updateSelectMult('hof-position');
		$('hof-postip').title = tPos[league]; updateTip('hof-postip'); updateSelectMult('hof-postip');
		$('retnum-number').value = '';
		var tType2 = ['Alltime/Career', 'Season', 'Series', 'Game'];
		var tAll = new Array();
		for (var i = 0 ; i < tType2.length ; i++) {
			if ((league == 1 || league == 4) && tType2[i] == 'Series') {}
			else {
				tAll.push('\'' + tType2[i] + '\'');
				tType2[i] = '<option value="' +i + '">' + tType2[i] + '</option>';
			}
		}
		tType2.push('<option value="-">[All]</option>');
		$('pl-records-tp2').update(tType2.join(''));
	}
}
function changeModeUS(id) {
	id = (id != null ? id : 'championships');
	currentUtype = id;
	['championships', 'records', 'winloss', 'hof', 'retnum', 'teamstadiums'].each(function(id_) {
		$(id_).removeClassName('selected');	
		$('f-' + id_).hide();
	});
	$(id).addClassName('selected');
	$('f-' + id).show();
}
function runUSLeagues() {
	t1 = currentTime();
	var tab = initTab();
	var league = (currentLeague == 'nfl' ? 1 : (currentLeague == 'nba' ? 2 : (currentLeague == 'nhl' ? 3 : 4)));
	var h = new Hash();
	h.set('league', league);
	h.set('type', currentUtype);
	h.set('tm', $('pl-' + currentUtype + '-tm') ? $F('pl-' + currentUtype + '-tm') : null);
	h.set('yr', $('pl-' + currentUtype + '-yr') ? $F('pl-' + currentUtype + '-yr') : null);
	h.set('se', $('pl-' + currentUtype + '-se') ? $F('pl-' + currentUtype + '-se') : null);
	h.set('tp1', $('pl-' + currentUtype + '-tp1') ? $F('pl-' + currentUtype + '-tp1') : null);
	h.set('tp2', $('pl-' + currentUtype + '-tp2') ? $F('pl-' + currentUtype + '-tp2') : null);
	h.set('pf', $('records-pf').checked ? '1' : '0');
	h.set('num', $F('retnum-number'));
	h.set('pos', $F('hof-position'));
	addOptions(h);
	new Ajax.Updater(tab, '/USLeaguesServlet?run', {
		parameters: h,
		onComplete: handleRender
	});
}
function resetUSLeagues() {
	closeTabs();
	changeModeUS();
	changeLeague('nfl');
}
/*============================
  ========== SEARCH ========== 
  ============================*/
function dpatternFocus() {
	if ($F('dpattern') == TX_SEARCH2) {
		$('dpattern').addClassName('focus');
		$('dpattern').value = '';
	}
	$$('#content .selmultiple').each(function(el){
		$(el).style.visibility = 'hidden';
	});
}
function dpatternBlur() {
	if ($F('dpattern') == '') {
		$('dpattern').removeClassName('focus');
		$('dpattern').value = TX_SEARCH2;
	}
	$$('#content .selmultiple').each(function(el){
		$(el).style.visibility = 'visible';
	});
}
function directSearch(li) {
	if (li && li.id && li.id != 'LR') {
		Event.stopObserving($('dpattern'), 'keyup');
		location.href = '/search?p=' + li.id + '&entity';
	}
	else if (event.keyCode == 13 || li.id == 'LR') {
		location.href = '/search?p=' + escape($F('dpattern'));
	}
}
function runSearch() {
	t1 = currentTime();
	var pattern = $F('pattern');
	if (pattern == '') { 
		alert('No search pattern defined.');
	}
	else {
		var tab = initTab();
		var tScope = $('search-form')['scope'];
		var tScopeValue = [];
		$R(0, tScope.length - 1).each(function(i) {
			if (tScope[i].checked) {
				tScopeValue.push(tScope[i].value);
			}
		});
		var h = new Hash();
		h.set('pattern', $F('pattern'));
		h.set('case', $F('case'));
		h.set('match', $F('match'));
		h.set('scope', tScopeValue.join(','));
		addOptions(h);
		new Ajax.Updater(tab, '/SearchServlet?run', {
			parameters: h,
			onComplete: handleRender
		});
	}
}
function resetSearch() {
	closeTabs();
	$('pattern').setValue('').activate();
	$('case').checked = false;
	$('match').checked = false;
	$('ref').checked = true;
	$$('.scope input').each(function(id) {
		$(id).checked = true;
	});
}
/*============================
  ========== LOGIN =========== 
  ============================*/
function auth() {
	if ($F('login') == '') {
		$('login').focus();
	}
	else if ($F('password') == '') {
		$('password').focus();
	}
	else {
		$('flogin').submit();
	}
}
function rauth() {
	$('login').value = $('rlogin').value;
	$('password').value = $('rpassword').value;
	auth();
}
function createAccount() {
	var tSp = [];
	$$('#sp2 li').each(function(el){
		tSp.push($(el).id.replace('sp-', ''));
	});
	if ($('rlogin').value == '') {
		accountErr(TX_MLOGIN);
		$('rlogin').focus();
		{return;}
	}
	else if ($('rpassword').value == '') {
		accountErr(TX_MPASSWORD);
		$('rpassword').focus();
		{return;}
	}
	
	else if ($('rpassword2').value == '') {
		accountErr(TX_MCONFIRMPWD);
		$('rpassword2').focus();
		{return;}
	}
	else if ($('remail').value == '') {
		accountErr(TX_MEMAIL);
		$('remail').focus();
		{return;}
	}
	else if ($('rpassword').value != $('rpassword2').value) {
		accountErr(TX_PWDNOTMATCH);
		$('rpassword2').focus();
		{return;}
	}
	else if (tSp.length == 0) {
		accountErr(TX_MSPORTS);
		{return;}
	}
	$('rmsg').update('<div><img src="/img/db/loading.gif?6"/></div>').removeClassName('error').removeClassName('success').show();
	var h = $H();
	$$('.register input').each(function(el) {
		h.set(el.id, el.value);
	});
	h.set('rsports', tSp.join(','));
	alert(tSp.join(','));
	new Ajax.Request('/LoginServlet?create', { onSuccess: function(response) {
		var s = response.responseText;
		if (!/ERR\|.*/.match(s)) {
			$('rmsg').update(s).removeClassName('error').addClassName('success').show();
		}
		else {
			accountErr(s.split('|')[1]);
			$('rlogin').focus();
		}
	}, parameters: h });
}
function accountErr(s) {
	$('rmsg').update(s).removeClassName('success').addClassName('error').show();
}
function moveSport(sp, list1, list2) {
	$(list2).insert($(sp));
	Event.observe($(sp), 'click', function(){
		moveSport(this, list2, list1);
	});
}
/*============================
  ========== UPDATE ========== 
  ============================*/
var currentAlias = null;
var currentId = null;
function updatePhoto(name) {
	$('currentimg').update('<img alt="" src="' + IMG_URL + name + '"/><br/><a href="javascript:removePhoto(\'' + name + '\');">' + TX_REMOVE + '</a>');
	$('currentimg').show();	
}
function removePhoto(name) {
	new Ajax.Request('/ImageServlet?remove=1&name=' + name, {
		onSuccess: function(response){
			$('currentimg').hide();
		}
	});
}
function showMessage(text) {
	$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
	$('msg').update('<div>' + text.replace(/ERR\:/i, '') + '</div>');
}
/*========== RESULTS ==========*/
var tValues = [];
var dz = null;
function initUpdateResults(value) {
	['sp', 'cp', 'ev', 'se', 'se2', 'yr', 'pl1', 'pl2'].each(function(s){
		new Ajax.Autocompleter(
			s,
			'ajaxsearch',
			'/update/ajax/' + s,
			{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
		);
	});
	$$('#update-results input', '#update-results textarea').each(function(el){
		if ($(el).type == 'button') {
			return;
		}
		$(el).value = $(el).name;
		$(el).title = $(el).name;
		$(el).addClassName('default');
		Event.observe($(el), 'focus', function(){
			if ($(this).value == $(this).name) {
				$(this).value = '';
			}
			$(this).select();
		});
		Event.observe($(el), 'change', function(){
			$(this).removeClassName('completed').removeClassName('completed2');
			tValues[$(this).id] = null;
			$('msg').update('<div class="warning">' + TX_MODIF_WARNING + '</div>');
		});
		Event.observe($(el), 'blur', function(){
			if ($(this).value == '') {
				$(this).value = $(this).name;
				$(this).removeClassName('completed').removeClassName('completed2');
				tValues[$(this).id] = null;
			}
			else if ($(this).value != $(this).name && !$(this).hasClassName('completed')) {
				$(this).removeClassName('completed').addClassName('completed2');
				tValues[$(this).id] = null;
			}
		});
	});
	dz = new Dropzone($('dz-file'), {
		url: '/',
		paramName: 'photo-file',
		addRemoveLinks: false});
	dz.on('processing', function(file) {
		$$('#imgzone p')[0].remove();
	});
	loadResValues(value);
	currentAlias = 'RS';
	$('fresults').style.width = '700px';
}
function loadResValues(value) {
	$('msg').update('');
	var t = value.split('~');
	if (t != null && t.length > 1) {
		tValues['sp'] = t[0]; $('sp').value = t[1]; $('sp').addClassName('completed');
		tValues['cp'] = t[2]; $('cp').value = t[3]; $('cp').addClassName('completed');
		tValues['ev'] = t[4]; $('ev').value = t[5]; updateType('ev', t[6]); $('ev').addClassName('completed');
		if (t[7] != '') {tValues['se'] = t[7]; $('se').value = t[8]; updateType('se', t[9]); $('se').addClassName('completed');}
		if (t[10] != '') {tValues['se2'] = t[10]; $('se2').value = t[11]; updateType('se2', t[12]); $('se2').addClassName('completed');}
		tValues['yr'] = t[13]; $('yr').value = t[14]; $('yr').addClassName('completed');
		if (t.length > 16) {
			tValues['id'] = t[15];
			$('id').value = tValues['id'];
			if (dz != null) {
				$('dz-file').update('<p>' + TX_CLICK_DRAGDROP + '</p>');
				dz.options.url = '/ImageServlet?upload-photo&entity=RS&id=' + tValues['id'];
			}
			// Result Info
			tValues['dt1'] = t[16]; if (t[16] != '') {$('dt1').value = t[16]; $('dt1').addClassName('completed2');} else {$('dt1').value = $('dt1').name; $('dt1').removeClassName('completed2');}
			tValues['dt2'] = t[17]; if (t[17] != '') {$('dt2').value = t[17]; $('dt2').addClassName('completed2');} else {$('dt2').value = $('dt2').name; $('dt2').removeClassName('completed2');}
			tValues['pl1'] = t[18]; if (t[18] != '') {$('pl1').value = t[19]; $('pl1').addClassName('completed').removeClassName('completed2');} else {$('pl1').value = $('pl1').name; $('pl1').removeClassName('completed').removeClassName('completed2');}
			tValues['pl2'] = t[20]; if (t[20] != '') {$('pl2').value = t[21]; $('pl2').addClassName('completed').removeClassName('completed2');} else {$('pl2').value = $('pl2').name; $('pl2').removeClassName('completed').removeClassName('completed2');}
			tValues['exa'] = t[22]; if (t[22] != '') {$('exa').value = t[22]; $('exa').addClassName('completed2');} else {$('exa').value = $('exa').name; $('exa').removeClassName('completed2');}
			tValues['cmt'] = t[23]; if (t[23] != '') {$('cmt').value = t[23]; $('cmt').addClassName('completed2');} else {$('cmt').value = $('cmt').name; $('cmt').removeClassName('completed2');}
			if (t[24] != '') {
				updatePhoto(t[24]);
			}
			else {
				$('currentimg').hide();
			}
			tValues['exl'] = t[25]; if (t[25] != '') {$('exl').value = t[25].replace(/\|/gi, '\r\n'); $('exl').addClassName('completed2');} else {$('exl').value = $('exl').name; $('exl').removeClassName('completed2');}
			// Rankings
			var j = 25;
			for (var i = 1 ; i <= 20 ; i++) {
				tValues['rk' + i] = t[++j];
				// Name
				if (tValues['rk' + i] != '') {
					$('rk' + i).value = t[++j];
					$('rk' + i).addClassName('completed').removeClassName('completed2');
				}
				else {
					j++;
					$('rk' + i).value = $('rk' + i).name;
					$('rk' + i).removeClassName('completed').removeClassName('completed2');
				}
				// Result/Score
				tValues['rs' + i] = t[++j];
				if (tValues['rs' + i] != '') {
					$('rs' + i).value = tValues['rs' + i];
					$('rs' + i).addClassName('completed2');
				}
				else {
					$('rs' + i).value = $('rs' + i).name;
					$('rs' + i).removeClassName('completed2');
				}
			}
			// Person List
			for (var i = 1 ; i <= pListCount ; i++) {
				tValues['rk' + i + 'list'] = null;
			}
			if (t[j + 1].indexOf('rkl') == 0) {
				j++;
				rkList = t[j].substring(4);
				var t_ = rkList.split('#');
				var t__ = null;
				var t___ = null;
				for (var i = 0 ; i < t_.length ; i++) {
					t__ = t_[i].split('|');
					t___ = [];
					for (var i_ = 0 ; i_ < t__.length ; i_++) {
						t___.push(t__[i_].split(':')[0]);
					}
					tValues['rk' + (i + 1) + 'list'] = t___.join('|');
				}
			}
			else {
				rkList = null;
			}
			// Draws
			var k = j;
			tValues['drid'] = t[++k];
			$('cbdraw').checked = (tValues['drid'] != '');
			if (tValues['drid'] != '') {
				['qf1', 'qf2', 'qf3', 'qf4', 'sf1', 'sf2', 'thd'].each(function(s){
					tValues[s + 'w'] = t[++k];
					$(s + 'w').value = t[++k];
					if (tValues[s + 'w']) { $(s + 'w').addClassName('completed'); } else { $(s + 'w').removeClassName('completed'); }
					tValues[s + 'l'] = t[++k];
					$(s + 'l').value = t[++k];
					if (tValues[s + 'l']) { $(s + 'l').addClassName('completed'); } else { $(s + 'l').removeClassName('completed'); }
					tValues[s + 'rs'] = t[++k];
					$(s + 'rs').value = tValues[s + 'rs'];
					if (tValues[s + 'rs']) { $(s + 'rs').addClassName('completed2'); } else { $(s + 'rs').removeClassName('completed2'); }
				});
				$('draw').show();
			}
			else {
				$$('#draw input').each(function(el){
					tValues[$(el).id] = '';
					$(el).value = $(el).name;
					$(el).removeClassName('completed').removeClassName('completed2');
				});
				$('draw').hide();
			}
		}
	}
}
function clearValue(s) {
	tValues[s] = '';
	$(s).value = '';
	$(s).removeClassName('completed').removeClassName('completed2');
	$(s).focus();
}
function setValue(text, li) {
	var t = li.id.split('|');
	if (t[0].indexOf('-') != -1) { // Result
		$(t[0]).value = t[1];
	}
	else { // Data
		tValues[text.id] = t[1];
		$(text).removeClassName('completed2').addClassName('completed');
		if (t.length > 2) {
			updateType(t[0], t[2]);
		}
	}
}
var currentTp = null;
function updateType(s, tp) {
	if ((tValues['se2'] != null && s == 'se2') || (tValues['se'] != null && tValues['se2'] == null && s == 'se') || (tValues['ev'] != null && tValues['se'] == null && tValues['se2'] == null && s == 'ev')) {
		currentTp = parseInt(tp);
	}
	['rk1', 'rk2', 'rk3', 'rk4', 'rk5', 'rk6', 'rk7', 'rk8', 'rk9', 'rk10', 'rk11', 'rk12', 'rk13', 'rk14', 'rk15', 'rk16', 'rk17', 'rk18', 'rk19', 'rk20', 'qf1w', 'qf1l', 'qf2w', 'qf2l', 'qf3w', 'qf3l', 'qf4w', 'qf4l', 'sf1w', 'sf1l', 'sf2w', 'sf2l', 'thdw', 'thdl'].each(function(s){
		Event.stopObserving($(s), 'blur');
		Event.stopObserving($(s), 'keydown');
		new Ajax.Autocompleter(
			s,
			'ajaxsearch',
			'/update/ajax/' + (currentTp < 10 ? 'pr' : (currentTp == 50 ? 'tm' : 'cn')) + (tValues['sp'] != null ? '-' + tValues['sp'] : ''),
			{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
		);
		Event.observe($(s), 'blur', function(){
			if ($(this).value == '') {
				$(this).value = $(this).name;
			}
			else if ($(this).value != $(this).name && !$(this).hasClassName('completed')) {
				$(this).addClassName('completed2');
			}
		});
	});
}
function loadResult(type) {
	var h = $H({tp: type});
	['id', 'sp', 'cp', 'ev', 'se', 'se2', 'yr'].each(function(s){
		h.set(s, tValues[s]);
	});
	new Ajax.Request('/update/load', {
		onSuccess: function(response){
			var text = response.responseText;
			if (text != '') {
				loadResValues(text);	
			}
		},
		parameters: h
	});
}
function addResult() {
	tValues['id'] = null;
	$('id').value = '';
	saveResult();
}
function saveResult() {
	$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
	var h = $H({sp: tValues['sp']});
	var t = ['id', 'sp', 'cp', 'ev', 'se', 'se2', 'yr', 'dt1', 'dt2', 'pl1', 'pl2', 'exa', 'cmt', 'img', 'exl', 'drid', 'qf1w', 'qf1l', 'qf1rs', 'qf2w', 'qf2l', 'qf2rs', 'qf3w', 'qf3l', 'qf3rs', 'qf4w', 'qf4l', 'qf4rs', 'sf1w', 'sf1l', 'sf2w', 'sf1rs', 'sf2l', 'sf2rs', 'thdw', 'thdl', 'thdrs'];
	for (var i = 1 ; i <= 20 ; i++) {
		t.push('rk' + i);
		t.push('rs' + i);
	}
	for (var i = 1 ; i <= pListCount ; i++) {
		t.push('rk' + i + 'list');
	}
	t.each(function(s){
		h.set(s, tValues[s]);
		if ($(s) && ($(s).hasClassName('completed') || $(s).hasClassName('completed2'))) {
			h.set(s + "-l", $F(s));
		}
	});
	new Ajax.Request('/update/save', {
		onSuccess: function(response){
			var text = response.responseText;
			showMessage(text.split('#')[1]);
			if (text.indexOf('ERR:') == -1) {
				tValues['id'] = text.split('#')[0];
				$('id').value = tValues['id'];
			}
		},
		parameters: h
	});
}
function deleteResult() {
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	dQuestion.open();
	$('confirmtxt').update(TX_CONFIRM + ' ?');
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
		var h = $H({id: tValues['id']});
		new Ajax.Request('/update/delete', {
			onSuccess: function(response){
				var text = response.responseText;
				showMessage(text);
			},
			parameters: h
		});
		$('header').setStyle({ opacity: 1.0 });
		$('content').setStyle({ opacity: 1.0 });
		dQuestion.close();
	});
}
function toggleDraw() {
	if ($('cbdraw').checked == true) {
		$('draw').show();
	}
	else {
		$('draw').hide();
	}
}
function loadDataTip(type) {
	$('datatip').show();
	$('datatip').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('datatip'), '/update/data/' + type);
}
var rkList = null;
var pListIndex = null;
var pListCount = 20;
function setPLInput(id) {
	Event.stopObserving($(id), 'blur');
	Event.stopObserving($(id), 'keydown');
	new Ajax.Autocompleter(
		id,
		'ajaxsearch2',
		'/update/ajax/pr' + (tValues['sp'] != null ? '-' + tValues['sp'] : ''),
		{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
	);
	if ($(id).value == '') {
		$(id).value = $(id).name;	
	}
	$(id).addClassName('default');
	Event.observe($(id), 'focus', function(){
		if ($(this).value == $(this).name) {
			$(this).value = '';
		}
	});
	Event.observe($(id), 'blur', function(){
		if ($(this).value == '') {
			$(this).value = $(this).name;
		}
		else if ($(this).value != $(this).name && !$(this).hasClassName('completed')) {
			$(this).addClassName('completed2');
		}
	});
}
function initPersonList(index) {
	var html = [];
	var t = (rkList ? rkList.split('#') : null);
	t = (t ? t[index - 1] : null);
	t = (t ? t.split('|') : null);
	var pid = null;
	var ptxt = null;
	for (var i = 1 ; i <= pListCount ; i++) {
		if (t && t[i - 1]) {
			pid = t[i - 1].split(':')[0];
			ptxt = t[i - 1].split(':')[1];
		}
		else {
			pid = null;
			ptxt = '';
		}
		tValues['plist' + i] = pid;
		html.push('<tr><td><input type="text" id="plist' + i + '" tabindex="' + (100000 + i) + '" name="Name #' + i + '" class="' + (pid != null ? 'completed' : '') + '" value="' + ptxt + '"/><a href="javascript:clearValue(\'plist' + i + '\');">[X]</a></td></tr>');	
	}
	$('plist').update('<table>' + html.join('') + '</table>');
	$$('#plist input').each(function(id){
		setPLInput(id);
	});
	pListIndex = index;
	dPersonList.open();
}
function savePersonList() {
	var t = [];
	var val = null;
	for (var i = 1 ; i <= pListCount ; i++) {
		val = tValues['plist' + i];
		t.push(val && val != '' ? val : $('plist' + i).value);
	}
	tValues['rk' + pListIndex + 'list'] = t.join('|');
	dPersonList.close();
}
function addPersonList() {
	for (var i = pListCount + 1 ; i <= pListCount + 10 ; i++) {
		$$('#plist table')[0].insert('<tr><td><input type="text" id="plist' + i + '" tabindex="' + (100000 + i) + '" name="Name #' + i + '"/><a href="javascript:clearValue(\'plist' + i + '\');">[X]</a></td></tr>');
		setPLInput('plist' + i);
	}
	pListCount += 10;
}
/*========== DATA ==========*/
var isMerge = null;
function initUpdateData() {
	$$('#update-data input').each(function(el){
		if ($(el).id.lastIndexOf('-l') == $(el).id.length - 2) {
			new Ajax.Autocompleter(
				$(el).id,
				'ajaxsearch',
				'/update/ajax/' + $(el).id,
				{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
			);
		}
		if ($(el).type == 'button') {
			return;
		}
		$(el).value = $(el).name;
		$(el).addClassName('default');
		Event.observe($(el), 'focus', function(){
			if ($(this).value == $(this).name) {
				$(this).value = '';
			}
			$(this).select();
		});
		Event.observe($(el), 'change', function(){
			$(this).removeClassName('completed');
			$('msg').update('<div class="warning">' + TX_MODIF_WARNING + '</div>');
		});
		Event.observe($(el), 'blur', function(){
			if ($(this).value == '') {
				$(this).removeClassName('completed');
				var s = $(this).id.substring(0, $(this).id.lastIndexOf('-l'));
				if ($(s)) {
					$(s).value = '';
				}
			}
			else {
				$(this).addClassName('completed');
			}
		});
	});
	dz = new Dropzone($('dz-file'), {
		url: '/',
		paramName: 'photo-file',
		addRemoveLinks: false});
	dz.on("complete", function(file) {
		$$('#imgzone p')[0].remove();
	});
	showPanel('PR');
}
function showPanel(p) {
	if (currentAlias != null) {
		$('link-' + currentAlias).style.fontWeight = 'normal';
		$('table-' + currentAlias).hide();
	}
	$('link-' + p).style.fontWeight = 'bold';
	$('table-' + p).show();
	currentAlias = p;
	loadEntity('last');
}
function loadEntity(action_, id_) {
	var h = $H({action: action_, alias: currentAlias, id: (id_ ? id_ : currentId)});
	new Ajax.Request('/update/load-entity?t=' + currentTime(), {
		onSuccess: function(response){
			setEntityValues(response.responseText);
		},
		parameters: h
	});
	$('msg').update();
}
function setEntityValues(text) {
	var t = text.split('~');
	if (t.length <= 1) {
		return;
	}
	var i = 0;
	currentId = t[i++];
	if (dz != null) {
		$('dz-file').update('<p>' + TX_CLICK_DRAGDROP + '</p>');
		dz.options.url = '/ImageServlet?upload-photo&entity=' + currentAlias + '&id=' + currentId;
	}
	if (currentAlias == 'PR') {
		$('pr-id').value = currentId;
		$('pr-lastname').value = t[i++];
		$('pr-firstname').value = t[i++];
		$('pr-sport').value = t[i++];
		$('pr-sport-l').value = t[i++];
		$('pr-team').value = t[i++];
		$('pr-team-l').value = t[i++];
		$('pr-country').value = t[i++];
		$('pr-country-l').value = t[i++];
		$('pr-link').value = t[i++];
		$('pr-link-l').value = t[i++];
	}
	else if (currentAlias == 'CP') {
		$('cp-id').value = currentId;
		$('cp-label').value = t[i++];
		$('cp-labelfr').value = t[i++];
		$('cp-index').value = t[i++];
	}
	else if (currentAlias == 'CT') {
		$('ct-id').value = currentId;
		$('ct-label').value = t[i++];
		$('ct-labelfr').value = t[i++];
		$('ct-state').value = t[i++];
		$('ct-state-l').value = t[i++];
		$('ct-country').value = t[i++];
		$('ct-country-l').value = t[i++];
		$('ct-link').value = t[i++];
		$('ct-link-l').value = t[i++];
	}
	else if (currentAlias == 'CX') {
		$('cx-id').value = currentId;
		$('cx-label').value = t[i++];
		$('cx-labelfr').value = t[i++];
		$('cx-city').value = t[i++];
		$('cx-city-l').value = t[i++];
		$('cx-link').value = t[i++];
		$('cx-link-l').value = t[i++];
	}
	else if (currentAlias == 'CB') {
		$('cb-id').value = currentId;
		$('cb-login').value = t[i++];
		$('cb-name').value = t[i++];
		$('cb-email').value = t[i++];
		$('cb-active').checked = (t[i++] == '1');
		$('cb-admin').checked = (t[i++] == '1');
		$('cb-sports').value = t[i++];
	}
	else if (currentAlias == 'CN') {
		$('cn-id').value = currentId;
		$('cn-label').value = t[i++];
		$('cn-labelfr').value = t[i++];
		$('cn-code').value = t[i++];
	}
	else if (currentAlias == 'EV') {
		$('ev-id').value = currentId;
		$('ev-label').value = t[i++];
		$('ev-labelfr').value = t[i++];
		$('ev-type').value = t[i++];
		$('ev-type-l').value = t[i++];
		$('ev-index').value = t[i++];
	}
	else if (currentAlias == 'OL') {
		$('ol-id').value = currentId;
		$('ol-year').value = t[i++];
		$('ol-year-l').value = t[i++];
		$('ol-city').value = t[i++];
		$('ol-city-l').value = t[i++];
		$('ol-type').value = t[i++];
		$('ol-start').value = t[i++];
		$('ol-end').value = t[i++];
		$('ol-sports').value = t[i++];
		$('ol-events').svalue = t[i++];
		$('ol-countries').value = t[i++];
		$('ol-persons').value = t[i++];
	}
	else if (currentAlias == 'SP') {
		$('sp-id').value = currentId;
		$('sp-label').value = t[i++];
		$('sp-labelfr').value = t[i++];
		$('sp-type').value = t[i++];
		$('sp-index').value = t[i++];
	}
	else if (currentAlias == 'ST') {
		$('st-id').value = currentId;
		$('st-label').value = t[i++];
		$('st-labelfr').value = t[i++];
		$('st-code').value = t[i++];
		$('st-capital').value = t[i++];
	}
	else if (currentAlias == 'TM') {
		$('tm-id').value = currentId;
		$('tm-label').value = t[i++];
		$('tm-sport').value = t[i++];
		$('tm-sport-l').value = t[i++];
		$('tm-country').value = t[i++];
		$('tm-country-l').value = t[i++];
		$('tm-league').value = t[i++];
		$('tm-league-l').value = t[i++];
		$('tm-conference').value = t[i++];
		$('tm-division').value = t[i++];
		$('tm-comment').value = t[i++];
		$('tm-year1').value = t[i++];
		$('tm-year2').value = t[i++];
		$('tm-link').value = t[i++];
		$('tm-link-l').value = t[i++];
	}
	else if (currentAlias == 'YR') {
		$('yr-id').value = currentId;
		$('yr-label').value = t[i++];
	}
	if (currentAlias == 'PR' || currentAlias == 'CT' || currentAlias == 'CX') {
		if (t[i++] != '') {
			updatePhoto(t[i - 1]);
		}
		else {
			$('currentimg').hide();
		}
		$('imgzone').show();
	}
	else {
		$('imgzone').hide();
	}
	$$('#update-data input').each(function(el){
		if ($(el).value != '' && $(el).id.indexOf('-id') == -1) {
			$(el).addClassName('completed');
		}
		else {
			$(el).removeClassName('completed');
		}
	});
}
function newEntity() {
	var t = $$('#table-' + currentAlias + ' input');
	t.each(function(el){
		$(el).value = '';
		$(el).removeClassName('completed');
	});
	currentId = null;
	t[1].focus();
}
function saveEntity() {
	$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
	var h = $H({alias: currentAlias, id: currentId});
	$$('#table-' + currentAlias + ' input').each(function(el){
		if ($(el).id.lastIndexOf('-l') < $(el).id.length - 2) {
			h.set($(el).id, ($(el).type == 'checkbox' ? $(el).checked : $(el).value));
		}
	});
	new Ajax.Request('/update/save-entity', {
		onSuccess: function(response){
			var text = response.responseText;
			showMessage(text);
			if (text.indexOf('ERR:') == -1) {
				var id_ = text.substring(text.lastIndexOf('#') + 1);
				$(currentAlias.toLowerCase() + '-id').value = id_;
				currentId = id_;
			}
		},
		parameters: h
	});
}
function deleteEntity() {
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	dQuestion.open();
	$('confirmtxt').update(TX_CONFIRM + ' ?');
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
		var h = $H({alias: currentAlias, id: currentId});
		new Ajax.Request('/update/delete-entity', {
			onSuccess: function(response){
				var text = response.responseText;
				loadEntity('previous');
				showMessage(text);
			},
			parameters: h
		});
		$('header').setStyle({ opacity: 1.0 });
		$('content').setStyle({ opacity: 1.0 });
		dQuestion.close();
	});
}
function copyEntity() {
	$(currentAlias.toLowerCase() + '-id').value = '';
	currentId = null;
}
function mergeEntity(id1_, id2_) {
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	dQuestion.open();
	new Ajax.Updater($('confirmtxt'), '/update/merge?confirm=1&id1=' + id1_ + '&id2=' + id2_ + '&alias=' + currentAlias);
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
		var h = $H({alias: currentAlias, id1: id1_, id2: id2_});
		new Ajax.Request('/update/merge', {
			onSuccess: function(response){
				showMessage(response.responseText);
			},
			parameters: h
		});
		$('header').setStyle({ opacity: 1.0 });
		$('content').setStyle({ opacity: 1.0 });
		dQuestion.close();
	});
}
function findEntity(m) {
	isMerge = (m == 1);
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	dFind.open();
	$('fresults').update('');
	$('fpattern').value = '';
	$('fpattern').focus();
}
function searchEntity() {
	if ($F('fpattern').length < 2) {
		return;
	}
	$('fresults').update('<img src="/img/db/loading.gif?6"/>');
	var h = $H({value: $F('fpattern')});
	new Ajax.Request('/update/ajax/' + currentAlias.toLowerCase(), {
		onSuccess: function(response){
			$('fresults').update(response.responseText);
			$$('#fresults li').each(function(el){
				Event.observe($(el), 'click', function(){
					var id = $(this).id.split('|')[1];
					$('header').setStyle({ opacity: 1.0 });
					$('content').setStyle({ opacity: 1.0 });
					dFind.close();
					if (currentAlias == 'RS') {
						tValues['id'] = id;
						loadResult('direct');
					}
					else if ($('update-pictures')) {
						loadPictures('direct', id);
					}
					else if (isMerge) {
						mergeEntity(currentId, id);
					}
					else {
						loadEntity('direct', id);
					}
				});
			});
		},
		parameters: h
	});
}
/*========== OVERVIEW ==========*/
function loadOverview() {
	var h = $H({entity: $F('oventity'), sport: $F('ovsport'), count: $F('ovcount'), pattern: $F('ovpattern')});
	$('ovcontent').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('ovcontent'), '/update/load-overview', {
		parameters: h
	});
}
/*========== PICTURES ==========*/
var dzp = null;
var fp = null;
function initPictures() {
	dzp = new Dropzone($('dz-file'), {
		url: '/',
		paramName: 'f',
		autoProcessQueue: false,
		addRemoveLinks: false});
	dzp.on('addedfile', function(f) {
		fp = f;
		$('name-local').update(f.name);
	});
	dzp.on('success', function(f, text) {
		loadPictures('direct');
	});
	currentAlias = $F('type');
	loadPictures('last');
}
function uploadPicture() {
	dzp.options.url = '/ImageServlet?upload=1&entity=' + currentAlias + '&id=' + currentId + '&size=' + ($('size1').checked ? 'L' : 'S') + '&y1=' + $F('year1') + '&y2=' + $F('year2');
	dzp.processFile(fp);
}
function deletePicture() {
	new Ajax.Request('/ImageServlet?remove=1&name=' + $F('list-remote'), {
		onSuccess: function(response){
			loadPictures('direct');
		}
	});
}
function loadPicture() {
	$('img-remote').update('<img alt="-" src="' + ($F('list-remote') != null ? IMG_URL + $F('list-remote') : '/img/noimage.png') + '"/>');
}
function loadPictures(action_, id_) {
	var h = $H({action: action_, alias: currentAlias, id: (id_ ? id_ : currentId)});
	new Ajax.Request('/update/load-entity?t=' + currentTime(), {
		onSuccess: function(response){
			var t = response.responseText.split('~');
			$('label-remote').update(t[1]);
			currentId = t[0];
			var h_ = $H({entity: $F('type'), id: t[0], size: ($('size1').checked ? 'L' : 'S')});
			new Ajax.Request('/ImageServlet?list=1', {
				onSuccess: function(response){
					var t_ = [];
					response.responseText.split(',').each(function(s){
						if (s != '') {
							t_.push('<option value="' + s + '">' + s + '</option>');	
						}
					});
					$('list-remote').update(t_.join(''));
					if (t_.length > 0) {
						$('list-remote').selectedIndex = 0;
					}
					loadPicture();
				},
				parameters: h_
			});
		},
		parameters: h
	});
}
/*========== IMPORT ==========*/
var dzi = null;
var fi = null;
function initImport() {
	dzi = new Dropzone($('dz-file'), {
		url: '/',
		paramName: 'f',
		autoProcessQueue: false,
		addRemoveLinks: false});
	dzi.on('addedfile', function(f) {
		fi = f;
		$('fname').update(f.name);
		$('processbtn').disabled = false;
		$('updatebtn').disabled = true;
	});
	dzi.on('sending', function() {
		$('report').update('<div><img src="/img/db/loading.gif?6"/></div>');
	});
	dzi.on('success', function(f, text) {
		$('report').update(text);
		$('updatebtn').disabled = (text.indexOf("ERROR:") > -1);
	});
}
function executeImport(u) {
	dzi.options.url = '/update/execute-import?type=' + $F('type') + '&update=' + u;
	dzi.processFile(fi);
}
function loadTemplate() {
	location.href = '/update/load-template?type=' + $F('type');
}
/*========== TOOLS ==========*/
function executeQuery(index) {
	var url = '/update/execute-query?index=' + index + ($('rcsv').checked ? '&csv=1' : '');
	if ($('rcsv').checked) {
		location.href = url;
	}
	else {
		$('qresults').update('<img src="/img/db/loading.gif?6"/>');
		new Ajax.Request(url, {
			onSuccess: function(response){
				$('qresults').update(response.responseText);		
			}
		});
	}
}
/*========== ADMIN ==========*/
function saveConfig() {
	$('msg2').update('<div><img src="/img/db/loading.gif?6"/></div>');
	var h = $H();
	$$('#tconfig input').each(function(el){
		h.set('p_' + $(el).id, $(el).value);
	});
	new Ajax.Request('/update/save-config', {
		onSuccess: function(response){
			var text = response.responseText;
			$('msg2').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
			$('msg2').update('<div>' + text.replace(/^ERR\:/i, '') + '</div>');
		},
		parameters: h
	});
}