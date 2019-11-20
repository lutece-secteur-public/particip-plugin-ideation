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
package fr.paris.lutece.plugins.ideation.business; 


import javax.validation.constraints.*;

import org.hibernate.validator.constraints.*;

import fr.paris.lutece.plugins.ideation.business.Idee.Status;
import fr.paris.lutece.portal.service.resource.IExtendableResource;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;


/**
 * This is the business class for the object Atelier
 */ 
public class Atelier implements Serializable, IExtendableResource
{
    private static final long serialVersionUID = 1L;

    public static final String PROPERTY_RESOURCE_TYPE = "ATELIER";
    public static final String WORKFLOW_RESOURCE_TYPE = "IDEATION_ATELIER";
    
    // Variables declarations 
    private int _nId;
    
    @NotEmpty( message = "#i18n{ideation.validation.atelier.Titre.notEmpty}" )
    private String _strTitre;
    
    @NotEmpty( message = "#i18n{ideation.validation.atelier.Description.notEmpty}" )
    private String _strDescription;

    @NotEmpty( message = "#i18n{ideation.validation.atelier.Campagne.notEmpty}" )
    private String _strCampagne;
    
    @NotEmpty( message = "#i18n{ideation.validation.atelier.Thematique.notEmpty}" )
    private String _strThematique;
    
    @NotEmpty( message = "#i18n{ideation.validation.atelier.Type.notEmpty}" )
    @Size( max = 50 , message = "#i18n{ideation.validation.atelier.Type.size}" ) 
    private String _strType;
    
    @NotNull( message = "#i18n{ideation.validation.atelier.DateDebutPhase1.notEmpty}" )
    private Date _dateDateDebutPhase1;
    
    @NotNull( message = "#i18n{ideation.validation.atelier.DateFinPhase1.notEmpty}" )
    private Date _dateDateFinPhase1;
    
    @NotNull( message = "#i18n{ideation.validation.atelier.DateDebutPhase2.notEmpty}" )
    private Date _dateDateDebutPhase2;
    
    @NotNull( message = "#i18n{ideation.validation.atelier.DateFinPhase2.notEmpty}" )
    private Date _dateDateFinPhase2;
    
    @NotNull( message = "#i18n{ideation.validation.atelier.DateDebutPhase3.notEmpty}" )
    private Date _dateDateDebutPhase3;
    
    @NotNull( message = "#i18n{ideation.validation.atelier.DateFinPhase3.notEmpty}" )
    private Date _dateDateFinPhase3;
    
    @Size( max = 50 , message = "#i18n{ideation.validation.atelier.LocalisationType.size}" ) 
    private String _strLocalisationType;
    
    @Size( max = 50 , message = "#i18n{ideation.validation.atelier.LocalisationArdt.size}" ) 
    private String _strLocalisationArdt;
    
    private String _strAddress;

    private Double _dLongitude;
    
    private Double _dLatitude;
    
    private String _strTextePhase1;
    
    private String _strTitrePhase3;
    
    private String _strTextePhase3;
    
    private int _nJoursDeRappelPhase1 ;

    private int _nJoursDeRappelPhase2 ;
    
    private Long _nCout;
    
    private String _strHandicap;
    
    private String _strLieuAtelier;

    private String _strDateAtelier;

    @NotEmpty( message = "#i18n{ideation.validation.atelier.ListCodeIdee.notEmpty}" )
    private String _strListCodeIdee;
    
    @URL(message = "#i18n{portal.validation.message.url}")
    @Size( max = 255 , message = "#i18n{ideation.validation.atelier.LienFormulairePhase2.size}" ) 
    private String _strLienFormulairePhase2;
    
    private String _strGeoJson;
    
    private Status _statusPublic;
    
    



    /**
     * Status of idee
     */
    public enum Status
    {
        STATUS_EN_CO_CONSTRUCTION( "ENCOCONSTRUCTION", "ideation.message.labelStatusEnCoConstruction", true ),
        STATUS_SUPPRIME_PAR_MDP( "SUPPRIMEPARMDP", "ideation.message.labelSupprimeParMdp", false );

        private static final Map<String, Status> valueMap;
        private static final List<Status> listStatusPublished = new ArrayList<>( );
        private static final List<Status> listStatusUnPublished = new ArrayList<>( );

