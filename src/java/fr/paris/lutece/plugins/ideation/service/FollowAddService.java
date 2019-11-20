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
package fr.paris.lutece.plugins.ideation.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.document.business.attributes.DocumentAttribute;
import fr.paris.lutece.plugins.extend.modules.follow.business.Follow;
import fr.paris.lutece.plugins.extend.modules.follow.service.IFollowService;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.portal.business.resourceenhancer.IResourceDisplayManager;
import fr.paris.lutece.util.xml.XmlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;



/**
 * Manager for add on display
 * TODO : move this class into a document specific class !
 */
public class FollowAddService implements IResourceDisplayManager
{
    public static final String PROPERTY_RESOURCE_TYPE 	= "document";
    
    private static final String MARK_NB_ASSOCIES 		= "nbAssocies" ;
    private static final String MARK_LIST_CHILDS 		= "listChilds" ;
    private static final String MARK_IDEA				= "idea" ;
    
    @Inject
    @Named( "extendfollow.followService" )
    private IFollowService _followService;

    @Override
    public void getXmlAddOn( StringBuffer strXml, String strResourceType, int nResourceId )
    {
    	return ;
    }

    @Override
    public void buildPageAddOn( Map<String, Object> model, String strResourceType, int nIdResource,
        String strPortletId, HttpServletRequest request )
    {
    	 if ( PROPERTY_RESOURCE_TYPE.equals( strResourceType ) )
         {
         	Document doc = DocumentHome.findByPrimaryKey( nIdResource );
         	DocumentAttribute attr = doc.getAttribute("num_idea");
         	int nCountFollowers = 0;
         	List <Idee> listChilds = null ;
         	List <Idee> listChildsDB = new ArrayList<Idee>() ;
         	if ( attr!=null && StringUtils.isNotEmpty(attr.getTextValue() ) )
         	{
 	            Idee ideeParent = IdeeHome.findByPrimaryKey( Integer.parseInt( attr.getTextValue() ) ) ;
 	            if ( ideeParent!=null )
 	            {
 		            Follow follow = _followService.findByResource( String.valueOf( ideeParent.getId( ) ), "IDEE" );
 		            
 		            listChilds = ideeParent.getChildIdees() ;
 		            if( follow != null )
 		            {
 		            	nCountFollowers = follow.getFollowCount(  ) ;
 		            }
 		            for (Idee ideeChild : listChilds )
 		            {
	 		            follow = _followService.findByResource( String.valueOf( ideeChild.getId( ) ), "IDEE" );
	 	            	if( follow != null )
	 		 	        {
	 	            		nCountFollowers += follow.getFollowCount(  ) ;
	 		 	        }
	 	            	listChildsDB.add( IdeeHome.findByPrimaryKey( ideeChild.getId( ) ) ) ;
 		            }
 		         	model.put( MARK_IDEA,  ideeParent);
 	            }
         	}
         	model.put( MARK_NB_ASSOCIES, nCountFollowers);
         	model.put( MARK_LIST_CHILDS, listChildsDB);
         }
         	
    }
}
