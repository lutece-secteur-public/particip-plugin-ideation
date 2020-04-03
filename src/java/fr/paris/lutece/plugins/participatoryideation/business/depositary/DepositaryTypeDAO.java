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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for DepositaryType objects
 */

public final class DepositaryTypeDAO implements IDepositaryTypeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_depositary_type ) FROM participatoryideation_depositaries_types";
    private static final String SQL_QUERY_SELECT = "SELECT id_depositary_type, code_depositary_type, libelle, code_complement_type FROM participatoryideation_depositaries_types WHERE id_depositary_type = ?";
    private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id_depositary_type, code_depositary_type, libelle, code_complement_type FROM participatoryideation_depositaries_types WHERE code_depositary_type = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatoryideation_depositaries_types ( id_depositary_type, code_depositary_type, libelle, code_complement_type ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatoryideation_depositaries_types WHERE id_depositary_type = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatoryideation_depositaries_types SET id_depositary_type = ?, code_depositary_type = ?, libelle = ?, code_complement_type = ? WHERE id_depositary_type = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_depositary_type, code_depositary_type, libelle, code_complement_type FROM participatoryideation_depositaries_types";
    private static final String SQL_QUERY_SELECTALL_JOIN_CAMPAIGN = "SELECT types.id_depositary_type, types.code_depositary_type, types.libelle, types.code_complement_type, campaigns_depositaries.code_campaign FROM participatoryideation_depositaries_types types INNER JOIN participatoryideation_depositaries campaigns_depositaries ON campaigns_depositaries.code_depositary_type = types.code_depositary_type";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAIGN = "SELECT types.id_depositary_type, types.code_depositary_type, types.libelle, types.code_complement_type FROM participatoryideation_depositaries_types types INNER JOIN participatoryideation_depositaries campaigns_depositaries ON campaigns_depositaries.code_depositary_type = types.code_depositary_type where campaigns_depositaries.code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_depositary_type FROM participatoryideation_depositaries_types";

    private static final String SQL_QUERY_SELECTALL_VALUES = "SELECT depositary_values.code_depositary_type, depositary_values.code, depositary_values.libelle FROM participatoryideation_depositaries_types_values depositary_values INNER JOIN participatoryideation_depositaries campaigns_depositaries ON depositary_values.code_depositary_type = campaigns_depositaries.code_depositary_type";
    private static final String SQL_QUERY_SELECTALL_VALUES_BY_CAMPAIGN = SQL_QUERY_SELECTALL_VALUES + " where campaigns_depositaries.code_campaign = ?";

    private static final String SQL_QUERY_SELECTALL_DISTINCT_VALUES = "SELECT DISTINCT depositary_values.code_depositary_type, depositary_values.code, depositary_values.libelle FROM participatoryideation_depositaries_types_values depositary_values INNER JOIN participatoryideation_depositaries campaigns_depositaries ON depositary_values.code_depositary_type = campaigns_depositaries.code_depositary_type";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin ) )
        {
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                nKey = daoUtil.getInt( 1 ) + 1;
            }
        }

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( DepositaryType depositary, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            depositary.setId( newPrimaryKey( plugin ) );

            daoUtil.setInt( 1, depositary.getId( ) );
            daoUtil.setString( 2, depositary.getCode( ) );
            daoUtil.setString( 3, depositary.getLibelle( ) );
            daoUtil.setString( 4, depositary.getCodeComplementType( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DepositaryType load( int nKey, Plugin plugin )
    {
        DepositaryType depositary = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                depositary = getRow( daoUtil );
            }
        }

        return depositary;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public DepositaryType load( String strCode, Plugin plugin )
    {
        DepositaryType depositary = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin ) )
        {
            daoUtil.setString( 1, strCode );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                depositary = getRow( daoUtil );
            }
        }

        return depositary;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nKey, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( DepositaryType depositary, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            daoUtil.setInt( 1, depositary.getId( ) );
            daoUtil.setString( 2, depositary.getCode( ) );
            daoUtil.setString( 3, depositary.getLibelle( ) );
            daoUtil.setString( 4, depositary.getCodeComplementType( ) );
            daoUtil.setInt( 5, depositary.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<DepositaryType> selectDepositaryTypesList( Plugin plugin )
    {
        Collection<DepositaryType> depositaryList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                DepositaryType depositary = new DepositaryType( );

                depositary.setId( daoUtil.getInt( 1 ) );
                depositary.setCode( daoUtil.getString( 2 ) );
                depositary.setLibelle( daoUtil.getString( 3 ) );
                depositary.setCodeComplementType( daoUtil.getString( 4 ) );

                depositaryList.add( depositary );
            }
        }

        return depositaryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdDepositaryTypesList( Plugin plugin )
    {
        Collection<Integer> depositaryList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                depositaryList.add( daoUtil.getInt( 1 ) );
            }
        }

        return depositaryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<DepositaryType> selectDepositaryTypesListByCampaign( String strCampaignCode, Plugin plugin )
    {
        List<DepositaryType> depositaryList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAIGN, plugin ) )
        {
            daoUtil.setString( 1, strCampaignCode );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                DepositaryType depositary = getRow( daoUtil );

                depositaryList.add( depositary );
            }
        }

        loadValues( strCampaignCode, depositaryList, plugin );
        return depositaryList;
    }

    private DepositaryType getRow( DAOUtil daoUtil )
    {
        DepositaryType depositaryType = new DepositaryType( );

        depositaryType.setId( daoUtil.getInt( 1 ) );
        depositaryType.setCode( daoUtil.getString( 2 ) );
        depositaryType.setLibelle( daoUtil.getString( 3 ) );
        depositaryType.setCodeComplementType( daoUtil.getString( 4 ) );

        return depositaryType;
    }

    private void loadValues( List<DepositaryType> listDepositaryTypes, Plugin plugin )
    {
        loadValues( null, listDepositaryTypes, plugin );
    }

    private void loadValues( String strCampaignCode, List<DepositaryType> listDepositaryTypes, Plugin plugin )
    {
        HashMap<String, ReferenceList> depositaryMap = new HashMap<>( );

        String queryStr = ( strCampaignCode != null ) ? SQL_QUERY_SELECTALL_VALUES_BY_CAMPAIGN : SQL_QUERY_SELECTALL_DISTINCT_VALUES;

        try ( DAOUtil daoUtil = new DAOUtil( queryStr, plugin ) )
        {
            if ( strCampaignCode != null )
            {
                daoUtil.setString( 1, strCampaignCode );
            }

            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                String codeDepositaryType = daoUtil.getString( 1 );
                String codeValue = daoUtil.getString( 2 );
                String libelleValue = daoUtil.getString( 3 );

                ReferenceList referenceList = depositaryMap.get( codeDepositaryType );
                if ( referenceList == null )
                {
                    referenceList = new ReferenceList( );
                    depositaryMap.put( codeDepositaryType, referenceList );
                }

                referenceList.addItem( codeValue, libelleValue );
            }
        }

        for ( DepositaryType depositaryType : listDepositaryTypes )
        {
            if ( DepositaryType.CODE_COMPLEMENT_TYPE_LIST.equals( depositaryType.getCodeComplementType( ) ) )
            {
                ReferenceList values = depositaryMap.get( depositaryType.getCode( ) );
                if ( values == null )
                {
                    values = new ReferenceList( );
                }
                depositaryType.setValues( values );
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<DepositaryType>> selectDepositaryTypesMapByCampaign( Plugin plugin )
    {
        Map<String, List<DepositaryType>> depositaryTypeMap = new HashMap<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_JOIN_CAMPAIGN, plugin ) )
        {
            daoUtil.executeQuery( );

            List<DepositaryType> listAllDepositaryType = new ArrayList<>( );

            while ( daoUtil.next( ) )
            {
                DepositaryType depositaryType = getRow( daoUtil );
                String strCodeCampaign = daoUtil.getString( 5 );

                List<DepositaryType> depositaryTypeList = depositaryTypeMap.get( strCodeCampaign );
                if ( depositaryTypeList == null )
                {
                    depositaryTypeList = new ArrayList<DepositaryType>( );
                    depositaryTypeMap.put( strCodeCampaign, depositaryTypeList );
                }
                depositaryTypeList.add( depositaryType );
                listAllDepositaryType.add( depositaryType );
            }

            loadValues( listAllDepositaryType, plugin );
        }

        return depositaryTypeMap;
    }
}
