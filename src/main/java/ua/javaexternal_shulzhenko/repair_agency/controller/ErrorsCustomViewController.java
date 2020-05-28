package ua.javaexternal_shulzhenko.repair_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRA_JSPFiles;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Log4j2
@Controller
public class ErrorsCustomViewController implements ErrorController {

    @RequestMapping(CRAPaths.ERROR)
    public String handelError(HttpServletRequest req){

        Object stCode = req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (stCode != null){

            int code = Integer.parseInt(stCode.toString());

            if(code <= HttpStatus.INTERNAL_SERVER_ERROR.value()){
                req.setAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE404);
            } else {
                req.setAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE500);
            }
        }else {
            req.setAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE500);
        }
        return CRA_JSPFiles.CORE_PAGE;
    }

    @Override
    public String getErrorPath() {
        return CRAPaths.ERROR;
    }
}
