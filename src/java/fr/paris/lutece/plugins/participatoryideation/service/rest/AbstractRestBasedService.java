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

import org.json.JSONArray;
import org.json.JSONObject;

import fr.paris.lutece.plugins.participatoryideation.service.processor.IdeationClientProcessor;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides some util methods for rest-based services.
 */
public abstract class AbstractRestBasedService
{

    // *********************************************************************************************
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
    // * UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS UTILS *
    // *********************************************************************************************

    /**
     * Parse a String typed REST response.
     */
    protected String parseString( String restRequest )
    {
        try
        {
            JSONObject json = new JSONObject( IdeationClientProcessor.getProcess( restRequest ) );
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
    protected boolean parseBoolean( String restRequest )
    {
        try
        {
            JSONObject json = new JSONObject( IdeationClientProcessor.getProcess( restRequest ) );
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
    protected ReferenceList parseReferenceList( String restRequest )
    {
        ReferenceList listAreas = new ReferenceList( );
        try
        {
            JSONObject jsonResult = new JSONObject( IdeationClientProcessor.getProcess( restRequest ) );
            if ( jsonResult.getString( "status" ).equals( "OK" ) )
            {
                JSONArray jsonArray = jsonResult.getJSONArray( "result" );

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
    protected ReferenceList parseValueList( String restRequest )
    {
        ReferenceList listAreas = new ReferenceList( );
        try
        {
            JSONObject jsonResult = new JSONObject( IdeationClientProcessor.getProcess( restRequest ) );
            if ( jsonResult.getString( "status" ).equals( "OK" ) )
            {
                JSONArray jsonArray = jsonResult.getJSONArray( "result" );

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
    protected int countValueList( String restRequest )
    {
        try
        {
            JSONObject areasJson = new JSONObject( IdeationClientProcessor.getProcess( restRequest ) );
            if ( areasJson.getString( "status" ).equals( "OK" ) )
            {
                JSONArray jsonArray = areasJson.getJSONArray( "result" );
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
