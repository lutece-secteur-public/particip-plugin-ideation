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
 *   AtelierCommentListener  and the following disclaimer in the documentation and/or other materials
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

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DateTimeConverter;
import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.campagnebp.business.Campagne;
import fr.paris.lutece.plugins.campagnebp.business.CampagneHome;
import fr.paris.lutece.plugins.campagnebp.business.CampagneTheme;
import fr.paris.lutece.plugins.campagnebp.business.CampagneThemeHome;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierForm;
import fr.paris.lutece.plugins.ideation.business.AtelierFormEntry;
import fr.paris.lutece.plugins.ideation.business.AtelierFormEntryHome;
import fr.paris.lutece.plugins.ideation.business.AtelierFormHome;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResult;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultHome;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.AtelierSearcher;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.service.AtelierService;
import fr.paris.lutece.plugins.ideation.service.AtelierStaticService;
import fr.paris.lutece.plugins.ideation.service.AtelierWSService;
import fr.paris.lutece.plugins.ideation.service.IdeationErrorException;
import fr.paris.lutece.plugins.ideation.service.IdeeService;
import fr.paris.lutece.plugins.ideation.service.IdeeUsersService;
import fr.paris.lutece.plugins.ideation.service.IdeeWSService;
import fr.paris.lutece.plugins.ideation.utils.CsvUtils;
import fr.paris.lutece.plugins.ideation.utils.IdeeExportUtils;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.admin.annotations.Controller;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.util.url.UrlItem;


/**
 * This class provides the user interface to manage Atelier features ( manage,
 * create, modify, remove )
 */
@Controller( controllerJsp = "ManageAteliers.jsp", controllerPath = "jsp/admin/plugins/ideation/", right = "IDEATION_MANAGEMENT_ATELIER" )
public class AtelierJspBean extends ManageAtelierJspBean
{

    ////////////////////////////////////////////////////////////////////////////
    // Constants

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // templates
    private static final String TEMPLATE_MANAGE_ATELIERS = "/admin/plugins/ideation/manage_ateliers.html";
    private static final String TEMPLATE_CREATE_ATELIER = "/admin/plugins/ideation/create_atelier.html";
    private static final String TEMPLATE_MODIFY_ATELIER = "/admin/plugins/ideation/modify_atelier.html";
    private static final String TEMPLATE_MODIFY_ATELIER_FORM = "/admin/plugins/ideation/modify_atelierform.html";

    private static final String TEMPLATE_CREATE_ATELIER_FORM = "/admin/plugins/ideation/create_atelierform.html";
    private static final String TEMPLATE_RESULTS_VOTES = "/admin/plugins/ideation/results_votes.html";
    private static final String TEMPLATE_VIEW_ATELIER_HISTORY = "/admin/plugins/ideation/view_atelier_history.html";
    // Parameters
    private static final String PARAMETER_ID_ATELIER = "id";
    private static final String PARAMETER_ID_ATELIER_FORM = "id_atelierForm";
    private static final String PARAMETER_ID_ATELIER_FORM_ENTRY_COMP = "idAtelierFormEntryComp";
    private static final String PARAMETER_LIST_CODE_IDEES = "list_code_idee";
    private static final String PARAMETER_TITRE_PHASE3 = "titre_phase3";
    private static final String PARAMETER_TEXTE_PHASE3 = "texte_phase3";
    private static final String PARAMETER_INTEGERS_REGEX = "\\D+";

    private static final String PARAMETER_TITRE_ALTERNATIVE1 = "titre_alternative1";
    private static final String PARAMETER_TITRE_ALTERNATIVE2 = "titre_alternative2";
    private static final String PARAMETER_TITRE_ALTERNATIVE3 = "titre_alternative3";
    private static final String PARAMETER_DESC_ALTERNATIVE1 = "description_alternative1";
    private static final String PARAMETER_DESC_ALTERNATIVE2 = "description_alternative2";
    private static final String PARAMETER_DESC_ALTERNATIVE3 = "description_alternative3";
    private static final String PARAMETER_DESC_COMP_ALT1 = "descComp_alt1";
    private static final String PARAMETER_DESC_COMP_ALT2 = "descComp_alt2";
    private static final String PARAMETER_DESC_COMP_ALT3 = "descComp_alt3";
    private static final String PARAMETER_DESC_COMP_VISIBLE = "desc_comp_isVisible";

    private static final String PARAMETER_FILTER_CODE_CAMPAGNE = "filter_code_campagne";
    private static final String PARAMETER_FILTER_CODE_THEME = "filter_code_theme";
    private static final String PARAMETER_FILTER_TITRE_OU_DESCRIPTION = "filter_titre_ou_description";
    private static final String PARAMETER_FILTER_TYPE_LOCALISATION = "filter_type_localisation";
    private static final String PARAMETER_FILTER_ARRANDISSEMENT = "filter_arrandissement";
    private static final String PARAMETER_FILTER_TYPE = "filter_type";
    private static final String PARAMETER_FILTER_HANDICAP = "filter_handicap";
    private static final String PARAMETER_SORT_COLUMN = "sort_column";
    private static final String PARAMETER_SORT_ORDER = "sort_order";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_ATELIERS = "ideation.manage_ateliers.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ATELIER = "ideation.modify_atelier.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_ATELIER_FORM = "ideation.modify_atelier_form.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ATELIER = "ideation.create_atelier.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_ATELIER_FORM = "ideation.create_atelier_form.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_RESULTS_VOTES = "ideation.manage_ateliers.labelPageResultsvotes";
    private static final String PROPERTY_PHASE_NON_DEMARRE = "ideation.manage_ateliers.phaseNonDemarre";
    private static final String PROPERTY_PHASE_PHASE1 = "ideation.manage_ateliers.phasePhase1";
    private static final String PROPERTY_PHASE_PHASE2 = "ideation.manage_ateliers.phasePhase2";
    private static final String PROPERTY_PHASE_PHASE3 = "ideation.manage_ateliers.phasePhase3";
    private static final String PROPERTY_PHASE_ACHEVE = "ideation.manage_ateliers.phaseAcheve";
    private static final String PROPERTY_NB_JOURS_ALERT = "ideation.atelierAlerteFinPhases";
    private static final String PROPERTY_SEPERATION_CODE_IDEES = "ideation.creation_atelier_separation_codes_idees";
    private static final String PROPERTY_PAGE_TITLE_VIEW_ATELIER_HISTORY = "ideation.view_history_atelier.pageTitle";

    // Markers
    private static final String MARK_ATELIER_LIST = "atelier_list";
    private static final String MARK_ATELIER = "atelier";
    private static final String MARK_ID_ATELIER = "id_atelier";
    private static final String MARK_ATELIER_FORM = "atelier_form";
    private static final String MARK_ATELIER_FORM_DB = "atelier_form_db";
    private static final String MARK_CHOIX_TITRE = "choix_titre";
    private static final String MARK_CHOIX_DESC = "choix_desc";
    private static final String MARK_CHOIX_DESC_COMP = "choix_desc_comp";
    private static final String MARK_LIST_DESC_COMP = "list_desc_comp";
    private static final String MARK_FORMAT_DATE = "dd/MM/yyyy";
    private static final String MARK_LANGUAGE = "language";
    private static final String MARK_LIST_CAMPAGNES = "listCampagnes";
    private static final String MARK_LOCALISATION_TYPE_LIST = "type_localisation_list";
    private static final String MARK_CAMPAGNETHEME_LIST = "campagnetheme_list";
    private static final String MARK_HANDICAP_LIST = "handicap_list";
    private static final String MARK_ARRONDISSEMENTS_LIST = "arrondissements_list";
    private static final String MARK_LIST_PHASES = "listPhases";
    private static final String MARK_LIST_NB_COMMENTAIRES = "listNbCommentaires";
    private static final String MARK_LIST_NB_VOTES = "listNbVotes";
    private static final String MARK_LIST_LAST_ACTIONS = "listLastActions";
    private static final String MARK_LIST_ALERTS = "listAlerts";
    private static final String MARK_JOURS_ALERTE = "j_alert";
    private static final String MARK_LIST_HAS_ATELIER_FORM = "listHasAtelierForm";
    private static final String MARK_ORDER_BY_DATE_CREATION = "date_creation";
    private static final String MARK_LIST_RESULT_VOTES_TITRE = "list_results_votes_titre";
    private static final String MARK_LIST_RESULT_VOTES_DESC = "list_results_votes_description";
    private static final String MARK_LIST_RESULT_VOTES_DESC_COMP = "list_results_votes_descComp";
    private static final String MARK_MAX_VOTES_TITRE = "maxVotesTitre";
    private static final String MARK_MAX_VOTES_DESC = "maxVotesDesc";
    private static final String MARK_LIST_MAX_VOTES_COMP = "listmaxVotesComp";
    private static final String MARK_FILTER_CODE_CAMPAGNE = "filter_code_campagne";
    private static final String MARK_FILTER_CODE_THEME = "filter_code_theme";

