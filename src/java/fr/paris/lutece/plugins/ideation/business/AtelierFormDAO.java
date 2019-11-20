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
 * This class provides Data Access methods for AtelierForm objects
 */

public final class AtelierFormDAO implements IAtelierFormDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_atelierform ) FROM ideation_atelier_form";
    private static final String SQL_QUERY_SELECT = "SELECT id_atelierform, id_atelier, id_choix_titre, id_choix_description  FROM ideation_atelier_form WHERE id_atelierform = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_atelier_form ( id_atelierform, id_atelier, id_choix_titre, id_choix_description  ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ideation_atelier_form WHERE id_atelierform = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_atelier_form SET id_atelierform = ?, id_atelier = ?, id_choix_titre = ? , id_choix_description = ?  WHERE id_atelierform = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_atelierform, id_atelier, id_choix_titre, id_choix_description  FROM ideation_atelier_form";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_atelierform FROM ideation_atelier_form";
    private static final String SQL_QUERY_SELECT_BY_ATELIER = " WHERE id_atelier = ? ";

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
    public void insert( AtelierForm atelierForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        atelierForm.setId( newPrimaryKey( plugin ) );

        int nIndex = 1;
        daoUtil.setInt( nIndex++ , atelierForm.getId( ) );
        daoUtil.setInt( nIndex++ , atelierForm.getIdAtelier( ) );
        daoUtil.setInt( nIndex++ , atelierForm.getChoixTitre( ).getId( ) );
        daoUtil.setInt( nIndex++ , atelierForm.getChoixDescription( ).getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
        
        atelierForm.getChoixTitre( ).setIdAtelierForm( atelierForm.getId( ) );
        atelierForm.getChoixDescription().setIdAtelierForm( atelierForm.getId( ) );
        AtelierFormEntryHome.create( atelierForm.getChoixTitre( ) );
        AtelierFormEntryHome.create( atelierForm.getChoixDescription( ) );
        updateAtelierForm( atelierForm, plugin ) ;
    }
   
    /**
     * {@inheritDoc }
     */
    @Override
    public AtelierForm load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );

        AtelierForm atelierForm = null;
        AtelierFormEntry atelierFormEntryTitre = null ;
        AtelierFormEntry atelierFormEntryDesc = null ;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            atelierForm = new AtelierForm();
            atelierFormEntryTitre = new AtelierFormEntry() ;
            atelierFormEntryDesc = new AtelierFormEntry() ;
            atelierForm.setId( daoUtil.getInt( nIndex++ ) );
            atelierForm.setIdAtelier( daoUtil.getInt( nIndex++ ) );
            atelierFormEntryTitre =  AtelierFormEntryHome.findByPrimaryKey(  daoUtil.getInt( nIndex++ ) ) ;
            atelierFormEntryDesc =  AtelierFormEntryHome.findByPrimaryKey( daoUtil.getInt( nIndex++ ) ) ;
            
            if ( atelierFormEntryTitre != null )
            {
            	atelierForm.setChoixTitre( atelierFormEntryTitre );
            }
            if ( atelierFormEntryDesc != null )
            {
            	atelierForm.setChoixDescription( atelierFormEntryDesc );
            }
        }

        daoUtil.free( );
        
        return atelierForm;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
    	AtelierForm  atelier = AtelierFormHome.findByPrimaryKey( nKey );
    	List <AtelierFormEntry> list = AtelierFormEntryHome.getAtelierFormEntrysByAtelierForm( atelier );
    	
    	AtelierFormResultHome.removeByAtelierForm( atelier );

    	for ( AtelierFormEntry afe : list )
    	{
    		AtelierFormEntryHome.remove( afe.getId( ) );
    	}

    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeUpdate( );
        daoUtil.free( );
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( AtelierForm atelierForm, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        int nIndex = 1;
        daoUtil.setInt( nIndex++ , atelierForm.getId( ) );
        daoUtil.setInt( nIndex++ , atelierForm.getIdAtelier( ) );
        daoUtil.setInt( nIndex++ , atelierForm.getChoixTitre( ).getId( ) );
        daoUtil.setInt( nIndex++ , atelierForm.getChoixDescription( ).getId( ) );
        
        daoUtil.setInt( nIndex , atelierForm.getId( ) );
        

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public List<AtelierForm> selectAtelierFormsList( Plugin plugin )
    {
        List<AtelierForm> atelierFormList = new ArrayList<AtelierForm>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            AtelierForm atelierForm = new AtelierForm(  );
            AtelierFormEntry atelierFormEntryTitre = new AtelierFormEntry(  );
            AtelierFormEntry atelierFormEntryDesc = new AtelierFormEntry(  );
            int nIndex = 1;
            atelierForm.setId( daoUtil.getInt( nIndex++ ) );
            atelierForm.setIdAtelier( daoUtil.getInt( nIndex++ ) );
            atelierFormEntryTitre.setId( daoUtil.getInt( nIndex++ ) );
            atelierFormEntryDesc.setId( daoUtil.getInt( nIndex++ ) );
            atelierForm.setChoixTitre( atelierFormEntryTitre );
            atelierForm.setChoixDescription( atelierFormEntryDesc );
            
            atelierFormList.add( atelierForm );
        }

        daoUtil.free( );
        return atelierFormList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List<Integer> selectIdAtelierFormsList( Plugin plugin )
    {
            List<Integer> atelierFormList = new ArrayList<Integer>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                atelierFormList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return atelierFormList;
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public AtelierForm loadByAtelier( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL +  SQL_QUERY_SELECT_BY_ATELIER , plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );

        AtelierForm atelierForm = null;
        AtelierFormEntry atelierFormEntryTitre = null ;
        AtelierFormEntry atelierFormEntryDesc = null ;
        if ( daoUtil.next( ) )
        {
            int nIndex = 1;
            atelierForm = new AtelierForm();
            atelierFormEntryTitre = new AtelierFormEntry() ;
            atelierFormEntryDesc = new AtelierFormEntry() ;
            atelierForm.setId( daoUtil.getInt( nIndex++ ) );
            atelierForm.setIdAtelier( daoUtil.getInt( nIndex++ ) );
            atelierFormEntryTitre =  AtelierFormEntryHome.findByPrimaryKey(  daoUtil.getInt( nIndex++ ) ) ;
            atelierFormEntryDesc =  AtelierFormEntryHome.findByPrimaryKey( daoUtil.getInt( nIndex++ ) ) ;
            
            if ( atelierFormEntryTitre != null )
            {
            	atelierForm.setChoixTitre( atelierFormEntryTitre );
            }
            if ( atelierFormEntryDesc != null )
            {
            	atelierForm.setChoixDescription( atelierFormEntryDesc );
            }
        }

        daoUtil.free( );
        
        return atelierForm;
    }
    
    /**
     * Update idChoixTitre & idChoixDescription
     * @param atelierForm the atelier Form
     * @param plugin the plugin
     */
    private void updateAtelierForm( AtelierForm atelierForm, Plugin plugin )
    {
    	List <AtelierFormEntry> listAtelierEntry = AtelierFormEntryHome.getAtelierFormEntrysByAtelierForm( atelierForm ) ;
    	if ( listAtelierEntry.size()>1 )
    	{
    		atelierForm.setChoixTitre( listAtelierEntry.get( 0 ) );
    		atelierForm.setChoixDescription( listAtelierEntry.get( 1 ) );
    	}
    	store( atelierForm, plugin ) ;
    }
}