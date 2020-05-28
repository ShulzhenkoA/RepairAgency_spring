package ua.javaexternal_shulzhenko.repair_agency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import ua.javaexternal_shulzhenko.repair_agency.constants.*;
import ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.impl.ContentProvideCommands;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class GetRequestsController {

    @GetMapping({CRAPaths.START, CRAPaths.HOME, CRAPaths.LOGIN, CRAPaths.REGISTRATION, CRAPaths.CREATE_ORDER,
            CRAPaths.ADMIN_HOME, CRAPaths.MAN_MAS_REGISTRATION, CRAPaths.EDIT_USER, CRAPaths.EDIT_ORDER})
    public String handleSimpleRequest(HttpServletRequest req, Model model) {
        String requestRecourse = req.getServletPath();
        return ContentProvideCommands.COMMANDS.get(requestRecourse).handleRequest(model);
    }

    @GetMapping({CRAPaths.REVIEWS, CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS,
            CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS})
    public String handlePaginatedRequest(@RequestParam(required = false) String page, HttpServletRequest req, Model model) {

        String requestRecourse = req.getServletPath();
        String uri = req.getRequestURI();
        model
                .addAttribute(Attributes.PAGE_NUM, page)
                .addAttribute(Attributes.URI, uri);

        return ContentProvideCommands.COMMANDS.get(requestRecourse).handleRequest(model);
    }

    @GetMapping({CRAPaths.CUSTOMER_HOME, CRAPaths.CUSTOMER_ORDER_HISTORY,
            CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public String handlePaginatedRequestForConcreteUser(
            @RequestParam(required = false) String page, @SessionAttribute(required = false) User user,
            HttpServletRequest req, Model model) {

        String requestRecourse = req.getServletPath();
        String uri = req.getRequestURI();
        model
                .addAttribute(Attributes.PAGE_NUM, page)
                .addAttribute(Attributes.URI, uri)
                .addAttribute(Attributes.USER_ID, user.getId());
        return ContentProvideCommands.COMMANDS.get(requestRecourse).handleRequest(model);
    }

    @GetMapping(CRAPaths.LANGUAGE)
    public String handleLanguageChanging(HttpServletRequest req) {
        String prev = req.getHeader(CommonConstants.REFERER);
        return CommonConstants.REDIRECT + prev;
    }
}


