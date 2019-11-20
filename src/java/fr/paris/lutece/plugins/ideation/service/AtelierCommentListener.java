package fr.paris.lutece.plugins.ideation.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;

import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentListener;
import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.profanityfilter.utils.ProfanityResult;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class AtelierCommentListener implements ICommentListener
{

    private static final String MESSAGE_COMMENT_CLOSED = "ideation.message.comment.closed";
    private static final String MESSAGE_VALIDATION_COMMENT = "ideation.validation.idee.comment.profanity";

   /**
    * {@inheritDoc}
    */
    @Override
    public void createComment( String strIdExtendableResource, boolean bPublished )
    {
     	
    	String strActionCreateAtelierComment=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT_ATELIER);
    	AtelierWSService.getInstance().processActionByName(strActionCreateAtelierComment, Integer.parseInt(strIdExtendableResource));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createComment( String strIdExtendableResource, boolean bPublished, HttpServletRequest request )
    {
        	
    	
    	String strActionCreateAtelierComment=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT_ATELIER);
    	AtelierWSService.getInstance().processActionByName(strActionCreateAtelierComment, Integer.parseInt(strIdExtendableResource),request);
    	

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishComment( String strIdExtendableResource, boolean bPublished )
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteComment( String strIdExtendableResource, List<Integer> listIdRemovedComment )
    {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String checkComment( String comment, String uidUser )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String checkComment( String comment, String uidUser, String strResourceType, String strResourceId )
    {
        StringBuilder sbError = new StringBuilder( );
        int nId_Atelier = Integer.parseInt( strResourceId );
        Atelier atelier = AtelierHome.findByPrimaryKey( nId_Atelier );
        Date date = new Date(  );

        date = DateUtils.truncate( date, Calendar.DATE );
        if ( atelier != null && ( date.before( atelier.getDateDebutPhase1( ) ) || date.after( atelier.getDateFinPhase1( ) ) ) )
        {
            sbError.append( I18nService.getLocalizedString( MESSAGE_COMMENT_CLOSED,
                    new Locale( "fr", "FR" ) ) );
            sbError.append( ", " );
        }
        else
        {
            ProfanityResult _pfResult = IdeationProfanityFilter.getInstance( ).scanString( comment, "COMMENT",
                    uidUser );
            Set<String> words = _pfResult.getSwearWords( );
            if ( words != null && !words.isEmpty( ) )
            {
                sbError.append(
                        I18nService.getLocalizedString( MESSAGE_VALIDATION_COMMENT, new Locale( "fr", "FR" ) ) );
            }

            for ( String wrd : words )
            {
                sbError.append( wrd );
                sbError.append( ", " );
            }
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
        int nIdAtelier = Integer.parseInt( strIdExtendableResource );
        Atelier atelier = AtelierHome.findByPrimaryKey( nIdAtelier );
        Date date = new Date( );

        date = DateUtils.truncate( date, Calendar.DATE );
        if ( atelier != null
                && ( date.before( atelier.getDateDebutPhase1( ) ) || date.after( atelier.getDateFinPhase1( ) ) ) )
        {
            return false;
        }

        return true;
    }
}

