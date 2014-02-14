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

import org.osgi.framework.Filter;
import org.osgi.service.http.runtime.HttpServiceRuntime;

/**
 * Defines standard names for Http Service constants.
 * 
 * @author $Id: 090036680102766f5ae9ac805619b2ac3be45c84 $
 * @since 1.3
 */
public final class HttpConstants {
	private HttpConstants() {
		// non-instantiable
	}

	/**
	 * Service property specifying the name of an {@link HttpContext} service.
	 * 
	 * <p>
	 * For {@link HttpContext} services, this service property must be
	 * specified. Http Context services without this service property must be
	 * ignored.
	 * 
	 * <p>
	 * For servlet, listener, servlet filter, or resource servlet services, this
	 * service property refers to the name of the associated Http Context
	 * service. If this service property is not specified, then the default Http
	 * Conext must be used. If there is no Http Context service matching the
	 * specified name or the matching Http Context service is registered by
	 * another bundle but does not have the
	 * {@link #HTTP_WHITEBOARD_CONTEXT_SHARED} service property set to true, the
	 * servlet, listener, servlet filter, or resource servlet service must be
	 * ignored.
	 * 
	 * <p>
	 * For {@link HttpContext} services, the value of this service property must
	 * be of type {@code String}, {@code String[]}, or
	 * {@code Collection<String>}. For servlet, listener, servlet filter, or
	 * resource servlet services, the value of this service property must be of
	 * type {@code String}
	 * 
	 * @see #HTTP_WHITEBOARD_CONTEXT_SHARED
	 */
	public static final String	HTTP_WHITEBOARD_CONTEXT_NAME			= "osgi.http.whiteboard.context.name";

	/**
	 * Service property specifying whether an {@link HttpContext} service can be
	 * used by bundles other than the bundle which registered the Http Context
	 * service.
	 * 
	 * <p>
	 * By default Http Context services can only be used by the bundle which
	 * registered the Http Context service.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code Boolean}.
	 * 
	 * @see #HTTP_WHITEBOARD_CONTEXT_NAME
	 */
	public static final String	HTTP_WHITEBOARD_CONTEXT_SHARED			= "osgi.http.whiteboard.context.shared";

	/**
	 * Service property specifying the servlet name of a {@code Servlet}
	 * service.
	 * 
	 * <p>
	 * This name is used as the value for the
	 * {@code ServletConfig.getServletName()} method. If this service property
	 * is not specified, the fully qualified name of the service object's class
	 * is used as the servlet name. Filter services may refer to servlets by
	 * this name in their {@link #HTTP_WHITEBOARD_FILTER_SERVLET} service
	 * property to apply the filter to the servlet.
	 * 
	 * <p>
	 * Servlet names must be unique among all servlet services associated with
	 * an {@link HttpContext}. If multiple servlet services associated with the
	 * same HttpContext have the same servlet name, then all but the highest
	 * ranked servlet service must be ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String}.
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_NAME			= "osgi.http.whiteboard.servlet.name";

	/**
	 * Service property specifying the request mappings for a {@code Servlet}
	 * service.
	 * 
	 * <p>
	 * The specified patterns are used to determine whether a request should be
	 * mapped to the servlet. Servlet services without this service property or
	 * {@link #HTTP_WHITEBOARD_SERVLET_ERROR_PAGE} must be ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 12.2 Specification of Mappings"
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_PATTERN			= "osgi.http.whiteboard.servlet.pattern";

	/**
	 * Service property specifying whether a {@code Servlet} service acts as an
	 * error page.
	 * 
	 * <p>
	 * The service property values may be the name of a fully qualified
	 * exception class or a three digit HTTP status code. Any value that is not
	 * a three digit number is considered to be the name of a fully qualified
	 * exception class.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_ERROR_PAGE		= "osgi.http.whiteboard.servlet.errorPage";

	/**
	 * Service property specifying whether a {@code Servlet} service supports
	 * asynchronous processing.
	 * 
	 * <p>
	 * By default Servlet services do not support asynchronous processing.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code Boolean}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 2.3.3.3 Asynchronous Processing"
	 */
	public static final String	HTTP_WHITEBOARD_SERVLET_ASYNC_SUPPORTED	= "osgi.http.whiteboard.servlet.asyncSupported";

