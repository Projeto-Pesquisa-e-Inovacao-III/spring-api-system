package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/api/controle/admin/dev")
@Profile("dev")
public class AdminDevController {
    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final UsuarioService usuarioService;
    private final PersonalService personalService;

    public AdminDevController(AdminRepository adminRepository, AdminService adminService, UsuarioService usuarioService, PersonalService personalService) {
        this.adminRepository = adminRepository;
        this.adminService = adminService;
        this.usuarioService = usuarioService;
        this.personalService = personalService;
    }

        @PostMapping("/criar-dono")
        public HashMap<String, String> criarDono() {
            Usuario admin = new Usuario();
            usuarioService.aplicarSenhaCriptografada(admin, "admin123");
            admin.setEmail("fabio.admin@email.com");
            admin.setNome("Fábio");
            admin.setSexo("Masculino");
            admin.setRoles(Set.of(Role.ADMIN, Role.PERSONAL, Role.DONO));
            admin.setDataNascimento(LocalDate.parse("1990-01-01"));

            admin = usuarioService.salvarUsuario(admin);
            admin = adminService.createProfile(admin).getUsuario();
            personalService.createProfile(admin, "CREF12345");

            Usuario finalAdmin = admin;
            return new HashMap<>(){{
                put("senha", "admin123");
                put("email", finalAdmin.getEmail());
            }};
        }
}