        static
        {
            valueMap = new HashMap<String, Status>( );
            for ( Status s : Status.values( ) )
            {
                valueMap.put( s.strValue, s );
                if ( s.isPublished( ) )
                {
                    listStatusPublished.add( s );
                }
                else
                {
                    listStatusUnPublished.add( s );
                }
            }
        }

        public static Status getByValue( String strEtape )
        {
            return valueMap.get( strEtape );
        }

        public static List<Status> getAllStatusPublished()
        {
            return listStatusPublished;
        }

        public static List<Status> getAllStatusUnPublished()
        {
            return listStatusUnPublished;
        }

        private final String strValue;
        private final String strLibelle;
        private final boolean bPublished;

        Status( String strValue, String strMessage, boolean bPublished )
        {
            this.strValue = strValue;
            this.strLibelle = strMessage;
            this.bPublished = bPublished;
        }

        public String getValue( )
        {
            return this.strValue;
        }

        public String getLibelle( )
        {
            return this.strLibelle;
        }

        public boolean isPublished( )
        {
            return this.bPublished;
        }
    }
    /**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }

    /**
     * Returns the Titre
     * @return The Titre
     */
    public String getTitre( )
    {
        return _strTitre;
    }

    /**
     * Sets the Titre
     * @param strTitre The Titre
     */ 
    public void setTitre( String strTitre )
    {
        _strTitre = strTitre;
    }
    /**
     * Returns the Description
     * @return The Description
     */
    public String getDescription( )
    {
        return _strDescription;
    }

    /**
     * Sets the Description
     * @param strDescription The Description
     */ 
    public void setDescription( String strDescription )
    {
        _strDescription = strDescription;
    }
    
    /**
     * Returns the Campagne
     * @return The Campagne
     */
    public String getCampagne( ) 
    {
		return _strCampagne;
	}
    /**
     * Sets the campagne
     * @param _strCampagne The Campagne
     */
	public void setCampagne( String strCampagne ) 
	{
		_strCampagne = strCampagne;
	}
	/**
	 * Returns the Thematique
	 * @return the Thematique
	 */
	public String getThematique( ) 
	{
		return _strThematique;
	}
	/**
	 * Sets the Thematique
	 * @param _strThematique the Thematique
	 */
	public void setThematique( String strThematique )
	{
		_strThematique = strThematique;
	}

	/**
     * Returns the Type
     * @return The Type
     */
    public String getType( )
    {
        return _strType;
    }

    /**
     * Sets the Type
     * @param strType The Type
     */ 
    public void setType( String strType )
    {
        _strType = strType;
    }
    /**
     * Returns the DateDebutPhase1
     * @return The DateDebutPhase1
     */
    public Date getDateDebutPhase1( )
    {
        return _dateDateDebutPhase1;
    }

    /**
     * Sets the DateDebutPhase1
     * @param dateDateDebutPhase1 The DateDebutPhase1
     */ 
    public void setDateDebutPhase1( Date dateDateDebutPhase1 )
    {
        _dateDateDebutPhase1 = dateDateDebutPhase1;
    }
    /**
     * Returns the DateFinPhase1
     * @return The DateFinPhase1
     */
    public Date getDateFinPhase1( )
    {
        return _dateDateFinPhase1;
    }

    /**
     * Sets the DateFinPhase1
     * @param dateDateFinPhase1 The DateFinPhase1
     */ 
    public void setDateFinPhase1( Date dateDateFinPhase1 )
    {
        _dateDateFinPhase1 = dateDateFinPhase1;
    }
    /**
     * Returns the DateDebutPhase2
     * @return The DateDebutPhase2
     */
    public Date getDateDebutPhase2( )
    {
        return _dateDateDebutPhase2;
    }

    /**
     * Sets the DateDebutPhase2
     * @param dateDateDebutPhase2 The DateDebutPhase2
     */ 
    public void setDateDebutPhase2( Date dateDateDebutPhase2 )
    {
        _dateDateDebutPhase2 = dateDateDebutPhase2;
    }
    /**
     * Returns the DateFinPhase2
     * @return The DateFinPhase2
     */
    public Date getDateFinPhase2( )
    {
        return _dateDateFinPhase2;
    }

