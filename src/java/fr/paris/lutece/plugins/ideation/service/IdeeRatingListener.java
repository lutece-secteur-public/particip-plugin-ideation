package fr.paris.lutece.plugins.ideation.service;

import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingListener;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class IdeeRatingListener implements IRatingListener
{
	private static SolrIdeeIndexer _solrIdeeIndexer  = SpringContextService.getBean( "ideation.solrIdeeIndexer" );
	
	@Override
	public void createVote( String strIdExtendableResource ) 
	{
		updateIdeeIndexer ( strIdExtendableResource ) ;
	}

	@Override
	public void cancelVote(LuteceUser user, String strIdExtendableResource, String strExtendableResourceType) 
	{
		updateIdeeIndexer ( strIdExtendableResource ) ;
	}
	
	private void updateIdeeIndexer (String strIdExtendableResource )
	{
		_solrIdeeIndexer.removeIdee( IdeeHome.findByPrimaryKey( Integer.parseInt( strIdExtendableResource ) ) );
		_solrIdeeIndexer.writeIdee(  IdeeHome.findByPrimaryKey( Integer.parseInt( strIdExtendableResource ) ) );
	}

    @Override
    public boolean canVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
        return true;
    }

}
