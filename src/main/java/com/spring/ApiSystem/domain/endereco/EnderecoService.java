package com.spring.ApiSystem.domain.endereco;

import com.spring.ApiSystem.domain.cep.CepRepository;
import com.spring.ApiSystem.domain.cep.dto.response.DadosCepDTO;

import com.spring.ApiSystem.domain.cep.exception.CepNaoEncontradoException;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqAtualizarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.domain.endereco.dto.response.*;
import com.spring.ApiSystem.domain.usuario.UsuarioService;
import com.spring.ApiSystem.domain.endereco.exception.EnderecoNaoExistePorId;
import com.spring.ApiSystem.domain.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.domain.cep.CEP;
import com.spring.ApiSystem.domain.usuario.Usuario;
import com.spring.ApiSystem.domain.cep.ViaCepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final UsuarioService usuarioService;
    private final EnderecoMapper enderecoMapper;
    private final ViaCepService viaCepService;
    private final CepRepository cepRepository;

    public EnderecoService(EnderecoRepository enderecoRepository, UsuarioService usuarioService, EnderecoMapper enderecoMapper, ViaCepService viaCepService,
                           CepRepository cepRepository) {
        this.enderecoRepository = enderecoRepository;
        this.usuarioService = usuarioService;
        this.enderecoMapper = enderecoMapper;
        this.viaCepService = viaCepService;
        this.cepRepository = cepRepository;
    }

    @Transactional
    public ResCadastrarEnderecoDTO cadastrarEndereco(ReqCadastrarEnderecoDTO enderecoDTO, String email) {
           Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);

            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep().id());
            if (cep == null){
                throw new CepNaoEncontradoException();
            }

            Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
            endereco.setCep(cep);

            Optional<Endereco> enderecoExistente = consultarEnderecoExistente(endereco);
            if (enderecoExistente.isPresent()) {
                return enderecoMapper.toResCadastrarEnderecoDTO(enderecoExistente.get());
            }

            endereco.setUsuario(usuarioEncontrado);
            endereco.setDataCriacao(LocalDateTime.now());

            enderecoRepository.save(endereco);
            return enderecoMapper.toResCadastrarEnderecoDTO(endereco);
    }

    @Transactional
    public ResAtualizarEnderecoDTO atualizarEndereco(Long id, ReqAtualizarEnderecoDTO enderecoDTO, String email) {
           Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);
           Endereco enderecoEncontrado = buscarEnderecoPorIdEUsuario(id, usuarioEncontrado);

            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep().id());
            if (cep == null) {
                throw new CepNaoEncontradoException();
            }

            enderecoMapper.partialUpdate(enderecoDTO, enderecoEncontrado);
            enderecoEncontrado.setDataAtualizacao(LocalDateTime.now());
            enderecoEncontrado.setCep(cepRepository.getReferenceById(cep.getId()));

            enderecoRepository.save(enderecoEncontrado);
            return enderecoMapper.toResAtualizarEnderecoDTO(enderecoEncontrado);

    }

    @Transactional
    public Boolean removerEndereco(Long id, String email) {
            Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);
            Endereco enderecoEncontrado = buscarEnderecoPorIdEUsuario(id, usuarioEncontrado);
            enderecoRepository.delete(enderecoEncontrado);
            return true;
    }

    @Transactional(readOnly = true)
    public List<ResListarEnderecoDTO> listarEnderecos(String email) {
        Usuario usuarioEncontrado = usuarioService.buscarUsuarioPorEmail(email);
        List<Endereco> enderecos = enderecoRepository
                .findByUsuarioId(usuarioEncontrado.getId());

        return enderecoMapper.toResListarEnderecosDTO(enderecos);
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
                ),
                endereco.getDataCriacao(),
                endereco.getDataAtualizacao()
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
                .findByCepIdAndNumeroAndComplementoAndUnidadeAndTipo(
                        endereco.getCep().getId(),
                        endereco.getNumero(),
                        endereco.getComplemento(),
                        endereco.getUnidade(),
                        endereco.getTipo()
                );
    }
    private Endereco buscarEnderecoPorIdEUsuario(Long id, Usuario usuario) {
        return enderecoRepository
                .findByIdAndUsuario(id, usuario)
                .orElseThrow(EnderecoNaoExistePorId::new);
    }
}
