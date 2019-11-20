package fr.paris.lutece.plugins.ideation.service;

import java.util.List;

import fr.paris.lutece.plugins.ideation.business.AtelierFormResult;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultEntry;

public interface IAtelierFormService {
	
	void doSaveVote(AtelierFormResult atelierFormResult,List<AtelierFormResultEntry> listAtelierFormResult);

}
