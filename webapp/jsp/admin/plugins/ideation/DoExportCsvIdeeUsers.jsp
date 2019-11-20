<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="manageideationIdee" scope="session" class="fr.paris.lutece.plugins.ideation.web.IdeeJspBean" />
<% 
	manageideationIdee.doExportCsvUsers( request, response );
%>
