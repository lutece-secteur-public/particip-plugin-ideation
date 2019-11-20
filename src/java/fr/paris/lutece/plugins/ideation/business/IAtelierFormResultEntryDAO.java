
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
 * IAtelierFormResultEntryDAO Interface
 */
public interface IAtelierFormResultEntryDAO
{
    /**
     * Insert a new record in the table.
     * @param atelierFormResultEntry instance of the AtelierFormResultEntry object to insert
     * @param plugin the Plugin
     */
    void insert( AtelierFormResultEntry atelierFormResultEntry, Plugin plugin );

    /**
     * Update the record in the table
     * @param atelierFormResultEntry the reference of the AtelierFormResultEntry
     * @param plugin the Plugin
     */
    void store( AtelierFormResultEntry atelierFormResultEntry, Plugin plugin );

    /**
     * Delete the records by the identifier of the atelierFormResult
     * @param nKey The identifier of all the AtelierFormResultEntrys to delete
     * @param plugin the Plugin
     */
    void deleteByIdFormResult( int nKey, Plugin plugin );
    
    /**
     * Delete the records by the identifier of the atelierFormEntry
     * @param nKey The identifier of all the AtelierFormResultEntrys to delete
     * @param plugin the Plugin
     */
    void deleteByIdFormEntry( int nKey, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param nIdAtelierFormResult The identifier of the atelierFormResult
     * @param nIdAtelierFormEntry The identifier of the atelierFormEntry
     * @param plugin the Plugin
     * @return The instance of the atelierFormResultEntry
     */
    AtelierFormResultEntry load( int nIdAtelierFormResult, int nIdAtelierFormEntry, Plugin plugin );

    /**
     * Load the data of all the atelierFormResultEntry objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the data of all the atelierFormResultEntry objects
     */
    Collection<AtelierFormResultEntry> selectAtelierFormResultEntrysList( Plugin plugin );
    
    /**
     * Load the id of all the atelierFormResultEntry objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the id of all the atelierFormResultEntry objects
     */
    Collection<Integer> selectIdAtelierFormResultEntrysList( Plugin plugin );
    
    /**
     * Load the data of all the atelierFormResultEntry objects where the atelier form result id is specified in parameter
     * and returns them in form of a list
     * @param nIdAtelierFormResult The atelier form result identifier
     * @return the list which contains the data of all the atelierFormResultEntry objects
     */
    List<AtelierFormResultEntry> selectAtelierFormResultEntrysListByIdAtelierFormResult( int nIdAtelierFormResult, Plugin plugin );
}

