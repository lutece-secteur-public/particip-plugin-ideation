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
package fr.paris.lutece.plugins.ideation.utils.constants;

import fr.paris.lutece.portal.service.util.AppPropertiesService;

/**
 *
 * IdeationConstants
 *
 */
public final class IdeationConstants
{

    // Properties
    public static final String PROPERTY_WORKFLOW_ID = "ideation.idWorkflow";
    public static final String PROPERTY_WORKFLOW_ID_ATELIER = "ideation.idWorkflowAtelier";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT = "ideation.workFlowActionNameCreateComment";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT_ATELIER = "ideation.workFlowActionNameCreateCommentAtelier";
    
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_FOLLOW = "ideation.workFlowActionNameFollow";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CANCEL_FOLLOW = "ideation.workFlowActionNameCancelFollow";
    
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_CREATE_IDEE = "ideation.workFlowActionNameCreateIdee";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_DELETE_IDEE = "ideation.workFlowActionNameDeleteIdee";
    public static final String PROPERTY_WORKFLOW_ACTION_NAME_DELETE_IDEE_BY_MDP = "ideation.workFlowActionNameDeleteIdeeByMdp";
    
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_1= "ideation.workFlowAtelierStateEnAttentePhase1";
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_1= "ideation.workFlowAtelierStatePhase1";
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_2= "ideation.workFlowAtelierStateEnAttentePhase2";
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_2= "ideation.workFlowAtelierStatePhase2";
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_3= "ideation.workFlowAtelierStateEnAttentePhase3";
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_3= "ideation.workFlowAtelierStatePhase3";
    public static final String PROPERTY_WORKFLOW_ATELIER_STATE_NAME_CLOS= "ideation.workFlowAtelierStateClos";

    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_1= "ideation.workFlowAtelierActionDebutPhase1";
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_2= "ideation.workFlowAtelierActionDebutPhase2";
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_3= "ideation.workFlowAtelierActionDebutPhase3";
    
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_1= "ideation.workFlowAtelierActionFinPhase1";
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_2= "ideation.workFlowAtelierActionFinPhase2";
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_3= "ideation.workFlowAtelierActionFinPhase3";
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_3_ATELIER_PHYSIQUE= "ideation.workFlowAtelierActionDebutPhase3AtelierPhysique";
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_3_ATELIER_PHYSIQUE= "ideation.workFlowAtelierActionFinPhase3AtelierPhysique";
    
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_RAPPEL_FIN_PHASE_1= "ideation.workFlowAtelierActionRappelFinPhase1"; 
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_RAPPEL_FIN_PHASE_2= "ideation.workFlowAtelierActionRappelFinPhase2"; 
    public static final String PROPERTY_WORKFLOW_ATELIER_ACTION_GENERATION_IDEE = "ideation.workFlowAtelierActionGenerationIdee"; 

    public static final String PROPERTY_GENERATE_IDEE_DEPOSITAIRE_TYPE = "ideation.atelier.generateIdee.depositaire_type";
    public static final String PROPERTY_GENERATE_IDEE_DEPOSITAIRE = "ideation.atelier.generateIdee.depositaire";
    public static final String PROPERTY_GENERATE_IDEE_LUTECE_USER_NAME = "ideation.atelier.generateIdee.luteceUserName";
      

    public static final String ATELIER_TYPE_NUMERIQUE="numerique";
    public static final String ATELIER_TYPE_PHYSIQUE="physique";

    /**
     * Private constructor
     */
    private IdeationConstants(  )
    {
    }
}