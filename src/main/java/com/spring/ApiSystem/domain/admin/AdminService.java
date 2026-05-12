package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.admin.dto.request.ReqAdicionarRoleDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroAdminDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResRoleNeedDataDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResUsuarioWithRolesResponseDTO;
import com.spring.ApiSystem.domain.admin.exception.AdminNaoExisteException;
import com.spring.ApiSystem.domain.aluno.Aluno;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.mapper.CpfMapper;
import com.spring.ApiSystem.domain.personal.Personal;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.telefone.Telefone;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.infrastructure.email.dto.Email;
import com.spring.ApiSystem.shared.infrastructure.email.service.EmailService;
import com.spring.ApiSystem.shared.security.PasswordGenerator;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class AdminService {
    private static final Logger log = LogManager.getLogger(AdminService.class);
    private final AdminRepository adminRepository;
    private final UsuarioService usuarioService;
    private final PersonalService personalService;
    private final JpaUserDetailsService userDetailsService;
    private final AlunoService alunoService;
    private final CpfMapper cpfMapper;
    private final EmailService emailService;

    public AdminService(AdminRepository adminRepository, UsuarioService usuarioService, PersonalService personalService, JpaUserDetailsService userDetailsService, AlunoService alunoService, CpfMapper cpfMapper, EmailService emailService) {
        this.adminRepository = adminRepository;
        this.usuarioService = usuarioService;
        this.personalService = personalService;
        this.userDetailsService = userDetailsService;
        this.alunoService = alunoService;
        this.cpfMapper = cpfMapper;
        this.emailService = emailService;
    }

    @Transactional
    public Usuario addRoleToUser(Role role, Long userId, ReqAdicionarRoleDTO dto){
        if(role.equals(Role.DONO)) throw new IllegalArgumentException("Não é permitido adicionar a role DONO a um usuário.");
        Admin admin = userDetailsService.getCurrentAdmin();

        Usuario usuario = usuarioService.buscarUsuarioPorId(userId);

        if(usuario.isRole(role)){
            throw new IllegalArgumentException("O usuário já possui a role: " + role);
        }

        log.info("O Admin de id {} está adicionando a role {} ao usuário de id {}", admin.getId(), role, userId);

        usuarioService.addRoleToUsuario(usuario, role);

        if(hasProfileFromThisRole(userId, role)){
            log.info("O Usuario id {} tem o profile daquela role, ativando profile.", userId);
            switch (role) {
                case ALUNO -> alunoService.enableProfile(userId);
                case PERSONAL -> personalService.enableProfile(userId);
                case ADMIN -> enableProfile(userId);
            }
            return usuario;
        }

        // criar profile se não tiver um existente
        log.info("O Usuario id {} não tem o profile daquela role, criando profile.", userId);
        switch (role) {
            case ALUNO -> {
                if(dto.cpf() == null || dto.cpf().isBlank()){
                    throw new IllegalArgumentException("CPF é obrigatório para criar profile de aluno.");
                }
                alunoService.createProfile(usuario, cpfMapper.toCpf(dto.cpf()));
            }
            case PERSONAL -> {
                if(dto.cref() == null || dto.cref().isBlank()){
                    throw new IllegalArgumentException("CREF é obrigatório para criar profile de personal.");
                }
                personalService.createProfile(usuario, dto.cref());
            }
            case ADMIN -> createProfile(usuario);
        }

        return usuario;
    }

    @Transactional
    public void softDelete(Long userId){
        Admin admin = userDetailsService.getCurrentAdmin();

        Usuario usuario = usuarioService.buscarUsuarioPorId(userId);

        if(usuario.isDono()){
            throw new IllegalArgumentException("Não é permitido remover um usuário com a role DONO.");
        }

        if(isTargetSelfAdmin(usuario, admin)){
            throw new IllegalArgumentException("Um administrador não pode se auto-remover.");
        }

        usuario.setAtivo(false);
        if(usuario.isAdmin()){
            usuario.removeRole(Role.ADMIN);
            disableProfile(userId);
        }

        usuarioService.salvarUsuario(usuario);
        log.info("O Admin de id {} inativou (soft delete) o usuário de id {}", admin.getId(), userId);
    }

    @Transactional
    public Usuario removeRole(Role role, Long userId){
        Admin admin = userDetailsService.getCurrentAdmin();

        Usuario usuario = usuarioService.buscarUsuarioPorId(userId);

        if(usuario.isDono() && (role.equals(Role.DONO) || role.equals(Role.ADMIN))){
            throw new IllegalArgumentException("Não é permitido remover a role admnistradora de um usuário com a role DONO.");
        }

        if(isTargetSelfAdmin(usuario, admin) && role.equals(Role.ADMIN)){
            throw new IllegalArgumentException("Um administrador não pode remover sua propria role.");
        }

        if(usuario.isAdmin()){
            throw new IllegalArgumentException("Um admistrador não pode ter a role de admistrador removida, apenas a role DONO posso fazer isso.");
        }

        if(usuario.hasOnlyRole(role)){
            throw new IllegalArgumentException("Não é permitido remove a ultima role do usuario. Caso deseje deletar-lo utilize o meio de deletar.");
        }

        usuario = usuarioService.removeRoleFromUsuario(usuario, role);

        log.info("O Admin de id {} removeu a role {} do usuário de id {}", admin.getId(), role, userId);

        return usuario;
    }

    @Transactional
    public ResCadastrarPersonalDTO createPersonalUser(ReqCadastroPersonalDTO personalDTO){
        Admin admin = userDetailsService.getCurrentAdmin();
        ResCadastrarPersonalDTO personalCreated = personalService.cadastrarPersonalDto(personalDTO);
        log.info("O Admin de id {} criou o personal de id {}", admin.getId(), personalCreated.id());
        return personalCreated;
    }

    private boolean isTargetSelfAdmin(Usuario target, Admin admin){
        return target.getId().equals(admin.getId());
    }

    public ResRoleNeedDataDTO verifyNeedExtraData(Long usuarioId, Role role){
        boolean needData = false;
        HashMap<String, String> fields = null;

        if(!hasProfileFromThisRole(usuarioId, role)){
            switch (role) {
                case ALUNO -> {
                    needData = true;
                    fields = new HashMap<>();
                    fields.put("cpf", "String");
                }
                case PERSONAL -> {
                    needData = true;
                    fields = new HashMap<>();
                    fields.put("cref", "String");
                }
            }
        }

        return new ResRoleNeedDataDTO(needData, fields);
    }

    private boolean hasProfileFromThisRole(Long usuarioid, Role role){
        Usuario usuario = usuarioService.buscarUsuarioPorId(usuarioid);

        return switch (role) {
            case ALUNO -> usuario.getAluno() != null;
            case PERSONAL -> usuario.getPersonal() != null;
            case ADMIN -> usuario.getAdmin() != null;
            default -> false;
        };
    }

    private Admin findById(Long usuarioId){
        return adminRepository.findById(usuarioId).orElseThrow(() -> new AdminNaoExisteException("Admin não encontrado"));
    }

    @Transactional
    public void enableProfile(Long id) {
        Admin admin = findById(id);
        admin.setProfileAtivo(true);
        adminRepository.save(admin);
    }

    @Transactional
    public void disableProfile(Long id) {
        Admin admin = findById(id);
        admin.setProfileAtivo(false);
        adminRepository.save(admin);
    }

    public Admin createProfile(Usuario usuario){
        return adminRepository.save(new Admin(null, usuario));
    }

    @Transactional
    public Page<ResUsuarioWithRolesResponseDTO> listUsersWithFilter(String nome, String email, Role role, Pageable pageable) {
        userDetailsService.getCurrentAdmin();
        Page<Usuario> usuarios;
        if (role != null) {
            usuarios = usuarioService.findAllUsersPagedWithRolesAndRoleAndFilters(pageable, role, nome, email);
        } else {
            usuarios = usuarioService.findAllUsersPagedWithRolesAndFilters(pageable, nome, email);
        }
        return usuarios.map(usuario -> {
            String cpf = null;
            String anamnese = null;
            String cref = null;
            if (usuario.isAluno() && usuario.getAluno() != null) {
                Aluno aluno = usuario.getAluno();
                if (aluno.getCpf() != null) {
                    cpf = aluno.getCpf().toString();
                }
                if (aluno.getAnamnese() != null) {
                    anamnese = String.valueOf(aluno.getAnamnese().getId());
                }
            }
            if (usuario.isPersonal() && usuario.getPersonal() != null) {
                Personal personal = usuario.getPersonal();
                if (personal.getCref() != null) {
                    cref = personal.getCref();
                }
            }
            return new ResUsuarioWithRolesResponseDTO(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.isAtivo(),
                    usuario.getRoles().stream().map(Enum::name).toList(),
                    usuario.getCaminhoFoto(),
                    usuario.getDataNascimento(),
                    cpf,
                    anamnese,
                    cref
            );
        });
    }

    public boolean existsByEmail(String email){
        return usuarioService.emailExiste(email);
    }

    @Transactional
    public void createAdminUser(ReqCadastroAdminDTO dto) {
        Admin admin = userDetailsService.getCurrentAdmin();
        usuarioService.validarEmailExistente(dto.email());

        Usuario adminCriado = new Usuario();
        adminCriado.setNome(dto.nome());
        adminCriado.setSexo(dto.sexo());
        adminCriado.setRoles(Set.of(Role.ADMIN));
        adminCriado.setDataNascimento(dto.dataNascimento());
        adminCriado.setEmail(dto.email());
        adminCriado.setTelefones(List.of(new Telefone(null, dto.telefone().pais(), dto.telefone().ddd(), dto.telefone().numero(), adminCriado)));

        String randomSenha = PasswordGenerator.generate(10);
        usuarioService.aplicarSenhaCriptografada(adminCriado, randomSenha);

        adminCriado = usuarioService.salvarUsuario(adminCriado);
        adminCriado = createProfile(adminCriado).getUsuario();

        String corpoEmail = String.format(
                "Olá %s,<br>Seu perfil de Admin foi criado com sucesso!<br><br>Sua senha temporária é: <strong>%s</strong>",
                adminCriado.getNome(),
                randomSenha
        );

        emailService.enviarEmail(new Email(
                adminCriado.getEmail(),
                "Bem-vindo ao sistema de academia",
                corpoEmail
        ));

        log.info("O Admin de id {} criou um novo admin de id {}", admin.getId(), adminCriado.getId());
    }

    @Transactional
    public void createInitialAdminUser(String email, String password) {
        if(usuarioService.emailExiste(email)){
            log.info("Admin inicial já existe, pulando criação.");
            return;
        }
        Usuario admin = new Usuario();
        usuarioService.aplicarSenhaCriptografada(admin, password);
        admin.setEmail(email);
        admin.setNome("ADMIN");
        admin.setSexo("M");
        admin.setRoles(Set.of(Role.ADMIN, Role.PERSONAL, Role.DONO));
        admin.setDataNascimento(LocalDate.parse("1990-01-01"));
        admin.setTelefones(List.of(new Telefone(null, "55", "11", "999999999", admin)));

        admin = usuarioService.salvarUsuario(admin);
        admin = createProfile(admin).getUsuario();
        personalService.createProfile(admin, "CREF12345");
        log.info("Usuario admin inicial criado na inicialização.");
    }
}

