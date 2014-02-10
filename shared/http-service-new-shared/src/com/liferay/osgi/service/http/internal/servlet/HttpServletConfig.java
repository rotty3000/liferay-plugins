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

package com.liferay.osgi.service.http.internal.servlet;

import com.liferay.osgi.service.http.internal.HttpServletContext;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 */
public class HttpServletConfig implements ServletConfig {

	public HttpServletConfig(
		HttpServletContext httpServletContext, String servletName,
		ServletProperties servletProperties) {

		_httpServletContext = httpServletContext;
		_servletName = servletName;
		_servletProperties = servletProperties;
	}

	@Override
	public String getInitParameter(String name) {
		return _servletProperties.getInitParameters().get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(
			_servletProperties.getInitParameters().keySet());
	}

	@Override
	public HttpServletContext getServletContext() {
		return _httpServletContext;
	}

	@Override
	public String getServletName() {
		return _servletName;
	}

	protected Map<String, String> getInitParameters() {
		return _servletProperties.getInitParameters();
	}

	protected void setInitParameter(String name, String value) {
		_servletProperties.getInitParameters().put(name, value);
	}

	private HttpServletContext _httpServletContext;
	private String _servletName;
	private ServletProperties _servletProperties;

}