/*
 * Copyright (c) 2002-2019, Mairie de Paris
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

import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

import java.io.Serializable;


/**
 * This is the business class for the object CampagneDepositaire
 */ 
public class CampagneDepositaire implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    @NotEmpty( message = "#i18n{participatoryideation.validation.campagnedepositaire.CodeCampagne.notEmpty}" )
    @Size( max = 50 , message = "#i18n{participatoryideation.validation.campagnedepositaire.CodeCampagne.size}" ) 
    private String _strCodeCampagne;
    
    @NotEmpty( message = "#i18n{participatoryideation.validation.campagnedepositaire.CodeDepositaireType.notEmpty}" )
    @Size( max = 50 , message = "#i18n{participatoryideation.validation.campagnedepositaire.CodeDepositaireType.size}" ) 
    private String _strCodeDepositaireType;

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
     * Returns the CodeCampagne
     * @return The CodeCampagne
     */
    public String getCodeCampagne( )
    {
        return _strCodeCampagne;
    }

    /**
     * Sets the CodeCampagne
     * @param strCodeCampagne The CodeCampagne
     */ 
    public void setCodeCampagne( String strCodeCampagne )
    {
        _strCodeCampagne = strCodeCampagne;
    }
    /**
     * Returns the CodeDepositaireType
     * @return The CodeDepositaireType
     */
    public String getCodeDepositaireType( )
    {
        return _strCodeDepositaireType;
    }

    /**
     * Sets the CodeDepositaireType
     * @param strCodeDepositaireType The CodeDepositaireType
     */ 
    public void setCodeDepositaireType( String strCodeDepositaireType )
    {
        _strCodeDepositaireType = strCodeDepositaireType;
    }
}
