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

/**
 * This class provides instances management methods (create, find, ...) for Depositary objects
 */

public final class DepositaryHome
{
    // Static variable pointed at the DAO instance

    private static IDepositaryDAO _dao = SpringContextService.getBean( "participatoryideation.depositaryDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private DepositaryHome( )
    {
    }

    /**
     * Create an instance of the depositary class
     * 
     * @param depositary
     *            The instance of the Depositary which contains the informations to store
     * @return The instance of depositary which has been created with its primary key.
     */
    public static Depositary create( Depositary depositary )
    {
        _dao.insert( depositary, _plugin );

        return depositary;
    }

    /**
     * Update of the depositary which is specified in parameter
     * 
     * @param depositary
     *            The instance of the Depositary which contains the data to store
     * @return The instance of the depositary which has been updated
     */
    public static Depositary update( Depositary depositary )
    {
        _dao.store( depositary, _plugin );

        return depositary;
    }

    /**
     * Change a campaign code
     * 
     * @param oldCampaignCode
     *            The campaign code to change
     * @param newCampaignCode
     *            The new campaign code
     */
    public static void changeCampainCode( String oldCampaignCode, String newCampaignCode )
    {
        _dao.changeCampainCode( oldCampaignCode, newCampaignCode, _plugin );
    }

    /**
     * Remove the depositary whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositary Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a depositary whose identifier is specified in parameter
     * 
     * @param nKey
     *            The depositary primary key
     * @return an instance of Depositary
     */
    public static Depositary findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the depositary objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the depositary objects
     */
    public static Collection<Depositary> getDepositariesList( )
    {
        return _dao.selectDepositariesList( _plugin );
    }

    /**
     * Load the id of all the depositary objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the depositary objects
     */
    public static Collection<Integer> getIdDepositariesList( )
    {
        return _dao.selectIdDepositariesList( _plugin );
    }

    /**
     * Load the data of all the depositary objects for a campaign and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the depositary objects
     */
    public static Collection<Depositary> getDepositaryListByCampaign( String codeCampaign )
    {
        return _dao.selectDepositaryListByCampaign( codeCampaign, _plugin );
    }
}
