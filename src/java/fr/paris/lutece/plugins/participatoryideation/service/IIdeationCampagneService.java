package fr.paris.lutece.plugins.participatoryideation.service;

import fr.paris.lutece.util.ReferenceList;

public interface IIdeationCampagneService {

    /**
     * Generate a new complete ideation campagne.
     * 
     * @return the generated campagne code, or '' if not generated
     */
    public String generate( );

    public String getCampaignWholeArea( String codeCampaign );
    public ReferenceList getCampaignAreas( String codeCampaign );
    public int getCampaignNumberAreas( String codeCampaign );

    // Same as precedent, for last campagne
    public String getCampaignWholeArea( );
    public ReferenceList getCampaignAreas( );
    public int getCampaignNumberAreas( );

}
