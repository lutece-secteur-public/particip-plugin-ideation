package fr.paris.lutece.plugins.participatoryideation.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IdeeExportUtils {
    public static final String ERROR_NOTHING_TO_EXPORT = "participatoryideation.message.csv.error.nothingToExport";
    private static final String CSV = "csv";
    private static final String APPLICATION_CSV = "application/csv";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String ATTACHMENT = "attachment ;filename=\"";
    private static final String DOUBLE_QUOTE = "\"";
    private static final String APPLICATION_OCTET = "application/octet-stream";
    private static final String PRAGMA = "Pragma";
    private static final String PUBLIC_STRING = "public";
    private static final String EXPIRES = "Expires";
    private static final String ZERO = "0";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String CACHE_CONTROL_VALUE = "must-revalidate,post-check=0,pre-check=0";

    /**
     *
     * Creates a new IdeeExportUtils.java object.
     */
    private IdeeExportUtils(  )
    {
    }

    /**
    * Writes the http header in the response
    * @param request the httpServletRequest
    * @param response the http response
    * @param strFileName the name of the file who must insert in the response
    * @param strFileExtension the file extension
    */
    public static void addHeaderResponse( HttpServletRequest request, HttpServletResponse response, String strFileName,
        String strFileExtension )
    {
        response.setHeader( CONTENT_DISPOSITION, ATTACHMENT + strFileName + DOUBLE_QUOTE );

        if ( strFileExtension.equals( CSV ) )
        {
            response.setContentType( APPLICATION_CSV );
        }
        else
        {
            String strMimeType = request.getSession(  ).getServletContext(  ).getMimeType( strFileName );

            if ( strMimeType != null )
            {
                response.setContentType( strMimeType );
            }
            else
            {
                response.setContentType( APPLICATION_OCTET );
            }
        }

        response.setHeader( PRAGMA, PUBLIC_STRING );
        response.setHeader( EXPIRES, ZERO );
        response.setHeader( CACHE_CONTROL, CACHE_CONTROL_VALUE );
    }
}
