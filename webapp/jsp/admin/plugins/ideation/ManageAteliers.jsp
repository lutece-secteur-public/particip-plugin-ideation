<jsp:useBean id="manageatelierAtelier" scope="session" class="fr.paris.lutece.plugins.ideation.web.AtelierJspBean" />
<% String strContent = manageatelierAtelier.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
