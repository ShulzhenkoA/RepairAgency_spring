package ua.javaexternal_shulzhenko.repair_agency.services.auth_failure;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.CommonConstants;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class CustomAuthenticationFailureHandlerTest {

    @InjectMocks
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @MockBean
    private HttpServletRequest req;

    @MockBean
    private HttpServletResponse resp;

    @MockBean
    private AuthenticationException exc;

    @Test
    public void handlingFailure_whenBadCredentials_setCorrespondingAttribute() throws IOException, ServletException {
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(CRAPaths.LOGIN)).thenReturn(requestDispatcher);
        when(exc.getMessage()).thenReturn(CommonConstants.BAD_CREDENTIALS);

        authenticationFailureHandler.onAuthenticationFailure(req, resp, exc);

        verify(req, times(1)).setAttribute(Attributes.PASS, "");
    }

    @Test
    public void handlingFailure_whenOtherExcMessage_setEmailAttribute() throws IOException, ServletException {
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(CRAPaths.LOGIN)).thenReturn(requestDispatcher);
        when(exc.getMessage()).thenReturn("Some message describing email authentication error.");

        authenticationFailureHandler.onAuthenticationFailure(req, resp, exc);

        verify(req, times(1)).setAttribute(Attributes.EMAIL, "");
    }
}