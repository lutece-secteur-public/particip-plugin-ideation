/*
 * Copyright (c) 2002-2020, City of Paris
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
package fr.paris.lutece.plugins.participatoryideation.service.rest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.paris.lutece.plugins.participatoryideation.service.authentication.RequestAuthenticationService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.httpaccess.HttpAccess;
import fr.paris.lutece.util.httpaccess.HttpAccessException;
import fr.paris.lutece.util.signrequest.RequestAuthenticator;

/**
 * This class provides some util methods for rest-based services.
 */
public abstract class AbstractRestBasedService
{

    // *********************************************************************************************
    // * GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST *
    // * GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST GET-POST *
    // *********************************************************************************************

    /**
     * Perform a get HTTP request, expecting a JSon formatted result.
     */
    protected JSONObject doGetJSon( String url )
    {
        try
        {
            RequestAuthenticator authenticator = RequestAuthenticationService.getRequestAuthenticator( );
            return new JSONObject( new HttpAccess( ).doGet( url, authenticator, Arrays.asList( ) ) );
        }
        catch( Exception e )
        {
            AppLogService.error( "An error occurs when calling '" + url + "'", e );
            return null;
        }
    }

    /**
     * Perform a post HTTP request with parameters as Map, expecting a JSon formatted result.
     */
    protected JSONObject doPostJSon( String url, Map<String, String> params ) throws JSONException, HttpAccessException
    {
        try
        {
            RequestAuthenticator authenticator = RequestAuthenticationService.getRequestAuthenticator( );
            return new JSONObject( new HttpAccess( ).doPost( url, params, authenticator, Arrays.asList( ) ) );
        }
        catch( Exception e )
        {
            AppLogService.error( "An error occured when calling '" + url + "'", e );
            return null;
        }
    }

    // *********************************************************************************************
    // * MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST *
    // * MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST MAP-POST *
    // *********************************************************************************************

    /**
     * Post a map, parse and return another map.
     */
    protected Map<String, String> doPostMapToMap( JSONObject json )
    {
        Map<String, String> map = new HashMap<>( );
        try
        {
            if ( json.getString( "status" ).equals( "OK" ) )
            {
                JSONObject jsonResult = json.getJSONObject( "result" );
                if ( jsonResult != null )
                {
                    @SuppressWarnings( "unchecked" )
                    Iterator<String> keys = json.keys( );
                    while ( keys.hasNext( ) )
                    {
                        String key = keys.next( );
                        map.put( key, json.getString( key ) );
                    }
                }
            }
            return map;
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            return map;
        }
    }

    // *********************************************************************************************
    // * PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE *
    // * PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE PARSE *
    // *********************************************************************************************

    /**
     * Parse a String typed REST response.
     */
    protected String parseString( JSONObject json )
    {
        try
        {
            return json.getString( "result" );
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            return "";
        }
    }

    /**
     * Parse a boolean typed REST response.
     */
    protected boolean parseBoolean( JSONObject json )
    {
        try
        {
            return json.getBoolean( "result" );
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            return false;
        }
    }

    /**
     * Parse a ReferenceList typed REST response.
     */
    protected ReferenceList parseReferenceList( JSONObject json )
    {
        ReferenceList listAreas = new ReferenceList( );
        try
        {
            if ( json.getString( "status" ).equals( "OK" ) )
            {
                JSONArray jsonArray = json.getJSONArray( "result" );

                if ( jsonArray != null )
                {
                    int len = jsonArray.length( );
                    for ( int i = 0; i < len; i++ )
                    {
                        JSONObject item = jsonArray.getJSONObject( i );
                        listAreas.addItem( item.getString( "code" ), item.isNull( "title" ) ? item.getString( "name" ) : item.getString( "title" ) );
                    }
                }
            }

            return listAreas;
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            return listAreas;
        }
    }

    /**
     * Parse a list of value REST response.
     */
    protected ReferenceList parseValueList( JSONObject json )
    {
        ReferenceList listAreas = new ReferenceList( );
        try
        {
            if ( json.getString( "status" ).equals( "OK" ) )
            {
                JSONArray jsonArray = json.getJSONArray( "result" );

                if ( jsonArray != null )
                {
                    int len = jsonArray.length( );
                    for ( int i = 0; i < len; i++ )
                    {
                        listAreas.addItem( jsonArray.get( i ).toString( ), jsonArray.get( i ).toString( ) );
                    }
                }
            }

            return listAreas;
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            return listAreas;
        }
    }

    /**
     * Count number of key/value in such typed REST response.
     */
    protected int countValueList( JSONObject json )
    {
        try
        {
            if ( json.getString( "status" ).equals( "OK" ) )
            {
                JSONArray jsonArray = json.getJSONArray( "result" );
                return jsonArray.length( );
            }

            return 0;
        }
        catch( Exception e )
        {
            AppLogService.error( e.getMessage( ), e );
            return 0;
        }
    }

}
