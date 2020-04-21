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

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.participatoryideation.business.capgeo.QpvQva;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalSearcher;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationErrorException;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.participatoryideation.service.ProposalService;
import fr.paris.lutece.plugins.participatoryideation.service.ProposalUsersService;
import fr.paris.lutece.plugins.participatoryideation.service.SolrProposalIndexer;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignDataProvider;
import fr.paris.lutece.plugins.participatoryideation.service.capgeo.QpvQvaService;
import fr.paris.lutece.plugins.participatoryideation.util.CsvUtils;
import fr.paris.lutece.plugins.participatoryideation.util.ParticipatoryIdeationConstants;
import fr.paris.lutece.plugins.participatoryideation.util.ProposalExportUtils;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
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

/**
 * This class provides the user interface to manage Proposal features ( manage, create, modify, remove )
 */
@Controller( controllerJsp = "ManageProposals.jsp", controllerPath = "jsp/admin/plugins/participatoryideation/", right = "PARTICIPATORYIDEATION_PROPOSALS_MANAGEMENT" )
public class ProposalJspBean extends ManageIdeationProposalsJspBean
{

    // //////////////////////////////////////////////////////////////////////////
    // ParticipatoryIdeationConstants

    // templates
    private static final String TEMPLATE_MANAGE_PROPOSALS = "/admin/plugins/participatoryideation/manage_proposals.ftl";
    private static final String TEMPLATE_INIT_PROPOSAL = "/admin/plugins/participatoryideation/init_proposal.ftl";
    private static final String TEMPLATE_COMPLETE_PROPOSAL = "/admin/plugins/participatoryideation/complete_proposal.ftl";
    private static final String TEMPLATE_MODIFY_PROPOSAL = "/admin/plugins/participatoryideation/modify_proposal.ftl";
    private static final String TEMPLATE_CONFIRM_REMOVE_PROPOSAL = "/admin/plugins/participatoryideation/confirm_remove_proposal.ftl";

    // Parameters
    private static final String PARAMETER_CAMPAIGN_CODE = "campaign_code";
    private static final String PARAMETER_ID_PROPOSAL = "id";
    private static final String PARAMETER_ID_ACTION = "id_action";
    private static final String PARAMETER_ID_RESOURCE = "id_resource";
    private static final String PARAMETER_FILTER_CODE_CAMPAIGN = "filter_code_campaign";
    private static final String PARAMETER_FILTER_CODE_THEME = "filter_code_theme";
    private static final String PARAMETER_FILTER_TITRE_OU_DESCRIPTION = "filter_titre_ou_description";
    private static final String PARAMETER_FILTER_PUBLIC_STATE = "filter_status";
    private static final String PARAMETER_FILTER_QPVQVA = "filter_qpvqva";
    private static final String PARAMETER_FILTER_HANDICAP = "filter_handicap";
    private static final String PARAMETER_FILTER_TYPE_LOCATION = "filter_type_location";
    private static final String PARAMETER_FILTER_AREA = "filter_arrondissement";
    private static final String PARAMETER_SORT_COLUMN = "sort_column";
    private static final String PARAMETER_SORT_ORDER = "sort_order";
    private static final String PARAMETER_MOTIFRECEV = "motifRecev";
    private static final String PARAMETER_PLUGIN_NAME = "plugin_name";

    // Properties for page titles
    private static final String PROPERTY_PAGE_TITLE_MANAGE_PROPOSALS = "participatoryideation.manage_proposals.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CREATE_PROPOSAL = "participatoryideation.create_proposal.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_MODIFY_PROPOSAL = "participatoryideation.modify_proposal.pageTitle";
    private static final String PROPERTY_PAGE_TITLE_CONFIRM_REMOVE_PROPOSAL = "participatoryideation.confirm_remove_proposal.pageTitle";

    // Markers
    private static final String MARK_CAMPAIGN_CODE = "campaign_code";
    private static final String MARK_CAMPAIGN_LIST = "campaign_list";
    private static final String MARK_CAMPAIGNTHEME_LIST = "campaigntheme_list";
    private static final String MARK_HANDICAP_LIST = "handicap_list";
    private static final String MARK_PROPOSAL_LIST = "proposal_list";
    private static final String MARK_PROPOSAL = "proposal";
    private static final String MARK_PROPOSAL_BO_FORM = "proposal_bo_form";
    private static final String MARK_FILTER_CODE_CAMPAIGN = "filter_code_campaign";
    private static final String MARK_FILTER_CODE_THEME = "filter_code_theme";
    private static final String MARK_FILTER_TITRE_OU_DESCRIPTION = "filter_titre_ou_description";
    private static final String MARK_FILTER_QPVQVA = "filter_qpvqva";
    private static final String MARK_FILTER_HANDICAP = "filter_handicap";
    private static final String MARK_FILTER_TYPE_LOCATION = "filter_type_location";
    private static final String MARK_FILTER_ARRONDISSEMENT = "filter_arrondissement";
    private static final String MARK_LANGUAGE = "language";
    private static final String MARK_LOCATION_TYPE_LIST = "type_location_list";
    private static final String MARK_AREA_LIST = "area_list";
    private static final String MARK_SORT_COLUMN = "sort_column";
    private static final String MARK_SORT_ORDER = "sort_order";

