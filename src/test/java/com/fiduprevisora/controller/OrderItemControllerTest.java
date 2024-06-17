package com.fiduprevisora.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiduprevisora.entity.OrderItem;
import com.fiduprevisora.service.OrderItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderItemController.class)
class OrderItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderItemService orderItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllOrderItems_ShouldReturnOrderItems() throws Exception {
        mockMvc.perform(get("/api/order-items"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderItemById_WhenOrderItemExists_ShouldReturnOrderItem() throws Exception {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        given(orderItemService.getOrderItemById(1L)).willReturn(orderItem);

        mockMvc.perform(get("/api/order-items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createOrderItem_ShouldCreateOrderItem() throws Exception {
        OrderItem orderItem = new OrderItem();
        given(orderItemService.saveOrderItem(any(OrderItem.class))).willReturn(orderItem);

        mockMvc.perform(post("/api/order-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItem)))
                .andExpect(status().isOk());
    }

    @Test
    void updateOrderItem_WhenOrderItemExists_ShouldReturnUpdatedOrderItem() throws Exception {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        given(orderItemService.getOrderItemById(1L)).willReturn(orderItem);
        given(orderItemService.saveOrderItem(any(OrderItem.class))).willReturn(orderItem);

        mockMvc.perform(put("/api/order-items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderItem)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteOrderItem_ShouldDeleteOrderItem() throws Exception {
        Long orderItemId = 1L;
        doNothing().when(orderItemService).deleteOrderItem(orderItemId);

        mockMvc.perform(delete("/api/order-items/" + orderItemId))
                .andExpect(status().isOk());
    }
}
