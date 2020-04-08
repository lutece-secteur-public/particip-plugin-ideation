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

/**
 * This class provides instances management methods (create, find, ...) for Submitter objects
 */

public final class SubmitterHome
{
    // Static variable pointed at the DAO instance

    private static ISubmitterDAO _dao = SpringContextService.getBean( "participatoryideation.submitterDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private SubmitterHome( )
    {
    }

    /**
     * Create an instance of the submitter class
     * 
     * @param submitter
     *            The instance of the Submitter which contains the informations to store
     * @return The instance of submitter which has been created with its primary key.
     */
    public static Submitter create( Submitter submitter )
    {
        _dao.insert( submitter, _plugin );

        return submitter;
    }

    /**
     * Update of the submitter which is specified in parameter
     * 
     * @param submitter
     *            The instance of the Submitter which contains the data to store
     * @return The instance of the submitter which has been updated
     */
    public static Submitter update( Submitter submitter )
    {
        _dao.store( submitter, _plugin );

        return submitter;
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
     * Remove the submitter whose identifier is specified in parameter
     * 
     * @param nKey
     *            The submitter Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }

    // /////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a submitter whose identifier is specified in parameter
     * 
     * @param nKey
     *            The submitter primary key
     * @return an instance of Submitter
     */
    public static Submitter findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Load the data of all the submitter objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the submitter objects
     */
    public static Collection<Submitter> getSubmittersList( )
    {
        return _dao.selectSubmittersList( _plugin );
    }

    /**
     * Load the id of all the submitter objects and returns them in form of a collection
     * 
     * @return the collection which contains the id of all the submitter objects
     */
    public static Collection<Integer> getIdSubmittersList( )
    {
        return _dao.selectIdSubmittersList( _plugin );
    }

    /**
     * Load the data of all the submitter objects for a campaign and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the submitter objects
     */
    public static Collection<Submitter> getSubmitterListByCampaign( String codeCampaign )
    {
        return _dao.selectSubmitterListByCampaign( codeCampaign, _plugin );
    }
}
