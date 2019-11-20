<jsp:useBean id="manageideationDepositaire" scope="session" class="fr.paris.lutece.plugins.ideation.web.DepositaireTypeJspBean" />
<% String strContent = manageideationDepositaire.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
