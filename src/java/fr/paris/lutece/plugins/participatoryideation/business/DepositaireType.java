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
package fr.paris.lutece.plugins.participatoryideation.business;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import fr.paris.lutece.util.ReferenceList;

/**
 * This is the business class for the object DepositaireType
 */
public class DepositaireType implements Serializable
{
    public static final String CODE_COMPLEMENT_TYPE_NONE = "NONE";
    public static final String CODE_COMPLEMENT_TYPE_LIST = "LIST";
    public static final String CODE_COMPLEMENT_TYPE_FREE = "FREE";

    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    @NotEmpty( message = "#i18n{participatoryideation.validation.depositaire.Code.notEmpty}" )
    @Size( max = 50, message = "#i18n{participatoryideation.validation.depositaire.Code.size}" )
    private String _strCode;

    @NotEmpty( message = "#i18n{participatoryideation.validation.depositaire.Libelle.notEmpty}" )
    @Size( max = 50, message = "#i18n{participatoryideation.validation.depositaire.Libelle.size}" )
    private String _strLibelle;

    @NotEmpty( message = "#i18n{participatoryideation.validation.depositaire.CodeComplementType.notEmpty}" )
    private String _strCodeComplementType;

    // Non null when codeComplementType is "LIST"
    private ReferenceList _listValues;

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
     * Returns the Code
     * 
     * @return The Code
     */
    public String getCode( )
    {
        return _strCode;
    }

    /**
     * Sets the Code
     * 
     * @param strCode
     *            The Code
     */
    public void setCode( String strCode )
    {
        _strCode = strCode;
    }

    /**
     * Returns the Libelle
     * 
     * @return The Libelle
     */
    public String getLibelle( )
    {
        return _strLibelle;
    }

    /**
     * Sets the Libelle
     * 
     * @param strLibelle
     *            The Libelle
     */
    public void setLibelle( String strLibelle )
    {
        _strLibelle = strLibelle;
    }

    /**
     * Returns the CodeComplementType
     * 
     * @return The CodeComplementType
     */
    public String getCodeComplementType( )
    {
        return _strCodeComplementType;
    }

    /**
     * Sets the CodeComplementType
     * 
     * @param strCodeComplementType
     *            The CodeComplementType
     */
    public void setCodeComplementType( String strCodeComplementType )
    {
        _strCodeComplementType = strCodeComplementType;
    }

    /**
     * @return the values
     */
    public ReferenceList getValues( )
    {
        return _listValues;
    }

    /**
     * @param values
     *            the values to set
     */
    public void setValues( ReferenceList values )
    {
        _listValues = values;
    }
}
