<#include "manageproposallinks_tabs.ftl" />
<@tabs tab="link" />

<@rowBoxHeader i18nTitleKey="participatoryideation.create_several_links.pageTitle">

    <form class="form-horizontal" method="post" name="create_several_links" action="jsp/admin/plugins/participatoryideation/ManageProposalLinks.jsp">

        <@messages errors=errors />
        
        <input type="hidden" id="id" name="id"/>

        <div class="form-group">
			<label class="col-xs-12 col-sm-3 col-md-3" for="parentCodeCampaign">#i18n{participatoryideation.create_several_links.labelParentCodeCampaign} * : </label>
			<div class="col-xs-12 col-sm-9 col-md-9">
 				<select id="parentCodeCampaign" name="parentCodeCampaign" class="form-control">
					<option value=""></option>					         
					<#list global_static?keys as key>
						<#assign campaign = global_static[key].campaign>
						<option value="${campaign.code}" <#if severalLinksParentCodeCampain?? && campaign.code == severalLinksParentCodeCampain> selected="selected"</#if>>${campaign.title}</option>
					</#list>
           		</select>
				<p class="help-block">#i18n{participatoryideation.create_several_links.labelParentCodeCampaign.help}</p>
			</div>               
        </div>

		<@fieldInputText i18nLabelKey="participatoryideation.create_several_links.labelParentCodeProposal" inputName="parentCodeProposal" value="${severalLinksParentCodeProposal!}" i18nHelpBlockKey='ideation.create_several_links.labelParentCodeProposal.help' />

        <div class="form-group">
			<label class="col-xs-12 col-sm-3 col-md-3" for="childCodeCampaign">#i18n{participatoryideation.create_several_links.labelChildCodeCampaign} * : </label>
			<div class="col-xs-12 col-sm-9 col-md-9">
 				<select id="childCodeCampaign" name="childCodeCampaign" class="form-control">
					<option value=""></option>					         
					<#list global_static?keys as key>
						<#assign campaign = global_static[key].campaign>
              			<option value="${campaign.code}" <#if severalLinksChildCodeCampain?? && campaign.code == severalLinksChildCodeCampain> selected="selected"</#if>>${campaign.title}</option>
					</#list>
           		</select>
				<p class="help-block">#i18n{participatoryideation.create_several_links.labelChildCodeCampaign.help}</p>
			</div>               
        </div>

		<@fieldInputText i18nLabelKey="participatoryideation.create_several_links.labelChildCodeProposal" inputName="childCodesProposals" value="${severalLinksChildCodesProposals!}" i18nHelpBlockKey='ideation.create_several_links.labelChildCodeProposal.help' />

        <@actionButtons button1Name="action_createSeveralLinks" button2Name="view_manageLinks"/>

    </form>

</@rowBoxHeader>

