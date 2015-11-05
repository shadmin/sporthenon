<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<!-- Report Error -->
<div id="d-error" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("report.error", session).toUpperCase()%></div>
	<div class="fscontent"><%=StringUtils.text("page.address", session)%>:<br/><input id="errlinkurl" disabled="disabled" style="width:650px;margin-top:3px;"/><br/><br/>
	<%=StringUtils.text("error.description", session)%>:<br/><textarea id="errlinktext" rows="10" cols="80" style="width:650px;margin-top:3px;"></textarea></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="saveError();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dError);"/></div>
</div>
</div>
<!-- Link -->
<div id="d-link" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.link", session).toUpperCase()%></div>
	<div class="fscontent"><%=StringUtils.text("direct.address", session)%>:<br/><input id="linktxt" type="text" readonly="readonly" onclick="this.select();"/><br/>(<%=StringUtils.text("use.ctrl.C", session)%>)</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dLink);"/></div>
</div>
</div>
<!-- Info -->
<div id="d-info" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("dialog.info", session).toUpperCase()%></div>
	<div class="fscontent"><%=StringUtils.text("info.statistics", session)%>:<table style="width:600px;margin-top:8px;">
		<tr><th><%=StringUtils.text("address", session)%></th><td></td></tr><tr><th><%=StringUtils.text("size", session)%></th><td></td></tr>
		<tr><th><%=StringUtils.text("display.time", session)%></th><td></td></tr><tr><th><%=StringUtils.text("pictures", session)%></th><td></td></tr>
	</table></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dInfo);"/></div>
</div>
</div>
<!-- Person List -->
<div id="d-plist" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Person List</div>
	<div class="fscontent" style="width:400px;height:300px;overflow:auto;"><div id="ajaxsearch2" class="ajaxsearch"></div><div id="plist"></div><a href="javascript:addPersonList();">+10</a></div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="savePersonList();"/><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dPersonList);"/></div>
</div>
</div>
<!-- Find -->
<div id="d-find" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("find", session).toUpperCase()%></div>
	<div class="fscontent"><input type="text" id="fpattern" onkeydown="if(event.keyCode==13){searchEntity();}"/><div id="fresults"></div></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("cancel", session)%>" onclick="closeDialog(dFind);"/></div>
</div>
</div>
<!-- Confirm -->
<div id="d-question" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle"><%=StringUtils.text("confirm", session).toUpperCase()%></div>
	<div class="fscontent"><div id="confirmtxt"></div></div>
	<div class="dlgbuttons"><input type="button" class="button cancel" value="<%=StringUtils.text("no", session)%>" onclick="closeDialog(dQuestion);"/><input id="confirmbtn" type="button" class="button ok" value="<%=StringUtils.text("yes", session)%>"/></div>
