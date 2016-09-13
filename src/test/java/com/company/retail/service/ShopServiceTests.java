package com.company.retail.service;
/**
 * @author omkar
 */

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.company.retail.BaseTest;
import com.company.retail.exception.RetailManagerServiceException;
import com.company.retail.models.Location;
import com.company.retail.models.Shop;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;


@RunWith(SpringJUnit4ClassRunner.class)
public class ShopServiceTests extends BaseTest {

    Location location_expected = null;

    @Before
    public void setData() throws Exception {
        /**
         * Setting expected location is needed because it is observed that the google api returns
         * different values at different times. TODO why?
         * So hard coding latitude and longitude values can fail anytime
         */
        location_expected = setLocationFromGoogleApi("1600 Amphitheatre Parkway Mountain View, CA".concat(",").concat(94043 + ""));
    }

    @Test
    public void testGeoApiResolver() {
        String address = "1600 Amphitheatre Parkway Mountain View, CA".concat(",").concat(94043 + "");
        Location location = ShopLocatorServiceImpl.geoApiResolver(address);
        assert (location.equals(location_expected));
    }

    @Test(expected = RetailManagerServiceException.class)
    public void testSave() {
        Shop shop = buildShop("Test Shop", "1600 Amphitheatre Parkway Mountain View, CA", 94043);
        shopLocatorService.saveShop(shop);

        Shop shopWithLocation = buildShopWithLocation(shop, location_expected.getLatitude(), 
        													location_expected.getLongitude());
        assert (shopLocatorService.getAll().get(0).equals(shopWithLocation));
        shopLocatorService.saveShop(null);
    }


    @Test
    public void testNearest() {
        Shop s1 = buildShopWithLocation(buildShop("Shop 1", "Number 1", 1), 1.0, 1.0);
        Shop s2 = buildShopWithLocation(buildShop("Shop 2", "Number 2", 2), 2.0, 2.0);
        Shop s3 = buildShopWithLocation(buildShop("Shop 3", "Number 3", 2), -2.0, -2.0);
        Shop s4 = buildShopWithLocation(buildShop("Shop 4", "Number 4", 2), -2.0, 2.0);
        Shop s5 = buildShopWithLocation(buildShop("Shop 5", "Number 5", 2), 2.0, -2.0);
        shopListHolder.add(s1);
        shopListHolder.add(s2);
        shopListHolder.add(s3);
        shopListHolder.add(s4);
        shopListHolder.add(s5);
        /**
         *    Distance Calculation
         * s1 -> sqrt(2)
         * s2 -> sqrt(8)
         * s3 -> sqrt(8)
         * s4 -> sqrt(8)
         * s5 -> sqrt(8)
         */
        assert shopLocatorService.getNearestShop(new Location(0.0,0.0)).equals(s1);
        /**
         *    Distance Calculation
         * s1 -> sqrt(5)
         * s2 -> sqrt(9)
         * s3 -> sqrt(25)
         * s4 -> sqrt(25)
         * s5 -> sqrt(9)
         */
        assert shopLocatorService.getNearestShop(new Location(2.0,-1.0)).equals(s1);
        // Same location as s1
        assert shopLocatorService.getNearestShop(new Location(1.0,1.0)).equals(s1);
    }

    private Shop buildShop(String name, String number, int post) {
        Shop shop = new Shop();
        shop.setShopName(name);
        shop.setShopAddress(new Shop.ShopAddress(number, post));
        return shop;
    }

    private Shop buildShopWithLocation(Shop shop, Double latitude, Double longitude) {
        shop.getShopAddress().setLocation(new Location(latitude, longitude));
    	return shop;
    }

    private Location setLocationFromGoogleApi(String address) throws Exception {
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCuJmJrMJvMzu_4ZgMpK7dJiV_26sBwBJ0");
        GeocodingResult result = GeocodingApi.geocode(context, address).await()[0];
        LatLng location = result.geometry.location;
        return new Location(location.lat, location.lng);
    }
}
