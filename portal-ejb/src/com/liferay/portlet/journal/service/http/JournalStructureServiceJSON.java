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

package com.liferay.portlet.journal.service.http;

import com.liferay.portlet.journal.service.JournalStructureServiceUtil;

import org.json.JSONObject;

/**
 * <a href="JournalStructureServiceJSON.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalStructureServiceJSON {
	public static JSONObject addStructure(java.lang.String structureId,
		boolean autoStructureId, java.lang.String plid, java.lang.String name,
		java.lang.String description, java.lang.String xsd,
		boolean addCommunityPermissions, boolean addGuestPermissions)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.journal.model.JournalStructure returnValue = JournalStructureServiceUtil.addStructure(structureId,
				autoStructureId, plid, name, description, xsd,
				addCommunityPermissions, addGuestPermissions);

		return JournalStructureJSONSerializer.toJSONObject(returnValue);
	}

	public static JSONObject addStructure(java.lang.String structureId,
		boolean autoStructureId, java.lang.String plid, java.lang.String name,
		java.lang.String description, java.lang.String xsd,
		java.lang.String[] communityPermissions,
		java.lang.String[] guestPermissions)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.journal.model.JournalStructure returnValue = JournalStructureServiceUtil.addStructure(structureId,
				autoStructureId, plid, name, description, xsd,
				communityPermissions, guestPermissions);

		return JournalStructureJSONSerializer.toJSONObject(returnValue);
	}

	public static void deleteStructure(java.lang.String companyId,
		long groupId, java.lang.String structureId)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		JournalStructureServiceUtil.deleteStructure(companyId, groupId,
			structureId);
	}

	public static JSONObject getStructure(java.lang.String companyId,
		long groupId, java.lang.String structureId)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.journal.model.JournalStructure returnValue = JournalStructureServiceUtil.getStructure(companyId,
				groupId, structureId);

		return JournalStructureJSONSerializer.toJSONObject(returnValue);
	}

	public static JSONObject updateStructure(long groupId,
		java.lang.String structureId, java.lang.String name,
		java.lang.String description, java.lang.String xsd)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.journal.model.JournalStructure returnValue = JournalStructureServiceUtil.updateStructure(groupId,
				structureId, name, description, xsd);

		return JournalStructureJSONSerializer.toJSONObject(returnValue);
	}
}