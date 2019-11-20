package fr.paris.lutece.plugins.ideation.web.etape;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import fr.paris.lutece.plugins.ideation.business.DepositaireType;
import fr.paris.lutece.plugins.ideation.business.DepositaireTypeHome;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.ProfanityFilter;
import fr.paris.lutece.plugins.ideation.service.IdeationProfanityFilter;
import fr.paris.lutece.plugins.ideation.service.IdeeService;
import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.profanityfilter.utils.ProfanityResult;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.util.AppLogService;


public class FormEtapeLocation extends  AbstractFormEtape  {

	private static final java.util.regex.Pattern patternAdresseArrondissement = java.util.regex.Pattern.compile(", 75[0-1]([0-2][0-9]) PARIS");
	private static final String I18N_ERROR_ARRONDISSEMENT_EMPTY="ideation.validation.idee.FormEtapeLocation.Arrondissement.notEmpty";
	private static final String I18N_ERROR_ADRESS_FORMAT="ideation.validation.idee.FormEtapeLocation.AdressFormat";
	private static final String I18N_ERROR_ADRESS_NOT_VALID="ideation.validation.idee.FormEtapeLocation.AdressNotValid";
	private static final String I18N_ERROR_ADRESS_ARDT_MISMATCH="ideation.validation.idee.FormEtapeLocation.ArdtMismatch";
	   
	private static final String I18N_ERROR_COMPLEMENT_EMPTY="ideation.validation.idee.FormEtapeLocation.depositaire_complement.notEmpty";
	private static final String I18N_ERROR_COMPLEMENT_PROFANITY="ideation.validation.idee.FormEtapeLocation.depositaire_complement.profanity";
	
	@NotEmpty( message = "#i18n{ideation.validation.idee.FormEtapeLocation.CodeTheme.notEmpty}" )
    @Size( max = 50 , message = "#i18n{ideation.validation.idee.FormEtapeLocation.CodeTheme.size}" ) 
    private String _strCodeTheme;
	@NotEmpty( message = "#i18n{ideation.validation.idee.FormEtapeLocation.localisationTypeNotEmpty}" )
	private String _strLocalisationType;
	@Pattern( regexp = "^$|(7500[1-9])|(7501[0-9])|(75020)", message = "#i18n{ideation.validation.idee.localisationArdt.pattern}" )
	private String _strLocalisationArdt;
	    
	private String _strGeojson;
	private String _strAdress;
	
	@NotEmpty( message = "#i18n{ideation.validation.idee.FormEtapeLocation.depositaireType.notEmpty}" )
	private String _strDepositaireType;
	@Size( max = 50 , message = "#i18n{ideation.validation.idee.Depositaire.size}" )
	private String _strDepositaire;
	

	
	/**
     * Returns the LocalisationArdt
     * @return The LocalisationArdt
     */
    public String getLocalisationArdt( )
    {
        return _strLocalisationArdt;
    }

    /**
     * Sets the LocalisationArdt
     * @param strLocalisationArdt The LocalisationArdt
     */ 
    public void setLocalisationArdt( String strLocalisationArdt )
    {
    	_strLocalisationArdt = strLocalisationArdt;
    }

	public String getLocalisationType() {
		return _strLocalisationType;
	}

	public void setLocalisationType(String _strLocalisationType) {
		this._strLocalisationType = _strLocalisationType;
	}
	
	 /**
     * Returns the CodeTheme
     * @return The CodeTheme
     */
    public String getCodeTheme( )
    {
        return _strCodeTheme;
    }

    /**
     * Sets the CodeTheme
     * @param strCodeTheme The CodeTheme
     */ 
    public void setCodeTheme( String strCodeTheme )
    {
        _strCodeTheme = strCodeTheme;
    }
    
	
	 public String getGeojson() {
		return _strGeojson;
	}

