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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.ideation.web.IdeationApp;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Atelier objects
 */

public final class AtelierDAO implements IAtelierDAO
{
	//marks 
	private static final String MARK_SEPERATION_CODE_IDEES = "ideation.creation_atelier_separation_codes_idees";
    
	// Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_atelier ) FROM ideation_atelier";
    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_atelier ( id_atelier, titre, description, campagne, thematique, type, dateDebutPhase1, dateFinPhase1, dateDebutPhase2, dateFinPhase2, dateDebutPhase3, dateFinPhase3, localisationType, localisationArdt, address, longitude, latitude, geojson, textePhase1, titrePhase3, textePhase3, listCodeIdee, lienFormulairePhase2, status_public, joursDeRappelPhase1 , joursDeRappelPhase2, cout, lieuAtelier, dateAtelier, handicap) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_atelier SET id_atelier = ?, titre = ?, description = ?, campagne = ?, thematique = ?, type = ?, dateDebutPhase1 = ?, dateFinPhase1 = ?, dateDebutPhase2 = ?, dateFinPhase2 = ?, dateDebutPhase3 = ?, dateFinPhase3 = ?, localisationType = ?, localisationArdt = ?, address = ?, longitude = ?, latitude = ?, geojson = ?, textePhase1 = ?, titrePhase3 = ?, textePhase3 = ?, listCodeIdee = ?, lienFormulairePhase2 = ?, status_public = ?, joursDeRappelPhase1 = ?, joursDeRappelPhase2 = ?, cout = ?, lieuAtelier = ?, dateAtelier = ?, handicap = ? WHERE id_atelier = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_atelier, titre, description, campagne, thematique, type, dateDebutPhase1, dateFinPhase1, dateDebutPhase2, dateFinPhase2, dateDebutPhase3, dateFinPhase3, localisationType, localisationArdt, address, longitude, latitude, geojson, textePhase1, titrePhase3, textePhase3, listCodeIdee, lienFormulairePhase2, status_public, joursDeRappelPhase1, joursDeRappelPhase2, cout, lieuAtelier, dateAtelier, handicap FROM ideation_atelier";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_atelier FROM ideation_atelier";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL+ " WHERE id_atelier = ?";

