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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration.Dynamic;

/**
 * @author Raymond Augé
 */
public class DynamicFilterRegistration implements Dynamic {

	public DynamicFilterRegistration(String name, Filter filter) {
		_name = name;
		_filter = filter;

		_dispatcherServletNameMappings =
			new HashMap<DispatcherType, List<String>[]>();
		_dispatcherUrlPatternMappings =
			new HashMap<DispatcherType, List<String>[]>();

		_servletNameMappings = new HashSet<String>();
		_urlPatternMappings = new HashSet<String>();
	}

	@Override
	public void addMappingForServletNames(
		EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter,
		String... servletNames) {

		for (String servletName : servletNames) {
			_servletNameMappings.add(servletName);
		}

		for (DispatcherType dispatcherType : dispatcherTypes) {
			List<String>[] mappingArray = _dispatcherServletNameMappings.get(
				dispatcherType);

			if (mappingArray == null) {
				mappingArray = new List[2];

				mappingArray[0] = new ArrayList<String>();
				mappingArray[1] = new ArrayList<String>();

				_dispatcherServletNameMappings.put(dispatcherType, mappingArray);
			}

			for (String servletName : servletNames) {
				if (!isMatchAfter) {
					mappingArray[0].add(servletName);
				}
				else {
					mappingArray[1].add(servletName);
				}
			}
		}
	}

	@Override
	public void addMappingForUrlPatterns(
		EnumSet<DispatcherType> dispatcherTypes, boolean isMatchAfter,
		String... urlPatterns) {

		for (String urlPattern : urlPatterns) {
			_urlPatternMappings.add(urlPattern);
		}

		for (DispatcherType dispatcherType : dispatcherTypes) {
			List<String>[] mappingArray = _dispatcherUrlPatternMappings.get(
				dispatcherType);

			if (mappingArray == null) {
				mappingArray = new List[2];

				mappingArray[0] = new ArrayList<String>();
				mappingArray[1] = new ArrayList<String>();

				_dispatcherServletNameMappings.put(dispatcherType, mappingArray);
			}

			for (String servletName : urlPatterns) {
				if (!isMatchAfter) {
					mappingArray[0].add(servletName);
				}
				else {
					mappingArray[1].add(servletName);
				}
			}
		}
	}

	public Map<DispatcherType, List<String>[]>
		getDispatcherServletNameMappings() {

		return _dispatcherServletNameMappings;
	}

	public Map<DispatcherType, List<String>[]>
		getDispatcherUrlPatternMappings() {

		return _dispatcherUrlPatternMappings;
	}

	public Filter getFilter() {
		return _filter;
	}

	@Override
	public Collection<String> getServletNameMappings() {
		return _servletNameMappings;
	}

	@Override
	public Collection<String> getUrlPatternMappings() {
		return _urlPatternMappings;
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

	@Override
	public String getName() {
		return _name;
	}

	public boolean isAsyncSupported() {
		return _asyncSupported;
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
	public void setAsyncSupported(boolean asyncSupported) {
		_asyncSupported = asyncSupported;
	}

	private boolean _asyncSupported = false;
	private String _className;
	private Map<DispatcherType, List<String>[]> _dispatcherServletNameMappings;
	private Map<DispatcherType, List<String>[]> _dispatcherUrlPatternMappings;
	private Filter _filter;
	private Map<String, String> _initParameters = new HashMap<String, String>();
	private String _name;
	private Set<String> _servletNameMappings;
	private Set<String> _urlPatternMappings;

}