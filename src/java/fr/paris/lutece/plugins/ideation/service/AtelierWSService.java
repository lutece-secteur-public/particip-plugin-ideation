package fr.paris.lutece.plugins.ideation.service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;

public class AtelierWSService implements IAtelierWSService {
	
	private static final String BEAN_ATELIER_WEB_SERVICE_SERVICE="ideation.atelierWSService";
	private static IAtelierWSService _singleton;
	
	/**
     * Get the unique instance of the Idee Web Service Service
     * @return The instance
     */
	public static IAtelierWSService getInstance(  )
    {
		if(_singleton == null){
			_singleton= SpringContextService.getBean( BEAN_ATELIER_WEB_SERVICE_SERVICE );
			
		}
		return _singleton;
		 
    }
  

	
	
	@Override
	public void processActionByName(String strWorkflowIdeeActionName, int nIdAtelier, HttpServletRequest request )
	{
		
		 
        
		int nIdWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID_ATELIER, -1 );
		 
        if ( nIdWorkflow != -1 && WorkflowService.getInstance(  ).isAvailable() && !StringUtils.isEmpty(strWorkflowIdeeActionName))
         {
            
        	//Initialize the workflow, this creates the state for our resource
            WorkflowService.getInstance(  ).getState( nIdAtelier,  Atelier.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, -1 );
         	List<Action> actionsList = WorkflowService.getInstance(  ).getMassActions(nIdWorkflow);
             
            for(Action action: actionsList){
            	 
            	 if(action.getName( ).equals(strWorkflowIdeeActionName)){
            		 
            		 WorkflowService.getInstance(  ).doProcessAction(nIdAtelier, Atelier.WORKFLOW_RESOURCE_TYPE, action.getId(), -1, request, new Locale( "fr" , "FR" ), true);
         			
            	 }
             }
	     }
		
	}

	@Override
	public void processActionByName(String strWorkflowIdeeActionName, int nIdAtelier )
	{
		
		
		int nIdWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID_ATELIER, -1 );
	    	 
        if ( nIdWorkflow != -1 && WorkflowService.getInstance(  ).isAvailable() && !StringUtils.isEmpty(strWorkflowIdeeActionName))
         {
        	//Initialize the workflow, this creates the state for our resource
            WorkflowService.getInstance(  ).getState( nIdAtelier,  Atelier.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, -1 );
         	List<Action> actionsList = WorkflowService.getInstance(  ).getMassActions(nIdWorkflow);
             
            for(Action action: actionsList){
            	 
            	 if(action.getName( ).equals(strWorkflowIdeeActionName)){
            		 
            		 WorkflowService.getInstance(  ).doProcessAction(nIdAtelier, Atelier.WORKFLOW_RESOURCE_TYPE, action.getId(), -1, null, new Locale( "fr" , "FR" ), true);
         			
            	 }
             }
	     }
		
	}
	public static String removeAccent( String source )
    {
        return Normalizer.normalize( source, Normalizer.Form.NFD ).replaceAll( "[^\\p{ASCII}]", "" );
    }
	
	
	public State getAtelierState( int nIdAtelier )
	{
		
		
		int nIdWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID_ATELIER, -1 );
		   	 
        if ( nIdWorkflow != -1 && WorkflowService.getInstance(  ).isAvailable() )
         {
        	//Initialize the workflow, this creates the state for our resource
        	return    WorkflowService.getInstance(  ).getState( nIdAtelier,  Atelier.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, -1 );
         	
	     }
        return null;
		
	}

	
}
