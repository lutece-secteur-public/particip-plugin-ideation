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
 * This class provides Data Access methods for ProfanityFilterDAO objects
 */

public final class ProfanityFilterDAO implements IProfanityFilterDAO
{
    // Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_profanity_filter ( id_user, word, ressource_type, counter ) VALUES ( ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_profanity_filter SET id_user = ?, word = ?, ressource_type = ?, counter = ? WHERE id_user = ? and word = ? and ressource_type = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT profanityfilter.id_user, profanityfilter.word, profanityfilter.ressource_type, profanityfilter.counter FROM ideation_profanity_filter profanityfilter";


	@Override
	public void insert(ProfanityFilter profanityFilter, Plugin plugin) {

		int nCpt = 1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );
        daoUtil.setString( nCpt++, profanityFilter.getUidUser( ) );
        daoUtil.setString( nCpt++, profanityFilter.getWord( ) );
        daoUtil.setString( nCpt++, profanityFilter.getRessourceType( ) );
        daoUtil.setInt( nCpt++, profanityFilter.getCounter( ) );
     
        daoUtil.executeUpdate( );
        daoUtil.free( );
		
	}

	@Override
	public void store(ProfanityFilter profanityFilter, Plugin plugin) {
		 
		int nCpt = 1;
	    DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
	        
	    daoUtil.setString( nCpt++, profanityFilter.getUidUser( ) );
        daoUtil.setString( nCpt++, profanityFilter.getWord( ) );
        daoUtil.setString( nCpt++, profanityFilter.getRessourceType( ) );
        daoUtil.setInt( nCpt++, profanityFilter.getCounter( ) );
     
        daoUtil.setString( nCpt++, profanityFilter.getUidUser( ) );
        daoUtil.setString( nCpt++, profanityFilter.getWord( ) );
        daoUtil.setString( nCpt++, profanityFilter.getRessourceType( ) );
        
        daoUtil.executeUpdate( );
        daoUtil.free( );
		
	}

	@Override
	public Collection<ProfanityFilter> findProfanityFilter(Plugin plugin,
			ProfanityFilterSearcher pfFilterSearcher) {
		  
		  Collection<ProfanityFilter> result = new ArrayList<ProfanityFilter>( );
		  
	      DAOUtil daoUtil;
	        
	        if (pfFilterSearcher != null) {
	            daoUtil = new DAOUtil( appendFilters( SQL_QUERY_SELECTALL, pfFilterSearcher ), plugin );
	            setFilterValues( daoUtil, pfFilterSearcher );
	        } else {
	            daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
	        }
	        daoUtil.executeQuery(  );

	        while ( daoUtil.next(  ) )
	        {
	        	ProfanityFilter profanityFilter = getRow(daoUtil);
	        	result.add( profanityFilter);
	        }

	        daoUtil.free( );

	        return result;
	}
	
	 /**
     * Creates the preparedStatement for apply filters
     * @param query The begining of the query
     * @param ideeSearcher The ideeSearcher
     * @return The sql statement
     */
    private String appendFilters( String query, ProfanityFilterSearcher pfFilterSearcher ) {
 
        //Create the where clause
        StringBuilder stringBuilder = new StringBuilder();
        if ( pfFilterSearcher.getUidUser( )!= null ) {
            stringBuilder.append(" profanityfilter.id_user = ? AND");
        }
        if ( pfFilterSearcher.getRessourceType( ) != null ) {
            stringBuilder.append(" profanityfilter.ressource_type = ? AND");
        }
        if ( pfFilterSearcher.getWord( ) != null ) {
            stringBuilder.append(" profanityfilter.word = ? AND");
        }
     
        
        if (stringBuilder.length( ) > 0) {
            //Remove the final " AND"
            stringBuilder.setLength( stringBuilder.length(  ) - 4 );
        }

        //Assemble all clauses
        StringBuilder finalQuery = new StringBuilder();
        finalQuery.append(query);
       
        if (stringBuilder.length() > 0) {
            finalQuery.append(" WHERE ");
            finalQuery.append(stringBuilder.toString());
        }
        

        return finalQuery.toString();
    }
    
    /**
     * Sets the pfFilterSearcher values for export and search
     * @param daoUtil The daoUtil
     * @param validatedFilters The validatedFilters
     */
    private void setFilterValues( DAOUtil daoUtil, ProfanityFilterSearcher pfFilterSearcher ) {
    	
        int nCpt = 1;
        
        if ( pfFilterSearcher.getUidUser( )!= null ) {
        	daoUtil.setString(nCpt++, pfFilterSearcher.getUidUser( ));
        }
        if ( pfFilterSearcher.getRessourceType( ) != null ) {
        	daoUtil.setString(nCpt++, pfFilterSearcher.getRessourceType( ));

        }
        if ( pfFilterSearcher.getWord( ) != null ) {
        	daoUtil.setString(nCpt++, pfFilterSearcher.getWord( ) );
        }
        
     }
    
    private ProfanityFilter getRow( DAOUtil daoUtil)
    {
        
        int nCpt=1;
        ProfanityFilter profanityFilter = new ProfanityFilter();
        
        profanityFilter.setUidUser( daoUtil.getString( nCpt++ ));
        profanityFilter.setWord( daoUtil.getString( nCpt++ ) );
        profanityFilter.setRessourceType ( daoUtil.getString( nCpt++ ) );
        profanityFilter.setCounter( daoUtil.getInt( nCpt++ ) );
        
        return profanityFilter;
    }
    
}
