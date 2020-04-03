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

import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryType;
import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryTypeHome;
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
 * This class provides the user interface to manage DepositaryType features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDepositaryTypes.jsp", controllerPath = "jsp/admin/plugins/participatoryideation/", right = "PARTICIPATORYIDEATION_MANAGEMENT" )
public class DepositaryTypeJspBean extends ManageIdeationJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_DEPOSITARIES = "/admin/plugins/participatoryideation/manage_depositarytypes.html";
    private static final String TEMPLATE_CREATE_DEPOSITARY = "/admin/plugins/participatoryideation/create_depositarytype.html";
    private static final String TEMPLATE_MODIFY_DEPOSITARY = "/admin/plugins/participatoryideation/modify_depositarytype.html";

    // Parameters
    private static final String PARAMETER_ID_DEPOSITARY = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DEPOSITARIES = "participatoryideation.manage_depositarytypes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DEPOSITARY = "participatoryideation.modify_depositarytype.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DEPOSITARY = "participatoryideation.create_depositarytype.pageTitle";

    // Markers
    private static final String MARK_DEPOSITARY_LIST = "depositarytype_list";
    private static final String MARK_DEPOSITARY = "depositarytype";

    private static final String JSP_MANAGE_DEPOSITARIES = "jsp/admin/plugins/participatoryideation/ManageDepositaryTypes.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DEPOSITARY = "participatoryideation.message.confirmRemoveDepositaryType";
    private static final String PROPERTY_DEFAULT_LIST_DEPOSITARY_PER_PAGE = "participatoryideation.listDepositaryTypes.itemsPerPage";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatoryideation.model.entity.depositarytype.attribute.";

    // Views
    private static final String VIEW_MANAGE_DEPOSITARIES = "manageDepositaryTypes";
    private static final String VIEW_CREATE_DEPOSITARY = "createDepositaryType";
    private static final String VIEW_MODIFY_DEPOSITARY = "modifyDepositaryType";

    // Actions
    private static final String ACTION_CREATE_DEPOSITARY = "createDepositaryType";
    private static final String ACTION_MODIFY_DEPOSITARY = "modifyDepositaryType";
    private static final String ACTION_REMOVE_DEPOSITARY = "removeDepositaryType";
    private static final String ACTION_CONFIRM_REMOVE_DEPOSITARY = "confirmRemoveDepositaryType";

    // Infos
    private static final String INFO_DEPOSITARY_CREATED = "participatoryideation.info.depositarytype.created";
    private static final String INFO_DEPOSITARY_UPDATED = "participatoryideation.info.depositarytype.updated";
    private static final String INFO_DEPOSITARY_REMOVED = "participatoryideation.info.depositarytype.removed";

    // Session variable to store working values
    private DepositaryType _depositarytype;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEPOSITARIES, defaultView = true )
    public String getManageDepositaryTypes( HttpServletRequest request )
    {
        _depositarytype = null;
        List<DepositaryType> listDepositaryTypes = (List<DepositaryType>) DepositaryTypeHome.getDepositaryTypesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DEPOSITARY_LIST, listDepositaryTypes, JSP_MANAGE_DEPOSITARIES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEPOSITARIES, TEMPLATE_MANAGE_DEPOSITARIES, model );
    }

    /**
     * Returns the form to create a depositarytype
     *
     * @param request
     *            The Http request
     * @return the html code of the depositarytype form
     */
    @View( VIEW_CREATE_DEPOSITARY )
    public String getCreateDepositaryType( HttpServletRequest request )
    {
        _depositarytype = ( _depositarytype != null ) ? _depositarytype : new DepositaryType( );

        Map<String, Object> model = getModel( );
        model.put( MARK_DEPOSITARY, _depositarytype );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DEPOSITARY, TEMPLATE_CREATE_DEPOSITARY, model );
    }

    /**
     * Process the data capture form of a new depositarytype
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DEPOSITARY )
    public String doCreateDepositaryType( HttpServletRequest request )
    {
        populate( _depositarytype, request );

        // Check constraints
        if ( !validateBean( _depositarytype, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DEPOSITARY );
        }

        DepositaryTypeHome.create( _depositarytype );
        addInfo( INFO_DEPOSITARY_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DEPOSITARIES );
    }

    /**
     * Manages the removal form of a depositarytype whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DEPOSITARY )
    public String getConfirmRemoveDepositaryType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEPOSITARY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DEPOSITARY ) );
        url.addParameter( PARAMETER_ID_DEPOSITARY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DEPOSITARY, url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a depositarytype
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage depositarytypes
     */
    @Action( ACTION_REMOVE_DEPOSITARY )
    public String doRemoveDepositaryType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEPOSITARY ) );
        DepositaryTypeHome.remove( nId );
        addInfo( INFO_DEPOSITARY_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DEPOSITARIES );
    }

    /**
     * Returns the form to update info about a depositarytype
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DEPOSITARY )
    public String getModifyDepositaryType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEPOSITARY ) );

        if ( _depositarytype == null || ( _depositarytype.getId( ) != nId ) )
        {
            _depositarytype = DepositaryTypeHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_DEPOSITARY, _depositarytype );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DEPOSITARY, TEMPLATE_MODIFY_DEPOSITARY, model );
    }

    /**
     * Process the change form of a depositarytype
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DEPOSITARY )
    public String doModifyDepositaryType( HttpServletRequest request )
    {
        populate( _depositarytype, request );

        // Check constraints
        if ( !validateBean( _depositarytype, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DEPOSITARY, PARAMETER_ID_DEPOSITARY, _depositarytype.getId( ) );
        }

        DepositaryTypeHome.update( _depositarytype );
        addInfo( INFO_DEPOSITARY_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_DEPOSITARIES );
    }
}
