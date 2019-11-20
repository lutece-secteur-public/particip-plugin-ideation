package fr.paris.lutece.plugins.ideation.service;

import java.util.Collection;
import java.util.Set;

import fr.paris.lutece.plugins.ideation.business.ProfanityFilter;
import fr.paris.lutece.plugins.ideation.business.ProfanityFilterHome;
import fr.paris.lutece.plugins.ideation.business.ProfanityFilterSearcher;
import fr.paris.lutece.plugins.profanityfilter.utils.ProfanityResult;
import fr.paris.lutece.portal.service.spring.SpringContextService;


public class IdeationProfanityFilter {
	
	private static IdeationProfanityFilter _singleton;
	private static final String BEAN_PFFILTER_SERVICE="ideation.ideationProfanityFilter";

	
	/**
     * Get the unique instance of the IdeationProfanityFilter
     * @return The instance
     */
	public static IdeationProfanityFilter getInstance(  )
    {
		if(_singleton == null){

			_singleton= SpringContextService.getBean( BEAN_PFFILTER_SERVICE );
		}
		return _singleton;
		 
    }
	
	
	public ProfanityResult scanString(String strVal, String ressourceType, String userUid) 
	{
		
		
		ProfanityResult profanityResult = fr.paris.lutece.plugins.profanityfilter.service.ProfanityFilter.getService().checkStringCounter( strVal, ressourceType );
        Set<String> swearWords = profanityResult.getSwearWords();
        
        for(String strWord:swearWords){
        	
        	ProfanityFilter prfilter= findRow( strWord, ressourceType, userUid);
        	if(prfilter != null){
        		
        		prfilter.incCounter(1);
        		ProfanityFilterHome.update(prfilter);
        
        	}else{
        		
        		ProfanityFilter _proFilter = new ProfanityFilter( );
        		_proFilter.setRessourceType(ressourceType);
        		_proFilter.setUidUser(userUid);
        		_proFilter.setWord(strWord);
        		_proFilter.setCounter(1);
        		ProfanityFilterHome.create(_proFilter);
        	}
        	
        }
        
        
		return profanityResult;
		
	}
    
	public ProfanityFilter findRow(String strWord, String ressourceType, String userUid){
		
		
		ProfanityFilterSearcher pfSearch= new ProfanityFilterSearcher( );

		pfSearch.setRessourceType(ressourceType);
		pfSearch.setUidUser(userUid);
		pfSearch.setWord(strWord);
		
		
		Collection<ProfanityFilter> _pf= null;

		_pf= ProfanityFilterHome.getProfanityFilterListSearch(pfSearch);
		
		if(_pf != null){
			
			for(ProfanityFilter pfilter:_pf){
				return pfilter;
			}	
		}
			return null;
		
	}

}
