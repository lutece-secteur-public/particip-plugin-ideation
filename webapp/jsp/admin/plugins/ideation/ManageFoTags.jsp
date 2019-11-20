<jsp:useBean id="manageideetagsFoTag" scope="session" class="fr.paris.lutece.plugins.ideation.web.FoTagJspBean" />
<% String strContent = manageideetagsFoTag.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
