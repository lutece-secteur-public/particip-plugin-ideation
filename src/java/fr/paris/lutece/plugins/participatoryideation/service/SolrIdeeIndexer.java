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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;

import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.leaflet.business.GeolocItem;
import fr.paris.lutece.plugins.participatoryideation.business.Idee;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeHome;
import fr.paris.lutece.plugins.participatoryideation.business.IdeeSearcher;
import fr.paris.lutece.plugins.participatoryideation.web.IdeationApp;
import fr.paris.lutece.plugins.search.solr.business.SolrServerService;
import fr.paris.lutece.plugins.search.solr.business.field.Field;
import fr.paris.lutece.plugins.search.solr.indexer.SolrIndexer;
import fr.paris.lutece.plugins.search.solr.indexer.SolrIndexerService;
import fr.paris.lutece.plugins.search.solr.indexer.SolrItem;
import fr.paris.lutece.plugins.search.solr.util.SolrConstants;
import fr.paris.lutece.portal.service.prefs.UserPreferencesService;
import fr.paris.lutece.portal.service.search.SearchItem;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.url.UrlItem;


public class SolrIdeeIndexer implements SolrIndexer
{

    private static final String SHORT_NAME = "idee";
    private static final String PARAMETER_XPAGE = "page";
    private static final String XPAGE_IDEE = "idee";
    private static final String PARAMETER_CODE_CAMPAGNE = "campagne";
    private static final String PARAMETER_CODE_IDEE = "idee";
    
    @Inject
    private IRatingService _ratingService;

    public void writeIdee( Idee idee ) {
        try {
            write(getItem(idee));
        } catch (Exception e) {
            AppLogService.error( "[SolrIdeeIndexer] Error during write (idee " + idee.getReference() + ") " + e.getMessage(), e);
        }
    }
    
    public void removeIdee (  Idee idee ){
    	
    	try {
    		 SolrClient SOLR_SERVER = SolrServerService.getInstance(  ).getSolrServer(  );
    	     SOLR_SERVER.deleteByQuery( SearchItem.FIELD_UID + ":"+SolrIndexerService.getWebAppName( )+"_"+getResourceUid( Integer.toString(idee.getId()),null ));
    	     SOLR_SERVER.commit(  );

        } catch (Exception e) {
            AppLogService.error( "[SolrIdeeIndexer] Error during remove (idee " + idee.getReference() + ") " + e.getMessage(), e);
        }
    }

