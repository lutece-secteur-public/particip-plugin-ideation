<#include "manageproposallinks_tabs.ftl" />
<@tabs tab="link" />

<@rowBoxHeader i18nTitleKey="participatoryideation.create_link.pageTitle">

    <form class="form-horizontal" method="post" name="create_link" action="jsp/admin/plugins/participatoryideation/ManageProposalLinks.jsp">

        <@messages errors=errors />
        
        <input type="hidden" id="id" name="id"/>

        <div class="form-group">
			<label class="col-xs-12 col-sm-3 col-md-3" for="parentCodeCampaign">#i18n{participatoryideation.create_link.labelParentCodeCampaign} * : </label>
			<div class="col-xs-12 col-sm-9 col-md-9">
 				<select id="parentCodeCampaign" name="parentCodeCampaign" class="form-control">
					<option value=""></option>					         
					<#list global_static?keys as key>
						<#assign campaign = global_static[key].campaign>
						<option value="${campaign.code}" <#if link.parentCodeCampaign?? && campaign.code == link.parentCodeCampaign> selected="selected"</#if>>${campaign.title}</option>
					</#list>
           		</select>
				<p class="help-block">#i18n{participatoryideation.create_link.labelParentCodeCampaign.help}</p>
			</div>               
        </div>

		<@fieldInputText i18nLabelKey="participatoryideation.create_link.labelParentCodeProposal" inputName="parentCodeProposal" value="${link.parentCodeProposal!}" i18nHelpBlockKey='ideation.create_link.labelParentCodeProposal.help' />

        <div class="form-group">
			<label class="col-xs-12 col-sm-3 col-md-3" for="childCodeCampaign">#i18n{participatoryideation.create_link.labelChildCodeCampaign} * : </label>
			<div class="col-xs-12 col-sm-9 col-md-9">
 				<select id="childCodeCampaign" name="childCodeCampaign" class="form-control">
					<option value=""></option>					         
					<#list global_static?keys as key>
						<#assign campaign = global_static[key].campaign>
              			<option value="${campaign.code}" <#if link.childCodeCampaign?? && campaign.code == link.childCodeCampaign> selected="selected"</#if>>${campaign.title}</option>
					</#list>
           		</select>
				<p class="help-block">#i18n{participatoryideation.create_link.labelChildCodeCampaign.help}</p>
			</div>               
        </div>

		<@fieldInputText i18nLabelKey="participatoryideation.create_link.labelChildCodeProposal" inputName="childCodeProposal" value="${link.childCodeProposal!}" i18nHelpBlockKey='ideation.create_link.labelChildCodeProposal.help' />

        <@actionButtons button1Name="action_createLink" button2Name="view_manageLinks"/>

    </form>

</@rowBoxHeader>

