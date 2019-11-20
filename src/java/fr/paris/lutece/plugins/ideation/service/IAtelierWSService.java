package fr.paris.lutece.plugins.ideation.service;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.workflowcore.business.state.State;

/**
 * 
 *IAtelierWSService
 *
 */
public interface IAtelierWSService {
	  
	
    /**
     * process workflow action by name
     * @param strWorkflowIdeeActionName the name of the action to process
     * @param nIdAtelier the id of the Atelier
     * request the HttpServletRequest request
     */
    void processActionByName(String strWorkflowIdeeActionName, int nIdAtelier);
    
    /**
     * process workflow action by name
     * @param strWorkflowIdeeActionName the name of the action to process
     * @param nIdAtelier the id of the Atelier
     * request the HttpServletRequest request
     */
    void processActionByName(String strWorkflowIdeeActionName, int nIdAtelier, HttpServletRequest request);
    /**
     * return the Atelier State
     * @param nIdAtelier int nIdAtlelier
     * @return the Atelier State
     */
	
    public State getAtelierState( int nIdAtelier );


}
