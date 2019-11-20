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
import java.util.List;


/**
 * This is the business class for the object AtelierForm
 */ 
public class AtelierForm implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations 
    private int _nId;
    private int _nIdAtelier;
	private AtelierFormEntry _choixTitre;
	private AtelierFormEntry _choixDescription;
	private List<AtelierFormEntry> _choixDescriptionsComplementaires;


    /**
     * Returns the Id the id AtelierForm
     * @return The Id the id AtelierForm
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id AtelierForm
     * @param nId The id AtelierForm
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }
    
    /**
     * Returns the Id the id Atelier
     * @return The id Atelier
     */
    public int getIdAtelier( )
    {
		return _nIdAtelier;
	}
    
    /**
     * Sets the Id Atelier
     * @param nIdAtelier The id Atelier
     */
	public void setIdAtelier( int nIdAtelier ) 
	{
		this._nIdAtelier = nIdAtelier;
	}

	/**
     * Gets the choixTitre
     * @return choixTitre
     */
	public AtelierFormEntry getChoixTitre( ) 
	{
		return _choixTitre;
	}
	
	/**
	 * Sets choixTitre 
	 * @param _choixTitre the choixTitre 
	 */
	public void setChoixTitre( AtelierFormEntry _choixTitre )
	{
		this._choixTitre = _choixTitre;
	}
	
	/**
	 *  Gets the choixDescription
	 * @return choixDescription
	 */
	public AtelierFormEntry getChoixDescription( )
	{
		return _choixDescription;
	}
	
	/**
	 * Sets the choixDescription
	 * @param _choixDescription the choixDescription
	 */
	public void setChoixDescription( AtelierFormEntry choixDescription ) 
	{
		_choixDescription = choixDescription;
	}
	/**
	 * Gets the list choixDescriptionsComplementaires
	 * @return list choixDescriptionsComplementaires
	 */
	public List<AtelierFormEntry> getChoixDescriptionComplementaires( )
	{
		return _choixDescriptionsComplementaires;
	}
	
	/**
	 * Sets the choixDescriptionsComplementaires
	 * @param choixDescriptionComplementaires the choixDescriptionsComplementaires
	 */
	public void setChoixDescriptionComplementaires(
			List<AtelierFormEntry> choixDescriptionComplementaires ) 
	{
		_choixDescriptionsComplementaires = choixDescriptionComplementaires;
	}

}
