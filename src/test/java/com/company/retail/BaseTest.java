package com.company.retail;
/**
 * @author omkar
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.company.retail.db.ShopListHolder;
import com.company.retail.service.ShopLocatorService;
import com.fasterxml.jackson.databind.ObjectMapper;


@ContextConfiguration({"/test-spring-context.xml"})
public class BaseTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ShopLocatorService shopLocatorService;

    @Autowired
    protected ShopListHolder shopListHolder;

}
