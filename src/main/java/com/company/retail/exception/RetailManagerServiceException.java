package com.company.retail.exception;
/**
 * @author omkar
 */

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;

public class RetailManagerServiceException extends RuntimeException {

	private static final long serialVersionUID = 5123065897436414173L;
	
	@NotNull
    private HttpStatus httpStatusCode;

    public RetailManagerServiceException(Exception e, String message, HttpStatus httpStatusCode) {
        super(message, e);
        this.httpStatusCode = httpStatusCode;
    }

    public RetailManagerServiceException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatusCode() {
        return httpStatusCode;
    }
}
