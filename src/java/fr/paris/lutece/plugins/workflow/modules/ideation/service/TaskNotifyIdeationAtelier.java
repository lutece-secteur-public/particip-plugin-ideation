/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.workflow.modules.ideation.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.service.AtelierService;
import fr.paris.lutece.plugins.ideation.service.IdeeService;
import fr.paris.lutece.plugins.ideation.service.subscription.IdeationSubscriptionProviderService;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.plugins.workflow.modules.ideation.business.TaskNotifyIdeationConfig;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 * 
 * TaskNotifyIdeation Task
 *
 */
public class TaskNotifyIdeationAtelier extends SimpleTask {
	// BEANS CONFIG
	public static final String CONFIG_SERVICE_BEAN_NAME = "workflow-ideation.taskNotifyIdeationConfigService";

	// PARAMETERS
	private static final String PARAM_BP_EMAIL = "budgetparticipatif.email";
	private static final String PARAM_COMMENT = "comment";

	// MARKS
	private static final String MARK_ATELIER = "atelier";
	private static final String MARK_PSEUDO_DESTINATAIRE = "pseudo_destinataire";
	private static final String MARK_COMMENT_CONTENT = "comment_content";

	private static final String MARK_GENERATED_IDEE_ID = "idee_id";
	private static final String MARK_GENERATED_IDEE_CAMPAIN = "idee_campain";
	private static final String MARK_GENERATED_IDEE_CODE = "idee_code";
	private static final String MARK_GENERATED_IDEE_TITLE = "idee_title";
	private static final String MARK_GENERATED_IDEE_URL = "idee_url";

