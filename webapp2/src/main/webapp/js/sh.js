/*============================
  ============================*/
function fillPicklistXML(response) {
	var xml = (response != null ? response.responseXML : pl.outerHTML);
	if (!xml) {return;}
	var root = xml.documentElement;
	var picklistId = root.getAttribute('id');
	var picklist = $(picklistId);
	var items = root.getElementsByTagName('item');
	if (items.length > 0) {
		var tHtml = new Array();
		var node = null;
		for (var i = 0 ; i < items.length ; i++) {
			node = items[i];
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
				text += '<br/>- ' + h.get(t[i]);
			}
			text += (t.length > 10 ? '<br/>(+' + (t.length - 10) + ' more)' : '');
		}
		text += '<br/><br/><span class="bold">(' + TX_CLICK_CHANGE + ')</span>';
	}
	$(hTips.get(pl)).update(text);
}
function handleRender() {
	var info = $$('#content .infostats')[0];
	$$('#content .rendertip').each(function(el) {
		if ($('treeresults')) {
			$(el).previous('a').title = el.innerHTML;
		}
		else {
			new Control.Window($(document.body).down('[href=#' + el.id + ']'),{
				position: 'relative', hover: true, offsetLeft: 20, offsetTop: 28, className: 'tip'
			});		
		}
	});
	t2 = currentTime();
	var t = elapsedTime(t1, t2);
	if (info) {
		info.update(info.innerHTML.replace('#DTIME#', t));
	}
	if ($('loadtime')) {
		$('loadtime').down('span').update(t);
		$('loadtime').show();
	}
	if ($('msgerr')) {
		$$('#topbar .toolbar')[0].hide();
		$('errorlink').hide();
	}
	// Favorites
	if ($('favimg')) {
		var url = $$('#content .url')[0].innerHTML;
		if (isFavorite(url)) {
			$('favimg').onclick = function(){deleteFavorite();};
			$('favimg').src = '/img/menu/favorites.png';
			$('favimg').title = TX_DELFAV;
		}
		else {
			$('favimg').onclick = function(){addFavorite();};
			$('favimg').src = '/img/menu/favorites2.png';
			$('favimg').title = TX_ADDFAV;
		}
	}
}
function toggleContent(el) {
	if (el.tagName != 'IMG') {
		el = $(el).previous('img');
	}
	var isDisplayed = (el.src.indexOf('collapse.png') != -1);
	var obj = $(el).up('div').next('table');
	if (!$(obj)) {
		obj = $(el).up('div').next('div');
	}
	if (!isDisplayed) {
		obj.show();
	}
	else {
		obj.hide();
	}
	el.src = '/img/render/' + (isDisplayed ? 'expand.png' : 'collapse.png') + '?v=' + VERSION + '_2';
}
function info(s) {
	window.location.href = '/results/' + s;
}
function currentTime() {
	return new Date().getTime();
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
		}
	});
}
function togglePlist(img, id) {
	if ($(id).style.display == 'none') {
		$(id).show();
		img.src = '/img/render/minus.png?v=' + VERSION;
	}
	else {
		$(id).hide();
		img.src = '/img/render/plus.png?v=' + VERSION;
	}
}
function winrecMore(row) {
	var table = $(row).up('table');
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
			if (cell.getAttribute('rowspan')) {
				for (var j = 0 ; j < parseInt(cell.getAttribute('rowspan')) ; j++) {
					tr = tr.next();
					t[index] += tr.outerHTML;	
				}
			}
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
function moreImg(c) {
	$$('.' + c).each(function(el) {
		el.show();
	});
	$(c + '-link').hide();
}
function addFavorite() {
	var fav = getCookie('shfav');
	var url = $$('#content .url')[0].innerHTML;
	url = url.replace('http://', '').replace('https://', '');
	url = url.substring(url.indexOf('/')).replace('&amp;', '&');
	var title = document.title.replace(' | Sporthenon', '');
	title = title.replace(/\&nbsp\;/gi, ' ');
	fav += '|' + url + ':' + title;
	setCookie('shfav', fav);
	if ($('favimg')) {
		$('favimg').onclick = function(){deleteFavorite();};
		$('favimg').src = '/img/menu/favorites.png';
		$('favimg').title = TX_DELFAV;	
	}
}
function deleteFavorite(url) {
	if (!url) {
		url = $$('#content .url')[0].innerHTML;
	}
	url = url.replace('http://', '').replace('https://', '');
	url = url.substring(url.indexOf('/'));
	var tfav = getCookie('shfav').split('|');
	var tfav_ = [];
	for (var i = 0 ; i < tfav.length ; i++) {
		if (tfav[i].indexOf(url) != 0) {
			tfav_.push(tfav[i]);
		}
	}
	setCookie('shfav', tfav_.join('|'));
	if ($('favimg')) {
		$('favimg').onclick = function(){addFavorite();};
		$('favimg').src = '/img/menu/favorites2.png';
		$('favimg').title = TX_ADDFAV;	
	}
}
function deleteFavClick(i) {
	deleteFavorite($('fav-' + i).down('a').href);
	$('fav-' + i).remove();
}
function isFavorite(url) {
	url = url.replace('http://', '').replace('https://', '');
	url = url.substring(url.indexOf('/')).replace('&amp;', '&');
	var fav = false;
	var tfav = getCookie('shfav').split('|');
	for (var i = 0 ; i < tfav.length ; i++ && !fav) {
		if (tfav[i].indexOf(url) == 0) {
			fav = true;
		}
	}
	return fav;
}
function toggle(id) {
	var img = $('img-' + id);
	if ($(id).style.display == 'none') {
		$(id).show();
		img.src = img.src.replace('expand', 'collapse') + '?v=' + VERSION;
	}
	else {
		$(id).hide();
		img.src = img.src.replace('collapse', 'expand') + '?v=' + VERSION;
	}
}
var currentExpPhoto = null;
function thumbnailClick(id){
	if (currentExpPhoto == null) {
		currentExpPhoto = $$('.info img')[0].id;
	}
	$(id).style.width = $(currentExpPhoto).style.width;
	$(id).style.height = $(currentExpPhoto).style.height;
	$(id).title = TX_ENLARGE;
	$("link-" + id).href = IMG_URL + id;
	$(currentExpPhoto).style.width = '50px';
	$(currentExpPhoto).style.height = '50px';
	$(currentExpPhoto).title = '';
	$("link-" + currentExpPhoto).href = 'javascript:thumbnailClick(\"' + currentExpPhoto + '\");';
	currentExpPhoto = id;
}
function showFavorites() {
	$$('#content .selmultiple').each(function(el){
		$(el).style.visibility = 'hidden';
	});
	$('favorites').show();
}
function hideFavorites() {
	$$('#content .selmultiple').each(function(el){
		$(el).style.visibility = 'visible';
	});
	$('favorites').hide();
}
function enlargePhoto(id) {
	var li = $('ph-' + id);
	var dpdiv = $('dpicture-div');
	dpdiv.style.paddingLeft = '8px';	
	if (li.innerHTML.indexOf('<img') == 0) {
		var img = li.getElementsByTagName('img')[0];
		dpdiv.style.width = 'auto';
		dpdiv.style.height = 'auto';
		var html = img.outerHTML;
		if (img.alt != '') {
			html += '<div class="source">' + img.alt + '</div>';
			dpdiv.style.paddingLeft = '15px';
		}
		dpdiv.update(html);
		dPicture.open();
	}
	else {
		new Ajax.Request('/ImageServlet?load&directid=' + id, {
			onComplete: function(response){
				var html = response.responseText;
				var iframe = li.getElementsByTagName('iframe')[0];
				dpdiv.style.width = iframe.width + 'px';
				dpdiv.style.height = iframe.height + 'px';
				dpdiv.update(html);
				dPicture.open();
			}
		});
	}
	setOpacity(0.4);
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
	Effect.ScrollTo('header', {duration: 0.5});
}
function setCookie(name, value) {
    var d = new Date();
    d.setTime(d.getTime() + (10*365*24*60*60*1000));
    document.cookie = name + '=' + value + '; expires=' + d.toUTCString() + ';path=/';
}
function getCookie(name) {
    name = name + '=';
    var ca = document.cookie.split(';');
    for(var i = 0 ; i < ca.length ; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
        	c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
        	return c.substring(name.length,c.length);
        }
    }
    return '';
}
function setOpacity(x) {
	$('header').setStyle({ opacity: x });
	$('footer').setStyle({ opacity: x });
	$('content').setStyle({ opacity: x });
}
function closeDialog(dlg) {
	dlg.close();
	setOpacity(1.0);
}
var dError = null;
var dPicture = null;
var dLink = null;
var dInfo = null;
var dPersonList = null;
var dRound = null;
var dFind = null;
var dQuestion = null;
var dComment = null;
var dHelp = null;
function share(type) {
	var url = $$('#content .url')[0].innerHTML;
	if (type == 'fb') {
		url = 'https://www.facebook.com/sharer/sharer.php?u=' + encodeURIComponent(url);
	}
	else if (type == 'tw') {
		url = 'https://twitter.com/share?url=' + encodeURIComponent(url);
	}
	else if (type == 'lnk') {
		url = 'https://www.linkedin.com/sharing/share-offsite/?url=' + encodeURIComponent(url);
	}
	else if (type == 'bg') {
		url = 'https://www.blogger.com/blog-this.g?u=' + encodeURIComponent(url) + '&n=' + escape(document.title);
	}
	else if (type == 'tm') {
		url = 'http://tumblr.com/share?&u=' + encodeURIComponent(url);
	}
	$('shareopt').hide();
	window.open(url, '_blank');
}
function exportPage(type) {
	var url = $$('#content .url')[0].innerHTML;
	if (url) {
		location.href = url + (url.indexOf('?') == -1 ? '?' : '&') + 'export=' + type;
		$('exportopt').hide();
	}
}
function displayErrorReport() {
	var url = $$('#content .url')[0].innerHTML;
	setOpacity(0.4);
	$('errlinkurl').value = url;
	$('errlinktext').value = '';
	dError.open();
	$('errlinktext').focus();
}
function displayLink() {
	var url = $$('#content .url')[0].innerHTML;
	if (dLink && url) {
		setOpacity(0.4);
		dLink.open();
		$('linktxt').value = url.replace('&amp;', '&');
		$('linktxt').select();
		$('linktxt').focus();
	}
}
function displayInfo() {
	var info = $$('#content .infostats')[0].innerHTML;
	var tInfo = info.split('|');
	var t = $$('#d-info td');
	t[0].update(tInfo[0] + ' ' + TX_KB);
	t[1].update(tInfo[1] + ' ' + TX_SECONDS);
	t[2].update(tInfo[2]);
	t[3].update(tInfo[3] == 'fr' ? 'Français' : 'English');
	t[3].className = ('lang-' + tInfo[3]);
	t[4].update(tInfo[4]);
	setOpacity(0.4);
	dInfo.open();
}
function printCurrentTab() {
	var url = $$('#content .url')[0].innerHTML;
	if (url) {
		window.open(url + '?print', '_blank');
	}
}
function saveError() {
	const url_ = $F('errlinkurl');
	var h = $H({url: url_, text: $F('errlinktext')});
	new Ajax.Request('/IndexServlet?error=1', {
		onSuccess: function(response){
			window.location.href = url_;
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
	container.down().style.width = '50000px';
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
	createSlider('sports', 1090, 120, true);
}
function getLastUpdates(row, p, sp) {
	var tbody = $('tlast').down('tbody');
	var cell = null;
	if (row != null) {
		cell = $(row).up();
		cell.update('<img src="/img/db/loading.gif?6"/>');
		cell.style.backgroundColor = '#FFF';
	}
	else {
		$('dupdates-loading').show();
	}
	new Ajax.Request('/IndexServlet?' + (p != null ? 'p=' + p : '') + (sp != null ? 'sport=' + sp : '') + '&lastupdates&t=' + currentTime(), {
		onSuccess: function(response){
			if (cell != null) {
				$(cell).up('tbody').insert(response.responseText);
				$(cell).up('tr').remove();
			}
			else {
				$('dupdates-loading').hide();
				tbody.update(response.responseText);
			}
		}
	});
}
function getRandomEvent() {
	new Ajax.Updater($('randomeventvalue'), '/IndexServlet?randomevent&t=' + currentTime());
}
var cindex = 0;
var cmax = 1;
var cdata = [];
var clabel = [];
var ccolor = [['Gradient(#ff8b6b:#ff3900)'], ['Gradient(#ff8b6b:#ff3900)']];
function loadReport(cdata_, clabel_, ccolor_) {
	$('chart').update('<canvas id="cvs" width="720" height="400"></canvas>');
	if (cindex == 0 || cindex == 1) {
		new RGraph.HBar('cvs', cdata_)
		.Set('gutter.top', 0)
		.Set('gutter.left', 120)
		.Set('labels', clabel_)
		.Set('colors', ccolor_)
		.Set('text.font', 'Verdana')
		.Set('text.size', 8)
		.Set('vmargin', 8)
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
	const isMobile = (document.body.clientWidth < 640);
	const treeresults = $('treeresults');
	if (treeresults && !isMobile) {
		window.scrollTo(0, 0);
		new Ajax.Updater(treeresults, '/BrowseResults', {
			onComplete: handleRender,
			parameters: h
		});
	}
	else {
		window.location.href = '/SearchResults?' + h.toQueryString();
	}
}
function resetResults() {
	$('pl-sp').selectedIndex = 0;
	changeSport();
}
var currentNodeLink = null;
function treeLeafClick(anchor, value) {
	if (value.indexOf('link-') == 0) {
		info(value.substring(5));
		return;
	}
	else if (value.indexOf('calendar-') == 0) {
		location.href = '/calendar/' + value.substring(9);
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
		$(anchor).addClassName('selectednode');
		if (currentNodeLink != null) {
			$(currentNodeLink).removeClassName('selectednode');	
		}
		currentNodeLink = anchor;
		return;
	}
	if ($('treeresults')) {
		$('treeresults').update('<div class="loading"></div>');
	}
	runResults(t);
}
/*==============================
  ========== CALENDAR ========== 
  ==============================*/
var isDate2 = false;
function refreshDate(i) {
	var y = $F('yr' + i);
	var m = $F('mo' + i);
	var d = $F('dt' + i);
	$('year' + i).update(y);
	if (m != '') {
		m = $('mo' + i).options[$('mo' + i).options.selectedIndex].text;
		m = m.substring(4).toUpperCase();
		$('month' + i).update(m);
		$('month' + i).show();
		$('year' + i).style.paddingTop = '0px';
	}
	else {
		$('year' + i).style.paddingTop = '25px';
		$('month' + i).hide();
		d = '';
		$('dt' + i).value = '';
	}
	if (d != '') {
		$('day' + i).update(d);
		$('day' + i).show();
		$('month' + i).style.paddingTop = '0px';
	}
	else {
		$('day' + i).hide();
		$('month' + i).style.paddingTop = '20px';
	}
}
function showDate2() {
	refreshDate(2);
	$('spanbutton').hide();
	$('date2-1').show();
	$('date2-2').show();
	$('label1').show();
	isDate2 = true;
}
function runCalendar() {
	$("errperiod").hide();
	var tm = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	t1 = currentTime();
	var yr1 = $F('yr1');
	var mo1 = $F('mo1');
	var dt1 = $F('dt1');
	var yr2 = $F('yr2');
	var mo2 = $F('mo2');
	var dt2 = $F('dt2');
	if (isDate2) {
		mo2 = (mo2 == '' ? '12' : mo2);
		dt2 = (dt2 == '' ? tm[parseInt(mo2)] : dt2);
	}
	else {
		yr2 = yr1;
		mo2 = (mo1 == '' ? '12' : mo1);
		dt2 = (dt1 == '' ? tm[parseInt(mo2)] : dt1);
	}
	mo1 = (mo1 == '' ? '01' : mo1);
	dt1 = (dt1 == '' ? '01' : dt1);
	var d1 = Date.parse(yr1 + '-' + mo1 + '-' + dt1);
	var d2 = Date.parse(yr2 + '-' + mo2 + '-' + dt2);
	if (d2 - d1 > 365 * 24 * 60 * 60 * 1000) {
		$("errperiod").style.display = 'block';
	}
	else {
		var h = $H({dt1: yr1 + mo1 + dt1, dt2: yr2 + mo2 + dt2});
		window.location.href = '/SearchCalendar?' + h.toQueryString();
	}
}
function resetCalendar() {
	$('spanbutton').show();
	$('date2-1').hide();
	$('date2-2').hide();
	$('label1').hide();
	$('yr1').value = todayY;
	$('mo1').value = todayM;
	$('dt1').value = todayD;
	refreshDate(1);
	isDate2 = false;
	$("errperiod").hide();
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
		if (el.value.indexOf(',') === -1 && parseInt(el.value) > 0) {
			var pattern = new RegExp('(^|.*,)' + el.value + '(,.*|$)');
			var text = null;
			if (currentOl == '0' || pattern.match(currentOl)) {
				text = el.text;
				text = text.substring(text.indexOf(' - ') + 3) + ' ' + text.substring(0, 4);
				sliderContent.push(++n > 0 && n % 10 == 0 ? '</div><div class="slide">' : '');
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
	window.location.href = '/SearchOlympics?' + h.toQueryString() + '&type=' + (ind ? 'ind' : 'cnt');
}
function resetOlympics() {
	var ind = ($('olympics-individual') ? true : false);
	$('olt1').checked = true;
	changeModeOL();
	$('summer-pl-ol').selectedIndex = 0;
	$('winter-pl-ol').selectedIndex = 0;
	$('sq1').checked = true; $('sq1').onclick();
	$('wq1').checked = true; $('wq1').onclick();
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
		$('pl-records-tp1').options.selectedIndex = 0;
		$('pl-records-ct').update(tRcCtI[league]); updateTip('pl-records-ct'); updateSelectMult('pl-records-ct');
		$('pl-stats-yr').update(tStatsYr[league]); updateTip('pl-stats-yr'); updateSelectMult('pl-stats-yr');
		$('pl-stats-ct').update(tStatsCtI[league]); updateTip('pl-stats-ct'); updateSelectMult('pl-stats-ct');
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
		$('lrecords-pf').update(id == 'nfl' ? 'Include postseason/Super Bowl' : 'Include playoffs');
	}
}
function changeModeUS(id) {
	id = (id != null ? id : 'championships');
	currentUtype = id;
	['championships', 'records', 'stats', 'hof', 'retnum', 'teamstadiums'].each(function(id_) {
		$(id_).removeClassName('selected');	
		$('f-' + id_).hide();
	});
	$(id).addClassName('selected');
	$('f-' + id).show();
}
function changeRcType() {
	var league = (currentLeague == 'nfl' ? 1 : (currentLeague == 'nba' ? 2 : (currentLeague == 'nhl' ? 3 : 4)));
	$('pl-records-ct').update($('pl-records-tp1').value == 't' ? tRcCtT[league] : ($('pl-records-tp1').value == 'i' ? tRcCtI[league] : tRcCtA[league]));
	updateTip('pl-records-ct');
	updateSelectMult('pl-records-ct');
}
function changeStType() {
	var league = (currentLeague == 'nfl' ? 1 : (currentLeague == 'nba' ? 2 : (currentLeague == 'nhl' ? 3 : 4)));
	$('pl-stats-ct').update($('pl-stats-type').value == 't' ? tStatsCtT[league] : tStatsCtI[league]);
	updateTip('pl-stats-ct');
	updateSelectMult('pl-stats-ct');
}
function runUSLeagues() {
	t1 = currentTime();
	var league = (currentLeague == 'nfl' ? 1 : (currentLeague == 'nba' ? 2 : (currentLeague == 'nhl' ? 3 : 4)));
	var h = new Hash();
	h.set('league', league);
	h.set('type', currentUtype);
	h.set('tm', $('pl-' + currentUtype + '-tm') ? $F('pl-' + currentUtype + '-tm') : null);
	h.set('yr', $('pl-' + currentUtype + '-yr') ? $F('pl-' + currentUtype + '-yr') : null);
	h.set('ct', $('pl-' + currentUtype + '-ct') ? $F('pl-' + currentUtype + '-ct') : null);
	h.set('tp1', $('pl-' + currentUtype + '-tp1') ? $F('pl-' + currentUtype + '-tp1') : null);
	h.set('tp2', $('pl-' + currentUtype + '-tp2') ? $F('pl-' + currentUtype + '-tp2') : null);
	h.set('tpind', $('pl-stats-type').value == 'i' ? '1' : '0');
	h.set('tptm', $('pl-stats-type').value == 't' ? '1' : '0');
	h.set('pf', $('records-pf').checked ? '1' : '0');
	h.set('num', $F('retnum-number'));
	h.set('pos', $F('hof-position'));
	window.location.href = '/SearchUSLeagues?' + h.toQueryString();
}
function resetUSLeagues() {
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
}
function dpatternBlur() {
	if ($F('dpattern') == '') {
		$('dpattern').removeClassName('focus');
		$('dpattern').value = TX_SEARCH2;
	}
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
		var tScope = $('search-form')['scope'];
		var tScopeValue = [];
		$R(0, tScope.length - 1).each(function(i) {
			if (tScope[i].checked) {
				tScopeValue.push(tScope[i].value);
			}
		});
		var h = new Hash();
		h.set('pattern', $F('pattern'));
		h.set('max', $F('max') != '' ? $F('max') : '0');
		h.set('match', $F('match'));
		h.set('scope', tScopeValue.join(','));
		window.location.href = '/Search?' + h.toQueryString();
	}
}
function resetSearch() {
	$('pattern').setValue('').activate();
	$('match').checked = false;
	$('max').value = '100';
	$$('.scope input').each(function(id) {
		$(id).checked = true;
	});
}
/*============================
  ========== LOGIN =========== 
  ============================*/
function auth() {
	if ($F('username') == '') {
		$('username').focus();
	}
	else if ($F('password') == '') {
		$('password').focus();
	}
	else {
		$('flogin').submit();
	}
}
function rauth() {
	$('username').value = $('rlogin').value;
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
	$('rmsg').update('<div><img src="/img/db/loading.gif?6"/></div>').show();
	var h = $H();
	$$('.register input').each(function(el) {
		h.set(el.id, el.value);
	});
	h.set('rsports', tSp.join(','));
	new Ajax.Request('/LoginServlet?create', { onSuccess: function(response) {
		var s = response.responseText;
		if (!/ERR\|.*/.match(s)) {
			$('rmsg').update(TX_ACCOUNT_CREATED).removeClassName('error').addClassName('success').show();
			$('rlogin').focus();
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
var currentInputValue = null;
var currentAlias = null;
var currentId = null;
function addPhoto() {
	$('currentphotos').update('<div><img class="loading" src="/img/db/loading2.gif?6"/></div>');
	if ($F('emb') != '') { // Embedded
		var h = $H();
		h.set('embedded', '1');
		h.set('value', $F('emb'));
		h.set('source', $F('src'));
		new Ajax.Request('/ImageServlet?upload-photo&entity=' + currentAlias + '&id=' + currentId, {
			onSuccess: function(response){
				loadPhotos();
			},
			parameters: h
		});
	}
	else {
		if (fd != null) {
			dzd.options.url = '/ImageServlet?upload-photo&entity=' + currentAlias + '&id=' + currentId + '&source=' + escape($F('src'));
			dzd.processFile(fd);
			fd = null;
		}
		if (fr != null) {
			dzr.options.url = '/ImageServlet?upload-photo&entity=RS&id=' + tValues['id'] + '&source=' + escape($F('src'));
			dzr.processFile(fr);
			fr = null;
		}
	}
	$('dz-file').update('<p>' + TX_CLICK_DRAGDROP + '</p>');
	$('emb').value = '';
	$('src').value = '';
}
function loadPhotos() {
	$('currentphotos').update('<div><img class="loading" src="/img/db/loading2.gif?6"/></div>');
	new Ajax.Updater('currentphotos', '/ImageServlet?load&entity=' + currentAlias + '&id=' + currentId, {});
}
function removePhoto(id, name) {
	new Ajax.Request('/ImageServlet?remove-photo=1&id=' + id + '&name=' + name, {
		onSuccess: function(response){
			$('currentphoto-' + id).hide();
			$('currentphoto-rm-' + id).hide();
		}
	});
}
function showLoading() {
	$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
}
function showMessage(text) {
	$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
	$('msg').update('<div>' + text.replace(/ERR\:/i, '') + '</div>');
}
function showWarning() {
	$('msg').update('<div class="warning">' + TX_MODIF_WARNING + '</div>');
}
/*========== RESULTS ==========*/
var tValues = [];
var dzr = null;
var fr = null;
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
		if ($(el).type == 'button' || $(el).type == 'radio' || $(el).id == 'yrfind') {
			return;
		}
		$(el).value = $(el).name;
		$(el).title = $(el).name;
		$(el).addClassName('default');
		Event.observe($(el), 'focus', function(){
			if ($(this).value == $(this).name) {
				$(this).value = '';
			}
			currentInputValue = $(this).value;
			$(this).select();
		});
		Event.observe($(el), 'blur', function(){
			if (currentInputValue != $(this).value && $(this).id != 'emb' && $(this).id != 'src') {
				showWarning();
			}
			if ($(this).value == '') {
				$(this).removeClassName('completed').removeClassName('completed2');
				$(this).value = $(this).name;
				tValues[$(this).id] = null;
			}
			else if (!$(this).hasClassName('completed')) {
				$(this).addClassName('completed2');
			}
		});
	});
	dzr = new Dropzone($('dz-file'), {
		url: '/',
		paramName: 'photo-file',
		autoProcessQueue: false,
		addRemoveLinks: false});
	dzr.on('addedfile', function(f) {
		fr = f;
		$$('#imgzone p')[0].remove();
	});
	dzr.on("success", function(f) {
		loadPhotos();
	});
	addRounds();
	loadResValues(value);
	currentAlias = 'RS';
	$('fresults').style.width = '700px';
}
function loadResValues(value) {
	$('msg').update('');
	$('pagelink1').hide();
	$('pagelink2').hide();
	var t = value.split('~');
	if (t != null && t	.length > 1) {
		$('modifmode1').checked = true;
		$('shortcutdiv').show();
		$('treediv').style.marginTop = '85px';
		tValues['sp'] = t[0]; $('sp').value = t[1]; $('sp').addClassName('completed');
		tValues['cp'] = t[2]; $('cp').value = t[3]; $('cp').addClassName('completed');
		tValues['ev'] = t[4]; $('ev').value = t[5]; updateType('ev', t[6]); $('ev').addClassName('completed');
		if (t[7] != '') {tValues['se'] = t[7]; $('se').value = t[8]; updateType('se', t[9]); $('se').addClassName('completed');} else {$('se').value = $('se').name; $('se').removeClassName('completed');}
		if (t[10] != '') {tValues['se2'] = t[10]; $('se2').value = t[11]; updateType('se2', t[12]); $('se2').addClassName('completed');} else {$('se2').value = $('se2').name; $('se2').removeClassName('completed');}
		tValues['yr'] = t[13]; $('yr').value = t[14]; $('yr').addClassName('completed');
		addRounds(true);
		if (t.length > 16) {
			tValues['id'] = t[15];
			$('id').value = tValues['id'];
			currentId = tValues['id'];
			if (dzr != null) {
				$('dz-file').update('<p>' + TX_CLICK_DRAGDROP + '</p>');
				fr = null;
			}
			// Result Info
			tValues['dt1'] = t[16]; if (t[16] != '') {$('dt1').value = t[16]; $('dt1').addClassName('completed2');} else {$('dt1').value = $('dt1').name; $('dt1').removeClassName('completed2');}
			tValues['dt2'] = t[17]; if (t[17] != '') {$('dt2').value = t[17]; $('dt2').addClassName('completed2');} else {$('dt2').value = $('dt2').name; $('dt2').removeClassName('completed2');}
			tValues['pl1'] = t[18]; if (t[18] != '') {$('pl1').value = t[19]; $('pl1').addClassName('completed').removeClassName('completed2');} else {$('pl1').value = $('pl1').name; $('pl1').removeClassName('completed').removeClassName('completed2');}
			tValues['pl2'] = t[20]; if (t[20] != '') {$('pl2').value = t[21]; $('pl2').addClassName('completed').removeClassName('completed2');} else {$('pl2').value = $('pl2').name; $('pl2').removeClassName('completed').removeClassName('completed2');}
			tValues['exa'] = t[22]; if (t[22] != '') {$('exa').value = t[22]; $('exa').addClassName('completed2');} else {$('exa').value = $('exa').name; $('exa').removeClassName('completed2');}
			tValues['cmt'] = t[23]; if (t[23] != '') {$('cmt').value = t[23]; $('cmt').addClassName('completed2');} else {$('cmt').value = $('cmt').name; $('cmt').removeClassName('completed2');}
			$('metadata').update(t[24]);
			tValues['inact'] = t[25]; $('inact').checked = (t[25] == '1');
			tValues['inprogress'] = t[26]; $('inprogress').checked = (t[26] == '1');
			tValues['nodate'] = t[27]; $('nodate').checked = (t[27] == '1');
			tValues['noplace'] = t[28]; $('noplace').checked = (t[29] == '1');
			tValues['exl'] = t[29]; if (t[29] != '') {$('exl').value = t[29].replace(/\|/gi, '\r\n'); $('exl').addClassName('completed2');} else {$('exl').value = $('exl').name; $('exl').removeClassName('completed2');}
			// Rankings
			var j = 29;
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
			tValues['pldel'] = '';
			for (var i = 1 ; i <= pListCount ; i++) {
				tValues['rk' + i + 'list'] = null;
			}
			if (t[j + 1].indexOf('rkl') == 0) {
				j++;
				rkList = t[j].substring(4);
				var t_ = rkList.split('#');
				var t__ = null;
				var t___ = null;
				var idpl = null;
				var idp = null;
				var indexp = null;
				for (var i = 0 ; i < t_.length ; i++) {
					t__ = t_[i].split('|');
					t___ = [];
					for (var i_ = 0 ; i_ < t__.length ; i_++) {
						idpl = t__[i_].split(':')[0];
						idp = t__[i_].split(':')[1];
						indexp = t__[i_].split(':')[3];
						t___.push(idpl + ':' + idp + ':' + indexp);
					}
					while (t__.length > pListCount) {
						addPersonList();
					}
					tValues['rk' + (i + 1) + 'list'] = t___.join('|');
				}
			}
			else {
				rkList = null;
			}
			// Rounds
			var k = j + 1;
			var rdindex = 1;
			var trd = [];
			while (tValues['rd' + rdindex + 'rt']) {
				tValues['rd' + rdindex + 'rt'] = null;
				tValues['rd' + rdindex + 'rk1'] = null;
				tValues['rd' + rdindex + 'rk2'] = null;
				tValues['rd' + rdindex + 'rk3'] = null;
				tValues['rd' + rdindex + 'rk4'] = null;
				tValues['rd' + rdindex + 'rk5'] = null;
				tValues['rd' + rdindex + 'pl1'] = null;
				tValues['rd' + rdindex + 'pl2'] = null;
				rdindex++;
			}
			tValues['rddel'] = '';
			rdindex = 1;
			while (t[k]) {
				if (t[k].indexOf('rd-') == 0) {
					var t_ = t[k].substring(3).split('|');
					$('rd' + rdindex + 'id').value = t_[0];
					tValues['rd' + rdindex + 'rt'] = t_[1];
					$('rd' + rdindex + 'rt').value = t_[2]; $('rd' + rdindex + 'rt').addClassName('completed');
					tValues['rd' + rdindex + 'rk1'] = t_[3];
					if (t_[4] != '') {$('rd' + rdindex + 'rk1').value = t_[4]; $('rd' + rdindex + 'rk1').addClassName('completed');} else {$('rd' + rdindex + 'rk1').value = $('rd' + rdindex + 'rk1').name; $('rd' + rdindex + 'rk1').removeClassName('completed').removeClassName('completed2');}
					if (t_[5] != '') {$('rd' + rdindex + 'rs1').value = t_[5]; $('rd' + rdindex + 'rs1').addClassName('completed2');} else {$('rd' + rdindex + 'rs1').value = $('rd' + rdindex + 'rs1').name; $('rd' + rdindex + 'rs1').removeClassName('completed2');}
					tValues['rd' + rdindex + 'rk2'] = t_[6];
					if (t_[7] != '') {$('rd' + rdindex + 'rk2').value = t_[7]; $('rd' + rdindex + 'rk2').addClassName('completed');} else {$('rd' + rdindex + 'rk2').value = $('rd' + rdindex + 'rk2').name; $('rd' + rdindex + 'rk2').removeClassName('completed').removeClassName('completed2');}
					if (t_[8] != '') {$('rd' + rdindex + 'rs2').value = t_[8]; $('rd' + rdindex + 'rs2').addClassName('completed2');} else {$('rd' + rdindex + 'rs2').value = $('rd' + rdindex + 'rs2').name; $('rd' + rdindex + 'rs2').removeClassName('completed2');}
					tValues['rd' + rdindex + 'rk3'] = t_[9];
					if (t_[10] != '') {$('rd' + rdindex + 'rk3').value = t_[10]; $('rd' + rdindex + 'rk3').addClassName('completed');} else {$('rd' + rdindex + 'rk3').value = $('rd' + rdindex + 'rk3').name; $('rd' + rdindex + 'rk3').removeClassName('completed').removeClassName('completed2');}
					if (t_[11] != '') {$('rd' + rdindex + 'rs3').value = t_[11]; $('rd' + rdindex + 'rs3').addClassName('completed2');} else {$('rd' + rdindex + 'rs3').value = $('rd' + rdindex + 'rs3').name; $('rd' + rdindex + 'rs3').removeClassName('completed2');}
					tValues['rd' + rdindex + 'rk4'] = t_[12];
					if (t_[13] != '') {$('rd' + rdindex + 'rk4').value = t_[13]; $('rd' + rdindex + 'rk4').addClassName('completed');} else {$('rd' + rdindex + 'rk4').value = $('rd' + rdindex + 'rk4').name; $('rd' + rdindex + 'rk4').removeClassName('completed').removeClassName('completed2');}
					if (t_[14] != '') {$('rd' + rdindex + 'rs4').value = t_[14]; $('rd' + rdindex + 'rs4').addClassName('completed2');} else {$('rd' + rdindex + 'rs4').value = $('rd' + rdindex + 'rs4').name; $('rd' + rdindex + 'rs4').removeClassName('completed2');}
					tValues['rd' + rdindex + 'rk5'] = t_[15];
					if (t_[16] != '') {$('rd' + rdindex + 'rk5').value = t_[16]; $('rd' + rdindex + 'rk5').addClassName('completed');} else {$('rd' + rdindex + 'rk5').value = $('rd' + rdindex + 'rk5').name; $('rd' + rdindex + 'rk5').removeClassName('completed').removeClassName('completed2');}
					if (t_[17] != '') {$('rd' + rdindex + 'rs5').value = t_[17]; $('rd' + rdindex + 'rs5').addClassName('completed2');} else {$('rd' + rdindex + 'rs5').value = $('rd' + rdindex + 'rs5').name; $('rd' + rdindex + 'rs5').removeClassName('completed2');}
					if (t_[18] != '') {$('rd' + rdindex + 'dt1').value = t_[18]; $('rd' + rdindex + 'dt1').addClassName('completed2');} else {$('rd' + rdindex + 'dt1').value = $('rd' + rdindex + 'dt1').name; $('rd' + rdindex + 'dt1').removeClassName('completed2');}
					if (t_[19] != '') {$('rd' + rdindex + 'dt2').value = t_[19]; $('rd' + rdindex + 'dt2').addClassName('completed2');} else {$('rd' + rdindex + 'dt2').value = $('rd' + rdindex + 'dt2').name; $('rd' + rdindex + 'dt2').removeClassName('completed2');}
					tValues['rd' + rdindex + 'pl1'] = t_[20];
					if (t_[21] != '') {$('rd' + rdindex + 'pl1').value = t_[21]; $('rd' + rdindex + 'pl1').addClassName('completed');} else {$('rd' + rdindex + 'pl1').value = $('rd' + rdindex + 'pl1').name; $('rd' + rdindex + 'pl1').removeClassName('completed').removeClassName('completed2');}
					tValues['rd' + rdindex + 'pl2'] = t_[22];
					if (t_[23] != '') {$('rd' + rdindex + 'pl2').value = t_[23]; $('rd' + rdindex + 'pl2').addClassName('completed');} else {$('rd' + rdindex + 'pl2').value = $('rd' + rdindex + 'pl2').name; $('rd' + rdindex + 'pl2').removeClassName('completed').removeClassName('completed2');}
					if (t_[24] != '') {$('rd' + rdindex + 'exa').value = t_[24]; $('rd' + rdindex + 'exa').addClassName('completed2');} else {$('rd' + rdindex + 'exa').value = $('rd' + rdindex + 'exa').name; $('rd' + rdindex + 'exa').removeClassName('completed2');}
					if (t_[25] != '') {$('rd' + rdindex + 'cmt').value = t_[25]; $('rd' + rdindex + 'cmt').addClassName('completed2');} else {$('rd' + rdindex + 'cmt').value = $('rd' + rdindex + 'cmt').name; $('rd' + rdindex + 'cmt').removeClassName('completed2');}
					trd.push(replaceAll(t[k].substring(3), '|', '~'));
				}
				else {
					break;
				}
				if (rdindex % 10 == 0) {
					addRounds();
				}
				rdindex++;
				k++;
			}
			// Other info
			var l = k;
			tValues['rdlist'] = trd.join('|');
			if (t[l] != '') {
				$('pagelink1').href = '/results/' + t[l];
				$('pagelink1').show();
				l++;
				$('pagelink2').href = '/result/' + t[l];
				$('pagelink2').show();
			}
			l++;
			$('otherids').update();
			if (t[l] != '') {
				var t_ = t[l].split(',');
				if (t_.length > 1) {
					var toid = [];
					for (var i = 0 ; i < t_.length ; i++) {
						toid.push('<a href="javascript:tValues[\'id\']=' + t_[i] + ';loadResult(\'direct\');"' + (tValues['id'] == t_[i] ? ' style="font-weight:bold;"' : '') + '>(' + (i + 1) + ')</a>');
					}
					$('otherids').update(toid.join(' '));	
				}
			}
			loadPhotos();
		}
	}
}
function clearValue(s, focus) {
	var t = s.split(',');
	for (var i = 0 ; i < t.length ; i++) {
		s = t[i];
		tValues[s] = '';
		$(s).value = '';
		if ($(s + '-l')) {
			s = s + '-l';
			$(s).value = '';
		}
		$(s).removeClassName('completed').removeClassName('completed2');
		$(s).value = $(s).name;
	}
	if (focus) {
		$(s).focus();
	}
	showWarning();
}
function clearFieldset(s) {
	$$('#' + s + ' input').each(function(s_){
		clearValue(s_.id);
	});
	if (s == 'rounds') {
		for (var i = 1 ; i <= rdCount ; i++) {
			deleteRound(i);
		}
	}
	else if (s == 'rankings') {
		$('rk1').focus();
	}
}
function setValue(text, li) {
	var t = li.id.split('|');
	if (t[0].indexOf('-') != -1) { // Data
		$(t[0]).value = t[1];
	}
	else { // Result
		tValues[text.id] = t[1];
		$(text).removeClassName('completed2').addClassName('completed');
		if (t.length > 2) {
			updateType(t[0], t[2]);
		}
	}
	var index = text.value.indexOf('[#');
	if (index != -1) {
		text.value = text.value.substring(0, index - 1);	
	}
	showWarning();
}
var currentTp = null;
function updateType(s, tp) {
	if ((tValues['se2'] != null && s == 'se2') || (tValues['se'] != null && tValues['se2'] == null && s == 'se') || (tValues['ev'] != null && tValues['se'] == null && tValues['se2'] == null && s == 'ev')) {
		currentTp = parseInt(tp);
	}
	['rk1', 'rk2', 'rk3', 'rk4', 'rk5', 'rk6', 'rk7', 'rk8', 'rk9', 'rk10', 'rk11', 'rk12', 'rk13', 'rk14', 'rk15', 'rk16', 'rk17', 'rk18', 'rk19', 'rk20'].each(function(s){
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
	['id', 'sp', 'cp', 'ev', 'se', 'se2', 'yr', 'yrfind'].each(function(s){
		h.set(s, tValues[s]);
	});
	new Ajax.Request('/update/load', {
		onSuccess: function(response){
			var text = response.responseText;
			if (text != '') {
				tValues['ev'] = null;
				tValues['se'] = null;
				tValues['se2'] = null;
				loadResValues(text);
			}
		},
		parameters: h
	});
	$('yrfind').value = '';
}
function saveResult() {
	showLoading();
	if ($('addmode1').checked == true) {
		tValues['id'] = null;
		$('id').value = '';
	}
	var h = $H({sp: tValues['sp']});
	var t = ['id', 'sp', 'cp', 'ev', 'se', 'se2', 'yr', 'dt1', 'dt2', 'pl1', 'pl2', 'exa', 'cmt', 'img', 'exl'];
	for (var i = 1 ; i <= 20 ; i++) {
		t.push('rk' + i);
		t.push('rs' + i);
	}
	for (var i = 1 ; i <= pListCount ; i++) {
		t.push('rk' + i + 'list');
	}
	saveRounds();
	t.push('rdlist');
	t.push('rddel');
	t.push('pldel');
	t.each(function(s){
		h.set(s, tValues[s]);
		if ($(s) && ($(s).hasClassName('completed') || $(s).hasClassName('completed2'))) {
			h.set(s + "-l", $F(s));
		}
	});
	h.set('inact', $('inact').checked ? '1' : '0');
	h.set('inprogress', $('inprogress').checked ? '1' : '0');
	h.set('nodate', $('nodate').checked ? '1' : '0');
	h.set('noplace', $('noplace').checked ? '1' : '0');
	new Ajax.Request('/update/save', {
		onSuccess: function(response){
			var text = response.responseText;
			if (text.indexOf('ERR:') == -1) {
				tValues['id'] = text.split('#')[0];
				$('id').value = tValues['id'];
				showMessage(text.split('#')[1]);
			}
			else {
				showMessage(text);
			}
		},
		parameters: h
	});
}
function deleteResult() {
	setOpacity(0.4);
	dQuestion.open();
	$('confirmtxt').update(TX_CONFIRM + ' ?');
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		showLoading();
		var h = $H({id: tValues['id']});
		new Ajax.Request('/update/delete', {
			onSuccess: function(response){
				var text = response.responseText;
				showMessage(text);
			},
			parameters: h
		});
		setOpacity(1.0);
		dQuestion.close();
	});
}
function loadDataTip(type) {
	$('datatip').show();
	$('datatip').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('datatip'), '/update/data/' + type);
}
var rkList = null;
var pListIndex = null;
var pListCount = 20;
function setInput(id) {
	Event.stopObserving($(id), 'blur');
	Event.stopObserving($(id), 'keydown');
	var isAjax = /(plist.*|.*(rt|rk\d+|pl|pl1|pl2)$)/.match(id);
	isAjax &= (id.indexOf('plist') == -1 || id.indexOf('-index') == -1);
	if (isAjax) {
		var url = null;
		if (/.*(rt|pl|pl1|pl2)$/.match(id)) {
			url = '/update/ajax/' + (/.*rt$/.match(id) ? 'rt' : 'pl1');
		}
		else {
			url = '/update/ajax/' + (currentTp < 10 || id.indexOf('plist') == 0 ? 'pr' : (currentTp == 50 ? 'tm' : 'cn')) + (tValues['sp'] != null ? '-' + tValues['sp'] : '');
		}
		new Ajax.Autocompleter(
			id,
			'ajaxsearch' + (id.indexOf('plist') == 0 ? '2' : (id.indexOf('rddlg') == 0 ? '3' : '')),
			url,
			{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
		);
	}
	if ($(id).value == '') {
		$(id).value = $(id).name;
	}
	$(id).addClassName('default');
	Event.observe($(id), 'focus', function(){
		if ($(this).value == $(this).name) {
			$(this).value = '';
		}
		$(this).select();
	});
	Event.observe($(id), 'blur', function(){
		if ($(this).value == '') {
			$(this).value = $(this).name;
			$(this).removeClassName('completed').removeClassName('completed2');
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
	var plid = null;
	var pid = null;
	var ptxt = null;
	var pindex = null;
	for (var i = 1 ; i <= pListCount ; i++) {
		if (t && t[i - 1]) {
			plid = t[i - 1].split(':')[0];
			pid = t[i - 1].split(':')[1];
			ptxt = t[i - 1].split(':')[2];
			pindex = t[i - 1].split(':')[3];
		}
		else {
			plid = null;
			pid = null;
			ptxt = '';
			pindex = '';
		}
		tValues['plist_id' + i] = (plid && plid != null ? plid : 0);
		tValues['plist' + i] = pid;
		html.push('<tr id="plrow' + i + '"><td><a href="javascript:deletePersonList(' + i + ');"><img title="' + TX_REMOVE + '" src="/img/delete.gif"/></a></td><td><input type="text" id="plist' + i + '-index" tabindex="' + (100000 + i) + '" name="Index" style="width:50px;" class="' + (pindex != null && pindex != '' ? 'completed2' : '') + '" value="' + pindex + '"/></td><td><input type="text" id="plist' + i + '" tabindex="' + (100001 + i) + '" name="Name #' + i + '" class="' + (pid != null ? 'completed' : '') + '" value="' + ptxt + '"/><a href="javascript:clearValue(\'plist' + i + '\', true);">[X]</a></td></tr>');	
	}
	$('plist').update('<table>' + html.join('') + '</table>');
	$$('#plist input').each(function(el){
		setInput(el.id);
	});
	pListIndex = index;
	$('plist-title').update('[' + index + ']');
	dPersonList.open();
	setOpacity(0.4);
}
function savePersonList() {
	var t = [];
	var t_ = [];
	var val1 = null;
	var val2 = null;
	for (var i = 1 ; i <= pListCount ; i++) {
		if ($('plist' + i + '-index')) {
			val1 = tValues['plist_id' + i];
			val2 = tValues['plist' + i];
			t.push(val1 + ':' + (val2 && val2 != '' ? val2 : $('plist' + i).value) + ':' + $('plist' + i + '-index').value.replace('Index', ''));
			if (val2 && val2 != '' && val2.indexOf('Name #') == -1) {
				t_.push(val1 + ':' + val2 + ':' + $('plist' + i).value + ':' + $('plist' + i + '-index').value.replace('Index', ''));	
			}
		}
	}
	tValues['rk' + pListIndex + 'list'] = t.join('|');
	var t__ = [];
	if (rkList != null) {
		t__ = rkList.split('#');
		t__[pListIndex - 1] = t_.join('|');
	}
	rkList = t__.join('#');
	dPersonList.close();
	setOpacity(1.0);
}
function addPersonList() {
	try {
		for (var i = pListCount + 1 ; i <= pListCount + 10 ; i++) {
			$$('#plist table')[0].insert('<tr id="plrow' + i + '"><td><a href="javascript:deletePersonList(' + i + ');"><img title="' + TX_REMOVE + '" src="/img/delete.gif"/></a></td><td><input type="text" id="plist' + i + '-index" tabindex="' + (100000 + i) + '" name="Index" style="width:50px;"/></td><td><input type="text" id="plist' + i + '" tabindex="' + (100001 + i) + '" name="Name #' + i + '"/><a href="javascript:clearValue(\'plist' + i + '\', true);">[X]</a></td></tr>');
			setInput('plist' + i);
			setInput('plist' + i + '-index');
			tValues['plist_id' + i] = 0;
		}
	}
	catch(err){}
	pListCount += 10;
}
function deletePersonList(index) {
	if (tValues['plist_id' + index] != '') {
		tValues['pldel'] = (tValues['pldel'] ? tValues['pldel'] + '|' : '') + tValues['plist_id' + index];
	}
	$('plrow' + index).remove();
}
var rdCount = 0;
var rdDlgCurrent = 0;
function addRounds(clear) {
	try {
		var rtable = $$('#rounds table')[0];
		if (clear) {
			rtable.update();
			rdCount = 0;
		}
		var html = null;
		for (var i = rdCount + 1 ; i <= rdCount + 10 ; i++) {
			html = ['<tr id="rdrow' + i + '">'];
			html.push('<td><a href="javascript:deleteRound(' + i + ');"><img title="' + TX_REMOVE + '" src="/img/delete.gif"/></a></td>');
			html.push('<td><a href="javascript:openRoundDialog(' + i + ');"><img title="' + TX_OPEN_DIALOG + '" src="/img/update/dialog.png"/></a></td>');
			html.push('<td><input type="hidden" id="rd' + i + 'id"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'rt" tabindex="' + (1000 + (16*(i-1))) + '" name="' + TX_TYPE + '" style="width:150px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'rk1" tabindex="' + (1001 + (16*(i-1))) + '" name="' + TX_RANK1 + '" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'rk1\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'rs1" tabindex="' + (1002 + (16*(i-1))) + '" name="' + TX_RESULT_SCORE + '" style="width:90px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'rk2" tabindex="' + (1003 + (16*(i-1))) + '" name="' + TX_RANK2 + '" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'rk2\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'rs2" tabindex="' + (1004 + (16*(i-1))) + '" name="' + TX_RESULT + '" style="width:90px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'rk3" tabindex="' + (1005 + (16*(i-1))) + '" name="' + TX_RANK3 + '" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'rk3\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'rs3" tabindex="' + (1006 + (16*(i-1))) + '" name="' + TX_RESULT + '" style="width:90px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'rk4" tabindex="' + (1007 + (16*(i-1))) + '" name="' + TX_RANK4 + '" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'rk4\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'rs4" tabindex="' + (1008 + (16*(i-1))) + '" name="' + TX_RESULT + '" style="width:90px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'rk5" tabindex="' + (1009 + (16*(i-1))) + '" name="' + TX_RANK5 + '" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'rk5\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'rs5" tabindex="' + (1010 + (16*(i-1))) + '" name="' + TX_RESULT + '" style="width:90px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'dt1" tabindex="' + (1011 + (16*(i-1))) + '" name="' + TX_DATE + ' #1" style="width:80px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'dt2" tabindex="' + (1012 + (16*(i-1))) + '" name="' + TX_DATE + ' #2" style="width:80px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'pl1" tabindex="' + (1013 + (16*(i-1))) + '" name="' + TX_PLACE + ' #1" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'pl1\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'pl2" tabindex="' + (1014 + (16*(i-1))) + '" name="' + TX_PLACE + ' #2" style="width:200px;"/><a href="javascript:clearValue(\'rd' + i + 'pl2\', true);">[X]</a></td>');
			html.push('<td><input type="text" id="rd' + i + 'exa" tabindex="' + (1015 + (16*(i-1))) + '" name="' + TX_TIE + '" style="width:50px;"/></td>');
			html.push('<td><input type="text" id="rd' + i + 'cmt" tabindex="' + (1016 + (16*(i-1))) + '" name="' + TX_COMMENT + '" style="width:150px;"/></td>');
			html.push('</tr>');
			rtable.insert(html.join(''));
		}
		$$('#rounds input').each(function(el){
			setInput($(el).id);
		});
	}
	catch(err){}
	rdCount += 10;
}
function deleteRound(index) {
	if ($('rdrow' + index)) {
		tValues['rddel'] = (tValues['rddel'] ? tValues['rddel'] + '|' : '') + $('rd' + index + 'id').value;
		$('rdrow' + index).remove();	
	}
}
function saveRounds() {
	var t = [];
	var t_ = [];
	var rt = null;
	var rtl = null;
	for (var i = 1 ; i <= rdCount ; i++) {
		rt = tValues['rd' + i + 'rt'];
		rtl = $('rd' + i + 'rt');
		if (rtl && (rtl.hasClassName('completed') || rtl.hasClassName('completed2'))) {
			t_ = [$('rd' + i + 'id').value];
			t_.push(rt);
			t_.push(rtl.value);
			t_.push(tValues['rd' + i + 'rk1']);
			t_.push($('rd' + i + 'rk1').hasClassName('completed2') ? $('rd' + i + 'rk1').value : '');
			t_.push($('rd' + i + 'rs1').hasClassName('completed2') ? $('rd' + i + 'rs1').value : '');
			t_.push(tValues['rd' + i + 'rk2']);
			t_.push($('rd' + i + 'rk2').hasClassName('completed2') ? $('rd' + i + 'rk2').value : '');
			t_.push($('rd' + i + 'rs2').hasClassName('completed2') ? $('rd' + i + 'rs2').value : '');
			t_.push(tValues['rd' + i + 'rk3']);
			t_.push($('rd' + i + 'rk3').hasClassName('completed2') ? $('rd' + i + 'rk3').value : '');
			t_.push($('rd' + i + 'rs3').hasClassName('completed2') ? $('rd' + i + 'rs3').value : '');
			t_.push(tValues['rd' + i + 'rk4']);
			t_.push($('rd' + i + 'rk4').hasClassName('completed2') ? $('rd' + i + 'rk4').value : '');
			t_.push($('rd' + i + 'rs4').hasClassName('completed2') ? $('rd' + i + 'rs4').value : '');
			t_.push(tValues['rd' + i + 'rk5']);
			t_.push($('rd' + i + 'rk5').hasClassName('completed2') ? $('rd' + i + 'rk5').value : '');
			t_.push($('rd' + i + 'rs5').hasClassName('completed2') ? $('rd' + i + 'rs5').value : '');
			t_.push($('rd' + i + 'dt1').hasClassName('completed2') ? $('rd' + i + 'dt1').value : '');
			t_.push($('rd' + i + 'dt2').hasClassName('completed2') ? $('rd' + i + 'dt2').value : '');
			t_.push(tValues['rd' + i + 'pl1']);
			t_.push($('rd' + i + 'pl1').value);
			t_.push(tValues['rd' + i + 'pl2']);
			t_.push($('rd' + i + 'pl2').value);
			t_.push($('rd' + i + 'exa').hasClassName('completed2') ? $('rd' + i + 'exa').value : '');
			t_.push($('rd' + i + 'cmt').hasClassName('completed2') ? $('rd' + i + 'cmt').value : '');
			t.push(t_.join('~'));
		}
	}
	tValues['rdlist'] = t.join('|');
}
function openRoundDialog(index, opened) {
	rdDlgCurrent = index;
	$$('#dlg-round input', '#dlg-round textarea').each(function(el){
		setInput($(el).id);
	});
	index = 'rd' + index;
	var text = null;
	$('rddlg-id').value = $(index + 'id').value;
	['rt', 'rk1', 'rk2', 'rk3', 'rk4', 'rk5', 'pl1', 'pl2', 'rs1', 'rs2', 'rs3', 'rs4', 'rs5', 'dt1', 'dt2', 'exa', 'cmt'].each(function(s){
		if (/(rt|rk|pl).*$/.match(s)) {
			tValues['rddlg-' + s] = tValues[index + s];
			if (tValues['rddlg-' + s] && tValues['rddlg-' + s] != '') {
				$('rddlg-' + s).addClassName('completed');
			}
			else {
				$('rddlg-' + s).removeClassName('completed').removeClassName('completed2');
			}
		}
		text = $(index + s).value;
		if (s == 'cmt') {
			text = text.replace(/\¨/g, '\r\n');
		}
		$('rddlg-' + s).value = text;
		if (!$('rddlg-' + s).hasClassName('completed')) {
			if ($('rddlg-' + s).value != '' && text != $(index + s).name) {
				$('rddlg-' + s).addClassName('completed2');
			}
			else {
				$('rddlg-' + s).removeClassName('completed').removeClassName('completed2');
			}
		}
	});
	$('round-title').update('[' + rdDlgCurrent + ']');
	if (!opened) {
		setOpacity(0.4);
		dRound.open();
	}
}
function moveRound(index) {
	if (rdDlgCurrent + index < 1) {
		index = 0;
	}
	openRoundDialog(rdDlgCurrent + index, 1);
}
function copyRStoRD() {
	['rk1', 'rk2', 'rk3', 'rk4', 'rk5', 'pl2', 'rs1', 'rs2', 'rs3', 'rs4', 'rs5', 'dt2', 'exa'].each(function(s){
		if (/(rt|rk|pl).*$/.match(s)) {
			tValues['rddlg-' + s] = tValues[s];
			if (tValues['rddlg-' + s] && tValues['rddlg-' + s] != '') {
				$('rddlg-' + s).addClassName('completed');
			}
			else {
				$('rddlg-' + s).removeClassName('completed').removeClassName('completed2');
			}
		}
		text = $(s).value;
		$('rddlg-' + s).value = text;
		if (!$('rddlg-' + s).hasClassName('completed')) {
			if ($('rddlg-' + s).value != '' && text != $(s).name) {
				$('rddlg-' + s).addClassName('completed2');
			}
			else {
				$('rddlg-' + s).removeClassName('completed').removeClassName('completed2');
			}
		}
	});
}
function setRoundValues() {
	var index = 'rd' + rdDlgCurrent;
	var text = null;
	['rt', 'rk1', 'rk2', 'rk3', 'rk4', 'rk5', 'pl1', 'pl2', 'rs1', 'rs2', 'rs3', 'rs4', 'rs5', 'dt1', 'dt2', 'exa', 'cmt'].each(function(s){
		if (/(rt|rk|pl).*$/.match(s)) {
			tValues[index + s] = tValues['rddlg-' + s];
			if (tValues[index + s] && tValues[index + s] != '') {
				$(index + s).addClassName('completed');
			}
			else {
				$(index + s).removeClassName('completed').removeClassName('completed2');
			}
		}
		text = $('rddlg-' + s).value;
		if (s == 'cmt') {
			text = text.replace(/[\n\r]/g, '¨');
		}
		$(index + s).value = text;
		if (!$(index + s).hasClassName('completed')) {
			if ($(index + s).value != '' && text != $('rddlg-' + s).name) {
				$(index + s).addClassName('completed2');
			}
			else {
				$(index + s).removeClassName('completed').removeClassName('completed2');
			}
		}
	});
	setOpacity(1.0);
	dRound.close();
}
function openCommentDialog() {
	if ($('cmt').hasClassName('completed2')) {
		var t = $('cmt').value.replace(/\|/g, '\r\n').split('·');
		var tooltip = (t[0].indexOf('##') == -1);
		$('cmt-en').value = t[0].replace('##', '');
		$('cmt-fr').value = (t.length > 1 ? t[1].replace('##', '') : '');
		if (/^EN\:/.match($('cmt-en').value)) {
			$('cmt-en').value = $('cmt-en').value.substring(3);
			$('cmt-fr').value = $('cmt-fr').value.substring(3);
		}
		$('cmtmode1').checked = (tooltip ? true : false);
		$('cmtmode2').checked = (tooltip ? false : true);
	}
	else {
		$('cmt-en').value = '';
		$('cmt-fr').value = '';
		$('cmtmode1').checked = true;
		$('cmtmode2').checked = false;
	}
	setOpacity(0.4);
	dComment.open();
}
function saveComment() {
	var prefix = ($('cmtmode1').checked ? '' : '##');
	var cmt = ($('cmt-en').value != '' ? 'EN:' + prefix + $('cmt-en').value : '');
	cmt += (cmt != '' ? '·' : '') + ($('cmt-fr').value != '' ? 'FR:' + prefix + $('cmt-fr').value : '');
	cmt = cmt.replace(/[\n\r]/g, '|');
	if (cmt != '') {
		$('cmt').value = cmt;
		$('cmt').addClassName('completed2');
	}
	else {
		$('cmt').value = $('cmt').name;
		$('cmt').removeClassName('completed2');
	}
	closeDialog(dComment);
}
/*========== DATA ==========*/
var isMerge = null;
var isCopyPic = null;
var dzd = null;
var fd = null;
function initUpdateData() {
	$$('#update-data input', '#update-data textarea').each(function(el){
		if ($(el).id.lastIndexOf('-l') == $(el).id.length - 2) {
			new Ajax.Autocompleter(
				$(el).id,
				'ajaxsearch',
				'/update/ajax/' + $(el).id,
				{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
			)
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
			$(this).removeClassName('completed').addClassName('completed2');
			if ($(this).id != 'emb' && $(this).id != 'src') {
				showWarning();	
			}
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
			if ($(this).id == 'rc-type1') {
				updateRecordType($(this).value);
			}
		});
	});
	dzd = new Dropzone($('dz-file'), {
		url: '/',
		paramName: 'photo-file',
		autoProcessQueue: false,
		addRemoveLinks: false});
	dzd.on("addedfile", function(f) {
		fd = f;
		$$('#imgzone p')[0].remove();
	});
	dzd.on("success", function(f) {
		loadPhotos();
	});
	showPanel('PR');
}
function updateRecordType(tp) {
	['rc-rank1-l', 'rc-rank2-l', 'rc-rank3-l', 'rc-rank4-l', 'rc-rank5-l'].each(function(s){
		Event.stopObserving($(s), 'blur');
		Event.stopObserving($(s), 'keydown');
		new Ajax.Autocompleter(
			s,
			'ajaxsearch',
			'/update/ajax/' + (tp.toLowerCase() == 'individual' ? 'pr' : 'tm') + '-' + s,
			{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
		);
		Event.observe($(s), 'blur', function(){
			if ($(this).value == '') {
				$(this).value = $(this).name;
			}
			else {
				$(this).addClassName('completed');
			}
		});
	});
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
	if (dzd != null) {
		$('dz-file').update('<p>' + TX_CLICK_DRAGDROP + '</p>');
	}
	if ($('table-exl')) {
		$('table-exl').hide();
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
		$('exl').value = t[i++];
		$('table-exl').show();
	}
	else if (currentAlias == 'CL') {
		$('cl-id').value = currentId;
		$('cl-sport').value = t[i++];
		$('cl-sport-l').value = t[i++];
		$('cl-championship').value = t[i++];
		$('cl-championship-l').value = t[i++];
		$('cl-event').value = t[i++];
		$('cl-event-l').value = t[i++];
		$('cl-subevent').value = t[i++];
		$('cl-subevent-l').value = t[i++];
		$('cl-subevent2').value = t[i++];
		$('cl-subevent2-l').value = t[i++];
		$('cl-complex').value = t[i++];
		$('cl-complex-l').value = t[i++];
		$('cl-city').value = t[i++];
		$('cl-city-l').value = t[i++];
		$('cl-country').value = t[i++];
		$('cl-country-l').value = t[i++];
		$('cl-date1').value = t[i++];
		$('cl-date2').value = t[i++];
	}
	else if (currentAlias == 'CP') {
		$('cp-id').value = currentId;
		$('cp-label').value = t[i++];
		$('cp-labelfr').value = t[i++];
		$('cp-index').value = t[i++];
		$('exl').value = t[i++];
		$('table-exl').show();
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
		$('exl').value = t[i++];
		$('table-exl').show();
	}
	else if (currentAlias == 'CX') {
		$('cx-id').value = currentId;
		$('cx-label').value = t[i++];
		$('cx-city').value = t[i++];
		$('cx-city-l').value = t[i++];
		$('cx-link').value = t[i++];
		$('cx-link-l').value = t[i++];
		$('exl').value = t[i++];
		$('table-exl').show();
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
		$('exl').value = t[i++];
		$('table-exl').show();
	}
	else if (currentAlias == 'EV') {
		$('ev-id').value = currentId;
		$('ev-label').value = t[i++];
		$('ev-labelfr').value = t[i++];
		$('ev-type').value = t[i++];
		$('ev-type-l').value = t[i++];
		$('ev-index').value = t[i++];
		$('exl').value = t[i++];
		$('table-exl').show();
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
		$('exl').value = t[i++];
		$('table-exl').show();
	}
	else if (currentAlias == 'OR_') {
		$('or-id').value = currentId;
		$('or-olympics').value = t[i++];
		$('or-olympics-l').value = t[i++];
		$('or-country').value = t[i++];
		$('or-country-l').value = t[i++];
		$('or-gold').value = t[i++];
		$('or-silver').value = t[i++];
		$('or-bronze').value = t[i++];
	}
	else if (currentAlias == 'RT') {
		$('rt-id').value = currentId;
		$('rt-label').value = t[i++];
		$('rt-labelfr').value = t[i++];
		$('rt-index').value = t[i++];
	}
	else if (currentAlias == 'SP') {
		$('sp-id').value = currentId;
		$('sp-label').value = t[i++];
		$('sp-labelfr').value = t[i++];
		$('sp-type').value = t[i++];
		$('sp-index').value = t[i++];
		$('exl').value = t[i++];
		$('table-exl').show();
	}
	else if (currentAlias == 'ST') {
		$('st-id').value = currentId;
		$('st-label').value = t[i++];
		$('st-labelfr').value = t[i++];
		$('st-code').value = t[i++];
		$('st-capital').value = t[i++];
		$('exl').value = t[i++];
		$('table-exl').show();
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
		$('exl').value = t[i++];
		$('table-exl').show();
	}
	else if (currentAlias == 'YR') {
		$('yr-id').value = currentId;
		$('yr-label').value = t[i++];
	}
	else if (currentAlias == 'HF') {
		$('hf-id').value = currentId;
		$('hf-league').value = t[i++];
		$('hf-league-l').value = t[i++];
		$('hf-year').value = t[i++];
		$('hf-year-l').value = t[i++];
		$('hf-person').value = t[i++];
		$('hf-person-l').value = t[i++];
		$('hf-position').value = t[i++];
	}
	else if (currentAlias == 'RC') {
		$('rc-id').value = currentId;
		$('rc-sport').value = t[i++];
		$('rc-sport-l').value = t[i++];
		$('rc-championship').value = t[i++];
		$('rc-championship-l').value = t[i++];
		$('rc-event').value = t[i++];
		$('rc-event-l').value = t[i++];
		$('rc-subevent').value = t[i++];
		$('rc-subevent-l').value = t[i++];
		$('rc-type1').value = t[i++];
		$('rc-type2').value = t[i++];
		$('rc-city').value = t[i++];
		$('rc-city-l').value = t[i++];
		$('rc-label').value = t[i++];
		$('rc-rank1').value = t[i++];
		$('rc-rank1-l').value = t[i++];
		$('rc-record1').value = t[i++];
		$('rc-date1').value = t[i++];
		$('rc-rank2').value = t[i++];
		$('rc-rank2-l').value = t[i++];
		$('rc-record2').value = t[i++];
		$('rc-date2').value = t[i++];
		$('rc-rank3').value = t[i++];
		$('rc-rank3-l').value = t[i++];
		$('rc-record3').value = t[i++];
		$('rc-date3').value = t[i++];
		$('rc-rank4').value = t[i++];
		$('rc-rank4-l').value = t[i++];
		$('rc-record4').value = t[i++];
		$('rc-date4').value = t[i++];
		$('rc-rank5').value = t[i++];
		$('rc-rank5-l').value = t[i++];
		$('rc-record5').value = t[i++];
		$('rc-date5').value = t[i++];
		$('rc-counting').value = t[i++];
		$('rc-index').value = t[i++];
		$('rc-tie').value = t[i++];
		$('rc-comment').value = t[i++];
		updateRecordType($F('rc-type1'));
	}
	else if (currentAlias == 'RN') {
		$('rn-id').value = currentId;
		$('rn-league').value = t[i++];
		$('rn-league-l').value = t[i++];
		$('rn-team').value = t[i++];
		$('rn-team-l').value = t[i++];
		$('rn-person').value = t[i++];
		$('rn-person-l').value = t[i++];
		$('rn-year').value = t[i++];
		$('rn-year-l').value = t[i++];
		$('rn-number').value = t[i++];
	}
	else if (currentAlias == 'TS') {
		$('ts-id').value = currentId;
		$('ts-league').value = t[i++];
		$('ts-league-l').value = t[i++];
		$('ts-team').value = t[i++];
		$('ts-team-l').value = t[i++];
		$('ts-complex').value = t[i++];
		$('ts-complex-l').value = t[i++];
		$('ts-date1').value = t[i++];
		$('ts-date2').value = t[i++];
		$('ts-renamed').value = t[i++];
	}
	else if (currentAlias == 'WL') {
		$('wl-id').value = currentId;
		$('wl-league').value = t[i++];
		$('wl-league-l').value = t[i++];
		$('wl-team').value = t[i++];
		$('wl-team-l').value = t[i++];
		$('wl-type').value = t[i++];
		$('wl-win').value = t[i++];
		$('wl-loss').value = t[i++];
		$('wl-tie').value = t[i++];
		$('wl-otloss').value = t[i++];
	}
	if (currentAlias == 'PR' || currentAlias == 'CT' || currentAlias == 'CX') {
		loadPhotos();
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
	if ($('exl').value != '') {
		$('exl').addClassName('completed2');
	}
	else {
		$('exl').removeClassName('completed2');
	}
	['pr-link-l', 'ct-link-l', 'cx-link-l', 'tm-link-l'].each(function(el){
		if (el.toUpperCase().indexOf(currentAlias) == 0) {
			Event.stopObserving($(el), 'keyup');
			new Ajax.Autocompleter(
				$(el).id,
				'ajaxsearch',
				'/update/ajax/' + $(el).id + '~' + currentId,
				{ paramName: 'value', minChars: 2, frequency: 0.05, afterUpdateElement: setValue}
			)
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
	showLoading();
	var h = $H({alias: currentAlias, id: currentId});
	$$('#table-' + currentAlias + ' input').each(function(el){
		if ($(el).id.lastIndexOf('-l') < $(el).id.length - 2) {
			h.set($(el).id, ($(el).type == 'checkbox' ? $(el).checked : $(el).value));
		}
	});
	h.set('exl', $('exl').value);
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
	setOpacity(0.4);
	dQuestion.open();
	$('confirmtxt').update(TX_CONFIRM + ' ?');
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		showLoading();
		var h = $H({alias: currentAlias, id: currentId});
		new Ajax.Request('/update/delete-entity', {
			onSuccess: function(response){
				var text = response.responseText;
				loadEntity('previous');
				showMessage(text);
			},
			parameters: h
		});
		setOpacity(1.0);
		dQuestion.close();
	});
}
function copyEntity() {
	$(currentAlias.toLowerCase() + '-id').value = '';
	currentId = null;
}
function mergeEntity(id1_, id2_) {
	setOpacity(0.4);
	dQuestion.open();
	new Ajax.Updater($('confirmtxt'), '/update/merge?confirm=1&id1=' + id1_ + '&id2=' + id2_ + '&alias=' + currentAlias);
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		showLoading();
		var h = $H({alias: currentAlias, id1: id1_, id2: id2_});
		new Ajax.Request('/update/merge', {
			onSuccess: function(response){
				showMessage(response.responseText);
			},
			parameters: h
		});
		setOpacity(1.0);
		dQuestion.close();
	});
}
function findEntity(m) {
	isMerge = (m == 1);
	isCopyPic = (m == 2);
	setOpacity(0.4);
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
					setOpacity(1.0);
					dFind.close();
					if (currentAlias == 'RS') {
						tValues['id'] = id;
						loadResult('direct');
					}
					else if (isMerge) {
						mergeEntity(currentId, id);
					}
					else if (isCopyPic) {
						copyPicture(currentId, id);
					}
					else if ($('update-pictures')) {
						loadPictures('direct', id);
						$('sport').selectedIndex = 0;
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
	var h = $H({entity: $F('oventity'), sport: $F('ovsport'), count: $F('ovcount'), pattern: $F('ovpattern'), showimg: ($('showimg').checked ? '1' : '0')});
	$('ovcontent').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('ovcontent'), '/update/load-overview', {
		parameters: h
	});
}
function setOverviewFlag(flag, id) {
	new Ajax.Request('/update/set-overview-flag?flag=' + flag + '&id=' + id);
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
		$('remove-local').show();
	});
	dzp.on('success', function(f, text) {
		loadPictures('direct');
		$('year1').value = '';
		$('year2').value = '';
	});
	changePictureType();
}
function changePictureType() {
	currentAlias = $F('type');
	loadPictures('last');
	if (currentAlias == 'CP' || currentAlias == 'EV' || currentAlias == 'TM') {
		$('spcell1').show();
		$('spcell2').show();
	}
	else {
		$('spcell1').hide();
		$('spcell2').hide();
	}
}
function removeLocalPicture() {
	dzp.removeFile(fp);
	$('name-local').update();
	$('remove-local').hide();
}
function uploadPicture() {
	if (fp != null) {
		var sp = $F('sport');
		var alias_ = currentAlias;
		var id_ = currentId;
		if ((alias_ == 'CP' || alias_ == 'EV') && sp != '') {
			alias_ = 'SP' + alias_;
			id_ = sp + "-" + id_;
		}
		dzp.options.url = '/ImageServlet?upload=1&entity=' + alias_ + '&id=' + id_ + '&size=' + ($('size1').checked ? 'L' : 'S') + '&y1=' + $F('year1') + '&y2=' + $F('year2');
		dzp.processFile(fp);
	}
}
function downloadPicture() {
	if ($F('list-remote') != null) {
		window.open(IMG_URL + $F('list-remote'), '_blank');
	}
}
function deletePicture() {
	if ($F('list-remote') != null) {
		if (confirm('Confirm ?')) {
			new Ajax.Request('/ImageServlet?remove-pic=1&name=' + $F('list-remote'), {
				onSuccess: function(response){
					loadPictures('direct');
				}
			});
		}
	}
}
function loadPicture() {
	$('img-remote').update('<img alt="-" src="' + ($F('list-remote') != null ? IMG_URL + $F('list-remote') : '/img/noimage.png') + '"/>');
}
function loadPictures(action_, id_) {
	var h = $H({action: action_, alias: currentAlias, id: (id_ ? id_ : currentId), sp: $F('sport')});
	new Ajax.Request('/update/load-entity?t=' + currentTime(), {
		onSuccess: function(response){
			if (response.responseText == '') {
				return;
			}
			var t = response.responseText.split('~');
			$('label-remote').update(t[1] + (currentAlias == 'TM' ? '<span style="font-weight:normal;font-style:italic;"> - ' + t[3] + '</span>' : ''));
			$('nopic').checked = (t[t.length - 1] == '1');
			currentId = t[0];
			var sp = $F('sport');
			var alias_ = currentAlias;
			var id__ = currentId;
			if ((alias_ == 'CP' || alias_ == 'EV') && sp != '') {
				alias_ = 'SP' + alias_;
				id__ = sp + "-" + id__;
			}
			var h_ = $H({entity: alias_, id: id__, size: ($('size1').checked ? 'L' : 'S')});
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
function copyPicture(id1_, id2_) {
	var h = $H({entity: currentAlias, id1: id1_, id2: id2_});
	new Ajax.Request('/ImageServlet?copy=1', {
		onSuccess: function(response){
			loadPictures('direct', id2_);
		},
		parameters: h
	});
}
function setNopic() {
	var h = $H({entity: currentAlias, id: currentId, value: ($('nopic').checked ? '1' : '0')});
	new Ajax.Request('/ImageServlet?nopic=1', {
		onSuccess: function(response){},
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
		if (/html$/.match(text)) {
			$('report').update("<span style='color:green;font-weight:bold;'>" + TX_IMPORT_COMPLETE.replace('{1}', '/update/execute-import?file=' + text) + "</span>");
		}
		else {
			$('report').update(text);
			$('updatebtn').disabled = (text.indexOf(TX_ERROR.toUpperCase() + ":") > -1);
		}
	});
}
function executeImport(u) {
	if (fi != null) {
		dzi.options.url = '/update/execute-import?type=' + $F('type') + '&update=' + u;
		dzi.processFile(fi);
		setTimeout(checkImportProgress, 250);
		$('progressbar').show();
	}
}
function checkImportProgress() {
	new Ajax.Request('check-progress-import?t=' + currentTime(), {
		onSuccess: function(response){
			var t = response.responseText.split('|');
			var pg = t[2];
			$('pgpercent').update(pg + ' % (' + t[0] + ' / ' + t[1] + ')');
			$('progress').style.width = (pg * 2) + 'px';
			if (parseInt(pg) < 100) {
				setTimeout(checkImportProgress, 250);
			}
		}
	});
}
function loadTemplate(ext) {
	location.href = '/update/load-template?type=' + $F('type') + '&ext=' + ext;
}
/*========== QUERY ==========*/
function executeQuery(index) {
	var isYearReport = (index == 2);
	var output = (isYearReport ? 'ovcontent' : 'qresults')
	var url = '/update/execute-query?index=' + index + (!isYearReport && $('rcsv') && $('rcsv').checked ? '&csv=1' : '') + (isYearReport ? '&year=' + $F('year') + '&sport=' + $F('ovsport') : '');
	if (index == -1) {
		url += '&query=' + escape($F('query'));
	}
	if (!isYearReport && $('rcsv').checked) {
		location.href = url;
	}
	else {
		$(output).update('<img src="/img/db/loading.gif?6"/>');
		new Ajax.Request(url, {
			onSuccess: function(response){
				$(output).update(response.responseText);
				var q = $(output).down('td').innerHTML;
				q = replaceAll(q, '&nbsp;', ' ');
				q = replaceAll(q, '&lt;', '<');
				q = replaceAll(q, '&gt;', '>');
				$('query').value = q;
			}
		});
	}
}
/*========== EXT.LINKS ==========*/
var currentExtLinkEntity = null;
function loadExtLinks() {
	var h = $H({sport: $F('elsport'), count: $F('elcount'), idmax: $F('elidmax'), pattern: $F('elpattern'), entity: $F('elentity'), includechecked: ($('elincludechecked').checked ? '1' : '0')});
	$('elcontent').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('elcontent'), '/update/load-extlinks', {
		parameters: h
	});
	currentExtLinkEntity = $F('elentity');
	$('msg').update();
}
function checkAllLinks() {
	$$('#elcontent input').each(function(el){
		$(el).checked = true;
	});
}
function addExtLink(id) {
	var t = [];
	var t_ = $$('#el-' + id + ' td');
	t.push('<tr><td style="display:none;">0</td>');
	t.push('<td>' + t_[1].innerHTML + '</td>');
	t.push('<td>' + t_[2].innerHTML + '</td>');
	t.push('<td><input type="text" style="width:60px;"/></td>');
	t.push('<td><input type="text" style="width:500px;"/></td>');
	t.push('<td><input type="checkbox"/></td><td/></tr>');	
	$('el-' + id).insert({
		after: t.join('')
	});
}
function modifyExtLink(id) {
	var t = $$('#el-' + id + ' td');
	t[3].innerHTML = '<input type="text" value="' + t[3].innerHTML + '" style="width:60px;"/>';
	t[4].innerHTML = '<input type="text" value="' + t[4].down('a').href + '" style="width:500px;"/>';
}
function removeExtLink(id) {
	var t = $$('#el-' + id + ' td');
	new Ajax.Request('/update/delete-extlink', {
		onSuccess: function(response){
			$('el-' + id).remove();
		},
		parameters: $H({id: t[0].innerHTML})
	});
}
function saveExtLinks() {
	var t = [];
	$$('#elcontent tbody tr').each(function(el){
		var t_ = $(el).getElementsByTagName('input');
		var t__ = $(el).getElementsByTagName('td');
		var idlink = t__[0].innerHTML;
		var iditem = t__[1].innerHTML;
		if (t_.length == 1) {
			t.push(idlink + '~' + (t_[0].checked ? '1' : '0'));
		}
		else if (t_[0].value != '') {
			t.push(idlink + '~' + iditem + '~' + t_[0].value + '~' + t_[1].value + '~' + (t_[2].checked ? '1' : '0'));
		}
	});
	new Ajax.Request('/update/save-extlinks', {
		onSuccess: function(response){
			var text = response.responseText;
			$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
			$('msg').update('<div>' + text.replace(/^ERR\:/i, '') + '</div>');
			loadExtLinks();
		},
		parameters: $H({entity: currentExtLinkEntity, value: t.join('|')})
	});
}
function updateLinksAuto() {
	setOpacity(0.4);
	dQuestion.open();
	$('confirmtxt').update(TX_CONFIRM + ' ?');
	Event.stopObserving($('confirmbtn'), 'click');
	Event.observe($('confirmbtn'), 'click', function(){
		setOpacity(1.0);
		dQuestion.close();
		$('elcontent').update('<img src="/img/db/loading.gif?6"/>');
		var h = $H({sport: $F('elsport'), count: $F('elcount'), idmax: $F('elidmax'), pattern: $F('elpattern'), entity: $F('elentity'), includechecked: ($('elincludechecked').checked ? '1' : '0')});
		new Ajax.Request('/update/updateauto-extlinks', {
			onSuccess: function(response){
				var text = response.responseText;
				$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
				$('msg').update('<div>' + text.replace(/^ERR\:/i, '') + '</div>');
				loadExtLinks();
			},
			parameters: h
		});
	});
}
/*========== TRANSLATIONS ==========*/
var currentTranslationEntity = null;
function loadTranslations() {
	var h = $H({range: $F('trrange'), pattern: $F('trpattern'), entity: $F('trentity'), includechecked: ($('trincludechecked').checked ? '1' : '0')});
	$('trcontent').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('trcontent'), '/update/load-translations', {
		parameters: h
	});
	currentTranslationEntity = $F('trentity');
}
function checkAllTranslations() {
	$$('#trcontent input').each(function(el){
		$(el).checked = true;
	});
}
function saveTranslations() {
	var t = [];
	$$('#trcontent tbody tr').each(function(el){
		var cb = $(el).getElementsByTagName('input')[0];
		var t_ = $(el).getElementsByTagName('td');
		t.push(t_[0].innerHTML + '~' + t_[1].innerHTML + '~' + (cb.checked ? '1' : '0'));
	});
	new Ajax.Request('/update/save-translations', {
		onSuccess: function(response){
			var text = response.responseText;
			$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
			$('msg').update('<div>' + text.replace(/^ERR\:/i, '') + '</div>');
			loadTranslations();
		},
		parameters: $H({entity: currentTranslationEntity, value: t.join('|')})
	});
}
/*========== ERRORS ==========*/
function loadErrors() {
	$('ercontent').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('ercontent'), '/update/load-errors');
}
function removeError(id) {
	if (confirm('Confirm ?')) {
		$('ercontent').update('<img src="/img/db/loading.gif?6"/>');
		new Ajax.Request('/update/remove-error', {
			onSuccess: function(response){
				loadErrors();
			},
			parameters: $H({value: id})
		});
		new Ajax.Updater($('ercontent'), '/update/remove-error');
	}
}
/*========== REDIRECTIONS ==========*/
function loadRedirections() {
	$('recontent').update('<img src="/img/db/loading.gif?6"/>');
	new Ajax.Updater($('recontent'), '/update/load-redirections');
}
function saveRedirections() {
	var t = [];
	$$('#recontent tbody tr').each(function(el){
		var t_ = $(el).getElementsByTagName('input');
		var t__ = $(el).getElementsByTagName('td');
		var idredirect = t__[0].innerHTML;
		t.push(idredirect + '~' + t_[0].value + '~' + t_[1].value);
	});
	new Ajax.Request('/update/save-redirections', {
		onSuccess: function(response){
			var text = response.responseText;
			$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
			$('msg').update('<div>' + text.replace(/^ERR\:/i, '') + '</div>');
			loadRedirections();
		},
		parameters: $H({value: t.join('|')})
	});
}
function addRedirection(id) {
	var t = [];
	//var t_ = $$('#re-' + id + ' td');
	t.push('<tr><td style="display:none;">0</td>');
	t.push('<td><input type="text" style="width:450px;"/></td>');
	t.push('<td><input type="text" style="width:450px;"/></td></tr>');
	$('re-' + id).insert({
		after: t.join('')
	});
}
/*========== ADMIN ==========*/
function saveConfig() {
	$('msg').update('<div><img src="/img/db/loading.gif?6"/></div>');
	var h = $H();
	$$('#tconfig input, #tconfig textarea').each(function(el){
		h.set('p_' + $(el).id, $(el).value);
	});
	h.set('new', $('configadd').value);
	new Ajax.Request('/update/save-config', {
		onSuccess: function(response){
			var text = response.responseText;
			$('msg').style.color = (text.indexOf('ERR:') > -1 ? '#F00' : '#0A0');
			$('msg').update('<div>' + text.replace(/^ERR\:/i, '') + '</div>');
		},
		parameters: h
	});
}
/*============================
  ========== MOBILE ========== 
  ============================*/
function toggleSearchM(){
	$('shmenu').removeClassName('displayed');
	$('headertop').toggleClassName('displayed');
	$('dpattern').focus();
}
function toggleMenuM(){
	$('headertop').removeClassName('displayed');
	$('shmenu').toggleClassName('displayed');
}