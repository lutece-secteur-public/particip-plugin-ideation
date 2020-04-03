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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatoryideation.business.depositary.Depositary;
import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryHome;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides the user interface to manage Depositary features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageDepositaries.jsp", controllerPath = "jsp/admin/plugins/participatoryideation/", right = "PARTICIPATORYIDEATION_MANAGEMENT" )
public class DepositaryJspBean extends ManageIdeationJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAIGNDEPOSITARIES = "/admin/plugins/participatoryideation/manage_campaigndepositaries.html";
    private static final String TEMPLATE_CREATE_CAMPAIGNDEPOSITARY = "/admin/plugins/participatoryideation/create_campaigndepositary.html";
    private static final String TEMPLATE_MODIFY_CAMPAIGNDEPOSITARY = "/admin/plugins/participatoryideation/modify_campaigndepositary.html";

    // Parameters
    private static final String PARAMETER_ID_CAMPAIGNDEPOSITARY = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNDEPOSITARIES = "participatoryideation.manage_campaigndepositaries.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNDEPOSITARY = "participatoryideation.modify_campaigndepositary.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNDEPOSITARY = "participatoryideation.create_campaigndepositary.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGNDEPOSITARY_LIST = "campaigndepositary_list";
    private static final String MARK_CAMPAIGNDEPOSITARY = "campaigndepositary";

    private static final String JSP_MANAGE_CAMPAIGNDEPOSITARIES = "jsp/admin/plugins/participatoryideation/ManageDepositaries.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAIGNDEPOSITARY = "participatoryideation.message.confirmRemoveDepositary";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAIGNDEPOSITARY_PER_PAGE = "participatoryideation.listDepositaries.itemsPerPage";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatoryideation.model.entity.campaigndepositary.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAIGNDEPOSITARIES = "manageDepositaries";
    private static final String VIEW_CREATE_CAMPAIGNDEPOSITARY = "createDepositary";
    private static final String VIEW_MODIFY_CAMPAIGNDEPOSITARY = "modifyDepositary";

    // Actions
    private static final String ACTION_CREATE_CAMPAIGNDEPOSITARY = "createDepositary";
    private static final String ACTION_MODIFY_CAMPAIGNDEPOSITARY = "modifyDepositary";
    private static final String ACTION_REMOVE_CAMPAIGNDEPOSITARY = "removeDepositary";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAIGNDEPOSITARY = "confirmRemoveDepositary";

    // Infos
    private static final String INFO_CAMPAIGNDEPOSITARY_CREATED = "participatoryideation.info.campaigndepositary.created";
    private static final String INFO_CAMPAIGNDEPOSITARY_UPDATED = "participatoryideation.info.campaigndepositary.updated";
    private static final String INFO_CAMPAIGNDEPOSITARY_REMOVED = "participatoryideation.info.campaigndepositary.removed";

    // Session variable to store working values
    private Depositary _campaigndepositary;

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAIGNDEPOSITARIES, defaultView = true )
    public String getManageDepositaries( HttpServletRequest request )
    {
        _campaigndepositary = null;
        List<Depositary> listDepositaries = (List<Depositary>) DepositaryHome.getDepositariesList( );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAIGNDEPOSITARY_LIST, listDepositaries, JSP_MANAGE_CAMPAIGNDEPOSITARIES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAIGNDEPOSITARIES, TEMPLATE_MANAGE_CAMPAIGNDEPOSITARIES, model );
    }

    /**
     * Returns the form to create a campaigndepositary
     *
     * @param request
     *            The Http request
     * @return the html code of the campaigndepositary form
     */
    @View( VIEW_CREATE_CAMPAIGNDEPOSITARY )
    public String getCreateDepositary( HttpServletRequest request )
    {
        _campaigndepositary = ( _campaigndepositary != null ) ? _campaigndepositary : new Depositary( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNDEPOSITARY, _campaigndepositary );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAIGNDEPOSITARY, TEMPLATE_CREATE_CAMPAIGNDEPOSITARY, model );
    }

    /**
     * Process the data capture form of a new campaigndepositary
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAIGNDEPOSITARY )
    public String doCreateDepositary( HttpServletRequest request )
    {
        populate( _campaigndepositary, request );

        // Check constraints
        if ( !validateBean( _campaigndepositary, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAIGNDEPOSITARY );
        }

        DepositaryHome.create( _campaigndepositary );
        addInfo( INFO_CAMPAIGNDEPOSITARY_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNDEPOSITARIES );
    }

    /**
     * Manages the removal form of a campaigndepositary whose identifier is in the http request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAIGNDEPOSITARY )
    public String getConfirmRemoveDepositary( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNDEPOSITARY ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAIGNDEPOSITARY ) );
        url.addParameter( PARAMETER_ID_CAMPAIGNDEPOSITARY, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAIGNDEPOSITARY, url.getUrl( ),
                AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campaigndepositary
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage campaigndepositaries
     */
    @Action( ACTION_REMOVE_CAMPAIGNDEPOSITARY )
    public String doRemoveDepositary( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNDEPOSITARY ) );
        DepositaryHome.remove( nId );
        addInfo( INFO_CAMPAIGNDEPOSITARY_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNDEPOSITARIES );
    }

    /**
     * Returns the form to update info about a campaigndepositary
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAIGNDEPOSITARY )
    public String getModifyDepositary( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAIGNDEPOSITARY ) );

        if ( _campaigndepositary == null || ( _campaigndepositary.getId( ) != nId ) )
        {
            _campaigndepositary = DepositaryHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGNDEPOSITARY, _campaigndepositary );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAIGNDEPOSITARY, TEMPLATE_MODIFY_CAMPAIGNDEPOSITARY, model );
    }

    /**
     * Process the change form of a campaigndepositary
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAIGNDEPOSITARY )
    public String doModifyDepositary( HttpServletRequest request )
    {
        populate( _campaigndepositary, request );

        // Check constraints
        if ( !validateBean( _campaigndepositary, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAIGNDEPOSITARY, PARAMETER_ID_CAMPAIGNDEPOSITARY, _campaigndepositary.getId( ) );
        }

        DepositaryHome.update( _campaigndepositary );
        addInfo( INFO_CAMPAIGNDEPOSITARY_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_CAMPAIGNDEPOSITARIES );
    }
}
