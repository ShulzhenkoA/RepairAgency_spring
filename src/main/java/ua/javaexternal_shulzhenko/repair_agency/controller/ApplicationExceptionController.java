package ua.javaexternal_shulzhenko.repair_agency.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRA_JSPFiles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@ControllerAdvice
public class ApplicationExceptionController {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public String handleSQLException(HttpServletRequest req, HttpServletResponse resp, Exception exc) {
        log.error("Internal server error: " + req.getRequestURI(), exc);
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        req.setAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE500);
        return CRA_JSPFiles.CORE_PAGE;
    }
}
