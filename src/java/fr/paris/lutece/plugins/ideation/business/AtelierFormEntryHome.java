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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for AtelierFormEntry objects
 */

public final class AtelierFormEntryHome
{
    // Static variable pointed at the DAO instance

    private static IAtelierFormEntryDAO _dao = SpringContextService.getBean( "ideation.atelierFormEntryDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "ideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AtelierFormEntryHome(  )
    {
    }

    /**
     * Create an instance of the atelierFormEntry class
     * @param atelierFormEntry The instance of the AtelierFormEntry which contains the informations to store
     * @return The  instance of atelierFormEntry which has been created with its primary key.
     */
    public static AtelierFormEntry create( AtelierFormEntry atelierFormEntry )
    {
        _dao.insert( atelierFormEntry, _plugin );

        return atelierFormEntry;
    }

    /**
     * Update of the atelierFormEntry which is specified in parameter
     * @param atelierFormEntry The instance of the AtelierFormEntry which contains the data to store
     * @return The instance of the  atelierFormEntry which has been updated
     */
    public static AtelierFormEntry update( AtelierFormEntry atelierFormEntry )
    {
        _dao.store( atelierFormEntry, _plugin );

        return atelierFormEntry;
    }

    /**
     * Remove the atelierFormEntry whose identifier is specified in parameter
     * @param nKey The atelierFormEntry Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a atelierFormEntry whose identifier is specified in parameter
     * @param nKey The atelierFormEntry primary key
     * @return an instance of AtelierFormEntry
     */
    public static AtelierFormEntry findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the atelierFormEntry objects and returns them in form of a collection
     * @return the collection which contains the data of all the atelierFormEntry objects
     */
    public static List<AtelierFormEntry> getAtelierFormEntrysList( )
    {
        return _dao.selectAtelierFormEntrysList( _plugin );
    }
    
    /**
     * Load the id of all the atelierFormEntry objects and returns them in form of a collection
     * @return the collection which contains the id of all the atelierFormEntry objects
     */
    public static List<Integer> getIdAtelierFormEntrysList( )
    {
        return _dao.selectIdAtelierFormEntrysList( _plugin );
    }
    
    /**
     * Load the data of the atelierFormEntry objects by atelier form and returns them in form of a collection
     * @return the collection which contains the data of the atelierFormEntry objects AtelierForm atelierForm
     */
    public static List<AtelierFormEntry> getAtelierFormEntrysByAtelierForm( AtelierForm atelierForm )
    {
        return _dao.selectAtelierFormEntrysListByAtelierForm( atelierForm, _plugin );
    }
    
    /**
     * Load the data of the atelierFormEntry complementaires objects by atelier form and returns them in form of a collection
     * @return the collection which contains the data of the atelierFormEntry objects AtelierForm atelierForm
     */
    public static List<AtelierFormEntry> getFormEntriesComplByAtelierForm( AtelierForm atelierForm )
    {
        return _dao.selectFormEntriesComplByAtelierForm( atelierForm, _plugin );
    }
    
}

