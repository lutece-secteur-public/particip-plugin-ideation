/*
 * Copyright (c) 2002-2015, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.participatoryideation.service.capgeo;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import fr.paris.lutece.plugins.participatoryideation.business.capgeo.QpvQva;
import fr.paris.lutece.plugins.participatoryideation.business.capgeo.QpvQvaRestResponse;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.httpaccess.HttpAccess;

public class QpvQvaService {

    private static final String PROPERTY_CAPGEO_QPV_QVA_URL = "ideation.capgeo.qpvqva";
    private static final String CAPGEO_QPV_QVA_URL = AppPropertiesService.getProperty(
            PROPERTY_CAPGEO_QPV_QVA_URL, "http://services1.arcgis.com/yFAX7hJID4ONeUHP/arcgis/rest/services/QPV_QVA_GPRU/FeatureServer/0/query" );

    private static final String PARAMETER_GEOMETRY_TYPE="geometryType";
    private static final String PARAMETER_GEOMETRY_TYPE_VALUE="esriGeometryPoint";
    private static final String PARAMETER_IN_SR="inSR";
    private static final String PARAMETER_IN_SR_VALUE="4326";
    private static final String PARAMETER_SPATIAL_REL="esriSpatialRelIntersects";
    private static final String PARAMETER_SPATIAL_REL_VALUE="esriSpatialRelIntersects";
    private static final String PARAMETER_OUT_FIELDS="outFields";
    private static final String PARAMETER_OUT_FIELDS_VALUE="C_NQPV,L_NQPV,C_NAT_QPV,GPRU_NOM,EXT_BP,FID";
    private static final String PARAMETER_RETURN_GEOMETRY="returnGeometry";
    private static final String PARAMETER_RETURN_GEOMETRY_VALUE="false";
    private static final String PARAMETER_RETURN_IDS_ONLY="returnIdsOnly";
    private static final String PARAMETER_RETURN_IDS_ONLY_VALUE="false";
    private static final String PARAMETER_RETURN_COUNT_ONLY="returnCountOnly";
    private static final String PARAMETER_RETURN_COUNT_ONLY_VALUE="false";
    private static final String PARAMETER_RETURN_Z="returnZ";
    private static final String PARAMETER_RETURN_Z_VALUE="false";
    private static final String PARAMETER_RETURN_M="returnM";
    private static final String PARAMETER_RETURN_M_VALUE="false";
    private static final String PARAMETER_RETURN_DISTINCT="returnDistinctValues";
    private static final String PARAMETER_RETURN_DISTINCT_VALUE="false";
    private static final String PARAMETER_FORMAT="f";
    private static final String PARAMETER_FORMAT_VALUE="json";

    private static final String PARAMETER_GEOMETRY="geometry";

    private static ObjectMapper _mapper;
    static
    {
        _mapper = new ObjectMapper(  );
        _mapper.configure( DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false );
    }

    /** Private constructor */
    private QpvQvaService()
    {
    }

    private static void addCommonParameters( Map<String, String> mapParameters, double longitude, double latitude ) {
        mapParameters.put(PARAMETER_GEOMETRY_TYPE,PARAMETER_GEOMETRY_TYPE_VALUE);
        mapParameters.put(PARAMETER_IN_SR,PARAMETER_IN_SR_VALUE);
        mapParameters.put(PARAMETER_SPATIAL_REL,PARAMETER_SPATIAL_REL_VALUE);
        mapParameters.put(PARAMETER_RETURN_GEOMETRY,PARAMETER_RETURN_GEOMETRY_VALUE);
        mapParameters.put(PARAMETER_RETURN_IDS_ONLY,PARAMETER_RETURN_IDS_ONLY_VALUE);
        mapParameters.put(PARAMETER_RETURN_COUNT_ONLY,PARAMETER_RETURN_COUNT_ONLY_VALUE);
        mapParameters.put(PARAMETER_RETURN_Z,PARAMETER_RETURN_Z_VALUE);
        mapParameters.put(PARAMETER_RETURN_M,PARAMETER_RETURN_M_VALUE);
        mapParameters.put(PARAMETER_RETURN_DISTINCT,PARAMETER_RETURN_DISTINCT_VALUE);
        mapParameters.put(PARAMETER_FORMAT,PARAMETER_FORMAT_VALUE);
        String strLonLat = String.format( Locale.ENGLISH, "%.6f,%.6f", longitude, latitude );
        mapParameters.put(PARAMETER_GEOMETRY, strLonLat);
    }

    public static List<QpvQva> getQpvQva(double longitude, double latitude)
            throws Exception {
        HttpAccess httpAccess = new HttpAccess( );
        Map<String, String> mapParameters = new ConcurrentHashMap<String, String>(  );
        
        addCommonParameters(mapParameters, longitude, latitude);
        mapParameters.put(PARAMETER_OUT_FIELDS,PARAMETER_OUT_FIELDS_VALUE);

        QpvQvaRestResponse response;
        
       /* ClientConfig clientConfig = new DefaultClientConfig(  );
        clientConfig.getFeatures(  ).put( JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE );
        Client client = Client.create( clientConfig );
        //client.addFilter( new HTTPBasicAuthFilter( userName, password ) );
        WebResource webResource = client.resource( CAPGEO_QPV_QVA_URL );
        
        //MultivaluedMap multivaluedMap = new MultivaluedMapImpl();
        
        ClientResponse clientResponse = webResource
        		.queryParam(PARAMETER_GEOMETRY_TYPE, PARAMETER_GEOMETRY_TYPE_VALUE)
        		.queryParam(PARAMETER_IN_SR, PARAMETER_IN_SR_VALUE)
        		.queryParam(PARAMETER_SPATIAL_REL,PARAMETER_SPATIAL_REL_VALUE)
        		.queryParam(PARAMETER_RETURN_GEOMETRY,PARAMETER_RETURN_GEOMETRY_VALUE)
        		.queryParam(PARAMETER_RETURN_IDS_ONLY,PARAMETER_RETURN_IDS_ONLY_VALUE)
        		.queryParam(PARAMETER_RETURN_COUNT_ONLY,PARAMETER_RETURN_COUNT_ONLY_VALUE)
        		.queryParam(PARAMETER_RETURN_Z,PARAMETER_RETURN_Z_VALUE)
        		.queryParam(PARAMETER_RETURN_M,PARAMETER_RETURN_M_VALUE)
        		.queryParam(PARAMETER_RETURN_DISTINCT,PARAMETER_RETURN_DISTINCT_VALUE)
        		.queryParam(PARAMETER_FORMAT,PARAMETER_FORMAT_VALUE)
        		.queryParam(PARAMETER_GEOMETRY, mapParameters.get("geometry"))
        		.type( MediaType.APPLICATION_JSON ).accept( MediaType.APPLICATION_JSON )
                .get( ClientResponse.class );
        
        int status = clientResponse.getStatus();*/
        
        String strJson = httpAccess.doPost(CAPGEO_QPV_QVA_URL, mapParameters);
        AppLogService.info("Ideation QpvQvaService qpvqva: got response from " + CAPGEO_QPV_QVA_URL + strJson);
        response = _mapper.readValue( strJson, QpvQvaRestResponse.class );
        List<QpvQva> features = response.getFeatures();
        return features;
    }

}