    private static final String MARK_WORKFLOW_STATE_MAP = "workflow_state_map"; // Workflow state of each proposal. Key = id of proposal.
    private static final String MARK_WORKFLOW_ACTIONS_MAP = "workflow_actions_map"; // Workflow actions of each proposal. Key = id of proposal.

    private static final String MARK_RESOURCE_HISTORY = "workflow_history";

    private static final String JSP_MANAGE_PROPOSALS = "jsp/admin/plugins/participatoryideation/ManageProposals.jsp";

    // Properties
    private static final String MESSAGE_ERROR_PROPOSAL_REMOVED = "participatoryideation.message.error.proposal.removed";
    private static final String MESSAGE_ERROR_CAMPAIGN_NOT_SPECIFIED = "participatoryideation.message.error.campaign.notSpecified";
    private static final String MESSAGE_ERROR_ARRONDISSEMENT_EMPTY = "participatoryideation.validation.proposal.Arrondissement.notEmpty";
    private static final String MESSAGE_ERROR_ADDRESS_FORMAT = "participatoryideation.validation.proposal.Address.Format";
    private static final String MESSAGE_ERROR_ADDRESS_LOCATION_TYPE_EMPTY = "participatoryideation.validation.proposal.LocationType.NotEmpty";
    private static final String MESSAGE_ERROR_PROPOSAL_NO_SUCH_WORKFLOW_ACTION = "participatoryideation.message.error.workflow.noSuchAction";
    private static final String MESSAGE_ERROR_PROPOSAL_NO_SUCH_WORKFLOW_RESOURCE = "participatoryideation.message.error.workflow.noSuchResource";

    private static final String VALIDATION_ATTRIBUTES_PREFIX = "participatoryideation.model.entity.proposal.attribute.";

    private static final String PROPERTY_CSV_EXTENSION = "participatoryideation.csv.extension";
    private static final String PROPERTY_CSV_FILE_NAME = "participatoryideation.csv.file.name";

    // Views
    private static final String VIEW_MANAGE_PROPOSALS = "manageProposals";
    private static final String VIEW_INIT_PROPOSAL = "initProposal";
    private static final String VIEW_COMPLETE_PROPOSAL = "completeProposal";
    private static final String VIEW_MODIFY_PROPOSAL = "modifyProposal";
    private static final String VIEW_CONFIRM_REMOVE_PROPOSAL = "confirmRemoveProposal";

    // Actions
    private static final String ACTION_CREATE_PROPOSAL = "createProposal";
    private static final String ACTION_MODIFY_PROPOSAL = "modifyProposal";
    private static final String ACTION_SEARCH_PROPOSAL = "searchProposal";
    private static final String ACTION_CANCEL_SEARCH = "cancelSearch";
    private static final String ACTION_CONFIRM_REMOVE_PROPOSAL = "confirmRemoveProposal";
    private static final String ACTION_PROCESS_WORKFLOW_ACTION = "processWorkflowAction";

    // Infos
    private static final String INFO_PROPOSAL_CREATED = "participatoryideation.info.proposal.created";
    private static final String INFO_PROPOSAL_UPDATED = "participatoryideation.info.proposal.updated";
    private static final String INFO_PROPOSAL_REMOVED = "participatoryideation.info.proposal.removed";

    private static SolrProposalIndexer _solrProposalIndexer = SpringContextService.getBean( "participatoryideation.solrProposalIndexer" );

