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

import fr.paris.lutece.plugins.participatoryideation.business.DepositaireType;
import fr.paris.lutece.plugins.participatoryideation.business.DepositaireTypeHome;
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
 * This class provides the user interface to manage DepositaireType features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDepositaireTypes.jsp", controllerPath = "jsp/admin/plugins/participatoryideation/", right = "IDEATION_MANAGEMENT" )
public class DepositaireTypeJspBean extends ManageIdeationJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_DEPOSITAIRES = "/admin/plugins/participatoryideation/manage_depositairetypes.html";
    private static final String TEMPLATE_CREATE_DEPOSITAIRE = "/admin/plugins/participatoryideation/create_depositairetype.html";
    private static final String TEMPLATE_MODIFY_DEPOSITAIRE = "/admin/plugins/participatoryideation/modify_depositairetype.html";


    // Parameters
    private static final String PARAMETER_ID_DEPOSITAIRE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_DEPOSITAIRES = "participatoryideation.manage_depositairetypes.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_DEPOSITAIRE = "participatoryideation.modify_depositairetype.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_DEPOSITAIRE = "participatoryideation.create_depositairetype.pageTitle";

    // Markers
    private static final String MARK_DEPOSITAIRE_LIST = "depositairetype_list";
    private static final String MARK_DEPOSITAIRE = "depositairetype";

    private static final String JSP_MANAGE_DEPOSITAIRES = "jsp/admin/plugins/participatoryideation/ManageDepositaireTypes.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_DEPOSITAIRE = "participatoryideation.message.confirmRemoveDepositaireType";
    private static final String PROPERTY_DEFAULT_LIST_DEPOSITAIRE_PER_PAGE = "participatoryideation.listDepositaireTypes.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatoryideation.model.entity.depositairetype.attribute.";

    // Views
    private static final String VIEW_MANAGE_DEPOSITAIRES = "manageDepositaireTypes";
    private static final String VIEW_CREATE_DEPOSITAIRE = "createDepositaireType";
    private static final String VIEW_MODIFY_DEPOSITAIRE = "modifyDepositaireType";

    // Actions
    private static final String ACTION_CREATE_DEPOSITAIRE = "createDepositaireType";
    private static final String ACTION_MODIFY_DEPOSITAIRE = "modifyDepositaireType";
    private static final String ACTION_REMOVE_DEPOSITAIRE = "removeDepositaireType";
    private static final String ACTION_CONFIRM_REMOVE_DEPOSITAIRE = "confirmRemoveDepositaireType";

    // Infos
    private static final String INFO_DEPOSITAIRE_CREATED = "participatoryideation.info.depositairetype.created";
    private static final String INFO_DEPOSITAIRE_UPDATED = "participatoryideation.info.depositairetype.updated";
    private static final String INFO_DEPOSITAIRE_REMOVED = "participatoryideation.info.depositairetype.removed";
    
    // Session variable to store working values
    private DepositaireType _depositairetype;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_DEPOSITAIRES, defaultView = true )
    public String getManageDepositaireTypes( HttpServletRequest request )
    {
        _depositairetype = null;
        List<DepositaireType> listDepositaireTypes = (List<DepositaireType>) DepositaireTypeHome.getDepositaireTypesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_DEPOSITAIRE_LIST, listDepositaireTypes, JSP_MANAGE_DEPOSITAIRES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_DEPOSITAIRES, TEMPLATE_MANAGE_DEPOSITAIRES, model );
    }

    /**
     * Returns the form to create a depositairetype
     *
     * @param request The Http request
     * @return the html code of the depositairetype form
     */
    @View( VIEW_CREATE_DEPOSITAIRE )
    public String getCreateDepositaireType( HttpServletRequest request )
    {
        _depositairetype = ( _depositairetype != null ) ? _depositairetype : new DepositaireType(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_DEPOSITAIRE, _depositairetype );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_DEPOSITAIRE, TEMPLATE_CREATE_DEPOSITAIRE, model );
    }

    /**
     * Process the data capture form of a new depositairetype
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_DEPOSITAIRE )
    public String doCreateDepositaireType( HttpServletRequest request )
    {
        populate( _depositairetype, request );

        // Check constraints
        if ( !validateBean( _depositairetype, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_DEPOSITAIRE );
        }

        DepositaireTypeHome.create( _depositairetype );
        addInfo( INFO_DEPOSITAIRE_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DEPOSITAIRES );
    }

    /**
     * Manages the removal form of a depositairetype whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_DEPOSITAIRE )
    public String getConfirmRemoveDepositaireType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEPOSITAIRE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_DEPOSITAIRE ) );
        url.addParameter( PARAMETER_ID_DEPOSITAIRE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_DEPOSITAIRE,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a depositairetype
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage depositairetypes
     */
    @Action( ACTION_REMOVE_DEPOSITAIRE )
    public String doRemoveDepositaireType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEPOSITAIRE ) );
        DepositaireTypeHome.remove( nId );
        addInfo( INFO_DEPOSITAIRE_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DEPOSITAIRES );
    }

    /**
     * Returns the form to update info about a depositairetype
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_DEPOSITAIRE )
    public String getModifyDepositaireType( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_DEPOSITAIRE ) );

        if ( _depositairetype == null || ( _depositairetype.getId(  ) != nId ))
        {
            _depositairetype = DepositaireTypeHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_DEPOSITAIRE, _depositairetype );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_DEPOSITAIRE, TEMPLATE_MODIFY_DEPOSITAIRE, model );
    }

    /**
     * Process the change form of a depositairetype
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_DEPOSITAIRE )
    public String doModifyDepositaireType( HttpServletRequest request )
    {
        populate( _depositairetype, request );

        // Check constraints
        if ( !validateBean( _depositairetype, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_DEPOSITAIRE, PARAMETER_ID_DEPOSITAIRE, _depositairetype.getId( ) );
        }

        DepositaireTypeHome.update( _depositairetype );
        addInfo( INFO_DEPOSITAIRE_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_DEPOSITAIRES );
    }
}
