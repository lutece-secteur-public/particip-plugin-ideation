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

import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryType;
import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryTypeHome;
import fr.paris.lutece.test.LuteceTestCase;
import fr.paris.lutece.util.ReferenceList;

/**
 * DepositaryTypeTest
 */
public class DepositaryTypeHomeTest extends LuteceTestCase
{
    private final static String CODE = "0";
    private final static String LIBELLE = "Libelle";
    private final static String CODE_COMPLEMENT = "FREE";
    private final static ReferenceList REFERENCE_LIST = new ReferenceList( );

    public void testBusiness( )
    {
        REFERENCE_LIST.addItem( "123", "abc" );

        // Create an object
        DepositaryType instance = new DepositaryType( );
        instance.setCode( CODE );
        instance.setLibelle( LIBELLE );
        instance.setCodeComplementType( CODE_COMPLEMENT );
        instance.setValues( REFERENCE_LIST );
        DepositaryTypeHome.create( instance );

        // Test findByCode method
        DepositaryType storedInstance = DepositaryTypeHome.findByCode( CODE );
        assertTrue( instanceEquals( instance, storedInstance ) );

        // Test findBy method
        storedInstance = DepositaryTypeHome.findByPrimaryKey( instance.getId( ) );
        assertTrue( instanceEquals( instance, storedInstance ) );
        assertNull( storedInstance.getValues( ) );

        // List test
        DepositaryTypeHome.getDepositaryTypesList( );

        // Delete test
        DepositaryTypeHome.remove( instance.getId( ) );

    }

    private boolean instanceEquals( DepositaryType instance1, DepositaryType instance2 )
    {
        if ( !instance1.getCode( ).equals( instance2.getCode( ) ) )
        {
            return false;
        }

        if ( !instance1.getLibelle( ).equals( instance2.getLibelle( ) ) )
        {
            return false;
        }

        if ( !instance1.getCodeComplementType( ).equals( instance2.getCodeComplementType( ) ) )
        {
            return false;
        }

        return true;
    }
}
