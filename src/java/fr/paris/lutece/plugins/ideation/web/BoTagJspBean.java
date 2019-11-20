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

 
package fr.paris.lutece.plugins.ideation.web;

import fr.paris.lutece.plugins.ideation.business.BoTag;
import fr.paris.lutece.plugins.ideation.business.BoTagHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;


/**
 * This class provides the user interface to manage BoTag features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageBoTags.jsp", controllerPath = "jsp/admin/plugins/ideation/", right = "IDEATION_TAGS_MANAGEMENT" )
public class BoTagJspBean extends ManageIdeeTagsJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_BOTAGS = "/admin/plugins/ideation/manage_botags.html";
    private static final String TEMPLATE_CREATE_BOTAG = "/admin/plugins/ideation/create_botag.html";
    private static final String TEMPLATE_MODIFY_BOTAG = "/admin/plugins/ideation/modify_botag.html";


    // Parameters
    private static final String PARAMETER_ID_BOTAG = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_BOTAGS = "ideation.manage_botags.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_BOTAG = "ideation.modify_botag.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_BOTAG = "ideation.create_botag.pageTitle";

    // Markers
    private static final String MARK_BOTAG_LIST = "botag_list";
    private static final String MARK_BOTAG = "botag";

    private static final String JSP_MANAGE_BOTAGS = "jsp/admin/plugins/ideation/ManageBoTags.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_BOTAG = "ideation.message.confirmRemoveBoTag";
    private static final String PROPERTY_DEFAULT_LIST_BOTAG_PER_PAGE = "ideation.listBoTags.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "ideation.model.entity.botag.attribute.";

    // Views
    private static final String VIEW_MANAGE_BOTAGS = "manageBoTags";
    private static final String VIEW_CREATE_BOTAG = "createBoTag";
    private static final String VIEW_MODIFY_BOTAG = "modifyBoTag";

    // Actions
    private static final String ACTION_CREATE_BOTAG = "createBoTag";
    private static final String ACTION_MODIFY_BOTAG = "modifyBoTag";
    private static final String ACTION_REMOVE_BOTAG = "removeBoTag";
    private static final String ACTION_CONFIRM_REMOVE_BOTAG = "confirmRemoveBoTag";

    // Infos
    private static final String INFO_BOTAG_CREATED = "ideation.info.botag.created";
    private static final String INFO_BOTAG_UPDATED = "ideation.info.botag.updated";
    private static final String INFO_BOTAG_REMOVED = "ideation.info.botag.removed";
    
    // Session variable to store working values
    private BoTag _botag;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_BOTAGS, defaultView = true )
    public String getManageBoTags( HttpServletRequest request )
    {
        _botag = null;
        List<BoTag> listBoTags = (List<BoTag>) BoTagHome.getBoTagsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_BOTAG_LIST, listBoTags, JSP_MANAGE_BOTAGS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_BOTAGS, TEMPLATE_MANAGE_BOTAGS, model );
    }

    /**
     * Returns the form to create a botag
     *
     * @param request The Http request
     * @return the html code of the botag form
     */
    @View( VIEW_CREATE_BOTAG )
    public String getCreateBoTag( HttpServletRequest request )
    {
        _botag = ( _botag != null ) ? _botag : new BoTag(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_BOTAG, _botag );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_BOTAG, TEMPLATE_CREATE_BOTAG, model );
    }

    /**
     * Process the data capture form of a new botag
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_BOTAG )
    public String doCreateBoTag( HttpServletRequest request )
    {
        populate( _botag, request );

        // Check constraints
        if ( !validateBean( _botag, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_BOTAG );
        }

        BoTagHome.create( _botag );
        addInfo( INFO_BOTAG_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_BOTAGS );
    }

    /**
     * Manages the removal form of a botag whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_BOTAG )
    public String getConfirmRemoveBoTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BOTAG ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_BOTAG ) );
        url.addParameter( PARAMETER_ID_BOTAG, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_BOTAG,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a botag
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage botags
     */
    @Action( ACTION_REMOVE_BOTAG )
    public String doRemoveBoTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BOTAG ) );
        BoTagHome.remove( nId );
        addInfo( INFO_BOTAG_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_BOTAGS );
    }

    /**
     * Returns the form to update info about a botag
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_BOTAG )
    public String getModifyBoTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_BOTAG ) );

        if ( _botag == null || ( _botag.getId(  ) != nId ))
        {
            _botag = BoTagHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_BOTAG, _botag );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_BOTAG, TEMPLATE_MODIFY_BOTAG, model );
    }

    /**
     * Process the change form of a botag
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_BOTAG )
    public String doModifyBoTag( HttpServletRequest request )
    {
        populate( _botag, request );

        // Check constraints
        if ( !validateBean( _botag, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_BOTAG, PARAMETER_ID_BOTAG, _botag.getId( ) );
        }

        BoTagHome.update( _botag );
        addInfo( INFO_BOTAG_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_BOTAGS );
    }
}
