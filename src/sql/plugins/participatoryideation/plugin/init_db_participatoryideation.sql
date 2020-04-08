INSERT INTO participatoryideation_submitters_complement_types (id_submitter_complement_type, code_submitter_complement_type, libelle) VALUES
	(1, 'NONE', 'No complement'),
	(2, 'LIST', 'List of values'),
	(3, 'FREE', 'Free value');

INSERT INTO participatoryideation_submitters_types (id_submitter_type, code_submitter_type, libelle, code_complement_type) VALUES
	(1, 'INDIVIDUAL' , 'Individual'      , 'NONE'),
	(2, 'OTHER'      , 'Other'           , 'FREE'),
	(3, 'COUNCIL'    , 'Location council', 'LIST'),
	(4, 'ASSOCIATION', 'Association'     , 'FREE');

INSERT INTO participatoryideation_submitters (id_submitter, code_campaign, code_submitter_type) VALUES
	(1, 'A', 'INDIVIDUAL'),
	(2, 'A', 'COUNCIL'),
	(3, 'A', 'ASSOCIATION'),
	(4, 'A', 'OTHER');

INSERT INTO participatoryideation_submitters_types_values (id_submitter_type_value, code_submitter_type, code, libelle) VALUES
	(1, 'COUNCIL', '01VENDO', '1er - Vendome'),
	(2, 'COUNCIL', '01PALAI', '1er - Palais Royal'),
	(3, 'COUNCIL', '02VIVIE', '2e - Vivienne Gaillon');

INSERT INTO task_change_proposal_status_cf (id_task, proposal_status) VALUES
	(1, 'SUBMITTED'),
	(2, 'UNDER_STUDY'),
	(3, 'UNDER_COBUILDING'),
	(4, 'REJECTED'),
	(5, 'GROUPED'),
	(6, 'ACCEPTED'),
	(7, 'MODERATE'),
	(8, 'DELETED_BY_SUBMITTER');

INSERT INTO task_notify_ideation_cf (id_task, sender_name, sender_email, subject, message, recipients_cc, recipients_bcc, isFollowers, isSubmitter) VALUES
	(2, 'OpenPB', 'no-reply@openbp.org', 'Open PB - About your proposal', 'Thanks for submitting your proposal !', '', '', 0, 0);
	
