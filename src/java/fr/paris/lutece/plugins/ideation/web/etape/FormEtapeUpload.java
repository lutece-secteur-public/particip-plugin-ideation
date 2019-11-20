package fr.paris.lutece.plugins.ideation.web.etape;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

import fr.paris.lutece.plugins.ideation.service.IdeationUploadHandler;
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
			_ideationUploadHandler = SpringContextService.getBean("ideation.IdeeAsynchronousHandler");
		}
		return _ideationUploadHandler.getListUploadedFiles( FIELD_ATTACHED_IMG, request.getSession(  ) );
	}

	public List<FileItem> getDocs(HttpServletRequest request) {
		if (_ideationUploadHandler == null) {
			_ideationUploadHandler = SpringContextService.getBean("ideation.IdeeAsynchronousHandler");
		}
		return _ideationUploadHandler.getListUploadedFiles( FIELD_ATTACHED_DOC, request.getSession(  ) );
	}

	public void reInitFormSession(HttpServletRequest request)
	{
		if (_ideationUploadHandler == null) {
			_ideationUploadHandler = SpringContextService.getBean("ideation.IdeeAsynchronousHandler");
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
            _ideationUploadHandler = SpringContextService.getBean("ideation.IdeeAsynchronousHandler");
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
	
