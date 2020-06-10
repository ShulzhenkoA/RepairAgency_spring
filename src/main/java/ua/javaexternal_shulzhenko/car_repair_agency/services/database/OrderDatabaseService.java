package ua.javaexternal_shulzhenko.car_repair_agency.services.database;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.services.editing.impl.OrderEditor;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderDatabaseService {

    void createOrder(Order order);

    Order getOrderById(int id);

    Page<Order> getPageableOrdersByStatuses(Pageable pageable, OrderStatus... status);

    Page<Order> getPageableOrdersByTwoStatusesForCustomer(
            OrderStatus status, OrderStatus secondStatus, int customerId, Pageable pageable);

    Page<Order> getPageableOrdersByTwoExcludeStatusesForCustomer(
            OrderStatus status, OrderStatus secondStatus, int customerId, Pageable pageable);

    Page<Order> getPageableOrdersByTwoStatusesForMaster(
            OrderStatus status, OrderStatus secondStatus, int masterId, Pageable pageable);

    Page<Order> getPageableOrdersByTwoExcludeStatusesForMaster(
            OrderStatus status, OrderStatus secondStatus, int masterId, Pageable pageable);

    void changeOrderStatus(int orderId, OrderStatus status);

    void changeOrderStatus(int orderId, OrderStatus status, LocalDateTime dateTime);

    void editOrder(Order order, OrderEditingForm editingForm, List<OrderEditor.OrderEdits> edits);
}
