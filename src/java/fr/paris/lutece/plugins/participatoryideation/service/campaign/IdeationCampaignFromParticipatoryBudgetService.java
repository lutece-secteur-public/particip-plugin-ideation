/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatoryideation.service.campaign;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.paris.lutece.plugins.participatoryideation.service.processor.IdeationClientProcessor;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides campaign services and informations from plugin-participatorybudget. It uses the REST API of the plugin. 
 */
public class IdeationCampaignFromParticipatoryBudgetService implements IIdeationCampaignService 
{

	private final static String REST_URL =  
		  AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
		+ AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url");
	
	// *********************************************************************************************
	// * AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA *
	// * AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA *
	// *********************************************************************************************

    @Override
    public int getCampaignNumberLocalizedAreas(String codeCampaign) {
		return countValueList( codeCampaign + "/localized-areas" );
    }
    
    @Override
    public int getCampaignNumberLocalizedAreas() {
		return countValueList( "localized-areas" );
    }

    @Override
    public ReferenceList getCampaignAllAreas(String codeCampaign) {
		return parseValueList( codeCampaign + "/all-areas" );
    }
    
    @Override
    public ReferenceList getCampaignAllAreas() {
		return parseValueList( "all-areas" );
    }

    @Override
    public ReferenceList getCampaignLocalizedAreas(String codeCampaign) {
		return parseValueList( codeCampaign + "/localized-areas" );
    }
    
    @Override
    public ReferenceList getCampaignLocalizedAreas() {
		return parseValueList( "localized-areas" );
    }

    @Override
    public String getCampaignWholeArea(String codeCampaign) {
    	return parseString( codeCampaign + "/whole-area" );
    }
    
    @Override
    public String getCampaignWholeArea() {
    	return parseString( "whole-area" );
    }

    // *********************************************************************************************
	// * PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASE *
	// * PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASE *
	// *********************************************************************************************

    @Override
    public boolean isBeforeBeginning( String phase ) {
    	return parseBoolean( phase + "/before-beginning" );
    }
    
    @Override
    public boolean isBeforeBeginning( String codeCampaign, String phase ) {
    	return parseBoolean( codeCampaign + "/" + phase + "/before-beginning" );
    }
    
    @Override
    public boolean isAfterBeginning( String phase ) {
    	return parseBoolean( phase + "/after-beginning" );
    }

    @Override
    public boolean isAfterBeginning( String codeCampaign, String phase ) {
    	return parseBoolean( codeCampaign + "/" + phase + "/after-beginning" );
    }
    
    @Override
    public boolean isDuring( String phase ) {
    	return parseBoolean( phase + "/during" );
    }

    @Override
    public boolean isDuring( String codeCampaign, String phase ) {
    	return parseBoolean( codeCampaign + "/" + phase + "/during" );
    }

    @Override
    public boolean isBeforeEnd( String phase ) {
    	return parseBoolean( phase + "/before-end" );
    }

    @Override
    public boolean isBeforeEnd( String codeCampaign, String phase ) {
    	return parseBoolean( codeCampaign + "/" + phase + "/before-end" );
    }

    @Override
    public boolean isAfterEnd( String phase ) {
    	return parseBoolean( phase + "/after-end" );
    }

    @Override
    public boolean isAfterEnd( String codeCampaign, String phase ) {
    	return parseBoolean( codeCampaign + "/" + phase + "/after-end" );
    }

	// *********************************************************************************************
	// * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEME *
	// * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEME *
	// *********************************************************************************************

    @Override
	public ReferenceList getCampaignThemes(String codeCampaign) {
		return parseReferenceList( codeCampaign + "/themes" );
	}

	@Override
	public ReferenceList getCampaignThemes() {
		return parseReferenceList( "themes" );
	}

	// *********************************************************************************************
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
	// * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
	// *********************************************************************************************

	/**
	 * Parse a String typed REST response.
	 */
	private String parseString( String restRequest ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess( REST_URL + restRequest ) );
        	return json.getString("result"); 
        }
    	catch ( Exception e ) 
    	{
        	AppLogService.error( e.getMessage() , e );
			return "";
		}    
	}
	
	/**
	 * Parse a boolean typed REST response.
	 */
	private boolean parseBoolean( String restRequest ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess( REST_URL + restRequest ) );
        	return json.getBoolean("result"); 
        }
    	catch ( Exception e ) 
    	{
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
	}
	
	/**
	 * Parse a ReferenceList typed REST response.
	 */
	private ReferenceList parseReferenceList( String restRequest ) {
        ReferenceList listAreas = new ReferenceList();
    	try {
        	JSONObject jsonResult =  new JSONObject( IdeationClientProcessor.getProcess( REST_URL + restRequest ) );
        	if ( jsonResult.getString( "status" ).equals( "OK" ) ) 
        	{
            	JSONArray jsonArray = jsonResult.getJSONArray( "result" );

            	if ( jsonArray != null ) 
            	{ 
            	   int len = jsonArray.length();
            	   for ( int i = 0; i < len; i++ ) 
            	   {
            		   JSONObject item = jsonArray.getJSONObject( i );
            		   listAreas.addItem( item.getString( "code" ) , item.getString( "name" ) );
            	   }
            	}
        	}
        	
        	return listAreas;
        } 
    	catch ( Exception e ) 
    	{
        	AppLogService.error( e.getMessage() , e );
			return listAreas;
		}
	}
	
	/**
	 * Parse a list of value REST response.
	 */
	private ReferenceList parseValueList( String restRequest ) {
        ReferenceList listAreas = new ReferenceList();
    	try {
        	JSONObject jsonResult =  new JSONObject( IdeationClientProcessor.getProcess( REST_URL + restRequest ) );
        	if ( jsonResult.getString( "status" ).equals( "OK" ) ) 
        	{
            	JSONArray jsonArray = jsonResult.getJSONArray( "result" );

            	if ( jsonArray != null ) 
            	{ 
            	   int len = jsonArray.length();
            	   for ( int i = 0; i < len; i++ ) { 
						listAreas.addItem( jsonArray.get( i ).toString(), jsonArray.get( i ).toString() );
            	   }
            	}
        	}
        	
        	return listAreas;
        } 
    	catch ( Exception e ) 
    	{
        	AppLogService.error( e.getMessage() , e );
			return listAreas;
		}
	}
	
	/**
	 * Count number of key/value in such typed REST response.
	 */
	private int countValueList( String restRequest ) {
    	try {
        	JSONObject areasJson =  new JSONObject( IdeationClientProcessor.getProcess( REST_URL + restRequest ) );
        	if ( areasJson.getString("status").equals("OK") ) 
        	{
            	JSONArray jsonArray = areasJson.getJSONArray( "result" );
            	return jsonArray.length();
        	}
        	
        	return 0;
        } 
    	catch ( Exception e )
    	{
        	AppLogService.error( e.getMessage() , e );
        	return 0;
		}
	}
	
}