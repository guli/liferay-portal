/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.portlet.softwarerepository.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.impl.PrincipalBean;
import com.liferay.portlet.softwarerepository.model.SRFrameworkVersion;
import com.liferay.portlet.softwarerepository.service.SRFrameworkVersionLocalServiceUtil;
import com.liferay.portlet.softwarerepository.service.SRFrameworkVersionService;
import com.liferay.portlet.softwarerepository.service.permission.SRFrameworkVersionPermission;

/**
 * <a href="SRFrameworkVersionServiceImpl.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Jorge Ferrer
 * @author Brian Wing Shun Chan
 *
 */
public class SRFrameworkVersionServiceImpl
	extends PrincipalBean implements SRFrameworkVersionService {

	public SRFrameworkVersion addFrameworkVersion(
			String plid, String name, String url, boolean active, int priority,
			boolean addCommunityPermissions, boolean addGuestPermissions)
		throws PortalException, SystemException {

		SRFrameworkVersionPermission.check(
			getPermissionChecker(), plid, ActionKeys.ADD_FRAMEWORK_VERSION);

		return SRFrameworkVersionLocalServiceUtil.addFrameworkVersion(
			getUserId(), plid, name, url, active, priority,
			addCommunityPermissions, addGuestPermissions);
	}

	public SRFrameworkVersion addFrameworkVersion(
			String plid, String name, String url, boolean active, int priority,
			String[] communityPermissions, String[] guestPermissions)
		throws PortalException, SystemException {

		SRFrameworkVersionPermission.check(
			getPermissionChecker(), plid, ActionKeys.ADD_FRAMEWORK_VERSION);

		return SRFrameworkVersionLocalServiceUtil.addFrameworkVersion(
			getUserId(), plid, name, url, active, priority,
			communityPermissions, guestPermissions);
	}

	public void deleteFrameworkVersion(long frameworkVersionId)
		throws PortalException, SystemException {

		SRFrameworkVersionPermission.check(
			getPermissionChecker(), frameworkVersionId, ActionKeys.DELETE);

		SRFrameworkVersionLocalServiceUtil.deleteFrameworkVersion(
			frameworkVersionId);
	}

	public SRFrameworkVersion getFrameworkVersion(long frameworkVersionId)
		throws PortalException, SystemException {

		SRFrameworkVersionPermission.check(
			getPermissionChecker(), frameworkVersionId, ActionKeys.VIEW);

		return SRFrameworkVersionLocalServiceUtil.getFrameworkVersion(
			frameworkVersionId);
	}

	public SRFrameworkVersion updateFrameworkVersion(
			long frameworkVersionId, String name, String url, boolean active,
			int priority)
		throws PortalException, SystemException {

		SRFrameworkVersionPermission.check(
			getPermissionChecker(), frameworkVersionId, ActionKeys.UPDATE);

		return SRFrameworkVersionLocalServiceUtil.updateFrameworkVersion(
			frameworkVersionId, name, url, active, priority);
	}

}