    private static final String MARK_FILTER_TYPE_LOCALISATION = "filter_type_localisation";
    private static final String MARK_FILTER_ARRANDISSEMENT = "filter_arrandissement";
    private static final String MARK_FILTER_TYPE = "filter_type";
    private static final String MARK_FILTER_HANDICAP = "filter_handicap";
    private static final String MARK_SORT_COLUMN = "sort_column";
    private static final String MARK_SORT_ORDER = "sort_order";
    private static final String MARK_ATELIER_HISTORY = "atelier_history";
    private static final String MARK_CAN_GENERATE_IDEE = "canGenerateIdee";
    private static final String MARK_GENERATED_IDEE_ID = "generatedIdeeId";

    // JSP
    private static final String JSP_MANAGE_ATELIERS = "jsp/admin/plugins/ideation/ManageAteliers.jsp";

    // Properties
    private static final String MESSAGE_CONFIRM_REMOVE_ATELIER = "ideation.message.confirmRemoveAtelier";
    private static final String MESSAGE_ERROR_ATELIER_REMOVED = "ideation.message.error.atelier.removed";
    private static final String PROPERTY_CSV_EXTENSION = "ideation.csv.extension";
    private static final String PROPERTY_CSV_FILE_NAME = "ideation.csv.file.name";
    private static final String VALIDATION_ATTRIBUTES_PREFIX = "ideation.model.entity.atelier.attribute.";
    private static final String MESSAGE_ERROR_DATES_STEP1 = "ideation.validation.atelier.DateFinPhase1AfterDateDebutPhase1";
    private static final String MESSAGE_ERROR_DATES_STEP2 = "ideation.validation.atelier.DateFinPhase2AfterDateDebutPhase2";
    private static final String MESSAGE_ERROR_DATES_STEP3 = "ideation.validation.atelier.DateFinPhase3AfterDateDebutPhase3";
    private static final String MESSAGE_ERROR_DATES_START_STEP2_AFTER_END_STEP1 = "ideation.validation.atelier.DateDebutPhase2AfterDateFinPhase1";
    private static final String MESSAGE_ERROR_DATES_START_STEP3_AFTER_END_STEP2 = "ideation.validation.atelier.DateDebutPhase3AfterDateFinPhase2";
    private static final String MESSAGE_ERROR_VALIDATION_CODES_IDEES = "ideation.validation.atelier.ListCodeIdee.notValid";
    private static final String MESSAGE_ERROR_VALIDATION_CODE_NOT_EXIST = "ideation.validation.atelier.ListCodeIdee.notExist";
    private static final String MESSAGE_ERROR_VALIDATION_CODE_EXIST = "ideation.validation.atelier.ListCodeIdee.Exist";
    private static final String MESSAGE_ERROR_TITRE_ALTERATIVE1_NOT_EMPTY = "ideation.validation.atelierformentry.TitreAlternative1.notEmpty";
    private static final String MESSAGE_ERROR_DESC_ALTERATIVE1_NOT_EMPTY = "ideation.validation.atelierformentry.DescriptionAlternative1.notEmpty";
    private static final String MESSAGE_ERROR_DESC_COMP1_NOT_EMPTY = "ideation.validation.atelierformentry.DescriptionComp1.notEmpty";
    private static final String MESSAGE_ERROR_TITRE_PHASE3_NOT_EMPTY = "ideation.validation.atelier.titrePhase3.notEmpty";
    private static final String MESSAGE_ERROR_TEXTE_PHASE3_NOT_EMPTY = "ideation.validation.atelier.textePhase3.notEmpty";
    private static final String MESSAGE_ERROR_COUT_NOT_EMPTY = "ideation.validation.atelier.Cout.notEmpty";

    private static final String MESSAGE_ERROR_ARRONDISSEMENT_EMPTY = "ideation.validation.atelier.Arrondissement.notEmpty";
    private static final String MESSAGE_ERROR_ADDRESS_FORMAT = "ideation.validation.atelier.Address.Format";
    private static final String MESSAGE_ERROR_ADDRESS_NOT_VALID = "ideation.validation.atelier.Address.NotValid";
    private static final String MESSAGE_ERROR_ADDRESS_ARDT_MISMATCH = "ideation.validation.atelier.Address.ArdtMismatch";
    private static final String MESSAGE_ERROR_ADDRESS_LOCALISATION_TYPE_EMPTY = "ideation.validation.atelier.LocalisationType.NotEmpty";

    // Views
    private static final String VIEW_MANAGE_ATELIERS = "manageAteliers";
    private static final String VIEW_CREATE_ATELIER = "createAtelier";
    private static final String VIEW_MODIFY_ATELIER = "modifyAtelier";
    private static final String VIEW_CREATE_ATELIER_FORM = "createAtelierForm";
    private static final String VIEW_MODIFY_ATELIER_FORM = "modifyAtelierForm";
    private static final String VIEW_RESULTS_VOTES = "resultsVotes";
    private static final String VIEW_ATELIER_HISTORY = "viewAtelierHistory";  

    // Actions
    private static final String ACTION_CREATE_ATELIER = "createAtelier";
    private static final String ACTION_MODIFY_ATELIER = "modifyAtelier";
    private static final String ACTION_REMOVE_ATELIER = "removeAtelier";
    private static final String ACTION_VALIDPHASE3 = "validPhase3";
    private static final String ACTION_REMOVE_ATELIER_FORM_ENTRY_COMP = "removeAtelierFormEntryComp";
    private static final String ACTION_CONFIRM_REMOVE_ATELIER = "confirmRemoveAtelier";
    private static final String ACTION_CREATE_ATELIER_FORM = "createAtelierForm";
    private static final String ACTION_MODIFY_ATELIER_FORM = "modifyAtelierForm";
    private static final String ACTION_SEARCH_ATELIER = "searchAtelier";
    private static final String ACTION_CANCEL_SEARCH = "cancelSearch";
    private static final String ACTION_GENERATE_IDEE = "generateIdee";

    // Infos
    private static final String INFO_ATELIER_CREATED = "ideation.info.atelier.created";
    private static final String INFO_ATELIER_UPDATED = "ideation.info.atelier.updated";
    private static final String INFO_ATELIER_REMOVED = "ideation.info.atelier.removed";
    private static final String INFO_ATELIER_FORM_CREATED = "ideation.info.atelier_form.created";
    private static final String INFO_ATELIER_FORM_ENTRY_UPDATED = "ideation.info.atelier_form_entry.updated";
    private static final String INFO_ATELIER_FORM_ENTRY_REMOVED = "ideation.info.atelier_form_entry.removed";
    private static final String INFO_ATELIER_IDEE_GENERATED = "ideation.info.atelier.ideeGenerated";

    private static final java.util.regex.Pattern _patternAdresseArrondissement = java.util.regex.Pattern
            .compile( ", 75[0-1]([0-2][0-9]) PARIS" );

    // Session variable to store working values
    private Atelier _atelier;
    private AtelierForm _atelierForm;
    private AtelierSearcher _atelierSearcher;
    private static AtelierSearcher defaultSearcher;

    static
    {
        defaultSearcher = new AtelierSearcher( );
        defaultSearcher.setOrderAscDesc( AtelierSearcher.ORDER_DESC );
        defaultSearcher.setOrderColumn( AtelierSearcher.COLUMN_ID );
    }

