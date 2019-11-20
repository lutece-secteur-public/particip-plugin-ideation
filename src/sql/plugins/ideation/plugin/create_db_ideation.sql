
--
-- Structure for table ideation_phase_types
--
DROP TABLE IF EXISTS ideation_idees_files;
DROP TABLE IF EXISTS ideation_idees;

DROP TABLE IF EXISTS ideation_campagnes_depositaires;
DROP TABLE IF EXISTS ideation_depositaire_types_values;
DROP TABLE IF EXISTS ideation_depositaire_types;
DROP TABLE IF EXISTS ideation_depositaire_complement_types;




--
-- Structure for table ideation_depositaire_complement_types
--

CREATE TABLE ideation_depositaire_complement_types (
id_depositaire_complement_type int(6) NOT NULL,
code_depositaire_complement_type varchar(50) NOT NULL,
libelle varchar(255) NOT NULL,
PRIMARY KEY (id_depositaire_complement_type),
UNIQUE INDEX (code_depositaire_complement_type)
);

--
-- Structure for table ideation_depositaire_types
--


CREATE TABLE ideation_depositaire_types (
id_depositaire_type int(6) NOT NULL,
code_depositaire_type varchar(50) NOT NULL,
libelle varchar(255) NOT NULL,
code_complement_type varchar(50) NOT NULL,
PRIMARY KEY (id_depositaire_type),
UNIQUE INDEX (code_depositaire_type),
CONSTRAINT `fk_ideation_depositaire_types_complement` FOREIGN KEY(`code_complement_type`) references ideation_depositaire_complement_types(`code_depositaire_complement_type`)
);

--
-- Structure for table ideation_depositaire_types_values
--

CREATE TABLE ideation_depositaire_types_values (
id_depositaire_type_value int(6) NOT NULL,
code_depositaire_type varchar(50) NOT NULL,
code varchar(50) NOT NULL,
libelle varchar(255) NOT NULL,
PRIMARY KEY (id_depositaire_type_value),
CONSTRAINT `fk_ideation_depositaire_type_values_depositaire` FOREIGN KEY(`code_depositaire_type`) references ideation_depositaire_types(`code_depositaire_type`),
UNIQUE INDEX (code_depositaire_type, code)
);


--
-- Structure for table ideation_campagnes_depositaires
--


CREATE TABLE ideation_campagnes_depositaires (
id_campagne_depositaire int(6) NOT NULL,
code_campagne varchar(50) NOT NULL,
code_depositaire_type varchar(50) NOT NULL,
PRIMARY KEY (id_campagne_depositaire),
CONSTRAINT `fk_ideation_campagnes_depositaires_campagne` FOREIGN KEY(`code_campagne`) references ideation_campagnes(`code_campagne`),
CONSTRAINT `fk_ideation_campagnes_depositaires_depositaire` FOREIGN KEY(`code_depositaire_type`) references ideation_depositaire_types(`code_depositaire_type`)
);

DROP TABLE IF EXISTS ideation_bo_tags;
CREATE TABLE ideation_bo_tags (
  `id_bo_tag` int(6) NOT NULL,
  `value` varchar(50) NOT NULL,
   PRIMARY KEY (id_bo_tag)
);

DROP TABLE IF EXISTS ideation_fo_tags;
CREATE TABLE ideation_fo_tags (
  `id_fo_tag` int(6) NOT NULL,
  `value` varchar(50) NOT NULL,
   PRIMARY KEY (id_fo_tag)
);

--
-- Structure for table ideation_idees
--

