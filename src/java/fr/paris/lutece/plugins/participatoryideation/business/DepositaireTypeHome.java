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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class provides instances management methods (create, find, ...) for DepositaireType objects
 */

public final class DepositaireTypeHome
{
    // Static variable pointed at the DAO instance

    private static IDepositaireTypeDAO _dao = SpringContextService.getBean( "participatoryideation.depositaireTypeDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DepositaireTypeHome( )
    {
    }

    /**
     * Create an instance of the depositaireType class
     * 
     * @param depositaireType
     *            The instance of the DepositaireType which contains the informations to store
     * @return The instance of depositaireType which has been created with its primary key.
     */
    public static DepositaireType create( DepositaireType depositaireType )
    {
        _dao.insert( depositaireType, _plugin );

        return depositaireType;
    }

    /**
     * Update of the depositaireType which is specified in parameter
     * 
     * @param depositaireType
     *            The instance of the DepositaireType which contains the data to store
     * @return The instance of the depositaireType which has been updated
     */
    public static DepositaireType update( DepositaireType depositaireType )
    {
        _dao.store( depositaireType, _plugin );

        return depositaireType;
    }

    /**
     * Remove the depositaireType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositaireType Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a depositaireType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositaireType primary key
     * @return an instance of DepositaireType
     */
    public static DepositaireType findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the depositaireType objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the depositaireType objects
     */
    public static Collection<DepositaireType> getDepositaireTypesList( )
    {
        return _dao.selectDepositaireTypesList( _plugin );
    }

    /**
     * Load the id of all the depositaireType objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the depositaireType objects
     */
    public static Collection<Integer> getIdDepositaireTypesList( )
    {
        return _dao.selectIdDepositaireTypesList( _plugin );
    }

    /**
     * Load the data of all the depositaireType objects for a campagne and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the depositaireType objects
     */
    public static Collection<DepositaireType> getDepositaireTypesListByCampagne( String strCampagneCode )
    {
        return _dao.selectDepositaireTypesListByCampagne( strCampagneCode, _plugin );
    }

    /**
     * Returns an instance of a depositaireType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositaireType primary key
     * @return an instance of DepositaireType
     */
    public static DepositaireType findByCode( String strCode )
    {
        return _dao.load( strCode, _plugin );
    }

    /**
     * Load the data of all the depositaireType objects mapped to a campagne and returns them in form of a map
     * 
     * @return the collection which contains the data of all the depositaireType objects
     */
    public static Map<String, List<DepositaireType>> getDepositaireTypesMapByCampagne( )
    {
        return _dao.selectDepositaireTypesMapByCampagne( _plugin );
    }

}
