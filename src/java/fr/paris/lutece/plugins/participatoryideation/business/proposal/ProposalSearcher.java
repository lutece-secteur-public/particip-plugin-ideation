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
package fr.paris.lutece.plugins.participatoryideation.business.proposal;

/**
 * This is the business class for storing search infos
 */
public class ProposalSearcher
{

    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_DATE_CREATION = "creation_timestamp";
    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

    public static final String QPVQVA_UNKNOWN = "UNKNOWN";

    private String _strCodeCampaign;
    private String _strCodeTheme;
    private Integer _nExportedTag;
    private String _strTitreOuDescriptionOuRef;
    private Integer _nIdWorkflowState;
    private String _strTypeQpvQva;
    private String _strHandicap;
    private String _strTypeLocation;
    private String _strArrondissement;
    private String _strLuteceUserName;

    private String _strOrderAscDesc;
    private String _strOrderColumn;
    private String _strStatusPublic;
    private Boolean _bIsPublished;

    /**
     * @return the CodeCampaign
     */
    public String getCodeCampaign( )
    {
        return _strCodeCampaign;
    }

    /**
     * @param CodeCampaign
     *            the CodeCampaign to set
     */
    public void setCodeCampaign( String CodeCampaign )
    {
        this._strCodeCampaign = CodeCampaign;
    }

    /**
     * @return the CodeTheme
     */
    public String getCodeTheme( )
    {
        return _strCodeTheme;
    }

    /**
     * @param CodeTheme
     *            the CodeTheme to set
     */
    public void setCodeTheme( String CodeTheme )
    {
        this._strCodeTheme = CodeTheme;
    }

    /**
     * Returns the exported Tag
     * 
     * @return The exported Tag
     */
    public Integer getExportedTag( )
    {
        return _nExportedTag;
    }

    /**
     * Sets the _nExportedTag
     * 
     * @param The
     *            Exported Tagd
     * 
     */
    public void setExportedTag( Integer nExportedTag )
    {
        _nExportedTag = nExportedTag;
    }

    /**
     * @return the TitreOuDescription
     */
    public String getTitreOuDescriptionouRef( )
    {
        return _strTitreOuDescriptionOuRef;
    }

    /**
     * @param strTitreOuDescription
     *            the TitreOuDescription to set
     */
    public void setTitreOuDescriptionouRef( String strTitreOuDescription )
    {
        this._strTitreOuDescriptionOuRef = strTitreOuDescription;
    }

    /**
     * @return the IdWorkflowState
     */
    public Integer getIdWorkflowState( )
    {
        return _nIdWorkflowState;
    }

    /**
     * @param nIdWorkflowState
     *            the IdWorkflowState to set
     */
    public void setIdWorkflowState( Integer nIdWorkflowState )
    {
        this._nIdWorkflowState = nIdWorkflowState;
    }

    /**
     * @return the TypeQpvQva
     */
    public String getTypeQpvQva( )
    {
        return _strTypeQpvQva;
    }

    /**
     * @param strTypeQpvQva
     *            the TypeQpvQva to set
     */
    public void setTypeQpvQva( String strTypeQpvQva )
    {
        this._strTypeQpvQva = strTypeQpvQva;
    }

    public String getHandicap( )
    {
        return _strHandicap;
    }

    public void setHandicap( String strHandicap )
    {
        this._strHandicap = strHandicap;
    }

    /**
     * @return the TypeLocation
     */
    public String getTypeLocation( )
    {
        return _strTypeLocation;
    }

    /**
     * @param strTypeLocation
     *            the TypeLocation to set
     */
    public void setTypeLocation( String strTypeLocation )
    {
        this._strTypeLocation = strTypeLocation;
    }

    /**
     * @return the Arrondissement
     */
    public String getArrondissement( )
    {
        return _strArrondissement;
    }

    /**
     * @param strArrondissement
     *            the Arrondissement to set
     */
    public void setArrondissement( String strArrondissement )
    {
        this._strArrondissement = strArrondissement;
    }

    /**
     * @return the lutece User Name
     */
    public String getLuteceUserName( )
    {
        return _strLuteceUserName;
    }

    /**
     * set Lutece User Name
     * 
     * @param _strLuteceUserName
     *            the lutece User Name
     */
    public void setLuteceUserName( String _strLuteceUserName )
    {
        this._strLuteceUserName = _strLuteceUserName;
    }

    /**
     * @return the OrderAscDesc
     */
    public String getOrderAscDesc( )
    {
        return _strOrderAscDesc;
    }

    /**
     * @param strOrderAscDesc
     *            the OrderAscDesc to set
     */
    public void setOrderAscDesc( String strOrderAscDesc )
    {
        this._strOrderAscDesc = strOrderAscDesc;
    }

    /**
     * @return the OrderColumn
     */
    public String getOrderColumn( )
    {
        return _strOrderColumn;
    }

    /**
     * @param strOrderColumn
     *            the OrderColumn to set
     */
    public void setOrderColumn( String strOrderColumn )
    {
        this._strOrderColumn = strOrderColumn;
    }

    /**
     * Returns the Status
     * 
     * @return The Status
     */
    public String getStatusPublic( )
    {
        return _strStatusPublic;
    }

    /**
     * Sets the Status
     * 
     * @param status
     *            The Status
     */
    public void setStatusPublic( String strStatus )
    {
        _strStatusPublic = strStatus;
    }

    public Boolean getIsPublished( )
    {
        return _bIsPublished;
    }

    public void setIsPublished( Boolean _bIsPublished )
    {
        this._bIsPublished = _bIsPublished;
    }
}
