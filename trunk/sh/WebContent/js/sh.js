/* ==================== COMMON ==================== */
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
	var text = TEXT_CURRENTLY_SELECTED + ':';
	if (empty) {
		text = '';
	}
	else {
		var t = $(pl).value.split(',');
		if (t[0] == 0) {
			text += '<br/>[' + TEXT_ALL + ']';
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
		text += '<br/><br/><span class="bold">(' + TEXT_CLICK_CHANGE + ')</span>';
	}
	$(hTips.get(pl)).update(text);
}
function handleRender() {
	var title = $('title-' + tabcurrent);
	var tabId = '#t-' + tabcurrent;
	var shorttitle = $$(tabId + ' .shorttitle')[0].innerHTML;
	var info = $$(tabId + ' .infostats')[0];
	title.update(shorttitle);
	$$(tabId + ' .rendertip').each(function(el) {
		new Control.Window($(document.body).down('[href=#' + el.id + ']'),{  
			position: 'relative', hover: true, offsetLeft: 20, offsetTop: 0, className: 'tip'
		});
	});
	t2 = currentTime();
	info.update(info.innerHTML.replace('#DTIME#', elapsedTime(t1, t2)));
}
function toggleContent(el) {
	if (el.tagName != 'IMG') {
		el = $(el).previous('img');
	}
	var isDisplayed = (el.src.indexOf('collapse.gif') != -1);
	var table = $(el).up('table');
	var row = table.down('tbody').down();
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
		row[!isDisplayed ? 'show' : 'hide']();
	}
	else {
		table.down('tr').show();
	}
	el.src = 'img/render/' + (isDisplayed ? 'expand.gif' : 'collapse.gif');
}
function info(s) {
	t1 = currentTime();
	if ($('tabcontrol')) {
		window.scrollTo(0, 0);
		addTab(TEXT_BLANK);
		var tab = initTab();
		new Ajax.Updater(tab, 'InfoRefServlet?p=' + s, {
			onComplete: handleRender,
			parameters: addOptions($H())
		});
	}
	else {
		location.href = 'ref?p=' + s;
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
function refSeeFull(row, p) {
	var cell = $(row).down();
	cell.update('<img src="img/db/loading.gif?6"/>');
	cell.style.backgroundColor = '#FFF';
	new Ajax.Request('InfoRefServlet?p=' + p, {
		onSuccess: function(response){
			$(row).hide();
			$(row).up('tbody').up('table').update(response.responseText);
		},
		parameters: addOptions($H())
	});
}
function winrecSeeFull(row) {
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
	var tr = $("tb-" + id).down('tr');
	while (tr != null) {
		cell = $(tr).down('.srt', sortIndex);
		if (cell) {
			index = cell.innerHTML;
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
	$("tb-" + id).update(!isAsc ? tRow.join('') : tRow.reverse().join(''));
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
		img.alt = img.alt.replace(TEXT_EXPAND, TEXT_COLLAPSE);
		$('treeview').style.border = '1px solid #000';
	}
	else {
		$('treeview').removeClassName('expanded').addClassName('collapsed');
		img.src = img.src.replace('collapse', 'expand');
		img.alt = img.alt.replace(TEXT_COLLAPSE, TEXT_EXPAND);
		$('treeview').style.border = '1px solid #DDD';
	}
	$('treeicontxt').update(img.alt);
	//img.title = img.alt;
	treeExpanded = !treeExpanded;
}
/* ==================== UTILS ==================== */
var t1 = null;
var t2 = null;
function elapsedTime(t1_, t2_) {
	return ((t2_ - t1_) / 1000);
}
function backTop() {
	window.scrollTo(0, 0);
}
function setLang(s) {
	new Ajax.Request('IndexServlet?lang=1&value=' + s, {
		onSuccess: function(response){
			window.location.reload();
		}
	});
}
/* ==================== TABCONTROL ==================== */
var tabcurrent = 0;
var tabcount = 0;
function initTabControl() {
	addTab(TEXT_BLANK);
	
	tabbar.insert('<li title="Open New Tab" id="link-add"><a href="javascript:void(0);"></a></li>');
	var link = $$('#link-add a')[0];
	link.observe('click', function(){addTab(TEXT_BLANK);});
}
function getCloseImg(idx) {
	var img = new Element('img', {id: 'close-' + idx, src: 'img/component/tabcontrol/close.gif'});
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
		addTab(TEXT_BLANK);
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
	$('title-' + tab.id.replace('t-', '')).update(TEXT_LOADING);
	$('export').removeClassName('export-disabled').addClassName('export').disabled = '';
	$('link').removeClassName('link-disabled').addClassName('link').disabled = '';
	$('print').removeClassName('print-disabled').addClassName('print').disabled = '';
	$('info').removeClassName('info-disabled').addClassName('info').disabled = '';
	$('close').removeClassName('close-disabled').addClassName('close').disabled = '';
	return tab.update('<div class="loading"></div>');
}
function closeTabs() {
	$('tabbar').update('<li/>');
	$$('#tabcontrol .tc').each(function(el) {
		el.remove();
	});
	$('export').removeClassName('export').addClassName('export-disabled').disabled = 'disabled';
	$('link').removeClassName('link').addClassName('link-disabled').disabled = 'disabled';
	$('print').removeClassName('print').addClassName('print-disabled').disabled = 'disabled';
	$('info').removeClassName('info').addClassName('info-disabled').disabled = 'disabled';
	$('close').removeClassName('close').addClassName('close-disabled').disabled = 'disabled';
	tabcurrent = 0;
	tabcount = 0;
	initTabControl();
}
function closeDialog(dlg) {
	dlg.close();
	$('header').setStyle({ opacity: 1.0 });
	$('content').setStyle({ opacity: 1.0 });
}
var dExport = null;
var dLink = null;
var dInfo = null;
function refreshLastUpdates() {
	$('dupdates').update('<img src="img/db/loading.gif?6" alt="Loading..."/>');
	new Ajax.Updater($('dupdates'), 'IndexServlet?t=' + currentTime() + '&lastupdates=1&count=' + $('countupdt').value, {});
}
function displayExport() {
	if (dExport) {
		$('header').setStyle({ opacity: 0.4 });
		$('content').setStyle({ opacity: 0.4 });
		$('ehtml').checked = true;
		dExport.open();
	}
}
function displayLink() {
	var url = $$('#' + tabs.activeContainer.id + ' .url')[0].innerHTML;
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
	var url = $$('#' + tabs.activeContainer.id + ' .url')[0].innerHTML;
	var info = $$('#' + tabs.activeContainer.id + ' .infostats')[0].innerHTML;
	var tInfo = info.split('|');
	var t = $$('#d-info td');
	t[0].update(url);
	t[1].update(tInfo[0] + '&nbsp;' + TEXT_KB);
	t[2].update(tInfo[1] + '&nbsp;' + TEXT_SECONDS);
	t[3].update(tInfo[2]);
	$('header').setStyle({ opacity: 0.4 });
	$('content').setStyle({ opacity: 0.4 });
	dInfo.open();
}
function exportTab() {
	var url = $$('#' + tabs.activeContainer.id + ' .url')[0].innerHTML;
	if (url) {
		location.href = url + '&export=' + ($('ehtml').checked ? 'html' : ($('eexcel').checked ? 'excel' : ($('epdf').checked ? 'pdf' : 'text')));
	}
}
function printCurrentTab() {
	var url = $$('#' + tabs.activeContainer.id + ' .url')[0].innerHTML;
	if (url) {
		window.open(url + '&print', '_blank');
	}
}
/* ==================== SLIDER ==================== */
var hSliders = new Hash();
function sliderLinkOver() {
	var src = this.down().src;
	this.down().src = src.replace('.gif', '-over.gif');
}
function sliderLinkOut() {
	var src = this.down().src;
	this.down().src = src.replace('-over.gif', '.gif');
}
function handleSliderArr(id) {
	var slider = hSliders.get(id);
	var length = slider.slides.length;
	var links = $$('#' + id + ' .slider-control');
	links[0].style.visibility = (!slider.current || slider.current._index == 0 ? 'hidden' : '');
	links[1].style.visibility = (length <= 1 || (slider.current && slider.current._index == length - 1) ? 'hidden' : '');
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
		//link.up().setStyle({marginTop: (h / 2) - 25});
	});
	hSliders.set(id, new Carousel(container, slides, links, {duration: 0.4, circular: (c ? true : false), afterMove: (!c ? function(){handleSliderArr(id);} : null)}));
	if (!c) {
		handleSliderArr(id);
	}
	return hSliders.get(id);
}
/* ==================== SELECT-MULTIPLE ==================== */
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
	container.down().innerHTML = TEXT_SELECT + ': ' + s;
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
/* ==================== HOME ==================== */
function loadHomeData() {
	new Ajax.Request('IndexServlet?t=' + currentTime(), {
		onSuccess: function(response) {
			var xml = response.responseXML;
			if (!xml) {return;}
			var root = xml.documentElement;
			var node = null;

			// Statistics
			var stat = root.getElementsByTagName('stats')[0];
			for (var i = 0 ; i < stat.childNodes.length ; i++) {
				node = stat.childNodes[i];
				$(node.getAttribute('id')).update(node.getAttribute('value'));
			}
			$('img-stat').hide();$('table-stat').show();
		}
	});
	refreshLastUpdates();
}
/* ==================== RESULTS ==================== */
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
			if (val.indexOf('_') != -1) {
				val = val.substring(val.lastIndexOf('_') + 1);
			}
			txt = t[i][0];
			array.push({value: val, text: txt});
		}
		fillPicklistArray(picklistId, array);
	}
	else {
		var h = $H({sp: $F('pl-sp'), cp: $F('pl-cp'), ev: $F('pl-ev'), se: $F('pl-se'), se2: $F('pl-se2')});
		new Ajax.Request('ResultServlet?' + picklistId, {
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
		h = $H({sp: sp_, cp: cp_, ev: ev_, se: se_, yr: 0});
	}
	else { // Picklist
		h = $H({sp: $F('pl-sp'), cp: $F('pl-cp'), ev: $F('pl-ev'), se: $F('pl-se'), se2: $F('pl-se2'), yr: $F('pl-yr')});
	}
	addOptions(h);
	new Ajax.Updater(tab, 'ResultServlet?run', {
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
	var t = value.split('_');
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
/* ==================== OLYMPICS ==================== */
function initOlympics(picklistId) {
	var type = (picklistId.indexOf('summer') == 0 ? 'summer' : 'winter');
	var url = 'OlympicsServlet?type=' + type;
	new Ajax.Request(url, {
		onSuccess: fillPicklistXML,
		onFailure: function(response) {}
	});
}
function changeModeOL() {
	var isSummer = $F('olt1');
	if (isSummer) {
		$('summerfs').show();
		$('winterfs').hide();
		changeOlympics('summer-pl-ol');
	}
	else {
		$('summerfs').hide();
		$('winterfs').show();
		changeOlympics('winter-pl-ol');
	}
}
function changeOlympics(id) {
	var isSummer = (id.indexOf('summer') == 0);
	var code = (isSummer ? 'summer' : 'winter');
	var h = $H({ol: $F(code + '-pl-ol')});
	h.set('type', code);
	new Ajax.Request('OlympicsServlet?tree=1', {
		onSuccess: function(response){
			eval(response.responseText);
			getPicklistOL(code + '-pl-sp');
			updateSliderSp(code);
		},
		parameters: h
	});
	new Ajax.Request('OlympicsServlet?pl-cn', {
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
		array.push({value: 0, text: '[' + TEXT_ALL + ']'});
	}
	fillPicklistArray(picklistId, array);
}
function updateSliderOl(code) {
	var slider = $$('#slider-' + code + '-ol .content')[0];
	var sliderContent = ['<div id="' + code + '-sl1" class="slide">'];
	var n = -1;
	var currentOl = $F(code + '-pl-ol');
	$$('#' + code + '-pl-ol option').each(function(el) {
		if (el.text.indexOf('---') != 0) {
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
	var url = 'OlympicsServlet?run&type=' + (ind ? 'ind' : 'cnt');
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
/* ==================== US LEAGUES ==================== */
var currentLeague = null;
var currentUtype = null;
function initSliderUS() {
	var sliderContent = [];
	sliderContent.push('<div id="sl-nfl" class="slide" title="NFL" style="background-image:url(img/db/nfl.gif)"></div>');
	sliderContent.push('<div id="sl-nba" class="slide" style="background-image:url(img/db/nba.gif)"></div>');
	sliderContent.push('<div id="sl-nhl" class="slide" style="background-image:url(img/db/nhl.gif)"></div>');
	sliderContent.push('<div id="sl-mlb" class="slide" style="background-image:url(img/db/mlb.gif)"></div>');
	sliderContent.push('<div id="sl-nfl" class="slide" title="NFL" style="background-image:url(img/db/nfl.gif)"></div>');
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
		var url = 'USLeaguesServlet?league=' + league;
		new Ajax.Request(url + '&pl-hof-yr', { onSuccess: fillPicklistXML });
		new Ajax.Request(url + '&pl-championship-yr', { onSuccess: fillPicklistXML });
		new Ajax.Request(url + '&pl-retnum-tm', { onSuccess: fillPicklistXML });
		new Ajax.Request(url + '&pl-teamstadium-tm', { onSuccess: fillPicklistXML });
		new Ajax.Request(url + '&pl-winloss-tm', { onSuccess: fillPicklistXML });
		new Ajax.Request(url + '&pl-record-se', { onSuccess: fillPicklistXML });
		$('hof-position').value = '';
		$('hof-postip').title = tPos[league];
		$('retnum-number').value = '';
		var tType2 = ['Career', 'History', 'Season', 'Series', 'Game', 'Half', 'Quarter'];
		var tAll = new Array();
		for (var i = 0 ; i < tType2.length ; i++) {
			if (league == 3 && tType2[i] == 'Quarter') {
				tType2[i] = 'Period';
			}
			if ((league == 1 || league == 4) && tType2[i] == 'Series') {}
			else {
				tAll.push('\'' + tType2[i] + '\'');
				tType2[i] = '<option value="\'' + tType2[i] + '\'">' + tType2[i] + '</option>';
			}
		}
		tType2.push('<option value="' + tAll.join(',') + '">[' + TEXT_ALL + ']</option>');
		$('pl-record-tp2').update(tType2.join(''));
	}
}
function changeModeUS(id) {
	id = (id != null ? id : 'championship');
	currentUtype = id;
	['championship', 'record', 'winloss', 'hof', 'retnum', 'teamstadium'].each(function(id_) {
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
	//var type = ($F('hof') ? 'hof' : ($F('retnum') ? 'retnum' : ($F('teamstadium') ? 'teamstadium' : ($F('championship') ? 'championship' : ($F('record') ? 'record' : 'winloss')))));
	var h = new Hash();
	h.set('league', league);
	h.set('type', currentUtype);
	h.set('tm', $('pl-' + currentUtype + '-tm') ? $F('pl-' + currentUtype + '-tm') : null);
	h.set('yr', $('pl-' + currentUtype + '-yr') ? $F('pl-' + currentUtype + '-yr') : null);
	h.set('se', $('pl-' + currentUtype + '-se') ? $F('pl-' + currentUtype + '-se') : null);
	h.set('tp1', $('pl-' + currentUtype + '-tp1') ? $F('pl-' + currentUtype + '-tp1') : null);
	h.set('tp2', $('pl-' + currentUtype + '-tp2') ? $F('pl-' + currentUtype + '-tp2') : null);
	h.set('pf', $('record-pf').checked ? '1' : '0');
	h.set('num', $F('retnum-number'));
	h.set('pos', $F('hof-position'));
	addOptions(h);
	new Ajax.Updater(tab, 'USLeaguesServlet?run', {
		parameters: h,
		onComplete: handleRender
	});
}
function resetUSLeagues() {
	closeTabs();
	changeModeUS();
	changeLeague('nfl');
}
/* ==================== SEARCH ==================== */
function directSearch() {
	if (event.keyCode == 13) {
		location.href = 'search?p=' + $F('dpattern');
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
		new Ajax.Updater(tab, 'SearchServlet?run', {
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
/* ==================== PROJECT ==================== */
function loadChart() {
	$('charttxt').update('<tr><td><img src="img/db/loading.gif?6"/></td></tr>');
	new Ajax.Request('ProjectServlet?index=' + $('charts').value, {
		onSuccess: function(response) {
			var tData = new Array();
			var xml = response.responseXML;
			if (!xml) {return;}
			var root = xml.documentElement;
			var node = null;
			var tHtml = new Array();
			var key = null;
			var value = null;
			var n1 = 0;
			var n2 = 0;
			for (var i = 0 ; i < root.childNodes.length ; i++) {
				node = root.childNodes[i];
				key = node.getAttribute('key');
				value = parseInt(node.getAttribute('value'));
				if (n1 < 5 && value > 0) {
					tData.push({data: [[0, value]], label: key});
					n1++;
				}
				if (n2 < 10 && value > 0) {
					tHtml.push('<tr><td>' + key + '</td><td>' + value + '</td></tr>');
				}
			}
			$('charttxt').update('<tr><th>Type</th><th>Count</th></tr>' + tHtml.join(''));
			Flotr.draw($('chart'), tData, {
				//colors: ['#00A8F0', '#C0D800', '#CB4B4B', '#4DA74D', '#9440ED'],
				resolution: 2,
				shadowSize: 4,
				HtmlText: true, 
				grid: {outlineWidth: 0, verticalLines: false,  horizontalLines: false},
				xaxis: {showLabels: false},
				yaxis: {showLabels: false},
				pie: {show: true},
				legend:{position: 'se', backgroundColor: '#FFF'}
			});
		}
	});
}
/* ==================== LOGIN ==================== */
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
	$('login').value = $('r-login').value;
	$('password').value = $('r-password').value;
	auth();
}
function createAccount() {
	if ($('r-login').value == '') {
		accountErr('Field "Login" is mandatory.');
		$('r-login').focus();
		{return;}
	}
	else if ($('r-password').value == '') {
		accountErr('Field "Password" is mandatory.');
		$('r-password').focus();
		{return;}
	}
	else if ($('r-password2').value == '') {
		accountErr('You must confirm the password.');
		$('r-password2').focus();
		{return;}
	}
	else if ($('r-email').value == '') {
		accountErr('Field "E-Mail Address" is mandatory.');
		$('r-email').focus();
		{return;}
	}
	else if ($('r-password').value != $('r-password2').value) {
		accountErr('The entered passwords do not match.');
		$('r-password2').focus();
		{return;}
	}
	$('r-msg').update('Please wait...').removeClassName('error').removeClassName('success').addClassName('waiting').show();
	var h = $H();
	$$('.register input').each(function(el) {
		h.set(el.id, el.value);
	});
	new Ajax.Request('LoginServlet?create', { onSuccess: function(response) {
		var s = response.responseText;
		if (!/ERR\|.*/.match(s)) {
			$('r-msg').update(s).removeClassName('error').removeClassName('waiting').addClassName('success').show();
		}
		else {
			accountErr(s.split('|')[1]);
			$('r-login').focus();
		}
	}, parameters: h });
}
function accountErr(s) {
	$('r-msg').update(s).removeClassName('success').removeClassName('waiting').addClassName('error').show();
}