/**
 * Copyright (c) 2000-2011 Liferay, Inc. All rights reserved.
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

package com.liferay.awsimport.portlet;

import com.liferay.aws.client.AWSECommerceService;
import com.liferay.aws.client.AWSECommerceServicePortType;
import com.liferay.aws.client.DecimalWithUnits;
import com.liferay.aws.client.Errors;
import com.liferay.aws.client.Image;
import com.liferay.aws.client.Item;
import com.liferay.aws.client.ItemAttributes;
import com.liferay.aws.client.ItemAttributes.ItemDimensions;
import com.liferay.aws.client.ItemSearchRequest;
import com.liferay.aws.client.Items;
import com.liferay.aws.client.OfferSummary;
import com.liferay.aws.client.OperationRequest;
import com.liferay.aws.client.Price;
import com.liferay.aws.client.Tag;
import com.liferay.aws.client.Tags;
import com.liferay.aws.extension.AwsHandlerResolver;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;
import com.liferay.portlet.expando.model.ExpandoBridge;
import com.liferay.portlet.expando.model.ExpandoColumnConstants;
import com.liferay.portlet.messageboards.model.MBCategory;
import com.liferay.portlet.messageboards.model.MBMessage;
import com.liferay.portlet.messageboards.model.MBMessageConstants;
import com.liferay.portlet.messageboards.service.MBCategoryLocalServiceUtil;
import com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.liferay.util.portlet.PortletProps;

import java.io.IOException;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.math.BigInteger;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;

import javax.xml.ws.Holder;

public class AWSImportPortlet extends MVCPortlet {

	public void importFromAWS(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws IOException, PortletException {

		try {
			String keywords = ParamUtil.getString(actionRequest, "keywords");
			String searchIndex = ParamUtil.getString(
				actionRequest, "searchIndex");
			String type = ParamUtil.getString(
				actionRequest, "type", BlogsEntry.class.getName());
			int pages = ParamUtil.getInteger(actionRequest, "pages", 1);

			if (pages < 1) {
				pages = 1;
			}

			if (Validator.isNull(keywords) || Validator.isNull(searchIndex)) {
				return;
			}

			ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

			AWSECommerceService service = new AWSECommerceService();
			service.setHandlerResolver(new AwsHandlerResolver(_SECRET_KEY));

			AWSECommerceServicePortType port =
				service.getAWSECommerceServicePort();

			ItemSearchRequest itemSearchRequest = new ItemSearchRequest();

			itemSearchRequest.setSearchIndex(searchIndex);
			itemSearchRequest.setKeywords(keywords);
			itemSearchRequest.getResponseGroup().add("Images");
			itemSearchRequest.getResponseGroup().add("ItemAttributes");
			itemSearchRequest.getResponseGroup().add("OfferSummary");

			List<ItemSearchRequest> request = new ArrayList<ItemSearchRequest>();
			request.add(itemSearchRequest);

			Holder<OperationRequest> operationRequestHolder =
				new Holder<OperationRequest>();

			Holder<List<Items>> itemsHolder = new Holder<List<Items>>();

			createCustomFields(themeDisplay.getCompanyId(), type);

			int page = 1;
			Errors errors = null;

			do {
				itemSearchRequest.setItemPage(
					new BigInteger(String.valueOf(page)));

				port.itemSearch(
					"", _AWS_ACCESS_KEY_ID, _SECRET_KEY, "", "",
					"2010-11-01", itemSearchRequest, request,
					operationRequestHolder, itemsHolder);

				errors = operationRequestHolder.value.getErrors();

				if ((errors != null) && !errors.getError().isEmpty()) {
					for (Errors.Error error : errors.getError()) {
						System.err.println(error.getMessage());
					}

					break;
				}

				System.out.println("[AWS IMPORT] Processing page " + page);

				System.out.println(
					"[AWS IMPORT] Result Time = " +
					operationRequestHolder.value.getRequestProcessingTime());

				Items itemList = itemsHolder.value.get(0);

				errors = itemList.getRequest().getErrors();

				if ((errors != null) && !errors.getError().isEmpty()) {
					for (Errors.Error error : errors.getError()) {
						System.err.println(error.getMessage());
					}

					break;
				}

				int size = itemList.getTotalResults().intValue();

				if (page == 1) {
					System.out.println(
						"[AWS IMPORT] Number of results: " + size);
					System.out.println(
						"[AWS IMPORT] Number of pages: " +
						itemList.getTotalPages().intValue());
				}

				for (Item item : itemList.getItem()) {
					if (type.equals(BlogsEntry.class.getName())) {
						addBlogEntry(actionRequest, item);
					}
					else if (type.equals(MBMessage.class.getName())) {
						addMBMessage(actionRequest, item);
					}
				}

				Thread.sleep(1100);

				page++;
			}
			while (page <= pages);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void addBlogEntry(ActionRequest actionRequest, Item item) {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			System.out.println(
				"[AWS IMPORT] Item: " + item.getItemAttributes().getTitle());

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				BlogsEntry.class.getName(), actionRequest);

			String title = item.getItemAttributes().getTitle();

			if (title.length() > 150) {
				title = title.substring(0, 149);
			}

			Calendar cal = Calendar.getInstance(themeDisplay.getTimeZone());

			int displayDateMonth = ParamUtil.getInteger(
				actionRequest, "displayDateMonth", cal.get(Calendar.MONTH));
			int displayDateDay = ParamUtil.getInteger(
				actionRequest, "displayDateDay", cal.get(Calendar.DAY_OF_MONTH));
			int displayDateYear = ParamUtil.getInteger(
				actionRequest, "displayDateYear", cal.get(Calendar.YEAR));
			int displayDateHour = ParamUtil.getInteger(
				actionRequest, "displayDateHour", cal.get(Calendar.HOUR));
			int displayDateMinute = ParamUtil.getInteger(
				actionRequest, "displayDateMinute", cal.get(Calendar.MINUTE));
			int displayDateAmPm = ParamUtil.getInteger(
				actionRequest, "displayDateAmPm");

			if (displayDateAmPm == Calendar.PM) {
				displayDateHour += 12;
			}

			String content = getPlainContent(item);

			Map<String,Serializable> attributes = getAttributes(item);
			String[] assetTagNames = getAssetTags(serviceContext, item);

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);
			serviceContext.setAssetTagNames(assetTagNames);
			serviceContext.setExpandoBridgeAttributes(attributes);

			BlogsEntryLocalServiceUtil.addEntry(
				themeDisplay.getUserId(), title, "", content, displayDateMonth,
				displayDateDay, displayDateYear, displayDateHour,
				displayDateMinute, false, false, null, false, null, null, null,
				serviceContext);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void addMBMessage(ActionRequest actionRequest, Item item) {
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		try {
			System.out.println(
				"[AWS IMPORT] Item: " + item.getItemAttributes().getTitle());

			ServiceContext serviceContext = ServiceContextFactory.getInstance(
				MBCategory.class.getName(), actionRequest);

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			long categoryId = getCategoryId(item, serviceContext);

			serviceContext = ServiceContextFactory.getInstance(
				MBMessage.class.getName(), actionRequest);

			serviceContext.setAddGroupPermissions(true);
			serviceContext.setAddGuestPermissions(true);

			String title = item.getItemAttributes().getTitle();

			if (title.length() > 150) {
				title = title.substring(0, 149);
			}

			String content = getPlainContent(item).trim();

			Map<String,Serializable> attributes = getAttributes(item);
			String[] assetTagNames = getAssetTags(serviceContext, item);

			serviceContext.setAssetTagNames(assetTagNames);
			serviceContext.setExpandoBridgeAttributes(attributes);

			MBMessageLocalServiceUtil.addMessage(
				themeDisplay.getUserId(), themeDisplay.getUser().getFullName(),
				themeDisplay.getScopeGroupId(), categoryId, title, content,
				MBMessageConstants.DEFAULT_FORMAT, Collections.EMPTY_LIST,
				false, 0, false, serviceContext);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createCustomFields(long companyId, String type)
		throws Exception {

		ExpandoBridge expandoBridge = null;

		if (type.equals(BlogsEntry.class.getName())) {
			expandoBridge =
				BlogsEntryLocalServiceUtil.createBlogsEntry(0).getExpandoBridge();
		}
		else if (type.equals(MBMessage.class.getName())) {
			expandoBridge =
				MBMessageLocalServiceUtil.createMBMessage(0).getExpandoBridge();
		}

		String[] fields = new String[] {
				"ASIN", "DetailPageURL", "SmallImageURL", "SmallImageHeight",
				"SmallImageWidth", "MediumImageURL", "MediumImageHeight",
				"MediumImageWidth", "LargeImageURL", "LargeImageHeight",
				"LargeImageWidth", "Binding", "Brand", "Color", "EAN",
				"Feature", "FormFactor", "IncludedSoftware",
				"ItemDimensionsHeight", "ItemDimensionsLength",
				"ItemDimensionsWeight", "ItemDimensionsWidth", "Label",
				"ListPriceAmount", "ListPriceCurrencyCode",
				"ListPriceFormattedPrice", "Manufacturer", "Model", "MPN",
				"OperatingSystem", "PackageQuantity", "ProductGroup",
				"ProductTypeName", "Publisher", "ReleaseDate", "Studio", "UPC",
				"OfferSummaryLowestNewPriceAmount",
				"OfferSummaryLowestNewPriceCurrencyCode",
				"OfferSummaryLowestNewPriceFormattedPrice",
				"OfferSummaryLowestUsedPriceAmount",
				"OfferSummaryLowestUsedPriceCurrencyCode",
				"OfferSummaryLowestUsedPriceFormattedPrice",
				"OfferSummaryLowestCollectiblePriceAmount",
				"OfferSummaryLowestCollectiblePriceCurrencyCode",
				"OfferSummaryLowestCollectiblePriceFormattedPrice",
				"OfferSummaryLowestRefurbishedPriceAmount",
				"OfferSummaryLowestRefurbishedPriceCurrencyCode",
				"OfferSummaryLowestRefurbishedPriceFormattedPrice",
				"OfferSummaryTotalNew", "OfferSummaryTotalUsed",
				"OfferSummaryTotalCollectible",
				"OfferSummaryTotalRefurbished"
		};

		for (String field : fields) {
			if (!expandoBridge.hasAttribute(field)) {
				expandoBridge.addAttribute(
					field, ExpandoColumnConstants.STRING_ARRAY);

				UnicodeProperties properties =
					expandoBridge.getAttributeProperties(field);

				properties.setProperty(
					ExpandoColumnConstants.INDEX_TYPE,
					String.valueOf(ExpandoColumnConstants.INDEX_TYPE_KEYWORD));

				expandoBridge.setAttributeProperties(field, properties);
			}
		}
	}


	private String[] getAssetTags(ServiceContext serviceContext, Item item) {
		Set<String> assetTagset = new HashSet<String>();

		ItemAttributes _itemAttributes = item.getItemAttributes();

		assetTagset.add(_itemAttributes.getBrand());
//		assetTagset.addAll(_itemAttributes.getFormFactor());
		assetTagset.add(_itemAttributes.getLabel());
		assetTagset.add(_itemAttributes.getManufacturer());
//		assetTagset.add(_itemAttributes.getOperatingSystem());
//		assetTagset.add(_itemAttributes.getProductGroup());
//		assetTagset.add(_itemAttributes.getProductTypeName());

		Tags tags = item.getTags();

		if (tags != null) {
			for (Tag tag : tags.getTag()) {
				assetTagset.add(tag.getName());
			}
		}

		Iterator<String> itr = assetTagset.iterator();
		Set<String> newAssetTagset = new HashSet<String>();

		while (itr.hasNext()) {
			String tag = itr.next();
			AssetTag assetTag = null;

			try {
				assetTag = AssetTagLocalServiceUtil.getTag(
					serviceContext.getScopeGroupId(), tag);
			}
			catch (Exception e) {
				try {
					assetTag = AssetTagLocalServiceUtil.addTag(
						serviceContext.getUserId(), tag, null, serviceContext);
				}
				catch (Exception e1) {
				}
			}

			if (assetTag != null) {
				newAssetTagset.add(assetTag.getName());
			}
		}

		return newAssetTagset.toArray(new String[newAssetTagset.size()]);
	}

	private Map<String,Serializable> getAttributes(Item item) {
		Map<String,Serializable> attributes =
			new HashMap<String,Serializable>();

		attributes.put("ASIN", item.getASIN());
		attributes.put("DetailPageURL", item.getDetailPageURL());

		Image _smallImage = item.getSmallImage();

		attributes.put("SmallImageURL", (_smallImage != null ? _smallImage.getURL() : ""));
		attributes.put("SmallImageHeight", (_smallImage != null ? formatInt(_smallImage.getHeight()) : _ZERO));
		attributes.put("SmallImageWidth", (_smallImage != null ? formatInt(_smallImage.getWidth()) : _ZERO));

		Image _mediumImage = item.getMediumImage();

		attributes.put("MediumImageURL", (_mediumImage != null ? _mediumImage.getURL() : ""));
		attributes.put("MediumImageHeight", (_mediumImage != null ? formatInt(_mediumImage.getHeight()) : _ZERO));
		attributes.put("MediumImageWidth", (_mediumImage != null ? formatInt(_mediumImage.getWidth()) : _ZERO));

		Image _largeImage = item.getLargeImage();

		attributes.put("LargeImageURL", (_largeImage != null ? _largeImage.getURL() : ""));
		attributes.put("LargeImageHeight", (_largeImage != null ? formatInt(_largeImage.getHeight()) : _ZERO));
		attributes.put("LargeImageWidth", (_largeImage != null ? formatInt(_largeImage.getWidth()) : _ZERO));

		ItemAttributes _itemAttributes = item.getItemAttributes();

		attributes.put("Binding", _itemAttributes.getBinding());
		attributes.put("Brand", _itemAttributes.getBrand());
		attributes.put("Color", _itemAttributes.getColor());
		attributes.put("EAN", _itemAttributes.getEAN());
		attributes.put("Feature", toArray(_itemAttributes.getFeature()));
		attributes.put("FormFactor", toArray(_itemAttributes.getFormFactor()));
		attributes.put("IncludedSoftware", _itemAttributes.getIncludedSoftware());

		ItemDimensions _itemDimensions = _itemAttributes.getItemDimensions();

		attributes.put("ItemDimensionsHeight", (_itemDimensions != null ? formatInt(_itemDimensions.getHeight()) : _ZERO));
		attributes.put("ItemDimensionsLength", (_itemDimensions != null ? formatInt(_itemDimensions.getLength()) : _ZERO));
		attributes.put("ItemDimensionsWeight", (_itemDimensions != null ? formatInt(_itemDimensions.getWeight()) : _ZERO));
		attributes.put("ItemDimensionsWidth", (_itemDimensions != null ? formatInt(_itemDimensions.getWidth()) : _ZERO));

		attributes.put("Label", _itemAttributes.getLabel());

		Price _listPrice = _itemAttributes.getListPrice();

		attributes.put("ListPriceAmount", (_listPrice != null ? formatInt(_listPrice.getAmount()) : _ZERO));
		attributes.put("ListPriceCurrencyCode", (_listPrice != null ?_listPrice.getCurrencyCode() : ""));
		attributes.put("ListPriceFormattedPrice",  (_listPrice != null ? _listPrice.getFormattedPrice() : ""));

		attributes.put("Manufacturer", _itemAttributes.getManufacturer());
		attributes.put("Model", _itemAttributes.getModel());
		attributes.put("MPN", _itemAttributes.getMPN());
		attributes.put("OperatingSystem", _itemAttributes.getOperatingSystem());
		attributes.put("PackageQuantity", formatInt(_itemAttributes.getPackageQuantity()));
		attributes.put("ProductGroup", _itemAttributes.getProductGroup());
		attributes.put("ProductTypeName", _itemAttributes.getProductTypeName());
		attributes.put("Publisher", _itemAttributes.getPublisher());
		attributes.put("ReleaseDate", formatDate(_itemAttributes.getReleaseDate()));
		attributes.put("Studio", _itemAttributes.getStudio());
		attributes.put("UPC", _itemAttributes.getUPC());

		OfferSummary _offerSummary = item.getOfferSummary();

		Price _lowestNewPrice = _offerSummary.getLowestNewPrice();

		attributes.put("OfferSummaryLowestNewPriceAmount", (_lowestNewPrice != null ? formatInt(_lowestNewPrice.getAmount()) : _ZERO));
		attributes.put("OfferSummaryLowestNewPriceCurrencyCode", (_lowestNewPrice != null ?_lowestNewPrice.getCurrencyCode() : ""));
		attributes.put("OfferSummaryLowestNewPriceFormattedPrice",  (_lowestNewPrice != null ? _lowestNewPrice.getFormattedPrice() : ""));

		Price _lowestUsedPrice = _offerSummary.getLowestUsedPrice();

		attributes.put("OfferSummaryLowestUsedPriceAmount", (_lowestUsedPrice != null ? formatInt(_lowestUsedPrice.getAmount()) : _ZERO));
		attributes.put("OfferSummaryLowestUsedPriceCurrencyCode", (_lowestUsedPrice != null ?_lowestUsedPrice.getCurrencyCode() : ""));
		attributes.put("OfferSummaryLowestUsedPriceFormattedPrice",  (_lowestUsedPrice != null ? _lowestUsedPrice.getFormattedPrice() : ""));

		Price _lowestCollectiblePrice = _offerSummary.getLowestCollectiblePrice();

		attributes.put("OfferSummaryLowestCollectiblePriceAmount", (_lowestCollectiblePrice != null ? formatInt(_lowestCollectiblePrice.getAmount()) : _ZERO));
		attributes.put("OfferSummaryLowestCollectiblePriceCurrencyCode", (_lowestCollectiblePrice != null ?_lowestCollectiblePrice.getCurrencyCode() : ""));
		attributes.put("OfferSummaryLowestCollectiblePriceFormattedPrice",  (_lowestCollectiblePrice != null ? _lowestCollectiblePrice.getFormattedPrice() : ""));

		Price _lowestRefurbishedPrice = _offerSummary.getLowestRefurbishedPrice();

		attributes.put("OfferSummaryLowestRefurbishedPriceAmount", (_lowestRefurbishedPrice != null ? formatInt(_lowestRefurbishedPrice.getAmount()) : _ZERO));
		attributes.put("OfferSummaryLowestRefurbishedPriceCurrencyCode", (_lowestRefurbishedPrice != null ?_lowestRefurbishedPrice.getCurrencyCode() : ""));
		attributes.put("OfferSummaryLowestRefurbishedPriceFormattedPrice",  (_lowestRefurbishedPrice != null ? _lowestRefurbishedPrice.getFormattedPrice() : ""));

		attributes.put("OfferSummaryTotalNew", _offerSummary.getTotalNew());
		attributes.put("OfferSummaryTotalUsed", _offerSummary.getTotalUsed());
		attributes.put("OfferSummaryTotalCollectible", _offerSummary.getTotalCollectible());
		attributes.put("OfferSummaryTotalRefurbished", _offerSummary.getTotalRefurbished());

		for (Map.Entry<String,Serializable> attr : attributes.entrySet()) {
			if (!(attr.getValue() instanceof String[])) {
				attr.setValue(new String[] {(String)attr.getValue()});
			}
		}

		return attributes;
	}

	private long getCategoryId(Item item, ServiceContext serviceContext) {
		String productGroup = item.getItemAttributes().getProductGroup();

		if (Validator.isNull(productGroup)) {
			return 0;
		}

		DynamicQuery query = DynamicQueryFactoryUtil.forClass(
			MBCategory.class, PortalClassLoaderUtil.getClassLoader());

		query.add(
			RestrictionsFactoryUtil.eq(
				"groupId", serviceContext.getScopeGroupId()));
		query.add(
			RestrictionsFactoryUtil.eq("name", productGroup));

		try {
			List categories = MBCategoryLocalServiceUtil.dynamicQuery(query);

			if (!categories.isEmpty()) {
				return ((MBCategory)categories.get(0)).getCategoryId();
			}
			else {
				MBCategory category = MBCategoryLocalServiceUtil.addCategory(
					serviceContext.getUserId(), 0, productGroup, productGroup,
					"default", null, null, null, 0, false, null, null, 0, null,
					false, null, 0, false, null, null, false, false,
					serviceContext);

				return category.getCategoryId();
			}
		}
		catch (Exception e1) {
		}

		return 0;
	}

	private String[] toArray(List<String> list) {
		if (list == null) {
			return new String[0];
		}

		return list.toArray(new String[list.size()]);
	}

	private String formatDate(String date) {
		if (Validator.isNull(date)) {
			return "19700101000000";
		}

		return date.replaceAll("-", "").concat("000000");
	}

	private String formatInt(BigInteger bi) {
		if (bi == null) {
			return _ZERO;
		}

		return String.format("%011d", bi.intValue());
	}

	private String formatInt(DecimalWithUnits bdwu) {
		if (bdwu == null) {
			return _ZERO;
		}

		return String.format("%011d", bdwu.getValue().intValue());
	}

	private String getPlainContent(Item item) {
		ItemAttributes itemAttributes = item.getItemAttributes();

		if (itemAttributes == null) {
			return null;
		}

		StringBundler sb = new StringBundler();

		for (String feature : itemAttributes.getFeature()) {
			sb.append(feature);
			sb.append(StringPool.SPACE);
		}

		if (Validator.isNull(sb.toString())) {
			sb.append(item.getASIN());
		}

		return sb.toString();
	}

	private static final String _AWS_ACCESS_KEY_ID = PortletProps.get(
		"aws.access.key.id");
	private static final String _SECRET_KEY = PortletProps.get(
		"secret.key");
	private static final String _ZERO = "00000000000";

}