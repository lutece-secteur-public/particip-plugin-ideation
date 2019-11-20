<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageatelier" scope="session" class="fr.paris.lutece.plugins.ideation.web.ManageAtelierJspBean" />

<% manageatelier.init( request, manageatelier.RIGHT_MANAGEATELIER ); %>
<%= manageatelier.getManageAtelierHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
