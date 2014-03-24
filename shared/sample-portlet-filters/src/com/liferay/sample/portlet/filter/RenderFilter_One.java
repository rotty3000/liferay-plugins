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

package com.liferay.sample.portlet.filter;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;
import javax.portlet.filter.RenderFilter;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=58"
	}
)
public class RenderFilter_One implements ActionFilter, RenderFilter {

	public void destroy() {
		System.out.println(toString() + " in destroy!");
	}

	public void init(FilterConfig filterConfig) throws PortletException {
		System.out.println(toString() + " in init!");
	}

	@Override
	public void doFilter(
			ActionRequest actionRequest, ActionResponse actionResponse,
			FilterChain filterChain)
		throws IOException, PortletException {

		System.out.println(
			toString() + " filtering action for " +
				actionResponse.getNamespace());

		filterChain.doFilter(actionRequest, actionResponse);
	}

	public void doFilter(
			RenderRequest renderRequest, RenderResponse renderResponse,
			FilterChain filterChain)
		throws IOException, PortletException {

		System.out.println(
			toString() + " filtering render for " +
				renderResponse.getNamespace());

		filterChain.doFilter(renderRequest, renderResponse);
	}

	@Activate
	private void activate() {
		System.out.println(toString() + " activated!");
	}

	@Deactivate
	private void deactivate() {
		System.out.println(toString() + " deactivated!");
	}

}