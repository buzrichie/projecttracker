package org.amalitechrichmond.projecttracker.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccessChecker {

        public String getCurrentUserId() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                throw new RuntimeException("No authenticated user found.");
            }
            return auth.getName();
        }

        public boolean isOwner(String userIdFromEntity) {
            return userIdFromEntity.equals(getCurrentUserId());
        }
    }

