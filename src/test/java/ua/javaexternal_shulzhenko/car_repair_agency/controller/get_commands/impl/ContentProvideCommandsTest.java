package ua.javaexternal_shulzhenko.car_repair_agency.controller.get_commands.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.*;
import ua.javaexternal_shulzhenko.car_repair_agency.controller.get_commands.RequestHandleCommand;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.OrderDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.ReviewDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.UserDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.pagination.Pagination;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ContentProvideCommandsTest {

    @MockBean
    private UserDatabaseService userDatabaseService;

    @MockBean
    private OrderDatabaseService orderDatabaseService;

    @MockBean
    private ReviewDatabaseService reviewDatabaseService;

    @MockBean
    private Pagination pagination;

    @MockBean
    private Model model;

    @Autowired
    private ContentProvideCommands contentProvideCommands;

    @ParameterizedTest
    @CsvSource({
            CRAPaths.LOGIN + ", " + CRA_JSPFiles.LOGIN_MAIN_BLOCK,
            CRAPaths.REGISTRATION + ", " + CRA_JSPFiles.REGISTRATION_MAIN_BLOCK,
            CRAPaths.CREATE_ORDER + ", " + CRA_JSPFiles.ORDER_FORM,
            CRAPaths.MAN_MAS_REGISTRATION + ", " + CRA_JSPFiles.ADMIN_PAGE,
            CRAPaths.EDIT_USER + ", " + CRA_JSPFiles.USER_EDITING_MAIN_BLOCK,
            CRAPaths.EDIT_ORDER + ", " + CRA_JSPFiles.ORDER_EDITING_MAIN_BLOCK})
    public void command_setsCorrespondingMainBlockAndReturnCorePage(String commandName, String fileName) {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(commandName);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(model).addAttribute(Attributes.MAIN_BLOCK, fileName),
                () -> assertEquals(returnedResource, CRA_JSPFiles.CORE_PAGE));
    }

    @Test
    public void commandStart_returnRedirectHome() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.START);

        String returnedResource = command.handleRequest(model);

        assertEquals(CommonConstants.REDIRECT + CRAPaths.HOME, returnedResource);
    }

    @Test
    public void commandHome_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.HOME);

        Page<Review> reviews = Page.empty();

        when(model.addAttribute(any(), any())).thenReturn(model);
        when(reviewDatabaseService.getPageableReviews(PageRequest.of(0, PaginationConstants.REVIEWS_FOR_HOME,
                Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(reviews);


        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(reviewDatabaseService, times(1))
                        .getPageableReviews(PageRequest.of(0, PaginationConstants.REVIEWS_FOR_HOME,
                                Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(model, times(1)).addAttribute(Attributes.REVIEWS, reviews.getContent()),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.COMMON_HOME),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandCustomerHome_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.CUSTOMER_HOME);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByTwoExcludeStatusesForCustomer(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                        Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByTwoExcludeStatusesForCustomer(
                                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandCustomerOrderHistory_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.CUSTOMER_ORDER_HISTORY);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByTwoStatusesForCustomer(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                        Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByTwoStatusesForCustomer(
                                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandManagerHome_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.MANAGER_HOME);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByStatuses(
                PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE, Sort.by(Sort.Order.desc(CommonConstants.ID))),
                OrderStatus.PENDING))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByStatuses(
                                PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID))),
                                OrderStatus.PENDING),
                () -> verify(userDatabaseService, times(1)).getUsersByRole(Role.MASTER),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.MASTERS, Collections.EMPTY_LIST),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandActiveOrders_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.ACTIVE_ORDERS);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByStatuses(
                PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE, Sort.by(Sort.Order.desc(CommonConstants.ID))),
                OrderStatus.CAR_WAITING, OrderStatus.REPAIR_WORK, OrderStatus.REPAIR_COMPLETED))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByStatuses(
                                PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID))),
                                OrderStatus.CAR_WAITING, OrderStatus.REPAIR_WORK, OrderStatus.REPAIR_COMPLETED),
                () -> verify(userDatabaseService, times(1)).getUsersByRole(Role.MASTER),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.MASTERS, Collections.EMPTY_LIST),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandOrderHistory_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.ORDER_HISTORY);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByStatuses(
                PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE, Sort.by(Sort.Order.desc(CommonConstants.ID))),
                OrderStatus.ORDER_COMPLETED, OrderStatus.REJECTED))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByStatuses(
                                PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID))),
                                OrderStatus.ORDER_COMPLETED, OrderStatus.REJECTED),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandCustomers_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.CUSTOMERS);

        Page<User> customers = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(userDatabaseService.getPageableUsersByRole(
                Role.CUSTOMER, PageRequest.of(
                        0, PaginationConstants.USERS_FOR_PAGE, Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(customers);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(userDatabaseService, times(1))
                        .getPageableUsersByRole(
                                Role.CUSTOMER, PageRequest.of(
                                        0, PaginationConstants.USERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(pagination, times(1)).createPaginationModel("URI", customers),
                () -> verify(model, times(1)).addAttribute(Attributes.CUSTOMERS, customers.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandMasters_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.MASTERS);

        Page<User> masters = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(userDatabaseService.getPageableUsersByRole(
                Role.MASTER, PageRequest.of(
                        0, PaginationConstants.USERS_FOR_PAGE, Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(masters);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(userDatabaseService, times(1))
                        .getPageableUsersByRole(
                                Role.MASTER, PageRequest.of(
                                        0, PaginationConstants.USERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(pagination, times(1)).createPaginationModel("URI", masters),
                () -> verify(model, times(1)).addAttribute(Attributes.MASTERS, masters.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandMasterHome_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.MASTER_HOME);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByTwoExcludeStatusesForMaster(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                        Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByTwoExcludeStatusesForMaster(
                                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandMasterCompletedOrders_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.MASTER_COMPLETED_ORDERS);

        Page<Order> orders = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);
        when(model.addAttribute(any(), any())).thenReturn(model);

        when(orderDatabaseService.getPageableOrdersByTwoStatusesForMaster(
                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                        Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(orders);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(orderDatabaseService, times(1))
                        .getPageableOrdersByTwoStatusesForMaster(
                                OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                                0, PageRequest.of(0, PaginationConstants.ORDERS_FOR_PAGE,
                                        Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(pagination, times(1)).createPaginationModel("URI", orders),
                () -> verify(model, times(1)).addAttribute(Attributes.ORDERS, orders.getContent()),
                () -> verify(model, times(1)).addAttribute(Attributes.PG_MODEL, null),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandAdminHome_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.ADMIN_HOME);

        when(model.addAttribute(any(), any())).thenReturn(model);

        when(userDatabaseService.getUsersByRole(Role.MASTER)).thenReturn(Collections.EMPTY_LIST);
        when(userDatabaseService.getUsersByRole(Role.MANAGER)).thenReturn(Collections.EMPTY_LIST);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(userDatabaseService, times(1)).getUsersByRole(Role.MASTER),
                () -> verify(userDatabaseService, times(1)).getUsersByRole(Role.MANAGER),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MASTERS, Collections.EMPTY_LIST),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MANAGERS, Collections.EMPTY_LIST),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ADMIN_PAGE),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }

    @Test
    public void commandReview_passesActionsChain() {
        RequestHandleCommand command = contentProvideCommands.getCommands().get(CRAPaths.REVIEWS);

        Page<Review> reviews = Page.empty();

        when(model.getAttribute(Attributes.URI)).thenReturn("URI");
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(reviewDatabaseService.getPageableReviews(PageRequest.of(0, PaginationConstants.REVIEWS_FOR_REVIEW_PAGE,
                Sort.by(Sort.Order.desc(CommonConstants.ID)))))
                .thenReturn(reviews);

        String returnedResource = command.handleRequest(model);

        assertAll(
                () -> verify(reviewDatabaseService, times(1))
                        .getPageableReviews(PageRequest.of(0, PaginationConstants.REVIEWS_FOR_REVIEW_PAGE,
                                Sort.by(Sort.Order.desc(CommonConstants.ID)))),
                () -> verify(model, times(1)).addAttribute(Attributes.REVIEWS, reviews.getContent()),
                () -> verify(model, times(1))
                        .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.REVIEWS),
                () -> assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource));
    }
}