update core_admin_right set admin_url='jsp/admin/plugins/ideation/ManageCampagneDepositaires.jsp' where id_right="IDEATION_MANAGEMENT";

--
-- ADD COLUMN URL PROJECT TO TABLE IDEATION_IDEE
--

ALTER TABLE ideation_idees ADD id_project varchar(50) NULL;
ALTER TABLE ideation_idees ADD titre_projet long varchar NULL;
ALTER TABLE ideation_idees ADD url_projet long varchar NULL;
