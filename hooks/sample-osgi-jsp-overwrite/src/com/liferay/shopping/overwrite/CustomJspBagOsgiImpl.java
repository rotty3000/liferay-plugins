/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
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
package com.liferay.shopping.overwrite;

import com.liferay.portal.deploy.hot.CustomJspBag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
@Component(immediate = true, service = CustomJspBag.class)
public class CustomJspBagOsgiImpl implements CustomJspBag {

	@Override
	public String getCustomJspDir() {
		return "/WEB-INF/custom_jsps";
	}

	@Override
	public List<String> getCustomJsps() {
		return _customJsps;
	}

	@Override
	public String getPluginPackageName() {
		return "Sample OSGI JSP Overwrite";
	}

	@Override
	public ServletContext getServletContext() {
		return _servletContext;
	}

	@Override
	public String getServletContextName() {
		return _servletContext.getServletContextName();
	}

	@Override
	public boolean isCustomJspGlobal() {
		return true;
	}

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.shopping.overwrite)"
	)
	protected void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private final List<String> _customJsps = new ArrayList<>();
	private ServletContext _servletContext;

}