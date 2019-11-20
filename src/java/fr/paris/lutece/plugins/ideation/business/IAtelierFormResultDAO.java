
/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *	 and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *	 and the following disclaimer in the documentation and/or other materials
 *	 provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *	 contributors may be used to endorse or promote products derived from
 *	 this software without specific prior written permission.
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
 
package fr.paris.lutece.plugins.ideation.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.Collection;
import java.util.List;



/**
 * IAtelierFormResultDAO Interface
 */
public interface IAtelierFormResultDAO
{
    /**
     * Insert a new record in the table.
     * @param atelierFormResult instance of the AtelierFormResult object to insert
     * @param plugin the Plugin
     */
    void insert( AtelierFormResult atelierFormResult, Plugin plugin );

    /**
     * Update the record in the table
     * @param atelierFormResult the reference of the AtelierFormResult
     * @param plugin the Plugin
     */
    void store( AtelierFormResult atelierFormResult, Plugin plugin );

    /**
     * Delete a record from the table
     * @param nKey The identifier of the AtelierFormResult to delete
     * @param plugin the Plugin
     */
    void delete( int nKey, Plugin plugin );

    /**
     * Delete all the AtelierFormResult associated with the AtelierForm
     * @param atelierForm The AtelierForm
     * @param plugin the Plugin
     */
    void deleteByAtelierForm( AtelierForm atelierForm, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param nKey The identifier of the atelierFormResult
     * @param plugin the Plugin
     * @return The instance of the atelierFormResult
     */
    AtelierFormResult load( int nKey, Plugin plugin );

    /**
     * Load the data from the table
     * @param nIdAtelierForm The identifier of the atelier form
     * @param strGuid the Guid
     * @param plugin the Plugin
     * @return The instance of the atelierFormResult
     */
    AtelierFormResult load( int nIdAtelierForm, String strGuid, Plugin plugin );

    /**
     * Load the data of all the atelierFormResult objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the data of all the atelierFormResult objects
     */
    Collection<AtelierFormResult> selectAtelierFormResultsList( Plugin plugin );

    /**
     * Load the data of all the atelierFormResult objects and returns them as a collection
     * @param plugin the Plugin
     * @param nIdAtelierForm nIdAtelierForm
     * @return The collection which contains the data of all the atelierFormResult objects
     */
    Collection<AtelierFormResult> selectAtelierFormResultsList(int nIdAtelierForm, Plugin plugin );
    
    /**
     * Load the id of all the atelierFormResult objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the id of all the atelierFormResult objects
     */
    Collection<Integer> selectIdAtelierFormResultsList( Plugin plugin );

    /**
     * Load the data of the atelierFormResultEntry objects by atelier form result and returns them in form of a collection
     * @param atelierFormResult
     * @param plugin the Plugin
     * @return The collection which contains the data of the atelierFormResultEntry objects
     */
    List<AtelierFormResultEntry> selectAtelierFormResultEntrysByAtelierFormResult( AtelierFormResult atelierFormResult, Plugin plugin );

    /**
     * Returns the number where an alternative had been selected
     * @param nIdAtelierForm The atelierForm Id
     * @param nIdAtelierFormEntry The atelierFormEntry Id
     * @param nChoix The choice
     * @param plugin the Plugin
     * @return The number where an alternative had been selected
     */
    Integer getEntryResult( int nIdAtelierForm, int nIdAtelierFormEntry, int nChoix, Plugin plugin );

    /**
     * Returns the number of user that has voted a form
     * @param nIdAtelierForm The atelierForm Id
     * @param plugin the Plugin
     * @return The number of user that has voted a form
     */
    Integer getNbVotes( int nIdAtelierForm, Plugin plugin );

    /**
     * Returns the number where an alternative of the title had been selected
     * @param nIdAtelierForm The atelierForm Id
     * @param nChoix The choice
     * @param plugin the Plugin
     * @return The number where an alternative of the title had been selected
     */
    Integer getTitreResult( int nIdAtelierForm, int nChoix, Plugin plugin );

    /**
     * Returns the number where an alternative of the description had been selected
     * @param nIdAtelierForm The atelierForm Id
     * @param nChoix The choice
     * @param plugin the Plugin
     * @return The number where an alternative of the description had been selected
     */
    Integer getDescriptionResult( int nIdAtelierForm, int nChoix, Plugin plugin );
}

