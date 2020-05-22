package ua.javaexternal_shulzhenko.repair_agency.entities.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    MANAGER,
    MASTER,
    CUSTOMER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}