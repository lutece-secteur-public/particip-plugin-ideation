DELETE FROM core_admin_right WHERE id_right = 'PARTICIPATORYIDEATION_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('PARTICIPATORYIDEATION_MANAGEMENT','participatoryideation.adminFeature.ManageSubmitters.name',1,'jsp/admin/plugins/participatoryideation/ManageSubmitters.jsp','participatoryideation.adminFeature.ManageSubmitters.description',0,'ideation',NULL,NULL,NULL,4);

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

INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_list.addonBeans.0', 'participatoryideation.IdeationSolrAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_list.fq', 'type:proposal" AND proposal_status_text:"true');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_list.mapping', 'false');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_list.template', 'skin/plugins/participatoryideation/ideation_list_solr_search_results.html');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_map.addonBeans.0', 'participatoryideation.IdeationSolrAddon');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_map.fq', 'type:proposal" AND proposal_status_text:"true');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_map.mapping', 'true');
INSERT INTO core_datastore VALUES ('solr.app.conf.proposals_map.template', 'skin/plugins/participatoryideation/ideation_map_solr_search_results.html');

