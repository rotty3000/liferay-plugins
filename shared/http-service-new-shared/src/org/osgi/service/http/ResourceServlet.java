/*
 * Copyright (c) OSGi Alliance (2012, 2013). All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.osgi.service.http;

import javax.servlet.GenericServlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Bundle resource servlet.
 * 
 * <p>
 * The {@code ResourceServlet} is a marker servlet which can be used to serve
 * bundle resources. A {@code ResourceServlet} object is registered as a
 * {@code javax.servlet.Servlet} service along with the
 * {@link HttpConstants#HTTP_WHITEBOARD_SERVLET_NAME},
 * {@link HttpConstants#HTTP_WHITEBOARD_SERVLET_PATTERN},
 * {@link HttpConstants#HTTP_WHITEBOARD_RESOURCE_PREFIX}, and
 * {@link HttpConstants#HTTP_WHITEBOARD_CONTEXT_NAME} service properties.
 * 
 * @author $Id: a0e9d3133f801e09e9acff0a675a66f4436b0064 $
 * @since 1.3
 * @ThreadSafe
 */
public final class ResourceServlet extends GenericServlet {

	private static final long	serialVersionUID	= 1L;

	/**
	 * ResourceServlet constructor.
	 * 
	 * <p>
	 * This method implementation is empty.
	 */
	public ResourceServlet() {
		// empty
	}

	/**
	 * ResourceServlet service method.
	 * 
	 * <p>
	 * This method implementation is empty. This method is not called or used by
	 * Http Service.
	 * 
	 * @param request The HTTP request.
	 * @param response The HTTP response.
	 */
	public void service(ServletRequest request, ServletResponse response) {
		// empty
	}
}
