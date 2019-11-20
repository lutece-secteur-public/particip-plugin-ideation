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

public class AtelierStaticService extends AbstractCacheableService implements IAtelierStaticService {

    private static IAtelierStaticService _singleton;
    private static final String BEAN_IDEATION_STATIC_SERVICE="ideation.atelierStaticService";

    private static final String SERVICE_NAME = "Atelier Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";
    private static final String MARK_CAMPAGNE_STATIC = "campagne_static";

    private static final String MARK_CAMPAGNE = "campagne";
    private static final String MARK_ARRONDISSEMENTS_LIST = "arrondissements_list";
    private static final String MARK_ARRONDISSEMENTS_MAP = "arrondissements_map";
    private static final String MARK_LOCALISATION_TYPE_LIST = "type_localisation_list";
    private static final String MARK_LOCALISATION_TYPE_MAP = "type_localisation_map";
    private static final String MARK_HANDICAP_LIST = "handicap_list";
    private static final String MARK_HANDICAP_MAP  = "handicap_map";
    private static final String MARK_THEMES_LIST = "themes_list";
    private static final String MARK_THEMES_MAP = "themes_map";

    public static final String CACHE_KEY = "[atelierStatic]";
  

    public void fillAllStaticContent( Map<String, Object> model )
    {
    	Object cached = getFromCache( CACHE_KEY );
        if ( cached == null ) 
        {
           cached = putAllStaticContentInCache(  ); 
        }
        model.put( MARK_GLOBAL_STATIC, cached );

        model.put( MARK_ARRONDISSEMENTS_MAP, IdeeService.getInstance().getArrondissementsMap() );
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance().getTypeLocalisationList());
        model.put( MARK_LOCALISATION_TYPE_MAP, IdeeService.getInstance().getTypeLocalisationMap());
    }
    
    private Map<String, Object> putAllStaticContentInCache(  ) 
    {
        Map<String, Object> content = new HashMap<String, Object>();
        Collection<Campagne> listCampagne = CampagneHome.getCampagnesList();
        Map<String, List<CampagneTheme>> mapThemes = CampagneThemeHome.getCampagneThemesMapByCampagne( );
        
        for ( Campagne campagne: listCampagne ) 
        {
            Map<String, Object> campagneContent = new HashMap<String, Object>( );
            campagneContent.put( MARK_CAMPAGNE, campagne );
            campagneContent.put( MARK_THEMES_LIST, mapThemes.get( campagne.getCode(  ) ) );

            Map<String, CampagneTheme> mapThemesByCode = new HashMap<String, CampagneTheme>( );
            
            for ( CampagneTheme campagneTheme: mapThemes.get( campagne.getCode(  ) ) ) 
            {
                mapThemesByCode.put( campagneTheme.getCode(  ), campagneTheme );
            }
            campagneContent.put( MARK_THEMES_MAP, mapThemesByCode );
           

            content.put( campagne.getCode( ), campagneContent );
        }
        putInCache ( CACHE_KEY, content );
        
        return content;
    }

    public static IAtelierStaticService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATION_STATIC_SERVICE );
        }
        return _singleton;
    }

    public AtelierStaticService()
    {
            initCache();
    }

    public String getName(  )
    {
        return SERVICE_NAME;
    }

}
