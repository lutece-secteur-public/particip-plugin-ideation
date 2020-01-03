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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneTheme;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneThemeHome;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeSearcher;
import fr.paris.lutece.plugins.participatoryideation.business.capgeo.QpvQva;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationErrorException;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeUsersService;
import fr.paris.lutece.plugins.participatoryideation.service.SolrIdeeIndexer;
import fr.paris.lutece.plugins.participatoryideation.service.capgeo.QpvQvaService;
import fr.paris.lutece.plugins.participatoryideation.util.Constants;
import fr.paris.lutece.plugins.participatoryideation.util.CsvUtils;
import fr.paris.lutece.plugins.participatoryideation.util.IdeeExportUtils;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;


/**
 * This class provides the user interface to manage Idee features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageIdees.jsp", controllerPath = "jsp/admin/plugins/participatoryideation/", right = "IDEATION_IDEES_MANAGEMENT" )
public class IdeeJspBean extends ManageIdeationIdeesJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    // templates
    private static final String TEMPLATE_MANAGE_IDEES        = "/admin/plugins/participatoryideation/manage_idees.html";
    private static final String TEMPLATE_CREATE_IDEE         = "/admin/plugins/participatoryideation/create_idee.html";
    private static final String TEMPLATE_MODIFY_IDEE         = "/admin/plugins/participatoryideation/modify_idee.html";
    private static final String TEMPLATE_CONFIRM_REMOVE_IDEE = "/admin/plugins/participatoryideation/confirm_remove_idee.html";


    // Parameters
    private static final String PARAMETER_ID_IDEE = "id";
    private static final String PARAMETER_FILTER_CODE_CAMPAGNE = "filter_code_campagne";
    private static final String PARAMETER_FILTER_CODE_THEME = "filter_code_theme";
    private static final String PARAMETER_FILTER_TITRE_OU_DESCRIPTION = "filter_titre_ou_description";
    private static final String PARAMETER_FILTER_PUBLIC_STATE = "filter_status";
    private static final String PARAMETER_FILTER_QPVQVA = "filter_qpvqva";
    private static final String PARAMETER_FILTER_HANDICAP = "filter_handicap";
    private static final String PARAMETER_FILTER_TYPE_LOCALISATION = "filter_type_localisation";
    private static final String PARAMETER_FILTER_ARRONDISSEMENT    = "filter_arrondissement";
    private static final String PARAMETER_SORT_COLUMN = "sort_column";
    private static final String PARAMETER_SORT_ORDER = "sort_order";
    private static final String PARAMETER_MOTIFRECEV = "motifRecev";
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_IDEES = "participatoryideation.manage_idees.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_IDEE = "participatoryideation.create_idee.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_IDEE = "participatoryideation.modify_idee.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CONFIRM_REMOVE_IDEE = "participatoryideation.confirm_remove_idee.pageTitle";

    // Markers
    private static final String MARK_ARRONDISSEMENTS_LIST = "arrondissements_list";
    private static final String MARK_CAMPAGNETHEME_LIST = "campagnetheme_list";
    private static final String MARK_HANDICAP_LIST = "handicap_list";
    private static final String MARK_IDEE_LIST = "idee_list";
    private static final String MARK_IDEE = "idee";
    private static final String MARK_IDEE_BO_FORM = "idee_bo_form";
    private static final String MARK_FILTER_CODE_CAMPAGNE = "filter_code_campagne";
    private static final String MARK_FILTER_CODE_THEME = "filter_code_theme";
    private static final String MARK_FILTER_TITRE_OU_DESCRIPTION = "filter_titre_ou_description";
    private static final String MARK_FILTER_QPVQVA = "filter_qpvqva";
    private static final String MARK_FILTER_HANDICAP = "filter_handicap";
    private static final String MARK_FILTER_TYPE_LOCALISATION = "filter_type_localisation";
    private static final String MARK_FILTER_ARRONDISSEMENT    = "filter_arrondissement";
    private static final String MARK_LANGUAGE = "language";
    private static final String MARK_LOCALISATION_TYPE_LIST = "type_localisation_list";
    private static final String MARK_SORT_COLUMN = "sort_column";
    private static final String MARK_SORT_ORDER = "sort_order";
    private static final String MARK_WORKFLOW_STATES_LIST = "workflow_states_list";
    private static final String MARK_WORKFLOW_STATES_MAP = "workflow_states_map";

    private static final String MARK_RESOURCE_HISTORY = "workflow_history";

    private static final String JSP_MANAGE_IDEES = "jsp/admin/plugins/participatoryideation/ManageIdees.jsp";

    // Properties
    private static final String MESSAGE_ERROR_IDEE_REMOVED = "participatoryideation.message.error.idee.removed";
    
    private static final String MESSAGE_ERROR_ARRONDISSEMENT_EMPTY = "participatoryideation.validation.idee.Arrondissement.notEmpty";
    private static final String MESSAGE_ERROR_ADDRESS_FORMAT = "participatoryideation.validation.idee.Address.Format";
    private static final String MESSAGE_ERROR_ADDRESS_NOT_VALID = "participatoryideation.validation.idee.Address.NotValid";
    private static final String MESSAGE_ERROR_ADDRESS_ARDT_MISMATCH = "participatoryideation.validation.idee.Address.ArdtMismatch";
    private static final String MESSAGE_ERROR_ADDRESS_LOCALISATION_TYPE_EMPTY = "participatoryideation.validation.idee.LocalisationType.NotEmpty";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatoryideation.model.entity.idee.attribute.";

    private static final String PROPERTY_CSV_EXTENSION = "participatoryideation.csv.extension";
    private static final String PROPERTY_CSV_FILE_NAME = "participatoryideation.csv.file.name";
    
    // Views
    private static final String VIEW_MANAGE_IDEES = "manageIdees";
    private static final String VIEW_CREATE_IDEE = "createIdee";
    private static final String VIEW_MODIFY_IDEE = "modifyIdee";
    private static final String VIEW_CONFIRM_REMOVE_IDEE = "confirmRemoveIdee";

    // Actions
    private static final String ACTION_CREATE_IDEE = "createIdee";
    private static final String ACTION_MODIFY_IDEE = "modifyIdee";
    private static final String ACTION_SEARCH_IDEE = "searchIdee";
    private static final String ACTION_CANCEL_SEARCH = "cancelSearch";
    private static final String ACTION_CONFIRM_REMOVE_IDEE = "confirmRemoveIdee";

    // Infos
    private static final String INFO_IDEE_CREATED = "participatoryideation.info.idee.created";
    private static final String INFO_IDEE_UPDATED = "participatoryideation.info.idee.updated";
    private static final String INFO_IDEE_REMOVED = "participatoryideation.info.idee.removed";

    private static SolrIdeeIndexer _solrIdeeIndexer  = SpringContextService.getBean( "participatoryideation.solrIdeeIndexer" );
    
    private static final java.util.regex.Pattern _patternAdresseArrondissement = java.util.regex.Pattern.compile( ", 75[0-1]([0-2][0-9]) PARIS" );
    
    // Session variable to store working values
    private IdeeBoForm _ideeBoForm;
    private Idee _idee;
    private IdeeSearcher _ideeSearcher;
    private static IdeeSearcher defaultSearcher;
   
    static {
       defaultSearcher = new IdeeSearcher();
       defaultSearcher.setOrderAscDesc( IdeeSearcher.ORDER_DESC );
       defaultSearcher.setOrderColumn( IdeeSearcher.COLUMN_REFERENCE );
    }
    
    // ***********************************************************************************
    // * MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MA *
    // * MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MA *
    // ***********************************************************************************
    
    /**
     * Build the Manage View
     * @param request The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_IDEES, defaultView = true )
    public String getManageIdees( HttpServletRequest request )
    {
        _idee = null;
        _ideeBoForm = null;
        IdeeSearcher currentSearcher = _ideeSearcher != null ? _ideeSearcher : defaultSearcher;
        List<Idee> listIdees = (List<Idee>) IdeeHome.getIdeesListSearch( currentSearcher );
        Map<String, Object> model = getPaginatedListModel( request, MARK_IDEE_LIST, listIdees, JSP_MANAGE_IDEES );

        if ( _ideeSearcher != null ) {
            if ( StringUtils.isNotBlank( _ideeSearcher.getCodeCampagne(  ) ) ) {
                model.put( MARK_FILTER_CODE_CAMPAGNE , _ideeSearcher.getCodeCampagne(  ) );
            }
            if ( StringUtils.isNotBlank( _ideeSearcher.getCodeTheme(  ) ) ) {
                model.put( MARK_FILTER_CODE_THEME , _ideeSearcher.getCodeTheme(  ) );
            }
            if ( StringUtils.isNotBlank( _ideeSearcher.getTitreOuDescriptionouRef(  ) ) ) {
                model.put( MARK_FILTER_TITRE_OU_DESCRIPTION , _ideeSearcher.getTitreOuDescriptionouRef(  ) );
            }
            if ( _ideeSearcher.getStatusPublic( ) != null) {
                model.put( PARAMETER_FILTER_PUBLIC_STATE , _ideeSearcher.getStatusPublic( ) );
            }
            if ( StringUtils.isNotBlank ( _ideeSearcher.getTypeQpvQva (  ) ) ) {
                model.put( MARK_FILTER_QPVQVA , _ideeSearcher.getTypeQpvQva(  ) );
            }
            if ( StringUtils.isNotBlank ( _ideeSearcher.getHandicap (  ) ) ) {
                model.put( MARK_FILTER_HANDICAP , _ideeSearcher.getHandicap(  ) );
            }
            if ( StringUtils.isNotBlank ( _ideeSearcher.getTypeLocalisation (  ) ) ) {
                model.put( MARK_FILTER_TYPE_LOCALISATION , _ideeSearcher.getTypeLocalisation(  ) );
            }
            if ( StringUtils.isNotBlank ( _ideeSearcher.getArrondissement (  ) ) ) {
                model.put( MARK_FILTER_ARRONDISSEMENT , _ideeSearcher.getArrondissement( ) );
            }
            if ( StringUtils.isNotBlank ( _ideeSearcher.getOrderColumn (  ) ) ) {
                model.put( MARK_SORT_COLUMN , _ideeSearcher.getOrderColumn(  ) );
            }
            if ( StringUtils.isNotBlank ( _ideeSearcher.getOrderAscDesc (  ) ) ) {
                model.put( MARK_SORT_ORDER , _ideeSearcher.getOrderAscDesc(  ) );
            }
        }

        IdeationStaticService.getInstance(  ).fillAllStaticContent( model );

        //TODO cache this ?
        if (WorkflowService.getInstance(  ).isAvailable()) {
            
        	List<Idee.Status> enumList = Arrays.asList(Idee.Status.values());
            ReferenceList WorkflowStatesReferenceList = new ReferenceList();
            
            for( Idee.Status status: enumList ) 
            {
                WorkflowStatesReferenceList.addItem( status.getValeur( ) , I18nService.getLocalizedString( status.getLibelle( ) , new Locale("fr","FR") ) );
            }
            model.put ( MARK_WORKFLOW_STATES_LIST, WorkflowStatesReferenceList );
            model.put ( MARK_WORKFLOW_STATES_MAP, WorkflowStatesReferenceList.toMap() );
        }

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_IDEES, TEMPLATE_MANAGE_IDEES, model );
    }

    // ***********************************************************************************
    // * SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SE *
    // * SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SE *
    // ***********************************************************************************
    
    /**
     * Process the search filters/sorts
     * @param request The HTTP request
     * @return The page
     */
    @Action( value = ACTION_SEARCH_IDEE )
    public String doSearchIdee( HttpServletRequest request )
    {
        if ( _ideeSearcher == null ) {
            _ideeSearcher = new IdeeSearcher();
        }

        String strCodeCampagne = request.getParameter( PARAMETER_FILTER_CODE_CAMPAGNE );
        if ( strCodeCampagne != null ) {
            if ( StringUtils.isBlank( strCodeCampagne ) ) {
                _ideeSearcher.setCodeCampagne( null );
            }
            else {
                _ideeSearcher.setCodeCampagne( strCodeCampagne );
            }
        }

        String strCodeTheme = request.getParameter( PARAMETER_FILTER_CODE_THEME );
        if ( strCodeTheme != null ) {
            if ( StringUtils.isBlank( strCodeTheme ) ) {
                _ideeSearcher.setCodeTheme( null );
            }
            else {
                _ideeSearcher.setCodeTheme( strCodeTheme );
            }
        }

        String strTitre = request.getParameter( PARAMETER_FILTER_TITRE_OU_DESCRIPTION );
        if ( strTitre != null ) {
            if ( StringUtils.isBlank( strTitre ) ) {
                _ideeSearcher.setTitreOuDescriptionouRef( null );
            }
            else {
                _ideeSearcher.setTitreOuDescriptionouRef( strTitre );
            }
        }

        String strPublicState = request.getParameter( PARAMETER_FILTER_PUBLIC_STATE );
        if ( strPublicState != null ) {
            if ( StringUtils.isBlank( strPublicState ) ) {
                _ideeSearcher.setStatusPublic( null );
            }
            else {
                _ideeSearcher.setStatusPublic( strPublicState  );
            }
        }

        String strTypeQpvQva = request.getParameter( PARAMETER_FILTER_QPVQVA );
        if ( strTypeQpvQva != null ) {
            if ( StringUtils.isBlank( strTypeQpvQva ) ) {
                _ideeSearcher.setTypeQpvQva( null );
            }
            else {
                _ideeSearcher.setTypeQpvQva( strTypeQpvQva );
            }
        }

        String strHandicap = request.getParameter( PARAMETER_FILTER_HANDICAP );
        if ( strHandicap != null ) {
            if ( StringUtils.isBlank( strHandicap ) ) {
                _ideeSearcher.setHandicap( null );
            }
            else {
                _ideeSearcher.setHandicap( strHandicap );
            }
        }

        String strTypeLocalisation = request.getParameter( PARAMETER_FILTER_TYPE_LOCALISATION );
        if ( strTypeLocalisation != null ) {
            if ( StringUtils.isBlank( strTypeLocalisation ) ) {
                _ideeSearcher.setTypeLocalisation( null );
            }
            else {
                _ideeSearcher.setTypeLocalisation( strTypeLocalisation );
            }
        }

        String strArrondissement = request.getParameter( PARAMETER_FILTER_ARRONDISSEMENT );
        if ( strArrondissement != null ) {
            if ( StringUtils.isBlank( strArrondissement ) ) {
                _ideeSearcher.setArrondissement( null );
            }
            else {
                _ideeSearcher.setArrondissement( strArrondissement );
            }
        }

        String strSortColumn = request.getParameter( PARAMETER_SORT_COLUMN );
        if ( strSortColumn != null ) {
            if ( StringUtils.isBlank( strSortColumn ) ) {
                _ideeSearcher.setOrderColumn( null );
            }
            else {
                _ideeSearcher.setOrderColumn( strSortColumn );
            }
        }

        String strSortAscDesc = request.getParameter( PARAMETER_SORT_ORDER );
        if ( strSortAscDesc != null ) {
            if ( StringUtils.isBlank( strSortAscDesc ) ) {
                _ideeSearcher.setOrderAscDesc( null );
            }
            else {
                _ideeSearcher.setOrderAscDesc( strSortAscDesc );
            }
        }

        return redirectView( request, VIEW_MANAGE_IDEES );
    }

    /**
     * Reset the search
     * @param request The HTTP request
     * @return The page
     */
    @Action( value = ACTION_CANCEL_SEARCH )
    public String doCancelSearch( HttpServletRequest request )
    {
        _ideeSearcher = null;
        return redirectView( request, VIEW_MANAGE_IDEES );
    }

    // ***********************************************************************************
    // * CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CR *
    // * CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CR *
    // ***********************************************************************************

    @View( VIEW_CREATE_IDEE )
    public String getCreateIdee( HttpServletRequest request )
    {
        _idee = ( _idee != null ) ? _idee : new Idee( );

//        Collection<Campagne> listCampagnes = CampagneHome.getCampagnesList( );
        String lastCampagneCode = CampagneHome.getLastCampagne().getCode();
        List<CampagneTheme> listCampagneThemes = (List<CampagneTheme>) CampagneThemeHome.getCampagneThemesListByCampagne( lastCampagneCode ) ;

        Map<String, Object> model = getModel( );
        model.put( MARK_IDEE, _idee );
        model.put( MARK_LANGUAGE, getLocale( ) );
//        model.put( MARK_LIST_CAMPAGNES, listCampagnes );
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance( ).getTypeLocalisationList( ) );
        model.put( MARK_CAMPAGNETHEME_LIST, listCampagneThemes );
        model.put( MARK_HANDICAP_LIST, IdeeService.getInstance().getHandicapCodesList() );
        model.put( MARK_ARRONDISSEMENTS_LIST, IdeeService.getInstance( ).getArrondissements( ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_IDEE, TEMPLATE_CREATE_IDEE, model );
    }

    @Action( ACTION_CREATE_IDEE )
    public String doCreateIdee( HttpServletRequest request )
    {

        populate( _idee, request );

//        String strCodesIdees = request.getParameter( PARAMETER_LIST_CODE_IDEES ) == null ? StringUtils.EMPTY
//                : request.getParameter( PARAMETER_LIST_CODE_IDEES );

        // Check constraints
        if ( !validateBean( _idee, VALIDATION_ATTRIBUTES_PREFIX ) || !isIdeeAddressValid( request ) /* || !checkCodesIdees( strCodesIdees, request.getLocale( ) */ )
        {
            return redirectView( request, VIEW_CREATE_IDEE );
        }
        
        if ( Idee.LOCALISATION_TYPE_PARIS.equals(_idee.getLocalisationType()) && StringUtils.isEmpty( _idee.getGeoJson() ) )
        {
        	_idee.setLocalisationArdt( null );
        }
        
        _idee.setCodeIdee          ( 0 );
        _idee.setCodeCampagne      ( CampagneHome.getLastCampagne().getCode() );
        _idee.setDepositaireType   ( AppPropertiesService.getProperty( Constants.PROPERTY_GENERATE_IDEE_DEPOSITAIRE_TYPE ) );
        _idee.setDepositaire       ( AppPropertiesService.getProperty( Constants.PROPERTY_GENERATE_IDEE_DEPOSITAIRE ) );
        _idee.setLuteceUserName    ( AppPropertiesService.getProperty( Constants.PROPERTY_GENERATE_IDEE_LUTECE_USER_NAME )  );
        _idee.setCreationTimestamp ( new java.sql.Timestamp( ( new java.util.Date( ) ).getTime( ) ) );
        _idee.setStatusPublic      ( Idee.Status.STATUS_DEPOSE );
        _idee.setTypeQpvQva        ( IdeationApp.QPV_QVA_NO );
        _idee.setDocs              ( new ArrayList<File>( ) );
        _idee.setImgs              ( new ArrayList<File>( ) );
        _idee.setDejadepose        ("");
        _idee.setCreationmethod    ("");
        _idee.setOperatingbudget   ("");

        /* Identifying QPV from address if GeoJSON available*/
        if ( StringUtils.isNotEmpty( _idee.getGeoJson() ) ) 
        {
	        List <QpvQva> listQpvqva;
	        try {
	            listQpvqva = QpvQvaService.getQpvQva( _idee.getLongitude(), _idee.getLatitude() );
	            if (listQpvqva.size() == 0) {
	            	_idee.setTypeQpvQva(IdeationApp.QPV_QVA_NO);
	            	_idee.setIdQpvQva(null);
	            	_idee.setLibelleQpvQva(null);
	            } else {
	                //For backwards compatibility, choose a QPV/QVA in priority if it exists
	                QpvQva resQpvQva = null;
	                for (QpvQva qpvqva: listQpvqva) {
	                    if (StringUtils.isNotBlank(qpvqva.getType())) {
	                        resQpvQva = qpvqva;
	                        break;
	                    }
	                }
	                //If not qpvqva, just take the first one...
	                if (resQpvQva == null) {
	                    resQpvQva = listQpvqva.get(0);
	                }
	
	                if (StringUtils.isNotBlank(resQpvQva.getType())) {
	                	_idee.setTypeQpvQva(resQpvQva.getType());
	                	_idee.setIdQpvQva(resQpvQva.getId());
	                	_idee.setLibelleQpvQva(resQpvQva.getLibelle());
	                } else if (StringUtils.isNotBlank(resQpvQva.getGpruNom())) {
	                	_idee.setTypeQpvQva(IdeationApp.QPV_QVA_GPRU);
	                	_idee.setIdQpvQva(resQpvQva.getFid());
	                	_idee.setLibelleQpvQva(resQpvQva.getGpruNom());
	                } else if (StringUtils.isNotBlank(resQpvQva.getExtBp())) {
	                	_idee.setTypeQpvQva(IdeationApp.QPV_QVA_QBP);
	                	_idee.setIdQpvQva(resQpvQva.getFid());
	                	_idee.setLibelleQpvQva(resQpvQva.getExtBp());
	                } else {
	                	_idee.setTypeQpvQva(resQpvQva.getType());
	                	_idee.setIdQpvQva(resQpvQva.getId());
	                	_idee.setLibelleQpvQva(resQpvQva.getLibelle());
	                }
	            }
	        } catch (Exception e) {
	        	_idee.setTypeQpvQva(IdeationApp.QPV_QVA_ERR);
	        	_idee.setIdQpvQva(null);
	        	_idee.setLibelleQpvQva(null);
	            AppLogService.error("IDEATION : error when trying to identify QPV from address", e);
	        }
    	}
        else
        {
        	_idee.setAdress( null );
        }

        try 
        {
            IdeeService.getInstance().createIdee( _idee );
        } catch (IdeationErrorException e) {
            AppLogService.error("IDEATION : error when trying to create idee into DB from BO", e);
        }

        _idee = null;
        addInfo( INFO_IDEE_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_IDEES );
    }

    // ***********************************************************************************
    // * MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MO *
    // * MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MO *
    // ***********************************************************************************

    /**
     * Returns the form to update info about a idee
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_IDEE )
    public String getModifyIdee( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_IDEE ) );

        //Always reload idee because the daemon may modify it.
        //We store the user input that needs to be saved in _ideeBoForm
        _idee = IdeeHome.findByPrimaryKey( nId );

        if ( _ideeBoForm == null || _ideeBoForm.getId() != nId )
        {
            _ideeBoForm = new IdeeBoForm();
        }

        Map<String, Object> model = getModel(  );
        model.put( MARK_IDEE, _idee );
        model.put( MARK_IDEE_BO_FORM, _ideeBoForm );

        if ( _idee.getCodeCampagne() != null ) {
            IdeationStaticService.getInstance(  ).fillCampagneStaticContent( model, _idee.getCodeCampagne() );
        } else {
            IdeationStaticService.getInstance(  ).fillAllStaticContent( model );
        }

        if (WorkflowService.getInstance(  ).isAvailable()) {
            int idWorkflow = AppPropertiesService.getPropertyInt( Constants.PROPERTY_WORKFLOW_ID, -1 );
            model.put( MARK_RESOURCE_HISTORY, WorkflowService.getInstance(  )
                           .getDisplayDocumentHistory( _idee.getId(), Idee.WORKFLOW_RESOURCE_TYPE, idWorkflow, request,
                getLocale(  ) ) );
        }

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_IDEE, TEMPLATE_MODIFY_IDEE, model );
    }

    /**
     * Process the change form of a idee
     *
     * @param request The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_IDEE )
    public String doModifyIdee( HttpServletRequest request )
    {
        populate( _ideeBoForm, request );

        // Check constraints
        if ( !validateBean( _ideeBoForm, VALIDATION_ATTRIBUTES_PREFIX )   )
        {
            return redirect( request, VIEW_MODIFY_IDEE, PARAMETER_ID_IDEE, _idee.getId( ) );
        }

        copyIdeeBoFormToIdee( _ideeBoForm, _idee );
        IdeeHome.updateBO( _idee );
        addInfo( INFO_IDEE_UPDATED, getLocale(  ) );

        try {
            //There is a race condition here because this
            //idee was loaded in the view, the daemon may have changed it
            //A workaround is to save it again if it is wrongly indexed.
            //It only affects this because the rest of the use of _idee doesn't use
            //fields that are changed by the daemon, but the indexing does.
            _solrIdeeIndexer.writeIdee( _idee );
        } catch (Exception e) {
            addInfo( "Erreur d'écriture dans Solr, mais l'idée est correctement modifiée." );
            AppLogService.error ("Ideation, error writing idee to solr", e);
        }

        return redirectView( request, VIEW_MANAGE_IDEES );
    }
    
    // ***********************************************************************************
    // * REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE RE *
    // * REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE RE *
    // ***********************************************************************************

    /**
     * 
     * @param request the HTTP request
     * @return the url view manage idees
     * @throws AccessDeniedException  the access denied exception
     */
    public String doRemoveIdee( HttpServletRequest request )
            throws AccessDeniedException
        {
    		int nIdIdee = Integer.parseInt( request.getParameter( PARAMETER_ID_IDEE ) );

            Idee idee= IdeeHome.findByPrimaryKey(nIdIdee);
            IdeeService.getInstance().removeIdeeByMdp(idee);
         
            addInfo( INFO_IDEE_REMOVED, getLocale( ) );
          
            return  ( AppPathService.getBaseUrl( request ) + JSP_MANAGE_IDEES ) ;
                   
        }
    
    
    /**
     * Returns the form to put the explanation for the deletion
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_CONFIRM_REMOVE_IDEE )
    public String getConfirmRemoveIdeePage( HttpServletRequest request )
    {
        int nIdIdee = Integer.parseInt( request.getParameter( PARAMETER_ID_IDEE ) );
        
        if ( ( _idee == null ) || ( _idee.getId(  ) != nIdIdee ) )
        {
            _idee = IdeeHome.findByPrimaryKey( nIdIdee );
        }

        if ( _idee.getStatusIsRemoved(  ) )
        {
            UrlItem url = new UrlItem( JSP_MANAGE_IDEES );
            Map<String, Object> requestParameters = new HashMap<String, Object>(  );
            requestParameters.put( PARAMETER_PLUGIN_NAME, "participatoryideation" );

            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_IDEE_REMOVED,
                    JSP_MANAGE_IDEES, AdminMessage.TYPE_ERROR, requestParameters );
            return redirect( request, strMessageUrl );
        }
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_IDEE, _idee );

        return getPage( PROPERTY_PAGE_TITLE_CONFIRM_REMOVE_IDEE, TEMPLATE_CONFIRM_REMOVE_IDEE, model );
    }
    /**
     * 
     * @param request the HTTP request
     * @return the admin message to confirm removing idee
     * @throws AccessDeniedException  the access denied exception
     */
    @Action( ACTION_CONFIRM_REMOVE_IDEE )
    public String getConfirmRemoveIdee( HttpServletRequest request )
            throws AccessDeniedException
    {
        int nIdIdee = Integer.parseInt( request.getParameter( PARAMETER_ID_IDEE ) );
        
        if ( ( _idee == null ) || ( _idee.getId(  ) != nIdIdee ) )
        {
            _idee = IdeeHome.findByPrimaryKey( nIdIdee );
        }

        if ( _idee.getStatusIsRemoved(  ) )
        {
            Map<String, Object> requestParameters = new HashMap<String, Object>(  );
            requestParameters.put( PARAMETER_PLUGIN_NAME, "participatoryideation" );

            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_IDEE_REMOVED,
                    JSP_MANAGE_IDEES, AdminMessage.TYPE_ERROR, requestParameters );
            return redirect( request, strMessageUrl );
        }

        String strMotifRecev = request.getParameter( PARAMETER_MOTIFRECEV ) ;

        _idee.setMotifRecev( strMotifRecev );
        IdeeHome.updateBO( _idee );
        addInfo( INFO_IDEE_UPDATED, getLocale(  ) );

        return redirect( request, doRemoveIdee( request ) );
    }

    // ***********************************************************************************
    // * EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS E *
    // * EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS E *
    // ***********************************************************************************

    /**
     * Export the values from core_user_preferences into csv file
     * 
     * @param request The Http request
     * @param response The Http response
     */
    public void doExportCsvUsers( HttpServletRequest request, HttpServletResponse response )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_IDEE ) );
        
        if ( _idee == null || ( _idee.getId(  ) != nId ))
        {
            _idee = IdeeHome.findByPrimaryKey( nId );
        }
        
        List<Integer> subIds = (List<Integer>) IdeeHome.getSubIdeesId( nId, IdeeHome.GetSubIdeesMethod.ALL_FAMILY );
        
        List<ArrayList<String>> valuesList = IdeeUsersService.getExportInfosList( subIds );
        try
        {
            //Generate CSV file
            String strFormatExtension = AppPropertiesService.getProperty( PROPERTY_CSV_EXTENSION );
            String strFileName = "idee" + nId + "_" + AppPropertiesService.getProperty( PROPERTY_CSV_FILE_NAME ) + "." +
                strFormatExtension;
            IdeeExportUtils.addHeaderResponse( request, response, strFileName, strFormatExtension );

            OutputStream os = response.getOutputStream(  );

            //say how to decode the csv file, with utf8
            byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }; // BOM values
            os.write( bom ); // adds BOM 
            CsvUtils.writeCsv( CsvUtils.IDEEUSERS_PREFIX_CSV, valuesList, os, getLocale(  ) );

            os.flush(  );
            os.close(  );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }
    
    // ***********************************************************************************
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
    // ***********************************************************************************

    private void copyIdeeBoFormToIdee( IdeeBoForm ideeBoForm, Idee idee ) {

        if (StringUtils.isNotEmpty( ideeBoForm.getTitre() ) ) 
        {
            idee.setTitre( ideeBoForm.getTitre( ) );
        } 
        else 
        {
        	idee.setTitre( null );
        }
    	
        if (StringUtils.isNotEmpty( ideeBoForm.getDescription() ) ) 
        {
            idee.setDescription( ideeBoForm.getDescription( ) );
        } 
        else 
        {
        	idee.setDescription( null );
        }
    	
        if (StringUtils.isNotEmpty( ideeBoForm.getCout() ) ) 
        {
            idee.setCout( Long.parseLong(ideeBoForm.getCout( )) );
        } 
        else 
        {
        	idee.setCout( null );
        }
    	
       	// Trying to determine LocalisationType using LocalisationArdt.
       	idee.setLocalisationType( StringUtils.isNotEmpty( ideeBoForm.getLocalisationArdt() ) ? Idee.LOCALISATION_TYPE_ARDT : Idee.LOCALISATION_TYPE_PARIS );
    	
        if (StringUtils.isNotEmpty( ideeBoForm.getLocalisationArdt() ) ) 
        {
            idee.setLocalisationArdt( ideeBoForm.getLocalisationArdt( ) );
        } 
        else 
        {
        	idee.setLocalisationArdt( null );
        } 	
        
        idee.setTypeQpvQva( ideeBoForm.getTypeQpvQva() );
        if ( IdeationApp.QPV_QVA_QPV.equals(ideeBoForm.getTypeQpvQva())
          || IdeationApp.QPV_QVA_QVA.equals(ideeBoForm.getTypeQpvQva())
          ||  IdeationApp.QPV_QVA_GPRU.equals(ideeBoForm.getTypeQpvQva())
          ||  IdeationApp.QPV_QVA_QBP.equals(ideeBoForm.getTypeQpvQva())
        ) { 
            idee.setIdQpvQva(ideeBoForm.getIdQpvQva());
            idee.setLibelleQpvQva(ideeBoForm.getLibelleQpvQva());
        } else {
            idee.setIdQpvQva(null);
            idee.setLibelleQpvQva(null);
        }
        
        idee.setHandicap( ideeBoForm.getHandicap() );

        if (StringUtils.isNotEmpty(ideeBoForm.getIdProjet( ) ) ) 
        {
        	idee.setIdProjet( ideeBoForm.getIdProjet( ) );
        } 
        else 
        {
        	idee.setIdProjet( null );
        }
        
        if (StringUtils.isNotEmpty(ideeBoForm.getTitreProjet( ) ) ) 
        {
        	idee.setTitreProjet( ideeBoForm.getTitreProjet( ) );
        } 
        else 
        {
        	idee.setTitreProjet( null );
        }
        
        if (StringUtils.isNotEmpty( ideeBoForm.getUrlProjet( ) ) ) 
        {
            idee.setUrlProjet( ideeBoForm.getUrlProjet( ) );
        } 
        else 
        {
        	idee.setUrlProjet( null );
        }
        
        if (StringUtils.isNotEmpty( ideeBoForm.getWinnerProjet( ) ) ) 
        {
            idee.setWinnerProjet( ideeBoForm.getWinnerProjet() );
        } 
        else 
        {
        	idee.setWinnerProjet( null );
        }
    }

    private boolean isIdeeAddressValid( HttpServletRequest request )
    {
        boolean bIsValid = true;

        if ( _idee.getLocalisationType( ).equals( Idee.LOCALISATION_TYPE_ARDT ) && StringUtils.isEmpty( _idee.getLocalisationArdt( ) ) )
        {
            addError( MESSAGE_ERROR_ARRONDISSEMENT_EMPTY, request.getLocale( ) );
            bIsValid = false;
        }
        
        if ( StringUtils.isNotBlank( _idee.getAdress( ) ) )
        {
            if ( StringUtils.isEmpty( _idee.getLocalisationType( ) ) )
            {
                addError( MESSAGE_ERROR_ADDRESS_LOCALISATION_TYPE_EMPTY, request.getLocale( ) );
                bIsValid = false;
            }
//            if ( StringUtils.isEmpty( _idee.getGeoJson( ) ) )
//            {
//                addError( MESSAGE_ERROR_ADDRESS_NOT_VALID + " - Unable to identity GeoJSON for address '" + _idee.getAdress( ) + "'", request.getLocale( ) );
//                bIsValid = false;
//            }
        }
        else
        {
        	_idee.setLatitude( null );
        	_idee.setLongitude( null );
        }

        if ( StringUtils.isNotEmpty( _idee.getGeoJson( ) ) )
        {
            GeolocItem geolocItem = null;

            try
            {
                geolocItem = GeolocItem.fromJSON( _idee.getGeoJson( ) );
                _idee.setAdress( geolocItem.getAddress( ) );
                _idee.setLatitude( geolocItem.getLat( ) );
                _idee.setLongitude( geolocItem.getLon( ) );
                Matcher m = _patternAdresseArrondissement.matcher( _idee.getAdress( ) );
                if ( m.find( ) ) {
	                int nArdt = Integer.parseInt( m.group( 1 ) );
	                String strArdt = IdeeService.getInstance( ).getArrondissementCode( nArdt );
	                if ( _idee.getLocalisationType( ).equals( Idee.LOCALISATION_TYPE_ARDT )
	                        && StringUtils.isNotEmpty( _idee.getLocalisationArdt( ) )
	                        && ( !strArdt.equals( _idee.getLocalisationArdt( ) ) ) )
	                {
	                    addError( MESSAGE_ERROR_ADDRESS_ARDT_MISMATCH, request.getLocale( ) );
	                    bIsValid = false;
	                }
	                else
	                {
	                	_idee.setLocalisationArdt( strArdt );
	                }
                }
            }
            catch ( Exception e )
            {
                addError( MESSAGE_ERROR_ADDRESS_FORMAT, getLocale( ) );
                AppLogService.error( "IdeeJspBean: malformed data from client: address = " + _idee.getGeoJson( )
                        + "; exception " + e );
                bIsValid = false;
            }
        }
        return bIsValid;
    }

}