    private static final String SQL_QUERY_INSERT_ASSOCIATION = "INSERT INTO atelier_idees ( id_atelier, id_idee ) VALUES ( ?, ? ) ";
    private static final String SQL_QUERY_DELETE_ASSOCIATION_BY_ID_ATELIER = "DELETE FROM atelier_idees WHERE id_atelier = ? ";
    private static final String SQL_QUERY_DELETE_ASSOCIATION_BY_ID_IDEE = "DELETE FROM atelier_idees WHERE id_idee = ? ";
    private static final String SQL_QUERY_SELECTALL_ID_IDEES_BY_ATELIER = "SELECT id_idee FROM atelier_idees WHERE id_atelier = ? ";
    private static final String SQL_QUERY_SELECT_ALL_ASSOCIATION = "SELECT id_atelier, id_idee FROM atelier_idees ";
    private static final String SQL_QUERY_SELECT_ATELIER_BY_IDEE = SQL_QUERY_SELECT_ALL_ASSOCIATION +" WHERE id_idee = ? ";
    private static final String SQL_QUERY_SELECT_IDEES_BY_ATELIER = SQL_QUERY_SELECT_ALL_ASSOCIATION +" WHERE id_atelier = ? ";

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
    public void insert( Atelier atelier, Plugin plugin )
    {
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        atelier.setId( newPrimaryKey( plugin ) );
        int nCpt=1;
        daoUtil.setInt( nCpt++, atelier.getId( ) );
        daoUtil.setString( nCpt++, atelier.getTitre( ) );
        daoUtil.setString( nCpt++, atelier.getDescription( ) );
        daoUtil.setString( nCpt++, atelier.getCampagne( ) );
        daoUtil.setString( nCpt++, atelier.getThematique( ) );
        daoUtil.setString( nCpt++, atelier.getType( ) );
        daoUtil.setDate( nCpt++, atelier.getDateDebutPhase1( ) );
        daoUtil.setDate( nCpt++, atelier.getDateFinPhase1( ) );
        daoUtil.setDate( nCpt++, atelier.getDateDebutPhase2( ) );
        daoUtil.setDate( nCpt++, atelier.getDateFinPhase2( ) );
        daoUtil.setDate( nCpt++, atelier.getDateDebutPhase3( ) );
        daoUtil.setDate( nCpt++, atelier.getDateFinPhase3( ) );
        daoUtil.setString( nCpt++, atelier.getLocalisationType( ) );
        daoUtil.setString( nCpt++, atelier.getLocalisationArdt( ) );
        daoUtil.setString( nCpt++, atelier.getAddress( ) );
        if ( atelier.getLongitude( ) != null )
        {
            daoUtil.setDouble( nCpt++, atelier.getLongitude( ) );
        }
        else
        {
            daoUtil.setDoubleNull( nCpt++ );
        }
        if ( atelier.getLatitude( ) != null )
        {
            daoUtil.setDouble( nCpt++, atelier.getLatitude( ) );
        }
        else
        {
            daoUtil.setDoubleNull( nCpt++ );
        }
        daoUtil.setString( nCpt++, atelier.getGeoJson( ) );
        daoUtil.setString( nCpt++, atelier.getTextePhase1( ) );
        daoUtil.setString( nCpt++, atelier.getTitrePhase3( ) );
        daoUtil.setString( nCpt++, atelier.getTextePhase3( ) );
        daoUtil.setString( nCpt++, atelier.getListCodeIdee( ) );
        daoUtil.setString( nCpt++, atelier.getLienFormulairePhase2( ) );
        daoUtil.setString( nCpt++, atelier.getStatusPublic( ).getValue( ) );
        daoUtil.setInt( nCpt++, atelier.getJoursDeRappelPhase1( ) );
        daoUtil.setInt( nCpt++, atelier.getJoursDeRappelPhase2( ) );
        daoUtil.setLong( nCpt++, atelier.getCout( ) );
        daoUtil.setString( nCpt++, atelier.getLieuAtelier( ) );
        daoUtil.setString( nCpt++, atelier.getDateAtelier( ) );
        daoUtil.setString( nCpt++, atelier.getHandicap( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
        insertAsscociation(  atelier, plugin ) ;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Atelier load( int nKey, Plugin plugin )
    {
    	int nCpt=1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( nCpt++ , nKey );
        daoUtil.executeQuery( );

        Atelier atelier = null;

        if ( daoUtil.next( ) )
        {
        	atelier=getRow(daoUtil);
        }

        daoUtil.free( );
        return atelier;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Atelier atelier, Plugin plugin )
    {
    	updateAsscociation( atelier, plugin ) ;

    	int nCpt=1;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );
        
        daoUtil.setInt( nCpt++ , atelier.getId( ) );
        daoUtil.setString( nCpt++ , atelier.getTitre( ) );
        daoUtil.setString( nCpt++ , atelier.getDescription( ) );
        daoUtil.setString( nCpt++ , atelier.getCampagne( ) );
        daoUtil.setString( nCpt++ , atelier.getThematique( ) );
        daoUtil.setString( nCpt++ , atelier.getType( ) );
        daoUtil.setDate( nCpt++ , atelier.getDateDebutPhase1( ) );
        daoUtil.setDate( nCpt++ , atelier.getDateFinPhase1( ) );
        daoUtil.setDate( nCpt++ , atelier.getDateDebutPhase2( ) );
        daoUtil.setDate( nCpt++ , atelier.getDateFinPhase2( ) );
        daoUtil.setDate( nCpt++ , atelier.getDateDebutPhase3( ) );
        daoUtil.setDate( nCpt++ , atelier.getDateFinPhase3( ) );
        daoUtil.setString( nCpt++ , atelier.getLocalisationType( ) );
        daoUtil.setString( nCpt++ , atelier.getLocalisationArdt( ) );
        daoUtil.setString( nCpt++, atelier.getAddress( ) );
        if ( atelier.getLongitude( ) != null )
        {
            daoUtil.setDouble( nCpt++, atelier.getLongitude( ) );
        }
        else
        {
            daoUtil.setDoubleNull( nCpt++ );
        }
        if ( atelier.getLatitude( ) != null )
        {
            daoUtil.setDouble( nCpt++, atelier.getLatitude( ) );
        }
        else
        {
            daoUtil.setDoubleNull( nCpt++ );
        }
        daoUtil.setString( nCpt++, atelier.getGeoJson( ) );
        daoUtil.setString( nCpt++ , atelier.getTextePhase1( ) );
        daoUtil.setString( nCpt++ , atelier.getTitrePhase3( ) );
        daoUtil.setString( nCpt++ , atelier.getTextePhase3( ) );
        daoUtil.setString( nCpt++ , atelier.getListCodeIdee( ) );
        daoUtil.setString( nCpt++ , atelier.getLienFormulairePhase2( ) );
        daoUtil.setString( nCpt++, atelier.getStatusPublic( )!=null?atelier.getStatusPublic( ).getValue( ):null );
        daoUtil.setInt( nCpt++, atelier.getJoursDeRappelPhase1( ) );
        daoUtil.setInt( nCpt++, atelier.getJoursDeRappelPhase2( ) );
        daoUtil.setLong( nCpt++, atelier.getCout( ) );
        daoUtil.setString( nCpt++, atelier.getLieuAtelier( ) );
        daoUtil.setString( nCpt++, atelier.getDateAtelier( ) );
        daoUtil.setString( nCpt++, atelier.getHandicap( ) );

        daoUtil.setInt( nCpt++ , atelier.getId( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Atelier> selectAteliersList( Plugin plugin )
    {
        Collection<Atelier> atelierList = new ArrayList<Atelier>(  );
        Atelier atelier=null;
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
        	atelier=getRow(daoUtil);
            atelierList.add(atelier );
        }

        daoUtil.free( );
        return atelierList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdAteliersList( Plugin plugin )
    {
            Collection<Integer> atelierList = new ArrayList<Integer>( );
            DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin );
            daoUtil.executeQuery(  );

            while ( daoUtil.next(  ) )
            {
                atelierList.add( daoUtil.getInt( 1 ) );
            }

            daoUtil.free( );
            return atelierList;
    }

    private Atelier getRow( DAOUtil daoUtil)
    {
        
        int nCpt=1;
        Atelier atelier = new Atelier(  );
        atelier = new Atelier();
        atelier.setId( daoUtil.getInt( nCpt++ ) );
        atelier.setTitre( daoUtil.getString( nCpt++ ) );
        atelier.setDescription( daoUtil.getString( nCpt++ ) );
        atelier.setCampagne( daoUtil.getString( nCpt++ ) );
        atelier.setThematique( daoUtil.getString( nCpt++ ) );
        atelier.setType( daoUtil.getString( nCpt++ ) );
        atelier.setDateDebutPhase1( daoUtil.getDate( nCpt++ ) );
        atelier.setDateFinPhase1( daoUtil.getDate( nCpt++ ) );
        atelier.setDateDebutPhase2( daoUtil.getDate( nCpt++ ) );
        atelier.setDateFinPhase2( daoUtil.getDate( nCpt++ ) );
        atelier.setDateDebutPhase3( daoUtil.getDate( nCpt++ ) );
        atelier.setDateFinPhase3( daoUtil.getDate( nCpt++ ) );
        atelier.setLocalisationType( daoUtil.getString( nCpt++ ) );
        atelier.setLocalisationArdt( daoUtil.getString( nCpt++ ) );
        atelier.setAddress( daoUtil.getString( nCpt++ ) );
        atelier.setLongitude( daoUtil.getDouble( nCpt++ ) );
        atelier.setLatitude( daoUtil.getDouble( nCpt++ ) );
        atelier.setGeoJson( daoUtil.getString( nCpt++ ) );
        atelier.setTextePhase1( daoUtil.getString( nCpt++ ) );
        atelier.setTitrePhase3( daoUtil.getString( nCpt++ ) );
        atelier.setTextePhase3( daoUtil.getString( nCpt++ ) );
        atelier.setListCodeIdee( daoUtil.getString( nCpt++ ) );
        atelier.setLienFormulairePhase2( daoUtil.getString( nCpt++ ) );
        atelier.setStatusPublic( Atelier.Status.getByValue( daoUtil.getString( nCpt++ ) ) );

        atelier.setJoursDeRappelPhase1( daoUtil.getInt( nCpt++ ) );
        atelier.setJoursDeRappelPhase2( daoUtil.getInt( nCpt++ ) );
        atelier.setCout( daoUtil.getLong( nCpt++ ) );
        atelier.setLieuAtelier( daoUtil.getString( nCpt++ ) );
        atelier.setDateAtelier( daoUtil.getString( nCpt++ ) );
        atelier.setHandicap( daoUtil.getString( nCpt++ ) );

        return atelier;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Atelier loadAtelierByIdee( int nIdIdee, Plugin plugin )
    {
    	int nCpt=1;
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_ATELIER_BY_IDEE, plugin );
    	daoUtil.setInt( nCpt++ , nIdIdee );
    	daoUtil.executeQuery( );
    	
    	Atelier atelier = null;
    	
    	if ( daoUtil.next( ) )
    	{
    		atelier = load( daoUtil.getInt( 1 ), plugin ) ;
    	}
    	
    	daoUtil.free( );
    	return atelier;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public List <Idee> loadIdeesByAtelier( int nIdAtelier, Plugin plugin )
    {
    	int nCpt=1;
    	List<Idee> listIdIdees = new ArrayList<Idee> ();
    	DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_IDEES_BY_ATELIER, plugin );
    	daoUtil.setInt( nCpt++ , nIdAtelier );
    	daoUtil.executeQuery( );
    	
    	while ( daoUtil.next(  ) )
    	{
    		Idee idee = IdeeHome.findByPrimaryKey ( daoUtil.getInt( 2 ) ) ;
    		if ( idee != null )
    		{
    			listIdIdees.add( idee );
    		}
    	}
    	daoUtil.free( );
    	return listIdIdees;
    }
    
    private void insertAsscociation( Atelier atelier, Plugin plugin )
    {
    	String[] listIdees = atelier.getListCodeIdee().split( AppPropertiesService.getProperty( MARK_SEPERATION_CODE_IDEES ) );
    	
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_ASSOCIATION, plugin );
        
        for ( String strId : listIdees )
        {
	        int nCpt=1, nCode = 0;
	        String[] listIdeeCodeCampagne = strId.split("-");
            String strCodeCampagne = listIdeeCodeCampagne[0].trim();
            
	        daoUtil.setInt( nCpt++, atelier.getId( ) );
	        try
    		{
	        	//nCode = Integer.parseInt( strId.replaceAll( MARK_INTEGERS_REGEX ,StringUtils.EMPTY ) );
	        	nCode = Integer.parseInt( listIdeeCodeCampagne[1] );
    		}
    		catch( NumberFormatException e )
    		{
    			break;
    		}
	        
	        Idee idee = IdeeHome.findByCodes(strCodeCampagne, nCode);
	        if(idee != null)
	        {
	        	nCode = idee.getId();
	        }
	        
	        daoUtil.setInt( nCpt++, nCode );
	        daoUtil.executeUpdate( );
        }
        daoUtil.free( );
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteAssociationsByIdAtelier( int nIdAtelier, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ASSOCIATION_BY_ID_ATELIER, plugin );
        daoUtil.setInt( 1 , nIdAtelier );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteAssociationsByIdIdee( int nIdIdee, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_ASSOCIATION_BY_ID_IDEE, plugin );
        daoUtil.setInt( 1 , nIdIdee );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    public void updateAsscociation( Atelier atelier, Plugin plugin )
    {
    		Atelier atelierDb = load( atelier.getId( ) , plugin );
    		if ( ! atelier.getListCodeIdee( ).equals( atelierDb.getListCodeIdee( ) ) )
    		{
    			deleteAssociationsByIdAtelier( atelier.getId( ), plugin ) ;
    			insertAsscociation( atelier, plugin ) ;
    		}
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdeeIdsByAtelier( int nIdAtelier, Plugin plugin )
    {
        Collection<Integer> ideeIdsList = new ArrayList<Integer>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID_IDEES_BY_ATELIER, plugin );
        daoUtil.setInt( 1 , nIdAtelier );
        daoUtil.executeQuery( );

        while ( daoUtil.next(  ) )
        {
            ideeIdsList.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free( );
        return ideeIdsList;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Atelier> selectAteliersListSearch( AtelierSearcher atelierSearcher, Plugin plugin )
    {

        Map<Integer, Atelier> atelierMap = new LinkedHashMap<Integer, Atelier>(  );
        DAOUtil daoUtil;
        if ( atelierSearcher != null ) 
        {
            daoUtil = new DAOUtil( appendFilters( SQL_QUERY_SELECTALL, atelierSearcher ), plugin );
            setFilterValues( daoUtil, atelierSearcher );
        } 
        else
        {
            daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        }
        daoUtil.executeQuery(  );

        while ( daoUtil.next(  ) )
        {
            Atelier atelier = getRow(daoUtil);
            atelierMap.put( atelier.getId(), atelier );
        }
        daoUtil.free( );

        ArrayList<Atelier> result = new ArrayList<Atelier>(atelierMap.values());
        return result;
    
    }
    /**
     * Creates the preparedStatement for apply filters
     * @param query The begining of the query
     * @param atelierSearcher The atelierSearcher
     * @return The sql statement
     */
    private String appendFilters( String query, AtelierSearcher atelierSearcher ) {

       
        //Create the where clause
        StringBuilder stringBuilder = new StringBuilder( );
        if ( atelierSearcher.getTitreOuDescription( ) != null ) 
        {
        	stringBuilder.append(" ( titre LIKE ? OR description LIKE ? ) AND");
        }
        if ( atelierSearcher.getType( ) != null ) 
        {
        	stringBuilder.append(" type = ? AND");
        }
        if ( atelierSearcher.getCodeCampagne() != null ) 
        {
            stringBuilder.append(" campagne = ? AND");
        }
        if ( atelierSearcher.getCodeTheme() != null ) 
        {
            stringBuilder.append(" thematique = ? AND");
        }
        if ( atelierSearcher.getTypeLocalisation( ) != null ) 
        {
        	stringBuilder.append(" localisationtype = ? AND");
        }
        if ( atelierSearcher.getLocalisationArdt( ) != null ) 
        {
        	stringBuilder.append(" localisationardt = ? AND");
        }
        if ( IdeationApp.HANDICAP_LABEL_YES.equals( atelierSearcher.getHandicap() ) ) {
            stringBuilder.append(" handicap = ? AND");
        } else if ( IdeationApp.HANDICAP_LABEL_NO.equals( atelierSearcher.getHandicap() ) ) {
            stringBuilder.append(" ( (handicap IS NULL) OR (handicap = ?) ) AND");
        }

        if (stringBuilder.length() > 0) {
            //Remove the final " AND"
            stringBuilder.setLength( stringBuilder.length(  ) - 4 );
        }

        //Create the order by clause without SQL Injection
        String strOrder = AtelierSearcher.ORDER_ASC.equals( atelierSearcher.getOrderAscDesc( ) )
                       || AtelierSearcher.ORDER_DESC.equals( atelierSearcher.getOrderAscDesc( ) ) ?
                    		   atelierSearcher.getOrderAscDesc( ) : null;
        StringBuilder stringBuilderOrderBy = new StringBuilder( );
        
        if ( AtelierSearcher.COLUMN_ID.equals( atelierSearcher.getOrderColumn( ) ) ) 
        {
            stringBuilderOrderBy.append( "id_atelier");
            if ( strOrder != null ) 
            {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(atelierSearcher.getOrderAscDesc( ) );
            }
        }
        if ( AtelierSearcher.COLUMN_TITRE.equals( atelierSearcher.getOrderColumn( ) ) ) 
        {
            stringBuilderOrderBy.append( "titre");
            if ( strOrder != null ) 
            {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(atelierSearcher.getOrderAscDesc( ) );
            }
        }
        if ( AtelierSearcher.COLUMN_ARRDT.equals( atelierSearcher.getOrderColumn( ) ) ) 
        {
            stringBuilderOrderBy.append( "localisationardt");
            if ( strOrder != null ) 
            {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(atelierSearcher.getOrderAscDesc( ) );
            }
        }
        if ( AtelierSearcher.COLUMN_ARRDT.equals( atelierSearcher.getOrderColumn( ) ) ) 
        {
            stringBuilderOrderBy.append( "localisationardt");
            if ( strOrder != null ) 
            {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(atelierSearcher.getOrderAscDesc( ) );
            }
        }
        if ( AtelierSearcher.COLUMN_CAMPAGNE.equals( atelierSearcher.getOrderColumn( ) ) ) 
        {
            stringBuilderOrderBy.append( "campagne");
            if ( strOrder != null ) 
            {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(atelierSearcher.getOrderAscDesc( ) );
            }
        }
        if ( AtelierSearcher.COLUMN_THEMATIQUE.equals( atelierSearcher.getOrderColumn( ) ) ) 
        {
            stringBuilderOrderBy.append( "thematique");
            if ( strOrder != null ) 
            {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(atelierSearcher.getOrderAscDesc( ) );
            }
        }

        //Assemble all clauses
        StringBuilder finalQuery = new StringBuilder();
        finalQuery.append(query);
        
        if (stringBuilder.length() > 0) {
            finalQuery.append(" WHERE ");
            finalQuery.append(stringBuilder.toString());
        }
        if (stringBuilderOrderBy.length() > 0) {
            finalQuery.append(" ORDER BY ");
            finalQuery.append(stringBuilderOrderBy);
        }

        return finalQuery.toString( );
    }
    
    private void setFilterValues( DAOUtil daoUtil, AtelierSearcher atelierSearcher ) 
    {
        int nCpt = 1;
        
        if ( atelierSearcher.getTitreOuDescription( ) != null ) 
        {
        	daoUtil.setString(nCpt++, "%"+atelierSearcher.getTitreOuDescription( )+"%");
            daoUtil.setString(nCpt++, "%"+atelierSearcher.getTitreOuDescription( )+"%");
        }
        if ( atelierSearcher.getType( ) != null ) 
        {
        	daoUtil.setString( nCpt++, atelierSearcher.getType( ) );
        }
        if ( atelierSearcher.getCodeCampagne( ) != null ) 
        {
            daoUtil.setString( nCpt++, atelierSearcher.getCodeCampagne( ) );
        }
        if ( atelierSearcher.getCodeTheme( ) != null ) 
        {
            daoUtil.setString( nCpt++, atelierSearcher.getCodeTheme( ) );
        }
        if ( atelierSearcher.getTypeLocalisation( ) != null ) 
        {
            daoUtil.setString( nCpt++, atelierSearcher.getTypeLocalisation( ) );
        }
        if ( atelierSearcher.getLocalisationArdt( ) != null ) 
        {
        	daoUtil.setString( nCpt++, atelierSearcher.getLocalisationArdt( ) );
        }
        if ( atelierSearcher.getHandicap() != null ) {
            daoUtil.setString(nCpt++, atelierSearcher.getHandicap());
        }
     }
}
