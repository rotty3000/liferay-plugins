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

import com.liferay.osgi.service.http.internal.servlet.HttpServletConfig;
import com.liferay.osgi.service.http.internal.servlet.ServletProperties;
import com.liferay.portal.kernel.util.Validator;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;

import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.http.runtime.ServletDTO;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"provider=Liferay Inc."
	},
	service = {
		ServletCollector.class
	}
)
public class ServletCollector {

	@Reference(
		cardinality = ReferenceCardinality.MANDATORY,
		target="(provider=Liferay Inc.)"
	)
	protected void setLiferayHttpService(
		LiferayHttpService liferayHttpService) {

		_liferayHttpService = liferayHttpService;
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target="(osgi.http.whiteboard.servlet.pattern=*)"
	)
	protected void setServlet(Servlet servlet, Map<String, Object> properties) {
		try {
			ServletProperties servletProperties = ServletProperties.cnv(
				properties, servlet);

			if (!targetMatches(servletProperties)) {
				return;
			}

			HttpServletContext httpServletContext =
				_liferayHttpService.getServletContext(servletProperties);

			ConcurrentMap<Servlet, ServletDTO> servletMap =
				httpServletContext.getServletMap();

			ServletDTO servletDTO = new ServletDTO();

			servletDTO.asyncSupported = servletProperties.getProps().
				osgi_http_whiteboard_servlet_asyncSupported();
			servletDTO.name = servletProperties.getServletName();
			servletDTO.patterns =
				servletProperties.getProps().osgi_http_whiteboard_servlet_pattern().
					toArray(new String[0]);
			servletDTO.serviceId = servletProperties.getProps().service_id();
			servletDTO.servletContextId = httpServletContext.getServiceId();
			servletDTO.servletInfo = servlet.getServletInfo();

			ServletConfig servletConfig = new HttpServletConfig(
				httpServletContext, servletDTO.name, servletProperties);

			servlet.init(servletConfig);

			servletMap.put(servlet, servletDTO);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void unsetLiferayHttpService(
		LiferayHttpService liferayHttpService) {

		_liferayHttpService = null;
	}

	protected void unsetServlet(
		Servlet servlet, Map<String, Object> properties) {

		try {
			ServletProperties servletProperties = ServletProperties.cnv(
				properties, servlet);

			HttpServletContext servletContext =
				_liferayHttpService.getServletContext(servletProperties);

			ConcurrentMap<Servlet, ServletDTO> servletMap =
				servletContext.getServletMap();

			ServletDTO servletDTO = servletMap.remove(servlet);

			if (servletDTO != null) {
				servlet.destroy();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private boolean targetMatches(ServletProperties servletProperties)
		throws InvalidSyntaxException {

		String targetFilterString =
			servletProperties.getProps().osgi_http_whiteboard_target();

		if (Validator.isNull(targetFilterString)) {
			return true;
		}

		Filter filter = FrameworkUtil.createFilter(targetFilterString);

		return filter.matches(_liferayHttpService.getAttributes());
	}

	private LiferayHttpService _liferayHttpService;

}