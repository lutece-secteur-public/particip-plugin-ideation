/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
package fr.paris.lutece.plugins.ideation.web;

import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.util.url.UrlItem;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.plugins.campagnebp.service.MyInfosService;
import fr.paris.lutece.plugins.campagnebp.web.MyInfosXPage;
import fr.paris.lutece.plugins.ideation.business.Atelier;
import fr.paris.lutece.plugins.ideation.business.AtelierForm;
import fr.paris.lutece.plugins.ideation.business.AtelierFormEntry;
import fr.paris.lutece.plugins.ideation.business.AtelierFormEntryHome;
import fr.paris.lutece.plugins.ideation.business.AtelierFormHome;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResult;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultEntry;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultEntryHome;
import fr.paris.lutece.plugins.ideation.business.AtelierFormResultHome;
import fr.paris.lutece.plugins.ideation.business.AtelierHome;
import fr.paris.lutece.plugins.ideation.business.Idee;
import fr.paris.lutece.plugins.ideation.service.AtelierFormService;
import fr.paris.lutece.plugins.ideation.service.IdeationStaticService;
import fr.paris.lutece.plugins.ideation.service.IdeeUsersService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.message.SiteMessage;
import fr.paris.lutece.portal.service.message.SiteMessageException;
import fr.paris.lutece.portal.service.message.SiteMessageService;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.portal.PortalService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.util.mvc.commons.annotations.Action;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.utils.MVCUtils;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;

/**
 * This class provides a simple implementation of an XPage
 */
 
@Controller( xpageName = "atelier" , pageTitleI18nKey = "ideation.xpage.atelier.pageTitle" , pagePathI18nKey = "ideation.xpage.atelier.pagePathLabel" )
public class IdeeAtelierApp extends MVCApplication
{
    /**
     * 
     */
    private static final long serialVersionUID = 2201290034605296020L;
    private static final String TEMPLATE_ATELIER = "/skin/plugins/ideation/atelier.html";
    private static final String TEMPLATE_ATELIER_PHASE2 = "/skin/plugins/ideation/atelier_phase2.html";
    private static final String TEMPLATE_ATELIER_PHASE3 = "/skin/plugins/ideation/atelier_phase3.html";

    //Jsp redirections
    private static final String JSP_PORTAL = "jsp/site/Portal.jsp";
    
    //Views/Actions
    private static final String STEP_OPINION = "opinion";
    private static final String STEP_VOTEFOR = "votefor";
    private static final String STEP_FINAL = "final";
    
    // Parameters
    private static final String PARAMETER_ATELIER = "atelier";

    // Properties
    private static final String PROPERTY_ATELIER_TAB_LABEL_PHASE1="ideation.atelier.tab.label.phase1";
    private static final String PROPERTY_ATELIER_TAB_LABEL_PHASE2="ideation.atelier.tab.label.phase2";
    private static final String PROPERTY_ATELIER_TAB_LABEL_PHASE3="ideation.atelier.tab.label.phase3";
    private static final String PROPERTY_ATELIER_PRESENTIEL_CANNOT_ACCESS = "ideation.site_property.form.atelier_presentiel_cannot_access" ;
    private static final String MESSAGE_ERROR_VALIDATION_FIELDS_NOT_SELECTED = "ideation.validation.atelierformresult.Fields.notSelected";
    private static final String MESSAGE_CANNOT_VOTE = "ideation.message.atelier.cannotVote";
    // Markers
    private static final String MARK_ATELIER = "atelier";
    private static final String MARK_ATELIER_FORM = "atelierForm";
    private static final String MARK_IS_EXTEND_INSTALLED = "isExtendInstalled";
    private static final String MARK_IS_DATE_AFTER = "isDateAfter";
    private static final String MARK_CODE_CAMPAGNE = "codeCampagne";
    private static final String MARK_LINKED_IDEES_MAP = "linkedIdeesMap";
    private static final String MARK_ATELIER_TABS_LIST = "atelierTabsList";
    private static final String MARK_ATELIER_SELECTED_TAB = "selectedAtelierTab";
    private static final String MARK_ATELIER_STEP_NOT_STARTED = "atelierStepNotStarted";
    private static final String MARK_ATELIER_FORM_ENTRY_LIST = "atelierFormEntryList";
    private static final String MARK_CANNOT_VOTE = "cannotVote";
    private static final String MARK_ATELIER_FORM_CANNOT_SUBMIT = "atelierFormCannotSubmit";
    private static final String MARK_ATELIER_FIN_PHASE_2 = "atelierFinPhase2";
    
