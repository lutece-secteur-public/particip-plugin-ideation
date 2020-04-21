<#include "manageideation_proposal_tabs.ftl" />
<@tabs tab="proposal" />

<@rowBoxHeader i18nTitleKey="participatoryideation.init_proposal.pageTitle">

	<#-- *********************************************************************************** -->
	<#-- * FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM * -->
	<#-- * FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM FORM * -->
	<#-- *********************************************************************************** -->

	<form class="form-horizontal" method="post" name="init_proposal" action="jsp/admin/plugins/participatoryideation/ManageProposals.jsp">

		<@messages errors=errors />

		<input type="hidden" id="id" name="id"/>

		<@fieldInputCombo i18nLabelKey="participatoryideation.init_proposal.labelCampaign" inputName="campaign_code" items=campaign_list value="${(proposal.codeCampaign)!}" i18nHelpBlockKey="participatoryideation.init_proposal.labelCampaign.help" />

		<@actionButtons button1Name="view_completeProposal" button2Name="view_manageProposals"/>

	</form>

</@rowBoxHeader>
