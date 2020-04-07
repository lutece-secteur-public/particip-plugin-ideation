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
package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryType;
import fr.paris.lutece.plugins.participatoryideation.business.depositary.DepositaryTypeHome;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignService;
import fr.paris.lutece.portal.service.cache.AbstractCacheableService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.workflow.WorkflowService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

public class IdeationStaticService extends AbstractCacheableService implements IIdeationStaticService
{

    private static IIdeationStaticService _singleton;
    private static final String BEAN_IDEATION_STATIC_SERVICE = "participatoryideation.ideationStaticService";

    private static final String SERVICE_NAME = "Ideation Static Cache";

    private static final String MARK_GLOBAL_STATIC = "global_static";
    private static final String MARK_CAMPAIGN_STATIC = "campaign_static";
    private static final String MARK_LIST_CAMPAIGN = "list_campaign";
    private static final String MARK_LIST_STATUS_STATIC = "status_static_list";

    private static final String MARK_CAMPAIGN = "campaign";

    private static final String MARK_AREA_LIST = "area_list";

    private static final String MARK_THEME_LIST = "theme_list";

    private static final String MARK_QPVQVA_LIST = "qpvqva_list";
    private static final String MARK_QPVQVA_MAP = "qpvqva_map";

    private static final String MARK_HANDICAP_LIST = "handicap_list";
    private static final String MARK_HANDICAP_MAP = "handicap_map";

    private static final String MARK_LOCATION_TYPE_LIST = "type_location_list";
    private static final String MARK_LOCATION_TYPE_MAP = "type_location_map";

    private static final String MARK_DEPOSITARIES_TYPES_LIST = "depositary_types_list";
    private static final String MARK_DEPOSITARIES_TYPES_MAP = "depositaries_types_map";
    private static final String MARK_DEPOSITARIES_TYPES_LIST_VALUES_MAP = "depositaries_types_list_values_map";

