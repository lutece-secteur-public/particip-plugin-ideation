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
package fr.paris.lutece.plugins.participatoryideation.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public class IdeeBoForm
{

    private int _nId;

    private String _strTitre;
    private String _strDescription;
    private String _strCout;
    private String _strLocalisationArdt;

    private String _strTypeQpvQva;
    private String _strIdQpvQva;
    private String _strLibelleQpvQva;
    private String _strHandicap;
    private String _strIdProjet;
    private String _strTitreProjet;
    private String _strUrlProjet;
    private String _strWinnerProjet;

    /**
     * Returns the Id
     * 
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * 
     * @param nId
     *            The Id
     */
    public void setId( int nId )
    {
        _nId = nId;
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

    /**
     * @return the IdQpvQva
     */
    public String getIdQpvQva( )
    {
        return _strIdQpvQva;
    }

    /**
     * @param strIdQpvQva
     *            the IdQpvQva to set
     */
    public void setIdQpvQva( String strIdQpvQva )
    {
        this._strIdQpvQva = strIdQpvQva;
    }

    /**
     * @return the LibelleQpvQva
     */
    public String getLibelleQpvQva( )
    {
        return _strLibelleQpvQva;
    }

    /**
     * @param strLibelleQpvQva
     *            the LibelleQpvQva to set
     */
    public void setLibelleQpvQva( String strLibelleQpvQva )
    {
        this._strLibelleQpvQva = strLibelleQpvQva;
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
     * GET IdProjet the id project
     * 
     * @return IdProjet the id project
     */
    public String getIdProjet( )
    {
        return _strIdProjet;
    }

    /**
     * SET IdProjet
     * 
     * @param strIDProjet
     *            the Id project
     */
    public void setIdProjet( String strIDProjet )
    {
        this._strIdProjet = strIDProjet;
    }

    /**
     * GET the title project
     * 
     * @return TitreProjet the title project
     */
    public String getTitreProjet( )
    {
        return _strTitreProjet;
    }

    /**
     * SET the title project
     * 
     * @param strTitreProjet
     *            the title project
     */
    public void setTitreProjet( String strTitreProjet )
    {
        this._strTitreProjet = strTitreProjet;
    }

    /**
     * Get the url project
     * 
     * @return the url project
     */
    public String getUrlProjet( )
    {
        return _strUrlProjet;
    }

    /**
     * Set the url project
     * 
     * @param strUrlProjet
     *            the url project
     */
    public void setUrlProjet( String strUrlProjet )
    {
        _strUrlProjet = strUrlProjet;
    }

    /**
     * Get the winner project value
     * 
     * @return the winner project value
     */
    public String getWinnerProjet( )
    {
        return _strWinnerProjet;
    }

    /**
     * Set the winner project value
     * 
     * @param strUrlProjet
     *            the winner project value
     */
    public void setWinnerProjet( String strWinnerProjet )
    {
        _strWinnerProjet = strWinnerProjet;
    }

    public String getTitre( )
    {
        return _strTitre;
    }

    public void setTitre( String _strTitre )
    {
        this._strTitre = _strTitre;
    }

    public String getDescription( )
    {
        return _strDescription;
    }

    public void setDescription( String _strDescription )
    {
        this._strDescription = _strDescription;
    }

    public String getCout( )
    {
        return _strCout;
    }

    public void setCout( String _strCout )
    {
        this._strCout = _strCout;
    }

    public String getLocalisationArdt( )
    {
        return _strLocalisationArdt;
    }

    public void setLocalisationArdt( String _strLocalisationArdt )
    {
        this._strLocalisationArdt = _strLocalisationArdt;
    }

}
