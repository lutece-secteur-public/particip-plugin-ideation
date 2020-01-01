/*
 * Copyright (c) 2002-2019, Mairie de Paris
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
package fr.paris.lutece.plugins.participatoryideation.service;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.paris.lutece.plugins.participatoryideation.service.processor.IdeationClientProcessor;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

public class IdeationCampagneService implements IIdeationCampagneService {
	
	private static final String BEAN_IDEATIONCAMPAGNE_SERVICE = "participatoryideation.ideationCampagneService";
	
	private static IIdeationCampagneService _singleton;
	
	public static IIdeationCampagneService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATIONCAMPAGNE_SERVICE );
        }
        return _singleton;
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public boolean isBeforeBeginning( String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + phase + "/before-beginning"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBeforeBeginning( String codeCampaign, String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/" + phase + "/before-beginning"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfterBeginning( String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + phase + "/after-beginning"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfterBeginning( String codeCampaign, String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/" + phase + "/after-beginning"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuring( String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + phase + "/during"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDuring( String codeCampaign, String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/" + phase + "/during"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBeforeEnd( String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + phase + "/before-end"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBeforeEnd( String codeCampaign, String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/" + phase + "/before-end"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfterEnd( String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + phase + "/after-end"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfterEnd( String codeCampaign, String phase ) {
    	try {
        	JSONObject json =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/" + phase + "/after-end"));
        	return json.getBoolean("result"); }
    	catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return false;
		}    
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCampaignNumberAreas(String codeCampaign) {
    	try {
        	JSONObject areasJson =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/areas"));
        	if (areasJson.getString("status").equals("OK")) {
            	JSONArray jsonArray = areasJson.getJSONArray("result");
            	return jsonArray.length();
        	}
        	return 0;

        } catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
        	return 0;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int getCampaignNumberAreas() {
    	try {
        	JSONObject areasJson =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + "areas"));
        	if (areasJson.getString("status").equals("OK")) {
            	JSONArray jsonArray = areasJson.getJSONArray("result");
            	return jsonArray.length();
        	}
        	return 0;

        } catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
        	return 0;
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getCampaignAreas(String codeCampaign) {
        ReferenceList listAreas = new ReferenceList();
    	try {
        	JSONObject areasJson =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + codeCampaign + "/areas"));
        	if (areasJson.getString("status").equals("OK")) {
            	JSONArray jsonArray = areasJson.getJSONArray("result");

            	if (jsonArray != null) { 
            	   int len = jsonArray.length();
            	   for ( int i = 0; i < len; i++ ) { 
						listAreas.addItem(jsonArray.get(i).toString(), jsonArray.get(i).toString());
            	   }
            	}
        	}
        	
        	return listAreas;
        } catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return listAreas;
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ReferenceList getCampaignAreas() {
        ReferenceList listAreas = new ReferenceList();
    	try {
        	JSONObject areasJson =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + "areas"));
        	if (areasJson.getString("status").equals("OK")) {
            	JSONArray jsonArray = areasJson.getJSONArray("result");

            	if (jsonArray != null) { 
            	   int len = jsonArray.length();
            	   for ( int i = 0; i < len; i++ ) { 
						listAreas.addItem(jsonArray.get(i).toString(), jsonArray.get(i).toString());
            	   }
            	}
        	}
        	
        	return listAreas;
        } catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return listAreas;
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCampaignWholeArea(String codeCampaign) {
    	try {
        	JSONObject wholeJson =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			  + codeCampaign + "/whole"));
        	return wholeJson.getString("result");
        } catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return "";
		}
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCampaignWholeArea() {
    	try {
        	JSONObject wholeJson =  new JSONObject(IdeationClientProcessor.getProcess(
    			      AppPropertiesService.getProperty("participatoryideation.campaign.rest.webapp.url")
    	  			    + AppPropertiesService.getProperty("participatoryideation.campaign.rest.demand.base_url")
    	  			    + "whole"));
        	return wholeJson.getString("result");
        } catch (Exception e) {
        	AppLogService.error( e.getMessage() , e );
			return "";
		}
    }
}