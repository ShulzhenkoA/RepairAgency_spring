package ua.javaexternal_shulzhenko.repair_agency.services.database.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.repair_agency.services.database.repository.OrderRepository;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.OrderEditor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrdersDatabaseInteractionServiceTest {

    @Autowired
    private OrdersDatabaseInteractionService ordersDatabaseInteractionService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Order order;

    @MockBean
    private OrderEditingForm orderEditingForm;

    private Collection<OrderStatus> statuses;

    private List<OrderEditor.OrderEdits> edits;

    @Test
    public void creatingOrder_savesOrder(){
        ordersDatabaseInteractionService.createOrder(order);

        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void gettingOrderById_getsOneOrder(){
        ordersDatabaseInteractionService.getOrderById(0);

        verify(orderRepository, times(1)).getOne(0);
    }

    @Test
    public void gettingPageableOrdersByStatuses_findsAllByStatusIn(){
        Pageable pageable = Pageable.unpaged();
        statuses = Arrays.asList(OrderStatus.CAR_WAITING, OrderStatus.REPAIR_WORK);

        ordersDatabaseInteractionService.getPageableOrdersByStatuses(pageable, OrderStatus.CAR_WAITING, OrderStatus.REPAIR_WORK);

        verify(orderRepository, times(1)).findAllByStatusIn(statuses, pageable);
    }

    @Test
    public void gettingPageableOrdersByTwoStatusesForCustomer_findsAllByCustomerIdAndStatusesIn(){
        Pageable pageable = Pageable.unpaged();
        statuses = Arrays.asList(OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);

        ordersDatabaseInteractionService.getPageableOrdersByTwoStatusesForCustomer(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, 0, pageable);

        verify(orderRepository, times(1))
                .findAllByCustomer_IdAndStatusIn(0, statuses, pageable);
    }

    @Test
    public void gettingPageableOrdersByTwoExcludeStatusesForCustomer_findsAllByCustomerIdAndStatusesNotIn(){
        Pageable pageable = Pageable.unpaged();
        statuses = Arrays.asList(OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);

        ordersDatabaseInteractionService.getPageableOrdersByTwoExcludeStatusesForCustomer(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, 0, pageable);

        verify(orderRepository, times(1))
                .findAllByCustomer_IdAndStatusNotIn(0, statuses, pageable);
    }

    @Test
    public void gettingPageableOrdersByTwoStatusesForMaster_findsAllByMasterIdAndStatusesIn(){
        Pageable pageable = Pageable.unpaged();
        statuses = Arrays.asList(OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);

        ordersDatabaseInteractionService.getPageableOrdersByTwoStatusesForMaster(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, 0, pageable);

        verify(orderRepository, times(1))
                .findAllByMaster_IdAndStatusIn(0, statuses, pageable);
    }

    @Test
    public void gettingPageableOrdersByTwoExcludeStatusesForMaster_findsAllByMasterIdAndStatusesNotIn(){
        Pageable pageable = Pageable.unpaged();
        statuses = Arrays.asList(OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);

        ordersDatabaseInteractionService.getPageableOrdersByTwoExcludeStatusesForMaster(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, 0, pageable);

        verify(orderRepository, times(1))
                .findAllByMaster_IdAndStatusNotIn(0, statuses, pageable);
    }

    @Test
    public void changingOrderStatus_setsStatus(){
        when(orderRepository.getOne(0)).thenReturn(order);

        ordersDatabaseInteractionService.changeOrderStatus(0, OrderStatus.CAR_WAITING);

        verify(order, times(1)).setStatus(OrderStatus.CAR_WAITING);
    }

    @Test
    public void changingOrderStatusWithDateTime_setsStatusAndSetsDateTime(){
        when(orderRepository.getOne(0)).thenReturn(order);

        ordersDatabaseInteractionService.changeOrderStatus(0, OrderStatus.CAR_WAITING, any(LocalDateTime.class));

        assertAll(
                () -> verify(order, times(1)).setStatus(OrderStatus.CAR_WAITING),
                () -> verify(order, times(1)).setRepairCompletionDate(null));
    }

    @Test
    public void editingOrder_whenEditsContainsPrice_setsPrice(){
        edits = new LinkedList<>();
        edits.add(OrderEditor.OrderEdits.PRICE);
        when(orderEditingForm.getPrice()).thenReturn("0");

        ordersDatabaseInteractionService.editOrder(order, orderEditingForm, edits);

        verify(order, times(1)).setPrice(Double.parseDouble(orderEditingForm.getPrice()));
    }

    @Test
    public void editingOrder_whenEditsContainsMasterId_setsMaster(){
        edits = new LinkedList<>();
        edits.add(OrderEditor.OrderEdits.MASTER_ID);

        ordersDatabaseInteractionService.editOrder(order, orderEditingForm, edits);

        verify(order, times(1)).setMaster(any());
    }

    @Test
    public void editingOrder_whenEditsContainsStatus_setsStatus(){
        edits = new LinkedList<>();
        edits.add(OrderEditor.OrderEdits.STATUS);

        ordersDatabaseInteractionService.editOrder(order, orderEditingForm, edits);

        verify(order, times(1)).setStatus(orderEditingForm.getStatus());
    }

    @Test
    public void editingOrder_whenEditsContainsManagerComment_setsManagerComment(){
        edits = new LinkedList<>();
        edits.add(OrderEditor.OrderEdits.MANAGER_COMMENT);

        ordersDatabaseInteractionService.editOrder(order, orderEditingForm, edits);

        verify(order, times(1)).setManagerComment(orderEditingForm.getManagerComment());
    }
}