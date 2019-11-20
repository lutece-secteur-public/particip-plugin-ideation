<jsp:useBean id="manageideetagsBoTag" scope="session" class="fr.paris.lutece.plugins.ideation.web.BoTagJspBean" />
<% String strContent = manageideetagsBoTag.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
