package ua.javaexternal_shulzhenko.repair_agency.configuration;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.Role;
import ua.javaexternal_shulzhenko.repair_agency.services.auth_failure.CustomAuthenticationFailureHandler;
import ua.javaexternal_shulzhenko.repair_agency.services.auth_success.CustomAuthenticationSuccessHandler;
import ua.javaexternal_shulzhenko.repair_agency.services.encoding.PasswordEncodingService;
import ua.javaexternal_shulzhenko.repair_agency.services.database.UsersDBService;

@Configuration
@EnableWebSecurity
public class CraWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    UsersDBService usersDBService;
    PasswordEncodingService passwordEncodingService;
    CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    public CraWebSecurityConfiguration(UsersDBService usersDBService, PasswordEncodingService passwordEncodingService,
                                       CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                                       CustomAuthenticationFailureHandler authenticationFailureHandler) {

        this.usersDBService = usersDBService;
        this.passwordEncodingService = passwordEncodingService;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(CRAPaths.CUSTOMER_HOME).hasAuthority(Role.CUSTOMER.name())
                .antMatchers(CRAPaths.ADMIN_HOME).hasAuthority(Role.ADMIN.name())
                .antMatchers(CRAPaths.LOGIN, CRAPaths.REGISTRATION).anonymous()
        .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .permitAll()
        .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/home");
    }

    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(usersDBService).passwordEncoder(passwordEncodingService.gerBCryptPasswordEncoder());
    }

}
