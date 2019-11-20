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
import java.util.Collection;

/**
 * This class provides Data Access methods for CampagneDepositaire objects
 */

public final class CampagneDepositaireDAO implements ICampagneDepositaireDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK                = "SELECT max( id_campagne_depositaire ) FROM ideation_campagnes_depositaires";
    private static final String SQL_QUERY_SELECT                = "SELECT id_campagne_depositaire, code_campagne, code_depositaire_type FROM ideation_campagnes_depositaires WHERE id_campagne_depositaire = ?";
    private static final String SQL_QUERY_INSERT                = "INSERT INTO ideation_campagnes_depositaires ( id_campagne_depositaire, code_campagne, code_depositaire_type ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE                = "DELETE FROM ideation_campagnes_depositaires WHERE id_campagne_depositaire = ? ";
    private static final String SQL_QUERY_UPDATE                = "UPDATE ideation_campagnes_depositaires SET id_campagne_depositaire = ?, code_campagne = ?, code_depositaire_type = ? WHERE id_campagne_depositaire = ?";
    private static final String SQL_QUERY_SELECTALL             = "SELECT id_campagne_depositaire, code_campagne, code_depositaire_type FROM ideation_campagnes_depositaires";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAGNE = "SELECT id_campagne_depositaire, code_campagne, code_depositaire_type FROM ideation_campagnes_depositaires WHERE code_campagne = ?";
    private static final String SQL_QUERY_SELECTALL_ID          = "SELECT id_campagne_depositaire FROM ideation_campagnes_depositaires";

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
    public void insert( CampagneDepositaire campagneDepositaire, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        campagneDepositaire.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, campagneDepositaire.getId( ) );
        daoUtil.setString( 2, campagneDepositaire.getCodeCampagne( ) );
        daoUtil.setString( 3, campagneDepositaire.getCodeDepositaireType( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public CampagneDepositaire load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );

        CampagneDepositaire campagneDepositaire = null;

        if ( daoUtil.next( ) )
        {
            campagneDepositaire = new CampagneDepositaire();
            campagneDepositaire.setId( daoUtil.getInt( 1 ) );
            campagneDepositaire.setCodeCampagne( daoUtil.getString( 2 ) );
            campagneDepositaire.setCodeDepositaireType( daoUtil.getString( 3 ) );
        }

        daoUtil.free( );
        return campagneDepositaire;
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
    public void store( CampagneDepositaire campagneDepositaire, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        daoUtil.setInt( 1, campagneDepositaire.getId( ) );
        daoUtil.setString( 2, campagneDepositaire.getCodeCampagne( ) );
        daoUtil.setString( 3, campagneDepositaire.getCodeDepositaireType( ) );
        daoUtil.setInt( 4, campagneDepositaire.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<CampagneDepositaire> selectCampagneDepositairesList( Plugin plugin )
    {
        Collection<CampagneDepositaire> campagneDepositaireList = new ArrayList<CampagneDepositaire>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            CampagneDepositaire campagneDepositaire = getRow( daoUtil );
            campagneDepositaireList.add( campagneDepositaire );
        }

        daoUtil.free( );
        return campagneDepositaireList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
	public Collection<CampagneDepositaire> selectCampagneDepositaireListByCampagne(String codeCampagne, Plugin plugin) {
        Collection<CampagneDepositaire> campagneDepositaireList = new ArrayList<CampagneDepositaire>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAGNE, plugin );
        daoUtil.setString( 1 , codeCampagne );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            CampagneDepositaire campagneDepositaire = getRow( daoUtil );
            campagneDepositaireList.add( campagneDepositaire );
        }

        daoUtil.free( );
        return campagneDepositaireList;
	}

	/**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdCampagneDepositairesList( Plugin plugin )
    {
            Collection<Integer> campagneDepositaireList = new ArrayList<Integer>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                campagneDepositaireList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return campagneDepositaireList;
    }

    // ***********************************************************************************
    // * GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GE *
    // * GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GE *
    // ***********************************************************************************
    
    private CampagneDepositaire getRow( DAOUtil daoUtil )
    {
    	int nCpt=1;
        
    	CampagneDepositaire campagneDepositaire = new CampagneDepositaire(  );
        
        campagneDepositaire.setId                  ( daoUtil.getInt   ( nCpt++ ) );
        campagneDepositaire.setCodeCampagne        ( daoUtil.getString( nCpt++ ) );
        campagneDepositaire.setCodeDepositaireType ( daoUtil.getString( nCpt++ ) );

        return campagneDepositaire;
    }
}
