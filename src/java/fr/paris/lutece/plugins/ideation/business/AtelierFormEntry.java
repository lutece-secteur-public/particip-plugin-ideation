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

import org.hibernate.validator.constraints.*;
import java.io.Serializable;


/**
 * This is the business class for the object AtelierFormEntry
 */ 
public class AtelierFormEntry implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    
    private int _nIdAtelierForm;
    private String _strAlternative1;
    private String _strAlternative2;
    private String _strAlternative3;

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
     * Returns the Alternative1
     * @return The Alternative1
     */
    public String getAlternative1( )
    {
        return _strAlternative1;
    }

    /**
     * Sets the Alternative1
     * @param strAlternative1 The Alternative1
     */ 
    public void setAlternative1( String strAlternative1 )
    {
        _strAlternative1 = strAlternative1;
    }
    /**
     * Returns the Alternative2
     * @return The Alternative2
     */
    public String getAlternative2( )
    {
        return _strAlternative2;
    }

    /**
     * Sets the Alternative2
     * @param strAlternative2 The Alternative2
     */ 
    public void setAlternative2( String strAlternative2 )
    {
        _strAlternative2 = strAlternative2;
    }
    /**
     * Returns the Alternative3
     * @return The Alternative3
     */
    public String getAlternative3( )
    {
        return _strAlternative3;
    }

    /**
     * Sets the Alternative3
     * @param strAlternative3 The Alternative3
     */ 
    public void setAlternative3( String strAlternative3 )
    {
        _strAlternative3 = strAlternative3;
    }

	public int getIdAtelierForm() {
		return _nIdAtelierForm;
	}

	public void setIdAtelierForm(int _nIdAtelierForm) {
		this._nIdAtelierForm = _nIdAtelierForm;
	}
}
