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

import java.util.Collection;

import fr.paris.lutece.portal.service.plugin.Plugin;

/**
 * IProposalDAO Interface
 */
public interface IProposalDAO
{
    /**
     * Insert a new record in the table.
     * 
     * @param proposal
     *            instance of the Proposal object to insert
     * @param plugin
     *            the Plugin
     */
    void insert( Proposal proposal, Plugin plugin );

    /**
     * Update the record in the table from the BO (only some fields)
     * 
     * @param proposal
     *            the reference of the Proposal
     * @param plugin
     *            the Plugin
     */
    void storeBO( Proposal proposal, Plugin plugin );

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * 
     * @param nKey
     *            The identifier of the proposal
     * @param plugin
     *            the Plugin
     * @return The instance of the proposal
     */
    Proposal load( int nKey, Plugin plugin );

    /**
     * Load the data from the table
     * 
     * @param strCodeCampaign
     *            The campaign code
     * @param nCodeProposal
     *            The proposal code
     * @param plugin
     *            the Plugin
     * @return The instance of the proposal
     */
    Proposal loadByCodes( String strCodeCampaign, int nCodeProposal, Plugin plugin );

    /**
     * Load the data of all the proposal objects and returns them as a collection
     * 
     * @param plugin
     *            the Plugin
     * @return The collection which contains the data of all the proposal objects
     */
    Collection<Proposal> selectProposalsList( Plugin plugin );

    /**
     * Load the data of all the proposal objects matching the searc and returns them as a collection
     * 
     * @param plugin
     *            the Plugin
     * @return The collection which contains the data of all the proposal objects
     */
    Collection<Proposal> selectProposalsListSearch( Plugin plugin, ProposalSearcher proposalSearcher );

    /**
     * Load the id of all the proposal objects and returns them as a collection
     * 
     * @param plugin
     *            the Plugin
     * @return The collection which contains the id of all the proposal objects
     */
    Collection<Integer> selectIdProposalsList( Plugin plugin );

    /**
     * Check if the proposal has a parent
     * 
     * @param nIdProposal
     *            The identifier of the proposal
     * @param plugin
     *            The Plugin
     * @return The identifier of the parent proposal, or 0
     */
    int hasParent( int nIdProposal, Plugin plugin );

    /**
     * Delete all the links associated with the parent proposal
     * 
     * @param nParentProposalId
     *            The identifier of the parent proposal
     * @param plugin
     *            the Plugin
     */
    void deleteLinkByParent( int nParentProposalId, Plugin plugin );

    /**
     * Delete all the links associated with the child proposal
     * 
     * @param nChildProposalId
     *            The identifier of the child proposal
     * @param plugin
     *            the Plugin
     */
    void deleteLinkByChild( int nChildProposalId, Plugin plugin );
}
