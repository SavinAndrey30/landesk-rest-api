package ru.t1.asavin.techSupportAutomation.service.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.t1.asavin.techSupportAutomation.entity.Role;
import ru.t1.asavin.techSupportAutomation.entity.User;

import java.util.Set;

public class ServiceTestsUtil {

    public static User createUserObject(Long id, String username, String password, String userFullName, String userPhone, String userLocation,
                                        String userEmail, Set<Role> roles) {
        User user = new User();

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setUserFullName(userFullName);
        user.setUserPhone(userPhone);
        user.setUserLocation(userLocation);
        user.setUserEmail(userEmail);
        user.setRoles(roles);

        return user;
    }

    public static Authentication setupSecurityContext(User user) {
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return auth;
    }
}
