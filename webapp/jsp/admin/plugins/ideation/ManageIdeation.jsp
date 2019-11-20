<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageideation" scope="session" class="fr.paris.lutece.plugins.ideation.web.ManageIdeationJspBean" />

<% manageideation.init( request, manageideation.RIGHT_MANAGEIDEATION ); %>
<%= manageideation.getManageIdeationHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
