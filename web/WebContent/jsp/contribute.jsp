<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.sporthenon.utils.StringUtils" %>
<jsp:include page="/jsp/common/header.jsp" />
<div id="contribute">
<div class="fieldset">
	<div class="fstitle info">À PROPOS DE LA CONTRIBUTION</div>
	<div class="fscontent">
		SPORTHENON est entièrement gratuit, tant pour la consultation que pour la modification.<br/><br/>
		Les données ajoutées sont vérifiées régulièrement par les administrateurs.
	</div>
</div>
<div class="fieldset">
	<div class="fstitle help">COMMENT CONTRIBUER ?</div>
	<div class="fscontent">
		Voici les étapes à suivre pour devenir contributeur et ajouter/modifier des résultats dans Sporthenon :<br/><br/>
		<b>1. Compte utilisateur</b><hr/>
		<ul><li>Cliquer sur le lien&nbsp;<span style="background-image:url(../img/menu/login.png);"><a href="/login"><u>Connexion</u></a></span>&nbsp;en haut de la page</li>
		<li>Saisir les informations obligatoires (marquées par un <font style="font-weight:bold;color:#F00;">*</font>)</li>
		<li>Cliquer sur le bouton <span style="background-image:url(../img/db/ok.png);"><u>Créer un compte</u></span></li>
		<li>Un message apparaît confirmant la création du compte</li>
		</ul>
		<br/><b>2. Espace contributeur</b><hr/>
		<ul><li>Une fois connecté, cliquer sur le lien &nbsp;<span style="background-image:url(../img/menu/contributor_area.png);"><a href="/update/overview"><u>Espace contributeur</u></a></span>&nbsp;en haut de la page</li>
		<li>Pour ajouter ou modifier un résultat, cliquer sur le bouton&nbsp;<span style="background-image:url(../img/update/results.png);"><a href="/update/results"><u>Résultats</u></a></li>
		<li></li></ul>
		<br/><b>3. Wiki/questions</b><hr/>
		<ul><li>Les différent modules de l'espace contributeur</li></ul>
	</div>
</div>
</div>
<jsp:include page="/jsp/common/footer.jsp" />