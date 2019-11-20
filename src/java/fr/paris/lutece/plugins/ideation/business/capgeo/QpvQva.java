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

package fr.paris.lutece.plugins.ideation.business.capgeo;

public class QpvQva {
    String _strType;
    String _strId;
    String _strLibelle;
    String _strGpruNom;
    String _strExtBp;
    String _strFid;

    /**
     * @return the type
     */
    public String getType() {
        return _strType;
    }

    /**
     * @param strType the type to set
     */
    public void setType(String strType) {
        _strType = strType;
    }

    /**
     * @return the id
     */
    public String getId() {
        return _strId;
    }

    /**
     * @param strId the id to set
     */
    public void setId(String strId) {
        _strId = strId;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return _strLibelle;
    }

    /**
     * @param strLibelle the libelle to set
     */
    public void setLibelle(String strLibelle) {
        _strLibelle = strLibelle;
    }

    /**
     * @return the GpruNom
     */
    public String getGpruNom() {
        return _strGpruNom;
    }

    /**
     * @param strGpruNom the GpruNom to set
     */
    public void setGpruNom(String strGpruNom) {
        this._strGpruNom = strGpruNom;
    }

    /**
     * @return the ExtBp
     */
    public String getExtBp() {
        return _strExtBp;
    }

    /**
     * @param strExtBp the ExtBp to set
     */
    public void setExtBp(String strExtBp) {
        this._strExtBp = strExtBp;
    }

    /**
     * @return the Fid
     */
    public String getFid() {
        return _strFid;
    }

    /**
     * @param strFid the Fid to set
     */
    public void setFid(String strFid) {
        this._strFid = strFid;
    }
}
