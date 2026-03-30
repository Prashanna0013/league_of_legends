package com.example.lol.security;

import com.example.lol.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CurrentUserService {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }

        if (principal instanceof String) {
            return Long.parseLong((String) principal);
        }

        return null;
    }

    public Role getCurrentRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String auth = authority.getAuthority(); // ROLE_ADMIN
            if (auth != null && auth.startsWith("ROLE_")) {
                String roleName = auth.substring("ROLE_".length());
                return Role.valueOf(roleName);
            }
        }
        return null;
    }
}

