package fr.paris.lutece.plugins.ideation.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.extender.CommentResourceExtender;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.extend.service.extender.history.ResourceExtenderHistoryService;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.utils.CsvUtils;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.spring.SpringContextService;


public class IdeeUsersService implements IIdeeUsersService {

    private static ICommentService _commentService;
    private static IResourceExtenderHistoryService _resourceHistoryService;
    public static final String EXTENDER_TYPE_CSV = "extender_type";
    public static final String EXTENDER_VALUE_DEPOSITAIRE_CSV = "depositaire";
    /**
     * Get the comment service
     * @return the comment service
     */
    public static ICommentService getCommentService(  )
    {
        if ( _commentService == null )
        {
            _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
        }
        return _commentService;
    }
    /**
     * Get the resource extender history service
     * @return the resource extender history service
     */
    private static IResourceExtenderHistoryService getResourceExtenderHistoryService( )
    {
        if ( _resourceHistoryService == null )
        {
            _resourceHistoryService = SpringContextService.getBean( ResourceExtenderHistoryService.BEAN_SERVICE );
        }
        return _resourceHistoryService;
    }
    /**
     * 
     * @param nId_Idee
     * @return
     */
    private static List<String> getCommentators( int nId_Idee )
    {
        List<String> commentatorsList = new ArrayList<String>(  );
        
        ICommentService commentService = getCommentService(  );
        String strId = String.valueOf( nId_Idee );
        
        List<Comment> commentsList = commentService.findByResource( strId, Idee.PROPERTY_RESOURCE_TYPE, false, false );
        
        for ( Comment comment : commentsList )
        {
            if ( !commentatorsList.contains( comment.getLuteceUserName(  ) ) )
            {
                commentatorsList.add( comment.getLuteceUserName(  ) );
            }            
        }
        
        return commentatorsList;
    }
    /**
     * 
     * @param nId_Idee
     * @return
     */
    public static List<String> getFollowers( int nId_Idee )
    {
        List<String> followersList = new ArrayList<String>(  );
        String strId = String.valueOf( nId_Idee );
        ResourceExtenderHistoryFilter resourceExtenderHistoryFilter = new ResourceExtenderHistoryFilter(  );
        
        resourceExtenderHistoryFilter.setIdExtendableResource( strId );
        resourceExtenderHistoryFilter.setExtendableResourceType( Idee.PROPERTY_RESOURCE_TYPE );
        resourceExtenderHistoryFilter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
        
        List<ResourceExtenderHistory> histories = getResourceExtenderHistoryService().findByFilter( resourceExtenderHistoryFilter );
        
        if ( CollectionUtils.isNotEmpty( histories ) )
        {
            for ( ResourceExtenderHistory history : histories )
            {
                if ( !followersList.contains( history.getUserGuid(  ) ) )
                {
                    followersList.add( history.getUserGuid(  ) );
                }
            }
        }
        return followersList;
    }
    /**
     * 
     * @param infosList
     * @param usersList
     * @param prefKeysList
     * @param extenderType
     */
    public static void getValuesFromUsers( List<ArrayList<String>> infosList,
            							   Idee idee,
                                           List<String> usersList, 
                                           List<String> prefKeysList,
                                           String extenderType )
    {
        Set<String> usersNoDuplicatesList = new HashSet<>( usersList );
        int nKeys = prefKeysList.size();
        
        for ( String strUserId : usersNoDuplicatesList )
        {
            ArrayList<String> valuesList = new ArrayList<String>(  );
            
            for ( int i = 0; i < nKeys; i++ )
            {
                String columnName = prefKeysList.get( i );
                String value;
                if ( columnName.equals( EXTENDER_TYPE_CSV ) )
                {
                    value = extenderType;
                }
                else if ( columnName.equals( CsvUtils.PARAMETER_ID_USER_FIELD_CSV ) )
                {
                    value = strUserId;
                }
                else
                {
                    value = UserPreferencesService.instance(  ).get( strUserId, columnName, "");
                }
                
                valuesList.add( value );
            }
            valuesList.add( idee.getReference() );
            
            infosList.add( valuesList );
        }
    }
    
    /**
     * Returns user infos and idee codes
     * @param nId_Idee
     * @return
     */
    private static List<ArrayList<String>> getExportInfosList( List<Integer> subIdeesIds, String userPrefixId )
    {
        List<ArrayList<String>> infosList = new ArrayList<ArrayList<String>>(  );

        ArrayList<String> prefKeysList = (ArrayList<String>) CsvUtils.getPrefKeys( userPrefixId );

        for (int ideeId : subIdeesIds)
        {
	        Idee idee = IdeeHome.findByPrimaryKey( ideeId );
	        
            List<String> usersList = new ArrayList<String>(  );
            
	        if ( idee != null )
	        {
	            usersList.add( idee.getLuteceUserName(  ) );
	        }
	        getValuesFromUsers( infosList, idee, usersList, prefKeysList, EXTENDER_VALUE_DEPOSITAIRE_CSV );
	        
	        usersList = getCommentators( ideeId );
	        getValuesFromUsers( infosList, idee, usersList, prefKeysList, CommentResourceExtender.EXTENDER_TYPE_COMMENT );
	        
	        usersList = getFollowers( ideeId );
	        getValuesFromUsers( infosList, idee, usersList, prefKeysList, FollowResourceExtender.RESOURCE_EXTENDER );
        }
        
        return infosList;
    }
    
    /**
     * Returns user infos and idee codes
     * @param nId_Idee
     * @return
     */
    public static List<ArrayList<String>> getExportInfosList( List<Integer> subIdeesIds )
    {
    	return getExportInfosList( subIdeesIds, CsvUtils.IDEEUSERS_PREFIX_CSV );
    }
    
    /**
     * Retrieve the users of an 'atelier'
     * @param nId_Idee
     * @return
     */
    public static List<ArrayList<String>> getExportAtelierUsersList( List<Integer> ideesIdList )
    {
    	return getExportInfosList( ideesIdList, CsvUtils.ATELIERUSERS_PREFIX_CSV );
    }
    
    /**
     * Check if a user is a follower of an idee
     * @param strId_Idee
     * @param userId
     * @return true when the user is a follower of the idee
     */
    public static boolean isFollower( int nId_Idee, String userId )
    {
        ResourceExtenderHistoryFilter resourceExtenderHistoryFilter = new ResourceExtenderHistoryFilter(  );
        String strId = String.valueOf( nId_Idee );
        resourceExtenderHistoryFilter.setIdExtendableResource( strId );
        resourceExtenderHistoryFilter.setExtendableResourceType( Idee.PROPERTY_RESOURCE_TYPE );
        resourceExtenderHistoryFilter.setExtenderType( FollowResourceExtender.RESOURCE_EXTENDER );
        
        List<ResourceExtenderHistory> histories = getResourceExtenderHistoryService().findByFilter( resourceExtenderHistoryFilter );

        for ( ResourceExtenderHistory history : histories )
        {
            if ( userId.equals( history.getUserGuid(  ) ) )
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a user has submit the idee
     * @param nId_Idee
     * @param userId
     * @return
     */
    public static boolean isDepositaire( int nId_Idee, String userId )
    {
        Idee idee = IdeeHome.findByPrimaryKey( nId_Idee );
        
        return ( idee != null && userId.equals( idee.getLuteceUserName(  ) ) );
    }
}
