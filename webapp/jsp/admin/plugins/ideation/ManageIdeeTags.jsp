<%@ page errorPage="../../ErrorPage.jsp" %>

<jsp:include page="../../AdminHeader.jsp" />

<jsp:useBean id="manageideetags" scope="session" class="fr.paris.lutece.plugins.ideation.web.ManageIdeeTagsJspBean" />

<% manageideetags.init( request, manageideetags.RIGHT_MANAGEIDEETAGS ); %>
<%= manageideetags.getManageIdeeTagsHome ( request ) %>

<%@ include file="../../AdminFooter.jsp" %>
