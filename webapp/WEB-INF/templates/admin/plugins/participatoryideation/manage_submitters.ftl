<#include "manageideation_tabs.ftl" />
<@tabs tab="submitter" />

<@rowBox>
    <@boxHeader i18nTitleKey="participatoryideation.manage_submitters.tableLabel">
        <@headerButtons>

        
        <form class="form-inline pull-right" method="post" name="manage_submitters" action="jsp/admin/plugins/participatoryideation/ManageSubmitters.jsp">
            <!-- Add pull-right to class1 -->
            <@actionButtons button1Name="view_createSubmitter" icon1="icon-plus icon-white" i18nValue1Key="participatoryideation.manage_submitters.buttonAdd" />
        </form>
        
    </@headerButtons>
    </@boxHeader>
    <@boxBody>    
    <@messages infos=infos />
    <@paginationAdmin paginator=paginator combo=1 />
    
    <@table>
        <tr>
            <input type="hidden" id="id" name="id"/>
            <th>#i18n{participatoryideation.manage_submitters.columnCodeCampaign}</th>
            <th>#i18n{participatoryideation.manage_submitters.columnCodeSubmitter}</th>
            <th>#i18n{portal.util.labelActions}</th>
        </tr>
        <@tableHeadBodySeparator />
            <#list submitter_list as submitter >
        <tr>
            <input type="hidden" id="id" name="id"/>
            <td>
                ${submitter.codeCampaign}
            </td>
            <td>
                ${submitter.codeSubmitterType}
            </td>
        <td>
            <a href="jsp/admin/plugins/participatoryideation/ManageSubmitters.jsp?view=modifySubmitter&id=${submitter.id}"
               class="btn btn-primary btn-small" title="#i18n{portal.util.labelModify}">
                <i class="fa fa-pencil"></i>
            </a>

            <a href="jsp/admin/plugins/participatoryideation/ManageSubmitters.jsp?action=confirmRemoveSubmitter&id=${submitter.id}"
               class="btn btn-danger btn-small" title="#i18n{portal.util.labelDelete}" >
                <i class="fa fa-trash"></i>
            </a>
        </td>
        </tr>
        </#list>
    </@table>

    <@paginationAdmin paginator=paginator />

  </@boxBody>
</@rowBox>

