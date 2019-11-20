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
 * This class provides Data Access methods for FoTag objects
 */

public final class FoTagDAO implements IFoTagDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_fo_tag ) FROM ideation_fo_tags";
    private static final String SQL_QUERY_SELECT = "SELECT id_fo_tag, value FROM ideation_fo_tags WHERE id_fo_tag = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_fo_tags ( id_fo_tag, value ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM ideation_fo_tags WHERE id_fo_tag = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_fo_tags SET id_fo_tag = ?, value = ? WHERE id_fo_tag = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_fo_tag, value FROM ideation_fo_tags";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_fo_tag FROM ideation_fo_tags";

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
    public void insert( FoTag foTag, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        foTag.setId( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, foTag.getId( ) );
        daoUtil.setString( 2, foTag.getValue( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public FoTag load( int nKey, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1 , nKey );
        daoUtil.executeQuery( );

        FoTag foTag = null;

        if ( daoUtil.next( ) )
        {
            foTag = new FoTag();
            foTag.setId( daoUtil.getInt( 1 ) );
            foTag.setValue( daoUtil.getString( 2 ) );
        }

        daoUtil.free( );
        return foTag;
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
    public void store( FoTag foTag, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        daoUtil.setInt( 1, foTag.getId( ) );
        daoUtil.setString( 2, foTag.getValue( ) );
        daoUtil.setInt( 3, foTag.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<FoTag> selectFoTagsList( Plugin plugin )
    {
        Collection<FoTag> foTagList = new ArrayList<FoTag>(  );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            FoTag foTag = new FoTag(  );
            
            foTag.setId( daoUtil.getInt( 1 ) );
                foTag.setValue( daoUtil.getString( 2 ) );

            foTagList.add( foTag );
        }

        daoUtil.free( );
        return foTagList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdFoTagsList( Plugin plugin )
    {
            Collection<Integer> foTagList = new ArrayList<Integer>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                foTagList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return foTagList;
    }
}
