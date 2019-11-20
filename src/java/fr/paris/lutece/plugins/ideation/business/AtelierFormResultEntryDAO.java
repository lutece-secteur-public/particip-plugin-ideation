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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class provides Data Access methods for AtelierFormResultEntry objects
 */

public final class AtelierFormResultEntryDAO implements IAtelierFormResultEntryDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_atelier_form_result ) FROM ideation_atelier_form_result_entry";
    private static final String SQL_QUERY_SELECT = "SELECT id_atelier_form_result, id_atelier_form_entry, numero_choix FROM ideation_atelier_form_result_entry WHERE id_atelier_form_result = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_atelier_form_result_entry ( id_atelier_form_result, id_atelier_form_entry, numero_choix ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE_BY_ID_FORM_RESULT = "DELETE FROM ideation_atelier_form_result_entry WHERE id_atelier_form_result = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID_FORM_ENTRY = "DELETE FROM ideation_atelier_form_result_entry WHERE id_atelier_form_entry = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_atelier_form_result_entry SET id_atelier_form_result = ?, id_atelier_form_entry = ?, numero_choix = ? WHERE id_atelier_form_result = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_atelier_form_result, id_atelier_form_entry, numero_choix FROM ideation_atelier_form_result_entry";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_atelier_form_result FROM ideation_atelier_form_result_entry";
    private static final String SQL_QUERY_AND_BY_ID_ATELIER_FORM_ENTRY = " AND id_atelier_form_entry = ?";

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  );
        daoUtil.executeQuery( );

        int nKey = 1;

        if( daoUtil.next( ) )
        {
                nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free();

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( AtelierFormResultEntry atelierFormResultEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setInt( 1, atelierFormResultEntry.getIdAtelierFormResult( ) );
        daoUtil.setInt( 2, atelierFormResultEntry.getIdAtelierFormEntry( ) );
        daoUtil.setInt( 3, atelierFormResultEntry.getNumeroChoix( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AtelierFormResultEntry load( int nIdAtelierFormResult, int nIdAtelierFormEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nIdAtelierFormResult );
        daoUtil.setInt( 2 , nIdAtelierFormEntry );
        daoUtil.executeQuery( );

        AtelierFormResultEntry atelierFormResultEntry = null;

        if ( daoUtil.next( ) )
        {
            atelierFormResultEntry = new AtelierFormResultEntry();
            atelierFormResultEntry.setIdAtelierFormResult( daoUtil.getInt( 1 ) );
            atelierFormResultEntry.setIdAtelierFormEntry( daoUtil.getInt( 2 ) );
            atelierFormResultEntry.setNumeroChoix( daoUtil.getInt( 3 ) );
        }

        daoUtil.free( );
        return atelierFormResultEntry;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByIdFormResult( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_FORM_RESULT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByIdFormEntry( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_FORM_ENTRY, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AtelierFormResultEntry atelierFormResultEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        daoUtil.setInt( 1, atelierFormResultEntry.getIdAtelierFormResult( ) );
        daoUtil.setInt( 2, atelierFormResultEntry.getIdAtelierFormEntry( ) );
        daoUtil.setInt( 3, atelierFormResultEntry.getNumeroChoix( ) );
        daoUtil.setInt( 4, atelierFormResultEntry.getIdAtelierFormResult( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<AtelierFormResultEntry> selectAtelierFormResultEntrysList( Plugin plugin )
    {
        Collection<AtelierFormResultEntry> atelierFormResultEntryList = new ArrayList<AtelierFormResultEntry>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AtelierFormResultEntry atelierFormResultEntry = new AtelierFormResultEntry(  );

            atelierFormResultEntry.setIdAtelierFormResult( daoUtil.getInt( 1 ) );
            atelierFormResultEntry.setIdAtelierFormEntry( daoUtil.getInt( 2 ) );
            atelierFormResultEntry.setNumeroChoix( daoUtil.getInt( 3 ) );

            atelierFormResultEntryList.add( atelierFormResultEntry );
        }

        daoUtil.free( );
        return atelierFormResultEntryList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdAtelierFormResultEntrysList( Plugin plugin )
    {
        Collection<Integer> atelierFormResultEntryList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            atelierFormResultEntryList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return atelierFormResultEntryList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<AtelierFormResultEntry> selectAtelierFormResultEntrysListByIdAtelierFormResult(
            int nIdAtelierFormResult, Plugin plugin )
    {
        List<AtelierFormResultEntry> atelierFormResultEntryList = new ArrayList<AtelierFormResultEntry>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nIdAtelierFormResult );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AtelierFormResultEntry atelierFormResultEntry = new AtelierFormResultEntry(  );

            atelierFormResultEntry.setIdAtelierFormResult( daoUtil.getInt( 1 ) );
            atelierFormResultEntry.setIdAtelierFormEntry( daoUtil.getInt( 2 ) );
            atelierFormResultEntry.setNumeroChoix( daoUtil.getInt( 3 ) );

            atelierFormResultEntryList.add( atelierFormResultEntry );
        }

        daoUtil.free( );
        return atelierFormResultEntryList;
    }
}