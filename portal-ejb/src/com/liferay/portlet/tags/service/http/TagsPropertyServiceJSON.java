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

package com.liferay.portlet.tags.service.http;

import com.liferay.portlet.tags.service.TagsPropertyServiceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <a href="TagsPropertyServiceJSON.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TagsPropertyServiceJSON {
	public static JSONObject addProperty(long entryId, java.lang.String key,
		java.lang.String value)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.tags.model.TagsProperty returnValue = TagsPropertyServiceUtil.addProperty(entryId,
				key, value);

		return TagsPropertyJSONSerializer.toJSONObject(returnValue);
	}

	public static JSONObject addProperty(java.lang.String userId,
		java.lang.String entryName, java.lang.String key, java.lang.String value)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.tags.model.TagsProperty returnValue = TagsPropertyServiceUtil.addProperty(userId,
				entryName, key, value);

		return TagsPropertyJSONSerializer.toJSONObject(returnValue);
	}

	public static void deleteProperty(long propertyId)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		TagsPropertyServiceUtil.deleteProperty(propertyId);
	}

	public static JSONArray getProperties(long entryId)
		throws com.liferay.portal.SystemException, java.rmi.RemoteException {
		java.util.List returnValue = TagsPropertyServiceUtil.getProperties(entryId);

		return TagsPropertyJSONSerializer.toJSONArray(returnValue);
	}

	public static JSONArray getPropertyValues(java.lang.String companyId,
		java.lang.String key)
		throws com.liferay.portal.SystemException, java.rmi.RemoteException {
		java.util.List returnValue = TagsPropertyServiceUtil.getPropertyValues(companyId,
				key);

		return TagsPropertyJSONSerializer.toJSONArray(returnValue);
	}

	public static JSONObject updateProperty(long propertyId,
		java.lang.String key, java.lang.String value)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.tags.model.TagsProperty returnValue = TagsPropertyServiceUtil.updateProperty(propertyId,
				key, value);

		return TagsPropertyJSONSerializer.toJSONObject(returnValue);
	}
}