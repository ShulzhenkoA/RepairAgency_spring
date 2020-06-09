package ua.javaexternal_shulzhenko.repair_agency.services.database.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.OrderEditingForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.exceptions.DataBaseInteractionException;
import ua.javaexternal_shulzhenko.repair_agency.services.database.OrderDatabaseService;
import ua.javaexternal_shulzhenko.repair_agency.services.database.UserDatabaseService;
import ua.javaexternal_shulzhenko.repair_agency.services.database.repository.OrderRepository;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.OrderEditor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class OrdersDatabaseInteractionService implements OrderDatabaseService {

    private static OrderRepository orderRepository;
    private final UserDatabaseService userDatabaseService;

    @Autowired
    public OrdersDatabaseInteractionService(OrderRepository orderRepository,
                                            UserDatabaseService userDatabaseService){
        OrdersDatabaseInteractionService.orderRepository = orderRepository;
        this.userDatabaseService = userDatabaseService;
    }

    @Transactional
    public void createOrder(Order order){
        orderRepository.save(order);
    }

    public Order getOrderById(int id){
        return orderRepository.getOne(id);
    }

    public Page<Order> getPageableOrdersByStatuses(Pageable pageable, OrderStatus... status){

        Collection<OrderStatus> statuses = Arrays.asList(status);
        return orderRepository.findAllByStatusIn(statuses, pageable);
    }

    public Page<Order> getPageableOrdersByTwoStatusesForCustomer(
            OrderStatus status, OrderStatus secondStatus, int customerId, Pageable pageable){

        Collection<OrderStatus> statuses = Arrays.asList(status, secondStatus);
        return orderRepository.findAllByCustomer_IdAndStatusIn(customerId, statuses, pageable);
    }

    public Page<Order> getPageableOrdersByTwoExcludeStatusesForCustomer(
            OrderStatus status, OrderStatus secondStatus, int customerId, Pageable pageable){

        Collection<OrderStatus> statuses = Arrays.asList(status, secondStatus);
        return orderRepository.findAllByCustomer_IdAndStatusNotIn(customerId, statuses, pageable);
    }

    public Page<Order> getPageableOrdersByTwoStatusesForMaster(
            OrderStatus status, OrderStatus secondStatus, int masterId, Pageable pageable){

        Collection<OrderStatus> statuses = Arrays.asList(status, secondStatus);
        return orderRepository.findAllByMaster_IdAndStatusIn(masterId, statuses, pageable);
    }

    public Page<Order> getPageableOrdersByTwoExcludeStatusesForMaster(
            OrderStatus status, OrderStatus secondStatus, int masterId, Pageable pageable){

        Collection<OrderStatus> statuses = Arrays.asList(status, secondStatus);
        return orderRepository.findAllByMaster_IdAndStatusNotIn(masterId, statuses, pageable);
    }

    @Transactional
    public void changeOrderStatus(int orderId, OrderStatus status){
        Order order = orderRepository.getOne(orderId);
        order.setStatus(status);
    }

    @Transactional
    public void changeOrderStatus(int orderId, OrderStatus status, LocalDateTime dateTime){
        Order order = orderRepository.getOne(orderId);
        order.setStatus(status);
        order.setRepairCompletionDate(dateTime);
    }

    @Transactional
    public void editOrder(Order order, OrderEditingForm editingForm, List<OrderEditor.OrderEdits> edits){
        for (OrderEditor.OrderEdits edit : edits) {
            switch (edit){
                case PRICE:
                    order.setPrice(Double.parseDouble(editingForm.getPrice()));
                    break;
                case MASTER_ID:
                    User master = userDatabaseService.getUserById(editingForm.getMasterID());
                    order.setMaster(master);
                    break;
                case STATUS:
                    order.setStatus(editingForm.getStatus());
                    break;
                case MANAGER_COMMENT:
                    order.setManagerComment(editingForm.getManagerComment());
                    break;
                default:
                    throw new DataBaseInteractionException("Can't edit such order data: " + edit);
            }
        }
    }
}
