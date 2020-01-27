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

import fr.paris.lutece.test.LuteceTestCase;

/**
 * DepositaireTypeTest
 */
public class CampagneDepositaireHomeTest extends LuteceTestCase
{
    private final static String CODE_CAMPAIGN = "XYZ";
    private final static String CODE_DEPOSITAIRE_TYPE = "ABCD1234";

    public void testBusiness( )
    {
        // Initialize an object
        CampagneDepositaire instance = new CampagneDepositaire( );
        instance.setCodeCampagne( CODE_CAMPAIGN );
        instance.setCodeDepositaireType( CODE_DEPOSITAIRE_TYPE );

        // Create test
        CampagneDepositaireHome.create( instance );
        int id = instance.getId( );

        // List test
        Collection<Integer> idList = CampagneDepositaireHome.getIdCampagneDepositairesList( );
        assertTrue( idList.size( ) > 0 );
        boolean campagneDepositaireFound = false;
        for ( Integer integer : idList )
        {
            campagneDepositaireFound = ( campagneDepositaireFound || ( integer == id ) );
        }
        assertTrue( campagneDepositaireFound );

        // List by campaign test
        Collection<CampagneDepositaire> campagneDepositaireList = CampagneDepositaireHome.getCampagneDepositaireListByCampagne( CODE_CAMPAIGN );

        assertEquals( 1, campagneDepositaireList.size( ) );

        CampagneDepositaire [ ] campagneDepositaireArray = new CampagneDepositaire [ 1];
        campagneDepositaireList.toArray( campagneDepositaireArray );
        checkAssertsOnInstance( campagneDepositaireArray [0], instance );

        // List by campaign test
        campagneDepositaireList = CampagneDepositaireHome.getCampagneDepositaireListByCampagne( CODE_CAMPAIGN + "123" );
        assertEquals( 0, campagneDepositaireList.size( ) );

        // Find test
        CampagneDepositaire instanceStored = CampagneDepositaireHome.findByPrimaryKey( id );
        assertTrue( instanceStored != null );
        checkAssertsOnInstance( instanceStored, instance );

        // Delete test
        CampagneDepositaireHome.remove( instance.getId( ) );

        campagneDepositaireList = CampagneDepositaireHome.getCampagneDepositaireListByCampagne( CODE_CAMPAIGN );
        assertEquals( 0, campagneDepositaireList.size( ) );

        Collection<Integer> idListAfterDelete = CampagneDepositaireHome.getIdCampagneDepositairesList( );
        assertEquals( idList.size( ) - 1, idListAfterDelete.size( ) );
    }

    private void checkAssertsOnInstance( CampagneDepositaire instanceStored, CampagneDepositaire instance )
    {
        assertEquals( instanceStored.getId( ), instance.getId( ) );
        assertEquals( instanceStored.getCodeDepositaireType( ), instance.getCodeDepositaireType( ) );
        assertEquals( instanceStored.getCodeCampagne( ), instance.getCodeCampagne( ) );
    }
}