CREATE TABLE ideation_idees (
	id_idee int(6) NOT NULL,
	lutece_user_name varchar(255) NOT NULL,
	titre long varchar NOT NULL,
	dejadepose long varchar NULL,
	description long varchar NOT NULL,
	cout bigint(20) NULL,
	code_campagne varchar(50) NOT NULL,
	code_idee int(6) NOT NULL,
	code_theme varchar(50) NOT NULL,
	localisation_type varchar(50) NOT NULL,
	localisation_ardt varchar(50) NULL ,
	depositaire_type varchar(50) NOT NULL,
	depositaire long varchar NULL,
	accept_exploit smallint NOT NULL default false,
	accept_contact smallint NOT NULL default true,
	address long varchar NULL,
	longitude float NULL,
	latitude float NULL,
	type_nqpv_qva varchar(50) NOT NULL,
	id_nqpv_qva varchar(50) NULL,
	libelle_nqpv_qva long varchar NULL,
	creation_timestamp datetime NOT NULL,
	backoffice_id_bo_tag int(6) NULL,
	eudonet_exported_tag int(6) default 0,
	status_public varchar(50) NOT NULL,
	status_eudonet varchar(50) NULL,
	motif_recev long varchar NULL,
	id_project varchar(50) NULL,
	titre_projet long varchar NULL,
	url_projet long varchar NULL,
	creationmethod long varchar NULL,
	operatingbudget long varchar NULL
		
	PRIMARY KEY (id_idee),
	UNIQUE INDEX (code_campagne, code_idee),
	CONSTRAINT `fk_ideation_idees_campagne` FOREIGN KEY(`code_campagne`) references ideation_campagnes(`code_campagne`),
	CONSTRAINT `fk_ideation_idees_bo_tag` FOREIGN KEY(`backoffice_id_bo_tag`) references ideation_bo_tags(`id_bo_tag`)
);


CREATE TABLE `ideation_idees_files` (
  `id_idee_file` int(6) NOT NULL,
  `id_file` int(11) NOT NULL,
  `id_idee` int(11) NOT NULL,
   type varchar(50) NOT NULL,
   PRIMARY KEY (id_idee_file),
   INDEX (id_idee, type, id_file),
   INDEX (id_file),
   CONSTRAINT `ideation_idees_files_file` FOREIGN KEY(`id_file`) references core_file(`id_file`),
   CONSTRAINT `ideation_idees_files_idee` FOREIGN KEY(`id_idee`) references ideation_idees(`id_idee`)
);

DROP TABLE IF EXISTS ideation_idees_fo_tags;
CREATE TABLE ideation_idees_fo_tags (
  `id_idee_fo_tag` int(6) NOT NULL,
  `id_idee` int(6) NOT NULL,
  `id_fo_tag` int(6) NOT NULL,
   PRIMARY KEY (id_idee_fo_tag),
   UNIQUE INDEX (id_idee, id_fo_tag),
   INDEX (id_fo_tag),
   CONSTRAINT `ideation_idees_tags_fo_tag` FOREIGN KEY(`id_fo_tag`) references ideation_fo_tags(`id_fo_tag`),
   CONSTRAINT `ideation_idees_tags_idee` FOREIGN KEY(`id_idee`) references ideation_idees(`id_idee`)
);

DROP TABLE IF EXISTS ideation_profanity_filter;
CREATE TABLE ideation_profanity_filter (
	id_user varchar(255) NULL,
	word varchar(50) NOT NULL,
	ressource_type varchar(50) NOT NULL,
	counter int(24) default 0,

	PRIMARY KEY (id_user, word, ressource_type)
);

DROP TABLE IF EXISTS ideation_idees_links;
CREATE TABLE ideation_idees_links (
  `id_idee_link` int(6) NOT NULL,
  `id_idee_parent` int(6) NOT NULL,
  `id_idee_child` int(6) NOT NULL,
   PRIMARY KEY (id_idee_link),
   UNIQUE INDEX (id_idee_child, id_idee_parent),
   INDEX (id_idee_parent),
   CONSTRAINT `ideation_idees_parent_idee` FOREIGN KEY(`id_idee_parent`) references ideation_idees(`id_idee`),
   CONSTRAINT `ideation_idees_child_idee` FOREIGN KEY(`id_idee_child`) references ideation_idees(`id_idee`)
);


