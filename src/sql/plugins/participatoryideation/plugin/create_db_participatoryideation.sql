DROP TABLE IF EXISTS task_notify_ideation_cf;
DROP TABLE IF EXISTS task_change_idee_status_cf;
DROP TABLE IF EXISTS ideation_idees_links;
DROP TABLE IF EXISTS ideation_idees_files;
DROP TABLE IF EXISTS ideation_idees;
DROP TABLE IF EXISTS ideation_campagnes_depositaires;
DROP TABLE IF EXISTS ideation_depositaire_types_values;
DROP TABLE IF EXISTS ideation_depositaire_types;
DROP TABLE IF EXISTS ideation_depositaire_complement_types;

CREATE TABLE IF NOT EXISTS ideation_depositaire_complement_types (
  id_depositaire_complement_type int NOT NULL,
  code_depositaire_complement_type varchar(50) NOT NULL,
  libelle varchar(255) NOT NULL,
  PRIMARY KEY (id_depositaire_complement_type)
);
ALTER TABLE ideation_depositaire_complement_types ADD CONSTRAINT uc_code_depositaire_complement_type UNIQUE (code_depositaire_complement_type);

CREATE TABLE IF NOT EXISTS ideation_depositaire_types (
  id_depositaire_type int NOT NULL,
  code_depositaire_type varchar(50) NOT NULL,
  libelle varchar(255) NOT NULL,
  code_complement_type varchar(50) NOT NULL,
  PRIMARY KEY (id_depositaire_type)
);
ALTER TABLE ideation_depositaire_types ADD CONSTRAINT uc_code_depositaire_type UNIQUE (code_depositaire_type);
ALTER TABLE ideation_depositaire_types ADD CONSTRAINT fk_ideation_depositaire_types_complement  FOREIGN KEY (code_complement_type) REFERENCES ideation_depositaire_complement_types (code_depositaire_complement_type);

CREATE TABLE IF NOT EXISTS ideation_depositaire_types_values (
  id_depositaire_type_value int NOT NULL,
  code_depositaire_type varchar(50) NOT NULL,
  code varchar(50) NOT NULL,
  libelle varchar(255) NOT NULL,
  PRIMARY KEY (id_depositaire_type_value)
);
ALTER TABLE ideation_depositaire_types_values ADD CONSTRAINT uc_code_depositaire_type_code UNIQUE (code_depositaire_type,code);
ALTER TABLE ideation_depositaire_types_values ADD CONSTRAINT fk_ideation_depositaire_type_values_depositaire  FOREIGN KEY (code_depositaire_type) REFERENCES ideation_depositaire_types (code_depositaire_type);

CREATE TABLE IF NOT EXISTS ideation_campagnes_depositaires (
  id_campagne_depositaire int NOT NULL,
  code_campagne varchar(50) NOT NULL,
  code_depositaire_type varchar(50) NOT NULL,
  PRIMARY KEY (id_campagne_depositaire)
);
ALTER TABLE ideation_campagnes_depositaires ADD CONSTRAINT fk_ideation_campagnes_depositaires_depositaire  FOREIGN KEY (code_depositaire_type) REFERENCES ideation_depositaire_types (code_depositaire_type);

CREATE TABLE IF NOT EXISTS ideation_idees (
  id_idee int NOT NULL,
  lutece_user_name varchar(255) NOT NULL,
  titre VARCHAR(255) NOT NULL,
  description VARCHAR(4000) NOT NULL,
  cout bigint DEFAULT NULL,
  code_campagne varchar(50) NOT NULL,
  code_idee int NOT NULL,
  code_theme varchar(50) NOT NULL,
  localisation_type varchar(50) NOT NULL,
  localisation_ardt varchar(50) DEFAULT NULL,
  depositaire_type varchar(50) NOT NULL,
  depositaire VARCHAR(255),
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
  PRIMARY KEY (id_idee)
);
ALTER TABLE ideation_idees ADD CONSTRAINT uc_code_campagne_code_idee UNIQUE (code_campagne,code_idee);

CREATE TABLE IF NOT EXISTS ideation_idees_files (
  id_idee_file int NOT NULL,
  id_file int NOT NULL,
  id_idee int NOT NULL,
  type varchar(50) NOT NULL,
  PRIMARY KEY (id_idee_file)
);
ALTER TABLE ideation_idees_files ADD CONSTRAINT fk_ideation_idees_files_idee  FOREIGN KEY (id_idee) REFERENCES ideation_idees (id_idee);
CREATE INDEX ideation_idees_files_index_id_idee ON ideation_idees_files ( id_idee,type,id_file );
CREATE INDEX ideation_idees_files_index_id_file ON ideation_idees_files ( id_file );

CREATE TABLE IF NOT EXISTS ideation_idees_links (
  id_idee_link int NOT NULL,
  id_idee_parent int NOT NULL,
  id_idee_child int NOT NULL,
  PRIMARY KEY (id_idee_link)
);
ALTER TABLE ideation_idees_links ADD CONSTRAINT uc_id_idee_child_id_idee_parent UNIQUE (id_idee_child,id_idee_parent);
CREATE INDEX ideation_idees_links_index_id_idee_parent ON ideation_idees_links ( id_idee_parent );

CREATE TABLE IF NOT EXISTS task_change_idee_status_cf (
  id_task int NOT NULL,
  idee_status varchar(255) DEFAULT NULL,
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
  isDepositaire smallint NOT NULL,
  PRIMARY KEY (id_task)
);