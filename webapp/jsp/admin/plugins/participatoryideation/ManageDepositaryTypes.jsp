<jsp:useBean id="manageDepositaryType" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.DepositaryTypeJspBean" />
<% String strContent = manageDepositaryType.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
