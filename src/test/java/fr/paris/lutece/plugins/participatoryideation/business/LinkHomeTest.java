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
package fr.paris.lutece.plugins.participatoryideation.business;

import java.util.Collection;

import fr.paris.lutece.plugins.participatoryideation.business.link.Link;
import fr.paris.lutece.plugins.participatoryideation.business.link.LinkHome;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.IdeeHome;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

/**
 * DepositaireTypeTest
 */
public class LinkHomeTest extends LuteceTestCase
{

    private final static String PARENT_CAMPAIGN = "abc";
    private final static int PARENT_CODE_ID = 1;
    private final static String PARENT_TITLE = "parent_title";

    private final static String CHILD_CAMPAIGN = "xyz";
    private final static int CHILD_CODE_ID = 1;
    private final static String CHILD_TITLE = "child_title";

    private final static String PARENT_CAMPAIGN_2 = "abc_2";
    private final static int PARENT_CODE_ID_2 = 1;
    private final static String PARENT_TITLE_2 = "parent_title_2";

    private final static String CHILD_CAMPAIGN_2 = "xyz_2";
    private final static int CHILD_CODE_ID_2 = 1;
    private final static String CHILD_TITLE_2 = "child_title_2";

    public void testBusiness( )
    {
        // *********************************************************************************************
        // * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA *
        // * DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA DATA *
        // *********************************************************************************************

        Idee ideeParent = IdeeHomeTest.getMockIdeeInstance( );
        ideeParent.setCodeCampagne( PARENT_CAMPAIGN );
        ideeParent.setCodeIdee( PARENT_CODE_ID );
        ideeParent.setTitre( PARENT_TITLE );
        IdeeHome.create( ideeParent );

        Idee ideeChild = IdeeHomeTest.getMockIdeeInstance( );
        ideeChild.setCodeCampagne( CHILD_CAMPAIGN );
        ideeChild.setCodeIdee( CHILD_CODE_ID );
        ideeChild.setTitre( CHILD_TITLE );
        IdeeHome.create( ideeChild );

        Idee ideeParent2 = IdeeHomeTest.getMockIdeeInstance( );
        ideeParent2.setCodeCampagne( PARENT_CAMPAIGN_2 );
        ideeParent2.setCodeIdee( PARENT_CODE_ID_2 );
        ideeParent2.setTitre( PARENT_TITLE_2 );
        IdeeHome.create( ideeParent2 );

        Idee ideeChild2 = IdeeHomeTest.getMockIdeeInstance( );
        ideeChild2.setCodeCampagne( CHILD_CAMPAIGN_2 );
        ideeChild2.setCodeIdee( CHILD_CODE_ID_2 );
        ideeChild2.setTitre( CHILD_TITLE_2 );
        IdeeHome.create( ideeChild2 );

        Link link = new Link( );

        link.setChildCodeCampagne( CHILD_CAMPAIGN );
        link.setChildId( ideeChild.getId( ) );
        link.setChildCodeIdee( CHILD_CODE_ID );
        link.setChildTitle( CHILD_TITLE );

        link.setParentCodeCampagne( PARENT_CAMPAIGN );
        link.setParentId( ideeParent.getId( ) );
        link.setParentCodeIdee( PARENT_CODE_ID );
        link.setParentTitle( PARENT_TITLE );

        LinkHome.create( link );

        // *********************************************************************************************
        // * TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST *
        // * TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST *
        // *********************************************************************************************

        // Test getIdLinksList method
        Collection<Integer> idLinksList = LinkHome.getIdLinksList( );
        assertEquals( 1, idLinksList.size( ) );
        assertEquals( link.getId( ), (int) idLinksList.iterator( ).next( ) );

        // Test getLinksList method
        Collection<Link> linksList = LinkHome.getLinksList( );
        assertEquals( 1, linksList.size( ) );
        Link storedLink = linksList.iterator( ).next( );
        assertEquals( link, storedLink );

        // Test update method
        link.setChildCodeCampagne( CHILD_CAMPAIGN_2 );
        link.setChildId( ideeChild2.getId( ) );
        link.setChildCodeIdee( CHILD_CODE_ID_2 );
        link.setChildTitle( CHILD_TITLE_2 );

        link.setParentCodeCampagne( PARENT_CAMPAIGN_2 );
        link.setParentId( ideeParent2.getId( ) );
        link.setParentCodeIdee( PARENT_CODE_ID_2 );
        link.setParentTitle( PARENT_TITLE_2 );

        LinkHome.update( link );

        linksList = LinkHome.getLinksList( );
        storedLink = linksList.iterator( ).next( );
        assertEquals( link, storedLink );

        // Test remove method
        LinkHome.remove( link.getId( ) );
        idLinksList = LinkHome.getIdLinksList( );
        assertEquals( 0, idLinksList.size( ) );
    }

    // *********************************************************************************************
    // * UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL *
    // * UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL UTIL *
    // *********************************************************************************************

    private void assertEquals( Link link1, Link link2 )
    {
        assertEquals( link1.getId( ), link2.getId( ) );

        assertEquals( link1.getChildCodeCampagne( ), link2.getChildCodeCampagne( ) );

        assertEquals( link1.getChildId( ), link2.getChildId( ) );

        assertEquals( link1.getChildCodeIdee( ), link2.getChildCodeIdee( ) );
        assertEquals( link1.getChildTitle( ), link2.getChildTitle( ) );

        assertEquals( link1.getParentCodeCampagne( ), link2.getParentCodeCampagne( ) );
        assertEquals( link1.getParentId( ), link2.getParentId( ) );
        assertEquals( link1.getParentCodeIdee( ), link2.getParentCodeIdee( ) );
        assertEquals( link1.getParentTitle( ), link2.getParentTitle( ) );
    }

}
