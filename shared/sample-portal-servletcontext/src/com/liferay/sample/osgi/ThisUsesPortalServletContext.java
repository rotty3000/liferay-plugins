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

package com.liferay.sample.osgi;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true
)
public class ThisUsesPortalServletContext {

	@Activate
	public void activate() {
		// here the _servletContext is available
	}

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		target ="(&(bean.id=javax.servlet.ServletContext)(original.bean=*))"
	)
	public void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	public void unsetServletContext() {
		_servletContext = null;
	}

	private ServletContext _servletContext;

}