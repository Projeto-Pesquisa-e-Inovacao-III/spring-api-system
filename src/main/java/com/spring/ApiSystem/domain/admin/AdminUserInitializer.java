package com.spring.ApiSystem.domain.admin;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminUserInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminUserInitializer.class);
    private final AdminService adminUserService;
    private String email;
    private String password;

    public AdminUserInitializer(
            AdminService adminUserService,
            @Value("${init.email}") String email,
            @Value("${init.password}") String password
    ) {
        this.email = email;
        this.password = password;
        this.adminUserService = adminUserService;
    }

    @Override
    public void run(String @NonNull ... args) {
        log.info("Inicializando usuário admin inicial.");

        if (!adminUserService.existsByEmail(email)) {
            adminUserService.createAdminUser(email, password);
            email = null;
            password = null;
            return;
        }

        log.info("Usuário admin inicial já existe. Nenhuma ação necessária.");
    }
}
