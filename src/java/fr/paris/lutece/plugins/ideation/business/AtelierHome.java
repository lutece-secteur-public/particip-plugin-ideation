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

import java.util.Collection;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Atelier objects
 */

public final class AtelierHome
{
    // Static variable pointed at the DAO instance

    private static IAtelierDAO _dao = SpringContextService.getBean( "ideation.atelierDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "ideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private AtelierHome(  )
    {
    }

    /**
     * Create an instance of the atelier class
     * @param atelier The instance of the Atelier which contains the informations to store
     * @return The  instance of atelier which has been created with its primary key.
     */
    public static Atelier create( Atelier atelier )
    {
        _dao.insert( atelier, _plugin );

        return atelier;
    }

    /**
     * Update of the atelier which is specified in parameter
     * @param atelier The instance of the Atelier which contains the data to store
     * @return The instance of the  atelier which has been updated
     */
    public static Atelier update( Atelier atelier )
    {
        _dao.store( atelier, _plugin );

        return atelier;
    }

    /**
     * Remove all the associations between idees with the atelier whose
     * identifier is specified in parameter
     * 
     * @param nIdAtelier The atelier Id
     */
    public static void removeAssociationsByIdAtelier( int nIdAtelier )
    {
        _dao.deleteAssociationsByIdAtelier( nIdAtelier, _plugin );
    }
    
    /**
     * Remove all the associations between idees with the atelier whose
     * identifier is specified in parameter
     * 
     * @param nIdIdee The idee Id
     */
    public static void removeAssociationsByIdIdee( int nIdIdee )
    {
        _dao.deleteAssociationsByIdIdee( nIdIdee, _plugin );
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a atelier whose identifier is specified in parameter
     * @param nKey The atelier primary key
     * @return an instance of Atelier
     */
    public static Atelier findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Load the data of all the atelier objects and returns them in form of a collection
     * @return the collection which contains the data of all the atelier objects
     */
    public static Collection<Atelier> getAteliersList( )
    {
        return _dao.selectAteliersList( _plugin );
    }
    
    /**
     * Load the id of all the atelier objects and returns them in form of a collection
     * @return the collection which contains the id of all the atelier objects
     */
    public static Collection<Integer> getIdAteliersList( )
    {
        return _dao.selectIdAteliersList( _plugin );
    }

    /**
     * Load the id of all the idees objects associated with an atelier and returns them as a collection
     * @param nIdAtelier The atelier Id
     * @return The collection which contains the id of all the idee objects
     */
    public static Collection<Integer> getIdeeIdsListByAtelier( int nIdAtelier )
    {
        return _dao.selectIdeeIdsByAtelier( nIdAtelier, _plugin );
    }

    /**
     * Load the Atelier by Id the Idee
     * @param nIdIdee the id of Idee
     * @return Atelier the atelier
     */
    public static Atelier getAtelierByIdee( int nIdIdee )
    {
        return _dao.loadAtelierByIdee( nIdIdee, _plugin );
    }
    /**
     * Load the list if idees by atelier
     * @param nIdAtelier the id of atelier
     * @return  list of Idees
     */
    public static List <Idee> getIdeesByAtelier( int nIdAtelier )
    {
        return _dao.loadIdeesByAtelier(nIdAtelier, _plugin ) ;
    }
    
    public static Collection<Atelier> getAteliersListSearch( AtelierSearcher atelierSearcher )
    {
        return _dao.selectAteliersListSearch( atelierSearcher, _plugin );
    }
    


}

