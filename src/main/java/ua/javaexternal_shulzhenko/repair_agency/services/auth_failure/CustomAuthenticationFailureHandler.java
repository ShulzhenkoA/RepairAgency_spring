package ua.javaexternal_shulzhenko.repair_agency.services.auth_failure;

import lombok.extern.log4j.Log4j2;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import ua.javaexternal_shulzhenko.repair_agency.constants.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Service
public final class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp,
                                        AuthenticationException exc) throws IOException, ServletException {

        if (exc.getMessage().equals(CommonConstants.BAD_CREDENTIALS)) {
            log.warn("Attempt to log in using non-existing email: " + req.getParameter(Parameters.EMAIL) +
                    "\t User-Agent: " + req.getHeader(Parameters.USER_AGENT));
            req.setAttribute(Attributes.PASS, "");
        } else {
            log.warn("Wrong password log in attempt. User: " + req.getParameter(Parameters.EMAIL) +
                    "\t User-Agent: " + req.getHeader(Parameters.USER_AGENT));
            req.setAttribute(Attributes.EMAIL, "");
        }

        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        req.setAttribute(Attributes.PREV_EMAIL, req.getParameter(Parameters.EMAIL));
        req.getRequestDispatcher(CRAPaths.LOGIN).forward(req, resp);
    }
}
