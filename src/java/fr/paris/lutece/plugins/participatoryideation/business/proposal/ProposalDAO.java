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
package fr.paris.lutece.plugins.participatoryideation.business.proposal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal.Status;
import fr.paris.lutece.plugins.participatoryideation.web.IdeationApp;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Proposal objects
 */

public final class ProposalDAO implements IProposalDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_proposal ) FROM participatoryideation_proposals";
    private static final String SQL_QUERY_NEW_CODE_PROPOSAL = "SELECT max( code_proposal ) FROM participatoryideation_proposals where code_campaign = ?";

    private static final String SQL_QUERY_INSERT = "INSERT INTO participatoryideation_proposals ( id_proposal, lutece_user_name, titre, field1, description, cout, code_theme, location_type, location_ardt, submitter_type, submitter, accept_exploit, accept_contact, address,longitude,latitude,creation_timestamp,code_campaign,code_proposal,type_nqpv_qva,id_nqpv_qva,libelle_nqpv_qva, status_public, status_eudonet, motif_recev,id_project, titre_projet, url_projet, winner_projet, field2, field3, handicap, handicap_complement) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatoryideation_proposals SET eudonet_exported_tag=?, status_public=?, status_eudonet=?, motif_recev=?, type_nqpv_qva=?, id_nqpv_qva=?, libelle_nqpv_qva=?, id_project = ?, titre_projet = ?, url_projet =?, winner_projet =?, titre =? , description =? , cout =? , location_type =? , location_ardt =?, handicap=?, handicap_complement=? WHERE id_proposal = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT proposals.id_proposal, proposals.lutece_user_name, proposals.titre, proposals.field1, proposals.description, proposals.cout, proposals.code_theme, proposals.location_type, proposals.location_ardt, proposals.submitter_type, proposals.submitter, proposals.accept_exploit, proposals.accept_contact, proposals.address, proposals.longitude, proposals.latitude, proposals.creation_timestamp, proposals.code_campaign, proposals.code_proposal, proposals.eudonet_exported_tag, proposals.type_nqpv_qva, proposals.id_nqpv_qva, proposals.libelle_nqpv_qva, proposals.status_public, proposals.status_eudonet, proposals.motif_recev, proposals.id_project, proposals.titre_projet, proposals.url_projet, proposals.winner_projet, proposals.field2, proposals.field3, proposals.handicap, proposals.handicap_complement FROM participatoryideation_proposals proposals";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL + " WHERE proposals.id_proposal = ?";
    private static final String SQL_QUERY_SELECT_BY_CODES = SQL_QUERY_SELECTALL + " WHERE proposals.code_campaign = ? and proposals.code_proposal = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_proposal FROM participatoryideation_proposals";

    private static final String SQL_QUERY_NEW_PK_FILE = "SELECT max( id_proposal_file ) FROM participatoryideation_proposals_files";
    private static final String SQL_QUERY_INSERT_FILE = "INSERT INTO participatoryideation_proposals_files (id_proposal_file, id_file, id_proposal, type) values ( ?, ? , ? , ? )";
    private static final String SQL_QUERY_SELECTALL_FILES = "SELECT id_file, id_proposal, type FROM participatoryideation_proposals_files";
    private static final String SQL_QUERY_SELECT_FILE = SQL_QUERY_SELECTALL_FILES + " WHERE id_proposal = ?";

    private static final String SQL_QUERY_NEW_PK_PROPOSAL_LINK = "SELECT max( id_proposal_link ) FROM participatoryideation_proposals_links";
    private static final String SQL_QUERY_INSERT_LINK = "INSERT INTO participatoryideation_proposals_links ( id_proposal_link, id_proposal_parent, id_proposal_child ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_LINKED_PROPOSALS = "SELECT proposals.id_proposal, proposals.code_campaign, proposals.code_proposal FROM participatoryideation_proposals_links links INNER JOIN participatoryideation_proposals proposals ON";
    private static final String SQL_QUERY_SELECT_CHILD_PROPOSALS = SQL_QUERY_SELECT_LINKED_PROPOSALS
            + " links.id_proposal_child = proposals.id_proposal WHERE links.id_proposal_parent = ?";
    private static final String SQL_QUERY_SELECT_PARENT_PROPOSALS = SQL_QUERY_SELECT_LINKED_PROPOSALS
            + " links.id_proposal_parent = proposals.id_proposal WHERE links.id_proposal_child = ?";

    private static final String SQL_QUERY_DELETE_LINK_BY_PARENT = "DELETE FROM participatoryideation_proposals_links WHERE id_proposal_parent = ?";
    private static final String SQL_QUERY_DELETE_LINK_BY_CHILD = "DELETE FROM participatoryideation_proposals_links WHERE id_proposal_child = ?";
    private static final String SQL_QUERY_SELECTALL_LINKS = "SELECT child_proposals.id_proposal, child_proposals.code_campaign, child_proposals.code_proposal, parent_proposals.id_proposal, parent_proposals.code_campaign, parent_proposals.code_proposal FROM participatoryideation_proposals_links links inner join participatoryideation_proposals child_proposals ON links.id_proposal_child = child_proposals.id_proposal inner join participatoryideation_proposals parent_proposals ON links.id_proposal_parent = parent_proposals.id_proposal";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }

        return nKey;
    }

    /**
     * Generates a new code proposal for this campaign
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newCodeProposal( String strCodeCampaign, Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_CODE_PROPOSAL, plugin ) )
        {
            daoUtil.setString( 1, strCodeCampaign );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Proposal proposal, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nCpt = 1;

            proposal.setId( newPrimaryKey( plugin ) );
            proposal.setCodeProposal( newCodeProposal( proposal.getCodeCampaign( ), plugin ) );

            daoUtil.setInt( nCpt++, proposal.getId( ) );
            daoUtil.setString( nCpt++, proposal.getLuteceUserName( ) );
            daoUtil.setString( nCpt++, proposal.getTitre( ) );
            daoUtil.setString( nCpt++, proposal.getField1( ) );
            daoUtil.setString( nCpt++, proposal.getDescription( ) );

            if ( proposal.getCout( ) != null )
            {
                daoUtil.setLong( nCpt++, proposal.getCout( ) );
            }
            else
            {
                daoUtil.setLongNull( nCpt++ );
            }

            daoUtil.setString( nCpt++, proposal.getCodeTheme( ) );
            daoUtil.setString( nCpt++, proposal.getLocationType( ) );
            daoUtil.setString( nCpt++, proposal.getLocationArdt( ) );
            daoUtil.setString( nCpt++, proposal.getSubmitterType( ) );
            daoUtil.setString( nCpt++, proposal.getSubmitter( ) );
            daoUtil.setBoolean( nCpt++, proposal.isAcceptExploit( ) );
            daoUtil.setBoolean( nCpt++, proposal.isAcceptContact( ) );
            daoUtil.setString( nCpt++, proposal.getAdress( ) );

            if ( proposal.getLongitude( ) != null )
            {
                daoUtil.setDouble( nCpt++, proposal.getLongitude( ) );
            }
            else
            {
                daoUtil.setDoubleNull( nCpt++ );
            }

            if ( proposal.getLatitude( ) != null )
            {
                daoUtil.setDouble( nCpt++, proposal.getLatitude( ) );
            }
            else
            {
                daoUtil.setDoubleNull( nCpt++ );
            }

            daoUtil.setTimestamp( nCpt++, proposal.getCreationTimestamp( ) );
            daoUtil.setString( nCpt++, proposal.getCodeCampaign( ) );
            daoUtil.setInt( nCpt++, proposal.getCodeProposal( ) );
            daoUtil.setString( nCpt++, proposal.getTypeQpvQva( ) );
            daoUtil.setString( nCpt++, proposal.getIdQpvQva( ) );
            daoUtil.setString( nCpt++, proposal.getLibelleQpvQva( ) );
            daoUtil.setString( nCpt++, ( proposal.getStatusPublic( ) == null ) ? null : proposal.getStatusPublic( ).getValeur( ) );
            daoUtil.setString( nCpt++, ( proposal.getStatusEudonet( ) == null ) ? null : proposal.getStatusEudonet( ).getValeur( ) );
            daoUtil.setString( nCpt++, proposal.getMotifRecev( ) );
            daoUtil.setString( nCpt++, proposal.getIdProjet( ) );
            daoUtil.setString( nCpt++, proposal.getTitreProjet( ) );
            daoUtil.setString( nCpt++, proposal.getUrlProjet( ) );
            daoUtil.setString( nCpt++, proposal.getWinnerProjet( ) );
            daoUtil.setString( nCpt++, proposal.getfield2( ) );
            daoUtil.setString( nCpt++, proposal.getField3( ) );
            daoUtil.setString( nCpt++, proposal.getHandicap( ) );
            daoUtil.setString( nCpt++, ( proposal.getHandicapComplement( ) == null ? "" : proposal.getHandicapComplement( ) ) );

            daoUtil.executeUpdate( );
        }

        insertFiles( proposal, plugin );
    }

    private void insertFiles( Proposal proposal, Plugin plugin )
    {
        for ( File file : proposal.getImgs( ) )
        {
            int id = file.getIdFile( );
            insertFile( Proposal.ATTACHED_FILE_TYPE_IMG, id, proposal.getId( ), plugin );
        }
        for ( File file : proposal.getDocs( ) )
        {
            int id = file.getIdFile( );
            insertFile( Proposal.ATTACHED_FILE_TYPE_DOC, id, proposal.getId( ), plugin );
        }
    }

    /**
     * Generates a new proposal_file primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKeyFile( Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_FILE, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }

        return nKey;
    }

    private void insertFile( String string, int id, int id2, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_FILE, plugin ) )
        {
            daoUtil.setInt( 1, newPrimaryKeyFile( plugin ) );
            daoUtil.setInt( 2, id );
            daoUtil.setInt( 3, id2 );
            daoUtil.setString( 4, string );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Proposal load( int nKey, Plugin plugin )
    {
        Proposal proposal = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                proposal = getRow( daoUtil );
            }
        }

        if ( proposal != null )
        {
            loadFileIds( proposal, plugin );
            loadLinkedProposals( proposal, plugin );
        }

        return proposal;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Proposal loadByCodes( String strCodeCampaign, int nCodeProposal, Plugin plugin )
    {
        Proposal proposal = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODES, plugin ) )
        {
            daoUtil.setString( 1, strCodeCampaign );
            daoUtil.setInt( 2, nCodeProposal );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                proposal = getRow( daoUtil );
            }
        }

        if ( proposal != null )
        {
            loadFileIds( proposal, plugin );
            loadLinkedProposals( proposal, plugin );
        }

        return proposal;
    }

    private void loadFileIds( Proposal proposal, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FILE, plugin ) )
        {
            daoUtil.setInt( 1, proposal.getId( ) );
            daoUtil.executeQuery( );

            List<File> listDocs = new ArrayList<File>( );
            List<File> listImgs = new ArrayList<File>( );
            while ( daoUtil.next( ) )
            {
                int fileId = daoUtil.getInt( 1 );
                int proposalId = daoUtil.getInt( 2 );
                String type = daoUtil.getString( 3 );
                if ( Proposal.ATTACHED_FILE_TYPE_DOC.equals( daoUtil.getString( 3 ) ) )
                {
                    listDocs.add( FileHome.findByPrimaryKey( fileId ) );
                }
                else
                    if ( Proposal.ATTACHED_FILE_TYPE_IMG.equals( daoUtil.getString( 3 ) ) )
                    {
                        listImgs.add( FileHome.findByPrimaryKey( fileId ) );
                    }
                    else
                    {
                        AppLogService.info( "Ideation, unknown attached file type " + fileId + "," + proposalId + "," + type );
                    }
            }

            proposal.setDocs( listDocs );
            proposal.setImgs( listImgs );
        }
    }

    private void loadLinkedProposals( Proposal proposal, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CHILD_PROPOSALS, plugin ) )
        {
            daoUtil.setInt( 1, proposal.getId( ) );
            daoUtil.executeQuery( );
            List<Proposal> listProposals = getLinkedProposals( daoUtil );
            proposal.setChildProposals( listProposals );
        }

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARENT_PROPOSALS, plugin ) )
        {
            daoUtil.setInt( 1, proposal.getId( ) );
            daoUtil.executeQuery( );
            List<Proposal> listProposals = getLinkedProposals( daoUtil );
            proposal.setParentProposals( listProposals );
        }
    }

    private List<Proposal> getLinkedProposals( DAOUtil daoUtil )
    {
        List<Proposal> listProposals = new ArrayList<Proposal>( );
        while ( daoUtil.next( ) )
        {
            Proposal otherProposal = getFirstLinkedProposalRow( daoUtil );
            listProposals.add( otherProposal );
        }
        return listProposals;
    }

    private Proposal getFirstLinkedProposalRow( DAOUtil daoUtil )
    {
        return getLinkedProposalRow( daoUtil, 1 );
    }

    private Proposal getChildProposalRow( DAOUtil daoUtil )
    {
        return getLinkedProposalRow( daoUtil, 1 );
    }

    private Proposal getParentProposalRow( DAOUtil daoUtil )
    {
        return getLinkedProposalRow( daoUtil, 4 );
    }

    private Proposal getLinkedProposalRow( DAOUtil daoUtil, int nCpt )
    {
        Proposal otherProposal = new Proposal( );
        otherProposal.setId( daoUtil.getInt( nCpt++ ) );
        otherProposal.setCodeCampaign( daoUtil.getString( nCpt++ ) );
        otherProposal.setCodeProposal( daoUtil.getInt( nCpt++ ) );
        return otherProposal;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hasParent( int nIdProposal, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARENT_PROPOSALS, plugin ) )
        {
            daoUtil.setInt( 1, nIdProposal );
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                return daoUtil.getInt( 1 );
            }
        }

        return 0;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void storeBO( Proposal proposal, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            int nCpt = 1;

            daoUtil.setInt( nCpt++, proposal.getExportedTag( ) );

            if ( proposal.getStatusPublic( ) != null )
            {
                daoUtil.setString( nCpt++, proposal.getStatusPublic( ).getValeur( ) );
            }
            else
            {
                daoUtil.setString( nCpt++, null );
            }

            if ( proposal.getStatusEudonet( ) != null )
            {
                daoUtil.setString( nCpt++, proposal.getStatusEudonet( ).getValeur( ) );
            }
            else
            {
                daoUtil.setString( nCpt++, null );
            }

            daoUtil.setString( nCpt++, proposal.getMotifRecev( ) );

            daoUtil.setString( nCpt++, proposal.getTypeQpvQva( ) );
            daoUtil.setString( nCpt++, proposal.getIdQpvQva( ) );
            daoUtil.setString( nCpt++, proposal.getLibelleQpvQva( ) );
            daoUtil.setString( nCpt++, proposal.getIdProjet( ) );
            daoUtil.setString( nCpt++, proposal.getTitreProjet( ) );
            daoUtil.setString( nCpt++, proposal.getUrlProjet( ) );
            daoUtil.setString( nCpt++, proposal.getWinnerProjet( ) );
            daoUtil.setString( nCpt++, proposal.getTitre( ) );
            daoUtil.setString( nCpt++, proposal.getDescription( ) );

            if ( proposal.getCout( ) != null )
            {
                daoUtil.setLong( nCpt++, proposal.getCout( ) );
            }
            else
            {
                daoUtil.setLongNull( nCpt++ );

            }

            daoUtil.setString( nCpt++, proposal.getLocationType( ) );
            daoUtil.setString( nCpt++, proposal.getLocationArdt( ) );

            daoUtil.setString( nCpt++, proposal.getHandicap( ) );
            daoUtil.setString( nCpt++, proposal.getHandicapComplement( ) );

            daoUtil.setInt( nCpt++, proposal.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Proposal> selectProposalsList( Plugin plugin )
    {
        return selectProposalsListSearch( plugin, null );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Proposal> selectProposalsListSearch( Plugin plugin, ProposalSearcher proposalSearcher )
    {
        Map<Integer, Proposal> proposalMap = new LinkedHashMap<Integer, Proposal>( );

        String queryStr = ( proposalSearcher != null ) ? appendFilters( SQL_QUERY_SELECTALL, proposalSearcher ) : SQL_QUERY_SELECTALL;
        try ( DAOUtil daoUtil = new DAOUtil( queryStr, plugin ) )
        {
            if ( proposalSearcher != null )
            {
                setFilterValues( daoUtil, proposalSearcher );
            }
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Proposal proposal = getRow( daoUtil );
                proposalMap.put( proposal.getId( ), proposal );
                proposal.setDocs( new ArrayList<File>( ) );
                proposal.setImgs( new ArrayList<File>( ) );
                proposal.setChildProposals( new ArrayList<Proposal>( ) );
                proposal.setParentProposals( new ArrayList<Proposal>( ) );
            }
        }

        // Use workflow services instead of joining into the workflow tables
        if ( proposalSearcher != null && proposalSearcher.getIdWorkflowState( ) != null )
        {
            WorkflowService workflowService = WorkflowService.getInstance( );
            if ( workflowService.isAvailable( ) )
            {
                List<Integer> allIds = workflowService.getResourceIdListByIdState( proposalSearcher.getIdWorkflowState( ), Proposal.WORKFLOW_RESOURCE_TYPE );
                HashSet<Integer> hsAllIds = new HashSet<Integer>( allIds );
                for ( Iterator<Entry<Integer, Proposal>> it = proposalMap.entrySet( ).iterator( ); it.hasNext( ); )
                {
                    Entry<Integer, Proposal> entry = it.next( );
                    if ( !hsAllIds.contains( entry.getKey( ) ) )
                    {
                        it.remove( );
                    }
                }
            }
        }

        // TODO do we need to filter these ?
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_FILES, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                int proposalId = daoUtil.getInt( 2 );
                int fileId = daoUtil.getInt( 1 );
                String type = daoUtil.getString( 3 );

                Proposal proposal = proposalMap.get( proposalId );
                if ( proposal != null )
                {
                    if ( Proposal.ATTACHED_FILE_TYPE_DOC.equals( type ) )
                    {
                        proposal.getDocs( ).add( FileHome.findByPrimaryKey( fileId ) );
                    }
                    else
                        if ( Proposal.ATTACHED_FILE_TYPE_IMG.equals( type ) )
                        {
                            proposal.getImgs( ).add( FileHome.findByPrimaryKey( fileId ) );
                        }
                        else
                        {
                            AppLogService.info( "Ideation, unknown attached file type " + fileId + "," + proposalId + "," + type );
                        }
                }
            }
        }

        // TODO do we need to filter these ?
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_LINKS, plugin ) )
        {
            daoUtil.executeQuery( );
            while ( daoUtil.next( ) )
            {
                int idProposalChild = daoUtil.getInt( 1 );
                int idProposalParent = daoUtil.getInt( 4 );
                Proposal mapProposalParent = proposalMap.get( idProposalParent );
                Proposal mapProposalChild = proposalMap.get( idProposalChild );

                Proposal proposalParent = ( mapProposalParent == null ) ? getParentProposalRow( daoUtil ) : mapProposalParent;
                Proposal proposalChild = ( mapProposalChild == null ) ? getChildProposalRow( daoUtil ) : mapProposalChild;

                if ( mapProposalParent != null )
                {
                    mapProposalParent.getChildProposals( ).add( proposalChild );
                }

                if ( mapProposalChild != null )
                {
                    mapProposalChild.getParentProposals( ).add( proposalParent );
                }
            }
        }

        ArrayList<Proposal> result = new ArrayList<Proposal>( proposalMap.values( ) );
        return result;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdProposalsList( Plugin plugin )
    {
        Collection<Integer> proposalList = new ArrayList<Integer>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                proposalList.add( daoUtil.getInt( 1 ) );
            }
        }

        return proposalList;
    }

    private Proposal getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;

        Proposal proposal = new Proposal( );
        proposal.setId( daoUtil.getInt( nCpt++ ) );
        proposal.setLuteceUserName( daoUtil.getString( nCpt++ ) );
        proposal.setTitre( daoUtil.getString( nCpt++ ) );
        proposal.setField1( daoUtil.getString( nCpt++ ) );
        proposal.setDescription( daoUtil.getString( nCpt++ ) );
        proposal.setCout( (Long) daoUtil.getObject( nCpt++ ) );
        proposal.setCodeTheme( daoUtil.getString( nCpt++ ) );
        proposal.setLocationType( daoUtil.getString( nCpt++ ) );
        proposal.setLocationArdt( daoUtil.getString( nCpt++ ) );
        proposal.setSubmitterType( daoUtil.getString( nCpt++ ) );
        proposal.setSubmitter( daoUtil.getString( nCpt++ ) );
        proposal.setAcceptExploit( daoUtil.getBoolean( nCpt++ ) );
        proposal.setAcceptContact( daoUtil.getBoolean( nCpt++ ) );
        proposal.setAdress( daoUtil.getString( nCpt++ ) );

        Float fLongitude = ( (Float) daoUtil.getObject( nCpt++ ) );
        if ( fLongitude != null )
        {
            proposal.setLongitude( fLongitude.doubleValue( ) );
        }

        Float fLatitude = ( (Float) daoUtil.getObject( nCpt++ ) );
        if ( fLatitude != null )
        {
            proposal.setLatitude( fLatitude.doubleValue( ) );
        }

        proposal.setCreationTimestamp( daoUtil.getTimestamp( nCpt++ ) );
        proposal.setCodeCampaign( daoUtil.getString( nCpt++ ) );
        proposal.setCodeProposal( daoUtil.getInt( nCpt++ ) );
        proposal.setExportedTag( daoUtil.getInt( nCpt++ ) );
        proposal.setTypeQpvQva( daoUtil.getString( nCpt++ ) );
        proposal.setIdQpvQva( daoUtil.getString( nCpt++ ) );
        proposal.setLibelleQpvQva( daoUtil.getString( nCpt++ ) );
        proposal.setStatusPublic( Proposal.Status.getByValue( daoUtil.getString( nCpt++ ) ) );
        proposal.setStatusEudonet( Proposal.Status.getByValue( daoUtil.getString( nCpt++ ) ) );
        proposal.setMotifRecev( daoUtil.getString( nCpt++ ) );
        proposal.setIdProjet( daoUtil.getString( nCpt++ ) );
        proposal.setTitreProjet( daoUtil.getString( nCpt++ ) );
        proposal.setUrlProjet( daoUtil.getString( nCpt++ ) );
        proposal.setWinnerProjet( daoUtil.getString( nCpt++ ) );
        proposal.setfield2( daoUtil.getString( nCpt++ ) );
        proposal.setField3( daoUtil.getString( nCpt++ ) );
        proposal.setHandicap( daoUtil.getString( nCpt++ ) );
        proposal.setHandicapComplement( daoUtil.getString( nCpt++ ) );

        return proposal;
    }

    /**
     * Creates the preparedStatement for apply filters
     * 
     * @param query
     *            The begining of the query
     * @param proposalSearcher
     *            The proposalSearcher
     * @return The sql statement
     */
    private String appendFilters( String query, ProposalSearcher proposalSearcher )
    {

        // Create the joins
        StringBuilder stringBuilderJoin = new StringBuilder( );

        // Create the where clause
        StringBuilder stringBuilder = new StringBuilder( );
        if ( proposalSearcher.getCodeCampaign( ) != null )
        {
            stringBuilder.append( " proposals.code_campaign = ? AND" );
        }
        if ( proposalSearcher.getCodeTheme( ) != null )
        {
            stringBuilder.append( " proposals.code_theme = ? AND" );
        }
        if ( proposalSearcher.getExportedTag( ) != null )
        {
            stringBuilder.append( " proposals.eudonet_exported_tag = ? AND" );
        }
        if ( proposalSearcher.getTitreOuDescriptionouRef( ) != null )
        {
            stringBuilder.append( " (proposals.titre LIKE ? OR proposals.description LIKE ? OR proposals.id_proposal = ? ) AND" );
        }
        if ( proposalSearcher.getTypeQpvQva( ) != null )
        {
            if ( ProposalSearcher.QPVQVA_UNKNOWN.equals( proposalSearcher.getTypeQpvQva( ) ) )
            {
                stringBuilder.append( " proposals.type_nqpv_qva != ? AND" );
                stringBuilder.append( " proposals.type_nqpv_qva != ? AND" );
                stringBuilder.append( " proposals.type_nqpv_qva != ? AND" );
                stringBuilder.append( " proposals.type_nqpv_qva != ? AND" );
                stringBuilder.append( " proposals.type_nqpv_qva != ? AND" );
                stringBuilder.append( " proposals.type_nqpv_qva != ? AND" );
            }
            else
            {
                stringBuilder.append( " proposals.type_nqpv_qva = ? AND" );
            }
        }
        if ( IdeationApp.HANDICAP_LABEL_YES.equals( proposalSearcher.getHandicap( ) ) )
        {
            stringBuilder.append( " proposals.handicap = ? AND" );
        }
        else
            if ( IdeationApp.HANDICAP_LABEL_NO.equals( proposalSearcher.getHandicap( ) ) )
            {
                stringBuilder.append( " ( (proposals.handicap IS NULL) OR (proposals.handicap = ?) ) AND" );
            }

        if ( proposalSearcher.getTypeLocation( ) != null )
        {
            stringBuilder.append( " proposals.location_type = ? AND" );
        }
        if ( proposalSearcher.getArrondissement( ) != null )
        {
            stringBuilder.append( " proposals.location_ardt = ? AND" );
        }
        if ( proposalSearcher.getStatusPublic( ) != null )
        {
            stringBuilder.append( " proposals.status_public = ? AND" );
        }
        if ( proposalSearcher.getLuteceUserName( ) != null )
        {
            stringBuilder.append( " proposals.lutece_user_name = ? AND" );
        }

        if ( proposalSearcher.getIsPublished( ) != null )
        {
            stringBuilder.append( getFilterPublishedOrNot( proposalSearcher.getIsPublished( ) ) );
        }

        if ( stringBuilder.length( ) > 0 )
        {
            // Remove the final " AND"
            stringBuilder.setLength( stringBuilder.length( ) - 4 );
        }

        // Create the order by clause without SQL Injection
        String strOrder = ProposalSearcher.ORDER_ASC.equals( proposalSearcher.getOrderAscDesc( ) )
                || ProposalSearcher.ORDER_DESC.equals( proposalSearcher.getOrderAscDesc( ) ) ? proposalSearcher.getOrderAscDesc( ) : null;
        StringBuilder stringBuilderOrderBy = new StringBuilder( );
        if ( ProposalSearcher.COLUMN_REFERENCE.equals( proposalSearcher.getOrderColumn( ) ) )
        {
            // COLUMN_REFERENCE means lexicographic sort on code_campaign, code_proposal
            stringBuilderOrderBy.append( "proposals.code_campaign" );
            if ( strOrder != null )
            {
                stringBuilderOrderBy.append( " " );
                stringBuilderOrderBy.append( strOrder );
            }
            stringBuilderOrderBy.append( ", proposals.code_proposal" );
            if ( strOrder != null )
            {
                stringBuilderOrderBy.append( " " );
                stringBuilderOrderBy.append( proposalSearcher.getOrderAscDesc( ) );
            }
        }
        else
        {
            if ( ProposalSearcher.COLUMN_DATE_CREATION.equals( proposalSearcher.getOrderColumn( ) ) )
            {
                stringBuilderOrderBy.append( "proposals." );
                stringBuilderOrderBy.append( proposalSearcher.getOrderColumn( ) );
            }
            if ( stringBuilderOrderBy.length( ) > 0 && strOrder != null )
            {
                stringBuilderOrderBy.append( " " );
                stringBuilderOrderBy.append( proposalSearcher.getOrderAscDesc( ) );
            }
        }

        // Assemble all clauses
        StringBuilder finalQuery = new StringBuilder( );
        finalQuery.append( query );
        if ( stringBuilderJoin.length( ) > 0 )
        {
            finalQuery.append( " " );
            finalQuery.append( stringBuilderJoin.toString( ) );
        }
        if ( stringBuilder.length( ) > 0 )
        {
            finalQuery.append( " WHERE " );
            finalQuery.append( stringBuilder.toString( ) );
        }
        if ( stringBuilderOrderBy.length( ) > 0 )
        {
            finalQuery.append( " ORDER BY " );
            finalQuery.append( stringBuilderOrderBy );
        }

        return finalQuery.toString( );
    }

    /**
     * Sets the proposalSearcher values for export and search
     * 
     * @param daoUtil
     *            The daoUtil
     * @param validatedFilters
     *            The validatedFilters
     */
    private void setFilterValues( DAOUtil daoUtil, ProposalSearcher proposalSearcher )
    {
        int nCpt = 1;
        if ( proposalSearcher.getCodeCampaign( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getCodeCampaign( ) );
        }
        if ( proposalSearcher.getCodeTheme( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getCodeTheme( ) );
        }
        if ( proposalSearcher.getExportedTag( ) != null )
        {
            daoUtil.setInt( nCpt++, proposalSearcher.getExportedTag( ) );
        }
        if ( proposalSearcher.getTitreOuDescriptionouRef( ) != null )
        {
            daoUtil.setString( nCpt++, "%" + proposalSearcher.getTitreOuDescriptionouRef( ) + "%" );
            daoUtil.setString( nCpt++, "%" + proposalSearcher.getTitreOuDescriptionouRef( ) + "%" );

            if ( proposalSearcher.getTitreOuDescriptionouRef( ).matches( "\\d+" ) )
            {
                daoUtil.setInt( nCpt++, Integer.parseInt( proposalSearcher.getTitreOuDescriptionouRef( ) ) );
            }
            else
            {
                daoUtil.setString( nCpt++, StringUtils.EMPTY );
            }
        }
        if ( proposalSearcher.getTypeQpvQva( ) != null )
        {
            if ( ProposalSearcher.QPVQVA_UNKNOWN.equals( proposalSearcher.getTypeQpvQva( ) ) )
            {
                daoUtil.setString( nCpt++, IdeationApp.QPV_QVA_ERR );
                daoUtil.setString( nCpt++, IdeationApp.QPV_QVA_NO );
                daoUtil.setString( nCpt++, IdeationApp.QPV_QVA_QPV );
                daoUtil.setString( nCpt++, IdeationApp.QPV_QVA_QVA );
                daoUtil.setString( nCpt++, IdeationApp.QPV_QVA_GPRU );
                daoUtil.setString( nCpt++, IdeationApp.QPV_QVA_QBP );
            }
            else
            {
                daoUtil.setString( nCpt++, proposalSearcher.getTypeQpvQva( ) );
            }
        }
        if ( proposalSearcher.getHandicap( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getHandicap( ) );
        }
        if ( proposalSearcher.getTypeLocation( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getTypeLocation( ) );
        }
        if ( proposalSearcher.getArrondissement( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getArrondissement( ) );
        }
        if ( proposalSearcher.getLuteceUserName( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getLuteceUserName( ) );
        }
        if ( proposalSearcher.getStatusPublic( ) != null )
        {
            daoUtil.setString( nCpt++, proposalSearcher.getStatusPublic( ) );
        }

    }

    private static String getFilterPublishedOrNot( boolean bPublished )
    {
        StringBuffer strBuffer = new StringBuffer( );
        strBuffer.append( " proposals.status_public in (" );
        for ( Status status : bPublished ? Proposal.Status.getAllStatusPublished( ) : Proposal.Status.getAllStatusUnPublished( ) )
        {
            strBuffer.append( "'" );
            strBuffer.append( status.getValeur( ) );
            strBuffer.append( "'" );
            strBuffer.append( "," );
        }
        // remove last ,
        if ( strBuffer.length( ) != 0 )
        {
            strBuffer.setLength( strBuffer.length( ) - 1 );
        }
        strBuffer.append( ") AND" );
        return strBuffer.toString( );

    }

    /**
     * Generates a new proposal_link primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKeyProposalLink( Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_PROPOSAL_LINK, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }

        return nKey;
    }

    private void insertLink( int nIdParentProposal, int nIdChildProposal, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_LINK, plugin ) )
        {
            daoUtil.setInt( 1, newPrimaryKeyProposalLink( plugin ) );
            daoUtil.setInt( 2, nIdParentProposal );
            daoUtil.setInt( 3, nIdChildProposal );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteLinkByParent( int nParentProposalId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK_BY_PARENT, plugin ) )
        {
            daoUtil.setInt( 1, nParentProposalId );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteLinkByChild( int nChildProposalId, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK_BY_CHILD, plugin ) )
        {
            daoUtil.setInt( 1, nChildProposalId );
            daoUtil.executeUpdate( );
        }
    }
}
