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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal.Status;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.test.LuteceTestCase;

/**
 * DepositaryTypeTest
 */
public class ProposalHomeTest extends LuteceTestCase
{
    public final static String MOCK_CODE_CAMPAIGN = "mockCodeCampaign";
    public final static int MOCK_CODE_PROPOSAL = 1;
    public final static String MOCK_TITLE = "mockTitle";
    public final static String MOCK_DESCRIPTION = "mockDescription";
    public final static String MOCK_LUTECE_USER_NAME = "mockLuteceUserName";
    public final static String MOCK_CODE_THEME = "mockCodeTheme";
    public final static String MOCK_LOCALISATION_TYPE = "mockLocalisationType";
    public final static String MOCK_DEPOSITARY_TYPE = "mockDepositaryType";
    public final static boolean MOCK_ACCEPT_EXPLOIT = true;
    public final static String MOCK_TYPE_QPV_QVA = "mockTypeQpvQva";
    public final static Timestamp MOCK_CREATION_TIMESTAMP = new Timestamp( System.currentTimeMillis( ) );
    public final static boolean MOCK_ACCEPT_CONTACT = false;
    public final static String MOCK_HANDICAP = "yes";
    public final static String MOCK_HANDICAP_COMPLEMENT = "mockHandicapComplement";
    public final static Status MOCK_STATUS_PUBLIC = Status.STATUS_A_ETUDE;
    public final static Status MOCK_STATUS_EUDONET = Status.STATUS_EN_CO_CONSTRUCTION;
    public final static List<File> MOCK_IMGS = new ArrayList<>( );
    public final static List<File> MOCK_DOCS = new ArrayList<>( );

    // *********************************************************************************************
    // * TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST *
    // * TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST TEST *
    // *********************************************************************************************

    public void testBusiness( )
    {

    }

    // *********************************************************************************************
    // * MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK *
    // * MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK MOCK *
    // *********************************************************************************************

    public static Proposal getMockProposalInstance( )
    {
        Proposal proposal = new Proposal( );

        proposal.setCodeCampaign( MOCK_CODE_CAMPAIGN );
        proposal.setCodeProposal( MOCK_CODE_PROPOSAL );
        proposal.setTitre( MOCK_TITLE );
        proposal.setDescription( MOCK_DESCRIPTION );
        proposal.setLuteceUserName( MOCK_LUTECE_USER_NAME );
        proposal.setCodeTheme( MOCK_CODE_THEME );
        proposal.setLocalisationType( MOCK_LOCALISATION_TYPE );
        proposal.setDepositaryType( MOCK_DEPOSITARY_TYPE );
        proposal.setAcceptExploit( MOCK_ACCEPT_EXPLOIT );
        proposal.setTypeQpvQva( MOCK_TYPE_QPV_QVA );
        proposal.setCreationTimestamp( MOCK_CREATION_TIMESTAMP );
        proposal.setAcceptContact( MOCK_ACCEPT_CONTACT );
        proposal.setHandicap( MOCK_HANDICAP );
        proposal.setHandicapComplement( MOCK_HANDICAP_COMPLEMENT );
        proposal.setStatusPublic( MOCK_STATUS_PUBLIC );
        proposal.setStatusEudonet( MOCK_STATUS_EUDONET );
        proposal.setImgs( MOCK_IMGS );
        proposal.setDocs( MOCK_DOCS );

        return proposal;
    }

}
