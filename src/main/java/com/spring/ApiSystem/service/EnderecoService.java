package com.spring.ApiSystem.service;

import com.spring.ApiSystem.dto.cep.response.DadosCepDTO;
import com.spring.ApiSystem.dto.endereco.request.EnderecoDTO;
import com.spring.ApiSystem.dto.endereco.response.BuscarEnderecoPorIdDTO;
import com.spring.ApiSystem.dto.endereco.response.ResEnderecoDTO;
import com.spring.ApiSystem.exception.EnderecoNaoExistePorId;
import com.spring.ApiSystem.mapper.EnderecoMapper;
import com.spring.ApiSystem.model.CEP;
import com.spring.ApiSystem.model.Endereco;
import com.spring.ApiSystem.model.Usuario;
import com.spring.ApiSystem.repository.EnderecoRepository;
import com.spring.ApiSystem.repository.UserRepository;
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

    public ResEnderecoDTO cadastrarEndereco(EnderecoDTO enderecoDTO, String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {
            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep());
            if (cep != null) {
                Endereco endereco = enderecoMapper.toEntity(enderecoDTO);
                endereco.setUsuario(usuarioEncontrado.get());
                endereco.setData_criacao(LocalDateTime.now());
                endereco.setCep(cep);
                enderecoRepository.save(endereco);
                return enderecoMapper.toResEnderecoDTO(endereco);
            }
        }

        return null;
    }

    public List<ResEnderecoDTO> listarEnderecos(String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);

        if (usuarioEncontrado.isPresent()) {
            return enderecoRepository.findByUsuario(usuarioEncontrado.get().getId());
        }

        return null;
    }

    public ResEnderecoDTO atualizarEndereco(Long id, EnderecoDTO enderecoDTO, String email) {
        Optional<Usuario> usuarioEncontrado = userRepository.findByEmail(email);
        Optional<Endereco> enderecoEncontrado = enderecoRepository.findByIdAndUsuario(id, usuarioEncontrado.get());

        if (enderecoEncontrado.isPresent()) {
            CEP cep = viaCepService.procurarCEP(enderecoDTO.cep());
            if (cep != null) {
                enderecoMapper.atualizarEnderecoFromDto(enderecoDTO, enderecoEncontrado.get());
                enderecoEncontrado.get().setData_atualizacao(LocalDateTime.now());
                enderecoEncontrado.get().setCep(cep);
                enderecoRepository.save(enderecoEncontrado.get());
                return enderecoMapper.toResEnderecoDTO(enderecoEncontrado.get());
            }
        }

        return null;
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

    public BuscarEnderecoPorIdDTO buscarPorId(Long id) {
        Endereco endereco = findById(id);
        return new BuscarEnderecoPorIdDTO(
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
                endereco.getData_criacao(),
                endereco.getData_atualizacao()
        );
    }

    public Endereco findById(Long id) {
        return enderecoRepository
                .findById(id)
                .orElseThrow(EnderecoNaoExistePorId::new);
    }
}


