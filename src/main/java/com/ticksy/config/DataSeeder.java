package com.ticksy.config;

import com.ticksy.model.*;
import com.ticksy.repository.*;

import java.util.List;

public class DataSeeder {

    public static void seed() {
        seedRoles();
        seedStatuses();
        seedPriorities();
        seedCategories();
        seedDepartments();
        seedAdminUser();
    }

    private static void seedRoles() {
        RoleRepository repo = new RoleRepository();
        if (repo.count() > 0) return;

        repo.save(new Role("ADMIN", "System Administrator"));
        repo.save(new Role("AGENT", "IT Support Agent"));
        repo.save(new Role("USER", "Employee"));
    }

    private static void seedStatuses() {
        StatusRepository repo = new StatusRepository();
        if (repo.count() > 0) return;

        repo.save(new Status("OPEN", "Ticket is open"));
        repo.save(new Status("ASSIGNED", "Ticket has been assigned"));
        repo.save(new Status("IN_PROGRESS", "Agent is working on it"));
        repo.save(new Status("RESOLVED", "Ticket has been resolved"));
        repo.save(new Status("CLOSED", "Ticket is closed"));
    }

    private static void seedPriorities() {
        PriorityRepository repo = new PriorityRepository();
        if (repo.count() > 0) return;

        repo.save(new Priority("LOW", 1, "#22c55e"));
        repo.save(new Priority("MEDIUM", 2, "#f59e0b"));
        repo.save(new Priority("HIGH", 3, "#f97316"));
        repo.save(new Priority("CRITICAL", 4, "#ef4444"));
    }

    private static void seedCategories() {
        CategoryRepository repo = new CategoryRepository();
        if (repo.count() > 0) return;

        repo.save(new Category("Hardware", "Hardware related issues"));
        repo.save(new Category("Software", "Software related issues"));
        repo.save(new Category("Network", "Network and connectivity issues"));
        repo.save(new Category("Account", "Account and access issues"));
        repo.save(new Category("Other", "Other issues"));
    }

    private static void seedDepartments() {
        DepartmentRepository repo = new DepartmentRepository();
        if (repo.count() > 0) return;

        repo.save(new Department("IT", "Information Technology"));
        repo.save(new Department("HR", "Human Resources"));
        repo.save(new Department("Finance", "Finance Department"));
        repo.save(new Department("Operations", "Operations Department"));
    }

    private static void seedAdminUser() {
        UserRepository userRepo = new UserRepository();
        if (userRepo.findByUsername("admin").isPresent()) return;

        RoleRepository roleRepo = new RoleRepository();
        List<Role> adminRoles = roleRepo.searchByName("ADMIN");
        if (adminRoles.isEmpty()) return;

        DepartmentRepository deptRepo = new DepartmentRepository();
        List<Department> itDepts = deptRepo.searchByName("IT");

        User admin = new User();
        admin.setUsername("admin");
        admin.setPasswordHash("admin123");
        admin.setFullName("System Administrator");
        admin.setEmail("admin@ticksy.com");
        admin.setPhone("000-000-0000");
        admin.setRole(adminRoles.get(0));
        admin.setActive(true);
        if (!itDepts.isEmpty()) {
            admin.setDepartment(itDepts.get(0));
        }

        userRepo.save(admin);
    }
}
