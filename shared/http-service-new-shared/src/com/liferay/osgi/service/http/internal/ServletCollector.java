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

import com.liferay.osgi.service.http.internal.servlet.ErrorPageHolder;
import com.liferay.osgi.service.http.internal.servlet.Holder;
import com.liferay.osgi.service.http.internal.servlet.HttpServletConfig;
import com.liferay.osgi.service.http.internal.servlet.ResourceHolder;
import com.liferay.osgi.service.http.internal.servlet.ServletHolder;
import com.liferay.osgi.service.http.internal.servlet.ServletProperties;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.net.URLConnection;

import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.dto.DTO;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;
import org.osgi.service.http.runtime.ErrorPageDTO;
import org.osgi.service.http.runtime.ResourceDTO;
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

			if (httpServletContext == null) {
				return;
			}

			List<String> errorPage = servletProperties.getProps().
				osgi_http_whiteboard_servlet_errorPage();
			String resourcePrefix = servletProperties.getProps().
				osgi_http_whiteboard_resource_prefix();

			if ((errorPage != null) && !errorPage.isEmpty()) {
				doErrorPage(servlet, servletProperties, httpServletContext);
			}
			else if (resourcePrefix != null) {
				doResource(servlet, servletProperties, httpServletContext);
			}
			else {
				doServlet(servlet, servletProperties, httpServletContext);
			}
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

			HttpServletContext httpServletContext =
				_liferayHttpService.getServletContext(servletProperties);

			if (httpServletContext == null) {
				return;
			}

			ConcurrentMap<Servlet, Holder<Servlet, ? extends DTO>> servletMap =
				httpServletContext.getServletMap();

			Holder<Servlet, ? extends DTO> holder = servletMap.remove(servlet);

			if (holder != null) {
				holder.destroy();
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void doErrorPage(
			Servlet servlet, ServletProperties servletProperties,
			HttpServletContext httpServletContext)
		throws ServletException {

		ErrorPageDTO errorPageDTO = new ErrorPageDTO();

		errorPageDTO.asyncSupported = servletProperties.getProps().
			osgi_http_whiteboard_servlet_asyncSupported();

		// TODO

		errorPageDTO.errorCodes = new long[0];
		errorPageDTO.exceptions = new String[0];
		errorPageDTO.name = servletProperties.getServletName();
		errorPageDTO.serviceId = servletProperties.getProps().service_id();
		errorPageDTO.servletContextId = httpServletContext.getServiceId();

		ServletConfig servletConfig = new HttpServletConfig(
			httpServletContext, errorPageDTO.name, servletProperties);

		servlet.init(servletConfig);

		errorPageDTO.servletInfo = servlet.getServletInfo();

		ConcurrentMap<Servlet, Holder<Servlet, ? extends DTO>> servletHolders =
			httpServletContext.getServletMap();

		servletHolders.put(
			servlet, new ErrorPageHolder(servlet, errorPageDTO));
	}

	private void doResource(
			Servlet servlet, ServletProperties servletProperties,
			HttpServletContext httpServletContext)
		throws ServletException {

		ResourceDTO resourceDTO = new ResourceDTO();

		resourceDTO.patterns = servletProperties.getProps().
			osgi_http_whiteboard_servlet_pattern().toArray(new String[0]);
		resourceDTO.prefix = servletProperties.getProps().
			osgi_http_whiteboard_resource_prefix();
		resourceDTO.serviceId = servletProperties.getProps().service_id();
		resourceDTO.servletContextId = httpServletContext.getServiceId();

		Servlet resourceServlet = new ResourceWrapperServlet(
			resourceDTO.prefix);

		ServletConfig servletConfig = new HttpServletConfig(
			httpServletContext, servlet.getClass().getName(),
			servletProperties);

		resourceServlet.init(servletConfig);

		ConcurrentMap<Servlet, Holder<Servlet, ? extends DTO>> servletHolders =
			httpServletContext.getServletMap();

		servletHolders.put(
			servlet, new ResourceHolder(resourceServlet, resourceDTO));
	}

	private void doServlet(
			Servlet servlet, ServletProperties servletProperties,
			HttpServletContext httpServletContext)
		throws ServletException {

		ServletDTO servletDTO = new ServletDTO();

		servletDTO.asyncSupported = servletProperties.getProps().
			osgi_http_whiteboard_servlet_asyncSupported();
		servletDTO.name = servletProperties.getServletName();
		servletDTO.patterns = servletProperties.getProps().
			osgi_http_whiteboard_servlet_pattern().toArray(new String[0]);
		servletDTO.serviceId = servletProperties.getProps().service_id();
		servletDTO.servletContextId = httpServletContext.getServiceId();

		ServletConfig servletConfig = new HttpServletConfig(
			httpServletContext, servletDTO.name, servletProperties);

		servlet.init(servletConfig);

		servletDTO.servletInfo = servlet.getServletInfo();

		ConcurrentMap<Servlet, Holder<Servlet, ? extends DTO>> servletHolders =
			httpServletContext.getServletMap();

		servletHolders.put(servlet, new ServletHolder(servlet, servletDTO));
	}

	private long stream(InputStream input, OutputStream output)
		throws IOException {

		ReadableByteChannel inputChannel = null;
		WritableByteChannel outputChannel = null;

		try {
			inputChannel = Channels.newChannel(input);
			outputChannel = Channels.newChannel(output);
			ByteBuffer buffer = ByteBuffer.allocate(10240);
			long size = 0;

			while (inputChannel.read(buffer) != -1) {
				buffer.flip();
				size += outputChannel.write(buffer);
				buffer.clear();
			}

			return size;
		}
		finally {
			if (outputChannel != null) {
				try {
					outputChannel.close();
				}
				catch (IOException ignore) {
					/**/
				}
			}

			if (inputChannel != null) {
				try {
					inputChannel.close();
				}
				catch (IOException ignore) {
					/**/
				}
			}
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

	private class ResourceWrapperServlet extends HttpServlet {

		public ResourceWrapperServlet(String prefix) {
			_prefix = prefix;
		}

		@Override
		protected void service(
				HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

			ServletContext servletContext = getServletContext();

			String requestURI = request.getRequestURI();

			URL url = servletContext.getResource(_prefix.concat(requestURI));

			if (url != null) {
				URLConnection urlConnection = url.openConnection();

				int contentLength = urlConnection.getContentLength();
				String mimeType = servletContext.getMimeType(requestURI);

				response.setContentType(mimeType);
				response.setContentLength(contentLength);

				stream(
					urlConnection.getInputStream(), response.getOutputStream());
			}
			else {
				response.sendError(
					HttpServletResponse.SC_NOT_FOUND,
					"Could not find " + requestURI);
			}
		}

		private final String _prefix;

	}

}