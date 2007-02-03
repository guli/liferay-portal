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

package com.liferay.portlet.wiki.service.http;

import com.liferay.portlet.wiki.service.WikiPageServiceUtil;

import org.json.JSONObject;

/**
 * <a href="WikiPageServiceJSON.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class WikiPageServiceJSON {
	public static JSONObject addPage(java.lang.String nodeId,
		java.lang.String title)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.wiki.model.WikiPage returnValue = WikiPageServiceUtil.addPage(nodeId,
				title);

		return WikiPageJSONSerializer.toJSONObject(returnValue);
	}

	public static void deletePage(java.lang.String nodeId,
		java.lang.String title)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		WikiPageServiceUtil.deletePage(nodeId, title);
	}

	public static JSONObject getPage(java.lang.String nodeId,
		java.lang.String title)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.wiki.model.WikiPage returnValue = WikiPageServiceUtil.getPage(nodeId,
				title);

		return WikiPageJSONSerializer.toJSONObject(returnValue);
	}

	public static JSONObject getPage(java.lang.String nodeId,
		java.lang.String title, double version)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.wiki.model.WikiPage returnValue = WikiPageServiceUtil.getPage(nodeId,
				title, version);

		return WikiPageJSONSerializer.toJSONObject(returnValue);
	}

	public static JSONObject revertPage(java.lang.String nodeId,
		java.lang.String title, double version)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.wiki.model.WikiPage returnValue = WikiPageServiceUtil.revertPage(nodeId,
				title, version);

		return WikiPageJSONSerializer.toJSONObject(returnValue);
	}

	public static JSONObject updatePage(java.lang.String nodeId,
		java.lang.String title, java.lang.String content,
		java.lang.String format)
		throws com.liferay.portal.SystemException, 
			com.liferay.portal.PortalException, java.rmi.RemoteException {
		com.liferay.portlet.wiki.model.WikiPage returnValue = WikiPageServiceUtil.updatePage(nodeId,
				title, content, format);

		return WikiPageJSONSerializer.toJSONObject(returnValue);
	}
}