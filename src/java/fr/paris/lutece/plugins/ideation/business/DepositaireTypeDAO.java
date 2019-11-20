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
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides Data Access methods for DepositaireType objects
 */

public final class DepositaireTypeDAO implements IDepositaireTypeDAO {
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_depositaire_type ) FROM ideation_depositaire_types";
    private static final String SQL_QUERY_SELECT = "SELECT id_depositaire_type, code_depositaire_type, libelle, code_complement_type FROM ideation_depositaire_types WHERE id_depositaire_type = ?";
    private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id_depositaire_type, code_depositaire_type, libelle, code_complement_type FROM ideation_depositaire_types WHERE code_depositaire_type = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_depositaire_types ( id_depositaire_type, code_depositaire_type, libelle, code_complement_type ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ideation_depositaire_types WHERE id_depositaire_type = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_depositaire_types SET id_depositaire_type = ?, code_depositaire_type = ?, libelle = ?, code_complement_type = ? WHERE id_depositaire_type = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_depositaire_type, code_depositaire_type, libelle, code_complement_type FROM ideation_depositaire_types";
    private static final String SQL_QUERY_SELECTALL_JOIN_CAMPAGNE = "SELECT types.id_depositaire_type, types.code_depositaire_type, types.libelle, types.code_complement_type, campagnes_depositaires.code_campagne FROM ideation_depositaire_types types INNER JOIN ideation_campagnes_depositaires campagnes_depositaires ON campagnes_depositaires.code_depositaire_type = types.code_depositaire_type";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAGNE = "SELECT types.id_depositaire_type, types.code_depositaire_type, types.libelle, types.code_complement_type FROM ideation_depositaire_types types INNER JOIN ideation_campagnes_depositaires campagnes_depositaires ON campagnes_depositaires.code_depositaire_type = types.code_depositaire_type where campagnes_depositaires.code_campagne = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_depositaire_type FROM ideation_depositaire_types";

    private static final String SQL_QUERY_SELECTALL_VALUES = "SELECT depositaire_values.code_depositaire_type, depositaire_values.code, depositaire_values.libelle FROM ideation_depositaire_types_values depositaire_values INNER JOIN ideation_campagnes_depositaires campagnes_depositaires ON depositaire_values.code_depositaire_type = campagnes_depositaires.code_depositaire_type";
    private static final String SQL_QUERY_SELECTALL_VALUES_BY_CAMPAGNE = SQL_QUERY_SELECTALL_VALUES + " where campagnes_depositaires.code_campagne = ?";

    private static final String SQL_QUERY_SELECTALL_DISTINCT_VALUES = "SELECT DISTINCT depositaire_values.code_depositaire_type, depositaire_values.code, depositaire_values.libelle FROM ideation_depositaire_types_values depositaire_values INNER JOIN ideation_campagnes_depositaires campagnes_depositaires ON depositaire_values.code_depositaire_type = campagnes_depositaires.code_depositaire_type";

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
    public void insert( DepositaireType depositaire, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        depositaire.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, depositaire.getId( ) );
        daoUtil.setString( 2, depositaire.getCode( ) );
        daoUtil.setString( 3, depositaire.getLibelle( ) );
        daoUtil.setString( 4, depositaire.getCodeComplementType( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DepositaireType load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );

        DepositaireType depositaire = null;

        if ( daoUtil.next( ) )
        {
            depositaire = new DepositaireType();
            depositaire.setId( daoUtil.getInt( 1 ) );
            depositaire.setCode( daoUtil.getString( 2 ) );
            depositaire.setLibelle( daoUtil.getString( 3 ) );
            depositaire.setCodeComplementType( daoUtil.getString( 4 ) );
        }

        daoUtil.free( );
        return depositaire;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DepositaireType load( String strCode, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin );
        daoUtil.setString( 1 , strCode );
        daoUtil.executeQuery( );

        DepositaireType depositaire = null;

        if ( daoUtil.next( ) )
        {
            depositaire = getRow( daoUtil );
        }

        daoUtil.free( );
        return depositaire;
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
    public void store( DepositaireType depositaire, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        daoUtil.setInt( 1, depositaire.getId( ) );
        daoUtil.setString( 2, depositaire.getCode( ) );
        daoUtil.setString( 3, depositaire.getLibelle( ) );
        daoUtil.setString( 4, depositaire.getCodeComplementType( ) );
        daoUtil.setInt( 5, depositaire.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<DepositaireType> selectDepositaireTypesList( Plugin plugin )
    {
        Collection<DepositaireType> depositaireList = new ArrayList<DepositaireType>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DepositaireType depositaire = new DepositaireType(  );
            
            depositaire.setId( daoUtil.getInt( 1 ) );
                depositaire.setCode( daoUtil.getString( 2 ) );
                depositaire.setLibelle( daoUtil.getString( 3 ) );
                depositaire.setCodeComplementType( daoUtil.getString( 4 ) );

            depositaireList.add( depositaire );
        }

        daoUtil.free( );
        return depositaireList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdDepositaireTypesList( Plugin plugin )
    {
            Collection<Integer> depositaireList = new ArrayList<Integer>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                depositaireList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return depositaireList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<DepositaireType> selectDepositaireTypesListByCampagne( String strCampagneCode, Plugin plugin )
    {
        List<DepositaireType> depositaireList = new ArrayList<DepositaireType>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAGNE, plugin );
        daoUtil.setString( 1, strCampagneCode );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            DepositaireType depositaire = getRow ( daoUtil );

            depositaireList.add( depositaire );
        }

        daoUtil.free( );
        loadValues( strCampagneCode, depositaireList, plugin );
        return depositaireList;
    }

    private DepositaireType getRow( DAOUtil daoUtil ) {

            DepositaireType depositaireType = new DepositaireType(  );

            depositaireType.setId( daoUtil.getInt( 1 ) );
            depositaireType.setCode( daoUtil.getString( 2 ) );
            depositaireType.setLibelle( daoUtil.getString( 3 ) );
            depositaireType.setCodeComplementType( daoUtil.getString( 4 ) );

            return depositaireType;
    }

    private void loadValues( List<DepositaireType> listDepositaireTypes, Plugin plugin )
    {
        loadValues( null, listDepositaireTypes, plugin );
    }

    private void loadValues( String strCampagneCode, List<DepositaireType> listDepositaireTypes, Plugin plugin )
    {
        HashMap<String, ReferenceList> depositaireMap = new HashMap<String, ReferenceList>(  );
        DAOUtil daoUtil;
        if (strCampagneCode != null) {
            daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_VALUES_BY_CAMPAGNE, plugin );
            daoUtil.setString( 1, strCampagneCode );
        } else {
            daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_DISTINCT_VALUES, plugin );
        }
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            String codeDepositaireType = daoUtil.getString( 1 );
            String codeValue = daoUtil.getString( 2 );
            String libelleValue = daoUtil.getString( 3 );

            ReferenceList referenceList = depositaireMap.get( codeDepositaireType );
            if ( referenceList == null ) {
                referenceList = new ReferenceList();
                depositaireMap.put( codeDepositaireType, referenceList );
            }

            referenceList.addItem( codeValue, libelleValue );
        }

        daoUtil.free( );

        for (DepositaireType depositaireType: listDepositaireTypes) {
            if (DepositaireType.CODE_COMPLEMENT_TYPE_LIST.equals(depositaireType.getCodeComplementType())) {
                ReferenceList values = depositaireMap.get( depositaireType.getCode() );
                if (values == null) {
                    values = new ReferenceList();
                }
                depositaireType.setValues(values);
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<DepositaireType>> selectDepositaireTypesMapByCampagne( Plugin plugin )
    {
        Map<String, List<DepositaireType>> depositaireTypeMap = new HashMap<String, List<DepositaireType>>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_JOIN_CAMPAGNE, plugin );
        daoUtil.executeQuery(  );

        List<DepositaireType> listAllDepositaireType = new ArrayList<DepositaireType>(  );
        while ( daoUtil.next(  ) )
        {
            DepositaireType depositaireType = getRow( daoUtil );
            String strCodeCampagne = daoUtil.getString( 5 );

            List<DepositaireType> depositaireTypeList = depositaireTypeMap.get( strCodeCampagne );
            if (depositaireTypeList == null) {
                depositaireTypeList = new ArrayList<DepositaireType>();
                depositaireTypeMap.put( strCodeCampagne, depositaireTypeList );
            }
            depositaireTypeList.add( depositaireType );
            listAllDepositaireType.add ( depositaireType );
        }

        daoUtil.free( );
        loadValues( listAllDepositaireType, plugin );
        return depositaireTypeMap;
    }
}
