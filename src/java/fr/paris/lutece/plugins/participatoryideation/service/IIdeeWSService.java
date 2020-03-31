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
package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import fr.paris.lutece.plugins.participatoryideation.business.proposal.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.proposal.IdeeSearcher;

public interface IIdeeWSService
{

    /**
     * Load the data of all the idee objects and returns them in form of a collection
     * 
     * @return the collection which contains the data of all the idee objects
     */
    public Collection<Idee> getIdeesList( );

    /**
     * 
     * @param nKey
     * @return
     */
    Idee getIdeeByIdentifiant( int nKey );

    /**
     * 
     * @param nKey
     * @param strCampagne
     * @return
     */
    Idee getIdeeByIdentifiantAndCampagne( int nKey, String strCampagne );

    /**
     * Load the data of all the idee objects searched and returns them in form of a collection
     * 
     * @param ideeSearcher
     *            an IdeeSearcher
     * @return the collection which contains the data of all the idee objects
     */
    public Collection<Idee> getIdeesListSearch( IdeeSearcher ideeSearcher );

    /**
     * Update of the idee which is specified in parameter
     * 
     * @param idee
     *            The instance of the Idee which contains the data to store
     * @return The instance of the idee which has been updated
     */
    public Idee updateIdee( Idee idee );

    public void updateIdee( Idee ideeLutece, Idee ideeEudonet, boolean notify, HttpServletRequest request );

    public void updateIdee( Idee ideeLutece, Idee ideeEudonet, HttpServletRequest request );

    /**
     * Insert comment in module extend comment
     * 
     * @param idee
     */
    public void createComment( Idee idee );

    /**
     * process workflow action by name
     * 
     * @param strWorkflowIdeeActionName
     *            the name of the action to process
     * @param nIdIdee
     *            the id of the idee request the HttpServletRequest request
     */
    void processActionByName( String strWorkflowIdeeActionName, int nIdIdee );

    /**
     * process workflow action by name
     * 
     * @param strWorkflowIdeeActionName
     *            the name of the action to process
     * @param nIdIdee
     *            the id of the idee request the HttpServletRequest request
     */
    void processActionByName( String strWorkflowIdeeActionName, int nIdIdee, HttpServletRequest request );

}
