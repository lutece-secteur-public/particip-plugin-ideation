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
package fr.paris.lutece.plugins.participatoryideation.service.myinfos;

import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.util.url.UrlItem;

/**
 * This class provides very simple 'my infos' services : - myinfos are always valid
 */
public class MyInfosService implements IMyInfosService
{

	private static final String PAGE_MY_INFOS = "mesInfos";
    private static final String VIEW_MY_INFOS = "mesinfos";
    private static final String PARAMETER_COMPLETE_INFOS = "completeInfos";

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    private static final String BEAN_MYINFOS_SERVICE = "participatoryideation.myInfosService";

    private static IMyInfosService _singleton;

    public static IMyInfosService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_MYINFOS_SERVICE );
        }
        return _singleton;
    }

    // *********************************************************************************************
    // * ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID I *
    // * ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID ISVALID I *
    // *********************************************************************************************

    @Override
    public boolean isUserValid( String userId )
    {
        return true;
    }

    // *********************************************************************************************
    // * FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL *
    // * FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL FILL *
    // *********************************************************************************************

    @Override
    public String getUrlMyInfosFillAction( )
    {
        UrlItem urlItem = new UrlItem( AppPathService.getPortalUrl( ) );
        
        urlItem.addParameter( MVCUtils.PARAMETER_PAGE, PAGE_MY_INFOS );
        urlItem.addParameter( MVCUtils.PARAMETER_VIEW, VIEW_MY_INFOS );
        urlItem.addParameter( PARAMETER_COMPLETE_INFOS, Boolean.TRUE.toString( ) );

        return urlItem.getUrl( );
    }

}