    // beans
    private ICommentService _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );

    
    private IResourceExtenderHistoryService _resourceExtenderHistoryService = SpringContextService
            .getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_ATELIERS, defaultView = true )
    public String getManageAteliers( HttpServletRequest request )
    {
        _atelier = null;
        _atelierForm = null ;
        AtelierSearcher currentSearcher = _atelierSearcher != null ? _atelierSearcher : defaultSearcher;
        List<Atelier> listAteliers = (List<Atelier>) AtelierHome.getAteliersListSearch( currentSearcher );

        Map<String, Object> model = getPaginatedListModel( request, MARK_ATELIER_LIST, listAteliers,
                                                           JSP_MANAGE_ATELIERS );

        Collection<Campagne> listCampagnes = CampagneHome.getCampagnesList( );

        List<CampagneTheme> listCampagneThemes = (List<CampagneTheme>) CampagneThemeHome.getCampagneThemesList( );

        if ( _atelierSearcher != null )
        {
            if ( StringUtils.isNotBlank( _atelierSearcher.getCodeCampagne( ) ) )
            {
                model.put( MARK_FILTER_CODE_CAMPAGNE, _atelierSearcher.getCodeCampagne( ) );
            }

            if ( StringUtils.isNotBlank( _atelierSearcher.getCodeTheme( ) ) )
            {
                model.put( MARK_FILTER_CODE_THEME, _atelierSearcher.getCodeTheme( ) );
            }

            if ( StringUtils.isNotBlank( _atelierSearcher.getTitreOuDescription( ) ) )
            {
                model.put( PARAMETER_FILTER_TITRE_OU_DESCRIPTION, _atelierSearcher.getTitreOuDescription( ) );
            }
            if ( StringUtils.isNotBlank( _atelierSearcher.getTypeLocalisation( ) ) )
            {
                model.put( MARK_FILTER_TYPE_LOCALISATION, _atelierSearcher.getTypeLocalisation( ) );
            }
            if ( StringUtils.isNotBlank( _atelierSearcher.getLocalisationArdt( ) ) )
            {
                model.put( MARK_FILTER_ARRANDISSEMENT, _atelierSearcher.getLocalisationArdt( ) );
            }
            if ( StringUtils.isNotBlank( _atelierSearcher.getType( ) ) )
            {
            	model.put( MARK_FILTER_TYPE, _atelierSearcher.getType( ) );
            }
            if ( StringUtils.isNotBlank( _atelierSearcher.getHandicap() ) )
            {
            	model.put( MARK_FILTER_HANDICAP, _atelierSearcher.getHandicap( ) );
            }

            if ( StringUtils.isNotBlank( _atelierSearcher.getOrderColumn( ) ) )
            {
                model.put( MARK_SORT_COLUMN, _atelierSearcher.getOrderColumn( ) );
            }
            if ( StringUtils.isNotBlank( _atelierSearcher.getOrderAscDesc( ) ) )
            {
                model.put( MARK_SORT_ORDER, _atelierSearcher.getOrderAscDesc( ) );
            }
            
        }

        AtelierStaticService.getInstance( ).fillAllStaticContent( model );

        List<String> listPhases = new ArrayList<String>( listAteliers.size( ) );
        List<Integer> listNbCommentaires = new ArrayList<Integer>( listAteliers.size( ) );
        List<Integer> listNbVotes = new ArrayList<Integer>( listAteliers.size( ) );
        List<Date> listLastActions = new ArrayList<Date>( listAteliers.size( ) );
       
        List<Boolean> listAlert = new ArrayList<Boolean>( listAteliers.size( ) );
        ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
        List<ResourceExtenderHistory> listHistories = null;
        int nAlertJours = Integer.parseInt( AppPropertiesService.getProperty( PROPERTY_NB_JOURS_ALERT ) );
        List<Boolean> listHasAtelierForm = new ArrayList<Boolean>() ;
        AtelierForm atelierFormFromDb = null ;
        Timestamp dLastPostFormResult;
        Collection<AtelierFormResult> listAtelierFormResult;
        int nbVotes;
        for ( Atelier atelier : listAteliers )
        {
        	
        	nbVotes = 0;
        	dLastPostFormResult=null;
            listPhases.add( getPhaseActive( atelier, request ) );
            listNbCommentaires.add( _commentService.getCommentNb( String.valueOf( atelier.getId( ) ),
                                                                  Atelier.PROPERTY_RESOURCE_TYPE, false, true ) );
            
            
            atelierFormFromDb = AtelierFormHome.getAtelierFormByAtelier( atelier.getId( ) ) ;
            if ( atelierFormFromDb == null )
            {
            	listHasAtelierForm.add( false ) ;
            }
            else
            {
            	listHasAtelierForm.add( true ) ;
            	nbVotes = AtelierFormResultHome.getNbVotes(atelierFormFromDb.getId());
            	if(nbVotes>0)
            	{
            		listAtelierFormResult=AtelierFormResultHome.getAtelierFormResultsListByIdForm(atelierFormFromDb.getId());
            		if(listAtelierFormResult.size()>0)
            		{
            			dLastPostFormResult=listAtelierFormResult.iterator().next().getCreationTimestamp();
            		}
            	}
                     
            }
            listNbVotes.add( nbVotes );
            
           
            if(dLastPostFormResult!=null)
        	{
        		listLastActions.add((new Date(dLastPostFormResult.getTime())));
        	}
            else
            {
            	filter.setExtendableResourceType( Atelier.PROPERTY_RESOURCE_TYPE );
	            filter.setIdExtendableResource( String.valueOf( atelier.getId( ) ) );
	            filter.setAscSort( false );
	            filter.setSortedAttributeName( MARK_ORDER_BY_DATE_CREATION );
	            listHistories = _resourceExtenderHistoryService.findByFilter( filter );
	            if ( listHistories.size( ) > 0  )
	            {
	            		listLastActions.add( (Date) ( listHistories.get( 0 ).getDateCreation( ) ) );
	            }
            }
           
            if ( ( differenceDates( atelier.getDateFinPhase1( ) ) <= nAlertJours
                    && differenceDates( atelier.getDateFinPhase1( ) ) > 0 )
                    || ( differenceDates( atelier.getDateFinPhase2( ) ) <= nAlertJours
                            && differenceDates( atelier.getDateFinPhase2( ) ) > 0 ) )
            {
                listAlert.add( true );
            }
            else
            {
                listAlert.add( false );
            }
            
            

        }
        model.put( MARK_LANGUAGE, getLocale( ) );
        model.put( MARK_LIST_CAMPAGNES, listCampagnes );
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance( ).getTypeLocalisationList( ) );
        model.put( MARK_CAMPAGNETHEME_LIST, listCampagneThemes );
        model.put( MARK_HANDICAP_LIST, IdeeService.getInstance( ).getHandicapCodesList() );
        model.put( MARK_ARRONDISSEMENTS_LIST, IdeeService.getInstance( ).getArrondissements( ) );

        model.put( MARK_LIST_PHASES, listPhases );
        model.put( MARK_LIST_NB_COMMENTAIRES, listNbCommentaires );
        model.put( MARK_LIST_NB_VOTES, listNbVotes );
        model.put( MARK_LIST_LAST_ACTIONS, listLastActions );
        model.put( MARK_LIST_ALERTS, listAlert );
        model.put( MARK_LIST_HAS_ATELIER_FORM, listHasAtelierForm );
        model.put( MARK_JOURS_ALERTE, nAlertJours );

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_ATELIERS, TEMPLATE_MANAGE_ATELIERS, model );
    }

    private String getPhaseActive( Atelier atelier, HttpServletRequest request )
    {
        java.util.Date dateToday = new java.util.Date( );

        SimpleDateFormat sdf = new SimpleDateFormat( MARK_FORMAT_DATE );

        try
        {
            dateToday = sdf.parse( sdf.format( dateToday ) );
        }
        catch ( ParseException ex )
        {
            ex.printStackTrace( );
        }

        String strPhaseActive = StringUtils.EMPTY;

        if ( dateToday.before( atelier.getDateDebutPhase1( ) ) )
        {
            strPhaseActive = PROPERTY_PHASE_NON_DEMARRE;
        }
        if ( ( dateToday.before( atelier.getDateFinPhase1( ) ) ) && ( dateToday.after( atelier.getDateDebutPhase1( ) ) )
                || dateToday.equals( atelier.getDateDebutPhase1( ) )
                || dateToday.equals( atelier.getDateFinPhase1( ) ) )
        {
            strPhaseActive = PROPERTY_PHASE_PHASE1;
        }
        if ( ( dateToday.after( atelier.getDateDebutPhase2( ) ) && ( dateToday.before( atelier.getDateFinPhase2( ) ) ) )
                || dateToday.equals( atelier.getDateDebutPhase2( ) )
                || dateToday.equals( atelier.getDateFinPhase2( ) ) )
        {
            strPhaseActive = PROPERTY_PHASE_PHASE2;
        }
        if ( ( dateToday.after( atelier.getDateDebutPhase3( ) ) && ( dateToday.before( atelier.getDateFinPhase3( ) ) ) )
                || dateToday.equals( atelier.getDateDebutPhase3( ) )
                || dateToday.equals( atelier.getDateFinPhase3( ) ) )
        {
            strPhaseActive = PROPERTY_PHASE_PHASE3;
        }
        if ( dateToday.after( atelier.getDateFinPhase3( ) ) )
        {
            strPhaseActive = PROPERTY_PHASE_ACHEVE;
        }
        return I18nService.getLocalizedString( strPhaseActive, request.getLocale( ) );
    }

    private static long differenceDates( Date date )
    {
        java.util.Date dateToday = new java.util.Date( );
        SimpleDateFormat sdf = new SimpleDateFormat( MARK_FORMAT_DATE );
        try
        {
            dateToday = sdf.parse( sdf.format( dateToday ) );
        }
        catch ( ParseException ex )
        {
            ex.printStackTrace( );
        }
        long diff = date.getTime( ) - dateToday.getTime( );

        return ( diff / ( 1000 * 60 * 60 * 24 ) );
    }

    /**
     * Returns the form to create a atelier
     *
     * @param request
     *            The Http request
     * @return the html code of the atelier form
     */
    @View( VIEW_CREATE_ATELIER )
    public String getCreateAtelier( HttpServletRequest request )
    {
        _atelier = ( _atelier != null ) ? _atelier : new Atelier( );

        Collection<Campagne> listCampagnes = CampagneHome.getCampagnesList( );
        List<CampagneTheme> listCampagneThemes = (List<CampagneTheme>) CampagneThemeHome.getCampagneThemesList( );

        Map<String, Object> model = getModel( );
        model.put( MARK_ATELIER, _atelier );
        model.put( MARK_LANGUAGE, getLocale( ) );
        model.put( MARK_LIST_CAMPAGNES, listCampagnes );
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance( ).getTypeLocalisationList( ) );
        model.put( MARK_CAMPAGNETHEME_LIST, listCampagneThemes );
        model.put( MARK_HANDICAP_LIST, IdeeService.getInstance( ).getHandicapCodesList() );
        model.put( MARK_ARRONDISSEMENTS_LIST, IdeeService.getInstance( ).getArrondissements( ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_ATELIER, TEMPLATE_CREATE_ATELIER, model );
    }

    /**
     * Returns the form to create a atelier form
     *
     * @param request
     *            The Http request
     * @return the html code of the atelierForm form
     */
    @View( VIEW_CREATE_ATELIER_FORM )
    public String getCreateAtelierForm( HttpServletRequest request )
    {
        _atelierForm = ( _atelierForm != null ) ? _atelierForm : new AtelierForm( );

        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        if ( _atelier == null || _atelier.getId( ) != nId )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }

        if ( _atelier.getStatusPublic( ) != null &&  _atelier.getStatusPublic( ).isPublished( ) == false )
        {
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATELIER_REMOVED,
                    JSP_MANAGE_ATELIERS, AdminMessage.TYPE_ERROR );
            return redirect( request, strMessageUrl );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_ID_ATELIER, nId );
        model.put( MARK_ATELIER_FORM, _atelierForm );
        model.put( MARK_CHOIX_TITRE, _atelierForm.getChoixTitre( ) );
        model.put( MARK_CHOIX_DESC, _atelierForm.getChoixDescription( ) );
        model.put( MARK_LANGUAGE, getLocale( ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_ATELIER_FORM, TEMPLATE_CREATE_ATELIER_FORM, model );
    }

    /**
     * Process the data capture form of a new atelier
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_ATELIER )
    public String doCreateAtelier( HttpServletRequest request )
    {
        DateTimeConverter dtConverter = new DateConverter( null );
        dtConverter.setPattern( MARK_FORMAT_DATE );
        ConvertUtils.register( dtConverter, Date.class );

        populate( _atelier, request );

        String strCodesIdees = request.getParameter( PARAMETER_LIST_CODE_IDEES ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_LIST_CODE_IDEES );

        // Check constraints
        if ( !validateBean( _atelier, VALIDATION_ATTRIBUTES_PREFIX ) || !checkDates( request.getLocale( ) )
                || !checkCodesIdees( strCodesIdees, request.getLocale( ), _atelier.getId( ) )
                || !isAtelierAddressValid( request ) )
        {
            return redirectView( request, VIEW_CREATE_ATELIER );
        }

        _atelier.setStatusPublic( Atelier.Status.STATUS_EN_CO_CONSTRUCTION );
        
        AtelierHome.create( _atelier );
        _atelier = null;
        addInfo( INFO_ATELIER_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ATELIERS );
    }

    /**
     * Process the data capture form of a new atelier form
     *
     * @param request
     *            The Http Request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_CREATE_ATELIER_FORM )
    public String doCreateAtelierForm( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        if ( _atelier == null || _atelier.getId( ) != nId )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }

        if ( _atelier.getStatusPublic( ) != null &&  _atelier.getStatusPublic( ).isPublished( ) == false )
        {
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATELIER_REMOVED,
                    JSP_MANAGE_ATELIERS, AdminMessage.TYPE_ERROR );
            return redirect( request, strMessageUrl );
        }

        // Check constraints
        if ( !checkAtelierFormEntries( request, request.getLocale( ), nId ) )
        {
            return redirect( request, VIEW_CREATE_ATELIER_FORM, PARAMETER_ID_ATELIER, nId );
        }

        AtelierFormHome.create( _atelierForm );
        _atelierForm = null;
        addInfo( INFO_ATELIER_FORM_CREATED, getLocale( ) );

        return redirect( request, VIEW_MODIFY_ATELIER_FORM, PARAMETER_ID_ATELIER, nId );
    }

    private boolean checkDates( Locale locale )
    {
    	boolean boolValidDates = true ;
    	
    	if ( ! ( _atelier.getDateDebutPhase1( ).before(_atelier.getDateFinPhase1( ) )
    			|| _atelier.getDateDebutPhase1( ).equals(_atelier.getDateFinPhase1( ) ) ) )
        {
            addError( MESSAGE_ERROR_DATES_STEP1, locale );
            boolValidDates = false;
        }
    	if ( ! ( _atelier.getDateDebutPhase2( ).before(_atelier.getDateFinPhase2( ) )
    			|| _atelier.getDateDebutPhase2( ).equals(_atelier.getDateFinPhase2( ) ) ) )
    	{
    		addError( MESSAGE_ERROR_DATES_STEP2 , locale ) ;
    		boolValidDates = false;
    	}
    	if ( ! ( _atelier.getDateDebutPhase3( ).before(_atelier.getDateFinPhase3( ) )
    			|| _atelier.getDateDebutPhase3( ).equals(_atelier.getDateFinPhase3( ) ) ) )
    	{
    		addError( MESSAGE_ERROR_DATES_STEP3 , locale ) ;
    		boolValidDates = false;
    	}
    	if ( ! ( _atelier.getDateFinPhase1( ).before(_atelier.getDateDebutPhase2( ) ) ) )
    	{
    		addError( MESSAGE_ERROR_DATES_START_STEP2_AFTER_END_STEP1 , locale ) ;
    		boolValidDates = false;
    	}
    	if ( ! ( _atelier.getDateFinPhase2( ).before(_atelier.getDateDebutPhase3( ) ) ) )
    	{
    		addError( MESSAGE_ERROR_DATES_START_STEP3_AFTER_END_STEP2 , locale ) ;
    		boolValidDates = false;
    	}
    	return boolValidDates ;
    }

    private boolean checkCodesIdees( String strCodesIdees, Locale locale, int idAtelier )
    {
        boolean boolValidCodesIdees = true;

        String[] listIdees = strCodesIdees.split( AppPropertiesService.getProperty( PROPERTY_SEPERATION_CODE_IDEES ) );
        for ( String strId : listIdees )
        {
            int nCode = 0;
            String[] listIdeeCodeCampagne = strId.split("-");
            String strCodeCampagne = listIdeeCodeCampagne[0].trim();
            try
            {
                //nCode = Integer.parseInt( strId.replaceAll( PARAMETER_INTEGERS_REGEX, StringUtils.EMPTY ) );
            	nCode = Integer.parseInt( listIdeeCodeCampagne[1] );
            }
            catch ( Exception e )
            {
                addError( I18nService.getLocalizedString( MESSAGE_ERROR_VALIDATION_CODES_IDEES, locale ) + " (Liste : " + strCodesIdees + ", Idée concernée : " + strId + ", Erreur : " + e.toString() + ")" );
                boolValidCodesIdees = false;
                break;
            }
            Idee idee = IdeeHome.findByCodes(strCodeCampagne, nCode);
            if ( idee == null )
            {
                addError( I18nService.getLocalizedString( MESSAGE_ERROR_VALIDATION_CODE_NOT_EXIST, locale ) + " (Idée : " + strId + ")" );
                boolValidCodesIdees = false;
                break;
            }
            else
            {
            	Atelier atelier = AtelierHome.getAtelierByIdee( idee.getId() );
                if ( atelier != null && atelier.getId( ) != idAtelier )
                {
                    addError( MESSAGE_ERROR_VALIDATION_CODE_EXIST, locale );
                    boolValidCodesIdees = false;
                    break;
                }
            }
            
        }

        return boolValidCodesIdees;
    }

    private boolean isAtelierAddressValid( HttpServletRequest request )
    {
        boolean bIsValid = true;

        if ( _atelier.getLocalisationType( ).equals( Idee.LOCALISATION_TYPE_ARDT )
                && StringUtils.isEmpty( _atelier.getLocalisationArdt( ) ) )
        {
            addError( MESSAGE_ERROR_ARRONDISSEMENT_EMPTY, request.getLocale( ) );
            bIsValid = false;
        }
        if ( StringUtils.isNotBlank( _atelier.getAddress( ) ) )
        {
            if ( StringUtils.isEmpty( _atelier.getLocalisationType( ) ) )
            {
                addError( MESSAGE_ERROR_ADDRESS_LOCALISATION_TYPE_EMPTY, request.getLocale( ) );
                bIsValid = false;
            }
            if ( StringUtils.isEmpty( _atelier.getGeoJson( ) ) )
            {
                addError( MESSAGE_ERROR_ADDRESS_NOT_VALID, request.getLocale( ) );
                bIsValid = false;
            }
        }
        else
        {
            _atelier.setLatitude( null );
            _atelier.setLongitude( null );
        }

        if ( StringUtils.isNotEmpty( _atelier.getGeoJson( ) ) )
        {
            GeolocItem geolocItem = null;

            try
            {
                geolocItem = GeolocItem.fromJSON( _atelier.getGeoJson( ) );
                _atelier.setAddress( geolocItem.getAddress( ) );
                _atelier.setLatitude( geolocItem.getLat( ) );
                _atelier.setLongitude( geolocItem.getLon( ) );
                Matcher m = _patternAdresseArrondissement.matcher( _atelier.getAddress( ) );
                m.find( );
                int nArdt = Integer.parseInt( m.group( 1 ) );
                String strArdt = IdeeService.getInstance( ).getArrondissementCode( nArdt );
                if ( _atelier.getLocalisationType( ).equals( Idee.LOCALISATION_TYPE_ARDT )
                        && StringUtils.isNotEmpty( _atelier.getLocalisationArdt( ) )
                        && ( !strArdt.equals( _atelier.getLocalisationArdt( ) ) ) )
                {
                    addError( MESSAGE_ERROR_ADDRESS_ARDT_MISMATCH, request.getLocale( ) );
                    bIsValid = false;
                }
                else
                {
                    _atelier.setLocalisationArdt( strArdt );
                }
            }
            catch ( IOException e )
            {
                addError( MESSAGE_ERROR_ADDRESS_FORMAT, getLocale( ) );
                AppLogService.error( "AtelierJspBean: malformed data from client: address = " + _atelier.getGeoJson( )
                        + "; exception " + e );
                bIsValid = false;
            }
        }
        return bIsValid;
    }

    private boolean checkAtelierFormEntries( HttpServletRequest request, Locale locale, int idAtelier )
    {

        String strTitreAlternative1 = request.getParameter( PARAMETER_TITRE_ALTERNATIVE1 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_TITRE_ALTERNATIVE1 );
        String strTitreAlternative2 = request.getParameter( PARAMETER_TITRE_ALTERNATIVE2 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_TITRE_ALTERNATIVE2 );
        String strTitreAlternative3 = request.getParameter( PARAMETER_TITRE_ALTERNATIVE3 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_TITRE_ALTERNATIVE3 );

        String strDescAlternative1 = request.getParameter( PARAMETER_DESC_ALTERNATIVE1 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_ALTERNATIVE1 );
        String strDescAlternative2 = request.getParameter( PARAMETER_DESC_ALTERNATIVE2 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_ALTERNATIVE2 );
        String strDescAlternative3 = request.getParameter( PARAMETER_DESC_ALTERNATIVE3 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_ALTERNATIVE3 );

        String strDescCompAlt1 = request.getParameter( PARAMETER_DESC_COMP_ALT1 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_COMP_ALT1 );
        String strDescCompAlt2 = request.getParameter( PARAMETER_DESC_COMP_ALT2 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_COMP_ALT2 );
        String strDescCompAlt3 = request.getParameter( PARAMETER_DESC_COMP_ALT3 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_COMP_ALT3 );

        String strDescCompVisible = request.getParameter( PARAMETER_DESC_COMP_VISIBLE ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_DESC_COMP_VISIBLE );

        boolean bValid = true;
        AtelierFormEntry atelierFormEntryTitre = new AtelierFormEntry( );
        AtelierFormEntry atelierFormEntryDesc = new AtelierFormEntry( );
        AtelierFormEntry atelierFormEntryComp = new AtelierFormEntry( );

        AtelierForm atelierForm = AtelierFormHome.getAtelierFormByAtelier( idAtelier );
        List<AtelierFormEntry> listAtelierFormEntryCompl = new ArrayList<AtelierFormEntry>( );
        List<AtelierFormEntry> listAtelierFormEntryCompDb = null;
        if ( atelierForm != null )
        {
            listAtelierFormEntryCompDb = AtelierFormEntryHome.getFormEntriesComplByAtelierForm( atelierForm );
            for ( int i = 0; i < listAtelierFormEntryCompDb.size( ); i++ )
            {
                AtelierFormEntry atelierFormEntry = new AtelierFormEntry( );

                String strDescComp1 = request.getParameter( "descComp_" + i + "_alt1" ) == null ? StringUtils.EMPTY
                        : request.getParameter( "descComp_" + i + "_alt1" );
                String strDescComp2 = request.getParameter( "descComp_" + i + "_alt2" ) == null ? StringUtils.EMPTY
                        : request.getParameter( "descComp_" + i + "_alt2" );
                String strDescComp3 = request.getParameter( "descComp_" + i + "_alt3" ) == null ? StringUtils.EMPTY
                        : request.getParameter( "descComp_" + i + "_alt3" );

                if ( StringUtils.isNotEmpty( strDescComp1 ) )
                {
                    atelierFormEntry.setAlternative1( strDescComp1 );
                    atelierFormEntry.setAlternative2( strDescComp2 );
                    atelierFormEntry.setAlternative3( strDescComp3 );
                    atelierFormEntry.setId( listAtelierFormEntryCompDb.get( i ).getId( ) );
                    atelierFormEntry.setIdAtelierForm( atelierForm.getId( ) );
                    listAtelierFormEntryCompl.add( atelierFormEntry );
                }
                else
                {
                    addError( MESSAGE_ERROR_TITRE_ALTERATIVE1_NOT_EMPTY, request.getLocale( ) );
                    bValid = false;
                }
            }
        }

        if ( StringUtils.isNotEmpty( strTitreAlternative1 ) )
        {
            atelierFormEntryTitre.setAlternative1( strTitreAlternative1 );
            atelierFormEntryTitre.setAlternative2( strTitreAlternative2 );
            atelierFormEntryTitre.setAlternative3( strTitreAlternative3 );
        }
        else
        {
            addError( MESSAGE_ERROR_TITRE_ALTERATIVE1_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }

        if ( StringUtils.isNotEmpty( strDescAlternative1 ) )
        {
            atelierFormEntryDesc.setAlternative1( strDescAlternative1 );
            atelierFormEntryDesc.setAlternative2( strDescAlternative2 );
            atelierFormEntryDesc.setAlternative3( strDescAlternative3 );
        }
        else
        {
            addError( MESSAGE_ERROR_DESC_ALTERATIVE1_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }
        if ( StringUtils.isNotEmpty( strDescCompAlt1 ) )
        {
            atelierFormEntryComp.setAlternative1( strDescCompAlt1 );
            atelierFormEntryComp.setAlternative2( strDescCompAlt2 );
            atelierFormEntryComp.setAlternative3( strDescCompAlt3 );
            atelierFormEntryComp.setIdAtelierForm( atelierForm.getId( ) );
            listAtelierFormEntryCompl.add( atelierFormEntryComp );
        }
        else if ( Boolean.parseBoolean( strDescCompVisible ) && StringUtils.isEmpty( strDescCompAlt1 ) )
        {
            addError( MESSAGE_ERROR_DESC_COMP1_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }

        if ( atelierForm != null )
        {
            AtelierFormEntry formEntryTitre = atelierForm.getChoixTitre( );
            AtelierFormEntry formEntryDesc = atelierForm.getChoixDescription( );

            atelierFormEntryTitre.setId( formEntryTitre.getId( ) );
            atelierFormEntryTitre.setIdAtelierForm( atelierForm.getId( ) );
            atelierFormEntryDesc.setId( formEntryDesc.getId( ) );
            atelierFormEntryDesc.setIdAtelierForm( atelierForm.getId( ) );

        }
        
        if ( _atelierForm == null  )
        {
        	_atelierForm = new AtelierForm( );
        }
        _atelierForm.setIdAtelier( idAtelier );
        _atelierForm.setChoixTitre( atelierFormEntryTitre );
        _atelierForm.setChoixDescription( atelierFormEntryDesc );

        _atelierForm.setChoixDescriptionComplementaires( listAtelierFormEntryCompl );
        return bValid;
    }

    /**
     * Manages the removal form of a atelier whose identifier is in the http
     * request
     *
     * @param request
     *            The Http request
     * @return the html code to confirm
     */
    @Action( ACTION_CONFIRM_REMOVE_ATELIER )
    public String getConfirmRemoveAtelier( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );
        UrlItem url = new UrlItem( getActionUrl( ACTION_REMOVE_ATELIER ) );
        url.addParameter( PARAMETER_ID_ATELIER, nId );

        String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_ATELIER,
                                                                  url.getUrl( ), AdminMessage.TYPE_CONFIRMATION );

        return redirect( request, strMessageUrl );
    }

    /**
     * Handles the removal form of a atelier
     *
     * @param request
     *            The Http request
     * @return the jsp URL to display the form to manage ateliers
     */
    @Action( ACTION_REMOVE_ATELIER )
    public String doRemoveAtelier( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );
        
        if ( _atelier == null || _atelier.getId( ) != nId )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }
        _atelier.setStatusPublic( Atelier.Status.STATUS_SUPPRIME_PAR_MDP );
        AtelierHome.update( _atelier );

        AtelierHome.removeAssociationsByIdAtelier( nId );

        addInfo( INFO_ATELIER_REMOVED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ATELIERS );
    }

    @Action( ACTION_REMOVE_ATELIER_FORM_ENTRY_COMP )
    public String doRemoveAtelierFormEntryComp( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );
        int nAtelierFormEntryComp = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER_FORM_ENTRY_COMP ) );

        AtelierFormEntryHome.remove( nAtelierFormEntryComp );
        addInfo( INFO_ATELIER_FORM_ENTRY_REMOVED, getLocale( ) );

        return redirect( request, VIEW_MODIFY_ATELIER_FORM, PARAMETER_ID_ATELIER, nId );
    }

    /**
     * Returns the form to update info about a atelier
     *
     * @param request The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_ATELIER )
    public String getModifyAtelier( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        if ( _atelier == null || ( _atelier.getId(  ) != nId ))
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }
        AtelierForm atelierFormFromDb = AtelierFormHome.getAtelierFormByAtelier( nId ) ;
        List<CampagneTheme> listCampagneThemes = (List<CampagneTheme>) CampagneThemeHome.getCampagneThemesList(  );
        
        Map<String, Object> model = getModel(  );
        model.put( MARK_ATELIER, _atelier );
        model.put( MARK_LANGUAGE, getLocale(  ) );
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance( ).getTypeLocalisationList( ) );
        model.put( MARK_ARRONDISSEMENTS_LIST, IdeeService.getInstance().getArrondissements( ) );
        model.put( MARK_HANDICAP_LIST, IdeeService.getInstance( ).getHandicapCodesList() );
        model.put( MARK_ATELIER_FORM_DB, atelierFormFromDb );
        model.put( MARK_CAMPAGNETHEME_LIST, listCampagneThemes );
        
        // Check to show the button to generate the idee
        int nParentIdeeId = AtelierService.getInstance().ideeAlreadyGenerated( _atelier.getId( ) );
        State state = AtelierWSService.getInstance( ).getAtelierState( _atelier.getId( ) );

        if ( nParentIdeeId != 0 )
        {
            model.put( MARK_GENERATED_IDEE_ID, nParentIdeeId );
        }
        else if ( state != null && state.getName( ).equals( AppPropertiesService
                .getProperty( IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_CLOS ) ) )
        {
            model.put( MARK_CAN_GENERATE_IDEE, true );
        }
 
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_ATELIER, TEMPLATE_MODIFY_ATELIER, model );
    }

    /**
     * Returns the form to update info about a atelier form
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_ATELIER_FORM )
    public String getModifyAtelierForm( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );
        
        if ( _atelierForm == null  )
        {
        	_atelierForm = AtelierFormHome.getAtelierFormByAtelier( nId );
        }
        
        List<AtelierFormEntry> listAtelierFormEntryComp = null;

        AtelierFormEntry atelierFormEntryComp = null;
        if ( _atelierForm != null )
        {
            listAtelierFormEntryComp = _atelierForm.getChoixDescriptionComplementaires( );
            if ( listAtelierFormEntryComp != null )
            {
                for ( AtelierFormEntry atfe : listAtelierFormEntryComp )
                {
                    if ( atfe.getId( ) == 0 )
                    {
                        atelierFormEntryComp = atfe;
                    }
                }
            }
        }
        listAtelierFormEntryComp = AtelierFormEntryHome.getFormEntriesComplByAtelierForm( _atelierForm );

        Map<String, Object> model = getModel( );
        model.put( MARK_ID_ATELIER, nId );
        model.put( MARK_ATELIER_FORM, _atelierForm );
        model.put( MARK_LANGUAGE, getLocale( ) );
        model.put( MARK_CHOIX_TITRE, _atelierForm.getChoixTitre( ) );
        model.put( MARK_CHOIX_DESC, _atelierForm.getChoixDescription( ) );
        model.put( MARK_CHOIX_DESC_COMP, atelierFormEntryComp );
        model.put( MARK_LIST_DESC_COMP, listAtelierFormEntryComp );
        return getPage( PROPERTY_PAGE_TITLE_MODIFY_ATELIER_FORM, TEMPLATE_MODIFY_ATELIER_FORM, model );
    }

    /**
     * Process the change form of a atelier
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_ATELIER )
    public String doModifyAtelier( HttpServletRequest request )
    {
        DateTimeConverter dtConverter = new DateConverter( );
        dtConverter.setPattern( MARK_FORMAT_DATE );
        ConvertUtils.register( dtConverter, Date.class );

        populate( _atelier, request );

        if ( _atelier.getStatusPublic( ) != null && _atelier.getStatusPublic( ).isPublished( ) == false )
        {
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_ATELIER_REMOVED,
                    JSP_MANAGE_ATELIERS, AdminMessage.TYPE_ERROR );
            return redirect( request, strMessageUrl );
        }

        String strCodesIdees = request.getParameter( PARAMETER_LIST_CODE_IDEES ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_LIST_CODE_IDEES );
        // Check constraints
        if ( !validateBean( _atelier, VALIDATION_ATTRIBUTES_PREFIX ) || !checkDates( request.getLocale( ) )
                || !checkCodesIdees( strCodesIdees, request.getLocale( ), _atelier.getId( ) )
                || !isAtelierAddressValid( request ) )
        {
            return redirect( request, VIEW_MODIFY_ATELIER, PARAMETER_ID_ATELIER, _atelier.getId( ) );
        }

        AtelierHome.update( _atelier );
        _atelier = null;
        addInfo( INFO_ATELIER_UPDATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_ATELIERS );
    }

    /**
     * Process the change form of a atelier form
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_ATELIER_FORM )
    public String doModifyAtelierForm( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        // Check constraints
        if ( !checkAtelierFormEntries( request, request.getLocale( ), nId ) )
        {
            return redirect( request, VIEW_MODIFY_ATELIER_FORM, PARAMETER_ID_ATELIER, nId );
        }

        AtelierFormEntryHome.update( _atelierForm.getChoixTitre( ) );
        AtelierFormEntryHome.update( _atelierForm.getChoixDescription( ) );
        List<AtelierFormEntry> listAtelierFormEntryCompl = _atelierForm.getChoixDescriptionComplementaires( );

        if ( listAtelierFormEntryCompl.size( ) > 0 )
        {
            for ( AtelierFormEntry formDescComp : listAtelierFormEntryCompl )
            {
                if ( formDescComp.getId( ) == 0 )
                {
                    AtelierFormEntryHome.create( formDescComp );
                }
                else
                {
                    AtelierFormEntryHome.update( formDescComp );
                }
            }
        }
        addInfo( INFO_ATELIER_FORM_ENTRY_UPDATED, getLocale( ) );
        _atelierForm = null;

        return redirect( request, VIEW_MODIFY_ATELIER_FORM, PARAMETER_ID_ATELIER, nId );
    }

    /**
     * Generate the idee
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_GENERATE_IDEE )
    public String doGenerateIdee( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        if ( _atelier == null || _atelier.getId( ) != nId )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }
        String strTitrePhase3 = _atelier.getTitrePhase3( );
        String strTextePhase3 = _atelier.getTextePhase3( );
        Long lCout = _atelier.getCout( );
        boolean bValid = true;

        if ( strTitrePhase3 == null || StringUtils.isEmpty( strTitrePhase3 ) )
        {
            addError( MESSAGE_ERROR_TITRE_PHASE3_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }
        if ( strTextePhase3 == null || StringUtils.isEmpty( strTextePhase3 ) )
        {
            addError( MESSAGE_ERROR_TEXTE_PHASE3_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }
        if ( lCout==0 )
        {
        	addError( MESSAGE_ERROR_COUT_NOT_EMPTY, request.getLocale( ) );
        	bValid = false;
        }

        if ( bValid == false )
        {
            return redirect( request, VIEW_MODIFY_ATELIER, PARAMETER_ID_ATELIER, nId );
        }
        
        // Check if the idee has already been generated
        int nParentIdeeId = AtelierService.getInstance().ideeAlreadyGenerated( _atelier.getId( ) );

        if ( nParentIdeeId == 0 )
        {
            Idee ideeGenerated = new Idee( );

            ideeGenerated.setTitre( _atelier.getTitrePhase3( ) );
            ideeGenerated.setDescription( _atelier.getTextePhase3( ) );

            ideeGenerated.setCodeCampagne( _atelier.getCampagne( ) );
            ideeGenerated.setCodeTheme( _atelier.getThematique( ) );
            ideeGenerated.setLocalisationType( _atelier.getLocalisationType( ) );
            ideeGenerated.setLocalisationArdt( _atelier.getLocalisationArdt( ) );
            ideeGenerated.setDepositaireType( AppPropertiesService
                    .getProperty( IdeationConstants.PROPERTY_GENERATE_IDEE_DEPOSITAIRE_TYPE ) );
            ideeGenerated.setDepositaire( AppPropertiesService
                    .getProperty( IdeationConstants.PROPERTY_GENERATE_IDEE_DEPOSITAIRE ) );
            ideeGenerated.setTypeQpvQva( IdeationApp.QPV_QVA_NO );
            ideeGenerated.setAdress( _atelier.getAddress( ) );
            ideeGenerated.setLongitude( _atelier.getLongitude( ) );
            ideeGenerated.setLatitude( _atelier.getLatitude( ) );
            ideeGenerated.setCout( _atelier.getCout( ) );
            ideeGenerated.setHandicap( _atelier.getHandicap() );
            ideeGenerated.setHandicapComplement( "" );

            ideeGenerated.setCreationTimestamp( new java.sql.Timestamp( ( new java.util.Date( ) ).getTime( ) ) );
            ideeGenerated.setStatusPublic( Idee.Status.STATUS_DEPOSE );
            ideeGenerated.setLuteceUserName( AppPropertiesService
                    .getProperty( IdeationConstants.PROPERTY_GENERATE_IDEE_LUTECE_USER_NAME )  );
            ideeGenerated.setDocs( new ArrayList<File>( ) );
            ideeGenerated.setImgs( new ArrayList<File>( ) );

            try
            {
                IdeeService.getInstance( ).createIdeeFromAtelier( ideeGenerated, _atelier );
                createWorkflowResource( ideeGenerated, request );

                String strWorkflowActionGenerationIdee = AppPropertiesService
                        .getProperty( IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_GENERATION_IDEE );
                AtelierWSService.getInstance( ).processActionByName( strWorkflowActionGenerationIdee,
                                                                     _atelier.getId( ) );
            }
            catch ( IdeationErrorException e )
            {
                AppLogService.error( e );
            }
            addInfo( INFO_ATELIER_IDEE_GENERATED, getLocale( ) );
        }
        _atelier = null;
        return redirectView( request, VIEW_MANAGE_ATELIERS );
    }

    /**
     * Process the form of the page ideation. Can't have this in the service
     * because it needs to be done after the transaction because the task uses
     * the idee from the database
     * 
     * @param idee
     *            The idee
     */
    private void createWorkflowResource( Idee idee, HttpServletRequest request )
    {

        int idWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
        String strWorkflowActionNameCreateIdee = AppPropertiesService
                .getProperty( IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_IDEE );

        if ( idWorkflow != -1 )
        {
            try
            {
                // Initialize the workflow, this creates the state for our
                // resource
                WorkflowService.getInstance( ).getState( idee.getId( ), Idee.WORKFLOW_RESOURCE_TYPE, idWorkflow, -1 );
                IdeeWSService.getInstance( ).processActionByName( strWorkflowActionNameCreateIdee, idee.getId( ) );

            }
            catch ( Exception e )
            {
                AppLogService.error( "Ideation: error in idee creation workflow", e );
            }
        }
        else
        {
            AppLogService.error( "Ideation: app property idWorkflow not set" );
        }
    }

    /**
     * Export the values from core_user_preferences into csv file
     * 
     * @param request
     *            The Http request
     * @param response
     *            The Http response
     */
    public void doExportCsvUsers( HttpServletRequest request, HttpServletResponse response )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        if ( _atelier == null || ( _atelier.getId( ) != nId ) )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }
        List<Integer> ideesIdList = (List<Integer>) AtelierHome.getIdeeIdsListByAtelier( _atelier.getId( ) );
        List<ArrayList<String>> valuesList = IdeeUsersService.getExportAtelierUsersList( ideesIdList );
        try
        {
            // Generate CSV file
            String strFormatExtension = AppPropertiesService.getProperty( PROPERTY_CSV_EXTENSION );
            String strFileName = "atelier" + nId + "_" + AppPropertiesService.getProperty( PROPERTY_CSV_FILE_NAME )
                    + "." + strFormatExtension;
            IdeeExportUtils.addHeaderResponse( request, response, strFileName, strFormatExtension );

            OutputStream os = response.getOutputStream( );

            // say how to decode the csv file, with utf8
            byte[] bom = new byte[]
            { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }; // BOM values
            os.write( bom ); // adds BOM
            CsvUtils.writeCsv( CsvUtils.ATELIERUSERS_PREFIX_CSV, valuesList, os, request.getLocale( ) );

            os.flush( );
            os.close( );
        }
        catch ( IOException e )
        {
            AppLogService.error( e );
        }
    }

    /**
     * Process to search an atelier
     * 
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( value = ACTION_SEARCH_ATELIER )
    public String doSearchAtelier( HttpServletRequest request )
    {

        if ( _atelierSearcher == null )
        {
            _atelierSearcher = new AtelierSearcher( );
        }

        String strCodeCampagne = request.getParameter( PARAMETER_FILTER_CODE_CAMPAGNE );
        if ( strCodeCampagne != null )
        {
            if ( StringUtils.isBlank( strCodeCampagne ) )
            {
                _atelierSearcher.setCodeCampagne( null );
            }
            else
            {
                _atelierSearcher.setCodeCampagne( strCodeCampagne );
            }
        }

        String strCodeTheme = request.getParameter( PARAMETER_FILTER_CODE_THEME );
        if ( strCodeTheme != null )
        {
            if ( StringUtils.isBlank( strCodeTheme ) )
            {
                _atelierSearcher.setCodeTheme( null );
            }
            else
            {
                _atelierSearcher.setCodeTheme( strCodeTheme );
            }
        }

        String strTitreOuDesc = request.getParameter( PARAMETER_FILTER_TITRE_OU_DESCRIPTION );
        if ( strTitreOuDesc != null )
        {
            if ( StringUtils.isBlank( strTitreOuDesc ) )
            {
                _atelierSearcher.setTitreOuDescription( null );
            }
            else
            {
                _atelierSearcher.setTitreOuDescription( strTitreOuDesc );
            }
        }
        
        String strType = request.getParameter( PARAMETER_FILTER_TYPE );
        if ( strType != null )
        {
            if ( StringUtils.isBlank( strType ) )
            {
                _atelierSearcher.setType( null );
            }
            else
            {
                _atelierSearcher.setType( strType );
            }
        }
        
        String strTypeLocalisation = request.getParameter( PARAMETER_FILTER_TYPE_LOCALISATION );
        if ( strTypeLocalisation != null )
        {
            if ( StringUtils.isBlank( strTypeLocalisation ) )
            {
                _atelierSearcher.setTypeLocalisation( null );
            }
            else
            {
                _atelierSearcher.setTypeLocalisation( strTypeLocalisation );
            }
        }

        String strArrandissement = request.getParameter( PARAMETER_FILTER_ARRANDISSEMENT );
        if ( strArrandissement != null )
        {
            if ( StringUtils.isBlank( strArrandissement ) )
            {
                _atelierSearcher.setLocalisationArdt( null );
            }
            else
            {
                _atelierSearcher.setLocalisationArdt( strArrandissement );
            }
        }

        String strHandicap = request.getParameter( PARAMETER_FILTER_HANDICAP );
        if ( strHandicap != null ) {
            if ( StringUtils.isBlank( strHandicap ) ) {
            	_atelierSearcher.setHandicap( null );
            }
            else {
            	_atelierSearcher.setHandicap( strHandicap );
            }
        }

        String strSortColumn = request.getParameter( PARAMETER_SORT_COLUMN );
        if ( strSortColumn != null )
        {
            if ( StringUtils.isBlank( strSortColumn ) )
            {
                _atelierSearcher.setOrderColumn( null );
            }
            else
            {
                _atelierSearcher.setOrderColumn( strSortColumn );
            }
        }

        String strSortAscDesc = request.getParameter( PARAMETER_SORT_ORDER );
        if ( strSortAscDesc != null )
        {
            if ( StringUtils.isBlank( strSortAscDesc ) )
            {
                _atelierSearcher.setOrderAscDesc( null );
            }
            else
            {
                _atelierSearcher.setOrderAscDesc( strSortAscDesc );
            }
        }

        return redirectView( request, VIEW_MANAGE_ATELIERS );

    }

    /**
     * Reset the search
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @Action( value = ACTION_CANCEL_SEARCH )
    public String doCancelSearch( HttpServletRequest request )
    {
        _atelierSearcher = null;
        return redirectView( request, VIEW_MANAGE_ATELIERS );
    }

    /**
     * Returns the results of votes
     * 
     * @param request
     *            the HTTP request
     * @return The Jsp URL of the process result
     */
    @View( VIEW_RESULTS_VOTES )
    public String getResultsVotes( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );

        if ( _atelier == null )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }
        List<AtelierFormEntry> listAtelierFormEntryComp = null;
        AtelierFormEntry atelierFormEntryComp = null;
        Map<String, Object> model = getModel( );
        int maxVotesTitre = 0, maxVotesDesc = 0;
        if ( _atelierForm != null )
        {
            listAtelierFormEntryComp = _atelierForm.getChoixDescriptionComplementaires( );
            if ( listAtelierFormEntryComp != null )
            {
                for ( AtelierFormEntry atfe : listAtelierFormEntryComp )
                {
                    if ( atfe.getId( ) == 0 )
                    {
                        atelierFormEntryComp = atfe;
                    }
                }
            }
        }
        _atelierForm = AtelierFormHome.getAtelierFormByAtelier( nId );
        List<Integer> resultsVotesTitre = new ArrayList<Integer>( 3 );
        List<Integer> resultsVotesDescription = new ArrayList<Integer>( 3 );
        List<Integer> resultsVotesDescriptionComp = new ArrayList<Integer>( );
        ;
        List<Integer> listMaxVotesDescComp = new ArrayList<Integer>( );
        List<List<Integer>> listResultVotesComp = new ArrayList<List<Integer>>( );

        if ( _atelierForm != null )
        {
            listAtelierFormEntryComp = AtelierFormEntryHome.getFormEntriesComplByAtelierForm( _atelierForm );

            int maxVotesComp = 0;
            for ( int i = 1; i <= 3; i++ )
            {
                resultsVotesTitre.add( AtelierFormResultHome.getTitreResult( _atelierForm.getId( ), i ) );
                resultsVotesDescription.add( AtelierFormResultHome.getDescriptionResult( _atelierForm.getId( ), i ) );
            }

            for ( AtelierFormEntry afe : listAtelierFormEntryComp )
            {
                resultsVotesDescriptionComp = new ArrayList<Integer>( 3 );
                resultsVotesDescriptionComp
                        .add( AtelierFormResultHome.getEntryResult( _atelierForm.getId( ), afe.getId( ), 1 ) );
                resultsVotesDescriptionComp
                        .add( AtelierFormResultHome.getEntryResult( _atelierForm.getId( ), afe.getId( ), 2 ) );
                resultsVotesDescriptionComp
                        .add( AtelierFormResultHome.getEntryResult( _atelierForm.getId( ), afe.getId( ), 3 ) );
                listResultVotesComp.add( resultsVotesDescriptionComp );
                maxVotesComp = Collections.max( resultsVotesDescriptionComp );
                listMaxVotesDescComp.add( maxVotesComp );
            }
            maxVotesTitre = Collections.max( resultsVotesTitre );
            maxVotesDesc = Collections.max( resultsVotesDescription );

            model.put( MARK_CHOIX_TITRE, _atelierForm.getChoixTitre( ) );
            model.put( MARK_CHOIX_DESC, _atelierForm.getChoixDescription( ) );
        }
        model.put( MARK_ID_ATELIER, nId );
        model.put( MARK_ATELIER_FORM, _atelierForm );
        model.put( MARK_LANGUAGE, getLocale( ) );
        model.put( MARK_CHOIX_DESC_COMP, atelierFormEntryComp );
        model.put( MARK_LIST_DESC_COMP, listAtelierFormEntryComp );
        model.put( MARK_LIST_RESULT_VOTES_TITRE, resultsVotesTitre );
        model.put( MARK_MAX_VOTES_TITRE, maxVotesTitre );
        model.put( MARK_LIST_RESULT_VOTES_DESC, resultsVotesDescription );
        model.put( MARK_MAX_VOTES_DESC, maxVotesDesc );
        model.put( MARK_LIST_RESULT_VOTES_DESC_COMP, listResultVotesComp );
        model.put( MARK_LIST_MAX_VOTES_COMP, listMaxVotesDescComp );
        model.put( MARK_ATELIER, _atelier );

        return getPage( PROPERTY_PAGE_TITLE_RESULTS_VOTES, TEMPLATE_RESULTS_VOTES, model );
    }

    /**
     * Add title and teste phase 3 to atelier
     * 
     * @param request
     *            the HTTP request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_VALIDPHASE3 )
    public String doValidPhase3( HttpServletRequest request )
    {

        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );
        if ( _atelier == null )
        {
            _atelier = AtelierHome.findByPrimaryKey( nId );
        }
        String strTitrePhase3 = request.getParameter( PARAMETER_TITRE_PHASE3 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_TITRE_PHASE3 );
        String strTextePhase3 = request.getParameter( PARAMETER_TEXTE_PHASE3 ) == null ? StringUtils.EMPTY
                : request.getParameter( PARAMETER_TEXTE_PHASE3 );
        boolean bValid = true;

        if ( StringUtils.isNotEmpty( strTextePhase3 ) && StringUtils.isEmpty( strTitrePhase3 ) )
        {
            addError( MESSAGE_ERROR_TITRE_PHASE3_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }
        if ( StringUtils.isNotEmpty( strTitrePhase3 ) && StringUtils.isEmpty( strTextePhase3 ) )
        {
            addError( MESSAGE_ERROR_TEXTE_PHASE3_NOT_EMPTY, request.getLocale( ) );
            bValid = false;
        }

        _atelier.setTitrePhase3( strTitrePhase3 );
        _atelier.setTextePhase3( strTextePhase3 );

        if ( bValid )
        {
            AtelierHome.update( _atelier );
            _atelier = null;
        }
        else
        {
            return redirect( request, VIEW_RESULTS_VOTES, PARAMETER_ID_ATELIER, nId );
        }
        addInfo( INFO_ATELIER_UPDATED, getLocale( ) );
        return redirectView( request, VIEW_MANAGE_ATELIERS );
    }
    
    /**
     * Returns the history of a atelier
     *
     * @param request The Http request
     * @return the html code of the atelier form
     */
    @View( VIEW_ATELIER_HISTORY )
    public String getAtelierHistory( HttpServletRequest request )
    {
        
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_ATELIER ) );
        int nIdWorkflow=AppPropertiesService.getPropertyInt(IdeationConstants.PROPERTY_WORKFLOW_ID_ATELIER, -1);
        Atelier atelier = AtelierHome.findByPrimaryKey( nId );
        Map<String, Object> model = getModel(  );
        
        model.put( MARK_ATELIER_HISTORY,
                WorkflowService.getInstance(  )
                               .getDisplayDocumentHistory( nId, Atelier.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, request,
                    getLocale(  ) ) );
        
        
        model.put( MARK_ATELIER, atelier );
        model.put( MARK_LANGUAGE, getLocale(  ) );
        
      
        return getPage( PROPERTY_PAGE_TITLE_VIEW_ATELIER_HISTORY, TEMPLATE_VIEW_ATELIER_HISTORY, model );
    }
	
}
