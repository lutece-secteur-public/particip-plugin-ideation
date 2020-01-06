/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatoryideation.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import java.util.Collection;



/**
 * IIdeeDAO Interface
 */
public interface IIdeeDAO
{
    /**
     * Insert a new record in the table.
     * @param idee instance of the Idee object to insert
     * @param plugin the Plugin
     */
    void insert( Idee idee, Plugin plugin );

    /**
     * Update the record in the table from the BO (only some fields)
     * @param idee the reference of the Idee
     * @param plugin the Plugin
     */
    void storeBO( Idee idee, Plugin plugin );


    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param nKey The identifier of the idee
     * @param plugin the Plugin
     * @return The instance of the idee
     */
    Idee load( int nKey, Plugin plugin );

    /**
     * Load the data from the table
     * @param strCodeCampagne The campagne code
     * @param nCodeIdee The idee code
     * @param plugin the Plugin
     * @return The instance of the idee
     */
    Idee loadByCodes( String strCodeCampagne, int nCodeIdee, Plugin plugin );

    /**
     * Load the data of all the idee objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the data of all the idee objects
     */
    Collection<Idee> selectIdeesList( Plugin plugin );

    /**
     * Load the data of all the idee objects matching the searc and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the data of all the idee objects
     */
    Collection<Idee> selectIdeesListSearch( Plugin plugin, IdeeSearcher ideeSearcher );

    /**
     * Load the id of all the idee objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the id of all the idee objects
     */
    Collection<Integer> selectIdIdeesList( Plugin plugin );
    
    /**
     * Check if the idee has a parent
     * @param nIdIdee The identifier of the idee
     * @param plugin The Plugin
     * @return The identifier of the parent idee, or 0
     */
    int hasParent( int nIdIdee, Plugin plugin );

    /**
     * Delete all the links associated with the parent idee
     * @param nParentIdeeId The identifier of the parent idee
     * @param plugin the Plugin
     */
    void deleteLinkByParent( int nParentIdeeId, Plugin plugin );

    /**
     * Delete all the links associated with the child idee
     * @param nChildIdeeId The identifier of the child idee
     * @param plugin the Plugin
     */
    void deleteLinkByChild( int nChildIdeeId, Plugin plugin );
}

