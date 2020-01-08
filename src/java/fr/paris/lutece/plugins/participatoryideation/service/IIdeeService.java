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
package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

public interface IIdeeService
{

    void createIdeeDB( Idee idee ) throws IdeationErrorException;

    void createIdee( Idee idee ) throws IdeationErrorException;

    void removeIdee( Idee idee );

    void removeIdeeByMdp( Idee idee );

    // ReferenceList getArrondissements();
    //
    // Map<String, String> getArrondissementsMap();
    //
    // Double[] getArrondissementLatLong(String strCode);
    //
    // Double[] getParisLatLong();
    //
    // String getArrondissementCode(Integer nNumero);

    ReferenceList getQpvQvaCodesList( );

    Map<String, String> getQpvQvaCodesMap( );

    ReferenceList getHandicapCodesList( );

    Map<String, String> getHandicapCodesMap( );

    ReferenceList getTypeLocalisationList( );

    Map<String, String> getTypeLocalisationMap( );

    /**
     * return true if the idee is published
     * 
     * @param idee
     *            true if the idee is published
     * @return true if yhe idee is published
     */
    boolean isPublished( Idee idee );

    /**
     * Returns a Set containing guid of depositaries, from proposition ids.
     */
    public Set<String> getUniqueUserGuidsIdeesDepositaires( List<Integer> propIds );

    /**
     * Returns a Set containing guid of followers, from proposition ids.
     */
    public Set<String> getUniqueUserGuidsIdeesFollowers( List<Integer> propIds );

}
