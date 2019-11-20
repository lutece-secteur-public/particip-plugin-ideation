package fr.paris.lutece.plugins.ideation.service;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.resource.IExtendableResource;
import fr.paris.lutece.portal.service.resource.IExtendableResourceService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.url.UrlItem;

public class AtelierExtendableResourceService implements IExtendableResourceService
{
    private static final String MESSAGE_ATELIER_RESOURCE_TYPE_DESCRIPTION = "ideation.resource.atelier.resourceTypeDescription";
    private static final String PARAMETER_ATELIER_ID = "id";
   

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInvoked( String strResourceType )
    {
        return Atelier.PROPERTY_RESOURCE_TYPE.equals( strResourceType );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IExtendableResource getResource( String strIdResource, String strResourceType )
    {
        if ( StringUtils.isNotBlank( strIdResource ) && StringUtils.isNumeric( strIdResource ) )
        {
            int nIdIdee = Integer.parseInt( strIdResource );

            return AtelierHome.findByPrimaryKey(nIdIdee);
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceType(  )
    {
        return Atelier.PROPERTY_RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceTypeDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_ATELIER_RESOURCE_TYPE_DESCRIPTION, locale );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceUrl( String strIdResource, String strResourceType )
    {
        if ( StringUtils.isNotBlank( strIdResource ) && StringUtils.isNumeric( strIdResource ) )
        {
            UrlItem urlItem = new UrlItem( AppPathService.getPortalUrl( ) );
            urlItem.addParameter( PARAMETER_ATELIER_ID, strIdResource );

            return urlItem.getUrl( );
        }

        return null;
    }
}
