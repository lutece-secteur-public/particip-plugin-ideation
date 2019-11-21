package fr.paris.lutece.plugins.ideation.service;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.follow.service.IFollowListener;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class IdeeFollowListener implements IFollowListener {

	@Override
	public void follow(String strExtendableResourceType, String strIdExtendableResource, HttpServletRequest request) {
		String strWorkflowActionNameFollow = AppPropertiesService
				.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_FOLLOW);
		IdeeWSService.getInstance().processActionByName(strWorkflowActionNameFollow,
				Integer.parseInt(strIdExtendableResource), request);

	}

	@Override
	public void cancelFollow(String strExtendableResourceType, String strIdExtendableResource,
			HttpServletRequest request) {
		String strWorkflowActionNameFollow = AppPropertiesService
				.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CANCEL_FOLLOW);
		IdeeWSService.getInstance().processActionByName(strWorkflowActionNameFollow,
				Integer.parseInt(strIdExtendableResource), request);
	}

	@Override
	public boolean canFollow(String strExtendableResourceType, String strIdExtendableResource, LuteceUser user) {
		return true;
	}

}
