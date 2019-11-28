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
package fr.paris.lutece.plugins.participatoryideation.web;
 

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.avatar.service.AvatarService;
import fr.paris.lutece.plugins.participatorybudget.service.MyInfosService;
import fr.paris.lutece.plugins.participatorybudget.web.MyInfosXPage;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.service.IIdeeWSService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationErrorException;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeUsersService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeWSService;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
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
 * This class provides the user interface to view Idee xpages
 */
 
@Controller( xpageName = "idee" , pageTitleI18nKey = "ideation.xpage.idee.pageTitle" , pagePathI18nKey = "ideation.xpage.idee.pagePathLabel" )
public class IdeeXPage extends MVCApplication
{
    /**
     *
     */
    private static final long serialVersionUID = 2703580251118435168L;

    // Templates
    private static final String TEMPLATE_VIEW_IDEE="/skin/plugins/participatoryideation/view_idee.html";
    
    // Parameters
    private static final String PARAM_BP_EMAIL = "participatorybudget.email" ;
    //private static final String PARAM_BP_FIRST_NAME = "participatorybudget.firstname" ;
    //private static final String PARAM_BP_LASTE_NAME = "participatorybudget.lastname" ;
    private static final String PARAM_BP_NICKNAME = "portal.nickname" ;
    private static final String PARAMETER_CODE_IDEE="idee";
    private static final String PARAMETER_CODE_CAMPAGNE="campagne";
    private static final String PARAMETER_SHOW_CONTACT="showContact";
    
    private static final String PARAMETER_LAST_NAME_USAGER = "last_name_usager";
    private static final String PARAMETER_EMAIL_USAGER = "email_usager";
    private static final String PARAMETER_QUESTION_USAGER = "question_usager";
    
    // Markers
    private static final String MARK_IDEE = "idee";
    private static final String MARK_NICKNAME = "nickname";
    private static final String MARK_NICKNAME_USER = "nicknameUser";
    private static final String MARK_IS_OWN_IDEE = "is_own_idee";
    private static final String MARK_AVATAR_URL = "avatar_url";
    private static final String MARK_IS_EXTEND_INSTALLED = "isExtendInstalled";
    
    private static final String MARK_CODE_IDEE="idee";
    private static final String MARK_CODE_CAMPAGNE="campagne";
    
    private static final String MARK_LASTNAME_USER="lastNameUser";
    private static final String MARK_FIRSTNAME_USER="firstNameUser";
    private static final String MARK_EMAIL_USER="emailUser";
    
    private static final String MARK_NOM_DEP="nom_depositaire";
    private static final String MARK_NOM_CONTACT="nom_contacteur";
    private static final String MARK_MESSAGE="message";
    private static final String MARK_SHOW_CONTACT="show_contact";
    private static final String MARK_MESSAGE_NOT_ACCEPT="message_not_accept";
    // Properties
    private static final String PROPERTY_CONTACT_SUBJECT="ideation.site_property.view_idee.site_properties.contact_subject";
    private static final String PROPERTY_CONTACT_MESSAGE_CONTENT="ideation.site_property.view_idee.site_properties.contact_message_content.textblock";
    private static final String PROPERTY_CONTACT_MESSAGE_NOT_ACCEPT="ideation.site_property.view_idee.site_properties.contact_message_not_accept";
    
    private static final String INFO_MESSAGE_SEND="ideation.site_property.view_idee.site_properties.info_message_send";
    private static final String ERROR_MESSAGE_SEND="ideation.site_property.view_idee.site_properties.error_message_send";
    
    // Views
    private static final String VIEW_VIEW_IDEE = "viewIdee";
    
    // Actions
    private static final String ACTION_CONTACTER_DEPOSITAIRE = "contacterDepositaire";
    
    // CONSTANTS
    private static final String CONSTANT_CONTACT_EXTENDER_TYPE = "contact";
    
    
    
