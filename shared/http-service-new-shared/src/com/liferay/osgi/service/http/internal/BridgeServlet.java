/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
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

package com.liferay.osgi.service.http.internal;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"bean.id=javax.servlet.http.HttpServlet",
		"original.bean=true",
		"provider=Liferay Inc."
	},
	service = {
		HttpServlet.class
	}
)
public class BridgeServlet extends HttpServlet {

	public BridgeServlet() {
		_debug = new AtomicReference<Boolean>(Boolean.FALSE);
		_liferayHttpService = new AtomicReference<LiferayHttpService>();
	}

	@Override
	protected void service(
			HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		if (_debug.get()) {
			dump(request, response);
		}

		String requestURI = request.getRequestURI();

		String parentContextPath = _liferayHttpService.get().getContextPath();

		if (requestURI.startsWith(parentContextPath)) {
			requestURI = requestURI.substring(parentContextPath.length());
		}

		ServletContext servletContext = matchFromRequestURI(requestURI);

		if (servletContext != null) {
			RequestDispatcher requestDispatcher =
				servletContext.getRequestDispatcher(requestURI);

			if (requestDispatcher != null) {
				requestDispatcher.forward(request, response);

				return;
			}
		}

		response.sendError(
			HttpServletResponse.SC_NOT_FOUND,
			"No endpoint found for URI " + requestURI);
	}

	protected void setDebug(boolean debug) {
		_debug.set(debug);
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		target="(provider=Liferay Inc.)"
	)
	protected void setLiferayHttpService(
		LiferayHttpService liferayHttpService) {

		_liferayHttpService.set(liferayHttpService);
	}

	@SuppressWarnings("unused")
	protected void unsetLiferayHttpService(
		LiferayHttpService liferayHttpService) {

		_liferayHttpService.set(null);
	}

	private void dump(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		response.setContentType("text/html;encoding=UTF-8");

		PrintWriter writer = response.getWriter();

		writer.println("<h2>HttpService</h2>");

		writer.println("<ul>");

		writer.print("<li>");
		writer.print("AuthType = " + request.getAuthType());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("CharacterEncoding = " + request.getCharacterEncoding());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("ContentLength = " + request.getContentLength());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("ContentType = " + request.getContentType());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("ContextPath = " + request.getContextPath());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("LocalAddr = " + request.getLocalAddr());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("LocalName = " + request.getLocalName());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("LocalPort = " + request.getLocalPort());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("Method = " + request.getMethod());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("PathInfo = " + request.getPathInfo());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("PathTranslated = " + request.getPathTranslated());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("Protocol = " + request.getProtocol());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("QueryString = " + request.getQueryString());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("RemoteAddr = " + request.getRemoteAddr());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("RemoteHost = " + request.getRemoteHost());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("RemotePort = " + request.getRemotePort());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("RemoteUser = " + request.getRemoteUser());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("RequestedSessionId = " + request.getRequestedSessionId());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("RequestURI = " + request.getRequestURI());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("Scheme = " + request.getScheme());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("ServerName = " + request.getServerName());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("ServerPort = " + request.getServerPort());
		writer.println("</li>");
		writer.print("<li>");
		writer.print("ServletPath = " + request.getServletPath());
		writer.println("</li>");

		writer.println("</ul>");

		writer.println("<h3>Paramters</h3>");

		Enumeration<String> enumeration = request.getParameterNames();

		writer.println("<ul>");

		while (enumeration.hasMoreElements()) {
			Object name = enumeration.nextElement();

			writer.print("<li>");
			writer.print(
				name + " = " + request.getParameter(String.valueOf(name)));
			writer.println("</li>");
		}

		writer.println("</ul>");

		writer.println("<h3>Attributes</h3>");

		enumeration = request.getAttributeNames();

		writer.println("<ul>");

		while (enumeration.hasMoreElements()) {
			Object name = enumeration.nextElement();

			writer.print("<li>");
			writer.print(
				name + " = " + request.getAttribute(String.valueOf(name)));
			writer.println("</li>");
		}

		writer.println("</ul>");

		writer.println("<h3>Headers</h3>");

		enumeration = request.getHeaderNames();

		writer.println("<ul>");

		while (enumeration.hasMoreElements()) {
			Object name = enumeration.nextElement();

			writer.print("<li>");
			writer.print(
				name + " = " + request.getHeader(String.valueOf(name)));
			writer.println("</li>");
		}

		writer.println("</ul>");
	}

	private ServletContext matchFromRequestURI(String requestURI) {
		int pos = requestURI.lastIndexOf('/');
		ServletContext servletContext = null;

		do {
			servletContext = _liferayHttpService.get().getServletContext(
				requestURI);

			if (servletContext != null) {
				break;
			}

			if (pos > -1) {
				requestURI = requestURI.substring(0, pos);
				pos = requestURI.lastIndexOf('/');
			}
			else {
				break;
			}
		}
		while (servletContext == null);

		return servletContext;
	}

	private AtomicReference<Boolean> _debug;
	private AtomicReference<LiferayHttpService> _liferayHttpService;

}