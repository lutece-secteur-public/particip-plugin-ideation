package fr.paris.lutece.plugins.participatoryideation.web.etape;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;import org.hibernate.validator.constraints.NotEmpty;

import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;


public class FormEtapeTitle extends  AbstractFormEtape  {

	private static final String I18N_ERROR_TITRE_MIN_LENGTH="participatoryideation.validation.idee.Titre.sizeMin";
	private static final String I18N_ERROR_TITRE_MAX_LENGTH="participatoryideation.validation.idee.Titre.sizeMax";
	
	private static final String I18N_ERROR_DEJADEPOSE_MIN_LENGTH="participatoryideation.validation.idee.Dejadepose.sizeMin";
	private static final String I18N_ERROR_DEJADEPOSE_MAX_LENGTH="participatoryideation.validation.idee.Dejadepose.sizeMax";
	
	private static final String I18N_ERROR_CREATIONMETHOD_MIN_LENGTH="participatoryideation.validation.idee.Creationmethod.sizeMin";
	private static final String I18N_ERROR_CREATIONMETHOD_MAX_LENGTH="participatoryideation.validation.idee.Creationmethod.sizeMax";
	
	private static final String DSKEY_TITRE_MIN_LENGTH="participatoryideation.site_property.form.titre.minLength";
	private static final String DSKEY_TITRE_MAX_LENGTH="participatoryideation.site_property.form.titre.maxLength";
	
	private static final String DSKEY_DEJADEPOSE_MIN_LENGTH="participatoryideation.site_property.form.dejadepose.minLength";
	private static final String DSKEY_DEJADEPOSE_MAX_LENGTH="participatoryideation.site_property.form.dejadepose.maxLength";
	
	private static final String DSKEY_CREATIONMETHOD_MIN_LENGTH="participatoryideation.site_property.form.creationmethod.minLength";
	private static final String DSKEY_CREATIONMETHOD_MAX_LENGTH="participatoryideation.site_property.form.creationmethod.maxLength";
	
	@NotEmpty( message = "#i18n{ideation.validation.idee.Titre.notEmpty}" )
    @Size( max = 255 , message = "#i18n{ideation.validation.idee.Titre.size}" ) 
    private String _strTitre;
	
	private String _strDejadepose;

	private String _strCreationmethod;


	public String getTitre() {
		return _strTitre;
	}

	public void setTitre(String strTitre) {
		this._strTitre = strTitre;
	}
	
    public String getDejadepose() {
		return _strDejadepose;
	}

	public void setDejadepose(String strDejadepose) {
		this._strDejadepose = strDejadepose;
	}

    public String getCreationmethod() {
		return _strCreationmethod;
	}

	public void setCreationmethod(String strCreationmethod) {
		this._strCreationmethod = strCreationmethod;
	}

