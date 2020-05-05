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

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalHome;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.ProposalSearcher;
import fr.paris.lutece.plugins.participatoryideation.util.ParticipatoryIdeationConstants;
import fr.paris.lutece.plugins.participatoryideation.web.IdeationApp;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

public class ProposalService implements IProposalService
{

    private static final String PROPERTY_LABEL_NQPV = "participatoryideation.qpvqva.nqpv.label";
    private static final String PROPERTY_LABEL_QVA = "participatoryideation.qpvqva.qva.label";
    private static final String PROPERTY_LABEL_GPRU = "participatoryideation.qpvqva.gpru.label";
    private static final String PROPERTY_LABEL_QBP = "participatoryideation.qpvqva.qbp.label";
    private static final String PROPERTY_LABEL_ERR = "participatoryideation.qpvqva.err.label";
    private static final String PROPERTY_LABEL_NON = "participatoryideation.qpvqva.non.label";
    private static final String PROPERTY_LABEL_UNK = "participatoryideation.qpvqva.unk.label";
    private static final String PROPERTY_LABEL_ARDT = "participatoryideation.location_type.ardt.label";
    private static final String PROPERTY_LABEL_PARIS = "participatoryideation.location_type.paris.label";

    private static final String PROPERTY_FIELD4_LABEL_YES = "participatoryideation.field4.yes.label";
    private static final String PROPERTY_FIELD4_LABEL_NO = "participatoryideation.field4.no.label";

    private static volatile ReferenceList _listQpvQvaCodes;
    private static volatile Map<String, String> _mapQpvQvaCodes;

    private static volatile ReferenceList _listField4Codes;
    private static volatile Map<String, String> _mapField4Codes;

    private static volatile ReferenceList _listTypeLocation;
    private static volatile Map<String, String> _mapTypeLocation;

