package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.Map;

public interface ILinkStaticService {

    /**
     * Fill the model with commons objects used in templates for all campagnes
     * @param model The model
     */
    public void fillAllStaticContent(Map<String, Object> model);

}
