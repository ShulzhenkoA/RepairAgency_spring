package ua.javaexternal_shulzhenko.repair_agency.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class GetRequestsControllerTest {

    @Autowired
    private GetRequestsController controller;

    @MockBean
    private HttpServletRequest req;

    @MockBean
    private Model model;

    @Test
    public void handlingLanguageRequest_returnsRedirectReferer() {
        when(req.getHeader(CommonConstants.REFERER)).thenReturn("/referer_path");

        String returnedResource = controller.handleLanguageChanging(req);

        assertEquals(CommonConstants.REDIRECT + "/referer_path", returnedResource);
    }

    @ParameterizedTest
    @ValueSource(strings = {CRAPaths.REVIEWS, CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS,
            CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS})
    public void handlingPaginatedRequest_setsPageNumAttribute(String resource) {
        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);

        controller.handlePaginatedRequest("1", req, model);

        verify(model, times(1)).addAttribute(Attributes.PAGE_NUM, "1");
    }

    @ParameterizedTest
    @ValueSource(strings = {CRAPaths.REVIEWS, CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS,
            CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS})
    public void handlingPaginatedRequest_setsUriAttribute(String resource) {
        when(req.getServletPath()).thenReturn(resource);
        when(req.getRequestURI()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);

        controller.handlePaginatedRequest("1", req, model);

        verify(model, times(1)).addAttribute(Attributes.URI, resource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.CUSTOMER_HOME, CRAPaths.CUSTOMER_ORDER_HISTORY,
            CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public void handlingPaginatedRequestsForConcreteUser_setsPageNumAttribute(String resource) {
        User user = User.builder().id(1).build();

        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(user.getId());

        controller.handlePaginatedRequestForConcreteUser("1", user, req, model);

        verify(model, times(1)).addAttribute(Attributes.PAGE_NUM, "1");
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.CUSTOMER_HOME, CRAPaths.CUSTOMER_ORDER_HISTORY,
            CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public void handlingPaginatedRequestsForConcreteUser_setsUriAttribute(String resource) {
        User user = User.builder().id(1).build();

        when(req.getServletPath()).thenReturn(resource);
        when(req.getRequestURI()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(user.getId());

        controller.handlePaginatedRequestForConcreteUser("1", user, req, model);

        verify(model, times(1)).addAttribute(Attributes.URI, resource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.CUSTOMER_HOME, CRAPaths.CUSTOMER_ORDER_HISTORY,
            CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public void handlingPaginatedRequestsForConcreteUser_setsUserIdAttribute(String resource) {
        User user = User.builder().id(1).build();

        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(user.getId());

        controller.handlePaginatedRequestForConcreteUser("1", user, req, model);

        verify(model, times(1)).addAttribute(Attributes.USER_ID, user.getId());
    }
}