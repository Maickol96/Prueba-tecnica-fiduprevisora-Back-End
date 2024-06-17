package com.fiduprevisora.service;

import com.fiduprevisora.entity.Orders;
import com.fiduprevisora.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getOrderById_WhenOrderExists_ShouldReturnOrder() {
        Long orderId = 1L;
        Orders order = new Orders();
        order.setId(orderId);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Orders foundOrder = orderService.getOrderById(orderId);

        assertNotNull(foundOrder);
        assertEquals(orderId, foundOrder.getId());
        verify(orderRepository).findById(orderId);
    }

    @Test
    void getOrderById_WhenOrderDoesNotExist_ShouldReturnNull() {
        Long orderId = 999L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Orders result = orderService.getOrderById(orderId);

        assertNull(result);
        verify(orderRepository).findById(orderId);
    }

    @Test
    void saveOrder_ShouldSaveAndReturnOrder() {
        Orders order = new Orders();
        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        Orders savedOrder = orderService.saveOrder(order);

        assertNotNull(savedOrder);
        verify(orderRepository).save(order);
    }

    @Test
    void deleteOrder_ShouldDeleteOrder() {
        Long orderId = 1L;
        doNothing().when(orderRepository).deleteById(orderId);

        assertDoesNotThrow(() -> orderService.deleteOrder(orderId));
        verify(orderRepository).deleteById(orderId);
    }
}
