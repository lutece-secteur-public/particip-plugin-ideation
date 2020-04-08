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
package fr.paris.lutece.plugins.participatoryideation.business.submitter;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This class provides instances management methods (create, find, ...) for SubmitterType objects
 */

public final class SubmitterTypeHome
{
    // Static variable pointed at the DAO instance

    private static ISubmitterTypeDAO _dao = SpringContextService.getBean( "participatoryideation.submitterTypeDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private SubmitterTypeHome( )
    {
    }

    /**
     * Create an instance of the submitterType class
     * 
     * @param submitterType
     *            The instance of the SubmitterType which contains the informations to store
     * @return The instance of submitterType which has been created with its primary key.
     */
    public static SubmitterType create( SubmitterType submitterType )
    {
        _dao.insert( submitterType, _plugin );

        return submitterType;
    }

    /**
     * Update of the submitterType which is specified in parameter
     * 
     * @param submitterType
     *            The instance of the SubmitterType which contains the data to store
     * @return The instance of the submitterType which has been updated
     */
    public static SubmitterType update( SubmitterType submitterType )
    {
        _dao.store( submitterType, _plugin );

        return submitterType;
    }

    /**
     * Remove the submitterType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The submitterType Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a submitterType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The submitterType primary key
     * @return an instance of SubmitterType
     */
    public static SubmitterType findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the submitterType objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the submitterType objects
     */
    public static Collection<SubmitterType> getSubmitterTypesList( )
    {
        return _dao.selectSubmitterTypesList( _plugin );
    }

    /**
     * Load the id of all the submitterType objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the submitterType objects
     */
    public static Collection<Integer> getIdSubmitterTypesList( )
    {
        return _dao.selectIdSubmitterTypesList( _plugin );
    }

    /**
     * Load the data of all the submitterType objects for a campaign and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the submitterType objects
     */
    public static Collection<SubmitterType> getSubmitterTypesListByCampaign( String strCampaignCode )
    {
        return _dao.selectSubmitterTypesListByCampaign( strCampaignCode, _plugin );
    }

    /**
     * Returns an instance of a submitterType whose identifier is specified in parameter
     * 
     * @param nKey
     *            The submitterType primary key
     * @return an instance of SubmitterType
     */
    public static SubmitterType findByCode( String strCode )
    {
        return _dao.load( strCode, _plugin );
    }

    /**
     * Load the data of all the submitterType objects mapped to a campaign and returns them in form of a map
     * 
     * @return the collection which contains the data of all the submitterType objects
     */
    public static Map<String, List<SubmitterType>> getSubmitterTypesMapByCampaign( )
    {
        return _dao.selectSubmitterTypesMapByCampaign( _plugin );
    }

}
