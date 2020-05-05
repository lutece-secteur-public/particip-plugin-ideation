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
package fr.paris.lutece.plugins.participatoryideation.service.campaign;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.business.submitter.SubmitterType;
import fr.paris.lutece.plugins.participatoryideation.business.submitter.SubmitterTypeHome;
import fr.paris.lutece.plugins.participatoryideation.util.ParticipatoryIdeationConstants;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * This class provides ideation campaign default data. Campaign phases are considered as opened.
 *
 * TODO : some data should be configurable in a simple way in BO (for example, string with list of values separated by comma...)
 */
public class IdeationCampaignDataProvider implements IIdeationCampaignDataProvider
{

    private static String PROPERTY_PREFIX = "participatoryideation.campaign.";
    private static String PROPERTY_SEP = ";";

    // Campaigns
    private static ReferenceList campaigns = null;

    // Themes
    // Map :
    // Key = campaign code
    // Value = Reference list with code and label
    private static Map<String, ReferenceList> themeLabels = null;

    // Themes
    // Map :
    // Key = campaign code
    // Value = Reference list with code and front color rgb
    private static Map<String, ReferenceList> themeFrontRgbs = null;

    // Areas
    // Map :
    // Key = campaign code
    // Value = Reference list with code and label
    private static Map<String, ReferenceList> areaLabels = null;

    // Area types
    // Map :
    // Key = campaign code
    // Value = Reference list with code and type (whole / localized)
    private static Map<String, ReferenceList> areaTypes = null;

    // Phase date
    // Map :
    // Key = campaign code
    // Value = Array with two values : begin datetime, end datetime
    private static Map<String, Timestamp [ ]> dates = null;

    //
    // Areas Map :
    // Key = campaign code
    // Value = Map :
    // Key = field code
    // Value = data
    //
    private static Map<String, Map<String, String [ ]>> fields = null;

    // *********************************************************************************************
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // * SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON SINGLETON *
    // *********************************************************************************************

    private static final String BEAN_IDEATIONCAMPAIGN_DATA_PROVIDER = "participatoryideation.ideationCampaignDataProvider";

    private static IIdeationCampaignDataProvider _singleton;

