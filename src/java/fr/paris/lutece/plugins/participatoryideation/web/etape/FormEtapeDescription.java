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
package fr.paris.lutece.plugins.participatoryideation.web.etape;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import fr.paris.lutece.plugins.participatoryideation.business.proposal.Proposal;
import fr.paris.lutece.plugins.participatoryideation.service.campaign.IdeationCampaignDataProvider;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;

public class FormEtapeDescription extends AbstractFormEtape
{

    private static final String I18N_ERROR_DESCRIPTION_MIN_LENGTH = "participatoryideation.validation.proposal.Description.sizeMin";
    private static final String I18N_ERROR_DESCRIPTION_MAX_LENGTH = "participatoryideation.validation.proposal.Description.sizeMax";

    private static final String I18N_ERROR_FIELD3_EMPTY = "participatoryideation.validation.proposal.Field3.empty";
    private static final String I18N_ERROR_FIELD3_MIN_LENGTH = "participatoryideation.validation.proposal.Field3.sizeMin";
    private static final String I18N_ERROR_FIELD3_MAX_LENGTH = "participatoryideation.validation.proposal.Field3.sizeMax";

    private static final String I18N_ERROR_FIELD4_EMPTY = "participatoryideation.validation.proposal.Field4.empty";
    private static final String I18N_ERROR_FIELD4_MIN_LENGTH = "participatoryideation.validation.proposal.Field4.sizeMin";
    private static final String I18N_ERROR_FIELD4_MAX_LENGTH = "participatoryideation.validation.proposal.Field4.sizeMax";
    private static final String I18N_ERROR_FIELD4_COMPLEMENT_MIN_LENGTH = "participatoryideation.validation.proposal.Field4Complement.sizeMin";
    private static final String I18N_ERROR_FIELD4_COMPLEMENT_MAX_LENGTH = "participatoryideation.validation.proposal.Field4Complement.sizeMax";

    private static final String DSKEY_DESCRIPTION_MIN_LENGTH = "participatoryideation.site_property.form.description.minLength";
    private static final String DSKEY_DESCRIPTION_MAX_LENGTH = "participatoryideation.site_property.form.description.maxLength";
    private static final String DSKEY_FIELD4_COMPLEMENT_MIN_LENGTH = "participatoryideation.site_property.form.field4_complement.minLength";
    private static final String DSKEY_FIELD4_COMPLEMENT_MAX_LENGTH = "participatoryideation.site_property.form.field4_complement.maxLength";

    @NotEmpty( message = "#i18n{participatoryideation.validation.proposal.FormEtapeDescription.description.notEmpty}" )
    @Size( max = 10000, message = "#i18n{participatoryideation.validation.proposal.Description.size}" )
    private String _strDescription;
    @Pattern( regexp = "(\\d|\\s){0,20}", message = "#i18n{participatoryideation.validation.proposal.FormEtapeDescription.cout.pattern}" )
    private String _strCout;

    // ------------------------------------------------------------

    public String getDescription( )
    {
        return _strDescription;
    }

    public void setDescription( String _strDescription )
    {
        this._strDescription = _strDescription;
    }

    // ------------------------------------------------------------

    public String getCout( )
    {
        return _strCout;
    }

    public void setCout( String _strCout )
    {
        this._strCout = _strCout;
    }

    // ------------------------------------------------------------

    private String _strField4;

    public String getField4( )
    {
        return _strField4;
    }

    public void setField4( String strField4 )
    {
        this._strField4 = strField4;
    }

    // ------------------------------------------------------------

    private String _strField4Complement;

    public String getField4Complement( )
    {
        return _strField4Complement;
    }

    public void setField4Complement( String strField4Complement )
    {
        this._strField4Complement = strField4Complement;
    }

    // ------------------------------------------------------------

    private String _strField3;

    public String getField3( )
    {
        return _strField3;
    }

    public void setField3( String strField3 )
    {
        this._strField3 = strField3;
    }

    // ------------------------------------------------------------

