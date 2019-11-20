package fr.paris.lutece.plugins.ideation.service;

import java.util.Collection;
import java.util.Map;

import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;

public interface IAtelierService {

    /**
     * Check if an idee has been generated from the atelier.
     * 
     * @param nIdAtelier
     *            The identifier of the Atelier object
     * @return the generated idea id, or 0 if not generated
     */
    public int ideeAlreadyGenerated( int nIdAtelier );

}
