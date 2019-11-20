package fr.paris.lutece.plugins.ideation.web.etape;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractFormEtape  implements IFormEtape {

	boolean _bValidated;
	
	@Override
	public List<String> checkValidationErrors(HttpServletRequest request) {
	
		return null;
	}
	@Override
	public List<String> checkValidationErrorsLocalized(HttpServletRequest request, Locale locale) {
	
		return null;
	}
	
	public boolean isValidated() {
		return _bValidated;
	}
	public void setValidated(boolean _bValidated) {
		this._bValidated = _bValidated;
	}
}
