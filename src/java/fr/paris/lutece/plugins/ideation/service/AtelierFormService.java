package fr.paris.lutece.plugins.ideation.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import fr.paris.lutece.plugins.ideation.business.AtelierFormResult;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultEntry;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultEntryHome;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class AtelierFormService implements IAtelierFormService {

	
	private static final String BEAN_ATELIER_FORM_SERVICE="ideation.atelierFormService";
	private static IAtelierFormService _singleton;
	private static final String BEAN_TRANSACTION_MANAGER="ideation.ideeServiceTransactionManager";
	
	
	public static IAtelierFormService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_ATELIER_FORM_SERVICE );
           
          }

        return _singleton;
    }
	 //Don't forget to use InnoDB tables for the following tables!
    //core_file, core_physical_file, ideation_idees, ideation_idees_files
    //Check with:
    //sql> show table status ;
	@Transactional( BEAN_TRANSACTION_MANAGER )
	public  synchronized void doSaveVote(AtelierFormResult atelierFormResult,List<AtelierFormResultEntry> listAtelierFormResult)
	{
		
		atelierFormResult.setCreationTimestamp( new java.sql.Timestamp( ( new java.util.Date(  ) ).getTime(  ) ) );
		AtelierFormResultHome.create( atelierFormResult );
		for(AtelierFormResultEntry entry:listAtelierFormResult)
		{
			   entry.setIdAtelierFormResult( atelierFormResult.getId( ) );
               AtelierFormResultEntryHome.create( entry );
        }
			
		
	}

	
	
}
