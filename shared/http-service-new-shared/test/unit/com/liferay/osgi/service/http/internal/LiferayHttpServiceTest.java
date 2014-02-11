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

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.http.runtime.ServletContextDTO;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Raymond Augé
 */
@PrepareForTest({FrameworkUtil.class})
@RunWith(PowerMockRunner.class)
public class LiferayHttpServiceTest extends BaseHttpTesting {

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

}