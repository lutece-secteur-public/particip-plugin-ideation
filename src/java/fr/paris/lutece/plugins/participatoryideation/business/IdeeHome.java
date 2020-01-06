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

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class provides instances management methods (create, find, ...) for Idee objects
 */

public final class IdeeHome
{
    // Static variable pointed at the DAO instance

    private static IIdeeDAO _dao = SpringContextService.getBean( "participatoryideation.ideeDAO" );
    private static Plugin _plugin = PluginService.getPlugin( "participatoryideation" );

    /**
     * Private constructor - this class need not be instantiated
     */
    private IdeeHome(  )
    {
    }

    /**
     * Create an instance of the idee class
     * @param idee The instance of the Idee which contains the informations to store
     * @return The  instance of idee which has been created with its primary key.
     */
    public static Idee create( Idee idee )
    {
        _dao.insert( idee, _plugin );

        return idee;
    }

    /**
     * Update of the idee which is specified in parameter
     * @param idee The instance of the Idee which contains the data to store
     * @return The instance of the  idee which has been updated
     */
    public static Idee updateBO( Idee idee )
    {
        _dao.storeBO( idee, _plugin );

        return idee;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Finders

    /**
     * Returns an instance of a idee whose identifier is specified in parameter
     * @param nKey The idee primary key
     * @return an instance of Idee
     */
    public static Idee findByPrimaryKey( int nKey )
    {
        return _dao.load( nKey, _plugin);
    }

    /**
     * Returns an instance of a idee whose codes is specified in parameters
     * @param strCodeCampagne The campagne code
     * @param nCodeIdee The idee code
     * @return an instance of Idee
     */
    public static Idee findByCodes( String strCodeCampagne, int nCodeIdee )
    {
        return _dao.loadByCodes( strCodeCampagne, nCodeIdee, _plugin);
    }

    /**
     * Load the data of all the idee objects and returns them in form of a collection
     * @return the collection which contains the data of all the idee objects
     */
    public static Collection<Idee> getIdeesList( )
    {
        return _dao.selectIdeesList( _plugin );
    }

    /**
     * Load the data of all the idee objects searched and returns them in form of a collection
     * @param ideeSearcher an IdeeSearcher
     * @return the collection which contains the data of all the idee objects
     */
    public static Collection<Idee> getIdeesListSearch( IdeeSearcher ideeSearcher )
    {
        return _dao.selectIdeesListSearch( _plugin, ideeSearcher );
    }
    
    /**
     * Load the id of all the idee objects and returns them in form of a collection
     * @return the collection which contains the id of all the idee objects
     */
    public static Collection<Integer> getIdIdeesList( )
    {
        return _dao.selectIdIdeesList( _plugin );
    }
    
    /**
     * Check if the idee has a parent
     * @param nIdIdee The identifier of the idee
     * @return The identifier of the parent idee, or 0
     */
    public static int hasParent( int nIdIdee )
    {
        return _dao.hasParent( nIdIdee, _plugin );
    }

    /**
     * Delete all the links associated with the parent idee
     * @param nParentIdeeId The identifier of the parent idee
     */
    public static void removeLinkByParent( int nParentIdeeId )
    {
        _dao.deleteLinkByParent( nParentIdeeId, _plugin );
    }

    /**
     * Delete all the links associated with the child idee
     * @param nChildIdeeId The identifier of the child idee
     */
    public static void removeLinkByChild( int nChildIdeeId )
    {
        _dao.deleteLinkByChild( nChildIdeeId, _plugin );
    }

    /**
     * Load the parent and children idees from the ids
     * @param idee The Idee with child and parents lists not null.
     */
    public static void loadMissingLinkedIdees ( Idee idee ) {
        //Use the mandatory title to check if an idee has been fully loaded from the database.
        List<Idee> listChildIdees = idee.getChildIdees();
        for (int i = 0 ; i<listChildIdees.size(); i++) {
            if (listChildIdees.get(i).getTitre() == null) {
                idee.getChildIdees().set(i, IdeeHome.findByPrimaryKey(listChildIdees.get(i).getId()));
            }
        }
        List<Idee> listParentIdees = idee.getParentIdees();
        for (int i = 0 ; i<listParentIdees.size(); i++) {
            if (listParentIdees.get(i).getTitre() == null) {
                idee.getParentIdees().set(i, IdeeHome.findByPrimaryKey(listParentIdees.get(i).getId()));
            }
        }
    }

    /* ***********************************************************************************
       * SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS * 
       * SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS SUB_INFOS * 
       *********************************************************************************** */

	/**
	 * Method of getting infos of sub ideas.
	 */
	public enum GetSubIdeesMethod { 
	    /**
	     * Only of this idea.
	     */
		THIS_ONLY, 
	      
	    /**
	     * Of the idea and its direct children.
	     */
	  	THIS_AND_CHILDREN, 
	      
	    /**
	     * Of the idea, its direct children, and sub-children.
	     */
	  	ALL_FAMILY 
	};
	
	/**
	 * Returns a list of sub ideas.
	 */
	public static List<Integer> getSubIdeesId(int nIdeeId, GetSubIdeesMethod method) {
		
		List<Integer> _ids = new ArrayList<Integer>( );
		_ids.add(nIdeeId);
		
		// Appends ids of children, recursively or only for the first children.
		if (method != GetSubIdeesMethod.THIS_ONLY) 
		{
			Idee _idee = IdeeHome.findByPrimaryKey(nIdeeId);		
			List<Idee> children = _idee.getChildIdees();
			if (children != null) 
			{
				for (Idee ideeChild : children) {
					if (ideeChild != null)
					{
						GetSubIdeesMethod _newMethod = (method == GetSubIdeesMethod.THIS_AND_CHILDREN ? GetSubIdeesMethod.THIS_ONLY : GetSubIdeesMethod.ALL_FAMILY);
						_ids.addAll( getSubIdeesId(ideeChild.getId(), _newMethod) );
					}
				}
			}
		}
		
		return _ids;
	}

	/**
	 * Returns a string with the pseudo of idea and eventually its children.
	 */
	public static String getSubIdeesNicknames(int nIdeeId, GetSubIdeesMethod method, String separator) {
				
		StringBuffer childrenPseudos = new StringBuffer();
		
		List<Integer> _nIds = getSubIdeesId( nIdeeId, method);
		
		for (int nIds :_nIds )
		{
			Idee _idee = IdeeHome.findByPrimaryKey(nIds);		

			childrenPseudos.append( UserPreferencesService.instance().getNickname(_idee.getLuteceUserName()) );
			
			switch ( _idee.getDepositaireType() )
			{
			  case "PARTICULIER": childrenPseudos.append( " (particulier)"                                        ).append( separator ); break;
			  case "ASSO"       : childrenPseudos.append( " (association "         + _idee.getDepositaire() + ")" ).append( separator ); break;
			  case "CONSEIL"    : childrenPseudos.append( " (conseil de quartier " + _idee.getDepositaire() + ")" ).append( separator ); break;
			  case "AUTRE"      : childrenPseudos.append( " (autre "               + _idee.getDepositaire() + ")" ).append( separator ); break;
			}
			
		}
		
		String s = childrenPseudos.toString();
		return s.substring(0, s.length() - separator.length());
	}

	/**
	 * Returns a string with the pseudo of idea and eventually its children, nicely structured.
	 */
	public static String getSubIdeesNicknamesNiceText(int nIdeeId, GetSubIdeesMethod method, String separator) {
				
		StringBuffer childrenPseudos = new StringBuffer();
		
		List<Integer> _nIds = getSubIdeesId( nIdeeId, method);

		// How many depositaries, by type ?
		int nbPart = 0;
		int nbAsso = 0;
		int nbCons = 0;
		int nbAutr = 0;
		for (int nId :_nIds )
		{
			switch ( IdeeHome.findByPrimaryKey(nId).getDepositaireType() )
			{
			  case "PARTICULIER": nbPart++; break;
			  case "ASSO"       : nbAsso++; break;
			  case "CONSEIL"    : nbCons++; break;
			  case "AUTRE"      : nbAutr++; break;
			}
		}

		if ( nbPart + nbAsso + nbCons + nbAutr == 0) {
			AppLogService.error("IdeeHome.getSubIdeesNicknamesNiceText says 'Unable to find depositaries for proposition #" + nIdeeId + "'.");
			return "Aucun dépositaire n'a été identifié pour ce projet";
		}
		
		if      ( nbAsso == 0 && nbCons == 0 && nbAutr == 0 ) {
			if ( nbPart == 1 ) {
				childrenPseudos.append( "Ce projet a été élaboré sur proposition d’un particulier" );
			}
			else {
				childrenPseudos.append( "Ce projet a été élaboré à partir d'un regroupement de propositions de particuliers" );
			}
		}
		else if ( nbPart == 0 && nbAsso == 1 && nbCons == 0 && nbAutr == 0 ) childrenPseudos.append( "Ce projet a été élaboré sur proposition de l’association « " + IdeeHome.findByPrimaryKey(_nIds.get(0)).getDepositaire() + " »" );
		else if ( nbPart == 0 && nbAsso == 0 && nbCons == 1 && nbAutr == 0 ) childrenPseudos.append( "Ce projet a été élaboré sur proposition du conseil de quartier « " + IdeeHome.findByPrimaryKey(_nIds.get(0)).getDepositaire() + " »" );
		else if ( nbPart == 0 && nbAsso == 0 && nbCons == 0 && nbAutr == 1 ) childrenPseudos.append( "Ce projet a été élaboré sur proposition du collectif « " + IdeeHome.findByPrimaryKey(_nIds.get(0)).getDepositaire() + " »" );
		else { 
			childrenPseudos.append( "Ce projet a été élaboré sur proposition de ... " );
			childrenPseudos.append( getSubIdeesNicknames( nIdeeId, method, separator ) );
		}
		
		return childrenPseudos.toString();
	}

}

