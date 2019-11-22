package fr.paris.lutece.plugins.participatoryideation.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

import fr.paris.lutece.plugins.participatorybudget.business.campaign.Campagne;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhase;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagnePhaseHome;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneTheme;
import fr.paris.lutece.plugins.participatorybudget.business.campaign.CampagneThemeHome;
import fr.paris.lutece.plugins.participatorybudget.service.campaign.CampagnesService;
import fr.paris.lutece.plugins.participatoryideation.business.CampagneDepositaire;
import fr.paris.lutece.plugins.participatoryideation.business.CampagneDepositaireHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class IdeationCampagneService implements IIdeationCampagneService {
	
	private static final String BEAN_IDEATIONCAMPAGNE_SERVICE = "ideation.ideationCampagneService";
	
	private static IIdeationCampagneService _singleton;
	
	public static IIdeationCampagneService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEATIONCAMPAGNE_SERVICE );
        }
        return _singleton;
    }
	
	
	// ***********************************************************************************
	// * GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERAT *
	// * GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERATE GENERAT *
	// ***********************************************************************************
	
    /**
     * {@inheritDoc}
     */
    @Override
	public String generate( )
    {
    	// Creates new campagne ---------------------------------------------------------------------------
    	
    	Campagne lastCampagne = CampagneHome.getLastCampagne();
    	char newCampagneCode = lastCampagne.getCode().charAt(0);
    	newCampagneCode += 1;
    	
    	int campagneYear = 2014 + (newCampagneCode- 'A');

    	Campagne newCampagne = new Campagne();
    	newCampagne.setCode               (newCampagneCode + "");
    	newCampagne.setTitle              ("Budget Participatif " + campagneYear);
    	newCampagne.setDescription        ("Campagne du budget participatif Paris " + campagneYear);
    	newCampagne.setActive             (true);
    	newCampagne.setCodeModerationType ("NONE");
    	newCampagne.setModerationDuration (0);
    	
        CampagneHome.create( newCampagne );
    	
        // Creates phases ---------------------------------------------------------------------------------
        
        Collection<CampagnePhase> lastPhases = CampagnePhaseHome.getCampagnePhasesListByCampagne( lastCampagne.getCode() );
        
        for (CampagnePhase lastPhase : lastPhases) {
        	
			CampagnePhase phase = new CampagnePhase();

			Calendar newStart = Calendar.getInstance(); newStart.setTime( lastPhase.getStart() ); newStart.add( Calendar.YEAR, 1 );
			Calendar newEnd   = Calendar.getInstance();   newEnd.setTime(   lastPhase.getEnd() );   newEnd.add( Calendar.YEAR, 1 );
			
			phase.setCodePhaseType ( lastPhase.getCodePhaseType() );
			phase.setCodeCampagne  ( "" + newCampagneCode );
			phase.setStart         ( new Timestamp(newStart.getTimeInMillis()) );
			phase.setEnd           ( new Timestamp(  newEnd.getTimeInMillis()) );
			
			CampagnePhaseHome.create( phase );
		}
    	
    	// Creates themes ---------------------------------------------------------------------------------
    	
        Collection<CampagneTheme> lastThemes = CampagneThemeHome.getCampagneThemesListByCampagne( lastCampagne.getCode() );
        
        for (CampagneTheme lastTheme : lastThemes) {
        	
        	CampagneTheme theme = new CampagneTheme();

			theme.setCode         ( lastTheme.getCode() );
			theme.setCodeCampagne ( "" + newCampagneCode );
			theme.setTitle        ( lastTheme.getTitle() );
			theme.setDescription  ( lastTheme.getDescription() );
			theme.setActive       ( true );
			
			CampagneThemeHome.create( theme );
		}
        
    	// Creates depositary ----------------------------------------------------------------------------- 
    	
        Collection<CampagneDepositaire> lastDepositaires = CampagneDepositaireHome.getCampagneDepositaireListByCampagne( lastCampagne.getCode() );
        
        for (CampagneDepositaire lastDepositaire : lastDepositaires) {
        	
        	CampagneDepositaire depositaire = new CampagneDepositaire();

        	depositaire.setCodeDepositaireType ( lastDepositaire.getCodeDepositaireType() );
        	depositaire.setCodeCampagne        ( "" + newCampagneCode );
			
			CampagneDepositaireHome.create( depositaire );
		}
        
        // Reseting cache
        
        CampagnesService.getInstance().reset();
        
        return newCampagne.getCode();
    }

}
