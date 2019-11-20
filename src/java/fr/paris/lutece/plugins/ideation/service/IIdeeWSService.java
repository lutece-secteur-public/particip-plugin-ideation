package fr.paris.lutece.plugins.ideation.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeSearcher;

public interface IIdeeWSService {
	  
	
	/**
     * Load the data of all the idee objects and returns them in form of a collection
     * @return the collection which contains the data of all the idee objects
     */
	public Collection<Idee> getIdeesList( );
	/**
	 * 
	 * @param nKey
	 * @return
	 */
	Idee getIdeeByIdentifiant( int nKey);
	
	/**
	 * 
	 * @param nKey
	 * @param strCampagne
	 * @return
	 */
	Idee getIdeeByIdentifiantAndCampagne( int nKey, String strCampagne);

	/**
     * Load the data of all the idee objects searched and returns them in form of a collection
     * @param ideeSearcher an IdeeSearcher
     * @return the collection which contains the data of all the idee objects
     */
	public Collection<Idee> getIdeesListSearch(IdeeSearcher ideeSearcher);
	
	/**
     * Update of the idee which is specified in parameter
     * @param idee The instance of the Idee which contains the data to store
     * @return The instance of the  idee which has been updated
     */
     public Idee updateIdee( Idee idee );

     public void updateIdee(Idee ideeLutece, Idee ideeEudonet, boolean notify, HttpServletRequest request);

     public void updateIdee(Idee ideeLutece, Idee ideeEudonet, HttpServletRequest request);

    /**
     * Insert comment in module extend comment
     * @param idee
     */
    public void createComment( Idee idee );
    /**
     * process workflow action by name
     * @param strWorkflowIdeeActionName the name of the action to process
     * @param nIdIdee the id of the idee
     * request the HttpServletRequest request
     */
    void processActionByName(String strWorkflowIdeeActionName, int nIdIdee);
    
    /**
     * process workflow action by name
     * @param strWorkflowIdeeActionName the name of the action to process
     * @param nIdIdee the id of the idee
     * request the HttpServletRequest request
     */
    void processActionByName(String strWorkflowIdeeActionName, int nIdIdee, HttpServletRequest request);
    
	
	


}
