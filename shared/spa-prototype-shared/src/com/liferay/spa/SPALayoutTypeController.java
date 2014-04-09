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

package com.liferay.spa;

import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypeController;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.LayoutTypeWrapper;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.LayoutTypeControllerHelper;

import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"layout.type=spa"
	},
	service = LayoutTypeController.class
)
public class SPALayoutTypeController implements LayoutTypeController {

	@Override
	public String[] getConfigurationActionDelete() {
		return _EMPTY_ARRAY;
	}

	@Override
	public String[] getConfigurationActionUpdate() {
		return _EMPTY_ARRAY;
	}

	/**
	 * You only need this to return a value if there is a configuration UI
	 * component to this controller.
	 *
	 * The portal has to be able to perform a dispatch to this url. In order to
	 * be able to provide such a UI, the implemetor would have to provide a
	 * servlet which can handle the dispatch.
	 *
	 * @return the path to a resource for the edit layout UI
	 */
	@Override
	public String getEditPage() {
		return null;
	}

	@Override
	public LayoutTypeWrapper getLayoutTypeWrapper(
		LayoutTypePortlet layoutTypePortlet) {

		return new LayoutTypeWrapper(layoutTypePortlet, this);
	}

	@Override
	public String getURL() {
		return null;
	}

	/**
	 * Generate the content of the type.
	 *
	 * @param request the {@link HttpServletRequest}
	 * @param response the {@link HttpServletRequest}
	 * @param themeDisplay the {@link ThemeDisplay}
	 * @param portletId the portlet id
	 * @return true if the result should be wrapped by the portal decorations
	 *         (only valid if the return type is text/html
	 */
	@Override
	public boolean includeLayoutContent(
			HttpServletRequest request, HttpServletResponse response,
			ThemeDisplay themeDisplay, String portletId)
		throws Exception {

		String requestURI = request.getRequestURI();
		String queryString = request.getQueryString();

		// We'll always have been forwarded

		String forwardRequestURI = (String)request.getAttribute(
			RequestDispatcher.FORWARD_REQUEST_URI);

		if (forwardRequestURI != null) {
			requestURI = forwardRequestURI;

			String forwardQueryString = (String)request.getAttribute(
				RequestDispatcher.FORWARD_QUERY_STRING);

			queryString = forwardQueryString;
		}

		// If this is a portlet request, let it be handled natively so we can do
		// basic things like login. This could get more advanced if needed. For
		// instance, login is always maximized, but we're not checking it here.
		// We might only do this if MAXIMIZED or some other state. However,
		// the framework controller could implement it's own portlet rendering.

		if (Validator.isNotNull(portletId)) {
			return doPortlet(request, response, themeDisplay, portletId);
		}

		// This is where the business logic of the framework controller would
		// start.

		response.setContentType("text/plain");

		ServletOutputStream outputStream = response.getOutputStream();

		PrintWriter printWriter = new PrintWriter(outputStream);

		printWriter.print(
			"Hello World! " + requestURI +
				((queryString != null) ? "?" + queryString : ""));

		printWriter.close();

		return true;
	}

	/**
	 * @return true if the type implemented can be the first page of the site
	 */
	@Override
	public boolean isFirstPageable() {
		return true;
	}

	/**
	 * @return true if the type implemented can have child pages
	 */
	@Override
	public boolean isParentable() {
		return false;
	}

	/**
	 * @return if the type implemented can appear in the sitemap
	 */
	@Override
	public boolean isSitemapable() {
		return true;
	}

	/**
	 * @return true if the type implemented can have friendly urls
	 */
	@Override
	public boolean isURLFriendliable() {
		return true;
	}

	/**
	 * Used to determine if this type can take effect if it matches certain
	 * criteria. Any of the input data can be used as criteria.
	 */
	@Override
	public boolean matches(
		Layout layout, String friendlyURL, String queryString) {

		return true;
	}

	@Activate
	private void activate() {
		System.out.println(this + " activated!");
	}

	@Deactivate
	private void deactivate() {
		System.out.println(this + " deactivated!");
	}

	private boolean doPortlet(
			HttpServletRequest request, HttpServletResponse response,
			ThemeDisplay themeDisplay, String portletId)
		throws Exception {

		Layout layout = themeDisplay.getLayout();

		LayoutTypeWrapper layoutTypeWrapper =
			(LayoutTypeWrapper)layout.getLayoutType();

		LayoutTypeController layoutTypeFactory =
			LayoutTypeControllerHelper.getLayoutTypeFactory("portlet");

		layoutTypeWrapper = layoutTypeFactory.getLayoutTypeWrapper(
			(LayoutTypePortlet)layoutTypeWrapper.getWrappedLayoutType());

		return layoutTypeWrapper.includeLayoutContent(
			request, response, themeDisplay, portletId);
	}

	private static final String[] _EMPTY_ARRAY = new String[0];

}