    /**
     * Sets the DateFinPhase2
     * @param dateDateFinPhase2 The DateFinPhase2
     */ 
    public void setDateFinPhase2( Date dateDateFinPhase2 )
    {
        _dateDateFinPhase2 = dateDateFinPhase2;
    }
    /**
     * Returns the DateDebutPhase3
     * @return The DateDebutPhase3
     */
    public Date getDateDebutPhase3( )
    {
        return _dateDateDebutPhase3;
    }

    /**
     * Sets the DateDebutPhase3
     * @param dateDateDebutPhase3 The DateDebutPhase3
     */ 
    public void setDateDebutPhase3( Date dateDateDebutPhase3 )
    {
        _dateDateDebutPhase3 = dateDateDebutPhase3;
    }
    /**
     * Returns the DateFinPhase3
     * @return The DateFinPhase3
     */
    public Date getDateFinPhase3( )
    {
        return _dateDateFinPhase3;
    }

    /**
     * Sets the DateFinPhase3
     * @param dateDateFinPhase3 The DateFinPhase3
     */ 
    public void setDateFinPhase3( Date dateDateFinPhase3 )
    {
        _dateDateFinPhase3 = dateDateFinPhase3;
    }
    /**
     * Returns the LocalisationType
     * @return The LocalisationType
     */
    public String getLocalisationType( )
    {
        return _strLocalisationType;
    }

    /**
     * Sets the LocalisationType
     * @param strLocalisationType The LocalisationType
     */ 
    public void setLocalisationType( String strLocalisationType )
    {
        _strLocalisationType = strLocalisationType;
    }
    /**
     * Returns the LocalisationArdt
     * @return The LocalisationArdt
     */
    public String getLocalisationArdt( )
    {
        return _strLocalisationArdt;
    }

    /**
     * Sets the LocalisationArdt
     * @param strLocalisationArdt The LocalisationArdt
     */ 
    public void setLocalisationArdt( String strLocalisationArdt )
    {
        _strLocalisationArdt = strLocalisationArdt;
    }
    
    /**
     * Returns the Address
     * @return The Address
     */
    public String getAddress( )
    {
        return _strAddress;
    }

    /**
     * Sets the Address
     * @param strAddress The Address
     */
    public void setAddress( String strAddress )
    {
        _strAddress = strAddress;
    }

    /**
     * Returns the Longitude
     * @return The Longitude
     */
    public Double getLongitude( )
    {
        return _dLongitude;
    }

    /**
     * Sets the Longitude
     * @param nLongitude The Longitude
     */ 
    public void setLongitude( Double dLongitude )
    {
        _dLongitude = dLongitude;
    }
    /**
     * Returns the Latitude
     * @return The Latitude
     */
    public Double getLatitude( )
    {
        return _dLatitude;
    }

    /**
     * Sets the Latitude
     * @param nLatitude The Latitude
     */ 
    public void setLatitude( Double dLatitude )
    {
        _dLatitude = dLatitude;
    }

    /**
     * Returns the GeoJson
     * @return The GeoJson
     */
    public String getGeoJson()
    {
        return _strGeoJson;
    }

    /**
     * Sets the GeoJson
     * @param The GeoJson
     */
    public void setGeoJson( String strGeoJson )
    {
        _strGeoJson = strGeoJson;
    }

    /**
     * Returns the TextePhase1
     * @return The TextePhase1
     */
    public String getTextePhase1( )
    {
        return _strTextePhase1;
    }

    /**
     * Sets the TextePhase1
     * @param strTextePhase1 The TextePhase1
     */ 
    public void setTextePhase1( String strTextePhase1 )
    {
        _strTextePhase1 = strTextePhase1;
    }
    /**
     * Returns the TitrePhase3
     * @return The TitrePhase3
     */
    public String getTitrePhase3( )
    {
        return _strTitrePhase3;
    }

    /**
     * Sets the TitrePhase3
     * @param strTitrePhase3 The TitrePhase3
     */ 
    public void setTitrePhase3( String strTitrePhase3 )
    {
        _strTitrePhase3 = strTitrePhase3;
    }
    /**
     * Returns the TextePhase3
     * @return The TextePhase3
     */
    public String getTextePhase3( )
    {
        return _strTextePhase3;
    }

