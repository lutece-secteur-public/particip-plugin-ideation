<#include "manageideation_tabs.ftl" />
<@tabs tab="submittertype" />

<@rowBoxHeader i18nTitleKey="participatoryideation.create_submittertype.pageTitle">
    <form class="form-horizontal" method="post" name="create_submittertype" action="jsp/admin/plugins/participatoryideation/ManageSubmitterTypes.jsp">
        <@messages errors=errors />
        <input type="hidden" id="id" name="id"/>
        
        <@fieldInputText i18nLabelKey="participatoryideation.create_submittertype.labelCode" inputName="code" mandatory=true value="${submittertype.code!''}" i18nHelpBlockKey="participatoryideation.create_submittertype.labelCode.help" />
        <@fieldInputText i18nLabelKey="participatoryideation.create_submittertype.labelLibelle" inputName="libelle" mandatory=true value="${submittertype.title!''}" i18nHelpBlockKey="participatoryideation.create_submittertype.labelLibelle.help" />
        <@fieldInputText i18nLabelKey="participatoryideation.create_submittertype.labelCodeComplementType" inputName="code_complement_type" mandatory=true value="${submittertype.description!''}" i18nHelpBlockKey="participatoryideation.create_submittertype.labelCodeComplementType.help" />
        <@actionButtons button1Name="action_createSubmitterType" button2Name="view_manageSubmitterTypes"/>
    </form>
</@rowBoxHeader>

