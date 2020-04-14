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
package fr.paris.lutece.plugins.participatoryideation.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalSearcher;
import fr.paris.lutece.plugins.participatoryideation.web.IdeationApp;
import fr.paris.lutece.plugins.search.solr.business.SolrServerService;
import fr.paris.lutece.plugins.search.solr.business.field.Field;
import fr.paris.lutece.plugins.search.solr.indexer.SolrIndexer;
import fr.paris.lutece.plugins.search.solr.indexer.SolrIndexerService;
import fr.paris.lutece.plugins.search.solr.indexer.SolrItem;
import fr.paris.lutece.plugins.search.solr.util.SolrConstants;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.url.UrlItem;

public class SolrProposalIndexer implements SolrIndexer
{

    private static final String SHORT_NAME = "proposal";
    private static final String PARAMETER_XPAGE = "page";
    private static final String XPAGE_PROPOSAL = "proposal";
    private static final String PARAMETER_CODE_CAMPAIGN = "campaign";
    private static final String PARAMETER_CODE_PROPOSAL = "proposal";

    @Inject
    private IRatingService _ratingService;

    public void writeProposal( Proposal proposal )
    {
        try
        {
            write( getItem( proposal ) );
        }
        catch( Exception e )
        {
            AppLogService.error( "[SolrProposalIndexer] Error during write (proposal " + proposal.getReference( ) + ") " + e.getMessage( ), e );
        }
    }

    public void removeProposal( Proposal proposal )
    {

        try
        {
            SolrClient SOLR_SERVER = SolrServerService.getInstance( ).getSolrServer( );
            SOLR_SERVER.deleteByQuery(
                    SearchItem.FIELD_UID + ":" + SolrIndexerService.getWebAppName( ) + "_" + getResourceUid( Integer.toString( proposal.getId( ) ), null ) );
            SOLR_SERVER.commit( );

        }
        catch( Exception e )
        {
            AppLogService.error( "[SolrProposalIndexer] Error during remove (proposal " + proposal.getReference( ) + ") " + e.getMessage( ), e );
        }
    }

