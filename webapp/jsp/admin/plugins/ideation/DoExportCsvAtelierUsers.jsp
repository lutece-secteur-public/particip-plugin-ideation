<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="doExportCsvAtelierUsers" scope="session" class="fr.paris.lutece.plugins.ideation.web.AtelierJspBean" />
<% 
	doExportCsvAtelierUsers.doExportCsvUsers( request, response );
%>
