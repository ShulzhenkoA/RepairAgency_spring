package ua.javaexternal_shulzhenko.repair_agency.services.auth_success;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import ua.javaexternal_shulzhenko.repair_agency.constants.Attributes;
import ua.javaexternal_shulzhenko.repair_agency.constants.CRAPaths;
import ua.javaexternal_shulzhenko.repair_agency.entities.user.User;

import static ua.javaexternal_shulzhenko.repair_agency.services.database.UsersDBService.getUserInfoByEmail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Service
@Getter
@Setter
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp,
                                        Authentication authentication) throws IOException {
        setUserInfoToSession(req, authentication);
        String targetUrl = defineTargetUrl(authentication);
        redirectStrategy.sendRedirect(req, resp, targetUrl);
    }

    private void setUserInfoToSession(HttpServletRequest req, Authentication authentication) {
        User user = retrieveUser(authentication);
        HttpSession session = req.getSession();
        session.setAttribute(Attributes.USER, user);
    }

    private User retrieveUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return getUserInfoByEmail(email);
    }

    private String defineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String strAuth = authority.getAuthority();
            switch (strAuth) {
                case "CUSTOMER":
                    return CRAPaths.CUSTOMER_HOME;
                case "MASTER":
                    return CRAPaths.MASTER_HOME;
                case "MANAGER":
                    return CRAPaths.MANAGER_HOME;
                case "ADMIN":
                    return CRAPaths.ADMIN_HOME;
                default:
                    return CRAPaths.HOME;
            }
        }
        return CRAPaths.HOME;
    }
}
