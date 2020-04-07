/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.participatoryideation.service.notify;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.notify.TaskNotifyIdeationConfig;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.service.subscription.IdeationSubscriptionProviderService;
import fr.paris.lutece.plugins.participatoryideation.util.ParticipatoryIdeationConstants;
import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.LuteceUserService;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import nu.xom.jaxen.function.StringFunction;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * TaskNotifyIdeation Task
 *
 */
public class TaskNotifyIdeation extends SimpleTask
{
    // BEANS CONFIG
    public static final String CONFIG_SERVICE_BEAN_NAME = "participatoryideation.taskNotifyIdeationConfigService";

    // PARAMETERS
    private static final String PARAM_BP_EMAIL = "participatorybudget.email";
    private static final String PARAM_COMMENT = "comment";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_FROM_URL = "from_url";

    // MARKS
    private static final String MARK_PROPOSAL = "proposal";
    private static final String MARK_PSEUDO = "pseudo_declencheur";
    private static final String MARK_EMAIL = "email_declencheur";
    private static final String MARK_PSEUDO_DEPOSITARY = "pseudo_depositary";
    private static final String MARK_PSEUDO_DESTINATAIRE = "pseudo_destinataire";
    private static final String MARK_COMMENT_CONTENT = "comment_content";
    private static final String MARK_IS_BACK_USER = "comment_is_back_user";