    public SolrItem getItem( Idee idee )
        throws IOException
    {
        // the item
        SolrItem item = new SolrItem(  );
        item.setUid( getResourceUid( Integer.toString(idee.getId()),
                null ) );
        item.setDate( idee.getCreationTimestamp() );
        item.setType( "idee" );
        item.setSummary( idee.getDescription( ) );
        item.setTitle( idee.getTitre(  ) );
        item.setSite( SolrIndexerService.getWebAppName(  ) );
        item.setRole( "none" );


        String strCodeGeoloc;
        double dLongitude = 0;
        double dLatitude  = 0;

        if (idee.getAdress() != null && idee.getLongitude() != null && idee.getLatitude() != null) {
            dLongitude = idee.getLongitude();
            dLatitude = idee.getLatitude();
            if (Idee.LOCALISATION_TYPE_ARDT.equals(idee.getLocalisationType())) {
                strCodeGeoloc = "idee_geoloc-ardt-" + idee.getLocalisationArdt();
            } else {
                strCodeGeoloc = "idee_geoloc-paris";
            }
        }
        else 
        {
            if (Idee.LOCALISATION_TYPE_ARDT.equals(idee.getLocalisationType())) {
                strCodeGeoloc = "idee_ardt-" + idee.getLocalisationArdt();
            } else {
                strCodeGeoloc = "idee_paris";
            }
        } 
        item.addDynamicFieldGeoloc("idee", idee.getAdress(), dLongitude, dLatitude, strCodeGeoloc);
        item.addDynamicField("idee_status", String.valueOf(idee.getStatusPublic().isPublished( )));
        item.addDynamicFieldNotAnalysed("status", String.valueOf( idee.getStatusPublic( ).getValeur( ) ) );
        item.addDynamicFieldNotAnalysed("code_theme", idee.getCodeTheme());
        item.addDynamicFieldNotAnalysed("code_depositaire_type", idee.getDepositaireType());
        item.addDynamicField("campagne", idee.getCodeCampagne());
        item.addDynamicField("code_projet", (long) idee.getCodeIdee());
        item.addDynamicField("localisation",
                ((idee.getAdress() != null) && (!"".equals(idee.getAdress().trim()))) ? 
                	idee.getAdress() :
                	(Idee.LOCALISATION_TYPE_ARDT.equals(idee.getLocalisationType().trim()) ? 
                		idee.getLocalisationArdt() : 
                		"whole city" // TODO : Must get this string from campaign area service
                	)
                );
        item.addDynamicFieldNotAnalysed("localisation_type", idee.getLocalisationType());

        if (Idee.LOCALISATION_TYPE_ARDT.equals(idee.getLocalisationType().trim()) ) {
            item.addDynamicField("localisation_ardt",  idee.getLocalisationArdt());
        }

        item.addDynamicField("budget", idee.getCout(  ));

        item.addDynamicFieldNotAnalysed("type_qpvqva", idee.getTypeQpvQva());
        if (IdeationApp.QPV_QVA_QPV.equals(idee.getTypeQpvQva()) || IdeationApp.QPV_QVA_QVA.equals(idee.getTypeQpvQva())
        || IdeationApp.QPV_QVA_GPRU.equals(idee.getTypeQpvQva()) || IdeationApp.QPV_QVA_QBP.equals(idee.getTypeQpvQva())) {
            item.addDynamicField("libelle_qpvqva", idee.getLibelleQpvQva());
        }
        
        item.addDynamicFieldNotAnalysed("url_projet", idee.getUrlProjet( ));
        item.addDynamicFieldNotAnalysed("winner_projet", idee.getWinnerProjet( ));
        
        item.addDynamicFieldNotAnalysed( "handicap", idee.getHandicap() );
        
        if(idee.getStatusPublic().getValeur() != null)
        {
        	item.addDynamicField("statut_publique_project", idee.getStatusPublic().getValeur());
        }
        
        Rating rating = _ratingService.findByResource( String.valueOf( idee.getId( ) ), Idee.PROPERTY_RESOURCE_TYPE );
        
        if ( rating != null )
        {
	        item.addDynamicField("like", ( long ) rating.getScorePositifsVotes( ) );
	        item.addDynamicField("dislike", ( long ) rating.getScoreNegativesVotes( ) );
        }
        else
        {
        	item.addDynamicField("like", 0L );
        	item.addDynamicField("dislike", 0L);
        }


        item.setXmlContent("");
        UrlItem url = new UrlItem( SolrIndexerService.getBaseUrl(  ) );
        url.addParameter( PARAMETER_XPAGE, XPAGE_IDEE );
        url.addParameter( PARAMETER_CODE_CAMPAGNE, idee.getCodeCampagne(  ) );
        url.addParameter( PARAMETER_CODE_IDEE, idee.getCodeIdee(  ) );


        // Date Hierarchy
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime( idee.getCreationTimestamp() );
        item.setHieDate( calendar.get( GregorianCalendar.YEAR ) + "/" + ( calendar.get( GregorianCalendar.MONTH ) + 1 ) + "/" +
                calendar.get( GregorianCalendar.DAY_OF_MONTH ) + "/" );

        List<String> listCategorie = new ArrayList<String>();
        item.setCategorie(listCategorie);
        item.setUrl( url.getUrl(  ) );
        StringBuilder sb = new StringBuilder();
           sb.append(idee.getDescription(  ) + " " + idee.getTitre(  ) );
        if ( idee.getAdress() != null ) {
            sb.append( " " + idee.getAdress(  ) );
        }

        String strNickname = UserPreferencesService.instance(  ).getNickname( idee.getLuteceUserName(  ) );
        if ( ! StringUtils.isEmpty( strNickname ) ) {
            sb.append( " " + strNickname );
            item.addDynamicFieldNotAnalysed("pseudo", strNickname);
        }

        sb.append( " " + idee.getReference() );

        item.setContent( sb.toString() );

        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResourceUid( String strResourceId, String strResourceType )
    {
        StringBuilder sb = new StringBuilder( strResourceId );
        sb.append( '_' ).append( SHORT_NAME );

        return sb.toString(  );
    }

    @Override
    public List<Field> getAdditionalFields() {
        // TODO Auto-generated method stub
        return new ArrayList();
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return "Solr idee indexer";
    }

    @Override
    public List<SolrItem> getDocuments(String arg0) {
        // TODO Auto-generated method stub
        return new ArrayList();
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "SolrIdeeIndexer";
    }

    @Override
    public List<String> getResourcesName() {
        // TODO Auto-generated method stub
        return new ArrayList();
    }

    @Override
    public String getVersion() {
        // TODO Auto-generated method stub
        return "1.0.0";
    }

    /*
     * Index all ideas.
     */
    public List<String> indexDocuments() {

    	// Errors and logs management
    	List<String> errors = new ArrayList<String>();
    	StringBuffer sbLogs = SolrIndexerService.getSbLogs();
     
    	// Getting solrItems to index
        IdeeSearcher _ideeSearcher= new IdeeSearcher();
    	_ideeSearcher.setIsPublished(true);
    	
    	Collection<SolrItem> ideesSolrItems = new ArrayList<SolrItem>();
    	
    	Collection<Idee> ideesList = new ArrayList<Idee>();
		try {
	    	ideesList = IdeeHome.getIdeesListSearch(_ideeSearcher);
        } catch (Exception e) {
        	printIndexMessage(e, sbLogs);
        	errors.add( SolrIndexerService.buildErrorMessage( e ) );
        	errors.add( sbLogs.toString() );
        }

		for (Idee idee: ideesList) {
    		try {
    			ideesSolrItems.add( getItem(idee) );
            } catch (Exception e) {
            	printIndexMessage(e, sbLogs);
            	errors.add( SolrIndexerService.buildErrorMessage( e ) );
            	errors.add( sbLogs.toString() );
            }
    	}
    	
    	try {
        	sbLogs.append("\nIndexing " + ideesSolrItems.size() + " idea solr items, from " + ideesList.size() + " ideas\n");
			SolrIndexerService.write(ideesSolrItems, sbLogs);
		} catch (Exception e) {
        	printIndexMessage(e, sbLogs);
        	errors.add( SolrIndexerService.buildErrorMessage( e ) );
        	errors.add( sbLogs.toString() );
		}
    	
        return errors;
    }

    @Override
    public boolean isEnable() {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Copy paste from SolrIndexer because it's a private method
     */
    private static SolrInputDocument solrItem2SolrInputDocument( SolrItem solrItem )
    {
        SolrInputDocument solrInputDocument = new SolrInputDocument(  );
        String strWebappName = SolrIndexerService.getWebAppName(  );

        // Prefix the uid by the name of the site. Without that, it is necessary imposible to index two resources of two different sites with the same identifier
        solrInputDocument.addField( SearchItem.FIELD_UID,
        strWebappName + SolrConstants.CONSTANT_UNDERSCORE + solrItem.getUid(  ) );
        solrInputDocument.addField( SearchItem.FIELD_DATE, solrItem.getDate(  ) );
        solrInputDocument.addField( SearchItem.FIELD_TYPE, solrItem.getType(  ) );
        solrInputDocument.addField( SearchItem.FIELD_SUMMARY, solrItem.getSummary(  ) );
        solrInputDocument.addField( SearchItem.FIELD_TITLE, solrItem.getTitle(  ) );
        solrInputDocument.addField( SolrItem.FIELD_SITE, solrItem.getSite(  ) );
        solrInputDocument.addField( SearchItem.FIELD_ROLE, solrItem.getRole(  ) );
        solrInputDocument.addField( SolrItem.FIELD_XML_CONTENT, solrItem.getXmlContent(  ) );
        solrInputDocument.addField( SearchItem.FIELD_URL, solrItem.getUrl(  ) );
        solrInputDocument.addField( SolrItem.FIELD_HIERATCHY_DATE, solrItem.getHieDate(  ) );
        solrInputDocument.addField( SolrItem.FIELD_CATEGORIE, solrItem.getCategorie(  ) );
        solrInputDocument.addField( SolrItem.FIELD_CONTENT, solrItem.getContent(  ) );
        solrInputDocument.addField( SearchItem.FIELD_DOCUMENT_PORTLET_ID, solrItem.getDocPortletId(  ) );

        // Add the dynamic fields
        // They must be declared into the schema.xml of the solr server
        Map<String, Object> mapDynamicFields = solrItem.getDynamicFields(  );

        for ( String strDynamicField : mapDynamicFields.keySet(  ) )
        {
            solrInputDocument.addField( strDynamicField, mapDynamicFields.get( strDynamicField ) );
        }

        return solrInputDocument;
    }

    private static void write( SolrItem solrItem )
    {
        SolrClient SOLR_SERVER = SolrServerService.getInstance(  ).getSolrServer(  );
        try
        {
            SolrInputDocument solrInputDocument = solrItem2SolrInputDocument( solrItem );
            SOLR_SERVER.add( solrInputDocument );
            SOLR_SERVER.commit(  );
        }
        catch ( Exception e )
        {
            AppLogService.error( "IdeationApp, error during indexation" + e.getMessage(), e);
        }
    }
    
    /**
     * Adds the exception into the buffer and the StringBuffer
     * @param exception Exception to report
     * @param sbLogs StringBuffer to write to
     */
    private static void printIndexMessage( Exception exception, StringBuffer sbLogs )
    {
        sbLogs.append( " - ERROR : " );

        if ( exception != null )
        {
	        sbLogs.append( "(" + exception.getClass().getName() + ") " + exception.getMessage(  ) );
	        if ( exception.getCause(  ) != null )
	        {
	            sbLogs.append( " : " );
	            sbLogs.append( exception.getCause(  ).getMessage(  ) );
	        }
	        AppLogService.error( exception.getMessage(  ), exception );
        }
        else
        {
        	sbLogs.append( "'exception' param is null !" );
        }
        
    }


}
