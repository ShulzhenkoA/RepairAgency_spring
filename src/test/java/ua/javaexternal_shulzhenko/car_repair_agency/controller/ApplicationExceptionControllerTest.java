package ua.javaexternal_shulzhenko.car_repair_agency.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.CRA_JSPFiles;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ApplicationExceptionControllerTest {

    @InjectMocks
    private ApplicationExceptionController controller;

    @MockBean
    private HttpServletRequest req;

    @Test
    public void handlingException_returnsCorePage() {
        String returnedResource = controller.handleException(req, new Exception());
        assertEquals(CRA_JSPFiles.CORE_PAGE, returnedResource);
    }

    @Test
    public void handlingException_setsPage500MainBlock() {
        controller.handleException(req, new Exception());
        verify(req, times(1)).setAttribute(Attributes.MAIN_BLOCK, CRA_JSPFiles.PAGE500);
    }
}