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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Proposal objects
 */

public final class ProposalHome
{
    // Static variable pointed at the DAO instance

    private static IProposalDAO _dao = SpringContextService.getBean( "participatoryideation.proposalDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private ProposalHome( )
    {
    }

    /**
     * Create an instance of the proposal class
     * 
     * @param proposal
     *            The instance of the Proposal which contains the informations to store
     * @return The instance of proposal which has been created with its primary key.
     */
    public static Proposal create( Proposal proposal )
    {
        _dao.insert( proposal, _plugin );

        return proposal;
    }

    /**
     * Update of the proposal which is specified in parameter
     * 
     * @param proposal
     *            The instance of the Proposal which contains the data to store
     * @return The instance of the proposal which has been updated
     */
    public static Proposal updateBO( Proposal proposal )
    {
        _dao.storeBO( proposal, _plugin );

        return proposal;
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a proposal whose identifier is specified in parameter
     * 
     * @param nKey
     *            The proposal primary key
     * @return an instance of Proposal
     */
    public static Proposal findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a proposal whose codes is specified in parameters
     * 
     * @param strCodeCampaign
     *            The campaign code
     * @param nCodeProposal
     *            The proposal code
     * @return an instance of Proposal
     */
    public static Proposal findByCodes( String strCodeCampaign, int nCodeProposal )
    {
        return _dao.loadByCodes( strCodeCampaign, nCodeProposal, _plugin );
    }

    /**
     * Load the data of all the proposal objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the proposal objects
     */
    public static Collection<Proposal> getProposalsList( )
    {
        return _dao.selectProposalsList( _plugin );
    }

    /**
     * Load the data of all the proposal objects searched and returns them in form of a collection
     * 
     * @param proposalSearcher
     *            a ProposalSearcher
     * @return the collection which contains the data of all the proposal objects
     */
    public static Collection<Proposal> getProposalsListSearch( ProposalSearcher proposalSearcher )
    {
        return _dao.selectProposalsListSearch( _plugin, proposalSearcher );
    }

    /**
     * Load the id of all the proposal objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the proposal objects
     */
    public static Collection<Integer> getIdProposalsList( )
    {
        return _dao.selectIdProposalsList( _plugin );
    }

    /**
     * Check if the proposal has a parent
     * 
     * @param nIdProposal
     *            The identifier of the proposal
     * @return The identifier of the parent proposal, or 0
     */
    public static int hasParent( int nIdProposal )
    {
        return _dao.hasParent( nIdProposal, _plugin );
    }

    /**
     * Delete all the links associated with the parent proposal
     * 
     * @param nParentProposalId
     *            The identifier of the parent proposal
     */
    public static void removeLinkByParent( int nParentProposalId )
    {
        _dao.deleteLinkByParent( nParentProposalId, _plugin );
    }

    /**
     * Delete all the links associated with the child proposal
     * 
     * @param nChildProposalId
     *            The identifier of the child proposal
     */
    public static void removeLinkByChild( int nChildProposalId )
    {
        _dao.deleteLinkByChild( nChildProposalId, _plugin );
    }

    /**
     * Load the parent and children proposals from the ids
     * 
     * @param proposal
     *            The Proposal with child and parents lists not null.
     */
    public static void loadMissingLinkedProposals( Proposal proposal )
    {
        // Use the mandatory title to check if a proposal has been fully loaded from the database.
        List<Proposal> listChildProposals = proposal.getChildProposals( );
        for ( int i = 0; i < listChildProposals.size( ); i++ )
        {
            if ( listChildProposals.get( i ).getTitre( ) == null )
            {
                proposal.getChildProposals( ).set( i, ProposalHome.findByPrimaryKey( listChildProposals.get( i ).getId( ) ) );
            }
        }
        List<Proposal> listParentProposals = proposal.getParentProposals( );
        for ( int i = 0; i < listParentProposals.size( ); i++ )
        {
            if ( listParentProposals.get( i ).getTitre( ) == null )
            {
                proposal.getParentProposals( ).set( i, ProposalHome.findByPrimaryKey( listParentProposals.get( i ).getId( ) ) );
            }
        }
    }

    /*
     * *********************************************************************************** SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS
     * SUB_INFOS * SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS
     * ***********************************************************************************
     */

    /**
     * Method of getting infos of sub ideas.
     */
    public enum GetSubProposalsMethod
    {
        /**
         * Only of this idea.
         */
        THIS_ONLY,

        /**
         * Of the idea and its direct children.
         */
        THIS_AND_CHILDREN,

        /**
         * Of the idea, its direct children, and sub-children.
         */
        ALL_FAMILY
    };

    /**
     * Returns a list of sub ideas.
     */
    public static List<Integer> getSubProposalsId( int nProposalId, GetSubProposalsMethod method )
    {

        List<Integer> _ids = new ArrayList<Integer>( );
        _ids.add( nProposalId );

        // Appends ids of children, recursively or only for the first children.
        if ( method != GetSubProposalsMethod.THIS_ONLY )
        {
            Proposal _proposal = ProposalHome.findByPrimaryKey( nProposalId );
            List<Proposal> children = _proposal.getChildProposals( );
            if ( children != null )
            {
                for ( Proposal proposalChild : children )
                {
                    if ( proposalChild != null )
                    {
                        GetSubProposalsMethod _newMethod = ( method == GetSubProposalsMethod.THIS_AND_CHILDREN ? GetSubProposalsMethod.THIS_ONLY
                                : GetSubProposalsMethod.ALL_FAMILY );
                        _ids.addAll( getSubProposalsId( proposalChild.getId( ), _newMethod ) );
                    }
                }
            }
        }

        return _ids;
    }

    /**
     * Returns a string with the pseudo of idea and eventually its children.
     */
    public static String getSubProposalsNicknames( int nProposalId, GetSubProposalsMethod method, String separator )
    {

        StringBuffer childrenPseudos = new StringBuffer( );

        List<Integer> _nIds = getSubProposalsId( nProposalId, method );

        for ( int nIds : _nIds )
        {
            Proposal _proposal = ProposalHome.findByPrimaryKey( nIds );

            childrenPseudos.append( UserPreferencesService.instance( ).getNickname( _proposal.getLuteceUserName( ) ) );

            switch( _proposal.getSubmitterType( ) )
            {
                case "PARTICULIER":
                    childrenPseudos.append( " (particulier)" ).append( separator );
                    break;
                case "ASSO":
                    childrenPseudos.append( " (association " + _proposal.getSubmitter( ) + ")" ).append( separator );
                    break;
                case "CONSEIL":
                    childrenPseudos.append( " (conseil de quartier " + _proposal.getSubmitter( ) + ")" ).append( separator );
                    break;
                case "AUTRE":
                    childrenPseudos.append( " (autre " + _proposal.getSubmitter( ) + ")" ).append( separator );
                    break;
            }

        }

        String s = childrenPseudos.toString( );
        return s.substring( 0, s.length( ) - separator.length( ) );
    }

    /**
     * Returns a string with the pseudo of idea and eventually its children, nicely structured.
     */
    public static String getSubProposalsNicknamesNiceText( int nProposalId, GetSubProposalsMethod method, String separator )
    {

        StringBuffer childrenPseudos = new StringBuffer( );

        List<Integer> _nIds = getSubProposalsId( nProposalId, method );

        // How many submitters, by type ?
        int nbPart = 0;
        int nbAsso = 0;
        int nbCons = 0;
        int nbAutr = 0;
        for ( int nId : _nIds )
        {
            switch( ProposalHome.findByPrimaryKey( nId ).getSubmitterType( ) )
            {
                case "PARTICULIER":
                    nbPart++;
                    break;
                case "ASSO":
                    nbAsso++;
                    break;
                case "CONSEIL":
                    nbCons++;
                    break;
                case "AUTRE":
                    nbAutr++;
                    break;
            }
        }

        if ( nbPart + nbAsso + nbCons + nbAutr == 0 )
        {
            AppLogService.error( "ProposalHome.getSubProposalsNicknamesNiceText says 'Unable to find submitters for proposition #" + nProposalId + "'." );
            return "Aucun dépositaire n'a été identifié pour ce projet";
        }

        if ( nbAsso == 0 && nbCons == 0 && nbAutr == 0 )
        {
            if ( nbPart == 1 )
            {
                childrenPseudos.append( "Ce projet a été élaboré sur proposition d’un particulier" );
            }
            else
            {
                childrenPseudos.append( "Ce projet a été élaboré à partir d'un regroupement de propositions de particuliers" );
            }
        }
        else
            if ( nbPart == 0 && nbAsso == 1 && nbCons == 0 && nbAutr == 0 )
                childrenPseudos.append( "Ce projet a été élaboré sur proposition de l’association « "
                        + ProposalHome.findByPrimaryKey( _nIds.get( 0 ) ).getSubmitter( ) + " »" );
            else
                if ( nbPart == 0 && nbAsso == 0 && nbCons == 1 && nbAutr == 0 )
                    childrenPseudos.append( "Ce projet a été élaboré sur proposition du conseil de quartier « "
                            + ProposalHome.findByPrimaryKey( _nIds.get( 0 ) ).getSubmitter( ) + " »" );
                else
                    if ( nbPart == 0 && nbAsso == 0 && nbCons == 0 && nbAutr == 1 )
                        childrenPseudos.append( "Ce projet a été élaboré sur proposition du collectif « "
                                + ProposalHome.findByPrimaryKey( _nIds.get( 0 ) ).getSubmitter( ) + " »" );
                    else
                    {
                        childrenPseudos.append( "Ce projet a été élaboré sur proposition de ... " );
                        childrenPseudos.append( getSubProposalsNicknames( nProposalId, method, separator ) );
                    }

        return childrenPseudos.toString( );
    }

}
