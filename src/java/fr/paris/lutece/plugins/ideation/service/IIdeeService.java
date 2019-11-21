package fr.paris.lutece.plugins.ideation.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.business.IdeeHome;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;

public interface IIdeeService {

	void createIdeeDB(Idee idee) throws IdeationErrorException;

	void createIdee(Idee idee) throws IdeationErrorException;

	void removeIdee(Idee idee);

	void removeIdeeByMdp(Idee idee);

	ReferenceList getArrondissements();

	Map<String, String> getArrondissementsMap();

	Double[] getArrondissementLatLong(String strCode);

	Double[] getParisLatLong();

	String getArrondissementCode(Integer nNumero);

	ReferenceList getQpvQvaCodesList();

	Map<String, String> getQpvQvaCodesMap();

	ReferenceList getHandicapCodesList();

	Map<String, String> getHandicapCodesMap();

	ReferenceList getTypeLocalisationList();

	Map<String, String> getTypeLocalisationMap();

	/**
	 * return true if the idee is published
	 * 
	 * @param idee
	 *            true if the idee is published
	 * @return true if yhe idee is published
	 */
	boolean isPublished(Idee idee);

	/**
	 * Returns a Set containing guid of depositaries, from proposition ids.
	 */
	public Set<String> getUniqueUserGuidsIdeesDepositaires(List<Integer> propIds);

	/**
	 * Returns a Set containing guid of followers, from proposition ids.
	 */
	public Set<String> getUniqueUserGuidsIdeesFollowers(List<Integer> propIds);

}
