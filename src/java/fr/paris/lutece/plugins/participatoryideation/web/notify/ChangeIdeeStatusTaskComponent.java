/*
 * Copyright (c) 2002-2020, Mairie de Paris
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
package fr.paris.lutece.plugins.participatoryideation.web.notify;

import fr.paris.lutece.plugins.participatoryideation.business.Idee.Status;
import fr.paris.lutece.plugins.participatoryideation.business.notify.TaskChangeIdeeStatusConfig;
import fr.paris.lutece.plugins.participatoryideation.service.notify.TaskChangeIdeeStatus;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.util.ReferenceList;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * INotifyIdeationTaskComponent
 *
 */
public class ChangeIdeeStatusTaskComponent extends NoFormTaskComponent
{
    // PARAMETERS
    public static final String PARAMETER_IDEE_STATUS = "idee_status";

    // For rich text editor
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";
    public static final String MARK_IDEE = "idee";

    // MARKS
    public static final String MARK_CONFIG = "config";
    // TEMPLATES
    private static final String TEMPLATE_CHANGE_Idee_Status_CONFIG = "admin/plugins/participatoryideation/notify/task_change_idee_status_config.html";

    private static final String MARK_REF_LIST_STATUS = "refListStatus";

    // SERVICES
    @Inject
    @Named( TaskChangeIdeeStatus.CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskChangeIdeeStatusConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strIdeeStatus = request.getParameter( PARAMETER_IDEE_STATUS );

        TaskChangeIdeeStatusConfig config = _taskChangeIdeeStatusConfigService.findByPrimaryKey( task.getId( ) );
        Boolean bCreate = false;

        if ( config == null )
        {
            config = new TaskChangeIdeeStatusConfig( );
            config.setIdTask( task.getId( ) );
            bCreate = true;
        }

        config.setIdeeStatus( strIdeeStatus );

        if ( bCreate )
        {
            _taskChangeIdeeStatusConfigService.create( config );
        }
        else
        {
            _taskChangeIdeeStatusConfigService.update( config );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        TaskChangeIdeeStatusConfig config = _taskChangeIdeeStatusConfigService.findByPrimaryKey( task.getId( ) );
        ReferenceList refListStatus = new ReferenceList( );

        // refListStatus.addItem( StringUtils.EMPTY, StringUtils.EMPTY );
        Status [ ] listStat = Status.values( );

        for ( Status tmpState : listStat )
        {
            refListStatus.addItem( tmpState.getValeur( ), I18nService.getLocalizedString( tmpState.getLibelle( ), locale ) );
        }

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_REF_LIST_STATUS, refListStatus );
        model.put( MARK_CONFIG, config );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, locale );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CHANGE_Idee_Status_CONFIG, locale, model );

        return template.getHtml( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayTaskInformation( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTaskInformationXml( int nIdHistory, HttpServletRequest request, Locale locale, ITask task )
    {
        // TODO Auto-generated method stub
        return null;
    }
}
