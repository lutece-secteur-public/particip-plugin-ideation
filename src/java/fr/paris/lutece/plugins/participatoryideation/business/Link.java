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
package fr.paris.lutece.plugins.participatoryideation.business;

import java.io.Serializable;

/**
 * This is the business class for the object Link
 */
public class Link implements Serializable
{
    private static final long serialVersionUID = 1L;

    // Variables declarations
    private int _nId;

    private int _parentId;
    private int _childId;

    private String _parentCodeCampagne;
    private String _childCodeCampagne;

    private int _parentCodeIdee;
    private int _childCodeIdee;

    private String _parentTitle;
    private String _childTitle;

    public int getId( )
    {
        return _nId;
    }

    public void setId( int _nId )
    {
        this._nId = _nId;
    }

    public int getParentId( )
    {
        return _parentId;
    }

    public void setParentId( int _parentId )
    {
        this._parentId = _parentId;
    }

    public int getChildId( )
    {
        return _childId;
    }

    public void setChildId( int _childId )
    {
        this._childId = _childId;
    }

    public String getParentCodeCampagne( )
    {
        return _parentCodeCampagne;
    }

    public void setParentCodeCampagne( String _parentCodeCampagne )
    {
        this._parentCodeCampagne = _parentCodeCampagne;
    }

    public String getChildCodeCampagne( )
    {
        return _childCodeCampagne;
    }

    public void setChildCodeCampagne( String _childCodeCampagne )
    {
        this._childCodeCampagne = _childCodeCampagne;
    }

    public int getParentCodeIdee( )
    {
        return _parentCodeIdee;
    }

    public void setParentCodeIdee( int _parentCodeIdee )
    {
        this._parentCodeIdee = _parentCodeIdee;
    }

    public int getChildCodeIdee( )
    {
        return _childCodeIdee;
    }

    public void setChildCodeIdee( int _childCodeIdee )
    {
        this._childCodeIdee = _childCodeIdee;
    }

    public String getParentTitle( )
    {
        return _parentTitle;
    }

    public void setParentTitle( String _parentTitle )
    {
        this._parentTitle = _parentTitle;
    }

    public String getChildTitle( )
    {
        return _childTitle;
    }

    public void setChildTitle( String _childTitle )
    {
        this._childTitle = _childTitle;
    }

    // *********************************************************************************************
    // * TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING *
    // * TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING TOSTRING *
    // *********************************************************************************************

    @Override
    public String toString( )
    {
        return "{" + getId( ) + ", parent=" + getParentCodeIdee( ) + ", child=" + getChildCodeIdee( ) + "}";
    }

}