    /**
     * Sets the TextePhase3
     * @param strTextePhase3 The TextePhase3
     */ 
    public void setTextePhase3( String strTextePhase3 )
    {
        _strTextePhase3 = strTextePhase3;
    }
    /**
     * Returns the ListCodeIdee
     * @return The ListCodeIdee
     */
    public String getListCodeIdee( )
    {
        return _strListCodeIdee;
    }

    /**
     * Sets the ListCodeIdee
     * @param strListCodeIdee The ListCodeIdee
     */ 
    public void setListCodeIdee( String strListCodeIdee )
    {
        _strListCodeIdee = strListCodeIdee;
    }
    /**
     * Returns the LienFormulairePhase2
     * @return The LienFormulairePhase2
     */
    public String getLienFormulairePhase2( )
    {
        return _strLienFormulairePhase2;
    }

    /**
     * Sets the LienFormulairePhase2
     * @param strLienFormulairePhase2 The LienFormulairePhase2
     */ 
    public void setLienFormulairePhase2( String strLienFormulairePhase2 )
    {
        _strLienFormulairePhase2 = strLienFormulairePhase2;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getIdExtendableResource( )
    {
        return Integer.toString( _nId );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getExtendableResourceType( )
    {
        return PROPERTY_RESOURCE_TYPE;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getExtendableResourceName( )
    {
        return _strTitre;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getExtendableResourceDescription( )
    {
        return _strDescription;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String getExtendableResourceImageUrl( )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Returns the StatusPublic
     * @return The StatusPublic
     */
    public Status getStatusPublic( )
    {
        return _statusPublic;
    }

    /**
     * Sets the StatusPublic
     * @param statusPublic The StatusPublic
     */ 
    public void setStatusPublic( Status statusPublic )
    {
        _statusPublic = statusPublic;
    }

    /**
     * Returns the JoursRappelPhase1
     * @return JoursRappelPhase1 the number days of reminder for step 1
     */
	public int getJoursDeRappelPhase1( ) 
	{
		return _nJoursDeRappelPhase1;
	}
	/**
	 * Sets the the number days of reminder for step 1
	 * @param nJoursRappelPhase1 the number days of reminder for step 1
	 */
	public void setJoursDeRappelPhase1( int nJoursRappelPhase1 ) 
	{
		this._nJoursDeRappelPhase1 = nJoursRappelPhase1;
	}
	/**
	 * Returns the JoursRappelPhase2
	 * @return JoursRappelPhase2 the number days of reminder for step 2
	 */
	public int getJoursDeRappelPhase2( )
	{
		return _nJoursDeRappelPhase2;
	}
	/**
	 * Sets the  the the number days of reminder for step 2
	 * @param nJoursRappelPhase2 the number days of reminder for step 2
	 */
	public void setJoursDeRappelPhase2( int nJoursRappelPhase2 ) 
	{
		this._nJoursDeRappelPhase2 = nJoursRappelPhase2;
	}
	
	/**
	 * GET cout 
	 * @return cout the cout
	 */
	public Long getCout( ) 
	{
		return _nCout;
	}

	/**
	 * Set Cout
	 * @param _nCout  cout
	 */
	public void setCout( Long _nCout ) 
	{
		this._nCout = _nCout;
	}

	/**
	 * GET DateAtelier 
	 * @return DateAtelier the date
	 */
	public String getDateAtelier( ) 
	{
		return _strDateAtelier;
	}

	public void setHandicap( String _strHandicap ) 
	{
		this._strHandicap = _strHandicap;
	}

	public String getHandicap( ) 
	{
		return _strHandicap;
	}

	/**
	 * Set DateAtelier
	 * @param _strDateAtelier  date
	 */
	public void setDateAtelier( String _strDateAtelier ) 
	{
		this._strDateAtelier = _strDateAtelier;
	}

	/**
	 * GET LieuAtelier 
	 * @return LieuAtelier the Lieu
	 */
	public String getLieuAtelier( ) 
	{
		return _strLieuAtelier;
	}

	/**
	 * Set LieuAtelier
	 * @param _strLieuAtelier  Lieu
	 */
	public void setLieuAtelier( String _strLieuAtelier ) 
	{
		this._strLieuAtelier = _strLieuAtelier;
	}
}
