--
-- Structure for table ideation_idees
--
DROP TABLE IF EXISTS `ideation_idees`;
CREATE TABLE IF NOT EXISTS `ideation_idees` (
  `id_idee` int(6) NOT NULL,
  `lutece_user_name` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `titre` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `description` mediumtext COLLATE utf8_unicode_ci NOT NULL,
  `cout` bigint(20) DEFAULT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code_idee` int(6) NOT NULL,
  `code_theme` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `localisation_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `localisation_ardt` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `depositaire_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `depositaire` mediumtext COLLATE utf8_unicode_ci,
  `accept_exploit` smallint(6) NOT NULL DEFAULT '0',
  `address` mediumtext COLLATE utf8_unicode_ci,
  `longitude` float DEFAULT NULL,
  `latitude` float DEFAULT NULL,
  `type_nqpv_qva` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `id_nqpv_qva` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `libelle_nqpv_qva` mediumtext COLLATE utf8_unicode_ci,
  `creation_timestamp` datetime NOT NULL,
  `backoffice_id_bo_tag` int(6) DEFAULT NULL,
  `eudonet_exported_tag` int(6) DEFAULT '0',
  `status_public` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `status_eudonet` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `motif_recev` mediumtext COLLATE utf8_unicode_ci,
  `id_project` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `titre_projet` mediumtext COLLATE utf8_unicode_ci,
  `url_projet` mediumtext COLLATE utf8_unicode_ci,
  `dejadepose` mediumtext COLLATE utf8_unicode_ci,
  `accept_contact` smallint(6) NOT NULL DEFAULT '0',
  `winner_projet` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `creationmethod` mediumtext COLLATE utf8_unicode_ci,
  `operatingbudget` mediumtext COLLATE utf8_unicode_ci,
  `handicap` varchar(3) COLLATE utf8_unicode_ci NOT NULL,
  `handicap_complement` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_idee`),
  UNIQUE KEY `code_campagne` (`code_campagne`,`code_idee`),
  KEY `fk_ideation_idees_bo_tag` (`backoffice_id_bo_tag`),
  CONSTRAINT `fk_ideation_idees_bo_tag` FOREIGN KEY (`backoffice_id_bo_tag`) REFERENCES `ideation_bo_tags` (`id_bo_tag`),
  CONSTRAINT `fk_ideation_idees_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `ideation_campagnes` (`code_campagne`)
);

--
-- Structure for table ideation_idees_files
--
DROP TABLE IF EXISTS `ideation_idees_files`;
CREATE TABLE IF NOT EXISTS `ideation_idees_files` (
  `id_idee_file` int(6) NOT NULL,
  `id_file` int(11) NOT NULL,
  `id_idee` int(11) NOT NULL,
  `type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_idee_file`),
  KEY `id_idee` (`id_idee`,`type`,`id_file`),
  KEY `id_file` (`id_file`),
  CONSTRAINT `ideation_idees_files_file` FOREIGN KEY (`id_file`) REFERENCES `core_file` (`id_file`),
  CONSTRAINT `ideation_idees_files_idee` FOREIGN KEY (`id_idee`) REFERENCES `ideation_idees` (`id_idee`)
);

--
-- Structure for table ideation_idees_links
--
DROP TABLE IF EXISTS `ideation_idees_links`;
CREATE TABLE IF NOT EXISTS `ideation_idees_links` (
  `id_idee_link` int(6) NOT NULL,
  `id_idee_parent` int(6) NOT NULL,
  `id_idee_child` int(6) NOT NULL,
  PRIMARY KEY (`id_idee_link`),
  UNIQUE KEY `id_idee_child` (`id_idee_child`,`id_idee_parent`),
  KEY `id_idee_parent` (`id_idee_parent`)
);

--
-- Structure for table ideation_phase_types
--
DROP TABLE IF EXISTS `ideation_phase_types`;
CREATE TABLE IF NOT EXISTS `ideation_phase_types` (
  `id_phase_type` int(6) NOT NULL,
  `code_phase_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_phase_type`),
  UNIQUE KEY `code_phase_type` (`code_phase_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Structure for table task_notify_ideation_cf
--
DROP TABLE IF EXISTS task_notify_ideation_cf;
CREATE TABLE task_notify_ideation_cf(
  id_task INT NOT NULL,
  sender_name VARCHAR(255) DEFAULT NULL, 
  sender_email VARCHAR(255) DEFAULT NULL,
  subject VARCHAR(255) DEFAULT NULL, 
  message long VARCHAR DEFAULT NULL,
  recipients_cc VARCHAR(255) DEFAULT '' NOT NULL,
  recipients_bcc VARCHAR(255) DEFAULT '' NOT NULL,
  isFollowers SMALLINT NOT NULL,
  isDepositaire SMALLINT NOT NULL,
  PRIMARY KEY  (id_task)
  );

--
-- Structure for table task_change_idee_status_cf
--
DROP TABLE IF EXISTS task_change_idee_status_cf;
CREATE TABLE task_change_idee_status_cf(
  id_task INT NOT NULL,
  idee_status VARCHAR(255) DEFAULT NULL, 
  PRIMARY KEY  (id_task)
  );