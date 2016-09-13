package com.company.retail.controllers;
/**
 * @author omkar
 * @Description Common controller exception handling
 */

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.company.retail.exception.RetailManagerServiceException;
import com.company.retail.config.MessagesConstants;

@ControllerAdvice
public class GlobalExceptionController {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);
	
	/**
     * Exception handling at service level.
     */
    @ExceptionHandler(value = RetailManagerServiceException.class)
    public String handleServiceExceptions(RetailManagerServiceException e, HttpServletResponse response) {
    	logger.error("ERROR: " + e.getMessage(), e);
        response.setStatus(e.getHttpStatusCode().value());
        return e.getMessage();
    }

	@ExceptionHandler(Exception.class)
	public String handleAllExceptions(Exception e) {
		logger.error("ERROR: " + e.getMessage(), e);
        return MessagesConstants.RESPONSE_NOT_PROCESSED;
	}

	
	/*For business validations we will send specific errors in particular views or error field and
	 system won't crash*/
}