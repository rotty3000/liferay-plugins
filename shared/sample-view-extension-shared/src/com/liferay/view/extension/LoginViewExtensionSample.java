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

package com.liferay.view.extension;

import com.liferay.kernel.servlet.taglib.ViewExtension;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Carlos Sierra Andrés
 */
@Component(
	immediate = true, property = {"extension-id=login-form-after-password"},
	service = ViewExtension.class)
public class LoginViewExtensionSample implements ViewExtension {

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		RenderResponse renderResponse = (RenderResponse)request.getAttribute(
			JavaConstants.JAVAX_PORTLET_RESPONSE);

		PortletURL actionURL = renderResponse.createActionURL();
		try {
			actionURL.setWindowState(WindowState.MAXIMIZED);
		}
		catch (WindowStateException e) {
			e.printStackTrace();
		}

		actionURL.setParameter("extension", "true");

		PrintWriter writer = response.getWriter();

		writer.write(
			"<a href=\"" + actionURL.toString() + "\">extension link</a>");
	}

}