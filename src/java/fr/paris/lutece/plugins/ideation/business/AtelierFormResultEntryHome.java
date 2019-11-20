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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for AtelierFormResultEntry objects
 */

public final class AtelierFormResultEntryHome
{
    // Static variable pointed at the DAO instance

    private static IAtelierFormResultEntryDAO _dao = SpringContextService.getBean( "ideation.atelierFormResultEntryDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "ideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AtelierFormResultEntryHome(  )
    {
    }

    /**
     * Create an instance of the atelierFormResultEntry class
     * @param atelierFormResultEntry The instance of the AtelierFormResultEntry which contains the informations to store
     * @return The  instance of atelierFormResultEntry which has been created with its primary key.
     */
    public static AtelierFormResultEntry create( AtelierFormResultEntry atelierFormResultEntry )
    {
        _dao.insert( atelierFormResultEntry, _plugin );

        return atelierFormResultEntry;
    }

    /**
     * Update of the atelierFormResultEntry which is specified in parameter
     * @param atelierFormResultEntry The instance of the AtelierFormResultEntry which contains the data to store
     * @return The instance of the  atelierFormResultEntry which has been updated
     */
    public static AtelierFormResultEntry update( AtelierFormResultEntry atelierFormResultEntry )
    {
        _dao.store( atelierFormResultEntry, _plugin );

        return atelierFormResultEntry;
    }

    /**
     * Remove the atelierFormResultEntry whose identifier is specified in parameter
     * @param nKey The atelierFormResult Id
     */
    public static void removeByIdAtelierFormResult( int nKey )
    {
        _dao.deleteByIdFormResult( nKey, _plugin );
    }
    
    /**
     * Remove the atelierFormResultEntry whose identifier is specified in parameter
     * @param nKey The atelierFormEntry Id
     */
    public static void removeByIdAtelierFormEntry( int nKey )
    {
        _dao.deleteByIdFormEntry( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a atelierFormResultEntry whose identifier is specified in parameter
     * @param nIdAtelierFormResult The atelierFormResultEntry primary key
     * @param nIdAtelierFormEntry The atelierFormResultEntry primary key
     * @return an instance of AtelierFormResultEntry
     */
    public static AtelierFormResultEntry findByPrimaryKey( int nIdAtelierFormResult, int nIdAtelierFormEntry )
    {
        return _dao.load( nIdAtelierFormResult, nIdAtelierFormEntry, _plugin );
    }

    /**
     * Load the data of all the atelierFormResultEntry objects and returns them in form of a collection
     * @return the collection which contains the data of all the atelierFormResultEntry objects
     */
    public static Collection<AtelierFormResultEntry> getAtelierFormResultEntrysList( )
    {
        return _dao.selectAtelierFormResultEntrysList( _plugin );
    }
    
    /**
     * Load the id of all the atelierFormResultEntry objects and returns them in form of a collection
     * @return the collection which contains the id of all the atelierFormResultEntry objects
     */
    public static Collection<Integer> getIdAtelierFormResultEntrysList( )
    {
        return _dao.selectIdAtelierFormResultEntrysList( _plugin );
    }
    
    /**
     * Load the data of all the atelierFormResultEntry objects where the atelier form result id is specified in parameter
     * and returns them in form of a list
     * @param nIdAtelierFormResult The atelier form result identifier
     * @return the list which contains the data of all the atelierFormResultEntry objects
     */
    public static List<AtelierFormResultEntry> getAtelierFormResultEntrysListByIdAtelierFormResult( int nIdAtelierFormResult )
    {
        return _dao.selectAtelierFormResultEntrysListByIdAtelierFormResult( nIdAtelierFormResult, _plugin );
    }
}

