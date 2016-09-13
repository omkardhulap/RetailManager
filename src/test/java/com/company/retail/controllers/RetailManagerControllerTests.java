package com.company.retail.controllers;
/**
 * @author omkar
 */

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.company.retail.BaseTest;
import com.company.retail.config.MessagesConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RetailManagerController.class)
@AutoConfigureMockMvc
public class RetailManagerControllerTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void checkStatus() throws Exception {
        this.mockMvc.perform(get("/").header("Accept-Language", Locale.getDefault()))
        			.andDo(print())
        			.andExpect(status().isOk())
        			.andExpect(result -> {result.toString().equals(MessagesConstants.APP_SUCCESS);});
    }
}