    public static final String CACHE_KEY = "[ideationStatic]";

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    public static IIdeationStaticService getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATION_STATIC_SERVICE );
        }
        return _singleton;
    }

    public IdeationStaticService( )
    {
        initCache( );
    }

    public String getName( )
    {
        return SERVICE_NAME;
    }

    // *********************************************************************************************
    // * BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CA *
    // * BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CAMPAIGN BY_CA *
    // *********************************************************************************************

    /**
     * Returns static content for a specific campaign.
     */
    public void fillCampaignStaticContent( Map<String, Object> model, String strCampaignCode )
    {
        // Add global static data
        model.put( MARK_QPVQVA_LIST, ProposalService.getInstance( ).getQpvQvaCodesList( ) );
        model.put( MARK_QPVQVA_MAP, ProposalService.getInstance( ).getQpvQvaCodesMap( ) );
        model.put( MARK_HANDICAP_LIST, ProposalService.getInstance( ).getHandicapCodesList( ) );
        model.put( MARK_HANDICAP_MAP, ProposalService.getInstance( ).getHandicapCodesMap( ) );
        model.put( MARK_LOCATION_TYPE_LIST, ProposalService.getInstance( ).getTypeLocationList( ) );
        model.put( MARK_LOCATION_TYPE_MAP, ProposalService.getInstance( ).getTypeLocationMap( ) );

        // Add list of campaigns
        model.put( MARK_LIST_CAMPAIGN, IdeationCampaignService.getInstance( ).getCampaigns( ) );

        // Add list of proposal status
        if ( WorkflowService.getInstance( ).isAvailable( ) )
        {
            List<Proposal.Status> enumList = Arrays.asList( Proposal.Status.values( ) );
            ReferenceList WorkflowStatesReferenceList = new ReferenceList( );
            for ( Proposal.Status status : enumList )
            {
                if ( status.isPublished( ) )
                {
                    WorkflowStatesReferenceList.addItem( status.getValeur( ),
                            I18nService.getLocalizedString( status.getLibelle( ), new Locale( "fr", "FR" ) ) );
                }
            }
            model.put( MARK_LIST_STATUS_STATIC, WorkflowStatesReferenceList );
        }

        // Add static data of the specified campaign
        @SuppressWarnings( "unchecked" )
        Map<String, Object> cached = (Map<String, Object>) getFromCache( CACHE_KEY );
        if ( cached == null )
        {
            cached = putAllStaticContentInCache( );
        }
        model.put( MARK_CAMPAIGN_STATIC, cached.get( strCampaignCode ) );
    }

    // *********************************************************************************************
    // * BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_AL *
    // * BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_ALL BY_AL *
    // *********************************************************************************************

    /**
     * Returns static content for all campaigns
     */
    public void fillAllStaticContent( Map<String, Object> model )
    {
        // Add global static data
        model.put( MARK_QPVQVA_LIST, ProposalService.getInstance( ).getQpvQvaCodesList( ) );
        model.put( MARK_QPVQVA_MAP, ProposalService.getInstance( ).getQpvQvaCodesMap( ) );
        model.put( MARK_HANDICAP_LIST, ProposalService.getInstance( ).getHandicapCodesList( ) );
        model.put( MARK_HANDICAP_MAP, ProposalService.getInstance( ).getHandicapCodesMap( ) );
        model.put( MARK_LOCATION_TYPE_LIST, ProposalService.getInstance( ).getTypeLocationList( ) );
        model.put( MARK_LOCATION_TYPE_MAP, ProposalService.getInstance( ).getTypeLocationMap( ) );

        // Add static data of all campaigns
        @SuppressWarnings( "unchecked" )
        Map<String, Object> cached = (Map<String, Object>) getFromCache( CACHE_KEY );
        if ( cached == null )
        {
            cached = putAllStaticContentInCache( );
        }
        model.put( MARK_GLOBAL_STATIC, cached );

    }

    /**
     * Returns a map with data of all campaigns :
     * 
     * - content.Key = campaign code
     * 
     * - content.Value = a map with data of the campaign : - campaignContent.key = data name (campaign, themes, areas, submitter types, submitter types values)
     * - campaignContent.value = data values
     */
    private Map<String, Object> putAllStaticContentInCache( )
    {
        Map<String, Object> content = new HashMap<String, Object>( );

        // For each campaign, add data about submitters
        Map<String, List<DepositaryType>> mapDepositariesTypes = DepositaryTypeHome.getDepositaryTypesMapByCampaign( );
        ReferenceList listCampaign = IdeationCampaignService.getInstance( ).getCampaigns( );
        for ( ReferenceItem campaign : listCampaign )
        {
            Map<String, Object> campaignContent = new HashMap<String, Object>( );

            // Data about campaign
            campaignContent.put( MARK_CAMPAIGN, campaign );

            // Add themes of the campaign
            campaignContent.put( MARK_THEME_LIST, IdeationCampaignService.getInstance( ).getCampaignThemes( campaign.getCode( ) ) );

            // Add areas of the campaign
            campaignContent.put( MARK_AREA_LIST, IdeationCampaignService.getInstance( ).getCampaignAllAreas( campaign.getCode( ) ) );

            // Types of submitter of the campaign
            campaignContent.put( MARK_DEPOSITARIES_TYPES_LIST, mapDepositariesTypes.get( campaign.getCode( ) ) );
            Map<String, DepositaryType> mapDepositariesTypesByCode = new HashMap<String, DepositaryType>( );
            for ( DepositaryType depositaryType : mapDepositariesTypes.get( campaign.getCode( ) ) )
            {
                mapDepositariesTypesByCode.put( depositaryType.getCode( ), depositaryType );
            }
            campaignContent.put( MARK_DEPOSITARIES_TYPES_MAP, mapDepositariesTypesByCode );

            // Values of list-typed submitters of the campaign
            Map<String, String> mapDepositariesTypesListValuesByCode = new HashMap<String, String>( );
            for ( DepositaryType depositaryType : mapDepositariesTypes.get( campaign.getCode( ) ) )
            {
                if ( DepositaryType.CODE_COMPLEMENT_TYPE_LIST.equals( depositaryType.getCodeComplementType( ) ) )
                {
                    for ( ReferenceItem referenceItem : depositaryType.getValues( ) )
                    {
                        mapDepositariesTypesListValuesByCode.put( depositaryType.getCode( ) + "-" + referenceItem.getCode( ), referenceItem.getName( ) );
                    }
                }
            }
            campaignContent.put( MARK_DEPOSITARIES_TYPES_LIST_VALUES_MAP, mapDepositariesTypesListValuesByCode );

            content.put( campaign.getCode( ), campaignContent );
        }

        putInCache( CACHE_KEY, content );

        return content;
    }

}
