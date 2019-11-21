
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'IDEATION_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_MANAGEMENT','ideation.adminFeature.ManageIdeation.name',1,'jsp/admin/plugins/ideation/ManageCampagneDepositaires.jsp','ideation.adminFeature.ManageIdeation.description',0,'ideation',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'IDEATION_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDEATION_MANAGEMENT',1);



--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'IDEATION_IDEES_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_IDEES_MANAGEMENT','ideation.adminFeature.ManageIdeationIdees.name',1,'jsp/admin/plugins/ideation/ManageCampagneDepositaires.jsp','ideation.adminFeature.ManageIdeationIdees.description',0,'ideation',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'IDEATION_IDEES_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDEATION_IDEES_MANAGEMENT',1);


--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'IDEATION_TAGS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_TAGS_MANAGEMENT','ideation.adminFeature.ManageIdeeTags.name',1,'jsp/admin/plugins/ideation/ManageBoTags.jsp','ideation.adminFeature.ManageIdeeTags.description',0,'ideation',NULL,NULL,NULL,4);

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'IDEATION_LINKS_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDEATION_LINKS_MANAGEMENT','ideation.adminFeature.ManageIdeeLinks.name',1,'jsp/admin/plugins/ideation/ManageIdeeLinks.jsp','ideation.adminFeature.ManageIdeeLinks.description',0,'ideation',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'IDEATION_TAGS_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDEATION_TAGS_MANAGEMENT',1);


-- Default labels --
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.code_theme_label.htmlblock','<p>Choisissez le <strong>th&egrave;me *</strong> de votre projet</p>');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.image_exploit_label_text','J\'authorise la Mairie de Paris à exploiter cette image (...).');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.accept_contact_label_text','Acceptez-vous d''être contacté par courrier électronique par d''autres usagers du site ? Votre adresse email ne sera pas communiquée.');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.doc_label_text','Documents à déposer (hors images) :');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.image_label_text','Images à déposer :');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.cout_label_text','J\'estime le coût de mon projet (facultatif, exprimé en €) :');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.depositaire_label.htmlblock','<p>&Agrave; quel titre d&eacute;posez-vous ce projet ?</p>');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.localisation_label_precis_ardt','Numéro d\'arrondissement:');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.localisation_label_precis_adresse','Adresse:');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.localisation_label_toutparis','tout paris');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.localisation_type_label.htmlblock','<p>Choisissez sa <strong>port&eacute;e *</strong>.</p>');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.adress_label.htmlblock','<p>Vous pouvez optionellement indiquer une adresse pr&eacute;cise ou le nom d\'un &eacute;quipement.</p>');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.localisation_label_arrondissement','Un arrondissement');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.titre_label.htmlblock','<p>Choisissez le <strong>titre *</strong> de votre projet. Ce titre doit expliciter en quelques mots l&rsquo;objectif de votre proposition. Ce titre permettra aux autres internautes de retrouver votre projet &agrave; l&rsquo;aide des mots-cl&eacute;s que vous y aurez indiqu&eacute;.</p>');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.titre_label_text','Titre :');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.dejadepose_label_text','Avez-vous déja déposé ce projet lors d''une précédente édition ?');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.creationmethod_label_text','Quelle méthode d\'élaboration avez-vous suivie ?');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_idee.site_properties.contact_subject','contacter dépositeur');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_idee.site_properties.contact_message_content.textblock','${nom_depositaire}, ${nom_contacteur}, ${message}');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.titre.minLength','15');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.titre.maxLength','60');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.description_label.htmlblock','<p>D&eacute;crivez votre projet explicitement (...). ?</p>');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.description_label_text','Description * du projet');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.description.minLength','500');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.description.maxLength','2000');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_idee.non_existing.htmlblock','<p class="text-center">L\'idée n\'existe pas.</p>');
INSERT INTO `core_datastore` VALUES ('sitelabels.site_property.recherche.help.textblock','Entrez des mots-clés, un pseudo, une reference...');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_my_subscriptions.title.textblock','Mes notifications');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_my_subscriptions.message.textblock','Je souhaite recevoir une notification par email pour les évènements suivants :');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_my_subscriptions.buttonSave','J\'enregistre mes Notifications');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.approx.scoreRatioLimit','75');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.approx.distanceLimit','1000');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.approx.keywordResultsCount','6');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.approx.locationResultsCount','6');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.approx.previousCampaignsResultsCount','6');
INSERT INTO `core_datastore` VALUES ('ideation.site_property.view_idee.site_properties.contact_message_not_accept','Impossible d''envoyer de message au dépositaire');


