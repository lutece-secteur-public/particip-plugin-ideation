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

import fr.paris.lutece.plugins.participatoryideation.business.depositary.Depositary;
import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryHome;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * DepositaryTypeTest
 */
public class DepositaryHomeTest extends LuteceTestCase
{
    private final static String CODE_CAMPAIGN = "XYZ";
    private final static String CODE_DEPOSITARY_TYPE = "ABCD1234";

    public void testBusiness( )
    {
        // Initialize an object
        Depositary instance = new Depositary( );
        instance.setCodeCampaign( CODE_CAMPAIGN );
        instance.setCodeDepositaryType( CODE_DEPOSITARY_TYPE );

        // Create test
        DepositaryHome.create( instance );
        int id = instance.getId( );

        // List test
        Collection<Integer> idList = DepositaryHome.getIdDepositariesList( );
        assertTrue( idList.size( ) > 0 );
        boolean depositaryFound = false;
        for ( Integer integer : idList )
        {
            depositaryFound = ( depositaryFound || ( integer == id ) );
        }
        assertTrue( depositaryFound );

        // List by campaign test
        Collection<Depositary> depositaryList = DepositaryHome.getDepositaryListByCampaign( CODE_CAMPAIGN );

        assertEquals( 1, depositaryList.size( ) );

        Depositary [ ] depositaryArray = new Depositary [ 1];
        depositaryList.toArray( depositaryArray );
        checkAssertsOnInstance( depositaryArray [0], instance );

        // List by campaign test
        depositaryList = DepositaryHome.getDepositaryListByCampaign( CODE_CAMPAIGN + "123" );
        assertEquals( 0, depositaryList.size( ) );

        // Find test
        Depositary instanceStored = DepositaryHome.findByPrimaryKey( id );
        assertTrue( instanceStored != null );
        checkAssertsOnInstance( instanceStored, instance );

        // Delete test
        DepositaryHome.remove( instance.getId( ) );

        depositaryList = DepositaryHome.getDepositaryListByCampaign( CODE_CAMPAIGN );
        assertEquals( 0, depositaryList.size( ) );

        Collection<Integer> idListAfterDelete = DepositaryHome.getIdDepositariesList( );
        assertEquals( idList.size( ) - 1, idListAfterDelete.size( ) );
    }

    private void checkAssertsOnInstance( Depositary instanceStored, Depositary instance )
    {
        assertEquals( instanceStored.getId( ), instance.getId( ) );
        assertEquals( instanceStored.getCodeDepositaryType( ), instance.getCodeDepositaryType( ) );
        assertEquals( instanceStored.getCodeCampaign( ), instance.getCodeCampaign( ) );
    }
}
