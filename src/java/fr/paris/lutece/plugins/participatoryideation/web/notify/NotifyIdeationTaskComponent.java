/*
 * Copyright (c) 2002-2020, City of Paris
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

import fr.paris.lutece.plugins.participatoryideation.business.notify.TaskNotifyIdeationConfig;
import fr.paris.lutece.plugins.participatoryideation.service.notify.TaskNotifyIdeation;
import fr.paris.lutece.plugins.workflow.web.task.NoFormTaskComponent;
import fr.paris.lutece.plugins.workflowcore.service.config.ITaskConfigService;
import fr.paris.lutece.plugins.workflowcore.service.task.ITask;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.util.html.HtmlTemplate;

import org.apache.commons.lang.StringUtils;

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
public class NotifyIdeationTaskComponent extends NoFormTaskComponent
{

    // Properties
    public static final String PROPERTY_NOTIFY_MAIL_DEFAULT_SENDER_NAME = "workflow-ideation.notification_mail.default_sender_name";
    public static final String PROPERTY_NOTIFY_MAIL_DEFAULT_SENDER_EMAIL = "workflow-ideation.notification_mail.default_sender_email";

    // PARAMETERS
    public static final String PARAMETER_SUBJECT = "subject";
    public static final String PARAMETER_MESSAGE = "message";
    public static final String PARAMETER_SENDER_NAME = "sender_name";
    public static final String PARAMETER_SENDER_EMAIL = "sender_email";
    public static final String PARAMETER_RECIPIENTS_CC = "recipients_cc";
    public static final String PARAMETER_RECIPIENTS_BCC = "recipients_bcc";
    public static final String PARAMETER_FOLLOWERS = "followers";
    public static final String PARAMETER_SUBMITTER = "submitter";

    // For rich text editor
    public static final String MARK_WEBAPP_URL = "webapp_url";
    public static final String MARK_LOCALE = "locale";

    // MARKS
    public static final String MARK_CONFIG = "config";
    public static final String MARK_DEFAULT_SENDER_NAME = "default_sender_name";
    public static final String MARK_DEFAULT_SENDER_EMAIL = "default_sender_email";
    private static final String MARK_FALSE = "false";

    // TEMPLATES
    private static final String TEMPLATE_TASK_NOTIFY_DIRECTORY_CONFIG = "admin/plugins/participatoryideation/notify/task_notify_ideation_config.html";

    // SERVICES
    @Inject
    @Named( TaskNotifyIdeation.CONFIG_SERVICE_BEAN_NAME )
    private ITaskConfigService _taskNotifyIdeationConfigService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String doSaveConfig( HttpServletRequest request, Locale locale, ITask task )
    {
        String strSenderName = request.getParameter( PARAMETER_SENDER_NAME );
        String strSenderEmail = request.getParameter( PARAMETER_SENDER_EMAIL );
        String strSubject = request.getParameter( PARAMETER_SUBJECT );
        String strMessage = request.getParameter( PARAMETER_MESSAGE );
        String strRecipientsCc = request.getParameter( PARAMETER_RECIPIENTS_CC );
        String strRecipientsBcc = request.getParameter( PARAMETER_RECIPIENTS_BCC );
        String strFollowers = ( request.getParameter( PARAMETER_FOLLOWERS ) == null ) ? MARK_FALSE : request.getParameter( PARAMETER_FOLLOWERS );
        String strSumbitters = ( request.getParameter( PARAMETER_SUBMITTER ) == null ) ? MARK_FALSE : request.getParameter( PARAMETER_SUBMITTER );

        TaskNotifyIdeationConfig config = _taskNotifyIdeationConfigService.findByPrimaryKey( task.getId( ) );
        Boolean bCreate = false;

        if ( config == null )
        {
            config = new TaskNotifyIdeationConfig( );
            config.setIdTask( task.getId( ) );
            bCreate = true;
        }

        config.setMessage( strMessage );
        config.setSenderName( strSenderName );
        config.setSenderEmail( strSenderEmail );
        config.setSubject( strSubject );
        config.setRecipientsCc( StringUtils.isNotEmpty( strRecipientsCc ) ? strRecipientsCc : StringUtils.EMPTY );
        config.setRecipientsBcc( StringUtils.isNotEmpty( strRecipientsBcc ) ? strRecipientsBcc : StringUtils.EMPTY );
        config.setFollowers( Boolean.parseBoolean( strFollowers ) );
        config.setSubmitter( Boolean.parseBoolean( strSumbitters ) );

        if ( bCreate )
        {
            _taskNotifyIdeationConfigService.create( config );
        }
        else
        {
            _taskNotifyIdeationConfigService.update( config );
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayConfigForm( HttpServletRequest request, Locale locale, ITask task )
    {
        TaskNotifyIdeationConfig config = _taskNotifyIdeationConfigService.findByPrimaryKey( task.getId( ) );
        String strDefaultSenderName = AppPropertiesService.getProperty( PROPERTY_NOTIFY_MAIL_DEFAULT_SENDER_NAME );
        String strDefaultSenderEmail = AppPropertiesService.getProperty( PROPERTY_NOTIFY_MAIL_DEFAULT_SENDER_EMAIL );

        Map<String, Object> model = new HashMap<String, Object>( );

        model.put( MARK_CONFIG, config );
        model.put( MARK_DEFAULT_SENDER_NAME, strDefaultSenderName );
        model.put( MARK_DEFAULT_SENDER_EMAIL, strDefaultSenderEmail );
        model.put( MARK_WEBAPP_URL, AppPathService.getBaseUrl( request ) );
        model.put( MARK_LOCALE, locale );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_TASK_NOTIFY_DIRECTORY_CONFIG, locale, model );

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
