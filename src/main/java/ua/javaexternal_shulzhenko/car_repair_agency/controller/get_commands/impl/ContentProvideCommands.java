package ua.javaexternal_shulzhenko.car_repair_agency.controller.get_commands.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.*;
import ua.javaexternal_shulzhenko.car_repair_agency.controller.get_commands.RequestHandleCommand;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.PaginationConstants;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.pagination.PaginationModel;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.OrderDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.ReviewDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.database.UserDatabaseService;
import ua.javaexternal_shulzhenko.car_repair_agency.services.pagination.Pagination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContentProvideCommands {

    private UserDatabaseService userDatabaseService;
    private OrderDatabaseService orderDatabaseService;
    private ReviewDatabaseService reviewDatabaseService;
    private Pagination pagination;
    private final Map<String, RequestHandleCommand> commands = new HashMap<>();

    @Autowired
    public ContentProvideCommands(UserDatabaseService userDatabaseService,
                                  OrderDatabaseService orderDatabaseService,
                                  ReviewDatabaseService reviewDatabaseService,
                                  Pagination pagination) {
        this.userDatabaseService = userDatabaseService;
        this.orderDatabaseService = orderDatabaseService;
        this.reviewDatabaseService = reviewDatabaseService;
        this.pagination = pagination;
    }

    {
        commands.put(CRAPaths.START, model -> CommonConstants.REDIRECT + CRAPaths.HOME);

        commands.put(CRAPaths.LOGIN, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.LOGIN_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.REGISTRATION, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.REGISTRATION_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.EDIT_USER, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.USER_EDITING_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.EDIT_ORDER, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ORDER_EDITING_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.CREATE_ORDER, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ORDER_FORM);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.MAN_MAS_REGISTRATION, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ADMIN_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.HOME, model -> {

            int pageNum = extractPageNum(model);

            Page<Review> pageReviews = reviewDatabaseService.getPageableReviews(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.REVIEWS_FOR_HOME,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));
            model
                    .addAttribute(Attributes.REVIEWS, pageReviews.getContent())
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.COMMON_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });


        commands.put(CRAPaths.CUSTOMER_HOME, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByTwoExcludeStatusesForCustomer(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                    userId, PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });


        commands.put(CRAPaths.CUSTOMER_ORDER_HISTORY, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByTwoStatusesForCustomer(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, userId,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.MANAGER_HOME, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByStatuses(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))),
                    OrderStatus.PENDING);

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            List<User> masters = userDatabaseService.getUsersByRole(Role.MASTER);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.MASTERS, masters)
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.ACTIVE_ORDERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByStatuses(
                    PageRequest.of(pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))),
                    OrderStatus.CAR_WAITING, OrderStatus.REPAIR_WORK, OrderStatus.REPAIR_COMPLETED);

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            List<User> masters = userDatabaseService.getUsersByRole(Role.MASTER);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.MASTERS, masters)
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.ORDER_HISTORY, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByStatuses(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))),
                    OrderStatus.ORDER_COMPLETED, OrderStatus.REJECTED);

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.CUSTOMERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<User> customers = userDatabaseService.getPageableUsersByRole(
                    Role.CUSTOMER, PageRequest.of(
                            pageNum - 1, PaginationConstants.USERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = pagination.createPaginationModel(uri, customers);

            model.
                    addAttribute(Attributes.CUSTOMERS, customers.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.MASTERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<User> masters = userDatabaseService.getPageableUsersByRole(
                    Role.MASTER, PageRequest.of(
                            pageNum - 1, PaginationConstants.USERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));
            PaginationModel paginationModel = pagination.createPaginationModel(uri, masters);

            model.
                    addAttribute(Attributes.MASTERS, masters.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });


        commands.put(CRAPaths.MASTER_HOME, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByTwoExcludeStatusesForMaster(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, userId,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            model.
                    addAttribute(Attributes.ORDERS, orders.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });


        commands.put(CRAPaths.MASTER_COMPLETED_ORDERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = orderDatabaseService.getPageableOrdersByTwoStatusesForMaster(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, userId,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = pagination.createPaginationModel(uri, orders);

            model.
                    addAttribute(Attributes.ORDERS, orders.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.ADMIN_HOME, model -> {
            List<User> managers = userDatabaseService.getUsersByRole(Role.MANAGER);
            List<User> masters = userDatabaseService.getUsersByRole(Role.MASTER);

            model.
                    addAttribute(Attributes.MANAGERS, managers).
                    addAttribute(Attributes.MASTERS, masters).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ADMIN_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        commands.put(CRAPaths.REVIEWS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Review> pageReviews = reviewDatabaseService.getPageableReviews(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.REVIEWS_FOR_REVIEW_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = pagination.createPaginationModel(uri, pageReviews);

            model.
                    addAttribute(Attributes.REVIEWS, pageReviews.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.REVIEWS);
            return CRA_JSPFiles.CORE_PAGE;
        });
    }

    private static int extractPageNum(Model model) {
        String pageNum = (String) model.getAttribute(Attributes.PAGE_NUM);
        if (pageNum != null) {
            return Integer.parseInt(pageNum);
        } else {
            return 1;
        }
    }

    public Map<String, RequestHandleCommand> getCommands() {
        return commands;
    }
}