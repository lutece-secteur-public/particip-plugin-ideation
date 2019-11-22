package fr.paris.lutece.plugins.participatoryideation.web.etape;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public interface IFormEtape {
	
	List<String> checkValidationErrors(HttpServletRequest request);
	List<String> checkValidationErrorsLocalized(HttpServletRequest request, Locale locale);
	
	boolean isValidated();
	void setValidated(boolean _bValidated);
}
