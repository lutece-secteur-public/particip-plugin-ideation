package fr.paris.lutece.plugins.participatoryideation.service;

import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeSearcher;
import fr.paris.lutece.plugins.participatoryideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.workflowcore.business.action.Action;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.plugins.workflowcore.service.workflow.IWorkflowService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.http.SecurityUtil;

public class IdeeWSService implements IIdeeWSService {
	
	private static final String BEAN_IDEE_WEB_SERVICE_SERVICE="ideation.ideeWSService";
	private static IIdeeWSService _singleton;
	private static SolrIdeeIndexer _solrIdeeIndexer;
	private static final String BEAN_SOLR_IDEE_INDEXER="ideation.solrIdeeIndexer";
	private static ICommentService _commentService;
	private static IResourceExtenderHistoryService _resourceHistoryService;
	
	// properties
    private static final String PROPERTY_POLITENESS_COMMENTS_PROJECTS_NOT_SELECTED="eudonetbp.site_property.mail.politeness_comments_projects_not_selected.htmlblock";
    // Mark
    private static final String MARK_COMMENT="comment";
	
	/**
     * Get the unique instance of the Idee Web Service Service
     * @return The instance
     */
	public static IIdeeWSService getInstance(  )
    {
		if(_singleton == null){
			_solrIdeeIndexer= SpringContextService.getBean( BEAN_SOLR_IDEE_INDEXER );
			_singleton= SpringContextService.getBean( BEAN_IDEE_WEB_SERVICE_SERVICE );
			_commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
			_resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
			
		}
		return _singleton;
		 
    }
      
	@Override
	public Collection<Idee> getIdeesList() {
		
		return IdeeHome.getIdeesList();
		
	}
	
	@Override
	public Idee getIdeeByIdentifiant( int nKey) {
		
		return IdeeHome.findByPrimaryKey(nKey);
		
	}
	
	@Override
	public Idee getIdeeByIdentifiantAndCampagne( int nKey, String strCampagne) {
		
		return IdeeHome.findByCodes(strCampagne, nKey);
		
	}
	
	@Override
	public Collection<Idee> getIdeesListSearch(IdeeSearcher ideeSearcher) {
		
		return IdeeHome.getIdeesListSearch(ideeSearcher);
	}

	@Override
	public void updateIdee(Idee ideeLutece, Idee ideeEudonet, HttpServletRequest request) {
		updateIdee(ideeLutece, ideeEudonet, true, request);
	}

	@Override
	public void updateIdee(Idee ideeLutece, Idee ideeEudonet, boolean notify, HttpServletRequest request) {
		
		String valStatusLutece        = ideeLutece.getStatusPublic().getValeur();     // statut PUBLIC  sur LUTECE
		String valStatusEudonet       = ideeEudonet.getStatusEudonet( ).getValeur( ); // statut EUDONET sur LUTECE (si non valué, sera value au statut PUBLIC sur LUTECE)
		String valStatusEudonetLutece = valStatusLutece;                              // statut PUBLIC  sur EUDONET
		
		if(ideeLutece.getStatusEudonet( ) != null){
			valStatusEudonetLutece= ideeLutece.getStatusEudonet( ).getValeur( );
		}
		
		if (         valStatusLutece.equals(valStatusEudonet)
				|| (!valStatusLutece.equals(valStatusEudonet) && !valStatusLutece.equals(valStatusEudonetLutece) && !valStatusEudonet.equals(valStatusEudonetLutece))
				|| ( valStatusLutece.equals(Idee.Status.STATUS_SUPPRIME_PAR_USAGER.getValeur()))
				|| (valStatusEudonet.equals(Idee.Status.STATUS_SUPPRIME_PAR_USAGER.getValeur())))
		{
			// On ne fait rien
		}
		else
		{
		    IdeeHome.updateBO(ideeEudonet);
		    if (ideeEudonet.getMotifRecev( )!= null && StringUtils.isNotEmpty(ideeEudonet.getMotifRecev( )) && StringUtils.isNotBlank(ideeEudonet.getMotifRecev( )) 
		    		&& !( ideeLutece.getMotifRecev( )!= null && ideeEudonet.getMotifRecev( )!= null && ideeEudonet.getMotifRecev( ).equals(ideeLutece.getMotifRecev( )))){
		    	createComment(  ideeEudonet );
		    }
		    if (ideeEudonet.getStatusPublic().equals(Idee.Status.STATUS_SUPPRIME_PAR_MDP) || ideeEudonet.getStatusPublic().equals(Idee.Status.STATUS_SUPPRIME_PAR_USAGER)){
		    	_solrIdeeIndexer.removeIdee(ideeEudonet);
		    }
	        if ( WorkflowService.getInstance(  ).isAvailable(  ) )
	        {
	        	processAction( valStatusEudonet, ideeLutece, notify, request );
	        }	
		}
	}

    
	@Override
	public Idee updateIdee(Idee idee) {

		idee= IdeeHome.updateBO(idee);
		return idee;
	}
    
