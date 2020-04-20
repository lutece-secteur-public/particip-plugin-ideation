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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.participatoryideation.business.capgeo.QpvQva;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationErrorException;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.participatoryideation.service.IdeationUploadHandler;
import fr.paris.lutece.plugins.participatoryideation.service.ProposalService;
import fr.paris.lutece.plugins.participatoryideation.service.ProposalWSService;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignDataProvider;
import fr.paris.lutece.plugins.participatoryideation.service.capgeo.QpvQvaService;
import fr.paris.lutece.plugins.participatoryideation.service.myinfos.MyInfosService;
import fr.paris.lutece.plugins.participatoryideation.util.ParticipatoryIdeationConstants;
import fr.paris.lutece.plugins.participatoryideation.web.etape.FormEtapeApprox;
import fr.paris.lutece.plugins.participatoryideation.web.etape.FormEtapeDescription;
import fr.paris.lutece.plugins.participatoryideation.web.etape.FormEtapeLocation;
import fr.paris.lutece.plugins.participatoryideation.web.etape.FormEtapeRecap;
import fr.paris.lutece.plugins.participatoryideation.web.etape.FormEtapeTitle;
import fr.paris.lutece.plugins.participatoryideation.web.etape.FormEtapeUpload;
import fr.paris.lutece.plugins.participatoryideation.web.etape.IFormEtape;
import fr.paris.lutece.plugins.search.solr.business.SolrHighlights;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.business.SolrServerService;
import fr.paris.lutece.plugins.search.solr.indexer.SolrItem;
import fr.paris.lutece.plugins.search.solr.util.SolrUtil;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.filesystem.FileSystemUtil;

/**
 * This class provides a simple implementation of an XPage
 */

@Controller( xpageName = "ideation", pageTitleI18nKey = "participatoryideation.xpage.ideation.pageTitle", pagePathI18nKey = "participatoryideation.xpage.ideation.pagePathLabel" )
public class IdeationApp extends MVCApplication
{

    // *********************************************************************************************
    // * STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATI *
    // * STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATI *
    // *********************************************************************************************

    private static final long serialVersionUID = -3590277455677622883L;

    private static final String TEMPLATE_ETAPES = "/skin/plugins/participatoryideation/etapes.html";
    private static final String TEMPLATE_LOCATION = "/skin/plugins/participatoryideation/location.html";
    private static final String TEMPLATE_TITLE = "/skin/plugins/participatoryideation/title.html";
    private static final String TEMPLATE_APPROX = "/skin/plugins/participatoryideation/approx.html";
    private static final String TEMPLATE_DESCRIPTION = "/skin/plugins/participatoryideation/description.html";
    private static final String TEMPLATE_UPLOAD = "/skin/plugins/participatoryideation/upload.html";
    private static final String TEMPLATE_RECAP = "/skin/plugins/participatoryideation/recap.html";
    private static final String TEMPLATE_CONFIRMED = "/skin/plugins/participatoryideation/confirmed.html";

    // Jsp redirections
    private static final String JSP_PORTAL = "jsp/site/Portal.jsp";

    // Views/Actions
    private static final String STEP_LOCATION = "location";
    private static final String STEP_TITLE = "title";
    private static final String STEP_APPROX = "approx";
    private static final String STEP_DESCRIPTION = "description";
    private static final String STEP_UPLOAD = "upload";
    private static final String STEP_RECAP = "recap";
    private static final String STEP_CONFIRMED = "confirmed";
    private static final String ACTION_ABANDON = "abandon";

    // Parameters
    private static final String PARAMETER_APPROX_SOLR_DBG = "mdpdbg";
    private static final String PARAMETER_PAGE = "page";
    private static final String PARAMETER_CONF = "conf";
    private static final String PARAMETER_CAMPAIGN = "campaign";

    // Markers
    private static final String MARK_IDEATION_CAMPAIGN_DATA_PROVIDER_IMPLEMENTATION = "ideation_campaign_data_provider_implementation";

    private static final String MARK_APPROX_KEYWORD_LIST = "results_keyword_list";
    private static final String MARK_APPROX_PREVIOUS_CAMPAIGNS_LIST = "results_previous_campaigns_list";
    private static final String MARK_APPROX_LOCATION_LIST = "results_location_list";
    private static final String MARK_APPROX_KEYWORD_SOLR_DBG = "solr_keyword_query_dbg";
    private static final String MARK_APPROX_LOCATION_SOLR_DBG = "solr_location_query_dbg";

    private static final String MARK_STEPS_INDEX = "cur_etape_index";
    private static final String MARK_STEPS_CONTENT = "cur_etape_content";

    private static final String MARK_FORM_ETAPE_LOCATION = "form_etape_location";
    private static final String MARK_FORM_ETAPE_TITLE = "form_etape_title";
    private static final String MARK_FORM_ETAPE_APPROX = "form_etape_approx";
    private static final String MARK_FORM_ETAPE_DESCRIPTION = "form_etape_description";
    private static final String MARK_FORM_ETAPE_UPLOAD = "form_etape_upload";
    private static final String MARK_FORM_ETAPE_RECAP = "form_etape_recap";

    private static final String MARK_HANDLER = "handler";
    private static final String MARK_UPLOAD_DOCS = "docs";
    private static final String MARK_UPLOAD_IMGS = "imgs";

    private static final String MARK_RECAP_CODE_THEME = "recap_code_theme";
    private static final String MARK_RECAP_LOCATION_TYPE = "recap_location_type";
    private static final String MARK_RECAP_LOCATION_ARDT = "recap_location_ardt";
    private static final String MARK_RECAP_LOCATION_ADRESS = "recap_location_adress";
    private static final String MARK_RECAP_TITLE = "recap_title";
    private static final String MARK_RECAP_SUBMITTER_TYPE = "recap_submitter_type";
    private static final String MARK_RECAP_SUBMITTER = "recap_submitter";
    private static final String MARK_RECAP_DESCRIPTION = "recap_description";
    private static final String MARK_RECAP_COUT = "recap_cout";
    private static final String MARK_RECAP_DOCS = "recap_docs";
    private static final String MARK_RECAP_IMGS = "recap_imgs";
    private static final String MARK_RECAP_PROPOSAL_CREATED_CODE = "proposal_created_code";
    private static final String MARK_RECAP_PROPOSAL_CREATED_CAMPAIGN = "proposal_created_campaign";
    private static final String MARK_RECAP_PROPOSAL_CREATED_REFERENCE = "proposal_created_reference";

