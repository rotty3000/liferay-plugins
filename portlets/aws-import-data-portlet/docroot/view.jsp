<%/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
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
 */%>

<%@ include file="/init.jsp" %>

<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">
		<portlet:actionURL var="importActionURL" name="importFromAWS" />

		<aui:form action="<%= importActionURL %>" name="fm" method="post">

			<aui:input name="keywords" type="text" />

			<aui:select name="searchIndex">
				<aui:option value="All">All</aui:option>
				<aui:option value="Apparel">Apparel</aui:option>
				<aui:option value="ArtsAndCrafts">ArtsAndCrafts</aui:option>
				<aui:option value="Automotive">Automotive</aui:option>
				<aui:option value="Baby">Baby</aui:option>
				<aui:option value="Beauty">Beauty</aui:option>
				<aui:option value="Blended">Blended</aui:option>
				<aui:option value="Books">Books</aui:option>
				<aui:option value="Classical">Classical</aui:option>
				<aui:option value="DigitalMusic">DigitalMusic</aui:option>
				<aui:option value="Grocery">Grocery</aui:option>
				<aui:option value="MP3Downloads">MP3Downloads</aui:option>
				<aui:option value="DVD">DVD</aui:option>
				<aui:option value="Electronics">Electronics</aui:option>
				<aui:option value="HealthPersonalCare">HealthPersonalCare</aui:option>
				<aui:option value="HomeGarden">HomeGarden</aui:option>
				<aui:option value="Industrial">Industrial</aui:option>
				<aui:option value="Jewelry">Jewelry</aui:option>
				<aui:option value="KindleStore">KindleStore</aui:option>
				<aui:option value="Kitchen">Kitchen</aui:option>
				<aui:option value="Magazines">Magazines</aui:option>
				<aui:option value="Merchants">Merchants</aui:option>
				<aui:option value="Miscellaneous">Miscellaneous</aui:option>
				<aui:option value="MobileApps">MobileApps</aui:option>
				<aui:option value="Music">Music</aui:option>
				<aui:option value="MusicalInstruments">MusicalInstruments</aui:option>
				<aui:option value="MusicTracks">MusicTracks</aui:option>
				<aui:option value="OfficeProducts">OfficeProducts</aui:option>
				<aui:option value="OutdoorLiving">OutdoorLiving</aui:option>
				<aui:option value="PCHardware">PCHardware</aui:option>
				<aui:option value="PetSupplies">PetSupplies</aui:option>
				<aui:option value="Photo">Photo</aui:option>
				<aui:option value="Shoes">Shoes</aui:option>
				<aui:option value="Software">Software</aui:option>
				<aui:option value="SportingGoods">SportingGoods</aui:option>
				<aui:option value="Tools">Tools</aui:option>
				<aui:option value="Toys">Toys</aui:option>
				<aui:option value="UnboxVideo">UnboxVideo</aui:option>
				<aui:option value="VHS">VHS</aui:option>
				<aui:option value="Video">Video</aui:option>
				<aui:option value="VideoGames">VideoGames</aui:option>
				<aui:option value="Watches">Watches</aui:option>
				<aui:option value="Wireless">Wireless</aui:option>
				<aui:option value="WirelessAccessories">WirelessAccessories</aui:option>
			</aui:select>

			<aui:select name="type">
				<aui:option value="<%= BlogsEntry.class.getName() %>"><%= BlogsEntry.class.getName() %></aui:option>
				<aui:option value="<%= MBMessage.class.getName() %>"><%= MBMessage.class.getName() %></aui:option>
			</aui:select>

			<aui:input name="pages" type="text" />

			<aui:button-row>
				<aui:button type="submit" />
			</aui:button-row>
		</aui:form>
	</c:when>
	<c:otherwise>
		Please sign in!
	</c:otherwise>
</c:choose>