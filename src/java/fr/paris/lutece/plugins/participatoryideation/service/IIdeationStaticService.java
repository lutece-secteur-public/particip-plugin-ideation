package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.Map;

public interface IIdeationStaticService {

    /**
     * Fill the model with commons objects used in templates for this campagne
     * @param model The model
     * @param strCampagneCode The CampagneCode
     */
    public void fillCampagneStaticContent(Map<String, Object> model, String strCampagneCode);

    /**
     * Fill the model with commons objects used in templates for all campagnes
     * @param model The model
     */
    public void fillAllStaticContent(Map<String, Object> model);

}
