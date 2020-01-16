INSERT INTO ideation_depositaire_complement_types (id_depositaire_complement_type, code_depositaire_complement_type, libelle) VALUES
	(1, 'NONE', 'No complement'),
	(2, 'LIST', 'List of values'),
	(3, 'FREE', 'Free value');

INSERT INTO ideation_depositaire_types (id_depositaire_type, code_depositaire_type, libelle, code_complement_type) VALUES
	(1, 'INDIVIDUAL' , 'Individual'      , 'NONE'),
	(2, 'OTHER'      , 'Other'           , 'FREE'),
	(3, 'COUNCIL'    , 'District council', 'LIST'),
	(4, 'ASSOCIATION', 'Association'     , 'FREE');

INSERT INTO ideation_campagnes_depositaires (id_campagne_depositaire, code_campagne, code_depositaire_type) VALUES
	(1, 'A', 'INDIVIDUAL'),
	(2, 'A', 'COUNCIL'),
	(3, 'A', 'ASSOCIATION'),
	(4, 'A', 'OTHER');

INSERT INTO ideation_depositaire_types_values (id_depositaire_type_value, code_depositaire_type, code, libelle) VALUES
	(1, 'COUNCIL', '01VENDO', '1er - Vendôme'),
	(2, 'COUNCIL', '01PALAI', '1er - Palais Royal'),
	(3, 'COUNCIL', '02VIVIE', '2e - Vivienne – Gaillon');

INSERT INTO task_change_idee_status_cf (id_task, idee_status) VALUES
	(1, 'SUBMITTED'),
	(2, 'UNDER_STUDY'),
	(3, 'UNDER_COBUILDING'),
	(4, 'REJECTED'),
	(5, 'GROUPED'),
	(6, 'ACCEPTED'),
	(7, 'MODERATE'),
	(8, 'DELETED_BY_SUBMITTER');

INSERT INTO task_notify_ideation_cf (id_task, sender_name, sender_email, subject, message, recipients_cc, recipients_bcc, isFollowers, isDepositaire) VALUES
	(2, 'OpenPB', 'no-reply@openbp.org', 'Open PB - About your proposal', 'Thanks for submitting your proposal !', '', '', 0, 0);
	
INSERT INTO `workflow_workflow` (`id_workflow`, `name`, `description`, `creation_date`, `is_enabled`, `workgroup_key`) VALUES
	(100, 'Proposal workflow', 'Proposal workflow', '2010-01-01 12:00:00', 1, 'all');

INSERT INTO `workflow_state` (`id_state`, `name`, `description`, `id_workflow`, `is_initial_state`, `is_required_workgroup_assigned`, `id_icon`, `display_order`) VALUES
	(100, 'Draft', 'Draft', 100, 1, 0, NULL, 1),
	(101, 'Submitted', 'Submitted', 100, 0, 0, NULL, 2);
	
INSERT INTO workflow_action (id_action, name, description, id_workflow, id_state_before, id_state_after, id_icon, is_automatic, is_mass_action, display_order, is_automatic_reflexive_action) VALUES
	(1, 'Submit', 'Used when a draft proposal is definitely submitted. ', 100, 100, 101, 1, 0, 0, 1, 0);