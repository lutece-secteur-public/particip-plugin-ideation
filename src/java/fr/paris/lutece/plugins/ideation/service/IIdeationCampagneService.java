package fr.paris.lutece.plugins.ideation.service;

public interface IIdeationCampagneService {

    /**
     * Generate a new complete ideation campagne.
     * 
     * @return the generated campagne code, or '' if not generated
     */
    public String generate( );

}
