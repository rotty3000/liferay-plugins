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

import com.liferay.portal.kernel.struts.BaseStrutsAction;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.LayoutTypeFactory;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.LayoutTypeWrapper;
import com.liferay.portal.theme.ThemeDisplay;

import java.io.PrintWriter;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"layout.type=spa"
	},
	service = LayoutTypeFactory.class
)
public class SPALayoutTypeFactory extends BaseStrutsAction
	implements LayoutTypeFactory {

	@Override
	public String[] getConfigurationActionDelete() {
		return _EMPTY_ARRAY;
	}

	@Override
	public String[] getConfigurationActionUpdate() {
		return _EMPTY_ARRAY;
	}

	@Override
	public String getEditPage() {
		return _servletContext.getContextPath() + "/spa/conf/edit";
	}

	@Override
	public String getType() {
		return "spa";
	}

	@Override
	public String getURL() {
		return _URL;
	}

	@Override
	public String getURL(Map<String, String> variables) {
		return StringUtil.replace(
			_URL, StringPool.DOLLAR_AND_OPEN_CURLY_BRACE,
			StringPool.CLOSE_CURLY_BRACE, variables);
	}

	@Override
	public String getViewPage() {
		return _servletContext.getContextPath() + "/spa/conf/view";
	}

	@Override
	public boolean isExclusive() {
		return true;
	}

	@Override
	public boolean isFirstPageable() {
		return true;
	}

	@Override
	public boolean isParentable() {
		return false;
	}

	@Override
	public boolean isSitemapable() {
		return true;
	}

	@Override
	public boolean isURLFriendliable() {
		return true;
	}

	@Override
	public LayoutTypeWrapper getLayoutTypeWrapper(
		LayoutTypePortlet layoutTypePortlet) {

		return new SPALayoutTypeWrapper(layoutTypePortlet, this);
	}

	@Override
	public String execute(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String pathInfo = request.getPathInfo();

		PrintWriter writer = response.getWriter();

		writer.print("PathInfo: " + pathInfo);

		return null;
	}

	@Activate
	private void activate() {
		System.out.println(this + " activated!");
	}

	@Deactivate
	private void deactivate() {
		System.out.println(this + " deactivated!");
	}

	@Reference
	private void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
	}

	private static final String[] _EMPTY_ARRAY = new String[0];
	private static final String _URL =
		"${liferay:mainPath}/portal/layout?p_l_id=${liferay:plid}&" +
			"p_v_l_s_g_id=${liferay:pvlsgid}";

	private ServletContext _servletContext;

	private class SPALayoutTypeWrapper extends LayoutTypeWrapper {

		public SPALayoutTypeWrapper(
			LayoutTypePortlet layoutTypePortlet,
			LayoutTypeFactory layoutTypeFactory) {

			super(layoutTypePortlet, layoutTypeFactory);
		}

		@Override
		public void includeLayoutContent(
				HttpServletRequest request, HttpServletResponse response,
				ThemeDisplay themeDisplay, String portletId)
			throws Exception {

			response.setContentType("text/plain");

			ServletOutputStream outputStream = response.getOutputStream();

			PrintWriter printWriter = new PrintWriter(outputStream);

			printWriter.print("Hello World! " + request.getRequestURI());

			printWriter.close();
		}

	}

}