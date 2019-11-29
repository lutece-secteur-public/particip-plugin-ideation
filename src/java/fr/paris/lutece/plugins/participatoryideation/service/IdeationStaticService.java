package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneTheme;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneThemeHome;
import fr.paris.lutece.plugins.participatoryideation.business.DepositaireType;
import fr.paris.lutece.plugins.participatoryideation.business.DepositaireTypeHome;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class IdeationStaticService extends AbstractCacheableService implements IIdeationStaticService {

    private static IIdeationStaticService _singleton;
    private static final String BEAN_IDEATION_STATIC_SERVICE="participatoryideation.ideationStaticService";

    private static final String SERVICE_NAME = "Ideation Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";
    private static final String MARK_CAMPAGNE_STATIC = "campagne_static";
    private static final String MARK_LIST_CAMPAGNE = "list_campagne";
    private static final String MARK_LIST_STATUS_STATIC = "status_static_list";

    private static final String MARK_CAMPAGNE = "campagne";
    private static final String MARK_ARRONDISSEMENTS_LIST = "arrondissements_list";
    private static final String MARK_ARRONDISSEMENTS_MAP = "arrondissements_map";
    private static final String MARK_QPVQVA_LIST = "qpvqva_list";
    private static final String MARK_QPVQVA_MAP = "qpvqva_map";
    private static final String MARK_HANDICAP_LIST = "handicap_list";
    private static final String MARK_HANDICAP_MAP  = "handicap_map";
    private static final String MARK_LOCALISATION_TYPE_LIST = "type_localisation_list";
    private static final String MARK_LOCALISATION_TYPE_MAP = "type_localisation_map";
    private static final String MARK_THEMES_LIST = "themes_list";
    private static final String MARK_THEMES_MAP = "themes_map";
    private static final String MARK_DEPOSITAIRES_TYPES_LIST = "depositaire_types_list";
    private static final String MARK_DEPOSITAIRES_TYPES_MAP = "depositaires_types_map";
    private static final String MARK_DEPOSITAIRES_TYPES_LIST_VALUES_MAP = "depositaires_types_list_values_map";

    public static final String CACHE_KEY = "[ideationStatic]";

    public void fillCampagneStaticContent(Map<String, Object> model, String strCampagneCode) {
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = (Map<String, Object>) getFromCache( CACHE_KEY );
        if (cached == null) {
           cached = putAllStaticContentInCache(  ); 
        }
        Collection<Campagne> listCampagne = CampagneHome.getCampagnesList();
        if(listCampagne!=null && listCampagne.size()>0)
        {
        	model.put( MARK_LIST_CAMPAGNE, listCampagne );
        }
        model.put( MARK_CAMPAGNE_STATIC, cached.get(strCampagneCode) );
        model.put( MARK_ARRONDISSEMENTS_LIST, IdeeService.getInstance().getArrondissements() );
        model.put( MARK_ARRONDISSEMENTS_MAP, IdeeService.getInstance().getArrondissementsMap() );
        model.put( MARK_QPVQVA_LIST, IdeeService.getInstance().getQpvQvaCodesList());
        model.put( MARK_QPVQVA_MAP, IdeeService.getInstance().getQpvQvaCodesMap());
        model.put( MARK_HANDICAP_LIST, IdeeService.getInstance().getHandicapCodesList());
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance().getTypeLocalisationList());
        model.put( MARK_LOCALISATION_TYPE_MAP, IdeeService.getInstance().getTypeLocalisationMap());
        
        if ( WorkflowService.getInstance(  ).isAvailable( ) ) 
        {
        	List<Idee.Status> enumList = Arrays.asList(Idee.Status.values());
        	ReferenceList WorkflowStatesReferenceList = new ReferenceList();
        	for( Idee.Status status: enumList ) 
        	{
        		if ( status.isPublished( )  )
        		{
        			WorkflowStatesReferenceList.addItem( status.getValeur( ) , I18nService.getLocalizedString( status.getLibelle( ) , new Locale("fr","FR") ) );
        		}
        	}
        	model.put ( MARK_LIST_STATUS_STATIC, WorkflowStatesReferenceList );
        }
    }

    public void fillAllStaticContent(Map<String, Object> model) {
        Object cached = getFromCache( CACHE_KEY );
        if (cached == null) {
           cached = putAllStaticContentInCache(  ); 
        }
        model.put( MARK_GLOBAL_STATIC, cached );
        model.put( MARK_ARRONDISSEMENTS_LIST, IdeeService.getInstance().getArrondissements() );
        model.put( MARK_ARRONDISSEMENTS_MAP, IdeeService.getInstance().getArrondissementsMap() );
        model.put( MARK_QPVQVA_LIST, IdeeService.getInstance().getQpvQvaCodesList());
        model.put( MARK_QPVQVA_MAP, IdeeService.getInstance().getQpvQvaCodesMap());
        model.put( MARK_HANDICAP_LIST, IdeeService.getInstance().getHandicapCodesList());
        model.put( MARK_HANDICAP_MAP , IdeeService.getInstance().getHandicapCodesMap());
        model.put( MARK_LOCALISATION_TYPE_LIST, IdeeService.getInstance().getTypeLocalisationList());
        model.put( MARK_LOCALISATION_TYPE_MAP, IdeeService.getInstance().getTypeLocalisationMap());
        
    }

    private Map<String, Object> putAllStaticContentInCache(  ) {
        Map<String, Object> content = new HashMap<String, Object>();
        Collection<Campagne> listCampagne = CampagneHome.getCampagnesList();
        Map<String, List<CampagneTheme>> mapThemes = CampagneThemeHome.getCampagneThemesMapByCampagne( );
        Map<String, List<DepositaireType>> mapDepositairesTypes = DepositaireTypeHome.getDepositaireTypesMapByCampagne( );
        for (Campagne campagne: listCampagne) {
            Map<String, Object> campagneContent = new HashMap<String, Object>();
            campagneContent.put( MARK_CAMPAGNE, campagne );
            campagneContent.put( MARK_THEMES_LIST, mapThemes.get( campagne.getCode(  ) ) );
            campagneContent.put( MARK_DEPOSITAIRES_TYPES_LIST, mapDepositairesTypes.get( campagne.getCode(  ) ) );

            Map<String, CampagneTheme> mapThemesByCode = new HashMap<String, CampagneTheme>();
            for ( CampagneTheme campagneTheme: mapThemes.get( campagne.getCode(  ) ) ) {
                mapThemesByCode.put( campagneTheme.getCode(  ), campagneTheme );
            }
            campagneContent.put( MARK_THEMES_MAP, mapThemesByCode );

            Map<String, DepositaireType> mapDepositairesTypesByCode = new HashMap<String, DepositaireType>();
            for ( DepositaireType depositaireType: mapDepositairesTypes.get( campagne.getCode(  ) ) ) {
                mapDepositairesTypesByCode.put( depositaireType.getCode(  ), depositaireType );
            }
            campagneContent.put( MARK_DEPOSITAIRES_TYPES_MAP, mapDepositairesTypesByCode );

            Map<String, String> mapDepositairesTypesListValuesByCode = new HashMap<String, String>();
            for ( DepositaireType depositaireType: mapDepositairesTypes.get( campagne.getCode(  ) ) ) {
                if ( DepositaireType.CODE_COMPLEMENT_TYPE_LIST.equals( depositaireType.getCodeComplementType() ) ) {
                    for ( ReferenceItem referenceItem: depositaireType.getValues() ) {
                        mapDepositairesTypesListValuesByCode.put( depositaireType.getCode() + "-" + referenceItem.getCode(), referenceItem.getName() );
                    }
                }
            }
            campagneContent.put( MARK_DEPOSITAIRES_TYPES_LIST_VALUES_MAP, mapDepositairesTypesListValuesByCode );

            content.put(campagne.getCode(), campagneContent);
        }
        putInCache ( CACHE_KEY, content );
        return content;
    }

    public static IIdeationStaticService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATION_STATIC_SERVICE );
        }
        return _singleton;
    }

    public IdeationStaticService()
    {
            initCache();
    }

    public String getName(  )
    {
        return SERVICE_NAME;
    }

}