    private static final String MARK_CAMPAIGN_THEMES = "themes";
    private static final String MARK_CAMPAIGN_NUMBER_LOCALIZED_AREAS = "number_localized_areas";
    private static final String MARK_CAMPAIGN_LOCALIZED_AREAS = "localized_areas";
    private static final String MARK_CAMPAIGN_WHOLE_AREA = "whole_area";
    private static final String MARK_CAMPAIGN_SUBMITTER_TYPES = "submitter_types";

    public static final String QPV_QVA_QPV = "NQPV";
    public static final String QPV_QVA_QVA = "QVA";
    public static final String QPV_QVA_NO = "NON";
    public static final String QPV_QVA_ERR = "ERR";
    public static final String QPV_QVA_GPRU = "GPRU";
    public static final String QPV_QVA_QBP = "QBP";

    public static final String HANDICAP_LABEL_YES = "yes";
    public static final String HANDICAP_LABEL_NO = "no";

    private static final String PROPERTY_NEWPROJECTS_GEOLOC_FIELD = "participatoryideation.approx.newprojects.geoloc_field";
    private static final String PROPERTY_NEWPROJECTS_ARDT_FIELD = "participatoryideation.approx.newprojects.ardt_field";
    private static final String PROPERTY_OLDPROJECTS_ARDT_FIELD = "participatoryideation.approx.oldprojects.ardt_field";
    private static final String PROPERTY_NEWPROJECTS_TYPE = "participatoryideation.approx.newprojects.type";
    private static final String MESSAGE_CAMPAIGN_UNSPECIFIED = "participatoryideation.messages.campaign.unspecified";
    private static final String MESSAGE_CAMPAIGN_UNKNOWN = "participatoryideation.messages.campaign.unknown";
    private static final String MESSAGE_CAMPAIGN_IDEATION_CLOSED_SUBMIT = "participatoryideation.messages.campaign.ideation.closed.submit";

    private static final String SOLR_NEWPROJECTS_GEOLOC_FIELD = AppPropertiesService.getProperty( PROPERTY_NEWPROJECTS_GEOLOC_FIELD, "proposal_geoloc" );
    private static final String SOLR_NEWPROJECTS_ARDT_FIELD = AppPropertiesService.getProperty( PROPERTY_NEWPROJECTS_ARDT_FIELD, "location_ardt_text" );
    private static final String SOLR_OLDPROJECTS_ARDT_FIELD = AppPropertiesService.getProperty( PROPERTY_OLDPROJECTS_ARDT_FIELD, "location_text" );
    private static final String SOLR_NEWPROJECTS_TYPE = AppPropertiesService.getProperty( PROPERTY_NEWPROJECTS_TYPE, "proposal" );

    private static final String SOLR_PREVIOUS_CAMPAIGNS = "((type:proposal AND statut_publique_project_text:\"NONRETENU\") OR (type:\"PB Project\" AND statut_project_text:\"SUIVI\"))";

    private static final String DSKEY_APPROX_SCORE_RATIO_LIMIT = "participatoryideation.site_property.form.approx.scoreRatioLimit";
    private static final String DSKEY_APPROX_DISTANCE_LIMIT = "participatoryideation.site_property.form.approx.distanceLimit";
    private static final String DSKEY_APPROX_KEYWORD_RESULTS_COUNT = "participatoryideation.site_property.form.approx.keywordResultsCount";
    private static final String DSKEY_APPROX_LOCATION_RESULTS_COUNT = "participatoryideation.site_property.form.approx.locationResultsCount";
    private static final String DSKEY_APPROX_PREVIOUS_CAMPAIGNS_RESULTS_COUNT = "participatoryideation.site_property.form.approx.previousCampaignsResultsCount";

    // *********************************************************************************************
    // * ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES A *
    // * ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES ATTRIBUTES A *
    // *********************************************************************************************

    // To accumulate the results of the form. These represent user input.
    FormEtapeLocation _formEtapeLocation;
    FormEtapeTitle _formEtapeTitle;
    FormEtapeApprox _formEtapeApprox;
    FormEtapeDescription _formEtapeDescription;
    FormEtapeUpload _formEtapeUpload;
    FormEtapeRecap _formEtapeRecap;

    Proposal _proposalCreate; // The proposal we are building
    Proposal _proposalDisplay; // The last proposal submitted to show in recap, but avoid double submissions
    String _strNextStep; // The first non validated step of the form

    // *********************************************************************************************
    // * CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONST *
    // * CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONSTRUCTOR CONST *
    // *********************************************************************************************

    public IdeationApp( )
    {
        reInitFormSession( );
    }

