<jsp:useBean id="manageIdeationCampagne" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.IdeationCampagneJspBean" />
<% String strContent = manageIdeationCampagne.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
