package com.spring.ApiSystem.domain.endereco;

import com.spring.ApiSystem.domain.agendamento.Agendamento;
import com.spring.ApiSystem.domain.agendamento.AgendamentoRepository;
import com.spring.ApiSystem.domain.agendamento.enums.AgendamentoStatus;
import com.spring.ApiSystem.domain.cep.CepRepository;
import com.spring.ApiSystem.domain.cep.dto.response.DadosCepDTO;

import com.spring.ApiSystem.domain.cep.exception.CepNaoEncontradoException;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqAtualizarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.response.*;
import com.spring.ApiSystem.domain.endereco.exception.EnderecoAlreadyExistsException;
import com.spring.ApiSystem.domain.endereco.exception.EnderecoOutOfLimitException;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.endereco.exception.EnderecoNaoExistePorId;
import com.spring.ApiSystem.domain.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.domain.cep.CEP;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.cep.ViaCepService;
import com.spring.ApiSystem.domain.usuario.security.JpaUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioService usuarioService;
    private final EnderecoMapper enderecoMapper;
    private final ViaCepService viaCepService;
    private final CepRepository cepRepository;
    private final JpaUserDetailsService jpaUserDetailsService;

    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioService usuarioService, EnderecoMapper enderecoMapper, ViaCepService viaCepService,
                           CepRepository cepRepository, JpaUserDetailsService jpaUserDetailsService, AgendamentoRepository agendamentoRepository) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
        this.enderecoMapper = enderecoMapper;
        this.viaCepService = viaCepService;
        this.cepRepository = cepRepository;
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.agendamentoRepository = agendamentoRepository;
    }

    private final Integer LIMIT_ENDERECO = 6;

    public void checkLimit(){
        if(enderecoRepository.countByUsuarioIdAndAtivo(jpaUserDetailsService.getCurrentUser().getId(), true) >= LIMIT_ENDERECO){
            throw new EnderecoOutOfLimitException(LIMIT_ENDERECO);
        }
    }

    @Transactional
    public ResCadastrarEnderecoDTO cadastrarEndereco(ReqCadastrarEnderecoDTO enderecoDTO, String email, boolean bloquearCadastroExistente) {
            checkLimit();
            Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);

            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep());
            if (cep == null){
                throw new CepNaoEncontradoException();
            }

            Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
            endereco.setCep(cep);

            Optional<Endereco> enderecoExistente = consultarEnderecoExistente(endereco);
            if(bloquearCadastroExistente && enderecoExistente.isPresent()) {
                throw new EnderecoAlreadyExistsException();
            }

            if(enderecoExistente.isPresent()){
                return enderecoMapper.toResCadastrarEnderecoDTO(enderecoExistente.get());
            }

            endereco.setUsuario(usuarioEncontrado);

            enderecoRepository.save(endereco);
            return enderecoMapper.toResCadastrarEnderecoDTO(endereco);
    }

    @Transactional
    public ResAtualizarEnderecoDTO atualizarEndereco(Long id, ReqAtualizarEnderecoDTO enderecoDTO, String email) {
        Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);
        Endereco enderecoAntigo = buscarEnderecoPorIdEUsuario(id, usuarioEncontrado);

        CEP cep = viaCepService.procurarCEP(enderecoDTO.cep());
        if (cep == null) {
            throw new CepNaoEncontradoException();
        }

        enderecoAntigo.setAtivo(false);
        enderecoRepository.save(enderecoAntigo);

        Endereco enderecoNovo = enderecoMapper.toEntity(enderecoDTO);
        enderecoNovo.setUsuario(usuarioEncontrado);
        enderecoNovo.setAtivo(true);
        enderecoNovo.setCep(cepRepository.getReferenceById(cep.getId()));
        enderecoNovo = enderecoRepository.save(enderecoNovo);

        return enderecoMapper.toResAtualizarEnderecoDTO(enderecoNovo);
    }

    @Transactional
    public void removerEndereco(Long id, String email) {
        Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);
        Endereco enderecoEncontrado = buscarEnderecoPorIdEUsuario(id, usuarioEncontrado);
        enderecoEncontrado.setAtivo(false);
        enderecoRepository.save(enderecoEncontrado);
    }

    @Transactional(readOnly = true)
    public List<ResListarEnderecoDTO> listarEnderecos() {
        Usuario usuario = jpaUserDetailsService.getCurrentUser();
        List<Endereco> enderecos = enderecoRepository
                .findByUsuarioAndAtivo(usuario, true);

        return enderecoMapper.toResListarEnderecosDTO(enderecos);
    }

    @Transactional(readOnly = true)
    public List<ResListarEnderecoPorDataDeCriacaoDTO> listarEnderecosPorDataDeCriaCao(String email) {
        Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);
        List<Endereco> enderecos = enderecoRepository
                .findByUsuarioId(usuarioEncontrado.getId());

        return enderecoMapper.toResListarEnderecosPorDataDeCriacaoDTO(enderecos);
    }

    @Transactional(readOnly = true)
    public ResBuscarEnderecoPorIdDTO buscarPorIdDto(Long id) {
        Endereco endereco = buscarPorId(id);

        return new ResBuscarEnderecoPorIdDTO(
                endereco.getId(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getUnidade(),
                endereco.getTipo(),
                new DadosCepDTO(
                        endereco.getCep().getId(),
                        endereco.getCep().getLogradouro(),
                        endereco.getCep().getBairro(),
                        endereco.getCep().getLocalidade(),
                        endereco.getCep().getUf()
                )
        );
    }

    @Transactional(readOnly = true)
    public Endereco buscarPorId(Long id) {
        return enderecoRepository
                .findById(id)
                .orElseThrow(EnderecoNaoExistePorId::new);
    }

    private Optional<Endereco> consultarEnderecoExistente(Endereco endereco) {

        return enderecoRepository
                .findByCepIdAndNumeroAndComplementoAndAtivo(
                        endereco.getCep().getId(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        true
                );
    }
    private Endereco buscarEnderecoPorIdEUsuario(Long id, Usuario usuario) {
        return enderecoRepository
                .findByIdAndUsuario(id, usuario)
                .orElseThrow(EnderecoNaoExistePorId::new);
    }
}
