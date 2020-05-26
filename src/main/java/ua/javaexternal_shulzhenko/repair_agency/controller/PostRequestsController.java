package ua.javaexternal_shulzhenko.repair_agency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;
import ua.javaexternal_shulzhenko.repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.impl.ContentProvideCommands;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.*;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.services.database.OrdersDBService;
import ua.javaexternal_shulzhenko.repair_agency.services.database.UsersDBService;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.OrderEditor;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

import static ua.javaexternal_shulzhenko.repair_agency.services.editing.EditingOrderValidator.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.database.UsersDBService.*;
import static ua.javaexternal_shulzhenko.repair_agency.services.database.ReviewsDBService.addReview;


@Controller
public class PostRequestsController {

    private final UsersDBService usersDBService;
    private final OrdersDBService ordersDBService;

    @Autowired
    public PostRequestsController(UsersDBService usersDBService, OrdersDBService ordersDBService) {
        this.usersDBService = usersDBService;
        this.ordersDBService = ordersDBService;
    }

    @PostMapping(CRAPaths.LOGIN)
    public String handlePostRequest(Model model) {
        return ContentProvideCommands.COMMANDS.get(CRAPaths.LOGIN).handleRequest(model);
    }

    @PostMapping(CRAPaths.REGISTRATION)
    public String handleRegistration(@Valid RegistrationForm registrationForm, BindingResult bindingResult,
                                     Model model, HttpServletResponse resp) {

        String email = registrationForm.getEmail();
        boolean emailIsUnavailable = userWithEmailExist(email);

        if (!bindingResult.hasErrors() &&
                registrationForm.confirmationPassMatch() &&
                    !emailIsUnavailable) {

            User user = registrationForm.extractUser();
            createUser(user);
            model.addAttribute(Attributes.SUCCESS, "");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, registrationForm);

            if (!registrationForm.confirmationPassMatch()) {
                model.addAttribute(Attributes.WRONG_PASS_CONFIRMATION, "");
            }
            if (emailIsUnavailable) {
                model.addAttribute(Attributes.NOT_FREE_EMAIL, "");
            }
        }

        return ContentProvideCommands.COMMANDS.get(CRAPaths.REGISTRATION).handleRequest(model);
    }

    @PostMapping(CRAPaths.MAN_MAS_REGISTRATION)
    public String handleManMasRegistration(@Valid RegistrationForm registrationForm, BindingResult bindingResult,
                                           Model model, HttpServletResponse resp) {

        handleRegistration(registrationForm, bindingResult, model, resp);
        return ContentProvideCommands.COMMANDS.get(CRAPaths.MAN_MAS_REGISTRATION).handleRequest(model);
    }

    @PostMapping(CRAPaths.EDIT_USER)
    public String handleEditUser(@Valid UserEditingForm editingForm, BindingResult bindingResult,
                                 Model model, HttpServletResponse resp) {

        int userId = editingForm.getId();
        String email = editingForm.getEmail();
        boolean emailIsAvailable = userEmailIsAvailable(email, userId);

        if (!bindingResult.hasErrors() && emailIsAvailable) {
            User user = getUserById(editingForm.getId());
            UserEditor userEditor = new UserEditor(editingForm, user)
                    .compareFirstName()
                    .compareLastName()
                    .compareEmail()
                    .compareRole();
            usersDBService.editUser(user, editingForm, userEditor.getEdits());
            return CommonConstants.REDIRECT + CRAPaths.ADMIN_HOME;
        } else {
            if (!emailIsAvailable) {
                model.addAttribute(Attributes.NOT_FREE_EMAIL, "");
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, editingForm);
            return ContentProvideCommands.COMMANDS.get(CRAPaths.EDIT_USER).handleRequest(model);
        }
    }


    @PostMapping(CRAPaths.DELETE_USER)
    public String handleDeleteUser(@RequestParam() int userId) {

        deleteUser(userId);
        return CommonConstants.REDIRECT + CRAPaths.ADMIN_HOME;
    }


    @PostMapping(CRAPaths.CREATE_ORDER)
    public String handleCreateOrder(@Valid OrderForm orderForm, BindingResult bindingResult, Model model,
                                    @SessionAttribute(name = Attributes.USER, required = false) User customer,
                                    HttpServletResponse resp) {

        if (bindingResult.hasErrors()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, orderForm);
        } else {
            Order order = orderForm.extractOrder();
            order.setCustomer(customer);
            ordersDBService.createOrder(order);
            model.addAttribute(Attributes.MADE_ORDER, order);
        }

        return ContentProvideCommands.COMMANDS.get(CRAPaths.CREATE_ORDER).handleRequest(model);
    }

    @PostMapping(CRAPaths.EDIT_ORDER)
    public String handleEditOrder(@Valid OrderEditingForm editingForm, BindingResult bindingResult,
                                  Model model, HttpServletResponse resp) {

        int orderId = editingForm.getId();
        Order order = OrdersDBService.getOrderById(orderId);

        if (!bindingResult.hasErrors() &&
                !needMasterForThisStatus(editingForm, model) &&
                    !needPreviousPrice(editingForm, bindingResult, model)) {

            OrderEditor editor = new OrderEditor(editingForm, order)
                    .comparePrice()
                    .compareMasters()
                    .compareStatus()
                    .compareManagerComment();
            ordersDBService.editOrder(order, editingForm, editor.getEdits());
            return CommonConstants.REDIRECT + CRAPaths.MANAGER_HOME;
        } else {

            List<User> masters = getUsersByRole(Role.MASTER);

            OrderStatus status = order.getStatus();

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.CUR_ORDER_STATUS, status);
            model.addAttribute(Attributes.MASTERS, masters);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, editingForm);
            return ContentProvideCommands.COMMANDS.get(CRAPaths.EDIT_ORDER).handleRequest(model);
        }
    }

    @PostMapping(CRAPaths.EDIT_STATUS)
    public String handleEditStatus(@RequestParam OrderStatus status, @RequestParam int orderID) {

        if( status.equals(OrderStatus.REPAIR_WORK)){
            ordersDBService.changeOrderStatus(orderID, status);
        } else if (status.equals(OrderStatus.REPAIR_COMPLETED)){
            ordersDBService.changeOrderStatus(orderID, status, LocalDateTime.now());
        }
        return CommonConstants.REDIRECT + CRAPaths.MASTER_HOME;
    }

    @PostMapping({CRAPaths.REVIEWS})
    public String handleReviews(@Valid ReviewForm reviewForm, BindingResult bindingResult, Model model,
                                @SessionAttribute(name = Attributes.USER, required = false) User customer,
                                HttpServletRequest req, HttpServletResponse resp) {

        if (bindingResult.hasErrors()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, reviewForm);
        } else {
            Review review = reviewForm.extractReview();
            review.setCustomer(customer);
            addReview(review);
            model.addAttribute(Attributes.SUCCESS, "");
        }
        String pageNum = req.getParameter(Parameters.PAGE);
        String uri = req.getRequestURI();

        model.addAttribute(Attributes.PAGE_NUM, pageNum);
        model.addAttribute(Attributes.URI, uri);
        return ContentProvideCommands.COMMANDS.get(CRAPaths.REVIEWS).handleRequest(model);
    }
}


