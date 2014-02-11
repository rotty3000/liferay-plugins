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

import org.osgi.framework.Bundle;
import org.osgi.service.http.ServletContextHelper;

/**
 * @author Raymond Augé
 */
@SuppressWarnings("deprecation")
public class DefaultServletContextHelper extends ServletContextHelper
	implements HttpContextNameAware {

	public DefaultServletContextHelper(Bundle bundle, String contextName) {
		setBundle(bundle);

		_contextName = contextName;
	}

	@Override
	public String getContextName() {
		return _contextName;
	}

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

	private final String _contextName;

}