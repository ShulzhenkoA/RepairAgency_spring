package ua.javaexternal_shulzhenko.repair_agency.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.constants.Parameters;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.services.auth_entry_point.CustomAuthenticationEntryPoint;
import ua.javaexternal_shulzhenko.repair_agency.services.auth_failure.CustomAuthenticationFailureHandler;
import ua.javaexternal_shulzhenko.repair_agency.services.auth_success.CustomAuthenticationSuccessHandler;
import ua.javaexternal_shulzhenko.repair_agency.services.database.UserDatabaseService;

import static ua.javaexternal_shulzhenko.repair_agency.services.encoding.PasswordEncodingService.*;

@Configuration
@EnableWebSecurity
public class CraWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    UserDatabaseService userDatabaseService;
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    CustomAuthenticationFailureHandler authenticationFailureHandler;
    CustomAuthenticationEntryPoint entryPoint;

    @Autowired
    public CraWebSecurityConfiguration(UserDatabaseService userDatabaseService,
                                       CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                                       CustomAuthenticationFailureHandler authenticationFailureHandler,
                                       CustomAuthenticationEntryPoint entryPoint) {

        this.userDatabaseService = userDatabaseService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.entryPoint = entryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        CRAPaths.CUSTOMER_HOME, CRAPaths.CREATE_ORDER, CRAPaths.CUSTOMER_ORDER_HISTORY)
                        .hasAuthority(Role.CUSTOMER.name())
                .antMatchers(
                        CRAPaths.ADMIN_HOME, CRAPaths.MAN_MAS_REGISTRATION, CRAPaths.EDIT_USER,
                        CRAPaths.DELETE_USER)
                        .hasAuthority(Role.ADMIN.name())
                .antMatchers(
                        CRAPaths.MASTER_HOME, CRAPaths.MASTER_COMPLETED_ORDERS, CRAPaths.EDIT_STATUS)
                        .hasAuthority(Role.MASTER.name())
                .antMatchers(
                        CRAPaths.MANAGER_HOME, CRAPaths.ACTIVE_ORDERS, CRAPaths.ORDER_HISTORY,
                        CRAPaths.EDIT_ORDER, CRAPaths.CUSTOMERS, CRAPaths.MASTERS)
                        .hasAuthority(Role.MANAGER.name())
                .antMatchers(CRAPaths.LOGIN, CRAPaths.REGISTRATION)
                        .anonymous()
                .antMatchers(CRAPaths.ERROR).denyAll()
        .and()
                .csrf().disable()
                .formLogin()
                .loginPage(CRAPaths.LOGIN)
                .usernameParameter(Parameters.EMAIL)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
        .and()
                .logout()
                .logoutUrl(CRAPaths.LOGOUT)
                .logoutSuccessUrl(CRAPaths.HOME)
                .invalidateHttpSession(true)
        .and()
                .exceptionHandling()
                .authenticationEntryPoint(entryPoint);
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDatabaseService).passwordEncoder(gerBCryptPasswordEncoder());
    }
}
