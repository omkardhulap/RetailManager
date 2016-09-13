package com.company.retail.service;
/**
 * @author omkar
 */

import java.util.List;

import com.company.retail.models.Location;
import com.company.retail.models.Shop;

public interface ShopLocatorService {

	/**
	 * @Description From address fetches the latitude and longitude & 
	 * saves it as {@link ShopWithLocation} with other information passed in argument
	 */
	void saveShop(Shop shop);

	/**
	 * @Description Fetches the nearest shop details from {@link ShopWithLocation} 
	 * using latitude and longitude passed in as argument
	 */
	Shop getNearestShop(Location location);

	/**
	 * Gets the list of shops (Added for testing purpose)
	 */
	List<Shop> getAll();
}
