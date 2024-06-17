package com.fiduprevisora.service;

import com.fiduprevisora.entity.OrderItem;
import com.fiduprevisora.repository.OrderItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    @Test
    void getOrderItemById_WhenOrderItemExists_ShouldReturnOrderItem() {
        Long orderItemId = 1L;
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderItemId);
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.of(orderItem));

        OrderItem foundOrderItem = orderItemService.getOrderItemById(orderItemId);

        assertNotNull(foundOrderItem);
        assertEquals(orderItemId, foundOrderItem.getId());
        verify(orderItemRepository).findById(orderItemId);
    }

    @Test
    void getOrderItemById_WhenOrderItemDoesNotExist_ShouldReturnNull() {
        Long orderItemId = 999L;
        when(orderItemRepository.findById(orderItemId)).thenReturn(Optional.empty());

        OrderItem result = orderItemService.getOrderItemById(orderItemId);

        assertNull(result);
        verify(orderItemRepository).findById(orderItemId);
    }

    @Test
    void saveOrderItem_ShouldSaveAndReturnOrderItem() {
        OrderItem orderItem = new OrderItem();
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem);

        OrderItem savedOrderItem = orderItemService.saveOrderItem(orderItem);

        assertNotNull(savedOrderItem);
        verify(orderItemRepository).save(orderItem);
    }

    @Test
    void deleteOrderItem_ShouldDeleteOrderItem() {
        Long orderItemId = 1L;
        doNothing().when(orderItemRepository).deleteById(orderItemId);

        assertDoesNotThrow(() -> orderItemService.deleteOrderItem(orderItemId));
        verify(orderItemRepository).deleteById(orderItemId);
    }
}