    // CONSTANTS
    private static final String HTML_BR = "<br/>";
    private static final String HTML_R = "\r";
    private static final String ID_ALL = "*";
    private static final String ACTION_NAME_CREATE_PROPOSAL = AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_PROPOSAL );
    private static final String ACTION_NAME_CREATE_COMMENT = AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT );
    private static final String ACTION_NAME_FOLLOW = AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_FOLLOW );
    private static final String ACTION_NAME_CANCEL_FOLLOW = AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CANCEL_FOLLOW );

    // ERRORS
    private static final String ERROR_GET_EMAIL = "Ideation, failed to get email";

    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskNotifyIdeationConfigService;
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    @Override
    public String getTitle( Locale locale )
    {
        return "Ideation notify";
    }

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {

        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        LuteceUser user = null;
        String strComment = StringUtils.EMPTY;
        String strNickNameUser = StringUtils.EMPTY;
        String strEmailUser = StringUtils.EMPTY;
        boolean bBackUser = false;
        String actionName = resourceHistory.getAction( ).getName( );

        if ( request != null )
        {
            user = SecurityService.getInstance( ).getRegisteredUser( request );
            strComment = request.getParameter( PARAM_COMMENT ) == null ? StringUtils.EMPTY : request.getParameter( PARAM_COMMENT );
            if ( user != null && ( request.getParameter( PARAM_FROM_URL ) == null || !request.getParameter( PARAM_FROM_URL ).contains( "jsp/admin" ) ) )
            {
                strNickNameUser = UserPreferencesService.instance( ).getNickname( user.getName( ) );
                strEmailUser = user.getEmail( );
            }
            else
            {
                bBackUser = true;
                if ( !StringUtils.isEmpty( strComment ) && request.getParameter( PARAM_NAME ) != null )
                {
                    strNickNameUser = request.getParameter( PARAM_NAME );
                }

            }

        }
        if ( ( resourceHistory != null ) && Proposal.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
        {
            Proposal proposal = ProposalHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

            String strProposalLuteceUsername = proposal.getLuteceUserName( );
            String strNickNameDepositary = UserPreferencesService.instance( ).getNickname( strProposalLuteceUsername );
            String strNickNameDestinataire = StringUtils.EMPTY;

            if ( StringUtils.isNotBlank( strComment ) )
            {
                strComment = strComment.replaceAll( HTML_R, HTML_BR );
            }

            Map<String, Object> model = new HashMap<String, Object>( );
            model.put( MARK_PROPOSAL, proposal );
            model.put( MARK_PSEUDO, strNickNameUser );
            model.put( MARK_EMAIL, strEmailUser );
            model.put( MARK_PSEUDO_DEPOSITARY, strNickNameDepositary );
            model.put( MARK_COMMENT_CONTENT, strComment );
            model.put( MARK_IS_BACK_USER, bBackUser );

            TaskNotifyIdeationConfig config = _taskNotifyIdeationConfigService.findByPrimaryKey( this.getId( ) );
            String strSenderName = config.getSenderName( );
            String strRecipientBcc = config.getRecipientsBcc( );
            String strRecipientCc = config.getRecipientsCc( );
            String strSubject = config.getSubject( );

            String strMessage = StringUtils.EMPTY;

            String strEmail = StringUtils.EMPTY;
            String strSenderEmail = config.getSenderEmail( );

            if ( config.isFollowers( ) )
            {
                List<String> subscriber = new ArrayList<String>( );
                List<String> subscriberComment = new ArrayList<String>( );
                SubscriptionFilter filterSub = new SubscriptionFilter( );
                filterSub.setIdSubscribedResource( ID_ALL );

                filterSub.setSubscriptionProvider( IdeationSubscriptionProviderService.getService( ).getProviderName( ) );

                if ( actionName.equals( ACTION_NAME_CREATE_COMMENT ) )
                {

                    filterSub.setSubscriptionKey( IdeationSubscriptionProviderService.SUBSCRIPTION_NEW_COMMENT_ON_PARTICIPATE_PROPOSAL );
                    List<Subscription> listSubComment = SubscriptionService.getInstance( ).findByFilter( filterSub );
                    for ( Subscription subComment : listSubComment )
                    {
                        subscriberComment.add( subComment.getUserId( ) );

                    }

                }
                else
                    if ( !( actionName.equals( ACTION_NAME_FOLLOW ) || actionName.equals( ACTION_NAME_CANCEL_FOLLOW ) ) )
                    {
                        filterSub.setSubscriptionKey( IdeationSubscriptionProviderService.SUBSCRIPTION_NEW_STATE_ON_PARTICIPATE_PROPOSAL );
                        List<Subscription> listSubscription = SubscriptionService.getInstance( ).findByFilter( filterSub );

                        for ( Subscription subscription : listSubscription )
                        {
                            subscriber.add( subscription.getUserId( ) );

                        }
                    }

                ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
                filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
                filter.setExtendableResourceType( Proposal.PROPERTY_RESOURCE_TYPE );
                filter.setIdExtendableResource( String.valueOf( proposal.getId( ) ) );

                List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

                for ( ResourceExtenderHistory followerHistory : listHistories )
                {
                    if ( !subscriberComment.isEmpty( ) && actionName.equals( ACTION_NAME_CREATE_COMMENT )
                            && subscriberComment.contains( followerHistory.getUserGuid( ) ) )
                    {

                        strNickNameDestinataire = UserPreferencesService.instance( ).getNickname( followerHistory.getUserGuid( ) );
                        model.put( MARK_PSEUDO_DESTINATAIRE, strNickNameDestinataire );

                        strMessage = AppTemplateService
                                .getTemplateFromStringFtl( "[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage( ), locale, model ).getHtml( );
                        try
                        {
                            strEmail = UserPreferencesService.instance( ).get( followerHistory.getUserGuid( ), PARAM_BP_EMAIL, StringUtils.EMPTY );

                        }
                        catch( Exception e )
                        {
                            throw new RuntimeException( ERROR_GET_EMAIL, e );
                        }

                        MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );

                    }
                    else
                        if ( !subscriber.isEmpty( ) && subscriber.contains( followerHistory.getUserGuid( ) ) )
                        {

                            strNickNameDestinataire = UserPreferencesService.instance( ).getNickname( followerHistory.getUserGuid( ) );
                            model.put( MARK_PSEUDO_DESTINATAIRE, strNickNameDestinataire );

                            strMessage = AppTemplateService
                                    .getTemplateFromStringFtl( "[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage( ), locale, model )
                                    .getHtml( );
                            try
                            {
                                strEmail = UserPreferencesService.instance( ).get( followerHistory.getUserGuid( ), PARAM_BP_EMAIL, StringUtils.EMPTY );

                            }
                            catch( Exception e )
                            {
                                throw new RuntimeException( ERROR_GET_EMAIL, e );
                            }

                            MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );
                        }

                }
            }

            if ( config.isDepositary( ) )
            {

                List<Subscription> subDepoPart = null;
                List<Subscription> subDepoFollow = null;
                List<Subscription> listSubDepoComment = null;
                SubscriptionFilter filterSubDepo = new SubscriptionFilter( );
                filterSubDepo.setIdSubscriber( strProposalLuteceUsername );

                if ( actionName.equals( ACTION_NAME_CREATE_COMMENT ) )
                {

                    filterSubDepo.setSubscriptionKey( IdeationSubscriptionProviderService.SUBSCRIPTION_NEW_COMMENT_ON_MY_PROPOSAL );
                    listSubDepoComment = SubscriptionService.getInstance( ).findByFilter( filterSubDepo );

                }
                else
                    if ( ( actionName.equals( ACTION_NAME_FOLLOW ) || actionName.equals( ACTION_NAME_CANCEL_FOLLOW ) ) )
                    {

                        filterSubDepo.setSubscriptionKey( IdeationSubscriptionProviderService.SUBSCRIPTION_NEW_PARTICIPATION_ON_MY_PROPOSAL );
                        subDepoFollow = SubscriptionService.getInstance( ).findByFilter( filterSubDepo );

                    }
                    else
                    {
                        filterSubDepo.setSubscriptionKey( IdeationSubscriptionProviderService.SUBSCRIPTION_NEW_PARTICIPATION_ON_MY_PROPOSAL );
                        subDepoPart = SubscriptionService.getInstance( ).findByFilter( filterSubDepo );

                    }
                model.put( MARK_PSEUDO_DESTINATAIRE, strNickNameDepositary );
                if ( actionName.equals( ACTION_NAME_CREATE_PROPOSAL ) )
                {

                    try
                    {
                        strMessage = AppTemplateService
                                .getTemplateFromStringFtl( "[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage( ), locale, model ).getHtml( );
                        strEmail = UserPreferencesService.instance( ).get( strProposalLuteceUsername, PARAM_BP_EMAIL, StringUtils.EMPTY );
                    }
                    catch( Exception e )
                    {
                        throw new RuntimeException( ERROR_GET_EMAIL, e );
                    }

                    MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );
                }
                else
                    if ( listSubDepoComment != null && !listSubDepoComment.isEmpty( ) && actionName.equals( ACTION_NAME_CREATE_COMMENT ) )
                    {

                        try
                        {
                            strMessage = AppTemplateService
                                    .getTemplateFromStringFtl( "[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage( ), locale, model )
                                    .getHtml( );
                            strEmail = UserPreferencesService.instance( ).get( strProposalLuteceUsername, PARAM_BP_EMAIL, StringUtils.EMPTY );
                        }
                        catch( Exception e )
                        {
                            throw new RuntimeException( ERROR_GET_EMAIL, e );
                        }

                        MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );

                    }
                    else
                        if ( subDepoFollow != null && !subDepoFollow.isEmpty( )
                                && ( actionName.equals( ACTION_NAME_FOLLOW ) || actionName.equals( ACTION_NAME_CANCEL_FOLLOW ) ) )
                        {

                            try
                            {
                                strMessage = AppTemplateService
                                        .getTemplateFromStringFtl( "[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage( ), locale, model )
                                        .getHtml( );
                                strEmail = UserPreferencesService.instance( ).get( strProposalLuteceUsername, PARAM_BP_EMAIL, StringUtils.EMPTY );
                            }
                            catch( Exception e )
                            {
                                throw new RuntimeException( ERROR_GET_EMAIL, e );
                            }

                            MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );

                        }
                        else
                            if ( subDepoPart != null && !subDepoPart.isEmpty( ) )
                            {

                                try
                                {
                                    strMessage = AppTemplateService
                                            .getTemplateFromStringFtl( "[#ftl]\n[#setting date_format=\"dd/MM/yyyy\"]\n" + config.getMessage( ), locale, model )
                                            .getHtml( );
                                    strEmail = UserPreferencesService.instance( ).get( strProposalLuteceUsername, PARAM_BP_EMAIL, StringUtils.EMPTY );
                                }
                                catch( Exception e )
                                {
                                    throw new RuntimeException( ERROR_GET_EMAIL, e );
                                }

                                MailService.sendMailHtml( strEmail, strRecipientCc, strRecipientBcc, strSenderName, strSenderEmail, strSubject, strMessage );

                            }
            }
        }
    }
}
