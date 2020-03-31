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

import fr.paris.lutece.plugins.participatoryideation.business.notify.TaskChangeIdeeStatusConfig;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.service.IdeeService;
import fr.paris.lutece.plugins.participatoryideation.service.SolrIdeeIndexer;
import fr.paris.lutece.plugins.workflowcore.business.resource.ResourceHistory;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.resource.IResourceHistoryService;
import fr.paris.lutece.plugins.workflowcore.service.task.SimpleTask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import org.apache.commons.lang.StringUtils;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * TaskChangeIdeeStatus class
 *
 */
public class TaskChangeIdeeStatus extends SimpleTask
{
    /**
     * Name of the bean of the config service of this task
     */
    public static final String CONFIG_SERVICE_BEAN_NAME = "participatoryideation.taskChangeIdeeStatusConfigService";
    // From plugin-ideation
    private static final String BEAN_SOLR_IDEE_INDEXER = "participatoryideation.solrIdeeIndexer";

    // Messages
    private static final String MESSAGE_UNPUBLISHED_IDEE = "module.workflow.ideation.task_change_idee_status.labelUnpublishedIdee";
    private static final String MESSAGE_PUBLISHED_IDEE = "module.workflow.ideation.task_change_idee_status.labelPublishedIdee";
    @Inject
    private IResourceHistoryService _resourceHistoryService;
    @Inject
    @Named( CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskChangeIdeeStatusConfigService;

    @Override
    public void processTask( int nIdResourceHistory, HttpServletRequest request, Locale locale )
    {
        ResourceHistory resourceHistory = _resourceHistoryService.findByPrimaryKey( nIdResourceHistory );
        TaskChangeIdeeStatusConfig config = _taskChangeIdeeStatusConfigService.findByPrimaryKey( this.getId( ) );

        if ( ( config != null ) && ( resourceHistory != null ) && Idee.WORKFLOW_RESOURCE_TYPE.equals( resourceHistory.getResourceType( ) ) )
        {
            // We get the idee to update
            Idee idee = IdeeHome.findByPrimaryKey( resourceHistory.getIdResource( ) );

            if ( idee != null )
            {
                idee.setStatusPublic( Idee.Status.getByValue( config.getIdeeStatus( ) ) );
                IdeeHome.updateBO( idee );

                SolrIdeeIndexer solrIdeeIndexer = SpringContextService.getBean( BEAN_SOLR_IDEE_INDEXER );
                if ( IdeeService.getInstance( ).isPublished( idee ) )
                {
                    solrIdeeIndexer.writeIdee( idee );
                }
                else
                {
                    solrIdeeIndexer.removeIdee( idee );
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
        TaskChangeIdeeStatusConfig config = _taskChangeIdeeStatusConfigService.findByPrimaryKey( this.getId( ) );

        if ( ( config != null ) && ( config.getIdeeStatus( ) != null ) )
        {
            return I18nService.getLocalizedString( Idee.Status.getByValue( config.getIdeeStatus( ) ).getLibelle( ), locale );
        }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doRemoveConfig( )
    {
        _taskChangeIdeeStatusConfigService.remove( this.getId( ) );
    }
}