    public SolrItem getItem( Proposal proposal ) throws IOException
    {
        // the item
        SolrItem item = new SolrItem( );
        item.setUid( getResourceUid( Integer.toString( proposal.getId( ) ), null ) );
        item.setDate( proposal.getCreationTimestamp( ) );
        item.setType( "proposal" );
        item.setSummary( proposal.getDescription( ) );
        item.setTitle( proposal.getTitre( ) );
        item.setSite( SolrIndexerService.getWebAppName( ) );
        item.setRole( "none" );

        String strCodeGeoloc;
        double dLongitude = 0;
        double dLatitude = 0;

        if ( proposal.getAdress( ) != null && proposal.getLongitude( ) != null && proposal.getLatitude( ) != null )
        {
            dLongitude = proposal.getLongitude( );
            dLatitude = proposal.getLatitude( );
            if ( Proposal.LOCATION_TYPE_ARDT.equals( proposal.getLocationType( ) ) )
            {
                strCodeGeoloc = "proposal_geoloc-ardt-" + proposal.getLocationArdt( );
            }
            else
            {
                strCodeGeoloc = "proposal_geoloc-paris";
            }
        }
        else
        {
            if ( Proposal.LOCATION_TYPE_ARDT.equals( proposal.getLocationType( ) ) )
            {
                strCodeGeoloc = "proposal_ardt-" + proposal.getLocationArdt( );
            }
            else
            {
                strCodeGeoloc = "proposal_paris";
            }
        }
        item.addDynamicFieldGeoloc( "proposal", proposal.getAdress( ), dLongitude, dLatitude, strCodeGeoloc );
        item.addDynamicField( "proposal_status", String.valueOf( proposal.getStatusPublic( ).isPublished( ) ) );
        item.addDynamicFieldNotAnalysed( "status", String.valueOf( proposal.getStatusPublic( ).getValeur( ) ) );
        item.addDynamicFieldNotAnalysed( "code_theme", proposal.getCodeTheme( ) );
        item.addDynamicFieldNotAnalysed( "code_submitter_type", proposal.getSubmitterType( ) );
        item.addDynamicField( "campaign", proposal.getCodeCampaign( ) );
        item.addDynamicField( "code_projet", (long) proposal.getCodeProposal( ) );
        item.addDynamicField( "location", ( ( proposal.getAdress( ) != null ) && ( !"".equals( proposal.getAdress( ).trim( ) ) ) ) ? proposal.getAdress( )
                : ( Proposal.LOCATION_TYPE_ARDT.equals( proposal.getLocationType( ).trim( ) ) ? proposal.getLocationArdt( ) : "whole city" // TODO :
                                                                                                                                           // Must
                                                                                                                                           // get
                // this string from
                // campaign area
                // service
                ) );
        item.addDynamicFieldNotAnalysed( "location_type", proposal.getLocationType( ) );

        if ( Proposal.LOCATION_TYPE_ARDT.equals( proposal.getLocationType( ).trim( ) ) )
        {
            item.addDynamicField( "location_ardt", proposal.getLocationArdt( ) );
        }

        item.addDynamicField( "budget", proposal.getCout( ) );

        item.addDynamicFieldNotAnalysed( "type_qpvqva", proposal.getTypeQpvQva( ) );
        if ( IdeationApp.QPV_QVA_QPV.equals( proposal.getTypeQpvQva( ) ) || IdeationApp.QPV_QVA_QVA.equals( proposal.getTypeQpvQva( ) )
                || IdeationApp.QPV_QVA_GPRU.equals( proposal.getTypeQpvQva( ) ) || IdeationApp.QPV_QVA_QBP.equals( proposal.getTypeQpvQva( ) ) )
        {
            item.addDynamicField( "libelle_qpvqva", proposal.getLibelleQpvQva( ) );
        }

        item.addDynamicFieldNotAnalysed( "url_projet", proposal.getUrlProjet( ) );
        item.addDynamicFieldNotAnalysed( "winner_projet", proposal.getWinnerProjet( ) );

        item.addDynamicFieldNotAnalysed( "handicap", proposal.getHandicap( ) );

        if ( proposal.getStatusPublic( ).getValeur( ) != null )
        {
            item.addDynamicField( "statut_publique_project", proposal.getStatusPublic( ).getValeur( ) );
        }

        Rating rating = _ratingService.findByResource( String.valueOf( proposal.getId( ) ), Proposal.PROPERTY_RESOURCE_TYPE );

        if ( rating != null )
        {
            item.addDynamicField( "like", (long) rating.getScorePositifsVotes( ) );
            item.addDynamicField( "dislike", (long) rating.getScoreNegativesVotes( ) );
        }
        else
        {
            item.addDynamicField( "like", 0L );
            item.addDynamicField( "dislike", 0L );
        }

        item.setXmlContent( "" );
        UrlItem url = new UrlItem( SolrIndexerService.getBaseUrl( ) );
        url.addParameter( PARAMETER_XPAGE, XPAGE_PROPOSAL );
        url.addParameter( PARAMETER_CODE_CAMPAIGN, proposal.getCodeCampaign( ) );
        url.addParameter( PARAMETER_CODE_PROPOSAL, proposal.getCodeProposal( ) );

        // Date Hierarchy
        GregorianCalendar calendar = new GregorianCalendar( );
        calendar.setTime( proposal.getCreationTimestamp( ) );
        item.setHieDate( calendar.get( GregorianCalendar.YEAR ) + "/" + ( calendar.get( GregorianCalendar.MONTH ) + 1 ) + "/"
                + calendar.get( GregorianCalendar.DAY_OF_MONTH ) + "/" );

        List<String> listCategorie = new ArrayList<String>( );
        item.setCategorie( listCategorie );
        item.setUrl( url.getUrl( ) );
        StringBuilder sb = new StringBuilder( );
        sb.append( proposal.getDescription( ) + " " + proposal.getTitre( ) );
        if ( proposal.getAdress( ) != null )
        {
            sb.append( " " + proposal.getAdress( ) );
        }

        String strNickname = UserPreferencesService.instance( ).getNickname( proposal.getLuteceUserName( ) );
        if ( !StringUtils.isEmpty( strNickname ) )
        {
            sb.append( " " + strNickname );
            item.addDynamicFieldNotAnalysed( "pseudo", strNickname );
        }

        sb.append( " " + proposal.getReference( ) );

        item.setContent( sb.toString( ) );

        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceUid( String strResourceId, String strResourceType )
    {
        StringBuilder sb = new StringBuilder( strResourceId );
        sb.append( '_' ).append( SHORT_NAME );

        return sb.toString( );
    }

    @Override
    public List<Field> getAdditionalFields( )
    {
        // TODO Auto-generated method stub
        return new ArrayList( );
    }

    @Override
    public String getDescription( )
    {
        // TODO Auto-generated method stub
        return "Solr proposal indexer";
    }

    @Override
    public List<SolrItem> getDocuments( String arg0 )
    {
        // TODO Auto-generated method stub
        return new ArrayList( );
    }

    @Override
    public String getName( )
    {
        // TODO Auto-generated method stub
        return "SolrProposalIndexer";
    }

    @Override
    public List<String> getResourcesName( )
    {
        // TODO Auto-generated method stub
        return new ArrayList( );
    }

    @Override
    public String getVersion( )
    {
        // TODO Auto-generated method stub
        return "1.0.0";
    }

    /*
     * Index all ideas.
     */
    public List<String> indexDocuments( )
    {

        // Errors and logs management
        List<String> errors = new ArrayList<String>( );
        StringBuffer sbLogs = SolrIndexerService.getSbLogs( );

        // Getting solrItems to index
        ProposalSearcher _proposalSearcher = new ProposalSearcher( );
        _proposalSearcher.setIsPublished( true );

        Collection<SolrItem> proposalsSolrItems = new ArrayList<SolrItem>( );

        Collection<Proposal> proposalsList = new ArrayList<Proposal>( );
        try
        {
            proposalsList = ProposalHome.getProposalsListSearch( _proposalSearcher );
        }
        catch( Exception e )
        {
            printIndexMessage( e, sbLogs );
            errors.add( SolrIndexerService.buildErrorMessage( e ) );
            errors.add( sbLogs.toString( ) );
        }

        for ( Proposal proposal : proposalsList )
        {
            try
            {
                proposalsSolrItems.add( getItem( proposal ) );
            }
            catch( Exception e )
            {
                printIndexMessage( e, sbLogs );
                errors.add( SolrIndexerService.buildErrorMessage( e ) );
                errors.add( sbLogs.toString( ) );
            }
        }

        try
        {
            sbLogs.append( "\nIndexing " + proposalsSolrItems.size( ) + " idea solr items, from " + proposalsList.size( ) + " ideas\n" );
            SolrIndexerService.write( proposalsSolrItems, sbLogs );
        }
        catch( Exception e )
        {
            printIndexMessage( e, sbLogs );
            errors.add( SolrIndexerService.buildErrorMessage( e ) );
            errors.add( sbLogs.toString( ) );
        }

        return errors;
    }

    @Override
    public boolean isEnable( )
    {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Copy paste from SolrIndexer because it's a private method
     */
    private static SolrInputDocument solrItem2SolrInputDocument( SolrItem solrItem )
    {
        SolrInputDocument solrInputDocument = new SolrInputDocument( );
        String strWebappName = SolrIndexerService.getWebAppName( );

        // Prefix the uid by the name of the site. Without that, it is necessary imposible to index two resources of two different sites with the same
        // identifier
        solrInputDocument.addField( SearchItem.FIELD_UID, strWebappName + SolrConstants.CONSTANT_UNDERSCORE + solrItem.getUid( ) );
        solrInputDocument.addField( SearchItem.FIELD_DATE, solrItem.getDate( ) );
        solrInputDocument.addField( SearchItem.FIELD_TYPE, solrItem.getType( ) );
        solrInputDocument.addField( SearchItem.FIELD_SUMMARY, solrItem.getSummary( ) );
        solrInputDocument.addField( SearchItem.FIELD_TITLE, solrItem.getTitle( ) );
        solrInputDocument.addField( SolrItem.FIELD_SITE, solrItem.getSite( ) );
        solrInputDocument.addField( SearchItem.FIELD_ROLE, solrItem.getRole( ) );
        solrInputDocument.addField( SolrItem.FIELD_XML_CONTENT, solrItem.getXmlContent( ) );
        solrInputDocument.addField( SearchItem.FIELD_URL, solrItem.getUrl( ) );
        solrInputDocument.addField( SolrItem.FIELD_HIERATCHY_DATE, solrItem.getHieDate( ) );
        solrInputDocument.addField( SolrItem.FIELD_CATEGORIE, solrItem.getCategorie( ) );
        solrInputDocument.addField( SolrItem.FIELD_CONTENT, solrItem.getContent( ) );
        solrInputDocument.addField( SearchItem.FIELD_DOCUMENT_PORTLET_ID, solrItem.getDocPortletId( ) );

        // Add the dynamic fields
        // They must be declared into the schema.xml of the solr server
        Map<String, Object> mapDynamicFields = solrItem.getDynamicFields( );

        for ( String strDynamicField : mapDynamicFields.keySet( ) )
        {
            solrInputDocument.addField( strDynamicField, mapDynamicFields.get( strDynamicField ) );
        }

        return solrInputDocument;
    }

    private static void write( SolrItem solrItem )
    {
        SolrClient SOLR_SERVER = SolrServerService.getInstance( ).getSolrServer( );
        try
        {
            SolrInputDocument solrInputDocument = solrItem2SolrInputDocument( solrItem );
            SOLR_SERVER.add( solrInputDocument );
            SOLR_SERVER.commit( );
        }
        catch( Exception e )
        {
            AppLogService.error( "IdeationApp, error during indexation" + e.getMessage( ), e );
        }
    }

    /**
     * Adds the exception into the buffer and the StringBuffer
     * 
     * @param exception
     *            Exception to report
     * @param sbLogs
     *            StringBuffer to write to
     */
    private static void printIndexMessage( Exception exception, StringBuffer sbLogs )
    {
        sbLogs.append( " - ERROR : " );

        if ( exception != null )
        {
            sbLogs.append( "(" + exception.getClass( ).getName( ) + ") " + exception.getMessage( ) );
            if ( exception.getCause( ) != null )
            {
                sbLogs.append( " : " );
                sbLogs.append( exception.getCause( ).getMessage( ) );
            }
            AppLogService.error( exception.getMessage( ), exception );
        }
        else
        {
            sbLogs.append( "'exception' param is null !" );
        }

    }

}
