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
package fr.paris.lutece.plugins.participatoryideation.business.capgeo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;

/*
 * Sample from capgeo:
{
  "features" : [
    {
      "attributes" : {
        "FID" : 165, 
        "C_NQPV" : " ", 
        "L_NQPV" : " ", 
        "C_CAINSEE" : 0, 
        "C_DEP" : 0, 
        "D_MAJ" : null, 
        "C_NAT_QPV" : " ", 
        "SHAPE_Leng" : 0.035989042152, 
        "SHAPE_Area" : 4.10737008582E-05, 
        "GPRU_NOM" : "Joseph Bedier Porte d'Ivry", 
        "EXT_BP" : null
      }
    }, 
    {
      "attributes" : {
        "FID" : 1, 
        "C_NQPV" : "QP095040", 
        "L_NQPV" : "Le Village", 
        "C_CAINSEE" : 95487, 
        "C_DEP" : 95, 
        "D_MAJ" : 1422489600000, 
        "C_NAT_QPV" : "NQPV", 
        "SHAPE_Leng" : 0.0238159761081, 
        "SHAPE_Area" : 1.42189842196E-05, 
        "GPRU_NOM" : " ", 
        "EXT_BP" : null
      }
    }, 
  ]
}

*/

public class QpvQvaRestResponse {

    public static final String PARAMETER_ATTRIBUTES = "attributes";
    public static final String PARAMETER_FID = "FID";
    public static final String QPVQVA_PARAMETER_ID = "C_NQPV";
    public static final String QPVQVA_PARAMETER_LIBELLE = "L_NQPV";
    public static final String QPVQVA_PARAMETER_TYPE = "C_NAT_QPV";
    public static final String GPRU_PARAMETER_NOM = "GPRU_NOM";
    public static final String QBP_PARAMETER_EXT_BP = "EXT_BP";
    List<QpvQva> _features;

    @JsonProperty( "features" )
    public void  setFeatures( List<Map<String, Map<String, String>>> features) {
        List<QpvQva> listFeatures = new ArrayList<QpvQva>();
        for (Map<String, Map<String, String>> feature: features) {
            QpvQva qpvqva = new QpvQva();
            Map<String, String> attributes = feature.get(PARAMETER_ATTRIBUTES);
            qpvqva.setType(attributes.get(QPVQVA_PARAMETER_TYPE));
            qpvqva.setId(attributes.get(QPVQVA_PARAMETER_ID));
            qpvqva.setLibelle(attributes.get(QPVQVA_PARAMETER_LIBELLE));
            qpvqva.setGpruNom(attributes.get(GPRU_PARAMETER_NOM));
            qpvqva.setExtBp(attributes.get(QBP_PARAMETER_EXT_BP));
            qpvqva.setFid(attributes.get(PARAMETER_FID));
            listFeatures.add(qpvqva);
        }
        _features = listFeatures;
    }

    /**
     * @return the features
     */
    public List<QpvQva> getFeatures() {
        return _features;
    }
}
