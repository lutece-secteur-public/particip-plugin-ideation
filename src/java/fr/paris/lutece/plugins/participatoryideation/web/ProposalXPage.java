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
package fr.paris.lutece.plugins.participatoryideation.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.avatar.service.AvatarService;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.participatoryideation.service.ProposalService;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignService;
import fr.paris.lutece.plugins.participatoryideation.service.myinfos.MyInfosService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.mail.MailService;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.html.HtmlTemplate;

/**
 * This class provides the user interface to view Proposal xpages
 */

@Controller( xpageName = "proposal", pageTitleI18nKey = "participatoryideation.xpage.proposal.pageTitle", pagePathI18nKey = "participatoryideation.xpage.proposal.pagePathLabel" )
public class ProposalXPage extends MVCApplication
{
    /**
     *
     */
    private static final long serialVersionUID = 2703580251118435168L;

    // Templates
    private static final String TEMPLATE_VIEW_PROPOSAL = "/skin/plugins/participatoryideation/view_proposal.html";

    // Parameters
    private static final String PARAM_BP_EMAIL = "participatorybudget.email";
    private static final String PARAM_BP_NICKNAME = "portal.nickname";
    private static final String PARAMETER_CODE_PROPOSAL = "proposal";
    private static final String PARAMETER_CODE_CAMPAIGN = "campaign";
    private static final String PARAMETER_SHOW_CONTACT = "showContact";

    private static final String PARAMETER_LAST_NAME_USAGER = "last_name_usager";
    private static final String PARAMETER_QUESTION_USAGER = "question_usager";

    // Markers
    private static final String MARK_PROPOSAL = "proposal";
    private static final String MARK_NICKNAME = "nickname";
    private static final String MARK_NICKNAME_USER = "nicknameUser";
    private static final String MARK_IS_OWN_PROPOSAL = "is_own_proposal";
    private static final String MARK_AVATAR_URL = "avatar_url";
    private static final String MARK_IS_EXTEND_INSTALLED = "isExtendInstalled";

    private static final String MARK_CODE_PROPOSAL = "proposal";
    private static final String MARK_CODE_CAMPAIGN = "campaign";
    private static final String MARK_WHOLE_AREA = "whole_area";

    private static final String MARK_LASTNAME_USER = "lastNameUser";
    private static final String MARK_FIRSTNAME_USER = "firstNameUser";
    private static final String MARK_EMAIL_USER = "emailUser";

    private static final String MARK_NOM_DEP = "nom_submitter";
    private static final String MARK_NOM_CONTACT = "nom_contacteur";
    private static final String MARK_MESSAGE = "message";
    private static final String MARK_SHOW_CONTACT = "show_contact";
    private static final String MARK_MESSAGE_NOT_ACCEPT = "message_not_accept";
    // Properties
    private static final String PROPERTY_CONTACT_SUBJECT = "participatoryideation.site_property.view_proposal.site_properties.contact_subject";
    private static final String PROPERTY_CONTACT_MESSAGE_CONTENT = "participatoryideation.site_property.view_proposal.site_properties.contact_message_content.textblock";
    private static final String PROPERTY_CONTACT_MESSAGE_NOT_ACCEPT = "participatoryideation.site_property.view_proposal.site_properties.contact_message_not_accept";

    private static final String INFO_MESSAGE_SEND = "participatoryideation.site_property.view_proposal.site_properties.info_message_send";
    private static final String ERROR_MESSAGE_SEND = "participatoryideation.site_property.view_proposal.site_properties.error_message_send";

    // Views
    private static final String VIEW_VIEW_PROPOSAL = "viewProposal";

    // Actions
    private static final String ACTION_CONTACTER_SUBMITTER = "contacterSubmitter";

    // CONSTANTS
    private static final String CONSTANT_CONTACT_EXTENDER_TYPE = "contact";

