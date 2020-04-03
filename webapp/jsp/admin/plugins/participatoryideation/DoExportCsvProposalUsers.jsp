<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="manageideationProposal" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.ProposalJspBean" />
<% 
	manageideationProposal.doExportCsvUsers( request, response );
%>
