package fr.paris.lutece.plugins.ideation.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.paris.lutece.plugins.campagnebp.business.Campagne;
import fr.paris.lutece.plugins.campagnebp.business.CampagneHome;
import fr.paris.lutece.plugins.campagnebp.business.CampagneTheme;
import fr.paris.lutece.plugins.campagnebp.business.CampagneThemeHome;
import fr.paris.lutece.plugins.ideation.business.BoTag;
import fr.paris.lutece.plugins.ideation.business.BoTagHome;
import fr.paris.lutece.plugins.ideation.business.DepositaireType;
import fr.paris.lutece.plugins.ideation.business.DepositaireTypeHome;
import fr.paris.lutece.plugins.ideation.business.FoTag;
import fr.paris.lutece.plugins.ideation.business.FoTagHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class LinkStaticService extends AbstractCacheableService implements ILinkStaticService {

    private static ILinkStaticService _singleton;
    private static final String BEAN_IDEATION_STATIC_SERVICE = "ideation.linkStaticService";

    private static final String SERVICE_NAME = "Link Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";
    private static final String MARK_CAMPAGNE_STATIC = "campagne_static";

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
