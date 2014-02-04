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

package com.liferay.osgi.service.http.internal;

import com.liferay.osgi.service.http.internal.servlet.ServletConfigImpl;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.util.JS;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.ServletContextHelper;
import org.osgi.service.http.runtime.HttpServiceRuntime;
import org.osgi.service.http.runtime.ServletContextDTO;

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
		HttpService.class, HttpServlet.class
	}
)
@SuppressWarnings("deprecation")
public class LiferayHttpService extends HttpServlet
	implements HttpService, HttpServiceRuntime {

	public LiferayHttpService() throws InvalidSyntaxException {
		_contextMap =
			new ConcurrentHashMap<ServletContextHelper, HttpServletContext>();
		_contextNameMap = new ConcurrentHashMap<String, HttpServletContext>();
		_contextPathMap = new ConcurrentHashMap<String, HttpServletContext>();
		_defaultContextFilter = FrameworkUtil.createFilter(
			"(&(osgi.http.whiteboard.context.name=default)" +
				"(provider=Liferay Inc.))");
		_mappings = new ConcurrentHashMap<String, Object>();
		_servletContext = new AtomicReference<ServletContext>();
		_serviceRegistrations =
			new ConcurrentHashMap<String, ServiceRegistration<?>>();
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO
		return null;
	}

	@Override
	@Deprecated
	public HttpContext createDefaultHttpContext() {
		return (HttpContext)_defaultHttpContext;
	}

	@Override
	public ServletContextDTO[] getServletContextDTOs() {
		// TODO
//		Set<HttpContext> values = _contextMap.keySet();
//
//		HttpContext[] httpContexts = values.toArray(
//			new HttpContext[values.size()]);
//
//		HttpContextDTO[] httpContextDTOs = new HttpContextDTO[values.size()];
//
//		for (int i = 0; i < values.size(); i++) {
//			HttpContext httpContext = httpContexts[i];
//
//			httpContextDTOs[i] = new HttpContextDTO();
//
//			// TODO
//		}

		return null;
	}

	@Deprecated
	@Override
	public void registerResources(
		String alias, String name, HttpContext httpContext) {

		if (name == null) {
			name = "";
		}

		BundleContext bundleContext = _componentContext.getBundleContext();

		String contextName = identifyContextNameLegacy(httpContext);

		registerHttpContextLegacy(bundleContext, httpContext, contextName);

		Hashtable<String,String> properties = new Hashtable<String, String>();

		properties.put(HttpConstants.HTTP_WHITEBOARD_RESOURCE_PREFIX, name);
		properties.put(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, alias);
		properties.put(HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);

		ServiceRegistration<Servlet> serviceRegistration =
			bundleContext.registerService(
				Servlet.class, new HttpServlet() {}, properties);

		_serviceRegistrations.put(alias, serviceRegistration);
	}

	@Deprecated
	@Override
	@SuppressWarnings("rawtypes")
	public void registerServlet(
		String alias, Servlet servlet, Dictionary initparams,
		HttpContext httpContext) {

		BundleContext bundleContext = _componentContext.getBundleContext();

		String contextName = identifyContextNameLegacy(httpContext);

		registerHttpContextLegacy(bundleContext, httpContext, contextName);

		Hashtable<String, Object> properties = new Hashtable<String, Object>();

		properties.put(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED,
			initparams.get(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED));
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_ERROR_PAGE,
			initparams.get(
				HttpConstants.HTTP_WHITEBOARD_SERVLET_ERROR_PAGE));
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_NAME,
			servlet.getClass().getName());
		properties.put(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, alias);

		for (Enumeration em = initparams.keys();em.hasMoreElements();) {
			String key = String.valueOf(em.nextElement());

			properties.put(key, initparams.get(key));
		}

		ServiceRegistration<Servlet> serviceRegistration =
			bundleContext.registerService(
				Servlet.class, servlet, properties);

		_serviceRegistrations.put(alias, serviceRegistration);
	}

	@Override
	@Deprecated
	public void unregister(String alias) {
		ServiceRegistration<?> serviceRegistration =
			_serviceRegistrations.remove(alias);

		if (serviceRegistration == null) {
			return;
		}

		serviceRegistration.unregister();
	}

	@Activate
	protected void activate(ComponentContext componentContext)
		throws InvalidSyntaxException {

		_componentContext = componentContext;

		System.out.println(this + " activated!");
	}

	@Deactivate
	protected void deactivate() {
		_mappings.clear();

		_componentContext = null;

		_contextMap.clear();
		_contextNameMap.clear();
		_contextPathMap.clear();
		_mappings.clear();
		_serviceRegistrations.clear();

		System.out.println(this + " deactivated!");
	}

	protected void dump(
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

	protected HttpServletContext getServletContext(Map properties) {
		String servletContextName = MapUtil.getString(
			properties, HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME);

		HttpServletContext servletContext = null;

		if (Validator.isNotNull(servletContextName)) {
			servletContext = _contextNameMap.get(servletContextName);
		}

		if (servletContext == null) {
			servletContext = _contextMap.get(_defaultHttpContext);
		}

		return servletContext;
	}

	protected HttpServletContext getServletContext(String contextPath) {
		return _contextPathMap.get(contextPath);
	}

	protected String[] getServletPatterns(Map properties) {
		String[] patterns = null;

		Object value = properties.get(
			HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN);

		if (value instanceof String) {
			patterns = new String[] {(String)value};
		}
		else if (value.getClass().isArray() &&
				 (value.getClass().getComponentType() == String.class)) {

			patterns = (String[])value;
		}

		return patterns;
	}

	protected String identifyContextNameLegacy(HttpContext httpContext) {
		Bundle bundle = FrameworkUtil.getBundle(httpContext.getClass());

		return JS.getSafeName(bundle.getSymbolicName());
	}

	protected void registerHttpContextLegacy(
		BundleContext bundleContext, HttpContext httpContext,
		String contextName) {

		if (_contextMap.containsKey(httpContext)) {
			return;
		}

		Hashtable<String,String> properties = new Hashtable<String, String>();

		properties.put(HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, contextName);
		properties.put(
			HttpConstants.HTTP_WHITEBOARD_CONTEXT_SHARED,
			Boolean.FALSE.toString());

		bundleContext.registerService(
			HttpContext.class, httpContext, properties);
	}

	@Override
	protected void service(
			HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {

		String requestURI = request.getRequestURI();

		ServletContext servletContext = matchFromRequestURI(requestURI);

		if (servletContext != null) {
			RequestDispatcher requestDispatcher =
				servletContext.getRequestDispatcher(requestURI);

			if (requestDispatcher != null) {
				if (false) {
					requestDispatcher.include(request, response);
				}
				else {
					requestDispatcher.forward(request, response);
				}

				return;
			}
		}

		response.sendError(
			HttpServletResponse.SC_NOT_FOUND,
			"No endpoint found for URI " + requestURI);

		dump(request, response);
	}

	private ServletContext matchFromRequestURI(String requestURI) {
		int pos = requestURI.lastIndexOf('/');
		ServletContext servletContext = null;

		do {
			servletContext = getServletContext(requestURI);

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

	@Reference(
		cardinality = ReferenceCardinality.AT_LEAST_ONE,
		target = "(osgi.http.whiteboard.context.name=*)"
	)
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected void setServletContextHelper(
		ServletContextHelper servletContextHelper, Map properties) {

		if (_contextMap.containsKey(servletContextHelper)) {
			return;
		}

		HttpServletContext servletContext = new HttpServletContext(
			_servletContext.get(), servletContextHelper, properties, this);

		if (_contextNameMap.containsKey(
				servletContext.getServletContextName()) ||
			_contextPathMap.containsKey(servletContext.getContextPath())) {

			return;
		}

		if (_defaultContextFilter.matches(properties)) {
			_defaultHttpContext = servletContextHelper;
		}

		_contextMap.put(servletContextHelper, servletContext);
		_contextNameMap.put(
			servletContext.getServletContextName(), servletContext);
		_contextPathMap.put(servletContext.getContextPath(), servletContext);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		target="(osgi.http.whiteboard.servlet.pattern=*)"
	)
	@SuppressWarnings({"rawtypes", "unchecked"})
	protected void setServlet(Servlet servlet, Map properties) {
		if (!targetMatches(properties)) {
			return;
		}

		HttpServletContext servletContext = getServletContext(properties);
		String servletName = MapUtil.getString(
			properties, HttpConstants.HTTP_WHITEBOARD_SERVLET_NAME,
			servlet.getClass().getName());
		String[] patterns = getServletPatterns(properties);
		boolean asyncSupported = MapUtil.getBoolean(
			properties, HttpConstants.HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED);

		Dynamic addedServlet = servletContext.addServlet(servletName, servlet);

		addedServlet.addMapping(patterns);
		addedServlet.setAsyncSupported(asyncSupported);
		addedServlet.setInitParameters(properties);

		ServletConfig servletConfig = new ServletConfigImpl(
			servletContext, addedServlet.getName(),
			addedServlet.getInitParameters());

		try {
			servlet.init(servletConfig);
		}
		catch (ServletException se) {
			servletContext.removeServlet(servlet);

			throw new RuntimeException(se);
		}
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		target="(&(bean.id=javax.servlet.ServletContext)(original.bean=*))"
	)
	protected void setSerlvetContext(ServletContext servletContext) {
		_servletContext.set(servletContext);
	}

	protected boolean targetMatches(Map<String, ?> properties) {
		String targetFilterString = MapUtil.getString(
			properties, HttpConstants.HTTP_WHITEBOARD_TARGET);

		if (Validator.isName(targetFilterString)) {
			return false;
		}

		try {
			Filter filter = FrameworkUtil.createFilter(targetFilterString);

			return filter.matches(getAttributes());
		}
		catch (InvalidSyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	protected void unsetServletContextHelper(
		ServletContextHelper servletContextHelper) {

		HttpServletContext servletContext = _contextMap.remove(
			servletContextHelper);

		if (servletContext == null) {
			return;
		}

		_contextNameMap.remove(servletContext.getServletContextName());
		_contextPathMap.remove(servletContext.getContextPath());
	}

	protected void unsetServlet(Servlet servlet, Map properties) {
		HttpServletContext servletContext = getServletContext(properties);

		servletContext.removeServlet(servlet);
	}

	private ComponentContext _componentContext;
	private Map<ServletContextHelper, HttpServletContext> _contextMap;
	private Map<String, HttpServletContext> _contextNameMap;
	private Map<String, HttpServletContext> _contextPathMap;
	private ServletContextHelper _defaultHttpContext;
	private Filter _defaultContextFilter;
	private Map<String, Object> _mappings;
	private Map<String, ServiceRegistration<?>> _serviceRegistrations;
	private AtomicReference<ServletContext> _servletContext;

}