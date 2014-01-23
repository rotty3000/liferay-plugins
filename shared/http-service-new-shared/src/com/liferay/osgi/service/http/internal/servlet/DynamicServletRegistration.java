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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.ServletSecurityElement;

/**
 * @author Raymond Augé
 */
public class DynamicServletRegistration implements Dynamic {

	public DynamicServletRegistration(String servletName, Servlet servlet) {
		_name = servletName;
		_servlet = servlet;

		_className = _servlet.getClass().getName();
	}

	@Override
	public Set<String> addMapping(String... mappings) {
		for (String mapping : mappings) {
			_mappings.add(mapping);
		}

		return _mappings;
	}

	@Override
	public String getClassName() {
		return _className;
	}

	@Override
	public String getInitParameter(String name) {
		return _initParameters.get(name);
	}

	@Override
	public Map<String, String> getInitParameters() {
		return Collections.unmodifiableMap(_initParameters);
	}

	public int getLoadOnStartup() {
		return _loadOnStartup;
	}

	@Override
	public Collection<String> getMappings() {
		return _mappings;
	}

	@Override
	public String getName() {
		return _name;
	}

	@Override
	public String getRunAsRole() {
		return _roleName;
	}

	public Servlet getServlet() {
		return _servlet;
	}

	public boolean isAsyncSupported() {
		return _asyncSupported;
	}

	@Override
	public void setAsyncSupported(boolean asyncSupported) {
		_asyncSupported = asyncSupported;
	}

	@Override
	public boolean setInitParameter(String name, String value) {
		boolean exists = _initParameters.containsKey(name);

		if (!exists) {
			_initParameters.put(name, value);
		}

		return !exists;
	}

	@Override
	public Set<String> setInitParameters(Map<String, String> initParameters) {
		Set<String> _conflicting = new HashSet<String>();

		for (Entry<String, String> entry : initParameters.entrySet()) {
			if (!setInitParameter(entry.getKey(), entry.getValue())) {
				_conflicting.add(entry.getKey());
			}
		}

		return _conflicting;
	}

	@Override
	public void setLoadOnStartup(int loadOnStartup) {
		_loadOnStartup = loadOnStartup;
	}

	@Override
	public void setMultipartConfig(
		MultipartConfigElement multipartConfigElement) {

		_multipartConfigElement = multipartConfigElement;
	}

	@Override
	public void setRunAsRole(String roleName) {
		_roleName = roleName;
	}

	@Override
	public Set<String> setServletSecurity(
		ServletSecurityElement servletSecurityElement) {

		_servletSecurityElement = servletSecurityElement;

		return Collections.emptySet();
	}

	private boolean _asyncSupported = false;
	private String _className;
	private Map<String, String> _initParameters = new HashMap<String, String>();
	private int _loadOnStartup = 0;
	private String _name;
	private TreeSet<String> _mappings = new TreeSet<String>();
	private MultipartConfigElement _multipartConfigElement;
	private String _roleName;
	private Servlet _servlet;
	private ServletSecurityElement _servletSecurityElement;

}