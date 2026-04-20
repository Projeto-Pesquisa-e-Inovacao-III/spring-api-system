package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.admin.dto.request.ReqAdicionarRoleDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResRoleNeedDataDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResUsuarioWithRolesResponseDTO;
import com.spring.ApiSystem.domain.admin.exception.AdminNaoExisteException;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.mapper.CpfMapper;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import com.spring.ApiSystem.shared.config.filter.FilterService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
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

    public AdminService(AdminRepository adminRepository, UsuarioService usuarioService, PersonalService personalService, JpaUserDetailsService userDetailsService, AlunoService alunoService, CpfMapper cpfMapper, FilterService filterService) {
        this.adminRepository = adminRepository;
        this.usuarioService = usuarioService;
        this.personalService = personalService;
        this.userDetailsService = userDetailsService;
        this.alunoService = alunoService;
        this.cpfMapper = cpfMapper;
    }

    @Transactional
    public Usuario addRoleToUser(Role role, Long userId, ReqAdicionarRoleDTO dto){
        if(role.equals(Role.DONO)) throw new IllegalArgumentException("Não é permitido adicionar a role DONO a um usuário.");
        Admin admin = userDetailsService.getCurrentAdmin();

        Usuario usuario = usuarioService.buscarUsuarioPorId(userId);

        if(usuario.isRole(role)){
            throw new IllegalArgumentException("O usuário já possui a role: " + role);
        }

        log.info("O Usuario id {} está adicionando role {} ao usuário de id {}", admin.getId(), role, userId);

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
        usuario.setRoles(new HashSet<>(List.of(Role.DELETADO)));
        usuarioService.salvarUsuario(usuario);
    }

    @Transactional
    public Usuario retirarRole(Role role, Long userId){
        Admin admin = userDetailsService.getCurrentAdmin();

        Usuario usuario = usuarioService.buscarUsuarioPorId(userId);

        if(usuario.isDono()){
            throw new IllegalArgumentException("Não é permitido remover um usuário com a role DONO.");
        }

        if(isTargetSelfAdmin(usuario, admin)){
            throw new IllegalArgumentException("Um administrador não pode se auto-remover.");
        }

        usuario = usuarioService.removeRoleFromUsuario(usuario, role);

        return usuario;
    }

    @Transactional
    public ResCadastrarPersonalDTO criarPersonal(ReqCadastroPersonalDTO personalDTO){
        Admin admin = userDetailsService.getCurrentAdmin();

        return personalService.cadastrarPersonalDto(personalDTO);
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
    public Page<ResUsuarioWithRolesResponseDTO> listarUsuariosComFiltros(String nome, String email, Role role, Pageable pageable) {
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
                var aluno = usuario.getAluno();
                if (aluno.getCpf() != null) {
                    cpf = aluno.getCpf().toString();
                }
                if (aluno.getAnamnese() != null) {
                    anamnese = String.valueOf(aluno.getAnamnese().getId());
                }
            }
            if (usuario.isPersonal() && usuario.getPersonal() != null) {
                var personal = usuario.getPersonal();
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

}
