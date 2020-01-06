/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatoryideation.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatoryideation.business.Idee.Status;
import fr.paris.lutece.plugins.participatoryideation.web.IdeationApp;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.sql.DAOUtil;

/**
 * This class provides Data Access methods for Idee objects
 */

public final class IdeeDAO implements IIdeeDAO
{

    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_idee ) FROM ideation_idees";
    private static final String SQL_QUERY_NEW_CODE_IDEE = "SELECT max( code_idee ) FROM ideation_idees where code_campagne = ?";

    private static final String SQL_QUERY_INSERT = "INSERT INTO ideation_idees ( id_idee, lutece_user_name, titre, dejadepose, description, cout, code_theme, localisation_type, localisation_ardt, depositaire_type, depositaire, accept_exploit, accept_contact, address,longitude,latitude,creation_timestamp,code_campagne,code_idee,type_nqpv_qva,id_nqpv_qva,libelle_nqpv_qva, status_public, status_eudonet, motif_recev,id_project, titre_projet, url_projet, winner_projet, creationmethod, operatingbudget, handicap, handicap_complement) VALUES ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,? ) ";
    private static final String SQL_QUERY_UPDATE = "UPDATE ideation_idees SET eudonet_exported_tag=?, status_public=?, status_eudonet=?, motif_recev=?, type_nqpv_qva=?, id_nqpv_qva=?, libelle_nqpv_qva=?, id_project = ?, titre_projet = ?, url_projet =?, winner_projet =?, titre =? , description =? , cout =? , localisation_type =? , localisation_ardt =?, handicap=?, handicap_complement=? WHERE id_idee = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT idees.id_idee, idees.lutece_user_name, idees.titre, idees.dejadepose, idees.description, idees.cout, idees.code_theme, idees.localisation_type, idees.localisation_ardt, idees.depositaire_type, idees.depositaire, idees.accept_exploit, idees.accept_contact, idees.address, idees.longitude, idees.latitude, idees.creation_timestamp, idees.code_campagne, idees.code_idee, idees.eudonet_exported_tag, idees.type_nqpv_qva, idees.id_nqpv_qva, idees.libelle_nqpv_qva, idees.status_public, idees.status_eudonet, idees.motif_recev, idees.id_project, idees.titre_projet, idees.url_projet, idees.winner_projet, idees.creationmethod, idees.operatingbudget, idees.handicap, idees.handicap_complement FROM ideation_idees idees";
    private static final String SQL_QUERY_SELECT = SQL_QUERY_SELECTALL+" WHERE idees.id_idee = ?";
    private static final String SQL_QUERY_SELECT_BY_CODES = SQL_QUERY_SELECTALL+" WHERE idees.code_campagne = ? and idees.code_idee = ?";
    private static final String SQL_QUERY_SELECTALL_ID = "SELECT id_idee FROM ideation_idees";

    private static final String SQL_QUERY_NEW_PK_FILE = "SELECT max( id_idee_file ) FROM ideation_idees_files";
    private static final String SQL_QUERY_INSERT_FILE = "INSERT INTO ideation_idees_files (id_idee_file, id_file, id_idee, type) values ( ?, ? , ? , ? )";
    private static final String SQL_QUERY_SELECTALL_FILES = "SELECT id_file, id_idee, type FROM ideation_idees_files";
    private static final String SQL_QUERY_SELECT_FILE = SQL_QUERY_SELECTALL_FILES + " WHERE id_idee = ?";

    private static final String SQL_QUERY_NEW_PK_IDEE_LINK = "SELECT max( id_idee_link ) FROM ideation_idees_links";
    private static final String SQL_QUERY_INSERT_LINK = "INSERT INTO ideation_idees_links ( id_idee_link, id_idee_parent, id_idee_child ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_SELECT_LINKED_IDEES = "SELECT idees.id_idee, idees.code_campagne, idees.code_idee FROM ideation_idees_links links INNER JOIN ideation_idees idees ON";
    private static final String SQL_QUERY_SELECT_CHILD_IDEES = SQL_QUERY_SELECT_LINKED_IDEES + " links.id_idee_child = idees.id_idee WHERE links.id_idee_parent = ?";
    private static final String SQL_QUERY_SELECT_PARENT_IDEES = SQL_QUERY_SELECT_LINKED_IDEES + " links.id_idee_parent = idees.id_idee WHERE links.id_idee_child = ?";

    private static final String SQL_QUERY_DELETE_LINK_BY_PARENT = "DELETE FROM ideation_idees_links WHERE id_idee_parent = ?";
    private static final String SQL_QUERY_DELETE_LINK_BY_CHILD = "DELETE FROM ideation_idees_links WHERE id_idee_child = ?";
    private static final String SQL_QUERY_SELECTALL_LINKS = "SELECT child_idees.id_idee, child_idees.code_campagne, child_idees.code_idee, parent_idees.id_idee, parent_idees.code_campagne, parent_idees.code_idee FROM ideation_idees_links links inner join ideation_idees child_idees ON links.id_idee_child = child_idees.id_idee inner join ideation_idees parent_idees ON links.id_idee_parent = parent_idees.id_idee";
   

    /**
     * Generates a new primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin)
    {
	    int nKey = 1;
		
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK , plugin  ) )
        {
		    daoUtil.executeQuery( );
		
		    if( daoUtil.next( ) )
		    {
		            nKey = daoUtil.getInt( 1 ) + 1;
		    }
        }
        
        return nKey;
    }

    /**
     * Generates a new code idee for this campagne
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newCodeIdee( String strCodeCampagne, Plugin plugin)
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_CODE_IDEE , plugin  ) )
        {
	        daoUtil.setString( 1, strCodeCampagne );
	        daoUtil.executeQuery( );
	
	        if( daoUtil.next( ) )
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
    public void insert( Idee idee, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {        
	        int nCpt=1;
	        
	        idee.setId( newPrimaryKey( plugin ) );
	        idee.setCodeIdee( newCodeIdee( idee.getCodeCampagne(  ), plugin ) );
	
	        daoUtil.setInt   ( nCpt++, idee.getId( ) );
	        daoUtil.setString( nCpt++, idee.getLuteceUserName() );
	        daoUtil.setString( nCpt++, idee.getTitre() );
	        daoUtil.setString( nCpt++, idee.getDejadepose() );
	        daoUtil.setString( nCpt++, idee.getDescription() );
	        
	        if ( idee.getCout() != null )
	        {
	                daoUtil.setLong( nCpt++, idee.getCout() );
	        }
	        else
	        {
	                daoUtil.setLongNull( nCpt++ );    
	        }
	        
	        daoUtil.setString ( nCpt++, idee.getCodeTheme() );
	        daoUtil.setString ( nCpt++, idee.getLocalisationType() );
	        daoUtil.setString ( nCpt++, idee.getLocalisationArdt() );
	        daoUtil.setString ( nCpt++, idee.getDepositaireType());
	        daoUtil.setString ( nCpt++, idee.getDepositaire());
	        daoUtil.setBoolean( nCpt++, idee.isAcceptExploit());
	        daoUtil.setBoolean( nCpt++, idee.isAcceptContact());
	        daoUtil.setString ( nCpt++, idee.getAdress());
	        
	        if ( idee.getLongitude() != null ) 
	        {
	            daoUtil.setDouble( nCpt++, idee.getLongitude(  ) );
	        } 
	        else 
	        {
	            daoUtil.setDoubleNull( nCpt++ );
	        }
	        
	        if ( idee.getLatitude() != null ) 
	        {
	            daoUtil.setDouble( nCpt++, idee.getLatitude(  ) );
	        } 
	        else 
	        {
	            daoUtil.setDoubleNull( nCpt++ );
	        }
	        
	        daoUtil.setTimestamp( nCpt++, idee.getCreationTimestamp() );
	        daoUtil.setString   ( nCpt++, idee.getCodeCampagne() );
	        daoUtil.setInt      ( nCpt++, idee.getCodeIdee() );
	        daoUtil.setString   ( nCpt++, idee.getTypeQpvQva() );
	        daoUtil.setString   ( nCpt++, idee.getIdQpvQva() );
	        daoUtil.setString   ( nCpt++, idee.getLibelleQpvQva() );
	        daoUtil.setString   ( nCpt++, idee.getStatusPublic().getValeur() );
	        daoUtil.setString   ( nCpt++, ( idee.getStatusEudonet() == null) ? null : idee.getStatusEudonet().getValeur() );
	        daoUtil.setString   ( nCpt++, idee.getMotifRecev() );
	        daoUtil.setString   ( nCpt++, idee.getIdProjet( ) );
	        daoUtil.setString   ( nCpt++, idee.getTitreProjet( ) );
	        daoUtil.setString   ( nCpt++, idee.getUrlProjet( ) );
	        daoUtil.setString   ( nCpt++, idee.getWinnerProjet( ) );
	        daoUtil.setString   ( nCpt++, idee.getCreationmethod() );
	        daoUtil.setString   ( nCpt++, idee.getOperatingbudget() );
	        daoUtil.setString   ( nCpt++, idee.getHandicap() );
	        daoUtil.setString   ( nCpt++, ( idee.getHandicapComplement() == null ? "" : idee.getHandicapComplement() ) );
	
	        daoUtil.executeUpdate( );
        }
        
        insertFiles(idee, plugin);
    }

    private void insertFiles(Idee idee, Plugin plugin) {
        for ( File file : idee.getImgs(  ) ) 
        {
            int id = file.getIdFile();
            insertFile(Idee.ATTACHED_FILE_TYPE_IMG, id, idee.getId(), plugin);
        }
        for ( File file : idee.getDocs (  ) ) 
        {
            int id = file.getIdFile();
            insertFile(Idee.ATTACHED_FILE_TYPE_DOC, id, idee.getId(), plugin);
        }
    }

    /**
     * Generates a new idee_file primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKeyFile( Plugin plugin)
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_FILE , plugin  ) )
        {
	        daoUtil.executeQuery( );
	
	        if( daoUtil.next( ) )
	        {
	                nKey = daoUtil.getInt( 1 ) + 1;
	        }
        }
        
        return nKey;
    }

    private void insertFile(String string, int id, int id2, Plugin plugin) {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_FILE , plugin) )
        {
	        daoUtil.setInt   ( 1, newPrimaryKeyFile( plugin ) );
	        daoUtil.setInt   ( 2, id );
	        daoUtil.setInt   ( 3, id2 );
	        daoUtil.setString( 4, string );
	        
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Idee load( int nKey, Plugin plugin )
    {
        Idee idee = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
	        daoUtil.setInt( 1 , nKey );
	        daoUtil.executeQuery( );
	
	        if ( daoUtil.next( ) )
	        {   
	            idee = getRow(daoUtil);
	        }
        }
        
        if ( idee != null ) 
        {
            loadFileIds    (idee, plugin);
            loadLinkedIdees(idee, plugin);
        }
        
        return idee;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Idee loadByCodes( String strCodeCampagne, int nCodeIdee, Plugin plugin )
    {
        Idee idee = null;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_CODES, plugin ) )
        {
	        daoUtil.setString( 1 , strCodeCampagne );
	        daoUtil.setInt   ( 2 , nCodeIdee );
	        daoUtil.executeQuery( );
	
	        if ( daoUtil.next( ) )
	        {   
	            idee = getRow(daoUtil);
	        }
        }
        
        if ( idee != null ) 
        {
            loadFileIds    (idee, plugin);
            loadLinkedIdees(idee, plugin);
        }
        
        return idee;
    }

    private void loadFileIds(Idee idee, Plugin plugin) {
        try (DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_FILE , plugin ) )
        {
	        daoUtil.setInt( 1 , idee.getId(  ) );
	        daoUtil.executeQuery( );
	
	        List<File> listDocs = new ArrayList<File>();
	        List<File> listImgs = new ArrayList<File>();
	        while ( daoUtil.next( ) )
	        {
	            int fileId = daoUtil.getInt(1);
	            int ideeId = daoUtil.getInt(2);
	            String type = daoUtil.getString(3);
	            if (Idee.ATTACHED_FILE_TYPE_DOC.equals(daoUtil.getString(3))) {
	                listDocs.add(FileHome.findByPrimaryKey(fileId));
	            } else if (Idee.ATTACHED_FILE_TYPE_IMG.equals(daoUtil.getString(3))) {
	                listImgs.add(FileHome.findByPrimaryKey(fileId));
	            } else {
	                AppLogService.info("Ideation, unknown attached file type " + fileId + "," + ideeId + "," + type );
	            }
	        }
	
	        idee.setDocs(listDocs);
	        idee.setImgs(listImgs);
        }
    }

    private void loadLinkedIdees(Idee idee, Plugin plugin) {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_CHILD_IDEES , plugin ) )
        {
	        daoUtil.setInt( 1 , idee.getId(  ) );
	        daoUtil.executeQuery( );
	        List<Idee> listIdees = getLinkedIdees(daoUtil);
	        idee.setChildIdees(listIdees);
        }
	
	     try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARENT_IDEES , plugin ) )
	     {
	        daoUtil.setInt( 1 , idee.getId(  ) );
	        daoUtil.executeQuery( );
	        List<Idee> listIdees = getLinkedIdees(daoUtil);
	        idee.setParentIdees(listIdees);
        }
    }

    private List<Idee> getLinkedIdees(DAOUtil daoUtil) {
        List<Idee> listIdees = new ArrayList<Idee>();
        while ( daoUtil.next( ) )
        {
            Idee otherIdee = getFirstLinkedIdeeRow(daoUtil);
            listIdees.add(otherIdee);
        }
        return listIdees;
    }

    private Idee getFirstLinkedIdeeRow(DAOUtil daoUtil) {
        return getLinkedIdeeRow(daoUtil, 1);
    }
    private Idee getChildIdeeRow(DAOUtil daoUtil) {
        return getLinkedIdeeRow(daoUtil, 1);
    }
    private Idee getParentIdeeRow(DAOUtil daoUtil) {
        return getLinkedIdeeRow(daoUtil, 4);
    }
    private Idee getLinkedIdeeRow(DAOUtil daoUtil, int nCpt) {
        Idee otherIdee = new Idee();
        otherIdee.setId(daoUtil.getInt(nCpt++));
        otherIdee.setCodeCampagne(daoUtil.getString(nCpt++));
        otherIdee.setCodeIdee(daoUtil.getInt(nCpt++));
        return otherIdee;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int hasParent( int nIdIdee, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_PARENT_IDEES, plugin ) )
        {
	        daoUtil.setInt( 1, nIdIdee );
	        daoUtil.executeQuery( );
	        while ( daoUtil.next( ) )
	        {
	            return daoUtil.getInt( 1 );
	        }
        }
        
        return 0;
    }
    /**
     * {@inheritDoc }
     */
    @Override
    public void storeBO( Idee idee, Plugin plugin )
    {
    	try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin ) )
    	{
	       int nCpt=1;
	       
	       daoUtil.setInt( nCpt++, idee.getExportedTag( ) );
	       daoUtil.setString( nCpt++, idee.getStatusPublic().getValeur( ) );
	       if(idee.getStatusEudonet()!=null){
	    	   daoUtil.setString( nCpt++, idee.getStatusEudonet().getValeur() );
	       }else{
	    	   daoUtil.setString( nCpt++, null );
	       }
	       daoUtil.setString( nCpt++, idee.getMotifRecev());
	
	       daoUtil.setString( nCpt++, idee.getTypeQpvQva());
	       daoUtil.setString( nCpt++, idee.getIdQpvQva());
	       daoUtil.setString( nCpt++, idee.getLibelleQpvQva());
	       daoUtil.setString(nCpt++, idee.getIdProjet( ) );
	       daoUtil.setString(nCpt++, idee.getTitreProjet( ) );
	       daoUtil.setString( nCpt++, idee.getUrlProjet( ) );
	       daoUtil.setString( nCpt++, idee.getWinnerProjet( ) );
	
	       daoUtil.setString( nCpt++, idee.getTitre() );
	       daoUtil.setString( nCpt++, idee.getDescription() );
	       
	       if(idee.getCout()!=null)
	       {
	               daoUtil.setLong(nCpt++, idee.getCout());
	       }
	       else
	       {
	               daoUtil.setLongNull(nCpt++);    
	       
	       }
	       
	       daoUtil.setString( nCpt++, idee.getLocalisationType() );
	       daoUtil.setString( nCpt++, idee.getLocalisationArdt() );
	             
	       daoUtil.setString( nCpt++, idee.getHandicap() );
	       daoUtil.setString( nCpt++, idee.getHandicapComplement() );
	
		   daoUtil.setInt( nCpt++, idee.getId( ) );
		
		   daoUtil.executeUpdate( );
       	}
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Idee> selectIdeesList( Plugin plugin ) {
        return selectIdeesListSearch( plugin, null );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Idee> selectIdeesListSearch( Plugin plugin, IdeeSearcher ideeSearcher )
    {
        Map<Integer, Idee> ideeMap = new LinkedHashMap<Integer, Idee>(  );
        
        String queryStr = ( ideeSearcher != null ) ? appendFilters( SQL_QUERY_SELECTALL, ideeSearcher ) : SQL_QUERY_SELECTALL;
        try ( DAOUtil daoUtil = new DAOUtil( queryStr, plugin ) )
        {
	        if (ideeSearcher != null) {
	            setFilterValues( daoUtil, ideeSearcher );
	        } 
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            Idee idee = getRow(daoUtil);
	            ideeMap.put( idee.getId(), idee );
	            idee.setDocs(new ArrayList<File>());
	            idee.setImgs(new ArrayList<File>());
	            idee.setChildIdees(new ArrayList<Idee>());
	            idee.setParentIdees(new ArrayList<Idee>());
	        }
        }

        //Use workflow services instead of joining into the workflow tables
        if ( ideeSearcher != null && ideeSearcher.getIdWorkflowState() != null ) {
            WorkflowService workflowService = WorkflowService.getInstance();
            if ( workflowService.isAvailable() ) {
                List<Integer> allIds = workflowService.getResourceIdListByIdState( ideeSearcher.getIdWorkflowState(  ), Idee.WORKFLOW_RESOURCE_TYPE );
                HashSet<Integer> hsAllIds = new HashSet<Integer>(allIds);
                for(Iterator<Entry<Integer, Idee>> it = ideeMap.entrySet().iterator(); it.hasNext(); ) {
                    Entry<Integer, Idee> entry = it.next();
                    if(!hsAllIds.contains(entry.getKey())) {
                        it.remove();
                    }
                }
            }
        }

        //TODO do we need to filter these ?
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_FILES, plugin ) )
        {
	        daoUtil.executeQuery(  );
	        while ( daoUtil.next(  ) )
	        {
	            int ideeId  = daoUtil.getInt( 2 );
	            int fileId  = daoUtil.getInt( 1 );
	            String type = daoUtil.getString( 3);
	            
	            Idee idee   = ideeMap.get( ideeId ); 
	            if ( idee != null ) 
	            {
	                if ( Idee.ATTACHED_FILE_TYPE_DOC.equals(type) ) 
	                {
	                    idee.getDocs(  ).add( FileHome.findByPrimaryKey( fileId ) );
	                } 
	                else if ( Idee.ATTACHED_FILE_TYPE_IMG.equals(type) ) 
	                {
	                    idee.getImgs(  ).add( FileHome.findByPrimaryKey( fileId ) );
	                } 
	                else 
	                {
	                    AppLogService.info( "Ideation, unknown attached file type " + fileId + "," + ideeId + "," + type );
	                }
	            }
	        }
        }

        //TODO do we need to filter these ?
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_LINKS, plugin ) )
        {
	        daoUtil.executeQuery(  );
	        while ( daoUtil.next(  ) )
	        {
	            int idIdeeChild    = daoUtil.getInt( 1 );
	            int idIdeeParent   = daoUtil.getInt( 4 );
	            Idee mapIdeeParent = ideeMap.get( idIdeeParent );
	            Idee mapIdeeChild  = ideeMap.get( idIdeeChild );
	
	            Idee ideeParent = ( mapIdeeParent == null ) ? getParentIdeeRow( daoUtil ) : mapIdeeParent;
	            Idee ideeChild  = ( mapIdeeChild  == null ) ? getChildIdeeRow ( daoUtil ) : mapIdeeChild;
	
	            if ( mapIdeeParent != null ) 
	            {
	                mapIdeeParent.getChildIdees().add( ideeChild );
	            }
	            
	            if ( mapIdeeChild != null ) 
	            {
	                mapIdeeChild.getParentIdees().add( ideeParent );
	            }
	        }
        }

        ArrayList<Idee> result = new ArrayList<Idee>(ideeMap.values());
        return result;
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Integer> selectIdIdeesList( Plugin plugin )
    {
        Collection<Integer> ideeList = new ArrayList<Integer>( );
        
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL_ID, plugin ) )
        {
	        daoUtil.executeQuery(  );
	
	        while ( daoUtil.next(  ) )
	        {
	            ideeList.add( daoUtil.getInt( 1 ) );
	        }
        }

        return ideeList;
    }
    
    private Idee getRow( DAOUtil daoUtil )
    {
        int nCpt = 1;
        
        Idee idee = new Idee();
        idee.setId( daoUtil.getInt( nCpt++ ) );
        idee.setLuteceUserName( daoUtil.getString( nCpt++ ));
        idee.setTitre( daoUtil.getString( nCpt++ ) );
        idee.setDejadepose( daoUtil.getString( nCpt++ ) );
        idee.setDescription( daoUtil.getString( nCpt++ ) ); 
        idee.setCout((Long) daoUtil.getObject(nCpt++ ));
        idee.setCodeTheme( daoUtil.getString( nCpt++ ) );
        idee.setLocalisationType( daoUtil.getString( nCpt++ ) );
        idee.setLocalisationArdt( daoUtil.getString( nCpt++ ) );
        idee.setDepositaireType( daoUtil.getString( nCpt++ ) );
        idee.setDepositaire( daoUtil.getString( nCpt++ ) );
        idee.setAcceptExploit(daoUtil.getBoolean( nCpt++ )) ;  
        idee.setAcceptContact(daoUtil.getBoolean( nCpt++ )) ;  
        idee.setAdress(daoUtil.getString( nCpt++ ));
        
        Float fLongitude = ( (Float) daoUtil.getObject( nCpt++ ) );
        if ( fLongitude != null ) 
        {
            idee.setLongitude( fLongitude.doubleValue() );
        }
        
        Float fLatitude = ( (Float) daoUtil.getObject( nCpt++ ) );
        if ( fLatitude != null ) 
        {
            idee.setLatitude( fLatitude.doubleValue() );
        }
        
        idee.setCreationTimestamp(daoUtil.getTimestamp( nCpt++ ));
        idee.setCodeCampagne(daoUtil.getString( nCpt++ ));
        idee.setCodeIdee(daoUtil.getInt( nCpt++ ));
        idee.setExportedTag(daoUtil.getInt( nCpt++ ));
        idee.setTypeQpvQva(daoUtil.getString( nCpt++ ));
        idee.setIdQpvQva(daoUtil.getString( nCpt++ ));
        idee.setLibelleQpvQva(daoUtil.getString( nCpt++ ));
        idee.setStatusPublic(Idee.Status.getByValue(daoUtil.getString( nCpt++ )));
        idee.setStatusEudonet(Idee.Status.getByValue(daoUtil.getString( nCpt++ )));
        idee.setMotifRecev(daoUtil.getString( nCpt++ ));
        idee.setIdProjet( daoUtil.getString( nCpt++ ));
        idee.setTitreProjet(daoUtil.getString( nCpt++ ));
        idee.setUrlProjet(daoUtil.getString( nCpt++ ));
        idee.setWinnerProjet(daoUtil.getString( nCpt++ ));
        idee.setCreationmethod(daoUtil.getString( nCpt++ ));
        idee.setOperatingbudget(daoUtil.getString( nCpt++ ));
        idee.setHandicap(daoUtil.getString( nCpt++ ));
        idee.setHandicapComplement(daoUtil.getString( nCpt++ ));
        
        return idee;
    }
    
    /**
     * Creates the preparedStatement for apply filters
     * @param query The begining of the query
     * @param ideeSearcher The ideeSearcher
     * @return The sql statement
     */
    private String appendFilters( String query, IdeeSearcher ideeSearcher ) {

        //Create the joins
        StringBuilder stringBuilderJoin = new StringBuilder();

        //Create the where clause
        StringBuilder stringBuilder = new StringBuilder();
        if ( ideeSearcher.getCodeCampagne() != null ) {
            stringBuilder.append(" idees.code_campagne = ? AND");
        }
        if ( ideeSearcher.getCodeTheme() != null ) {
            stringBuilder.append(" idees.code_theme = ? AND");
        }
        if ( ideeSearcher.getExportedTag() != null ) {
            stringBuilder.append(" idees.eudonet_exported_tag = ? AND");
        }
        if ( ideeSearcher.getTitreOuDescriptionouRef() != null ) {
            stringBuilder.append(" (idees.titre LIKE ? OR idees.description LIKE ? OR idees.id_idee = ? ) AND");
        }
        if ( ideeSearcher.getTypeQpvQva() != null ) {
            if (IdeeSearcher.QPVQVA_UNKNOWN.equals(ideeSearcher.getTypeQpvQva())) {
                stringBuilder.append(" idees.type_nqpv_qva != ? AND");
                stringBuilder.append(" idees.type_nqpv_qva != ? AND");
                stringBuilder.append(" idees.type_nqpv_qva != ? AND");
                stringBuilder.append(" idees.type_nqpv_qva != ? AND");
                stringBuilder.append(" idees.type_nqpv_qva != ? AND");
                stringBuilder.append(" idees.type_nqpv_qva != ? AND");
            } else {
                stringBuilder.append(" idees.type_nqpv_qva = ? AND");
            }
        }
        if ( IdeationApp.HANDICAP_LABEL_YES.equals( ideeSearcher.getHandicap() ) ) {
            stringBuilder.append(" idees.handicap = ? AND");
        } else if ( IdeationApp.HANDICAP_LABEL_NO.equals( ideeSearcher.getHandicap() ) ) {
            stringBuilder.append(" ( (idees.handicap IS NULL) OR (idees.handicap = ?) ) AND");
        }

        if ( ideeSearcher.getTypeLocalisation() != null ) {
            stringBuilder.append(" idees.localisation_type = ? AND");
        }
        if ( ideeSearcher.getArrondissement() != null ) {
            stringBuilder.append(" idees.localisation_ardt = ? AND");
        }
        if ( ideeSearcher.getStatusPublic( ) != null ) {
            stringBuilder.append(" idees.status_public = ? AND");
        }
        if( ideeSearcher.getLuteceUserName() != null ){
        	stringBuilder.append(" idees.lutece_user_name = ? AND");
        }
        
        if( ideeSearcher.getIsPublished() != null ){
        	stringBuilder.append(getFilterPublishedOrNot(ideeSearcher.getIsPublished()));
        }

        if (stringBuilder.length() > 0) {
            //Remove the final " AND"
            stringBuilder.setLength( stringBuilder.length(  ) - 4 );
        }

        //Create the order by clause without SQL Injection
        String strOrder = IdeeSearcher.ORDER_ASC.equals(ideeSearcher.getOrderAscDesc())
                       || IdeeSearcher.ORDER_DESC.equals(ideeSearcher.getOrderAscDesc()) ?
                              ideeSearcher.getOrderAscDesc() : null;
        StringBuilder stringBuilderOrderBy = new StringBuilder();
        if ( IdeeSearcher.COLUMN_REFERENCE.equals(ideeSearcher.getOrderColumn()) ) {
            //COLUMN_REFERENCE means lexicographic sort on code_campagne, code_idee
            stringBuilderOrderBy.append("idees.code_campagne");
            if ( strOrder != null ) {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(strOrder);
            }
            stringBuilderOrderBy.append( ", idees.code_idee");
            if ( strOrder != null ) {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(ideeSearcher.getOrderAscDesc());
            }
        } else {
            if ( IdeeSearcher.COLUMN_DATE_CREATION.equals(ideeSearcher.getOrderColumn())) {
                stringBuilderOrderBy.append("idees.");
                stringBuilderOrderBy.append(ideeSearcher.getOrderColumn());
            }
            if ( stringBuilderOrderBy.length() > 0 && strOrder != null) {
                stringBuilderOrderBy.append(" ");
                stringBuilderOrderBy.append(ideeSearcher.getOrderAscDesc());
            }
        }

        //Assemble all clauses
        StringBuilder finalQuery = new StringBuilder();
        finalQuery.append(query);
        if (stringBuilderJoin.length() > 0) {
            finalQuery.append(" ");
            finalQuery.append(stringBuilderJoin.toString());
        }
        if (stringBuilder.length() > 0) {
            finalQuery.append(" WHERE ");
            finalQuery.append(stringBuilder.toString());
        }
        if (stringBuilderOrderBy.length() > 0) {
            finalQuery.append(" ORDER BY ");
            finalQuery.append(stringBuilderOrderBy);
        }

        return finalQuery.toString();
    }

    /**
     * Sets the ideeSearcher values for export and search
     * @param daoUtil The daoUtil
     * @param validatedFilters The validatedFilters
     */
    private void setFilterValues( DAOUtil daoUtil, IdeeSearcher ideeSearcher ) {
        int nCpt = 1;
        if ( ideeSearcher.getCodeCampagne() != null ) {
            daoUtil.setString(nCpt++, ideeSearcher.getCodeCampagne());
        }
        if ( ideeSearcher.getCodeTheme() != null ) {
            daoUtil.setString(nCpt++, ideeSearcher.getCodeTheme());
        }
        if ( ideeSearcher.getExportedTag() != null ) {
            daoUtil.setInt(nCpt++, ideeSearcher.getExportedTag());
        }
        if ( ideeSearcher.getTitreOuDescriptionouRef() != null ) 
        {
            daoUtil.setString(nCpt++, "%"+ideeSearcher.getTitreOuDescriptionouRef()+"%");
            daoUtil.setString(nCpt++, "%"+ideeSearcher.getTitreOuDescriptionouRef()+"%");
            
	        if ( ideeSearcher.getTitreOuDescriptionouRef( ).matches("\\d+") )
	        {
	            daoUtil.setInt(nCpt++, Integer.parseInt( ideeSearcher.getTitreOuDescriptionouRef( ) ) );
	        }
	        else
	        {
	        	daoUtil.setString(nCpt++,StringUtils.EMPTY);
	        }
        }
        if ( ideeSearcher.getTypeQpvQva() != null ) {
            if (IdeeSearcher.QPVQVA_UNKNOWN.equals(ideeSearcher.getTypeQpvQva())) {
                daoUtil.setString(nCpt++, IdeationApp.QPV_QVA_ERR);
                daoUtil.setString(nCpt++, IdeationApp.QPV_QVA_NO);
                daoUtil.setString(nCpt++, IdeationApp.QPV_QVA_QPV);
                daoUtil.setString(nCpt++, IdeationApp.QPV_QVA_QVA);
                daoUtil.setString(nCpt++, IdeationApp.QPV_QVA_GPRU);
                daoUtil.setString(nCpt++, IdeationApp.QPV_QVA_QBP);
            } else {
                daoUtil.setString(nCpt++, ideeSearcher.getTypeQpvQva());
            }
        }
        if ( ideeSearcher.getHandicap() != null ) {
            daoUtil.setString(nCpt++, ideeSearcher.getHandicap());
        }
        if ( ideeSearcher.getTypeLocalisation() != null ) {
            daoUtil.setString(nCpt++, ideeSearcher.getTypeLocalisation());
        }
        if ( ideeSearcher.getArrondissement() != null ) {
            daoUtil.setString(nCpt++, ideeSearcher.getArrondissement());
        }
        if( ideeSearcher.getLuteceUserName() != null ){
        	daoUtil.setString(nCpt++, ideeSearcher.getLuteceUserName());
        }
        if( ideeSearcher.getStatusPublic( ) != null ){
        	daoUtil.setString(nCpt++, ideeSearcher.getStatusPublic( ));
        }
      
       }
    
    
    private static String getFilterPublishedOrNot(boolean bPublished)
    {
    	StringBuffer strBuffer=new StringBuffer();
    	strBuffer.append(" idees.status_public in (");
    	for(Status status:bPublished?Idee.Status.getAllStatusPublished():Idee.Status.getAllStatusUnPublished())
    	{
    		strBuffer.append("'");
    		strBuffer.append(status.getValeur());
    		strBuffer.append("'");
    		strBuffer.append(",");
    	}
    	//remove last ,
    	if(strBuffer.length()!=0)
    	{
    		strBuffer.setLength( strBuffer.length(  ) - 1 );
    	}
    	strBuffer.append(") AND");
    	return strBuffer.toString();
    	
    }
    
    /**
     * Generates a new idee_link primary key
     * @param plugin The Plugin
     * @return The new primary key
     */
    public int newPrimaryKeyIdeeLink( Plugin plugin )
    {
        int nKey = 1;

        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK_IDEE_LINK, plugin ) )
        {
	        daoUtil.executeQuery( );
	
	        if ( daoUtil.next( ) )
	        {
	            nKey = daoUtil.getInt( 1 ) + 1;
	        }
        }
        
        return nKey;
    }

    private void insertLink( int nIdParentIdee, int nIdChildIdee, Plugin plugin )
    {
        try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT_LINK, plugin ) )
        {
	        daoUtil.setInt( 1, newPrimaryKeyIdeeLink( plugin ) );
	        daoUtil.setInt( 2, nIdParentIdee );
	        daoUtil.setInt( 3, nIdChildIdee );
	        daoUtil.executeUpdate( );
        }
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteLinkByParent( int nParentIdeeId, Plugin plugin )
    {
    	try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK_BY_PARENT, plugin ))
        {
	        daoUtil.setInt( 1 , nParentIdeeId );
	        daoUtil.executeUpdate( );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void deleteLinkByChild( int nChildIdeeId, Plugin plugin )
    {
    	try ( DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE_LINK_BY_CHILD, plugin ))
        {
	        daoUtil.setInt( 1 , nChildIdeeId );
	        daoUtil.executeUpdate( );
        }
    }
}
