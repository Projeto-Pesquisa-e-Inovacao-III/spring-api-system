package com.spring.ApiSystem.endereco;

import com.spring.ApiSystem.cep.dto.response.DadosCepDTO;

import com.spring.ApiSystem.endereco.dto.request.ReqAtualizarEnderecoDTO;
import com.spring.ApiSystem.endereco.dto.request.ReqCadastrarEnderecoDTO;
import com.spring.ApiSystem.endereco.dto.response.*;
import com.spring.ApiSystem.usuario.exception.EnderecoNaoExistePorId;
import com.spring.ApiSystem.endereco.mapper.EnderecoMapper;
import com.spring.ApiSystem.cep.CEP;
import com.spring.ApiSystem.usuario.Usuario;
import com.spring.ApiSystem.usuario.UserRepository;
import com.spring.ApiSystem.cep.ViaCepService;
import com.spring.ApiSystem.usuario.exception.UsuarioNaoEncontradoException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final UserRepository userRepository;
    private final EnderecoMapper enderecoMapper;
    private final ViaCepService viaCepService;

    public EnderecoService(EnderecoRepository enderecoRepository,
                           EnderecoMapper enderecoMapper,
                           UserRepository userRepository,
                           ViaCepService viaCepService) {
        this.enderecoRepository = enderecoRepository;
        this.userRepository = userRepository;
        this.enderecoMapper = enderecoMapper;
        this.viaCepService = viaCepService;
    }

    public ResCadastrarEnderecoDTO cadastrarEndereco(ReqCadastrarEnderecoDTO enderecoDTO, String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {
            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep().id());
            if (cep != null) {
                Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
                endereco.setUsuario(usuarioEncontrado.get());
                endereco.setDataCriacao(LocalDateTime.now());
                endereco.setCep(cep);
                enderecoRepository.save(endereco);
                return enderecoMapper.toResCadastrarEnderecoDTO(endereco);
            }
        }
        throw  new UsuarioNaoEncontradoException();
    }


    public ResAtualizarEnderecoDTO atualizarEndereco(Long id, ReqAtualizarEnderecoDTO enderecoDTO, String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);
        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByIdAndUsuario(id, usuarioEncontrado.get());

        if(enderecoEncontrado.isPresent()){
            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep().id());
            if(cep != null){
                enderecoMapper.partialUpdate(enderecoDTO, enderecoEncontrado.get());
                enderecoEncontrado.get().setDataAtualizacao(LocalDateTime.now());
                enderecoEncontrado.get().setCep(cep);
                enderecoRepository.save(enderecoEncontrado.get());
                return enderecoMapper.toResAtualizarEnderecoDTO(enderecoEncontrado.get());
            }
        }

        throw new UsuarioNaoEncontradoException();
    }


    public Boolean removerEndereco(Long id, String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);
        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByIdAndUsuario(id, usuarioEncontrado.get());

        if (enderecoEncontrado.isPresent()) {
            enderecoRepository.deleteById(id);
            return true;
        }

        return false;
    }

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

    public List<ResListarEnderecoDTO> listarEnderecos(String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {
            List<Endereco> enderecos = enderecoRepository
                    .findByUsuarioId(usuarioEncontrado.get().getId());
            return enderecoMapper.toResListarEnderecosDTO(enderecos);

        }

        throw new UsuarioNaoEncontradoException();
    }

    public Endereco buscarPorId(Long id) {
        return enderecoRepository
                .findById(id)
                .orElseThrow(EnderecoNaoExistePorId::new);
    }


}