	// CONSTANTS
	private static final String HTML_BR = "<br/>";
	private static final String HTML_R = "\r";
	private static final String ID_ALL = "*";
	private static final String ACTION_NAME_CREATE_COMMENT_ATELIER = AppPropertiesService
			.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT_ATELIER);

	// ERRORS
	private static final String ERROR_GET_EMAIL = "Ideation, failed to get email";

	@Inject
	private IResourceHistoryService _resourceHistoryService;
	@Inject
	@Named(CONFIG_SERVICE_BEAN_NAME)
	private ITaskConfigService _taskNotifyIdeationConfigService;
	@Inject
	private IResourceExtenderHistoryService _resourceExtenderHistoryService;

	@Override
	public String getTitle(Locale locale) {
		return "Ideation notify";
	}

	@Override
	public void processTask(int nIdResourceHistory, HttpServletRequest request, Locale locale) {

		ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey(nIdResourceHistory);
		String strComment = StringUtils.EMPTY;
		String actionName = resourceHistory.getAction().getName();

		if (request != null) {
			strComment = request.getParameter(PARAM_COMMENT) == null ? StringUtils.EMPTY : request.getParameter(PARAM_COMMENT);
		}

		if ( (resourceHistory != null) && Atelier.WORKFLOW_RESOURCE_TYPE.equals(resourceHistory.getResourceType()) ) {

			Atelier atelier = AtelierHome.findByPrimaryKey(resourceHistory.getIdResource());

			if (atelier != null)

			{

				if (StringUtils.isNotBlank(strComment)) {
					strComment = strComment.replaceAll(HTML_R, HTML_BR);
				}

				Map<String, Object> model = new HashMap<String, Object>();
				model.put(MARK_ATELIER, atelier);
				model.put(MARK_COMMENT_CONTENT, strComment);

				// Adding markers about generated idee
				int nGeneratedIdee = AtelierService.getInstance().ideeAlreadyGenerated( atelier.getId( ) );
				if ( nGeneratedIdee != 0) 
				{
					Idee idee = IdeeHome.findByPrimaryKey( nGeneratedIdee );
					
					String ideeUrl = AppPathService.getProdUrl( request ) + "/jsp/site/Portal.jsp?page=idee&campagne=" + idee.getCodeCampagne( ) + "&idee=" + idee.getCodeIdee( );

					model.put( MARK_GENERATED_IDEE_ID     , idee.getId() );
					model.put( MARK_GENERATED_IDEE_CAMPAIN, idee.getCodeCampagne() );
					model.put( MARK_GENERATED_IDEE_CODE   , idee.getCodeIdee()     );
					model.put( MARK_GENERATED_IDEE_TITLE  , idee.getTitre()        );
					model.put( MARK_GENERATED_IDEE_URL    , ideeUrl                );
				}

				TaskNotifyIdeationConfig config = _taskNotifyIdeationConfigService.findByPrimaryKey(this.getId());
				String strSenderName = config.getSenderName();
				String strRecipientBcc = config.getRecipientsBcc();
				String strRecipientCc = config.getRecipientsCc();
				String strSubject = config.getSubject();

				String strMessage = StringUtils.EMPTY;

				String strEmail = StringUtils.EMPTY;
				String strSenderEmail = config.getSenderEmail();
				List<Idee> listIdees = AtelierHome.getIdeesByAtelier(atelier.getId());

				if (config.isFollowers()) {
					SubscriptionFilter filterSub = new SubscriptionFilter();
					filterSub.setIdSubscribedResource(ID_ALL);
					filterSub.setSubscriptionProvider(
							IdeationSubscriptionProviderService.getService().getProviderName());
					if (actionName.equals(ACTION_NAME_CREATE_COMMENT_ATELIER)) {
						filterSub.setSubscriptionKey(
								IdeationSubscriptionProviderService.SUBSCRIPTION_PARTICIPANTS_ACTIONS_ON_ATELIERS);

					} else {

						filterSub.setSubscriptionKey(
								IdeationSubscriptionProviderService.SUBSCRIPTION_ATELIER_ORGANISATION_FOLLOWED_PROJECTS);
					}

					List<ResourceExtenderHistory> listHistoriesAllIdees = new ArrayList<>();

					ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter();
					filter.setExtenderType(FollowResourceExtender.RESOURCE_EXTENDER);
					filter.setExtendableResourceType(Idee.PROPERTY_RESOURCE_TYPE);

					for (Idee idee : listIdees) {
						filter.setIdExtendableResource(String.valueOf(idee.getId()));
						listHistoriesAllIdees.addAll(_resourceExtenderHistoryService.findByFilter(filter));
					}

					for (ResourceExtenderHistory followerHistory : listHistoriesAllIdees) {

						filterSub.setIdSubscriber(followerHistory.getUserGuid());
						List<Subscription> listSubscription = SubscriptionService.getInstance().findByFilter(filterSub);

						if (!CollectionUtils.isEmpty(listSubscription)) {

							String strNickNameDestinataire = UserPreferencesService.instance()
									.getNickname(followerHistory.getUserGuid());
							model.put(MARK_PSEUDO_DESTINATAIRE, strNickNameDestinataire);

							strMessage = AppTemplateService.getTemplateFromStringFtl(
									"[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage(), locale,
									model).getHtml();
							try {
								strEmail = UserPreferencesService.instance().get(followerHistory.getUserGuid(),
										PARAM_BP_EMAIL, StringUtils.EMPTY);

							} catch (Exception e) {
								throw new RuntimeException(ERROR_GET_EMAIL, e);
							}

							MailService.sendMailHtml(strEmail, strRecipientCc, strRecipientBcc, strSenderName,
									strSenderEmail, strSubject, strMessage);
						}
					}
				}

				if (config.isDepositaire()) {
					SubscriptionFilter filterSub = new SubscriptionFilter();
					filterSub.setIdSubscribedResource(ID_ALL);
					filterSub.setSubscriptionProvider(
							IdeationSubscriptionProviderService.getService().getProviderName());
					if (actionName.equals(ACTION_NAME_CREATE_COMMENT_ATELIER)) {
						filterSub.setSubscriptionKey(
								IdeationSubscriptionProviderService.SUBSCRIPTION_PARTICIPANTS_ACTIONS_ON_ATELIERS);

					} else {

						filterSub.setSubscriptionKey(
								IdeationSubscriptionProviderService.SUBSCRIPTION_ATELIER_ORGANISATION_SUBMITTED_PROJECTS);
					}

					for (Idee idee : listIdees) {

						filterSub.setIdSubscriber(idee.getLuteceUserName());
						List<Subscription> listSubscription = SubscriptionService.getInstance().findByFilter(filterSub);

						if (!CollectionUtils.isEmpty(listSubscription)) {

							String strNickNameDestinataire = UserPreferencesService.instance()
									.getNickname(idee.getLuteceUserName());
							model.put(MARK_PSEUDO_DESTINATAIRE, strNickNameDestinataire);

							try {
								strMessage = AppTemplateService.getTemplateFromStringFtl(
										"[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage(), locale,
										model).getHtml();
								strEmail = UserPreferencesService.instance().get(idee.getLuteceUserName(),
										PARAM_BP_EMAIL, StringUtils.EMPTY);
							} catch (Exception e) {
								throw new RuntimeException(ERROR_GET_EMAIL, e);
							}

							MailService.sendMailHtml(strEmail, strRecipientCc, strRecipientBcc, strSenderName,
									strSenderEmail, strSubject, strMessage);

						}
					}
				}
			}
		}
	}
}
