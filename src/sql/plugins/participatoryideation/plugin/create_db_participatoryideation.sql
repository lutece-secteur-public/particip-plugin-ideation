DROP TABLE IF EXISTS task_notify_ideation_cf;
DROP TABLE IF EXISTS task_change_proposal_status_cf;
DROP TABLE IF EXISTS participatoryideation_proposals_links;
DROP TABLE IF EXISTS participatoryideation_proposals_files;
DROP TABLE IF EXISTS participatoryideation_proposals;
DROP TABLE IF EXISTS participatoryideation_depositaries;
DROP TABLE IF EXISTS participatoryideation_depositaries_types_values;
DROP TABLE IF EXISTS participatoryideation_depositaries_types;
DROP TABLE IF EXISTS participatoryideation_depositaries_complement_types;

CREATE TABLE IF NOT EXISTS participatoryideation_depositaries_complement_types (
  id_depositary_complement_type int NOT NULL,
  code_depositary_complement_type varchar(50) NOT NULL,
  libelle varchar(255) NOT NULL,
  PRIMARY KEY (id_depositary_complement_type)
);

CREATE TABLE IF NOT EXISTS participatoryideation_depositaries_types (
  id_depositary_type int NOT NULL,
  code_depositary_type varchar(50) NOT NULL,
  libelle varchar(255) NOT NULL,
  code_complement_type varchar(50) NOT NULL,
  PRIMARY KEY (id_depositary_type)
);

CREATE TABLE IF NOT EXISTS participatoryideation_depositaries_types_values (
  id_depositary_type_value int NOT NULL,
  code_depositary_type varchar(50) NOT NULL,
  code varchar(50) NOT NULL,
  libelle varchar(255) NOT NULL,
  PRIMARY KEY (id_depositary_type_value)
);

CREATE TABLE IF NOT EXISTS participatoryideation_depositaries (
  id_depositary int NOT NULL,
  code_campaign varchar(50) NOT NULL,
  code_depositary_type varchar(50) NOT NULL,
  PRIMARY KEY (id_depositary)
);
ALTER TABLE participatoryideation_depositaries ADD CONSTRAINT fk_participatoryideation_depositaries_depositary  FOREIGN KEY (code_depositary_type) REFERENCES participatoryideation_depositaries_types (code_depositary_type);

CREATE TABLE IF NOT EXISTS participatoryideation_proposals (
  id_proposal int NOT NULL,
  lutece_user_name varchar(255) NOT NULL,
  titre VARCHAR(255) NOT NULL,
  description VARCHAR(4000) NOT NULL,
  cout bigint DEFAULT NULL,
  code_campaign varchar(50) NOT NULL,
  code_proposal int NOT NULL,
  code_theme varchar(50) NOT NULL,
  localisation_type varchar(50) NOT NULL,
  localisation_ardt varchar(50) DEFAULT NULL,
  depositary_type varchar(50) NOT NULL,
  depositary VARCHAR(255),
  accept_exploit smallint DEFAULT '0' NOT NULL,
  address VARCHAR(4000),
  longitude float DEFAULT NULL,
  latitude float DEFAULT NULL,
  type_nqpv_qva varchar(50) NOT NULL,
  id_nqpv_qva varchar(50) DEFAULT NULL,
  libelle_nqpv_qva VARCHAR(4000),
  creation_timestamp datetime NOT NULL,
  eudonet_exported_tag int DEFAULT '0',
  status_public varchar(50) NOT NULL,
  status_eudonet varchar(50) DEFAULT NULL,
  motif_recev VARCHAR(4000),
  id_project varchar(50) DEFAULT NULL,
  titre_projet VARCHAR(255),
  url_projet VARCHAR(500),
  dejadepose VARCHAR(255),
  accept_contact smallint DEFAULT '0' NOT NULL,
  winner_projet varchar(50) DEFAULT NULL,
  creationmethod VARCHAR(255),
  operatingbudget VARCHAR(255),
  handicap varchar(3) NOT NULL,
  handicap_complement varchar(255) NOT NULL,
  PRIMARY KEY (id_proposal)
);

CREATE TABLE IF NOT EXISTS participatoryideation_proposals_files (
  id_proposal_file int NOT NULL,
  id_file int NOT NULL,
  id_proposal int NOT NULL,
  type varchar(50) NOT NULL,
  PRIMARY KEY (id_proposal_file)
);
CREATE INDEX participatoryideation_proposals_files_index_id_proposal ON participatoryideation_proposals_files ( id_proposal,type,id_file );
CREATE INDEX participatoryideation_proposals_files_index_id_file ON participatoryideation_proposals_files ( id_file );

CREATE TABLE IF NOT EXISTS participatoryideation_proposals_links (
  id_proposal_link int NOT NULL,
  id_proposal_parent int NOT NULL,
  id_proposal_child int NOT NULL,
  PRIMARY KEY (id_proposal_link)
);
CREATE INDEX participatoryideation_proposals_links_index_id_proposal_parent ON participatoryideation_proposals_links ( id_proposal_parent );

CREATE TABLE IF NOT EXISTS task_change_proposal_status_cf (
  id_task int NOT NULL,
  proposal_status varchar(255) DEFAULT NULL,
  PRIMARY KEY (id_task)
);

CREATE TABLE IF NOT EXISTS task_notify_ideation_cf (
  id_task int NOT NULL,
  sender_name varchar(255) DEFAULT NULL,
  sender_email varchar(255) DEFAULT NULL,
  subject varchar(255) DEFAULT NULL,
  message VARCHAR(4000),
  recipients_cc varchar(255) DEFAULT '' NOT NULL,
  recipients_bcc varchar(255) DEFAULT '' NOT NULL,
  isFollowers smallint NOT NULL,
  isDepositary smallint NOT NULL,
  PRIMARY KEY (id_task)
);