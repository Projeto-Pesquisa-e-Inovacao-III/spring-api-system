package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.mapper.CpfMapper;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.personal.mapper.PersonalMapper;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.telefone.dto.request.ReqCadastrarTelefoneDTO;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.config.filter.FilterService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

@RestController
@RequestMapping("/api/controle/admin/dev")
@Profile("dev")
public class AdminDevController {

    // Controller Inimigo do SonarQube

    private final AdminRepository adminRepository;
    private final AdminService adminService;
    private final UsuarioService usuarioService;
    private final PersonalService personalService;
    private final JpaUserDetailsService userDetailsService;
    private final AlunoService alunoService;
    private final CpfMapper cpfMapper;
    private final FilterService filterService;
    private final PersonalMapper personalMapper;

    public AdminDevController(AdminRepository adminRepository, AdminService adminService, UsuarioService usuarioService, PersonalService personalService, JpaUserDetailsService userDetailsService, AlunoService alunoService, CpfMapper cpfMapper, FilterService filterService, PersonalMapper personalMapper) {
        this.adminRepository = adminRepository;
        this.adminService = adminService;
        this.usuarioService = usuarioService;
        this.personalService = personalService;
        this.userDetailsService = userDetailsService;
        this.alunoService = alunoService;
        this.cpfMapper = cpfMapper;
        this.filterService = filterService;
        this.personalMapper = personalMapper;
    }

        @PostMapping("/criar-dono")
        public HashMap<String, String> criarDono() {
            String email = "fabio.admin@email.com";
            String senha = "admin123";
            if(!usuarioService.emailExiste(email)) {
                Usuario admin = new Usuario();
                usuarioService.aplicarSenhaCriptografada(admin, "admin123");
                admin.setEmail(email);
                admin.setNome("Fábio");
                admin.setSexo("Masculino");
                admin.setRoles(Set.of(Role.ADMIN, Role.PERSONAL, Role.DONO));
                admin.setDataNascimento(LocalDate.parse("1990-01-01"));

                admin = usuarioService.salvarUsuario(admin);
                admin = adminService.createProfile(admin).getUsuario();
                personalService.createProfile(admin, "CREF12345");
            }
            return new HashMap<>(){{
                put("senha", senha);
                put("email", email);
            }};
        }

        @Operation(summary = "Criar Personal Retornando a senha", description = "Endpoint para cadastro de usuários no sistema")
        @PostMapping("/usuarios/personal")
        public ResponseEntity<HashMap<String, Object>> cadastrarUsuario(
                @Valid @RequestBody ReqCadastroPersonalDTO cadastroUsuarioDTO
        ) {
            String senha = "Personal123!";
            Personal usuarioEntity = personalMapper.toEntity(cadastroUsuarioDTO);

            usuarioEntity.setSenha(senha);

            ReqCadastrarTelefoneDTO telefoneDTO = cadastroUsuarioDTO.telefone();

            Telefone telefone = new Telefone();
            telefone.setPais(telefoneDTO.pais());
            telefone.setDdd(telefoneDTO.ddd());
            telefone.setNumero(telefoneDTO.numero());
            telefone.setUsuario(usuarioEntity.getUsuario());

            usuarioEntity.getTelefones().add(telefone);

            Personal personalSalvo = personalService.cadastrarPersonal(usuarioEntity);

            // TODO: CHAMAR EMAIl SERVICE

            // crimes de guerra
            HashMap<String, Object> resposta = new HashMap<>();
            resposta.put("Personal", personalMapper.toDtoCadastrarPersonal(personalSalvo));
            resposta.put("Senha", senha);

            return ResponseEntity.ok(resposta);
        }
}