	public void setGeojson(String _strGeojson) {
		this._strGeojson = _strGeojson;
	}
	
	
      public List<String> checkValidationErrors(HttpServletRequest request)
      {

          List<String> listErrors=new ArrayList<>();
          String strComplementType = DepositaireTypeHome.findByCode( getDepositaireType() ).getCodeComplementType();
          if (!DepositaireType.CODE_COMPLEMENT_TYPE_NONE.equals(strComplementType) && StringUtils.isBlank( getDepositaire())) {
              listErrors.add(I18N_ERROR_COMPLEMENT_EMPTY);
          }
    	  
    	  if (getLocalisationType().equals(Idee.LOCALISATION_TYPE_ARDT) && StringUtils.isEmpty( getLocalisationArdt())) {
    		  listErrors.add(I18N_ERROR_ARRONDISSEMENT_EMPTY);
    	  }
    	  if (StringUtils.isNotBlank(getAdress()) && StringUtils.isEmpty(getGeojson())) {
    		  listErrors.add(I18N_ERROR_ADRESS_NOT_VALID);
    	  }
    	  if (StringUtils.isNotEmpty(getGeojson())) {
                  GeolocItem geolocItem = null;
              
                  try {
                      geolocItem = GeolocItem.fromJSON(this.getGeojson());
                      setAdress(geolocItem.getAddress());
                      Matcher m = patternAdresseArrondissement.matcher(getAdress());
                      m.find();
                      int nArdt;
                      nArdt = Integer.parseInt(m.group(1));
                      String strArdt = IdeeService.getInstance().getArrondissementCode(nArdt);
                      if (getLocalisationType().equals(Idee.LOCALISATION_TYPE_ARDT) &&
                          StringUtils.isNotEmpty( getLocalisationArdt()) &&
                          (!strArdt.equals(getLocalisationArdt())) ) {
                          listErrors.add(I18N_ERROR_ADRESS_ARDT_MISMATCH);
                      } else {
                          setLocalisationArdt(strArdt);
                      }
                  } catch (IOException e) {
                      
                	  listErrors.add(I18N_ERROR_ADRESS_FORMAT);
                      AppLogService.error ( "IdeationApp: malformed data from client: address = " + getGeojson() + "; exeception " + e );
                  }
                 
              }
    	  
    	    return listErrors;
    	 }

    public List<String> checkValidationErrorsLocalized(HttpServletRequest request, Locale locale)
    {
        List<String> listErrors=new ArrayList<>();
        String userUid= "guid";
        if(SecurityService.getInstance().getRegisteredUser(request)!=null)
        {
        	LuteceUser user = SecurityService.getInstance().getRegisteredUser(request);
            userUid= user.getName( );
        }
        
        
        ProfanityResult profanityResult = IdeationProfanityFilter.getInstance().scanString(getDepositaire( ), ProfanityFilter.DEPOSITAIRE_RESOURCE_TYPE, userUid);
        Set<String> swearWords = profanityResult.getSwearWords();
        if (!swearWords.isEmpty()) {
            listErrors.add(I18nService.getLocalizedString(I18N_ERROR_COMPLEMENT_PROFANITY, locale) + StringUtils.join(swearWords.iterator(), ','));
        }
        return listErrors;
    }
	

	public String getAdress() {
		return _strAdress;
	}

	public void setAdress(String _strAdress) {
		this._strAdress = _strAdress;
	}
    	  		
	public String getDepositaireType() {
		return _strDepositaireType;
	}

	public void setDepositaireType(String _strDepositaireType) {
		this._strDepositaireType = _strDepositaireType;
	}

	public String getDepositaire() {
		return _strDepositaire;
	}

	public void setDepositaire(String _strConseilQuartier) {
		this._strDepositaire = _strConseilQuartier;
	}  	

	public boolean mustCopyDepositaire() {
		String strComplementType = DepositaireTypeHome.findByCode(getDepositaireType()).getCodeComplementType();
		return !DepositaireType.CODE_COMPLEMENT_TYPE_NONE.equals(strComplementType);
	}
}
