<jsp:useBean id="manageideelinks" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.IdeeLinksJspBean" />
<% String strContent = manageideelinks.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
