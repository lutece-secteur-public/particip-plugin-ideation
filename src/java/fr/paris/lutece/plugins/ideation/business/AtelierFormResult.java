package fr.paris.lutece.plugins.ideation.business;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 *AtelierFormResult
 *
 */
public class AtelierFormResult
{
    // Variables declarations
    private int _nId;
    @NotEmpty( message = "#i18n{ideation.validation.atelierformresult.Guid.notEmpty}" )
    @Size( max = 255 , message = "#i18n{ideation.validation.atelierformresult.Guid.size}" )
    private String _strGuid;
    private int _nIdAtelierForm;
    private int _nNumeroChoixTitre;
    private int _nNumeroChoixDescription;
	private List< AtelierFormResultEntry> _listChoixComplementaires;
    private Timestamp _creationTimestamp;
    
    /**
	 * @return the _creationTimestamp
	 */
	public Timestamp getCreationTimestamp() {
		return _creationTimestamp;
	}

	/**
	 * @param _creationTimestamp the _creationTimestamp to set
	 */
	public void setCreationTimestamp(Timestamp _creationTimestamp) {
		this._creationTimestamp = _creationTimestamp;
	}

	/**
     * Returns the Id
     * @return The Id
     */
    public int getId( )
    {
        return _nId;
    }

    /**
     * Sets the Id
     * @param nId The Id
     */ 
    public void setId( int nId )
    {
        _nId = nId;
    }

	/**
     * Returns the Guid
     * @return The Guid
     */
    public String getGuid()
    {
        return _strGuid;
    }

    /**
     * Sets the Guid
     * @param strGuid The Guid
     */
    public void setGuid( String strGuid )
    {
        _strGuid = strGuid;
    }

    /**
     * Returns the NumeroChoixTitre
     * @return The NumeroChoixTitre
     */
    public int getNumeroChoixTitre()
    {
        return _nNumeroChoixTitre;
    }

    /**
     * Sets the NumeroChoixTitre
     * @param nNumeroChoixTitre The NumeroChoixTitre
     */
    public void setNumeroChoixTitre( int nNumeroChoixTitre )
    {
        _nNumeroChoixTitre = nNumeroChoixTitre;
    }

    /**
     * Returns the NumeroChoixDescription
     * @return The NumeroChoixDescription
     */
    public int getNumeroChoixDescription()
    {
        return _nNumeroChoixDescription;
    }

    /**
     * Sets the NumeroChoixDescription
     * @param nNumeroChoixDescription The NumeroChoixDescription
     */
    public void setNumeroChoixDescription( int nNumeroChoixDescription )
    {
        _nNumeroChoixDescription = nNumeroChoixDescription;
    }

    /**
     * Returns the list of ChoixComplementaires
     * @return The list of ChoixComplementaires
     */
    public List<AtelierFormResultEntry> getListChoixComplementaires()
    {
        return _listChoixComplementaires;
    }

    /**
     * Sets the list of ChoixComplementaires
     * @parsam listChoixComplementaires
     */
    public void setListChoixComplementaires( List<AtelierFormResultEntry> listChoixComplementaires )
    {
        _listChoixComplementaires = listChoixComplementaires;
    }

    /**
     * Returns the IdAtelierForm
     * @return The IdAtelierForm
     */
    public int getIdAtelierForm()
    {
        return _nIdAtelierForm;
    }

    /**
     * Sets the IdAtelierForm
     * @param nIdAtelierForm The IdAtelierForm
     */
    public void setIdAtelierForm( int nIdAtelierForm )
    {
        _nIdAtelierForm = nIdAtelierForm;
    }

	
}