    private static final String MARK_ATELIER_CHECKED_ENTRY_MAP = "atelierCheckedEntryMap";
    private static final String MARK_ATELIER_PRESENTIEL_CANNOT_ACCESS = "atelierPresentielCannotAccess";
    
    private static final String MESSAGE_ATELIER_NOT_FOUND = "ideation.message.atelier.notFound";
    private static final String MESSAGE_ATELIER_CANNOT_ACCESS = "ideation.message.atelier.cannotAccess";
    private static final String MESSAGE_ATELIER_PRESENTIEL_CANNOT_ACCESS = "ideation.message.atelier.presentiel.cannotAccess";
    private static final String MESSAGE_ATELIER_STEP_NOT_STARTED = "ideation.message.atelier.stepNotStarted";
    private static final String MESSAGE_ATELIER_FORM_CANNOT_SUBMIT = "ideation.message.atelier.formCannotSubmit";
    public static final String ATELIER_TYPE_PRESENTIEL = "physique";
    public static final String ATELIER_TYPE_NUMERIQUE = "numerique";
    
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String ATELIER_FORM_ALTERNATIVE = "alternative_";

    // Session variable to store working values
    private AtelierFormResult _atelierFormResult;
    private Map<String, String> _resultChoiceMap;

    /**
     * Returns the content of the page atelier. 
     * @param request The HTTP request
     * @return The view
     * @throws SiteMessageException 
     */
    @View( value = STEP_OPINION, defaultView = true )
    public XPage viewOpinion( HttpServletRequest request ) throws SiteMessageException
    {
        Integer nIdAtelier;
        
        try
        {
            nIdAtelier = Integer.parseInt( request.getParameter( PARAMETER_ATELIER ) );
        }
        catch ( NumberFormatException nfe )
        {
            nIdAtelier = null;
        }

        Atelier atelier = null;
        
        if ( nIdAtelier != null )
        {
            atelier = AtelierHome.findByPrimaryKey( nIdAtelier );
        }

        if ( atelier == null )
        {
            SiteMessageService.setMessage( request, MESSAGE_ATELIER_NOT_FOUND, SiteMessage.TYPE_ERROR,
                    JSP_PORTAL );
        }

        Map<String, Object> model = getModel( );

        if ( atelier.getType( ).equals( ATELIER_TYPE_PRESENTIEL ) )
        {
            String strDefaultMsg = I18nService.getLocalizedString( MESSAGE_ATELIER_PRESENTIEL_CANNOT_ACCESS, new Locale( "fr", "FR" ) );
            String strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ATELIER_PRESENTIEL_CANNOT_ACCESS, strDefaultMsg );
            model.put( MARK_ATELIER_PRESENTIEL_CANNOT_ACCESS, strDataStoreValue );
        }

        Date date = new Date( );
        if ( date.before( atelier.getDateDebutPhase1( ) ) )
        {
            model.put( MARK_ATELIER_STEP_NOT_STARTED, MESSAGE_ATELIER_STEP_NOT_STARTED );
        }

        model.put( MARK_ATELIER, atelier );
        model.put( MARK_CODE_CAMPAGNE, atelier.getCampagne( ) );
        model.put( MARK_LINKED_IDEES_MAP, getIdeeIdsAndNames( atelier.getId( ) ) );
        model.put( MARK_IS_EXTEND_INSTALLED, PortalService.isExtendActivated( ) );
        model.put( MARK_IS_DATE_AFTER, date.after(atelier.getDateFinPhase1( )) );
        model.put( MARK_ATELIER_TABS_LIST, getAtelierMenu( atelier ) );
        model.put( MARK_ATELIER_SELECTED_TAB, PROPERTY_ATELIER_TAB_LABEL_PHASE1 );
        IdeationStaticService.getInstance( ).fillCampagneStaticContent( model, atelier.getCampagne( ) );

