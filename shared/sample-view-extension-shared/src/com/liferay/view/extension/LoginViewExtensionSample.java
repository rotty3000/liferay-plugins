/**
 * Copyright (c) 2000-2014 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.view.extension;

import com.liferay.taglib.util.PortletViewExtension;
import org.osgi.service.component.annotations.Component;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;
import java.io.IOException;

/**
 * @author Carlos Sierra Andrés
 */
@Component(property = {"extension-id=login-form-after-password"})
public class LoginViewExtensionSample implements PortletViewExtension {

	@Override
	public void render(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException {

		PortletURL actionURL = renderResponse.createActionURL();
		try {
			actionURL.setWindowState(WindowState.MAXIMIZED);
		} catch (WindowStateException e) {
			e.printStackTrace();
		}
		actionURL.setParameter("extension", "true");

		renderResponse.getWriter().write("<a href=\""+actionURL.toString()+"\">extension link</a>");


	}

}
