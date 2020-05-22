package ua.javaexternal_shulzhenko.repair_agency.controller.commands.impl;

import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRA_JSPFiles;
import ua.javaexternal_shulzhenko.repair_agency.controller.commands.FormHandler;
import ua.javaexternal_shulzhenko.repair_agency.controller.commands.RequestHandler;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.ReviewForm;
import ua.javaexternal_shulzhenko.repair_agency.services.database.ReviewsDBService;


import java.util.HashMap;
import java.util.Map;

public class DataHandleCommands {

    public static final Map<String, FormHandler> COMMANDS = new HashMap<>();


    static {
    /*     COMMANDS.put(CRAPaths.LOGIN, (req, resp) -> {
            LoginForm loginForm = new LoginForm(req);
            Set<String> inconsistencies = FormValidator.validateForm(loginForm);
            try {
                if (inconsistencies.isEmpty()) {
                    User user = UserAuthenticator.authenticate(loginForm);
                    addUserToSession(req, user);
                    String targetPath = defineTargetPathAfterLogin(req, user);
                    resp.sendRedirect(req.getContextPath() + targetPath);
                } else {
                    if (inconsistencies.contains(Parameters.EMAIL)) {
                        throw new AuthenticationException(AuthenticationException.VerificationExceptionType.EMAIL);
                    } else {
                        throw new AuthenticationException(AuthenticationException.VerificationExceptionType.PASS);
                    }
                }
            } catch (AuthenticationException exc) {
                inconsistencies.add(exc.getType().name());
                setDataForBadRequest(req, resp, inconsistencies, loginForm);
                ContentProvideCommands.COMMANDS.get(CRAPaths.LOGIN).handleRequest(req, resp);
                throw new AuthenticationException(exc.getType());
            }
        });
    }
        COMMANDS.put(CRAPaths.REGISTRATION, (req, resp) -> {
            RegistrationForm registrationForm = new RegistrationForm(req);
            Set<String> inconsistencies = FormValidator.validateForm(registrationForm);
            if (inconsistencies.isEmpty()) {
                UsersDBService.createUser(registrationForm);
                req.setAttribute(Attributes.SUCCESS, "");
            } else {
                setDataForBadRequest(req, resp, inconsistencies, registrationForm);
            }
            ContentProvideCommands.COMMANDS.get(CRAPaths.REGISTRATION).handleRequest(req, resp);
        });

        COMMANDS.put(CRAPaths.MAN_MAS_REGISTRATION, (req, resp) -> {
            RegistrationForm registrationForm = new RegistrationForm(req);
            Set<String> inconsistencies = FormValidator.validateForm(registrationForm);
            if (inconsistencies.isEmpty()) {
                UsersDBService.createUser(registrationForm);
                req.setAttribute(Attributes.SUCCESS, "");
            } else {
                setDataForBadRequest(req, resp, inconsistencies, registrationForm);
            }
            ContentProvideCommands.COMMANDS.get(CRAPaths.MAN_MAS_REGISTRATION).handleRequest(req, resp);
        });

        COMMANDS.put(CRAPaths.CREATE_ORDER, (req, resp) -> {
            OrderForm orderForm = new OrderForm(req);
            Set<String> inconsistencies = FormValidator.validateForm(orderForm);
            if (inconsistencies.isEmpty()) {
                OrdersDBService.addOrder(orderForm);
                Order order = OrdersDBService.getLastOrderForRegUser(orderForm.getUser().getId());
                req.setAttribute(Attributes.MADE_ORDER, order);
            } else {
                setDataForBadRequest(req, resp, inconsistencies, orderForm);
            }
            ContentProvideCommands.COMMANDS.get(CRAPaths.CREATE_ORDER).handleRequest(req, resp);
        });

        COMMANDS.put(CRAPaths.DELETE_USER, (req, resp) -> {
            int userId = Integer.parseInt(req.getParameter(Parameters.DELETING_USER_ID));
            UsersDBService.deleteUser(userId);
            resp.sendRedirect(req.getContextPath() + CRAPaths.ADMIN_HOME);
        });

        COMMANDS.put(CRAPaths.EDIT_USER, (req, resp) -> {
            UserEditingForm form = new UserEditingForm(req);
            Set<String> inconsistencies = FormValidator.validateForm(form);
            if (inconsistencies.isEmpty()) {
                User user = UsersDBService.getUserByID(form.getId());
                new UserEditor(form, user).
                        compareFirstName().
                        compareLastName().
                        compareEmail().
                        compareRole().edit();
                resp.sendRedirect(req.getContextPath() + CRAPaths.ADMIN_HOME);
            } else {
                setDataForBadRequest(req, resp, inconsistencies, form);
                ContentProvideCommands.COMMANDS.get(CRAPaths.EDIT_USER).handleRequest(req, resp);
            }
        });

        COMMANDS.put(CRAPaths.EDIT_ORDER, (req, resp) -> {
            OrderEditingForm form = new OrderEditingForm(req);
            Set<String> inconsistencies = FormValidator.validateForm(form);
            EditingOrderValidator.checkIfNeedMasterForThisStatus(form, inconsistencies);
            EditingOrderValidator.checkIfNeedPreviousPrice(form, inconsistencies);
            if (inconsistencies.isEmpty()) {
                Order order = OrdersDBService.getOrderById(form.getId());
                new OrderEditor(form, order).
                        comparePrice().
                        compareMasters().
                        compareStatus().
                        compareManagerComment().edit();
                resp.sendRedirect(req.getContextPath() + CRAPaths.MANAGER_HOME);
            } else {
                List<User> masters = UsersDBService.getUsersByRole(Role.MASTER);
                Order order = OrdersDBService.getOrderById(form.getId());
                req.setAttribute(Attributes.CUR_ORDER_STATUS, order.getStatus());
                req.setAttribute(Attributes.MASTERS, masters);
                setDataForBadRequest(req, resp, inconsistencies, form);
                ContentProvideCommands.COMMANDS.get(CRAPaths.EDIT_ORDER).handleRequest(req, resp);
            }
        });

        COMMANDS.put(CRAPaths.EDIT_STATUS, (req, resp) -> {
            String status = req.getParameter(Parameters.STATUS);
            String orderID = req.getParameter(Parameters.ORDER_ID);
            if (status.equals(OrderStatus.REPAIR_WORK.name())) {
                OrdersDBService.editOrderStatus(orderID, status);
            } else if (status.equals(OrderStatus.REPAIR_COMPLETED.name())) {
                OrdersDBService.editOrderStatusCompletionDate(orderID, status, LocalDateTime.now());
            }
            resp.sendRedirect(req.getContextPath() + CRAPaths.MASTER_HOME);
        });
*/
        COMMANDS.put(CRAPaths.REVIEWS, (form, bindingResult, model)-> {

            if (!bindingResult.hasErrors()) {

                //ReviewsDBService.addReview(form);
                //req.setAttribute(Attributes.SUCCESS, "");

                model.addAttribute(Attributes.SUCCESS);
                //setDataForBadRequest(req, resp, inconsistencies, form);
            }
            model.addAttribute(Attributes.MAIN_BLOCK, CRAPaths.REVIEWS);

            return ContentProvideCommands.COMMANDS.get(CRAPaths.REVIEWS).handleRequest(model);
        });
    }

    private DataHandleCommands() {
    }

   /* private static void setDataForBadRequest(HttpServletRequest req, HttpServletResponse resp, Set<String> inconsistencies, Form form) {
        req.setAttribute(Attributes.INCONSISTENCIES, inconsistencies);
        req.setAttribute(Attributes.PREV_FORM, form);
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    private static String defineTargetPathAfterLogin(HttpServletRequest req, User user) {
        switch (user.getRole()) {
            case CUSTOMER:
                String path = (String) req.getSession().getAttribute(Attributes.TO_CREATE_ORDER);
                if (path != null) {
                    return path;
                } else {
                    return CRAPaths.CUSTOMER_HOME;
                }
            case ADMIN:
                return CRAPaths.ADMIN_HOME;
            case MANAGER:
                return CRAPaths.MANAGER_HOME;
            case MASTER:
                return CRAPaths.MASTER_HOME;
            default:
                return CRAPaths.HOME;
        }
    }

    private static void addUserToSession(HttpServletRequest req, User user) {
        HttpSession session = req.getSession();
        session.setAttribute(Attributes.USER, user);
    }*/
}