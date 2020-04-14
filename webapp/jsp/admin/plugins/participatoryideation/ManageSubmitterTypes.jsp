<jsp:useBean id="manageSubmitterType" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.SubmitterTypeJspBean" />
<% String strContent = manageSubmitterType.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
