package com.liferay.portal.oauth.manager;

/**
 * @author Igor Beslic
 */
public class PortalOAuthManagerUtil {

	public static PortalOAuthManager getPortalOAuthManager() {
		return _portalOAuthManager;
	}

	public void setPortalOAuthManager(PortalOAuthManager portalOAuthManager) {
		_portalOAuthManager = portalOAuthManager;
	}

	private static PortalOAuthManager _portalOAuthManager;

}