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
package fr.paris.lutece.plugins.participatoryideation.util;

/**
 * This class provides constants.
 */
public final class Constants
{

    // Campaign phases
    public static final String PRE_IDEATION = "PRE_IDEATION";
    public static final String IDEATION = "IDEATION";
    public static final String POST_IDEATION = "POST_IDEATION";

    // Properties
    public static final String PROPERTY_WORKFLOW_ID = "participatoryideation.idWorkflow";

    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT = "participatoryideation.workFlowActionNameCreateComment";

    public static final String PROPERTY_WORKFLOW_ACTION_NAME_FOLLOW = "participatoryideation.workFlowActionNameFollow";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CANCEL_FOLLOW = "participatoryideation.workFlowActionNameCancelFollow";

    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CREATE_PROPOSAL = "participatoryideation.workFlowActionNameCreateProposal";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_DELETE_PROPOSAL = "participatoryideation.workFlowActionNameDeleteProposal";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_DELETE_PROPOSAL_BY_MDP = "participatoryideation.workFlowActionNameDeleteProposalByMdp";

    public static final String PROPERTY_GENERATE_PROPOSAL_DEPOSITAIRE_TYPE = "participatoryideation.atelier.generateProposal.depositaire_type";
    public static final String PROPERTY_GENERATE_PROPOSAL_DEPOSITAIRE = "participatoryideation.atelier.generateProposal.depositaire";
    public static final String PROPERTY_GENERATE_PROPOSAL_LUTECE_USER_NAME = "participatoryideation.atelier.generateProposal.luteceUserName";

    // Constructor
    private Constants( )
    {
        throw new UnsupportedOperationException( "This class must not be instanciated !" );
    }

}