    // *********************************************************************************************
    // * XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE *
    // * XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE XPAGE *
    // *********************************************************************************************

    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin ) throws SiteMessageException, UserNotSignedException
    {
        // Verify campaign is specified and open
        checkIdeationCampaignPhase( request );

        // If user's personal infos not filled, then redirect to the 'complete myinfos' webpage
        if ( !checkUserAuthorized( request ) )
        {
            return redirect( request, MyInfosService.getInstance( ).getUrlMyInfosFillAction( ) );
        }

        // Some automatic stuff
        if ( SecurityService.isAuthenticationEnable( ) && SecurityService.getInstance( ).getRegisteredUser( request ) != null )
        {
            _proposalCreate.setLuteceUserName( SecurityService.getInstance( ).getRegisteredUser( request ).getName( ) );
        }
        else
        {
            _proposalCreate.setLuteceUserName( "guid" );
        }

        String strAction = MVCUtils.getAction( request );
        String strView = MVCUtils.getView( request );

        if ( STEP_CONFIRMED.equals( strView ) )
        {
            // The confirmed step is special, you can view it if you have already completed the form at least once
            if ( _proposalDisplay == null )
            {
                return redirectView( request, _strNextStep );
            }
        }
        else
            if ( !ACTION_ABANDON.equals( strAction ) )
            {
                // View and actions from the steps
                if ( !( ( strView == null || allValidBeforeEtape( strView ) ) && ( strAction == null || allValidBeforeEtape( strAction ) ) ) )
                {
                    return redirectView( request, _strNextStep );
                }
            }

        return super.getPage( request, nMode, plugin );
    }

    // *********************************************************************************************
    // * STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP *
    // * STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP STEP *
    // *********************************************************************************************

    /**
     * Returns the content of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_LOCATION, defaultView = true )
    public XPage viewLocation( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( request );

        model.put( MARK_STEPS_INDEX, STEPS.LOCATION_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_LOCATION );

        model.put( MARK_CAMPAIGN_THEMES, IdeationCampaignDataProvider.getInstance( ).getCampaignThemes( _proposalCreate.getCodeCampaign( ) ) );

        model.put( MARK_CAMPAIGN_NUMBER_LOCALIZED_AREAS,
                IdeationCampaignDataProvider.getInstance( ).getCampaignNumberLocalizedAreas( _proposalCreate.getCodeCampaign( ) ) );
        model.put( MARK_CAMPAIGN_LOCALIZED_AREAS, IdeationCampaignDataProvider.getInstance( ).getCampaignLocalizedAreas( _proposalCreate.getCodeCampaign( ) ) );
        model.put( MARK_CAMPAIGN_WHOLE_AREA, IdeationCampaignDataProvider.getInstance( ).getCampaignWholeArea( _proposalCreate.getCodeCampaign( ) ) );

        model.put( MARK_CAMPAIGN_SUBMITTER_TYPES, IdeationCampaignDataProvider.getInstance( ).getCampaignSubmitterTypes( _proposalCreate.getCodeCampaign( ) ) );

        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = STEP_LOCATION )
    public XPage doLocation( HttpServletRequest request )
    {
        populate( _formEtapeLocation, request );
        if ( !isValidateFormEtape( request, _formEtapeLocation ) )
        {
            return redirectView( request, STEP_LOCATION );
        }

        convertFormEtapeLocation( _formEtapeLocation, _proposalCreate );
        _strNextStep = STEP_TITLE;
        return redirectView( request, STEP_TITLE );
    }

    /**
     * Returns the content of the title step.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_TITLE )
    public XPage viewTitle( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( request );
        model.put( MARK_STEPS_INDEX, STEPS.TITLE_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_TITLE );
        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = STEP_TITLE )
    public XPage doTitle( HttpServletRequest request )
    {
        populate( _formEtapeTitle, request );
        if ( !isValidateFormEtape( request, _formEtapeTitle ) )
        {
            return redirectView( request, STEP_TITLE );
        }

        convertFormEtapeTitle( _formEtapeTitle, _proposalCreate );
        _strNextStep = STEP_APPROX;
        return redirectView( request, STEP_APPROX );
    }

    /**
     * Returns the content of the approx step.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_APPROX )
    public XPage viewApprox( HttpServletRequest request )
    {
        SolrClient solrClient = SolrServerService.getInstance( ).getSolrServer( );
        List<SolrSearchResult> results_keyword = null;
        List<SolrSearchResult> results_location = null;
        List<SolrSearchResult> results_previous_campaigns = null;
        SolrQuery queryKeyword = new SolrQuery( );
        SolrQuery queryLocation = null;
        SolrQuery queryPreviousCampaigns = new SolrQuery( );
        if ( solrClient == null )
        {
            AppLogService.error( "IdeationApp, SolrServerService.getInstance(  ).getSolrServer(  ) returns null." );
        }
        else
        {
            HashSet<String> setProjects = new HashSet<String>( );

            results_previous_campaigns = getPreviousCampaignsApproxResults( queryPreviousCampaigns, solrClient );
            if ( results_previous_campaigns != null && results_previous_campaigns.size( ) > 0 )
            {
                for ( SolrSearchResult solrSearchResult : results_previous_campaigns )
                {
                    setProjects.add( solrSearchResult.getId( ) );
                }
            }

            results_keyword = getKeywordApproxResults( queryKeyword, solrClient );
            if ( results_keyword != null && results_keyword.size( ) > 0 )
            {
                for ( Iterator<SolrSearchResult> iterator = results_keyword.iterator( ); iterator.hasNext( ); )
                {
                    SolrSearchResult item = iterator.next( );
                    if ( setProjects.contains( item.getId( ) ) )
                    {
                        iterator.remove( );
                    }
                    else
                    {
                        setProjects.add( item.getId( ) );
                    }
                }
            }

            if ( StringUtils.isNotEmpty( _proposalCreate.getAdress( ) ) )
            {
                queryLocation = new SolrQuery( );
                results_location = getLocationApproxResults( queryLocation, solrClient );
                if ( results_location != null )
                {
                    for ( Iterator<SolrSearchResult> iterator = results_location.iterator( ); iterator.hasNext( ); )
                    {
                        SolrSearchResult item = iterator.next( );
                        if ( setProjects.contains( item.getId( ) ) )
                        {
                            iterator.remove( );
                        }
                    }
                }
            }

        }

        Map<String, Object> model = getModel( request );
        if ( request.getParameter( PARAMETER_APPROX_SOLR_DBG ) != null )
        {
            model.put( MARK_APPROX_KEYWORD_SOLR_DBG, queryKeyword );
            model.put( MARK_APPROX_LOCATION_SOLR_DBG, queryLocation );
        }
        model.put( MARK_APPROX_PREVIOUS_CAMPAIGNS_LIST, results_previous_campaigns );
        model.put( MARK_APPROX_KEYWORD_LIST, results_keyword );
        model.put( MARK_APPROX_LOCATION_LIST, results_location );
        model.put( MARK_STEPS_INDEX, STEPS.APPROX_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_APPROX );
        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    /**
     * set the common part of the location and keyword queries
     * 
     * @param query
     *            a query object to modify
     * @return The search results
     */
    private void setApproxProjectsQuery( SolrQuery query )
    {
        query.set( "defType", "edismax" );
        query.set( "q.op", "OR" );
    }

    /**
     * get the string to limit results around the current proposal, or null if there is no limit
     * 
     * @return the fq string
     */
    private String getDistanceFQ( )
    {
        String strApproxDistanceLimit = DatastoreService.getDataValue( DSKEY_APPROX_DISTANCE_LIMIT, "" );
        double distanceLimit = -1;
        if ( !"".equals( strApproxDistanceLimit ) )
        {
            try
            {
                distanceLimit = Integer.parseInt( strApproxDistanceLimit ) / 1000.0;
            }
            catch( NumberFormatException e )
            {
                AppLogService.error( "IdeationApp: error during approx search parsing DistanceLimit  " + e.getMessage( ), e );
            }
            if ( distanceLimit < 0 )
            {
                AppLogService.error( "IdeationApp: approx search negative DistanceLimit: parsed " + distanceLimit + " from " + strApproxDistanceLimit + ";" );
            }
        }
        if ( distanceLimit > 0 )
        {
            /*
             * String strGeofiltFq = "((type:" + SOLR_NEWPROJECTS_TYPE + " AND {!geofilt pt=" + _proposalCreate.getLatitude() + ","+
             * _proposalCreate.getLongitude() +" sfield="+SOLR_NEWPROJECTS_GEOLOC_FIELD+" d="+distanceLimit+"})" + " OR (type:"+ SOLR_OLDPROJECTS_TYPE +
             * " AND {!geofilt pt=" + _proposalCreate.getLatitude() + ","+ _proposalCreate.getLongitude() +" sfield="+
             * SOLR_OLDPROJECTS_GEOLOC_FIELD+" d="+distanceLimit+"}))";
             */
            String strGeofiltFq = "((type:" + SOLR_NEWPROJECTS_TYPE + " AND {!geofilt pt=" + _proposalCreate.getLatitude( ) + ","
                    + _proposalCreate.getLongitude( ) + " sfield=" + SOLR_NEWPROJECTS_GEOLOC_FIELD + " d=" + distanceLimit + "}))";

            return strGeofiltFq;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the results of a search by location and modifies the query
     * 
     * @param query
     *            a query object to modify
     * @param solrClient
     *            a solrClient
     * @return The search results
     */
    private List<SolrSearchResult> getLocationApproxResults( SolrQuery query, SolrClient solrClient )
    {
        List<SolrSearchResult> results = null;
        setApproxProjectsQuery( query );
        query.setQuery( _proposalCreate.getAdress( ) );
        String strLocationResultsCount = DatastoreService.getDataValue( DSKEY_APPROX_LOCATION_RESULTS_COUNT, "" );
        int nLocationResultsCount = 6;
        if ( !"".equals( strLocationResultsCount ) )
        {
            try
            {
                nLocationResultsCount = Integer.parseInt( strLocationResultsCount );
            }
            catch( NumberFormatException e )
            {
                AppLogService.error( "IdeationApp: error during approx search parsing LocationResultsCount  " + e.getMessage( ), e );
            }
        }
        query.setRows( nLocationResultsCount );

        ArrayList<String> listFQ = new ArrayList<String>( );
        // listFQ.add( SOLR_FQ_ALLPROJECTS );
        listFQ.add( "(type:proposal AND campaign_text:\"" + _proposalCreate.getCodeCampaign( ) + "\")" );
        String strGeofiltFq = getDistanceFQ( );
        if ( strGeofiltFq != null )
        {
            listFQ.add( strGeofiltFq );
        }
        query.setFilterQueries( listFQ.toArray( new String [ listFQ.size( )] ) );

        try
        {
            QueryResponse response = solrClient.query( query );
            List<SolrItem> itemList = response.getBeans( SolrItem.class );
            results = SolrUtil.transformSolrItemsToSolrSearchResults( itemList, null );
        }
        catch( SolrServerException e )
        {
            AppLogService.error( "IdeationApp: error during approx search; " + e.getMessage( ), e );
        }
        catch( IOException e )
        {
            AppLogService.error( "IdeationApp: error during approx search; " + e.getMessage( ), e );
        }
        return results;
    }

    /**
     * Returns the results of a search by keyword and modifies the query
     * 
     * @param query
     *            a query object to modify
     * @param solrClient
     *            a solrClient
     * @return The search results
     */
    private List<SolrSearchResult> getKeywordApproxResults( SolrQuery query, SolrClient solrClient )
    {
        List<SolrSearchResult> results = null;
        setApproxProjectsQuery( query );
        query.setQuery( _proposalCreate.getTitre( ) );
        String strKeywordResultsCount = DatastoreService.getDataValue( DSKEY_APPROX_KEYWORD_RESULTS_COUNT, "" );
        int nKeywordResultsCount = 6;
        if ( !"".equals( strKeywordResultsCount ) )
        {
            try
            {
                nKeywordResultsCount = Integer.parseInt( strKeywordResultsCount );
            }
            catch( NumberFormatException e )
            {
                AppLogService.error( "IdeationApp: error during approx search parsing KeywordResultsCount  " + e.getMessage( ), e );
            }
        }
        query.setRows( nKeywordResultsCount );

        Double [ ] dLatLon = null;
        ArrayList<String> listFQ = new ArrayList<String>( );
        // listFQ.add( SOLR_FQ_ALLPROJECTS );
        listFQ.add( "(type:proposal AND campaign_text:\"" + _proposalCreate.getCodeCampaign( ) + "\")" );
        if ( _proposalCreate.getLongitude( ) != null && _proposalCreate.getLatitude( ) != null )
        {
            dLatLon = new Double [ ] {
                    _proposalCreate.getLatitude( ), _proposalCreate.getLongitude( )
            };
            String strGeofiltFq = getDistanceFQ( );
            if ( strGeofiltFq != null )
            {
                listFQ.add( strGeofiltFq );
            }
        }
        if ( _proposalCreate.getLocationArdt( ) != null )
        {
            String strArdtFQ = "(" + SOLR_NEWPROJECTS_ARDT_FIELD + ":" + _proposalCreate.getLocationArdt( ) + " OR " + SOLR_OLDPROJECTS_ARDT_FIELD + ":\""
                    + getOldArdtText( _proposalCreate.getLocationArdt( ) ) + "\")";
            listFQ.add( strArdtFQ );
        }
        query.setFilterQueries( listFQ.toArray( new String [ listFQ.size( )] ) );
        String solrLatLon = null;
        if ( dLatLon != null )
        {
            solrLatLon = dLatLon [0] + "," + dLatLon [1];
        }
        if ( solrLatLon != null )
        {
            // We have values between 0 and ~10km because we are in paris
            // Using boost recip(X,0.25, 1, 1)
            // - means that for 0km, the score is multiplied by 1/(0.25*0+0.75) = 1.33
            // - means that for 1km, the score is multiplied by 1/(0.25*1+0.75) = 1.00
            // - means that for 10km, the score is multiplied by 1/(0.25*10+0.75) = 0.30
            // So a match that is further away needs to be a better match according to solr's "edismax" score
            // From empirical tests, this means that a 0km match can match on 3 times less words than a 10km match.
            // A match with a very close location (<1km) is better than a match without location which is in turn better
            // than a match with a far location (>1km)
            // Boost according to different fields depending on the type of the document..
            query.set( "boost", "product(if(termfreq(type,'proposal'), recip(geodist(proposal_geoloc," + solrLatLon
                    + "),0.25,1,0.75), 1),if(termfreq(type,'PB Project'), recip(geodist(location_precise_geoloc," + solrLatLon + "),0.25,1,0.75), 1))" );
        }
        query.setIncludeScore( true );

        query.setHighlight( true );

        try
        {
            QueryResponse response = solrClient.query( query );
            List<SolrItem> itemList = response.getBeans( SolrItem.class );
            if ( itemList.size( ) > 0 )
            {
                String strScoreRatioLimit = DatastoreService.getDataValue( DSKEY_APPROX_SCORE_RATIO_LIMIT, "" );
                double scoreRatioLimit = 0;
                if ( !"".equals( strScoreRatioLimit ) )
                {
                    try
                    {
                        scoreRatioLimit = Integer.parseInt( strScoreRatioLimit ) / 100.0;
                    }
                    catch( NumberFormatException e )
                    {
                        AppLogService.error( "IdeationApp: error during approx search parsing ScoreRatio  " + e.getMessage( ), e );
                    }
                    if ( scoreRatioLimit < 0 || scoreRatioLimit > 100 )
                    {
                        AppLogService.error( "IdeationApp: approx search scoreRatioLimit not between 0 and 100: parsed " + scoreRatioLimit + " from "
                                + strScoreRatioLimit + ";" );
                    }
                }

                if ( scoreRatioLimit > 0 && scoreRatioLimit <= 1 )
                {
                    SolrDocumentList listResults = response.getResults( );
                    double maxScore = (Float) listResults.getMaxScore( );
                    double filterScore = maxScore * scoreRatioLimit;
                    Iterator<SolrItem> mainIterator = itemList.iterator( );
                    Iterator<SolrDocument> scoreIterator = listResults.iterator( );
                    while ( mainIterator.hasNext( ) && scoreIterator.hasNext( ) )
                    {
                        mainIterator.next( );
                        SolrDocument document = scoreIterator.next( );
                        if ( ( (Float) document.getFieldValue( "score" ) ) < filterScore )
                        {
                            mainIterator.remove( );
                        }
                    }
                }
            }

            // HighLight
            SolrHighlights highlights = null;
            Map<String, Map<String, List<String>>> highlightsMap = response.getHighlighting( );
            if ( highlightsMap != null )
            {
                highlights = new SolrHighlights( highlightsMap );
            }

            results = SolrUtil.transformSolrItemsToSolrSearchResults( itemList, highlights );
        }
        catch( SolrServerException e )
        {
            AppLogService.error( "IdeationApp: error during approx search; " + e.getMessage( ), e );
        }
        catch( IOException e )
        {
            AppLogService.error( "IdeationApp: error during approx search; " + e.getMessage( ), e );
        }
        return results;
    }

    // These are in the old projects
    private String getOldArdtText( String strLocationArdt )
    {
        // 7500X -> "Xe arrondissement"
        // 750XX -> "XXe arrondissement"
        // return (Integer.parseInt(strLocationArdt) - 75000) + "e arrondissement" ;
        return strLocationArdt;
    }

    /**
     * Returns the results of a search by location and modifies the query
     * 
     * @param query
     *            a query object to modify
     * @param solrClient
     *            a solrClient
     * @return The search results
     */
    private List<SolrSearchResult> getPreviousCampaignsApproxResults( SolrQuery query, SolrClient solrClient )
    {
        List<SolrSearchResult> results = null;
        setApproxProjectsQuery( query );
        query.setQuery( _proposalCreate.getTitre( ) );
        String strPreviousCResultsCount = DatastoreService.getDataValue( DSKEY_APPROX_PREVIOUS_CAMPAIGNS_RESULTS_COUNT, "" );
        int nPreviousCResultsCount = 6;
        if ( !"".equals( strPreviousCResultsCount ) )
        {
            try
            {
                nPreviousCResultsCount = Integer.parseInt( strPreviousCResultsCount );
            }
            catch( NumberFormatException e )
            {
                AppLogService.error( "IdeationApp: error during approx search parsing LocationResultsCount  " + e.getMessage( ), e );
            }
        }

        query.setRows( nPreviousCResultsCount );

        query.setHighlight( true );

        ArrayList<String> listFQ = new ArrayList<String>( );
        listFQ.add( SOLR_PREVIOUS_CAMPAIGNS );

        query.setFilterQueries( listFQ.toArray( new String [ listFQ.size( )] ) );

        try
        {
            QueryResponse response = solrClient.query( query );
            List<SolrItem> itemList = response.getBeans( SolrItem.class );

            // HighLight
            SolrHighlights highlights = null;
            Map<String, Map<String, List<String>>> highlightsMap = response.getHighlighting( );
            if ( highlightsMap != null )
            {
                highlights = new SolrHighlights( highlightsMap );
            }

            results = SolrUtil.transformSolrItemsToSolrSearchResults( itemList, highlights );
        }
        catch( SolrServerException e )
        {
            AppLogService.error( "IdeationApp: error during approx search; " + e.getMessage( ), e );
        }
        catch( IOException e )
        {
            AppLogService.error( "IdeationApp: error during approx search; " + e.getMessage( ), e );
        }

        return results;
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = STEP_APPROX )
    public XPage doApprox( HttpServletRequest request )
    {
        _strNextStep = STEP_DESCRIPTION;
        return redirectView( request, STEP_DESCRIPTION );
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = ACTION_ABANDON )
    public XPage doAbandon( HttpServletRequest request )
    {
        reInitFormSession( request );
        return redirect( request, AppPathService.getBaseUrl( request ) );
    }

    /**
     * Returns the content of the description step.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_DESCRIPTION )
    public XPage viewDescription( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( request );
        model.put( MARK_STEPS_INDEX, STEPS.DESCRIPTION_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_DESCRIPTION );
        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = STEP_DESCRIPTION )
    public XPage doDescription( HttpServletRequest request )
    {
        populate( _formEtapeDescription, request );
        if ( !isValidateFormEtape( request, _formEtapeDescription ) )
        {

            return redirectView( request, STEP_DESCRIPTION );
        }

        convertFormEtapeDescription( _formEtapeDescription, _proposalCreate );
        _strNextStep = STEP_UPLOAD;
        return redirectView( request, STEP_UPLOAD );
    }

    /**
     * Returns the content of the upload step.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_UPLOAD )
    public XPage viewUpload( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( request );
        model.put( MARK_STEPS_INDEX, STEPS.UPLOAD_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_UPLOAD );
        model.put( MARK_HANDLER, SpringContextService.getBean( IdeationUploadHandler.BEAN_NAME ) );
        model.put( MARK_UPLOAD_DOCS, _formEtapeUpload.getDocs( request ) );
        model.put( MARK_UPLOAD_IMGS, _formEtapeUpload.getImgs( request ) );
        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = STEP_UPLOAD )
    public XPage doUpload( HttpServletRequest request )
    {
        // XXX handle synchronous upload
        populate( _formEtapeUpload, request );
        String strAccepExploit = request.getParameter( "accept_exploit" );
        if ( strAccepExploit != null && strAccepExploit.equals( "true" ) )
        {
            _formEtapeUpload.setAcceptExploit( strAccepExploit );
            _proposalCreate.setAcceptExploit( true );
        }
        else
        {
            _formEtapeUpload.setAcceptExploit( "false" );
            _proposalCreate.setAcceptExploit( false );
        }
        boolean hadSynchronousAction = _formEtapeUpload.populateSynchronousUpload( request );
        if ( !isValidateFormEtape( request, _formEtapeUpload ) || hadSynchronousAction )
        {

            return redirectView( request, STEP_UPLOAD );
        }

        convertFormEtapeUpload( request, _formEtapeUpload, _proposalCreate );
        _strNextStep = STEP_RECAP;

        return redirectView( request, STEP_RECAP );
    }

    /**
     * Returns the content of the recap step.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_RECAP )
    public XPage viewRecap( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( request );
        model.put( MARK_STEPS_INDEX, STEPS.RECAP_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_RECAP );
        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    /**
     * Process the form of the page ideation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @Action( value = STEP_RECAP )
    public XPage doRecap( HttpServletRequest request )
    {

        populate( _formEtapeRecap, request );
        String strAccepContact = request.getParameter( "accept_contact" );
        if ( strAccepContact != null && strAccepContact.equals( "true" ) )
        {
            _formEtapeRecap.setAcceptContact( "true" );
            _proposalCreate.setAcceptContact( true );
        }
        else
        {
            _formEtapeRecap.setAcceptContact( "false" );
            _proposalCreate.setAcceptContact( false );
        }
        if ( !isValidateFormEtape( request, _formEtapeRecap ) )
        {
            return redirectView( request, STEP_RECAP );
        }

        try
        {
            _proposalCreate.setCreationTimestamp( new java.sql.Timestamp( ( new java.util.Date( ) ).getTime( ) ) );
            _proposalCreate.setStatusPublic( Proposal.Status.STATUS_SUBMITTED );

            ProposalService.getInstance( ).createProposal( _proposalCreate );
            createWorkflowResource( _proposalCreate, request );
            // Clear the blobs for performance
            clearBlobs( _proposalCreate );
            reInitFormSession( request );
        }
        catch( IdeationErrorException e )
        {
            AppLogService.error( e );
        }

        return redirectView( request, STEP_CONFIRMED );
    }

    /**
     * Returns the content of the confirmation
     * 
     * @param request
     *            The HTTP request
     * @return The view
     */
    @View( value = STEP_CONFIRMED )
    public XPage viewConfirmed( HttpServletRequest request )
    {
        Map<String, Object> model = getModel( request, true );
        model.put( MARK_STEPS_INDEX, STEPS.CONFIRMED_INDEX.ordinal( ) );
        model.put( MARK_STEPS_CONTENT, TEMPLATE_CONFIRMED );
        return getXPage( TEMPLATE_ETAPES, request.getLocale( ), model );
    }

    // *********************************************************************************************
    // * FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM *
    // * FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM *
    // *********************************************************************************************

    /**
     * Process the form of the page ideation. Can't have this in the service because it needs to be done after the transaction because the task uses the
     * proposal from the database
     * 
     * @param proposal
     *            The proposal
     * @param request
     *            The HTTP request
     */
    private void createWorkflowResource( Proposal proposal, HttpServletRequest request )
    {

        int idWorkflow = AppPropertiesService.getPropertyInt( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
        String strWorkflowActionNameCreateProposal = AppPropertiesService
                .getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_PROPOSAL );

        if ( idWorkflow != -1 )
        {
            try
            {
                // Initialize the workflow, this creates the state for our resource
                WorkflowService.getInstance( ).getState( proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, idWorkflow, -1 );
                ProposalWSService.getInstance( ).processActionByName( strWorkflowActionNameCreateProposal, proposal.getId( ) );

            }
            catch( Exception e )
            {
                AppLogService.error( "Ideation: error in proposal creation workflow", e );
            }
        }
        else
        {
            AppLogService.error( "Ideation: app property idWorkflow not set" );
        }
    }

    private void clearBlobs( Proposal proposal )
    {
        for ( File f : proposal.getImgs( ) )
        {
            f.getPhysicalFile( ).setValue( null );
        }
        for ( File f : proposal.getDocs( ) )
        {
            f.getPhysicalFile( ).setValue( null );
        }
    }

    /**
     * Get a model Object filled with default values
     * 
     * @param request
     *            The HTTP request
     * @return The model
     */
    protected Map<String, Object> getModel( HttpServletRequest request )
    {
        return getModel( request, false );
    }

    /**
     * Get a model Object filled with default values
     * 
     * @param isConfirmed
     *            true if the parameter for the recap come from the last submitted proposal
     * @param request
     *            The HTTP request
     * @return The model
     */
    protected Map<String, Object> getModel( HttpServletRequest request, boolean isConfirmed )
    {
        Map<String, Object> model = super.getModel( );

        model.put( MARK_IDEATION_CAMPAIGN_DATA_PROVIDER_IMPLEMENTATION, IdeationCampaignDataProvider.getInstance( ).getClass( ).getName( ) );

        IdeationStaticService.getInstance( ).fillCampaignStaticContent( model, _proposalCreate.getCodeCampaign( ) );
        fillFormEtapes( model );
        fillRecap( model, isConfirmed ? _proposalDisplay : _proposalCreate, request );

        return model;
    }

    /**
     * Fill the model with commons objects used in templates from the proposal, or from the FormEtapes
     * 
     * @param proposal
     *            the proposal to use to fill the recap
     * @param request
     *            The HTTP request
     * @param model
     *            The model
     */
    protected void fillRecap( Map<String, Object> model, Proposal proposal, HttpServletRequest request )
    {
        // Step 1
        model.put( MARK_RECAP_SUBMITTER_TYPE, proposal.getSubmitterType( ) );
        model.put( MARK_RECAP_SUBMITTER, proposal.getSubmitter( ) );
        model.put( MARK_RECAP_CODE_THEME, proposal.getCodeTheme( ) );
        model.put( MARK_RECAP_LOCATION_TYPE, proposal.getLocationType( ) );
        model.put( MARK_RECAP_LOCATION_ARDT, proposal.getLocationArdt( ) );
        model.put( MARK_RECAP_LOCATION_ADRESS, proposal.getAdress( ) );

        // Step 2
        model.put( MARK_RECAP_TITLE, proposal.getTitre( ) );

        // step 4
        model.put( MARK_RECAP_DESCRIPTION, proposal.getDescription( ) );
        model.put( MARK_RECAP_COUT, proposal.getCout( ) );

        // step 5
        model.put( MARK_RECAP_DOCS, proposal.getDocs( ) );
        model.put( MARK_RECAP_IMGS, proposal.getImgs( ) );

        // step 6
        model.put( MARK_RECAP_PROPOSAL_CREATED_CODE, proposal.getCodeProposal( ) );
        model.put( MARK_RECAP_PROPOSAL_CREATED_CAMPAIGN, proposal.getCodeCampaign( ) );
        model.put( MARK_RECAP_PROPOSAL_CREATED_REFERENCE, proposal.getReference( ) );
    }

    protected void fillFormEtapes( Map<String, Object> model )
    {
        model.put( MARK_FORM_ETAPE_LOCATION, _formEtapeLocation );
        model.put( MARK_FORM_ETAPE_TITLE, _formEtapeTitle );
        model.put( MARK_FORM_ETAPE_APPROX, _formEtapeApprox );
        model.put( MARK_FORM_ETAPE_DESCRIPTION, _formEtapeDescription );
        model.put( MARK_FORM_ETAPE_UPLOAD, _formEtapeUpload );
        model.put( MARK_FORM_ETAPE_RECAP, _formEtapeRecap );
    }

    /**
     * Returns an exception when it is not the ideation phase
     * 
     * @param request
     *            The HTTP request
     * @throws SiteMessageException
     */
    private void checkIdeationCampaignPhase( HttpServletRequest request ) throws SiteMessageException
    {
        // Verify a campaign is specified
        if ( StringUtils.isBlank( _proposalCreate.getCodeCampaign( ) ) )
        {
            _proposalCreate.setCodeCampaign( request.getParameter( PARAMETER_CAMPAIGN ) );
        }

        if ( StringUtils.isBlank( _proposalCreate.getCodeCampaign( ) ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_CAMPAIGN_UNSPECIFIED, SiteMessage.TYPE_ERROR, JSP_PORTAL );
        }
        else
        {
        	// Verify the campaign exists
            if ( IdeationCampaignDataProvider.getInstance().getCampaigns().stream().filter( i -> i.getCode().equals( _proposalCreate.getCodeCampaign( ) ) ).count() < 1 )
            {
                SiteMessageService.setMessage( request, MESSAGE_CAMPAIGN_UNKNOWN, new String[] { _proposalCreate.getCodeCampaign( ) }, SiteMessage.TYPE_ERROR );
            }
        	
            if ( !IdeationCampaignDataProvider.getInstance( ).isDuring( _proposalCreate.getCodeCampaign( ), ParticipatoryIdeationConstants.IDEATION ) )
            {
                Map<String, Object> requestParameters = new HashMap<String, Object>( );
                requestParameters.put( PARAMETER_PAGE, "search-solr" );
                requestParameters.put( PARAMETER_CONF, "list_proposals" );
                SiteMessageService.setMessage( request, MESSAGE_CAMPAIGN_IDEATION_CLOSED_SUBMIT, SiteMessage.TYPE_ERROR, JSP_PORTAL, requestParameters );
            }
        }
    }

    private boolean allValidBeforeEtape( String strEtape )
    {
        return STEPS.getByName( strEtape ) != null && STEPS.getByName( strEtape ).ordinal( ) <= STEPS.getByName( _strNextStep ).ordinal( );
    }

    private boolean checkUserAuthorized( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = null;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRemoteUser( request );
            if ( user == null )
            {
                throw new UserNotSignedException( );
            }
            return MyInfosService.getInstance( ).isUserValid( user.getName( ) );
        }
        return false;

    }

    boolean isValidateFormEtape( HttpServletRequest request, IFormEtape formEtape )
    {

        if ( validateBean( formEtape ) )
        {

            List<String> listErrors = formEtape.checkValidationErrors( request );
            List<String> listErrorsLocalized = formEtape.checkValidationErrorsLocalized( request, getLocale( request ) );

            if ( !CollectionUtils.isEmpty( listErrors ) )
            {
                for ( String error : listErrors )
                {
                    addError( error, getLocale( request ) );

                }
            }
            if ( !CollectionUtils.isEmpty( listErrorsLocalized ) )
            {
                for ( String error : listErrorsLocalized )
                {
                    addError( error );
                }
            }
            if ( !CollectionUtils.isEmpty( listErrors ) || !CollectionUtils.isEmpty( listErrorsLocalized ) )
            {
                return false;
            }
            // Validate formEtape
            formEtape.setValidated( true );
            return true;
        }

        return false;
    }

    boolean isAllEtapeValidated( )
    {
        return _formEtapeLocation.isValidated( ) && _formEtapeTitle.isValidated( ) && _formEtapeDescription.isValidated( ) && _formEtapeUpload.isValidated( )
                && _formEtapeRecap.isValidated( );

    }

    private void convertFormEtapeLocation( FormEtapeLocation formEtapeLocation, Proposal proposal )
    {
        proposal.setLocationType( formEtapeLocation.getLocationType( ) );
        proposal.setCodeTheme( formEtapeLocation.getCodeTheme( ) );
        proposal.setSubmitterType( formEtapeLocation.getSubmitterType( ) );
        if ( formEtapeLocation.mustCopySubmitter( ) )
        {
            proposal.setSubmitter( formEtapeLocation.getSubmitter( ).trim( ) );
        }
        else
        {
            proposal.setSubmitter( null );
        }
        if ( StringUtils.isNotEmpty( formEtapeLocation.getGeojson( ) ) )
        {
            GeolocItem geolocItem;
            try
            {
                geolocItem = GeolocItem.fromJSON( formEtapeLocation.getGeojson( ) );
                proposal.setLatitude( geolocItem.getLat( ) );
                proposal.setLongitude( geolocItem.getLon( ) );
                proposal.setAdress( geolocItem.getAddress( ) );
                proposal.setLocationArdt( formEtapeLocation.getLocationArdt( ) );
                List<QpvQva> listQpvqva;
                try
                {
                    listQpvqva = QpvQvaService.getQpvQva( proposal.getLongitude( ), proposal.getLatitude( ) );
                    if ( listQpvqva.size( ) == 0 )
                    {
                        proposal.setTypeQpvQva( QPV_QVA_NO );
                        proposal.setIdQpvQva( null );
                        proposal.setLibelleQpvQva( null );
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
                            proposal.setTypeQpvQva( resQpvQva.getType( ) );
                            proposal.setIdQpvQva( resQpvQva.getId( ) );
                            proposal.setLibelleQpvQva( resQpvQva.getLibelle( ) );
                        }
                        else
                            if ( StringUtils.isNotBlank( resQpvQva.getGpruNom( ) ) )
                            {
                                proposal.setTypeQpvQva( QPV_QVA_GPRU );
                                proposal.setIdQpvQva( resQpvQva.getFid( ) );
                                proposal.setLibelleQpvQva( resQpvQva.getGpruNom( ) );
                            }
                            else
                                if ( StringUtils.isNotBlank( resQpvQva.getExtBp( ) ) )
                                {
                                    proposal.setTypeQpvQva( QPV_QVA_QBP );
                                    proposal.setIdQpvQva( resQpvQva.getFid( ) );
                                    proposal.setLibelleQpvQva( resQpvQva.getExtBp( ) );
                                }
                                else
                                {
                                    proposal.setTypeQpvQva( resQpvQva.getType( ) );
                                    proposal.setIdQpvQva( resQpvQva.getId( ) );
                                    proposal.setLibelleQpvQva( resQpvQva.getLibelle( ) );
                                }
                    }
                }
                catch( Exception e )
                {
                    proposal.setTypeQpvQva( QPV_QVA_ERR );
                    proposal.setIdQpvQva( null );
                    proposal.setLibelleQpvQva( null );
                    AppLogService.error( "IDEATION: error when using capgeo rest QpvQva service", e );
                }

            }
            catch( IOException e )
            {
                proposal.setTypeQpvQva( QPV_QVA_NO );
                proposal.setIdQpvQva( null );
                proposal.setLibelleQpvQva( null );
                proposal.setLatitude( null );
                proposal.setLongitude( null );
                proposal.setAdress( null );
                proposal.setLocationArdt( null );
                AppLogService.error( e );
            }
        }
        else
        {
            proposal.setTypeQpvQva( QPV_QVA_NO );
            proposal.setIdQpvQva( null );
            proposal.setLibelleQpvQva( null );
            proposal.setLatitude( null );
            proposal.setLongitude( null );
            proposal.setAdress( null );
            if ( formEtapeLocation.getLocationType( ).equals( Proposal.LOCATION_TYPE_ARDT ) && formEtapeLocation.getLocationArdt( ) != null )
            {
                proposal.setLocationArdt( formEtapeLocation.getLocationArdt( ) );
            }
            else
            {
                proposal.setLocationArdt( null );
            }
        }
    }

    private void convertFormEtapeTitle( FormEtapeTitle formEtapeTitle, Proposal proposal )
    {
        proposal.setTitre( formEtapeTitle.getTitre( ).trim( ) );
        proposal.setField1( formEtapeTitle.getField1( ).trim( ) );
        proposal.setfield2( formEtapeTitle.getfield2( ).trim( ) );
    }

    private void convertFormEtapeDescription( FormEtapeDescription formEtapeDescription, Proposal proposal )
    {
        proposal.setDescription( formEtapeDescription.getDescription( ).trim( ) );
        if ( StringUtils.isNotBlank( formEtapeDescription.getCout( ) ) )
        {
            proposal.setCout( Long.parseLong( formEtapeDescription.getCout( ).replaceAll( "\\s+", "" ) ) );
        }
        else
        {
            proposal.setCout( null );
        }
        proposal.setHandicap( formEtapeDescription.getHandicap( ).trim( ) );
        proposal.setHandicapComplement( formEtapeDescription.getHandicapComplement( ).trim( ) );
        proposal.setField3( formEtapeDescription.getField3( ).trim( ) );
    }

    private void convertFormEtapeUpload( HttpServletRequest request, FormEtapeUpload formEtapeUpload, Proposal proposal )
    {
        proposal.setImgs( convertAllFileItemsToFiles( formEtapeUpload.getImgs( request ) ) );
        proposal.setDocs( convertAllFileItemsToFiles( formEtapeUpload.getDocs( request ) ) );
    }

    private void reInitFormSession( )
    {
        reInitFormSession( null );
    }

    private void reInitFormSession( HttpServletRequest request )
    {
        _formEtapeLocation = new FormEtapeLocation( );
        _formEtapeTitle = new FormEtapeTitle( );
        _formEtapeApprox = new FormEtapeApprox( );
        _formEtapeDescription = new FormEtapeDescription( );

        if ( _formEtapeUpload != null && request != null )
        {
            _formEtapeUpload.reInitFormSession( request );
        }

        _formEtapeUpload = new FormEtapeUpload( );
        _formEtapeRecap = new FormEtapeRecap( );

        _proposalDisplay = _proposalCreate;
        _proposalCreate = new Proposal( );

        _strNextStep = STEP_LOCATION;
    }

    private List<File> convertAllFileItemsToFiles( List<FileItem> listFileItems )
    {
        ArrayList<File> listFiles = new ArrayList<File>( listFileItems.size( ) );
        for ( FileItem fileItem : listFileItems )
        {
            File file = new File( );
            file.setTitle( fileItem.getName( ) );
            file.setSize( ( fileItem.getSize( ) < Integer.MAX_VALUE ) ? (int) fileItem.getSize( ) : Integer.MAX_VALUE );
            file.setMimeType( FileSystemUtil.getMIMEType( file.getTitle( ) ) );

            PhysicalFile physicalFile = new PhysicalFile( );
            physicalFile.setValue( fileItem.get( ) );
            file.setPhysicalFile( physicalFile );
            listFiles.add( file );
        }

        return listFiles;
    }

    /**
     * Steps Order
     */
    enum STEPS
    {
        LOCATION_INDEX( STEP_LOCATION ), TITLE_INDEX( STEP_TITLE ), APPROX_INDEX( STEP_APPROX ), DESCRIPTION_INDEX( STEP_DESCRIPTION ), UPLOAD_INDEX(
                STEP_UPLOAD ), RECAP_INDEX( STEP_RECAP ), CONFIRMED_INDEX( STEP_CONFIRMED );

        String _strEtape;
        private static final Map<String, STEPS> nameMap;
        static
        {
            nameMap = new HashMap<String, STEPS>( );
            for ( STEPS s : STEPS.values( ) )
            {
                nameMap.put( s._strEtape, s );
            }
        }

        private STEPS( String strEtape )
        {
            _strEtape = strEtape;
        }

        public static STEPS getByName( String strEtape )
        {
            return nameMap.get( strEtape );
        }
    }

}
