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

package org.osgi.service.http.runtime;

import java.util.List;
import org.osgi.dto.DTO;
import org.osgi.dto.framework.ServiceReferenceDTO;
import org.osgi.service.http.HttpConstants;

/**
 * Represents an {@code HttpContext} object used by the Http Service runtime.
 * 
 * @NotThreadSafe
 * @author $Id$
 * @since 1.3
 */
public class HttpContextDTO extends DTO {

	/**
	 * The name of the http context.
	 * 
	 * @see HttpConstants#HTTP_WHITEBOARD_CONTEXT_NAME
	 */
	public String				name;

	/**
	 * Specifies whether the http context is shared.
	 * 
	 * @see HttpConstants#HTTP_WHITEBOARD_CONTEXT_SHARED
	 */
	public boolean				shared;

	/**
	 * The list of {@link FilterDTO} representing all the
	 * {@code Filter} currently registered by this HttpContext.
	 */
	public List<FilterDTO>			filters;

	/**
	 * The list of {@link ListenerDTO} representing all the
	 * {@code EventListener} currently registered by this HttpContext.
	 */
	public List<ListenerDTO>		listeners;

	/**
	 * The list of {@link ResourceServletDTO} representing all the resources
	 * currently registered by this HttpContext.
	 */
	public List<ResourceServletDTO>	resources;

	/**
	 * The list of {@link ServletDTO} representing all the
	 * {@code Servlet} currently registered by this HttpContext.
	 */
	public List<ServletDTO>			servlets;

	/**
	 * A reference to this service.
	 */
	public ServiceReferenceDTO	serviceReference;
}