<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="manageideationIdee" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.IdeeJspBean" />
<% 
	manageideationIdee.doExportCsvUsers( request, response );
%>
