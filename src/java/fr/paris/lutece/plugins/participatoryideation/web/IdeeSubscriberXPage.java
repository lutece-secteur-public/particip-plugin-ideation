/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.CommentFilter;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeSearcher;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeService;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignService;
import fr.paris.lutece.plugins.participatoryideation.util.Constants;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import freemarker.ext.beans.BeansWrapper;

/**
 * This class provides the user interface to view Idee xpages
 */
 
@Controller( xpageName = "myProjects" , pageTitleI18nKey = "participatoryideation.xpage.ideeSubscriber.pageTitle" , pagePathI18nKey = "participatoryideation.xpage.ideeSubscriber.pagePathLabel" )
public class IdeeSubscriberXPage extends MVCApplication
{
    /**
     *
     */
    private static final long serialVersionUID = 2703580251118435168L;


    private static final String TEMPLATE_VIEW_SUBSCRIBER_IDEE="/skin/plugins/participatoryideation/view_subscriber_idees.html";
    
    //Jsp redirections
    private static final String JSP_PORTAL = "jsp/site/Portal.jsp";
    // Parameters
    private static final String PARAMETER_PAGE = "page";
    
    private static final String PARAMETER_IDEE_ID ="idee_id";
    private static final String PARAMETER_CONFIRM_REMOVE_IDEE = "confirm_remove_idee";
    //private static final String PARAMETER_CONF = "conf";

    // Markers
    private static final String MARK_PROJECTS_SUBMITTED = "projectsSubmitted";
    private static final String MARK_PROJECTS_PARTICIPATE = "projectsParticipate";
    private static final String MARK_PROJECTS_COMMENTED = "projectsCommented";
  
    // Properties
    
    private static final String PROPERTY_CONFIRM_REMOVE_IDEE = "participatoryideation.messages.confirmRemoveIdee";
    private static final String MESSAGE_INFO_IDEE_REMOVED = "participatoryideation.message.removed.succes";
    private static final String MESSAGE_NOT_AUTHORIZED = "participatoryideation.messages.not.authorized";
    private static final String MESSAGE_CAMPAGNE_IDEATION_CLOSED_DELETE = "participatoryideation.messages.campagne.ideation.closed.delete";

    // Views
  
    private static final String VIEW_SUBSCRIBER_IDEE = "viewSubscriberIdees";
    
    // Actions
    private static final String ACTION_DELETE_IDEE = "actionDeleteIdee";
    //session variable
    private static ICommentService _commentService;
    private IResourceExtenderHistoryService _resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

    
    /**
     * Returns the idees for user
     *
     * @param request The Http request
     * @return The HT
     */
    
    
    @View( value = VIEW_SUBSCRIBER_IDEE, defaultView = true)
    public XPage getSubscriberIdees( HttpServletRequest request ) throws UserNotSignedException
    {
    	String strLuteceUserName="guid";
    	
    	LuteceUser user= checkUserAuthorized(request); 
    	if(user != null)
    		strLuteceUserName= user.getName( );
    	    	
    	IdeeSearcher _ideeSearcher= new IdeeSearcher();
    	_ideeSearcher.setLuteceUserName(strLuteceUserName);
    	_ideeSearcher.setIsPublished(true);
    	
    	Collection<Idee> ideesSubmitted=IdeeHome.getIdeesListSearch(_ideeSearcher);
    	
    	CommentFilter _commentFilter= new CommentFilter();
    	_commentFilter.setLuteceUserName(strLuteceUserName);

    	Collection<Idee> ideesCommented=getIdeesCommentedByUser(getCommentService().findByResource("*",Idee.PROPERTY_RESOURCE_TYPE, _commentFilter, 0, 10000, false));
    		
    	 ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );

         filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
         filter.setUserGuid( strLuteceUserName );
         filter.setExtendableResourceType(Idee.PROPERTY_RESOURCE_TYPE);
         //filter.setIdExtendableResource("*");
        
         List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

         Collection<Idee> ideesParticipate= getIdeesParticipatedByUser(listHistories);
         
    	Map<String, Object> model = getModel(  );
    	
    	model.put( MARK_PROJECTS_SUBMITTED, ideesSubmitted );
    	model.put( MARK_PROJECTS_PARTICIPATE, ideesParticipate );
    	model.put( MARK_PROJECTS_COMMENTED, ideesCommented);
    	
    	BeansWrapper wrapper = BeansWrapper.getDefaultInstance();

    	IdeationStaticService.getInstance(  ).fillAllStaticContent( model );
    		