    /**
     * Returns the form to update info about a idee
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( value = VIEW_VIEW_IDEE, defaultView = true )
    public XPage getViewIdee( HttpServletRequest request )throws UserNotSignedException, SiteMessageException 
    {
        String strCodeCampagne = request.getParameter( PARAMETER_CODE_CAMPAGNE );
        Integer nCodeIdee;
        try {
            nCodeIdee = Integer.parseInt( request.getParameter( PARAMETER_CODE_IDEE ) );
        } catch (NumberFormatException nfe) {
            nCodeIdee = null;
        }
        
        Idee _idee;
        if (strCodeCampagne != null && nCodeIdee != null) {
            _idee = IdeeHome.findByCodes( strCodeCampagne, nCodeIdee );
        } else {
            _idee = null;
        }
        
        String strContactMessageNotAccept = DatastoreService.getDataValue(
    			PROPERTY_CONTACT_MESSAGE_NOT_ACCEPT, "");

        Map<String, Object> model = getModel(  );
        
        String strShowContact = request.getParameter( PARAMETER_SHOW_CONTACT );
        
        if(strShowContact != null && strShowContact.equals("true"))
        {
        	if(!checkUserAuthorized(request))
            {
            	return redirect(request, AppPathService.getProdUrl(request)+MyInfosXPage.getUrlMyInfos());
            }
        }
        
        model.put( MARK_SHOW_CONTACT, strShowContact );
        model.put( MARK_MESSAGE_NOT_ACCEPT, strContactMessageNotAccept );
        
        
        if (_idee == null || !IdeeService.getInstance().isPublished(_idee)) {
            return getXPage( TEMPLATE_VIEW_IDEE, request.getLocale(  ), model );
        }
        IdeeHome.loadMissingLinkedIdees(_idee);
        model.put( MARK_IDEE, _idee );
        if (_idee.getLuteceUserName() != null) {
            model.put(MARK_AVATAR_URL,AvatarService.getAvatarUrl(_idee.getLuteceUserName()));
            model.put(MARK_NICKNAME,UserPreferencesService.instance(  ).getNickname(_idee.getLuteceUserName()));
            if (SecurityService.isAuthenticationEnable()) {
                LuteceUser user = SecurityService.getInstance().getRegisteredUser(request);
                if (user != null && user.getName() != null) {
                	
                	model.put(MARK_IS_OWN_IDEE, _idee.getLuteceUserName().equals(user.getName()));
                	model.put( MARK_NICKNAME_USER, UserPreferencesService.instance(  ).getNickname( user.getName()));
                    model.put( MARK_LASTNAME_USER, user.getUserInfos().get("user.name.given"));
                    model.put( MARK_FIRSTNAME_USER, user.getUserInfos().get("user.name.family"));
                    model.put( MARK_EMAIL_USER, user.getUserInfos().get("user.business-info.online.email"));
                    
                    String strEmail = UserPreferencesService.instance( ).get( user.getName(), PARAM_BP_EMAIL, StringUtils.EMPTY);
                    if(strEmail != null && !strEmail.isEmpty())
                    {
                    	model.put( MARK_EMAIL_USER, strEmail);
                    }
                    
                }
                
            }
        }
        model.put( MARK_IS_EXTEND_INSTALLED, PortalService.isExtendActivated(  ) );
        IdeationStaticService.getInstance(  ).fillCampagneStaticContent( model, _idee.getCodeCampagne(  ) );
        
        
        XPage xpage = getXPage( TEMPLATE_VIEW_IDEE, request.getLocale(  ), model );
        xpage.setTitle( _idee.getTitre( ) );
        
        return xpage;
    }
    
    @Action(value = ACTION_CONTACTER_DEPOSITAIRE )
    public XPage doContacterDepositaire(HttpServletRequest request) 
    {	
    	String strCodeCampagne = request.getParameter( PARAMETER_CODE_CAMPAGNE );
    	
    	Integer nCodeIdee;
        try {
            nCodeIdee = Integer.parseInt( request.getParameter( PARAMETER_CODE_IDEE ) );
        } catch (NumberFormatException nfe) {
            nCodeIdee = null;
        }
        
        Idee _idee;
        if (strCodeCampagne != null && nCodeIdee != null) {
            _idee = IdeeHome.findByCodes( strCodeCampagne, nCodeIdee );
        } else {
            _idee = null;
        }
        
        Map<String, String> viewModel = new HashMap<String, String> ();
    	viewModel.put( MARK_CODE_CAMPAGNE, strCodeCampagne);
    	viewModel.put( MARK_CODE_IDEE, "" + nCodeIdee);
        
        String strEmailDepositaire = "";
        String strNomDepositaire = "";
        if(_idee!=null)
        {
        	String strEmail = UserPreferencesService.instance( ).get( _idee.getLuteceUserName(), PARAM_BP_EMAIL, StringUtils.EMPTY);
            //String strFirstName = UserPreferencesService.instance( ).get( _idee.getLuteceUserName() ,PARAM_BP_FIRST_NAME, StringUtils.EMPTY);
            //String strLastName = UserPreferencesService.instance( ).get( _idee.getLuteceUserName() ,PARAM_BP_LASTE_NAME, StringUtils.EMPTY);
            String strNickname = UserPreferencesService.instance( ).get( _idee.getLuteceUserName() ,PARAM_BP_NICKNAME, StringUtils.EMPTY);
            
        	strEmailDepositaire = strEmail;
        	//strNomDepositaire = strFirstName + " " + strLastName;
        	strNomDepositaire = strNickname;
        }
    	
    	String strLastNameUsager = request.getParameter( PARAMETER_LAST_NAME_USAGER );
    	LuteceUser user=null;
    	if (SecurityService.isAuthenticationEnable()) {
            user = SecurityService.getInstance().getRegisteredUser(request);
    	}
        
            
    	String strEmailUsager = user!=null ?UserPreferencesService.instance( ).get( user.getName( ), PARAM_BP_EMAIL, StringUtils.EMPTY):"";
    	String strQuestionUsager = request.getParameter( PARAMETER_QUESTION_USAGER );
    	
    	String strEmailContent = StringUtils.EMPTY;
   
    	
        
        String strSubject = DatastoreService.getDataValue(
        		PROPERTY_CONTACT_SUBJECT, "");
        
    	String strContactMessage = DatastoreService.getDataValue(
    			PROPERTY_CONTACT_MESSAGE_CONTENT, "");
    	
    	
    	
    	Map<String, String> model = new HashMap<String, String> ();
    	String strLuteceUserName = "";
    	if (SecurityService.isAuthenticationEnable()) {
    	    user = SecurityService.getInstance().getRegisteredUser(request);
            if (user != null && user.getName() != null) {
            	strLuteceUserName = user.getName();
                model.put( MARK_LASTNAME_USER, user.getUserInfos().get("user.name.given"));
                model.put( MARK_FIRSTNAME_USER, user.getUserInfos().get("user.name.family"));
                model.put( MARK_EMAIL_USER, user.getUserInfos().get("user.business-info.online.email"));
            }
        }
    	
    	
    	
    	model.put( MARK_NOM_DEP, strNomDepositaire );
    	model.put( MARK_NOM_CONTACT, strLastNameUsager);
    	model.put( MARK_MESSAGE, strQuestionUsager);
    	
    	if(!strQuestionUsager.isEmpty())
    	{
    		try
        	{
        		HtmlTemplate t = AppTemplateService.getTemplateFromStringFtl( strContactMessage, request.getLocale(), model );
                strEmailContent = t.getHtml(  );
        	}       
            catch ( Exception e )
            {
                AppLogService.error( "Erreur template freemarker: " + e + ": " + e.getMessage(  ),e );
                String strErrorMessageSend = DatastoreService.getDataValue( ERROR_MESSAGE_SEND, "");
            	addError( strErrorMessageSend );
            	return redirect( request, VIEW_VIEW_IDEE, viewModel);
            }
    	}
        
        if(!strEmailDepositaire.isEmpty() && !strEmailUsager.isEmpty() && !strEmailContent.isEmpty())
        {
        	String strInfoMessageSend = DatastoreService.getDataValue(INFO_MESSAGE_SEND, "");
        	MailService.sendMailHtml( strEmailDepositaire, "", "", "", strEmailUsager, strSubject, strEmailContent );
        	contactDepHistory( _idee, strLuteceUserName );
        	addInfo( strInfoMessageSend );
        }
        else
        {
        	String strErrorMessageSend = DatastoreService.getDataValue(ERROR_MESSAGE_SEND, "");
        	addError( strErrorMessageSend );
        }
        
    	return redirect( request, VIEW_VIEW_IDEE, viewModel);
    }
    
    private boolean checkUserAuthorized(HttpServletRequest request)throws UserNotSignedException
    {
        LuteceUser user=null;
        if (SecurityService.isAuthenticationEnable()) {
            user=SecurityService.getInstance().getRemoteUser(request);
            if(user==null)
            {
                throw new UserNotSignedException();
            }
            return MyInfosService.loadUserInfos(user).getIsValid();
            	
        }
        return false;

    }
    
    private void contactDepHistory( Idee idee, String strLuteceUserName )
    {
    	IResourceExtenderHistoryService resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
    	ResourceExtenderHistory history = new ResourceExtenderHistory(  );
        history.setExtenderType( CONSTANT_CONTACT_EXTENDER_TYPE );
        //history.setIdExtendableResource( idee.getCodeCampagne() + "-" + String.format("%06d", idee.getCodeIdee()));
        history.setIdExtendableResource( "" + idee.getId( ) );
        history.setExtendableResourceType( Idee.PROPERTY_RESOURCE_TYPE );
        history.setIpAddress( StringUtils.EMPTY );
        history.setUserGuid( strLuteceUserName );
        resourceHistoryService.create( history );
    }
    
 
}
