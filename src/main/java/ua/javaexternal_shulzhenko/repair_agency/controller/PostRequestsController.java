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
import ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.RequestHandleCommand;
import ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.impl.ContentProvideCommands;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.*;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.Order;
import ua.javaexternal_shulzhenko.repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.repair_agency.entities.review.Review;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;
import ua.javaexternal_shulzhenko.repair_agency.services.database.OrderDatabaseService;
import ua.javaexternal_shulzhenko.repair_agency.services.database.ReviewDatabaseService;
import ua.javaexternal_shulzhenko.repair_agency.services.database.UserDatabaseService;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.EditingValidator;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.EditingOrderValidator;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.OrderEditor;
import ua.javaexternal_shulzhenko.repair_agency.services.editing.impl.UserEditor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class PostRequestsController {

    private final UserDatabaseService userDatabaseService;
    private final OrderDatabaseService orderDatabaseService;
    private final ReviewDatabaseService reviewDatabaseService;
    private final ContentProvideCommands contentProvideCommands;

    @Autowired
    public PostRequestsController(UserDatabaseService userDatabaseService,
                                  OrderDatabaseService orderDatabaseService,
                                  ReviewDatabaseService reviewDatabaseService,
                                  ContentProvideCommands contentProvideCommands) {
        this.userDatabaseService = userDatabaseService;
        this.orderDatabaseService = orderDatabaseService;
        this.reviewDatabaseService = reviewDatabaseService;
        this.contentProvideCommands = contentProvideCommands;
    }

    @PostMapping(CRAPaths.LOGIN)
    public String handlePostRequest(Model model) {
        Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
        RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.LOGIN);
        return requestHandleCommand.handleRequest(model);
    }

    @PostMapping(CRAPaths.REGISTRATION)
    public String handleRegistration(@Valid RegistrationForm registrationForm, BindingResult bindingResult,
                                     Model model, HttpServletResponse resp) {

        String email = registrationForm.getEmail();
        boolean emailIsAvailable = userDatabaseService.userEmailIsAvailable(email);

        if (!bindingResult.hasErrors() &&
                registrationForm.confirmationPassMatch() &&
                emailIsAvailable) {

            User user = registrationForm.extractUser();
            userDatabaseService.createUser(user);
            model.addAttribute(Attributes.SUCCESS, "");
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, registrationForm);

            if (!registrationForm.confirmationPassMatch()) {
                model.addAttribute(Attributes.WRONG_PASS_CONFIRMATION, "");
            }
            if (!emailIsAvailable) {
                model.addAttribute(Attributes.NOT_FREE_EMAIL, "");
            }
        }

        Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
        RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.REGISTRATION);
        return requestHandleCommand.handleRequest(model);
    }

    @PostMapping(CRAPaths.MAN_MAS_REGISTRATION)
    public String handleManMasRegistration(@Valid RegistrationForm registrationForm, BindingResult bindingResult,
                                           Model model, HttpServletResponse resp) {

        handleRegistration(registrationForm, bindingResult, model, resp);

        Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
        RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.MAN_MAS_REGISTRATION);
        return requestHandleCommand.handleRequest(model);
    }

    @PostMapping(CRAPaths.EDIT_USER)
    public String handleEditUser(@Valid UserEditingForm editingForm, BindingResult bindingResult,
                                 Model model, HttpServletResponse resp, UserEditor userEditor) {

        int userId = editingForm.getId();
        String email = editingForm.getEmail();
        boolean emailIsAvailable = userDatabaseService.userEmailIsAvailable(email, userId);

        if (!bindingResult.hasErrors() && emailIsAvailable) {
            User user = userDatabaseService.getUserById(editingForm.getId());
            userEditor
                    .setForm(editingForm)
                    .setUser(user)
                    .compareFirstName()
                    .compareLastName()
                    .compareEmail()
                    .compareRole();
            userDatabaseService.editUser(user, editingForm, userEditor.getEdits());
            return CommonConstants.REDIRECT + CRAPaths.ADMIN_HOME;
        } else {
            if (!emailIsAvailable) {
                model.addAttribute(Attributes.NOT_FREE_EMAIL, "");
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, editingForm);

            Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
            RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.EDIT_USER);
            return requestHandleCommand.handleRequest(model);
        }
    }

    @PostMapping(CRAPaths.DELETE_USER)
    public String handleDeleteUser(@RequestParam() int userId) {
        userDatabaseService.deleteUser(userId);
        return CommonConstants.REDIRECT + CRAPaths.ADMIN_HOME;
    }


    @PostMapping(CRAPaths.CREATE_ORDER)
    public String handleCreateOrder(@Valid OrderForm orderForm, BindingResult bindingResult, Model model,
                                    HttpServletResponse resp, @SessionAttribute(name = Attributes.USER) User customer) {

        if (bindingResult.hasErrors()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, orderForm);
        } else {
            Order order = orderForm.extractOrder();
            order.setCustomer(customer);
            orderDatabaseService.createOrder(order);
            model.addAttribute(Attributes.MADE_ORDER, order);
        }

        Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
        RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.CREATE_ORDER);
        return requestHandleCommand.handleRequest(model);
    }

    @PostMapping(CRAPaths.EDIT_ORDER)
    public String handleEditOrder(@Valid OrderEditingForm editingForm, BindingResult bindingResult,
                                  Model model, HttpServletResponse resp, OrderEditor orderEditor,
                                  EditingOrderValidator editingOrderValidator) {

        int orderId = editingForm.getId();
        Order order = orderDatabaseService.getOrderById(orderId);

        if (!bindingResult.hasErrors() &&
                !editingOrderValidator.needMasterForThisStatus(editingForm, model) &&
                !editingOrderValidator.needPreviousPrice(editingForm, bindingResult, model)) {

            orderEditor
                    .setForm(editingForm)
                    .setOrder(order)
                    .comparePrice()
                    .compareMasters()
                    .compareStatus()
                    .compareManagerComment();
            orderDatabaseService.editOrder(order, editingForm, orderEditor.getEdits());
            return CommonConstants.REDIRECT + CRAPaths.MANAGER_HOME;
        } else {

            List<User> masters = userDatabaseService.getUsersByRole(Role.MASTER);

            OrderStatus status = order.getStatus();

            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.CUR_ORDER_STATUS, status);
            model.addAttribute(Attributes.MASTERS, masters);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, editingForm);

            Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
            RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.EDIT_ORDER);
            return requestHandleCommand.handleRequest(model);
        }
    }

    @PostMapping(CRAPaths.EDIT_STATUS)
    public String handleEditStatus(@RequestParam OrderStatus status, @RequestParam int orderID) {

        if (status.equals(OrderStatus.REPAIR_WORK)) {
            orderDatabaseService.changeOrderStatus(orderID, status);
        } else if (status.equals(OrderStatus.REPAIR_COMPLETED)) {
            orderDatabaseService.changeOrderStatus(orderID, status, LocalDateTime.now());
        }
        return CommonConstants.REDIRECT + CRAPaths.MASTER_HOME;
    }

    @PostMapping({CRAPaths.REVIEWS})
    public String handleReviews(@Valid ReviewForm reviewForm, BindingResult bindingResult, Model model,
                                HttpServletRequest req, HttpServletResponse resp,
                                @SessionAttribute(name = Attributes.USER, required = false) User customer) {

        if (bindingResult.hasErrors()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            model.addAttribute(Attributes.BINDING_RESULT, bindingResult);
            model.addAttribute(Attributes.PREV_FORM, reviewForm);
        } else {
            Review review = reviewForm.extractReview();
            review.setCustomer(customer);
            reviewDatabaseService.addReview(review);
            model.addAttribute(Attributes.SUCCESS, "");
        }
        String pageNum = req.getParameter(Parameters.PAGE);
        String uri = req.getRequestURI();

        model.addAttribute(Attributes.PAGE_NUM, pageNum);
        model.addAttribute(Attributes.URI, uri);

        Map<String, RequestHandleCommand> commands = contentProvideCommands.getCommands();
        RequestHandleCommand requestHandleCommand = commands.get(CRAPaths.REVIEWS);
        return requestHandleCommand.handleRequest(model);
    }
}