	@Override
	public void createComment( Idee idee ) 
	{
		String strCommentPNS = DatastoreService.getDataValue(PROPERTY_POLITENESS_COMMENTS_PROJECTS_NOT_SELECTED, "");
		String strContentCommentPNS = idee.getMotifRecev();
		Map<String, String> model = new HashMap<String, String> ();
    	model.put( MARK_COMMENT, idee.getMotifRecev() );
    	try
    	{
    		HtmlTemplate t = AppTemplateService.getTemplateFromStringFtl( strCommentPNS , new Locale( "fr", "FR" ), model);
        	strContentCommentPNS = t.getHtml(  );
    	}
    	catch ( Exception e )
        {
            // 	_service.addToLog( "Erreur updateIdee: "+e.getMessage( ));
            AppLogService.error( "Erreur avec le template freemarker dans les proprietes du site: ",e );
        }
    	
		Comment comment = new Comment( );
        comment.setIdExtendableResource( "" + idee.getId() );
        comment.setExtendableResourceType( Idee.PROPERTY_RESOURCE_TYPE );
        comment.setIdParentComment( 0 );
        comment.setComment( strContentCommentPNS );
        Timestamp currentDate = new Timestamp( new Date( ).getTime( ) );
        comment.setDateComment( currentDate );
        comment.setDateLastModif( currentDate );
        comment.setName( "Mairie de Paris" );
        comment.setEmail( "lutece@lutece.com" );
        comment.setPublished( true );
        comment.setIpAddress( "" );
        comment.setIsAdminComment( true );
        comment.setIsImportant(true);
        //comment.setCommentOrder(1);
        comment.setPinned(true);
        _commentService.create( comment );

        ResourceExtenderHistory history = new ResourceExtenderHistory(  );
        history.setExtenderType( CommentResourceExtender.EXTENDER_TYPE_COMMENT );
        //history.setIdExtendableResource( idee.getCodeCampagne() + "-" + String.format("%06d", idee.getCodeIdee()) );
        history.setIdExtendableResource( "" + idee.getId() );
        history.setExtendableResourceType( Idee.PROPERTY_RESOURCE_TYPE );
        history.setIpAddress( StringUtils.EMPTY );
        history.setUserGuid( AppPropertiesService.getProperty( IdeationConstants.PROPERTY_GENERATE_IDEE_LUTECE_USER_NAME )  ); // Le commentaire est déposé par l'équipe du Budget Participatif.
        _resourceHistoryService.create( history );
	}

	private void processAction (String ideeStatut, Idee idee, boolean notify, HttpServletRequest request) {
		
		boolean       foundAction = false;
		int           nIdWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
    	String  ideeStatutLibelle = removeAccent(I18nService.getLocalizedString((Idee.Status.getByValue(ideeStatut).getLibelle( )), new Locale("fr","FR"))) + ( notify ? " (avec notification)" : " (sans notification)" ) ;

        if ( nIdWorkflow != -1 )
        {
        	List<Action> actionsList = WorkflowService.getInstance(  ).getMassActions(nIdWorkflow);
            for (Action action: actionsList)
            {
            	String     actionLibelle = removeAccent( action.getName() );
            	if ( actionLibelle.equals( ideeStatutLibelle ) ) 
            	{
            		 foundAction = true;
            		 IWorkflowService  _service = SpringContextService.getBean( fr.paris.lutece.plugins.workflowcore.service.workflow.WorkflowService.BEAN_SERVICE );
            		 _service.doProcessAction(idee.getId( ), Idee.WORKFLOW_RESOURCE_TYPE,  action.getId( ), -1, request, new Locale( "fr" , "FR" ), false, null);
//					 WorkflowService.getInstance(  ).doProcessAction(idee.getId( ), Idee.WORKFLOW_RESOURCE_TYPE, action.getId( ), -1 , null, new Locale( "fr" , "FR" ), false);
            	}
            }
            
            if (!foundAction) {
            	AppLogService.error("No such action on workflow #" + nIdWorkflow + " : '" + ideeStatutLibelle + "'");
            }
            
			/*State state= WorkflowService.getInstance(  ).getState( idee.getId(  ), Idee.WORKFLOW_RESOURCE_TYPE, nIdWorkflow, -1 );
			if(state != null){
				WorkflowService.getInstance(  ).doProcessAutomaticReflexiveActions(idee.getId(  ), Idee.WORKFLOW_RESOURCE_TYPE, state.getId( ), -1, new Locale( "fr" , "FR" ));
			}*/
        }
	}
	
	
	@Override
	public void processActionByName(String strWorkflowIdeeActionName, int nIdIdee, HttpServletRequest request )
	{
		
		
		int nIdWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
    	 
        if ( nIdWorkflow != -1 && WorkflowService.getInstance(  ).isAvailable() && !StringUtils.isEmpty(strWorkflowIdeeActionName))
         {
             
         	List<Action> actionsList = WorkflowService.getInstance(  ).getMassActions(nIdWorkflow);
             
            for(Action action: actionsList){
            	 
            	 if(action.getName( ).equals(strWorkflowIdeeActionName)){
            		 
            		 WorkflowService.getInstance(  ).doProcessAction(nIdIdee, Idee.WORKFLOW_RESOURCE_TYPE, action.getId(), -1, request, new Locale( "fr" , "FR" ), true);
         			
            	 }
             }
	     }
		
	}

	@Override
	public void processActionByName(String strWorkflowIdeeActionName, int nIdIdee )
	{
		
		
		int nIdWorkflow = AppPropertiesService.getPropertyInt( IdeationConstants.PROPERTY_WORKFLOW_ID, -1 );
    	 
        if ( nIdWorkflow != -1 && WorkflowService.getInstance(  ).isAvailable() && !StringUtils.isEmpty(strWorkflowIdeeActionName))
         {
             
         	List<Action> actionsList = WorkflowService.getInstance(  ).getMassActions(nIdWorkflow);
             
            for(Action action: actionsList){
            	 
            	 if(action.getName( ).equals(strWorkflowIdeeActionName)){
            		 
            		 WorkflowService.getInstance(  ).doProcessAction(nIdIdee, Idee.WORKFLOW_RESOURCE_TYPE, action.getId(), -1, null, new Locale( "fr" , "FR" ), true);
         			
            	 }
             }
	     }
		
	}
	public static String removeAccent( String source )
    {
        return Normalizer.normalize( source, Normalizer.Form.NFD ).replaceAll( "[^\\p{ASCII}]", "" );
    }

	
}
