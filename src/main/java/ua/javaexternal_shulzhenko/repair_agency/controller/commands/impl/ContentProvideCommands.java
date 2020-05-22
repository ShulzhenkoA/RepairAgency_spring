package ua.javaexternal_shulzhenko.repair_agency.controller.commands.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.repair_agency.constants.*;
import ua.javaexternal_shulzhenko.repair_agency.controller.commands.RequestHandler;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PaginationModel;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import javax.validation.Valid;

import static ua.javaexternal_shulzhenko.repair_agency.services.database.UsersDBService.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.database.ReviewsDBService.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.pagination.PagePaginationHandler.createPaginationModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentProvideCommands {

    public static final Map<String, RequestHandler> COMMANDS = new HashMap<>();

    static {

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

        COMMANDS.put(CRAPaths.ERROR404, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE404);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.ERROR500, model -> {
            model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE500);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.HOME, model -> {

            int pageNum = extractPageNum(model);

            Page<Review> pageReviews = getPageableReviews(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.REVIEWS_FOR_HOME.getAmount(),
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));
            model.
                    addAttribute(Attributes.REVIEWS, pageReviews.getContent()).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.COMMON_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });


        /*

        COMMANDS.put(CRAPaths.LOGOUT, (req, resp) -> {
            req.getSession().invalidate();
            resp.sendRedirect(req.getContextPath() + CRAPaths.HOME);
        });

        COMMANDS.put(CRAPaths.CUSTOMER_HOME,(req, resp) -> {
            User user = getUserFromSession(req);
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<Order> orders = OrdersDBService.getCustomerOrdersByOffsetAmountTwoExcludeStatuses(user, offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(),
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
        });

        COMMANDS.put(CRAPaths.CUSTOMER_ORDER_HISTORY,(req, resp) -> {
            User user = getUserFromSession(req);
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<Order> orders = OrdersDBService.getCustomerOrdersByOffsetAmountTwoStatuses(user, offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(),
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
        });

        COMMANDS.put(CRAPaths.MANAGER_HOME,(req, resp) -> {
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<Order> orders = OrdersDBService.getManagerOrdersByOffsetAmountStatus(offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(), OrderStatus.PENDING);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            List<User> masters = UsersDBService.getUsersByRole(Role.MASTER);
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.MASTERS, masters);
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.MANAGER_HOME);
        });

        COMMANDS.put(CRAPaths.ACTIVE_ORDERS,(req, resp) -> {
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<Order> orders = OrdersDBService.getManagerOrdersByAmountOffsetMultipleExcludeStatuses(offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(),
                    OrderStatus.PENDING, OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            List<User> masters = UsersDBService.getUsersByRole(Role.MASTER);
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.MASTERS, masters);
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.MANAGER_HOME);
        });

        COMMANDS.put(CRAPaths.ORDER_HISTORY,(req, resp) -> {
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<Order> orders = OrdersDBService.getManagerOrdersByOffsetAmountTwoStatuses(offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(),
                    OrderStatus.ORDER_COMPLETED, OrderStatus.REJECTED);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.MANAGER_HOME);
        });*/

        COMMANDS.put(CRAPaths.CUSTOMERS, model -> {

            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            Page<User> customers = getPageableUsersByRole(
                    Role.CUSTOMER, PageRequest.of(
                            pageNum - 1, PaginationConstants.USERS_FOR_PAGE.getAmount(),
                            Sort.by(Sort.Order.desc(CommonConstants.ID))));
            PaginationModel paginationModel = createPaginationModel(uri, customers);
            model.
                    addAttribute(Attributes.CUSTOMERS, customers.getContent()).
                    addAttribute(Attributes.PG_MODEL, paginationModel).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.MANAGER_HOME);
            return CRA_JSPFiles.CORE_PAGE;
        });

        /*COMMANDS.put(CRAPaths.MASTERS,(req, resp) -> {
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<User> masters = UsersDBService.getUsersByRoleOffsetAmount(Role.MASTER,
                    offset, PaginationConstants.USERS_FOR_PAGE.getAmount());
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, masters.getEntitiesTotalAmount(), PaginationConstants.USERS_FOR_PAGE.getAmount());
            req.setAttribute(Attributes.MASTERS, masters.getEntities());
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.MANAGER_HOME);
        });

        COMMANDS.put(CRAPaths.MASTER_HOME,(req, resp) -> {
            User user = getUserFromSession(req);
            int pageNum = extractPageNum(req);
            int offset = computeOffset(pageNum);
            PageEntities<Order> orders = OrdersDBService.getMasterOrdersByOffsetAmountTwoExcludeStatuses(user, offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(),
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
        });

        COMMANDS.put(CRAPaths.MASTER_COMPLETED_ORDERS, model -> {
            User user = getUserFromModel(model);
            int pageNum = extractPageNum(model);
            Page<Order> orders = getMasterOrdersByOffsetAmountTwoStatuses(user, offset,
                    PaginationConstants.ORDERS_FOR_PAGE.getAmount(),
                    OrderStatus.REJECTED, OrderStatus.ORDER_COMPLETED);
            PaginationModel paginationModel = pagePaginationHandler.createPaginationModel(req.getRequestURI(),
                    pageNum, orders.getEntitiesTotalAmount(), PaginationConstants.ORDERS_FOR_PAGE.getAmount());
            req.setAttribute(Attributes.ORDERS, orders.getEntities());
            req.setAttribute(Attributes.PG_MODEL, paginationModel);
            dispatchToCorePageWithBlock(req, resp, CRA_JSPFiles.CUSTOMER_MASTER_PAGE);
        });*/

        COMMANDS.put(CRAPaths.ADMIN_HOME, (model) -> {
            List<User> managers = getUsersByRole(Role.MANAGER);
            List<User> masters = getUsersByRole(Role.MASTER);
            model.
                    addAttribute(Attributes.MANAGERS, managers).
                    addAttribute(Attributes.MASTERS, masters).
                    addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.ADMIN_PAGE);
            return CRA_JSPFiles.CORE_PAGE;
        });

        COMMANDS.put(CRAPaths.REVIEWS, (model) -> {

            int pageNum = extractPageNum(model);
            String uri = (String) model.getAttribute(Attributes.URI);
            Page<Review> pageReviews = getPageableReviews(
                    PageRequest.of(
                            pageNum - 1, PaginationConstants.REVIEWS_FOR_REVIEW.getAmount(),
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

    private static User getUserFromModel(Model model) {
        return (User) model.getAttribute(Attributes.USER);
    }
}