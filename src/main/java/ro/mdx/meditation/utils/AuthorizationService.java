package ro.mdx.meditation.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ro.mdx.meditation.model.AppUser;

@Component
public class AuthorizationService {

    public AppUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof AppUser appUser) {
            return appUser;
        }
        throw new RuntimeException("No authenticated user found");
    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof AppUser appUser) {
            return appUser.getRole().equals(Role.ADMIN);
        }
        throw new RuntimeException("No authenticated user found");
    }
}