    // Workflow
    private static final String WORKFLOW_ID_DEFAULT = "100";
    private static final String WORKFLOW_ID = AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ID, WORKFLOW_ID_DEFAULT );

    // Session variable to store working values
    private ProposalBoForm _proposalBoForm;
    private Proposal _proposal;
    private ProposalSearcher _proposalSearcher;
    private static ProposalSearcher defaultSearcher;

    static
    {
        defaultSearcher = new ProposalSearcher( );
        defaultSearcher.setOrderAscDesc( ProposalSearcher.ORDER_DESC );
        defaultSearcher.setOrderColumn( ProposalSearcher.COLUMN_REFERENCE );
    }

    // ***********************************************************************************
    // * MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MA *
    // * MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MANAGE MA *
    // ***********************************************************************************

    /**
     * Build the Manage View
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @View( value = VIEW_MANAGE_PROPOSALS, defaultView = true )
    public String getManageProposals( HttpServletRequest request )
    {
        _proposal = null;
        _proposalBoForm = null;
        ProposalSearcher currentSearcher = _proposalSearcher != null ? _proposalSearcher : defaultSearcher;
        List<Proposal> listProposals = (List<Proposal>) ProposalHome.getProposalsListSearch( currentSearcher );
        Map<String, Object> model = getPaginatedListModel( request, MARK_PROPOSAL_LIST, listProposals, JSP_MANAGE_PROPOSALS );

        if ( _proposalSearcher != null )
        {
            if ( StringUtils.isNotBlank( _proposalSearcher.getCodeCampaign( ) ) )
            {
                model.put( MARK_FILTER_CODE_CAMPAIGN, _proposalSearcher.getCodeCampaign( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getCodeTheme( ) ) )
            {
                model.put( MARK_FILTER_CODE_THEME, _proposalSearcher.getCodeTheme( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getTitreOuDescriptionouRef( ) ) )
            {
                model.put( MARK_FILTER_TITRE_OU_DESCRIPTION, _proposalSearcher.getTitreOuDescriptionouRef( ) );
            }

            if ( _proposalSearcher.getStatusPublic( ) != null )
            {
                model.put( PARAMETER_FILTER_PUBLIC_STATE, _proposalSearcher.getStatusPublic( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getTypeQpvQva( ) ) )
            {
                model.put( MARK_FILTER_QPVQVA, _proposalSearcher.getTypeQpvQva( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getHandicap( ) ) )
            {
                model.put( MARK_FILTER_HANDICAP, _proposalSearcher.getHandicap( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getTypeLocation( ) ) )
            {
                model.put( MARK_FILTER_TYPE_LOCATION, _proposalSearcher.getTypeLocation( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getArrondissement( ) ) )
            {
                model.put( MARK_FILTER_ARRONDISSEMENT, _proposalSearcher.getArrondissement( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getOrderColumn( ) ) )
            {
                model.put( MARK_SORT_COLUMN, _proposalSearcher.getOrderColumn( ) );
            }

            if ( StringUtils.isNotBlank( _proposalSearcher.getOrderAscDesc( ) ) )
            {
                model.put( MARK_SORT_ORDER, _proposalSearcher.getOrderAscDesc( ) );
            }
        }

        IdeationStaticService.getInstance( ).fillAllStaticContent( model );

        // Add workflow informations for each proposal
        if ( WorkflowService.getInstance( ).isAvailable( ) )
        {
            // Identify workflow id
            int workflowId = -1;
            try
            {
                workflowId = Integer.parseInt( WORKFLOW_ID );
            }
            catch( NumberFormatException e )
            {
                workflowId = Integer.parseInt( WORKFLOW_ID_DEFAULT );
                AppLogService.error( "No such ideation workflow id : #" + WORKFLOW_ID + ", so using #100 by default.", e );
            }

            // Add data
            Map<String, State> stateMap = new HashMap<>( );
            Map<String, Collection<fr.paris.lutece.plugins.workflowcore.business.action.Action>> actionsMap = new HashMap<>( );
            for ( Proposal proposal : listProposals )
            {
                stateMap.put( "" + proposal.getId( ),
                        WorkflowService.getInstance( ).getState( proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, workflowId, -1 ) );
                actionsMap.put( "" + proposal.getId( ),
                        WorkflowService.getInstance( ).getActions( proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, workflowId, getUser( ) ) );
            }

            model.put( MARK_WORKFLOW_STATE_MAP, stateMap );
            model.put( MARK_WORKFLOW_ACTIONS_MAP, actionsMap );
        }

        return getPage( PROPERTY_PAGE_TITLE_MANAGE_PROPOSALS, TEMPLATE_MANAGE_PROPOSALS, model );
    }

    // ***********************************************************************************
    // * SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SE *
    // * SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SEARCH SE *
    // ***********************************************************************************

    /**
     * Process the search filters/sorts
     * 
     * @param request
     *            The HTTP request
     * @return The page
     */
    @Action( value = ACTION_SEARCH_PROPOSAL )
    public String doSearchProposal( HttpServletRequest request )
    {
        if ( _proposalSearcher == null )
        {
            _proposalSearcher = new ProposalSearcher( );
        }

        String strCodeCampaign = request.getParameter( PARAMETER_FILTER_CODE_CAMPAIGN );
        if ( strCodeCampaign != null )
        {
            if ( StringUtils.isBlank( strCodeCampaign ) )
            {
                _proposalSearcher.setCodeCampaign( null );
            }
            else
            {
                _proposalSearcher.setCodeCampaign( strCodeCampaign );
            }
        }

        String strCodeTheme = request.getParameter( PARAMETER_FILTER_CODE_THEME );
        if ( strCodeTheme != null )
        {
            if ( StringUtils.isBlank( strCodeTheme ) )
            {
                _proposalSearcher.setCodeTheme( null );
            }
            else
            {
                _proposalSearcher.setCodeTheme( strCodeTheme );
            }
        }

        String strTitre = request.getParameter( PARAMETER_FILTER_TITRE_OU_DESCRIPTION );
        if ( strTitre != null )
        {
            if ( StringUtils.isBlank( strTitre ) )
            {
                _proposalSearcher.setTitreOuDescriptionouRef( null );
            }
            else
            {
                _proposalSearcher.setTitreOuDescriptionouRef( strTitre );
            }
        }

        String strPublicState = request.getParameter( PARAMETER_FILTER_PUBLIC_STATE );
        if ( strPublicState != null )
        {
            if ( StringUtils.isBlank( strPublicState ) )
            {
                _proposalSearcher.setStatusPublic( null );
            }
            else
            {
                _proposalSearcher.setStatusPublic( strPublicState );
            }
        }

        String strTypeQpvQva = request.getParameter( PARAMETER_FILTER_QPVQVA );
        if ( strTypeQpvQva != null )
        {
            if ( StringUtils.isBlank( strTypeQpvQva ) )
            {
                _proposalSearcher.setTypeQpvQva( null );
            }
            else
            {
                _proposalSearcher.setTypeQpvQva( strTypeQpvQva );
            }
        }

        String strHandicap = request.getParameter( PARAMETER_FILTER_HANDICAP );
        if ( strHandicap != null )
        {
            if ( StringUtils.isBlank( strHandicap ) )
            {
                _proposalSearcher.setHandicap( null );
            }
            else
            {
                _proposalSearcher.setHandicap( strHandicap );
            }
        }

        String strTypeLocation = request.getParameter( PARAMETER_FILTER_TYPE_LOCATION );
        if ( strTypeLocation != null )
        {
            if ( StringUtils.isBlank( strTypeLocation ) )
            {
                _proposalSearcher.setTypeLocation( null );
            }
            else
            {
                _proposalSearcher.setTypeLocation( strTypeLocation );
            }
        }

        String strArrondissement = request.getParameter( PARAMETER_FILTER_AREA );
        if ( strArrondissement != null )
        {
            if ( StringUtils.isBlank( strArrondissement ) )
            {
                _proposalSearcher.setArrondissement( null );
            }
            else
            {
                _proposalSearcher.setArrondissement( strArrondissement );
            }
        }

        String strSortColumn = request.getParameter( PARAMETER_SORT_COLUMN );
        if ( strSortColumn != null )
        {
            if ( StringUtils.isBlank( strSortColumn ) )
            {
                _proposalSearcher.setOrderColumn( null );
            }
            else
            {
                _proposalSearcher.setOrderColumn( strSortColumn );
            }
        }

        String strSortAscDesc = request.getParameter( PARAMETER_SORT_ORDER );
        if ( strSortAscDesc != null )
        {
            if ( StringUtils.isBlank( strSortAscDesc ) )
            {
                _proposalSearcher.setOrderAscDesc( null );
            }
            else
            {
                _proposalSearcher.setOrderAscDesc( strSortAscDesc );
            }
        }

        return redirectView( request, VIEW_MANAGE_PROPOSALS );
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
        _proposalSearcher = null;
        return redirectView( request, VIEW_MANAGE_PROPOSALS );
    }

    // ***********************************************************************************
    // * CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CR *
    // * CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CREATE CR *
    // ***********************************************************************************
    // * First step : initialize the proposal by specifying the campaign *
    // * Second step : complete other data *
    // ***********************************************************************************

    @View( VIEW_INIT_PROPOSAL )
    public String getInitProposal( HttpServletRequest request )
    {
        _proposal = new Proposal( );

        Map<String, Object> model = getModel( );
        model.put( MARK_CAMPAIGN_LIST, IdeationCampaignDataProvider.getInstance( ).getCampaigns( ) );
        model.put( MARK_PROPOSAL, _proposal );
        model.put( MARK_LANGUAGE, getLocale( ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PROPOSAL, TEMPLATE_INIT_PROPOSAL, model );
    }

    @View( VIEW_COMPLETE_PROPOSAL )
    public String getCompleteProposal( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( );

        // If no campaign specified, expect it in request.
        if ( StringUtils.isBlank( _proposal.getCodeCampaign( ) ) )
        {
            _proposal.setCodeCampaign( request.getParameter( PARAMETER_CAMPAIGN_CODE ) );
            if ( StringUtils.isBlank( _proposal.getCodeCampaign( ) ) )
            {
                Map<String, Object> requestParameters = new HashMap<String, Object>( );
                requestParameters.put( PARAMETER_PLUGIN_NAME, "participatoryideation" );
                String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_CAMPAIGN_NOT_SPECIFIED, JSP_MANAGE_PROPOSALS,
                        AdminMessage.TYPE_ERROR, requestParameters );
                return redirect( request, strMessageUrl );
            }
        }

        // Data depending of specified campaign
        String campaignCode = _proposal.getCodeCampaign( );
        model.put( MARK_CAMPAIGN_CODE, campaignCode );
        model.put( MARK_CAMPAIGNTHEME_LIST, IdeationCampaignDataProvider.getInstance( ).getCampaignThemes( campaignCode ) );
        model.put( MARK_AREA_LIST, IdeationCampaignDataProvider.getInstance( ).getCampaignAllAreas( campaignCode ) );

        // Data NOT depending of specified campaign
        model.put( MARK_LOCATION_TYPE_LIST, ProposalService.getInstance( ).getTypeLocationList( ) );
        model.put( MARK_HANDICAP_LIST, ProposalService.getInstance( ).getHandicapCodesList( ) );
        model.put( MARK_PROPOSAL, _proposal );
        model.put( MARK_LANGUAGE, getLocale( ) );

        return getPage( PROPERTY_PAGE_TITLE_CREATE_PROPOSAL, TEMPLATE_COMPLETE_PROPOSAL, model );
    }

    @Action( ACTION_CREATE_PROPOSAL )
    public String doCreateProposal( HttpServletRequest request )
    {

        populate( _proposal, request );

        // Check constraints
        if ( !validateBean( _proposal, VALIDATION_ATTRIBUTES_PREFIX ) || !isProposalAddressValid( request ) )
        {
            return redirectView( request, VIEW_COMPLETE_PROPOSAL );
        }

        if ( Proposal.LOCATION_TYPE_PARIS.equals( _proposal.getLocationType( ) ) && StringUtils.isEmpty( _proposal.getGeoJson( ) ) )
        {
            _proposal.setLocationArdt( null );
        }

        _proposal.setCodeProposal( 0 );
        // _proposal.setCodeCampaign( CampaignHome.getLastCampaign( ).getCode( ) );
        _proposal.setSubmitterType( AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_GENERATE_PROPOSAL_SUBMITTER_TYPE ) );
        _proposal.setSubmitter( AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_GENERATE_PROPOSAL_SUBMITTER ) );
        _proposal.setLuteceUserName( AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_GENERATE_PROPOSAL_LUTECE_USER_NAME ) );
        _proposal.setCreationTimestamp( new java.sql.Timestamp( ( new java.util.Date( ) ).getTime( ) ) );
        _proposal.setStatusPublic( Proposal.Status.STATUS_SUBMITTED );
        _proposal.setTypeQpvQva( IdeationApp.QPV_QVA_NO );
        _proposal.setDocs( new ArrayList<File>( ) );
        _proposal.setImgs( new ArrayList<File>( ) );
        _proposal.setField1( "" );
        _proposal.setfield2( "" );
        _proposal.setField3( "" );

        /* Identifying QPV from address if GeoJSON available */
        if ( StringUtils.isNotEmpty( _proposal.getGeoJson( ) ) )
        {
            List<QpvQva> listQpvqva;
            try
            {
                listQpvqva = QpvQvaService.getQpvQva( _proposal.getLongitude( ), _proposal.getLatitude( ) );
                if ( listQpvqva.size( ) == 0 )
                {
                    _proposal.setTypeQpvQva( IdeationApp.QPV_QVA_NO );
                    _proposal.setIdQpvQva( null );
                    _proposal.setLibelleQpvQva( null );
                }
                else
                {
                    // For backwards compatibility, choose a QPV/QVA in priority if it exists
                    QpvQva resQpvQva = null;
                    for ( QpvQva qpvqva : listQpvqva )
                    {
                        if ( StringUtils.isNotBlank( qpvqva.getType( ) ) )
                        {
                            resQpvQva = qpvqva;
                            break;
                        }
                    }
                    // If not qpvqva, just take the first one...
                    if ( resQpvQva == null )
                    {
                        resQpvQva = listQpvqva.get( 0 );
                    }

                    if ( StringUtils.isNotBlank( resQpvQva.getType( ) ) )
                    {
                        _proposal.setTypeQpvQva( resQpvQva.getType( ) );
                        _proposal.setIdQpvQva( resQpvQva.getId( ) );
                        _proposal.setLibelleQpvQva( resQpvQva.getLibelle( ) );
                    }
                    else
                        if ( StringUtils.isNotBlank( resQpvQva.getGpruNom( ) ) )
                        {
                            _proposal.setTypeQpvQva( IdeationApp.QPV_QVA_GPRU );
                            _proposal.setIdQpvQva( resQpvQva.getFid( ) );
                            _proposal.setLibelleQpvQva( resQpvQva.getGpruNom( ) );
                        }
                        else
                            if ( StringUtils.isNotBlank( resQpvQva.getExtBp( ) ) )
                            {
                                _proposal.setTypeQpvQva( IdeationApp.QPV_QVA_QBP );
                                _proposal.setIdQpvQva( resQpvQva.getFid( ) );
                                _proposal.setLibelleQpvQva( resQpvQva.getExtBp( ) );
                            }
                            else
                            {
                                _proposal.setTypeQpvQva( resQpvQva.getType( ) );
                                _proposal.setIdQpvQva( resQpvQva.getId( ) );
                                _proposal.setLibelleQpvQva( resQpvQva.getLibelle( ) );
                            }
                }
            }
            catch( Exception e )
            {
                _proposal.setTypeQpvQva( IdeationApp.QPV_QVA_ERR );
                _proposal.setIdQpvQva( null );
                _proposal.setLibelleQpvQva( null );
                AppLogService.error( "IDEATION : error when trying to identify QPV from address", e );
            }
        }
        else
        {
            _proposal.setAdress( null );
        }

        try
        {
            ProposalService.getInstance( ).createProposal( _proposal );
        }
        catch( IdeationErrorException e )
        {
            AppLogService.error( "IDEATION : error when trying to create proposal into DB from BO", e );
        }

        _proposal = null;
        addInfo( INFO_PROPOSAL_CREATED, getLocale( ) );

        return redirectView( request, VIEW_MANAGE_PROPOSALS );
    }

    // ***********************************************************************************
    // * MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MO *
    // * MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MODIFY MO *
    // ***********************************************************************************

    /**
     * Returns the form to update info about a proposal
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_MODIFY_PROPOSAL )
    public String getModifyProposal( HttpServletRequest request )
    {
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PROPOSAL ) );

        // Always reload proposal because the daemon may modify it.
        // We store the user input that needs to be saved in _proposalBoForm
        _proposal = ProposalHome.findByPrimaryKey( nId );

        if ( _proposalBoForm == null || _proposalBoForm.getId( ) != nId )
        {
            _proposalBoForm = new ProposalBoForm( );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_PROPOSAL, _proposal );
        model.put( MARK_PROPOSAL_BO_FORM, _proposalBoForm );

        if ( _proposal.getCodeCampaign( ) != null )
        {
            IdeationStaticService.getInstance( ).fillCampaignStaticContent( model, _proposal.getCodeCampaign( ) );
        }
        else
        {
            IdeationStaticService.getInstance( ).fillAllStaticContent( model );
        }

        if ( WorkflowService.getInstance( ).isAvailable( ) )
        {
            int idWorkflow = AppPropertiesService.getPropertyInt( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
            model.put( MARK_RESOURCE_HISTORY, WorkflowService.getInstance( ).getDisplayDocumentHistory( _proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE,
                    idWorkflow, request, getLocale( ) ) );
        }

        return getPage( PROPERTY_PAGE_TITLE_MODIFY_PROPOSAL, TEMPLATE_MODIFY_PROPOSAL, model );
    }

    /**
     * Process the change form of a proposal
     *
     * @param request
     *            The Http request
     * @return The Jsp URL of the process result
     */
    @Action( ACTION_MODIFY_PROPOSAL )
    public String doModifyProposal( HttpServletRequest request )
    {
        populate( _proposalBoForm, request );

        // Check constraints
        if ( !validateBean( _proposalBoForm, VALIDATION_ATTRIBUTES_PREFIX ) )
        {
            return redirect( request, VIEW_MODIFY_PROPOSAL, PARAMETER_ID_PROPOSAL, _proposal.getId( ) );
        }

        copyProposalBoFormToProposal( _proposalBoForm, _proposal );
        ProposalHome.updateBO( _proposal );
        addInfo( INFO_PROPOSAL_UPDATED, getLocale( ) );

        try
        {
            // There is a race condition here because this
            // proposal was loaded in the view, the daemon may have changed it
            // A workaround is to save it again if it is wrongly indexed.
            // It only affects this because the rest of the use of _proposal doesn't use
            // fields that are changed by the daemon, but the indexing does.
            _solrProposalIndexer.writeProposal( _proposal );
        }
        catch( Exception e )
        {
            addInfo( "The proposal modify is done, but Solr was unable to index the document due to exception '" + e.getClass( ).getSimpleName( ) + " : "
                    + e.getMessage( ) + "'." );
            AppLogService.error( "An error occured during SOLR indexation of proposal #" + _proposalBoForm.getId( ) + " '" + _proposalBoForm.getTitre( ) + "'.",
                    e );
        }

        return redirectView( request, VIEW_MANAGE_PROPOSALS );
    }

    // ***********************************************************************************
    // * REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE RE *
    // * REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE REMOVE RE *
    // ***********************************************************************************

    /**
     * 
     * @param request
     *            the HTTP request
     * @return the url view manage proposals
     * @throws AccessDeniedException
     *             the access denied exception
     */
    public String doRemoveProposal( HttpServletRequest request ) throws AccessDeniedException
    {
        int nIdProposal = Integer.parseInt( request.getParameter( PARAMETER_ID_PROPOSAL ) );

        Proposal proposal = ProposalHome.findByPrimaryKey( nIdProposal );
        ProposalService.getInstance( ).removeProposalByMdp( proposal );

        addInfo( INFO_PROPOSAL_REMOVED, getLocale( ) );

        return ( AppPathService.getBaseUrl( request ) + JSP_MANAGE_PROPOSALS );

    }

    /**
     * Returns the form to put the explanation for the deletion
     *
     * @param request
     *            The Http request
     * @return The HTML form to update info
     */
    @View( VIEW_CONFIRM_REMOVE_PROPOSAL )
    public String getConfirmRemoveProposalPage( HttpServletRequest request )
    {
        int nIdProposal = Integer.parseInt( request.getParameter( PARAMETER_ID_PROPOSAL ) );

        if ( ( _proposal == null ) || ( _proposal.getId( ) != nIdProposal ) )
        {
            _proposal = ProposalHome.findByPrimaryKey( nIdProposal );
        }

        if ( _proposal.getStatusIsRemoved( ) )
        {
            Map<String, Object> requestParameters = new HashMap<String, Object>( );
            requestParameters.put( PARAMETER_PLUGIN_NAME, "participatoryideation" );

            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PROPOSAL_REMOVED, JSP_MANAGE_PROPOSALS, AdminMessage.TYPE_ERROR,
                    requestParameters );
            return redirect( request, strMessageUrl );
        }

        Map<String, Object> model = getModel( );
        model.put( MARK_PROPOSAL, _proposal );

        return getPage( PROPERTY_PAGE_TITLE_CONFIRM_REMOVE_PROPOSAL, TEMPLATE_CONFIRM_REMOVE_PROPOSAL, model );
    }

    /**
     * 
     * @param request
     *            the HTTP request
     * @return the admin message to confirm removing proposal
     * @throws AccessDeniedException
     *             the access denied exception
     */
    @Action( ACTION_CONFIRM_REMOVE_PROPOSAL )
    public String getConfirmRemoveProposal( HttpServletRequest request ) throws AccessDeniedException
    {
        int nIdProposal = Integer.parseInt( request.getParameter( PARAMETER_ID_PROPOSAL ) );

        if ( ( _proposal == null ) || ( _proposal.getId( ) != nIdProposal ) )
        {
            _proposal = ProposalHome.findByPrimaryKey( nIdProposal );
        }

        if ( _proposal.getStatusIsRemoved( ) )
        {
            Map<String, Object> requestParameters = new HashMap<String, Object>( );
            requestParameters.put( PARAMETER_PLUGIN_NAME, "participatoryideation" );

            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PROPOSAL_REMOVED, JSP_MANAGE_PROPOSALS, AdminMessage.TYPE_ERROR,
                    requestParameters );
            return redirect( request, strMessageUrl );
        }

        String strMotifRecev = request.getParameter( PARAMETER_MOTIFRECEV );

        _proposal.setMotifRecev( strMotifRecev );
        ProposalHome.updateBO( _proposal );
        addInfo( INFO_PROPOSAL_UPDATED, getLocale( ) );

        return redirect( request, doRemoveProposal( request ) );
    }

    // *********************************************************************************************
    // * WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW *
    // * WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW WORKFLOW *
    // *********************************************************************************************

    /**
     * Process an workflow action.
     * 
     * @param request
     *            the HTTP request
     * @return Manage proposal admin page
     * @throws AccessDeniedException
     *             the access denied exception
     */
    @Action( ACTION_PROCESS_WORKFLOW_ACTION )
    public String doProcessWorkflowAction( HttpServletRequest request ) throws AccessDeniedException
    {
        int actionId;
        int resourceId;

        // Get action id and resource id from HTTP request

        String strActionId = request.getParameter( PARAMETER_ID_ACTION );
        String strResourceId = request.getParameter( PARAMETER_ID_RESOURCE );

        try
        {
            actionId = Integer.parseInt( strActionId );
        }
        catch( NumberFormatException e )
        {
            Object [ ] msgParams = {
                    strActionId
            };
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PROPOSAL_NO_SUCH_WORKFLOW_ACTION, msgParams, JSP_MANAGE_PROPOSALS,
                    AdminMessage.TYPE_ERROR );
            return redirect( request, strMessageUrl );
        }

        try
        {
            resourceId = Integer.parseInt( strResourceId );
        }
        catch( NumberFormatException e )
        {
            Object [ ] msgParams = {
                    strResourceId
            };
            String strMessageUrl = AdminMessageService.getMessageUrl( request, MESSAGE_ERROR_PROPOSAL_NO_SUCH_WORKFLOW_RESOURCE, msgParams,
                    JSP_MANAGE_PROPOSALS, AdminMessage.TYPE_ERROR );
            return redirect( request, strMessageUrl );
        }

        // Process workflow action

        WorkflowService.getInstance( ).doProcessAction( resourceId, Proposal.WORKFLOW_RESOURCE_TYPE, actionId, -1, request, request.getLocale( ), false );

        return redirectView( request, VIEW_MANAGE_PROPOSALS );
    }

    // ***********************************************************************************
    // * EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS E *
    // * EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS EXPORT-USERS E *
    // ***********************************************************************************

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
        int nId = Integer.parseInt( request.getParameter( PARAMETER_ID_PROPOSAL ) );

        if ( _proposal == null || ( _proposal.getId( ) != nId ) )
        {
            _proposal = ProposalHome.findByPrimaryKey( nId );
        }

        List<Integer> subIds = (List<Integer>) ProposalHome.getSubProposalsId( nId, ProposalHome.GetSubProposalsMethod.ALL_FAMILY );

        List<ArrayList<String>> valuesList = ProposalUsersService.getExportInfosList( subIds );
        try
        {
            // Generate CSV file
            String strFormatExtension = AppPropertiesService.getProperty( PROPERTY_CSV_EXTENSION );
            String strFileName = "proposal" + nId + "_" + AppPropertiesService.getProperty( PROPERTY_CSV_FILE_NAME ) + "." + strFormatExtension;
            ProposalExportUtils.addHeaderResponse( request, response, strFileName, strFormatExtension );

            OutputStream os = response.getOutputStream( );

            // say how to decode the csv file, with utf8
            byte [ ] bom = new byte [ ] {
                    (byte) 0xEF, (byte) 0xBB, (byte) 0xBF
            }; // BOM values
            os.write( bom ); // adds BOM
            CsvUtils.writeCsv( CsvUtils.PROPOSALUSERS_PREFIX_CSV, valuesList, os, getLocale( ) );

            os.flush( );
            os.close( );
        }
        catch( IOException e )
        {
            AppLogService.error( e );
        }
    }

    // ***********************************************************************************
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS U *
    // ***********************************************************************************

    private void copyProposalBoFormToProposal( ProposalBoForm proposalBoForm, Proposal proposal )
    {

        if ( StringUtils.isNotEmpty( proposalBoForm.getTitre( ) ) )
        {
            proposal.setTitre( proposalBoForm.getTitre( ) );
        }
        else
        {
            proposal.setTitre( null );
        }

        if ( StringUtils.isNotEmpty( proposalBoForm.getDescription( ) ) )
        {
            proposal.setDescription( proposalBoForm.getDescription( ) );
        }
        else
        {
            proposal.setDescription( null );
        }

        if ( StringUtils.isNotEmpty( proposalBoForm.getCout( ) ) )
        {
            proposal.setCout( Long.parseLong( proposalBoForm.getCout( ) ) );
        }
        else
        {
            proposal.setCout( null );
        }

        // Trying to determine LocationType using LocationArdt.
        proposal.setLocationType( StringUtils.isNotEmpty( proposalBoForm.getLocationArdt( ) ) ? Proposal.LOCATION_TYPE_ARDT : Proposal.LOCATION_TYPE_PARIS );

        if ( StringUtils.isNotEmpty( proposalBoForm.getLocationArdt( ) ) )
        {
            proposal.setLocationArdt( proposalBoForm.getLocationArdt( ) );
        }
        else
        {
            proposal.setLocationArdt( null );
        }

        proposal.setTypeQpvQva( proposalBoForm.getTypeQpvQva( ) );
        if ( IdeationApp.QPV_QVA_QPV.equals( proposalBoForm.getTypeQpvQva( ) ) || IdeationApp.QPV_QVA_QVA.equals( proposalBoForm.getTypeQpvQva( ) )
                || IdeationApp.QPV_QVA_GPRU.equals( proposalBoForm.getTypeQpvQva( ) ) || IdeationApp.QPV_QVA_QBP.equals( proposalBoForm.getTypeQpvQva( ) ) )
        {
            proposal.setIdQpvQva( proposalBoForm.getIdQpvQva( ) );
            proposal.setLibelleQpvQva( proposalBoForm.getLibelleQpvQva( ) );
        }
        else
        {
            proposal.setIdQpvQva( null );
            proposal.setLibelleQpvQva( null );
        }

        proposal.setHandicap( proposalBoForm.getHandicap( ) );

        if ( StringUtils.isNotEmpty( proposalBoForm.getIdProjet( ) ) )
        {
            proposal.setIdProjet( proposalBoForm.getIdProjet( ) );
        }
        else
        {
            proposal.setIdProjet( null );
        }

        if ( StringUtils.isNotEmpty( proposalBoForm.getTitreProjet( ) ) )
        {
            proposal.setTitreProjet( proposalBoForm.getTitreProjet( ) );
        }
        else
        {
            proposal.setTitreProjet( null );
        }

        if ( StringUtils.isNotEmpty( proposalBoForm.getUrlProjet( ) ) )
        {
            proposal.setUrlProjet( proposalBoForm.getUrlProjet( ) );
        }
        else
        {
            proposal.setUrlProjet( null );
        }

        if ( StringUtils.isNotEmpty( proposalBoForm.getWinnerProjet( ) ) )
        {
            proposal.setWinnerProjet( proposalBoForm.getWinnerProjet( ) );
        }
        else
        {
            proposal.setWinnerProjet( null );
        }
    }

    private boolean isProposalAddressValid( HttpServletRequest request )
    {
        boolean bIsValid = true;

        if ( _proposal.getLocationType( ).equals( Proposal.LOCATION_TYPE_ARDT ) && StringUtils.isEmpty( _proposal.getLocationArdt( ) ) )
        {
            addError( MESSAGE_ERROR_ARRONDISSEMENT_EMPTY, request.getLocale( ) );
            bIsValid = false;
        }

        if ( StringUtils.isNotBlank( _proposal.getAdress( ) ) )
        {
            if ( StringUtils.isEmpty( _proposal.getLocationType( ) ) )
            {
                addError( MESSAGE_ERROR_ADDRESS_LOCATION_TYPE_EMPTY, request.getLocale( ) );
                bIsValid = false;
            }

            // FIXME : This code is waiting for reintegration of geojson mecanism...
            // if ( StringUtils.isEmpty( _proposal.getGeoJson( ) ) )
            // {
            // addError( MESSAGE_ERROR_ADDRESS_NOT_VALID + " - Unable to identity GeoJSON for address '" + _proposal.getAdress( ) + "'", request.getLocale( ) );
            // bIsValid = false;
            // }

        }
        else
        {
            _proposal.setLatitude( null );
            _proposal.setLongitude( null );
        }

        if ( StringUtils.isNotEmpty( _proposal.getGeoJson( ) ) )
        {
            GeolocItem geolocItem = null;

            try
            {
                geolocItem = GeolocItem.fromJSON( _proposal.getGeoJson( ) );
                _proposal.setAdress( geolocItem.getAddress( ) );
                _proposal.setLatitude( geolocItem.getLat( ) );
                _proposal.setLongitude( geolocItem.getLon( ) );

                // FIXME : Trying to automaticly identify zip code from geojson

                // private static final java.util.regex.Pattern _patternAdresseArrondissement = java.util.regex.Pattern.compile( ", 75[0-1]([0-2][0-9]) PARIS"
                // );

                // Matcher m = _patternAdresseArrondissement.matcher( _proposal.getAdress( ) );
                // if ( m.find( ) ) {
                // int nArdt = Integer.parseInt( m.group( 1 ) );
                // String strArdt = ProposalService.getInstance( ).getArrondissementCode( nArdt );
                // if ( _proposal.getLocationType( ).equals( Proposal.LOCATION_TYPE_ARDT )
                // && StringUtils.isNotEmpty( _proposal.getLocationArdt( ) )
                // && ( !strArdt.equals( _proposal.getLocationArdt( ) ) ) )
                // {
                // addError( MESSAGE_ERROR_ADDRESS_ARDT_MISMATCH, request.getLocale( ) );
                // bIsValid = false;
                // }
                // else
                // {
                // _proposal.setLocationArdt( strArdt );
                // }
                // }

            }
            catch( Exception e )
            {
                addError( MESSAGE_ERROR_ADDRESS_FORMAT, getLocale( ) );
                AppLogService.error( "ProposalJspBean: malformed data from client: address = " + _proposal.getGeoJson( ) + "; exception " + e );
                bIsValid = false;
            }
        }
        return bIsValid;
    }

}
