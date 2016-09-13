package com.company.retail.controllers;
/**
 * @author omkar
 */

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.junit.Before;
import org.junit.Test;

import com.company.retail.BaseTest;
import com.company.retail.db.ShopListHolder;
import com.company.retail.service.ShopLocatorServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

public class ApplicationIntegrationTests extends BaseTest {

	@Before
	public void setUp() {
		ShopListHolder shopListHolder = new ShopListHolder();
		ShopLocatorServiceImpl shopLocatorService = new ShopLocatorServiceImpl();
		shopLocatorService.setShopListHolder(shopListHolder);
		RetailManagerController retailManagerController = new RetailManagerController();
		retailManagerController.setShopLocatorService(shopLocatorService);
		retailManagerController.setObjectMapper(new ObjectMapper());
		RestAssuredMockMvc.standaloneSetup(retailManagerController);
	}

	@Test
	public void testApplicationStatus() {
		// Verfies application is up & running
		given().when().get("/shop/").then().statusCode(200);
	}
}

