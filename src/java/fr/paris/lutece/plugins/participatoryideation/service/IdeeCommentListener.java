/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.extend.modules.comment.service.ICommentListener;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignService;
import fr.paris.lutece.plugins.participatoryideation.util.Constants;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.util.AppPropertiesService;


public class IdeeCommentListener implements ICommentListener{

	 /**
     * {@inheritDoc}
     */
	private static final String MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT = "participatoryideation.messages.campagne.ideation.closed.comment";
	private static final String PROPERTY_ACTIVATION_COMMENTAIRES= "participatoryideation.site_property.form.forcer_activation_commentaires" ;

    @Override
	public void createComment(String strIdExtendableResource, boolean bPublished) {
	
    	
    	 Idee idee= IdeeHome.findByPrimaryKey(Integer.parseInt(strIdExtendableResource));
    	 if(idee.getExportedTag( ) != 0){
    		 idee.setExportedTag(2);
    		 IdeeHome.updateBO(idee);
    	 }
    	// String strWorkflowActionNameCreateComment=AppPropertiesService.getProperty(Constants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT);
    	// IdeeWSService.getInstance().processActionByName(strWorkflowActionNameCreateComment, Integer.parseInt(strIdExtendableResource) );
    	 
    	
    
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
	public void createComment(String strIdExtendableResource, boolean bPublished, HttpServletRequest request) {
	
    	
    	 Idee idee= IdeeHome.findByPrimaryKey(Integer.parseInt(strIdExtendableResource));
    	 
    	 if(idee.getExportedTag( ) != 0){
    		 idee.setExportedTag(2);
    		 IdeeHome.updateBO(idee);
    	 }
    	 
    	 String strWorkflowActionNameCreateComment=AppPropertiesService.getProperty(Constants.PROPERTY_WORKFLOW_ACTION_NAME_CREATE_COMMENT);
    	 IdeeWSService.getInstance().processActionByName(strWorkflowActionNameCreateComment, Integer.parseInt(strIdExtendableResource), request );
    	 
    	
    
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void publishComment( String strIdExtendableResource, boolean bPublished ) 
    {
		// Nothing	
	}
    
    /**
     * {@inheritDoc}
     */
	@Override
	public String checkComment( String comment, String uidUser )
	{
		StringBuilder sbError           = new StringBuilder( );
		String        strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ACTIVATION_COMMENTAIRES, "0" );
		
		if ( !IdeationCampaignService.getInstance().isDuring( Constants.IDEATION )  && strDataStoreValue.equals("0") )
		{			
			sbError.append( I18nService.getLocalizedString( MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT, new Locale("fr","FR") ) );
			sbError.append( ", " );
		}
		else
		{
			// Should check here.
		}
		
		// Remove last 
    	if ( sbError.length() != 0 )
    	{
    		sbError.setLength( sbError.length(  ) - 2 );
    	}
    	
		return sbError.toString( );
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void deleteComment(String arg0, List arg1) {
		// TODO Auto-generated method stub
		
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public String checkComment( String comment, String uidUser, String strResourceType, String strResourceId )
    {
        StringBuilder sbError           = new StringBuilder( );
        int           nId_Idee          = Integer.parseInt( strResourceId );
        Idee          idee              = IdeeHome.findByPrimaryKey( nId_Idee );
        String        strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ACTIVATION_COMMENTAIRES, "0");
        
        if ( idee != null && !IdeationCampaignService.getInstance().isDuring( idee.getCodeCampagne(), Constants.IDEATION ) && strDataStoreValue.equals( "0" ) )
        {
            sbError.append( I18nService.getLocalizedString( MESSAGE_CAMPAGNE_IDEATION_CLOSED_COMMENT, new Locale( "fr", "FR" ) ) );
            sbError.append( ", " );
        }
        else
        {
			// Should check here.
        }
        // remove last ,
        if ( sbError.length( ) != 0 )
        {
            sbError.setLength( sbError.length( ) - 2 );
        }
        return sbError.toString( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canComment( LuteceUser user, String strIdExtendableResource, String strExtendableResourceType )
    {
        int    nIdIdee           = Integer.parseInt( strIdExtendableResource );
        Idee   idee              = IdeeHome.findByPrimaryKey( nIdIdee );
        String strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ACTIVATION_COMMENTAIRES, "0");
        
        if ( idee != null && !IdeationCampaignService.getInstance().isDuring( idee.getCodeCampagne(), Constants.IDEATION ) && strDataStoreValue.equals( "0" ) )
        {
            return false;
        }

        return true;
    }
}
