package com.spring.ApiSystem.domain.admin;

import com.spring.ApiSystem.domain.admin.dto.request.ReqAdicionarRoleDTO;
import com.spring.ApiSystem.domain.admin.dto.request.ReqCadastroPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResCadastrarPersonalDTO;
import com.spring.ApiSystem.domain.admin.dto.response.ResRoleNeedDataDTO;
import com.spring.ApiSystem.domain.admin.exception.AdminNaoExisteException;
import com.spring.ApiSystem.domain.aluno.AlunoService;
import com.spring.ApiSystem.domain.aluno.mapper.CpfMapper;
import com.spring.ApiSystem.domain.personal.PersonalService;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.usuario.enums.Role;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UsuarioService usuarioService;
    private final PersonalService personalService;
    private final JpaUserDetailsService userDetailsService;
    private final AlunoService alunoService;
    private final CpfMapper cpfMapper;

    public AdminService(AdminRepository adminRepository, UsuarioService usuarioService, PersonalService personalService, JpaUserDetailsService userDetailsService, AlunoService alunoService, CpfMapper cpfMapper) {
        this.adminRepository = adminRepository;
        this.usuarioService = usuarioService;
        this.personalService = personalService;
        this.userDetailsService = userDetailsService;
        this.alunoService = alunoService;
        this.cpfMapper = cpfMapper;
    }

    @Transactional
    public Usuario adicionarRole(ReqAdicionarRoleDTO dto){
        Long userId = dto.userId();
        Role role = dto.role();
        if(role.equals(Role.DONO)) throw new IllegalArgumentException("Não é permitido adicionar a role DONO a um usuário.");
        Admin admin = userDetailsService.getCurrentAdmin();

        Usuario usuario = usuarioService.buscarUsuarioPorId(userId);

        if(usuario.isRole(role)){
            throw new IllegalArgumentException("O usuário já possui a role: " + role);
        }

        usuarioService.addRoleToUsuario(usuario, role);

        if(hasProfileFromThisRole(userId, role)){
            switch (role) {
                case ALUNO -> alunoService.enableProfile(userId);
                case PERSONAL -> personalService.enableProfile(userId);
                case ADMIN -> enableProfile(userId);
            }
            return usuario;
        }

        // criar profile se não tiver um existente

        switch (role) {
            case ALUNO -> alunoService.createProfile(usuario, cpfMapper.toCpf(dto.cpf()));
            case PERSONAL -> personalService.createProfile(usuario, dto.cref());
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
        usuarioService.salvarUsuario(usuario);
    }

    @Transactional
    public Usuario retirarRole(Long userId, Role role){
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

    public ResCadastrarPersonalDTO criarPersonal(ReqCadastroPersonalDTO personalDTO){
        Admin admin = userDetailsService.getCurrentAdmin();

        return personalService.cadastrarPersonalDto(personalDTO);
    }

    //public List<Usuario> listarUsuariosPaginado(Pageable pageable){
    //    userDetailsService.getCurrentAdmin();

     //   return usuarioService.findAllUsersPagedWithRoles(pageable);
    //}


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
}