        return getXPage( TEMPLATE_ATELIER, request.getLocale( ), model );
    }

    private Map<String, String> getIdeeIdsAndNames( int nIdAtelier )
    {
        Map<String, String> map = new HashMap<String, String>( );
        List<Idee> ideeList = AtelierHome.getIdeesByAtelier( nIdAtelier );
        
        for ( Idee idee : ideeList )
        {
            map.put( String.valueOf( idee.getCodeIdee() ), idee.getTitre( ) );
        }
        return map;
    }
    /**
     * Returns the content of the page atelier. 
     * @param request The HTTP request
     * @return The view
     * @throws SiteMessageException 
     * @throws UserNotSignedException 
     */
    @View( value = STEP_VOTEFOR )
    public XPage viewVoteFor( HttpServletRequest request ) throws SiteMessageException, UserNotSignedException
    {
        Integer nIdAtelier;
        
        try
        {
            nIdAtelier = Integer.parseInt( request.getParameter( PARAMETER_ATELIER ) );
        }
        catch ( NumberFormatException nfe )
        {
            nIdAtelier = null;
        }

        Atelier atelier = null;
        
        if ( nIdAtelier != null )
        {
            atelier = AtelierHome.findByPrimaryKey( nIdAtelier );
        }

        if ( atelier == null )
        {
            SiteMessageService.setMessage( request, MESSAGE_ATELIER_NOT_FOUND, SiteMessage.TYPE_ERROR,
                    JSP_PORTAL );
        }
        
        Map<String, Object> model = getModel( );

        if ( atelier.getType( ).equals( ATELIER_TYPE_PRESENTIEL ) )
        {
            String strDefaultMsg = I18nService.getLocalizedString( MESSAGE_ATELIER_PRESENTIEL_CANNOT_ACCESS, new Locale( "fr", "FR" ) );
            String strDataStoreValue = DatastoreService.getDataValue( PROPERTY_ATELIER_PRESENTIEL_CANNOT_ACCESS, strDefaultMsg );
            model.put( MARK_ATELIER_PRESENTIEL_CANNOT_ACCESS, strDataStoreValue );
        }

        Date date = new Date( );

        date = DateUtils.truncate( date, Calendar.DATE );
        if ( date.before( atelier.getDateDebutPhase2( ) ) )
        {
            model.put( MARK_ATELIER_STEP_NOT_STARTED, MESSAGE_ATELIER_STEP_NOT_STARTED );
        }
        
        if ( date.after( atelier.getDateFinPhase2( ) ) )
        {
        	model.put( MARK_ATELIER_FIN_PHASE_2, true );
            model.put( MARK_ATELIER_FORM_CANNOT_SUBMIT, MESSAGE_ATELIER_FORM_CANNOT_SUBMIT );
        }
        else
        {
          	model.put( MARK_ATELIER_FIN_PHASE_2, false );
        }

        // Check if the user can vote
        AtelierForm atelierForm = AtelierFormHome.getAtelierFormByAtelier( nIdAtelier );
        String strGuid = "guid";
        LuteceUser user = SecurityService.getInstance( ).getRemoteUser( request );
        //LuteceUser user = null;
        if(user != null)
        	strGuid = user.getName();

        if ( atelierForm != null )
        {
            AtelierFormResult atelierFormResult = AtelierFormResultHome.findByAtelierFormAndUser( atelierForm.getId( ), strGuid );

            if ( atelierFormResult != null )
            {
                // The user has already submitted the vote form
                model.put( MARK_ATELIER_FORM_CANNOT_SUBMIT, MESSAGE_ATELIER_FORM_CANNOT_SUBMIT );

                // Show what the user has submitted
                _atelierFormResult = atelierFormResult;

                if ( atelierFormResult != null )
                {
                    _resultChoiceMap = new HashMap<String, String>( );
                    _resultChoiceMap.put( String.valueOf( atelierForm.getChoixTitre( ).getId( ) ),
                                           String.valueOf( atelierFormResult.getNumeroChoixTitre( ) ) );
                    _resultChoiceMap.put( String.valueOf( atelierForm.getChoixDescription( ).getId( ) ),
                                           String.valueOf( atelierFormResult.getNumeroChoixDescription( ) ) );
                    
                    for ( AtelierFormResultEntry atelierFormResultEntry : atelierFormResult.getListChoixComplementaires( ) )
                    {
                        _resultChoiceMap.put( String.valueOf( atelierFormResultEntry.getIdAtelierFormEntry( ) ),
                                              String.valueOf( atelierFormResultEntry.getNumeroChoix( ) ) );
                    }
                }
            }

            if ( _atelierFormResult != null && _atelierFormResult.getGuid( ).equals( strGuid )
                    && _atelierFormResult.getIdAtelierForm( ) == atelierForm.getId( ) )
            {
                model.put( MARK_ATELIER_CHECKED_ENTRY_MAP, _resultChoiceMap );
            }
            else
            {
                _atelierFormResult = null;
                _resultChoiceMap = null;
            }
        }
        else
        {
            _atelierFormResult = null;
            _resultChoiceMap = null;
        }
        
        model.put( MARK_ATELIER, atelier );
        model.put( MARK_ATELIER_FORM, atelierForm );
        model.put( MARK_CODE_CAMPAGNE, atelier.getCampagne( ) );
        model.put( MARK_LINKED_IDEES_MAP, getIdeeIdsAndNames( atelier.getId( ) ) );
        model.put( MARK_IS_EXTEND_INSTALLED, PortalService.isExtendActivated( ) );
        model.put( MARK_ATELIER_TABS_LIST, getAtelierMenu( atelier ) );
        model.put( MARK_ATELIER_SELECTED_TAB, PROPERTY_ATELIER_TAB_LABEL_PHASE2 );
        model.put( MARK_ATELIER_FORM_ENTRY_LIST, getAtelierFormEntryList( atelier ) );
        IdeationStaticService.getInstance( ).fillCampagneStaticContent( model, atelier.getCampagne( ) );
        
        return getXPage( TEMPLATE_ATELIER_PHASE2, request.getLocale( ), model );
    }
    
    @Action( value = STEP_VOTEFOR )
    public XPage doVoteFor( HttpServletRequest request ) throws UserNotSignedException
    {

        Integer nIdAtelier = Integer.parseInt( request.getParameter( PARAMETER_ATELIER ) );
        
        AtelierForm atelierForm = AtelierFormHome.getAtelierFormByAtelier( nIdAtelier );
        String strGuid = "guid";
        LuteceUser user = SecurityService.getInstance( ).getRemoteUser( request );
        //LuteceUser user = null;
        if(user != null)
        	strGuid = user.getName( );

        if ( atelierForm != null )
        {
            AtelierFormResult atelierFormResult = AtelierFormResultHome.findByAtelierFormAndUser( atelierForm.getId( ), strGuid );

            if ( atelierFormResult != null )
            {
                addError( MESSAGE_CANNOT_VOTE, request.getLocale( ) );
                return redirect( request, STEP_VOTEFOR, PARAMETER_ATELIER, nIdAtelier );
            }
            // The user has not submitted the form yet
            atelierFormResult = new AtelierFormResult( );
            atelierFormResult.setIdAtelierForm( atelierForm.getId( ) );
            atelierFormResult.setGuid( strGuid );
            
            List<AtelierFormResultEntry> listEntriesResult= new ArrayList<>();
            
            List<AtelierFormEntry> choixDescCompList = AtelierFormEntryHome.getFormEntriesComplByAtelierForm( atelierForm );

            try
            {
                String strChoixTitre = request.getParameter( ATELIER_FORM_ALTERNATIVE + atelierForm.getChoixTitre( ).getId( ) );
                String strChoixDesc = request.getParameter( ATELIER_FORM_ALTERNATIVE + atelierForm.getChoixDescription( ).getId( ) );
                int nChoixTitre = strChoixTitre == null ? 0 : Integer.parseInt( strChoixTitre );
                int nChoixDesc = strChoixDesc == null ? 0 : Integer.parseInt( strChoixDesc );

                atelierFormResult.setNumeroChoixTitre( nChoixTitre );
                atelierFormResult.setNumeroChoixDescription( nChoixDesc );
               
                // Check if the selected title or description is correct
                if ( strChoixTitre == null
                        && ( !atelierForm.getChoixTitre( ).getAlternative2( ).isEmpty( )
                                || !atelierForm.getChoixTitre( ).getAlternative3( ).isEmpty( ) )
                        || strChoixDesc == null && ( !atelierForm.getChoixDescription( ).getAlternative2( ).isEmpty( )
                                || !atelierForm.getChoixDescription( ).getAlternative3( ).isEmpty( ) ) 
                        || nChoixTitre > 3 || nChoixTitre < 0 || nChoixDesc > 3 || nChoixDesc < 0 )
                {
                    throw new Exception( );
                }

                for ( AtelierFormEntry choixDescComp : choixDescCompList )
                {
                    String strChoix = request.getParameter( ATELIER_FORM_ALTERNATIVE + choixDescComp.getId( ) );

                    // Entry without alternative
                    if ( strChoix == null && choixDescComp.getAlternative2( ).isEmpty( )
                            && choixDescComp.getAlternative3( ).isEmpty( ) )
                    {
                        continue;
                    }
                    else
                    {
                        int nChoix = Integer.parseInt( strChoix );

                        // Check if the first alternative is selected, the user
                        // had at least two alternatives
                        // or if another alternative is selected then the
                        // altenative must not be an empty string
                        if ( nChoix == 1
                                && ( !choixDescComp.getAlternative2( ).isEmpty( )
                                        || !choixDescComp.getAlternative3( ).isEmpty( ) )
                                || nChoix == 2 && !choixDescComp.getAlternative2( ).isEmpty( )
                                || nChoix == 3 && !choixDescComp.getAlternative3( ).isEmpty( ) )
                        {
                            AtelierFormResultEntry atelierFormResultEntry = new AtelierFormResultEntry( );

                            atelierFormResultEntry.setIdAtelierFormEntry( choixDescComp.getId( ) );
                            atelierFormResultEntry.setNumeroChoix( nChoix );
                            listEntriesResult.add(atelierFormResultEntry);
                        }
                        else
                        {
                            throw new Exception();
                        }
                    }
                }
                //Do save Vote
                AtelierFormService.getInstance().doSaveVote(atelierFormResult, listEntriesResult);
                
            }
            catch ( Exception e )
            {
                setSessionVariables( request, atelierFormResult, atelierForm, choixDescCompList );
               
                addError( MESSAGE_ERROR_VALIDATION_FIELDS_NOT_SELECTED , request.getLocale( ) ) ;
                return redirect( request, STEP_VOTEFOR, PARAMETER_ATELIER, nIdAtelier );
            }
        }
        _atelierFormResult = null;
        _resultChoiceMap = null;

        return redirect( request, STEP_VOTEFOR, PARAMETER_ATELIER, nIdAtelier );
    }
    /**
     * Returns the content of the page atelier. 
     * @param request The HTTP request
     * @return The view
     * @throws SiteMessageException 
     */
    @View( value = STEP_FINAL )
    public XPage viewFinal( HttpServletRequest request ) throws SiteMessageException
    {
        Integer nIdAtelier;
        
        try
        {
            nIdAtelier = Integer.parseInt( request.getParameter( PARAMETER_ATELIER ) );
        }
        catch ( NumberFormatException nfe )
        {
            nIdAtelier = null;
        }

        Atelier atelier = null;
        
        if ( nIdAtelier != null )
        {
            atelier = AtelierHome.findByPrimaryKey( nIdAtelier );
        }

        if ( atelier == null )
        {
            SiteMessageService.setMessage( request, MESSAGE_ATELIER_NOT_FOUND, SiteMessage.TYPE_ERROR,
                    JSP_PORTAL );
        }

        Map<String, Object> model = getModel( );

        Date date = new Date( );
        if ( date.before( atelier.getDateDebutPhase3( ) ) )
        {
            model.put( MARK_ATELIER_STEP_NOT_STARTED, MESSAGE_ATELIER_STEP_NOT_STARTED );
        }

        model.put( MARK_ATELIER, atelier );
        model.put( MARK_CODE_CAMPAGNE, atelier.getCampagne( ) );
        model.put( MARK_LINKED_IDEES_MAP, getIdeeIdsAndNames( atelier.getId( ) ) );
        model.put( MARK_IS_EXTEND_INSTALLED, PortalService.isExtendActivated( ) );
        model.put( MARK_ATELIER_TABS_LIST, getAtelierMenu( atelier ) );

        model.put( MARK_ATELIER_SELECTED_TAB, PROPERTY_ATELIER_TAB_LABEL_PHASE3 );
        IdeationStaticService.getInstance( ).fillCampagneStaticContent( model, atelier.getCampagne( ) );

        return getXPage( TEMPLATE_ATELIER_PHASE3, request.getLocale( ), model );
        
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public XPage getPage( HttpServletRequest request, int nMode, Plugin plugin )
            throws SiteMessageException, UserNotSignedException
    {
        
        if ( !checkUserAuthorized( request ) )
        {
            return redirect( request, AppPathService.getProdUrl( request ) + MyInfosXPage.getUrlMyInfos( ) );
        }
        // Check user valid

        Integer nIdAtelier;
        Atelier atelier = null;

        try
        {
            nIdAtelier = Integer.parseInt( request.getParameter( PARAMETER_ATELIER ) );
        }
        catch ( NumberFormatException nfe )
        {
            nIdAtelier = null;
        }
        
        if ( nIdAtelier != null )
        {
            atelier = AtelierHome.findByPrimaryKey( nIdAtelier );
        }
        
        String strGuid = "guid";
        LuteceUser user = SecurityService.getInstance( ).getRemoteUser( request );
        //LuteceUser user = null;
        if(user != null)
        	strGuid = user.getName();
        
        String strAction = MVCUtils.getAction( request );
        String strView = MVCUtils.getView( request );

        if ( atelier == null || atelier.getStatusPublic( ) != null && !atelier.getStatusPublic( ).isPublished( ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_ATELIER_NOT_FOUND, SiteMessage.TYPE_ERROR );
        }
        if ( !isAtelierUser( atelier, strGuid ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_ATELIER_CANNOT_ACCESS, SiteMessage.TYPE_ERROR );
        }
        Date date = new Date( );
        
        date = DateUtils.truncate( date, Calendar.DATE );
        if ( strAction == null && strView == null )
        {
            if ( atelier.getType( ).equals( ATELIER_TYPE_NUMERIQUE ) )
            {
                if ( date.before( atelier.getDateDebutPhase2( ) )  )
                {
                    return redirect( request, STEP_OPINION, PARAMETER_ATELIER, atelier.getId( ) );
                }
                else if ( (date.after( atelier.getDateDebutPhase2( ) ) || date.equals( atelier.getDateDebutPhase2( ) )) && date.before( atelier.getDateDebutPhase3( ) ) )
                {
                    return redirect( request, STEP_VOTEFOR, PARAMETER_ATELIER, atelier.getId( ) );
                }
            }

            return redirect( request, STEP_FINAL, PARAMETER_ATELIER, atelier.getId( ) );
        }
        
        if ( strAction != null && strAction.equals( STEP_VOTEFOR ) && atelier.getType( ).equals( ATELIER_TYPE_NUMERIQUE )
                && ( date.before( atelier.getDateDebutPhase2( ) ) || date.after( atelier.getDateFinPhase2( ) ) ) )
        {
            SiteMessageService.setMessage( request, MESSAGE_ATELIER_FORM_CANNOT_SUBMIT, SiteMessage.TYPE_ERROR );
        }

        XPage xpage = super.getPage( request, nMode, plugin );
        xpage.setTitle( atelier.getTitre( ) );
        
        return xpage;
    }

    /**
     * Check if the user is authentified
     * @param request
     * @return
     * @throws UserNotSignedException
     */
    private boolean checkUserAuthorized( HttpServletRequest request ) throws UserNotSignedException
    {
        LuteceUser user = null;
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            user = SecurityService.getInstance( ).getRemoteUser( request );
            if ( user == null )
            {
                throw new UserNotSignedException( );
            }
            return MyInfosService.loadUserInfos( user ).getIsValid( );

        }
        return false;

    }
    
    /**
     * Check if the user has submitted an idee among the idees associated with the atelier
     * or if the user has followed one of those idees
     * @param atelier
     * @param userId
     * @return
     */
    private boolean isAtelierUser( Atelier atelier, String userId )
    {
        Collection<Integer> ideeIds = AtelierHome.getIdeeIdsListByAtelier( atelier.getId( ) );
        
        for ( Integer nIdeeId : ideeIds)
        {
            if ( IdeeUsersService.isDepositaire( nIdeeId, userId ) || IdeeUsersService.isFollower( nIdeeId, userId ) )
            {
                return true;
            }
        }
        return false;
    }
    
    private String getAtelierTabUrl( String strView, Atelier atelier )
    {
        UrlItem url = new UrlItem( getViewFullUrl( strView ) );
        url.addParameter( PARAMETER_ATELIER, atelier.getId( ) );
        
        return url.getUrl( );
    }
    
    /**
     * 
     * @param atelier
     * @return The list of the atelier menu
     */
    private List<String[]> getAtelierMenu( Atelier atelier )
    {
        SimpleDateFormat formatDate = new SimpleDateFormat( DATE_FORMAT );
        List<String[]> linkAndLabelList = new ArrayList<String[]>();
        
        String[] tabPhase1 = new String[4];
        
        tabPhase1[0] = getAtelierTabUrl( STEP_OPINION, atelier );
        tabPhase1[1] = PROPERTY_ATELIER_TAB_LABEL_PHASE1;
        tabPhase1[2] = formatDate.format( atelier.getDateDebutPhase1( ) );
        tabPhase1[3] = formatDate.format( atelier.getDateFinPhase1( ) );
        
        String[] tabPhase2 = new String[4];
        
        tabPhase2[0] = getAtelierTabUrl( STEP_VOTEFOR, atelier );
        tabPhase2[1] = PROPERTY_ATELIER_TAB_LABEL_PHASE2;
        tabPhase2[2] = formatDate.format( atelier.getDateDebutPhase2( ) );
        tabPhase2[3] = formatDate.format( atelier.getDateFinPhase2( ) );
        
        String[] tabPhase3 = new String[4];
        
        tabPhase3[0] = getAtelierTabUrl( STEP_FINAL, atelier );
        tabPhase3[1] = PROPERTY_ATELIER_TAB_LABEL_PHASE3;
        tabPhase3[2] = formatDate.format( atelier.getDateDebutPhase3( ) );
        tabPhase3[3] = formatDate.format( atelier.getDateFinPhase3( ) );
        
        linkAndLabelList.add( tabPhase1 );
        linkAndLabelList.add( tabPhase2 );
        linkAndLabelList.add( tabPhase3 );
        
        return linkAndLabelList;
    }
    
    private List<AtelierFormEntry> getAtelierFormEntryList( Atelier atelier )
    {
        List<AtelierFormEntry> atelierFormEntryList = null;
        
        if ( atelier != null )
        {
            AtelierForm atelierForm = AtelierFormHome.getAtelierFormByAtelier( atelier.getId( ) );
            
            if ( atelierForm != null )
            {
                atelierFormEntryList = AtelierFormEntryHome.getAtelierFormEntrysByAtelierForm( atelierForm );
            }
        }

        return atelierFormEntryList;
    }

    private void setSessionVariables( HttpServletRequest request, AtelierFormResult atelierFormResult,
            AtelierForm atelierForm, List<AtelierFormEntry> choixDescCompList )
    {
        _atelierFormResult = atelierFormResult;

        if ( atelierFormResult != null )
        {
            _resultChoiceMap = new HashMap<String, String>( );
            _resultChoiceMap.put( String.valueOf( atelierForm.getChoixTitre( ).getId( ) ),
                                   String.valueOf( atelierFormResult.getNumeroChoixTitre( ) ) );
            _resultChoiceMap.put( String.valueOf( atelierForm.getChoixDescription( ).getId( ) ),
                                   String.valueOf( atelierFormResult.getNumeroChoixDescription( ) ) );

            for ( AtelierFormEntry choixDescComp : choixDescCompList )
            {
                String strChoix = request.getParameter( ATELIER_FORM_ALTERNATIVE + choixDescComp.getId( ) );

                // Entry without alternative
                if ( strChoix == null )
                {
                    _resultChoiceMap.put( String.valueOf( choixDescComp.getId( ) ), "0" );
                }
                else
                {
                    try
                    {
                        int nChoix = Integer.parseInt( strChoix );

                        // Check if the first alternative is selected, the user
                        // had at least two alternatives
                        // or if another alternative is selected then the
                        // alternative must not be an empty string
                        if ( nChoix == 1
                                && ( !choixDescComp.getAlternative2( ).isEmpty( )
                                        || !choixDescComp.getAlternative3( ).isEmpty( ) )
                                || nChoix == 2 && !choixDescComp.getAlternative2( ).isEmpty( )
                                || nChoix == 3 && !choixDescComp.getAlternative3( ).isEmpty( ) )
                        {
                            _resultChoiceMap.put( String.valueOf( choixDescComp.getId( ) ), strChoix );
                        }
                        else
                        {
                            _resultChoiceMap.put( String.valueOf( choixDescComp.getId( ) ), "0" );
                        }
                    }
                    catch ( NumberFormatException e )
                    {
                        _resultChoiceMap.put( String.valueOf( choixDescComp.getId( ) ), "0" );
                    }
                }
            }
        }
        else
        {
            _resultChoiceMap = null;
        }
    }
}
