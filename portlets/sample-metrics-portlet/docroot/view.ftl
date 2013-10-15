<#--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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
-->

<#include "init.ftl" />

<div class="portlet" id="portlet_${plid}">
	<div class="portlet-topper">
		<h1 class="portlet-title">
			<span class="portlet-title-text">${portlet_display.getTitle()}</span>
		</h1>

		<menu class="portlet-topper-toolbar" id="portlet-topper-toolbar_${plid}" type="toolbar">
			<#if portlet_display.isShowBackIcon()>
				<a href="${portlet_back_url}" class="portlet-icon-back"><@liferay.language key="return-to-full-page" /></a>
			<#else>
				<#--${theme.portletIconOptions()}
				${theme.portletIconMinimize()}
				${theme.portletIconMaximize()}
				${theme.portletIconClose()}-->
			</#if>

		</menu>
	</div>

	<div class="portlet-content">
		<div id="chart" style="width:600px;height:600px">
		</div>
	</div>
</div>

<#include "script.ftl" />
