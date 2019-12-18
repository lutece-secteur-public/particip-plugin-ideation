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
package fr.paris.lutece.plugins.participatoryideation.web.etape;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.participatoryideation.service.IdeationUploadHandler;
import fr.paris.lutece.portal.service.spring.SpringContextService;


public class FormEtapeUpload extends  AbstractFormEtape {

    public static final String FIELD_ATTACHED_IMG = "imgs";
    public static final String FIELD_ATTACHED_DOC = "docs";
	
	private String _strAcceptExploit;
	IdeationUploadHandler _ideationUploadHandler;
	

	public String getAcceptExploit() {
		return _strAcceptExploit;
	}



	public void setAcceptExploit(String _strAcceptExploit) {
		this._strAcceptExploit = _strAcceptExploit;
	}
	
	public List<FileItem> getImgs(HttpServletRequest request) {
		if (_ideationUploadHandler == null) {
			_ideationUploadHandler = SpringContextService.getBean("participatoryideation.IdeeAsynchronousHandler");
		}
		return _ideationUploadHandler.getListUploadedFiles( FIELD_ATTACHED_IMG, request.getSession(  ) );
	}

	public List<FileItem> getDocs(HttpServletRequest request) {
		if (_ideationUploadHandler == null) {
			_ideationUploadHandler = SpringContextService.getBean("participatoryideation.IdeeAsynchronousHandler");
		}
		return _ideationUploadHandler.getListUploadedFiles( FIELD_ATTACHED_DOC, request.getSession(  ) );
	}

	public void reInitFormSession(HttpServletRequest request)
	{
		if (_ideationUploadHandler == null) {
			_ideationUploadHandler = SpringContextService.getBean("participatoryideation.IdeeAsynchronousHandler");
		}
		_ideationUploadHandler.removeSessionFiles( request.getSession(  ).getId(  ) );
	}
	

    /**
     * Process the synchronous (no javascript) uploads
     * @param request the request
     * @return wheter an action was done by the user, and we need to redisplay the same form
     */
    public boolean populateSynchronousUpload( HttpServletRequest request ) {
        if (_ideationUploadHandler == null) {
            _ideationUploadHandler = SpringContextService.getBean("participatoryideation.IdeeAsynchronousHandler");
        }

        // Files are only removed if a given flag is in the request
        _ideationUploadHandler.doRemoveFile( request, FIELD_ATTACHED_IMG );
        _ideationUploadHandler.doRemoveFile( request, FIELD_ATTACHED_DOC );

        // Files are only added if a given flag is in the request
        _ideationUploadHandler.addFilesUploadedSynchronously( request, FIELD_ATTACHED_IMG );
        _ideationUploadHandler.addFilesUploadedSynchronously( request, FIELD_ATTACHED_DOC );

        return  _ideationUploadHandler.hasRemoveFlag( request, FIELD_ATTACHED_IMG ) ||
                    _ideationUploadHandler.hasAddFileFlag( request, FIELD_ATTACHED_IMG ) ||
                    _ideationUploadHandler.hasRemoveFlag( request, FIELD_ATTACHED_DOC ) ||
                    _ideationUploadHandler.hasAddFileFlag( request, FIELD_ATTACHED_DOC );
    }

}
	
