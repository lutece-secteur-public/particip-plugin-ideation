package fr.paris.lutece.plugins.ideation.service;

import java.util.Date;

import fr.paris.lutece.plugins.campagnebp.service.CampagnesService;
import fr.paris.lutece.plugins.campagnebp.utils.Constants;
import fr.paris.lutece.plugins.extend.modules.comment.business.Comment;
import fr.paris.lutece.plugins.extend.modules.comment.service.CommentService;
import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentService;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingListener;
import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class ExtendableCommentRatingListener implements IRatingListener
{

    private static ICommentService _commentService = SpringContextService.getBean( CommentService.BEAN_SERVICE );

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelVote( LuteceUser arg0, String arg1, String arg2 )
    {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createVote( String arg0 )
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canVote( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
        int nIdComment;

        try
        {
            nIdComment = Integer.parseInt( strIdExtendableResource );

            Comment comment = _commentService.findByPrimaryKey( nIdComment );
            String strCommentResourceType = comment.getExtendableResourceType( );

            if ( strCommentResourceType.equals( Idee.PROPERTY_RESOURCE_TYPE ) )
            {
                Idee idee = IdeeHome.findByPrimaryKey( Integer.parseInt( comment.getIdExtendableResource( ) ) );

                // Can not rate an idee if not during its ideation campagne
                if ( idee == null || !CampagnesService.getInstance().isDuring( idee.getCodeCampagne( ), Constants.IDEATION ) )
                {
                    return false;
                }
            }

            if ( strCommentResourceType.equals( Atelier.PROPERTY_RESOURCE_TYPE ) )
            {
                Date date = new Date( );
                Atelier atelier = AtelierHome
                        .findByPrimaryKey( Integer.parseInt( comment.getIdExtendableResource( ) ) );

                if ( atelier == null || date.before( atelier.getDateDebutPhase1( ) )
                        || date.after( atelier.getDateFinPhase1( ) ) )
                {
                    return false;
                }
            }
        }
        catch ( NumberFormatException e )
        {
            return false;
        }
        return true;
    }
}
