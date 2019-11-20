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

import java.io.Serializable;


/**
 * This is the business class for the object CampagneTheme
 */ 
public class ProfanityFilter implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
	public static final String DESCRIPTION_RESOURCE_TYPE = "DESCRIPTION";
	public static final String TITLE_RESOURCE_TYPE = "TITLE";
	public static final String COMMENT_RESOURCE_TYPE = "COMMENT";
	public static final String DEPOSITAIRE_RESOURCE_TYPE = "DEPOSITAIRE";
	
    private String _strUidUser;

    private String _strWord;
    
    private String _strRessourceType;
    
    private int _nCounter;

    
    /**
     * 
     * @return
     */
    public int getCounter( )
    {
        return _nCounter;
    }

    /**
     * 
     * @param nCounter
     */
    public void setCounter( int nCounter )
    {
    	_nCounter = nCounter;
    }

    /**
     * 
     * @return
     */
    public void incCounter(int nVal){
    	
    	_nCounter = this._nCounter + nVal;
    }
    
   public void decCounter(int nVal){
    	
    	_nCounter = this._nCounter - nVal;
    }
    
    public String getUidUser( )
    {
        return _strUidUser;
    }

   
    public void setUidUser( String strUidUser )
    {
    	_strUidUser = strUidUser;
    }
    
    public String getWord( )
    {
        return _strWord;
    }

     
    public void setWord( String strWord )
    {
    	_strWord = strWord;
    }
 
    public String getRessourceType( )
    {
        return _strRessourceType;
    }
 
    public void setRessourceType( String strRessourceType )
    {
    	_strRessourceType = strRessourceType;
    }
   
}
