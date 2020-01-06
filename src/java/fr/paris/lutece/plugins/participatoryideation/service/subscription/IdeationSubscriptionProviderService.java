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
package fr.paris.lutece.plugins.participatoryideation.service.subscription;


import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.subscribe.business.Subscription;
import fr.paris.lutece.plugins.subscribe.business.SubscriptionFilter;
import fr.paris.lutece.plugins.subscribe.service.ISubscriptionProviderService;
import fr.paris.lutece.plugins.subscribe.service.SubscriptionService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.util.ReferenceList;


/**
 * IdeationSubscriptionProviderService
 */
public class IdeationSubscriptionProviderService implements ISubscriptionProviderService
{
    /**
     * Name of the bean of the DigglikeSubscriptionProviderService
     */
    public static final String BEAN_NAME = "participatoryideation.ideationSubscriptionProviderService";
    public static final String SUBSCRIPTION_NEW_COMMENT_ON_MY_PROPOSAL = "newCommentOnMyProposal";
    public static final String SUBSCRIPTION_NEW_COMMENT_ON_PARTICIPATE_PROPOSAL = "newCommentOnParticipateProposal";
    public static final String SUBSCRIPTION_NEW_STATE_ON_PARTICIPATE_PROPOSAL  = "newStateOnParticipateProposal";
    public static final String SUBSCRIPTION_NEW_STATE_ON_MY_PROPOSAL  = "newStateOnMyProposal";
    public static final String SUBSCRIPTION_NEW_PARTICIPATION_ON_MY_PROPOSAL  = "newParticipationOnMyProposal";
    public static final String SUBSCRIPTION_UPDATE_ON_REALIZATION  = "updateOnRealization";
    
    public static final String[] TAB_SUBSCRIPTIONS = new String[]
            {
    			SUBSCRIPTION_NEW_COMMENT_ON_MY_PROPOSAL, 
    			SUBSCRIPTION_NEW_COMMENT_ON_PARTICIPATE_PROPOSAL,
    			SUBSCRIPTION_NEW_STATE_ON_PARTICIPATE_PROPOSAL,
    			SUBSCRIPTION_NEW_STATE_ON_MY_PROPOSAL,
    			SUBSCRIPTION_NEW_PARTICIPATION_ON_MY_PROPOSAL,
    			SUBSCRIPTION_UPDATE_ON_REALIZATION
    		};
    
   
    
    private static final String SUBSCRIPTION_PROVIDER_NAME = "participatoryideation.subscriptionProviderName";
    private static final String MESSAGE_SUBSCRIBED_PREFIX= "participatoryideation.message.subscriptions.";
    private static final String ID_ALL = "*";
    private static IdeationSubscriptionProviderService _instance;
    private static ReferenceList _refListIdeationSubscription;
    
    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static IdeationSubscriptionProviderService getService(  )
    {
        if ( _instance == null )
        {
            synchronized ( IdeationSubscriptionProviderService.class )
            {
                _instance = SpringContextService.getBean( BEAN_NAME );
          
         	 }
        }

        return _instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProviderName(  )
    {
        return SUBSCRIPTION_PROVIDER_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSubscriptionHtmlDescription( LuteceUser user, String strSubscriptionKey,
        String strIdSubscribedResource, Locale locale )
    {
        
    	
    	   for (int i = 0; i < TAB_SUBSCRIPTIONS.length; i++) {
    		   if(TAB_SUBSCRIPTIONS[i].equals(strSubscriptionKey))
    				   {
    			   
    			   			return I18nService.getLocalizedString( MESSAGE_SUBSCRIBED_PREFIX+strSubscriptionKey, locale );
    				   }
    	  }

        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSubscriptionRemovable( LuteceUser user, String strSubscriptionKey, String strIdSubscribedResource )
    {

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlModifySubscription( LuteceUser user, String strSubscriptionKey, String strIdSubscribedResource )
    {
        //No subscription modification 
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifySubscriptionRemoval( Subscription subscription )
    {
        // Do nothing
    }

    /**
     * Remove a subscription of a user to a digg
     * @param user The user to remove the subscription of
     * @param nIdDigg The id of the digg
     */
    public void removeSubscription( LuteceUser user, String strSubscriptionKey )
    {
    	
    	
    	
    		SubscriptionFilter filter = new SubscriptionFilter( user.getName(  ), getProviderName(  ), strSubscriptionKey,
    				ID_ALL  );
    		List<Subscription> listSubscription = SubscriptionService.getInstance(  ).findByFilter( filter );
    		if ( ( listSubscription != null ) && ( listSubscription.size(  ) > 0 ) )
            {
                for ( Subscription subscription : listSubscription )
                {
                    SubscriptionService.getInstance(  ).removeSubscription( subscription, false );
                }
            }
    }

    
    

    /**
     * Create a new subscription to a digg
     * @param user The user to create a subscription of
     * @param nIdDigg The id of the digg to subscribe to
     */
    public void createSubscription( LuteceUser user, String strSubscriptionKey )
    {
        createSubscription(user.getName(), strSubscriptionKey);
    }
    
    /**
     * Create a new subscription to a digg
     * @param user The user to create a subscription of
     * @param nIdDigg The id of the digg to subscribe to
     */
    public void createSubscription( String strLuteceUserName, String strSubscriptionKey )
    {
        if ( !hasUserSubscribedToResource( strLuteceUserName, strSubscriptionKey ) )
        {
        	 Subscription subscription = new Subscription(  );
             subscription.setIdSubscribedResource( ID_ALL );
             subscription.setUserId( strLuteceUserName );
             subscription.setSubscriptionKey( strSubscriptionKey );
             subscription.setSubscriptionProvider( getProviderName(  ) );
             SubscriptionService.getInstance(  ).createSubscription( subscription );
        }
    }


   
    /**
     * Check if a user has subscribed to a resource
     * @param user The user
     * @param nId The id of the subscribed resource
     * @param strSubscriptionKey The subscription key
     * @return True if the user has subscribed to the resource, false otherwise
     */
    private boolean hasUserSubscribedToResource( String strLuteceUserName, String strSubscriptionKey )
    {
        SubscriptionFilter filter = new SubscriptionFilter( strLuteceUserName, getProviderName(  ), strSubscriptionKey,
                ID_ALL );
        List<Subscription> listSubscription = SubscriptionService.getInstance(  ).findByFilter( filter );

        if ( ( listSubscription != null ) && ( listSubscription.size(  ) > 0 ) )
        {
            return true;
        }

        return false;
    }

	@Override
	public String getSubscriptionHtmlDescriptionBis(LuteceUser user,
			String strSubscriptionKey, String strIdSubscribedResource,
			Locale locale, String userSub) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	public ReferenceList getRefListIdeationSubscription(Locale locale )
	{
		if(_refListIdeationSubscription==null)
		{
		  synchronized ( IdeationSubscriptionProviderService.class )
            {
			  	initRefListIdeationSubscription(locale);
            }
		}
		
		return _refListIdeationSubscription;
	}
	
	
	private void initRefListIdeationSubscription(Locale locale )
	{
		_refListIdeationSubscription=new ReferenceList();
		for (int i = 0; i < TAB_SUBSCRIPTIONS.length; i++) {
		_refListIdeationSubscription.addItem(TAB_SUBSCRIPTIONS[i], I18nService.getLocalizedString( MESSAGE_SUBSCRIBED_PREFIX+TAB_SUBSCRIPTIONS[i], locale ));
		}
	}
	
	 
}
