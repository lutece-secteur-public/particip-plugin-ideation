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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for SubmitterType objects
 */

public final class SubmitterTypeDAO implements ISubmitterTypeDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_submitter_type ) FROM participatoryideation_submitters_types";
    private static final String SQL_QUERY_SELECT = "SELECT id_submitter_type, code_submitter_type, libelle, code_complement_type FROM participatoryideation_submitters_types WHERE id_submitter_type = ?";
    private static final String SQL_QUERY_SELECT_BY_CODE = "SELECT id_submitter_type, code_submitter_type, libelle, code_complement_type FROM participatoryideation_submitters_types WHERE code_submitter_type = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatoryideation_submitters_types ( id_submitter_type, code_submitter_type, libelle, code_complement_type ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatoryideation_submitters_types WHERE id_submitter_type = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatoryideation_submitters_types SET id_submitter_type = ?, code_submitter_type = ?, libelle = ?, code_complement_type = ? WHERE id_submitter_type = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_submitter_type, code_submitter_type, libelle, code_complement_type FROM participatoryideation_submitters_types";
    private static final String SQL_QUERY_SELECTALL_JOIN_CAMPAIGN = "SELECT types.id_submitter_type, types.code_submitter_type, types.libelle, types.code_complement_type, campaigns_submitters.code_campaign FROM participatoryideation_submitters_types types INNER JOIN participatoryideation_submitters campaigns_submitters ON campaigns_submitters.code_submitter_type = types.code_submitter_type";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAIGN = "SELECT types.id_submitter_type, types.code_submitter_type, types.libelle, types.code_complement_type FROM participatoryideation_submitters_types types INNER JOIN participatoryideation_submitters campaigns_submitters ON campaigns_submitters.code_submitter_type = types.code_submitter_type where campaigns_submitters.code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_submitter_type FROM participatoryideation_submitters_types";

    private static final String SQL_QUERY_SELECTALL_VALUES = "SELECT submitter_values.code_submitter_type, submitter_values.code, submitter_values.libelle FROM participatoryideation_submitters_types_values submitter_values INNER JOIN participatoryideation_submitters campaigns_submitters ON submitter_values.code_submitter_type = campaigns_submitters.code_submitter_type";
    private static final String SQL_QUERY_SELECTALL_VALUES_BY_CAMPAIGN = SQL_QUERY_SELECTALL_VALUES + " where campaigns_submitters.code_campaign = ?";

    private static final String SQL_QUERY_SELECTALL_DISTINCT_VALUES = "SELECT DISTINCT submitter_values.code_submitter_type, submitter_values.code, submitter_values.libelle FROM participatoryideation_submitters_types_values submitter_values INNER JOIN participatoryideation_submitters campaigns_submitters ON submitter_values.code_submitter_type = campaigns_submitters.code_submitter_type";

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
    public void insert( SubmitterType submitter, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            submitter.setId( newPrimaryKey( plugin ) );

            daoUtil.setInt( 1, submitter.getId( ) );
            daoUtil.setString( 2, submitter.getCode( ) );
            daoUtil.setString( 3, submitter.getLibelle( ) );
            daoUtil.setString( 4, submitter.getCodeComplementType( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SubmitterType load( int nKey, Plugin plugin )
    {
        SubmitterType submitter = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                submitter = getRow( daoUtil );
            }
        }

        return submitter;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public SubmitterType load( String strCode, Plugin plugin )
    {
        SubmitterType submitter = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODE, plugin ) )
        {
            daoUtil.setString( 1, strCode );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                submitter = getRow( daoUtil );
            }
        }

        return submitter;
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
    public void store( SubmitterType submitter, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            daoUtil.setInt( 1, submitter.getId( ) );
            daoUtil.setString( 2, submitter.getCode( ) );
            daoUtil.setString( 3, submitter.getLibelle( ) );
            daoUtil.setString( 4, submitter.getCodeComplementType( ) );
            daoUtil.setInt( 5, submitter.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<SubmitterType> selectSubmitterTypesList( Plugin plugin )
    {
        Collection<SubmitterType> submitterList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                SubmitterType submitter = new SubmitterType( );

                submitter.setId( daoUtil.getInt( 1 ) );
                submitter.setCode( daoUtil.getString( 2 ) );
                submitter.setLibelle( daoUtil.getString( 3 ) );
                submitter.setCodeComplementType( daoUtil.getString( 4 ) );

                submitterList.add( submitter );
            }
        }

        return submitterList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdSubmitterTypesList( Plugin plugin )
    {
        Collection<Integer> submitterList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                submitterList.add( daoUtil.getInt( 1 ) );
            }
        }

        return submitterList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<SubmitterType> selectSubmitterTypesListByCampaign( String strCampaignCode, Plugin plugin )
    {
        List<SubmitterType> submitterList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAIGN, plugin ) )
        {
            daoUtil.setString( 1, strCampaignCode );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                SubmitterType submitter = getRow( daoUtil );

                submitterList.add( submitter );
            }
        }

        loadValues( strCampaignCode, submitterList, plugin );
        return submitterList;
    }

    private SubmitterType getRow( DAOUtil daoUtil )
    {
        SubmitterType submitterType = new SubmitterType( );

        submitterType.setId( daoUtil.getInt( 1 ) );
        submitterType.setCode( daoUtil.getString( 2 ) );
        submitterType.setLibelle( daoUtil.getString( 3 ) );
        submitterType.setCodeComplementType( daoUtil.getString( 4 ) );

        return submitterType;
    }

    private void loadValues( List<SubmitterType> listSubmitterTypes, Plugin plugin )
    {
        loadValues( null, listSubmitterTypes, plugin );
    }

    private void loadValues( String strCampaignCode, List<SubmitterType> listSubmitterTypes, Plugin plugin )
    {
        HashMap<String, ReferenceList> submitterMap = new HashMap<>( );

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
                String codeSubmitterType = daoUtil.getString( 1 );
                String codeValue = daoUtil.getString( 2 );
                String libelleValue = daoUtil.getString( 3 );

                ReferenceList referenceList = submitterMap.get( codeSubmitterType );
                if ( referenceList == null )
                {
                    referenceList = new ReferenceList( );
                    submitterMap.put( codeSubmitterType, referenceList );
                }

                referenceList.addItem( codeValue, libelleValue );
            }
        }

        for ( SubmitterType submitterType : listSubmitterTypes )
        {
            if ( SubmitterType.CODE_COMPLEMENT_TYPE_LIST.equals( submitterType.getCodeComplementType( ) ) )
            {
                ReferenceList values = submitterMap.get( submitterType.getCode( ) );
                if ( values == null )
                {
                    values = new ReferenceList( );
                }
                submitterType.setValues( values );
            }
        }

    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, List<SubmitterType>> selectSubmitterTypesMapByCampaign( Plugin plugin )
    {
        Map<String, List<SubmitterType>> submitterTypeMap = new HashMap<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_JOIN_CAMPAIGN, plugin ) )
        {
            daoUtil.executeQuery( );

            List<SubmitterType> listAllSubmitterType = new ArrayList<>( );

            while ( daoUtil.next( ) )
            {
                SubmitterType submitterType = getRow( daoUtil );
                String strCodeCampaign = daoUtil.getString( 5 );

                List<SubmitterType> submitterTypeList = submitterTypeMap.get( strCodeCampaign );
                if ( submitterTypeList == null )
                {
                    submitterTypeList = new ArrayList<SubmitterType>( );
                    submitterTypeMap.put( strCodeCampaign, submitterTypeList );
                }
                submitterTypeList.add( submitterType );
                listAllSubmitterType.add( submitterType );
            }

            loadValues( listAllSubmitterType, plugin );
        }

        return submitterTypeMap;
    }
}
