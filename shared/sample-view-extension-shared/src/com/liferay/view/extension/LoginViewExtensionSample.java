/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.view.extension;

import com.liferay.taglib.util.PortletViewExtension;
import org.apache.jasper.servlet.JspServlet;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import javax.servlet.Servlet;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author Carlos Sierra Andrés
 */
@Component(property = {"extension-id=login-form-after-password"})
public class LoginViewExtensionSample implements PortletViewExtension {

	private ServiceRegistration<Servlet> _servletServiceRegistration;

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException {

		PortletURL actionURL = renderResponse.createActionURL();
		try {
			actionURL.setWindowState(WindowState.MAXIMIZED);
		} catch (WindowStateException e) {
			e.printStackTrace();
		}
		actionURL.setParameter("extension", "true");

		renderResponse.getWriter().write("<a href=\""+actionURL.toString()+"\">extension link</a>");

		renderRequest.
			getPortletSession().
			getPortletContext().
			getRequestDispatcher("/o/sample-view-extension/extension.jsp").
	}

	@Activate
	protected void createJspServlet() {

		Dictionary<String, Object> properties = new Hashtable<String, Object>();

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

		Bundle bundle = FrameworkUtil.getBundle(LoginViewExtensionSample.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_servletServiceRegistration = bundleContext.registerService(
			Servlet.class, new JspServlet(), properties);
	}

}