    	return getXPage( TEMPLATE_VIEW_SUBSCRIBER_IDEE, request.getLocale(  ), model );
    }
    
    
    /**
     * Get the confirmation page before removing an idee
     * @param request The request
     * @return The HTML content if the site message could not be displayed
     * @throws SiteMessageException If a site message needs to be displayed
     * @throws UserNotSignedException 
     */
    @Action( ACTION_DELETE_IDEE )
    public XPage getDeleteIdee( HttpServletRequest request )
        throws SiteMessageException, UserNotSignedException
    {
        String strConfirmRemoveIdee = request.getParameter( PARAMETER_CONFIRM_REMOVE_IDEE );
        int nIdIdee = Integer.parseInt( request.getParameter( PARAMETER_IDEE_ID ) );
        String strLuteceUserName="guid";
        LuteceUser user= checkUserAuthorized(request); 
        if(user != null)
        	strLuteceUserName= user.getName( );
    	
    	Idee idee = IdeeHome.findByPrimaryKey( nIdIdee );

        if ( !IdeationCampaignService.getInstance().isDuring( idee.getCodeCampagne(), Constants.IDEATION ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_CAMPAGNE_IDEATION_CLOSED_DELETE, SiteMessage.TYPE_ERROR );
        }

        if ( strConfirmRemoveIdee != null )
        {
           
            IdeeSearcher _ideeSearcher= new IdeeSearcher();
        	_ideeSearcher.setLuteceUserName(strLuteceUserName);
        	_ideeSearcher.setIsPublished(true);
        	Collection<Idee> ideesSubmitted=IdeeHome.getIdeesListSearch(_ideeSearcher);
        	Boolean delete= false;
        	
        	for(Idee id: ideesSubmitted){
        		if(id.getId( ) == idee.getId( )){
        			delete = true;
        			break;
        		}
        	}
        	if(delete){
        		idee.setStatusPublic(Idee.Status.STATUS_SUPPRIME_PAR_USAGER);
        		IdeeService.getInstance().removeIdee(idee);  
	            addInfo(MESSAGE_INFO_IDEE_REMOVED,request.getLocale( ));
	            
	            return redirect( request, AppPathService.getBaseUrl( request ) + getActionFullUrl( VIEW_SUBSCRIBER_IDEE ) );
        	}else{
        		  SiteMessageService.setMessage( request, MESSAGE_NOT_AUTHORIZED, SiteMessage.TYPE_STOP );
        	}
           
        }
        Map<String, Object> requestParameters = new HashMap<String, Object>(  );
        requestParameters.put( PARAMETER_PAGE, "myProjects" );
        requestParameters.put( MVCUtils.PARAMETER_ACTION, ACTION_DELETE_IDEE );
        requestParameters.put( PARAMETER_IDEE_ID, nIdIdee );
        requestParameters.put( PARAMETER_CONFIRM_REMOVE_IDEE, "1" );
        SiteMessageService.setMessage( request, PROPERTY_CONFIRM_REMOVE_IDEE, SiteMessage.TYPE_CONFIRMATION,
            JSP_PORTAL, requestParameters );

        // Never return null because the setMessage method throw an exception
        return null;
    }

    
    
    private LuteceUser checkUserAuthorized(HttpServletRequest request)throws UserNotSignedException
    {
        LuteceUser user=null;
        if (SecurityService.isAuthenticationEnable()) {
            user=SecurityService.getInstance().getRemoteUser(request);
            if(user==null)
            {
                throw new UserNotSignedException();
            }
        }
        return user;
    }
    
    /**
     * Get the comment service
     * @return the comment service
     */
    private static ICommentService getCommentService( )
    {
        if ( _commentService == null )
        {
            _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
        }
        return _commentService;
    }
    /**
     * get the idees List
     * @param listComments
     * @return
     */
    private Collection<Idee> getIdeesCommentedByUser(Collection<Comment> listComments){
    	
    	Collection<Idee> ideesList= new ArrayList<Idee>();
    	List<Integer> IdExtendableResourceList= new ArrayList<Integer>();
    	for(Comment comment: listComments){
    		
    		String idRessource= comment.getIdExtendableResource( );
    		
    		if(idRessource != null && StringUtils.isNotEmpty(idRessource) && StringUtils.isNumeric(idRessource) ){
    			int nIdIdee= Integer.parseInt(idRessource);
    			Idee idee= IdeeHome.findByPrimaryKey(nIdIdee);
    			if (idee != null && IdeeService.getInstance().isPublished(idee) && !IdExtendableResourceList.contains(nIdIdee)){
    				ideesList.add(idee);
    				IdExtendableResourceList.add(nIdIdee);
    			}
    		}
    	}
    	
    	return ideesList;
    } 
    
    /**
     * get the idees List
     * @param listFollow
     * @return
     */
    private Collection<Idee> getIdeesParticipatedByUser(Collection<ResourceExtenderHistory> listFollow){
    	
    	Collection<Idee> ideesList= new ArrayList<Idee>();
    	for(ResourceExtenderHistory follow: listFollow){
    		
    		String idRessource= follow.getIdExtendableResource( );
    		
    		if(idRessource != null && StringUtils.isNotEmpty(idRessource) && StringUtils.isNumeric(idRessource)){
    			int nIdIdee= Integer.parseInt(idRessource);
    			Idee idee= IdeeHome.findByPrimaryKey(nIdIdee);
    			if (idee != null && IdeeService.getInstance().isPublished(idee)){
    				ideesList.add(idee);
    			}
    		}
    	}
    	
    	return ideesList;
    } 
    
}
