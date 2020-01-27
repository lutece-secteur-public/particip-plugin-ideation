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
package fr.paris.lutece.plugins.participatoryideation.business.notify;

import fr.paris.lutece.util.sql.DAOUtil;

/**
 *
 * TaskNotifyIdeationConfigDAO
 *
 */
public class TaskChangeIdeeStatusConfigDAO implements ITaskChangeIdeeStatusConfigDAO
{
    private static final String SQL_QUERY_FIND_BY_PRIMARY_KEY = "SELECT id_task,idee_status FROM task_change_idee_status_cf  WHERE id_task=?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO task_change_idee_status_cf( " + "id_task,idee_status)" + "VALUES (?,?)";
    private static final String SQL_QUERY_UPDATE = "UPDATE task_change_idee_status_cf " + " SET id_task = ?, idee_status = ? WHERE id_task = ? ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM task_change_idee_status_cf WHERE id_task = ? ";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void insert( TaskChangeIdeeStatusConfig config )
    {
        // FIXME use the plugin in all DAOUtil constructions
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT ) )
        {
            int nPos = 0;

            daoUtil.setInt( ++nPos, config.getIdTask( ) );
            daoUtil.setString( ++nPos, config.getIdeeStatus( ) );

            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void store( TaskChangeIdeeStatusConfig config )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE ) )
        {
            int nPos = 0;

            daoUtil.setInt( ++nPos, config.getIdTask( ) );
            daoUtil.setString( ++nPos, config.getIdeeStatus( ) );

            daoUtil.setInt( ++nPos, config.getIdTask( ) );
            daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TaskChangeIdeeStatusConfig load( int nIdTask )
    {
        TaskChangeIdeeStatusConfig config = null;

        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_FIND_BY_PRIMARY_KEY ) )
        {
            daoUtil.setInt( 1, nIdTask );

            daoUtil.executeQuery( );

            int nPos = 0;

            if ( daoUtil.next( ) )
            {
                config = new TaskChangeIdeeStatusConfig( );
                config.setIdTask( daoUtil.getInt( ++nPos ) );
                config.setIdeeStatus( daoUtil.getString( ++nPos ) );
            }
        }

        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete( int nIdState )
    {
        try( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE ) )
        {
            daoUtil.setInt( 1, nIdState );
            daoUtil.executeUpdate( );
        }
    }
}
