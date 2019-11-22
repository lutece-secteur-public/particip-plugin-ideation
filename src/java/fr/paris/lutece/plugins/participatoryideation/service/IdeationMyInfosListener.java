package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatorybudget.service.IMyInfosListener;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeSearcher;
import fr.paris.lutece.plugins.participatoryideation.service.subscription.IdeationSubscriptionProviderService;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.business.CommentFilter;
import fr.paris.lutece.plugins.extend.modules.comment.business.ICommentDAO;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentPlugin;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class IdeationMyInfosListener implements IMyInfosListener {

	private static final String BEAN_SOLR_IDEE_INDEXER="ideation.solrIdeeIndexer";
	private static final String BEAN_COMMENT_DAO="extend-comment.commentDAO";
	
	private ICommentService _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );
	private SolrIdeeIndexer _solrIdeeIndexer= SpringContextService.getBean( BEAN_SOLR_IDEE_INDEXER );
	 private ICommentDAO _commentDAO= SpringContextService.getBean( BEAN_COMMENT_DAO);

	@Override
	public void updateNickName(String strLuteceUserName, String strNickName) {
		
    //update comments
	CommentFilter _commentFilter= new CommentFilter();
    _commentFilter.setLuteceUserName(strLuteceUserName);
    	
    List<Comment> listComments=	_commentService.findByResource("*",Idee.PROPERTY_RESOURCE_TYPE, _commentFilter, 0, 10000, false);
    
    if(listComments!=null)
    {
    	
	    for(Comment comment:listComments)
	    {
	    	Comment commentPrimary=_commentService.findByPrimaryKey(comment.getIdComment());
	    	commentPrimary.setName(strNickName);
	        _commentDAO.store( commentPrimary, CommentPlugin.getPlugin( ) );
	    }
		
    }
    //reindex all user idees
    
    IdeeSearcher _ideeSearcher= new IdeeSearcher();
	_ideeSearcher.setLuteceUserName(strLuteceUserName);
	_ideeSearcher.setIsPublished(true);
	
	Collection<Idee> ideesSubmitted=IdeeHome.getIdeesListSearch(_ideeSearcher);
	 for(Idee idee:ideesSubmitted)
    {
    	
    	_solrIdeeIndexer.writeIdee( idee );
    	
    }
   }

	@Override
	public void createNickName(String strLuteceUserName, String strNickName) {
		
		ReferenceList refList= IdeationSubscriptionProviderService.getService().getRefListIdeationSubscription(Locale.FRENCH);
		
		for (ReferenceItem refItem:refList)
		{
			IdeationSubscriptionProviderService.getService().createSubscription(strLuteceUserName,refItem.getCode());
				
		}
	}

	@Override
	public int canChangeArrond(LuteceUser user) {
	  //return default value
	    return 0;
	}

	@Override
	public String deleteVotes(HttpServletRequest request) {

	    return null;
	}


	

}
