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

import com.liferay.osgi.service.http.util.TU;
import com.liferay.osgi.service.http.util.TU.HandyMap;

import java.util.Dictionary;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
import org.osgi.service.http.runtime.ServletContextDTO;

import org.springframework.mock.web.MockServletContext;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Raymond Augé
 */
@PrepareForTest({FrameworkUtil.class})
@RunWith(PowerMockRunner.class)
public class LiferayHttpServiceTest extends PowerMockito {

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
	}

	@Test
	public void testGetHttpServiceAttributes() {
		Map<String, Object> attributes = _liferayHttpService.getAttributes();

		Assert.assertNotNull(attributes);

		Assert.assertEquals("/o", attributes.get("osgi.http.endpoint"));
	}

	@Test
	public void testGetHttpServiceDTOs() {
		ServletContextDTO[] servletContextDTOs =
			_liferayHttpService.getServletContextDTOs();

		Assert.assertNotNull(servletContextDTOs);

		Assert.assertTrue(servletContextDTOs.length > 0);

		ServletContextDTO servletContextDTO = servletContextDTOs[0];

		Assert.assertEquals("", servletContextDTO.contextName);
		Assert.assertEquals("", servletContextDTO.contextPath);
		Assert.assertTrue(servletContextDTO.shared);
	}


	@Test
	public void testRegisterResource() {
		Matcher<Dictionary<String, ?>> m =
			new BaseMatcher<Dictionary<String, ?>>() {
				@Override
				public boolean matches(Object item) {
					return (item instanceof Dictionary);
				}
				@Override
				public void describeTo(Description description) {
				}
			};

		final AtomicReference<Object[]> args = new AtomicReference<Object[]>();

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

					return _serviceRegistration;
				}
			}
		);

		doNothing().when(
			_serviceRegistration
		).unregister();

		_liferayHttpService.registerResources("/blah", null, null);

		Mockito.verify(_bundleContext).registerService(
			(Class)args.get()[0], args.get()[1], (Dictionary)args.get()[2]);

		_liferayHttpService.unregister("/blah");

		Mockito.verify(_serviceRegistration).unregister();
	}

	@Test
	public void testGetContextPath() {
		String contextPath = _liferayHttpService.getContextPath();

		Assert.assertNotNull(contextPath);

		Assert.assertEquals("/o", contextPath);
	}

	@Test
	public void testGetServletContext_Default() {
		HttpServletContext servletContext =
			_liferayHttpService.getServletContext("");

		Assert.assertNotNull(servletContext);

		Assert.assertEquals("", servletContext.getContextPath());
	}

	@Test
	public void testResettingServletContext_Default() {
		HttpServletContext servletContext =
			_liferayHttpService.getServletContext("");

		int length = _liferayHttpService.getServletContextDTOs().length;

		_liferayHttpService.setServletContextHelper(
			servletContext.getServletContextHelper(),
			servletContext.getInitParameters());

		Assert.assertEquals(
			length, _liferayHttpService.getServletContextDTOs().length);
	}

	@Test
	public void testRemoveServletContext_Default() {
		HttpServletContext servletContext =
			_liferayHttpService.getServletContext("");

		int length = _liferayHttpService.getServletContextDTOs().length;

		_liferayHttpService.unsetServletContextHelper(
			servletContext.getServletContextHelper(),
			servletContext.getInitParameters());

		Assert.assertEquals(
			length, _liferayHttpService.getServletContextDTOs().length);
	}

	@Test
	public void testSetGetServletContext_Other() {
		_liferayHttpService.setServletContextHelper(
			_servletContextHelper,
			TU.serviceMap("osgi.http.whiteboard.context.name", "other"));

		HttpServletContext servletContext =
			_liferayHttpService.getServletContext("/other");

		Assert.assertNotNull(servletContext);

		Assert.assertEquals("/other", servletContext.getContextPath());
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

	@Mock
	private Bundle _bundle;

	@Mock
	private BundleContext _bundleContext;

	@Mock
	private BundleWiring _bundleWiring;

	@Mock
	private ComponentContext _componentContext;

	private LiferayHttpService _liferayHttpService;

	@Mock
	private ServletContextHelper _servletContextHelper;

	@Mock
	private ServiceRegistration _serviceRegistration;

}