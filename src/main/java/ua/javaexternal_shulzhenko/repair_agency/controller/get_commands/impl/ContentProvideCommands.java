package ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.repair_agency.constants.*;
import ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.RequestHandler;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.repair_agency.constants.PaginationConstants;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PaginationModel;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import static ua.javaexternal_shulzhenko.repair_agency.services.database.OrdersDBService.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.database.UsersDBService.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.database.ReviewsDBService.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.pagination.PagePaginationHandler.createPaginationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentProvideCommands {

    public static final Map<String, RequestHandler> COMMANDS = new HashMap<>();

    static {

        COMMANDS.put(CRAPaths.START, model -> CommonConstants.REDIRECT + CRAPaths.HOME);

        COMMANDS.put(CRAPaths.LOGIN, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.LOGIN_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.REGISTRATION, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.REGISTRATION_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.EDIT_USER, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.USER_EDITING_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.EDIT_ORDER, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ORDER_EDITING_MAIN_BLOCK);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.CREATE_ORDER, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ORDER_FORM);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.MAN_MAS_REGISTRATION, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ADMIN_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.HOME, model -> {

            int pageNum = extractPageNum(model);

            Page<Review> pageReviews = getPageableReviews(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.REVIEWS_FOR_HOME,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));
            model.
                    addAttribute(Attributes.REVIEWS, pageReviews.getContent()).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.COMMON_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });


        COMMANDS.put(CRAPaths.CUSTOMER_HOME, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = getPageableOrdersByTwoExcludeStatusesForCustomer(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED,
                    userId, PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });


        COMMANDS.put(CRAPaths.CUSTOMER_ORDER_HISTORY, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = getPageableOrdersByTwoStatusesForCustomer(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, userId,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.MANAGER_HOME, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Order> orders = getPageableOrderByStatus(
                    OrderStatus.PENDING, PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            List<User> masters = getUsersByRole(Role.MASTER);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.MASTERS, masters)
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.ACTIVE_ORDERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Order> orders = getPageableOrdersByThreeStatuses(
                    OrderStatus.CAR_WAITING, OrderStatus.REPAIR_WORK, OrderStatus.REPAIR_COMPLETED,
                    PageRequest.of(pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            List<User> masters = getUsersByRole(Role.MASTER);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.MASTERS, masters)
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.ORDER_HISTORY, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Order> orders = getPageableOrdersByTwoStatuses(
                    OrderStatus.ORDER_COMPLETED, OrderStatus.REJECTED,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            model
                    .addAttribute(Attributes.ORDERS, orders.getContent())
                    .addAttribute(Attributes.PG_MODEL, paginationModel)
                    .addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.CUSTOMERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<User> customers = getPageableUsersByRole(
                    Role.CUSTOMER, PageRequest.of(
                            pageNum - 1, PaginationConstants.USERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, customers);

            model.
                    addAttribute(Attributes.CUSTOMERS, customers.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.MASTERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<User> masters = getPageableUsersByRole(
                    Role.MASTER, PageRequest.of(
                            pageNum - 1, PaginationConstants.USERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));
            PaginationModel paginationModel = createPaginationModel(uri, masters);

            model.
                    addAttribute(Attributes.MASTERS, masters.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });


        COMMANDS.put(CRAPaths.MASTER_HOME, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);


            Page<Order> orders = getPageableOrdersByTwoExcludeStatusesForMaster(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, userId,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            model.
                    addAttribute(Attributes.ORDERS, orders.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });


        COMMANDS.put(CRAPaths.MASTER_COMPLETED_ORDERS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            int userId = (int) model.getAttribute(Attributes.USER_ID);

            Page<Order> orders = getPageableOrdersByTwoStatusesForMaster(
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED, userId,
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.ORDERS_FOR_PAGE,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, orders);

            model.
                    addAttribute(Attributes.ORDERS, orders.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.ADMIN_HOME, model -> {
            List<User> managers = getUsersByRole(Role.MANAGER);
            List<User> masters = getUsersByRole(Role.MASTER);

            model.
                    addAttribute(Attributes.MANAGERS, managers).
                    addAttribute(Attributes.MASTERS, masters).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ADMIN_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.REVIEWS, model -> {
            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);

            Page<Review> pageReviews = getPageableReviews(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.REVIEWS_FOR_REVIEW,
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));

            PaginationModel paginationModel = createPaginationModel(uri, pageReviews);

            model.
                    addAttribute(Attributes.REVIEWS, pageReviews.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.REVIEWS);
            return CRA_JSPFiles.CORE_PAGE;
        });
    }

    private ContentProvideCommands() {
    }

    private static int extractPageNum(Model model) {
        String pageNum = (String) model.getAttribute(Attributes.PAGE_NUM);
        if (pageNum != null) {
            return Integer.parseInt(pageNum);
        } else {
            return 1;
        }
    }
}