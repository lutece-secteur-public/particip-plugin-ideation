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
 * This class provides instances management methods (create, find, ...) for AtelierForm objects
 */

public final class AtelierFormHome
{
    // Static variable pointed at the DAO instance

    private static IAtelierFormDAO _dao = SpringContextService.getBean( "ideation.atelierFormDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "ideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AtelierFormHome(  )
    {
    }

    /**
     * Create an instance of the atelierForm class
     * @param atelierForm The instance of the AtelierForm which contains the informations to store
     * @return The  instance of atelierForm which has been created with its primary key.
     */
    public static AtelierForm create( AtelierForm atelierForm )
    {
        _dao.insert( atelierForm, _plugin );

        return atelierForm;
    }

    /**
     * Update of the atelierForm which is specified in parameter
     * @param atelierForm The instance of the AtelierForm which contains the data to store
     * @return The instance of the  atelierForm which has been updated
     */
    public static AtelierForm update( AtelierForm atelierForm )
    {
        _dao.store( atelierForm, _plugin );

        return atelierForm;
    }

    /**
     * Remove the atelierForm whose identifier is specified in parameter
     * @param nKey The atelierForm Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a atelierForm whose identifier is specified in parameter
     * @param nKey The atelierForm primary key
     * @return an instance of AtelierForm
     */
    public static AtelierForm findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the atelierForm objects and returns them in form of a collection
     * @return the collection which contains the data of all the atelierForm objects
     */
    public static List<AtelierForm> getAtelierFormsList( )
    {
        return _dao.selectAtelierFormsList( _plugin );
    }
    
    /**
     * Load the id of all the atelierForm objects and returns them in form of a collection
     * @return the collection which contains the id of all the atelierForm objects
     */
    public static List<Integer> getIdAtelierFormsList( )
    {
        return _dao.selectIdAtelierFormsList( _plugin );
    }
    
    public static AtelierForm getAtelierFormByAtelier( int nKey )
    {
    	return _dao.loadByAtelier( nKey, _plugin ) ;
    }
}

