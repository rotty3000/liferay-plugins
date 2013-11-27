<%@page import="com.liferay.portal.util.PortalUtil" %>
<%@page import="com.liferay.portal.kernel.cas.LiferayCasPrincipal" %>

<%@page import="javax.portlet.PortletSession"%>
<%@page import="java.net.URL"%>
<%@page import="java.io.InputStream" %>
<%@page import="java.io.InputStreamReader" %>
<%@page import="java.io.BufferedReader" %>
<%
/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
%>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<portlet:defineObjects />
<%

LiferayCasPrincipal principal = com.liferay.portal.kernel.cas.CasServiceUtil.getLiferayCasPrincipal(portletSession);

String token = principal.getProxyTicketFor("https://localhost:8443/redmine-2.4.0/issues.json");

URL url = new URL("https://localhost:8443/redmine-2.4.0/issues.json?ticket=" + token);

out.println(url);

/*BufferedReader remote = new BufferedReader(new InputStreamReader((InputStream)url.getContent()));

String line;

while ((line = remote.readLine()) != null) {
	out.println(line);
}*/
%>