	public List<String> checkValidationErrorsLocalized(HttpServletRequest request, Locale locale)
    {
        List<String> listErrors=new ArrayList<>();
        String userUid= "guid";
        
        LuteceUser user = SecurityService.getInstance().getRegisteredUser(request);
        if( user !=null )
        {
        	userUid= user.getName( );
        }
 
        String strMax = DatastoreService.getDataValue(DSKEY_TITRE_MAX_LENGTH,"");
        if (!"".equals(strMax)) {
            try {
                int nMax = Integer.parseInt(strMax);
                if (getTitre().trim().length() > nMax) {
                    listErrors.add(I18nService.getLocalizedString(I18N_ERROR_TITRE_MAX_LENGTH,
                        new String[] { Integer.toString(nMax) }, locale));
                }
            } catch (NumberFormatException nfe) {
                AppLogService.error("IdeationApp: NumberFormatException when parsing max Titre length from datastore, key " + DSKEY_TITRE_MAX_LENGTH, nfe);
            }
        }

        String strMin = DatastoreService.getDataValue(DSKEY_TITRE_MIN_LENGTH,"");
        if (!"".equals(strMin)) {
            try {
                int nMin = Integer.parseInt(strMin);
                if (getTitre().trim().length() < nMin) {
                    listErrors.add(I18nService.getLocalizedString(I18N_ERROR_TITRE_MIN_LENGTH,
                        new String[] { Integer.toString(nMin) }, locale));
                }
            } catch (NumberFormatException nfe) {
                AppLogService.error("IdeationApp: NumberFormatException when parsing max Titre length from datastore, key " + DSKEY_TITRE_MAX_LENGTH, nfe);
            }
        }
        
        if( getDejadepose( ).trim().length( ) > 0 )
        {
        	String strMinDejadepose = DatastoreService.getDataValue(DSKEY_DEJADEPOSE_MIN_LENGTH,"");
            if (!"".equals(strMinDejadepose)) {
                try {
                    int nMin = Integer.parseInt(strMinDejadepose);
                    if (getDejadepose().trim().length() < nMin) {
                        listErrors.add(I18nService.getLocalizedString(I18N_ERROR_DEJADEPOSE_MIN_LENGTH,
                            new String[] { Integer.toString(nMin) }, locale));
                    }
                } catch (NumberFormatException nfe) {
                    AppLogService.error("IdeationApp: NumberFormatException when parsing min Dejadepose length from datastore, key " + DSKEY_DEJADEPOSE_MIN_LENGTH, nfe);
                }
            }
            
            String strMaxDejadepose = DatastoreService.getDataValue(DSKEY_DEJADEPOSE_MAX_LENGTH,"");
            if (!"".equals(strMaxDejadepose)) {
                try {
                    int nMax = Integer.parseInt(strMaxDejadepose);
                    if (getDejadepose().trim().length() > nMax) {
                        listErrors.add(I18nService.getLocalizedString(I18N_ERROR_DEJADEPOSE_MAX_LENGTH,
                            new String[] { Integer.toString(nMax) }, locale));
                    }
                } catch (NumberFormatException nfe) {
                    AppLogService.error("IdeationApp: NumberFormatException when parsing max Dejadepose length from datastore, key " + DSKEY_DEJADEPOSE_MAX_LENGTH, nfe);
                }
            }
        }
        
        if( getCreationmethod( ).trim().length( ) > 0 )
        {
        	String strMinCreationmethod = DatastoreService.getDataValue(DSKEY_CREATIONMETHOD_MIN_LENGTH,"");
            if (!"".equals(strMinCreationmethod)) {
                try {
                    int nMin = Integer.parseInt(strMinCreationmethod);
                    if (getCreationmethod().trim().length() < nMin) {
                        listErrors.add(I18nService.getLocalizedString(I18N_ERROR_CREATIONMETHOD_MIN_LENGTH,
                            new String[] { Integer.toString(nMin) }, locale));
                    }
                } catch (NumberFormatException nfe) {
                    AppLogService.error("IdeationApp: NumberFormatException when parsing min Creationmethod length from datastore, key " + DSKEY_CREATIONMETHOD_MIN_LENGTH, nfe);
                }
            }
            
            String strMaxCreationmethod = DatastoreService.getDataValue(DSKEY_CREATIONMETHOD_MAX_LENGTH,"");
            if (!"".equals(strMaxCreationmethod)) {
                try {
                    int nMax = Integer.parseInt(strMaxCreationmethod);
                    if (getCreationmethod().trim().length() > nMax) {
                        listErrors.add(I18nService.getLocalizedString(I18N_ERROR_CREATIONMETHOD_MAX_LENGTH,
                            new String[] { Integer.toString(nMax) }, locale));
                    }
                } catch (NumberFormatException nfe) {
                    AppLogService.error("IdeationApp: NumberFormatException when parsing max Creationmethod length from datastore, key " + I18N_ERROR_CREATIONMETHOD_MAX_LENGTH, nfe);
                }
            }
        }
        
        return listErrors;
    }
	  		
	
	
}
