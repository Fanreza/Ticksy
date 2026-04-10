package com.ticksy.util;

import com.ticksy.model.User;

public class SessionManager {

    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentRole() {
        if (currentUser == null || currentUser.getRole() == null) return "";
        return currentUser.getRole().getName();
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(getCurrentRole());
    }

    public static boolean isAgent() {
        return "AGENT".equals(getCurrentRole());
    }

    public static boolean isUser() {
        return "USER".equals(getCurrentRole());
    }

    public static boolean canAccessMasterData() {
        return isAdmin();
    }

    public static boolean canAccessReports() {
        return isAdmin();
    }

    public static boolean canAssignTickets() {
        return isAdmin() || isAgent();
    }

    public static boolean canManageUsers() {
        return isAdmin();
    }

    public static boolean canViewAllTickets() {
        return isAdmin() || isAgent();
    }
}
