package fr.paris.lutece.plugins.ideation.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class LinkStaticService extends AbstractCacheableService implements ILinkStaticService {

    private static ILinkStaticService _singleton;
    private static final String BEAN_IDEATION_STATIC_SERVICE = "ideation.linkStaticService";

    private static final String SERVICE_NAME = "Link Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";

    private static final String MARK_CAMPAGNE = "campagne";

    public static final String CACHE_KEY = "[linkStatic]";
  

    public void fillAllStaticContent( Map<String, Object> model )
    {
    	Object cached = getFromCache( CACHE_KEY );
        if ( cached == null ) 
        {
           cached = putAllStaticContentInCache(  ); 
        }
        model.put( MARK_GLOBAL_STATIC, cached );
    }
    
    private Map<String, Object> putAllStaticContentInCache(  ) 
    {
        Map<String, Object> content = new HashMap<String, Object>();
        Collection<Campagne> listCampagne = CampagneHome.getCampagnesList();
        
        for ( Campagne campagne: listCampagne ) 
        {
            Map<String, Object> campagneContent = new HashMap<String, Object>( );
            campagneContent.put( MARK_CAMPAGNE, campagne );
            content.put( campagne.getCode( ), campagneContent );
        }
        putInCache ( CACHE_KEY, content );
        
        return content;
    }

    public static ILinkStaticService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATION_STATIC_SERVICE );
        }
        return _singleton;
    }

    public LinkStaticService()
    {
            initCache();
    }

    public String getName(  )
    {
        return SERVICE_NAME;
    }

}
