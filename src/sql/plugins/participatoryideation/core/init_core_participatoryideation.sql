DELETE FROM core_admin_right WHERE id_right = 'PARTICIPATORYIDEATION_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('PARTICIPATORYIDEATION_MANAGEMENT','participatoryideation.adminFeature.ManageIdeation.name',1,'jsp/admin/plugins/participatoryideation/ManageSubmitters.jsp','participatoryideation.adminFeature.ManageIdeation.description',0,'ideation',NULL,NULL,NULL,4);

DELETE FROM core_user_right WHERE id_right = 'PARTICIPATORYIDEATION_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('PARTICIPATORYIDEATION_MANAGEMENT',1);

DELETE FROM core_admin_right WHERE id_right = 'PARTICIPATORYIDEATION_PROPOSALS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('PARTICIPATORYIDEATION_PROPOSALS_MANAGEMENT','participatoryideation.adminFeature.ManageIdeationProposals.name',1,'jsp/admin/plugins/participatoryideation/ManageProposals.jsp','participatoryideation.adminFeature.ManageIdeationProposals.description',0,'ideation',NULL,NULL,NULL,4);

DELETE FROM core_user_right WHERE id_right = 'PARTICIPATORYIDEATION_PROPOSALS_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('PARTICIPATORYIDEATION_PROPOSALS_MANAGEMENT',1);

DELETE FROM core_admin_right WHERE id_right = 'PARTICIPATORYIDEATION_LINKS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('PARTICIPATORYIDEATION_LINKS_MANAGEMENT','participatoryideation.adminFeature.ManageProposalLinks.name',1,'jsp/admin/plugins/participatoryideation/ManageProposalLinks.jsp','participatoryideation.adminFeature.ManageProposalLinks.description',0,'ideation',NULL,NULL,NULL,4);

INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.titre.minLength','15');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.titre.maxLength','60');

INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.description.minLength','500');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.description.maxLength','2000');

INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.scoreRatioLimit','75');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.distanceLimit','1000');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.keywordResultsCount','6');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.locationResultsCount','6');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.previousCampaignsResultsCount','6');

INSERT INTO core_datastore VALUES ('solr.app.conf.list_proposals.addonBeans.0', 'participatoryideation.IdeationSolrAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.list_proposals.fq', 'type:proposal" AND proposal_status_text:"true');
INSERT INTO core_datastore VALUES ('solr.app.conf.list_proposals.mapping', 'false');
INSERT INTO core_datastore VALUES ('solr.app.conf.list_proposals.template', 'skin/plugins/participatoryideation/ideation_list_solr_search_results.html');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_proposals.addonBeans.0', 'participatoryideation.IdeationSolrAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_proposals.fq', 'type:proposal" AND proposal_status_text:"true');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_proposals.mapping', 'true');
INSERT INTO core_datastore VALUES ('solr.app.conf.map_proposals.template', 'skin/plugins/participatoryideation/ideation_map_solr_search_results.html');

INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action) VALUES
	(100, 'Submit', 'Used when a draft proposal is definitely submitted.', 100, 100, 101, 1, 0, 0, 1, 0),
	(101, 'Reinit', 'Return to draft state. ', 100, 101, 100, 3, 0, 0, 1, 0);
	
	
INSERT INTO `workflow_resource_workflow` (`id_resource`, `resource_type`, `id_state`, `id_workflow`, `id_external_parent`, `is_associated_workgroups`) VALUES 
	(1, 'PARTICIPATORYIDEATION_PROPOSAL', 101, 100, -1, 0),
	(2, 'PARTICIPATORYIDEATION_PROPOSAL', 100, 100, -1, 0),
	(3, 'PARTICIPATORYIDEATION_PROPOSAL', 100, 100, -1, 0);

INSERT INTO workflow_state (id_state, name, description, id_workflow, is_initial_state, is_required_workgroup_assigned, id_icon, display_order) VALUES
	(100, 'Draft', 'Draft', 100, 1, 0, NULL, 1),
	(101, 'Submitted', 'Submitted', 100, 0, 0, NULL, 2);
	
INSERT INTO workflow_workflow (id_workflow, name, description, creation_date, is_enabled, workgroup_key) VALUES
	(100, 'Proposal workflow', 'Proposal workflow', '2010-01-01 12:00:00', 1, 'all');