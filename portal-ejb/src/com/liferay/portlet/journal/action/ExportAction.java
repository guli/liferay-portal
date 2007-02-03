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

package com.liferay.portlet.journal.action;

import com.liferay.portal.NoSuchPortletPreferencesException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Image;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.model.impl.LayoutImpl;
import com.liferay.portal.service.ImageLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portal.service.persistence.PortletPreferencesPK;
import com.liferay.portal.util.Constants;
import com.liferay.portal.util.comparator.LayoutComparator;
import com.liferay.portlet.PortletPreferencesImpl;
import com.liferay.portlet.PortletPreferencesSerializer;
import com.liferay.portlet.admin.util.OmniadminUtil;
import com.liferay.portlet.imagegallery.model.IGFolder;
import com.liferay.portlet.imagegallery.model.IGImage;
import com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil;
import com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalContentSearch;
import com.liferay.portlet.journal.model.JournalStructure;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.model.impl.JournalArticleImpl;
import com.liferay.portlet.journal.model.impl.JournalContentSearchImpl;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalStructureLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;
import com.liferay.util.GetterUtil;
import com.liferay.util.ParamUtil;
import com.liferay.util.StringUtil;
import com.liferay.util.Time;
import com.liferay.util.Validator;
import com.liferay.util.servlet.ServletResponseUtil;
import com.liferay.util.zip.ZipWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="ExportAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ExportAction extends Action {

	public static final String COMPANY_ID = "liferay.com";

	public static final long DEFAULT_SITE_GROUP_ID = 1;

	public static final long  DEFAULT_CMS_GROUP_ID = 1;

	public ActionForward execute(
			ActionMapping mapping, ActionForm form, HttpServletRequest req,
			HttpServletResponse res)
		throws Exception {

		try {
			if (OmniadminUtil.isOmniadmin(req.getRemoteUser())) {
				long siteGroupId = ParamUtil.getLong(
					req, "siteGroupId", DEFAULT_SITE_GROUP_ID);

				long cmsGroupId = ParamUtil.getLong(
					req, "cmsGroupId", DEFAULT_CMS_GROUP_ID);

				ZipWriter zipWriter = new ZipWriter();

				List journalContentSearches = new ArrayList();

				insertDataCMSLayout(
					siteGroupId, zipWriter, journalContentSearches);
				insertDataCMSContent(
					cmsGroupId, zipWriter, journalContentSearches);
				insertDataImage(zipWriter);

				String fileName = "journal.zip";

				ServletResponseUtil.sendFile(res, fileName, zipWriter.finish());
			}

			return null;
		}
		catch (Exception e) {
			req.setAttribute(PageContext.EXCEPTION, e);

			return mapping.findForward(Constants.COMMON_ERROR);
		}
	}

	protected void addColumn(StringBuffer sb, boolean value) {
		//sb.append("'");

		if (value) {
			sb.append("TRUE");
		}
		else {
			sb.append("FALSE");
		}

		//sb.append("', ");
		sb.append(", ");
	}

	protected void addColumn(StringBuffer sb, double value) {
		sb.append(value);
		sb.append(", ");
	}

	protected void addColumn(StringBuffer sb, float value) {
		sb.append(value);
		sb.append(", ");
	}

	protected void addColumn(StringBuffer sb, int value) {
		sb.append(value);
		sb.append(", ");
	}

	protected void addColumn(StringBuffer sb, long value) {
		sb.append(value);
		sb.append(", ");
	}

	protected void addColumn(StringBuffer sb, short value) {
		sb.append(value);
		sb.append(", ");
	}

	protected void addColumn(StringBuffer sb, Date value) {
		addColumn(sb, value, true);
	}

	protected void addColumn(StringBuffer sb, Date value, boolean current) {
		if (current) {
			sb.append("CURRENT_TIMESTAMP, ");
		}
		else {
			sb.append("SPECIFIC_TIMESTAMP_");
			sb.append(Time.getSimpleDate(value, "yyyyMMddHHmmss"));
			sb.append(", ");
		}
	}

	protected void addColumn(StringBuffer sb, String value) {
		addColumn(sb, value, true);
	}

	protected void addColumn(StringBuffer sb, String value, boolean format) {
		if (format) {
			value = StringUtil.replace(
				value,
				new String[] {"\\", "'", "\"", "\n", "\r"},
				new String[] {"\\\\", "\\'", "\\\"", "\\n", "\\r"});
		}

		value = GetterUtil.getString(value);

		sb.append("'");
		sb.append(value);
		sb.append("', ");
	}

	protected void insertDataCMSContent(
			long cmsGroupId, ZipWriter zipWriter, List journalContentSearches)
		throws Exception {

		StringBuffer sb = new StringBuffer();

		List igImages = new ArrayList();

		Iterator itr = IGFolderLocalServiceUtil.getFolders(
			cmsGroupId).iterator();

		while (itr.hasNext()) {
			IGFolder folder = (IGFolder)itr.next();

			sb.append("insert into IGFolder (");
			sb.append("folderId, groupId, companyId, userId, createDate, ");
			sb.append("modifiedDate, parentFolderId, name");
			sb.append(") values (");
			addColumn(sb, folder.getFolderId());
			addColumn(sb, folder.getGroupId());
			addColumn(sb, folder.getCompanyId());
			addColumn(sb, folder.getUserId());
			addColumn(sb, folder.getCreateDate());
			addColumn(sb, folder.getModifiedDate());
			addColumn(sb, folder.getParentFolderId());
			addColumn(sb, folder.getName());
			removeTrailingComma(sb);
			sb.append(");\n");

			igImages.addAll(
				IGImageLocalServiceUtil.getImages(folder.getFolderId()));
		}

		//sb.append("\n");

		Collections.sort(igImages);

		itr = igImages.iterator();

		while (itr.hasNext()) {
			IGImage image = (IGImage)itr.next();

			sb.append("insert into IGImage (");
			sb.append("imageId, companyId, userId, createDate, modifiedDate, ");
			sb.append("folderId, description, height, width, size_");
			sb.append(") values (");
			addColumn(sb, image.getImageId());
			addColumn(sb, image.getCompanyId());
			addColumn(sb, image.getUserId());
			addColumn(sb, image.getCreateDate());
			addColumn(sb, image.getModifiedDate());
			addColumn(sb, image.getFolderId());
			addColumn(sb, image.getDescription());
			addColumn(sb, image.getHeight());
			addColumn(sb, image.getWidth());
			addColumn(sb, image.getSize());
			removeTrailingComma(sb);
			sb.append(");\n");
		}

		//sb.append("\n");

		itr = JournalArticleLocalServiceUtil.getArticles(
			cmsGroupId).iterator();

		while (itr.hasNext()) {
			JournalArticle article = (JournalArticle)itr.next();

			if (article.isApproved() &&
				JournalArticleLocalServiceUtil.isLatestVersion(
					article.getCompanyId(), article.getGroupId(),
					article.getArticleId(), article.getVersion())) {

				sb.append("insert into JournalArticle (");
				sb.append("companyId, groupId, articleId, version, userId, ");
				sb.append("userName, createDate, modifiedDate, title, ");
				sb.append("description, content, type_, structureId, ");
				sb.append("templateId, displayDate, approved, ");
				sb.append("approvedByUserId, approvedByUserName, expired");
				sb.append(") values (");
				addColumn(sb, article.getCompanyId());
				addColumn(sb, article.getGroupId());
				addColumn(sb, article.getArticleId());
				addColumn(sb, JournalArticleImpl.DEFAULT_VERSION);
				//addColumn(sb, article.getUserId());
				//addColumn(sb, article.getUserName());
				addColumn(sb, "liferay.com.1");
				addColumn(sb, "Joe Bloggs");
				addColumn(sb, article.getCreateDate());
				addColumn(sb, article.getModifiedDate());
				addColumn(sb, article.getTitle());
				addColumn(sb, article.getDescription());
				addColumn(sb, article.getContent());
				addColumn(sb, article.getType());
				addColumn(sb, article.getStructureId());
				addColumn(sb, article.getTemplateId());
				addColumn(sb, article.getDisplayDate(), false);
				addColumn(sb, article.getApproved());
				//addColumn(sb, article.getApprovedByUserId());
				//addColumn(sb, article.getApprovedByUserName());
				addColumn(sb, "liferay.com.1");
				addColumn(sb, "Joe Bloggs");
				addColumn(sb, article.getExpired());
				//addColumn(sb, article.getExpirationDate());
				//addColumn(sb, article.getReviewDate());
				removeTrailingComma(sb);
				sb.append(");\n");
			}
		}

		sb.append("\n");

		itr = journalContentSearches.iterator();

		while (itr.hasNext()) {
			JournalContentSearch contentSearch =
				(JournalContentSearch)itr.next();

			sb.append("insert into JournalContentSearch (");
			sb.append("portletId, layoutId, ownerId, articleId, companyId, ");
			sb.append("groupId");
			sb.append(") values (");
			addColumn(sb, contentSearch.getPortletId());
			addColumn(sb, contentSearch.getLayoutId());
			addColumn(sb, contentSearch.getOwnerId());
			addColumn(sb, contentSearch.getArticleId());
			addColumn(sb, contentSearch.getCompanyId());
			addColumn(sb, contentSearch.getGroupId());

			removeTrailingComma(sb);
			sb.append(");\n");
		}

		sb.append("\n");

		itr = JournalStructureLocalServiceUtil.getStructures(
			cmsGroupId).iterator();

		while (itr.hasNext()) {
			JournalStructure structure = (JournalStructure)itr.next();

			sb.append("insert into JournalStructure (");
			sb.append("companyId, structureId, groupId, userId, userName, ");
			sb.append("createDate, modifiedDate, name, description, xsd");
			sb.append(") values (");
			addColumn(sb, structure.getCompanyId());
			addColumn(sb, structure.getStructureId());
			addColumn(sb, structure.getGroupId());
			addColumn(sb, structure.getUserId());
			addColumn(sb, structure.getUserName());
			addColumn(sb, structure.getCreateDate());
			addColumn(sb, structure.getModifiedDate());
			addColumn(sb, structure.getName());
			addColumn(sb, structure.getDescription());
			addColumn(sb, structure.getXsd());
			removeTrailingComma(sb);
			sb.append(");\n");
		}

		sb.append("\n");

		itr = JournalTemplateLocalServiceUtil.getTemplates(
			cmsGroupId).iterator();

		while (itr.hasNext()) {
			JournalTemplate template = (JournalTemplate)itr.next();

			sb.append("insert into JournalTemplate (");
			sb.append("companyId, templateId, groupId, userId, userName, ");
			sb.append("createDate, modifiedDate, structureId, name, ");
			sb.append("description, xsl, langType, smallImage, smallImageURL");
			sb.append(") values (");
			addColumn(sb, template.getCompanyId());
			addColumn(sb, template.getTemplateId());
			addColumn(sb, template.getGroupId());
			addColumn(sb, template.getUserId());
			addColumn(sb, template.getUserName());
			addColumn(sb, template.getCreateDate());
			addColumn(sb, template.getModifiedDate());
			addColumn(sb, template.getStructureId());
			addColumn(sb, template.getName());
			addColumn(sb, template.getDescription());
			addColumn(sb, template.getXsl());
			addColumn(sb, template.getLangType());
			addColumn(sb, template.getSmallImage());
			addColumn(sb, template.getSmallImageURL());
			removeTrailingComma(sb);
			sb.append(");\n");
		}

		removeTrailingNewLine(sb);

		zipWriter.addEntry("portal-data-cms-content.sql", sb);
	}

	protected void insertDataCMSLayout(
			long siteGroupId, ZipWriter zipWriter,
			List journalContentSearches)
		throws Exception {

		StringBuffer sb = new StringBuffer();

		List layouts = LayoutLocalServiceUtil.getLayouts(
			LayoutImpl.PUBLIC + siteGroupId);

		Collections.sort(layouts, new LayoutComparator(true));

		Iterator itr = layouts.iterator();

		while (itr.hasNext()) {
			Layout layout = (Layout)itr.next();

			sb.append("insert into Layout (");
			sb.append("layoutId, ownerId, companyId, parentLayoutId, name, ");
			sb.append("title, type_, typeSettings, hidden_, friendlyURL, ");
			sb.append("themeId, colorSchemeId, priority");
			sb.append(") values (");
			addColumn(sb, layout.getLayoutId());
			addColumn(sb, layout.getOwnerId());
			addColumn(sb, layout.getCompanyId());
			addColumn(sb, layout.getParentLayoutId());
			addColumn(sb, layout.getName());
			addColumn(sb, layout.getTitle());
			addColumn(sb, layout.getType());
			addColumn(sb, layout.getTypeSettings());
			addColumn(sb, layout.getHidden());
			addColumn(sb, layout.getFriendlyURL());
			addColumn(sb, layout.getThemeId());
			addColumn(sb, layout.getColorSchemeId());
			addColumn(sb, layout.getPriority());
			removeTrailingComma(sb);
			sb.append(");\n");
		}

		sb.append("\n");

		itr = layouts.iterator();

		while (itr.hasNext()) {
			Layout layout = (Layout)itr.next();

			LayoutTypePortlet layoutType =
				(LayoutTypePortlet)layout.getLayoutType();

			List portletIds = layoutType.getPortletIds();

			Collections.sort(portletIds);

			for (int i = 0; i < portletIds.size(); i++) {
				String portletId = (String)portletIds.get(i);

				PortletPreferencesPK pk = new PortletPreferencesPK(
					portletId, layout.getLayoutId(), layout.getOwnerId());

				try {
					PortletPreferences portletPreferences =
						PortletPreferencesLocalServiceUtil.
							getPortletPreferences(pk);

					String prefsXml = portletPreferences.getPreferences();

					PortletPreferencesImpl prefs = (PortletPreferencesImpl)
						PortletPreferencesSerializer.fromDefaultXML(
							portletPreferences.getPreferences());

					String articleId =
						prefs.getValue("article-id", StringPool.BLANK);

					articleId = articleId.toUpperCase();

					if (Validator.isNotNull(articleId)) {

						// Make sure article id is upper case in the preferences
						// XML

						prefs.setValue("article-id", articleId);

						prefsXml = PortletPreferencesSerializer.toXML(prefs);

						// Add to the journal content search list

						JournalContentSearch journalContentSearch =
							new JournalContentSearchImpl();

						journalContentSearch.setPortletId(portletId);
						journalContentSearch.setLayoutId(layout.getLayoutId());
						journalContentSearch.setOwnerId(layout.getOwnerId());
						journalContentSearch.setArticleId(articleId);
						journalContentSearch.setCompanyId(
							layout.getCompanyId());
						journalContentSearch.setGroupId(layout.getGroupId());

						journalContentSearches.add(journalContentSearch);
					}

					sb.append("insert into PortletPreferences (");
					sb.append("portletId, layoutId, ownerId, preferences");
					sb.append(") values (");
					addColumn(sb, portletId);
					addColumn(sb, portletPreferences.getLayoutId());
					addColumn(sb, portletPreferences.getOwnerId());
					addColumn(sb, prefsXml);
					removeTrailingComma(sb);
					sb.append(");\n");
				}
				catch (NoSuchPortletPreferencesException nsppe) {
					_log.warn(nsppe.getMessage());
				}
			}

			sb.append("\n");
		}

		removeTrailingNewLine(sb);
		removeTrailingNewLine(sb);

		zipWriter.addEntry("portal-data-cms-layout.sql", sb);
	}

	protected void insertDataImage(ZipWriter zipWriter) throws Exception {
		StringBuffer sb = new StringBuffer();

		Iterator itr = ImageLocalServiceUtil.search(
			COMPANY_ID + "%").iterator();

		while (itr.hasNext()) {
			Image image = (Image)itr.next();

			String imageId = image.getImageId();

			boolean insert = true;

			if (imageId.startsWith("liferay.com.")) {
				String suffix = StringUtil.replace(imageId, "liferay.com.", "");

				if (Validator.isNumber(suffix)) {
					insert = false;
				}
			}

			if (imageId.indexOf(".image_gallery.") != -1) {
				insert = false;
			}

			if ((imageId.indexOf(".shopping.item.") != -1) /*&&
					imageId.endsWith(".large")*/) {

				insert = false;
			}

			if (insert) {
				sb.append("insert into Image (");
				sb.append("imageId, modifiedDate, text_, type_");
				sb.append(") values (");
				addColumn(sb, image.getImageId());
				addColumn(sb, image.getModifiedDate());
				addColumn(sb, image.getText(), false);
				addColumn(sb, image.getType());
				removeTrailingComma(sb);
				sb.append(");\n");
			}
		}

		removeTrailingNewLine(sb);

		zipWriter.addEntry("portal-data-image.sql", sb);
	}

	protected void removeTrailingComma(StringBuffer sb) {
		sb.delete(sb.length() - 2, sb.length());
	}

	protected void removeTrailingNewLine(StringBuffer sb) {
		if (sb.length() > 0) {
			sb.delete(sb.length() - 1, sb.length());
		}
	}

	private static Log _log = LogFactory.getLog(ExportAction.class);

}