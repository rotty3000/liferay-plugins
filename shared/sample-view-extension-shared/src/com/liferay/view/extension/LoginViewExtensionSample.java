/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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

package com.liferay.view.extension;

import com.liferay.kernel.servlet.taglib.ViewExtension;
import com.liferay.portal.kernel.util.JavaConstants;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.servlet.JspServlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.context.ServletContextHelper;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true, property = {"extension-id=login-form-after-password"},
	service = ViewExtension.class)
public class LoginViewExtensionSample implements ViewExtension{

	@Activate
	protected void createJspServlet() {
		Bundle bundle = FrameworkUtil.getBundle(LoginViewExtensionSample.class);

		BundleContext bundleContext = bundle.getBundleContext();

		Dictionary<String, Object> properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_NAME,
			"sample-view-extension");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_PATH,
			"/sample-view-extension");

		ServletContextHelper servletContextHelper = new ServletContextHelper(
			bundle) {};

		_contextServiceRegistration = bundleContext.registerService(
			ServletContextHelper.class, servletContextHelper, properties);

		properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"sample-view-extension");

		properties = new Hashtable<String, Object>();

		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_CONTEXT_SELECT,
			"sample-view-extension");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_NAME, "jsp");
		properties.put(
			HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "*.jsp");
		properties.put(
			"servlet.init.compilerClassName",
			"com.liferay.portal.servlet.jsp.compiler.compiler.JspCompiler");
		properties.put("servlet.init.httpMethods", "GET,POST,HEAD");
		properties.put("servlet.init.keepgenerated", "true");
		properties.put("servlet.init.logVerbosityLevel", "DEBUG");

		_servletServiceRegistration = bundleContext.registerService(
			Servlet.class, new JspServlet(), properties);
	}

	@Deactivate
	protected void deactivate() {
		_servletServiceRegistration.unregister();
		_contextServiceRegistration.unregister();
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		RenderResponse renderResponse = (RenderResponse)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);

		PortletURL actionURL = renderResponse.createActionURL();
		try {
			actionURL.setWindowState(WindowState.MAXIMIZED);
		}
		catch (WindowStateException e) {
			e.printStackTrace();
		}

		actionURL.setParameter("extension", "true");

		PrintWriter writer = response.getWriter();

		writer.write(
			"<a href=\"" + actionURL.toString() + "\">extension link</a>");
	}

	private ServiceRegistration<Servlet> _servletServiceRegistration;
	private ServiceRegistration<ServletContextHelper>
		_contextServiceRegistration;

}