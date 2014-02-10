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

import com.liferay.portal.kernel.util.MimeTypesUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.ServletContextHelper;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"provider=Liferay Inc."
	},
	service = {
		DefaultServletContextHelper.class, ServletContextHelper.class
	}
)
@SuppressWarnings("deprecation")
public class DefaultServletContextHelper extends ServletContextHelper
	implements HttpContext {

	@Override
	public String getMimeType(String name) {
		return MimeTypesUtil.getContentType(name);
	}

	@Override
	public boolean handleSecurity(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		// TODO

		return super.handleSecurity(request, response);
	}

	@Activate
	protected void activate(ComponentContext componentContext) {
		setBundle(componentContext.getBundleContext().getBundle());
	}

	@Deactivate
	protected void deactivate() {
		setBundle(null);
	}

}