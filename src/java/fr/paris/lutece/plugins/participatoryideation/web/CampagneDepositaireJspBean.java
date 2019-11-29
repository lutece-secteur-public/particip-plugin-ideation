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

import fr.paris.lutece.plugins.participatoryideation.business.CampagneDepositaire;
import fr.paris.lutece.plugins.participatoryideation.business.CampagneDepositaireHome;
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
 * This class provides the user interface to manage CampagneDepositaire features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageCampagneDepositaires.jsp", controllerPath = "jsp/admin/plugins/participatoryideation/", right = "IDEATION_MANAGEMENT" )
public class CampagneDepositaireJspBean extends ManageIdeationJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_CAMPAGNEDEPOSITAIRES = "/admin/plugins/participatoryideation/manage_campagnedepositaires.html";
    private static final String TEMPLATE_CREATE_CAMPAGNEDEPOSITAIRE = "/admin/plugins/participatoryideation/create_campagnedepositaire.html";
    private static final String TEMPLATE_MODIFY_CAMPAGNEDEPOSITAIRE = "/admin/plugins/participatoryideation/modify_campagnedepositaire.html";


    // Parameters
    private static final String PARAMETER_ID_CAMPAGNEDEPOSITAIRE = "id";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEDEPOSITAIRES = "participatoryideation.manage_campagnedepositaires.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEDEPOSITAIRE = "participatoryideation.modify_campagnedepositaire.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEDEPOSITAIRE = "participatoryideation.create_campagnedepositaire.pageTitle";

    // Markers
    private static final String MARK_CAMPAGNEDEPOSITAIRE_LIST = "campagnedepositaire_list";
    private static final String MARK_CAMPAGNEDEPOSITAIRE = "campagnedepositaire";

    private static final String JSP_MANAGE_CAMPAGNEDEPOSITAIRES = "jsp/admin/plugins/participatoryideation/ManageCampagneDepositaires.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_CAMPAGNEDEPOSITAIRE = "participatoryideation.message.confirmRemoveCampagneDepositaire";
    private static final String PROPERTY_DEFAULT_LIST_CAMPAGNEDEPOSITAIRE_PER_PAGE = "participatoryideation.listCampagneDepositaires.itemsPerPage";
 
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatoryideation.model.entity.campagnedepositaire.attribute.";

    // Views
    private static final String VIEW_MANAGE_CAMPAGNEDEPOSITAIRES = "manageCampagneDepositaires";
    private static final String VIEW_CREATE_CAMPAGNEDEPOSITAIRE = "createCampagneDepositaire";
    private static final String VIEW_MODIFY_CAMPAGNEDEPOSITAIRE = "modifyCampagneDepositaire";

    // Actions
    private static final String ACTION_CREATE_CAMPAGNEDEPOSITAIRE = "createCampagneDepositaire";
    private static final String ACTION_MODIFY_CAMPAGNEDEPOSITAIRE = "modifyCampagneDepositaire";
    private static final String ACTION_REMOVE_CAMPAGNEDEPOSITAIRE = "removeCampagneDepositaire";
    private static final String ACTION_CONFIRM_REMOVE_CAMPAGNEDEPOSITAIRE = "confirmRemoveCampagneDepositaire";

    // Infos
    private static final String INFO_CAMPAGNEDEPOSITAIRE_CREATED = "participatoryideation.info.campagnedepositaire.created";
    private static final String INFO_CAMPAGNEDEPOSITAIRE_UPDATED = "participatoryideation.info.campagnedepositaire.updated";
    private static final String INFO_CAMPAGNEDEPOSITAIRE_REMOVED = "participatoryideation.info.campagnedepositaire.removed";
    
    // Session variable to store working values
    private CampagneDepositaire _campagnedepositaire;
    
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_CAMPAGNEDEPOSITAIRES, defaultView = true )
    public String getManageCampagneDepositaires( HttpServletRequest request )
    {
        _campagnedepositaire = null;
        List<CampagneDepositaire> listCampagneDepositaires = (List<CampagneDepositaire>) CampagneDepositaireHome.getCampagneDepositairesList(  );
        Map<String, Object> model = getPaginatedListModel( request, MARK_CAMPAGNEDEPOSITAIRE_LIST, listCampagneDepositaires, JSP_MANAGE_CAMPAGNEDEPOSITAIRES );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_CAMPAGNEDEPOSITAIRES, TEMPLATE_MANAGE_CAMPAGNEDEPOSITAIRES, model );
    }

    /**
     * Returns the form to create a campagnedepositaire
     *
     * @param request The Http request
     * @return the html code of the campagnedepositaire form
     */
    @View( VIEW_CREATE_CAMPAGNEDEPOSITAIRE )
    public String getCreateCampagneDepositaire( HttpServletRequest request )
    {
        _campagnedepositaire = ( _campagnedepositaire != null ) ? _campagnedepositaire : new CampagneDepositaire(  );

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNEDEPOSITAIRE, _campagnedepositaire );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_CAMPAGNEDEPOSITAIRE, TEMPLATE_CREATE_CAMPAGNEDEPOSITAIRE, model );
    }

    /**
     * Process the data capture form of a new campagnedepositaire
     *
     * @param request The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_CAMPAGNEDEPOSITAIRE )
    public String doCreateCampagneDepositaire( HttpServletRequest request )
    {
        populate( _campagnedepositaire, request );

        // Check constraints
        if ( !validateBean( _campagnedepositaire, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirectView( request, VIEW_CREATE_CAMPAGNEDEPOSITAIRE );
        }

        CampagneDepositaireHome.create( _campagnedepositaire );
        addInfo( INFO_CAMPAGNEDEPOSITAIRE_CREATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEDEPOSITAIRES );
    }

    /**
     * Manages the removal form of a campagnedepositaire whose identifier is in the http
     * request
     *
     * @param request The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_CAMPAGNEDEPOSITAIRE )
    public String getConfirmRemoveCampagneDepositaire( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEDEPOSITAIRE ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_CAMPAGNEDEPOSITAIRE ) );
        url.addParameter( PARAMETER_ID_CAMPAGNEDEPOSITAIRE, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CAMPAGNEDEPOSITAIRE,
                url.getUrl(  ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a campagnedepositaire
     *
     * @param request The Http request
     * @return the jsp URL to display the form to manage campagnedepositaires
     */
    @Action( ACTION_REMOVE_CAMPAGNEDEPOSITAIRE )
    public String doRemoveCampagneDepositaire( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEDEPOSITAIRE ) );
        CampagneDepositaireHome.remove( nId );
        addInfo( INFO_CAMPAGNEDEPOSITAIRE_REMOVED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEDEPOSITAIRES );
    }

    /**
     * Returns the form to update info about a campagnedepositaire
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_CAMPAGNEDEPOSITAIRE )
    public String getModifyCampagneDepositaire( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_CAMPAGNEDEPOSITAIRE ) );

        if ( _campagnedepositaire == null || ( _campagnedepositaire.getId(  ) != nId ))
        {
            _campagnedepositaire = CampagneDepositaireHome.findByPrimaryKey( nId );
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_CAMPAGNEDEPOSITAIRE, _campagnedepositaire );

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_CAMPAGNEDEPOSITAIRE, TEMPLATE_MODIFY_CAMPAGNEDEPOSITAIRE, model );
    }

    /**
     * Process the change form of a campagnedepositaire
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_CAMPAGNEDEPOSITAIRE )
    public String doModifyCampagneDepositaire( HttpServletRequest request )
    {
        populate( _campagnedepositaire, request );

        // Check constraints
        if ( !validateBean( _campagnedepositaire, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_CAMPAGNEDEPOSITAIRE, PARAMETER_ID_CAMPAGNEDEPOSITAIRE, _campagnedepositaire.getId( ) );
        }

        CampagneDepositaireHome.update( _campagnedepositaire );
        addInfo( INFO_CAMPAGNEDEPOSITAIRE_UPDATED, getLocale(  ) );

        return redirectView( request, VIEW_MANAGE_CAMPAGNEDEPOSITAIRES );
    }
}
