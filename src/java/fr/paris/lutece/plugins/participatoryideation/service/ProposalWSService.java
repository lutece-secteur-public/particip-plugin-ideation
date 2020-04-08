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

import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalSearcher;
import fr.paris.lutece.plugins.participatoryideation.util.ParticipatoryIdeationConstants;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.workflow.IWorkflowService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;

public class ProposalWSService implements IProposalWSService
{

    private static final String BEAN_PROPOSAL_WEB_SERVICE_SERVICE = "participatoryideation.proposalWSService";
    private static IProposalWSService _singleton;
    private static SolrProposalIndexer _solrProposalIndexer;
    private static final String BEAN_SOLR_PROPOSAL_INDEXER = "participatoryideation.solrProposalIndexer";
    private static ICommentService _commentService;
    private static IResourceExtenderHistoryService _resourceHistoryService;

    // properties
    private static final String PROPERTY_POLITENESS_COMMENTS_PROJECTS_NOT_SELECTED = "eudonetbp.site_property.mail.politeness_comments_projects_not_selected.htmlblock";
    // Mark
    private static final String MARK_COMMENT = "comment";

    /**
     * Get the unique instance of the Proposal Web Service Service
     * 
     * @return The instance
     */
    public static IProposalWSService getInstance( )
    {
        if ( _singleton == null )
        {
            _solrProposalIndexer = SpringContextService.getBean( BEAN_SOLR_PROPOSAL_INDEXER );
            _singleton = SpringContextService.getBean( BEAN_PROPOSAL_WEB_SERVICE_SERVICE );
            _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
            _resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );

        }
        return _singleton;

    }

    @Override
    public Collection<Proposal> getProposalsList( )
    {

        return ProposalHome.getProposalsList( );

    }

    @Override
    public Proposal getProposalByIdentifiant( int nKey )
    {

        return ProposalHome.findByPrimaryKey( nKey );

    }

    @Override
    public Proposal getProposalByIdentifiantAndCampaign( int nKey, String strCampaign )
    {

        return ProposalHome.findByCodes( strCampaign, nKey );

    }

    @Override
    public Collection<Proposal> getProposalsListSearch( ProposalSearcher proposalSearcher )
    {

        return ProposalHome.getProposalsListSearch( proposalSearcher );
    }

    @Override
    public void updateProposal( Proposal proposalLutece, Proposal proposalEudonet, HttpServletRequest request )
    {
        updateProposal( proposalLutece, proposalEudonet, true, request );
    }

    @Override
    public void updateProposal( Proposal proposalLutece, Proposal proposalEudonet, boolean notify, HttpServletRequest request )
    {

        String valStatusLutece = proposalLutece.getStatusPublic( ).getValeur( ); // statut PUBLIC sur LUTECE
        String valStatusEudonet = proposalEudonet.getStatusEudonet( ).getValeur( ); // statut EUDONET sur LUTECE (si non valué, sera value au statut PUBLIC sur
        // LUTECE)
        String valStatusEudonetLutece = valStatusLutece; // statut PUBLIC sur EUDONET

        if ( proposalLutece.getStatusEudonet( ) != null )
        {
            valStatusEudonetLutece = proposalLutece.getStatusEudonet( ).getValeur( );
        }

        if ( valStatusLutece.equals( valStatusEudonet )
                || ( !valStatusLutece.equals( valStatusEudonet ) && !valStatusLutece.equals( valStatusEudonetLutece )
                        && !valStatusEudonet.equals( valStatusEudonetLutece ) )
                || ( valStatusLutece.equals( Proposal.Status.STATUS_SUPPRIME_PAR_USAGER.getValeur( ) ) )
                || ( valStatusEudonet.equals( Proposal.Status.STATUS_SUPPRIME_PAR_USAGER.getValeur( ) ) ) )
        {
            // On ne fait rien
        }
        else
        {
            ProposalHome.updateBO( proposalEudonet );
            if ( proposalEudonet.getMotifRecev( ) != null && StringUtils.isNotEmpty( proposalEudonet.getMotifRecev( ) )
                    && StringUtils.isNotBlank( proposalEudonet.getMotifRecev( ) ) && !( proposalLutece.getMotifRecev( ) != null
                            && proposalEudonet.getMotifRecev( ) != null && proposalEudonet.getMotifRecev( ).equals( proposalLutece.getMotifRecev( ) ) ) )
            {
                createComment( proposalEudonet );
            }
            if ( proposalEudonet.getStatusPublic( ).equals( Proposal.Status.STATUS_SUPPRIME_PAR_MDP )
                    || proposalEudonet.getStatusPublic( ).equals( Proposal.Status.STATUS_SUPPRIME_PAR_USAGER ) )
            {
                _solrProposalIndexer.removeProposal( proposalEudonet );
            }
            if ( WorkflowService.getInstance( ).isAvailable( ) )
            {
                processAction( valStatusEudonet, proposalLutece, notify, request );
            }
        }
    }

    @Override
    public Proposal updateProposal( Proposal proposal )
    {

        proposal = ProposalHome.updateBO( proposal );
        return proposal;
    }

    @Override
    public void createComment( Proposal proposal )
    {
        String strCommentPNS = DatastoreService.getDataValue( PROPERTY_POLITENESS_COMMENTS_PROJECTS_NOT_SELECTED, "" );
        String strContentCommentPNS = proposal.getMotifRecev( );
        Map<String, String> model = new HashMap<String, String>( );
        model.put( MARK_COMMENT, proposal.getMotifRecev( ) );
        try
        {
            HtmlTemplate t = AppTemplateService.getTemplateFromStringFtl( strCommentPNS, new Locale( "fr", "FR" ), model );
            strContentCommentPNS = t.getHtml( );
        }
        catch( Exception e )
        {
            // _service.addToLog( "Erreur updateProposal: "+e.getMessage( ));
            AppLogService.error( "Erreur avec le template freemarker dans les proprietes du site: ", e );
        }

        Comment comment = new Comment( );
        comment.setIdExtendableResource( "" + proposal.getId( ) );
        comment.setExtendableResourceType( Proposal.PROPERTY_RESOURCE_TYPE );
        comment.setIdParentComment( 0 );
        comment.setComment( strContentCommentPNS );
        Timestamp currentDate = new Timestamp( new Date( ).getTime( ) );
        comment.setDateComment( currentDate );
        comment.setDateLastModif( currentDate );
        comment.setName( "Mairie de Paris" );
        comment.setEmail( "lutece@lutece.com" );
        comment.setPublished( true );
        comment.setIpAddress( "" );
        comment.setIsAdminComment( true );
        comment.setIsImportant( true );
        // comment.setCommentOrder(1);
        comment.setPinned( true );
        _commentService.create( comment );

        ResourceExtenderHistory history = new ResourceExtenderHistory( );
        history.setExtenderType( CommentResourceExtender.EXTENDER_TYPE_COMMENT );
        // history.setIdExtendableResource( proposal.getCodeCampaign() + "-" + String.format("%06d", proposal.getCodeProposal()) );
        history.setIdExtendableResource( "" + proposal.getId( ) );
        history.setExtendableResourceType( Proposal.PROPERTY_RESOURCE_TYPE );
        history.setIpAddress( StringUtils.EMPTY );
        history.setUserGuid( AppPropertiesService.getProperty( ParticipatoryIdeationConstants.PROPERTY_GENERATE_PROPOSAL_LUTECE_USER_NAME ) ); // Le commentaire
                                                                                                                                               // est déposé par
        // l'équipe
        // du Budget Participatif.
        _resourceHistoryService.create( history );
    }

    private void processAction( String proposalStatut, Proposal proposal, boolean notify, HttpServletRequest request )
    {

        boolean foundAction = false;
        int nIdWorkflow = AppPropertiesService.getPropertyInt( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
        String proposalStatutLibelle = removeAccent(
                I18nService.getLocalizedString( ( Proposal.Status.getByValue( proposalStatut ).getLibelle( ) ), new Locale( "fr", "FR" ) ) )
                + ( notify ? " (avec notification)" : " (sans notification)" );

        if ( nIdWorkflow != -1 )
        {
            List<Action> actionsList = WorkflowService.getInstance( ).getMassActions( nIdWorkflow );
            for ( Action action : actionsList )
            {
                String actionLibelle = removeAccent( action.getName( ) );
                if ( actionLibelle.equals( proposalStatutLibelle ) )
                {
                    foundAction = true;
                    IWorkflowService _service = SpringContextService
                            .getBean( fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService.BEAN_SERVICE );
                    _service.doProcessAction( proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, action.getId( ), -1, request, new Locale( "fr", "FR" ), false,
                            null );
                    // WorkflowService.getInstance( ).doProcessAction(proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, action.getId( ), -1 , null, new
                    // Locale( "fr" ,
                    // "FR" ), false);
                }
            }

            if ( !foundAction )
            {
                AppLogService.error( "No such action on workflow #" + nIdWorkflow + " : '" + proposalStatutLibelle + "'" );
            }

            /*
             * State state= WorkflowService.getInstance( ).getState( proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, -1 ); if(state != null){
             * WorkflowService.getInstance( ).doProcessAutomaticReflexiveActions(proposal.getId( ), Proposal.WORKFLOW_RESOURCE_TYPE, state.getId( ), -1, new
             * Locale( "fr" , "FR" )); }
             */
        }
    }

    @Override
    public void processActionByName( String strWorkflowProposalActionName, int nIdProposal, HttpServletRequest request )
    {

        int nIdWorkflow = AppPropertiesService.getPropertyInt( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ID, -1 );

        if ( nIdWorkflow != -1 && WorkflowService.getInstance( ).isAvailable( ) && !StringUtils.isEmpty( strWorkflowProposalActionName ) )
        {

            List<Action> actionsList = WorkflowService.getInstance( ).getMassActions( nIdWorkflow );

            for ( Action action : actionsList )
            {

                if ( action.getName( ).equals( strWorkflowProposalActionName ) )
                {

                    WorkflowService.getInstance( ).doProcessAction( nIdProposal, Proposal.WORKFLOW_RESOURCE_TYPE, action.getId( ), -1, request,
                            new Locale( "fr", "FR" ), true );

                }
            }
        }

    }

    @Override
    public void processActionByName( String strWorkflowProposalActionName, int nIdProposal )
    {

        int nIdWorkflow = AppPropertiesService.getPropertyInt( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ID, -1 );

        if ( nIdWorkflow != -1 && WorkflowService.getInstance( ).isAvailable( ) && !StringUtils.isEmpty( strWorkflowProposalActionName ) )
        {

            List<Action> actionsList = WorkflowService.getInstance( ).getMassActions( nIdWorkflow );

            for ( Action action : actionsList )
            {

                if ( action.getName( ).equals( strWorkflowProposalActionName ) )
                {

                    WorkflowService.getInstance( ).doProcessAction( nIdProposal, Proposal.WORKFLOW_RESOURCE_TYPE, action.getId( ), -1, null,
                            new Locale( "fr", "FR" ), true );

                }
            }
        }

    }

    public static String removeAccent( String source )
    {
        return Normalizer.normalize( source, Normalizer.Form.NFD ).replaceAll( "[^\\p{ASCII}]", "" );
    }

}
