/*
 * Copyright (c) 2002-2019, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.participatoryideation.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.transaction.annotation.Transactional;

import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.follow.service.extender.FollowResourceExtender;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeSearcher;
import fr.paris.lutece.plugins.participatoryideation.utils.constants.IdeationConstants;
import fr.paris.lutece.plugins.participatoryideation.web.IdeationApp;
import fr.paris.lutece.portal.business.file.File;
import fr.paris.lutece.portal.business.file.FileHome;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.ReferenceList;

public class IdeeService implements IIdeeService {
	
	private static final String PROPERTY_ARRONDISSEMENT = "participatoryideation.arrondissement.";
    private static final String PROPERTY_ARRONDISSEMENT_EMPTY = "participatoryideation.arrondissementEmpty";
    private static final String PROPERTY_LABEL_NQPV = "participatoryideation.qpvqva.nqpv.label";
    private static final String PROPERTY_LABEL_QVA = "participatoryideation.qpvqva.qva.label";
    private static final String PROPERTY_LABEL_GPRU = "participatoryideation.qpvqva.gpru.label";
    private static final String PROPERTY_LABEL_QBP = "participatoryideation.qpvqva.qbp.label";
    private static final String PROPERTY_LABEL_ERR = "participatoryideation.qpvqva.err.label";
    private static final String PROPERTY_LABEL_NON = "participatoryideation.qpvqva.non.label";
    private static final String PROPERTY_LABEL_UNK = "participatoryideation.qpvqva.unk.label";
    private static final String PROPERTY_LABEL_ARDT = "participatoryideation.localisation_type.ardt.label";
    private static final String PROPERTY_LABEL_PARIS = "participatoryideation.localisation_type.paris.label";

    private static final String PROPERTY_HANDICAP_LABEL_YES = "participatoryideation.handicap.yes.label";
    private static final String PROPERTY_HANDICAP_LABEL_NO  = "participatoryideation.handicap.no.label";
  
	private static volatile ReferenceList _listArrondissements;
	private static volatile Map<String, String> _mapArrondissements;
	private static volatile Map<String,  Double[]> _hashArrondissementsLatLons;
	private static volatile Map<Integer, String> _hashArrondissementsCode;
	private static volatile ReferenceList _listQpvQvaCodes;
	private static volatile Map<String, String> _mapQpvQvaCodes;
	private static volatile ReferenceList       _listHandicapCodes;
	private static volatile Map<String, String>  _mapHandicapCodes;
	private static volatile ReferenceList _listTypeLocalisation;
	private static volatile Map<String, String> _mapTypeLocalisation;
	
	
	private static IIdeeService _singleton;
	private static SolrIdeeIndexer _solrIdeeIndexer;
	private static final String BEAN_IDEE_SERVICE="participatoryideation.ideeService";
	private static final String BEAN_TRANSACTION_MANAGER="participatoryideation.ideeServiceTransactionManager";
	private static final String BEAN_SOLR_IDEE_INDEXER="participatoryideation.solrIdeeIndexer";
	private static final Double[] _parisLatLon = { 48.859085, 2.347403 };
	private static final Double[][] _arrondissementLatLons = {
        { 48.862422, 2.337595 },
        { 48.868222, 2.343667 },
        { 48.863219, 2.360162 },
        { 48.854448, 2.357675 },
        { 48.844653, 2.351109 },
        { 48.849865, 2.334951 },
        { 48.856129, 2.312587 },
        { 48.873376, 2.312882 },
        { 48.877544, 2.338310 },
        { 48.876526, 2.361216 },
        { 48.859391, 2.380633 },
        { 48.841399, 2.390914 },
        { 48.829421, 2.366010 },
        { 48.831096, 2.329405 },
        { 48.843012, 2.294905 },
        { 48.860419, 2.279201 },
        { 48.887349, 2.308634 },
        { 48.891820, 2.348945 },
        { 48.886983, 2.386271 },
        { 48.863604, 2.401843 },
    };
	
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;

    public static IIdeeService getInstance(  )
    {
        if ( _singleton == null )
        {
            _singleton = SpringContextService.getBean( BEAN_IDEE_SERVICE );
            _solrIdeeIndexer= SpringContextService.getBean( BEAN_SOLR_IDEE_INDEXER );
            _hashArrondissementsLatLons=new HashMap<String, Double[]>();
            _hashArrondissementsCode=new HashMap<>();
            _listArrondissements = new ReferenceList(  );
            _listArrondissements.addItem( "",
                I18nService.getLocalizedString( PROPERTY_ARRONDISSEMENT_EMPTY, Locale.FRENCH ) );

            for ( int i = 0; i < 20; i++ )
            {
                String strCode = "" + ( 75001 + i );
                _listArrondissements.addItem( strCode,
                    I18nService.getLocalizedString( PROPERTY_ARRONDISSEMENT + strCode, Locale.FRENCH ) );
                _hashArrondissementsLatLons.put(strCode, _arrondissementLatLons[i]);
                _hashArrondissementsCode.put(i+1, strCode);
            }
            _mapArrondissements = _listArrondissements.toMap();
                    
            _listQpvQvaCodes = new ReferenceList();
            _listQpvQvaCodes.addItem(IdeationApp.QPV_QVA_NO , I18nService.getLocalizedString( PROPERTY_LABEL_NON, Locale.FRENCH ));
            _listQpvQvaCodes.addItem(IdeationApp.QPV_QVA_QPV, I18nService.getLocalizedString( PROPERTY_LABEL_NQPV, Locale.FRENCH ));
            _listQpvQvaCodes.addItem(IdeationApp.QPV_QVA_QVA , I18nService.getLocalizedString( PROPERTY_LABEL_QVA, Locale.FRENCH ));
            _listQpvQvaCodes.addItem(IdeationApp.QPV_QVA_GPRU , I18nService.getLocalizedString( PROPERTY_LABEL_GPRU, Locale.FRENCH ));
            _listQpvQvaCodes.addItem(IdeationApp.QPV_QVA_QBP , I18nService.getLocalizedString( PROPERTY_LABEL_QBP, Locale.FRENCH ));
            _listQpvQvaCodes.addItem(IdeationApp.QPV_QVA_ERR , I18nService.getLocalizedString( PROPERTY_LABEL_ERR, Locale.FRENCH ));
            _listQpvQvaCodes.addItem(IdeeSearcher.QPVQVA_UNKNOWN , I18nService.getLocalizedString( PROPERTY_LABEL_UNK, Locale.FRENCH ));
            _mapQpvQvaCodes = _listQpvQvaCodes.toMap();

            _listHandicapCodes = new ReferenceList();
            _listHandicapCodes.addItem(IdeationApp.HANDICAP_LABEL_YES, I18nService.getLocalizedString( PROPERTY_HANDICAP_LABEL_YES, Locale.FRENCH ));
            _listHandicapCodes.addItem(IdeationApp.HANDICAP_LABEL_NO , I18nService.getLocalizedString( PROPERTY_HANDICAP_LABEL_NO , Locale.FRENCH ));
            _mapHandicapCodes = _listHandicapCodes.toMap();

            _listTypeLocalisation = new ReferenceList();
            _listTypeLocalisation.addItem(Idee.LOCALISATION_TYPE_ARDT , I18nService.getLocalizedString( PROPERTY_LABEL_ARDT, Locale.FRENCH ));
            _listTypeLocalisation.addItem(Idee.LOCALISATION_TYPE_PARIS , I18nService.getLocalizedString( PROPERTY_LABEL_PARIS, Locale.FRENCH ));
            _mapTypeLocalisation = _listTypeLocalisation.toMap();
                
          }

        return _singleton;
    }

	
    private void createFiles(Idee idee, String type, List<File> attachedFiles)
    {
        for (File file: attachedFiles) {
            FileHome.create(file);
        }
    }

    //Don't forget to use InnoDB tables for the following tables!
    //core_file, core_physical_file, ideation_idees, ideation_idees_files
    //Check with:
    //sql> show table status ;
    @Transactional( BEAN_TRANSACTION_MANAGER )
	public synchronized void createIdeeDB(Idee idee) throws IdeationErrorException
	{
         createFiles( idee, Idee.ATTACHED_FILE_TYPE_DOC, idee.getDocs(  ));
         createFiles( idee, Idee.ATTACHED_FILE_TYPE_IMG, idee.getImgs(  ));
		 IdeeHome.create( idee );
	}
	public void createIdee(Idee idee) throws IdeationErrorException
	{
		_singleton.createIdeeDB(idee);
		_solrIdeeIndexer.writeIdee( idee );
	}

	public void removeIdeeCommon(Idee idee)
	{
        idee.setExportedTag(2);
        IdeeHome.updateBO(idee);
        IdeeHome.removeLinkByChild( idee.getId( ) );
        IdeeHome.removeLinkByParent( idee.getId( ) );
    }
	public void removeIdee(Idee idee)
	{
		removeIdeeCommon(idee);
        String strWorkflowActionNameDeleteIdee=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_DELETE_IDEE);
        IdeeWSService.getInstance().processActionByName(strWorkflowActionNameDeleteIdee, idee.getId());
	}
	
	
	public void removeIdeeByMdp(Idee idee)
	{
        removeIdeeCommon(idee);
        String strWorkflowActionNameDeleteIdeeByMdp=AppPropertiesService.getProperty(IdeationConstants.PROPERTY_WORKFLOW_ACTION_NAME_DELETE_IDEE_BY_MDP);
        IdeeWSService.getInstance().processActionByName(strWorkflowActionNameDeleteIdeeByMdp, idee.getId());
	}
   
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPublished(Idee idee)
    {
    	
    	return idee.getStatusPublic()!= null && idee.getStatusPublic().isPublished();
    }
	
	
	 /**
     * Provides the list of arrondissements
     * @return The list
     */
    public  ReferenceList getArrondissements(  )
    {
       

        return _listArrondissements;
    }
    
    
    /**
     * Provides the list of arrondissements
     * @return The list
     */
    public  String getArrondissementCode(Integer nNumero )
    {
       

        return _hashArrondissementsCode.get(nNumero);
    }
    
    
    
    
    public Double[] getArrondissementLatLong(String strCode)
    {
    	return _hashArrondissementsLatLons.get(strCode);
    }
    
    public Double[] getParisLatLong()
    {
    	return _parisLatLon;
    }
    
    /**
     * @return the mapArrondissements
     */
    public Map<String, String> getArrondissementsMap() {
        return _mapArrondissements;
    }
    
    
    /**
     * @return the QpvQvaCodes
     */
    public ReferenceList getQpvQvaCodesList() {
        return _listQpvQvaCodes;
    }

    /**
     * @return the QpvQvaCodes
     */
    public Map<String, String> getQpvQvaCodesMap() {
        return _mapQpvQvaCodes;
    }
    
    /**
     * @return the handicapCodes as a list
     */
    public ReferenceList getHandicapCodesList() {
        return _listHandicapCodes;
    }

    /**
     * @return the HandicapCodes as a map
     */
    public Map<String, String> getHandicapCodesMap() {
        return _mapHandicapCodes;
    }
    
    /**
     * @return the TypeLocalisation
     */
    public ReferenceList getTypeLocalisationList() {
        return _listTypeLocalisation;
    }

    /**
     * @return the TypeLocalisation
     */
    public Map<String, String> getTypeLocalisationMap() {
        return _mapTypeLocalisation;
    }
    
    // *********************************************************************************************
    // * DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT D *
    // * DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT DEPOSIT D *
    // *********************************************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getUniqueUserGuidsIdeesDepositaires ( List<Integer> propIds ) {
    	Set<String> userGuids = new HashSet<String>();

    	for ( Integer propId : propIds ) {
			Idee idee = IdeeHome.findByPrimaryKey( propId );
			if ( idee == null ) {
				AppLogService.error( "ERROR : Unable to find idee #'" + propId + "' !" );
			}
			else
			{
				userGuids.add( idee.getLuteceUserName() );
			}
		}
    	
    	return userGuids;
    }
    
    // *********************************************************************************************
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // * FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLOW FOLLO *
    // *********************************************************************************************

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getUniqueUserGuidsIdeesFollowers ( List<Integer> propIds ) {
    	Set<String> userGuids = new HashSet<String>();

    	for ( Integer propId : propIds ) {
            ResourceExtenderHistoryFilter filter = new ResourceExtenderHistoryFilter( );
            
            filter.setExtenderType          ( FollowResourceExtender.RESOURCE_EXTENDER );
            filter.setExtendableResourceType( "IDEE"                                   );
            filter.setIdExtendableResource  ( propId.toString()                        );

            List<ResourceExtenderHistory> listHistories = _resourceExtenderHistoryService.findByFilter( filter );

            for ( ResourceExtenderHistory followerHistory : listHistories )
            {
           		userGuids.add( followerHistory.getUserGuid() );
            }
		}
    	
    	return userGuids;
    }

    
}
