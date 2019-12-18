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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class LinkStaticService extends AbstractCacheableService implements ILinkStaticService {

    private static ILinkStaticService _singleton;
    private static final String BEAN_IDEATION_STATIC_SERVICE = "participatoryideation.linkStaticService";

    private static final String SERVICE_NAME = "Link Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";

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
