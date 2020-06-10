package ua.javaexternal_shulzhenko.car_repair_agency.services.auth_success;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import ua.javaexternal_shulzhenko.car_repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
class CustomAuthenticationSuccessHandlerTest {

    @InjectMocks
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Test
    public void handlingSuccessLogin_OnAuthenticationSuccessMethod_setsUserToSession() throws IOException {
        User user = new User();

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        HttpSession session = mock(HttpSession.class);
        Authentication authentication = mock(Authentication.class);

        when(req.getSession()).thenReturn(session);

        when(authentication.getPrincipal()).thenReturn(user);

        authenticationSuccessHandler.onAuthenticationSuccess(req, resp, authentication);

        verify(session, times(1)).setAttribute(eq(Attributes.USER), refEq(user));
    }
}