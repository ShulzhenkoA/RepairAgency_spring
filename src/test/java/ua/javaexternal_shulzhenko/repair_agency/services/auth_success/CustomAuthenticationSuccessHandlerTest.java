package ua.javaexternal_shulzhenko.repair_agency.services.auth_success;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CustomAuthenticationSuccessHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;

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

    @Test
    public void handlingSuccessLogin_withCustomer_sendToCorrespondHomePage() throws Exception {
        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_customer@mail.com")
                .param(Parameters.PASSWORD, "Customer123"))
                .andExpect(redirectedUrl(CRAPaths.CUSTOMER_HOME));
    }

    @Test
    public void handlingSuccessLogin_withAdmin_sendToCorrespondHomePage() throws Exception {
        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_admin@mail.com")
                .param(Parameters.PASSWORD, "Admin123"))
                .andExpect(redirectedUrl(CRAPaths.ADMIN_HOME));
    }

    @Test
    public void handlingSuccessLogin_withMaster_sendToCorrespondHomePage() throws Exception {
        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_master@mail.com")
                .param(Parameters.PASSWORD, "Master123"))
                .andExpect(redirectedUrl(CRAPaths.MASTER_HOME));
    }

    @Test
    public void handlingSuccessLogin_withManager_sendToCorrespondHomePage() throws Exception {
        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_manager@mail.com")
                .param(Parameters.PASSWORD, "Manager123"))
                .andExpect(redirectedUrl(CRAPaths.MANAGER_HOME));
    }
}