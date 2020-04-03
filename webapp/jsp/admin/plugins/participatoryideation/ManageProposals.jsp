<jsp:useBean id="manageideationProposal" scope="session" class="fr.paris.lutece.plugins.participatoryideation.web.ProposalJspBean" />
<% String strContent = manageideationProposal.processController ( request , response ); %>

<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:include page="../../AdminHeader.jsp" />

<%= strContent %>

<%@ include file="../../AdminFooter.jsp" %>
