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
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for AtelierFormEntry objects
 */

public final class AtelierFormEntryDAO implements IAtelierFormEntryDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_atelierformentry ) FROM ideation_atelier_form_entry";
    private static final String SQL_QUERY_SELECT = "SELECT id_atelierformentry, id_atelierform, alternative1, alternative2, alternative3 FROM ideation_atelier_form_entry WHERE id_atelierformentry = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_atelier_form_entry ( id_atelierformentry, id_atelierform, alternative1, alternative2, alternative3 ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ideation_atelier_form_entry WHERE id_atelierformentry = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_atelier_form_entry SET id_atelierformentry = ?, id_atelierform = ?, alternative1 = ?, alternative2 = ?, alternative3 = ? WHERE id_atelierformentry = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_atelierformentry,id_atelierform, alternative1, alternative2, alternative3 FROM ideation_atelier_form_entry";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_atelierformentry FROM ideation_atelier_form_entry";
    private static final String SQL_QUERY_SELECT_BY_ATELIER_FORM = " WHERE id_atelierform = ?" ;
    private static final String SQL_QUERY_SELECT_COMPLEMENTAIRES = " AND id_atelierformentry NOT IN ( ? , ? ) " ;
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
    public void insert( AtelierFormEntry atelierFormEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        atelierFormEntry.setId( newPrimaryKey( plugin ) );

        int nIndex = 1;
        daoUtil.setInt( nIndex++ , atelierFormEntry.getId( ) );
        
        daoUtil.setInt( nIndex++ , atelierFormEntry.getIdAtelierForm( ) );
        daoUtil.setString( nIndex++ , atelierFormEntry.getAlternative1( ) );
        daoUtil.setString( nIndex++ , atelierFormEntry.getAlternative2( ) );
        daoUtil.setString( nIndex++ , atelierFormEntry.getAlternative3( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AtelierFormEntry load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );

        AtelierFormEntry atelierFormEntry = null;

        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            atelierFormEntry = new AtelierFormEntry();
            atelierFormEntry.setId( daoUtil.getInt( nIndex++ ) );
            atelierFormEntry.setIdAtelierForm( daoUtil.getInt( nIndex++ ) );
            atelierFormEntry.setAlternative1( daoUtil.getString( nIndex++ ) );
            atelierFormEntry.setAlternative2( daoUtil.getString( nIndex++ ) );
            atelierFormEntry.setAlternative3( daoUtil.getString( nIndex++ ) );
        }

        daoUtil.free( );
        return atelierFormEntry;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AtelierFormEntry atelierFormEntry, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        int nIndex = 1;
        daoUtil.setInt( nIndex++ , atelierFormEntry.getId( ) );
        daoUtil.setInt( nIndex++ , atelierFormEntry.getIdAtelierForm( ) );
        daoUtil.setString( nIndex++ , atelierFormEntry.getAlternative1( ) );
        daoUtil.setString( nIndex++ , atelierFormEntry.getAlternative2( ) );
        daoUtil.setString( nIndex++ , atelierFormEntry.getAlternative3( ) );
        daoUtil.setInt( nIndex , atelierFormEntry.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AtelierFormEntry> selectAtelierFormEntrysList( Plugin plugin )
    {
        List<AtelierFormEntry> atelierFormEntryList = new ArrayList<AtelierFormEntry>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AtelierFormEntry atelierFormEntry = new AtelierFormEntry(  );
            int nIndex = 1;
            atelierFormEntry.setId( daoUtil.getInt( nIndex++ ) );
            atelierFormEntry.setIdAtelierForm( daoUtil.getInt( nIndex++ ) );
                atelierFormEntry.setAlternative1( daoUtil.getString( nIndex++ ) );
                atelierFormEntry.setAlternative2( daoUtil.getString( nIndex++ ) );
                atelierFormEntry.setAlternative3( daoUtil.getString( nIndex++ ) );

            atelierFormEntryList.add( atelierFormEntry );
        }

        daoUtil.free( );
        return atelierFormEntryList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAtelierFormEntrysList( Plugin plugin )
    {
            List<Integer> atelierFormEntryList = new ArrayList<Integer>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                atelierFormEntryList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return atelierFormEntryList;
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public List<AtelierFormEntry> selectAtelierFormEntrysListByAtelierForm( AtelierForm atelierForm, Plugin plugin )
    {
        List<AtelierFormEntry> atelierFormEntryList = new ArrayList<AtelierFormEntry>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL + SQL_QUERY_SELECT_BY_ATELIER_FORM, plugin );
        daoUtil.setInt( 1 , atelierForm.getId( ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AtelierFormEntry atelierFormEntry = new AtelierFormEntry(  );
            int nIndex = 1;
            atelierFormEntry.setId( daoUtil.getInt( nIndex++ ) );
            atelierFormEntry.setIdAtelierForm( daoUtil.getInt( nIndex++ ) );
                atelierFormEntry.setAlternative1( daoUtil.getString( nIndex++ ) );
                atelierFormEntry.setAlternative2( daoUtil.getString( nIndex++ ) );
                atelierFormEntry.setAlternative3( daoUtil.getString( nIndex++ ) );

            atelierFormEntryList.add( atelierFormEntry );
        }

        daoUtil.free( );
        return atelierFormEntryList;
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public List<AtelierFormEntry> selectFormEntriesComplByAtelierForm( AtelierForm atelierForm, Plugin plugin )
    {
        List<AtelierFormEntry> atelierFormEntryList = new ArrayList<AtelierFormEntry>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL + SQL_QUERY_SELECT_BY_ATELIER_FORM + SQL_QUERY_SELECT_COMPLEMENTAIRES, plugin );
        daoUtil.setInt( 1 , atelierForm.getId( ) );
        daoUtil.setInt( 2 , atelierForm.getChoixTitre( ).getId( ) );
        daoUtil.setInt( 3 , atelierForm.getChoixDescription( ).getId( ) );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AtelierFormEntry atelierFormEntry = new AtelierFormEntry(  );
            int nIndex = 1;
            atelierFormEntry.setId( daoUtil.getInt( nIndex++ ) );
            atelierFormEntry.setIdAtelierForm( daoUtil.getInt( nIndex++ ) );
                atelierFormEntry.setAlternative1( daoUtil.getString( nIndex++ ) );
                atelierFormEntry.setAlternative2( daoUtil.getString( nIndex++ ) );
                atelierFormEntry.setAlternative3( daoUtil.getString( nIndex++ ) );

            atelierFormEntryList.add( atelierFormEntry );
        }

        daoUtil.free( );
        return atelierFormEntryList;
    }
}