	/**
	 * Service property specifying the servlet filter name of a {@code Filter}
	 * service.
	 * 
	 * <p>
	 * This name is used as the value for the
	 * {@code FilterConfig.getFilterName()} method. If this service property is
	 * not specified, the fully qualified name of the service object's class is
	 * used as the servlet filter name.
	 * 
	 * <p>
	 * Servlet filter names must be unique among all servlet filter services
	 * associated with an {@link HttpContext}. If multiple servlet filter
	 * services associated with the same HttpContext have the same servlet
	 * filter name, then all but the highest ranked servlet filter service must
	 * be ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String}.
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_NAME				= "osgi.http.whiteboard.filter.name";

	/**
	 * Service property specifying the request mappings for a {@code Filter}
	 * service.
	 * 
	 * <p>
	 * The specified patterns are used to determine whether a request should be
	 * mapped to the servlet filter. Filter services without this service
	 * property or the {@link #HTTP_WHITEBOARD_FILTER_SERVLET} service property
	 * must be ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 12.2 Specification of Mappings"
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_PATTERN			= "osgi.http.whiteboard.filter.pattern";

	/**
	 * Service property specifying the {@link #HTTP_WHITEBOARD_SERVLET_NAME
	 * servlet names} for a {@code Filter} service.
	 * 
	 * <p>
	 * The specified names are used to determine the servlets whose requests
	 * should be mapped to the servlet filter. Filter services without this
	 * service property or the {@link #HTTP_WHITEBOARD_FILTER_PATTERN} service
	 * property must be ignored.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_SERVLET			= "osgi.http.whiteboard.filter.servlet";

	/**
	 * Service property specifying whether a {@code Filter} service supports
	 * asynchronous processing.
	 * 
	 * <p>
	 * By default Filters services do not support asynchronous processing.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code Boolean}.
	 * 
	 * @see "Java Servlet Specification Version 3.0, Section 2.3.3.3 Asynchronous Processing"
	 */
	public static final String	HTTP_WHITEBOARD_FILTER_ASYNC_SUPPORTED	= "osgi.http.whiteboard.filter.asyncSupported";

	/**
	 * Service property specifying the resource entry prefix for a
	 * {@link ResourceServlet} servlet service.
	 * 
	 * <p>
	 * This prefix is used to map a requested resource to the bundle's entries.
	 * If this service property is not specified, a prefix of the empty string
	 * is used.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String}.
	 */
	public static final String	HTTP_WHITEBOARD_RESOURCE_PREFIX			= "osgi.http.whiteboard.resource.prefix";

	/**
	 * Service property specifying the target filter to select the Http Service
	 * runtime to process the service.
	 * 
	 * <p>
	 * An Http Service implementation can define any number of
	 * {@link HttpServiceRuntime#getAttributes() attributes} which can be
	 * referenced by the target filter. The attributes should always include the
	 * {@link #HTTP_SERVICE_ENDPOINT_ATTRIBUTE osgi.http.endpoint} attribute if
	 * the endpoint information is known.
	 * 
	 * <p>
	 * If this service property is not specified, then all Http Service runtimes
	 * can process the service.
	 * 
	 * <p>
	 * The value of this service property must be of type {@code String} and be
	 * a valid {@link Filter filter string}.
	 */
	public static final String	HTTP_WHITEBOARD_TARGET					= "osgi.http.whiteboard.target";

	/**
	 * Http Service runtime attribute specifying the endpoints upon which the
	 * Http Service runtime is listening.
	 * 
	 * <p>
	 * An endpoint value is a URL to which the Http Service runtime is
	 * listening. For example, {@code http://192.168.1.10:8080/}. The relevant
	 * information contained in the URL is the scheme, IP Address of the bound
	 * interface, bound port, and the (optional) context path in a Servlet API
	 * servlet container for the Http Service runtime. An Http Service Runtime
	 * can be listening on multiple endpoints.
	 * 
	 * <p>
	 * The value of this attribute must be of type {@code String},
	 * {@code String[]}, or {@code Collection<String>}.
	 */
	public static final String	HTTP_SERVICE_ENDPOINT_ATTRIBUTE			= "osgi.http.endpoint";
}
