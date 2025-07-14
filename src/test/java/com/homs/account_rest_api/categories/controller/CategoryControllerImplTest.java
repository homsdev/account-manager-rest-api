package com.homs.account_rest_api.categories.controller;

import com.homs.account_rest_api.categories.service.CategoryService;
import com.homs.account_rest_api.categories.utils.DummyCategories;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@ActiveProfiles("Test")
@Slf4j
public class CategoryControllerImplTest extends TestCase {

    @MockBean
    CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private String baseUrl = "/api/categories";
    private DummyCategories dummyCategories;

    @Override
    @Before
    public void setUp() throws Exception {
        dummyCategories = new DummyCategories();
    }

    /**
     * HttpStatus 200 happy path, retrieve all categories
     * GET /api/categories
     */
    @Test
    public void shouldRespondWith200() throws Exception {

        when(categoryService.getAllCategories())
                .thenReturn(dummyCategories.getDummyCategoriesList());

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items.length()").value(3))
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    /**
     * HttpStatus 204 no content,return no content when there are not available categories for retrieval
     * GET /api/categories
     */
    @Test
    public void shouldReturn204NoContentWhenNoCategories() throws Exception {
        when(categoryService.getAllCategories())
                .thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(get(baseUrl))
                .andExpect(status().isNoContent())
                .andReturn();

        log.info(result.getResponse().getContentAsString());
    }
}