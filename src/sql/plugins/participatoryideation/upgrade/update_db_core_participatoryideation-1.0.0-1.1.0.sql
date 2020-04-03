INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_proposal.non_existing.htmlblock', '<p class="text-center">No such proposal.</p>');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_proposal.site_properties.contact_message_content.textblock', '${nom_depositary}, ${nom_contacteur}, ${message}');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_proposal.site_properties.contact_message_not_accept', 'Unable to sent the message.');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_proposal.site_properties.contact_subject', 'contact submitter');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_my_subscriptions.buttonSave', 'Save my subscriptions');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_my_subscriptions.message.textblock', 'I want to receive a notification about this events :');
INSERT INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('participatoryideation.site_property.view_my_subscriptions.title.textblock', 'My notifications');

INSERT INTO `workflow_workflow` (`id_workflow`, `name`, `description`, `creation_date`, `is_enabled`, `workgroup_key`) VALUES
	(100, 'Proposal workflow', 'Proposal workflow', '2010-01-01 12:00:00', 1, 'all');

INSERT INTO `workflow_state` (`id_state`, `name`, `description`, `id_workflow`, `is_initial_state`, `is_required_workgroup_assigned`, `id_icon`, `display_order`) VALUES
	(100, 'Draft', 'Draft', 100, 1, 0, NULL, 1),
	(101, 'Submitted', 'Submitted', 100, 0, 0, NULL, 2);
	
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action) VALUES
	(1, 'Submit', 'Used when a draft proposal is definitely submitted. ', 100, 100, 101, 1, 0, 0, 1, 0);