<%@page import="com.liferay.portal.security.permission.PermissionChecker"%>
<%@page import="com.liferay.portal.theme.ThemeDisplay"%>
<%@page import="com.liferay.portal.kernel.util.ParamUtil"%>
<%@page import="com.liferay.portal.kernel.util.StringPool"%>

<%@ include file="/html/init.jsp" %>

<%
String toolbarItem = ParamUtil.getString(request, "toolbarItem", "view-all");

PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();
%>

<div class="lfr-portlet-toolbar">
	<portlet:renderURL var="viewSitesURL">
		<portlet:param name="jspPage" value="/html/admin/view.jsp" />
	</portlet:renderURL>

	<span class="lfr-toolbar-button view-button <%= toolbarItem.equals("view-all") ? "current" : "" %>">
		<a href="<%= viewSitesURL %>"><liferay-ui:message key="view-all" /></a>
	</span>
<c:if test="<%= true %>">
	<portlet:renderURL var="addApplicationURL">
		<portlet:param name="jspPage" value="/html/admin/edit.jsp"/>
		<portlet:param name="referer" value="<%= currentURL %>"/>
	</portlet:renderURL>
	
	<span class="lfr-toolbar-button add-button <%= toolbarItem.equals("add") ? "current" : "" %>">
		<a href="<%= addApplicationURL %>"><liferay-ui:message key="add" /></a>
	</span>
</c:if>
</div>