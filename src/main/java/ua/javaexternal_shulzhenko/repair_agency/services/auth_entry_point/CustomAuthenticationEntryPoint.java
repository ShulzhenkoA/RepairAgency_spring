package ua.javaexternal_shulzhenko.repair_agency.services.auth_entry_point;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public final class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest req,
                         HttpServletResponse resp,
                         AuthenticationException exc) throws IOException, ServletException {
        String servletPath = req.getServletPath();
        if (servletPath.equals(CRAPaths.CUSTOMER_HOME) ||
                servletPath.equals(CRAPaths.MANAGER_HOME) ||
                    servletPath.equals(CRAPaths.MASTER_HOME) ||
                        servletPath.equals(CRAPaths.CREATE_ORDER)){

            resp.sendRedirect(req.getContextPath() + CRAPaths.LOGIN);
        }else {

            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            req.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            req.getRequestDispatcher(CRAPaths.ERROR).forward(req, resp);
        }
    }
}
