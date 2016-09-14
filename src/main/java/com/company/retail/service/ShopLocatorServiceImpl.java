package com.company.retail.service;
/**
 * @author omkar
 */

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.company.retail.config.ConfigConstants;
import com.company.retail.config.MessagesConstants;
import com.company.retail.db.ShopListHolder;
import com.company.retail.exception.RetailManagerServiceException;
import com.company.retail.models.DistanceMatrixModel;
import com.company.retail.models.Elements;
import com.company.retail.models.Location;
import com.company.retail.models.Shop;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

public class ShopLocatorServiceImpl implements ShopLocatorService {
	private static final Logger logger = LoggerFactory.getLogger(ShopLocatorServiceImpl.class);

	@Autowired
	private ShopListHolder shopListHolder;

	public ShopListHolder getShopListHolder() {
		return shopListHolder;
	}

	public void setShopListHolder(ShopListHolder shopListHolder) {
		this.shopListHolder = shopListHolder;
	}

	@Autowired
	ObjectMapper objectMapper;

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void saveShop(Shop shop) {

		validate(shop);

		// Form query string for the Google Maps Geocoding API
		Shop.ShopAddress shop_address = shop.getShopAddress();
		StringBuilder fullAddress = new StringBuilder(shop_address.getNumber())
				.append(",").append(shop_address.getPostCode());
		Location location = geoApiResolver(fullAddress.toString());

		//populate business object
		shop.getShopAddress().setLocation(location);

		//caching to in-memory db
		shopListHolder.add(shop);
	}

	private void validate(Shop shop) {
		if(shop == null || shop.getShopAddress() == null ||
				shop.getShopAddress().getNumber() == null|| shop.getShopAddress().getPostCode() ==  0) {
			logger.debug("Invalid shop - " + shop);
			throw new RetailManagerServiceException("Invalid shop details. No Address found", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * @Description Use Google Maps Geocoding API to retrieve the longitude and latitude 
	 * for the provided shopAddress
	 */
	public static Location geoApiResolver(String fullAddress) {
		GeoApiContext context = new GeoApiContext().setApiKey(ConfigConstants.geoApiKey);
		try {
			logger.debug("Calling Geo Coding Api for Address >>" + fullAddress);
			GeocodingResult result = GeocodingApi.geocode(context, fullAddress).await()[0];
			LatLng location = result.geometry.location;
			logger.debug("Geo Coding Api response >>" + location.lat + "," + location.lng);
			return new Location(location.lat,location.lng);
		} catch (Exception e) {
			logger.error("Error while using Google Maps Geocoding API", e);
			throw new RetailManagerServiceException(e, MessagesConstants.GOOGLE_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	/**
	 * @Description Use Google Distance Matrix API to to get shortest distance from multiple destinations
	 */
	@Override
	public Shop getNearestShop(Location location) {
		List<Shop> availableShops = shopListHolder.getAll();

		//check if nearest shop already available in cache
		if(availableShops == null || availableShops.isEmpty()) {
			logger.info("No shops added.");
			throw new RetailManagerServiceException(MessagesConstants.NO_SHOPS_ADDED, HttpStatus.OK);
		}

		//form URI string from available locations.
		StringBuffer urisb = new StringBuffer(ConfigConstants.distMatrixAPI_Base)
				.append(ConfigConstants.distMatrixAPI_Origin).append(location.getLatitude()).append(',').append(location.getLongitude())
				.append(ConfigConstants.distMatrixAPI_Destination);

		Location loc;
		for(Shop shop : availableShops){
			loc = shop.getShopAddress().getLocation();
			urisb.append(loc.getLatitude()).append(',').append(loc.getLongitude()).append('|');
		}

		urisb.append(ConfigConstants.distMatrixAPI_Key).append(ConfigConstants.distMatrixApiKey);
		String uriStr = urisb.toString();
		logger.debug("Google Distance Matrix API URI >> " + uriStr);

		//Testing
		//		uriStr = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=AIzaSyDbPwMDCH72N8Qf_qsQW9WggQj4tTc-IVw";

		DistanceMatrixModel distanceMatrixModel;
		try {
			distanceMatrixModel = (DistanceMatrixModel) objectMapper.readValue(new URL(uriStr), DistanceMatrixModel.class);
			logger.debug(distanceMatrixModel.toString());
		} catch (JsonParseException | JsonMappingException e) {
			logger.error("Google Distance Matrix API Response not parsed correctly. " + e);
			throw new RetailManagerServiceException(e, MessagesConstants.JSON_NOT_PROCESSED, HttpStatus.CONFLICT);
		} catch (MalformedURLException e) {
			logger.error("Google Distance Matrix API URI incorrectly. " + e);
			throw new RetailManagerServiceException(e, MessagesConstants.INCORRECT_SHOP_DETAILS, HttpStatus.CONFLICT);
		} catch (IOException e) {
			logger.error("Google Distance Matrix API not available. " + e);
			throw new RetailManagerServiceException(e, MessagesConstants.GOOGLE_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
		}

		if(!distanceMatrixModel.getStatus().equalsIgnoreCase("OK")){
			logger.error("Google Distance Matrix API did not responded successfully.");
			throw new RetailManagerServiceException(MessagesConstants.RESPONSE_NOT_PROCESSED, HttpStatus.BAD_REQUEST);
		}

		//Find nearest shop from response and pass on
		Elements[] elements = distanceMatrixModel.getRows()[0].getElements();
		//assuming 0'th destination is closest
		int closestDestinationIndex = 0;
		//preferring distance based calculation over duration base
		int tempValue, minValue = -1;

		for(int index = 0; index < elements.length; index++){
			//Status can be "OK", "ZERO_RESULTS","NOT_FOUND"
			if(elements[index].getStatus().equalsIgnoreCase("OK")){
				minValue = Integer.parseInt(elements[index].getDistance().getValue());
				closestDestinationIndex = index;
				break;
			}
		}

		if(minValue == -1){
			logger.error("Google Distance Matrix API did not responded successfully.");
			throw new RetailManagerServiceException(MessagesConstants.RESPONSE_NOT_PROCESSED, HttpStatus.BAD_REQUEST);
		}

		for(int index = closestDestinationIndex+1; index < elements.length; index++){
			if(elements[index].getStatus().equalsIgnoreCase("OK")){
				tempValue = Integer.parseInt(elements[index].getDistance().getValue());
				//assuming only 1 nearest shop exists
				if(tempValue < minValue){
					minValue = tempValue;
					closestDestinationIndex = index;
				}
			}
		}

		String nearestShopAddress = distanceMatrixModel.getDestination_addresses()[closestDestinationIndex];
		logger.debug("Nearest Shop Address from Latitude & Longitude as per Google" + nearestShopAddress);

		/*assuming google will return response for all the destinations, 
		 * more API testing required to avoid ArrayIndexOutofBound Exception scenario
		 */
		Shop nearestShop = availableShops.get(closestDestinationIndex);
		logger.debug("Nearest Shop Address in cache" + nearestShop.toString());

		//If nearestShop > 1 then, return List<Shop>
		return nearestShop;
	}

	@Override
	public List<Shop> getAll() {
		return shopListHolder.getAll();
	}

}
