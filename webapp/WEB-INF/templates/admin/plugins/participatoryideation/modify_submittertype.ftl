<#include "manageideation_tabs.ftl" />
<@tabs tab="submittertype" />

<@rowBoxHeader i18nTitleKey="participatoryideation.modify_submittertype.pageTitle">
    <form class="form-horizontal" method="post" name="modify_submittertype" action="jsp/admin/plugins/participatoryideation/ManageSubmitterTypes.jsp">
        <@messages errors=errors />
        <input type="hidden" id="id" name="id" value="${submittertype.id}"/>
        
        <@fieldInputText i18nLabelKey="participatoryideation.modify_submittertype.labelCode" inputName="code" mandatory=true value="${submittertype.code}" i18nHelpBlockKey="participatoryideation.modify_submittertype.labelCode.help" />
        <@fieldInputText i18nLabelKey="participatoryideation.modify_submittertype.labelLibelle" inputName="libelle" mandatory=true value="${submittertype.libelle}" i18nHelpBlockKey="participatoryideation.modify_submittertype.labelLibelle.help" />
        <@fieldInputText i18nLabelKey="participatoryideation.modify_submittertype.labelCodeComplementType" inputName="code_complement_type" mandatory=true value="${submittertype.codeComplementType}" i18nHelpBlockKey="participatoryideation.modify_submittertype.labelCodeComplementType.help" />
        <@actionButtons button1Name="action_modifySubmitterType" button2Name="view_manageSubmitterTypes"/>
    </form>
</@rowBoxHeader>

