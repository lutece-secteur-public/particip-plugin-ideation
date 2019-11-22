package fr.paris.lutece.plugins.participatoryideation.business;

import java.sql.Date;

/**
 * This is the business class for storing search infos
 */
public class LinkSearcher {

//    public static final String COLUMN_ID            = "id_link";
//    public static final String COLUMN_CODE_CAMPAGNE = "code_campagne";
//    public static final String COLUMN_CODE_IDEE     = "code_idee";
//    public static final String COLUMN_TITLE         = "title";

    private String  _strCodeCampagne;
    private Integer _nCodeIdee;
    private String  _strTitle;
    
	public String getCodeCampagne() {
		return _strCodeCampagne;
	}
	
	public void setCodeCampagne(String codeCampagne) {
		this._strCodeCampagne = codeCampagne;
	}
	
	public Integer getCodeIdee() {
		return _nCodeIdee;
	}
	
	public void setCodeIdee(Integer codeIdee) {
		this._nCodeIdee = codeIdee;
	}
	
	public String getTitle() {
		return _strTitle;
	}
	
	public void setTitle(String title) {
		this._strTitle = title;
	}


}
