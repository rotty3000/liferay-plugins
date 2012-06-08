package com.liferay.portal.oauth.hook.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Company;
import com.liferay.portal.oauth.manager.PortalOAuthManager;
import com.liferay.portal.oauth.manager.PortalOAuthManagerUtil;
import com.liferay.portal.service.CompanyLocalService;
import com.liferay.portal.service.CompanyLocalServiceWrapper;

/**
 * @author Igor Beslic
 */
public class CompanyLocalServiceImpl extends CompanyLocalServiceWrapper {

	public CompanyLocalServiceImpl(CompanyLocalService companyLocalService) {
		super(companyLocalService);
	}

	@Override
	public Company checkCompany(String webId, String mx, String shardName)
		throws PortalException, SystemException {

		Company company = super.checkCompany(webId, mx, shardName);

		try {
			PortalOAuthManager portalOAuthManager =
				PortalOAuthManagerUtil.getPortalOAuthManager();

			portalOAuthManager.deployDefaultRoles(company);
		}
		catch (PortalException pe) {
			throw pe;
		}
		catch (SystemException se) {
			throw se;
		}
		catch (Exception e) {
			throw new SystemException(e);
		}

		return company;
	}

}