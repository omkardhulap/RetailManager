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
		//TODO: add shopname, as per indian standard
		StringBuilder fullAddress = new StringBuilder(shop_address.getNumber())
				.append(",").append(shop_address.getPostCode());
		Location location = geoApiResolver(fullAddress.toString());

		//populate business object
		shop.getShopAddress().setLocation(location);

		//caching to in-memory db
		shopListHolder.add(shop);
	}

	//TODO: validate shopname
	private void validate(Shop shop) {
		if(shop == null || shop.getShopAddress() == null ||
				shop.getShopAddress().getNumber() == null|| shop.getShopAddress().getPostCode() ==  0) {
			logger.debug("Invalid shop - " + shop);
			throw new RetailManagerServiceException("Invalid shop details. No Address found", HttpStatus.BAD_REQUEST);
		}
	}

	public static Location geoApiResolver(String fullAddress) {
		GeoApiContext context = new GeoApiContext().setApiKey(ConfigConstants.geoApiKey);
		try {
			GeocodingResult result = GeocodingApi.geocode(context, fullAddress).await()[0];
			LatLng location = result.geometry.location;
			return new Location(location.lat,location.lng);
		} catch (Exception e) {
			logger.error("Error while using Google Maps Geocoding API", e);
			throw new RetailManagerServiceException(e, MessagesConstants.GOOGLE_SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	@Override
	public Shop getNearestShop(Location location) {
		List<Shop> availableShops = shopListHolder.getAll();

		//check if nearest shop already available in cache
		if(availableShops == null || availableShops.isEmpty()) {
			logger.info("No shops added.");
			throw new RetailManagerServiceException(MessagesConstants.NO_SHOPS_ADDED, HttpStatus.OK);
		}
		
		//Use Google Distance Matrix API to to get shortest distance from multiple destinations
		
		//form URI string from available locations.
		StringBuffer urisb = new StringBuffer(ConfigConstants.distMatrixAPI_Base)
		.append(ConfigConstants.distMatrixAPI_Origin).append(location.getLatitude()).append(',').append(location.getLongitude())
		.append(ConfigConstants.distMatrixAPI_Destination);
		
		Location loc;
		for(Shop shop : availableShops){
			loc = shop.getShopAddress().getLocation();
			urisb.append(loc.getLatitude()).append(',').append(loc.getLatitude()).append('|');
		}
		
		urisb.append(ConfigConstants.distMatrixAPI_Key).append(ConfigConstants.distMatrixApiKey);
		String uriStr = urisb.toString();
		logger.debug("Google Distance Matrix API URI >> " + uriStr);
		//Testing
		uriStr = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.6905615%2C-73.9976592%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626%7C40.659569%2C-73.933783%7C40.729029%2C-73.851524%7C40.6860072%2C-73.6334271%7C40.598566%2C-73.7527626&key=AIzaSyDbPwMDCH72N8Qf_qsQW9WggQj4tTc-IVw";
		DistanceMatrixModel distanceMatrixModel;
		try {
			distanceMatrixModel = (DistanceMatrixModel) objectMapper.readValue(new URL(uriStr), DistanceMatrixModel.class);
			System.out.println(distanceMatrixModel.toString());
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
		
		//TODO: Find nearestshop from list and pass on
		Shop nearest_shop = null;
		/*//TODO: determine nearest shop
		// Minimum is 0.0 since distance with itself will be 0
		double nearest = calculateDistance(location, allNearbyShops.get(0).getShopAddress().getLocation());
		double temp;
		Shop nearest_shop = allNearbyShops.get(0);

		// TODO: 29/8/16 - What happens if the list is very big, Need to think of a better data structure. Use BST?
		for(int i=1; i < allNearbyShops.size() ; i++) {
			temp = calculateDistance(location, allNearbyShops.get(i).getShopAddress().getLocation());
			*//** If distance of shops are equal, the first one found is returned
			 *  Since '<' is used in comparison
			 *//*
			if (temp < nearest) {
				nearest = temp;
				nearest_shop = allNearbyShops.get(i);
			}
		}

		if(nearest == 0.0) {
			logger.info("Found shop with an exact location match.");
		}*/
		return nearest_shop;
	}

	private Double calculateDistance(Location l1, Location l2) {
		Double diff_lat = l1.getLatitude() - l2.getLongitude();
		Double diff_lon = l1.getLongitude() - l2.getLongitude();

		return Math.sqrt(diff_lat * diff_lat + diff_lon * diff_lon);
	}

	@Override
	public List<Shop> getAll() {
		return shopListHolder.getAll();
	}
	
	

}
