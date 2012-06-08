package com.liferay.portal.oauth.manager;

import com.liferay.portal.NoSuchRoleException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.RoleConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.RoleLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Igor Beslic
 */
public class DefaultPortalOAuthManager implements PortalOAuthManager {

	public void deployDefaultRoles() throws Exception {
		List<Company> companies = CompanyLocalServiceUtil.getCompanies();

		for (Company company : companies) {
			deployDefaultRoles(company);
		}
	}

	public void deployDefaultRoles(Company company) throws Exception {
		User defaultUser = UserLocalServiceUtil.getDefaultUser(
			company.getCompanyId());

		for (Map.Entry<String, String> entry : _defaultRoles.entrySet()) {
			String name = entry.getKey();

			try {
				RoleLocalServiceUtil.getRole(company.getCompanyId(), name);
			}
			catch (NoSuchRoleException nsre) {
				Map<Locale, String> descriptionMap =
					new HashMap<Locale, String>();

				descriptionMap.put(LocaleUtil.getDefault(), entry.getValue());

				RoleLocalServiceUtil.addRole(
					defaultUser.getUserId(), company.getCompanyId(), name, null,
					descriptionMap, RoleConstants.TYPE_REGULAR);
			}
		}
	}

	public void deployKaleoDefaults() throws Exception {
		deployDefaultRoles();
	}

	public void deployKaleoDefaults(Company company) throws Exception {
		deployDefaultRoles(company);
	}

	public void setDefaultRoles(Map<String, String> defaultRoles) {
		_defaultRoles.putAll(defaultRoles);
	}

	private static Log _log = LogFactoryUtil.getLog(
		DefaultPortalOAuthManager.class);

	private Map<String, String> _defaultRoles = new HashMap<String, String>();
}