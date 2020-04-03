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
package fr.paris.lutece.plugins.participatoryideation.business.depositary;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class provides instances management methods (create, find, ...) for DepositaryType objects
 */

public final class DepositaryTypeHome
{
    // Static variable pointed at the DAO instance

    private static IDepositaryTypeDAO _dao = SpringContextService.getBean( "participatoryideation.depositaryTypeDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DepositaryTypeHome( )
    {
    }

    /**
     * Create an instance of the depositaryType class
     * 
     * @param depositaryType
     *            The instance of the DepositaryType which contains the informations to store
     * @return The instance of depositaryType which has been created with its primary key.
     */
    public static DepositaryType create( DepositaryType depositaryType )
    {
        _dao.insert( depositaryType, _plugin );

        return depositaryType;
    }

    /**
     * Update of the depositaryType which is specified in parameter
     * 
     * @param depositaryType
     *            The instance of the DepositaryType which contains the data to store
     * @return The instance of the depositaryType which has been updated
     */
    public static DepositaryType update( DepositaryType depositaryType )
    {
        _dao.store( depositaryType, _plugin );

        return depositaryType;
    }

    /**
     * Remove the depositaryType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositaryType Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a depositaryType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositaryType primary key
     * @return an instance of DepositaryType
     */
    public static DepositaryType findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the depositaryType objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the depositaryType objects
     */
    public static Collection<DepositaryType> getDepositaryTypesList( )
    {
        return _dao.selectDepositaryTypesList( _plugin );
    }

    /**
     * Load the id of all the depositaryType objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the depositaryType objects
     */
    public static Collection<Integer> getIdDepositaryTypesList( )
    {
        return _dao.selectIdDepositaryTypesList( _plugin );
    }

    /**
     * Load the data of all the depositaryType objects for a campaign and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the depositaryType objects
     */
    public static Collection<DepositaryType> getDepositaryTypesListByCampaign( String strCampaignCode )
    {
        return _dao.selectDepositaryTypesListByCampaign( strCampaignCode, _plugin );
    }

    /**
     * Returns an instance of a depositaryType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositaryType primary key
     * @return an instance of DepositaryType
     */
    public static DepositaryType findByCode( String strCode )
    {
        return _dao.load( strCode, _plugin );
    }

    /**
     * Load the data of all the depositaryType objects mapped to a campaign and returns them in form of a map
     * 
     * @return the collection which contains the data of all the depositaryType objects
     */
    public static Map<String, List<DepositaryType>> getDepositaryTypesMapByCampaign( )
    {
        return _dao.selectDepositaryTypesMapByCampaign( _plugin );
    }

}
