package ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRA_JSPFiles;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;
import ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.RequestHandler;
import ua.javaexternal_shulzhenko.repair_agency.entities.pagination.PaginationModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static ua.javaexternal_shulzhenko.repair_agency.controller.get_commands.impl.ContentProvideCommands.COMMANDS;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class ContentProvideCommandsTest {

    @MockBean
    private Model model;

    @Test
    public void startCommand_returnRedirectHome(){
        when(model.addAttribute(any(), any())).thenReturn(model);

        RequestHandler handler = COMMANDS.get(CRAPaths.START);
        String returnedResource = handler.handleRequest(model);

        assertEquals(CommonConstants.REDIRECT + CRAPaths.HOME, returnedResource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.HOME, CRAPaths.LOGIN, CRAPaths.REGISTRATION, CRAPaths.CREATE_ORDER,
            CRAPaths.ADMIN_HOME, CRAPaths.MAN_MAS_REGISTRATION, CRAPaths.EDIT_USER,
            CRAPaths.EDIT_ORDER, CRAPaths.REVIEWS, CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS,
            CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS, CRAPaths.CUSTOMER_HOME,
            CRAPaths.CUSTOMER_ORDER_HISTORY, CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public void command_ReturnCorePage(String command){
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);

        RequestHandler handler = COMMANDS.get(command);
        String returnedResource = handler.handleRequest(model);

        assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.HOME + ", " + CRA_JSPFiles.COMMON_HOME,
            CRAPaths.LOGIN + ", " + CRA_JSPFiles.LOGIN_MAIN_BLOCK,
            CRAPaths.REGISTRATION + ", " + CRA_JSPFiles.REGISTRATION_MAIN_BLOCK,
            CRAPaths.CREATE_ORDER + ", " + CRA_JSPFiles.ORDER_FORM,
            CRAPaths.ADMIN_HOME + ", " + CRA_JSPFiles.ADMIN_PAGE,
            CRAPaths.MAN_MAS_REGISTRATION + ", " + CRA_JSPFiles.ADMIN_PAGE,
            CRAPaths.EDIT_USER + ", " + CRA_JSPFiles.USER_EDITING_MAIN_BLOCK,
            CRAPaths.EDIT_ORDER + ", " + CRA_JSPFiles.ORDER_EDITING_MAIN_BLOCK,
            CRAPaths.REVIEWS + ", " + CRA_JSPFiles.REVIEWS,
            CRAPaths.MANAGER_HOME + ", " + CRA_JSPFiles.MANAGER_PAGE,
            CRAPaths.ACTIVE_ORDERS + ", " + CRA_JSPFiles.MANAGER_PAGE,
            CRAPaths.ORDER_HISTORY + ", " + CRA_JSPFiles.MANAGER_PAGE,
            CRAPaths.CUSTOMERS + ", " + CRA_JSPFiles.MANAGER_PAGE,
            CRAPaths.MASTERS + ", " + CRA_JSPFiles.MANAGER_PAGE,
            CRAPaths.CUSTOMER_HOME + ", " + CRA_JSPFiles.CUSTOMER_MASTER_PAGE,
            CRAPaths.CUSTOMER_ORDER_HISTORY + ", " + CRA_JSPFiles.CUSTOMER_MASTER_PAGE,
            CRAPaths.MASTER_HOME + ", " + CRA_JSPFiles.CUSTOMER_MASTER_PAGE,
            CRAPaths.MASTER_COMPLETED_ORDERS+ ", " + CRA_JSPFiles.CUSTOMER_MASTER_PAGE})
    public void command_setCorrespondFileToMainBlock(String command, String fileName){
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);

        RequestHandler handler = COMMANDS.get(command);
        handler.handleRequest(model);

        verify(model).addAttribute(Attributes.MAIN_BLOCK, fileName);
    }

    @ParameterizedTest
    @CsvSource({CRAPaths.REVIEWS, CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS,
            CRAPaths.ORDER_HISTORY, CRAPaths.CUSTOMERS, CRAPaths.MASTERS, CRAPaths.CUSTOMER_HOME,
            CRAPaths.CUSTOMER_ORDER_HISTORY, CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS})
    public void paginatedCommand_setPaginationAttribute(String command){
        when(model.addAttribute(any(), any())).thenReturn(model);
        when(model.getAttribute(Attributes.USER_ID)).thenReturn(0);

        RequestHandler handler = COMMANDS.get(command);
        handler.handleRequest(model);

        verify(model).addAttribute(Attributes.PG_MODEL, null);
    }

    /*@Test
    public void homeCommand_setReviews(){
        when(model.addAttribute(any(), any())).thenReturn(model);

        RequestHandler handler = COMMANDS.get(CRAPaths.HOME);
        handler.handleRequest(model);

        verify(model).addAttribute(eq(Attributes.REVIEWS), any);
    }*/
}