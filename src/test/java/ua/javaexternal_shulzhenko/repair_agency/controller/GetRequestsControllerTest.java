/*package ua.javaexternal_shulzhenko.repair_agency.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRA_JSPFiles;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class GetRequestsControllerTest {

    @InjectMocks
    private GetRequestsController controller;

    @MockBean
    private HttpServletRequest req;

    @MockBean
    private Model model;

    @Test
    public void handlingStartRequest_returnRedirectHome(){
        when(req.getServletPath()).thenReturn(CRAPaths.START);

        String returnedResource = controller.handleSimpleRequest(req, model);

        assertEquals(CommonConstants.REDIRECT + CRAPaths.HOME, returnedResource);
    }

    @Test
    public void handlingLanguageRequest_returnRedirectReferer(){
        when(req.getHeader(CommonConstants.REFERER)).thenReturn("/referer_path");

        String returnedResource = controller.handleLanguageChanging(req);

        assertEquals(CommonConstants.REDIRECT + "/referer_path", returnedResource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.HOME, CRAPaths.LOGIN, CRAPaths.REGISTRATION, CRAPaths.CREATE_ORDER,
            CRAPaths.ADMIN_HOME, CRAPaths.MAN_MAS_REGISTRATION, CRAPaths.EDIT_USER, CRAPaths.EDIT_ORDER})
    public void handlingSimpleRequests_ReturnCorePage(String resource){
        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);

        String returnedResource = controller.handleSimpleRequest(req, model);

        assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.REVIEWS, CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS,
            CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS})
    public void handlingPaginatedRequests_ReturnCorePage(String resource){
        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);

        String returnedResource = controller.handleSimpleRequest(req, model);

        assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.CUSTOMER_HOME, CRAPaths.CUSTOMER_ORDER_HISTORY,
            CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public void handlingPaginatedRequestsForConcreteUser_ReturnCorePage(String resource){
        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);

        String returnedResource = controller.handleSimpleRequest(req, model);

        assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource);
    }

    @ParameterizedTest
    @CsvSource({
            CRAPaths.LOGIN + ", " + CRA_JSPFiles.LOGIN_MAIN_BLOCK,
            CRAPaths.REGISTRATION + ", " + CRA_JSPFiles.REGISTRATION_MAIN_BLOCK,
            CRAPaths.EDIT_USER + ", " + CRA_JSPFiles.USER_EDITING_MAIN_BLOCK,
            CRAPaths.EDIT_ORDER + ", " + CRA_JSPFiles.ORDER_EDITING_MAIN_BLOCK,
            CRAPaths.CREATE_ORDER + ", " + CRA_JSPFiles.ORDER_FORM,
            CRAPaths.MAN_MAS_REGISTRATION + ", " + CRA_JSPFiles.ADMIN_PAGE})
    public void handlingSimpleRequests_setsCorrespondingMainBlockReturnCorePage(String resource, String mainBlock){
        when(req.getServletPath()).thenReturn(resource);
        when(model.addAttribute(any(), any())).thenReturn(model);

        assertEquals(CRA_JSPFiles.CORE_PAGE, controller.handleSimpleRequest(req, model));
        verify(model, times(1)).addAttribute(Attributes.MAIN_BLOCK, mainBlock);

    }




}*/