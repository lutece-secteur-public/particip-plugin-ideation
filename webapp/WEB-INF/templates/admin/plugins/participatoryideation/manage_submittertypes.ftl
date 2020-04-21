<#include "manageideation_tabs.ftl" />
<@tabs tab="submittertype" />

<@rowBox>
    <@boxHeader i18nTitleKey="participatoryideation.manage_submittertypes.tableLabel">
        <@headerButtons>

        
        <form class="form-inline pull-right" method="post" name="manage_submittertypes" action="jsp/admin/plugins/participatoryideation/ManageSubmitterTypes.jsp">
            <!-- Add pull-right to class1 -->
            <@actionButtons button1Name="view_createSubmitterType" icon1="icon-plus icon-white" i18nValue1Key="participatoryideation.manage_submittertypes.buttonAdd" />
        </form>
        
    </@headerButtons>
    </@boxHeader>
    <@boxBody>    
    <@messages infos=infos />
    <@paginationAdmin paginator=paginator combo=1 />
    
    <@table>
        <tr>
            <input type="hidden" id="id" name="id"/>
            <th>#i18n{participatoryideation.manage_submittertypes.columnCode}</th>
            <th>#i18n{participatoryideation.manage_submittertypes.columnLibelle}</th>
            <th>#i18n{participatoryideation.manage_submittertypes.columnCodeComplementType}</th>
            <th>#i18n{portal.util.labelActions}</th>
        </tr>
        <@tableHeadBodySeparator />
            <#list submittertype_list as submittertype >
        <tr>
            <input type="hidden" id="id" name="id"/>
            <td>
                ${submittertype.code}
            </td>
            <td>
                ${submittertype.libelle}
            </td>
            <td>
                ${submittertype.codeComplementType}
            </td>
        <td>
            <a href="jsp/admin/plugins/participatoryideation/ManageSubmitterTypes.jsp?view=modifySubmitterType&id=${submittertype.id}"
               class="btn btn-primary btn-small" title="#i18n{portal.util.labelModify}">
                <i class="fa fa-pencil"></i>
            </a>

            <a href="jsp/admin/plugins/participatoryideation/ManageSubmitterTypes.jsp?action=confirmRemoveSubmitterType&id=${submittertype.id}"
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

