package fr.paris.lutece.plugins.ideation.business;

import java.sql.Date;

/**
 * This is the business class for storing search infos
 */
public class AtelierSearcher {

	public static final String COLUMN_ID = "id_atelier";
    public static final String COLUMN_TITRE = "titre";
    public static final String COLUMN_CAMPAGNE = "campagne";
    public static final String COLUMN_THEMATIQUE = "thematique";
    public static final String COLUMN_ARRDT = "localisationardt";
    public static final String COLUMN_TYPE = "type";
    public static final String ORDER_ASC = "ASC";
    public static final String ORDER_DESC = "DESC";

    private String _strTitreOuDescription;
    private String _strPhaseActive;
    private int _nNombreCommentaires;
    private int _nNombreVotes;
	private Date _dateLastAction;
	private String _strCodeTheme;
	private String _strCodeCampagne;
	private String _strTypeLocalisation;
	private String _strLocalisationArdt;
	private String _strOrderAscDesc;
	private String _strOrderColumn;
	private String _strType;
	private String _strHandicap;
	
	
	/**
	 * 
	 * @return
	 */
	public String getTitreOuDescription( ) 
	{
		return _strTitreOuDescription;
	}
	/**
	 * 
	 * @param strTitreOuDescription
	 */
	public void setTitreOuDescription( String strTitreOuDescription ) 
	{
		this._strTitreOuDescription = strTitreOuDescription;
	}
	/**
	 * 
	 * @return
	 */
	public String getPhaseActive( ) 
	{
		return _strPhaseActive;
	}
	/**
	 * 
	 * @param strPhaseActive
	 */
	public void setPhaseActive( String strPhaseActive ) 
	{
		_strPhaseActive = strPhaseActive;
	}
	/**
	 * 
	 * @return
	 */
	public int getNombreCommentaires( ) 
	{
		return _nNombreCommentaires;
	}

	public void setNombreCommentaires( int nNombreCommentaires )
	{
		_nNombreCommentaires = nNombreCommentaires;
	}
	/**
	 * 
	 * @return
	 */
	public int getNombreVotes( )
	{
		return _nNombreVotes;
	}

	public void setNombreVotes( int nNombreVotes )
	{
		_nNombreVotes = nNombreVotes;
	}
	/**
	 * 
	 * @return
	 */
	public Date getDateLastAction( )
	{
		return _dateLastAction;
	}
	/**
	 * 
	 * @param _dateLastAction
	 */
	public void setDateLastAction( Date _dateLastAction ) {
		this._dateLastAction = _dateLastAction;
	}
	/**
	 * 
	 * @return
	 */
	public String getLocalisationArdt( )
	{
		return _strLocalisationArdt;
	}
	/**
	 * 
	 * @param strLocalisationArdt
	 */
	public void setLocalisationArdt( String strLocalisationArdt ) 
	{
		_strLocalisationArdt = strLocalisationArdt;
	}

	/**
	 * @return the CodeCampagne
	 */
	public String getCodeCampagne( ) 
	{
		return _strCodeCampagne;
	}

	/**
	 * @param CodeCampagne the CodeCampagne to set
	 */
	public void setCodeCampagne( String CodeCampagne ) 
	{
		this._strCodeCampagne = CodeCampagne;
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
	public void setOrderColumn( String strOrderColumn ) 
	{
		this._strOrderColumn = strOrderColumn;
	}
	/**
	 * 
	 * @return
	 */
	public String getType( ) 
	{
		return _strType;
	}
	/**
	 * 
	 * @param _strType
	 */
	public void setType( String strType ) 
	{
		_strType = strType;
	}
	
	public String getHandicap( ) 
	{
		return _strHandicap;
	}

	public void setHandicap( String strHandicap ) 
	{
		_strHandicap = strHandicap;
	}
	

}
