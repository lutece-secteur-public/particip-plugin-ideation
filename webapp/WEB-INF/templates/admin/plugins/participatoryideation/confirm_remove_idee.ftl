<#include "manageideation_proposal_tabs.ftl" />
<@tabs tab="proposal" />

<@rowBoxHeader i18nTitleKey="participatoryideation.confirm_remove_proposal.pageTitle">
    <form class="form-horizontal" method="post" name="confirm_remove_proposal" action="jsp/admin/plugins/participatoryideation/ManageProposals.jsp">
        <@messages errors=errors />
        <input type="hidden" id="id" name="id" value="${proposal.id}"/>
        <input type="hidden" id="exportedTag" name="exportedTag" value="${proposal.exportedTag!}"/>

        <@fieldInputText i18nLabelKey="participatoryideation.confirm_remove_proposal.labelMotifRecev" inputName="motifRecev" mandatory=true value="${proposal.motifRecev!}" i18nHelpBlockKey="participatoryideation.confirm_remove_proposal.labelMotifRecev.help" />

        <@actionButtons button1Name="action_confirmRemoveProposal" button2Name="view_manageProposals"/>
    </form>
</@rowBoxHeader>

