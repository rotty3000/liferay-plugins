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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;

import com.liferay.osgi.property.UnmodifiableDictionaryMap;
import com.liferay.osgi.service.http.util.TU;
import com.liferay.osgi.service.http.util.TU.HandyMap;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.Servlet;

import org.junit.Before;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpConstants;
import org.osgi.service.http.ServletContextHelper;

import org.springframework.mock.web.MockServletContext;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

/**
 * @author Raymond Augé
 */
public class BaseHttpTesting extends PowerMockito {

	@Before
	public void setUp() throws InvalidSyntaxException {
		buildMocks();

		_liferayHttpService = new LiferayHttpService();

		_liferayHttpService.setSerlvetContext(new MockServletContext());

		HandyMap serviceMap = TU.serviceMap(
			HttpConstants.HTTP_SERVICE_ENDPOINT_ATTRIBUTE, "/o").add(
			HttpConstants.HTTP_WHITEBOARD_CONTEXT_NAME, "").add(
			HttpConstants.HTTP_WHITEBOARD_CONTEXT_SHARED, true);

		_liferayHttpService.activate(_componentContext, serviceMap);

		_bridgeServlet = new BridgeServlet();

		_bridgeServlet.setLiferayHttpService(_liferayHttpService);

		_servletCollector = new ServletCollector();

		_servletCollector.setLiferayHttpService(_liferayHttpService);

		_httpServiceFactory = new HttpServiceFactory();

		_httpServiceFactory.setLiferayHttpService(_liferayHttpService);

		_httpServiceFactory.activate(_componentContext, serviceMap);
	}

	protected void buildMocks() {
		mockStatic(FrameworkUtil.class);

		when(
			FrameworkUtil.getBundle(any(Class.class))
		).thenReturn(
			_bundle
		);

		when(
			_componentContext.getBundleContext()
		).thenReturn(
			_bundleContext
		);

		when(
			_componentContext.getUsingBundle()
		).thenReturn(
			_bundle
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
			_bundle.getBundleContext()
		).thenReturn(
			_bundleContext
		);

		Class<?> clazz = getClass();

		when(
			_bundleWiring.getClassLoader()
		).thenReturn(
			clazz.getClassLoader()
		);
	}

	@SuppressWarnings("unchecked")
	protected void mockRegisterService(final AtomicReference<Object[]> args) {
		Matcher<Dictionary<String, ?>> m =
			new BaseMatcher<Dictionary<String, ?>>() {

				@Override
				public boolean matches(Object item) {
					return (item instanceof Dictionary);
				}

				@Override
				public void describeTo(Description description) {/**/}

			};

		when(
			_bundleContext.registerService(
				any(Class.class), any(), argThat(m))
		).thenAnswer(
			new Answer<ServiceRegistration<?>>() {

				@Override
				public ServiceRegistration<?> answer(
						InvocationOnMock invocation)
					throws Throwable {

					args.set(invocation.getArguments());

					Map<String, Object> map = toMap(args.get()[2]);

					_servletCollector.setServlet((Servlet)args.get()[1], map);

					return _serviceRegistration;
				}

			}
		);

		doAnswer(
			new Answer<Void>() {

				@Override
				public Void answer(InvocationOnMock invocation)
					throws Throwable {

					Map<String, Object> map = toMap(args.get()[2]);

					_servletCollector.unsetServlet((Servlet)args.get()[1], map);

					return null;
				}

			}
		).when(
			_serviceRegistration
		).unregister();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map<String, Object> toMap(Object arg) {
		return new UnmodifiableDictionaryMap<String, Object>((Dictionary)arg);
	}

	protected BridgeServlet _bridgeServlet;

	@Mock
	protected Bundle _bundle;

	@Mock
	protected BundleContext _bundleContext;

	@Mock
	protected BundleWiring _bundleWiring;

	@Mock
	protected ComponentContext _componentContext;

	protected HttpServiceFactory _httpServiceFactory;

	protected LiferayHttpService _liferayHttpService;

	protected ServletCollector _servletCollector;

	@Mock
	protected ServletContextHelper _servletContextHelper;

	@Mock
	protected ServiceRegistration<?> _serviceRegistration;

}