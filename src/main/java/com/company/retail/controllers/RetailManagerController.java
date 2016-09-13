package com.company.retail.controllers;
/**
 * @author omkar
 */

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.retail.exception.RetailManagerServiceException;
import com.company.retail.config.MessagesConstants;
import com.company.retail.models.Location;
import com.company.retail.models.ServiceResponseModel;
import com.company.retail.models.Shop;
import com.company.retail.service.ShopLocatorService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class RetailManagerController {

	private static final Logger logger = LoggerFactory.getLogger(RetailManagerController.class);

	@Autowired
	ShopLocatorService shopLocatorService;

	public void setShopLocatorService(ShopLocatorService shopLocatorService) {
		this.shopLocatorService = shopLocatorService;
	}

	@Autowired
	ObjectMapper objectMapper;

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@RequestMapping("/")
	public String status() {
		return MessagesConstants.APP_SUCCESS;
	}

	@RequestMapping(path = "/shop/add", method = RequestMethod.POST)
	public String addShop(HttpServletResponse httpResponse, @Validated @RequestBody Shop shop){
		String response;
		try {
			shopLocatorService.saveShop(shop);
			response = objectMapper.writeValueAsString(new ServiceResponseModel(true));
			httpResponse.setStatus(HttpStatus.CREATED.value());
			logger.debug("Shop details added successfully.");
		} catch (IOException io) {
			logger.error("Error while processing input during addShop", io);
			throw new RetailManagerServiceException(io, MessagesConstants.RESPONSE_NOT_PROCESSED, HttpStatus.OK);
		}
		return response ;
	}

	@RequestMapping(path = "/shop/getNearest", method = RequestMethod.GET)
	public String getNearestShop(@RequestParam("customerLatitude") String latitude,
			@RequestParam("customerLongitude") String longitude) {
		String response = null;
		try {
			Location location = new Location(Double.parseDouble(latitude), Double.parseDouble(longitude));
			Shop nearestShop = shopLocatorService.getNearestShop(location);
			response = objectMapper.writeValueAsString(nearestShop);
		} catch (NumberFormatException e) {
			logger.error("Invalid longitude - " + longitude + " or latitude - " + latitude + "\n", e);
			throw new RetailManagerServiceException(e, MessagesConstants.INVALID_LOCATION, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.error("Error getting nearest shop for location - (" + latitude + ", " +longitude + ")" , e);
			throw new RetailManagerServiceException(e, MessagesConstants.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return response;
	}

}
