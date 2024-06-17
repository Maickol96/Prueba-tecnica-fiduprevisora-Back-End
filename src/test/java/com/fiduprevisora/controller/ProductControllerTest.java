package com.fiduprevisora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiduprevisora.entity.Product;
import com.fiduprevisora.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
    class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper; // Utilizado para convertir objetos a JSON

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Sample Product");

        given(productService.getProductById(1L)).willReturn(product);
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        mockMvc.perform(get("/api/products/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sample Product"));
    }

    @Test
    void createProduct_ShouldCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("New Product");

        given(productService.saveProduct(any(Product.class))).willReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;
        given(productService.getProductById(productId)).willReturn(null);

        mockMvc.perform(get("/api/products/" + productId))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNotFound() throws Exception {
        Long productId = 999L;
        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Updated Name");

        given(productService.getProductById(productId)).willReturn(null);

        mockMvc.perform(put("/api/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturnOk() throws Exception {
        Long productId = 999L;
        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isOk());
    }
}
