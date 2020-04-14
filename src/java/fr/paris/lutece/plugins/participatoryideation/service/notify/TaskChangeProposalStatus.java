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
package fr.paris.lutece.plugins.participatoryideation.service.notify;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatoryideation.business.notify.TaskChangeProposalStatusConfig;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.service.ProposalService;
import fr.paris.lutece.plugins.participatoryideation.service.SolrProposalIndexer;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

/**
 * 
 * TaskChangeProposalStatus class
 *
 */
public class TaskChangeProposalStatus extends SimpleTask
{
    /**
     * Name of the bean of the config service of this task
     */
    public static final String CONFIG_SERVICE_BEAN_NAME = "participatoryideation.taskChangeProposalStatusConfigService";
    // From plugin-ideation
    private static final String BEAN_SOLR_PROPOSAL_INDEXER = "participatoryideation.solrProposalIndexer";

    // Messages
    private static final String MESSAGE_UNPUBLISHED_PROPOSAL = "module.workflow.ideation.task_change_proposal_status.labelUnpublishedProposal";
    private static final String MESSAGE_PUBLISHED_PROPOSAL = "module.workflow.ideation.task_change_proposal_status.labelPublishedProposal";
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskChangeProposalStatusConfigService;

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskChangeProposalStatusConfig config = _taskChangeProposalStatusConfigService.findByPrimaryKey( this.getId( ) );

        if ( ( config != null ) && ( resourceHistory != null ) && Proposal.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
        {
            // We get the proposal to update
            Proposal proposal = ProposalHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

            if ( proposal != null )
            {
                proposal.setStatusPublic( Proposal.Status.getByValue( config.getProposalStatus( ) ) );
                ProposalHome.updateBO( proposal );

                SolrProposalIndexer solrProposalIndexer = SpringContextService.getBean( BEAN_SOLR_PROPOSAL_INDEXER );
                if ( ProposalService.getInstance( ).isPublished( proposal ) )
                {
                    solrProposalIndexer.writeProposal( proposal );
                }
                else
                {
                    solrProposalIndexer.removeProposal( proposal );
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle( Locale locale )
    {
        TaskChangeProposalStatusConfig config = _taskChangeProposalStatusConfigService.findByPrimaryKey( this.getId( ) );

        if ( ( config != null ) && ( config.getProposalStatus( ) != null ) )
        {
            return I18nService.getLocalizedString( Proposal.Status.getByValue( config.getProposalStatus( ) ).getLibelle( ), locale );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        _taskChangeProposalStatusConfigService.remove( this.getId( ) );
    }
}
