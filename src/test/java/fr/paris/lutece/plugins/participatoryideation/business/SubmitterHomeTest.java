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
import java.util.Iterator;

import fr.paris.lutece.plugins.participatoryideation.business.submitter.Submitter;
import fr.paris.lutece.plugins.participatoryideation.business.submitter.SubmitterHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * SubmitterTypeTest
 */
public class SubmitterHomeTest extends LuteceTestCase
{
    private final static String CODE_CAMPAIGN = "XYZ";
    private final static String CODE_SUBMITTER_TYPE = "ABCD1234";

    public void testBusiness( )
    {
        // Initialize an object
        Submitter instance = new Submitter( );
        instance.setCodeCampaign( CODE_CAMPAIGN );
        instance.setCodeSubmitterType( CODE_SUBMITTER_TYPE );

        // Create test
        SubmitterHome.create( instance );
        int id = instance.getId( );

        // List test
        Collection<Integer> idList = SubmitterHome.getIdSubmittersList( );
        assertTrue( idList.size( ) > 0 );
        boolean submitterFound = false;
        for ( Integer integer : idList )
        {
            submitterFound = ( submitterFound || ( integer == id ) );
        }
        assertTrue( submitterFound );

        // List by campaign test
        Collection<Submitter> submitterList = SubmitterHome.getSubmitterListByCampaign( CODE_CAMPAIGN );

        assertEquals( 1, submitterList.size( ) );

        Submitter [ ] submitterArray = new Submitter [ 1];
        submitterList.toArray( submitterArray );
        checkAssertsOnInstance( submitterArray [0], instance );

        // List by campaign test
        submitterList = SubmitterHome.getSubmitterListByCampaign( CODE_CAMPAIGN + "123" );
        assertEquals( 0, submitterList.size( ) );

        // Find test
        Submitter instanceStored = SubmitterHome.findByPrimaryKey( id );
        assertTrue( instanceStored != null );
        checkAssertsOnInstance( instanceStored, instance );

        // Delete test
        SubmitterHome.remove( instance.getId( ) );

        submitterList = SubmitterHome.getSubmitterListByCampaign( CODE_CAMPAIGN );
        assertEquals( 0, submitterList.size( ) );

        Collection<Integer> idListAfterDelete = SubmitterHome.getIdSubmittersList( );
        assertEquals( idList.size( ) - 1, idListAfterDelete.size( ) );
    }

    private void checkAssertsOnInstance( Submitter instanceStored, Submitter instance )
    {
        assertEquals( instanceStored.getId( ), instance.getId( ) );
        assertEquals( instanceStored.getCodeSubmitterType( ), instance.getCodeSubmitterType( ) );
        assertEquals( instanceStored.getCodeCampaign( ), instance.getCodeCampaign( ) );
    }
}
