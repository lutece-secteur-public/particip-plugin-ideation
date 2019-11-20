package fr.paris.lutece.plugins.ideation.service;

import java.util.Collection;

import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.portal.service.spring.SpringContextService;

public class AtelierService implements IAtelierService {
	
	private static final String BEAN_ATELIER_SERVICE="ideation.atelierService";
	
	private static IAtelierService _singleton;
	
	
	public static IAtelierService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_ATELIER_SERVICE );
        }
        return _singleton;
    }
	
    /**
     * {@inheritDoc}
     */
    @Override
	public int ideeAlreadyGenerated( int nIdAtelier )
    {
        Collection<Integer> linkedIdees = AtelierHome.getIdeeIdsListByAtelier( nIdAtelier );
        int nParentIdeeId = 0;

        if ( linkedIdees != null )
        {
            for ( Integer ideeId : linkedIdees )
            {
                nParentIdeeId = IdeeHome.hasParent( ideeId );

                if ( nParentIdeeId != 0 )
                {
                    break;
                }
            }
        }
        return nParentIdeeId;
    }

}
