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

    Location expectedLocation = null;

    @Before
    public void setData() throws Exception {
        expectedLocation = setLocationFromGoogleApi("B-4 Shambhu Vihar Near DAV School, Aundh, Pune, 411007, India");
    }

    @Test
    public void testGeoApiResolver() {
        String address = "B-4 Shambhu Vihar Near DAV School, Aundh, Pune, 411007, India";
        ShopLocatorServiceImpl shopLocatorService = new ShopLocatorServiceImpl();
        Location location = shopLocatorService.geoApiResolver(address);
        assert (location.equals(expectedLocation));
    }

    @Test(expected = RetailManagerServiceException.class)
    public void testSave() {
        Shop shop = buildShop("My Shop", "B-4 Shambhu Vihar Near DAV School, Aundh, Pune, India", 411007);
        shopLocatorService.saveShop(shop);

        Shop shopWithLocation = buildShopWithLocation(shop, expectedLocation.getLatitude(), 
        													expectedLocation.getLongitude());
        assert(shopLocatorService.getAll().get(0).equals(shopWithLocation));
        shopLocatorService.saveShop(null);
    }


    @Test//(expected = RetailManagerServiceException.class)
    public void testNearest() {
        Shop s1 = buildShopWithLocation(buildShop("Shop 1", "Number 1", 1), 111.40,81.50);
        Shop s2 = buildShopWithLocation(buildShop("Shop 2", "Number 2", 2), 42.10,29.07);
        Shop s3 = buildShopWithLocation(buildShop("Shop 3", "Number 3", 3), -122.0,-23.60);
        Shop s4 = buildShopWithLocation(buildShop("Shop 4", "Number 4", 4), -32.50,82.90);
        Shop s5 = buildShopWithLocation(buildShop("Shop 5", "Number 5", 5), 42.06,-21.50);
        Shop s6 = buildShopWithLocation(buildShop("Nearest_Shop", "amba bhavani temple kopar road dombivli india", 421202), 19.2144658,73.0772353);
        shopListHolder.add(s1);
        shopListHolder.add(s2);
        shopListHolder.add(s3);
        shopListHolder.add(s4);
        shopListHolder.add(s5);
        shopListHolder.add(s6);
        shopLocatorService.saveShop(s1);
        shopLocatorService.saveShop(s2);
        shopLocatorService.saveShop(s3);
        shopLocatorService.saveShop(s4);
        shopLocatorService.saveShop(s5);
        shopLocatorService.saveShop(s6);

        /*https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&
        origins=18.5599434,73.8035086&destinations=111.40,81.50|42.10,29.07|-122.0,-23.60|-32.50,82.90|42.06,-21.50|19.2144658,73.0772353
        &key=AIzaSyDbPwMDCH72N8Qf_qsQW9WggQj4tTc-IVw*/
        
        //it may give AssertionError due to update in maps from hard coded values
        assert(shopLocatorService.getNearestShop(new Location(18.5599434,73.8035086)).equals(s6));
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
