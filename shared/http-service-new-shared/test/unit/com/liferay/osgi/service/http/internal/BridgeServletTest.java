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

import com.liferay.osgi.service.http.util.TU;
import com.liferay.osgi.service.http.util.TU.HandyMap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.ServletContextHelper;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;

import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Raymond Augé
 */
@PrepareForTest({FrameworkUtil.class})
@RunWith(PowerMockRunner.class)
public class BridgeServletTest extends PowerMockito {

	@Before
	public void setUp() throws InvalidSyntaxException {
		buildMocks();

		_liferayHttpService = new LiferayHttpService();

		_liferayHttpService.setSerlvetContext(new MockServletContext());

		DefaultServletContextHelper defaultServletContextHelper =
			new DefaultServletContextHelper();

		defaultServletContextHelper.activate(_componentContext);

		HandyMap serviceMap = TU.serviceMap(
			HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, "").add(
			HttpConstants.HTTP_WHITEBOARD_CONTEXT_SHARED, true);

		_liferayHttpService.setDefaultServletContextHelper(
			defaultServletContextHelper, serviceMap);

		_liferayHttpService.activate(
			_componentContext, TU.serviceMap("osgi.http.endpoint", "/o"));

		_bridgeServlet = new BridgeServlet();

		_bridgeServlet.setLiferayHttpService(_liferayHttpService);

		_servletCollector = new ServletCollector();

		_servletCollector.setLiferayHttpService(_liferayHttpService);

		_servletCollector.setServlet(
			new TestServlet(),
			TU.serviceMap(HttpConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "/b"));
	}

	@Test
	public void testNotFound() throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.setRequestURI("/o/a");

		MockHttpServletResponse response = new MockHttpServletResponse();

		_bridgeServlet.service(request, response);

		Assert.assertEquals(
			HttpServletResponse.SC_NOT_FOUND, response.getStatus());
	}

	@Test
	public void testFound() throws IOException, ServletException {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.setRequestURI("/o/b");

		MockHttpServletResponse response = new MockHttpServletResponse();

		_bridgeServlet.service(request, response);

		Assert.assertEquals(
			HttpServletResponse.SC_OK, response.getStatus());
		Assert.assertEquals("hello", response.getContentAsString());
	}

	private void buildMocks() {
		mockStatic(FrameworkUtil.class);

		when(
			FrameworkUtil.getBundle(_servletContextHelper.getClass())
		).thenReturn(
			_bundle
		);

		when(
			_componentContext.getBundleContext()
		).thenReturn(
			_bundleContext
		);

		when(
			_bundleContext.getBundle()
		).thenReturn(
			_bundle
		);

		when(
			_bundle.adapt(BundleWiring.class)
		).thenReturn(
			_bundleWiring
		);

		when(
			_bundle.adapt(BundleWiring.class)
		).thenReturn(
			_bundleWiring
		);

		Class<?> clazz = getClass();

		when(
			_bundleWiring.getClassLoader()
		).thenReturn(
			clazz.getClassLoader()
		);
	}

	private BridgeServlet _bridgeServlet;

	@Mock
	private Bundle _bundle;

	@Mock
	private BundleContext _bundleContext;

	@Mock
	private BundleWiring _bundleWiring;

	@Mock
	private ComponentContext _componentContext;

	private LiferayHttpService _liferayHttpService;

	private ServletCollector _servletCollector;

	@Mock
	private ServletContextHelper _servletContextHelper;

	@Mock
	private ServiceRegistration _serviceRegistration;

}