</div>
</div>
<!-- Help -->
<div id="d-help" class="dialog" style="display:none;">
<div class="fieldset">
	<div class="fstitle">Help</div>
	<div class="fscontent" style="width:850px;height:400px;overflow:auto;">
	<h3>1. Codes pays/équipes</h3>
	<a href="javascript:loadDataTip('country');"><%=StringUtils.text("country.codes", session)%></a><br/><br/>
	<a href="javascript:loadDataTip('state');"><%=StringUtils.text("country.states", session)%></a><br/><br/>
	<a href="javascript:loadDataTip('team');"><%=StringUtils.text("entity.TM", session)%></a>
	<div id="datatip" style="display:none;"></div>
	<h3>2. Format des champs</h3>
	<table><tr><th>Champ</th><th>Format</th><th>Exemples</th><th style="width:300px;">Commentaires</th></tr>
	<tr><td>Sport</td><td><b>NomSport</b></td><td><i>Football</i></td><td></td></tr>
	<tr><td>Championnat</td><td><b>NomChampionnat</b></td><td><i>Championnats du monde</i></td><td></td></tr>
	<tr><td>Epreuve</td><td><b>NomEpreuve (TypeEpreuve)</b></td><td><i>Hommes (Individuel)</i></td><td>Types d'épreuve :<ul><li>Individuel</li><li>Doubles</li></ul></td></tr>
	<tr><td>Année</td><td><b>AAAA</b></td><td><i>2001</i></td><td>Numéro de l'année sur 4 chiffres</td></tr>
	<tr><td>Date #1</td><td><b>JJ/MM/AAAA</b></td><td><i>01/12/2001<br/>12 Décembre 2001</i></td><td>Date de début de l'épreuve (<span class="underline">vide si épreuve sur 1 seule journée</span>)</td></tr>
	<tr><td>Date #2</td><td><b>JJ/MM/AAAA</b></td><td>Voir Date #1</td><td>Date de fin de l'épreuve (renseigner uniquement ce champ si épreuve sur 1 journée)</td></tr>
	<tr><td>Lieu/Ville&nbsp;#1</td><td><b>[NomComplexe,&nbsp;]NomVille[,&nbsp;CodeEtatUS], CodePays</b></td><td><i>Paris,&nbsp;FRA<br/>Los Angeles,&nbsp;CA,&nbsp;USA<br/>Stade&nbsp;Maracana,&nbsp;Rio&nbsp;de&nbsp;Janeiro,&nbsp;BRA</i></td><td>Lieu de départ/début (<span class="underline">vide si épreuve disputée à 1 seul endroit</span>)</td></tr>
	<tr><td>Lieu/Ville&nbsp;#2</td><td><b>[NomComplexe,&nbsp;]NomVille[,&nbsp;CodeEtatUS], CodePays</b></td><td>Voir Lieu/Ville #1</td><td>Lieu d'arrivée/fin (<span class="underline">renseigner uniquement ce champ si épreuve disputée à 1 seul endroit</span>)</td></tr>
	<tr><td>Ex-aequo</td><td><b>NuméroPosition1&#8209;NuméroPosition2[/...]</b></td><td><i>1&#8209;2<br/>1&#8209;3/4&#8209;5</i></td><td></td></tr>
	<tr><td>Commentaire</td><td><b>TexteCommentaire</b></td><td><i>L. Armstrong disqualified (doping)</i></td><td>Utiliser le caractère | pour les retours à la ligne</td></tr>
	<tr><td>1er,&nbsp;2ème,&nbsp;3ème...</td><td><b>NomAthlete[,&nbsp;PrenomAthlete]&nbsp;(CodePays[,&nbsp;NomEquipe])</b>&nbsp;ou<br/><b>NomEquipe[&nbsp;(CodePays)]</b>&nbsp;ou<br/><b>NomPays</b></td><td><i>Nadal,&nbsp;Rafael&nbsp;(ESP)<br/>Gretzky,&nbsp;Wayne&nbsp;(CAN,&nbsp;Edmonton&nbsp;Oilers)<br/>Juventus&nbsp;FC&nbsp;(ITA)<br/>Etats-Unis</i></td><td>Classement de l'épreuve</td></tr>
	<tr><td>URL photo</td><td><b>AdresseHttpImage</b></td><td><i>-</i></td><td>Adresse internet d'une photo illustrant l'épreuve</td></tr>
	<tr><td>Liens&nbsp;externes</td><td><b>AdresseHttpLien1<br/>[AdresseHttpLien2<br/>AdresseHttpLien3]</b></td><td><i>-</i></td><td></td></tr>
	</table>
	</div>
	<div class="dlgbuttons"><input type="button" class="button ok" value="<%=StringUtils.text("ok", session)%>" onclick="closeDialog(dHelp);"/></div>
</div>
</div>
<script type="text/javascript"><!--
dError = new Control.Modal($('d-error'),{ closeOnClick: false, fade: false });
dLink = new Control.Modal($('d-link'),{ closeOnClick: false, fade: false });
dInfo = new Control.Modal($('d-info'),{ closeOnClick: false, fade: false });
dPersonList = new Control.Modal($('d-plist'),{ closeOnClick: false, fade: false });
dFind = new Control.Modal($('d-find'),{ closeOnClick: false, fade: false });
dQuestion = new Control.Modal($('d-question'),{ closeOnClick: false, fade: false });
dHelp = new Control.Modal($('d-help'),{ closeOnClick: false, fade: false });
--></script>