    @Override
    public List<String> checkValidationErrorsLocalized( HttpServletRequest request, Proposal proposal, Locale locale )
    {
        List<String> listErrors = new ArrayList<>( );
        String userUid = "guid";
        LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
        if ( user != null )
        {
            userUid = user.getName( );
        }

        // ---------------------------------------------------------------------- Check description length

        String strMax = DatastoreService.getDataValue( DSKEY_DESCRIPTION_MAX_LENGTH, "" );
        if ( !"".equals( strMax ) )
        {
            try
            {
                int nMax = Integer.parseInt( strMax );
                if ( getDescription( ).trim( ).replaceAll( "(\\r\\n|\\n\\r)", " " ).length( ) > nMax )
                {
                    listErrors.add( I18nService.getLocalizedString( I18N_ERROR_DESCRIPTION_MAX_LENGTH, new String [ ] {
                            Integer.toString( nMax )
                    }, locale ) );
                }
            }
            catch( NumberFormatException nfe )
            {
                AppLogService.error(
                        "IdeationApp: NumberFormatException when parsing max Description length from datastore, key " + DSKEY_DESCRIPTION_MAX_LENGTH, nfe );
            }
        }

        String strMin = DatastoreService.getDataValue( DSKEY_DESCRIPTION_MIN_LENGTH, "" );
        if ( !"".equals( strMin ) )
        {
            try
            {
                int nMin = Integer.parseInt( strMin );
                if ( getDescription( ).trim( ).length( ) < nMin )
                {
                    listErrors.add( I18nService.getLocalizedString( I18N_ERROR_DESCRIPTION_MIN_LENGTH, new String [ ] {
                            Integer.toString( nMin )
                    }, locale ) );
                }
            }
            catch( NumberFormatException nfe )
            {
                AppLogService.error(
                        "IdeationApp: NumberFormatException when parsing max Description length from datastore, key " + DSKEY_DESCRIPTION_MAX_LENGTH, nfe );
            }
        }

        // ---------------------------------------------------------------------- Check mandatory and length of optional fields

        String [ ] fieldData = null;

        // TODO : Move these values into field data properties
        int fieldMinLength = 0; 
        int fieldMaxLength = 200;

        fieldData = IdeationCampaignDataProvider.getInstance( ).getCampaignFieldData( proposal.getCodeCampaign( ), "field3" );
        if ( "1".contentEquals( fieldData[0] ) )
        {
            if ( "1".contentEquals( fieldData[3] ) && StringUtils.isBlank( getField3( ) ) )
            {
                listErrors.add( I18nService.getLocalizedString( I18N_ERROR_FIELD3_EMPTY, new String [ ] {
                        fieldData [1]
                }, locale ) );
            }
            else
            {
                if ( getField3( ) != null && getField3( ).trim( ).length( ) < fieldMinLength )
                {
                    listErrors.add( I18nService.getLocalizedString( I18N_ERROR_FIELD3_MIN_LENGTH, new String [ ] { fieldData [1], "" + fieldMinLength }, locale ) );
                }

                if ( getField3( ) != null && getField3( ).trim( ).length( ) > fieldMaxLength )
                {
                    listErrors.add( I18nService.getLocalizedString( I18N_ERROR_FIELD3_MAX_LENGTH, new String [ ] { fieldData [1], "" + fieldMaxLength}, locale ) );
                }
            }
        }

        fieldData = IdeationCampaignDataProvider.getInstance( ).getCampaignFieldData( proposal.getCodeCampaign( ), "field4" );
        if ( "1".contentEquals( fieldData[0] ) )
        {
	        if ( "yes".equals( getField4( ) ) )
	        {
	            if ( "1".contentEquals( fieldData[3] ) && StringUtils.isBlank( getField4( ) ) )
	            {
	                listErrors.add( I18nService.getLocalizedString( I18N_ERROR_FIELD4_EMPTY, new String [ ] { fieldData [1] }, locale ) );
	            }
	            else
	            {
	                if ( getField4( ) != null && getField4( ).trim( ).length( ) < fieldMinLength )
	                {
	                    listErrors.add( I18nService.getLocalizedString( I18N_ERROR_FIELD4_MIN_LENGTH, new String [ ] { fieldData [1], "" + fieldMinLength }, locale ) );
	                }

	                if ( getField4( ) != null && getField4( ).trim( ).length( ) > fieldMaxLength )
	                {
	                    listErrors.add( I18nService.getLocalizedString( I18N_ERROR_FIELD4_MAX_LENGTH, new String [ ] { fieldData [1], "" + fieldMaxLength }, locale ) );
	                }
	            }
	        }
    	}

        return listErrors;
    }

}
