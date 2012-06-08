package com.liferay.portal.oauth.manager;

import com.liferay.portal.model.Company;

/**
 * @author Igor Beslic
 */
public interface PortalOAuthManager {

	public void deployDefaultRoles() throws Exception;

	public void deployDefaultRoles(Company company) throws Exception;

}