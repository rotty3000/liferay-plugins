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

import com.liferay.portal.PwdEncryptorException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.portlet.NoRedirectActionResponse;
import com.liferay.portal.kernel.struts.LastPath;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.pwd.PasswordEncryptorUtil;
import com.liferay.portal.service.LayoutLocalService;
import com.liferay.portal.util.PortalUtil;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.portlet.filter.ActionFilter;
import javax.portlet.filter.FilterChain;
import javax.portlet.filter.FilterConfig;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

/**
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=58"
	}
)
public class RenderFilter_One implements ActionFilter {

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig filterConfig) throws PortletException {
	}

	@Override
	public void doFilter(
			ActionRequest actionRequest, ActionResponse actionResponse,
			FilterChain filterChain)
		throws IOException, PortletException {

		String strutsAction = ParamUtil.getString(
			actionRequest, "struts_action");

		try {
			if (strutsAction.equals("/login/login")) {
				login(actionRequest, actionResponse, filterChain);
			}
			else if (strutsAction.equals("createAccount")) {
				//createAccount(actionRequest, actionResponse);
			}
			else {
				filterChain.doFilter(actionRequest, actionResponse);
			}
		}
		catch (Exception e) {
			throw new PortletException(e);
		}
	}

	private void login(
			ActionRequest actionRequest, ActionResponse actionResponse,
			FilterChain filterChain)
		throws IOException, PortletException, PwdEncryptorException,
			SystemException {

		long targetPlid = ParamUtil.getLong(actionRequest, "targetPlid");

		if (targetPlid > 0) {
			Layout targetLayout = _layoutLocalService.fetchLayout(
				targetPlid);

			Map<String, String[]> params = new HashMap<String, String[]>();

			params.put("p_l_id", new String[] {String.valueOf(targetPlid)});

			LastPath lastPath = new LastPath(
				"/c", "/portal/layout", params);

			PortletSession portletSession =
				actionRequest.getPortletSession();

			portletSession.setAttribute(
				WebKeys.LAST_PATH, lastPath,
				PortletSession.APPLICATION_SCOPE);
		}

		final Map<String, String[]> renderParams =
			new HashMap<String, String[]>();

		NoRedirectActionResponse noRedirectActionResponse =
			new NoRedirectActionResponse(actionResponse) {

			@Override
			public void setRenderParameter(String key, String value) {
				renderParams.put(key, new String[] {value});
			}

			@Override
			public void setRenderParameter(String key, String[] values) {
				renderParams.put(key, values);
			}

			@Override
			public void setRenderParameters(Map<String, String[]> parameters) {
				renderParams.putAll(parameters);
			}

		};

		filterChain.doFilter(actionRequest, noRedirectActionResponse);

		String login = ParamUtil.getString(actionRequest, "login");
		String password = ParamUtil.getString(actionRequest, "password");
		String rememberMe = ParamUtil.getString(
			actionRequest, "rememberMe", "true");

		String userPassword = PasswordEncryptorUtil.encrypt(password);

		if (Validator.isNull(noRedirectActionResponse.getRedirectLocation())) {
			actionResponse.setRenderParameter("login", login);
			actionResponse.setRenderParameter("rememberMe", rememberMe);
		}
		else {
			StringBuilder sb = new StringBuilder();

			sb.append(PortalUtil.getPathMain());
			sb.append("/portal/login?login=");
			sb.append(HttpUtil.encodePath(login));
			sb.append("&password=");
			sb.append(HttpUtil.encodePath(userPassword));
			sb.append("&rememberMe=");
			sb.append(rememberMe);

			String redirect = sb.toString();

			actionResponse.sendRedirect(redirect);
		}
	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private void setLayoutLocalService(LayoutLocalService layoutLocalService) {
		_layoutLocalService = layoutLocalService;
	}

	private LayoutLocalService _layoutLocalService;

}