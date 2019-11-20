/*
 * Copyright (c) 2002-2016, Mairie de Paris
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.workflowcore.business.state.State;
import fr.paris.lutece.portal.service.daemon.Daemon;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


/**
 * WorkflowAtelierDaemon daemon 
 */
public class WorkflowAtelierDaemon extends Daemon
{
	//Marks
	private static final String MARK_FORMAT_DATE = "dd/MM/yyyy";
	private static final String MARK_HAS_NOTIFIED = "hasNotified";
	private static final String MARK_RAPPEL_ATELIER_PHASE1 = "rappel_atelier_phase1_";
	private static final String MARK_RAPPEL_ATELIER_PHASE2 = "rappel_atelier_phase2_";
   
	/**
     * Default constructor
     */
    public WorkflowAtelierDaemon(  )
    {
        super(  );
        setPluginName( IdeationPlugin.PLUGIN_NAME );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run(  )
    {
        StringBuilder sbLogs = new StringBuilder();

        
        Date currentDate =  new java.util.Date(  );
        currentDate = DateUtils.truncate( currentDate, Calendar.DATE );
        
        StringBuilder sbResult = new StringBuilder(  );
        String rappel_atelier_phase1_key = StringUtils.EMPTY , rappel_atelier_phase2_key = StringUtils.EMPTY, 
        		rappel_atelier_phase1_value= StringUtils.EMPTY, rappel_atelier_phase2_value = StringUtils.EMPTY ;
        
        for( Atelier atelier:AtelierHome.getAteliersList( ) )
        {
			rappel_atelier_phase1_key = MARK_RAPPEL_ATELIER_PHASE1 + atelier.getId( ) ; 
			rappel_atelier_phase2_key = MARK_RAPPEL_ATELIER_PHASE2 + atelier.getId( ) ;
			rappel_atelier_phase1_value= DatastoreService.getDataValue( rappel_atelier_phase1_key , StringUtils.EMPTY );
			rappel_atelier_phase2_value= DatastoreService.getDataValue( rappel_atelier_phase2_key , StringUtils.EMPTY );
        	
        	if(atelier.getType().equals(IdeationConstants.ATELIER_TYPE_NUMERIQUE))
        	{
        		State state= AtelierWSService.getInstance().getAtelierState(atelier.getId());
        	
        		if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_1))&&( atelier.getDateDebutPhase1().equals(currentDate)||atelier.getDateDebutPhase1().before(currentDate)))
        		{
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_1,atelier,sbLogs,state.getName());
        					
        		
        		}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_1)) &&  atelier.getJoursDeRappelPhase1( )!=0 && ( differenceDates( atelier.getDateFinPhase1( ) ) == atelier.getJoursDeRappelPhase1( ) ) && rappel_atelier_phase1_value.equals( StringUtils.EMPTY ) )
        		{
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_RAPPEL_FIN_PHASE_1, atelier,sbLogs,state.getName( ) );		
        			DatastoreService.setDataValue( rappel_atelier_phase1_key , MARK_HAS_NOTIFIED );
        		}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_1))&& atelier.getDateFinPhase1().before(currentDate))
        		{	
        		
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_1,atelier,sbLogs,state.getName());
							
				}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_2))&&(atelier.getDateDebutPhase2().equals(currentDate)|| atelier.getDateDebutPhase2().before(currentDate)))
        		{
        		
        		   runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_2,atelier,sbLogs,state.getName());
   					
        		}
        		else if( state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_2)) && atelier.getJoursDeRappelPhase2( )!=0 && ( differenceDates( atelier.getDateFinPhase2( ) ) == atelier.getJoursDeRappelPhase2( ) ) && rappel_atelier_phase2_value.equals( StringUtils.EMPTY ) )
        		{
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_RAPPEL_FIN_PHASE_2, atelier,sbLogs,state.getName( ) );		
        			DatastoreService.setDataValue( rappel_atelier_phase2_key , MARK_HAS_NOTIFIED );
        		}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_2))&& atelier.getDateFinPhase2().before(currentDate))
        		{	
        			 runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_2,atelier,sbLogs,state.getName());
				}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_3))&& ( atelier.getDateDebutPhase3().equals(currentDate)|| atelier.getDateDebutPhase3().before(currentDate)))
        		{
        		
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_3,atelier,sbLogs,state.getName());		
        		}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_3))&& atelier.getDateFinPhase3().before(currentDate))
        		{	
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_3,atelier,sbLogs,state.getName());		
				}
        		
        	}
        	else
        	{
        		
        		//Traitement des ateliers physiques(Passage en attente  Phase 1  -> debut phase 3 )
        		State state= AtelierWSService.getInstance().getAtelierState(atelier.getId());
        		if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_EN_ATTENTE_PHASE_1)) &&( atelier.getDateDebutPhase3().equals(currentDate)||atelier.getDateDebutPhase3().before(currentDate)))
        		{
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_DEBUT_PHASE_3_ATELIER_PHYSIQUE,atelier,sbLogs,state.getName());
        					
        		}
        		else if(state.getName().equals(AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_STATE_NAME_PHASE_3))&& atelier.getDateFinPhase3().before(currentDate))
        		{	
        			runWorkflowAction(IdeationConstants.PROPERTY_WORKFLOW_ATELIER_ACTION_FIN_PHASE_3_ATELIER_PHYSIQUE,atelier,sbLogs,state.getName());		
				}
        		
        		
        	}
        	
        }	
        	
        setLastRunLogs( sbResult.toString(  ) );
       }
        
	    private static long differenceDates( Date date )
	    {
	        Date dateToday = new Date( );
	        SimpleDateFormat sdf = new SimpleDateFormat( MARK_FORMAT_DATE );
	        try
	        {
	            dateToday = sdf.parse( sdf.format( dateToday ) );
	        }
	        catch ( ParseException ex )
	        {
	            ex.printStackTrace( );
	        }
	        long diff = date.getTime( ) - dateToday.getTime( );
	
	        return ( diff / ( 1000 * 60 * 60 * 24 ) );
	    }
       private void runWorkflowAction(String strPropertyActionName,Atelier atelier, StringBuilder sbLogs,String strState)
       {
    	  String strAction=AppPropertiesService.getProperty(strPropertyActionName);
		  AtelierWSService.getInstance().processActionByName(strAction, atelier.getId());
		  sbLogs.append("--Nouvelle transition de workflow pour l'Atelier--");
		  sbLogs.append("--Titre:");
		  sbLogs.append(atelier.getTitre());
		  sbLogs.append("--id:");
		  sbLogs.append(atelier.getId());
		  sbLogs.append("--Etat:");
		  sbLogs.append(strState);
		  sbLogs.append("--Action::");
		  sbLogs.append(strAction);
		  sbLogs.append( "\n" );
	  	     	
       }
       
}