--
-- Structure for table ideation_atelier
--

DROP TABLE IF EXISTS ideation_atelier;
CREATE TABLE ideation_atelier (
id_atelier int(6) NOT NULL,
titre long varchar NULL ,
description long varchar NULL ,
campagne long varchar NOT NULL ,
thematique long varchar NOT NULL ,
type varchar(50) NOT NULL default '',
datedebutphase1 date NOT NULL,
datefinphase1 date NOT NULL,
datedebutphase2 date NOT NULL,
datefinphase2 date NOT NULL,
datedebutphase3 date NOT NULL,
datefinphase3 date NOT NULL,
localisationtype varchar(50) NULL,
localisationardt varchar(50) NULL,
address long varchar NULL,
longitude float NULL,
latitude float NULL,
geojson long varchar NULL,
textephase1 long varchar NULL ,
titrephase3 long varchar NULL ,
textephase3 long varchar NULL ,
listcodeidee long varchar NULL ,
lienformulairephase2 varchar(255) NOT NULL default '',
status_public varchar(50),
joursDeRappelPhase1 int(6) NULL default 0,
joursDeRappelPhase2 int(6) NULL default 0,
cout bigint(20) NULL default 0,
PRIMARY KEY (id_atelier)
);

--
-- Structure for table atelier_idees
--

DROP TABLE IF EXISTS atelier_idees;
CREATE TABLE atelier_idees (
id_atelier int(6) NOT NULL,
id_idee int(6) NOT NULL,
PRIMARY KEY (id_atelier,id_idee)
);



--
-- Structure for table ideation_atelier_form
--

DROP TABLE IF EXISTS ideation_atelier_form;
CREATE TABLE ideation_atelier_form (
id_atelierform int(6) NOT NULL,
id_atelier int(6) NOT NULL,
id_choix_titre int(6) default 0 ,
id_choix_description int(6) default 0 ,

PRIMARY KEY (id_atelierform)
);

--
-- Structure for table ideation_atelier_form_entry
--

DROP TABLE IF EXISTS ideation_atelier_form_entry;
CREATE TABLE ideation_atelier_form_entry (
id_atelierformentry int(6) NOT NULL,
id_atelierform int(6) NOT NULL,
alternative1 long varchar NULL ,
alternative2 long varchar NULL ,
alternative3 long varchar NULL ,
PRIMARY KEY (id_atelierformentry),
FOREIGN KEY (id_atelierform) REFERENCES ideation_atelier_form(id_atelierform)
);

--
-- Structure for table ideation_atelier_form_result
--

DROP TABLE IF EXISTS ideation_atelier_form_result_entry;
DROP TABLE IF EXISTS ideation_atelier_form_result;
CREATE TABLE ideation_atelier_form_result (
id_atelier_form_result int(6) NOT NULL,
id_atelier_form int(6) NOT NULL,
numero_choix_titre int(11) NOT NULL default '0',
numero_choix_description int(11) NOT NULL default '0',
guid varchar(255) NOT NULL default '',
creation_timestamp datetime NOT NULL,
PRIMARY KEY (id_atelier_form_result),
FOREIGN KEY (id_atelier_form) REFERENCES ideation_atelier_form(id_atelierform)
);

--
-- Structure for table ideation_atelier_form_result_entry
--

CREATE TABLE ideation_atelier_form_result_entry (
id_atelier_form_result int(6) NOT NULL,
id_atelier_form_entry int(11) NOT NULL default '0',
numero_choix int(11) NOT NULL default '0',
PRIMARY KEY (id_atelier_form_result, id_atelier_form_entry),
FOREIGN KEY (id_atelier_form_result) REFERENCES ideation_atelier_form_result(id_atelier_form_result),
FOREIGN KEY (id_atelier_form_entry) REFERENCES ideation_atelier_form_entry(id_atelierformentry)
);


