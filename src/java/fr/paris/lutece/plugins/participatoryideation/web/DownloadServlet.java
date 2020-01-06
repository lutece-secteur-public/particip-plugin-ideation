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
package fr.paris.lutece.plugins.participatoryideation.web;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFile;
import fr.paris.lutece.portal.business.physicalfile.PhysicalFileHome;


/**
 * Servlet serving document file resources
 */
public class DownloadServlet extends HttpServlet
{
	/**
	 *
	 */
	private static final long serialVersionUID = 8625639667629973645L;
    private static final String PARAMETER_ID = "id";

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException the servlet Exception
     * @throws IOException the io exception
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
        throws ServletException, IOException
    {
        String strResourceId = request.getParameter( PARAMETER_ID );
        File file;

        if ( strResourceId != null )
        {
            int nResourceId = Integer.parseInt( strResourceId );
			///XXX don't load the file in memory with getBytes, use getBinaryStream instead
			//use
			//int idx = 2;
			//InputStream inputStream = result.getBinaryStream(idx);
			//int fileLength = inputStream.available();
			//byte[] buffer = new byte[4096];
			//int bytesRead = -1;
			//
			//while ((bytesRead = inputStream.read(buffer)) != -1) {
			//outStream.write(buffer, 0, bytesRead);
			//}
            file = FileHome.findByPrimaryKey( nResourceId );
			PhysicalFile physicalFile = ( file.getPhysicalFile(  ) != null )
				? PhysicalFileHome.findByPrimaryKey( file.getPhysicalFile(  ).getIdPhysicalFile(  ) ) : null;
			if ( physicalFile != null ) {
                // set content properties and header attributes for the response
                response.setContentType( file.getMimeType(  ) );
                response.setContentLength( file.getSize(  ) );
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", file.getTitle());
                response.setHeader(headerKey, headerValue);

                OutputStream out = response.getOutputStream(  );
                out.write( physicalFile.getValue(  ) );
                out.flush(  );
                out.close(  );
			}
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return message
     */
    @Override
    public String getServletInfo(  )
    {
        return "Servlet serving files content from core_file and core_physical_file tables";
    }
}
