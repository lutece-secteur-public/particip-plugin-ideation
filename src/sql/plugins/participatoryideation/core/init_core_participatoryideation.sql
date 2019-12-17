DELETE FROM core_admin_right WHERE id_right = 'IDEATION_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_MANAGEMENT','participatoryideation.adminFeature.ManageIdeation.name',1,'jsp/admin/plugins/participatoryideation/ManageCampagneDepositaires.jsp','participatoryideation.adminFeature.ManageIdeation.description',0,'ideation',NULL,NULL,NULL,4);

DELETE FROM core_user_right WHERE id_right = 'IDEATION_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDEATION_MANAGEMENT',1);

DELETE FROM core_admin_right WHERE id_right = 'IDEATION_IDEES_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_IDEES_MANAGEMENT','participatoryideation.adminFeature.ManageIdeationIdees.name',1,'jsp/admin/plugins/participatoryideation/ManageCampagneDepositaires.jsp','participatoryideation.adminFeature.ManageIdeationIdees.description',0,'ideation',NULL,NULL,NULL,4);

DELETE FROM core_user_right WHERE id_right = 'IDEATION_IDEES_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDEATION_IDEES_MANAGEMENT',1);

DELETE FROM core_admin_right WHERE id_right = 'IDEATION_LINKS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_LINKS_MANAGEMENT','participatoryideation.adminFeature.ManageIdeeLinks.name',1,'jsp/admin/plugins/participatoryideation/ManageIdeeLinks.jsp','participatoryideation.adminFeature.ManageIdeeLinks.description',0,'ideation',NULL,NULL,NULL,4);

INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.titre.minLength','15');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.titre.maxLength','60');

INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.description.minLength','500');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.description.maxLength','2000');

INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.scoreRatioLimit','75');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.distanceLimit','1000');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.keywordResultsCount','6');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.locationResultsCount','6');
INSERT INTO core_datastore VALUES ('participatoryideation.site_property.form.approx.previousCampaignsResultsCount','6');