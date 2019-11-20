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
 * This class provides Data Access methods for AtelierFormResult objects
 */

public final class AtelierFormResultDAO implements IAtelierFormResultDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_atelier_form_result ) FROM ideation_atelier_form_result";
    private static final String SQL_QUERY_SELECT = "SELECT id_atelier_form_result, id_atelier_form, numero_choix_titre, numero_choix_description, guid, creation_timestamp FROM ideation_atelier_form_result WHERE id_atelier_form_result = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_atelier_form_result ( id_atelier_form_result, id_atelier_form, numero_choix_titre, numero_choix_description, guid, creation_timestamp ) VALUES ( ?, ?, ?, ?, ?, ?) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ideation_atelier_form_result WHERE id_atelier_form_result = ? ";
    private static final String SQL_QUERY_DELETE_BY_ID_ATELIER_FORM = "DELETE FROM ideation_atelier_form_result WHERE id_atelier_form = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_atelier_form_result SET id_atelier_form_result = ?, id_atelier_form = ?, numero_choix_titre = ?, numero_choix_description = ?, guid = ? WHERE id_atelier_form_result = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_atelier_form_result, id_atelier_form, numero_choix_titre, numero_choix_description, guid, creation_timestamp FROM ideation_atelier_form_result";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_atelier_form_result FROM ideation_atelier_form_result";
    private static final String SQL_QUERY_SELECT_BY_ATELIER_FORM_RESULT = " WHERE id_atelier_form_result = ?";
    private static final String SQL_QUERY_SELECT_BY_ATELIER_FORM_AND_GUID = " WHERE id_atelier_form = ? AND guid = ?";
    private static final String SQL_QUERY_SELECT_ENTRY_RESULT = "SELECT count(guid) FROM ideation_atelier_form_result iafr "
            + "INNER JOIN ideation_atelier_form_result_entry iafre ON iafr.id_atelier_form_result = iafre.id_atelier_form_result "
            + "WHERE id_atelier_form = ? AND id_atelier_form_entry = ? AND numero_choix = ?";
    private static final String SQL_QUERY_COUNT_GUID = "SELECT count(guid) FROM ideation_atelier_form_result WHERE id_atelier_form = ?";
    private static final String SQL_QUERY_COUNT_GUID_BY_TITRE = " AND numero_choix_titre = ?";
    private static final String SQL_QUERY_COUNT_GUID_BY_DESCRIPTION = " AND numero_choix_description = ?";
    private static final String SQL_QUERY_FILTER_BY_ATELIER_FORM = " WHERE id_atelier_form = ?";
    private static final String SQL_ORDER_BY_CREATION_DATE = " ORDER BY creation_timestamp DESC ";
    /**
     * Generates a new primary key
     * 
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey = 1;

        if ( daoUtil.next( ) )
        {
            nKey = daoUtil.getInt( 1 ) + 1;
        }

        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( AtelierFormResult atelierFormResult, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        atelierFormResult.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, atelierFormResult.getId( ) );
        daoUtil.setInt( 2, atelierFormResult.getIdAtelierForm( ) );
        daoUtil.setInt( 3, atelierFormResult.getNumeroChoixTitre( ) );
        daoUtil.setInt( 4, atelierFormResult.getNumeroChoixDescription( ) );
        daoUtil.setString( 5, atelierFormResult.getGuid( ) );
        daoUtil.setTimestamp(6, atelierFormResult.getCreationTimestamp());

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AtelierFormResult load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeQuery( );

        AtelierFormResult atelierFormResult = null;

        if ( daoUtil.next( ) )
        {
            atelierFormResult = new AtelierFormResult( );
            atelierFormResult.setId( daoUtil.getInt( 1 ) );
            atelierFormResult.setIdAtelierForm( daoUtil.getInt( 2 ) );
            atelierFormResult.setNumeroChoixTitre( daoUtil.getInt( 3 ) );
            atelierFormResult.setNumeroChoixDescription( daoUtil.getInt( 4 ) );
            atelierFormResult.setGuid( daoUtil.getString( 5 ) );
            atelierFormResult.setCreationTimestamp(daoUtil.getTimestamp( 6 ));
            
            atelierFormResult.setListChoixComplementaires( AtelierFormResultEntryHome
                    .getAtelierFormResultEntrysListByIdAtelierFormResult( atelierFormResult.getId( ) ) );
        }

        daoUtil.free( );
        return atelierFormResult;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public AtelierFormResult load( int nIdAtelierForm, String strGuid, Plugin plugin )
    {

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL + SQL_QUERY_SELECT_BY_ATELIER_FORM_AND_GUID, plugin );
        daoUtil.setInt( 1, nIdAtelierForm );
        daoUtil.setString( 2, strGuid );
        daoUtil.executeQuery( );

        AtelierFormResult atelierFormResult = null;

        if ( daoUtil.next( ) )
        {
            atelierFormResult = new AtelierFormResult( );
            atelierFormResult.setId( daoUtil.getInt( 1 ) );
            atelierFormResult.setIdAtelierForm( daoUtil.getInt( 2 ) );
            atelierFormResult.setNumeroChoixTitre( daoUtil.getInt( 3 ) );
            atelierFormResult.setNumeroChoixDescription( daoUtil.getInt( 4 ) );
            atelierFormResult.setGuid( daoUtil.getString( 5 ) );
            atelierFormResult.setCreationTimestamp(daoUtil.getTimestamp( 6 ));

            atelierFormResult.setListChoixComplementaires( AtelierFormResultEntryHome
                    .getAtelierFormResultEntrysListByIdAtelierFormResult( atelierFormResult.getId( ) ) );
        }

        daoUtil.free( );
        return atelierFormResult;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        AtelierFormResultEntryHome.removeByIdAtelierFormResult( nKey );

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteByAtelierForm( AtelierForm atelierForm, Plugin plugin )
    {
        List<AtelierFormEntry> atelierFormEntryList = AtelierFormEntryHome.getAtelierFormEntrysByAtelierForm( atelierForm );
        
        if ( atelierFormEntryList != null )
        {
            for ( AtelierFormEntry atelierFormEntry : atelierFormEntryList )
            {
                AtelierFormResultEntryHome.removeByIdAtelierFormEntry( atelierFormEntry.getId( ) );
            }
        }

        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_BY_ID_ATELIER_FORM, plugin );
        daoUtil.setInt( 1, atelierForm.getId( ) );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AtelierFormResult atelierFormResult, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, atelierFormResult.getId( ) );
        daoUtil.setInt( 2, atelierFormResult.getIdAtelierForm( ) );
        daoUtil.setInt( 3, atelierFormResult.getNumeroChoixTitre( ) );
        daoUtil.setInt( 4, atelierFormResult.getNumeroChoixDescription( ) );
        daoUtil.setString( 5, atelierFormResult.getGuid( ) );
        daoUtil.setInt( 6, atelierFormResult.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<AtelierFormResult> selectAtelierFormResultsList( Plugin plugin )
    {
        Collection<AtelierFormResult> atelierFormResultList = new ArrayList<AtelierFormResult>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            AtelierFormResult atelierFormResult = new AtelierFormResult( );

            atelierFormResult.setId( daoUtil.getInt( 1 ) );
            atelierFormResult.setIdAtelierForm( daoUtil.getInt( 2 ) );
            atelierFormResult.setNumeroChoixTitre( daoUtil.getInt( 3 ) );
            atelierFormResult.setNumeroChoixDescription( daoUtil.getInt( 4 ) );
            atelierFormResult.setGuid( daoUtil.getString( 5 ) );
            atelierFormResult.setCreationTimestamp(daoUtil.getTimestamp( 6 ));
            
            atelierFormResult.setListChoixComplementaires( AtelierFormResultEntryHome
                    .getAtelierFormResultEntrysListByIdAtelierFormResult( atelierFormResult.getId( ) ) );

            atelierFormResultList.add( atelierFormResult );
        }

        daoUtil.free( );
        return atelierFormResultList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<AtelierFormResult> selectAtelierFormResultsList(int nIdAtelierForm, Plugin plugin )
    {
        Collection<AtelierFormResult> atelierFormResultList = new ArrayList<AtelierFormResult>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL + SQL_QUERY_FILTER_BY_ATELIER_FORM +SQL_ORDER_BY_CREATION_DATE, plugin );
        daoUtil.setInt( 1, nIdAtelierForm );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            AtelierFormResult atelierFormResult = new AtelierFormResult( );

            atelierFormResult.setId( daoUtil.getInt( 1 ) );
            atelierFormResult.setIdAtelierForm( daoUtil.getInt( 2 ) );
            atelierFormResult.setNumeroChoixTitre( daoUtil.getInt( 3 ) );
            atelierFormResult.setNumeroChoixDescription( daoUtil.getInt( 4 ) );
            atelierFormResult.setGuid( daoUtil.getString( 5 ) );
            atelierFormResult.setCreationTimestamp(daoUtil.getTimestamp( 6 ));
            
            atelierFormResult.setListChoixComplementaires( AtelierFormResultEntryHome
                    .getAtelierFormResultEntrysListByIdAtelierFormResult( atelierFormResult.getId( ) ) );

            atelierFormResultList.add( atelierFormResult );
        }

        daoUtil.free( );
        return atelierFormResultList;
    }
    

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdAtelierFormResultsList( Plugin plugin )
    {
        Collection<Integer> atelierFormResultList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            atelierFormResultList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return atelierFormResultList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AtelierFormResultEntry> selectAtelierFormResultEntrysByAtelierFormResult(
            AtelierFormResult atelierFormResult, Plugin plugin )
    {
        List<AtelierFormResultEntry> atelierFormResultEntryList = new ArrayList<AtelierFormResultEntry>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL + SQL_QUERY_SELECT_BY_ATELIER_FORM_RESULT, plugin );
        daoUtil.setInt( 1, atelierFormResult.getId( ) );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            AtelierFormResultEntry atelierFormResultEntry = new AtelierFormResultEntry( );
            int nIndex = 1;
            atelierFormResultEntry.setIdAtelierFormResult( daoUtil.getInt( nIndex++ ) );
            atelierFormResultEntry.setIdAtelierFormEntry( daoUtil.getInt( nIndex++ ) );
            atelierFormResultEntry.setNumeroChoix( daoUtil.getInt( nIndex ) );

            atelierFormResultEntryList.add( atelierFormResultEntry );
        }

        daoUtil.free( );
        return atelierFormResultEntryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Integer getEntryResult( int nIdAtelierForm, int nIdAtelierFormEntry, int nChoix, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ENTRY_RESULT, plugin );
        daoUtil.setInt( 1, nIdAtelierForm );
        daoUtil.setInt( 2, nIdAtelierFormEntry );
        daoUtil.setInt( 3, nChoix );
        daoUtil.executeQuery( );

        Integer nVote = 0;

        if ( daoUtil.next( ) )
        {
            nVote = daoUtil.getInt( 1 );
        }

        daoUtil.free( );
        return nVote;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Integer getNbVotes( int nIdAtelierForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_GUID, plugin );
        daoUtil.setInt( 1, nIdAtelierForm );
        daoUtil.executeQuery( );

        Integer nVote = 0;

        if ( daoUtil.next( ) )
        {
            nVote = daoUtil.getInt( 1 );
        }

        daoUtil.free( );
        return nVote;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Integer getTitreResult( int nIdAtelierForm, int nChoix, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_GUID + SQL_QUERY_COUNT_GUID_BY_TITRE, plugin );
        daoUtil.setInt( 1, nIdAtelierForm );
        daoUtil.setInt( 2, nChoix );
        daoUtil.executeQuery( );

        Integer nVote = 0;

        if ( daoUtil.next( ) )
        {
            nVote = daoUtil.getInt( 1 );
        }

        daoUtil.free( );
        return nVote;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Integer getDescriptionResult( int nIdAtelierForm, int nChoix, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_COUNT_GUID + SQL_QUERY_COUNT_GUID_BY_DESCRIPTION, plugin );
        daoUtil.setInt( 1, nIdAtelierForm );
        daoUtil.setInt( 2, nChoix );
        daoUtil.executeQuery( );

        Integer nVote = 0;

        if ( daoUtil.next( ) )
        {
            nVote = daoUtil.getInt( 1 );
        }

        daoUtil.free( );
        return nVote;
    }
}