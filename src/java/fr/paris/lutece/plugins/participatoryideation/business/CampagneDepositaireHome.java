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
 
package fr.paris.lutece.plugins.participatoryideation.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;

/**
 * This class provides instances management methods (create, find, ...) for CampagneDepositaire objects
 */

public final class CampagneDepositaireHome
{
    // Static variable pointed at the DAO instance

    private static ICampagneDepositaireDAO _dao = SpringContextService.getBean( "ideation.campagneDepositaireDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "ideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private CampagneDepositaireHome(  )
    {
    }

    /**
     * Create an instance of the campagneDepositaire class
     * @param campagneDepositaire The instance of the CampagneDepositaire which contains the informations to store
     * @return The  instance of campagneDepositaire which has been created with its primary key.
     */
    public static CampagneDepositaire create( CampagneDepositaire campagneDepositaire )
    {
        _dao.insert( campagneDepositaire, _plugin );

        return campagneDepositaire;
    }

    /**
     * Update of the campagneDepositaire which is specified in parameter
     * @param campagneDepositaire The instance of the CampagneDepositaire which contains the data to store
     * @return The instance of the  campagneDepositaire which has been updated
     */
    public static CampagneDepositaire update( CampagneDepositaire campagneDepositaire )
    {
        _dao.store( campagneDepositaire, _plugin );

        return campagneDepositaire;
    }

    /**
     * Remove the campagneDepositaire whose identifier is specified in parameter
     * @param nKey The campagneDepositaire Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a campagneDepositaire whose identifier is specified in parameter
     * @param nKey The campagneDepositaire primary key
     * @return an instance of CampagneDepositaire
     */
    public static CampagneDepositaire findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the campagneDepositaire objects and returns them in form of a collection
     * @return the collection which contains the data of all the campagneDepositaire objects
     */
    public static Collection<CampagneDepositaire> getCampagneDepositairesList( )
    {
        return _dao.selectCampagneDepositairesList( _plugin );
    }
    
    /**
     * Load the id of all the campagneDepositaire objects and returns them in form of a collection
     * @return the collection which contains the id of all the campagneDepositaire objects
     */
    public static Collection<Integer> getIdCampagneDepositairesList( )
    {
        return _dao.selectIdCampagneDepositairesList( _plugin );
    }

    /**
     * Load the data of all the campagneDepositaire objects for a campagne and returns them in form of a collection
     * @return the collection which contains the data of all the campagneDepositaire objects
     */
    public static Collection<CampagneDepositaire> getCampagneDepositaireListByCampagne( String codeCampagne )
    {
        return _dao.selectCampagneDepositaireListByCampagne( codeCampagne, _plugin );
    }
}

