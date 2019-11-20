<jsp:useBean id="manageideationCampagneDepositaire" scope="session" class="fr.paris.lutece.plugins.ideation.web.CampagneDepositaireJspBean" />
<% String strContent = manageideationCampagneDepositaire.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
