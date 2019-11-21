package fr.paris.lutece.plugins.ideation.service;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentListener;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


public class IdeeCommentListener implements ICommentListener{

	 /**
     * {@inheritDoc}
     */
	private static final String MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT = "ideation.messages.campagne.ideation.closed.comment";
	private static final String PROPERTY_ACTIVATION_COMMENTAIRES= "ideation.site_property.form.forcer_activation_commentaires" ;

    @Override
	public void createComment(String strIdExtendableResource, boolean bPublished) {
	
    	
    	 Idee idee= IdeeHome.findByPrimaryKey(Integer.parseInt(strIdExtendableResource));
    	 if(idee.getExportedTag( ) != 0){
    		 idee.setExportedTag(2);
    		 IdeeHome.updateBO(idee);
    	 }
    	// String strWorkflowActionNameCreateComment=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT);
    	// IdeeWSService.getInstance().processActionByName(strWorkflowActionNameCreateComment, Integer.parseInt(strIdExtendableResource) );
    	 
    	
    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void createComment(String strIdExtendableResource, boolean bPublished, HttpServletRequest request) {
	
    	
    	 Idee idee= IdeeHome.findByPrimaryKey(Integer.parseInt(strIdExtendableResource));
    	 
    	 if(idee.getExportedTag( ) != 0){
    		 idee.setExportedTag(2);
    		 IdeeHome.updateBO(idee);
    	 }
    	 
    	 String strWorkflowActionNameCreateComment=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT);
    	 IdeeWSService.getInstance().processActionByName(strWorkflowActionNameCreateComment, Integer.parseInt(strIdExtendableResource), request );
    	 
    	
    
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void publishComment(String strIdExtendableResource,
			boolean bPublished) {
	
    	
	}
    /**
     * {@inheritDoc}
     */
	@Override
	public String checkComment(String comment, String uidUser) {

		
		StringBuilder sbError = new StringBuilder( );
		String strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ACTIVATION_COMMENTAIRES, "0" );
		
		if ( !CampagnesService.getInstance().isDuring("IDEATION")  && strDataStoreValue.equals("0") ) 
		{			
			sbError.append( I18nService.getLocalizedString( MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT, new Locale("fr","FR") ) );
			sbError.append(", ");
		}
		else
		{
			// Should check here.
		}
		//remove last ,
    	if(sbError.length()!=0)
    	{
    		sbError.setLength( sbError.length(  ) - 2 );
    	}
		return sbError.toString( );
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void deleteComment(String arg0, List arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public String checkComment( String comment, String uidUser, String strResourceType, String strResourceId )
    {
        StringBuilder sbError = new StringBuilder( );
        int nId_Idee = Integer.parseInt( strResourceId );
        Idee idee = IdeeHome.findByPrimaryKey( nId_Idee );
        String strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ACTIVATION_COMMENTAIRES, "0");
        
        if ( idee != null && !CampagnesService.getInstance().isDuring(idee.getCodeCampagne(), "IDEATION") && strDataStoreValue.equals("0") )
        {
            sbError.append( I18nService.getLocalizedString( MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT, new Locale( "fr", "FR" ) ) );
            sbError.append( ", " );
        }
        else
        {
			// Should check here.
        }
        // remove last ,
        if ( sbError.length( ) != 0 )
        {
            sbError.setLength( sbError.length( ) - 2 );
        }
        return sbError.toString( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canComment( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
        int nIdIdee = Integer.parseInt( strIdExtendableResource );
        Idee idee = IdeeHome.findByPrimaryKey( nIdIdee );
        String strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ACTIVATION_COMMENTAIRES, "0");
        
        if ( idee != null && !CampagnesService.getInstance().isDuring(idee.getCodeCampagne(), "IDEATION") && strDataStoreValue.equals("0") )
        {
            return false;
        }

        return true;
    }
}
