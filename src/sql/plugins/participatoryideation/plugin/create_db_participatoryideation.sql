DROP TABLE IF EXISTS `ideation_campagnes_depositaires`;
CREATE TABLE IF NOT EXISTS `ideation_campagnes_depositaires` (
  `id_campagne_depositaire` int(6) NOT NULL,
  `code_campagne` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code_depositaire_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_campagne_depositaire`),
  KEY `fk_ideation_campagnes_depositaires_campagne` (`code_campagne`),
  KEY `fk_ideation_campagnes_depositaires_depositaire` (`code_depositaire_type`),
  CONSTRAINT `fk_ideation_campagnes_depositaires_campagne` FOREIGN KEY (`code_campagne`) REFERENCES `participatorybudget_campaign` (`code_campagne`),
  CONSTRAINT `fk_ideation_campagnes_depositaires_depositaire` FOREIGN KEY (`code_depositaire_type`) REFERENCES `ideation_depositaire_types` (`code_depositaire_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `ideation_depositaire_complement_types`;
CREATE TABLE IF NOT EXISTS `ideation_depositaire_complement_types` (
  `id_depositaire_complement_type` int(6) NOT NULL,
  `code_depositaire_complement_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depositaire_complement_type`),
  UNIQUE KEY `code_depositaire_complement_type` (`code_depositaire_complement_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `ideation_depositaire_types`;
CREATE TABLE IF NOT EXISTS `ideation_depositaire_types` (
  `id_depositaire_type` int(6) NOT NULL,
  `code_depositaire_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `code_complement_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depositaire_type`),
  UNIQUE KEY `code_depositaire_type` (`code_depositaire_type`),
  KEY `fk_ideation_depositaire_types_complement` (`code_complement_type`),
  CONSTRAINT `fk_ideation_depositaire_types_complement` FOREIGN KEY (`code_complement_type`) REFERENCES `ideation_depositaire_complement_types` (`code_depositaire_complement_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `ideation_depositaire_types_values`;
CREATE TABLE IF NOT EXISTS `ideation_depositaire_types_values` (
  `id_depositaire_type_value` int(6) NOT NULL,
  `code_depositaire_type` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `libelle` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id_depositaire_type_value`),
  UNIQUE KEY `code_depositaire_type` (`code_depositaire_type`,`code`),
  CONSTRAINT `fk_ideation_depositaire_type_values_depositaire` FOREIGN KEY (`code_depositaire_type`) REFERENCES `ideation_depositaire_types` (`code_depositaire_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
  UNIQUE KEY `code_campagne` (`code_campagne`,`code_idee`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS `ideation_idees_links`;
CREATE TABLE IF NOT EXISTS `ideation_idees_links` (
  `id_idee_link` int(6) NOT NULL,
  `id_idee_parent` int(6) NOT NULL,
  `id_idee_child` int(6) NOT NULL,
  PRIMARY KEY (`id_idee_link`),
  UNIQUE KEY `id_idee_child` (`id_idee_child`,`id_idee_parent`),
  KEY `id_idee_parent` (`id_idee_parent`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `task_change_idee_status_cf`;
CREATE TABLE IF NOT EXISTS `task_change_idee_status_cf` (
  `id_task` int(11) NOT NULL,
  `idee_status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_task`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `task_notify_ideation_cf`;
CREATE TABLE IF NOT EXISTS `task_notify_ideation_cf` (
  `id_task` int(11) NOT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `sender_email` varchar(255) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `message` mediumtext,
  `recipients_cc` varchar(255) NOT NULL DEFAULT '',
  `recipients_bcc` varchar(255) NOT NULL DEFAULT '',
  `isFollowers` smallint(6) NOT NULL,
  `isDepositaire` smallint(6) NOT NULL,
  PRIMARY KEY (`id_task`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
