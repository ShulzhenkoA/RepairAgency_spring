package ua.javaexternal_shulzhenko.car_repair_agency.services.auth_entry_point;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.CRAPaths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@SpringBootTest
class CustomAuthenticationEntryPointTest {

    @InjectMocks
    CustomAuthenticationEntryPoint authenticationEntryPoint;

    @MockBean
    private HttpServletRequest req;

    @MockBean
    private HttpServletResponse resp;

    @MockBean
    private AuthenticationException exc;

    @Test
    public void promptingResource_whenCustomerHome_redirectsToLogin() throws IOException, ServletException {
        when(req.getServletPath()).thenReturn(CRAPaths.CUSTOMER_HOME);
        when(req.getContextPath()).thenReturn("RepairAgency");

        authenticationEntryPoint.commence(req, resp, exc);

        verify(resp, times(1)).sendRedirect("RepairAgency" + CRAPaths.LOGIN);
    }

    @Test
    public void promptingResource_whenMasterHome_redirectsToLogin() throws IOException, ServletException {
        when(req.getServletPath()).thenReturn(CRAPaths.MASTER_HOME);
        when(req.getContextPath()).thenReturn("RepairAgency");

        authenticationEntryPoint.commence(req, resp, exc);

        verify(resp, times(1)).sendRedirect("RepairAgency" + CRAPaths.LOGIN);
    }

    @Test
    public void promptingResource_whenManagerHome_redirectsToLogin() throws IOException, ServletException {
        when(req.getServletPath()).thenReturn(CRAPaths.MANAGER_HOME);
        when(req.getContextPath()).thenReturn("RepairAgency");

        authenticationEntryPoint.commence(req, resp, exc);

        verify(resp, times(1)).sendRedirect("RepairAgency" + CRAPaths.LOGIN);
    }

    @Test
    public void promptingResource_whenCreateOrder_redirectsToLogin() throws IOException, ServletException {
        when(req.getServletPath()).thenReturn(CRAPaths.CREATE_ORDER);
        when(req.getContextPath()).thenReturn("RepairAgency");

        authenticationEntryPoint.commence(req, resp, exc);

        verify(resp, times(1)).sendRedirect("RepairAgency" + CRAPaths.LOGIN);
    }

    @ParameterizedTest
    @ValueSource(strings = {CRAPaths.CUSTOMER_ORDER_HISTORY, CRAPaths.EDIT_ORDER,
            CRAPaths.ACTIVE_ORDERS, CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS,
            CRAPaths.MASTERS, CRAPaths.MASTER_COMPLETED_ORDERS, CRAPaths.EDIT_STATUS,
            CRAPaths.ADMIN_HOME, CRAPaths.DELETE_USER, CRAPaths.EDIT_USER,
            CRAPaths.MAN_MAS_REGISTRATION, CRAPaths.LOGIN, CRAPaths.REGISTRATION, CRAPaths.ERROR})
    public void promptingResource_whenNotAllowed_sendsTo404ErrorPage(String resource) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(req.getServletPath()).thenReturn(resource);
        when(req.getRequestDispatcher(CRAPaths.ERROR)).thenReturn(requestDispatcher);

        authenticationEntryPoint.commence(req, resp, exc);

        verify(resp, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(requestDispatcher, times(1)).forward(req, resp);
    }
}