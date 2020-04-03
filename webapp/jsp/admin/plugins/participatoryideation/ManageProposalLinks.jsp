<jsp:useBean id="manageproposallinks" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.ProposalLinksJspBean" />
<% String strContent = manageproposallinks.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
