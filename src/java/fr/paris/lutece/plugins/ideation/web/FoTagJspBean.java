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

import fr.paris.lutece.plugins.ideation.business.FoTag;
import fr.paris.lutece.plugins.ideation.business.FoTagHome;
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
 * This class provides the user interface to manage FoTag features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageFoTags.jsp", controllerPath = "jsp/admin/plugins/ideation/", right = "IDEATION_TAGS_MANAGEMENT" )
public class FoTagJspBean extends ManageIdeeTagsJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_FOTAGS = "/admin/plugins/ideation/manage_fotags.html";
    private static final String TEMPLATE_CREATE_FOTAG = "/admin/plugins/ideation/create_fotag.html";
    private static final String TEMPLATE_MODIFY_FOTAG = "/admin/plugins/ideation/modify_fotag.html";


    // Parameters
    private static final String PARAMETER_ID_FOTAG = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_FOTAGS = "ideation.manage_fotags.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_FOTAG = "ideation.modify_fotag.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_FOTAG = "ideation.create_fotag.pageTitle";

    // Markers
    private static final String MARK_FOTAG_LIST = "fotag_list";
    private static final String MARK_FOTAG = "fotag";

    private static final String JSP_MANAGE_FOTAGS = "jsp/admin/plugins/ideation/ManageFoTags.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_FOTAG = "ideation.message.confirmRemoveFoTag";
    private static final String PROPERTY_DEFAULT_LIST_FOTAG_PER_PAGE = "ideation.listFoTags.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "ideation.model.entity.fotag.attribute.";

    // Views
    private static final String VIEW_MANAGE_FOTAGS = "manageFoTags";
    private static final String VIEW_CREATE_FOTAG = "createFoTag";
    private static final String VIEW_MODIFY_FOTAG = "modifyFoTag";

    // Actions
    private static final String ACTION_CREATE_FOTAG = "createFoTag";
    private static final String ACTION_MODIFY_FOTAG = "modifyFoTag";
    private static final String ACTION_REMOVE_FOTAG = "removeFoTag";
    private static final String ACTION_CONFIRM_REMOVE_FOTAG = "confirmRemoveFoTag";

    // Infos
    private static final String INFO_FOTAG_CREATED = "ideation.info.fotag.created";
    private static final String INFO_FOTAG_UPDATED = "ideation.info.fotag.updated";
    private static final String INFO_FOTAG_REMOVED = "ideation.info.fotag.removed";
    
    // Session variable to store working values
    private FoTag _fotag;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_FOTAGS, defaultView = true )
    public String getManageFoTags( HttpServletRequest request )
    {
        _fotag = null;
        List<FoTag> listFoTags = (List<FoTag>) FoTagHome.getFoTagsList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_FOTAG_LIST, listFoTags, JSP_MANAGE_FOTAGS );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_FOTAGS, TEMPLATE_MANAGE_FOTAGS, model );
    }

    /**
     * Returns the form to create a fotag
     *
     * @param request The Http request
     * @return the html code of the fotag form
     */
    @View( VIEW_CREATE_FOTAG )
    public String getCreateFoTag( HttpServletRequest request )
    {
        _fotag = ( _fotag != null ) ? _fotag : new FoTag(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_FOTAG, _fotag );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_FOTAG, TEMPLATE_CREATE_FOTAG, model );
    }

    /**
     * Process the data capture form of a new fotag
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_FOTAG )
    public String doCreateFoTag( HttpServletRequest request )
    {
        populate( _fotag, request );

        // Check constraints
        if ( !validateBean( _fotag, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_FOTAG );
        }

        FoTagHome.create( _fotag );
        addInfo( INFO_FOTAG_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FOTAGS );
    }

    /**
     * Manages the removal form of a fotag whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_FOTAG )
    public String getConfirmRemoveFoTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FOTAG ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_FOTAG ) );
        url.addParameter( PARAMETER_ID_FOTAG, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_FOTAG,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a fotag
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage fotags
     */
    @Action( ACTION_REMOVE_FOTAG )
    public String doRemoveFoTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FOTAG ) );
        FoTagHome.remove( nId );
        addInfo( INFO_FOTAG_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FOTAGS );
    }

    /**
     * Returns the form to update info about a fotag
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_FOTAG )
    public String getModifyFoTag( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_FOTAG ) );

        if ( _fotag == null || ( _fotag.getId(  ) != nId ))
        {
            _fotag = FoTagHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_FOTAG, _fotag );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_FOTAG, TEMPLATE_MODIFY_FOTAG, model );
    }

    /**
     * Process the change form of a fotag
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_FOTAG )
    public String doModifyFoTag( HttpServletRequest request )
    {
        populate( _fotag, request );

        // Check constraints
        if ( !validateBean( _fotag, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_FOTAG, PARAMETER_ID_FOTAG, _fotag.getId( ) );
        }

        FoTagHome.update( _fotag );
        addInfo( INFO_FOTAG_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_FOTAGS );
    }
}
