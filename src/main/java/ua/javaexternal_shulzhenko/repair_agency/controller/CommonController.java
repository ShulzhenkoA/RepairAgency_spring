package ua.javaexternal_shulzhenko.repair_agency.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ua.javaexternal_shulzhenko.repair_agency.constants.*;
import ua.javaexternal_shulzhenko.repair_agency.controller.commands.impl.ContentProvideCommands;
import ua.javaexternal_shulzhenko.repair_agency.controller.commands.impl.DataHandleCommands;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.LoginForm;
import ua.javaexternal_shulzhenko.repair_agency.entities.forms.ReviewForm;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@SessionAttributes(Attributes.USER)
public class CommonController {

    @GetMapping({CRAPaths.HOME, CRAPaths.REGISTRATION, CRAPaths.LOGIN, CRAPaths.CREATE_ORDER, CRAPaths.LOGOUT,
            CRAPaths.ADMIN_HOME, CRAPaths.MAN_MAS_REGISTRATION, CRAPaths.EDIT_USER, CRAPaths.DELETE_USER,
            CRAPaths.EDIT_STATUS, CRAPaths.EDIT_ORDER, CRAPaths.ERROR404, CRAPaths.ERROR500})
    public String handleGetRequest(HttpServletRequest req, Model model){

        String requestRecourse = req.getServletPath();

        return ContentProvideCommands.COMMANDS.get(requestRecourse).handleRequest(model);
    }

    @GetMapping({CRAPaths.REVIEWS, CRAPaths.CUSTOMER_HOME, CRAPaths.CUSTOMER_ORDER_HISTORY,
            CRAPaths.MANAGER_HOME, CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS,
            CRAPaths.ACTIVE_ORDERS, CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS})
    public String handlePaginatedGetRequest(
            @RequestParam(required = false) String page, HttpServletRequest req, Model model){

        String requestRecourse = req.getServletPath();
        String uri = req.getRequestURI();
        model.
                addAttribute(Attributes.PAGE_NUM, page).
                addAttribute(Attributes.URI, uri);

        return ContentProvideCommands.COMMANDS.get(requestRecourse).handleRequest(model);
    }

    @GetMapping(CRAPaths.LANGUAGE)
    public String handleLanguageRequest(HttpServletRequest req){
        String prev = req.getHeader("referer");
        return "redirect:" + prev;
    }

    @PostMapping(CRAPaths.LOGIN)
    public String handlePostRequest(Model model){
        model.addAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.LOGIN_MAIN_BLOCK);
        return CRA_JSPFiles.CORE_PAGE;
        //return DataHandleCommands.COMMANDS.get(requestedResource).handleRequest(model); @Valid LoginForm loginForm, BindingResult bindingResult,
    }

    @PostMapping({CRAPaths.REVIEWS})
    public String handlePostReview(ReviewForm form, Model model){

        return null;//DataHandleCommands.COMMANDS.get(CRAPaths.REVIEWS).handleForm(form,model);
    }
}


