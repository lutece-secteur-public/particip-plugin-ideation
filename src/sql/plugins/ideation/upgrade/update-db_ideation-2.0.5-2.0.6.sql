--
-- Add DSKEY for new handicap question.
--

REPLACE INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('ideation.site_property.form.handicap_label.htmlblock'     , '<p><strong>Votre projet a-t-il vocation &agrave; aider les personnes en situation de handicap ? *</strong></p>');
REPLACE INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('ideation.site_property.form.handicap_complement_label'    , 'Pourriez-vous pr&eacute;ciser ?');
REPLACE INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('ideation.site_property.form.handicap_yes_label'           , 'Oui');
REPLACE INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('ideation.site_property.form.handicap_no_label'            , 'Non');
REPLACE INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('ideation.site_property.form.handicap_complement.minLength', '5');
REPLACE INTO `core_datastore` (`entity_key`, `entity_value`) VALUES ('ideation.site_property.form.handicap_complement.maxLength', '100');

