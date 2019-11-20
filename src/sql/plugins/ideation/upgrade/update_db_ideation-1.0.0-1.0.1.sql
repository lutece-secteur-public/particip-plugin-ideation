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
status_public varchar(50) NOT NULL,
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

--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'IDEATION_MANAGEMENT_ATELIER';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES
('IDEATION_MANAGEMENT_ATELIER','ideation.adminFeature.ManageAtelier.name',1,'jsp/admin/plugins/ideation/ManageAteliers.jsp','ideation.adminFeature.ManageAtelier.description',0,'ideation',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'IDEATION_MANAGEMENT_ATELIER';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDEATION_MANAGEMENT_ATELIER',1);

INSERT INTO `core_datastore` VALUES ('ideation.site_property.form.forcer_activation_commentaires','0');

INSERT INTO core_datastore VALUES ('ideation.site_property.atelier.presentiel_cannot_access', 'L\ atelier est de type présentiel, les phases 1 et 2 ne sont donc pas accessibles.');

