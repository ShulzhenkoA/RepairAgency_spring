package ua.javaexternal_shulzhenko.car_repair_agency.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.CRA_JSPFiles;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ErrorsCustomViewControllerTest {

    @InjectMocks
    private ErrorsCustomViewController controller;

    @MockBean
    private HttpServletRequest req;

    @Test
    public void handlingError_returnsCorePage() {
        String returnedResource = controller.handelError(req);
        assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource);
    }

    @ParameterizedTest
    @CsvSource({
            "500" + ", " + CRA_JSPFiles.PAGE500,
            "501" + ", " + CRA_JSPFiles.PAGE500,
            "404" + ", " + CRA_JSPFiles.PAGE404,
            "403" + ", " + CRA_JSPFiles.PAGE404,
            "" + ", " + CRA_JSPFiles.PAGE500})
    public void handlingError_setsMainBlockPageCorrespondingToErrorStatus(Object errorStatus, String mainBlock) {
        when(req.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(errorStatus);
        controller.handelError(req);
        verify(req, times(1)).setAttribute(Attributes.MAIN_BLOCK, mainBlock);
    }
}