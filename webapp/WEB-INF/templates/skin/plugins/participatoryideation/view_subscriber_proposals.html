
<h1 class="visuallyhidden">Page listant les projets que j'ai déposés ou auxquels je me suis associé.</h1>

<div id="my-projects-container" class="container">

	<@messages infos=infos />
	<@messages errors=errors />

	<#-- *********************************************************************************** -->
	<#-- * DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES * -->
	<#-- * DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES DEPOSES * -->
	<#-- *********************************************************************************** -->

	<div id="resultSubmited">
	
		<h2>#i18n{participatoryideation.proposalsSubscriber.submitted.projects}</h2>
		
		<div class="row row-eq-height">

			<#if !projectsSubmitted?has_content >
				<div class="col-xs-12">
					<p class="empty-list">Il semble que vous n'ayez pas déposé de projet.</p>
				</div>
			<#else>
			
				<#list projectsSubmitted as proposal>
					<#assign theme_class = "autre">
					<#if proposal.codeTheme??>					
						<#if     proposal.codeTheme = "CADRE">											<#assign theme_class = "cadre_vie">
						<#elseif proposal.codeTheme = "CULTURE">										<#assign theme_class = "culture">
						<#elseif proposal.codeTheme = "ECO">											<#assign theme_class = "economie_emploi">
						<#elseif proposal.codeTheme = "EDU">											<#assign theme_class = "education">
						<#elseif proposal.codeTheme = "ENVIRONNEMENT" || proposal.codeTheme = "NAT">		<#assign theme_class = "environnement">
						<#elseif proposal.codeTheme = "LOGEMENT">										<#assign theme_class = "logement">
						<#elseif proposal.codeTheme = "PARTICIPATION">									<#assign theme_class = "participation_citoyenne">
						<#elseif proposal.codeTheme = "PROPRETE">										<#assign theme_class = "proprete">
						<#elseif proposal.codeTheme = "SOLIDARITE" || proposal.codeTheme = "VIVREENSEMBLE">	<#assign theme_class = "solidarite">
						<#elseif proposal.codeTheme = "SPORT">											<#assign theme_class = "sport">
						<#elseif proposal.codeTheme = "TRANSPORT">										<#assign theme_class = "transport">
						<#elseif proposal.codeTheme = "NUMERIQUE">										<#assign theme_class = "ville_numerique">
						<#elseif proposal.codeTheme = "SANTE">											<#assign theme_class = "sante">
						<#elseif proposal.codeTheme = "SECURITE">										<#assign theme_class = "prevention_securite">
						</#if>
					</#if>
					
					<#if proposal.codeTheme??>
						<#assign campaign_year = "Budget Participatif " + (2014 + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"?index_of(proposal.codeCampaign))>
					</#if>
					
					<#if (proposal.codeTheme)??>
						<#assign theme_libelle = (global_static[proposal.codeCampaign].themes_map[proposal.codeTheme].title)!proposal.codeTheme>
					</#if>
					
					<#assign url_proposal = "jsp/site/Portal.jsp?page=proposal&campaign="+proposal.codeCampaign+"&proposal="+proposal.codeProposal>
					<#assign url_proposal = "jsp/site/Portal.jsp?page=proposal&campaign="+proposal.codeCampaign+"&proposal="+proposal.codeProposal>

					<div class="col-xs-12 col-sm-4 col-md-3 prop-panel">
						<div class="prop-card bordered-4px-theme-${theme_class}">
							
							<#-- Thématique -->
							<div class="prop-card-theme bgcolor-theme-${theme_class}">
								<img src="images/local/skin/i2_${theme_class}.png">
								<span>${theme_libelle}</span>
							</div>

							<#-- Location -->
							<div class="prop-card-loc">
								<img src="images/local/skin/marker_1.png" class="img-responsive pull-left" alt="" title="">
								<#if proposal.locationType == "paris"  >Projet « Tout Paris »<#elseif proposal.locationType == "ardt"  ><#if proposal.adress??>Projet d'arrondissement<#else>${(arrondissements_map[proposal.locationArdt])!proposal.locationArdt}</#if></#if>
							</div>
							
							<#-- Titre -->
							<div class="prop-card-titre">
								<h3>${proposal.titre!''}</h3>
							</div>

							<#-- Campaign -->
							<div class="prop-card-campaign">
								<p class="pull-right">
									${campaign_year}
								</p>
							</div>

							<#-- Ligne séparatrice colorée -->
							<div class="prop-card-line bgcolor-theme-${theme_class}"></div>

							<div class="buttons-wrapper">
							
								<a href="${url_proposal}" alt="${proposal.titre}" title="${proposal.titre}" class="btn btn-12rem btn-black-on-white">Consulter le projet</a>
	
								<#if proposal.canDelete()>
									<div class="prop-card-delete">
										 <a class="btn btn-12rem btn-black-on-white" href="jsp/site/Portal.jsp?page=myProjects&action=actionDeleteProposal&proposal_id=${proposal.id}">
											 <i class='fa fa-trash-o'></i> Supprimer
										 </a>
									</div>
								</#if>

							</div>

						</div>
					</div>
				</#list>
			</#if>
		</div>
	</div>
	
	<#-- *********************************************************************************** -->
	<#-- * ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE * -->
	<#-- * ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE ASSOCIE * -->
	<#-- *********************************************************************************** -->

	<div id="resultSubscribed">
	
		<h2> #i18n{participatoryideation.proposalsSubscriber.participate.projects}</h2>
		
		<div class="row row-eq-height">

			<#if !projectsParticipate?has_content >
				<div class="col-xs-12">
					<p class="empty-list">Il semble que vous ne vous soyez pas associé à un projet.</p>
				</div>
			<#else>

				<#list projectsParticipate as proposal>
					<#assign theme_class = "autre">
					<#if proposal.codeTheme??>					
						<#if     proposal.codeTheme = "CADRE">											<#assign theme_class = "cadre_vie">
						<#elseif proposal.codeTheme = "CULTURE">										<#assign theme_class = "culture">
						<#elseif proposal.codeTheme = "ECO">											<#assign theme_class = "economie_emploi">
						<#elseif proposal.codeTheme = "EDU">											<#assign theme_class = "education">
						<#elseif proposal.codeTheme = "ENVIRONNEMENT" || proposal.codeTheme = "NAT">		<#assign theme_class = "environnement">
						<#elseif proposal.codeTheme = "LOGEMENT">										<#assign theme_class = "logement">
						<#elseif proposal.codeTheme = "PARTICIPATION">									<#assign theme_class = "participation_citoyenne">
						<#elseif proposal.codeTheme = "PROPRETE">										<#assign theme_class = "proprete">
						<#elseif proposal.codeTheme = "SOLIDARITE" || proposal.codeTheme = "VIVREENSEMBLE">	<#assign theme_class = "solidarite">
						<#elseif proposal.codeTheme = "SPORT">											<#assign theme_class = "sport">
						<#elseif proposal.codeTheme = "TRANSPORT">										<#assign theme_class = "transport">
						<#elseif proposal.codeTheme = "NUMERIQUE">										<#assign theme_class = "ville_numerique">
						<#elseif proposal.codeTheme = "SANTE">											<#assign theme_class = "sante">
						<#elseif proposal.codeTheme = "SECURITE">										<#assign theme_class = "prevention_securite">
						</#if>
					</#if>
					
					<#if proposal.codeTheme??>
						<#assign campaign_year = "Budget Participatif " + (2014 + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"?index_of(proposal.codeCampaign))>
					</#if>
					
					<#if (proposal.codeTheme)??>
						<#assign theme_libelle = (global_static[proposal.codeCampaign].themes_map[proposal.codeTheme].title)!proposal.codeTheme>
					</#if>
					
					<#assign url_proposal = "jsp/site/Portal.jsp?page=proposal&campaign="+proposal.codeCampaign+"&proposal="+proposal.codeProposal>
					<#assign url_proposal = "jsp/site/Portal.jsp?page=proposal&campaign="+proposal.codeCampaign+"&proposal="+proposal.codeProposal>

					<div class="col-xs-12 col-sm-4 col-md-3 prop-panel">
						<div class="prop-card bordered-4px-theme-${theme_class}">
							
							<#-- Thématique -->
							<div class="prop-card-theme bgcolor-theme-${theme_class}">
								<img src="images/local/skin/i2_${theme_class}.png">
								<span>${theme_libelle}</span>
							</div>

							<#-- Location -->
							<div class="prop-card-loc">
								<img src="images/local/skin/marker_1.png" class="img-responsive pull-left" alt="" title="">
								<#if proposal.locationType == "paris"  >Projet « Tout Paris »<#elseif proposal.locationType == "ardt"  ><#if proposal.adress??>Projet d'arrondissement<#else>${(arrondissements_map[proposal.locationArdt])!proposal.locationArdt}</#if></#if>
							</div>
							
							<#-- Titre -->
							<div class="prop-card-titre">
								<h3>${proposal.titre!''}</h3>
							</div>

							<#-- Campaign -->
							<div class="prop-card-campaign">
								<p class="pull-right">
									${campaign_year}
								</p>
							</div>

							<#-- Ligne séparatrice colorée -->
							<div class="prop-card-line bgcolor-theme-${theme_class}"></div>

							<div class="buttons-wrapper">
							
								<a href="${url_proposal}" alt="${proposal.titre}" title="${proposal.titre}" class="btn btn-12rem btn-black-on-white">Consulter le projet</a>

								<#-- Ne plus s'associer à l'idée -->
								<div class="prop-card-unsubscribe">
									<a class="btn btn-12rem btn-black-on-white" href="jsp/site/plugins/extend/modules/follow/DoCancelFollow.jsp?idExtendableResource=${proposal.id}&extendableResourceType=PROPOSAL">
										<i class='fa fa-times-circle'></i> Ne plus m'associer
									</a>
								</div>

							</div>

						</div>
					</div>
				</#list>
			</#if>
		</div>
	</div>

	<#-- *********************************************************************************** -->
	<#-- * COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES * -->
	<#-- * COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES COMMENTES * -->
	<#-- *********************************************************************************** -->

	<div id="resultCommented">
	
		<h2> #i18n{participatoryideation.proposalsSubscriber.commented.projects}</h2>
		
		<div class="row row-eq-height">

			<#if !projectsCommented?has_content >
				<div class="col-xs-12">
					<p class="empty-list">Il semble que vous n'ayez pas commenté de projet.</p>
				</div>
			<#else>

				<#list projectsCommented as proposal>
					<#assign theme_class = "autre">
					<#if proposal.codeTheme??>					
						<#if     proposal.codeTheme = "CADRE">											<#assign theme_class = "cadre_vie">
						<#elseif proposal.codeTheme = "CULTURE">										<#assign theme_class = "culture">
						<#elseif proposal.codeTheme = "ECO">											<#assign theme_class = "economie_emploi">
						<#elseif proposal.codeTheme = "EDU">											<#assign theme_class = "education">
						<#elseif proposal.codeTheme = "ENVIRONNEMENT" || proposal.codeTheme = "NAT">		<#assign theme_class = "environnement">
						<#elseif proposal.codeTheme = "LOGEMENT">										<#assign theme_class = "logement">
						<#elseif proposal.codeTheme = "PARTICIPATION">									<#assign theme_class = "participation_citoyenne">
						<#elseif proposal.codeTheme = "PROPRETE">										<#assign theme_class = "proprete">
						<#elseif proposal.codeTheme = "SOLIDARITE" || proposal.codeTheme = "VIVREENSEMBLE">	<#assign theme_class = "solidarite">
						<#elseif proposal.codeTheme = "SPORT">											<#assign theme_class = "sport">
						<#elseif proposal.codeTheme = "TRANSPORT">										<#assign theme_class = "transport">
						<#elseif proposal.codeTheme = "NUMERIQUE">										<#assign theme_class = "ville_numerique">
						<#elseif proposal.codeTheme = "SANTE">											<#assign theme_class = "sante">
						<#elseif proposal.codeTheme = "SECURITE">										<#assign theme_class = "prevention_securite">
						
						</#if>
					</#if>
					
					<#if proposal.codeTheme??>
						<#assign campaign_year = "Budget Participatif " + (2014 + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"?index_of(proposal.codeCampaign))>
					</#if>
					
					<#if (proposal.codeTheme)??>
						<#assign theme_libelle = (global_static[proposal.codeCampaign].themes_map[proposal.codeTheme].title)!proposal.codeTheme>
					</#if>
					
					<#assign url_proposal = "jsp/site/Portal.jsp?page=proposal&campaign="+proposal.codeCampaign+"&proposal="+proposal.codeProposal>
					<#assign url_proposal = "jsp/site/Portal.jsp?page=proposal&campaign="+proposal.codeCampaign+"&proposal="+proposal.codeProposal>

					<div class="col-xs-12 col-sm-4 col-md-3 prop-panel">
						<div class="prop-card bordered-4px-theme-${theme_class}">
							
							<#-- Thématique -->
							<div class="prop-card-theme bgcolor-theme-${theme_class}">
								<img src="images/local/skin/i2_${theme_class}.png">
								<span>${theme_libelle}</span>
							</div>

							<#-- Location -->
							<div class="prop-card-loc">
								<img src="images/local/skin/marker_1.png" class="img-responsive pull-left" alt="" title="">
								<#if proposal.locationType == "paris"  >Projet « Tout Paris »<#elseif proposal.locationType == "ardt"  ><#if proposal.adress??>Projet d'arrondissement<#else>${(arrondissements_map[proposal.locationArdt])!proposal.locationArdt}</#if></#if>
							</div>
							
							<#-- Titre -->
							<div class="prop-card-titre">
								<h3>${proposal.titre!''}</h3>
							</div>

							<#-- Campaign -->
							<div class="prop-card-campaign">
								<p class="pull-right">
									${campaign_year}
								</p>
							</div>

							<#-- Ligne séparatrice colorée -->
							<div class="prop-card-line bgcolor-theme-${theme_class}"></div>

							<div class="buttons-wrapper">
							
								<a href="${url_proposal}" alt="${proposal.titre}" title="${proposal.titre}" class="btn btn-12rem btn-black-on-white">Consulter le projet</a>

							</div>

						</div>
					</div>
				</#list>
			</#if>
		</div>
	</div>
	
</div>