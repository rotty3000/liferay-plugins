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

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 */
public class ServletConfigImpl implements ServletConfig {

	public ServletConfigImpl(
		ServletContext servletContext, String servletName,
		Map<String, String> initParameters) {

		_servletContext = servletContext;
		_servletName = servletName;
		_initParameters = initParameters;

		if (_initParameters == null) {
			_initParameters = new Hashtable<String, String>();
		}
	}

	@Override
	public String getInitParameter(String name) {
		return _initParameters.get(name);
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(_initParameters.keySet());
	}

	public Map<String, String> getInitParameters() {
		return _initParameters;
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public String getServletName() {
		return _servletName;
	}

	public void setInitParameter(String name, String value) {
		_initParameters.put(name, value);
	}

	private Map<String, String> _initParameters;
	private ServletContext _servletContext;
	private String _servletName;

}