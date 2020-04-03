<jsp:useBean id="manageideationDepositary" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.DepositaryJspBean" />
<% String strContent = manageideationDepositary.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
