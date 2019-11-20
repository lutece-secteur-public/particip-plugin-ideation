package fr.paris.lutece.plugins.ideation.business;

/**
 * This is the business class for storing search infos
 */
public class IdeeSearcher {

    public static final String COLUMN_REFERENCE = "reference";
    public static final String COLUMN_DATE_CREATION = "creation_timestamp";
    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

	public static final String QPVQVA_UNKNOWN = "UNKNOWN";

	private String _strCodeCampagne;
	private String _strCodeTheme;
	private Integer _nExportedTag;
	private String _strTitreOuDescriptionOuRef;
	private Integer _nIdWorkflowState;
	private Integer _nIdFoTag;
	private Integer _nIdBoTag;
	private String _strTypeQpvQva;
	private String _strHandicap;
	private String _strTypeLocalisation;
	private String _strArrondissement;
	private String _strLuteceUserName;

	private String _strOrderAscDesc;
	private String _strOrderColumn;
	private String _strStatusPublic;
	private Boolean _bIsPublished;
	

	/**
	 * @return the CodeCampagne
	 */
	public String getCodeCampagne() {
		return _strCodeCampagne;
	}

	/**
	 * @param CodeCampagne the CodeCampagne to set
	 */
	public void setCodeCampagne(String CodeCampagne) {
		this._strCodeCampagne = CodeCampagne;
	}

	/**
	 * @return the CodeTheme
	 */
	public String getCodeTheme() {
		return _strCodeTheme;
	}

	/**
	 * @param CodeTheme the CodeTheme to set
	 */
	public void setCodeTheme(String CodeTheme) {
		this._strCodeTheme = CodeTheme;
	}
	
	/**
	 * Returns the exported Tag
	 * 
	 * @return The exported Tag
	 */
	public Integer getExportedTag() {
		return _nExportedTag;
	}

	/**
	 * Sets the _nExportedTag
	 * 
	 * @param The Exported Tagd
	 *            
	 */
	public void setExportedTag(Integer nExportedTag) {
		_nExportedTag = nExportedTag;
	}

	/**
	 * @return the TitreOuDescription
	 */
	public String getTitreOuDescriptionouRef() {
		return _strTitreOuDescriptionOuRef;
	}

	/**
	 * @param strTitreOuDescription the TitreOuDescription to set
	 */
	public void setTitreOuDescriptionouRef(String strTitreOuDescription) {
		this._strTitreOuDescriptionOuRef = strTitreOuDescription;
	}

	/**
	 * @return the IdWorkflowState
	 */
	public Integer getIdWorkflowState() {
		return _nIdWorkflowState;
	}

	/**
	 * @param nIdWorkflowState the IdWorkflowState to set
	 */
	public void setIdWorkflowState(Integer nIdWorkflowState) {
		this._nIdWorkflowState = nIdWorkflowState;
	}

	/**
	 * @return the IdFoTag
	 */
	public Integer getIdFoTag() {
		return _nIdFoTag;
	}

	/**
	 * @param nIdFoTag the IdFoTag to set
	 */
	public void setIdFoTag(Integer nIdFoTag) {
		this._nIdFoTag = nIdFoTag;
	}

	/**
	 * @return the IdBoTag
	 */
	public Integer getIdBoTag() {
		return _nIdBoTag;
	}

	/**
	 * @param nIdBoTag the IdBoTag to set
	 */
	public void setIdBoTag(Integer nIdBoTag) {
		this._nIdBoTag = nIdBoTag;
	}

	/**
	 * @return the TypeQpvQva
	 */
	public String getTypeQpvQva() {
		return _strTypeQpvQva;
	}

	/**
	 * @param strTypeQpvQva the TypeQpvQva to set
	 */
	public void setTypeQpvQva(String strTypeQpvQva) {
		this._strTypeQpvQva = strTypeQpvQva;
	}

	public String getHandicap() {
		return _strHandicap;
	}

	public void setHandicap(String strHandicap) {
		this._strHandicap = strHandicap;
	}


	/**
	 * @return the TypeLocalisation
	 */
	public String getTypeLocalisation() {
		return _strTypeLocalisation;
	}

	/**
	 * @param strTypeLocalisation the TypeLocalisation to set
	 */
	public void setTypeLocalisation(String strTypeLocalisation) {
		this._strTypeLocalisation = strTypeLocalisation;
	}
	
	/**
	 * @return the Arrondissement
	 */
	public String getArrondissement() {
		return _strArrondissement;
	}

	/**
	 * @param strArrondissement the Arrondissement to set
	 */
	public void setArrondissement(String strArrondissement) {
		this._strArrondissement = strArrondissement;
	}
	
	/** 
	 * @return the lutece User Name
	 */
	public String getLuteceUserName() {
		return _strLuteceUserName;
	}

	/**
	 * set Lutece User Name
	 * 
	 * @param _strLuteceUserName
	 *            the lutece User Name
	 */
	public void setLuteceUserName(String _strLuteceUserName) {
		this._strLuteceUserName = _strLuteceUserName;
	}

	/**
	 * @return the OrderAscDesc
	 */
	public String getOrderAscDesc() {
		return _strOrderAscDesc;
	}

	/**
	 * @param strOrderAscDesc the OrderAscDesc to set
	 */
	public void setOrderAscDesc(String strOrderAscDesc) {
		this._strOrderAscDesc = strOrderAscDesc;
	}

	/**
	 * @return the OrderColumn
	 */
	public String getOrderColumn() {
		return _strOrderColumn;
	}

	/**
	 * @param strOrderColumn the OrderColumn to set
	 */
	public void setOrderColumn(String strOrderColumn) {
		this._strOrderColumn = strOrderColumn;
	}
	
	/**
	 * Returns the Status
	 * 
	 * @return The Status
	 */
	public String getStatusPublic() {
		return _strStatusPublic;
	}

	/**
	 * Sets the Status
	 * 
	 * @param status
	 *            The Status
	 */
	public void setStatusPublic(String strStatus) {
		_strStatusPublic = strStatus;
	}

	public Boolean getIsPublished() {
		return _bIsPublished;
	}

	public void setIsPublished(Boolean _bIsPublished) {
		this._bIsPublished = _bIsPublished;
	}
}
