package fr.paris.lutece.plugins.ideation.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.leaflet.service.IconService;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchEngine;
import fr.paris.lutece.plugins.search.solr.business.SolrSearchResult;
import fr.paris.lutece.plugins.search.solr.indexer.SolrItem;
import fr.paris.lutece.plugins.search.solr.service.ISolrSearchAppAddOn;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

public class IdeationSolrAddon implements ISolrSearchAppAddOn {
    
    //Parameters copied from SolrSearchApp
    private static final String SOLRSEARCHAPP_PARAMETER_CONF = "conf";
    private static final String SOLRSEARCHAPP_MARK_POINTS_GEOJSON = "geojson";
    private static final String SOLRSEARCHAPP_MARK_POINTS_ID = "id";
    private static final String SOLRSEARCHAPP_MARK_POINTS_FIELDCODE = "code";
    private static final String SOLRSEARCHAPP_MARK_POINTS_TYPE = "type";
    private static final String SOLRSEARCHAPP_PROPERTY_SOLR_RESPONSE_MAX = "solr.reponse.max";
    private static final int SOLRSEARCHAPP_SOLR_RESPONSE_MAX = Integer.parseInt(AppPropertiesService.getProperty(
            SOLRSEARCHAPP_PROPERTY_SOLR_RESPONSE_MAX, "50"));

    private static final String PARAMETER_CONF_MAP = "map_idees";
    private static final String SOLR_QUERY_ALL = "*:*";
    private static final String PROPERTY_OLDPROJECTS_FQ = "ideation.oldprojects.fq";
    private static final String[] SOLR_FQ_OLDPROJECTS = { AppPropertiesService.getProperty(
            PROPERTY_OLDPROJECTS_FQ, "type:Projet 2015") };
    private static final String MARK_OLDPROJECTS_POINTS = "oldprojects_points";

	@Override
	public void buildPageAddOn(Map<String, Object> model, HttpServletRequest request) {
		Campagne lastCampagne = CampagneHome.getLastCampagne(); 
		IdeationStaticService.getInstance(  ).fillCampagneStaticContent(model, lastCampagne.getCode());
		
		//IdeationStaticService.getInstance(  ).fillAllStaticContent(model);

        if ( PARAMETER_CONF_MAP.equals( request.getParameter( SOLRSEARCHAPP_PARAMETER_CONF ) ) ) {
            SolrSearchEngine engine = SolrSearchEngine.getInstance();
            List<SolrSearchResult> listResultsGeoloc = engine.getGeolocSearchResults( SOLR_QUERY_ALL, SOLR_FQ_OLDPROJECTS, SOLRSEARCHAPP_SOLR_RESPONSE_MAX );
            List<HashMap<String, Object>> points = getGeolocModel(listResultsGeoloc);
            model.put( MARK_OLDPROJECTS_POINTS, points );
        }
	}

    /**
     * CopyPasted from SolrSearchApp to have the same freemarkers as if it was a search
     */
    private static List<HashMap<String, Object>> getGeolocModel( List<SolrSearchResult> listResultsGeoloc ) {
        List<HashMap<String, Object>> points = new ArrayList<HashMap<String, Object>>( listResultsGeoloc.size(  ) );
        HashMap<String, String> iconKeysCache = new HashMap<String, String>(  );

        for ( SolrSearchResult result : listResultsGeoloc )
        {
            Map<String, Object> dynamicFields = result.getDynamicFields(  );

            for ( String key : dynamicFields.keySet(  ) )
            {
                if ( key.endsWith( SolrItem.DYNAMIC_GEOJSON_FIELD_SUFFIX ) )
                {
                    HashMap<String, Object> h = new HashMap<String, Object>(  );
                    String strJson = (String) dynamicFields.get( key );
                    GeolocItem geolocItem = null;

                    try
                    {
                        geolocItem = GeolocItem.fromJSON( strJson );
                    }
                    catch ( IOException e )
                    {
                        AppLogService.error( "SolrSearchApp: error parsing geoloc JSON: " + strJson +
                            ", exception " + e );
                    }

                    if ( geolocItem != null )
                    {
                        String strType = result.getId(  ).substring( result.getId(  ).lastIndexOf( "_" ) + 1 );
                        String strIcon;

                        if ( iconKeysCache.containsKey( geolocItem.getIcon(  ) ) )
                        {
                            strIcon = iconKeysCache.get( geolocItem.getIcon(  ) );
                        }
                        else
                        {
                            strIcon = IconService.getIcon( strType, geolocItem.getIcon(  ) );
                            iconKeysCache.put( geolocItem.getIcon(  ), strIcon );
                        }

                        geolocItem.setIcon( strIcon );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_GEOJSON, geolocItem.toJSON(  ) );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_ID,
                            result.getId(  )
                                  .substring( result.getId(  ).indexOf( "_" ) + 1,
                                result.getId(  ).lastIndexOf( "_" ) ) );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_FIELDCODE, key.substring( 0, key.lastIndexOf( "_" ) ) );
                        h.put( SOLRSEARCHAPP_MARK_POINTS_TYPE, strType );
                        points.add( h );
                    }
                }
            }
        }
        return points;
    }
}
