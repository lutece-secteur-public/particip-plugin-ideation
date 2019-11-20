/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
 
package fr.paris.lutece.plugins.ideation.business;

import fr.paris.lutece.portal.service.plugin.Plugin;

import java.util.Collection;
import java.util.List;



/**
 * IAtelierDAO Interface
 */
public interface IAtelierDAO
{
    /**
     * Insert a new record in the table.
     * @param atelier instance of the Atelier object to insert
     * @param plugin the Plugin
     */
    void insert( Atelier atelier, Plugin plugin );

    /**
     * Update the record in the table
     * @param atelier the reference of the Atelier
     * @param plugin the Plugin
     */
    void store( Atelier atelier, Plugin plugin );
    
    /**
     * Delete records from the association table by atelier id
     * @param nIdAtelier The identifier of the Atelier to delete
     * @param plugin the Plugin
     */
    void deleteAssociationsByIdAtelier( int nIdAtelier, Plugin plugin );

    /**
     * Delete records from the association table by idee id
     * @param nIdIdee The identifier of the Idee to delete
     * @param plugin the Plugin
     */
    void deleteAssociationsByIdIdee( int nIdIdee, Plugin plugin );

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Load the data from the table
     * @param nKey The identifier of the atelier
     * @param plugin the Plugin
     * @return The instance of the atelier
     */
    Atelier load( int nKey, Plugin plugin );

    /**
     * Load the data of all the atelier objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the data of all the atelier objects
     */
    Collection<Atelier> selectAteliersList( Plugin plugin );
    
    /**
     * Load the id of all the atelier objects and returns them as a collection
     * @param plugin the Plugin
     * @return The collection which contains the id of all the atelier objects
     */
    Collection<Integer> selectIdAteliersList( Plugin plugin );

    /**
     * Load the id of all the idees objects associated with an atelier and returns them as a collection
     * @param nIdAtelier The identifier of the atelier
     * @param plugin the Plugin
     * @return The collection which contains the id of all the idee objects
     */
    Collection<Integer> selectIdeeIdsByAtelier( int nIdAtelier, Plugin plugin );

    /**
     * Load the Atelier by Id the Idee
     * @param nIdIdee  the id of Idee
     * @param plugin the Plugin
     * @return Atelier the atelier
     */
    Atelier loadAtelierByIdee( int nIdIdee, Plugin plugin );
    
    /**
     * Load the list if idees by atelier
     * @param nIdAtelier  the id of Atelier
     * @param plugin the Plugin
     * @return list of Idees
     */
    List <Idee> loadIdeesByAtelier( int nIdAtelier, Plugin plugin ) ;
    
    /**
     * 
     * @param atelierSearcher the atelier search
     * @param plugin the plugin 
     * @return list of atliers
     */
	Collection<Atelier> selectAteliersListSearch( AtelierSearcher atelierSearcher, Plugin plugin );

}

