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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Depositary objects
 */

public final class DepositaryDAO implements IDepositaryDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_campaign_depositary ) FROM participatoryideation_depositaries";
    private static final String SQL_QUERY_SELECT = "SELECT id_campaign_depositary, code_campaign, code_depositary_type FROM participatoryideation_depositaries WHERE id_campaign_depositary = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO participatoryideation_depositaries ( id_campaign_depositary, code_campaign, code_depositary_type ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM participatoryideation_depositaries WHERE id_campaign_depositary = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE participatoryideation_depositaries SET id_campaign_depositary = ?, code_campaign = ?, code_depositary_type = ? WHERE id_campaign_depositary = ?";
    private static final String SQL_QUERY_CHANGEALL_CAMPAIGN_CODE = "UPDATE participatoryideation_depositaries SET code_campaign = ? WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_campaign_depositary, code_campaign, code_depositary_type FROM participatoryideation_depositaries";
    private static final String SQL_QUERY_SELECTALL_BY_CAMPAIGN = "SELECT id_campaign_depositary, code_campaign, code_depositary_type FROM participatoryideation_depositaries WHERE code_campaign = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_campaign_depositary FROM participatoryideation_depositaries";

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
    public void insert( Depositary depositary, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            depositary.setId( newPrimaryKey( plugin ) );

            daoUtil.setInt( 1, depositary.getId( ) );
            daoUtil.setString( 2, depositary.getCodeCampaign( ) );
            daoUtil.setString( 3, depositary.getCodeDepositaryType( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Depositary load( int nKey, Plugin plugin )
    {
        Depositary depositary = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            daoUtil.setInt( 1, nKey );
            daoUtil.executeQuery( );

            if ( daoUtil.next( ) )
            {
                depositary = new Depositary( );
                depositary.setId( daoUtil.getInt( 1 ) );
                depositary.setCodeCampaign( daoUtil.getString( 2 ) );
                depositary.setCodeDepositaryType( daoUtil.getString( 3 ) );
            }
        }

        return depositary;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void changeCampainCode( String oldCampaignCode, String newCampaignCode, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_CHANGEALL_CAMPAIGN_CODE, plugin ) )
        {
            daoUtil.setString( 1, newCampaignCode );
            daoUtil.setString( 2, oldCampaignCode );
            daoUtil.executeUpdate( );
        }
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
    public void store( Depositary depositary, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
        {
            daoUtil.setInt( 1, depositary.getId( ) );
            daoUtil.setString( 2, depositary.getCodeCampaign( ) );
            daoUtil.setString( 3, depositary.getCodeDepositaryType( ) );
            daoUtil.setInt( 4, depositary.getId( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Depositary> selectDepositariesList( Plugin plugin )
    {
        Collection<Depositary> depositaryList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin ) )
        {
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Depositary depositary = getRow( daoUtil );
                depositaryList.add( depositary );
            }
        }

        return depositaryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Depositary> selectDepositaryListByCampaign( String codeCampaign, Plugin plugin )
    {
        Collection<Depositary> depositaryList = new ArrayList<>( );

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_BY_CAMPAIGN, plugin ) )
        {
            daoUtil.setString( 1, codeCampaign );
            daoUtil.executeQuery( );

            while ( daoUtil.next( ) )
            {
                Depositary depositary = getRow( daoUtil );
                depositaryList.add( depositary );
            }
        }

        return depositaryList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdDepositariesList( Plugin plugin )
    {
        Collection<Integer> depositaryList = new ArrayList<Integer>( );

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

    // ***********************************************************************************
    // * GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GE *
    // * GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GETROW GE *
    // ***********************************************************************************

    private Depositary getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;

        Depositary depositary = new Depositary( );

        depositary.setId( daoUtil.getInt( nCpt++ ) );
        depositary.setCodeCampaign( daoUtil.getString( nCpt++ ) );
        depositary.setCodeDepositaryType( daoUtil.getString( nCpt ) );

        return depositary;
    }
}
