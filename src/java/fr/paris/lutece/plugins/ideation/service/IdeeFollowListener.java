package fr.paris.lutece.plugins.ideation.service;

import java.util.List;


import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentListener;
import fr.paris.lutece.plugins.extend.modules.follow.service.IFollowListener;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.profanityfilter.utils.ProfanityResult;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


public class IdeeFollowListener implements IFollowListener{

	 /**
     * {@inheritDoc}
     */
	private static final String MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT = "ideation.messages.campagne.ideation.closed.comment";
	private static final String MESSAGE_VALIDATION_COMMENT = "ideation.validation.idee.comment.profanity";

	
	@Override
	public void follow(String strExtendableResourceType,
			String strIdExtendableResource, HttpServletRequest request) {
		
		
	 Idee idee= IdeeHome.findByPrimaryKey(Integer.parseInt(strIdExtendableResource));
   	 String strWorkflowActionNameFollow=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_FOLLOW);
   	 IdeeWSService.getInstance().processActionByName(strWorkflowActionNameFollow, Integer.parseInt(strIdExtendableResource), request );
   
		
	}

	@Override
	public void cancelFollow(String strExtendableResourceType,
			String strIdExtendableResource, HttpServletRequest request) {
		Idee idee= IdeeHome.findByPrimaryKey(Integer.parseInt(strIdExtendableResource));
	   	String strWorkflowActionNameFollow=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CANCEL_FOLLOW);
	   	IdeeWSService.getInstance().processActionByName(strWorkflowActionNameFollow, Integer.parseInt(strIdExtendableResource), request );
	 }

	@Override
	public boolean canFollow( String strExtendableResourceType, String strIdExtendableResource, LuteceUser user )
	{
		return true;
	}
	
   

	

  
	
}
