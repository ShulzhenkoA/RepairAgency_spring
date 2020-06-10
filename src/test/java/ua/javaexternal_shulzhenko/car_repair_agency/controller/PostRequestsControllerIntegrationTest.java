package ua.javaexternal_shulzhenko.car_repair_agency.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.car_repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.order.OrderStatus;
import ua.javaexternal_shulzhenko.car_repair_agency.entities.user.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class PostRequestsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_correctData_returnsFoundStatusCode() throws Exception {

        mockMvc.perform(post(CRAPaths.LOGIN)
                .param("email", "testing_customer@mail.com")
                .param("password", "Customer123"))
                .andExpect(status().isFound());
    }

    @ParameterizedTest
    @CsvSource({"testing_cust@mail.com, Customer123", "testing_customer@mail.com, Customer"})
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_incorrectData_returnsBadRequestStatusCode(String email, String password) throws Exception {

        mockMvc.perform(post(CRAPaths.LOGIN)
                .param("email", email)
                .param("password", password))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_withCustomer_sendsToCorrespondHomePage() throws Exception {

        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_customer@mail.com")
                .param(Parameters.PASSWORD, "Customer123"))
                .andExpect(redirectedUrl(CRAPaths.CUSTOMER_HOME));
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_withAdmin_sendsToCorrespondHomePage() throws Exception {

        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_admin@mail.com")
                .param(Parameters.PASSWORD, "Admin123"))
                .andExpect(redirectedUrl(CRAPaths.ADMIN_HOME));
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_withMaster_sendsToCorrespondHomePage() throws Exception {

        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_master@mail.com")
                .param(Parameters.PASSWORD, "Master123"))
                .andExpect(redirectedUrl(CRAPaths.MASTER_HOME));
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_withManager_sendsToCorrespondHomePage() throws Exception {

        mockMvc.perform(post(CRAPaths.LOGIN)
                .param(Parameters.EMAIL, "testing_manager@mail.com")
                .param(Parameters.PASSWORD, "Manager123"))
                .andExpect(redirectedUrl(CRAPaths.MANAGER_HOME));
    }

    @Test
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void registration_correctData_returnsOkStatusCode() throws Exception {

        mockMvc.perform(post(CRAPaths.REGISTRATION)
                .param("firstName", "User")
                .param("lastName", "User")
                .param("email", "user@mail.com")
                .param("password", "User1234")
                .param("passwordConfirmation", "User1234")
                .param("role", "CUSTOMER"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "'', User, user@mail.com, User1234, User1234, CUSTOMER",
            "User, '', user@mail.com, User1234, User1234, CUSTOMER",
            "User, User, usermail.com, User1234, User1234, CUSTOMER",
            "User, User, user@mail.com, User123, User1234, CUSTOMER",
            "User, User, user@mail.com, User1234, User1, CUSTOMER",
            "User, User, user@mail.com, User1234, User1234, ''"})
    public void registration_invalidFirstName_returnsBadRequestStatusCode(
            String firstName, String lastName, String email,
            String password, String passwordConfirmation, String role) throws Exception {

        mockMvc.perform(post(CRAPaths.REGISTRATION)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("passwordConfirmation", passwordConfirmation)
                .param("role", role))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "User, User, user@mail.com, User1234, User1234, MANAGER",
            "User, User, user@mail.com, User1234, User1234, MASTER"})
    @WithMockUser(authorities = {"ADMIN"})
    public void man_masRegistration_correctData_returnsOkStatusCode(
            String firstName, String lastName, String email,
            String password, String passwordConfirmation, String role) throws Exception {

        mockMvc.perform(post(CRAPaths.MAN_MAS_REGISTRATION)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("passwordConfirmation", passwordConfirmation)
                .param("role", role))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "'', User, user@mail.com, User1234, User1234, MANAGER",
            "User, '', user@mail.com, User1234, User1234, MANAGER",
            "User, User, usermail.com, User1234, User1234, MANAGER",
            "User, User, user@mail.com, User123, User1234, MANAGER",
            "User, User, user@mail.com, User1234, User1, MANAGER",
            "User, User, user@mail.com, User1234, User1234, ''"})
    @WithMockUser(authorities = {"ADMIN"})
    public void man_masRegistration_invalidFirstName_returnsBadRequestStatusCode(
            String firstName, String lastName, String email,
            String password, String passwordConfirmation, String role) throws Exception {

        mockMvc.perform(post(CRAPaths.MAN_MAS_REGISTRATION)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("password", password)
                .param("passwordConfirmation", passwordConfirmation)
                .param("role", role))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "3, User, User, user@mail.com, MANAGER",
            "3, User, User, testing_master@mail.com, MASTER",
            "3, User, User, user@mail.com, MASTER"})
    @WithMockUser(authorities = {"ADMIN"})
    public void editUser_correctData_returnsFoundStatusCode(
            String id, String firstName, String lastName, String email, String role) throws Exception {

        mockMvc.perform(post(CRAPaths.EDIT_USER)
                .param("id", id)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("role", role))
                .andExpect(status().isFound());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "3, '', User, testing_master@mail.com, MASTER",
            "3, User, '', testing_master@mail.com, MASTER",
            "3, User, '', testing_mastermail.com, MASTER",
            "3, User, User, testing_customer@mail.com, MASTER"})
    @WithMockUser(authorities = {"ADMIN"})
    public void editUser_invalidData_returnsBadRequestStatusCode(
            String id, String firstName, String lastName, String email, String role) throws Exception {

        mockMvc.perform(post(CRAPaths.EDIT_USER)
                .param("id", id)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("email", email)
                .param("role", role))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"ADMIN"})
    public void deleteUser_returnsFoundStatus() throws Exception {

        mockMvc.perform(post(CRAPaths.DELETE_USER)
                .param("userId", "3"))
                .andExpect(status().isFound());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql", "/database/TestDeleteOrders.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "BMW, x5, 2012, ENGINE_REPAIR, Some repair description",
            "OPEL, Astra, 2005, CHASSIS_REPAIR, Some repair description"})
    @WithMockUser(authorities = {"CUSTOMER"})
    public void createOrder_correctData_returnsOkStatusCode(
            String carBrand, String carModel, String carYear,
            String repairType, String repairDescription) throws Exception {

        mockMvc.perform(post(CRAPaths.CREATE_ORDER)
                .param("carBrand", carBrand)
                .param("carModel", carModel)
                .param("carYear", carYear)
                .param("repairType", repairType)
                .param("repairDescription", repairDescription)
                .sessionAttr(Attributes.USER, User
                        .builder()
                        .id(2)
                        .build()))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestPopulateUsers.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "'', x5, 2012, ENGINE_REPAIR, Some repair description",
            "BMW, '', 2012, ENGINE_REPAIR, Some repair description",
            "BMW, x5, 2101, ENGINE_REPAIR, Some repair description",
            "BMW, x5, 2012, '', Some repair description",
            "BMW, x5, 2012, ENGINE_REPAIR, ''"})
    @WithMockUser(authorities = {"CUSTOMER"})
    public void createOrder_invalidData_returnsBadRequestStatusCode(
            String carBrand, String carModel, String carYear,
            String repairType, String repairDescription) throws Exception {

        mockMvc.perform(post(CRAPaths.CREATE_ORDER)
                .param("carBrand", carBrand)
                .param("carModel", carModel)
                .param("carYear", carYear)
                .param("repairType", repairType)
                .param("repairDescription", repairDescription)
                .sessionAttr(Attributes.USER, User
                        .builder()
                        .id(2)
                        .build()))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestPopulateUsers.sql", "/database/TestPopulateOrders.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql", "/database/TestDeleteOrders.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "1, 95.50, 3, CAR_WAITING, Some manager comment",
            "1, 0.0, 0, REJECTED, Some manager comment"})
    @WithMockUser(authorities = {"MANAGER"})
    public void editOrder_correctData_returnsFoundStatusCode(
            String id, String price, String masterId, String status, String managerComment) throws Exception {

        mockMvc.perform(post(CRAPaths.EDIT_ORDER)
                .param("id", id)
                .param("price", price)
                .param("masterID", masterId)
                .param("status", status)
                .param("managerComment", managerComment))
                .andExpect(status().isFound());
    }

    @ParameterizedTest
    @Sql(value = {"/database/TestPopulateUsers.sql", "/database/TestPopulateOrders.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql", "/database/TestDeleteOrders.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @CsvSource({
            "1, '', 3, CAR_WAITING, Some manager comment",
            "1, 95.50, 0, CAR_WAITING, Some manager comment",
            "1, 95.50, 3, CAR_WAITING, ''",
            "1, 0, 3, CAR_WAITING, Some manager comment"})
    @WithMockUser(authorities = {"MANAGER"})
    public void editOrder_invalidData_returnsBadRequestStatusCode(
            String id, String price, String masterId, String status, String managerComment) throws Exception {

        mockMvc.perform(post(CRAPaths.EDIT_ORDER)
                .param("id", id)
                .param("price", price)
                .param("masterID", masterId)
                .param("status", status)
                .param("managerComment", managerComment))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql", "/database/TestPopulateOrders.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql", "/database/TestDeleteOrders.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"MASTER"})
    public void editStatus_returnsFoundStatusCode() throws Exception {

        mockMvc.perform(post(CRAPaths.EDIT_STATUS)
                .param("orderID", "1")
                .param("status", OrderStatus.REPAIR_WORK.name()))
                .andExpect(status().isFound());
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql", "/database/TestPopulateOrders.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql", "/database/TestDeleteOrders.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"CUSTOMER"})
    public void review_correctData_returnsOkStatusCode() throws Exception {

        mockMvc.perform(post(CRAPaths.REVIEWS)
                .param("reviewContent", "Some review")
                .sessionAttr(Attributes.USER, User
                        .builder()
                        .id(2)
                        .build()))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/database/TestPopulateUsers.sql", "/database/TestPopulateOrders.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = {"/database/TestDeleteUsers.sql", "/database/TestDeleteOrders.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(authorities = {"CUSTOMER"})
    public void review_invalidData_returnsBadRequestStatusCode() throws Exception {

        mockMvc.perform(post(CRAPaths.REVIEWS)
                .param("reviewContent", "")
                .sessionAttr(Attributes.USER, User
                        .builder()
                        .id(2)
                        .build()))
                .andExpect(status().isBadRequest());
    }
}