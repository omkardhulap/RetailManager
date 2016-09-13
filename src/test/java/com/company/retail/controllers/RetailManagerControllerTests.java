package com.company.retail.controllers;
/**
 * @author omkar
 */

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.retail.BaseTest;
import com.company.retail.config.MessagesConstants;
import com.company.retail.models.Shop;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RetailManagerController.class)
@AutoConfigureMockMvc
public class RetailManagerControllerTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkStatus() throws Exception {
        this.mockMvc.perform(get("/shop/").header("Accept-Language", Locale.getDefault()))
        			.andDo(print())
        			.andExpect(status().isOk())
        			.andExpect(result -> {result.toString().equals(MessagesConstants.APP_SUCCESS);});
    }
    
    @Test
	public void testAddShop() {
		// Bad request - Missing body
		given().when().post("/shop/add").then().statusCode(400);
		Shop shop = new Shop();
		shop.setShopName("New Shop");
		// Missing Address
		given().contentType("application/json").body(shop).when().post("/shop/add").then().statusCode(400);

		// Address is needed in a request body
		shop.setShopAddress(new Shop.ShopAddress("Address", 123));
		given().contentType("application/json").body(shop).when().post("/shop/add").then().statusCode(201);
	}

	@Test
	public void testBlankLatLong() {
		// Bad Request since request param is null
		given().when().get("/shop/getNearest?customerLatitude=&customerLongitude").then().statusCode(400);
	}

	@Test(expected = Exception.class)
	public void testInvalidLatLong() {
		// No shops has been added yet
		given().when().get("/shop/getNearest?customerLatitude=10.10&customerLongitude=89.55")
		.then().statusCode(HttpStatus.OK.value())
		.assertThat(result -> {result.toString().equals(MessagesConstants.NO_SHOPS_ADDED);}
				);
		// Bad Request since request param's value is invalid
		given().when().get("/shop/getNearest?customerLatitude=qwertyu&customerLongitude=tyu")
		.then().statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	//TODO: add test methods for getNearest end point
}
