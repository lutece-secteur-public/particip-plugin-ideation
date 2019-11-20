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
 * This class provides instances management methods (create, find, ...) for AtelierFormResult objects
 */

public final class AtelierFormResultHome
{
    // Static variable pointed at the DAO instance

    private static IAtelierFormResultDAO _dao = SpringContextService.getBean( "ideation.atelierFormResultDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "ideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AtelierFormResultHome(  )
    {
    }

    /**
     * Create an instance of the atelierFormResult class
     * @param atelierFormResult The instance of the AtelierFormResult which contains the informations to store
     * @return The  instance of atelierFormResult which has been created with its primary key.
     */
    public static AtelierFormResult create( AtelierFormResult atelierFormResult )
    {
        _dao.insert( atelierFormResult, _plugin );

        return atelierFormResult;
    }

    /**
     * Update of the atelierFormResult which is specified in parameter
     * @param atelierFormResult The instance of the AtelierFormResult which contains the data to store
     * @return The instance of the  atelierFormResult which has been updated
     */
    public static AtelierFormResult update( AtelierFormResult atelierFormResult )
    {
        _dao.store( atelierFormResult, _plugin );

        return atelierFormResult;
    }

    /**
     * Remove the atelierFormResult whose identifier is specified in parameter
     * @param nKey The atelierFormResult Id
     */
    public static void remove( int nKey )
    {
        _dao.delete( nKey, _plugin );
    }
    
    /**
     * Remove all the atelierFormResults and atelierFormResultEntrys associated with the AtelierForm
     * @param atelierForm The atelierForm
     */
    public static void removeByAtelierForm( AtelierForm atelierForm )
    {
        _dao.deleteByAtelierForm( atelierForm, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a atelierFormResult whose identifier is specified in parameter
     * @param nKey The atelierFormResult primary key
     * @return an instance of AtelierFormResult
     */
    public static AtelierFormResult findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin );
    }

    /**
     * Returns an instance of a atelierFormResult whose identifier is specified in parameter
     * @param nKey The atelierFormResult primary key
     * @return an instance of AtelierFormResult
     */
    public static AtelierFormResult findByAtelierFormAndUser( int nIdAtelierForm, String strGuid )
    {
        return _dao.load( nIdAtelierForm, strGuid, _plugin );
    }

    /**
     * Load the data of all the atelierFormResult objects and returns them in form of a collection
     * @return the collection which contains the data of all the atelierFormResult objects
     */
    public static Collection<AtelierFormResult> getAtelierFormResultsList( )
    {
        return _dao.selectAtelierFormResultsList( _plugin );
    }
    
    
    /**
     * Load the data of all the atelierFormResult objects and returns them in form of a collection
     * @param nIdForm the form id
     * @return the collection which contains the data of all the atelierFormResult objects
     */
    public static Collection<AtelierFormResult> getAtelierFormResultsListByIdForm( int nIdForm)
    {
        return _dao.selectAtelierFormResultsList(nIdForm, _plugin );
    }
    
    
    /**
     * Load the id of all the atelierFormResult objects and returns them in form of a collection
     * @return the collection which contains the id of all the atelierFormResult objects
     */
    public static Collection<Integer> getIdAtelierFormResultsList( )
    {
        return _dao.selectIdAtelierFormResultsList( _plugin );
    }
    
    /**
     * Load the data of the atelierFormResultEntry objects by atelier form result and returns them in form of a collection
     * @return the collection which contains the data of the atelierFormResultEntry objects
     */
    public static List<AtelierFormResultEntry> getAtelierFormResultEntrysByAtelierFormResult( AtelierFormResult atelierFormResult )
    {
        return _dao.selectAtelierFormResultEntrysByAtelierFormResult( atelierFormResult, _plugin );
    }
    
    /**
     * Returns the number where an alternative had been selected
     * @param nIdAtelierForm The atelierForm Id
     * @param nIdAtelierFormEntry The atelierFormEntry Id
     * @param nChoix The choice
     * @return The number where an alternative had been selected
     */
    public static Integer getEntryResult( int nIdAtelierForm, int nIdAtelierFormEntry, int nChoix )
    {
        return _dao.getEntryResult( nIdAtelierForm, nIdAtelierFormEntry, nChoix, _plugin );
    }

    /**
     * Returns the number of user that has voted a form
     * @param nIdAtelierForm The atelierForm Id
     * @return The number of user that has voted a form
     */
    public static Integer getNbVotes( int nIdAtelierForm )
    {
        return _dao.getNbVotes( nIdAtelierForm, _plugin );
    }

    /**
     * Returns the number where an alternative of the title had been selected
     * @param nIdAtelierForm The atelierForm Id
     * @param nChoix The choice
     * @return The number where an alternative of the title had been selected
     */
    public static Integer getTitreResult( int nIdAtelierForm, int nChoix )
    {
        return _dao.getTitreResult( nIdAtelierForm, nChoix, _plugin );
    }

    /**
     * Returns the number where an alternative of the description had been selected
     * @param nIdAtelierForm The atelierForm Id
     * @param nChoix The choice
     * @return The number where an alternative of the description had been selected
     */
    public static Integer getDescriptionResult( int nIdAtelierForm, int nChoix )
    {
        return _dao.getDescriptionResult( nIdAtelierForm, nChoix, _plugin );
    }
}