    private static IProposalService _singleton;
    private static SolrProposalIndexer _solrProposalIndexer;
    private static final String BEAN_PROPOSAL_SERVICE = "participatoryideation.proposalService";
    private static final String BEAN_TRANSACTION_MANAGER = "participatoryideation.proposalServiceTransactionManager";
    private static final String BEAN_SOLR_PROPOSAL_INDEXER = "participatoryideation.solrProposalIndexer";

    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    public static IProposalService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_PROPOSAL_SERVICE );
            _solrProposalIndexer = SpringContextService.getBean( BEAN_SOLR_PROPOSAL_INDEXER );

            _listQpvQvaCodes = new ReferenceList( );
            _listQpvQvaCodes.addItem( IdeationApp.QPV_QVA_NO, I18nService.getLocalizedString( PROPERTY_LABEL_NON, Locale.FRENCH ) );
            _listQpvQvaCodes.addItem( IdeationApp.QPV_QVA_QPV, I18nService.getLocalizedString( PROPERTY_LABEL_NQPV, Locale.FRENCH ) );
            _listQpvQvaCodes.addItem( IdeationApp.QPV_QVA_QVA, I18nService.getLocalizedString( PROPERTY_LABEL_QVA, Locale.FRENCH ) );
            _listQpvQvaCodes.addItem( IdeationApp.QPV_QVA_GPRU, I18nService.getLocalizedString( PROPERTY_LABEL_GPRU, Locale.FRENCH ) );
            _listQpvQvaCodes.addItem( IdeationApp.QPV_QVA_QBP, I18nService.getLocalizedString( PROPERTY_LABEL_QBP, Locale.FRENCH ) );
            _listQpvQvaCodes.addItem( IdeationApp.QPV_QVA_ERR, I18nService.getLocalizedString( PROPERTY_LABEL_ERR, Locale.FRENCH ) );
            _listQpvQvaCodes.addItem( ProposalSearcher.QPVQVA_UNKNOWN, I18nService.getLocalizedString( PROPERTY_LABEL_UNK, Locale.FRENCH ) );
            _mapQpvQvaCodes = _listQpvQvaCodes.toMap( );

            _listField4Codes = new ReferenceList( );
            _listField4Codes.addItem( IdeationApp.FIELD4_LABEL_YES, I18nService.getLocalizedString( PROPERTY_FIELD4_LABEL_YES, Locale.FRENCH ) );
            _listField4Codes.addItem( IdeationApp.FIELD4_LABEL_NO, I18nService.getLocalizedString( PROPERTY_FIELD4_LABEL_NO, Locale.FRENCH ) );
            _mapField4Codes = _listField4Codes.toMap( );

            _listTypeLocation = new ReferenceList( );
            _listTypeLocation.addItem( Proposal.LOCATION_AREA_TYPE_LOCALIZED, I18nService.getLocalizedString( PROPERTY_LABEL_ARDT, Locale.FRENCH ) );
            _listTypeLocation.addItem( Proposal.LOCATION_AREA_TYPE_WHOLE, I18nService.getLocalizedString( PROPERTY_LABEL_PARIS, Locale.FRENCH ) );
            _mapTypeLocation = _listTypeLocation.toMap( );

        }

        return _singleton;
    }

    private void createFiles( Proposal proposal, String type, List<File> attachedFiles )
    {
        for ( File file : attachedFiles )
        {
            FileHome.create( file );
        }
    }

    // Don't forget to use InnoDB tables for the following tables!
    // core_file, core_physical_file, participatoryideation_proposals, participatoryideation_proposals_files
    // Check with:
    // sql> show table status ;
    @Transactional( BEAN_TRANSACTION_MANAGER )
    public synchronized void createProposalDB( Proposal proposal ) throws IdeationErrorException
    {
        createFiles( proposal, Proposal.ATTACHED_FILE_TYPE_DOC, proposal.getDocs( ) );
        createFiles( proposal, Proposal.ATTACHED_FILE_TYPE_IMG, proposal.getImgs( ) );
        ProposalHome.create( proposal );
    }

    public void createProposal( Proposal proposal ) throws IdeationErrorException
    {
        _singleton.createProposalDB( proposal );
        _solrProposalIndexer.writeProposal( proposal );
    }

    public void removeProposalCommon( Proposal proposal )
    {
        proposal.setExportedTag( 2 );
        ProposalHome.updateBO( proposal );
        ProposalHome.removeLinkByChild( proposal.getId( ) );
        ProposalHome.removeLinkByParent( proposal.getId( ) );
    }

    public void removeProposal( Proposal proposal )
    {
        removeProposalCommon( proposal );
        String strWorkflowActionNameDeleteProposal = AppPropertiesService
                .getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_DELETE_PROPOSAL );
        ProposalWSService.getInstance( ).processActionByName( strWorkflowActionNameDeleteProposal, proposal.getId( ) );
    }

    public void removeProposalByMdp( Proposal proposal )
    {
        removeProposalCommon( proposal );
        String strWorkflowActionNameDeleteProposalByMdp = AppPropertiesService
                .getProperty( ParticipatoryIdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_DELETE_PROPOSAL_BY_MDP );
        ProposalWSService.getInstance( ).processActionByName( strWorkflowActionNameDeleteProposalByMdp, proposal.getId( ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPublished( Proposal proposal )
    {

        return proposal.getStatusPublic( ) != null && proposal.getStatusPublic( ).isPublished( );
    }

    /**
     * @return the QpvQvaCodes
     */
    public ReferenceList getQpvQvaCodesList( )
    {
        return _listQpvQvaCodes;
    }

    /**
     * @return the QpvQvaCodes
     */
    public Map<String, String> getQpvQvaCodesMap( )
    {
        return _mapQpvQvaCodes;
    }

    /**
     * @return the field4Codes as a list
     */
    public ReferenceList getField4CodesList( )
    {
        return _listField4Codes;
    }

    /**
     * @return the Field4Codes as a map
     */
    public Map<String, String> getField4CodesMap( )
    {
        return _mapField4Codes;
    }

    /**
     * @return the TypeLocation
     */
    public ReferenceList getTypeLocationList( )
    {
        return _listTypeLocation;
    }

    /**
     * @return the TypeLocation
     */
    public Map<String, String> getTypeLocationMap( )
    {
        return _mapTypeLocation;
    }

    // *********************************************************************************************
    // * SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER *
    // * SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER *
    // *********************************************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getUniqueUserGuidsProposalsSubmitters( List<Integer> propIds )
    {
        Set<String> userGuids = new HashSet<String>( );

        for ( Integer propId : propIds )
        {
            Proposal proposal = ProposalHome.findByPrimaryKey( propId );
            if ( proposal == null )
            {
                AppLogService.error( "ERROR : Unable to find proposal #'" + propId + "' !" );
            }
            else
            {
                userGuids.add( proposal.getLuteceUserName( ) );
            }
        }

        return userGuids;
    }

    // *********************************************************************************************
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // *********************************************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getUniqueUserGuidsProposalsFollowers( List<Integer> propIds )
    {
        Set<String> userGuids = new HashSet<String>( );

        for ( Integer propId : propIds )
        {
            ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );

            filter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
            filter.setExtendableResourceType( Proposal.PROPERTY_RESOURCE_TYPE );
            filter.setIdExtendableResource( propId.toString( ) );

            List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

            for ( ResourceExtenderHistory followerHistory : listHistories )
            {
                userGuids.add( followerHistory.getUserGuid( ) );
            }
        }

        return userGuids;
    }

}
