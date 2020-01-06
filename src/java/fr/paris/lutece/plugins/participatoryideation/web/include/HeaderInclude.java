/*
 * Copyright (c) 2002-2020, Mairie de Paris
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

package fr.paris.lutece.plugins.participatoryideation.web.include;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import fr.paris.lutece.plugins.participatoryideation.service.IdeeService;
import fr.paris.lutece.portal.service.content.PageData;
import fr.paris.lutece.portal.service.includes.PageInclude;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;


/**
 * Page include to add the
 */
public class HeaderInclude implements PageInclude
{
   
	public static final String MARK_HEADER_INCLUDE = "header_ideation_include";

    
    //session variable
    private static ICommentService _commentService;
    
    // Template
    private static final String TEMPLATE_HEADER_INCLUDE = "/skin/plugins/participatoryideation/header_include.html";
    
    // Properties
  
    // Mark
 
    private static final String MARK_HEADER_PROJECTS_SUBMITTED = "projectsHeaderSubmitted";
    private static final String MARK_HEADER_PROJECTS_PARTICIPATE = "projectsHeaderParticipate";
    private static final String MARK_HEADER_PROJECTS_COMMENTED = "projectsHeaderCommented";
    
    private static final String MARK_HEADER_COLOR = "header_color";
   
    
    private static final String CLASS_CSS_OUT = "logged-out";
    private static final String CLASS_CSS_IN = "logged-in";
  
    private static IResourceExtenderHistoryService _resourceExtenderHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

   

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillTemplate( Map<String, Object> rootModel, PageData data, int nMode, HttpServletRequest request )
    {                        
        rootModel.put( MARK_HEADER_INCLUDE, getHeaderTemplate(request));
    }
    
    /**
     * 
     * @param request
     * @return
     */
    private static String getHeaderTemplate ( HttpServletRequest request )
    {
    	 Map<String, Object> model = new HashMap<String, Object>(  );
         Locale locale = ( request == null ) ? null : request.getLocale(  );
         
         LuteceUser _user =  SecurityService.getInstance(  ).getRegisteredUser( request );

         model.put( MARK_HEADER_COLOR, CLASS_CSS_OUT );
         model.put( MARK_HEADER_PROJECTS_SUBMITTED, "" );  
         model.put( MARK_HEADER_PROJECTS_PARTICIPATE, "" );
         model.put( MARK_HEADER_PROJECTS_COMMENTED, "" );
         
         if ( _user != null )
         {    
        	    String strLuteceUserName= _user.getName( );
        		IdeeSearcher _ideeSearcher= new IdeeSearcher();
            	_ideeSearcher.setLuteceUserName(strLuteceUserName);
            	_ideeSearcher.setIsPublished(true);
            	
            	Collection<Idee> ideesSubmitted=IdeeHome.getIdeesListSearch(_ideeSearcher);
            	
            	CommentFilter _commentFilter= new CommentFilter();
            	_commentFilter.setLuteceUserName(strLuteceUserName);
            	
            	Collection<Idee> ideesCommented=getIdeesCommentedByUser(getCommentService().findByResource("*", Idee.PROPERTY_RESOURCE_TYPE, _commentFilter, 0, 10000, false));
            	 
            	 ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter(  );

                 filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
                 filter.setUserGuid( strLuteceUserName );
                 //filter.setIdExtendableResource("*");
                
                 List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

                 Collection<Idee> ideesParticipate= getIdeesParticipatedByUser(listHistories);
                 
            	
	             model.put( MARK_HEADER_COLOR, CLASS_CSS_IN );
	             model.put( MARK_HEADER_PROJECTS_SUBMITTED, ideesSubmitted.size( ) ); 
	             model.put( MARK_HEADER_PROJECTS_PARTICIPATE, ideesParticipate.size( ) );
	             model.put( MARK_HEADER_PROJECTS_COMMENTED, ideesCommented.size( ) );
         }                         

         HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_HEADER_INCLUDE, locale, model );

         return template.getHtml(  );
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
    private static Collection<Idee> getIdeesCommentedByUser(Collection<Comment> listComments){
    	
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
    private static Collection<Idee> getIdeesParticipatedByUser(Collection<ResourceExtenderHistory> listFollow){
    	
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