    public static IIdeationCampaignDataProvider getInstance( )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATIONCAMPAIGN_DATA_PROVIDER );
            loadProperties( );
        }
        return _singleton;
    }

    // *********************************************************************************************
    // * CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN *
    // * CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN CAMPAIGN *
    // *********************************************************************************************

    @Override
    public ReferenceList getCampaigns( )
    {
        return campaigns;
    }

    @Override
    public ReferenceItem getLastCampaign( )
    {
        return getCampaigns( ).stream( ).sorted( new Comparator<ReferenceItem>( )
        {
            @Override
            public int compare( ReferenceItem c1, ReferenceItem c2 )
            {
                return c2.getCode( ).compareTo( c1.getCode( ) );
            }
        } ).findFirst( ).get( );
    }

    // *********************************************************************************************
    // * PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASE *
    // * PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASES PHASE *
    // *********************************************************************************************

    @Override
    public boolean isBeforeBeginning( String codeCampaign, String phase )
    {
        if ( ParticipatoryIdeationConstants.IDEATION.equals( phase ) )
        {
            return new Date( ).before( dates.get( codeCampaign ) [0] );
        }
        AppLogService.error( "Unexpected phase code '" + phase + "'." );
        return false;
    }

    @Override
    public boolean isAfterBeginning( String codeCampaign, String phase )
    {
        if ( ParticipatoryIdeationConstants.IDEATION.equals( phase ) )
        {
            return new Date( ).after( dates.get( codeCampaign ) [0] );
        }
        AppLogService.error( "Unexpected phase code '" + phase + "'." );
        return false;
    }

    @Override
    public boolean isDuring( String codeCampaign, String phase )
    {
        return isAfterBeginning( codeCampaign, phase ) && isBeforeEnd( codeCampaign, phase );
    }

    @Override
    public boolean isBeforeEnd( String codeCampaign, String phase )
    {
        if ( ParticipatoryIdeationConstants.IDEATION.equals( phase ) )
        {
            return new Date( ).before( dates.get( codeCampaign ) [1] );
        }
        AppLogService.error( "Unexpected phase code '" + phase + "'." );
        return false;
    }

    @Override
    public boolean isAfterEnd( String codeCampaign, String phase )
    {
        if ( ParticipatoryIdeationConstants.IDEATION.equals( phase ) )
        {
            return new Date( ).after( dates.get( codeCampaign ) [1] );
        }
        AppLogService.error( "Unexpected phase code '" + phase + "'." );
        return false;
    }

    @Override
    public boolean isBeforeBeginning( String phase )
    {
        return isBeforeBeginning( getLastCampaign( ).getCode( ), phase );
    }

    @Override
    public boolean isAfterBeginning( String phase )
    {
        return isAfterBeginning( getLastCampaign( ).getCode( ), phase );
    }

    @Override
    public boolean isDuring( String phase )
    {
        return isDuring( getLastCampaign( ).getCode( ), phase );
    }

    @Override
    public boolean isBeforeEnd( String phase )
    {
        return isBeforeEnd( getLastCampaign( ).getCode( ), phase );
    }

    @Override
    public boolean isAfterEnd( String phase )
    {
        return isAfterEnd( getLastCampaign( ).getCode( ), phase );
    }

    // *********************************************************************************************
    // * AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA *
    // * AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA AREA *
    // *********************************************************************************************

    @Override
    public ReferenceItem getCampaignWholeAreaLabel( String codeCampaign )
    {
        // Assuming there should be only one whole-typed item
        return areaLabels.get( codeCampaign ).stream( ).filter( r -> Proposal.LOCATION_AREA_TYPE_WHOLE.contentEquals( r.getCode( ) ) ).findFirst( ).get( );
    }

    @Override
    public ReferenceList getCampaignLocalizedAreaLabels( String codeCampaign )
    {
        ReferenceList result = new ReferenceList( );
        for ( ReferenceItem item : areaLabels.get( codeCampaign ) )
        {
            if ( !Proposal.LOCATION_AREA_TYPE_WHOLE.equals( item.getCode( ) ) )
            {
                result.add( item );
            }
        }
        return result;
    }

    @Override
    public int getCampaignNumberLocalizedAreas( String codeCampaign )
    {
        return getCampaignLocalizedAreaLabels( codeCampaign ).size( );
    }

    @Override
    public ReferenceItem getLastCampaignWholeAreaLabel( )
    {
        return areaLabels.get( getLastCampaign( ).getCode( ) ).stream( ).filter( r -> Proposal.LOCATION_AREA_TYPE_WHOLE.contentEquals( r.getName( ) ) )
                .findFirst( ).get( );
    }

    @Override
    public ReferenceList getLastCampaignLocalizedAreaLabels( )
    {
        return getCampaignLocalizedAreaLabels( getLastCampaign( ).getCode( ) );
    }

    @Override
    public int getLastCampaignNumberLocalizedAreas( )
    {
        return getLastCampaignLocalizedAreaLabels( ).size( );
    }

    @Override
    public ReferenceList getCampaignAllAreaLabels( String codeCampaign )
    {
        ReferenceList result = new ReferenceList( );
        for ( ReferenceItem item : areaLabels.get( codeCampaign ) )
        {
            if ( Proposal.LOCATION_AREA_TYPE_LOCALIZED.equals( item.getName( ) ) )
            {
                result.add( item );
            }
        }
        return result;
    }

    @Override
    public ReferenceList getCampaignAllAreaTypes( String codeCampaign )
    {
        ReferenceList result = new ReferenceList( );
        for ( ReferenceItem item : areaTypes.get( codeCampaign ) )
        {
            if ( Proposal.LOCATION_AREA_TYPE_LOCALIZED.equals( item.getName( ) ) )
            {
                result.add( item );
            }
        }
        return result;
    }

    @Override
    public ReferenceList getLastCampaignAllAreaLabels( )
    {
        return getCampaignAllAreaLabels( getLastCampaign( ).getCode( ) );
    }

    @Override
    public ReferenceList getLastCampaignAllAreaTypes( )
    {
        return getCampaignAllAreaTypes( getLastCampaign( ).getCode( ) );
    }

    // *********************************************************************************************
    // * SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER *
    // * SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER SUBMITTER *
    // *********************************************************************************************

    @Override
    public Collection<SubmitterType> getCampaignSubmitterTypes( String codeCampaign )
    {
        return SubmitterTypeHome.getSubmitterTypesListByCampaign( codeCampaign );
    }

    // *********************************************************************************************
    // * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEME *
    // * THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEMES THEME *
    // *********************************************************************************************

    @Override
    public ReferenceList getCampaignThemes( String codeCampaign )
    {
        return themeLabels.get( codeCampaign );
    }

    @Override
    public ReferenceList getLastCampaignThemes( )
    {
        ReferenceList allThemes = new ReferenceList( );

        for ( ReferenceList campaignThemes : themeLabels.values( ) )
        {
            allThemes.addAll( campaignThemes );
        }

        return allThemes;
    }

    @Override
    public ReferenceList getCampaignThemesFrontRgb( String codeCampaign )
    {
        return themeFrontRgbs.get( codeCampaign );
    }

    // *********************************************************************************************
    // * FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELD *
    // * FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELDS FIELD *
    // *********************************************************************************************

    @Override
    public String [ ] getCampaignFieldData( String codeCampaign, String fieldCode )
    {
        return getCampaignFieldsData( codeCampaign ).get( fieldCode );
    }

    @Override
    public Map<String, String [ ]> getCampaignFieldsData( String codeCampaign )
    {
        return fields.get( codeCampaign );
    }

    // *********************************************************************************************
    // * LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD *
    // * LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD LOAD *
    // *********************************************************************************************

    private static void loadProperties( )
    {
        // Campaigns
        campaigns = new ReferenceList( );
        for ( int i = 1; true; i++ )
        {
            String propName = PROPERTY_PREFIX + i;
            String propValue = AppPropertiesService.getProperty( propName );
            if ( StringUtils.isBlank( propValue ) )
            {
                break;
            }

            String [ ] campaign = propValue.split( PROPERTY_SEP );
            if ( campaign.length != 2 )
            {
                AppLogService.error( "The property '" + propName + "' should be formated as 'code" + PROPERTY_SEP + "label' but is '" + propValue + "'." );
                break;
            }

            campaigns.addItem( campaign [0], campaign [1] );
        }

        // Themes for each campaign
        themeLabels = new HashMap<>( );
        themeFrontRgbs = new HashMap<>( );
        for ( ReferenceItem campaign : campaigns )
        {
            ReferenceList campaignThemes = new ReferenceList( );
            ReferenceList campaignThemeFrontRgbs = new ReferenceList( );
            for ( int i = 1; true; i++ )
            {
                String propName = PROPERTY_PREFIX + campaign.getCode( ) + ".theme." + i;
                String propValue = AppPropertiesService.getProperty( propName );
                if ( StringUtils.isBlank( propValue ) )
                {
                    break;
                }

                String [ ] theme = propValue.split( PROPERTY_SEP );
                if ( theme.length != 3 )
                {
                    AppLogService.error( "The property '" + propName + "' should be formated as 'code" + PROPERTY_SEP + "label" + PROPERTY_SEP
                            + "color' but is '" + propValue + "'." );
                    break;
                }

                campaignThemes.addItem( theme [0], theme [1] );
                campaignThemeFrontRgbs.addItem( theme [0], theme [2] );
            }
            themeLabels.put( campaign.getCode( ), campaignThemes );
            themeFrontRgbs.put( campaign.getCode( ), campaignThemeFrontRgbs );
        }

        // Areas for each campaign
        areaLabels = new HashMap<>( );
        areaTypes = new HashMap<>( );
        for ( ReferenceItem campaign : campaigns )
        {
            ReferenceList campaignAreas = new ReferenceList( );
            ReferenceList campaignTypes = new ReferenceList( );
            for ( int i = 1; true; i++ )
            {
                String propName = PROPERTY_PREFIX + campaign.getCode( ) + ".area." + i;
                String propValue = AppPropertiesService.getProperty( propName );
                if ( StringUtils.isBlank( propValue ) )
                {
                    break;
                }

                String [ ] area = propValue.split( PROPERTY_SEP );
                if ( area.length != 3 )
                {
                    AppLogService.error( "The property '" + propName + "' should be formated as 'code" + PROPERTY_SEP + "label" + PROPERTY_SEP
                            + "type' but is '" + propValue + "'." );
                    break;
                }

                campaignAreas.addItem( area [0], area [1] );
                campaignTypes.addItem( area [0], area [2] );
            }
            areaLabels.put( campaign.getCode( ), campaignAreas );
            areaTypes.put( campaign.getCode( ), campaignTypes );
        }

        // Phase dates for each campaign
        dates = new HashMap<>( );
        for ( ReferenceItem campaign : campaigns )
        {
            Timestamp [ ] beginEnd = new Timestamp [ 2];

            String propName = PROPERTY_PREFIX + campaign.getCode( ) + ".ideation";
            String propValue = AppPropertiesService.getProperty( propName );
            if ( StringUtils.isBlank( propValue ) )
            {
                break;
            }

            String [ ] beginEndStr = propValue.split( PROPERTY_SEP );
            if ( beginEndStr.length != 2 )
            {
                AppLogService
                        .error( "The property '" + propName + "' should be formated as 'timestamp" + PROPERTY_SEP + "timestamp' but is '" + propValue + "'." );
                break;
            }

            beginEnd [0] = Timestamp.valueOf( LocalDateTime.parse( beginEndStr [0] ) );
            beginEnd [1] = Timestamp.valueOf( LocalDateTime.parse( beginEndStr [1] ) );

            dates.put( campaign.getCode( ), beginEnd );
        }

        // Fields for each campaign
        fields = new HashMap<>( );
        for ( ReferenceItem campaign : campaigns )
        {
            Map<String, String [ ]> fieldData = new HashMap<>( );
            for ( int i = 1; i < 5; i++ ) // TODO : add unlimited fields in the last form step
            {
                String fieldName = "field" + i;
                String propName = PROPERTY_PREFIX + campaign.getCode( ) + "." + fieldName;

                String propActiveValue = AppPropertiesService.getProperty( propName + ".active" );
                String propLabelValue = AppPropertiesService.getProperty( propName + ".label" );
                String propDescriptionValue = AppPropertiesService.getProperty( propName + ".description" );
                String propMandatoryValue = AppPropertiesService.getProperty( propName + ".mandatory" );

                fieldData.put( fieldName, new String [ ] {
                        propActiveValue, propLabelValue, propDescriptionValue, propMandatoryValue
                } );
            }
            fields.put( campaign.getCode( ), fieldData );
        }

    }

}
