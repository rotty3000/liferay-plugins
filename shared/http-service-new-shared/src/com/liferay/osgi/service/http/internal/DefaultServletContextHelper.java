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

package com.liferay.osgi.service.http.internal;

import java.io.IOException;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.HttpContext;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"osgi.http.whiteboard.context.name=default",
		"osgi.http.whiteboard.context.shared=true",
		"provider=Liferay Inc."
	},
	service = HttpContext.class
)
public class DefaultHttpContext implements HttpContext {

	@Override
	public boolean handleSecurity(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		return true;
	}

	@Override
	public URL getResource(String name) {
		return null;
	}

	@Override
	public String getMimeType(String name) {
		return null;
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		_componentContext = componentContext;

		System.out.println(this + " activated!");
	}

	@Deactivate
	protected void deactivate() {
		System.out.println(this + " deactivated!");
	}

	private ComponentContext _componentContext;

}