    /**
     * Returns the form to update info about a proposal
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( value = VIEW_VIEW_PROPOSAL, defaultView = true )
    public XPage getViewProposal( HttpServletRequest request ) throws UserNotSignedException, SiteMessageException
    {
        String strCodeCampaign = request.getParameter( PARAMETER_CODE_CAMPAIGN );
        Integer nCodeProposal;
        try
        {
            nCodeProposal = Integer.parseInt( request.getParameter( PARAMETER_CODE_PROPOSAL ) );
        }
        catch( NumberFormatException nfe )
        {
            nCodeProposal = null;
        }

        Proposal _proposal;
        if ( strCodeCampaign != null && nCodeProposal != null )
        {
            _proposal = ProposalHome.findByCodes( strCodeCampaign, nCodeProposal );
        }
        else
        {
            _proposal = null;
        }

        String strContactMessageNotAccept = DatastoreService.getDataValue( PROPERTY_CONTACT_MESSAGE_NOT_ACCEPT, "" );

        Map<String, Object> model = getModel( );

        String strShowContact = request.getParameter( PARAMETER_SHOW_CONTACT );

        if ( strShowContact != null && strShowContact.equals( "true" ) )
        {
            if ( !checkUserAuthorized( request ) )
            {
                return redirect( request, AppPathService.getProdUrl( request ) + MyInfosService.getInstance( ).getUrlMyInfosFillAction( ) );
            }
        }

        model.put( MARK_SHOW_CONTACT, strShowContact );
        model.put( MARK_MESSAGE_NOT_ACCEPT, strContactMessageNotAccept );
        model.put( MARK_WHOLE_AREA, IdeationCampaignService.getInstance( ).getCampaignWholeArea( ) );

        if ( _proposal == null || !ProposalService.getInstance( ).isPublished( _proposal ) )
        {
            return getXPage( TEMPLATE_VIEW_PROPOSAL, request.getLocale( ), model );
        }

        ProposalHome.loadMissingLinkedProposals( _proposal );
        model.put( MARK_PROPOSAL, _proposal );

        if ( _proposal.getLuteceUserName( ) != null )
        {
            model.put( MARK_AVATAR_URL, AvatarService.getAvatarUrl( _proposal.getLuteceUserName( ) ) );
            model.put( MARK_NICKNAME, UserPreferencesService.instance( ).getNickname( _proposal.getLuteceUserName( ) ) );

            if ( SecurityService.isAuthenticationEnable( ) )
            {
                LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );

                if ( user != null && user.getName( ) != null )
                {
                    model.put( MARK_IS_OWN_PROPOSAL, _proposal.getLuteceUserName( ).equals( user.getName( ) ) );
                    model.put( MARK_NICKNAME_USER, UserPreferencesService.instance( ).getNickname( user.getName( ) ) );
                    model.put( MARK_LASTNAME_USER, user.getUserInfos( ).get( "user.name.given" ) );
                    model.put( MARK_FIRSTNAME_USER, user.getUserInfos( ).get( "user.name.family" ) );
                    model.put( MARK_EMAIL_USER, user.getUserInfos( ).get( "user.business-info.online.email" ) );

                    String strEmail = UserPreferencesService.instance( ).get( user.getName( ), PARAM_BP_EMAIL, StringUtils.EMPTY );
                    if ( strEmail != null && !strEmail.isEmpty( ) )
                    {
                        model.put( MARK_EMAIL_USER, strEmail );
                    }

                }

            }
        }
        model.put( MARK_IS_EXTEND_INSTALLED, PortalService.isExtendActivated( ) );
        IdeationStaticService.getInstance( ).fillCampaignStaticContent( model, _proposal.getCodeCampaign( ) );

        XPage xpage = getXPage( TEMPLATE_VIEW_PROPOSAL, request.getLocale( ), model );
        xpage.setTitle( _proposal.getTitre( ) );

        return xpage;
    }

    @Action( value = ACTION_CONTACTER_SUBMITTER )
    public XPage doContacterSubmitter( HttpServletRequest request )
    {
        String strCodeCampaign = request.getParameter( PARAMETER_CODE_CAMPAIGN );

        Integer nCodeProposal;
        try
        {
            nCodeProposal = Integer.parseInt( request.getParameter( PARAMETER_CODE_PROPOSAL ) );
        }
        catch( NumberFormatException nfe )
        {
            nCodeProposal = null;
        }

        Proposal _proposal;
        if ( strCodeCampaign != null && nCodeProposal != null )
        {
            _proposal = ProposalHome.findByCodes( strCodeCampaign, nCodeProposal );
        }
        else
        {
            _proposal = null;
        }

        Map<String, String> viewModel = new HashMap<String, String>( );
        viewModel.put( MARK_CODE_CAMPAIGN, strCodeCampaign );
        viewModel.put( MARK_CODE_PROPOSAL, "" + nCodeProposal );

        String strEmailSubmitter = "";
        String strNomSubmitter = "";
        if ( _proposal != null )
        {
            String strEmail = UserPreferencesService.instance( ).get( _proposal.getLuteceUserName( ), PARAM_BP_EMAIL, StringUtils.EMPTY );
            String strNickname = UserPreferencesService.instance( ).get( _proposal.getLuteceUserName( ), PARAM_BP_NICKNAME, StringUtils.EMPTY );

            strEmailSubmitter = strEmail;
            strNomSubmitter = strNickname;
        }

        String strLastNameUsager = request.getParameter( PARAMETER_LAST_NAME_USAGER );
        LuteceUser user = null;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRegisteredUser( request );
        }

        String strEmailUsager = ( user != null ) ? UserPreferencesService.instance( ).get( user.getName( ), PARAM_BP_EMAIL, StringUtils.EMPTY ) : "";
        String strQuestionUsager = request.getParameter( PARAMETER_QUESTION_USAGER );
        String strEmailContent = StringUtils.EMPTY;
        String strSubject = DatastoreService.getDataValue( PROPERTY_CONTACT_SUBJECT, "" );
        String strContactMessage = DatastoreService.getDataValue( PROPERTY_CONTACT_MESSAGE_CONTENT, "" );

        Map<String, String> model = new HashMap<String, String>( );
        String strLuteceUserName = "";
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user != null && user.getName( ) != null )
            {
                strLuteceUserName = user.getName( );
                model.put( MARK_LASTNAME_USER, user.getUserInfos( ).get( "user.name.given" ) );
                model.put( MARK_FIRSTNAME_USER, user.getUserInfos( ).get( "user.name.family" ) );
                model.put( MARK_EMAIL_USER, user.getUserInfos( ).get( "user.business-info.online.email" ) );
            }
        }

        model.put( MARK_NOM_DEP, strNomSubmitter );
        model.put( MARK_NOM_CONTACT, strLastNameUsager );
        model.put( MARK_MESSAGE, strQuestionUsager );

        if ( !strQuestionUsager.isEmpty( ) )
        {
            try
            {
                HtmlTemplate t = AppTemplateService.getTemplateFromStringFtl( strContactMessage, request.getLocale( ), model );
                strEmailContent = t.getHtml( );
            }
            catch( Exception e )
            {
                AppLogService.error( "Erreur template freemarker: " + e + ": " + e.getMessage( ), e );
                String strErrorMessageSend = DatastoreService.getDataValue( ERROR_MESSAGE_SEND, "" );
                addError( strErrorMessageSend );
                return redirect( request, VIEW_VIEW_PROPOSAL, viewModel );
            }
        }

        if ( !strEmailSubmitter.isEmpty( ) && !strEmailUsager.isEmpty( ) && !strEmailContent.isEmpty( ) )
        {
            String strInfoMessageSend = DatastoreService.getDataValue( INFO_MESSAGE_SEND, "" );
            MailService.sendMailHtml( strEmailSubmitter, "", "", "", strEmailUsager, strSubject, strEmailContent );
            contactDepHistory( _proposal, strLuteceUserName );
            addInfo( strInfoMessageSend );
        }
        else
        {
            String strErrorMessageSend = DatastoreService.getDataValue( ERROR_MESSAGE_SEND, "" );
            addError( strErrorMessageSend );
        }

        return redirect( request, VIEW_VIEW_PROPOSAL, viewModel );
    }

    private boolean checkUserAuthorized( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = null;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRemoteUser( request );
            if ( user == null )
            {
                throw new UserNotSignedException( );
            }
            return MyInfosService.getInstance( ).isUserValid( user.getName( ) );

        }
        return false;

    }

    private void contactDepHistory( Proposal proposal, String strLuteceUserName )
    {
        IResourceExtenderHistoryService resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
        ResourceExtenderHistory history = new ResourceExtenderHistory( );

        history.setExtenderType( CONSTANT_CONTACT_EXTENDER_TYPE );
        // history.setIdExtendableResource( proposal.getCodeCampaign() + "-" + String.format("%06d", proposal.getCodeProposal()));
        history.setIdExtendableResource( ( proposal != null ) ? ( "" + proposal.getId( ) ) : "proposal is null" );
        history.setExtendableResourceType( Proposal.PROPERTY_RESOURCE_TYPE );
        history.setIpAddress( StringUtils.EMPTY );
        history.setUserGuid( strLuteceUserName );

        resourceHistoryService.create( history );
    }

}
